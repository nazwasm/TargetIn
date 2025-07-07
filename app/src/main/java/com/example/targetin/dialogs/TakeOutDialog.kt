package com.example.targetin.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import com.example.targetin.R

class TakeOutDialog(context: Context) : Dialog(context) {

    private var onTakeOutListener: ((Int) -> Unit)? = null

    fun setOnTakeOutListener(listener: (Int) -> Unit) {
        onTakeOutListener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_take_money)

        val inputAmount = findViewById<EditText>(R.id.editamount) // ganti sesuai id kalau kamu tambah EditText untuk angka
        val btnCancel = findViewById<TextView>(R.id.btcancel)
        val btnSave = findViewById<TextView>(R.id.btsave)

        btnCancel.setOnClickListener {
            dismiss()
        }

        btnSave.setOnClickListener {
            val amountText = inputAmount.text.toString().replace("Rp", "").replace(".", "").trim()
            val amount = amountText.toIntOrNull() ?: 0
            onTakeOutListener?.invoke(amount)
            dismiss()
        }
    }
}
