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

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.TextView
import androidx.core.util.Pair
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.CompositeDateValidator
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.rasel.androidbaseapp.R
import com.rasel.androidbaseapp.data.models.ConferenceDay
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.TimeUnit

object TimeUtils {

    private const val TAG = "TimeUtils"

    /* const val CONFERENCE_DAY1_END = "2024-02-07T22:00:01-07:00"
    const val CONFERENCE_DAY1_START = "2024-02-07T07:00:00-07:00"
     ZonedDateTime.parse(CONFERENCE_DAY3_START)  */


    val CONFERENCE_DAY1_END = ZonedDateTime.now().plusDays(1L).plusHours(22)
    val CONFERENCE_DAY1_START = ZonedDateTime.now().plusDays(1L)
    val CONFERENCE_DAY2_END = ZonedDateTime.now().plusDays(2L).plusHours(22)
    val CONFERENCE_DAY2_START = ZonedDateTime.now().plusDays(2L)
    val CONFERENCE_DAY3_END = ZonedDateTime.now().plusDays(3L).plusHours(22)
    val CONFERENCE_DAY3_START = ZonedDateTime.now().plusDays(3L)

    val current: ZonedDateTime = ZonedDateTime.now().plusDays(1L)


    val ConferenceDays = listOf(
        ConferenceDay(
            CONFERENCE_DAY1_START,
            CONFERENCE_DAY1_END
        ),
        ConferenceDay(
            CONFERENCE_DAY2_START,
            CONFERENCE_DAY2_END
        ),
        ConferenceDay(
            CONFERENCE_DAY3_START,
            CONFERENCE_DAY3_END
        )
    )

    fun conferenceHasStarted(): Boolean {
        return ZonedDateTime.now().isAfter(ConferenceDays.first().start)
    }

    // region ZonedDateTime
    fun ZonedDateTime.toEpochMilli() = this.toInstant().toEpochMilli()
// endregion


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

    /* fun convertDate(date: String, inFormat: String?, outFormat: String?): String? {
        val sdf = SimpleDateFormat(inFormat, Locale.US)
        var formattedDate: String? = ""
        try {
            val convertedDate = sdf.parse(date)
            formattedDate = if (convertedDate != null) {
                SimpleDateFormat(outFormat, Locale.US).format(convertedDate)
            } else return ""
        } catch (e: ParseException) {
            Logger.e(TAG, "convertDate: " + e.message, e)
        } catch (e: NullPointerException) {
            Logger.e(TAG, "convertDate: " + e.message, e)
        }
        return formattedDate
    }*/

