package com.example.targetin.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction_history")
data class TransactionHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val wishlistId: Int,
    val type: String,
    val amount: Int,
    val note: String?,
    val date: String
)
