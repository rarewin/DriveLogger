package org.tirasweel.drivelogger.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import io.realm.kotlin.ext.query
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.tirasweel.drivelogger.BuildConfig
import org.tirasweel.drivelogger.R
import org.tirasweel.drivelogger.compose.DriveLoggerApp
import org.tirasweel.drivelogger.db.DriveLog
import org.tirasweel.drivelogger.ui.theme.DriveLoggerTheme
import org.tirasweel.drivelogger.utils.ConfirmDialogFragment
import org.tirasweel.drivelogger.utils.RealmUtil
import org.tirasweel.drivelogger.viewmodels.DriveLogViewModel
import java.io.File
import java.io.FileWriter

class MainActivity : ComponentActivity() {

    companion object {
        private const val TAG: String =
            "${BuildConfig.APPLICATION_ID}.MainActivity"
    }

    private val driveLogViewModel: DriveLogViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DriveLoggerTheme {
                DriveLoggerApp(
                    driveLogViewModel = driveLogViewModel,
                )
            }
        }
    }

    private fun executeExport() {
        getExternalFilesDir("DriveLogs")?.let { dir ->
            val file = File(dir, "export.json")

            val export = {
                val writer = FileWriter(file)

                val realm = RealmUtil.createRealm()
                val driveLogs = realm.query<DriveLog>().find()

                driveLogs.forEach { log ->
                    writer.write(Json.encodeToString(log))
                }

                writer.close()

                Toast.makeText(this, R.string.message_export_file_successful, Toast.LENGTH_LONG)
                    .show()
            }

            // 既にファイルが存在する場合、ダイアログで確認する.
            if (file.exists()) {
                ConfirmDialogFragment.createAlertDialog(
                    this,
                    null,
                    getString(R.string.message_export_file_already_exists)
                ) { response ->
                    if (response) {
                        export()
                    }
                }.show()
            } else {
                export()
            }

        }
    }
}