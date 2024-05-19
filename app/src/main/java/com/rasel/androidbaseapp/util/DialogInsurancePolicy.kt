package com.rasel.androidbaseapp.util

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rasel.androidbaseapp.databinding.DialogInsurancePolicyBinding
import com.rasel.androidbaseapp.ui.gallery.SearchRequestViewModel

class DialogInsurancePolicy : BottomSheetDialogFragment() {

    //    private lateinit var mBehavior: BottomSheetBehavior<View>
    lateinit var binding: DialogInsurancePolicyBinding

    private val searchRequestViewModel: SearchRequestViewModel by activityViewModels()


    /* override fun getTheme(): Int {
         return R.style.ThemeOverlay_App_BottomSheetDialog
     }*/

    /* override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setStyle(STYLE_NORMAL, R.style.ModalBottomSheetDialog)
     }*/
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DialogInsurancePolicyBinding.inflate(inflater, container, false)
//        mBehavior = BottomSheetBehavior.from(binding.root)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
//        (dialog as BottomSheetDialog).setCancelable(false)
//        mBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchRequestViewModel.cloneSearchQuery.observe(viewLifecycleOwner) {
            binding.tvDetailsTitle.text = it.search
        }

        binding.btnSearch.setOnClickListener {
            searchRequestViewModel.shouldWaitForNewUpdate = false
            searchRequestViewModel.searchQuery.value = searchRequestViewModel.cloneSearchQuery.value
            dismissAllowingStateLoss()
        }

        binding.btnContinue.setOnClickListener {
            val action = DialogInsurancePolicyDirections.actionDialogInsurancePolicyToNavFaq()
            findNavController().navigate(action)
        }

        /*binding.btnContinue.setOnClickListener {
            onItemClicked()
            dismiss()
        }*/
    }
}