package com.example.sampleinapppurchases

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.soloftech.inapplibraryv4.BillingProcessor
import com.soloftech.inapplibraryv4.OnBillingProcessorListener

class MainActivity : AppCompatActivity(), OnBillingProcessorListener {
    var billingProcessor: BillingProcessor?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        billingProcessor=BillingProcessor(this,"licency_key","android.test.purchased", this)
    }

    override fun onBillingProcessorConnected() {
        AlertDialog.Builder(this)
            .setTitle("Purchase Successful")
            .setMessage("restart your app to apply settings for inapp purchases")
            .setNegativeButton("Restart") { dialogInterface: DialogInterface, i: Int ->
                Toast.makeText(this@MainActivity,"Restart Successful", Toast.LENGTH_SHORT).show()
            }
            .create()
            .show()
    }

    override fun onBillingProcessorConnectionFailed(responseCode: Int, message: String?) {
    }

    override fun onPurchasedSuccessful() {
    }

    override fun onPurchasedFailed() {
    }

    override fun onBillingProcessorDisconnected() {
    }

    fun OnPurchase(view: View) {
        billingProcessor!!.purchase()
    }
}