package com.rasel.androidbaseapp.util

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.marginBottom
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.core.view.updatePaddingRelative
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rasel.androidbaseapp.EspressoIdlingResource
import com.rasel.androidbaseapp.R
import com.rasel.androidbaseapp.cache.entities.Plant
import com.rasel.androidbaseapp.databinding.DialogFullScreenBottomSheetBinding
import com.rasel.androidbaseapp.presentation.viewmodel.PlantListViewModel
import com.rasel.androidbaseapp.ui.plant_list.PlantAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random


@AndroidEntryPoint
class FullScreenBottomSheetDialog : BottomSheetDialogFragment() {
    private var orderId: String? = null
    private lateinit var binding: DialogFullScreenBottomSheetBinding
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
        binding = DialogFullScreenBottomSheetBinding.inflate(inflater, container, false)

        orderId = arguments?.getString(KEY_ORDER_ID)

        orderId?.let {
//            binding.tvTitle.text = it
        } ?: kotlin.run {
            dismiss()
        }

//        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
        dialog?.setOnShowListener { dialog ->
            val layout: FrameLayout? = (dialog as BottomSheetDialog).
            findViewById(com.google.android.material.R.id.design_bottom_sheet)
            layout?.let {
                val behavior = BottomSheetBehavior.from(it)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                behavior.skipCollapsed = true
            }
            // TODO: NAVHOST INSTANCE
//            return view
        }

        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        object : BottomSheetDialog(requireContext(), theme) {
            override fun onAttachedToWindow() {
                super.onAttachedToWindow()

                window?.let {
                    WindowCompat.setDecorFitsSystemWindows(it, false)
                }

                findViewById<View>(com.google.android.material.R.id.container)?.apply {
                    fitsSystemWindows = false
                    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
                        insets.apply {
                            view.updatePadding(bottom = 50)
                        }
//                        val insets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                        // Apply the insets as a margin to the view. This solution sets
                        // only the bottom, left, and right dimensions, but you can apply whichever
                        // insets are appropriate to your layout. You can also update the view padding
                        // if that's more appropriate.
                        /*view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                            leftMargin = insets.left
                            bottomMargin = insets.bottom +100
                            rightMargin = insets.right
                        }*/

                        // Return CONSUMED if you don't want want the window insets to keep passing
                        // down to descendant views.
//                        WindowInsetsCompat.CONSUMED
                    }
                }

                findViewById<View>(com.google.android.material.R.id.coordinator)?.fitsSystemWindows = false
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Pad the bottom of the ScrollView so that it scrolls up above the nav bar
        view.doOnApplyWindowInsets { v, insets, padding ->
            v.updatePaddingRelative(
                bottom = padding.bottom + insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom + 190.dp
            )
        }

        val adapter =  PlantAdapter(
            onItemClicked = {
            }, onBookmarkClicked = { plant : Plant, position ->
            })
        binding.recyclerview.adapter = adapter

        val bottomSheetBehavior = BottomSheetBehavior.from(requireView().parent as View)
//        bottomSheetBehavior.expandedOffset = 100 // Set expanded offset to 100 pixels
//        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
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
            val dialog = FullScreenBottomSheetDialog()
            val bundle = Bundle()
            bundle.putString(KEY_ORDER_ID, orderId)
            dialog.arguments = bundle
            dialog.show(fragmentManager, tag)
        }

    }


}