package com.psw.nearby_restaurant_app.retrofit.model

data class NaverLocalResponse (
    val total: Int,
    val display: Int,
    val items: List<LocalItem>
)

data class LocalItem(
    val title: String,
    val link: String,
    val category: String,
    val description: String,
    val telephone: String,
    val address: String,
    val roadAddress: String,
    val mapx: String,
    val mapy: String
)