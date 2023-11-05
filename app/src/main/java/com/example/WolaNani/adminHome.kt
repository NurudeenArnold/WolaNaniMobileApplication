package com.example.WolaNani

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class adminHome : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_home)

        val imgDonations = findViewById<ImageView>(R.id.DonationsImg)
        val btnDonations = findViewById<Button>(R.id.Donations)
        val imgVolunteer = findViewById<ImageView>(R.id.VolunteersImg)
        val btnVolunteer = findViewById<Button>(R.id.Volunteers)
        val imgFundraiser = findViewById<ImageView>(R.id.FundraisersImg)
        val btnFundraiser = findViewById<Button>(R.id.Fundraisers)
        val imgAddFundraiser = findViewById<ImageView>(R.id.AddFundraisersImg)
        val btnAddFundraiser = findViewById<Button>(R.id.AddFundraisers)
        drawerLayout = findViewById(R.id.drawerLayout) as DrawerLayout
        navigationView = findViewById(R.id.admin_nav_view) as NavigationView

        imgVolunteer.setOnClickListener {
            intentVolunteers()
        }
        btnVolunteer.setOnClickListener {
            intentVolunteers()
        }

        imgDonations.setOnClickListener {
            intentDonations()
        }
        btnDonations.setOnClickListener {
            intentDonations()
        }

        imgFundraiser.setOnClickListener {
            intentFundraisers()
        }
        btnFundraiser.setOnClickListener {
            intentFundraisers()
        }

        imgAddFundraiser.setOnClickListener {
            intentAddFundraisers()
        }
        btnAddFundraiser.setOnClickListener {
            intentAddFundraisers()
        }

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
                    Toast.makeText(this@adminHome,"Successfully Logged out", Toast.LENGTH_SHORT).show()
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
    fun intentDonations() {
        intent = Intent(this, Donations::class.java)
        startActivity(intent)
    }

    fun intentVolunteers() {
        intent = Intent(this, Volunteers::class.java)
        startActivity(intent)
    }

    fun intentFundraisers() {
        intent = Intent(this, Fundraisers::class.java)
        startActivity(intent)
    }

    fun intentAddFundraisers() {
        intent = Intent(this, AddFundraiser::class.java)
        startActivity(intent)
    }
    override fun onBackPressed() {

    }
}


