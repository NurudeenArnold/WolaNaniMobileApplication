package com.example.WolaNani

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Donations : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donations)

        drawerLayout = findViewById(R.id.drawerLayout) as DrawerLayout
        navigationView = findViewById(R.id.admin_nav_view) as NavigationView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val databaseReference = FirebaseDatabase.getInstance().reference.child("donations")
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val donatorList = mutableListOf<DonatorData>()
                for (snapshot in dataSnapshot.children) {
                    val itemOfDonation = snapshot.child("itemOfDonation").getValue(String::class.java)
                    val itemReference = snapshot.child("itemReference").getValue(String::class.java)
                    val phoneNumber = snapshot.child("phoneNumber").getValue(String::class.java)

                    itemOfDonation?.let { item ->
                        itemReference?.let { reference ->
                            phoneNumber?.let { phone ->
                                val donator = DonatorData(reference, item, phone)
                                donatorList.add(donator)
                            }
                        }
                    }
                }
                val adapter = DonatorAdapter(donatorList)
                recyclerView.adapter = adapter
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.donations -> startActivity(Intent(this,Donations::class.java))
                R.id.volunteers -> startActivity(Intent(this,Volunteers::class.java))
                R.id.fundraisers -> startActivity(Intent(this,Fundraisers::class.java))
                R.id.addFundraisers -> startActivity(Intent(this, AddFundraiser::class.java))
                R.id.user -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this,"Successfully Logged out", Toast.LENGTH_SHORT).show()
                    finish()
                }

            }
            true
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