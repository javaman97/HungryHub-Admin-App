package com.aman.hungryhub.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aman.hungryhub.AllItemActivity
import com.aman.hungryhub.databinding.ItemItemBinding
import com.aman.hungryhub.model.AllMenu
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference


class MenuItemAdapter(
    private val context: AllItemActivity,
    private val menuList: ArrayList<AllMenu>,
    databaseReference: DatabaseReference,
    private val onDeleteClickListener:(position:Int) -> Unit
) : RecyclerView.Adapter<MenuItemAdapter.AddItemViewHolder>() {

    private val itemQuantities = MutableList(menuList.size){1}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddItemViewHolder {
        val binding = ItemItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddItemViewHolder(binding)
    }



    override fun onBindViewHolder(holder: AddItemViewHolder, position: Int) {

        holder.bind(position)
    }
    override fun getItemCount(): Int = menuList.size

    inner class AddItemViewHolder(private val binding: ItemItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val quantity = itemQuantities[position]
                val menuItem = menuList[position]
                val uriString = menuItem.foodImage
                val uri = Uri.parse(uriString)
                foodNameTextView.text = menuItem.foodName
                priceTextView.text = menuItem.foodPrice
                Glide.with(context).load(uri).into(foodImageView)

                quantityTextView.text = quantity.toString()
                minusButton.setOnClickListener {
                    decreasesQuantitiy(position)

                }
                plsueButton.setOnClickListener {
                    increasesQuantitiy(position)
                }

                deleteButton.setOnClickListener {
//                    val itemPosition = adapterPosition
//                    if(itemPosition!= RecyclerView.NO_POSITION){
//                        onDeleteClickListener(position)
//                        deleteQuantitiy(itemPosition)
//                    }
                    onDeleteClickListener(position)
                }

            }
        }
        private fun increasesQuantitiy(position: Int) {
            if(itemQuantities[position]<10){
                itemQuantities[position]++
                binding.quantityTextView.text=itemQuantities[position].toString()

            }
        }
        private fun decreasesQuantitiy(position: Int) {
            if(itemQuantities[position]>1){
                itemQuantities[position]--
                binding.quantityTextView.text=itemQuantities[position].toString()

            }
            if (itemQuantities[position]==0){
                deleteQuantitiy(position)
            }
        }
        private fun deleteQuantitiy(position: Int) {

            menuList.removeAt(position)
            menuList.removeAt(position)
            menuList.removeAt(position)
            itemQuantities.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position,menuList.size)


        }
    }


}