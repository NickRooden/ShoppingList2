package com.myrood.shoppinglist2.domain

import androidx.lifecycle.LiveData

class GetShopListUseCase(private val repository: Repository) {
    fun getShopList(): LiveData<List<ShopItem>>{
        return repository.getShopList()
    }
}