package com.rasel.androidbaseapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.rasel.androidbaseapp.R
import com.rasel.androidbaseapp.base.BaseFragment
import com.rasel.androidbaseapp.databinding.FragmentMainBinding
import com.rasel.androidbaseapp.presentation.viewmodel.BaseViewModel
import com.rasel.androidbaseapp.presentation.viewmodel.CoroutinesErrorHandler
import com.rasel.androidbaseapp.presentation.viewmodel.MainViewModel
import com.rasel.androidbaseapp.presentation.viewmodel.TokenViewModel
import com.rasel.androidbaseapp.ui.MainActivity
import com.rasel.androidbaseapp.util.ApiResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding, BaseViewModel>() {
    override val viewModel: MainViewModel by viewModels()
    private val tokenViewModel: TokenViewModel by activityViewModels()

    private lateinit var navController: NavController
    private var token: String? = null

    override fun getViewBinding(): FragmentMainBinding = FragmentMainBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        tokenViewModel.token.observe(viewLifecycleOwner) { token ->
            this.token = token
            if (token == null)
                navController.navigate(R.id.action_global_loginFragment)
        }

        viewModel.userInfoResponse.observe(viewLifecycleOwner) {
            binding.infoTV.text = when (it) {
                is ApiResponse.Failure -> "Code: ${it.code}, ${it.errorMessage}"
                ApiResponse.Loading -> "Loading"
                is ApiResponse.Success -> "ID: ${it.data.id}\nMail: ${it.data.email}\n\nToken: $token"
            }
        }

        binding.infoButton.setOnClickListener {
            viewModel.getUserInfo(object : CoroutinesErrorHandler {
                override fun onError(message: String) {
                    binding.infoTV.text = "Error! $message"
                }
            })
        }

        binding.btnHome.setOnClickListener {
            startActivity(Intent(context, MainActivity::class.java))
        }
        binding.logoutButton.setOnClickListener {
            tokenViewModel.deleteToken()
        }
    }
}