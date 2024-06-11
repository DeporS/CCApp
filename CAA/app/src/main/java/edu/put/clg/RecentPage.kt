package edu.put.clg

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.lifecycle.ViewModelProvider

class RecentPage : Fragment() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var adapter: ArrayAdapter<Pair<String, String>>
    private lateinit var recentsList: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_recent_page, container, false)

        recentsList = view.findViewById<ListView>(R.id.recentsList)
        databaseHelper = DatabaseHelper(requireContext())

        updateListView()

        return view
    }

    fun updateListView(){
        println("Jestem w update")
        val codes = databaseHelper.getAllCodes()
        adapter = CodesAdapter(requireContext(), codes)
        recentsList.adapter = adapter
    }

}