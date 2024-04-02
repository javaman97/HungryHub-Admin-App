package com.aman.hungryhub.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.aman.hungryhub.databinding.PendingOrdersItemBinding


class PendingOrderAdapter(
    private val customerNames: ArrayList<String>,
    private val quantity: ArrayList<String>,
    private val foodImage: ArrayList<Int>,
    private val context: Context,


    ) : RecyclerView.Adapter<PendingOrderAdapter.PendingOrderViewHolder>()
{


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingOrderViewHolder {
        val binding =
            PendingOrdersItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return  PendingOrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PendingOrderViewHolder, position: Int) {
        holder.bind(position)
    }
    override fun getItemCount(): Int = customerNames.size
    inner class PendingOrderViewHolder(private val binding: PendingOrdersItemBinding):RecyclerView.ViewHolder(binding.root) {
        private var isAccepted = false

        fun bind(position: Int){
            binding.apply {
                customerName.text =customerNames[position]
                numberOfQuantity.text = quantity[position]
                orderFoodImage.setImageResource(foodImage[position])

                orderAcceptButton.apply {
                    if(!isAccepted){
                        text="Accept"
                    } else {
                        text ="Dispatch"
                        isAccepted =true
                    }

                    setOnClickListener {
                        if(!isAccepted){
                            text = "Dispatch"
                            isAccepted = true
                            showToast("Order is Accepted")
                        } else{
                            customerNames.removeAt(adapterPosition)
                            notifyItemRemoved(adapterPosition)
                            showToast("Order is Dispatched")
                        }
                    }
                }
            }

        }
        private fun showToast(message: String){
            Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
        }

    }
}

