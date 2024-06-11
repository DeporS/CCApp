package edu.put.clg

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import edu.put.clg.databinding.ActivityLoginBinding
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonLogin: Button
    private lateinit var buttonRegister: Button

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inicjalizacja Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Inicjalizacja pól widoku
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonLogin = findViewById(R.id.buttonLogin)
        buttonRegister = findViewById(R.id.buttonRegister)

        // Obsługa kliknięcia przycisku "Login"
        buttonLogin.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            // Logowanie użytkownika za pomocą adresu e-mail i hasła
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Logowanie zakończone sukcesem
                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()

                        // po zalogowaniu
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("email", email)
                        startActivity(intent)
                        finish()
                    } else {
                        // Logowanie nie powiodło się, wyświetl komunikat o błędzie
                        Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        // Obsługa kliknięcia przycisku "Register"
        buttonRegister.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            // Rejestracja nowego użytkownika za pomocą adresu e-mail i hasła
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Rejestracja zakończona sukcesem
                        Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                        // Tutaj możesz przekierować użytkownika do ekranu logowania lub wykonać inne działania
                    } else {
                        // Rejestracja nie powiodła się, wyświetl komunikat o błędzie
                        Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

}