package com.myrood.shoppinglist2.domain

data class ShopItem(
    val name: String,
    val count: Int,
    var enable: Boolean = true,
    var id: Int = UNDEFINED_ID
){
    companion object{
        const val UNDEFINED_ID = -1

    }
}
