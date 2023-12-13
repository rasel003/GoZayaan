package com.rasel.androidbaseapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import com.rasel.androidbaseapp.data.network.model.Localization
import com.rasel.androidbaseapp.databinding.FragmentSettingsBinding
import com.rasel.androidbaseapp.util.OrderUpdateHistoryMerchantDialog
import com.rasel.androidbaseapp.viewmodel.LocalizedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private val viewModel: LocalizedViewModel by activityViewModels()
    private lateinit var binding: FragmentSettingsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.localizationFlow.asLiveData().observe(viewLifecycleOwner){
            binding.btnEnglish.text = it.lblEnglish
            binding.btnBurmese.text = it.lblBurmese
            binding.btnChinese.text = it.lblChinese

            binding.textView.text =
                Localization.getTemplatedString(it.lblSelectedLanguage,
                    viewModel.currentLanguageFlow.value
                )

        }
        binding.btnEnglish.setOnClickListener{viewModel.switchToEnglish()}
        binding.btnBurmese.setOnClickListener{viewModel.switchToBurmese()}
        binding.btnChinese.setOnClickListener{viewModel.switchToChinese()}

        binding.textView.setOnClickListener {
            OrderUpdateHistoryMerchantDialog.display(
                childFragmentManager, "history",
                "it1"
            )
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = SettingsFragment()
    }
}