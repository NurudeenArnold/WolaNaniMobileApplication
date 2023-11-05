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
            } else {
                // Proceed with user registration
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
                        // Handle database error
                    }
                })
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.M)
    fun encryptString(text: String): String {
        val dataToEncrypt = text.toByteArray(Charsets.UTF_8)

        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        val keySpec = KeyGenParameterSpec.Builder(
            "myKeyAlias",
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        ).run {
            setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
            build()
        }
        keyGenerator.init(keySpec)
        val key = keyGenerator.generateKey()

        val cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7)

        cipher.init(Cipher.ENCRYPT_MODE, key)
        val iv = cipher.iv
        val encryptedData = cipher.doFinal(dataToEncrypt)

        val combinedData = ByteArray(iv.size + encryptedData.size)
        System.arraycopy(iv, 0, combinedData, 0, iv.size)
        System.arraycopy(encryptedData, 0, combinedData, iv.size, encryptedData.size)

        return Base64.encodeToString(combinedData, Base64.DEFAULT)
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    override fun onBackPressed() {

    }
}