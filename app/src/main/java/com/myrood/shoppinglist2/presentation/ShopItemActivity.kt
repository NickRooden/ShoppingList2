package com.myrood.shoppinglist2.presentation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import com.myrood.shoppinglist2.R
import com.myrood.shoppinglist2.databinding.ActivityShopItemBinding
import com.myrood.shoppinglist2.domain.ShopItem

class ShopItemActivity : AppCompatActivity(), ShopItemFragment.OnEditingFinishedListener {


    private var screenMode = SCREEN_MODE_EMPTY
    private var itemId = ShopItem.UNDEFINED_ID

    private lateinit var binding: ActivityShopItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkMode()

        //to stop create fragment few times, only first time we make it ourself
        if (savedInstanceState == null) {

            val fragment = when (screenMode) {
                EXTRA_MODE_ADD -> ShopItemFragment.instFragmentAddMode()
                EXTRA_MODE_EDIT -> ShopItemFragment.instFragmentEditMode(itemId)
                else -> throw RuntimeException("screenMode have not right value - $screenMode -")
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.sh_it_frag_container, fragment)
                .commit()
        }

    }


    private fun checkMode(){
        val mode = intent.getStringExtra(EXTRA_MODE)
        if (mode.isNullOrBlank()){
            throw RuntimeException("EXTRA_MODE is null or Blank - $mode -")
        }
        if (mode != EXTRA_MODE_ADD && mode != EXTRA_MODE_EDIT){
            throw RuntimeException("EXTRA_MODE have not right value - $mode -")
        }
        screenMode = mode
        if (mode == EXTRA_MODE_EDIT){
            itemId = intent.getIntExtra(EXTRA_MODE_ID, ShopItem.UNDEFINED_ID)
            if (itemId == ShopItem.UNDEFINED_ID){
                throw RuntimeException("itemId is UNDEFINED_ID")
            }
        }
    }

    companion object{

        private const val EXTRA_MODE = "extra_mode"
        private const val EXTRA_MODE_ADD = "mode_add"
        private const val EXTRA_MODE_EDIT = "mode_edit"
        private const val EXTRA_MODE_ID = "ShopItemId"

        private const val SCREEN_MODE_EMPTY = ""

        fun intentCreateModeAdd(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_MODE, EXTRA_MODE_ADD)
            return intent
        }

        fun intentCreateModeEdit(context: Context, itemId: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_MODE, EXTRA_MODE_EDIT)
            intent.putExtra(EXTRA_MODE_ID, itemId)
            return intent
        }
    }

    override fun onEditingFinished() {
        finish()
    }


}