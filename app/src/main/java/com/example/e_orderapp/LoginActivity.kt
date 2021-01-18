package com.example.e_orderapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginButton : Button = findViewById(R.id.login_button)
        val signinButton : Button = findViewById(R.id.login_signup_button)
        val etEmail : EditText = findViewById(R.id.login_image_email_et)
        val etPassword : EditText = findViewById(R.id.login_pw_et)
        auth = FirebaseAuth.getInstance()

        loginButton.setOnClickListener {
            if(etEmail.text.trim().toString().isEmpty()){
                etEmail.setError("Email required!")
                etEmail.requestFocus()
            }else if(etPassword.text.trim().toString().isEmpty()){
                etEmail.setError("Password required!")
                etEmail.requestFocus()
            }else{
                logIn(etEmail.text.trim().toString(),etPassword.text.trim().toString())
            }

        }

        signinButton.setOnClickListener {
            val intent = Intent(this,SignupActivity::class.java)
            startActivity(intent)
        }

    }

    private fun logIn(email:String,password:String){
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener{task->
                if(task.isSuccessful){
                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)
                }else{
                    Toast.makeText(this, "Error"+task.exception, Toast.LENGTH_SHORT).show()
                }
            }
    }
}