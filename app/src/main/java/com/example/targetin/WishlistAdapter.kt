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

class WishlistAdapter :
    ListAdapter<Wishlist, WishlistAdapter.WishlistViewHolder>(DIFF_CALLBACK) {

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

        // Pakai string resources
        holder.tvNamaBarang.text = wishlist.namaBarang
        holder.tvHarga.text = context.getString(R.string.text_harga, wishlist.harga)
        holder.tvHarian.text = context.getString(R.string.text_harian, wishlist.harian)
        holder.tvEstimasi.text = context.getString(R.string.text_estimasi, wishlist.estimasiHari)
        holder.tvPersentase.text = context.getString(R.string.text_persen)

        if (!wishlist.gambar.isNullOrEmpty()) {
            holder.imageItem.setImageURI(Uri.parse(wishlist.gambar))
        } else {
            holder.imageItem.setImageResource(R.drawable.newbalance)
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