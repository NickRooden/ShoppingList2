package com.myrood.shoppinglist2.presentation

import androidx.recyclerview.widget.DiffUtil
import com.myrood.shoppinglist2.domain.ShopItem

class ItemCallBack : DiffUtil.ItemCallback<ShopItem>() {
    override fun areItemsTheSame(oldItem: ShopItem, newItem: ShopItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ShopItem, newItem: ShopItem): Boolean {
        return oldItem == newItem
    }
}