    /*fun calculateTotalDays(endDate: String, startDate: String, datePattern: String): Int {

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
    }*/
    fun calculateTotalDays(startDate: String?, endDate: String?, format: String?): Int {
        val dateFormat = SimpleDateFormat(format, Locale.US)
        var startD: Date? = null
        var endD: Date? = null
        try {
            startD = dateFormat.parse(startDate)
            endD = dateFormat.parse(endDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return if (startD == null || endD == null) 0 else {
            val diff = endD.time - startD.time
            val ddd = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
            (ddd + 1).toInt()
        }
    }


    fun getStringFromDate(date: Date, outFormat: String): String {
        return SimpleDateFormat(outFormat, Locale.US).format(date)
    }

    fun getDateFromTimeInMillis(time: Long): Date {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time
        return calendar.time
    }

    fun getStringDateFromTimeInMillis(time: Long, outFormat: String): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time
        return SimpleDateFormat(outFormat, Locale.US).format(calendar.time)
    }

    fun getDateFromString(dateText: String, inFormat: String?): Date? {
        val sdf = SimpleDateFormat(inFormat, Locale.US)
        return try {
            sdf.parse(dateText)
        } catch (e: ParseException) {
            e.printStackTrace()
            null
        }
    }

    @JvmStatic
    fun getDateFromTime(time: String?, inputFormat: String?): Date? {
        try {
            val simpleDateFormat = SimpleDateFormat(inputFormat, Locale.getDefault())
            val date = simpleDateFormat.parse(time)
            if (date != null) {
                val calendar = Calendar.getInstance()
                calendar.time = date
                calendar[Calendar.YEAR] = Calendar.getInstance()[Calendar.YEAR]
                calendar[Calendar.MONTH] = Calendar.getInstance()[Calendar.MONTH]
                calendar[Calendar.DATE] = Calendar.getInstance()[Calendar.DATE]
                return calendar.time
            }
        } catch (e: ParseException) {
            Timber.tag(TAG).e(e, "convertTo_24HourTime: %s", e.message)
        }
        return null
    }

    // checks if two date has same year and month
    fun isSameMonth(date1: Date?, date2: Date?): Boolean {
        val calendar1 = Calendar.getInstance()
        calendar1.time = date1
        val calendar2 = Calendar.getInstance()
        calendar2.time = date2
        val sameYear = calendar1[Calendar.YEAR] == calendar2[Calendar.YEAR]
        val sameMonth = calendar1[Calendar.MONTH] == calendar2[Calendar.MONTH]
        // boolean sameDay = calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH);
        return sameMonth && sameYear
    }

    @JvmStatic
    fun getDateOfBirth(year: String, month: String): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.YEAR, -year.toInt())
        calendar.add(Calendar.MONTH, -month.toInt())
        val tempMonth = calendar[Calendar.MONTH] + 1
        return calendar[Calendar.YEAR].toString() + "-" + tempMonth + "-" + calendar[Calendar.DAY_OF_MONTH]
    }

    @JvmStatic
    fun convert12HourTime(time24: String): String {
        var _12HourTime = ""
        try {
            val _24HourSDF = SimpleDateFormat("HH:mm", Locale.US)
            val _12HourSDF = SimpleDateFormat("hh:mm a", Locale.US)
            val _24HourDt = _24HourSDF.parse(time24)
            if (_24HourDt != null) {
                _12HourTime = _12HourSDF.format(_24HourDt)
            }
        } catch (e: Exception) {
            Log.e(TAG, "convert12HourTime: " + e.message, e)
        }
        return _12HourTime
    }

    @JvmStatic
    fun convertTo_24HourTime(time12: String?): String {
        var _24HourTime = ""
        try {
            val _24HourSDF = SimpleDateFormat("HH:mm", Locale.US)
            val _12HourSDF = SimpleDateFormat("hh:mm a", Locale.US)
            val _12HourDt = _12HourSDF.parse(time12)
            if (_12HourDt != null) {
                _24HourTime = _24HourSDF.format(_12HourDt)
            }
        } catch (e: Exception) {
            Log.e(TAG, "convertTo_24HourTime: " + e.message, e)
        }
        return _24HourTime
    }

    fun getSavedTimeKey(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.now().toString()
        } else {
            Date().time.toString()
        }
    }

    //checking if last fetched time exceeded the interval time
    fun isFetchNeeded(savedAt: String): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ChronoUnit.MINUTES.between(
                LocalDateTime.parse(savedAt),
                LocalDateTime.now()
            ) >= MINIMUM_INTERVAL_IN_MINUTE_ORDER
        } else {
            val interVal = Date().time - savedAt.toLong()
            interVal >= MINIMUM_INTERVAL_IN_MINUTE_ORDER * 1000 * 60
        }
    }


    @JvmStatic
    fun showYearDialog(mContext: Context?, etSelectText: EditText) {
        val year = Calendar.getInstance()[Calendar.YEAR]
        val d = Dialog(mContext!!)
        d.setTitle("Select Year")
        d.setContentView(R.layout.custom_year_dialog)
        val set = d.findViewById<TextView>(R.id.tvSet)
        val cancel = d.findViewById<TextView>(R.id.tvCancel)
        val year_text = d.findViewById<TextView>(R.id.year_text)
        val nopicker = d.findViewById<NumberPicker>(R.id.numberPicker1)
        nopicker.maxValue = year + 50
        nopicker.minValue = year - 50
        nopicker.wrapSelectorWheel = false
        nopicker.value = year
        nopicker.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        set.setOnClickListener { v: View? ->
            etSelectText.setText(nopicker.value.toString())
            etSelectText.error = null
            d.dismiss()
        }
        cancel.setOnClickListener { v: View? -> d.dismiss() }
        d.show()
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

    @JvmOverloads
    fun getDatePicker(
        fromNow: Boolean = false,
        futureDate: Int = 0,
        isTodaySelected: Boolean = true
    ): MaterialDatePicker<Long> {
        val calendar = Calendar.getInstance(Locale.getDefault())
        val constraintsBuilder = CalendarConstraints.Builder()
        val listValidators = ArrayList<CalendarConstraints.DateValidator>()
        if (fromNow) {
            //  CalendarConstraints.DateValidator dateValidatorMin = DateValidatorPointForward.from(calendar.getTimeInMillis());
            listValidators.add(DateValidatorPointForward.now())
            constraintsBuilder.setStart(calendar.timeInMillis)
        }
        if (futureDate > 0) {
            calendar.add(Calendar.DATE, futureDate)
            constraintsBuilder.setEnd(calendar.timeInMillis)
            val dateValidatorMax: CalendarConstraints.DateValidator =
                DateValidatorPointBackward.before(calendar.timeInMillis)
            listValidators.add(dateValidatorMax)
        }
        val validators = CompositeDateValidator.allOf(listValidators)
        constraintsBuilder.setValidator(validators)
        val builder = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
            .setCalendarConstraints(constraintsBuilder.build())
//        .setTheme(R.style.ThemeOverlay_App_DatePicker)
        if (isTodaySelected) {
            builder.setSelection(MaterialDatePicker.todayInUtcMilliseconds())
        }
        return builder.build()
    }

    @JvmOverloads
    fun getDateRangePicker(
        fromNow: Boolean = false,
        futureDate: Int = 0,
        currentMonthSelected: Boolean = true
    ): MaterialDatePicker.Builder<Pair<Long, Long>> {
        val calendar = Calendar.getInstance(Locale.getDefault())
        val constraintsBuilder = CalendarConstraints.Builder()
        val listValidators = ArrayList<CalendarConstraints.DateValidator>()
        if (fromNow) {
            //  CalendarConstraints.DateValidator dateValidatorMin = DateValidatorPointForward.from(calendar.getTimeInMillis());
            listValidators.add(DateValidatorPointForward.now())
            constraintsBuilder.setStart(calendar.timeInMillis)
        }
        if (futureDate > 0) {
            calendar.add(Calendar.DATE, futureDate)
            constraintsBuilder.setEnd(calendar.timeInMillis)
            val dateValidatorMax: CalendarConstraints.DateValidator =
                DateValidatorPointBackward.before(calendar.timeInMillis)
            listValidators.add(dateValidatorMax)
        }
        val validators = CompositeDateValidator.allOf(listValidators)
        constraintsBuilder.setValidator(validators)
        val builder = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Select dates")
            .setCalendarConstraints(constraintsBuilder.build())
            .setTheme(R.style.ThemeOverlay_App_DatePicker)
        if (currentMonthSelected) {
            builder.setSelection(
                Pair(
                    MaterialDatePicker.thisMonthInUtcMilliseconds(),
                    MaterialDatePicker.todayInUtcMilliseconds()
                )
            )
        }
        return builder
    }

}
