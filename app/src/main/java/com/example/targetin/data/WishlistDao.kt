package com.example.targetin.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.targetin.model.Wishlist
import kotlinx.coroutines.flow.Flow

@Dao
interface WishlistDao {
    @Insert
    suspend fun insertWishlist(wishlist: Wishlist)

    @Query("SELECT * FROM wishlist WHERE isAchieved = 0")
    fun getAllWishlist(): Flow<List<Wishlist>>
}
