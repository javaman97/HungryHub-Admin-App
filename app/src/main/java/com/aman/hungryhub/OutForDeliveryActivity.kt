package com.aman.hungryhub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.aman.hungryhub.adapter.DeliveryAdapter
import com.aman.hungryhub.databinding.ActivityOutForDeliveryBinding


class OutForDeliveryActivity : AppCompatActivity() {
    private val binding : ActivityOutForDeliveryBinding by lazy {
        ActivityOutForDeliveryBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.backButton.setOnClickListener {
            finish()
        }
        val customerName = arrayListOf(

            "Jhone Deo",
            "Jane Smith",
            "Mike Johnson",
        )

        val moneyStatus = arrayListOf(
            "recevied",
            "notRecevied",
            "Pending",
        )
        val adapter = DeliveryAdapter(customerName,moneyStatus)
        binding.deliveryRecyclerView.adapter = adapter
        binding.deliveryRecyclerView.layoutManager = LinearLayoutManager(this)


    }
}