package com.example.targetin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.targetin.data.AppDatabase
import android.util.Log
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class OnGoingFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var wishlistAdapter: WishlistAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ongoing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("OnGoingFragment", "onViewCreated jalan")

        // dan seterusnya...

    recyclerView = view.findViewById(R.id.recyclerView)
        wishlistAdapter = WishlistAdapter()
        recyclerView.adapter = wishlistAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val db = AppDatabase.getDatabase(requireContext())

        // ✅ Ambil data dari Flow
        viewLifecycleOwner.lifecycleScope.launch {
            db.wishlistDao().getAllWishlist().collect { wishlistList ->
                wishlistAdapter.submitList(wishlistList)
            }
        }

        // Tombol tambah
        val btnTambah = view.findViewById<ImageView>(R.id.btnTambah)
        btnTambah.setOnClickListener {
            val intent = Intent(requireContext(), AddWishlistActivity::class.java)
            startActivity(intent)
        }
    }
}
