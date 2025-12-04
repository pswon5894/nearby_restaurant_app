package com.psw.nearby_restaurant_app

import android.app.Activity
import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.LocationServices
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.psw.nearby_restaurant_app.data.Restaurant
import com.psw.nearby_restaurant_app.databinding.ActivityMapBinding
import com.psw.nearby_restaurant_app.retrofit.model.LocalItem

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var binding: ActivityMapBinding
    private var mapFragment: MapFragment? = null
    private var naverMapInstance: NaverMap? = null
    private var currentMarker: Marker? = null
    private lateinit var locationManager: LocationManager
    private val PERMISSION_REQUEST_CODE = 1000
    private val restaurants = mutableListOf<Restaurant>()

    private lateinit var adapter: RestaurantAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMapBinding.inflate(layoutInflater)

        binding.rvRestaurants.layoutManager = LinearLayoutManager(this)
        adapter = RestaurantAdapter(restaurants)
        binding.rvRestaurants.adapter = adapter

        setContentView(binding.root)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // MapFragment 생성 또는 불러오기
        val fm = supportFragmentManager
        var mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map, it).commit()
            }

        // 지도 로딩
        mapFragment.getMapAsync(this)
    }

    @UiThread
    override fun onMapReady(naverMap: NaverMap) {
        // 지도 설정하기
        naverMap.uiSettings.isLocationButtonEnabled = true

        showRestaurantsOnMap(naverMap)

        locationFabClickListener(naverMap)

        // 위치 권한 체크 후 Follow 모드 적용
        enableLocationTracking(naverMap)
    }

    private fun showRestaurantsOnMap(naverMap: NaverMap) {
        // 기존 마커 제거를 원한다면 restaurantsMarkers 리스트로 관리해도 됨

        for (restaurant in restaurants) {
            val marker = Marker()
            marker.position = restaurant.latLng
            marker.iconTintColor = Color.BLUE   // 식당은 파란색 마커
            marker.map = naverMap

            val info = InfoWindow()
            info.adapter = object : InfoWindow.DefaultTextAdapter(this) {
                override fun getText(p0: InfoWindow): CharSequence {
                    return restaurant.name ?: "이름 없음"
                }
            }

            marker.setOnClickListener {
                info.open(marker)
                true
            }
        }
    }

    private fun locationFabClickListener(naverMap : NaverMap) {
        binding.fabCurrentLocation.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
            ) {
                startLocationUpdates(naverMap)
                Toast.makeText(this, "현재 위치로 이동함", Toast.LENGTH_SHORT).show()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    //위치 권한 체크 와 요청
    private fun enableLocationTracking(naverMap: NaverMap) {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
            ) {
            //권한 있음
            startLocationUpdates(naverMap)
//            naverMap.locationTrackingMode = LocationTrackingMode.Follow
        } else {
            //권한 없음
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun startLocationUpdates(naverMap: NaverMap) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationManager = LocationManager(fusedLocationClient) {latLng ->
            naverMap.moveCamera(CameraUpdate.scrollTo(latLng).animate(CameraAnimation.Fly))

            currentMarker?.map = null

            val marker = Marker()
            marker.position = latLng
            marker.map = naverMap
            marker.iconTintColor = Color.RED
            currentMarker = marker

            val infoWindow = InfoWindow()
            infoWindow.adapter = object : InfoWindow.DefaultTextAdapter(this@MapActivity) {
                override fun getText(infoWindow: InfoWindow): CharSequence {
                    return "현재 위치"
                }
            }

            infoWindow.open(marker)
        }

        locationManager.startLocationUpdate()

        naverMap.locationTrackingMode = com.naver.maps.map.LocationTrackingMode.NoFollow
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions,  grantResults)

        if(requestCode == PERMISSION_REQUEST_CODE) {
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mapFragment?.getMapAsync { map ->
                    startLocationUpdates(map)
                }
            } else {
                Toast.makeText(this, "위치 권한 필요", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        locationManager.stopLocationUpdates()
    }
}
