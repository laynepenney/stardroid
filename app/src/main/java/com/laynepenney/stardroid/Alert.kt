package com.laynepenney.stardroid

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

// TODO: res strings
class Alert(
    val msg: String?
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext(), theme)
            .setTitle("Error")
            .setMessage(msg ?: "unknown")
            .setNeutralButton("OK") { dialog, which -> dismiss() }
            .create()
    }
}