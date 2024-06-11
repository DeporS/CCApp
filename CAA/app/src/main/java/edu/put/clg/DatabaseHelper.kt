package edu.put.clg

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "codes.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "codes"
        private const val COLUMN_ID = "id"
        private const val COLUMN_CODE = "code"
        private const val COLUMN_VERDICT = "verdict"
    }

    private val _codesLiveData = MutableLiveData<List<Pair<String, String>>>()
    val codesLiveData: LiveData<List<Pair<String, String>>> get() = _codesLiveData

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_CODE TEXT, $COLUMN_VERDICT TEXT)"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertCode(code: String, verdict: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_CODE, code)
            put(COLUMN_VERDICT, verdict)
        }
        val result = db.insert(TABLE_NAME, null, values)
        updateLiveData()
        return result
    }

    fun getAllCodes(): List<Pair<String, String>> {
        val codes = mutableListOf<Pair<String, String>>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME ORDER BY $COLUMN_ID DESC", null)
        if (cursor.moveToFirst()) {
            do {
                val code = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CODE))
                val verdict = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VERDICT))
                codes.add(Pair(code, verdict))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return codes
    }

    private fun updateLiveData() {
        _codesLiveData.postValue(getAllCodes())
    }
}
