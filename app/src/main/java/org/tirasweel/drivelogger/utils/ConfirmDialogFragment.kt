package org.tirasweel.drivelogger.utils

import android.app.Activity
import android.app.AlertDialog
import org.tirasweel.drivelogger.R

class ConfirmDialogFragment {

    companion object {
        fun createAlertDialog(
            activity: Activity,
            title: String?,
            message: String,
            listener: (Boolean) -> Unit
        ): AlertDialog {

            val dialog = AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.yes) { _, _ ->
                    listener(true)
                }
                .setNegativeButton(R.string.no) { _, _ ->
                    listener(false)
                }
                .create()

            return dialog
        }
    }
}

