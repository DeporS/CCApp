package edu.put.clg

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup




class CheckScanFragment : Fragment() {


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
        val view = inflater.inflate(R.layout.fragment_check_scan, container, false)
        replaceFragment()
        return view
    }

    fun replaceFragment() {
        // Uzyskaj instancję FragmentManager
        val fragmentManager = requireActivity().supportFragmentManager

        // Rozpocznij transakcję fragmentu
        val transaction = fragmentManager.beginTransaction()

        // Zastąp bieżący fragment nowym fragmentem
        val newFragment = ScanPage()
        transaction.replace(R.id.mainLayout, newFragment)

        // Wykonaj transakcję
        transaction.commit()
    }

}