package com.example.targetin

import android.Manifest
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.targetin.data.AppDatabase
import com.example.targetin.model.Wishlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

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

        etNama = findViewById(R.id.etNama)
        etTarget = findViewById(R.id.etTarget)
        etSaving = findViewById(R.id.etSaving)
        spinnerType = findViewById(R.id.spinnerType)
        btnSave = findViewById(R.id.btnSave)
        imagePreview = findViewById(R.id.imagePreview)

        db = AppDatabase.getDatabase(this)

        // Tombol kembali
        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        // Tombol tambah foto
        findViewById<androidx.cardview.widget.CardView>(R.id.cardAddPhoto).setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        // Spinner tipe
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

        // Tombol simpan
        btnSave.setOnClickListener {
            val nama = etNama.text.toString()
            val target = etTarget.text.toString().toIntOrNull() ?: 0
            val harian = etSaving.text.toString().toIntOrNull() ?: 0
            val estimasiHari = if (harian > 0) target / harian else 0

            if (nama.isNotEmpty() && target > 0 && harian > 0) {
                val localImageUri = selectedImageUri?.let { uri ->
                    try {
                        val inputStream = contentResolver.openInputStream(uri)
                        val file = File(filesDir, "wishlist_${System.currentTimeMillis()}.jpg")
                        val outputStream = FileOutputStream(file)
                        inputStream?.copyTo(outputStream)
                        inputStream?.close()
                        outputStream.close()
                        Uri.fromFile(file).toString()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        null
                    }
                }

                val wishlist = Wishlist(
                    namaBarang = nama,
                    harga = target,
                    harian = harian,
                    estimasiHari = estimasiHari,
                    gambar = localImageUri,
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
