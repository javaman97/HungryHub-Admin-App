package com.aman.hungryhub

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity

import androidx.recyclerview.widget.LinearLayoutManager
import com.aman.hungryhub.adapter.OrderDetailsAdapter
import com.aman.hungryhub.databinding.ActivityOrderDetailBinding
import com.aman.hungryhub.model.OrderDetails

class OrderDetailActivity : AppCompatActivity() {
    private val binding:ActivityOrderDetailBinding by lazy {
        ActivityOrderDetailBinding.inflate(layoutInflater)
    }
    private var userName:String ? = null
    private var address:String?=null
    private var phoneNumber:String?=null
    private var totalPrice:String?=null
    private var foodNames:ArrayList<String> = arrayListOf()
    private var foodImages:ArrayList<String> = arrayListOf()
    private var foodQuantity:ArrayList<Int> = arrayListOf()
    private var foodPrices:ArrayList<String> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail)

        binding.backButton.setOnClickListener {
            finish()
        }
        getDataFromIntent()
    }

    private fun getDataFromIntent() {

        val receiveOrderDetails = intent.getSerializableExtra("UserOrderDetails") as OrderDetails
        receiveOrderDetails?.let{orderDetails ->
                userName = receiveOrderDetails.userName
                foodNames= receiveOrderDetails.foodNames as ArrayList<String>
                foodImages = receiveOrderDetails.foodImages as ArrayList<String>
                foodQuantity = receiveOrderDetails.foodQuantities as ArrayList<Int>
                address = receiveOrderDetails.address
                phoneNumber = receiveOrderDetails.phoneNumber
                foodPrices = receiveOrderDetails.foodPrices  as ArrayList<String>
                totalPrice = receiveOrderDetails.totalPrice

            setUserDetails()
            setAdapter()
        }

    }

    private fun setAdapter() {
binding.orderDetailsRV.layoutManager = LinearLayoutManager(this)
        val adapter = OrderDetailsAdapter(this, foodNames,foodImages,foodQuantity,foodPrices)

        binding.orderDetailsRV.adapter = adapter
    }

    private fun setUserDetails() {
        binding.name.text = userName
        binding.address.text = address
        binding.totalPay.text = totalPrice
        binding.phone.text = phoneNumber
    }
}