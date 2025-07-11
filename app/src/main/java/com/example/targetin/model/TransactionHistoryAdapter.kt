package com.example.targetin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.targetin.R
import com.example.targetin.model.TransactionHistory
import com.example.targetin.FormatUang.formatRupiah

class TransactionHistoryAdapter(
    private val items: List<TransactionHistory>
) : RecyclerView.Adapter<TransactionHistoryAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvAction: TextView = view.findViewById(R.id.tvAction)
        private val tvNote: TextView = view.findViewById(R.id.tvNote)
        private val tvDate: TextView = view.findViewById(R.id.tvDate)

        fun bind(item: TransactionHistory) {
            val formattedAmount = formatRupiah(item.amount)

            tvAction.text = when (item.type) {
                "save" -> "Saved $formattedAmount"
                "takeout" -> "Took out $formattedAmount"
                else -> formattedAmount
            }

            tvNote.text = item.note ?: ""
            tvNote.visibility = if (item.note.isNullOrEmpty()) View.GONE else View.VISIBLE

            tvDate.text = item.date
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
