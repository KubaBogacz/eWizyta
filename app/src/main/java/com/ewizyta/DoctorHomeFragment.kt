package com.ewizyta

import UserAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class DoctorHomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter
    private lateinit var userList: List<User>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home_doctor, container, false)

        // Inicjalizacja listy lekarzy (przykładowe dane)
        userList = createUserList()

        // Inicjalizacja RecyclerView
        recyclerView = view.findViewById(R.id.patientRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Inicjalizacja adaptera RecyclerView i przekazanie listy lekarzy
        userAdapter = UserAdapter(userList)
        recyclerView.adapter = userAdapter

        return view
    }

    private fun createUserList(): List<User> {
        val userList = mutableListOf<User>()

        // Dodawanie przykładowych lekarzy do listy
        userList.add(User("John", "Doe", "a@wp.pl", "123", "123456789", "13.12.2001", "Mężczyzna"))
        // Dodaj inne przykładowe lekarzy według potrzeb

        return userList
    }
}
