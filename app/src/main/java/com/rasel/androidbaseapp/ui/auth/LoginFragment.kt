package com.rasel.androidbaseapp.ui.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.rasel.androidbaseapp.R
import com.rasel.androidbaseapp.base.BaseFragment
import com.rasel.androidbaseapp.data.models.signin.Auth
import com.rasel.androidbaseapp.databinding.FragmentLoginBinding
import com.rasel.androidbaseapp.presentation.viewmodel.AuthViewModel
import com.rasel.androidbaseapp.presentation.viewmodel.BaseViewModel
import com.rasel.androidbaseapp.presentation.viewmodel.CoroutinesErrorHandler
import com.rasel.androidbaseapp.presentation.viewmodel.TokenViewModel
import com.rasel.androidbaseapp.util.ApiResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding, BaseViewModel>() {
    override val viewModel: AuthViewModel by viewModels()
    private val tokenViewModel: TokenViewModel by activityViewModels()

    private lateinit var navController: NavController
    override fun getViewBinding(): FragmentLoginBinding = FragmentLoginBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        tokenViewModel.token.observe(viewLifecycleOwner) { token ->
            if (token != null)
                navController.navigate(R.id.action_loginFragment_to_main_nav_graph)
        }

        viewModel.loginResponse.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResponse.Failure -> binding.loginTV.text = it.errorMessage
                ApiResponse.Loading -> binding.loginTV.text = "Loading"
                is ApiResponse.Success -> {
                    tokenViewModel.saveToken(it.data.token)
                }
            }
        }

        binding.loginButton.setOnClickListener {
            viewModel.login(
                Auth("john@mail.com", "changeme"),
                object : CoroutinesErrorHandler {
                    override fun onError(message: String) {
                        binding.loginTV.text = "Error! $message"
                    }
                }
            )
        }
    }
}