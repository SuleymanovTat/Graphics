package ru.suleymanovtat.graphics.presentation.graphics

import android.app.Application
import android.util.Base64
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.carloscar.themoviedb.data.network.util.NetworkUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.suleymanovtat.dressingbabyweather.repository.PointsRepository
import ru.suleymanovtat.graphics.R
import ru.suleymanovtat.graphics.model.PointsLocal
import ru.suleymanovtat.graphics.model.WrapperPoint


class GraphicsViewModel(application: Application) : AndroidViewModel(application) {

    val pointsLocal = MutableLiveData<PointsLocal>()

    fun getPoints(count: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = PointsRepository().getPoints(count)
            when (response) {
                is NetworkUtil.ResultWrapper.NetworkError -> postValue(R.string.error_network)
                is NetworkUtil.ResultWrapper.Success -> {
                    val value = response.value;
                    if (value?.result == 0) {
                        postValue(value)
                    } else {
                        if (value?.result == null) {
                            val mResponse = value?.response
                            if (mResponse?.result == -100) {
                                postValue(R.string.invalid_request)
                            } else {
                                postValue(mResponse?.message)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun postValue(value: WrapperPoint) {
        val sortPoints = value.response?.points?.sortedBy { point -> point.x }
        pointsLocal.postValue(PointsLocal(1, points = sortPoints))
    }

    fun postValue(resource: Int) {
        pointsLocal.postValue(PointsLocal(0, getString(resource)))
    }

    fun postValue(message: String?) {
        pointsLocal.postValue(PointsLocal(0, decode(message)))
    }

    fun getContext() = getApplication<Application>()
    fun getString(@StringRes id: Int): String = getContext().getString(id)

    private fun decode(response: String?) =
        try {
            val data = Base64.decode(response, Base64.DEFAULT)
            String(data, Charsets.UTF_8)
        } catch (e: Exception) {
            getString(R.string.error)
        }
}
