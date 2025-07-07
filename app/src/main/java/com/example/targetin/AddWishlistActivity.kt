package com.example.targetin

import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.targetin.data.AppDatabase
import com.example.targetin.model.Wishlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddWishlistActivity : AppCompatActivity() {

    private lateinit var etNama: EditText
    private lateinit var etTarget: EditText
    private lateinit var etSaving: EditText
    private lateinit var spinnerType: Spinner
    private lateinit var btnSave: Button
    private lateinit var imagePreview: ImageView

    private lateinit var db: AppDatabase
    private lateinit var typePilihan: String
    private var selectedImageUri: Uri? = null

    // Buat ambil gambar dari galeri
    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            selectedImageUri = it
            imagePreview.setImageURI(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_wishlist)

        // Inisialisasi komponen UI
        etNama = findViewById(R.id.etNama)
        etTarget = findViewById(R.id.etTarget)
        etSaving = findViewById(R.id.etSaving)
        spinnerType = findViewById(R.id.spinnerType)
        btnSave = findViewById(R.id.btnSave)
        imagePreview = findViewById(R.id.imagePreview)

        db = AppDatabase.getDatabase(this)

        // 🔙 Tombol kembali
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

        // 📷 Pilih gambar dari galeri
        val cardAddPhoto = findViewById<androidx.cardview.widget.CardView>(R.id.cardAddPhoto)
        cardAddPhoto.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        // 🔄 Spinner pilihan tipe
        val items = listOf("Daily", "Saving")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerType.adapter = adapter

        spinnerType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View, position: Int, id: Long) {
                typePilihan = items[position].lowercase()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                typePilihan = "daily"
            }
        }

        // 💾 Tombol simpan
        btnSave.setOnClickListener {
            val nama = etNama.text.toString()
            val target = etTarget.text.toString().toIntOrNull() ?: 0
            val harian = etSaving.text.toString().toIntOrNull() ?: 0
            val estimasiHari = if (harian > 0) (target / harian) else 0

            if (nama.isNotEmpty() && target > 0 && harian > 0) {
                val wishlist = Wishlist(
                    namaBarang = nama,
                    harga = target,
                    harian = harian,
                    estimasiHari = estimasiHari,
                    gambar = selectedImageUri?.toString(),
                    isAchieved = false
                )

                lifecycleScope.launch(Dispatchers.IO) {
                    db.wishlistDao().insertWishlist(wishlist)
                    runOnUiThread {
                        Toast.makeText(this@AddWishlistActivity, "Wishlist berhasil disimpan!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            } else {
                Toast.makeText(this, "Isi semua kolom dengan benar", Toast.LENGTH_SHORT).show()
            }
        }
    }
}