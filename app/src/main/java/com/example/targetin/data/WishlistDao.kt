package com.example.targetin.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.targetin.model.Wishlist
import kotlinx.coroutines.flow.Flow

@Dao
interface WishlistDao {
    @Insert
    suspend fun insertWishlist(wishlist: Wishlist)

    @Query("SELECT * FROM wishlist WHERE isAchieved = 0")
    fun getAllWishlist(): Flow<List<Wishlist>>

    @Delete
    suspend fun deleteWishlist(wishlist: Wishlist)

    @Query("DELETE FROM wishlist WHERE id = :id")
    suspend fun deleteWishlistById(id: Int)

    @Update
    suspend fun updateWishlist(wishlist: Wishlist)

    @Query("SELECT * FROM wishlist WHERE id = :id LIMIT 1")
    suspend fun getWishlistById(id: Int): Wishlist?

    @Query("SELECT * FROM wishlist WHERE isAchieved = 1")
    suspend fun getAllAchieved(): List<Wishlist>

}
