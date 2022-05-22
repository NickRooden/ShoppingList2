package com.myrood.shoppinglist2.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.myrood.shoppinglist2.domain.Repository
import com.myrood.shoppinglist2.domain.ShopItem

object RepositoryImpl: Repository {

    var shopListLD = MutableLiveData<List<ShopItem>>()

    private var shopList = mutableListOf<ShopItem>()

    init {
        for (i in 0 until 20){
            val item = ShopItem("Name - $i", i, true)
            addShopItem(item)
        }
    }

    var idCount = 0

    private fun updateShopListLD(){
        shopListLD.value = shopList.toList()
    }


    override fun addShopItem(shopItem: ShopItem) {
        if (shopItem.id == ShopItem.UNDEFINED_ID){
            shopItem.id = idCount++
        }
        shopList.add(shopItem)
        updateShopListLD()
    }

    override fun editShopItem(shopItem: ShopItem) {
        val oldItem = getShopItem(shopItem.id)
        shopList.remove(oldItem)
        shopList.add(shopItem)
        updateShopListLD()

    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shopList.remove(shopItem)
        updateShopListLD()
    }

    override fun getShopItem(shopItemId: Int): ShopItem {
        return shopList.find {
            it.id == shopItemId
        } ?: throw RuntimeException("There isn't such ShopItem with id $shopItemId")
    }

    override fun getShopList(): LiveData<List<ShopItem>> {
        return shopListLD
    }
}