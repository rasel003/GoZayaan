package com.rasel.androidbaseapp.base

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.rasel.androidbaseapp.R
import com.rasel.androidbaseapp.core.dialog.dismissLoadingDialog
import com.rasel.androidbaseapp.core.dialog.showLoadingDialog
import com.rasel.androidbaseapp.presentation.viewmodel.BaseViewModel
import com.rasel.androidbaseapp.util.getColorFromAttr
import com.rasel.androidbaseapp.util.getColorFromTheme
import com.rasel.androidbaseapp.util.setStatusBarColor
import com.rasel.androidbaseapp.util.showSnackBar
import timber.log.Timber

abstract class BaseFragment<VB : ViewBinding, ViewModel : BaseViewModel> : Fragment() {

    protected lateinit var binding: VB
    protected abstract val viewModel: ViewModel

    abstract fun getViewBinding(): VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        activity?.setStatusBarColor(context?.getColorFromAttr(R.attr.colorPrimary)!!)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = getViewBinding()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observerEvents()

//        setSystemBarIconColor()

    }

    private fun observerEvents() {
        viewModel.apply {
            /*isLoading.observe(viewLifecycleOwner, {
                handleLoading(it == true)
            })
            errorMessage.observe(viewLifecycleOwner, {
                handleErrorMessage(it)
            })
            noInternetConnectionEvent.observe(viewLifecycleOwner, {
                handleErrorMessage(getString(R.string.no_internet_connection))
            })
            connectTimeoutEvent.observe(viewLifecycleOwner, {
                handleErrorMessage(getString(R.string.connect_timeout))
            })
            forceUpdateAppEvent.observe(viewLifecycleOwner, {
                handleErrorMessage(getString(R.string.force_update_app))
            })
            serverMaintainEvent.observe(viewLifecycleOwner, {
                handleErrorMessage(getString(R.string.server_maintain_message))
            })
            unknownErrorEvent.observe(viewLifecycleOwner, {
                handleErrorMessage(getString(R.string.unknown_error))
            })*/
        }
    }

    protected open fun handleLoading(isLoading: Boolean) {
        if (isLoading) showLoadingDialog() else dismissLoadingDialog()
    }

  protected  fun handleLoading(progressCircular: ProgressBar, isLoading: Boolean) {
        progressCircular.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    protected open fun handleErrorMessage(message: String?) {
        if (message.isNullOrBlank()) return
        dismissLoadingDialog()
        Timber.e(message)
        showSnackBar(binding.root, message)
    }

    fun disableEdgeToEdge() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activity?.window?.setDecorFitsSystemWindows(false)
        } else {
            activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }
    }

    fun setSystemBarIconColor(
        isDarkStatusBar: Boolean = true,
        isDarkNavigationBar: Boolean = true
    ) {
        activity?.window?.let {
            val windowInsetsController = WindowCompat.getInsetsController(it, requireView())
            windowInsetsController.isAppearanceLightNavigationBars = isDarkNavigationBar
            windowInsetsController.isAppearanceLightStatusBars = isDarkStatusBar
        }

    }
}
