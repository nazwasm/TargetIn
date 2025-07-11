package com.example.targetin

import android.text.Editable
import android.widget.EditText
import android.text.TextWatcher
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.text.ParseException
import java.util.*

object FormatUang {

    private val localeID = Locale("in", "ID")
    private val formatter = NumberFormat.getCurrencyInstance(localeID)

    fun formatRupiah(value: Int?): String {
        return if (value != null) {
            formatter.format(value).replace(",00", "")
        } else {
            "Rp 0"
        }
    }

    fun formatRupiahPlain(value: Int?): String {
        return if (value != null) {
            NumberFormat.getNumberInstance(localeID).format(value)
        } else {
            "0"
        }
    }

    fun formatRupiahPlusMinus(value: Int?, isPlus: Boolean): String {
        val formatted = formatRupiahPlain(value)
        return if (isPlus) "+ Rp $formatted" else "- Rp $formatted"
    }

    fun unformatRupiah(value: String?): Int {
        return try {
            val clean = value?.replace("[^\\d]".toRegex(), "") ?: "0"
            clean.toInt()
        } catch (e: NumberFormatException) {
            0
        }
    }

    fun addRupiahFormatter(editText: EditText) {
        var current = ""
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != current) {
                    editText.removeTextChangedListener(this)

                    val clean = s.toString().replace("[Rp,.\\s]".toRegex(), "")
                    if (clean.isNotEmpty()) {
                        val parsed = clean.toLongOrNull() ?: 0L
                        val formatted = "Rp ${DecimalFormat("#,###", DecimalFormatSymbols(Locale("id", "ID"))).format(parsed)}"
                        current = formatted
                        editText.setText(formatted)
                        editText.setSelection(formatted.length)
                    } else {
                        current = ""
                    }

                    editText.addTextChangedListener(this)
                }
            }
        })
    }
}
