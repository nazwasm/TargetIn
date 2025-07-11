package com.example.targetin.data

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.targetin.model.TransactionHistory
import com.example.targetin.model.Wishlist

@Database(
    entities = [Wishlist::class, TransactionHistory::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wishlistDao(): WishlistDao
    abstract fun transactionHistoryDao(): TransactionHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE wishlist ADD COLUMN currentSavings INTEGER NOT NULL DEFAULT 0")
                // Optional: kalau tabel transaction_history belum ada, bisa tambahkan ini:
                // database.execSQL("CREATE TABLE IF NOT EXISTS `transaction_history` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `wishlistId` INTEGER NOT NULL, `type` TEXT NOT NULL, `amount` TEXT NOT NULL, `note` TEXT, `date` TEXT NOT NULL)")
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
                    .fallbackToDestructiveMigration() // kalau ga mau migrasi manual, bisa aktifin ini
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
