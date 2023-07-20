package org.tirasweel.drivelogger.utils

import android.text.format.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DateFormatConverter {
    companion object {
        fun Date.toLocaleDateString(): String {
            val locale = Locale.getDefault()
            val format = DateFormat.getBestDateTimePattern(locale, "yyyyEEEMMMMd")

            return SimpleDateFormat(format, locale).format(this)
        }

        fun Long.toLocalDateString(): String {
            val cal = Calendar.getInstance()
            cal.timeInMillis = this

            return cal.time.toLocaleDateString()
        }
    }
}