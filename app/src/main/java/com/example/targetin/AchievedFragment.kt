package com.example.targetin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.targetin.adapter.AchievedAdapter
import com.example.targetin.data.AppDatabase
import kotlinx.coroutines.launch

class AchievedFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var kosongText: TextView
    private lateinit var db: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_achieved, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewAchieved)
        kosongText = view.findViewById(R.id.tvKosong)
        db = AppDatabase.getDatabase(requireContext())

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        loadData()

        return view
    }

    private fun loadData() {
        lifecycleScope.launch {
            val achievedList = db.wishlistDao().getAllAchieved() // query yang return isAchieved = true

            if (achievedList.isEmpty()) {
                kosongText.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            } else {
                kosongText.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                recyclerView.adapter = AchievedAdapter(achievedList)
            }
        }
    }
}
