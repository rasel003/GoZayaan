
package com.rasel.androidbaseapp.binding_adapters

import android.view.View
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("isGone")
fun bindIsGone(view: View, isGone: Boolean) {
    view.visibility = if (isGone) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

/*@BindingAdapter(value = ["app:progressScaled", "android:max"], requireAll = true)
fun setProgress(progressBar: ProgressBar, likes: Int, max: Int) {
    progressBar.progress = (likes * max / 5).coerceAtMost(max)
}

@BindingAdapter("app:hideIfZero")
fun hideIfZero(view: View, number: Int) {
    view.visibility = if (number == 0) View.GONE else View.VISIBLE
}
@BindingAdapter("app:errorText")
fun setErrorMessage(view: TextInputLayout, errorMessage: String) {
    view.error = errorMessage
}
@BindingAdapter("app:enableOrDisable")
fun seEnableOrDisableMessage(view: MaterialButton, isProcessing: Boolean) {
    view.isEnabled = !isProcessing
}*/
