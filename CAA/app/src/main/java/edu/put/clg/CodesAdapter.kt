package edu.put.clg

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class CodesAdapter(context: Context, private val codes: List<Pair<String, String>>) : ArrayAdapter<Pair<String, String>>(context, 0, codes) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false)

        val code = getItem(position)?.first
        val verdict = getItem(position)?.second

        val text1 = view.findViewById<TextView>(android.R.id.text1)
        val text2 = view.findViewById<TextView>(android.R.id.text2)

        text1.text = code
        text2.text = verdict

        return view
    }
}