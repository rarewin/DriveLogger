package org.tirasweel.drivelogger.compose

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.tirasweel.drivelogger.db.DriveLog
import org.tirasweel.drivelogger.interfaces.LogListInteractionListener
import org.tirasweel.drivelogger.ui.theme.DriveLoggerTheme
import org.tirasweel.drivelogger.utils.DateFormatConverter.Companion.toLocalDateString

@Composable
fun DriveLogRow(
    modifier: Modifier = Modifier,
    driveLog: DriveLog,
    clickListener: LogListInteractionListener? = null,
) {
    Card(
        colors = CardDefaults.cardColors(
            contentColor = MaterialTheme.colorScheme.primary,
        ),
        modifier = modifier
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clickable { clickListener?.onItemClicked(driveLog) }
    ) {
        Column(modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp)) {
            Text(text = driveLog.date.toLocalDateString())
            Text(text = String.format("%.2f km", driveLog.milliMileage / 1000.0))

            Text(text = driveLog.fuelEfficient?.let { fuelEfficient ->
                String.format("%.2f km/L", fuelEfficient)
            } ?: "--- km/L")
        }
    }
}

@Preview
@Composable
fun DriveLogRowPreview() {
    DriveLoggerTheme {
        val driveLog: DriveLog = DriveLog().apply {
            createdDate = 111111
            milliMileage = 32040
            fuelEfficient = 24.8
        }
        DriveLogRow(
            driveLog = driveLog,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DriveLogRowDarkPreview() {
    DriveLoggerTheme {
        val driveLog: DriveLog = DriveLog().apply {
            createdDate = 111111
            milliMileage = 32040
            fuelEfficient = 24.8
        }
        DriveLogRow(
            driveLog = driveLog,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
