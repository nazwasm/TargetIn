package com.example.targetin

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.targetin.data.AppDatabase
import com.example.targetin.model.Wishlist
import com.example.targetin.dialogs.SaveMoneyDialog
import com.example.targetin.dialogs.TakeOutDialog
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class WishlistActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase
    private var wishlistId: Int = -1
    private var currentWishlist: Wishlist? = null

    private lateinit var textNama: TextView
    private lateinit var textHarga: TextView
    private lateinit var textHarian: TextView
    private lateinit var textAchievedDays: TextView
    private lateinit var textPersentase: TextView
    private lateinit var textStartDate: TextView
    private lateinit var textCollected: TextView
    private lateinit var textCollectedDate: TextView
    private lateinit var textRemaining: TextView
    private lateinit var textRemainingDetail: TextView
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wishlist)

        db = AppDatabase.getDatabase(this)
        wishlistId = intent.getIntExtra("WISHLIST_ID", -1)

        initView()

        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        findViewById<ImageView>(R.id.btnDelete).setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Hapus Wishlist")
                .setMessage("Yakin ingin menghapus wishlist ini?")
                .setPositiveButton("Ya") { _, _ ->
                    lifecycleScope.launch {
                        db.wishlistDao().deleteWishlistById(wishlistId)
                        finish()
                    }
                }
                .setNegativeButton("Batal", null)
                .show()
        }

        findViewById<TextView>(R.id.btnSaveMoney).setOnClickListener {
            showSaveMoneyDialog()
        }

        findViewById<TextView>(R.id.btnTakeOut).setOnClickListener {
            showTakeOutDialog()
        }

        if (wishlistId != -1) {
            loadWishlistData()
        }
    }

    private fun initView() {
        textNama = findViewById(R.id.nama_produk)
        textHarga = findViewById(R.id.harga_produk)
        textHarian = findViewById(R.id.tabungan_harian)
        textAchievedDays = findViewById(R.id.achieved_days)
        textPersentase = findViewById(R.id.persentase)
        textStartDate = findViewById(R.id.start_date)
        textCollected = findViewById(R.id.editTotal)
        textCollectedDate = findViewById(R.id.collectedDate)
        textRemaining = findViewById(R.id.remainingAmount)
        textRemainingDetail = findViewById(R.id.remainingDetail)
        imageView = findViewById(R.id.img_sepatu)
    }

    private fun loadWishlistData() {
        lifecycleScope.launch {
            currentWishlist = db.wishlistDao().getWishlistById(wishlistId)

            currentWishlist?.let { wishlist ->
                textNama.text = wishlist.namaBarang
                textHarga.text = getString(R.string.text_harga, wishlist.harga)
                textHarian.text = getString(R.string.text_harian, wishlist.harian)

                // Hitung dan tampilkan persentase
                val percent = (wishlist.currentSavings.toFloat() / wishlist.harga * 100).toInt()
                textPersentase.text = "$percent%"

                // Started Date
                textStartDate.text = wishlist.startedDate.toFormattedDate()

                // Achieved Days
                val achievedDays = if (wishlist.isAchieved) {
                    val duration = ((wishlist.lastSavedDate ?: System.currentTimeMillis()) - wishlist.startedDate) / (1000 * 60 * 60 * 24)
                    "$duration Days"
                } else {
                    "-"
                }
                textAchievedDays.text = achievedDays

                // Collected
                textCollected.text = "Rp ${wishlist.currentSavings}"
                textCollectedDate.text = wishlist.lastSavedDate?.toFormattedDate() ?: "-"

                // Remaining
                val remaining = wishlist.harga - wishlist.currentSavings
                textRemaining.text = "Rp $remaining"
                textRemainingDetail.text = "+ Rp ${wishlist.harian}"

                // Gambar
                if (!wishlist.gambar.isNullOrEmpty()) {
                    imageView.setImageURI(Uri.parse(wishlist.gambar))
                } else {
                    imageView.setImageResource(R.drawable.newbalance)
                }
            }
        }
    }

    private fun showSaveMoneyDialog() {
        currentWishlist?.let { wishlist ->
            val dialog = SaveMoneyDialog(this)
            dialog.setOnSaveListener { amount ->
                val newSavings = wishlist.currentSavings + amount
                val achieved = newSavings >= wishlist.harga

                val updated = wishlist.copy(
                    currentSavings = newSavings,
                    lastSavedDate = System.currentTimeMillis(),
                    isAchieved = achieved
                )

                lifecycleScope.launch {
                    db.wishlistDao().updateWishlist(updated)
                    loadWishlistData()
                    currentWishlist = updated
                }

                if (achieved) {
                    val intent = Intent(this@WishlistActivity, MainActivity::class.java)
                    intent.putExtra("NAVIGATE_TO", "ACHIEVED")
                    startActivity(intent)
                    finish()
                }
            }
            dialog.show()
        }
    }


    private fun showTakeOutDialog() {
        currentWishlist?.let { wishlist ->
            val dialog = TakeOutDialog(this)
            dialog.setOnTakeOutListener { amount ->
                val newSavings = (wishlist.currentSavings - amount).coerceAtLeast(0)

                val updated = wishlist.copy(
                    currentSavings = newSavings,
                    lastSavedDate = System.currentTimeMillis(), // biar update tanggal terakhir
                    isAchieved = newSavings >= wishlist.harga
                )

                lifecycleScope.launch {
                    db.wishlistDao().updateWishlist(updated)
                    loadWishlistData()
                    currentWishlist = updated
                }
            }
            dialog.show()
        }
    }

    private fun Long.toFormattedDate(): String {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
        return sdf.format(Date(this))
    }
}
