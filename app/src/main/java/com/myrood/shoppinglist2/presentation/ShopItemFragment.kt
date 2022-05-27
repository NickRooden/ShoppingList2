package com.myrood.shoppinglist2.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import com.myrood.shoppinglist2.R
import com.myrood.shoppinglist2.domain.ShopItem

class ShopItemFragment : Fragment() {

    private lateinit var viewModel: ShopItemViewModel

    private lateinit var onEditingFinishedListener: OnEditingFinishedListener

    private lateinit var tilName: TextInputLayout
    private lateinit var etName: EditText
    private lateinit var tilCount: TextInputLayout
    private lateinit var etCount: EditText
    private lateinit var saveBtn: Button

    private lateinit var screenMode: String
    private var itemId: Int = ShopItem.UNDEFINED_ID
    private var mode: String? = null
    private var itId: Int? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnEditingFinishedListener){
            onEditingFinishedListener = context
        }else{
            throw RuntimeException("Activity must emplement interface OnEditingFinishedListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("fragment", "fragment")
// some mess
        arguments?.let {
            mode = it.getString(EXTRA_MODE)
            if (mode.isNullOrBlank()){
                throw RuntimeException("EXTRA_MODE is null or Blank -$mode-")
            }
            if (mode != EXTRA_MODE_ADD && mode != EXTRA_MODE_EDIT){
                throw RuntimeException("EXTRA_MODE have not right value -$mode-")
            }
            screenMode = mode.toString()

            if (mode == EXTRA_MODE_EDIT){
                itId = it.getInt(EXTRA_MODE_ID)
                if (itId == ShopItem.UNDEFINED_ID){
                    throw RuntimeException("itemId is UNDEFINED_ID")
                }

            }
            itemId = itId?.toInt()?: -1

        }



    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment = inflater.inflate(R.layout.shop_item_fragment, container, false)
        return fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        initViews(view)

        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]

        when(screenMode){
            EXTRA_MODE_ADD -> launchAddScreen()
            EXTRA_MODE_EDIT -> launchEditScreen()
            else -> throw RuntimeException("screenMode is wrong - $screenMode -")
        }

        watchInputErrors()

        viewModel.finishScreen.observe(viewLifecycleOwner){
            onEditingFinishedListener.onEditingFinished()
        }

        resetInputErrors()

    }

    private fun watchInputErrors() {
        viewModel.errorInputName.observe(viewLifecycleOwner) {
            val msg = if (it) {
                getString(R.string.error_input_name)
            } else {
                null
            }
            tilName.error = msg
        }
        viewModel.errorInputCount.observe(viewLifecycleOwner) {
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

        viewModel.shopItem.observe(viewLifecycleOwner){

            etName.setText(it.name)
            etCount.setText(it.count.toString())

        }
        saveBtn.setOnClickListener{
            viewModel.editShopItem(etName.text?.toString(), etCount.text?.toString())
        }

    }



    private fun initViews(view: View){
        tilName = view.findViewById(R.id.til_name)
        etName = view.findViewById(R.id.et_name)
        tilCount = view.findViewById(R.id.til_count)
        etCount = view.findViewById(R.id.et_count)
        saveBtn = view.findViewById(R.id.save_button)
    }

    interface OnEditingFinishedListener{
        fun onEditingFinished()
    }

    companion object{

        private const val EXTRA_MODE = "extra_mode"
        private const val EXTRA_MODE_ADD = "mode_add"
        private const val EXTRA_MODE_EDIT = "mode_edit"
        private const val EXTRA_MODE_ID = "ShopItemId"

        private const val SCREEN_MODE = ""

        fun instFragmentAddMode() =
            ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_MODE, EXTRA_MODE_ADD)
                }
            }

        fun instFragmentEditMode(itemId: Int) =
            ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_MODE, EXTRA_MODE_EDIT)
                    putInt(EXTRA_MODE_ID, itemId)
                }
            }

    }
}