package com.example.WolaNani

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class Home : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        val ourHistory = findViewById<View>(R.id.OurHistoryBtn) as Button
        val OurHistoryImg = findViewById<ImageView>(R.id.OurHistoryImg) as ImageView
        val howWeOperate = findViewById<View>(R.id.HowWeOperateBtn) as Button
        val HowWeOperateImg = findViewById<ImageView>(R.id.HowWeOperateImg) as ImageView
        val aboutUs = findViewById<View>(R.id.AboutUsBtn) as Button
        val AboutUsImg = findViewById<ImageView>(R.id.AboutUsImg) as ImageView
        val donate = findViewById<View>(R.id.DonateBtn) as Button
        val DonateImg = findViewById<ImageView>(R.id.DonateImg) as ImageView
        val volunteer = findViewById<View>(R.id.VolunteerBtn) as Button
        val VolunteerImg = findViewById<ImageView>(R.id.VolunteerImg) as ImageView
        val contact = findViewById<View>(R.id.ContactBtn) as Button
        val ContactImg = findViewById<ImageView>(R.id.ContactImg) as ImageView

        drawerLayout = findViewById(R.id.drawerLayout) as DrawerLayout
        navigationView = findViewById(R.id.nav_view) as NavigationView

        val sharedPref = getSharedPreferences("MY_APP_PREFS", Context.MODE_PRIVATE)
        val getName = sharedPref.getString("USER_NAME", "")
        val usernameItem = navigationView.menu.findItem(R.id.user)
        usernameItem.title = "Logout From: " + getName

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> startActivity(Intent(this,Home::class.java))
                R.id.OurHistory -> startActivity(Intent(this,OurHistory::class.java))
                R.id.HowWeOperate -> startActivity(Intent(this,HowWeOperate::class.java))
                R.id.AboutUs -> startActivity(Intent(this,AboutUs::class.java))
                R.id.Donate -> startActivity(Intent(this,Donate::class.java))
                R.id.Volunteer -> startActivity(Intent(this,Volunteer::class.java))
                R.id.Contact -> startActivity(Intent(this,Contact::class.java))
                R.id.user -> {
                    val sharedPref = getSharedPreferences("MY_APP_PREFS", Context.MODE_PRIVATE)
                    val editor = sharedPref.edit()
                    editor.clear()
                    editor.apply()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this@Home,"Successfully Logged out", Toast.LENGTH_SHORT).show()
                    finish()
                }

            }
            true
        }

        ourHistory.setOnClickListener {

            startActivity(Intent(this,OurHistory::class.java))
        }

        OurHistoryImg.setOnClickListener {
            startActivity(Intent(this,OurHistory::class.java))
        }

        howWeOperate.setOnClickListener {
            startActivity(Intent(this,HowWeOperate::class.java))
        }

        HowWeOperateImg.setOnClickListener {
            startActivity(Intent(this,HowWeOperate::class.java))
        }

        aboutUs.setOnClickListener {
            startActivity(Intent(this,AboutUs::class.java))
        }

        AboutUsImg.setOnClickListener {
            startActivity(Intent(this,AboutUs::class.java))
        }

        donate.setOnClickListener {
            startActivity(Intent(this,Donate::class.java))
        }

        DonateImg.setOnClickListener {
            startActivity(Intent(this,Donate::class.java))
        }

        volunteer.setOnClickListener {
            startActivity(Intent(this,Volunteer::class.java))
        }

        VolunteerImg.setOnClickListener {
            startActivity(Intent(this,Volunteer::class.java))
        }

        contact.setOnClickListener {
            startActivity(Intent(this,Contact::class.java))
        }

        ContactImg.setOnClickListener {
            startActivity(Intent(this,Contact::class.java))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            true
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onBackPressed() {

    }
}