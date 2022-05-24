package com.myrood.shoppinglist2.presentation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import com.myrood.shoppinglist2.R
import com.myrood.shoppinglist2.domain.ShopItem

class ShopItemActivity : AppCompatActivity() {

    private lateinit var viewModel: ShopItemViewModel

    private lateinit var tilName: TextInputLayout
    private lateinit var etName: EditText
    private lateinit var tilCount: TextInputLayout
    private lateinit var etCount: EditText
    private lateinit var saveBtn: Button

    private var screenMode = SCREEN_MODE
    private var itemId = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)

        checkMode()

        initViews()

        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]

        when(screenMode){
            EXTRA_MODE_ADD -> launchAddScreen()
            EXTRA_MODE_EDIT -> launchEditScreen()
        }

        watchInputErrors()

        viewModel.finishScreen.observe(this){
            finish()
        }

        resetInputErrors()


        //Log.d("mode_extra", "mode - $mode, id - $itemId")

    }

    private fun watchInputErrors() {
        viewModel.errorInputName.observe(this) {
            val msg = if (it) {
                getString(R.string.error_input_name)
            } else {
                null
            }
            tilName.error = msg
        }
        viewModel.errorInputCount.observe(this) {
            val msg = if (it) {
                getString(R.string.error_input_count)
            } else {
                null
            }
            tilCount.error = msg
        }
    }

    private fun resetInputErrors() {
        etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetInputNameError()
            }

            override fun afterTextChanged(p0: Editable?) {}

        })
        etCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetInputCountError()
            }

            override fun afterTextChanged(p0: Editable?) {}

        })
    }

    private fun launchAddScreen(){

        saveBtn.setOnClickListener{
            viewModel.addShopItem(etName.text?.toString(), etCount.text?.toString())
        }

    }
    private fun launchEditScreen(){
        viewModel.getShopItem(itemId)

        viewModel.shopItem.observe(this){

            etName.setText(it.name)
            etCount.setText(it.count.toString())

        }
        saveBtn.setOnClickListener{
            viewModel.editShopItem(etName.text?.toString(), etCount.text?.toString())
        }

    }

    private fun checkMode(){
        val mode = intent.getStringExtra(EXTRA_MODE)
        if (!mode.isNullOrBlank()){
            throw RuntimeException("EXTRA_MODE is null or Blank")
        }
        if (mode != EXTRA_MODE_ADD && mode != EXTRA_MODE_EDIT){
            throw RuntimeException("EXTRA_MODE have not right value")
        }
        screenMode = mode
        if (mode == EXTRA_MODE_EDIT){
            itemId = intent.getIntExtra(EXTRA_MODE_ID, ShopItem.UNDEFINED_ID)
            if (itemId == ShopItem.UNDEFINED_ID){
                throw RuntimeException("itemId is UNDEFINED_ID")
            }
        }


    }

    private fun initViews(){
        tilName = findViewById(R.id.til_name)
        etName = findViewById(R.id.et_name)
        tilCount = findViewById(R.id.til_count)
        etCount = findViewById(R.id.et_count)
        saveBtn = findViewById(R.id.save_button)
    }

    companion object{

        private const val EXTRA_MODE = "extra_mode"
        private const val EXTRA_MODE_ADD = "mode_add"
        private const val EXTRA_MODE_EDIT = "mode_edit"
        private const val EXTRA_MODE_ID = "ShopItemId"

        private const val SCREEN_MODE = ""

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


}