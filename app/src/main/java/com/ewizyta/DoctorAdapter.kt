package com.ewizyta

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ewizyta.R

class DoctorAdapter(private val doctorList: List<Doctor>) : RecyclerView.Adapter<DoctorAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_doctor_profile, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val doctor = doctorList[position]
        holder.bind(doctor)
    }

    override fun getItemCount(): Int {
        return doctorList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val doctorImage: ImageView = itemView.findViewById(R.id.doctorImage)
        private val doctorName: TextView = itemView.findViewById(R.id.doctorName)
        private val doctorSpecialization: TextView = itemView.findViewById(R.id.doctorSpecialization)
        private val connectButton: Button = itemView.findViewById(R.id.connectButton)

        fun bind(doctor: Doctor) {
            // Ustaw odpowiednie wartości dla poszczególnych elementów widoku
            doctorName.text = doctor.name
            doctorSpecialization.text = doctor.specialization
            // Dodaj obsługę kliknięcia przycisku connectButton, jeśli jest wymagane
            connectButton.setOnClickListener {
                // Logika obsługi kliknięcia
            }
        }
    }
}