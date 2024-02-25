package com.rasel.androidbaseapp.util

import android.app.DatePickerDialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.navOptions
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.rasel.androidbaseapp.R
import java.util.*


fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}


fun Context.toastColorful(message: String) {

    val toast = Toast.makeText(this, message, Toast.LENGTH_LONG)
    toast.view?.apply {

        //Gets the actual oval background of the Toast then sets the colour filter
        background.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN)
        //  background.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(Color.BLACK, BlendModeCompat.SRC_IN)

        //Gets the TextView from the Toast so it can be edited
        findViewById<TextView>(android.R.id.message).apply {
            setTextColor(Color.WHITE)
        }
    }

    toast.show()
}

fun Context.toastColorfulShort(message: String) {
    val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
    val view = toast.view?.apply {

        //Gets the actual oval background of the Toast then sets the colour filter
        background.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN)

        //Gets the TextView from the Toast so it can be edited
        findViewById<TextView>(android.R.id.message).apply {
            setTextColor(Color.WHITE)
        }
    }

    toast.show()
}

fun ProgressBar.show() {
    visibility = View.VISIBLE
}

fun ProgressBar.hide() {
    visibility = View.GONE
}

/**
 * Shorthand extension function to make view gone
 */
fun View.makeGone() {
    this.visibility = View.GONE
}

/**
 * Shorthand extension function to make view visible
 */
fun View.makeVisible() {
    this.visibility = View.VISIBLE
}


fun View.snackBar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).also { snackbar ->
        snackbar.setAction("Ok") {
            snackbar.dismiss()
        }
    }.show()
}

fun Context.isNetworkAvailable(): Boolean {
    val applicationContext = this.applicationContext
    val connectivityManager =
        applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            //for other device how are able to connect with Ethernet
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    } else {
        val nwInfo = connectivityManager.activeNetworkInfo ?: return false
        return nwInfo.isConnected
    }
}

val options = navOptions {
    anim {
        enter = R.anim.slide_in_right
        exit = R.anim.slide_out_left
        popEnter = R.anim.slide_in_left
        popExit = R.anim.slide_out_right
    }
}

fun getDatePickerDialog(
    textView: TextView,
    year: Int = Calendar.getInstance().get(Calendar.YEAR),
    month: Int = Calendar.getInstance().get(Calendar.MONTH),
    day: Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
): DatePickerDialog {

    val datePickerDialog = DatePickerDialog(
        textView.context,
        { view, selectedYear, monthOfYear, dayOfMonth ->
            val selectedMonth = monthOfYear + 1
            val selectedDate = "$dayOfMonth/$selectedMonth/$selectedYear"
            textView.text = selectedDate
        },
        year,
        month,
        day
    )
    datePickerDialog.setOnShowListener {
        // dpd.getButton(DatePickerDialog.BUTTON_POSITIVE).setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.transparent))
        // dpd.getButton(DatePickerDialog.BUTTON_POSITIVE).setBackgroundResource(android.R.color.transparent)

        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE)
            .setTextColor(textView.context.getColorFromAttr(R.attr.colorSecondary))
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE)
            .setTextColor(textView.context.getColorFromAttr(R.attr.colorSecondary))

    }
    //  dpd.datePicker.minDate = Calendar.getInstance().timeInMillis - 1000

    textView.setOnClickListener {
        datePickerDialog.show()
    }

    return datePickerDialog
}

fun String.capitalizeFirstCharacter(): String {
    return substring(0, 1).toUpperCase() + substring(1)
}

fun TextInputEditText.disableError(view: TextInputLayout) {

}

val Int.dp: Int
get() = (this / Resources.getSystem().displayMetrics.density).toInt()
val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun expandAndCollapseButtonWithRecyclerview(
    recyclerView: RecyclerView,
    fab: ExtendedFloatingActionButton
) {
    recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            /*if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            binding.btnAddPatient.extend();
        }*/
            super.onScrollStateChanged(recyclerView, newState)
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            /*if (dy > 0) {
            binding.fabCreate.shrink();
        } else {
            binding.fabCreate.extend();
        }*/

            // if the recycler view is scrolled
            // above shrink the FAB
            if (dy > 10 && fab.isExtended) {
                fab.shrink()
            }

            // if the recycler view is scrolled
            // above extend the FAB
            if (dy < -10 && !fab.isExtended) {
                fab.extend()
            }

            // of the recycler view is at the first
            // item always extend the FAB
            if (!recyclerView.canScrollVertically(-1)) {
                fab.extend()
            }
        }
    })
}

fun addOrRemoveElevationAndStroke(
    add: Boolean,
    containerView: MaterialCardView,
    mContext: Context?
) {
    if (add) {
        containerView.cardElevation = 4f
        containerView.strokeColor = ContextCompat.getColor(mContext!!, R.color.white_400)
        containerView.strokeWidth = 2
        val shapeAppearanceModel = ShapeAppearanceModel.builder(
            mContext,
            R.style.ShapeAppearance_MaterialComponents_MediumComponent,
            R.style.ShapeAppearance_App_Card_TopRadius
        ).build()
        containerView.shapeAppearanceModel = shapeAppearanceModel
    } else {
        containerView.cardElevation = 0f
        containerView.strokeColor = ContextCompat.getColor(mContext!!, R.color.white_color)
        containerView.strokeWidth = 0
        val shapeAppearanceModel = ShapeAppearanceModel.builder(
            mContext,
            R.style.ShapeAppearance_MaterialComponents_MediumComponent,
            R.style.ShapeAppearance_App_Image_WithOutRounded
        ).build()
        containerView.shapeAppearanceModel = shapeAppearanceModel
    }
}
