package com.rasel.androidbaseapp.util

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rasel.androidbaseapp.EspressoIdlingResource
import com.rasel.androidbaseapp.R
import com.rasel.androidbaseapp.databinding.DialogOrderUpdateHistoryMerchantBinding
import com.rasel.androidbaseapp.presentation.viewmodel.PlantListViewModel
import com.rasel.androidbaseapp.ui.plant_list.PlantAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OrderUpdateHistoryMerchantDialog : BottomSheetDialogFragment() {
    private var orderId: String? = null
    private lateinit var binding: DialogOrderUpdateHistoryMerchantBinding
    val viewModel: PlantListViewModel by viewModels()


    override fun getTheme(): Int {
        return R.style.ThemeOverlay_App_BottomSheetDialog_FullScreen
    }

    /* override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setStyle(STYLE_NORMAL, R.style.ModalBottomSheetDialog2)
     }*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogOrderUpdateHistoryMerchantBinding.inflate(inflater, container, false)

        orderId = arguments?.getString(KEY_ORDER_ID)

        orderId?.let {
//            binding.tvTitle.text = it
        } ?: kotlin.run {
            dismiss()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = PlantAdapter {

        }
        binding.recyclerview.adapter = adapter

        val bottomSheetBehavior = BottomSheetBehavior.from(requireView().parent as View)
//        bottomSheetBehavior.expandedOffset = 100 // Set expanded offset to 100 pixels
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
//        bottomSheetBehavior.isFitToContents = false
//        bottomSheetBehavior.expandedOffset = 200.dp

        subscribeUi(adapter)
    }

    private fun subscribeUi(adapter: PlantAdapter) {
        viewModel.plants.observe(viewLifecycleOwner) { plants ->
            adapter.submitList(plants)
            EspressoIdlingResource.decrement()
        }
    }

    companion object {

        const val KEY_ORDER_ID = ""
        fun display(fragmentManager: FragmentManager, tag: String, orderId: String) {
            val dialog = OrderUpdateHistoryMerchantDialog()
            val bundle = Bundle()
            bundle.putString(KEY_ORDER_ID, orderId)
            dialog.arguments = bundle
            dialog.show(fragmentManager, tag)
        }

    }


}