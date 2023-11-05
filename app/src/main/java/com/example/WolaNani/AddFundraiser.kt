package com.example.WolaNani

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

class AddFundraiser : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    val databaseref = FirebaseDatabase.getInstance().getReferenceFromUrl("https://wolanani-53ae1-default-rtdb.firebaseio.com");
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_fundraiser)

        drawerLayout = findViewById(R.id.drawerLayout) as DrawerLayout
        navigationView = findViewById(R.id.admin_nav_view) as NavigationView
        val submit = findViewById<MaterialButton>(R.id.submitbtn)
        val txtName = findViewById<EditText>(R.id.Name)
        val txtDescription = findViewById<EditText>(R.id.Description)
        val txtGoal = findViewById<EditText>(R.id.Goal)

        submit.setOnClickListener {
            var name: String = txtName.text.toString()
            var description: String = txtDescription.text.toString()
            var goal: String = txtGoal.text.toString()

            if (name.isEmpty() && description.isEmpty() && goal.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                databaseref.child("fundraisers").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.hasChild(name)) {

                            Toast.makeText(this@AddFundraiser, "Fundraiser with this name already exists!", Toast.LENGTH_SHORT).show()
                        } else {

                            val randomKey = name.trim()
                            val total: Int = 0
                            databaseref.child("fundraisers").child(randomKey.toString()).child("name")
                                .setValue(name.trim())
                            databaseref.child("fundraisers").child(randomKey.toString()).child("description")
                                .setValue(description.trim())
                            databaseref.child("fundraisers").child(randomKey.toString()).child("goal")
                                .setValue(goal.toInt())
                            databaseref.child("fundraisers").child(randomKey.toString()).child("total")
                                .setValue(total)

                            Toast.makeText(this@AddFundraiser, "Fundraiser Added!", Toast.LENGTH_SHORT).show()

                            val intent = Intent(this@AddFundraiser, Fundraisers::class.java)
                            startActivity(intent)
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
            }
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