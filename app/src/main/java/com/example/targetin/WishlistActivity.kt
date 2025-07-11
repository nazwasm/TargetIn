package com.example.targetin

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.targetin.adapter.TransactionHistoryAdapter
import com.example.targetin.data.AppDatabase
import com.example.targetin.dialogs.SaveMoneyDialog
import com.example.targetin.dialogs.TakeOutDialog
import com.example.targetin.model.TransactionHistory
import com.example.targetin.model.Wishlist
import com.example.targetin.FormatUang.formatRupiah
import com.example.targetin.FormatUang.formatRupiahPlusMinus
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import android.net.Uri

class WishlistActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private var wishlistId: Int = -1
    private var currentWishlist: Wishlist? = null

    // UI Komponen
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
    private lateinit var recyclerViewHistory: RecyclerView
    private lateinit var historyAdapter: TransactionHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wishlist)

        db = AppDatabase.getDatabase(this)
        wishlistId = intent.getIntExtra("WISHLIST_ID", -1)

        initView()

        findViewById<ImageView>(R.id.btnBack).setOnClickListener { finish() }

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

        findViewById<TextView>(R.id.btnSaveMoney).setOnClickListener { showSaveMoneyDialog() }
        findViewById<TextView>(R.id.btnTakeOut).setOnClickListener { showTakeOutDialog() }

        if (wishlistId != -1) {
            loadWishlistData()
            loadTransactionHistory()
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
        recyclerViewHistory = findViewById(R.id.recyclerViewHistory)
    }

    private fun loadWishlistData() {
        lifecycleScope.launch {
            currentWishlist = db.wishlistDao().getWishlistById(wishlistId)

            currentWishlist?.let { wishlist ->
                textNama.text = wishlist.namaBarang
                if (!wishlist.gambar.isNullOrEmpty()) {
                    imageView.setImageURI(Uri.parse(wishlist.gambar))
                } else {
                    imageView.setImageResource(R.drawable.newbalance) // fallback default
                }
                textHarga.text = formatRupiah(wishlist.harga)
                textHarian.text = formatRupiah(wishlist.harian)

                val percent = (wishlist.currentSavings.toFloat() / wishlist.harga * 100).toInt()
                textPersentase.text = "$percent%"

                textStartDate.text = wishlist.startedDate.toFormattedDate()

                val achievedDays = if (wishlist.isAchieved) {
                    val duration = ((wishlist.lastSavedDate ?: System.currentTimeMillis()) - wishlist.startedDate) / (1000 * 60 * 60 * 24)
                    "$duration Days"
                } else {
                    "-"
                }
                textAchievedDays.text = achievedDays

                textCollected.text = formatRupiah(wishlist.currentSavings)
                textCollectedDate.text = wishlist.lastSavedDate?.toFormattedDate() ?: "-"

                val remaining = wishlist.harga - wishlist.currentSavings
                textRemaining.text = formatRupiah(remaining)

                // FIXED: ambil data latestHistory secara aman karena suspend function
                val latestHistory = db.transactionHistoryDao().getLatestByWishlistId(wishlist.id)
                textRemainingDetail.text = if (latestHistory != null) {
                    formatRupiahPlusMinus(latestHistory.amount, latestHistory.type == "save")
                } else {
                    formatRupiahPlusMinus(wishlist.harian, true)
                }


                loadTransactionHistory()
            }
        }
    }

    private fun loadTransactionHistory() {
        lifecycleScope.launch {
            currentWishlist?.let { wishlist ->
                db.transactionHistoryDao().getByWishlistId(wishlist.id).collect { histories ->
                    historyAdapter = TransactionHistoryAdapter(histories)
                    recyclerViewHistory.layoutManager = LinearLayoutManager(this@WishlistActivity)
                    recyclerViewHistory.adapter = historyAdapter
                }
            }
        }
    }

    private fun showSaveMoneyDialog() {
        currentWishlist?.let { wishlist ->
            val dialog = SaveMoneyDialog(this, wishlist.harian)
            dialog.setOnSaveListener { amount, note ->
                val newSavings = wishlist.currentSavings + amount
                val achieved = newSavings >= wishlist.harga

                val updated = wishlist.copy(
                    currentSavings = newSavings,
                    lastSavedDate = System.currentTimeMillis(),
                    isAchieved = achieved
                )

                lifecycleScope.launch {
                    db.wishlistDao().updateWishlist(updated)
                    currentWishlist = updated
                    loadWishlistData()

                    val formattedDate = getTodayFormatted()
                    val history = TransactionHistory(
                        wishlistId = wishlist.id,
                        type = "save",
                        amount = amount,
                        note = note,
                        date = formattedDate
                    )
                    db.transactionHistoryDao().insert(history)
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
            val dialog = TakeOutDialog(this, wishlist.harian)
            dialog.setOnTakeOutListener { amount, note ->
                val newSavings = (wishlist.currentSavings - amount).coerceAtLeast(0)

                val updated = wishlist.copy(
                    currentSavings = newSavings,
                    lastSavedDate = System.currentTimeMillis(),
                    isAchieved = newSavings >= wishlist.harga
                )

                val formattedDate = getTodayFormatted()

                lifecycleScope.launch {
                    db.wishlistDao().updateWishlist(updated)
                    currentWishlist = updated
                    loadWishlistData()

                    val history = TransactionHistory(
                        wishlistId = wishlist.id,
                        type = "takeout",
                        amount = amount,
                        note = note,
                        date = formattedDate
                    )
                    db.transactionHistoryDao().insert(history)
                }
            }
            dialog.show()
        }
    }

    private fun Long.toFormattedDate(): String {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
        return sdf.format(Date(this))
    }

    private fun getTodayFormatted(): String {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
        return sdf.format(Date())
    }
}
