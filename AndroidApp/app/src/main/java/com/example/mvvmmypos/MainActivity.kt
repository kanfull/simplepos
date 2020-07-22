package com.example.mvvmmypos

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.example.mvvmmypos.ui.main.MainFragment
//import com.facebook.stetho.Stetho
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        //Stetho.initializeWithDefaults(this);
    }
}