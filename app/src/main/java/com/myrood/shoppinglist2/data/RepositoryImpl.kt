package com.myrood.shoppinglist2.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.myrood.shoppinglist2.domain.Repository
import com.myrood.shoppinglist2.domain.ShopItem
import kotlin.random.Random

object RepositoryImpl: Repository {

    private var shopListLD = MutableLiveData<List<ShopItem>>()

    private var shopList = sortedSetOf<ShopItem>({o1, o2 -> o1.id.compareTo(o2.id)})

    init {
        for (i in 0 until 10){
            val item = ShopItem("Name - $i", i, Random.nextBoolean())
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