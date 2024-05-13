package com.aman.hungryhub.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.aman.hungryhub.PendingOrderActivity
import com.aman.hungryhub.databinding.PendingOrdersItemBinding
import com.bumptech.glide.Glide


class PendingOrderAdapter(
    private val context: Context,
    private val customerNames: MutableList<String>,
    private val quantity: MutableList<String>,
    private val foodImage: MutableList<String>,
    private val itemClicked: PendingOrderActivity

    ) : RecyclerView.Adapter<PendingOrderAdapter.PendingOrderViewHolder>()
{
   interface OnItemClicked{
       fun onItemClickListener(position: Int)
       fun onItemAcceptListener(position: Int)
       fun onItemDispatchListener(position: Int)


   }

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
                customerName.text = customerNames[position]
                numberOfQuantity.text = quantity[position]
                val uriString = foodImage[position]
                var uri = Uri.parse(uriString)
               Glide.with(context).load(uri).into(orderFoodImage)

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
                            itemClicked.onItemAcceptListener(position)
                        } else{
                            customerNames.removeAt(adapterPosition)
                            notifyItemRemoved(adapterPosition)
                            showToast("Order is Dispatched")
                            itemClicked.onItemDispatchListener(position)
                        }
                    }
                }
                itemView.setOnClickListener {
                    itemClicked.onItemClickListener(position)
                }
            }

        }
        private fun showToast(message: String){
            Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
        }

    }
}

