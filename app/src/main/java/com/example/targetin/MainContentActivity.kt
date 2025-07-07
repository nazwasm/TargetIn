package com.example.targetin

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainContentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_content)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // -------------------------- Tambahan dari Piti --------------------------
        val tabOnGoing = findViewById<TextView>(R.id.tabOnGoing)
        val tabAchieved = findViewById<TextView>(R.id.tabAchieved)

        // Tampilkan default fragment (On Going)
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