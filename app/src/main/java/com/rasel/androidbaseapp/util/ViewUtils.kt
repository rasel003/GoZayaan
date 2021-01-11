package com.rasel.androidbaseapp.util

import android.app.DatePickerDialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.navigation.navOptions
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.rasel.androidbaseapp.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


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

fun View.snackBar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).also { snackbar ->
        snackbar.setAction("Ok") {
            snackbar.dismiss()
        }
    }.show()
}

fun Context.isNetworkAvailable(): Boolean {
    val applicationContext = this.applicationContext
    val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

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

fun String.convertDate(inFormat: String, outFormat: String): String {
    val sdf = SimpleDateFormat(inFormat, Locale.US)
    var formattedDate = "N/A"
    try {
        val convertedDate = sdf.parse(this) ?: return "N/A"
        formattedDate = SimpleDateFormat(outFormat, Locale.US).format(convertedDate)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return formattedDate
}

fun calculateTotalDays(endDate: String, startDate: String, datePattern: String): Int {

    val dateFormat = SimpleDateFormat(datePattern, Locale.US)

    val startD = dateFormat.parse(startDate)

    val endD = dateFormat.parse(endDate)

    return if (startD == null || endD == null) {
        0
    } else {
        val intervalTime = endD.time - startD.time

        val intervalDays = TimeUnit.DAYS.convert(intervalTime, TimeUnit.MILLISECONDS)

        intervalDays.toInt() + 1
    }
}

fun Context.getRandomMaterialColor(typeColor: String): Int {
    var returnColor = Color.GRAY
    val arrayId = resources.getIdentifier("mdcolor_$typeColor", "array", packageName)

    if (arrayId != 0) {
        val colors = resources.obtainTypedArray(arrayId)
        val index = (Math.random() * colors.length()).toInt()
        returnColor = colors.getColor(index, Color.GRAY)
        colors.recycle()
    }
    return returnColor
}

@ColorInt
fun Context.getColorFromAttr(
    @AttrRes attrColor: Int,
    typedValue: TypedValue = TypedValue(),
    resolveRefs: Boolean = true
): Int {
    theme.resolveAttribute(attrColor, typedValue, resolveRefs)
    return typedValue.data
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

fun TextInputEditText.disableError(view : TextInputLayout){

}

fun Context.showPermissionRequestDialog(
    title: String,
    body: String,
    callback: () -> Unit
) {
    AlertDialog.Builder(this).also {
        it.setTitle(title)
        it.setMessage(body)
        it.setPositiveButton("Ok") { _, _ ->
            callback()
        }
    }.create().show()
}

fun Context.permissionGranted(permission: String) =
    ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
