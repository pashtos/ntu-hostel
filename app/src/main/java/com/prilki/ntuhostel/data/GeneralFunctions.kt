package com.prilki.ntuhostel.data

import android.content.Context
import com.shashank.sony.fancytoastlib.FancyToast


fun toast(context: Context, message: String, style: Int) {
    FancyToast.makeText(
        context, message,
        FancyToast.LENGTH_SHORT,
        style,
        false
    ).show()
}
