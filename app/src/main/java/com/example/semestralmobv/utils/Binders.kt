package com.example.semestralmobv.utils

import android.view.View
import androidx.databinding.BindingAdapter
import com.google.android.material.snackbar.Snackbar

@BindingAdapter(
    "showTextToast"
)
fun applyShowTextToast(
    view: View,
    message: String?
) {
    message?.let {
        Snackbar.make(view, it, Snackbar.LENGTH_SHORT).show()
    }
}