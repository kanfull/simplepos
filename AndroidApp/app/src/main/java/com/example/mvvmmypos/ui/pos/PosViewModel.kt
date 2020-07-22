package com.example.mvvmmypos.ui.pos

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvmmypos.model.ProductItem


class PosViewModel() : ViewModel() {
    // TODO: Implement the ViewModel
    val totalPrice: MutableLiveData<Double>
    var productBuyList:ArrayList<ProductItem>
    val productList = MutableLiveData<ArrayList<ProductItem?>>()

    init {
        productList.value = ArrayList()
        totalPrice=MutableLiveData<Double>(0.0)
        productBuyList= arrayListOf<ProductItem>()
    }

    fun getProductMutableLiveData(): MutableLiveData<ArrayList<ProductItem?>> {
        return productList
    }

    fun addBuyProduct(item: ProductItem){
        totalPrice.setValue(totalPrice.value?.plus(item.promotionPrice))
        this.productBuyList.add(item)
    }
    fun removeBuyProduct(item: ProductItem){
        totalPrice.setValue(totalPrice.value?.minus(item.promotionPrice))
        this.productBuyList.remove(item)
    }
}