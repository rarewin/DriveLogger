package org.tirasweel.drivelogger.compose

import android.content.Context
import android.content.res.Configuration
import android.util.AttributeSet
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.themeadapter.material.MdcTheme
import org.tirasweel.drivelogger.db.DriveLog
import org.tirasweel.drivelogger.fragments.LogListFragment
import org.tirasweel.drivelogger.utils.DateFormatConverter.Companion.toLocalDateString

class DriveLogRowComposeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    private val clickListener: LogListFragment.LogListInteractionListener?
) : AbstractComposeView(context, attrs, defStyle) {

    var driveLog by mutableStateOf<DriveLog?>(null)

    @Composable
    override fun Content() {
        MdcTheme {
            val driveLog = driveLog

            driveLog?.let {
                DriveLogRow(
                    driveLog = driveLog,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            clickListener?.onItemClick(driveLog)
                        }
                )
            }
        }
    }
}

@Composable
fun DriveLogRow(driveLog: DriveLog, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(text = driveLog.createdDate.toLocalDateString())
        Text(text = String.format("%.2f km/L", driveLog.milliMileage / 1000.0))
        Text(text = String.format("%.2f", driveLog.fuelEfficient ?: 0.0))
    }
}

@Preview
@Composable
fun DriveLogRowPreview() {
    MdcTheme {
        var driveLog: DriveLog = DriveLog().apply {
            createdDate = 111111
            milliMileage = 32040
            fuelEfficient = 24.8
        }
        DriveLogRow(
            driveLog,
            Modifier.fillMaxWidth()
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DriveLogRowDarkPreview() {
    MdcTheme {
        var driveLog: DriveLog = DriveLog().apply {
            createdDate = 111111
            milliMileage = 32040
            fuelEfficient = 24.8
        }
        DriveLogRow(
            driveLog,
            Modifier.fillMaxWidth()
        )
    }
}
