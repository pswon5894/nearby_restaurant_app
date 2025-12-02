package com.psw.nearby_restaurant_app

import android.app.Activity
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.location.LocationServices
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.psw.nearby_restaurant_app.databinding.ActivityMapBinding

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var binding: ActivityMapBinding
    private var mapFragment: MapFragment? = null
    private lateinit var locationManager: LocationManager
    private val PERMISSION_REQUEST_CODE = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMapBinding.inflate(layoutInflater)
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

    override fun onMapReady(naverMap: NaverMap) {
        // 지도 설정하기
        naverMap.uiSettings.isLocationButtonEnabled = true

        // 위치 권한 체크 후 Follow 모드 적용
        enableLocationTracking(naverMap)

//        naverMap.locationTrackingMode = LocationTrackingMode.Follow
    }

    //위치 권한 체크 와 요청
    private fun enableLocationTracking(naverMap: NaverMap) {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
            ) {
            //권한 있음
            naverMap.locationTrackingMode = LocationTrackingMode.Follow
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
            naverMap.moveCamera(CameraUpdate.scrollTo(latLng))
        }

        locationManager.startLocationUpdate()

        naverMap.locationTrackingMode = com.naver.maps.map.LocationTrackingMode.Follow
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
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