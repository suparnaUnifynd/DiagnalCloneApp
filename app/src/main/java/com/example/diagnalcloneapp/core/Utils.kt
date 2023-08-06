package com.example.diagnalcloneapp.core

import android.content.Context
import android.util.TypedValue
import kotlin.math.roundToInt

object Utils {

    fun dpToPx(context: Context, dp: Number): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics).roundToInt();
    }
}