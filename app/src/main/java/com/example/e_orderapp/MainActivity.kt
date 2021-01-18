package com.example.e_orderapp

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.e_orderapp.`interface`.Communicator
import com.example.e_orderapp.fragment.*
import com.example.e_orderapp.model.Order
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity(), Communicator {

    private val homeFragment = HomeFragment()
    private val orderFragment = OrderFragment()
    private val productFragment = ProductFragment()
    private val addOrderFragment = AddOrderFragment()
    private val customerFragment = CustomerFragment()
    private val salesFragment = SalesFragment()
    private val profileFragment = ProfileFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val msg = intent.getBooleanExtra("editActivity", false)

        if(msg){
            replaceFragment(orderFragment)
        }else{
            replaceFragment(homeFragment)
        }

        
        val btmNavigation : BottomNavigationView = findViewById(R.id.bottom_nav_bar)

        btmNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.ic_home -> replaceFragment(homeFragment)
                R.id.ic_order -> replaceFragment(orderFragment)
                R.id.ic_product -> replaceFragment(productFragment)
            }
            true
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        if (fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fl_container, fragment)
            transaction.commit()
        }
    }

    override fun navigateToAddFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fl_container, addOrderFragment).addToBackStack(null)
        transaction.commit()
    }

    override fun navigateToLoginActivity(auth: FirebaseAuth) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.custom_dialog_delete_order)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.setCancelable(true)
        val dPrompt = dialog.findViewById<TextView>(R.id.di_title)
        dPrompt.text = "Are you sure to logout ?"
        val dConfirmBtn = dialog.findViewById<Button>(R.id.di_confirm_delete_order)
        dConfirmBtn.text = "Yes"
        val dCancelBtn = dialog.findViewById<Button>(R.id.di_cancel_delete_order)
        dConfirmBtn.setOnClickListener {
            auth.signOut()
            Toast.makeText(this, "Logout successful.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            dialog.dismiss()
        }

        dCancelBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()


    }

    override fun navigateToDetailsActivity(currentOrder: Order) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra("Order", currentOrder)
        startActivity(intent)
    }

    override fun navigateToOrderFragment(item: Int) {
        if(item==1){
            Toast.makeText(this, "Add order successful!", Toast.LENGTH_SHORT).show()
            supportFragmentManager.popBackStack()
        }else{
            supportFragmentManager.popBackStack()
        }

    }

    override fun navigateToCustomerFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fl_container, customerFragment).addToBackStack(null)
        transaction.commit()
    }

    override fun navigateToSalesFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fl_container, salesFragment).addToBackStack(null)
        transaction.commit()
    }

    override fun navigateToProfileFragment(uID: String) {
        val transaction = supportFragmentManager.beginTransaction()
        val bundle = Bundle()
        bundle.putString("userID", uID)
        profileFragment.arguments = bundle
        transaction.replace(R.id.fl_container, profileFragment).addToBackStack(null)
        transaction.commit()
    }

    override fun navigateToLongClick() {
        Toast.makeText(this, "onLongClick is clicked!", Toast.LENGTH_SHORT).show()
    }

    override fun navigateToEditProductActivity() {
        Toast.makeText(this, "Edit clicked!", Toast.LENGTH_SHORT).show()
    }

    override fun createCSVFile() {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT,"This is a message!")
        startActivity(Intent.createChooser(shareIntent,"Share..."))

    }
        //generate data
//        val data = StringBuilder()
//        data.append("Time,Distance")
//        for (i in 0..4) {
//            data.append("Saya,suka")
//        }

//        try {
//            //saving the file into device
//            val out: FileOutputStream = openFileOutput("data.csv", Context.MODE_PRIVATE);
//            out.write(data.toString().toByteArray())
//            out.close()



//            //exporting
//            val context:Context = applicationContext
//            val fileLocation = File(filesDir, "data.csv")
//            val path:Uri = FileProvider.getUriForFile(context, "com.example.e_orderapp.fileprovider", fileLocation)
//            val fileIntent = Intent(Intent.ACTION_SEND)
//            fileIntent.type = "text/csv"
//            fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Data")
//            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//            fileIntent.putExtra(Intent.EXTRA_STREAM, path)
//            startActivity(Intent.createChooser(fileIntent, "Send mail"))

//        }catch (e: Exception){
//            e.printStackTrace();
//        }
//
//    }
}
