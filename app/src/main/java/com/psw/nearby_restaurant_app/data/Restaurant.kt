package com.psw.nearby_restaurant_app.data

import com.naver.maps.geometry.LatLng

data class Restaurant (
    val name: String?,
    val latLng: LatLng,
    val photoReference: String?,
    val address: String?,
    val types: List<String>? = null,  // 추가
    val foodType: String = "",               // 예: "일식", "햄버거"
    val rating: Double? = null,
    val website: String? = null //  홈페이지 URL 필드 추가
)