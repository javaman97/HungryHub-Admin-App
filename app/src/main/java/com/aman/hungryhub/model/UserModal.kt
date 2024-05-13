package com.aman.hungryhub.model

import android.location.Address

data class UserModal(

    val name:String? = null,
    val nameOfRestaurant : String? =null,
    val email:String? = null,
    val password:String? = null,
    val phone:String?=null,
    val address: String?= null
)