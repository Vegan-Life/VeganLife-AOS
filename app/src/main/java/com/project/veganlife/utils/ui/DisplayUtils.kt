package com.project.veganlife.utils.ui

import android.content.Context

class DisplayUtils {
    companion object {
        fun pxToDp(context: Context, px: Float): Float {
            val density = context.resources.displayMetrics.density
            return px / density
        }

        fun dpToPx(context: Context, dp: Float): Float {
            val density = context.resources.displayMetrics.density
            return Math.round(dp * density).toFloat()
        }
    }
}