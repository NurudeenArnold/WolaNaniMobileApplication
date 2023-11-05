package com.example.WolaNani

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Donate : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    val databaseref = FirebaseDatabase.getInstance().getReferenceFromUrl("https://wolanani-53ae1-default-rtdb.firebaseio.com");

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donate)

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
                R.id.home -> startActivity(Intent(this, Home::class.java))
                R.id.OurHistory -> startActivity(Intent(this, OurHistory::class.java))
                R.id.HowWeOperate -> startActivity(Intent(this, HowWeOperate::class.java))
                R.id.AboutUs -> startActivity(Intent(this, AboutUs::class.java))
                R.id.Donate -> startActivity(Intent(this, Donate::class.java))
                R.id.Volunteer -> startActivity(Intent(this, Volunteer::class.java))
                R.id.Contact -> startActivity(Intent(this, Contact::class.java))
                R.id.user -> {
                    val sharedPref = getSharedPreferences("MY_APP_PREFS", Context.MODE_PRIVATE)
                    val editor = sharedPref.edit()
                    editor.clear()
                    editor.apply()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this@Donate,"Successfully Logged out", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            true
        }

        val Fundraiser = findViewById<MaterialButton>(R.id.btnFundraisers)
        val submit = findViewById<MaterialButton>(R.id.submitbtn)
        val txtdonation = findViewById<EditText>(R.id.WhatYouDonating)
        val txtreference = findViewById<EditText>(R.id.reference)

        Fundraiser.setOnClickListener {
            startActivity(Intent(this, DonateFundraiser::class.java))
        }

        submit.setOnClickListener {
            var donation: String = txtdonation.text.toString()
            var reference: String = txtreference.text.toString()
            val getPhone = sharedPref.getString("PHONE", "")

            if (donation.isEmpty() && reference.isEmpty()) {
                Toast.makeText(this@Donate, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                databaseref.child("volunteers").addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val randomKey = databaseref.push().key

                        databaseref.child("donations").child(randomKey.toString()).child("itemReference")
                            .setValue(reference.trim())
                        databaseref.child("donations").child(randomKey.toString()).child("itemOfDonation")
                                .setValue(donation.trim())
                        databaseref.child("donations").child(randomKey.toString()).child("phoneNumber")
                            .setValue(getPhone.toString().trim())

                            Toast.makeText(this@Donate, "Thank you for Donating!", Toast.LENGTH_SHORT)
                                .show()

                            txtdonation.text.clear()
                            txtreference.text.clear()
                    }
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            true
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onBackPressed() {

    }
}