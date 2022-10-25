package org.tirasweel.drivelogger.utils

import android.text.format.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DateFormatConverter {
    companion object {
        fun Date.toLocaleDateString(): String {
            val locale = Locale.getDefault()
            val format = DateFormat.getBestDateTimePattern(locale, "yyyyEEEMMMMd")

            return SimpleDateFormat(format, locale).format(this)
        }
    }
}