package com.example.targetin.data

import androidx.room.*
import com.example.targetin.model.TransactionHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(history: TransactionHistory)

    @Query("SELECT * FROM transaction_history WHERE wishlistId = :wishlistId ORDER BY id DESC")
    fun getByWishlistId(wishlistId: Int): Flow<List<TransactionHistory>>

    @Query("DELETE FROM transaction_history WHERE wishlistId = :wishlistId")
    suspend fun deleteByWishlistId(wishlistId: Int)

    @Query("SELECT * FROM transaction_history WHERE wishlistId = :wishlistId ORDER BY id DESC LIMIT 1")
    suspend fun getLatestByWishlistId(wishlistId: Int): TransactionHistory?

}
