package com.example.mvvmmypos.workers

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.mvvmmypos.AppDatabase
import com.example.mvvmmypos.http.WebRequest
import com.example.mvvmmypos.model.Sale
import com.example.mvvmmypos.model.SaleServerMap
import com.example.mvvmmypos.model.Sync
import kotlinx.android.synthetic.main.syncing_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ServerSyncWorker(var appContext: Context, workerParams: WorkerParameters): Worker(appContext, workerParams) {
    override fun doWork() : Result{
        // Uploader order and order item
        ServerSyncWorker.syncServer(appContext!!,null,null,null,null,null)
        return Result.success()
    }

    companion object {
        fun syncServer(appContext: Context, syncProductTxt:TextView?, syncPriceText:TextView?,
                       syncPromotionText:TextView?, syncSaleTxt:TextView?, syncSaleItemTxt:TextView?)
        {
            val sharedPrefs =
                EncryptedSharedPreferences.create(
                    "SecureAppData",
                    MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
                    appContext!!,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                )

            var appdb = AppDatabase.getInstance(appContext!!)
            val host=sharedPrefs.getString("hostname","")
            val token=sharedPrefs.getString("token","")

            var dt:Long=0
            val lastSync=appdb.syncDao().getLastSync()
            if(lastSync!=null){
                dt=lastSync.syncTime
            }

            var products = WebRequest.getProduct(host!!, token!!, dt)
            appdb.productDao().insertAll(products!!)

            if(syncProductTxt!= null) {
                GlobalScope.launch(context = Dispatchers.Main) {
                    syncProductTxt.visibility = View.VISIBLE
                }
            }

            var prices = WebRequest.getPrice(host!!, token!!, dt)
            appdb.priceDao().insertAll(prices!!)

            if(syncPriceText!= null) {
                GlobalScope.launch(context = Dispatchers.Main) {
                    syncPriceText.visibility = View.VISIBLE
                }
            }

            var promotions = WebRequest.getPromotion(host!!, token!!, dt)
            appdb.promotionDao().insertAll(promotions!!)

            if(syncPromotionText!= null) {
                GlobalScope.launch(context = Dispatchers.Main) {
                    syncPromotionText.visibility = View.VISIBLE
                }
            }

            var sales=appdb.saleDao().getFromDate(dt)
            sales.forEach {
                var saleId= WebRequest.addSale(host!!, token!!,it.saleName)
                appdb.saleServerMapDao().insert(SaleServerMap(it.saleId,saleId))
                var items=appdb.saleItemDao().getFromDateById(dt,it.saleId)
                items.forEach { item ->
                    WebRequest.addSaleItem(host!!, token!!,saleId,item.productId,item.priceId,item.promotionId,item.salePrice,item.unit)
                }
            }

            if(syncSaleTxt!=null&&syncSaleItemTxt!=null) {
                GlobalScope.launch(context = Dispatchers.Main) {
                    syncSaleTxt.visibility = View.VISIBLE
                }

                GlobalScope.launch(context = Dispatchers.Main) {
                    syncSaleItemTxt.visibility = View.VISIBLE
                }
            }

            var epoch=System.currentTimeMillis()
            appdb.syncDao().insert(Sync(epoch,1))
        }
    }
}