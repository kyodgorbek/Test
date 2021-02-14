package com.example.test.utils

import android.view.View

fun <T : View> T.setVisibility(show: Boolean) {
    visibility = if (show) View.VISIBLE else View.GONE
}

fun <T : View> T.setSoftVisibility(show: Boolean) {
    visibility = if (show) View.VISIBLE else View.INVISIBLE
}