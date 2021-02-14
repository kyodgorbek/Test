package com.example.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.example.test.ui.UserListFragment
import com.example.test.utils.replace

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            replace(R.id.container, UserListFragment.newInstance())
        }
    }
}