package com.aman.hungryhub

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.aman.hungryhub.adapter.PendingOrderAdapter
import com.aman.hungryhub.databinding.ActivityPendingOrderBinding
import com.aman.hungryhub.model.OrderDetails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class PendingOrderActivity : AppCompatActivity(), PendingOrderAdapter.OnItemClicked {
    private lateinit var binding: ActivityPendingOrderBinding
    private var listOfName:MutableList<String> = mutableListOf()
    private var listOfTotalPrice:MutableList<String> = mutableListOf()
    private var listOfImageFirstFoodOrder:MutableList<String> = mutableListOf()
    private var listOfOrderItem:ArrayList<OrderDetails> = arrayListOf()
    private lateinit var database:FirebaseDatabase
    private lateinit var databaseOrderDetails: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPendingOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()

        databaseOrderDetails = database.reference.child("OrderDetails")
        getOrderDetails()

        binding.backButton.setOnClickListener {
            finish()
        }


    }

    private fun getOrderDetails() {
        databaseOrderDetails.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               for(orderSnapshot in snapshot.children){
                   val orderDetails = orderSnapshot.getValue(OrderDetails::class.java)

                   orderDetails?.let {
                       listOfOrderItem.add(it)
                   }
               }
                addDataToListForRecyclerView()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    private fun addDataToListForRecyclerView() {
      for(orderItem:OrderDetails in listOfOrderItem) {

          orderItem.userName?.let { listOfName.add(it) }
          orderItem.totalPrice?.let { listOfTotalPrice.add(it) }

//            orderItem.foodImages?.filterNot { it.isEmpty() }?.forEach {
//                listOfImageFirstFoodOrder.add(it.toString())
//            }
            }

        setAdapter()
    }

    private fun setAdapter() {
        binding.pendingOrdersRecyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = PendingOrderAdapter(this, listOfName,listOfTotalPrice,listOfImageFirstFoodOrder,this)
        binding.pendingOrdersRecyclerView.adapter = adapter
    }

    override fun onItemClickListener(position: Int) {
        val intent = Intent(this, OrderDetailActivity::class.java)
        val userOrderDetails = listOfOrderItem[position]
        intent.putExtra("UserOrderDetails",userOrderDetails)
        startActivity(intent)
    }

    override fun onItemAcceptListener(position: Int) {

        val childItemPushKey = listOfOrderItem[position].itemPushKey
        val clickItemOrderRef = childItemPushKey?.let{
            database.reference.child("OrderDetails").child(it)
        }
        clickItemOrderRef?.child("orderAccepted")?.setValue(true)
        updateOrderAcceptedStatus(position)
    }

    override fun onItemDispatchListener(position: Int) {
      val dispatchItemPushKey = listOfOrderItem[position].itemPushKey
        val dispatchItemOrderRef = database.reference.child("CompletedOrder").
                child(dispatchItemPushKey!!)
        dispatchItemOrderRef.setValue(listOfOrderItem[position])
            .addOnSuccessListener {
                deleteThisItemFromOrderDetails(dispatchItemPushKey)
            }


    }

    private fun updateOrderAcceptedStatus(position: Int){

        val userIdOfClickItem = listOfOrderItem[position].userUid
        val pushKeyOfClickItem = listOfOrderItem[position].itemPushKey
        val buyHistoryRef = database.reference.child("user").child(userIdOfClickItem!!).child("BuyHistory").child(pushKeyOfClickItem!!)
             buyHistoryRef.child("orderAccepted").setValue(true)
        databaseOrderDetails.child(pushKeyOfClickItem).child("orderAccepted").setValue(true)
    }
    private fun deleteThisItemFromOrderDetails(dispatchItemPushKey: String) {

        val orderDetailsItemRef = database.reference.child("OrderDetails").child(dispatchItemPushKey)

        orderDetailsItemRef.removeValue()
            .addOnSuccessListener {
                Toast.makeText(this,"Order is Dispatched",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this,"Order is not Dispatched",Toast.LENGTH_SHORT).show()
            }
    }


}