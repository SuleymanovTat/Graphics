package ru.suleymanovtat.dressingbabyweather.repository

import com.carloscar.themoviedb.data.network.util.NetworkUtil
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import ru.suleymanovtat.graphics.model.WrapperPoint
import ru.suleymanovtat.graphics.repository.network.ApiService
import ru.suleymanovtat.graphics.repository.network.RetrofitClient

class PointsRepository() {

    private val service: ApiService? = RetrofitClient.createService(ApiService::class.java)
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    suspend fun getPoints(count: Int): NetworkUtil.ResultWrapper<WrapperPoint?> {
        return NetworkUtil().safeApiCall(dispatcher) {
            service?.getPoints("", count)?.body()
        }
    }
}