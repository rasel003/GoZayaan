package com.rasel.androidbaseapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rasel.androidbaseapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
    }
}