package com.example.WolaNani

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DonateFundraiser : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donate_fundraiser)

        val databaseReference = FirebaseDatabase.getInstance().getReference("fundraisers")
        val spinner = findViewById<Spinner>(R.id.spinner)
        val submit = findViewById<MaterialButton>(R.id.submitbtn)
        val amount = findViewById<TextView>(R.id.Amount)

        var selectedFundraiserName: String? = null

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val fundraiserNames = mutableListOf<String>()

                for (fundraiserSnapshot in dataSnapshot.children) {
                    val fundraiserName = fundraiserSnapshot.key
                    fundraiserName?.let {
                        fundraiserNames.add(it)
                    }
                }

                val adapter = ArrayAdapter(this@DonateFundraiser, android.R.layout.simple_spinner_item, fundraiserNames)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter

                spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {

                        selectedFundraiserName = fundraiserNames[position]
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {

                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

        submit.setOnClickListener {
            if (selectedFundraiserName != null) {
                val totalEnteredStr = amount.text.toString()
                val totalEntered = totalEnteredStr.toIntOrNull()

                if (totalEntered != null) {

                    databaseReference.child(selectedFundraiserName!!).child("total")
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                val currentTotal = dataSnapshot.getValue(Int::class.java) ?: 0
                                val newTotal = currentTotal + totalEntered

                                databaseReference.child(selectedFundraiserName!!)
                                    .child("total").setValue(newTotal)

                                val toastMessage = "Thank you for donating to $selectedFundraiserName"
                                amount.text = ""
                                Toast.makeText(this@DonateFundraiser, toastMessage, Toast.LENGTH_SHORT).show()
                            }
                            override fun onCancelled(databaseError: DatabaseError) {
                            }
                        })
                } else {
                    Toast.makeText(this@DonateFundraiser, "Please enter a valid donation amount", Toast.LENGTH_SHORT).show()
                }
            }
        }
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