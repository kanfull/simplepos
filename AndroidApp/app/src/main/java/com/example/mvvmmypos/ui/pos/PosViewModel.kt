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
        this.productBuyList.add(item)
        this.updatePrice()
    }
    fun removeBuyProduct(item: ProductItem){
        this.productBuyList.remove(item)
        this.updatePrice()
    }

    private fun updatePrice(){
        var sum:Double=0.0
        this.productBuyList.forEach {
            sum+=it.promotionPrice
        }
        totalPrice.value = sum
    }
}