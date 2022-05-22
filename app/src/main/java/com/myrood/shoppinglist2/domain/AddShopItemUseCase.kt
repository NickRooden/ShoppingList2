package com.myrood.shoppinglist2.domain

class AddShopItemUseCase(private val repository: Repository) {

    fun addShopItem(shopItem: ShopItem){

        repository.addShopItem(shopItem)

    }
}