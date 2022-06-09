package com.myrood.shoppinglist2.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.myrood.shoppinglist2.R
import com.myrood.shoppinglist2.databinding.ShopItemDisabledBinding
import com.myrood.shoppinglist2.databinding.ShopItemEnabledBinding
import com.myrood.shoppinglist2.domain.ShopItem

class ShopListAdapter : ListAdapter<ShopItem, ShopListAdapter.ShopItemViewHolder>(ItemCallBack()) {


    var howmach = 0

    var onShopItemLongClickListener : ((ShopItem) -> Unit)? = null
    var onShopItemClickListener : ((ShopItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val layout = when(viewType){
            ENABLED_ITEM -> R.layout.shop_item_enabled
            DESABLED_ITEM -> R.layout.shop_item_disabled
            else -> throw RuntimeException ("Type of ShopItem is wrong")
        }
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            layout,
            parent,false
        )
        return ShopItemViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ShopItemViewHolder, position: Int) {
        Log.d("onbindviewholder", "how mach - ${howmach++}")

        //getItem onplace of shopList
        val shopItem = getItem(position)

        val binding = viewHolder.binding

        when (binding){
            is ShopItemEnabledBinding -> {
                with(binding){

                    nameId.text = shopItem.name
                    countId.text = shopItem.count.toString()
                }

            }
            is ShopItemDisabledBinding -> {
                with(binding){

                    nameId.text = shopItem.name
                    countId.text = shopItem.count.toString()
                }
            }
        }
        binding.root.setOnLongClickListener {
            onShopItemLongClickListener?.invoke(shopItem)
            true
        }
        binding.root.setOnClickListener {
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


    class ShopItemViewHolder(
        val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)

    companion object{

        const val ENABLED_ITEM = 1
        const val DESABLED_ITEM = 0

        const val MAX_POOL = 7
    }
}