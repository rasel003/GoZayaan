package com.rasel.androidbaseapp.ui.profile

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.util.Pair
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.rasel.androidbaseapp.R
import com.rasel.androidbaseapp.base.DownloadFragment
import com.rasel.androidbaseapp.core.theme.ThemeUtils
import com.rasel.androidbaseapp.data.models.Localization
import com.rasel.androidbaseapp.databinding.FragmentProfileBinding
import com.rasel.androidbaseapp.presentation.viewmodel.BaseViewModel
import com.rasel.androidbaseapp.presentation.viewmodel.LocalizedViewModel
import com.rasel.androidbaseapp.presentation.viewmodel.SettingUIModel
import com.rasel.androidbaseapp.presentation.viewmodel.SettingsViewModel
import com.rasel.androidbaseapp.ui.dialog.BankData
import com.rasel.androidbaseapp.ui.dialog.DialogForBank
import com.rasel.androidbaseapp.ui.image_slider.GridFragmentDirections
import com.rasel.androidbaseapp.ui.image_slider.ImageSliderFragmentDirections
import com.rasel.androidbaseapp.ui.scrolling_tab.WithScrollViewFragmentDirections
import com.rasel.androidbaseapp.ui.settings.LanguageSettingDialogFragment
import com.rasel.androidbaseapp.ui.settings.SettingsAdapter
import com.rasel.androidbaseapp.ui.settings.ThemeSettingDialogFragment
import com.rasel.androidbaseapp.util.FullScreenBottomSheetDialog
import com.rasel.androidbaseapp.util.TimeUtils
import com.rasel.androidbaseapp.util.TimeUtils.getStringDateFromTimeInMillis
import com.rasel.androidbaseapp.util.isResizableNeeded
import com.rasel.androidbaseapp.util.observe
import com.rasel.androidbaseapp.util.result.EventObserver
import com.rasel.androidbaseapp.util.setResizableText
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : DownloadFragment<FragmentProfileBinding, BaseViewModel>(),
    Toolbar.OnMenuItemClickListener {

    override val viewModel: SettingsViewModel by viewModels()
    private val localizedViewModel: LocalizedViewModel by activityViewModels()

    @Inject
    lateinit var settingsAdapter: SettingsAdapter

    @Inject
    lateinit var themeUtils: ThemeUtils

    private lateinit var dialogForBank: DialogForBank

    private lateinit var dateRangePicker: MaterialDatePicker<Pair<Long, Long>>
    private var endDate: String = ""
    private var startDate: String = ""

    private lateinit var datePicker: MaterialDatePicker<Long>
    private var selectedDate: String = ""

    override fun getViewBinding(): FragmentProfileBinding =
        FragmentProfileBinding.inflate(layoutInflater)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner


//        registerDownloadReceiver()

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>(MY_KEY)
            ?.observe(viewLifecycleOwner) {
                // get your result here
                // show Dialog B again if you like ?

                Timber.tag("rsl").d("result value : $it")

                if (it) {
                    /* val action = SettingsFragmentDirections.actionNavSettingsToDialogInsurancePolicy()
                     findNavController().navigate(action)*/
                }
            }

        val sentences = listOf(
            "The quick brown fox jumps over the lazy dog.",
            "Lorem ipsum dolor sit amet, consecrate disciplining elite.",
            "This is a random sentence.",
            "Kotlin is a modern programming language.",
            "Artificial Intelligence is shaping the future."
        )

        val bankDataList = mutableListOf<BankData>()

        for (i in 1..40) {
            bankDataList.add(BankData(i, sentences.random()))
        }


        dialogForBank = DialogForBank.display(bankDataList, ::addItem)


        val adapterOnHold = ArrayAdapter(
            requireContext(),
            R.layout.list_item,
            resources.getStringArray(R.array.hold_reasons_v2)
        )
        adapterOnHold.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)


        observe(viewModel.settings, ::onViewStateChange)
        viewModel.getSettings()

        viewModel.navigateToThemeSelector.observe(viewLifecycleOwner, EventObserver {
            ThemeSettingDialogFragment.newInstance()
                .show(parentFragmentManager, null)
        })
        viewModel.navigateToLanguageSelector.observe(viewLifecycleOwner, EventObserver {
            LanguageSettingDialogFragment.newInstance()
                .show(parentFragmentManager, null)
        })

        setBoldText()

        binding.tvCharacterDetails.setResizableText(
            getString(R.string.faq_after_dark_program_description),
            3,
            true
        )

        binding.cardViewImage.setOnClickListener {
            val result = binding.tvCharacterDetails.isResizableNeeded(
                getString(R.string.faq_after_dark_program_description),
                4
            )
            Toast.makeText(it.context, "Resizable $result", Toast.LENGTH_SHORT).show()
        }

    }

    // when activity has toolbar
    /*override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.main, menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_logOut -> {
                logOutFromApp(); true
            }

            R.id.action_settings -> {
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }*/

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_logOut -> {
                logOutFromApp(); true
            }

            R.id.action_settings -> {
                val action = GridFragmentDirections.actionGlobalGridFragment()
                findNavController().navigate(action)
                return true
            }

            R.id.action_image_slider -> {
                val action = ImageSliderFragmentDirections.actionGlobalNavImageSliderFragment()
                findNavController().navigate(action)
                return true
            }

            R.id.action_scroll_view -> {
                val action = WithScrollViewFragmentDirections.actionGlobalWithScrollViewFragment()
                findNavController().navigate(action)
                return true
            }

            R.id.action_fag -> {
                /*val action = SettingsFragmentDirections.actionNavSettingsToNavFaq()
                findNavController().navigate(action)*/
                return true
            }

            else -> false
        }
    }


    private fun logOutFromApp() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.titleLogOut))
            .setMessage(resources.getString(R.string.messageLogOut))
            .setNegativeButton(resources.getString(R.string.cancel)) { dialog, which ->
                // Respond to negative button press
                dialog.dismiss()
            }
            .setPositiveButton(resources.getString(R.string.ok)) { dialog, which ->
                // Respond to positive button press
                dialog.dismiss()
                activity?.finish()
            }
            .show()
    }


    private fun onViewStateChange(result: SettingUIModel) {
        if (result.isRedelivered) return
        when (result) {
            is SettingUIModel.Error -> handleErrorMessage(result.error)
            SettingUIModel.Loading -> handleLoading(true)
            is SettingUIModel.NightMode -> {
                result.nightMode.let {
                    themeUtils.setNightMode(it)
                }
            }

            is SettingUIModel.Success -> {
                handleLoading(false)
                result.data.let {
                    settingsAdapter.list = it
                }
            }
        }
    }

    private fun setBoldText() {
        // Find your TextView in the layout

        // The full text
        val fullText = "Mon 3rd Feb, 11 AM"

        // Create a SpannableString from the full text
        val spannableString = SpannableString(fullText)

        // Find the start and end indices of the bold part
        val startIndex = fullText.indexOf(" ")
        val endIndex = fullText.indexOf(",")

        if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
            // Set the StyleSpan to bold for the specified range
            spannableString.setSpan(
                StyleSpan(Typeface.BOLD),
                startIndex + 1,
                endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        // Set the SpannableString to the TextView
        binding.tvCharacterDetails2.text = spannableString
    }


    private fun addItem(bankData: BankData?) {
        Toast.makeText(requireContext(), bankData?.bankTitle, Toast.LENGTH_SHORT).show()
        dialogForBank.dismiss()
    }
    // mnopqrs

    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragment()

        const val MY_KEY = "result_check"

        private const val TAG = "rsl"
    }


}