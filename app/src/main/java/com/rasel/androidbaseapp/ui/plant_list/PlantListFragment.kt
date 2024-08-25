package com.rasel.androidbaseapp.ui.plant_list

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.rasel.androidbaseapp.EspressoIdlingResource
import com.rasel.androidbaseapp.R
import com.rasel.androidbaseapp.base.BaseFragment
import com.rasel.androidbaseapp.cache.entities.Plant
import com.rasel.androidbaseapp.databinding.FragmentPlantListBinding
import com.rasel.androidbaseapp.presentation.viewmodel.BaseViewModel
import com.rasel.androidbaseapp.presentation.viewmodel.PlantListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random

@AndroidEntryPoint
class PlantListFragment : BaseFragment<FragmentPlantListBinding, BaseViewModel>(),
    Toolbar.OnMenuItemClickListener {

    override fun getViewBinding(): FragmentPlantListBinding =
        FragmentPlantListBinding.inflate(layoutInflater)

    private lateinit var adapter: PlantAdapter
    override val viewModel: PlantListViewModel by viewModels()

    /* override fun onCreateView(
         inflater: LayoutInflater,
         container: ViewGroup?,
         savedInstanceState: Bundle?
     ): View {
         val binding = FragmentPlantListBinding.inflate(inflater, container, false)
         context ?: return binding.root



 //        setHasOptionsMenu(true)
         return binding.root
     }*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.inflateMenu(R.menu.menu_plant_list)
        binding.toolbar.setOnMenuItemClickListener(this)

        EspressoIdlingResource.increment()

        adapter = PlantAdapter(
            onItemClicked = {
                navigateToPlant(it)
            }, onBookmarkClicked = { plant: Plant, position ->
                val list = viewModel.plants.value ?: emptyList()
                val updatedList = list.map {
                    if (it.plantId == plant.plantId) {
                        it.copy(commentsCount = Random.nextInt().toString())
                    } else {
                        it
                    }
                }
                adapter.submitList(updatedList)
            })
        binding.plantList.adapter = adapter
        subscribeUi(adapter)

        binding.tvTest.setOnClickListener {
            findNavController().navigate(
                PlantListFragmentDirections.actionPlantListFragmentToPlantDetailFragment(
                    "bougainvillea-glabra"
                )
            )
        }
    }


    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.filter_zone -> {
                updateData()
                true
            }

            R.id.action_settings -> {
                val direction = PlantListFragmentDirections.actionNavPlantListFragmentToNavSettings()
                findNavController().navigate(direction)
                true
            }

            else -> false
        }
    }

    private fun navigateToPlant(
        plant: Plant
    ) {
        val direction = PlantListFragmentDirections.actionPlantListFragmentToPlantDetailFragment(
            plant.plantId
        )
        findNavController().navigate(direction)
    }
    /* override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
         inflater.inflate(R.menu.menu_plant_list, menu)
     }

     override fun onOptionsItemSelected(item: MenuItem): Boolean {
         return when (item.itemId) {
             R.id.filter_zone -> {
                 updateData()
                 true
             }

             else -> super.onOptionsItemSelected(item)
         }
     }*/

    private fun subscribeUi(adapter: PlantAdapter) {
        viewModel.plants.observe(viewLifecycleOwner) { plants ->
            adapter.submitList(plants)
            EspressoIdlingResource.decrement()
        }
    }

    private fun updateData() {
        with(viewModel) {
            if (isFiltered()) {
                clearGrowZoneNumber()
            } else {
                setGrowZoneNumber(9)
            }
        }
    }
}


