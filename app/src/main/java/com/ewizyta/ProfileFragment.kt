package com.ewizyta

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ewizyta.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.R
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ProfileFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private var userType: String = "null"

    private lateinit var binding: FragmentProfileBinding
    private lateinit var database: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var profilePictureRef: StorageReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        val currentUser = FirebaseAuth.getInstance().currentUser
        val currentUserUid = currentUser!!.uid
        FirebaseDatabase.getInstance().reference.child("Users").child(currentUserUid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        database = snapshot.ref
                        userType = "user"
                        val userData = snapshot.getValue(User::class.java)
                        updateProfileUI(userData)

                        val fileName = "profileImage_$currentUserUid.jpg"
                        profilePictureRef = FirebaseStorage.getInstance().getReference("profileImages/$fileName")
                        profilePictureRef.downloadUrl.addOnSuccessListener { uri ->
                            Picasso.get().load(uri).into(binding.recImage)
                        }
                    } else {
                        FirebaseDatabase.getInstance().reference.child("Couriers").child(currentUserUid)
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.exists()) {
                                        database = snapshot.ref
                                        userType = "courier"
                                        val userData = snapshot.getValue(User::class.java)
                                        updateProfileUI(userData)

                                        val fileName = "profileImage_$currentUserUid.jpg"
                                        profilePictureRef = FirebaseStorage.getInstance().getReference("profileImages/$fileName")
                                        profilePictureRef.downloadUrl.addOnSuccessListener { uri ->
                                            Picasso.get().load(uri).into(binding.recImage)
                                        }
                                    }
                                }
                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                }
                            })
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        binding.addPictureBtn.setOnClickListener {
            val openGalleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(openGalleryIntent, 1000)

        }

        binding.buttonLogOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(activity, Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        binding.buttonGoToEdit.setOnClickListener {
            val intent = Intent(activity, EditProfileActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        binding.buttonDeleteAccount.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Usuń konto")
                .setMessage("Czy na pewno chcesz usunąć konto? Ta akcja nie może zostać cofnięta.")
                .setPositiveButton("Usuń") { dialog, which ->
                    val user = FirebaseAuth.getInstance().currentUser
                    user?.let {
                        val dbRef = FirebaseDatabase.getInstance().getReference("Users").child(it.uid)
                        dbRef.removeValue().addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                it.delete()
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Log.d(TAG, "User account deleted.")
                                            val intent = Intent(activity, Login::class.java)
                                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                            startActivity(intent)
                                        } else {
                                            Log.w(TAG, "deleteUser:failure", task.exception)
                                            Toast.makeText(requireContext(), "Failed to delete account.",
                                                Toast.LENGTH_SHORT).show()
                                        }
                                    }
                            } else {
                                Log.w(TAG, "deleteUserData:failure", task.exception)
                                Toast.makeText(requireContext(), "Failed to delete user data.",
                                    Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
                .setNegativeButton("Anuluj", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()
        }
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 1000 ){
            if (resultCode == Activity.RESULT_OK) {
                val imageUri = data!!.data
//                profileImage.setImageURI(imageUri)
                if (imageUri != null) {
                    val user = FirebaseAuth.getInstance().currentUser
                    val dbRef = FirebaseDatabase.getInstance().getReference("Users").child(user!!.uid)
                    val currentUserUid = user!!.uid
                    val fileName = "profileImage_$currentUserUid.jpg"
                    storageReference = FirebaseStorage.getInstance().getReference("profileImages/$fileName")
                    storageReference.putFile(imageUri).addOnSuccessListener{
                        Toast.makeText(requireContext(), "Poprawnie dodano zdjęcie!", Toast.LENGTH_SHORT).show()

                        storageReference.downloadUrl.addOnSuccessListener { uri ->
                            Picasso.get().load(uri).into(binding.recImage)
                        }

                    }.addOnFailureListener{
                        Toast.makeText(requireContext(), "Coś poszło nie tak!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun updateProfileUI(userData: User?) {
        var data = "Imię: " + userData?.name.toString() + "\n"
        data += "Nazwisko: " + userData?.lastName.toString()  + "\n"
        data += "E-mail: " + userData?.email.toString()  + "\n"
        data += "Telefon: " + userData?.phone.toString() + "\n"
        data += "Data urodzenia: " + userData?.birthDate.toString() + "\n"
        data += "Płeć: " + userData?.gender.toString() + "\n"
        binding.userData.text = data

    }
}
