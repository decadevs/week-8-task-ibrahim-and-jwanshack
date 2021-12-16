package com.olamachia.maptrackerweekeighttask

import android.Manifest
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.olamachia.maptrackerweekeighttask.databinding.ActivityTrackerBinding
import com.olamachia.maptrackerweekeighttask.models.LocationModel

class TrackerActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityTrackerBinding
    private lateinit var locationManager: LocationManager
    private val refreshDistance = 2F
    private val refreshTime = 6000L
    private lateinit var shakCoordinates: LatLng
    private lateinit var ahmedCoordinates: LatLng

    // request permission for location
    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            Toast.makeText(this, "Allow me access to your Location!", Toast.LENGTH_SHORT).show()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTrackerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkPermission()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        locationManager= getSystemService(LOCATION_SERVICE) as LocationManager

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,refreshTime,refreshDistance
        ) { location ->
            Repository.FirebaseManipulation.addData(LocationModel("Shak", location.latitude, location.longitude
                )
            )

            val database = FirebaseDatabase.getInstance()
            val reference = database.getReference("Users")

            reference.addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        val ahmedModel = snapshot.child("Ahmed").getValue(LocationModel::class.java)
                        Log.e(
                            "Location 1",
                            "Name:${ahmedModel?.id!!} Longitude:${ahmedModel.longitude} Latitude:${ahmedModel.latitude}"
                        )
                        ahmedCoordinates = LatLng(ahmedModel.latitude!!, ahmedModel.longitude!!)

                        val shakModel = snapshot.child("Shak").getValue(LocationModel::class.java)
                        Log.e(
                            "Location 2",
                            "Name:${shakModel?.id!!} Longitude:${shakModel.longitude} Latitude:${shakModel.latitude}"
                        )
                        shakCoordinates = LatLng(shakModel.latitude!!, shakModel.longitude!!)

                        mMap.clear()
                        mMap.addMarker(MarkerOptions().position(ahmedCoordinates).title("Ahmed"))
                        mMap.addMarker(
                            MarkerOptions().position(shakCoordinates)
                                .title("Shak")
                        )

                        mMap.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                ahmedCoordinates,
                                16.0f
                            )
                        )

                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("cancel", error.toString())
                    }

                })
        }
    }

    private fun checkPermission() {

        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        when {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {

            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                resultLauncher.launch(permission)
            }

            else -> {

                Toast.makeText(
                    this,
                    "Location permission is needed",
                    Toast.LENGTH_SHORT
                ).show()
                resultLauncher.launch(permission)

            }
        }
    }

}


