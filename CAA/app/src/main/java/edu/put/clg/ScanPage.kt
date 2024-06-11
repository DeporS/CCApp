package edu.put.clg

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.database.ValueEventListener
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import org.w3c.dom.Text
import java.util.logging.Handler

class ScanPage : Fragment() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var database: FirebaseDatabase
    private lateinit var recentViewModel: RecentViewModel
    private lateinit var textOut: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance("https://clg-database-default-rtdb.europe-west1.firebasedatabase.app/")
        database.setPersistenceEnabled(true)

        recentViewModel = ViewModelProvider(requireActivity()).get(RecentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_scan_page, container, false)

        val checkButton = view.findViewById<Button>(R.id.checkBut)
        val scanButton = view.findViewById<Button>(R.id.scanBut)
        val codeInput = view.findViewById<EditText>(R.id.codeInput)
        textOut = view.findViewById<TextView>(R.id.appNameTextView)
        progressBar = view.findViewById<ProgressBar>(R.id.progressBar)


        databaseHelper = DatabaseHelper(requireContext())

        scanButton.setOnClickListener {
            val integrator = IntentIntegrator.forSupportFragment(this)
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE) // Skonfiguruj do skanowania tylko kodów QR
            integrator.setPrompt("Scan a QR Code")
            integrator.setCameraId(0) // 0 to tylna kamera, 1 to przednia kamera
            integrator.setBeepEnabled(true) // Dźwięk po skanowaniu
            integrator.captureActivity = MyCaptureActivity::class.java // pionowa orientacja
            integrator.initiateScan()
        }

        checkButton.setOnClickListener {
            val code = codeInput.text.toString().toInt()
            codeInput.setText("")
            if (isValidCode(code)) {
                checkCodeInDatabase(code, textOut, progressBar)
            } else {
                Toast.makeText(context, "Invalid code format", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result: IntentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                val scannedCode = result.contents.toIntOrNull()
                if (scannedCode != null && isValidCode(scannedCode)) {
                    checkCodeInDatabase(scannedCode, textOut, progressBar)
                    Toast.makeText(context, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "Invalid scanned code", Toast.LENGTH_LONG).show()
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun isValidCode(code: Int): Boolean {
        return code.toString().length == 8
    }

    private fun checkCodeInDatabase(code: Int, textOut: TextView, progressBar: ProgressBar) {
        val codesRef = database.getReference("codes")
        val usesRef = database.getReference("uses")

        codesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var codeExists = false
                for (childSnapshot in dataSnapshot.children) {
                    val valueRaw = childSnapshot.value
                    println(valueRaw)
                    val value = valueRaw.toString().toInt()

                    if (value == code) {
                        codeExists = true

                        usesRef.child(valueRaw.toString()).runTransaction(object : Transaction.Handler {
                            override fun doTransaction(mutableData: MutableData): Transaction.Result {
                                val uses = mutableData.getValue(Int::class.java) ?: 0
                                mutableData.value = uses + 1
                                return Transaction.success(mutableData)
                            }

                            override fun onComplete(databaseError: DatabaseError?, committed: Boolean, dataSnapshot: DataSnapshot?) {
                                if (committed) {
                                    // Wartość węzła została zwiększona o 1
                                    println("Wartość węzła została zwiększona o 1")
                                } else {
                                    // Nie udało się zwiększyć wartości węzła
                                    println("Nie udało się zwiększyć wartości węzła")
                                }
                            }
                        })
                        break
                    }
                }




                if (codeExists) {
                    // gdy kod istnieje w bazie

                    databaseHelper.insertCode(code.toString(), "Authentic")
                    println("Jest taki kod")
                    progressBar.visibility = View.VISIBLE

                    // Ukryj pasek postępu po 2 sekundach
                    android.os.Handler().postDelayed({
                        progressBar.visibility = View.GONE
                        textOut.text = "Authentic!"
                        textOut.setTextColor(Color.parseColor("#75d94e"))
                    }, 2000)


                } else {
                    databaseHelper.insertCode(code.toString(), "Fake")
                    println("Nie ma takiego kodu")
                    progressBar.visibility = View.VISIBLE

                    // Ukryj pasek postępu po 2 sekundach
                    android.os.Handler().postDelayed({
                        progressBar.visibility = View.GONE
                        textOut.text = "Fake!"
                        textOut.setTextColor(Color.parseColor("#d9574e"))
                    }, 2000)


                }


                // Przeładowanie fragmentu
                val fragmentManager = parentFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                val recentFragment = HoldingFragment()

                if (recentFragment != null) {
                    fragmentTransaction.detach(recentFragment)
                    fragmentTransaction.attach(recentFragment)
                    fragmentTransaction.commit()
                }


                // Wywołujemy metodę interfejsu, aby poinformować Activity o zmianach
                //onCodeCheckedListener.onCodeChecked()

                // Odśwież listę kodów w RecentPage
                //recentViewModel.refreshCodes()
                //reloadFragment()

                //restartApp(requireContext())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(context, "Błąd odczytu z bazy danych", Toast.LENGTH_SHORT).show()
            }
        })


    }

    private fun restartApp(context: Context) {
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        if (intent != null) {
            val mainIntent = Intent.makeRestartActivityTask(intent.component)
            context.startActivity(mainIntent)
            Runtime.getRuntime().exit(0)
        }
    }

    private fun reloadFragment() {
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val recentFragment = fragmentManager.findFragmentByTag("RECENT_FRAGMENT_TAG")

        if (recentFragment != null) {
            fragmentTransaction.detach(recentFragment)
            fragmentTransaction.attach(recentFragment)
            fragmentTransaction.commit()
        }
    }
}
