package com.myrood.shoppinglist2.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.myrood.shoppinglist2.R
import com.myrood.shoppinglist2.domain.ShopItem

class ShopListAdapter : RecyclerView.Adapter<ShopListAdapter.ShopItemViewHolder>() {

    var shopList = listOf<ShopItem>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

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
        val shopItem = shopList[position]
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
        val item = shopList[position]
        return if (item.enable){
            ENABLED_ITEM
        }else{
            DESABLED_ITEM
        }
    }

    override fun getItemCount(): Int {
        return shopList.size
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