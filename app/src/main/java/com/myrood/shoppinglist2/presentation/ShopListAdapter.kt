package com.myrood.shoppinglist2.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.myrood.shoppinglist2.R
import com.myrood.shoppinglist2.domain.ShopItem

class ShopListAdapter : ListAdapter<ShopItem, ShopListAdapter.ShopItemViewHolder>(ItemCallBack()) {

 //   var shopList = listOf<ShopItem>()
//    set(value) {
//        val shopItemCallBack = ShopItemCallBack(shopList, value)
//        val resultDiff = DiffUtil.calculateDiff(shopItemCallBack)
//        resultDiff.dispatchUpdatesTo(this)
//
//        field = value
//    }

    var howmach = 0

    var onShopItemLongClickListener : ((ShopItem) -> Unit)? = null
    var onShopItemClickListener : ((ShopItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val layout = when(viewType){
            ENABLED_ITEM -> R.layout.shop_item_enabled
            DESABLED_ITEM -> R.layout.shop_item_disabled
            else -> throw RuntimeException ("Type of ShopItem is wrong")
        }
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ShopItemViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ShopItemViewHolder, position: Int) {
        Log.d("onbindviewholder", "how mach - ${howmach++}")

        //getItem onplace of shopList
        val shopItem = getItem(position)
        viewHolder.viewName.text = shopItem.name
        viewHolder.viewCount.text = shopItem.count.toString()

        viewHolder.itemView.setOnLongClickListener {
            onShopItemLongClickListener?.invoke(shopItem)
            true
        }
        viewHolder.itemView.setOnClickListener {
            onShopItemClickListener?.invoke(shopItem)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (item.enable){
            ENABLED_ITEM
        }else{
            DESABLED_ITEM
        }
    }


    class ShopItemViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        val viewName = view.findViewById<TextView>(R.id.nameId)
        val viewCount = view.findViewById<TextView>(R.id.countId)

    }

    companion object{

        const val ENABLED_ITEM = 1
        const val DESABLED_ITEM = 0

        const val MAX_POOL = 7
    }
}