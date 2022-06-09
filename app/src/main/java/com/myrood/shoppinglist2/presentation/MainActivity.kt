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
import com.myrood.shoppinglist2.databinding.ActivityMainBinding
import com.myrood.shoppinglist2.domain.ShopItem
import com.myrood.shoppinglist2.presentation.ShopListAdapter.Companion.DESABLED_ITEM
import com.myrood.shoppinglist2.presentation.ShopListAdapter.Companion.ENABLED_ITEM
import com.myrood.shoppinglist2.presentation.ShopListAdapter.Companion.MAX_POOL

class MainActivity : AppCompatActivity(), ShopItemFragment.OnEditingFinishedListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var shopListAdapter: ShopListAdapter
    private var fragmentContainer: FragmentContainerView? = null

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        shopListAdapter = ShopListAdapter()
        //val lM = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        with(binding.shopListRv){
            adapter = shopListAdapter
            recycledViewPool.setMaxRecycledViews(ENABLED_ITEM, MAX_POOL)
            recycledViewPool.setMaxRecycledViews(DESABLED_ITEM, MAX_POOL)
            //setLayoutManager(lM)
        }



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

        actionOnSwipe(binding.shopListRv)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.shList.observe(this){

            //submitList on place shopList
            shopListAdapter.submitList(it)
            Log.d("shList", it.toString())
        }



        binding.shopItemAddBtn.setOnClickListener {

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
        //fragmentContainer = findViewById(R.id.main_ac_frag_container)
       return binding.mainAcFragContainer != null
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