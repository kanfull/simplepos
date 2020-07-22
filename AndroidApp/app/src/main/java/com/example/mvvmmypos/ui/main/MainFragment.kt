package com.example.mvvmmypos.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.mvvmmypos.AppDatabase
import com.example.mvvmmypos.R
import com.example.mvvmmypos.http.WebRequest
import com.example.mvvmmypos.http.WebRequest.Companion.loginWithToken
import com.example.mvvmmypos.model.Product
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.math.BigInteger
import java.security.MessageDigest


class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel

        // create encrypted shared pref
        val appContext= context?.applicationContext
        val sharedPrefs =
            EncryptedSharedPreferences.create("SecureAppData",
                MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
                appContext!!,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM )

        /*
        runBlocking {
            GlobalScope.async {
                var appdb = AppDatabase.getInstance(appContext!!)
                //appdb.productDao().deleteAll()
                appdb.productDao().insert(Product(0, "Hello"))
                var list = appdb.productDao().getAll();
                var length = list.size
                Log.i("MyDebug", length.toString())
            }.await()
        }
        */

        // check whether login have already token or not
        val token=sharedPrefs.getString("token","")
        val hostname=sharedPrefs.getString("hostname","")
        if(token!=""&&hostname!=""){ // found token
            runBlocking {
                val tokenValid = GlobalScope.async {
                    val loginWithToken = loginWithToken(hostname!!, token!!)
                    loginWithToken
                }.await()

                if(tokenValid!!) { // token is valid
                    // move to next fragment
                    findNavController().navigate(R.id.action_mainFragment_to_posFragment)
                }
            }

            // go to login page
        }

        loginButton.setOnClickListener { view ->
            runBlocking {
                var syncFreq=Integer.parseInt(syncTimeText.text.toString())
                if(syncFreq<15){
                    // show error
                }else {
                    val loginResult = GlobalScope.async {
                        WebRequest.login(
                            hostEditText.text.toString(),
                            userEditText.text.toString(),
                            passEditText.text.toString().md5()
                        )
                    }.await()

                    if (loginResult != null) {
                        val sharedPrefsEditor = sharedPrefs.edit()
                        sharedPrefsEditor.putString("hostname", hostEditText.text.toString())
                        sharedPrefsEditor.putString("token", loginResult?.jwt)
                        sharedPrefsEditor.putInt("sync_freq", syncFreq)
                        sharedPrefsEditor.apply()
                        findNavController().navigate(R.id.action_mainFragment_to_posFragment)
                    } else {
                        // TODO show login error
                    }
                }
            }
        }


    }

    fun String.md5(): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
    }
}