/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rasel.androidbaseapp.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Typeface
import android.net.wifi.WifiConfiguration
import android.os.Build
import android.os.Parcel
import android.text.Editable
import android.text.Layout
import android.text.Layout.Alignment
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.StaticLayout
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnAttachStateChangeListener
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.annotation.DimenRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.ParcelCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.ObservableBoolean
import androidx.databinding.ViewDataBinding
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.Task
import com.google.gson.Gson
import com.rasel.androidbaseapp.BuildConfig
import com.rasel.androidbaseapp.R
import com.rasel.androidbaseapp.data.models.Theme
import kotlinx.coroutines.Job
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.math.roundToInt

fun ObservableBoolean.hasSameValue(other: ObservableBoolean) = get() == other.get()

fun Int.isEven() = this % 2 == 0

fun View.isRtl() = layoutDirection == View.LAYOUT_DIRECTION_RTL

inline fun <T : ViewDataBinding> T.executeAfter(block: T.() -> Unit) {
    block()
    executePendingBindings()
}

/**
 * Calculated the widest line in a [StaticLayout].
 */
fun StaticLayout.textWidth(): Int {
    var width = 0f
    for (i in 0 until lineCount) {
        width = width.coerceAtLeast(getLineWidth(i))
    }
    return width.toInt()
}

fun newStaticLayout(
    source: CharSequence,
    paint: TextPaint,
    width: Int,
    alignment: Alignment,
    spacingmult: Float,
    spacingadd: Float,
    includepad: Boolean
): StaticLayout {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        StaticLayout.Builder.obtain(source, 0, source.length, paint, width).apply {
            setAlignment(alignment)
            setLineSpacing(spacingadd, spacingmult)
            setIncludePad(includepad)
        }.build()
    } else {
        @Suppress("DEPRECATION")
        StaticLayout(source, paint, width, alignment, spacingmult, spacingadd, includepad)
    }
}


/**
 * Alternative to Resources.getDimension() for values that are TYPE_FLOAT.
 */
fun Resources.getFloatUsingCompat(@DimenRes resId: Int): Float {
    return ResourcesCompat.getFloat(this, resId)
}

/**
 * Return the Wifi config wrapped in quotes.
 */
fun WifiConfiguration.quoteSsidAndPassword(): WifiConfiguration {
    return WifiConfiguration().apply {
        SSID = this@quoteSsidAndPassword.SSID.wrapInQuotes()
        preSharedKey = this@quoteSsidAndPassword.preSharedKey.wrapInQuotes()
    }
}

/**
 * Return the Wifi config without quotes.
 */
fun WifiConfiguration.unquoteSsidAndPassword(): WifiConfiguration {
    return WifiConfiguration().apply {
        SSID = this@unquoteSsidAndPassword.SSID.unwrapQuotes()
        preSharedKey = this@unquoteSsidAndPassword.preSharedKey.unwrapQuotes()
    }
}

fun String.wrapInQuotes(): String {
    var formattedConfigString: String = this
    if (!startsWith("\"")) {
        formattedConfigString = "\"$formattedConfigString"
    }
    if (!endsWith("\"")) {
        formattedConfigString = "$formattedConfigString\""
    }
    return formattedConfigString
}

// Helper function for calling a share functionality.
// Should be used when user presses a share button/menu item.
fun String.createShareIntent(activity: Activity) {
    val shareIntent = ShareCompat.IntentBuilder.from(activity)
        .setText(this)
        .setType("text/plain")
        .createChooserIntent()
        .addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
    activity.startActivity(shareIntent)
}

fun String.unwrapQuotes(): String {
    var formattedConfigString: String = this
    if (formattedConfigString.startsWith("\"")) {
        if (formattedConfigString.length > 1) {
            formattedConfigString = formattedConfigString.substring(1)
        } else {
            formattedConfigString = ""
        }
    }
    if (formattedConfigString.endsWith("\"")) {
        if (formattedConfigString.length > 1) {
            formattedConfigString =
                formattedConfigString.substring(0, formattedConfigString.length - 1)
        } else {
            formattedConfigString = ""
        }
    }
    return formattedConfigString
}

