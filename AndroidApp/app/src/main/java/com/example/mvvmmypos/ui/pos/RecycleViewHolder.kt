package com.example.mvvmmypos.ui.pos

import android.graphics.Paint
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmmypos.R
import com.example.mvvmmypos.model.ProductItem
import kotlinx.android.synthetic.main.pos_fragment.*

class RecycleViewHolder(itemsView: View,
                        viewModel:PosViewModel?
    ) :RecyclerView.ViewHolder(itemsView) {
    private var productNameCheckBox: CheckBox? = null
    private var productPricetTxt: TextView? = null
    private var productPromotionTxt: TextView? = null
    private var viewModel:PosViewModel? = null


    init {
        productNameCheckBox = itemView.findViewById(R.id.productNameCheckBox)
        productPricetTxt = itemView.findViewById(R.id.productPricetTxt)
        productPromotionTxt = itemView.findViewById(R.id.productPromotionTxt)
        this.viewModel=viewModel
    }

    fun bind(product: ProductItem) {
        productNameCheckBox?.text = product.productName
        productNameCheckBox?.setOnCheckedChangeListener{ buttonView, isChecked ->
            if(isChecked) {
                viewModel?.addBuyProduct(product)
            }else {
                viewModel?.removeBuyProduct(product)
            }
        }

        productPricetTxt?.text = product.price.toString()
        productPricetTxt?.paintFlags =  Paint.STRIKE_THRU_TEXT_FLAG
        productPromotionTxt?.text = product.promotionPrice.toString()
    }

    fun uncheck(){
        productNameCheckBox!!.isChecked=false
    }
}