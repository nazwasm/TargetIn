package com.example.targetin.data

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.targetin.model.Wishlist

@Database(entities = [Wishlist::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun wishlistDao(): WishlistDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Menambahkan kolom currentSavings ke tabel wishlist
                database.execSQL("ALTER TABLE wishlist ADD COLUMN currentSavings INTEGER NOT NULL DEFAULT 0")
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "wishlist_db"
                )
                    .addMigrations(MIGRATION_1_2)
                    .fallbackToDestructiveMigration() // Opsional: hapus ini kalau ingin pakai migrasi secara aman
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
