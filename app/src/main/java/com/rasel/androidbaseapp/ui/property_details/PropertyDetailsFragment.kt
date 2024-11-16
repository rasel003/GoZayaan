package com.rasel.androidbaseapp.ui.property_details

import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.RequestManager
import com.rasel.androidbaseapp.R
import com.rasel.androidbaseapp.cache.entities.Plant
import com.rasel.androidbaseapp.databinding.FragmentPlantDetailBinding
import com.rasel.androidbaseapp.util.toast
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import javax.inject.Inject

/**
 * A fragment representing a single Plant detail screen.
 */
@AndroidEntryPoint
class PropertyDetailsFragment : Fragment(R.layout.fragment_plant_detail) {

    @Inject
    lateinit var imageLoader: RequestManager
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var downloadedBitmap: Bitmap
    private val args: PropertyDetailsFragmentArgs by navArgs()


    /* @Inject
     lateinit var plantDetailViewModelFactory: PlantDetailViewModel.AssistedFactory

     private val plantDetailViewModel: PlantDetailViewModel by viewModels {
         PlantDetailViewModel.provideFactory(
             plantDetailViewModelFactory,
             args.plantId
         )
     }*/

    private lateinit var binding: FragmentPlantDetailBinding
    private val propertyDetailsVM: PropertyDetailsVM by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentPlantDetailBinding.bind(view).apply {
            item = args.model
            /*viewModel = plantDetailViewModel
            lifecycleOwner = viewLifecycleOwner
            callback = Callback { plant ->
                plant?.let {
                    hideAppBarFab(fab)
                    Snackbar.make(root, R.string.added_plant_to_garden, Snackbar.LENGTH_LONG).show()

                    getBitmapFromUrl(it.imageUrl)
                }
            }

            galleryNav.setOnClickListener { navigateToGallery() }*/

            var isToolbarShown = false

            // scroll change listener begins at Y = 0 when image is fully collapsed
            plantDetailScrollview.setOnScrollChangeListener(
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
            toolbar.setNavigationOnClickListener { view ->
                view.findNavController().navigateUp()
            }
        }

        setPermissionCallback()
//        imageLoader = Coil.imageLoader(requireContext())
    }

    private fun setPermissionCallback() {
        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
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
                val imageUri: Uri? =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }
        fos?.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            context?.toast("Saved to Photos")
        }
    }

    // Helper function for calling a share functionality.
    // Should be used when user presses a share button/menu item.
    @Suppress("DEPRECATION")
    private fun createShareIntent() {

    }

    fun interface Callback {
        fun add(plant: Plant?)
    }
}
