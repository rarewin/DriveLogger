package org.tirasweel.drivelogger.ui.compose.driveloglist

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.tirasweel.drivelogger.R
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
    ListItem(
        modifier = modifier.clickable { clickListener?.onItemClicked(driveLog) },
        headlineContent = {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = driveLog.date.toLocalDateString(),
                    modifier = Modifier.weight(1f)
                )
                driveLog.fuelEfficient?.let { fuelEfficient ->
                    Text(text = stringResource(id = R.string.text_km_l, fuelEfficient))
                }
            }
        },
        supportingContent = {
            Column {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(id = R.string.text_km, driveLog.milliMileage / 1000.0),
                        modifier = Modifier.weight(1f)
                    )
                    driveLog.totalMilliMileage?.let { totalMilliMileage ->
                        Text(
                            text = stringResource(id = R.string.text_km, totalMilliMileage / 1000.0),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                if (driveLog.memo.isNotEmpty()) {
                    Text(text = driveLog.memo)
                }
            }
        }
    )
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
