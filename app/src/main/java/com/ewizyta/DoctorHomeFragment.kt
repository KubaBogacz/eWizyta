package com.ewizyta

import UserAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class DoctorHomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter
    private lateinit var userList: MutableList<User>
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home_doctor, container, false)

        // Inicjalizacja RecyclerView
        recyclerView = view.findViewById(R.id.patientRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Inicjalizacja adaptera RecyclerView i pustej listy użytkowników
        userList = mutableListOf()
        userAdapter = UserAdapter(userList)
        recyclerView.adapter = userAdapter

        // Inicjalizacja referencji do bazy danych Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().reference.child("Users")

        // Pobierz listę użytkowników z Firebase Realtime Database
        getUserListFromFirebaseDatabase()

        return view
    }

    private fun getUserListFromFirebaseDatabase() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()

                for (userSnapshot in snapshot.children) {
                    val name = userSnapshot.child("name").getValue(String::class.java)
                    val lastName = userSnapshot.child("lastName").getValue(String::class.java)
                    val email = userSnapshot.child("email").getValue(String::class.java)
                    val password = userSnapshot.child("password").getValue(String::class.java)
                    val phone = userSnapshot.child("phone").getValue(String::class.java)
                    val birthDate = userSnapshot.child("birthDate").getValue(String::class.java)
                    val gender = userSnapshot.child("gender").getValue(String::class.java)

                    val user = User(name, lastName, email, password, phone, birthDate, gender)
                    userList.add(user)
                }

                // Aktualizuj listę użytkowników w RecyclerView
                userAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Obsłuż błąd
            }
        })
    }
}

