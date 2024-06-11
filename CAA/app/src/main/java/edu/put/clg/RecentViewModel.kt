package edu.put.clg

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class RecentViewModel(application: Application) : AndroidViewModel(application) {

    private val databaseHelper: DatabaseHelper = DatabaseHelper(application)

    private val _codes = MutableLiveData<List<Pair<String, String>>>()
    val codes: LiveData<List<Pair<String, String>>> get() = _codes

    init {
        loadCodes()
    }

    private fun loadCodes() {
        _codes.postValue(databaseHelper.getAllCodes())
    }

    fun refreshCodes() {
        loadCodes()
    }
}
