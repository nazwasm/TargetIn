package com.example.targetin

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.targetin.data.AppDatabase
import kotlinx.coroutines.launch

class OnGoingFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var wishlistAdapter: WishlistAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_ongoing, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.recyclerView)
        wishlistAdapter = WishlistAdapter { wishlist ->
            val intent = Intent(requireContext(), WishlistActivity::class.java)
            intent.putExtra("WISHLIST_ID", wishlist.id)
            startActivity(intent)
        }

        recyclerView.adapter = wishlistAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val db = AppDatabase.getDatabase(requireContext())
        viewLifecycleOwner.lifecycleScope.launch {
            db.wishlistDao().getAllWishlist().collect {
                wishlistAdapter.submitList(it)
            }
        }

        view.findViewById<View>(R.id.btnTambah).setOnClickListener {
            startActivity(Intent(requireContext(), AddWishlistActivity::class.java))
        }
    }
}
