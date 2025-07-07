package com.example.targetin

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Delay 2 detik sebelum pindah ke halaman berikutnya
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainContentActivity::class.java))
            finish()
        }, 2000) // 2000ms = 2 detik
    }
}