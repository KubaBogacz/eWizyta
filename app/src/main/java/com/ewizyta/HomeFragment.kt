package com.ewizyta

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class HomeFragment : Fragment() {

  private lateinit var recyclerView: RecyclerView
  private lateinit var doctorAdapter: DoctorAdapter
  private lateinit var doctorList: MutableList<Doctor>
  private lateinit var databaseReference: DatabaseReference
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.fragment_home, container, false)

    // Inicjalizacja RecyclerView
    recyclerView = view.findViewById(R.id.doctorRecyclerView)
    recyclerView.layoutManager = LinearLayoutManager(requireContext())

    // Inicjalizacja adaptera RecyclerView i przekazanie listy lekarzy
    doctorList = mutableListOf()
    doctorAdapter = DoctorAdapter(doctorList)
    recyclerView.adapter = doctorAdapter

    // Inicjalizacja referencji do bazy danych Firebase Realtime Database
    databaseReference = FirebaseDatabase.getInstance().reference.child("Doctors")

    // Pobierz listę użytkowników z Firebase Realtime Database
    getDoctorListFromFirebaseDatabase()

    return view
  }

  private fun getDoctorListFromFirebaseDatabase() {
    databaseReference.addValueEventListener(object : ValueEventListener {
      override fun onDataChange(snapshot: DataSnapshot) {
        doctorList.clear()

        for (userSnapshot in snapshot.children) {
          val name = userSnapshot.child("name").getValue(String::class.java)
          val lastName = userSnapshot.child("lastName").getValue(String::class.java)
          val email = userSnapshot.child("email").getValue(String::class.java)
          val password = userSnapshot.child("password").getValue(String::class.java)
          val phone = userSnapshot.child("phone").getValue(String::class.java)
          val birthDate = userSnapshot.child("birthDate").getValue(String::class.java)
          val gender = userSnapshot.child("gender").getValue(String::class.java)

          val doctor = Doctor(name, lastName, email, password, phone, birthDate, gender)
          doctorList.add(doctor)
        }

        // Aktualizuj listę użytkowników w RecyclerView
        doctorAdapter.notifyDataSetChanged()
      }

      override fun onCancelled(error: DatabaseError) {
        // Obsłuż błąd
      }
    })
  }

}