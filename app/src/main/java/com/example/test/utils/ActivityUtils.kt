package com.example.test.utils

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.test.R

fun AppCompatActivity.replaceBackStack(@IdRes container: Int, fragment: Fragment) {
    supportFragmentManager
        .beginTransaction()
        .setCustomAnimations(
            R.anim.slide_in_right,
            R.anim.slide_out_left,
            R.anim.slide_in_left,
            R.anim.slide_out_right
        )
        .replace(container, fragment)
        .addToBackStack(null)
        .commit()
}

fun AppCompatActivity.replace(@IdRes container: Int, fragment: Fragment) {
    supportFragmentManager
        .beginTransaction()
        .replace(container, fragment)
        .commit()
}