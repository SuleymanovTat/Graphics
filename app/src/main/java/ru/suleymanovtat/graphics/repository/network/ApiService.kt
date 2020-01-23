package ru.suleymanovtat.graphics.repository.network;

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import ru.suleymanovtat.graphics.model.WrapperPoint

interface ApiService {

    @POST("mobws/json/pointsList?version=1.1")
    suspend fun getPoints(@Body body: String, @Query("count") count: Int): Response<WrapperPoint>
}
