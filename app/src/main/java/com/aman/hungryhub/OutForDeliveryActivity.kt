package com.aman.hungryhub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.aman.hungryhub.adapter.DeliveryAdapter
import com.aman.hungryhub.databinding.ActivityOutForDeliveryBinding
import com.aman.hungryhub.model.OrderDetails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class OutForDeliveryActivity : AppCompatActivity() {
    private val binding : ActivityOutForDeliveryBinding by lazy {
        ActivityOutForDeliveryBinding.inflate(layoutInflater)
    }
    private lateinit var database: FirebaseDatabase
    private var listOfCompletedOrderList:ArrayList<OrderDetails> = arrayListOf()
    private lateinit var databaseOrderDetails: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.backButton.setOnClickListener {
            finish()
        }

        retreiveCompleteOrderDetail()
    }

    private fun retreiveCompleteOrderDetail() {
      database = FirebaseDatabase.getInstance()
        val completedOrderRef = database.reference.child("CompletedOrder")
            .orderByChild("currentTime")

        completedOrderRef.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                listOfCompletedOrderList.clear()
                for(orderSnapshot in snapshot.children){
                    val completeOrder = orderSnapshot.getValue(OrderDetails::class.java)
                    completeOrder?.let{
                        listOfCompletedOrderList.add(it)
                    }
                }
                listOfCompletedOrderList.reverse()

                setDataIntoRecyclerView()

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun setDataIntoRecyclerView() {
         val customerName = mutableListOf<String>()
        val moneyStatus= mutableListOf<Boolean>()

        for(order in listOfCompletedOrderList){
            order.userName?.let{
                customerName.add(it)
            }
           moneyStatus.add(order.paymentReceived)
        }
        val adapter = DeliveryAdapter(customerName,moneyStatus)
        binding.deliveryRecyclerView.adapter = adapter
        binding.deliveryRecyclerView.layoutManager = LinearLayoutManager(this)

    }
}