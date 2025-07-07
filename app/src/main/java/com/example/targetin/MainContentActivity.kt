package com.example.targetin

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainContentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainContent", "onCreate jalan")

        enableEdgeToEdge()
        setContentView(R.layout.activity_main_content)

        Log.d("MainContent", "setContentView berhasil")

        val tabOnGoing = findViewById<TextView>(R.id.tabOnGoing)
        val tabAchieved = findViewById<TextView>(R.id.tabAchieved)
        Log.d("MainContent", "findViewById selesai")

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, OnGoingFragment())
            .commit()

    // Handle klik tab OnGoing
        tabOnGoing.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, OnGoingFragment())
                .commit()

            tabOnGoing.setBackgroundResource(R.drawable.bg_tab_active)
            tabAchieved.setBackgroundResource(R.drawable.bg_tab_inactive)
        }

        // Handle klik tab Achieved
        tabAchieved.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AchievedFragment())
                .commit()

            tabOnGoing.setBackgroundResource(R.drawable.bg_tab_inactive)
            tabAchieved.setBackgroundResource(R.drawable.bg_tab_active)
        }
    }
}