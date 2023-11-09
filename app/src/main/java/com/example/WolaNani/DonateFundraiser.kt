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
import com.paypal.checkout.PayPalCheckout
import com.paypal.checkout.approve.OnApprove
import com.paypal.checkout.cancel.OnCancel
import com.paypal.checkout.config.CheckoutConfig
import com.paypal.checkout.config.Environment
import com.paypal.checkout.config.SettingsConfig
import com.paypal.checkout.createorder.CreateOrder
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.OrderIntent
import com.paypal.checkout.createorder.UserAction
import com.paypal.checkout.order.Amount
import com.paypal.checkout.order.AppContext
import com.paypal.checkout.order.OrderRequest
import com.paypal.checkout.order.PurchaseUnit


class DonateFundraiser : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    private val PAYPAL_REQUEST_CODE = 1234
    private var selectedFundraiserName: String? = null
    private var totalEntered: Double? = null
    private val PAYPAL_CLIENT_ID = "AZfrrZHDAOrQnok5caVlfk0KZcrFfbJoNx59JXgIH9PbYS0DI2npINfwiZDyT26KojJsz_D5aZadBT1g"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donate_fundraiser)

        val databaseReference = FirebaseDatabase.getInstance().getReference("fundraisers")
        val spinner = findViewById<Spinner>(R.id.spinner)
        val submit = findViewById<MaterialButton>(R.id.submitbtn)
        val amount = findViewById<TextView>(R.id.Amount)
        val returnUrl = "com.example.myloginapp://paypalpay"

        val config = CheckoutConfig(
            application = application,
            clientId = PAYPAL_CLIENT_ID,
            environment = Environment.SANDBOX,
            returnUrl = returnUrl,
            currencyCode = CurrencyCode.USD,
            userAction = UserAction.PAY_NOW,
            settingsConfig = SettingsConfig(
                loggingEnabled = true
            )
        )
        PayPalCheckout.setConfig(config)

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
                totalEntered = totalEnteredStr.toDoubleOrNull()

                if (totalEntered != null) {
                    val createOrder = CreateOrder { createOrderActions ->
                        val orderRequest = OrderRequest(
                            intent = OrderIntent.CAPTURE,
                            appContext = AppContext(userAction = UserAction.PAY_NOW),
                            purchaseUnitList = listOf(
                                PurchaseUnit(
                                    amount = Amount(currencyCode = CurrencyCode.USD, value = totalEntered.toString())
                                )
                            )
                        )
                        createOrderActions.create(orderRequest)
                    }

                    val onApprove = OnApprove { approval ->
                        approval.orderActions.capture { captureOrderResult ->
                            val databaseReference = FirebaseDatabase.getInstance().getReference("fundraisers")
                            val selectedFundraiserRef = databaseReference.child(selectedFundraiserName!!)

                            selectedFundraiserRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    val currentTotal = dataSnapshot.child("total").getValue(Double::class.java) ?: 0.0
                                    val newTotal = currentTotal + totalEntered!!

                                    selectedFundraiserRef.child("total").setValue(newTotal)
                                }

                                override fun onCancelled(databaseError: DatabaseError) {
                                }
                            })
                            Toast.makeText(this@DonateFundraiser, "Donation completed", Toast.LENGTH_SHORT).show()
                            amount.text = null
                        }
                    }
                    val onCancel = OnCancel {
                        Toast.makeText(this@DonateFundraiser, "Donation canceled", Toast.LENGTH_SHORT).show()
                    }
                    PayPalCheckout.start(createOrder, onApprove, null, onCancel)
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