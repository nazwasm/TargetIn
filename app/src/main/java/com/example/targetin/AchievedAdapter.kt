package com.example.targetin.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.targetin.R
import com.example.targetin.model.Wishlist

class AchievedAdapter(private val list: List<Wishlist>) :
    RecyclerView.Adapter<AchievedAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nama: TextView = view.findViewById(R.id.tvNamaBarang)
        val harga: TextView = view.findViewById(R.id.tvHarga)
        val achieved: TextView = view.findViewById(R.id.tvAchieved)
        val image: ImageView = view.findViewById(R.id.imageItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_wishlist_achieved, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.nama.text = item.namaBarang
        holder.harga.text = "Rp ${item.harga}"

        val days = if (item.lastSavedDate != null) {
            val diff = (item.lastSavedDate!! - item.startedDate) / (1000 * 60 * 60 * 24)
            "$diff days"
        } else {
            "-"
        }

        holder.achieved.text = "Achieved in $days"

        if (!item.gambar.isNullOrEmpty()) {
            holder.image.setImageURI(Uri.parse(item.gambar))
        } else {
            holder.image.setImageResource(R.drawable.logo_wishlist)
        }
    }
}