/** Make the first instance of [boldText] in a CharSequece bold. */
fun CharSequence.makeBold(boldText: String): CharSequence {
    val start = indexOf(boldText)
    val end = start + boldText.length
    val span = StyleSpan(Typeface.BOLD)
    return if (this is Spannable) {
        setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        this
    } else {
        SpannableString(this).apply {
            setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }
}

/**
 * Having to suppress lint. Bug raised: 128789886
 */
@SuppressLint("WrongConstant")
fun AppCompatActivity.updateForTheme(theme: Theme) = when (theme) {
    Theme.DARK -> delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
    Theme.LIGHT -> delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
    Theme.SYSTEM -> delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
    Theme.BATTERY_SAVER -> delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO


 /*Theme.DARK -> delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
    Theme.LIGHT -> delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
    Theme.SYSTEM -> delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    Theme.BATTERY_SAVER -> delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY*/
}

/**
 * Combines this [LiveData] with another [LiveData] using the specified [combiner] and returns the
 * result as a [LiveData].
 */
fun <A, B, Result> LiveData<A>.combine(
    other: LiveData<B>,
    combiner: (A, B) -> Result
): LiveData<Result> {
    val result = MediatorLiveData<Result>()
    result.addSource(this) { a ->
        val b = other.value
        if (b != null) {
            result.postValue(combiner(a, b))
        }
    }
    result.addSource(other) { b ->
        val a = this@combine.value
        if (a != null) {
            result.postValue(combiner(a, b))
        }
    }
    return result
}

/**
 * Combines this [LiveData] with other two [LiveData]s using the specified [combiner] and returns
 * the result as a [LiveData].
 */
fun <A, B, C, Result> LiveData<A>.combine(
    other1: LiveData<B>,
    other2: LiveData<C>,
    combiner: (A, B, C) -> Result
): LiveData<Result> {
    val result = MediatorLiveData<Result>()
    result.addSource(this) { a ->
        val b = other1.value
        val c = other2.value
        if (b != null && c != null) {
            result.postValue(combiner(a, b, c))
        }
    }
    result.addSource(other1) { b ->
        val a = this@combine.value
        val c = other2.value
        if (a != null && c != null) {
            result.postValue(combiner(a, b, c))
        }
    }
    result.addSource(other2) { c ->
        val a = this@combine.value
        val b = other1.value
        if (a != null && b != null) {
            result.postValue(combiner(a, b, c))
        }
    }
    return result
}

fun View.doOnApplyWindowInsets(f: (View, WindowInsetsCompat, ViewPaddingState) -> Unit) {
    // Create a snapshot of the view's padding state
    val paddingState = createStateForView(this)
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        f(v, insets, paddingState)
        insets
    }
    requestApplyInsetsWhenAttached()
}

/**
 * Call [View.requestApplyInsets] in a safe away. If we're attached it calls it straight-away.
 * If not it sets an [View.OnAttachStateChangeListener] and waits to be attached before calling
 * [View.requestApplyInsets].
 */
fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        requestApplyInsets()
    } else {
        addOnAttachStateChangeListener(object : OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                v.requestApplyInsets()
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }
}
/*fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        // We're already attached, just request as normal
        requestApplyInsets()
    } else {
        // We're not attached to the hierarchy, add a listener to
        // request when we are
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                v.removeOnAttachStateChangeListener(this)
                v.requestApplyInsets()
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }
}*/

private fun createStateForView(view: View) = ViewPaddingState(
    view.paddingLeft,
    view.paddingTop, view.paddingRight, view.paddingBottom, view.paddingStart, view.paddingEnd
)

data class ViewPaddingState(
    val left: Int,
    val top: Int,
    val right: Int,
    val bottom: Int,
    val start: Int,
    val end: Int
)

@SuppressLint("NewApi") // Lint does not understand isAtLeastQ currently
fun DrawerLayout.shouldCloseDrawerFromBackPress(): Boolean {
    if (Build.VERSION.SDK_INT >= 29) {
        // If we're running on Q, and this call to closeDrawers is from a key event
        // (for back handling), we should only honor it IF the device is not currently
        // in gesture mode. We approximate that by checking the system gesture insets
        return rootWindowInsets?.let {
            val systemGestureInsets = it.systemGestureInsets
            return systemGestureInsets.left == 0 && systemGestureInsets.right == 0
        } ?: false
    }
    // On P and earlier, always close the drawer
    return true
}

