package com.example.samplepos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.security.crypto.MasterKeys
import java.math.BigInteger
import java.security.MessageDigest
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.work.*
import com.example.samplepos.model.LoginWorker
import com.example.samplepos.model.SyncOrderWorker

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = "Please login!"

        var myContext=getApplicationContext()

        val sharedPrefs =
            EncryptedSharedPreferences.create("SecureAppData",
                MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
                myContext,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM )

        val loginBtn = findViewById<Button>(R.id.loginButton)
        loginBtn.setOnClickListener {
            var host=(findViewById<TextView>(R.id.hostEditText)).text.toString()
            var user=(findViewById<TextView>(R.id.userEditText)).text.toString()
            var pass=(findViewById<TextView>(R.id.passEditText)).text.toString().md5()
            var token="null"

            val data = Data.Builder()
            data.putString("host",host)
            data.putString("user",user)
            data.putString("pass",pass)
            val loginRequest: WorkRequest = OneTimeWorkRequestBuilder<LoginWorker>()
                .setInputData(data.build()).build()

            WorkManager.getInstance(myContext).enqueue(loginRequest)

            WorkManager.getInstance(myContext).getWorkInfoByIdLiveData(loginRequest.id)
                .observe(this, Observer { workInfo ->
                    if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {
                        var token=workInfo.outputData.getString("uriString")
                        Log.i("SimplePOS", token)
                        val intent = Intent(this, pos::class.java).apply {
                            putExtra(EXTRA_MESSAGE, token)
                        }
                        startActivity(intent)
                    }
                })



            var edit=sharedPrefs.edit();
            edit.putString("host",host)
            edit.putString("user",user)
            edit.putString("token",token)
            edit.apply()
        }
    }

    fun String.md5(): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
    }
}
