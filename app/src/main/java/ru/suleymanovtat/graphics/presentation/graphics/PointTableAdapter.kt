package ru.suleymanovtat.graphics.presentation.graphics

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_point.view.*
import ru.suleymanovtat.graphics.R
import ru.suleymanovtat.graphics.model.Point

class PointTableAdapter(
    var items: List<Point>
) : RecyclerView.Adapter<PointTableAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_point,
                parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        with(holder.mView) {
            tvX.text = String.format(tvX.context.getString(R.string.x), item.x)
            tvY.text = String.format(tvX.context.getString(R.string.y), item.y)
        }
    }

    class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView)
}