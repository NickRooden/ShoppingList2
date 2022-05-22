package com.myrood.shoppinglist2.domain

class DeleteShopItemUseCase(private val repository: Repository) {
    fun deleteShopItem(shopItem: ShopItem){
        repository.deleteShopItem(shopItem)
    }
}