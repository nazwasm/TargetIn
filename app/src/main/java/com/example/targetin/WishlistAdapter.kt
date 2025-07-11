package com.example.targetin

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.targetin.model.Wishlist
import java.io.File
import com.example.targetin.FormatUang.formatRupiah

class WishlistAdapter(
    private val onItemClick: (Wishlist) -> Unit
) : ListAdapter<Wishlist, WishlistAdapter.WishlistViewHolder>(DIFF_CALLBACK) {

    class WishlistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNamaBarang: TextView = itemView.findViewById(R.id.tvNamaBarang)
        val tvHarga: TextView = itemView.findViewById(R.id.tvHarga)
        val tvHarian: TextView = itemView.findViewById(R.id.tvHarian)
        val tvEstimasi: TextView = itemView.findViewById(R.id.tvEstimasi)
        val tvPersentase: TextView = itemView.findViewById(R.id.tvPersentase)
        val imageItem: ImageView = itemView.findViewById(R.id.imageItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishlistViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_wishlist_ongoing, parent, false)
        return WishlistViewHolder(view)
    }

    override fun onBindViewHolder(holder: WishlistViewHolder, position: Int) {
        val wishlist = getItem(position)
        val context = holder.itemView.context
        val persen = if (wishlist.harga > 0) {
            ((wishlist.currentSavings.toDouble() / wishlist.harga) * 100).toInt().coerceAtMost(100)
        } else {
            0
        }

        holder.tvNamaBarang.text = wishlist.namaBarang
        holder.tvHarga.text = formatRupiah(wishlist.harga)
        holder.tvHarian.text = "${formatRupiah(wishlist.harian)} harian"
        holder.tvEstimasi.text = context.getString(R.string.text_estimasi, wishlist.estimasiHari)
        holder.tvPersentase.text = "$persen%"

        // ✅ Aman tampilkan gambar jika dari local file URI
        if (!wishlist.gambar.isNullOrEmpty()) {
            try {
                val uri = Uri.parse(wishlist.gambar)
                val path = uri.path
                if (path != null) {
                    val file = File(path)
                    if (file.exists()) {
                        holder.imageItem.setImageURI(Uri.fromFile(file))
                    } else {
                        holder.imageItem.setImageResource(R.drawable.newbalance)
                    }
                } else {
                    holder.imageItem.setImageResource(R.drawable.newbalance)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                holder.imageItem.setImageResource(R.drawable.newbalance)
            }
        } else {
            holder.imageItem.setImageResource(R.drawable.newbalance)
        }

        // 🚀 Klik listener
        holder.itemView.setOnClickListener {
            onItemClick(wishlist)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Wishlist>() {
            override fun areItemsTheSame(oldItem: Wishlist, newItem: Wishlist): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Wishlist, newItem: Wishlist): Boolean {
                return oldItem == newItem
            }
        }
    }
}
