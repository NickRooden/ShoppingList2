package com.myrood.shoppinglist2.domain

class EditShopItemUseCase(private val repository: Repository) {
    fun editShopItem(shopItem: ShopItem){
        repository.editShopItem(shopItem)
    }
}