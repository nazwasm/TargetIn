package com.example.targetin.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import com.example.targetin.FormatUang
import com.example.targetin.R
import com.example.targetin.FormatUang.formatRupiah
import com.example.targetin.FormatUang.unformatRupiah
import com.example.targetin.FormatUang.addRupiahFormatter

class TakeOutDialog(context: Context, private val defaultAmount: Int) : Dialog(context) {

    private var onTakeOutListener: ((Int, String?) -> Unit)? = null

    fun setOnTakeOutListener(listener: (Int, String?) -> Unit) {
        onTakeOutListener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_take_money)

        val inputAmount = findViewById<EditText>(R.id.editamount)
        addRupiahFormatter(inputAmount)
        inputAmount.setText(defaultAmount.toString())
        val inputNote = findViewById<EditText>(R.id.editnote)
        val btnCancel = findViewById<TextView>(R.id.btcancel)
        val btnSave = findViewById<TextView>(R.id.btsave)

        btnCancel.setOnClickListener {
            dismiss()
        }

        btnSave.setOnClickListener {
            val amountText = inputAmount.text.toString().replace("Rp", "").replace(".", "").trim()
            val amount = FormatUang.unformatRupiah(amountText)
            Log.d("TakeOutDialog", "Nominal dikeluarkan: ${formatRupiah(amount)}")
            val note = inputNote.text.toString().takeIf { it.isNotBlank() }
            onTakeOutListener?.invoke(amount, note)
            dismiss()
        }
    }
}
