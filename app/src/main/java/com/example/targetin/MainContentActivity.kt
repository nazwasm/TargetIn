package com.example.targetin

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.enableEdgeToEdge
import android.util.Log

class MainContentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_content)

        val tabOnGoing = findViewById<TextView>(R.id.tabOnGoing)
        val tabAchieved = findViewById<TextView>(R.id.tabAchieved)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, OnGoingFragment())
            .commit()

        tabOnGoing.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, OnGoingFragment())
                .commit()

            tabOnGoing.setBackgroundResource(R.drawable.bg_tab_active)
            tabAchieved.setBackgroundResource(R.drawable.bg_tab_inactive)
        }

        tabAchieved.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AchievedFragment())
                .commit()

            tabOnGoing.setBackgroundResource(R.drawable.bg_tab_inactive)
            tabAchieved.setBackgroundResource(R.drawable.bg_tab_active)
        }
    }
}
