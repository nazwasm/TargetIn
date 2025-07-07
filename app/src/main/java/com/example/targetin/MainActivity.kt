package com.example.targetin

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Delay 2 detik sebelum pindah ke halaman berikutnya
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainContentActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)     // 2000ms = 2 detik
    }
}