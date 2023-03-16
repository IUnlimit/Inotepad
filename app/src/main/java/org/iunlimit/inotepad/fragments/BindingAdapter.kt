package org.iunlimit.inotepad.fragments

import android.view.View
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import org.iunlimit.inotepad.R

class BindingAdapter {

    companion object {

        @BindingAdapter("android:emptyDatabase")
        @JvmStatic
        fun emptyDatabase(view: View, emptyDatabase: MutableLiveData<Boolean>) {
            val visibility = if (emptyDatabase.value!!) { View.VISIBLE } else { View.INVISIBLE }
            view.findViewById<ImageView>(R.id.no_data_imageView)?.visibility = visibility
            view.findViewById<TextView>(R.id.no_data_textView)?.visibility = visibility
        }

        @BindingAdapter("android:parseType")
        @JvmStatic
        fun parseType(view: Spinner, ordinal: Int) {
            view.setSelection(ordinal)
        }

        @BindingAdapter("android:parseTypeColor")
        @JvmStatic
        fun parseTypeColor(view: CardView, color: Int) {
            view.setCardBackgroundColor(
                view.context.getColor(color)
            )
        }

    }

}