/** Compatibility removeIf since it was added in API 24 */
fun <T> MutableCollection<T>.compatRemoveIf(predicate: (T) -> Boolean): Boolean {
    val it = iterator()
    var removed = false
    while (it.hasNext()) {
        if (predicate(it.next())) {
            it.remove()
            removed = true
        }
    }
    return removed
}


/** Convenience for callbacks/listeners whose return value indicates an event was consumed. */
inline fun consume(f: () -> Unit): Boolean {
    f()
    return true
}

/**
 * Allows calls like
 *
 * `viewGroup.inflate(R.layout.foo)`
 */
fun ViewGroup.inflate(@LayoutRes layout: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layout, this, attachToRoot)
}

/**
 * Allows calls like
 *
 * `supportFragmentManager.inTransaction { add(...) }`
 */
inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}

// region ViewModels

/**
 * For Actvities, allows declarations like
 * ```
 * val myViewModel = viewModelProvider(myViewModelFactory)
 * ```
 */
inline fun <reified VM : ViewModel> FragmentActivity.viewModelProvider(
    provider: ViewModelProvider.Factory
) =
    ViewModelProvider(this, provider).get(VM::class.java)

/**
 * For Fragments, allows declarations like
 * ```
 * val myViewModel = viewModelProvider(myViewModelFactory)
 * ```
 */
inline fun <reified VM : ViewModel> Fragment.viewModelProvider(
    provider: ViewModelProvider.Factory
) =
    ViewModelProvider(this, provider).get(VM::class.java)

/**
 * Like [Fragment.viewModelProvider] for Fragments that want a [ViewModel] scoped to the Activity.
 */
inline fun <reified VM : ViewModel> Fragment.activityViewModelProvider(
    provider: ViewModelProvider.Factory
) =
    ViewModelProvider(requireActivity(), provider).get(VM::class.java)

/**
 * Like [Fragment.viewModelProvider] for Fragments that want a [ViewModel] scoped to the parent
 * Fragment.
 */
inline fun <reified VM : ViewModel> Fragment.parentViewModelProvider(
    provider: ViewModelProvider.Factory
) =
    ViewModelProvider(parentFragment!!, provider).get(VM::class.java)

// endregion
// region Parcelables, Bundles

/** Write a boolean to a Parcel. */
fun Parcel.writeBooleanUsingCompat(value: Boolean) = ParcelCompat.writeBoolean(this, value)

/** Read a boolean from a Parcel. */
fun Parcel.readBooleanUsingCompat() = ParcelCompat.readBoolean(this)

// endregion
// region LiveData

/** Uses `Transformations.map` on a LiveData */
/*fun <X, Y> LiveData<X>.map(body: (X) -> Y): LiveData<Y> {
    return Transformations.map(this, body)
}*/

fun <T> MutableLiveData<T>.setValueIfNew(newValue: T) {
    if (this.value != newValue) value = newValue
}

// endregion


/**
 * Helper to force a when statement to assert all options are matched in a when statement.
 *
 * By default, Kotlin doesn't care if all branches are handled in a when statement. However, if you
 * use the when statement as an expression (with a value) it will force all cases to be handled.
 *
 * This helper is to make a lightweight way to say you meant to match all of them.
 *
 * Usage:
 *
 * ```
 * when(sealedObject) {
 *     is OneType -> //
 *     is AnotherType -> //
 * }.checkAllMatched
 */
val <T> T.checkAllMatched: T
    get() = this

// region Coroutines

/**
 * Cancel the Job if it's active.
 */
fun Job?.cancelIfActive() {
    if (this?.isActive == true) {
        cancel()
    }
}

// endregion

// region UI utils

// endregion

// region Firebase
suspend fun <T> Task<T>.suspendAndWait(): T =
    suspendCancellableCoroutine { continuation ->
        addOnSuccessListener { result ->
            continuation.resume(result)
        }
        addOnFailureListener { exception ->
            continuation.resumeWithException(exception)
        }
        addOnCanceledListener {
            continuation.resumeWithException(Exception("Firebase Task was cancelled"))
        }
    }
// endregion

/**
 * Helper to throw exceptions only in Debug builds, logging a warning otherwise.
 */
fun exceptionInDebug(t: Throwable) {
    if (BuildConfig.DEBUG) {
        throw t
    } else {
        Timber.e(t)
    }
}


private const val TAG = "Utils"

