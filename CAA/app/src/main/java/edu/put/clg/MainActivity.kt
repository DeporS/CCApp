package edu.put.clg

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val email = intent.getStringExtra("email").toString()

        val viewPager: ViewPager = findViewById(R.id.viewPager)
        viewPager.adapter = VPA(supportFragmentManager, email)

        val tabLayout: TabLayout = findViewById(R.id.tab_layout)
        tabLayout.setupWithViewPager(viewPager)

        FirebaseApp.initializeApp(this)

    }

}