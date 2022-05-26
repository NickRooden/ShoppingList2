package com.myrood.shoppinglist2.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.myrood.shoppinglist2.data.RepositoryImpl
import com.myrood.shoppinglist2.domain.AddShopItemUseCase
import com.myrood.shoppinglist2.domain.EditShopItemUseCase
import com.myrood.shoppinglist2.domain.GetShopItemUseCase
import com.myrood.shoppinglist2.domain.ShopItem
import java.lang.Exception

class ShopItemViewModel : ViewModel() {

    private val repository = RepositoryImpl
    private val editShopItemUseCase = EditShopItemUseCase(repository)
    private val addShopItemUseCase = AddShopItemUseCase(repository)
    private val getShopItemUseCase = GetShopItemUseCase(repository)

    private var _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean>
        get() = _errorInputName

    private var _errorInputCount = MutableLiveData<Boolean>()
    val errorInputCount: LiveData<Boolean>
        get() = _errorInputCount

    private var _shopItem = MutableLiveData<ShopItem>()
    val shopItem: LiveData<ShopItem>
        get() = _shopItem

    private var _finishScreen = MutableLiveData<Unit>()
    val finishScreen: LiveData<Unit>
        get() = _finishScreen


    fun editShopItem(inputName: String?, inputCount: String?){

        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val validation = validateInput(name, count)
        if (validation){
            val shopItem = _shopItem.value
            shopItem?.let {
                val item = it.copy(name = name, count = count)
                editShopItemUseCase.editShopItem(item)
                _finishScreen.value = Unit
            }
        }
    }
    fun addShopItem(inputName: String?, inputCount: String?){

        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val validation = validateInput(name, count)
        if (validation){
            val shopItem = ShopItem(name, count, true)
            addShopItemUseCase.addShopItem(shopItem)

        }
        _finishScreen.value = Unit

    }
    fun getShopItem(shopItemId: Int){
        _shopItem.value = getShopItemUseCase.getShopItem(shopItemId)
    }

    private fun parseCount(inputCount: String?): Int {

        return try {
            inputCount?.trim()?.toInt() ?: 0
        }catch (e: Exception){
            0
        }
    }

    private fun parseName(inputName: String?) = inputName?.trim()?:""

    private fun validateInput(name: String, count: Int): Boolean{

        var result = true
        if (name.isBlank()){
            _errorInputName.value = true
            result = false
        }
        if (count <= 0){
            _errorInputCount.value = true
            result = false
        }
        return result

    }

    fun resetInputNameError(){ _errorInputName.value = false}
    fun resetInputCountError(){ _errorInputCount.value = false}

}