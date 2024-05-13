package com.aman.hungryhub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.aman.hungryhub.databinding.ActivityAdminProfileBinding
import com.aman.hungryhub.model.UserModal
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class AdminProfileActivity : AppCompatActivity() {

    private val binding : ActivityAdminProfileBinding by lazy {
        ActivityAdminProfileBinding.inflate(layoutInflater)
    }
    private lateinit var auth:FirebaseAuth
    private lateinit var database:FirebaseDatabase
    private lateinit var adminRef:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        adminRef = database.reference.child("user")

        binding.backButton.setOnClickListener {
            finish()
        }
        binding.saveInfoButton.setOnClickListener {
            updateUserData()
        }

        binding.name.isEnabled = false
        binding.address.isEnabled = false
        binding.email.isEnabled = false
        binding.phone.isEnabled = false
        binding.password.isEnabled =false
        binding.saveInfoButton.isEnabled = false
        var isEnable = false
        binding.editButton.setOnClickListener {
            isEnable =! isEnable
            binding.name.isEnabled = isEnable
            binding.address.isEnabled = isEnable
            binding.email.isEnabled = isEnable
            binding.phone.isEnabled = isEnable
            binding.password.isEnabled =isEnable
            if(isEnable){
                binding.name.requestFocus()
            }
            binding.saveInfoButton.isEnabled = isEnable

        }
        retriveUserData()
    }

    private fun updateUserData() {
        var updateName = binding.name.text.toString()
        var updateEmail =binding.email.text.toString()
        var updateAddress  =binding.address.text.toString()
       var updatePhone = binding.phone.text.toString()
       var updatePassword= binding.password.text.toString()
        var currentUserUid = auth.currentUser?.uid
        if(currentUserUid!=null){
            val userRef = adminRef.child(currentUserUid)
            userRef.child("name").setValue(updateName)
            userRef.child("email").setValue(updateEmail)
            userRef.child("password").setValue(updatePassword)
            userRef.child("address").setValue(updateAddress)
            userRef.child("phone").setValue(updatePhone)

            Toast.makeText(this,"Profile Updated Sucessfully", Toast.LENGTH_SHORT).show()
            auth.currentUser?.updateEmail(updateEmail)
            auth.currentUser?.updatePassword(updatePassword)
        } else {
            Toast.makeText(this,"Profile Updated Failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun retriveUserData() {
        val currentUser = auth.currentUser?.uid
     if(currentUser!=null){
         val userRef = adminRef.child(currentUser)
         userRef.addListenerForSingleValueEvent(object :ValueEventListener{
             override fun onDataChange(snapshot: DataSnapshot) {
                 if(snapshot.exists()){
                     var ownerName = snapshot.child("name").getValue()
                     var email = snapshot.child("email").getValue()
                     var password = snapshot.child("password").getValue()
                     var address = snapshot.child("address").getValue()
                     var phone = snapshot.child("phone").getValue()

                     setDataToTextView(ownerName,email,password,address,phone)
                 }
             }

             override fun onCancelled(error: DatabaseError) {
                 TODO("Not yet implemented")
             }

         })
     }

    }

    private fun setDataToTextView(ownerName: Any?, email: Any?, password: Any?, address: Any?, phone: Any?) {

        binding.name.setText(ownerName.toString())
        binding.email.setText(email.toString())
        binding.address.setText(address.toString())
        binding.phone.setText(phone.toString())
        binding.password.setText(password.toString())


    }


}