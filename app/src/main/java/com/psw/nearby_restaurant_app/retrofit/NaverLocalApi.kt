package com.psw.nearby_restaurant_app.retrofit

import com.psw.nearby_restaurant_app.retrofit.model.NaverLocalResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NaverLocalApi {

    @GET("v1/search/local.json")
    suspend fun searchRestaurants(
        @Header("X-Naver-Client-Id") clientId: String,
        @Header("X-Naver-Client-Secret") clientSecret: String,
        @Query("query") query: String,
        @Query("display") display: Int = 20,
        @Query("start") start: Int = 1,
        @Query("sort") sort: String = "distance",
        @Query("coordinate") coordinate: String? = null
    ): NaverLocalResponse
}