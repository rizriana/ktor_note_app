package com.learn.ktornoteapp.utils

import android.content.Context
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast

fun Context.showToast(
    message: String?, length: Int = Toast.LENGTH_SHORT,
) {
    message?.let {
        Toast.makeText(this, it, length).show()
    }
}

fun View.makeVisible() {
    this.visibility = View.VISIBLE
}

fun View.makeInvisible() {
    this.visibility = View.INVISIBLE
}
