package com.aman.hungryhub

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.aman.hungryhub.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {


    private lateinit var auth:FirebaseAuth
    private  val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)


        binding.addMenu.setOnClickListener {
            val intent = Intent(this, AddItemActivity::class.java)
            startActivity(intent)
        }

        binding.allItemMenu.setOnClickListener {
            val intent = Intent(this, AllItemActivity::class.java)
            startActivity(intent)
        }
        binding.orderDispatched.setOnClickListener {
            val intent = Intent(this, OutForDeliveryActivity::class.java)
            startActivity(intent)
        }
        binding.profile.setOnClickListener {
            val intent = Intent(this, AdminProfileActivity::class.java)
            startActivity(intent)
        }
        binding.createNewUser.setOnClickListener {
            val intent = Intent(this, CreateUserActivity::class.java)
            startActivity(intent)
        }
        binding.pendingOrderTextView.setOnClickListener {
            val intent = Intent(this, PendingOrderActivity::class.java)
            startActivity(intent)
        }


        binding.logOut.setOnClickListener{
            showLogoutConfirmationDialog()
        }



    }
    private fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Logout")
        builder.setMessage("Are you sure you want to logout?")
        builder.setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int ->
            // User clicked Yes, perform logout
            auth.signOut()
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
            dialogInterface.dismiss()
        }
        builder.setNegativeButton("No") { dialogInterface: DialogInterface, i: Int ->
            // User clicked No, dismiss dialog
            dialogInterface.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }
}