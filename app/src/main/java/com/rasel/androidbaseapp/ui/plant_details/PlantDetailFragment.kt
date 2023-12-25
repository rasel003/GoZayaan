
package com.rasel.androidbaseapp.ui.plant_details

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.rasel.androidbaseapp.R
import com.rasel.androidbaseapp.cache.entities.Plant
import com.rasel.androidbaseapp.databinding.FragmentPlantDetailBinding
import com.rasel.androidbaseapp.ui.plant_details.PlantDetailFragment.Callback
import com.rasel.androidbaseapp.util.permissionGranted
import com.rasel.androidbaseapp.util.showPermissionRequestDialog
import com.rasel.androidbaseapp.util.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import javax.inject.Inject

/**
 * A fragment representing a single Plant detail screen.
 */
@AndroidEntryPoint
class PlantDetailFragment : Fragment(R.layout.fragment_plant_detail) {

    @Inject
    lateinit var imageLoader: RequestManager
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var downloadedBitmap: Bitmap

   /* @Inject
    lateinit var plantDetailViewModelFactory: PlantDetailViewModel.AssistedFactory

    private val plantDetailViewModel: PlantDetailViewModel by viewModels {
        PlantDetailViewModel.provideFactory(
            plantDetailViewModelFactory,
            args.plantId
        )
    }*/

    private lateinit var binding: FragmentPlantDetailBinding
    private val plantDetailViewModel: PlantDetailViewModel by viewModels ()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding =  FragmentPlantDetailBinding.bind(view).apply {
            viewModel = plantDetailViewModel
            lifecycleOwner = viewLifecycleOwner
            callback = Callback { plant ->
                plant?.let {
                    hideAppBarFab(fab)
                    Snackbar.make(root, R.string.added_plant_to_garden, Snackbar.LENGTH_LONG).show()

                    getBitmapFromUrl(it.imageUrl)
                }
            }

            galleryNav.setOnClickListener { navigateToGallery() }

            var isToolbarShown = false

            // scroll change listener begins at Y = 0 when image is fully collapsed
            /* plantDetailScrollview.setOnScrollChangeListener(
                 NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->

                     // User scrolled past image to height of toolbar and the title text is
                     // underneath the toolbar, so the toolbar should be shown.
                     val shouldShowToolbar = scrollY > toolbar.height

                     // The new state of the toolbar differs from the previous state; update
                     // appbar and toolbar attributes.
                     if (isToolbarShown != shouldShowToolbar) {
                         isToolbarShown = shouldShowToolbar

                         // Use shadow animator to add elevation if toolbar is shown
                         appbar.isActivated = shouldShowToolbar

                         // Show the plant name if toolbar is shown
                         toolbarLayout.isTitleEnabled = shouldShowToolbar
                     }
                 }
             )
 */
            /*toolbar.setNavigationOnClickListener { view ->
                view.findNavController().navigateUp()
            }

            toolbar.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_share -> {
                        createShareIntent()
                        true
                    }
                    else -> false
                }
            }*/
        }

        setPermissionCallback()
//        imageLoader = Coil.imageLoader(requireContext())
    }

    private fun getBitmapFromUrl(mediaDownloadURL : String) = viewLifecycleOwner.lifecycleScope.launch {
        // binding.progressbar.visible(true)
        if (mediaDownloadURL.isNotEmpty()) {
           /* val request = ImageRequest.Builder(requireContext())
                .data(mediaDownloadURL)
                .build()
            downloadedBitmap = (imageLoader.execute(request).drawable as BitmapDrawable).bitmap*/
            downloadedBitmap = plantDetailViewModel.loadImageAsync(mediaDownloadURL)

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                checkPermissionAndSaveBitmap()
            }else {
                saveMediaToStorage(downloadedBitmap)
            }
        } else {
            requireContext().toast("No Media Downloaded")
        }
    }
    private fun checkPermissionAndSaveBitmap() {
        //  binding.progressbar.visible(false)
        when {
            requireContext().permissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
                saveMediaToStorage(downloadedBitmap)
            }
            shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
                requireContext().showPermissionRequestDialog(
                    getString(R.string.permission_title),
                    getString(R.string.write_permission_request)
                ) {
                    requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }
    }

    private fun setPermissionCallback() {
        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    if (::downloadedBitmap.isInitialized) {
                        saveMediaToStorage(downloadedBitmap)
                    }
                }
            }
    }

    private fun saveMediaToStorage(bitmap: Bitmap) {
        val filename = "${System.currentTimeMillis()}.jpg"
        var fos: OutputStream? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            context?.contentResolver?.also { resolver ->
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(
                        MediaStore.MediaColumns.RELATIVE_PATH,
                        Environment.DIRECTORY_PICTURES
                    )
                }
                val imageUri: Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }
        fos?.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            context?.toast("Saved to Photos")
        }
    }

    private fun navigateToGallery() {
        plantDetailViewModel.plant?.value?.let { plant ->
            val direction = PlantDetailFragmentDirections.actionPlantDetailFragmentToGalleryFragment(plant.name)
            findNavController().navigate(direction)
        }
    }

    // Helper function for calling a share functionality.
    // Should be used when user presses a share button/menu item.
    @Suppress("DEPRECATION")
    private fun createShareIntent() {
        val shareText = plantDetailViewModel.plant?.value.let { plant ->
            if (plant == null) {
                ""
            } else {
                getString(R.string.share_text_plant, plant.name)
            }
        }
        val shareIntent = ShareCompat.IntentBuilder.from(requireActivity())
            .setText(shareText)
            .setType("text/plain")
            .createChooserIntent()
            .addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        startActivity(shareIntent)
    }

    // FloatingActionButtons anchored to AppBarLayouts have their visibility controlled by the scroll position.
    // We want to turn this behavior off to hide the FAB when it is clicked.
    //
    // This is adapted from Chris Banes' Stack Overflow answer: https://stackoverflow.com/a/41442923
    private fun hideAppBarFab(fab: FloatingActionButton) {
        val params = fab.layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior as FloatingActionButton.Behavior
        behavior.isAutoHideEnabled = false
        fab.hide()
    }

    fun interface Callback {
        fun add(plant: Plant?)
    }
}
