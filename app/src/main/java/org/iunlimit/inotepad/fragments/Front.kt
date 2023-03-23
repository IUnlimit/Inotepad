package org.iunlimit.inotepad.fragments

import android.graphics.Typeface
import android.widget.TextView

fun setFont(view: TextView) {
    Typeface.createFromAsset(
        view.context.assets,
        "font/jetbrains_mono_regular_nerd_font_complete.ttf"
    ).let {
        view.typeface = it
    }
}