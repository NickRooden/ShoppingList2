package com.myrood.shoppinglist2.presentation

import androidx.lifecycle.ViewModel
import com.myrood.shoppinglist2.data.RepositoryImpl
import com.myrood.shoppinglist2.domain.DeleteShopItemUseCase
import com.myrood.shoppinglist2.domain.EditShopItemUseCase
import com.myrood.shoppinglist2.domain.GetShopListUseCase
import com.myrood.shoppinglist2.domain.ShopItem

class MainViewModel: ViewModel() {

    val repository = RepositoryImpl

    val deleteShopItemUseCase = DeleteShopItemUseCase(repository)
    val editShopItemUseCase = EditShopItemUseCase(repository)
    val getShopListUseCase = GetShopListUseCase(repository)

    val shList = getShopListUseCase.getShopList()

    fun deleteShopItem(shopItem: ShopItem){
        deleteShopItemUseCase.deleteShopItem(shopItem)
    }

    fun changeEnableState(shopItem: ShopItem){
        val newItem = shopItem.copy(enable = !shopItem.enable)
        editShopItemUseCase.editShopItem(newItem)
    }


}