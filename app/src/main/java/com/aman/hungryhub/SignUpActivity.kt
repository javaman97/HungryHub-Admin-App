package com.aman.hungryhub


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aman.hungryhub.databinding.ActivitySignUpBinding
import com.aman.hungryhub.model.UserModal
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var userName: String
    private lateinit var nameOfRestaurant: String
    private lateinit var database: DatabaseReference

    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // intilization firebase Auth

        auth = Firebase.auth

        //inilization Firebase database

        database = Firebase.database.reference


        binding.CreateAccount.setOnClickListener {
            // det text from edit Text
            email = binding.emailOrPhone.text.toString().trim()
            userName = binding.nameOfOwner.text.toString().trim()
            nameOfRestaurant = binding.resturentName.text.toString().trim()
            password = binding.editTextPass.text.toString().trim()

            if(userName.isBlank() || nameOfRestaurant.isBlank()|| email.isBlank() || password.isBlank())
            {
                Toast.makeText(this,"Please fill all details", Toast.LENGTH_SHORT).show()
            } else{
                createAccount(email,password)
            }

        }
        binding.alreadyHaveAccount.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        val locationList = arrayOf("Makkawala", "Sailangaon", "Raipur")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, locationList)
        val autoCompleteTextView = binding.listOfLocation
        autoCompleteTextView.setAdapter(adapter)

    }

    private fun createAccount(email: String, password: String) {

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->

            if(task.isSuccessful){
                Toast.makeText(this," Account created Successfully", Toast.LENGTH_SHORT).show()
                saveUserData()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

            else{

                Toast.makeText(this,"Account Creation Failed", Toast.LENGTH_SHORT).show()
                Log.d("Account", "createAccount: Failure", task.exception)
            }
        }
    }

    //save data into database
    private fun saveUserData() {
        // det text from edit Text
        email = binding.emailOrPhone.text.toString().trim()
        userName = binding.nameOfOwner.text.toString().trim()
        nameOfRestaurant = binding.resturentName.text.toString().trim()
        password = binding.editTextPass.text.toString().trim()

        val user = UserModal(userName,nameOfRestaurant,email,password)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        //save usr data to firebase database
        database.child("user").child(userId).setValue(user)
    }
}