package com.ewizyta

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ewizyta.R.layout.item_doctor_profile
import com.ewizyta.Doctor

class DoctorAdapter(private val doctors: List<Doctor>) : RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(item_doctor_profile, parent, false)
        return DoctorViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        val currentDoctor = doctors[position]
        holder.bind(currentDoctor)
    }

    override fun getItemCount(): Int {
        return doctors.size
    }

    inner class DoctorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.doctorName)
        private val specializationTextView: TextView = itemView.findViewById(R.id.doctorSpecialization)

        @SuppressLint("SetTextI18n")
        fun bind(doctor: Doctor) {
            nameTextView.text = "${doctor.name} ${doctor.surname}"
            specializationTextView.text = doctor.specialization
        }
    }
}
