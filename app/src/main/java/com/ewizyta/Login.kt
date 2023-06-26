package com.ewizyta


import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ewizyta.databinding.ActivityLoginBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()

        binding.doctorRegisterButton.setOnClickListener{
            val intent = Intent(this, RegisterDoctor::class.java)
            startActivity(intent)
        }

        binding.button2.setOnClickListener{
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                if (email.contains('@')) {
                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener{
                        if (it.isSuccessful) {
                            val userId = it.result?.user?.uid
                            if (userId != null) {
                                firebaseDatabase.reference.addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        val isDoctor = snapshot.child("Doctors").child(userId).exists()
                                        if (isDoctor) {
                                            FirebaseMessaging.getInstance().token.addOnCompleteListener(
                                                OnCompleteListener { task ->
                                                    if (!task.isSuccessful) {
                                                        Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                                                        return@OnCompleteListener
                                                    }
                                                    val token = task.result
                                                    val uid = FirebaseAuth.getInstance().currentUser?.uid
                                                    val userTokenRef = FirebaseDatabase.getInstance().getReference("Doctors/$uid/fcmToken")
                                                    Log.d(TAG, "FCM Token: $token")
                                                })
                                            val intent = Intent(this@Login, DoctorHome::class.java)
                                            startActivity(intent)
                                        } else {
                                            FirebaseMessaging.getInstance().token.addOnCompleteListener(
                                                OnCompleteListener { task ->
                                                    if (!task.isSuccessful) {
                                                        Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                                                        return@OnCompleteListener
                                                    }
                                                    val token = task.result
                                                    val uid = FirebaseAuth.getInstance().currentUser?.uid
                                                    val userTokenRef = FirebaseDatabase.getInstance().getReference("Users/$uid/fcmToken")
                                                    userTokenRef.setValue(token)
                                                    Log.d(TAG, "FCM Token: $token")
                                                })
                                            val intent = Intent(this@Login, Home::class.java)
                                            startActivity(intent)
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        Toast.makeText(this@Login, error.message, Toast.LENGTH_SHORT).show()
                                    }
                                })
                            }

                        } else {
                            Toast.makeText(this, "Niepoprawne dane logowania!", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Niewłaściwy e-mail!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Uzupełnij wszystkie pola!", Toast.LENGTH_SHORT).show()
            }

        }
    }

    fun registerFromLogin(view : View) {
        val intent = Intent(this, Register::class.java)
        startActivity(intent)
    }

}