package ru.suleymanovtat.graphics.presentation.graphics

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.os.Bundle
import android.os.Environment
import android.text.format.DateFormat
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlinx.android.synthetic.main.graphics_fragment.*
import ru.suleymanovtat.graphics.R
import java.io.File
import java.io.FileOutputStream
import java.util.*
import kotlin.collections.ArrayList


class GraphicsFragment : Fragment(R.layout.graphics_fragment) {

    companion object {
        fun newInstance() = GraphicsFragment()
    }

    private lateinit var viewModel: GraphicsViewModel

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_screenshot) {
            askPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) {
                val bitmap = getBitmap()
                if (bitmap != null) {
                    saveFile(bitmap)
                    showToast(R.string.schedule_saved)
                }
            }.onDeclined { e ->
                showToast(R.string.permission_denied)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true);
        super.onViewCreated(view, savedInstanceState)
        mChart.setNoDataText("")
        buttonGo.setOnClickListener {
            editTextCountPoint.hideKeyboard()
            val test: String = editTextCountPoint.text.toString().trim()
            if (test.isEmpty()) {
                showToast(R.string.insert_the_number)
            } else {
                progressBar.visibility = View.VISIBLE
                viewModel.getPoints(test.toInt())
                mChart.visibility = View.GONE
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(GraphicsViewModel::class.java)
        viewModel.pointsLocal.observe(this, Observer { pointsLocal ->
            when (pointsLocal.status) {
                0 -> {
                    textViewMessage.text = pointsLocal.message
                    textViewMessage.visibility = View.VISIBLE
                    group.visibility = View.GONE
                }
                1 -> {
                    if (pointsLocal.points != null) {
                        val pointsAdapter = PointTableAdapter(pointsLocal.points!!)
                        recyclerViewPoints.adapter = pointsAdapter
                        setData(pointsLocal.points!!.map { Entry(it.x!!, it.y!!) })
                        textViewMessage.visibility = View.GONE
                        group.visibility = View.VISIBLE
                    }
                }
            }
            progressBar.visibility = View.GONE
        })
    }

    private fun setData(values: List<Entry>) {
        mChart.setTouchEnabled(true)
        mChart.setPinchZoom(true)
        val pointMarkerView = PointMarkerView(activity, R.layout.custom_marker_view)
        pointMarkerView.chartView = mChart
        mChart.marker = pointMarkerView
        val lineDataSet: LineDataSet
        if (mChart.data != null && mChart.data.dataSetCount > 0) {
            lineDataSet = mChart.data.getDataSetByIndex(0) as LineDataSet
            lineDataSet.values = values
            mChart.data.notifyDataChanged()
            mChart.notifyDataSetChanged()
        } else {
            lineDataSet = LineDataSet(values, getString(R.string.line))
            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            lineDataSet.setDrawIcons(false)
            lineDataSet.enableDashedLine(10f, 5f, 0f)
            lineDataSet.enableDashedHighlightLine(10f, 5f, 0f)
            lineDataSet.color = Color.BLUE
            lineDataSet.setCircleColor(Color.DKGRAY)
            lineDataSet.lineWidth = 1f
            lineDataSet.circleRadius = 3f
            lineDataSet.setDrawCircleHole(false)
            lineDataSet.valueTextSize = 9f
            lineDataSet.setDrawFilled(true)
            lineDataSet.formLineWidth = 1f
            lineDataSet.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
            lineDataSet.formSize = 15f
            val drawable =
                ContextCompat.getDrawable(activity!!, R.drawable.fade_blue)
            lineDataSet.fillDrawable = drawable
            val dataSets: ArrayList<ILineDataSet> = ArrayList()
            dataSets.add(lineDataSet)
            val data = LineData(dataSets)
            mChart.data = data
            mChart.invalidate()
        }
        mChart.visibility = View.VISIBLE
        mChart.invalidate()
    }

    private fun getBitmap(): Bitmap? {
        val view = activity!!.window.decorView.rootView
        val bitmap = Bitmap.createBitmap(
            fragment.measuredWidth, fragment.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        val theme = activity!!.theme
        val ta = theme
            .obtainStyledAttributes(intArrayOf(android.R.attr.windowBackground))
        val res = ta.getResourceId(0, 0)
        val background =
            activity!!.resources.getDrawable(res)
        background.draw(canvas)
        view.draw(canvas)
        return bitmap
    }

    private fun saveFile(bitmap: Bitmap) {
        val now = Date()
        DateFormat.format("yyyy-MM-dd_hh:mm:ss", now)
        val mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpeg"
        val imageFile = File(mPath)
        val outputStream = FileOutputStream(imageFile)
        val quality = 100
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        outputStream.flush()
        outputStream.close()
    }

    fun showToast(string: Int) {
        Toast.makeText(activity, string, Toast.LENGTH_LONG).show()
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}
