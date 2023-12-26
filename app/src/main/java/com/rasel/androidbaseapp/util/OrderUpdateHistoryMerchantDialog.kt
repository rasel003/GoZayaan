package com.rasel.androidbaseapp.util

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rasel.androidbaseapp.R
import com.rasel.androidbaseapp.databinding.DialogOrderUpdateHistoryMerchantBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OrderUpdateHistoryMerchantDialog : BottomSheetDialogFragment() {
    private var orderId: String? = null
    private lateinit var binding: DialogOrderUpdateHistoryMerchantBinding

    override fun getTheme(): Int {
        return R.style.ThemeOverlay_App_BottomSheetDialog_FullScreen
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogOrderUpdateHistoryMerchantBinding.inflate(inflater, container, false)

        orderId = arguments?.getString(KEY_ORDER_ID)

        orderId?.let {
            binding.tvOrderId.text = it
        } ?: kotlin.run {
            dismiss()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    companion object {

        val KEY_ORDER_ID = ""
        fun display(fragmentManager: FragmentManager, tag: String, orderId: String) {
            val dialog = OrderUpdateHistoryMerchantDialog()
            val bundle = Bundle()
            bundle.putString(KEY_ORDER_ID, orderId)
            dialog.arguments = bundle
            dialog.show(fragmentManager, tag)
        }

    }


}