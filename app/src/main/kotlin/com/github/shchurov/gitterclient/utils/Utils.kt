package com.github.shchurov.gitterclient.utils

import android.content.Context
import android.content.res.Resources
import android.support.annotation.ColorRes
import android.util.TypedValue
import android.widget.Toast
import com.github.shchurov.gitterclient.App
import com.github.shchurov.gitterclient.R

fun Context.showToast(textId: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(App.context, textId, duration).show()
}

fun getPrimaryColor(theme: Resources.Theme) : Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(R.attr.colorPrimary, typedValue, true)
    return typedValue.data
}