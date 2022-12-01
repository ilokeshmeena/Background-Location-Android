package com.lmmarketings.trackit.user

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.*
import com.lmmarketings.trackit.R
import java.util.*


class FrontEnd : AppCompatActivity() {
    lateinit var databaseReference: DatabaseReference
    var previousLatLng: LatLng? = null
    var currentLatLng: LatLng? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_front_end)
//        val latitudeUI=findViewById<TextView>(R.id.frontend_latitude)
//        val longitudeUI=findViewById<TextView>(R.id.frontend_longitude)
        val btnLocation=findViewById<Button>(R.id.frontend_track_location)
            btnLocation.visibility=View.GONE
        val latitude=findViewById<TextView>(R.id.frontend_longitude)
        val longitude=findViewById<TextView>(R.id.frontend_latitude)
        fetchUpdatedLocation()
        btnLocation.setOnClickListener {
            val strUri =
                "http://maps.google.com/maps?q=loc:" + latitude.text.toString().substringAfter(": ") + "," + longitude.text.toString().substringAfter(": " )
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(strUri))

            intent.setClassName(
                "com.google.android.apps.maps",
                "com.google.android.maps.MapsActivity"
            )

            startActivity(intent)
        }
    }
    private fun fetchUpdatedLocation(){
        databaseReference = FirebaseDatabase.getInstance().getReference("Location")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var latitude = 0.0
                var longitude = 0.0
                val data = dataSnapshot.childrenCount
                for (d in 0 until data) {
                    latitude = dataSnapshot.child("latitude").getValue(Double::class.java)!!.toDouble()
                    longitude = dataSnapshot.child("longitude").getValue(Double::class.java)!!.toDouble()
                }
                findViewById<TextView>(R.id.frontend_longitude).text="Latitude : $latitude"
                findViewById<TextView>(R.id.frontend_latitude).text="Longitude : $longitude"
                if(findViewById<Button>(R.id.frontend_track_location).visibility==View.GONE && latitude!=0.0 && longitude!=0.0){
                    findViewById<Button>(R.id.frontend_track_location).visibility=View.VISIBLE
                }
                currentLatLng = LatLng(latitude, longitude)
                if (previousLatLng == null || previousLatLng !== currentLatLng) {
                    // add marker line
                    previousLatLng = currentLatLng
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}