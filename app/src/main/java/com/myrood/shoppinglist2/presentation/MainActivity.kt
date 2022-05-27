package com.myrood.shoppinglist2.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.myrood.shoppinglist2.R
import com.myrood.shoppinglist2.domain.ShopItem
import com.myrood.shoppinglist2.presentation.ShopListAdapter.Companion.DESABLED_ITEM
import com.myrood.shoppinglist2.presentation.ShopListAdapter.Companion.ENABLED_ITEM
import com.myrood.shoppinglist2.presentation.ShopListAdapter.Companion.MAX_POOL

class MainActivity : AppCompatActivity(), ShopItemFragment.OnEditingFinishedListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var shopListAdapter: ShopListAdapter
    private var fragmentContainer: FragmentContainerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        shopListAdapter = ShopListAdapter()
        val recyclerView = findViewById<RecyclerView>(R.id.shop_list_rv)
        recyclerView.adapter = shopListAdapter
        //val lM = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        //recyclerView.setLayoutManager(lM)
        recyclerView.recycledViewPool.setMaxRecycledViews(ENABLED_ITEM, MAX_POOL)
        recyclerView.recycledViewPool.setMaxRecycledViews(DESABLED_ITEM, MAX_POOL)

        val isLandOrient = isLandOrient()

        shopListAdapter.onShopItemLongClickListener = {
            viewModel.changeEnableState(it)

        }

        shopListAdapter.onShopItemClickListener = {

            if (isLandOrient){

                launchFragment(ShopItemFragment.instFragmentEditMode(it.id))

            }else{
                val intent = ShopItemActivity.intentCreateModeEdit(this, it.id)
                startActivity(intent)
            }


        }

        actionOnSwipe(recyclerView)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.shList.observe(this){

            //submitList on place shopList
            shopListAdapter.submitList(it)
            Log.d("shList", it.toString())
        }

        val addBtn = findViewById<FloatingActionButton>(R.id.shopItem_add_btn)

        addBtn.setOnClickListener {

            if (isLandOrient){

                launchFragment(ShopItemFragment.instFragmentAddMode())

            }else{
                val intent = ShopItemActivity.intentCreateModeAdd(this)
                startActivity(intent)
            }

        }

    }

    private fun launchFragment(fragment: ShopItemFragment){
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_ac_frag_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun isLandOrient(): Boolean{
        fragmentContainer = findViewById(R.id.main_ac_frag_container)
       return fragmentContainer != null
    }

    private fun actionOnSwipe(recyclerView: RecyclerView) {
        val callback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                //currentList on place of shopList
                val item = shopListAdapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteShopItem(item)
            }

        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun onEditingFinished() {
        Toast.makeText(this,"Success!", Toast.LENGTH_LONG).show()
        supportFragmentManager.popBackStack()
    }
}