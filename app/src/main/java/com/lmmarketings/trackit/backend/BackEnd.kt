package com.lmmarketings.trackit.backend

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lmmarketings.trackit.R

class BackEnd : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_back_end)
        startService(this)
    }
    private fun startService(context: Context) {
        val startServiceIntent = Intent(context, LocationService::class.java)

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            context.startForegroundService(startServiceIntent)
        } else {
            // Pre-O behavior.
            context.startService(startServiceIntent)
        }

    }
}