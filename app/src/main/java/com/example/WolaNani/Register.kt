package com.example.WolaNani

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.security.keystore.KeyProperties
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import javax.crypto.Cipher
import android.security.keystore.KeyGenParameterSpec
import android.util.Base64
import javax.crypto.KeyGenerator
import at.favre.lib.crypto.bcrypt.BCrypt


class Register : AppCompatActivity() {
    val databaseref = FirebaseDatabase.getInstance().getReferenceFromUrl("https://wolanani-53ae1-default-rtdb.firebaseio.com");

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val Email = findViewById<EditText>(R.id.email) as EditText
        val Password = findViewById<EditText>(R.id.password) as EditText
        val FullName = findViewById<EditText>(R.id.fullname) as EditText
        val PhoneNumber = findViewById<EditText>(R.id.phonenumber) as EditText
        val loginbtn = findViewById<View>(R.id.LoginLink) as MaterialButton
        val Registerbtn = findViewById<View>(R.id.registerbtn) as MaterialButton

        loginbtn.setOnClickListener {
            val Intent = Intent(this,MainActivity::class.java)
            startActivity(Intent)
        }

        Registerbtn.setOnClickListener {
            var emailtxt: String = Email.text.toString()
            var passwordtxt: String = Password.text.toString()
            var fullnametxt: String = FullName.text.toString()
            var phonenumbertxt: String = PhoneNumber.text.toString()

            if (emailtxt.isEmpty() || passwordtxt.isEmpty() || fullnametxt.isEmpty() || phonenumbertxt.isEmpty()) {
                Toast.makeText(this@Register, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else if (!isValidEmail(emailtxt)) {
                Toast.makeText(this@Register, "Invalid email format", Toast.LENGTH_SHORT).show()
            } else if (!isValidPhoneNumber(phonenumbertxt)) {
                Toast.makeText(this@Register, "Invalid phone number", Toast.LENGTH_SHORT).show()
            } else {
                if (!isInvalidPhoneNumber(phonenumbertxt)) {
                    databaseref.child("users").addListenerForSingleValueEvent(object : ValueEventListener {
                        @RequiresApi(Build.VERSION_CODES.M)
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.hasChild(phonenumbertxt)) {
                                Toast.makeText(this@Register, "Phone Number has already been Registered", Toast.LENGTH_SHORT).show()
                            } else {
                                databaseref.child("users").child(phonenumbertxt).child("phoneNumber").setValue(phonenumbertxt.trim())
                                databaseref.child("users").child(phonenumbertxt).child("fullName").setValue(fullnametxt.trim())
                                databaseref.child("users").child(phonenumbertxt).child("email").setValue(emailtxt.trim())
                                databaseref.child("users").child(phonenumbertxt).child("password").setValue(encryptString(passwordtxt.trim()))

                                Toast.makeText(this@Register, "User Registered Successfully", Toast.LENGTH_SHORT).show()

                                val intent = Intent(this@Register, MainActivity::class.java)
                                startActivity(intent)
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {
                        }
                    })
                } else {
                    Toast.makeText(this@Register, "Invalid phone number", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    fun encryptString(password: String): String {
        val hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray())
        return hashedPassword
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
        val phoneRegex = Regex("^[0-9]{10}$")
        return phoneRegex.matches(phoneNumber)
    }
    private fun isInvalidPhoneNumber(phoneNumber: String): Boolean {
        return phoneNumber == "1234567890"
    }
    override fun onBackPressed() {

    }
}