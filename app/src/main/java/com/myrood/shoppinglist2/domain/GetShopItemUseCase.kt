package com.myrood.shoppinglist2.domain

class GetShopItemUseCase(private val repository: Repository) {
    fun getShopItem(shopItemId: Int): ShopItem{
        return repository.getShopItem(shopItemId)
    }
}