fun Activity.changeStatusBarColor(color: Int, isLight: Boolean) {
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.statusBarColor = color

    WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = isLight
}


fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)


fun String.maskNumber(): String {
    return mapIndexed { index, c ->
        if (index > 3 && length - index > 2) "X" else c
    }.joinToString("")
}

private fun isValidEmail(email: String): Boolean {
    //String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
    // final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    //String emailreg = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    //return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    val newEmailPattern =
        "[\\w\\-+.]+(\\.[\\w-_]+)*@[\\w-]+(\\.[\\w\\-_]+)*(\\.[\\w\\-]{2,})+"
    //String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\-+\\.+[a-z]+";
    return email.matches(newEmailPattern.toRegex())
}

fun copyToClipboard(context: Context, copyText: String?) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Orko Text", copyText)
    clipboard.setPrimaryClip(clip)
    context.toastSuccess("Copied to Clipboard")
}

open class NoUnderlineClickSpan(val context: Context) : ClickableSpan() {
    override fun updateDrawState(textPaint: TextPaint) {
        textPaint.isUnderlineText = true
        textPaint.color = ContextCompat.getColor(context, R.color.colorPrimary)
    }

    override fun onClick(widget: View) {}
}

fun Any.toJsonString(gson: Gson = Gson()): String {
    return gson.toJson(this)
}



@SuppressLint("SetTextI18n")
@Suppress("DEPRECATION") // It has been replaced by a Builder, which is minAPI 28, so OK for now
fun TextView.isResizableNeeded(
    fullText: String,
    maxLines: Int) : Boolean{

    // Since we take the string character by character, we don't want to break up the Windows-style
    // line endings.
    val adjustedText = fullText.replace("\r\n", "\n")
    // Check if even the text has to be resizable.
    val textLayout = StaticLayout(
        adjustedText,
        paint,
        width - paddingLeft - paddingRight,
        Layout.Alignment.ALIGN_NORMAL,
        lineSpacingMultiplier,
        lineSpacingExtra,
        includeFontPadding
    )
    return !(textLayout.lineCount <= maxLines || adjustedText.isEmpty())
}


