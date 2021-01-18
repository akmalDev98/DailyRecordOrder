package com.example.e_orderapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.text.buildSpannedString
import com.example.e_orderapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignupActivity : AppCompatActivity() {
    
    private lateinit var auth : FirebaseAuth
    private val mData = FirebaseDatabase.getInstance().reference.child("User")
    
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val signInButton : Button = findViewById(R.id.signin_button)
        val signInEmail : EditText = findViewById(R.id.signin_email_et)
        val signInPassword : EditText = findViewById(R.id.signin_password_et)
        val signInUsername : EditText = findViewById(R.id.signin_username_et)
        val signInBusinessName : EditText = findViewById(R.id.signin_businessname_et)
        auth = FirebaseAuth.getInstance()


        signInButton.setOnClickListener {


            if(signInEmail.text.trim().toString().isEmpty()){
                signInEmail.setError("Email is required!")
                signInEmail.requestFocus()
            } else if(signInPassword.text.trim().toString().isEmpty()){
                signInPassword.setError("Password is required!")
                signInPassword.requestFocus()
            } else if(signInUsername.text.trim().toString().isEmpty()){
                signInUsername.setError("Username is required!")
                signInUsername.requestFocus()
            } else if(signInBusinessName.text.trim().toString().isEmpty()){
                signInBusinessName.setError("Businessname is required")
                signInBusinessName.requestFocus()
            } else{
                createNewUser(
                    signInEmail.text.trim().toString(),
                    signInPassword.text.trim().toString(),
                    signInUsername.text.trim().toString(),
                    signInBusinessName.text.trim().toString()
                )
            }

        }


    }
    
    private fun createNewUser(email:String,password:String,userName:String,businessName:String){
        var uID : String = ""
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){task ->
                if(task.isSuccessful){
                    auth.signInWithEmailAndPassword(email,password)
                    uID = auth.currentUser!!.uid
                    val newKey = mData.push().key
                    val newUser = User(newKey!!,uID,email,password,userName, businessName)
                    mData.child(newKey!!).setValue(newUser)
                    Toast.makeText(this, "Sign In Successful!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)
                }else{
                    Toast.makeText(this, "Error"+task.exception, Toast.LENGTH_SHORT).show()
                }
            }


    }
    
}