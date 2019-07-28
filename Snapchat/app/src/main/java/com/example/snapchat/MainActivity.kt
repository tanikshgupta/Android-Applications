package com.example.snapchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    var emailEditText: EditText? = null
    var passwordEditText: EditText? = null
    val mAuth = FirebaseAuth.getInstance();


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setTitle("Snapchat")

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)

        if (mAuth.currentUser!=null) {
            loginFunction()
        }

    }

    fun goFunction(view: View) {

        mAuth.signInWithEmailAndPassword(emailEditText?.text.toString(), passwordEditText?.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    loginFunction()
                } else {
                    // If sign in fails, display a message to the user.
                    mAuth.createUserWithEmailAndPassword(emailEditText?.text.toString(), passwordEditText?.text.toString()).addOnCompleteListener(this) {task ->
                        if (task.isSuccessful) {
                            FirebaseDatabase.getInstance().getReference().child("users").child(task.result?.user!!.uid).child("email").setValue(emailEditText?.text.toString())
                            loginFunction();
                        } else {
                            Toast.makeText(this, "Login Failed!",Toast.LENGTH_SHORT).show();
                        }

                    }
                }


            }

    }

    fun loginFunction() {
        //Move to next activity

        val intent = Intent(this,SnapsActivity::class.java)
        startActivity(intent)
    }
}