@SuppressLint("SetTextI18n")
@Suppress("DEPRECATION") // It has been replaced by a Builder, which is minAPI 28, so OK for now
fun TextView.setResizableText(
    fullText: String,
    maxLines: Int,
    viewMore: Boolean,
    inlineViewMore: Boolean=false,
    applyExtraHighlights: ((Spannable) -> (Spannable))? = null
) {
    val width = width
    if (width <= 0) {
        post {
            setResizableText(fullText, maxLines, viewMore, applyExtraHighlights = applyExtraHighlights)
        }
        return
    }
    movementMethod = LinkMovementMethod.getInstance()
    // Since we take the string character by character, we don't want to break up the Windows-style
    // line endings.
    val adjustedText = fullText.replace("\r\n", "\n")
    // Check if even the text has to be resizable.
    val textLayout = StaticLayout(
        adjustedText,
        paint,
        width - paddingLeft - paddingRight,
        Layout.Alignment.ALIGN_NORMAL,
        lineSpacingMultiplier,
        lineSpacingExtra,
        includeFontPadding
    )
    if (textLayout.lineCount <= maxLines || adjustedText.isEmpty()) {
        // No need to add 'read more' / 'read less' since the text fits just as well (less than max lines #).
        val htmlText = adjustedText.replace("\n", "<br/>")
        text = addClickablePartTextResizable(
            fullText,
            maxLines,
            HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_COMPACT),
            null,
            viewMore,
            applyExtraHighlights
        )
        return
    }
    val charactersAtLineEnd = textLayout.getLineEnd(maxLines - 1)
    val suffixText =
        if (viewMore) resources.getString(R.string.resizable_text_read_more) else resources.getString(
            R.string.resizable_text_read_less
        )
    var charactersToTake = charactersAtLineEnd - suffixText.length / 2 // Good enough first guess
    if (charactersToTake <= 0) {
        // Happens when text is empty
        val htmlText = adjustedText.replace("\n", "<br/>")
        text = addClickablePartTextResizable(
            fullText,
            maxLines,
            HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_COMPACT),
            null,
            viewMore,
            applyExtraHighlights
        )
        return
    }
    if (!viewMore) {
        // We can set the text immediately because nothing needs to be measured
        val htmlText = adjustedText.replace("\n", "<br/>")
        text = addClickablePartTextResizable(
            fullText,
            maxLines,
            HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_COMPACT),
            suffixText,
            viewMore,
            applyExtraHighlights
        )
        return
    }
    val lastHasNewLine =
        adjustedText.substring(
            textLayout.getLineStart(maxLines - 1),
            textLayout.getLineEnd(maxLines - 1)
        )
            .contains("\n")
    var linedText = if (lastHasNewLine) {
        val charactersPerLine =
            textLayout.getLineEnd(0) / (textLayout.getLineWidth(0) / textLayout.ellipsizedWidth.toFloat())
        val lineOfSpaces =
            "\u00A0".repeat(charactersPerLine.roundToInt()) // non breaking space, will not be thrown away by HTML parser
        charactersToTake += lineOfSpaces.length - 1
        adjustedText.take(textLayout.getLineStart(maxLines - 1)) +
                adjustedText.substring(
                    textLayout.getLineStart(maxLines - 1),
                    textLayout.getLineEnd(maxLines - 1)
                )
                    .replace("\n", lineOfSpaces) +
                adjustedText.substring(textLayout.getLineEnd(maxLines - 1))
    } else {
        adjustedText
    }
    // Check if we perhaps need to even add characters? Happens very rarely, but can be possible if there was a long word just wrapped
    val shortenedString = linedText.take(charactersToTake)
    val shortenedStringWithSuffix = shortenedString + suffixText
    val shortenedStringWithSuffixLayout = StaticLayout(
        shortenedStringWithSuffix,
        paint,
        width - paddingLeft - paddingRight,
        Layout.Alignment.ALIGN_NORMAL,
        lineSpacingMultiplier,
        lineSpacingExtra,
        includeFontPadding
    )
    val modifier: Int
    if (shortenedStringWithSuffixLayout.getLineEnd(maxLines - 1) >= shortenedStringWithSuffix.length) {
        modifier = 1
        charactersToTake-- // We might just be at the right position already
    } else {
        modifier = -1
    }
    do {
        charactersToTake += modifier
        val baseString = linedText.take(charactersToTake)
        val appended = baseString + suffixText
        val newLayout = StaticLayout(
            appended,
            paint,
            width - paddingLeft - paddingRight,
            Layout.Alignment.ALIGN_NORMAL,
            lineSpacingMultiplier,
            lineSpacingExtra,
            includeFontPadding
        )
    } while ((modifier < 0 && newLayout.getLineEnd(maxLines - 1) < appended.length) ||
        (modifier > 0 && newLayout.getLineEnd(maxLines - 1) >= appended.length)
    )
    if (modifier > 0) {
        charactersToTake-- // We went overboard with 1 char, fixing that
    }
    // We need to convert newlines because we are going over to HTML now
    val htmlText = linedText.take(charactersToTake).replace("\n", "<br/>")
    text = addClickablePartTextResizable(
        fullText,
        maxLines,
        HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_COMPACT),
        suffixText,
        viewMore,
        applyExtraHighlights
    )
}

private fun TextView.addClickablePartTextResizable(
    fullText: String,
    maxLines: Int,
    shortenedText: Spanned,
    clickableText: String?,
    viewMore: Boolean,
    applyExtraHighlights: ((Spannable) -> (Spannable))? = null
): Spannable {
    val builder = SpannableStringBuilder(shortenedText)
    if (clickableText != null) {
        builder.append(clickableText)
//        val startIndexOffset = if (viewMore) 4 else 0 // Do not highlight the 3 dots and the space
        val startIndexOffset = if (viewMore) 0 else 0 // Do not highlight the 3 dots and the space

        builder.setSpan(
            object : NoUnderlineClickSpan(context) {
                override fun onClick(widget: View) {
                    if (viewMore) {
                        setResizableText(fullText, maxLines, false, applyExtraHighlights = applyExtraHighlights)
                    } else {
                        setResizableText(fullText, maxLines, true, applyExtraHighlights = applyExtraHighlights)
                    }
                }
            },
            builder.indexOf(clickableText) + startIndexOffset,
            builder.indexOf(clickableText) + clickableText.length,
            0
        )
    }
    if (applyExtraHighlights != null) {
        return applyExtraHighlights(builder)
    }
    return builder
}


