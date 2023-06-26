package com.ewizyta

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeFragment : Fragment() {

  private lateinit var recyclerView: RecyclerView
  private lateinit var doctorAdapter: DoctorAdapter
  private lateinit var doctorList: List<Doctor>

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.fragment_home, container, false)

    // Inicjalizacja RecyclerView
    recyclerView = view.findViewById(R.id.doctorRecyclerView)
    recyclerView.layoutManager = LinearLayoutManager(requireContext())

    // Inicjalizacja adaptera RecyclerView i przekazanie listy lekarzy
    doctorAdapter = DoctorAdapter(doctorList)
    recyclerView.adapter = doctorAdapter

    return view
  }
}