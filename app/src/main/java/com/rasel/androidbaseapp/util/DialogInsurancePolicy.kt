package com.rasel.androidbaseapp.util

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rasel.androidbaseapp.R
import com.rasel.androidbaseapp.databinding.DialogInsurancePolicyBinding

class DialogInsurancePolicy : BottomSheetDialogFragment() {
    lateinit var binding: DialogInsurancePolicyBinding

    override fun getTheme(): Int {
        return R.style.ThemeOverlay_PaperflyGo_BottomSheetDialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DialogInsurancePolicyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        (dialog as BottomSheetDialog).setCancelable(false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnContinue.setOnClickListener { dismiss() }
    }
}