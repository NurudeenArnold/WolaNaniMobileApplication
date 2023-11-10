package com.example.WolaNani

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

class Volunteer : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    val databaseref = FirebaseDatabase.getInstance().getReferenceFromUrl("https://wolanani-53ae1-default-rtdb.firebaseio.com");

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volunteer)

        drawerLayout = findViewById(R.id.drawerLayout) as DrawerLayout
        navigationView = findViewById(R.id.nav_view) as NavigationView

        val sharedPref = getSharedPreferences("MY_APP_PREFS", Context.MODE_PRIVATE)
        val getName = sharedPref.getString("USER_NAME", "")
        val usernameItem = navigationView.menu.findItem(R.id.user)
        val submit = findViewById<MaterialButton>(R.id.submitbtn)

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
                    Toast.makeText(this@Volunteer, "Successfully Logged out", Toast.LENGTH_SHORT)
                        .show()
                    finish()
                }
            }
            true
        }

        val txthome = findViewById<EditText>(R.id.HomeAddress)
        val txtcity = findViewById<EditText>(R.id.City)
        val txtstate = findViewById<EditText>(R.id.State)
        val txtpostalCode = findViewById<EditText>(R.id.PostalCode)
        val txtreason = findViewById<EditText>(R.id.Reason)

        submit.setOnClickListener {
            var home: String = txthome.text.toString()
            var city: String = txtcity.text.toString()
            var state: String = txtstate.text.toString()
            var postalCode: String = txtpostalCode.text.toString()
            var reason: String = txtreason.text.toString()
            val getPhone = sharedPref.getString("PHONE", "")


            if (home.isEmpty() || city.isEmpty() || state.isEmpty() || postalCode.isEmpty() || reason.isEmpty()) {
                Toast.makeText(this@Volunteer, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                databaseref.child("volunteers").addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.hasChild(getPhone.toString())) {
                            Toast.makeText(
                                this@Volunteer,
                                "Your phone number is already registered as a volunteer",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        else {
                            databaseref.child("volunteers").child(getPhone.toString()).child("name")
                                .setValue(getName.toString().trim())
                            databaseref.child("volunteers").child(getPhone.toString()).child("phoneNumber")
                                .setValue(getPhone.toString().trim())
                            databaseref.child("volunteers").child(getPhone.toString()).child("home")
                                .setValue(home.trim())
                            databaseref.child("volunteers").child(getPhone.toString()).child("city")
                                .setValue(city.trim())
                            databaseref.child("volunteers").child(getPhone.toString()).child("state")
                                .setValue(state.trim())
                            databaseref.child("volunteers").child(getPhone.toString()).child("postalCode")
                                .setValue(postalCode.trim())
                            databaseref.child("volunteers").child(getPhone.toString()).child("reason")
                                .setValue(reason.trim())
                            Toast.makeText(this@Volunteer, "User Volunteered Successfully", Toast.LENGTH_SHORT)
                                .show()

                            Log.d("Name" + getName, "")
                            txthome.text.clear()
                            txtcity.text.clear()
                            txtpostalCode.text.clear()
                            txtstate.text.clear()
                            txtreason.text.clear()
                        }
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
        startActivity(Intent(this, Home::class.java))
    }
}