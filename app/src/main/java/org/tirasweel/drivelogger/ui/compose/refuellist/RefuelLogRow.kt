package org.tirasweel.drivelogger.ui.compose.refuellist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.tirasweel.drivelogger.db.RefuelLog
import org.tirasweel.drivelogger.ui.theme.DriveLoggerTheme
import org.tirasweel.drivelogger.utils.DateFormatConverter.Companion.toLocalDateString
import java.util.Calendar

@Composable
fun RefuelLogRow(
    modifier: Modifier = Modifier,
    refuelLog: RefuelLog,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
        ) {
            Text(text = refuelLog.date.toLocalDateString())
            Text(
                text = String.format(
                    "%.2f km / %.2f L",
                    refuelLog.milliMileage / 1000.0,
                    refuelLog.fuelMilliliters / 1000.0,
                )
            )
            Text(text = String.format("%d yen / L", refuelLog.fuelMilliPricePerLiter / 1000))
        }
    }
}

@Preview
@Composable
private fun RefuelLogRowPreview() {
    DriveLoggerTheme {
        RefuelLogRow(
            refuelLog = RefuelLog().apply {
                val cal = Calendar.getInstance()
                cal.set(2023, 7, 3, 23, 12, 5)

                date = cal.timeInMillis
                milliMileage = 632380
                fuelMilliliters = 34300
                fuelMilliPricePerLiter = 154 * 1000
            },
        )
    }
}