package com.example.WolaNani

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.security.keystore.KeyProperties
import android.util.Base64
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import at.favre.lib.crypto.bcrypt.BCrypt

class MainActivity : AppCompatActivity() {
    val databaseref = FirebaseDatabase.getInstance().getReferenceFromUrl("https://wolanani-53ae1-default-rtdb.firebaseio.com");

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val edtusername = findViewById<View>(R.id.phoneNumber) as TextView
        val edtpassword = findViewById<View>(R.id.password) as TextView
        val loginbtn = findViewById<View>(R.id.loginbtn) as MaterialButton
        val registerbtn = findViewById<TextView>(R.id.RegisterLink) as MaterialButton

        loginbtn.setOnClickListener {
            var phoneNumber: String = edtusername.text.toString()
            var password: String = edtpassword.text.toString()
            if (phoneNumber.isEmpty() || password.isEmpty()) {
                Toast.makeText(this@MainActivity,"Please enter your Phone Number and Password!", Toast.LENGTH_SHORT).show()
            }
            else{
                databaseref.child("users").addListenerForSingleValueEvent(object : ValueEventListener {
                    @RequiresApi(Build.VERSION_CODES.M)
                    override fun onDataChange(@NonNull snapshot: DataSnapshot) {
                        val adminUsername = "123"
                        val adminPassword = "admin"

                        if (snapshot.hasChild(phoneNumber)){

                            val hashedPassword: String = snapshot.child(phoneNumber).child("password").getValue(String::class.java) ?: ""
                            val getPassword : String = snapshot.child(phoneNumber).child("password").getValue(String::class.java) ?: ""
                            val getName = snapshot.child(phoneNumber).child("fullName").getValue(String::class.java)
                            val getPhone = snapshot.child(phoneNumber).child("phoneNumber").getValue(String::class.java)
                            if(verifyPassword(password.trim(), hashedPassword)){
                                Toast.makeText(this@MainActivity,"Successfully Logged in as : $getName.", Toast.LENGTH_SHORT).show()
                                edtusername.text = ""
                                edtpassword.text = ""
                                val intent = Intent(this@MainActivity, Home::class.java)
                                val sharedPref = getSharedPreferences("MY_APP_PREFS", Context.MODE_PRIVATE)
                                val editor = sharedPref.edit()
                                editor.putString("USER_NAME", getName)
                                editor.putString("PHONE", getPhone)
                                editor.apply()
                                startActivity(intent)
                                finish()
                            }
                            else {
                                Toast.makeText(this@MainActivity, "Wrong Password!", Toast.LENGTH_SHORT).show()
                            }
                        }
                        else if(phoneNumber == adminUsername && password == adminPassword){

                            Toast.makeText(this@MainActivity, "Logged in as Admin", Toast.LENGTH_SHORT).show()

                            val intent = Intent(this@MainActivity, adminHome::class.java)
                            startActivity(intent)

                            edtusername.text = ""
                            edtpassword.text = ""
                        }
                        else {
                            Toast.makeText(this@MainActivity, "Wrong Phone Number!", Toast.LENGTH_SHORT).show()
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
            }

        }


        registerbtn.setOnClickListener {
            val intent = Intent(this,Register::class.java)
            startActivity(intent)
        }


    }
    fun verifyPassword(enteredPassword: String, hashedPassword: String): Boolean {
        val result = BCrypt.verifyer().verify(enteredPassword.toCharArray(), hashedPassword)
        return result.verified
    }
    override fun onBackPressed() {

    }
}