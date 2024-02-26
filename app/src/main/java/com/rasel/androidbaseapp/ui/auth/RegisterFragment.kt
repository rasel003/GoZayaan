package com.rasel.androidbaseapp.ui.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.rasel.androidbaseapp.base.BaseFragment
import com.rasel.androidbaseapp.databinding.FragmentRegisterBinding
import com.rasel.androidbaseapp.presentation.viewmodel.AuthViewModel
import com.rasel.androidbaseapp.presentation.viewmodel.BaseViewModel

class RegisterFragment : BaseFragment<FragmentRegisterBinding, BaseViewModel>() {

    override fun getViewBinding(): FragmentRegisterBinding = FragmentRegisterBinding.inflate(layoutInflater)
    override val viewModel: AuthViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}