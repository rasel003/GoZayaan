package com.rasel.androidbaseapp.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.DialogFragment
import com.rasel.androidbaseapp.R
import com.rasel.androidbaseapp.databinding.DialogForBankBinding
import java.util.*

class DialogForBank internal constructor(
    bankDataList: List<BankData>,
    private var onItemClicked: ((bankData: BankData) -> Unit)
) : DialogFragment() {
    private val bankDataList: List<BankData>
    private lateinit var binding: DialogForBankBinding
    private val selectionAdapter: SelectionAdapter = SelectionAdapter {
        onItemClicked(it)
    }

    init {
        selectionAdapter.submitList(bankDataList)
        this.bankDataList = bankDataList
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.ThemeOverlay_App_DayNight_FullScreenDialog)
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
            dialog.window?.setWindowAnimations(R.style.Slide)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DialogForBankBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setNavigationOnClickListener { dismiss() }
        binding.toolbar.title = "Bank List"

        val searchView = binding.toolbar.menu.getItem(0).actionView as SearchView?
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                val suggestions = ArrayList<BankData>()
                for (people in bankDataList) {
                    if (people.bankTitle.lowercase(Locale.getDefault()).contains(
                            query.lowercase(
                                Locale.getDefault()
                            )
                        )
                    ) {
                        suggestions.add(people)
                    }
                }
                selectionAdapter.submitList(suggestions)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                val suggestions = ArrayList<BankData>()
                for (people in bankDataList) {
                    if (people.bankTitle.lowercase(Locale.getDefault()).contains(
                            newText.lowercase(
                                Locale.getDefault()
                            )
                        )
                    ) {
                        suggestions.add(people)
                    }
                }
                selectionAdapter.submitList(suggestions)
                return true
            }
        })
        binding.recyclerView.adapter = selectionAdapter
    }

    companion object {
        fun display(
            bankDataList: List<BankData>,
            onItemClicked: ((bankData: BankData) -> Unit)
        ): DialogForBank {
            return DialogForBank(bankDataList, onItemClicked)
        }
    }
}
