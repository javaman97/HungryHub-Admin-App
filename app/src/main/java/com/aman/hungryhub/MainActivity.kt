package com.aman.hungryhub

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.aman.hungryhub.databinding.ActivityMainBinding
import com.aman.hungryhub.model.OrderDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MainActivity : AppCompatActivity() {


    private lateinit var auth:FirebaseAuth
    private lateinit var database:FirebaseDatabase
    private lateinit var completedOrderReference:DatabaseReference

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

        pendingOrders()

        completedOrders()

        wholeTimeEarning()

        binding.logOut.setOnClickListener{
            auth.signOut()
            finish()
        }



    }

    private fun wholeTimeEarning() {
    val listOfTotalPay = mutableListOf<Int>()
    completedOrderReference = FirebaseDatabase.getInstance().reference.child("CompletedOrderDetails")
        completedOrderReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               for(orderSnapshot in snapshot.children){
                   var completeOrder = orderSnapshot.getValue(OrderDetails::class.java)

                   completeOrder?.totalPrice?.replace("$","")?.toIntOrNull()
                       ?.let{
                           i->
                           listOfTotalPay.add(i)
                       }
               }
                binding.totalEarning.text = listOfTotalPay.sum().toString()+"$"
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun completedOrders() {
        val completedOrderRef = database.reference.child("CompletedOrderDetails")
        var completedItemCount = 0
       completedOrderRef.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                completedItemCount = snapshot.childrenCount.toInt()
                binding.completeOrder.text = completedItemCount.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun pendingOrders() {
        database= FirebaseDatabase.getInstance()
        val pendingOrderRef = database.reference.child("OrderDetails")
        var pendingItemCount =0
        pendingOrderRef.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                pendingItemCount = snapshot.childrenCount.toInt()
                binding.pendingOrders.text = pendingItemCount.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
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