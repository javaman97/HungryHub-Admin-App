package com.aman.hungryhub

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.aman.hungryhub.adapter.PendingOrderAdapter
import com.aman.hungryhub.databinding.ActivityPendingOrderBinding


class PendingOrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPendingOrderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPendingOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }
        val orderCustomerName = arrayListOf(

            "Jhone Deo",
            "Jane Smith",
            "Mike Johnson",
        )

        val orderQuantity = arrayListOf(
            "8",
            "7",
            "4",
        )
        val orderFoodImage = arrayListOf(R.drawable.menu1, R.drawable.menu2, R.drawable.menu3)
        val adapter = PendingOrderAdapter (orderCustomerName,orderQuantity,orderFoodImage, this )

        binding.pendingOrdersRecyclerView.adapter = adapter
        binding.pendingOrdersRecyclerView.layoutManager = LinearLayoutManager(this)
    }
}