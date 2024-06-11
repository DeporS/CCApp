package edu.put.clg

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth


class ContactPage(email: String) : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_contact_page, container, false)

        val emailText = view.findViewById<TextView>(R.id.emailText)

        // Pobierz bieżącego użytkownika
        val user = FirebaseAuth.getInstance().currentUser

        // Sprawdź, czy użytkownik jest zalogowany
        if (user != null) {
            // Użytkownik jest zalogowany, więc możesz pobrać jego adres e-mail
            val email = user.email
            if (email != null) {
                val text = "Hello, $email!"

                val spannableString = SpannableString(text)

                // Indeks początku i końca tekstu emaila
                val startIndex = text.indexOf(email)
                val endIndex = startIndex + email.length

                // Ustawienie koloru tekstu emaila
                spannableString.setSpan(
                    ForegroundColorSpan(Color.YELLOW),
                    startIndex,
                    endIndex,
                    0
                )

                // Ustawienie sformatowanego tekstu w TextView
                emailText.text = spannableString

                Log.d("TAG", "User email: $email")
            } else {
                // Adres e-mail użytkownika jest pusty
                Log.d("TAG", "User email is null")
            }
        } else {
            // Użytkownik nie jest zalogowany
            Log.d("TAG", "User is not logged in")
        }

        return view
    }


}