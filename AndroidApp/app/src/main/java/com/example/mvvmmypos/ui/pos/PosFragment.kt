package com.example.mvvmmypos.ui.pos

import android.content.Intent
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.provider.AlarmClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import androidx.work.*
import com.example.mvvmmypos.AppDatabase
import com.example.mvvmmypos.R
import com.example.mvvmmypos.model.ProductItem
import com.example.mvvmmypos.model.Sale
import com.example.mvvmmypos.model.SaleItem
import com.example.mvvmmypos.workers.ServerSyncWorker
import kotlinx.android.synthetic.main.pos_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit


class PosFragment : Fragment() {
    var recycleViewAdapter: RecycleViewAdapter? = null

    companion object {
        fun newInstance() = PosFragment()
    }

    private lateinit var viewModel: PosViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.pos_fragment, container, false)
    }

    var wokerRunning=false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PosViewModel::class.java)
        // TODO: Use the ViewModel

        val appContext= context?.applicationContext
        var appdb = AppDatabase.getInstance(appContext!!)

        // set height of recycle view
        panelLayout.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        var panelHeight = panelLayout.measuredHeight
        var statusHeight=this.getStatusBarHeight()
        var frameheight = Resources.getSystem().getDisplayMetrics().heightPixels
        var params = buyLayout.layoutParams
        params.height = frameheight-panelHeight-statusHeight-120 // padding
        buyLayout.layoutParams=params

        // set adaptor of recycle view
        viewModel.getProductMutableLiveData().observe(viewLifecycleOwner , Observer<ArrayList<ProductItem?>>{productArrayList ->
            recycleViewAdapter = RecycleViewAdapter(context, productArrayList,viewModel)
            rv_main!!.layoutManager = LinearLayoutManager(context)
            rv_main!!.adapter = recycleViewAdapter
            rv_main.addItemDecoration(DividerItemDecoration(rv_main.getContext(), DividerItemDecoration.VERTICAL));
        })

        // bind live data
        viewModel.totalPrice.observe(viewLifecycleOwner, Observer<Double> { newDouble -> // buy button
            buyBtn.isEnabled = (newDouble>0)
            buyBtn.text = "ซื้อ " + String.format("%.2f", newDouble) + " บาท"
        })

        // set listener for logout
        logoutBtn.setOnClickListener { view ->
            val sharedPrefs = EncryptedSharedPreferences.create("SecureAppData",
                MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
                appContext!!,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM )
            val sharedPrefsEditor = sharedPrefs.edit()
            sharedPrefsEditor.putString("token", "")
            sharedPrefsEditor.apply()

            Navigation.findNavController(view).popBackStack()
        }
        // set listener for sync button
        syncBtn.setOnClickListener { view ->
            Navigation.findNavController(view).navigate(R.id.action_posFragment_to_syncingFragment)
        }
        // set listener for buy button
        buyBtn.setOnClickListener { view ->
            GlobalScope.launch {
                var saleId=appdb.saleDao().insert(Sale("Kan POS",
                    System.currentTimeMillis(),
                    System.currentTimeMillis()
                ))
                var buyList=viewModel.productBuyList
                buyList.forEach {
                    appdb.saleItemDao().insert(SaleItem(saleId,it.productId,it.priceId,it.promotionId,it.promotionPrice,1,
                        System.currentTimeMillis(),
                        System.currentTimeMillis()))
                }

                GlobalScope.launch(context = Dispatchers.Main) {
                    for (i in 0 until rv_main.getChildCount()) {
                        val holder: RecycleViewHolder =
                            rv_main.findViewHolderForAdapterPosition(i) as RecycleViewHolder
                        holder.uncheck()
                    }
                }
            }
        }

        // add product
        GlobalScope.launch {
            var appdb = AppDatabase.getInstance(appContext!!)
            var list = appdb.productDao().getAllProductItem();
            GlobalScope.launch(context = Dispatchers.Main) {
                viewModel.productList.value?.clear()
                viewModel.productList += list
            }
        }


        // run work manager

        /*
        val serverSyncWorker= OneTimeWorkRequestBuilder<ServerSyncWorker>()
            .build()

        WorkManager.getInstance(appContext).getWorkInfoByIdLiveData(serverSyncWorker.id)
            .observe(viewLifecycleOwner, Observer { workInfo ->
                if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {
                    val current = LocalDateTime.now()
                    val formatter = DateTimeFormatter.ofPattern("HH:mm")
                    val formatted = current.format(formatter)

                    GlobalScope.launch {
                        var appdb = AppDatabase.getInstance(appContext!!)
                        var list = appdb.productDao().getAllProductItem();
                        GlobalScope.launch(context = Dispatchers.Main) {
                            viewModel.productList.value?.clear()
                            viewModel.productList += list
                            syncBtn.text="Sync (Last sync: $formatted)"
                        }
                    }
                }
            })

         */


        val serverSyncWorker= PeriodicWorkRequest.Builder(ServerSyncWorker::class.java,15,
            TimeUnit.MINUTES).build()
        WorkManager.getInstance(appContext).enqueue(serverSyncWorker)

        WorkManager.getInstance(appContext).getWorkInfoByIdLiveData(serverSyncWorker.id)
            .observe(viewLifecycleOwner, Observer { workInfo ->
                if (workInfo != null) {
                    when (workInfo.state){
                        WorkInfo.State.RUNNING -> this.wokerRunning=true
                        WorkInfo.State.ENQUEUED -> {
                            if(this.wokerRunning){
                                val current = LocalDateTime.now()
                                val formatter = DateTimeFormatter.ofPattern("HH:mm")
                                val formatted = current.format(formatter)

                                GlobalScope.launch {
                                    var appdb = AppDatabase.getInstance(appContext!!)
                                    var list = appdb.productDao().getAllProductItem();
                                    GlobalScope.launch(context = Dispatchers.Main) {
                                        viewModel.productList.value?.clear()
                                        viewModel.productList += list
                                        syncBtn.text="Sync (Last sync: $formatted)"
                                    }
                                }
                            }
                        }
                    }

                }
            })
    }

    fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    operator fun <T> MutableLiveData<ArrayList<T>>.plusAssign(values: List<T>) {
        val value = this.value ?: arrayListOf()
        value.addAll(values)
        this.value = value
    }
}