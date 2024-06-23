package com.rasel.androidbaseapp.ui.slideshow

import android.content.Context
import android.util.Size
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.rasel.androidbaseapp.R
import com.rasel.androidbaseapp.util.dp
import kotlin.math.roundToInt


class PopUpClass {
    //PopupWindow display method
    fun showPopupWindow(view: View) {
        //Create a View object yourself through inflater


        val inflater = LayoutInflater.from(view.context)
        val popupView = inflater.inflate(R.layout.pop_up_layout, null)

        //Specify the length and width through constants
        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.MATCH_PARENT

        //Make Inactive Items Outside Of PopupWindow
        val focusable = true

        //Create a window with our parameters
        val popupWindow = PopupWindow(popupView, width, height, focusable)

        //Set the location of the window on the screen
//        popupWindow.showAtLocation(view, Gravity.CENTER, view.x.toInt(), view.y.toInt())
        popupWindow.showAsDropDown(view)

        //Initialize the elements of our window, install the handler
        val test2 = popupView.findViewById<TextView>(R.id.titleText)
        test2.setText(R.string.view_sessions_text)

        val buttonEdit = popupView.findViewById<Button>(R.id.messageButton)
        buttonEdit.setOnClickListener { //As an example, display the message
            Toast.makeText(view.context, "Wow, popup action button", Toast.LENGTH_SHORT).show()
        }


        //Handler for clicking on the inactive zone of the window
        popupView.setOnTouchListener { v, event -> //Close the window when clicked
            popupWindow.dismiss()
            true
        }
    }

    fun showPopupWindow2(anchor: View) {

        //Specify the length and width through constants
        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT

        //Make Inactive Items Outside Of PopupWindow
        val focusable = true

        PopupWindow(anchor, width, height, focusable).apply {
//            isOutsideTouchable = false
            isFocusable = true
            val inflater = LayoutInflater.from(anchor.context)
            contentView = inflater.inflate(R.layout.pop_up_layout, null).apply {
                measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                )
            }
        }.also { popupWindow ->
            // Absolute location of the anchor view
            val location = IntArray(2).apply {
                anchor.getLocationOnScreen(this)
            }
            val size = Size(
                popupWindow.contentView.measuredWidth,
                popupWindow.contentView.measuredHeight
            )

            /*popupWindow.showAtLocation(
                anchor,
                Gravity.BOTTOM or Gravity.END,
                location[0] ,
                location[1]
            )*/

            popupWindow.showAtLocation(
                anchor,
                Gravity.END,
                0,
                0
            )

//            popupWindow.showAsDropDown(anchor, 0, (-0.2 * anchor.height).roundToInt(), Gravity.CENTER)
            anchor.isVisible = false

            popupWindow.setOnDismissListener {
                anchor.isVisible = true
            }
        }
    }
}