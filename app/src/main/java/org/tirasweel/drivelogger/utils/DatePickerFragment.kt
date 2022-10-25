package org.tirasweel.drivelogger.utils

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import org.tirasweel.drivelogger.BuildConfig

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    interface DatePickerFragmentListener {
        fun onDateSelected(yer: Int, month: Int, dayOfMonth: Int)
    }

    companion object {
        private const val TAG: String = "${BuildConfig.APPLICATION_ID}.DatePickerFragment"
    }

    enum class Keys {
        REQUEST,

        YEAR,
        MONTH,
        DAY;

        val key: String
            get() = this.name
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        var year = 0
        var month = 0
        var day = 0

        arguments?.apply {
            year = getInt(Keys.YEAR.name)
            month = getInt(Keys.MONTH.name)
            day = getInt(Keys.DAY.name)
        }

        return DatePickerDialog(requireActivity(), this, year, month - 1, day)
    }

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//
//        if (context is DatePickerFragmentListener) {
//            listener = context
//        } else {
//            throw RuntimeException("DatePickerFragmentListener is not implemented")
//        }
//    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val result = Bundle().apply {
            putInt(Keys.YEAR.key, year)
            putInt(Keys.MONTH.key, month)
            putInt(Keys.DAY.key, dayOfMonth)
        }

        parentFragmentManager.setFragmentResult(Keys.REQUEST.key, result)
    }
}