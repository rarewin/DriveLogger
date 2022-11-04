package org.tirasweel.drivelogger.utils

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import org.tirasweel.drivelogger.R

class ConfirmDialogFragment : DialogFragment() {

    private lateinit var title: String
    private lateinit var message: String

    enum class Keys {
        REQUEST,

        RESULT,

        MESSAGE;

        val key: String
            get() = this.name
    }

    companion object {
        fun newInstance(
            fragment: Fragment,
            message: String,
            listener: (Boolean) -> Unit
        ): ConfirmDialogFragment {
            var dialog = ConfirmDialogFragment()

            dialog.apply {
                val bundle = Bundle()
                bundle.apply {
                    putString(Keys.MESSAGE.key, message)
                    arguments = bundle
                }

                fragment.childFragmentManager.setFragmentResultListener(
                    Keys.REQUEST.key,
                    fragment.viewLifecycleOwner
                ) { _, result ->
//                    assert(requestKey == Keys.REQUEST.key)

                    val response = result.getBoolean(Keys.RESULT.key)

                    listener.invoke(response)
                }
            }

            return dialog
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        arguments?.let {
            message = it.getString(Keys.MESSAGE.key, "")
        }

        val builder = AlertDialog.Builder(requireContext())

        builder
            .setMessage(message)
            .setPositiveButton(R.string.yes) { _, _ ->
                setFragmentResult(
                    Keys.REQUEST.name,
                    Bundle().apply {
                        putBoolean(Keys.RESULT.key, true)
                    }
                )
            }
            .setNegativeButton(R.string.no) { _, _ ->
                setFragmentResult(
                    Keys.REQUEST.name,
                    Bundle().apply {
                        putBoolean(Keys.RESULT.key, false)
                    }
                )
            }

        return builder.create()
    }
}

