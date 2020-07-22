package com.example.mvvmmypos.ui.sync

import android.opengl.Visibility
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.mvvmmypos.R
import com.example.mvvmmypos.workers.ServerSyncWorker
import kotlinx.android.synthetic.main.pos_fragment.*
import kotlinx.android.synthetic.main.syncing_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SyncingFragment : Fragment() {

    companion object {
        fun newInstance() = SyncingFragment()
    }

    private lateinit var viewModel: SyncingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.syncing_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SyncingViewModel::class.java)
        // TODO: Use the ViewModel

        backButton.setOnClickListener { view ->
            Navigation.findNavController(view).popBackStack()
        }


        GlobalScope.launch {
            val appContext= context?.applicationContext

            ServerSyncWorker.syncServer(appContext!!,syncProductTxt,syncPriceTxt,syncPromotionTxt,syncSaleTxt,syncSaleItemTxt)

            GlobalScope.launch(context = Dispatchers.Main) {
                backButton.visibility = View.VISIBLE
            }
        }
    }

}