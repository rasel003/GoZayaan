package com.rasel.androidbaseapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rasel.androidbaseapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity3 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
    }
}