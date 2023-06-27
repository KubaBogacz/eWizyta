package com.ewizyta

import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.DatePicker
import android.widget.PopupMenu
import android.widget.TextView
import com.ewizyta.databinding.ActivityDoctorEditProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class EditDoctorProfileActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private lateinit var binding: ActivityDoctorEditProfileBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference : DatabaseReference
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        val currentUser = FirebaseAuth.getInstance().currentUser
        val currentUserUID = currentUser!!.uid

        FirebaseDatabase.getInstance().reference.child("Users").child(currentUserUID)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        databaseReference = dataSnapshot.ref
                        val userData = dataSnapshot.getValue(Doctor::class.java)
                        getUserData(userData)
                    } else {
                        FirebaseDatabase.getInstance().reference.child("Doctors").child(currentUserUID)
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.exists()) {
                                        databaseReference = snapshot.ref
                                        val userData = snapshot.getValue(Doctor::class.java)
                                        getUserData(userData)
                                    }
                                }
                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                }
                            })
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e(ContentValues.TAG, "Database error: ${databaseError.message}")
                }
            })

        Locale.setDefault(Locale("pl", "PL"))

        binding.birthdate.setOnClickListener {
            val locale = Locale("pl", "PL")
            DatePickerDialog(
                this,
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).apply {
                setButton(DatePickerDialog.BUTTON_POSITIVE, "Zatwierdź", this)
                setButton(DatePickerDialog.BUTTON_NEGATIVE, "Anuluj", this)
                context.resources.configuration.setLocale(locale)
                show()
            }
        }

        binding.gender.setOnClickListener { view ->
            val popupMenu = PopupMenu(this, view)
            popupMenu.menuInflater.inflate(R.menu.gender_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.male -> {
                        binding.gender.setText("Mężczyzna")
                        true
                    }
                    R.id.female -> {
                        binding.gender.setText("Kobieta")
                        true
                    }
                    R.id.other -> {
                        binding.gender.setText("Inna")
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }

        binding.buttonEditData.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            val name = binding.name.text.toString()
            val lastName = binding.lastName.text.toString()
            val phone = binding.phone.text.toString()
            val birthDate = binding.birthdate.text.toString()
            val gender = binding.gender.text.toString()
            val specialization = binding.specialization.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty() && lastName.isNotEmpty() && phone.isNotEmpty()
                && birthDate.isNotEmpty() && gender.isNotEmpty() && specialization.isNotEmpty()) {
                if (email.contains('@')) {
                    val updates = mapOf<String, Any>(
                        "name" to name,
                        "lastName" to lastName,
                        "email" to email,
                        "phone" to phone,
                        "password" to password,
                        "birthDate" to birthDate,
                        "gender" to gender,
                        "specialization" to specialization
                    )
                    currentUser.updateEmail(email)
                    currentUser.updatePassword(password)
                    databaseReference.updateChildren(updates)
                    val intent = Intent(this, Home::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        findViewById<TextView>(R.id.birthdate).text = dateFormat.format(calendar.time)
    }

    private fun getUserData(userData: Doctor?) {
        binding.name.text = Editable.Factory.getInstance().newEditable(userData?.name.toString())
        binding.lastName.text = Editable.Factory.getInstance().newEditable(userData?.lastName.toString())
        binding.email.text = Editable.Factory.getInstance().newEditable(userData?.email.toString())
        binding.password.text = Editable.Factory.getInstance().newEditable(userData?.password.toString())
        binding.phone.text = Editable.Factory.getInstance().newEditable(userData?.phone.toString())
        binding.birthdate.text = Editable.Factory.getInstance().newEditable(userData?.birthDate.toString())
        binding.gender.text = Editable.Factory.getInstance().newEditable(userData?.gender.toString())
        binding.specialization.text = Editable.Factory.getInstance().newEditable(userData?.specialization.toString())
    }

}