package com.example.targetin.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wishlist")
data class Wishlist(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val namaBarang: String,
    val harga: Int,
    val harian: Int,
    val estimasiHari: Int,
    val gambar: String?,
    val currentSavings: Int = 0,
    val startedDate: Long = System.currentTimeMillis(),
    val lastSavedDate: Long? = null,
    val isAchieved: Boolean = false
)