package com.example.mvvmmypos.ui.pos

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmmypos.R
import com.example.mvvmmypos.model.ProductItem

class RecycleViewAdapter(
    list1: Context?,
    private val list: ArrayList<ProductItem?>,
    viewModel:PosViewModel?
)
    : RecyclerView.Adapter<RecycleViewHolder>(){
    private var viewModel:PosViewModel? = null

    init {
        this.viewModel=viewModel
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecycleViewHolder {
        return RecycleViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false),viewModel)
    }

    override fun onBindViewHolder(holder: RecycleViewHolder, position: Int) {
        holder.bind(list[position]!!)
    }

    override fun getItemCount(): Int = list.size

}