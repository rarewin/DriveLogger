package org.tirasweel.drivelogger.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import org.tirasweel.drivelogger.BuildConfig
import org.tirasweel.drivelogger.R
import org.tirasweel.drivelogger.compose.DriveLogEditScreen
import org.tirasweel.drivelogger.compose.DriveLogEditScreenClickListener
import org.tirasweel.drivelogger.ui.theme.DriveLoggerTheme
import org.tirasweel.drivelogger.utils.ConfirmDialogFragment
import org.tirasweel.drivelogger.viewmodels.DriveLogEditViewModel

class LogEditActivity : ComponentActivity() {

    companion object {
        private const val TAG: String =
            "${BuildConfig.APPLICATION_ID}.LogEditActivity"
    }

    enum class IntentKey {
        OpenMode,
        DriveLogId,
        Date,
        Mileage
    }

    private val viewModel: DriveLogEditViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = intent.extras

        bundle?.getLong(IntentKey.DriveLogId.name)?.let { id ->
            Log.d(TAG, "ID: $id")
            viewModel.setDriveLog(id)
        }

        setContent {
            DriveLoggerTheme {
                DriveLogEditScreen(
                    modifier = Modifier.fillMaxWidth(),
                    clickListener = object : DriveLogEditScreenClickListener {
                        override fun onClickBack() {
                            Log.d(TAG, "onBackPressed")
                            // confirmBack()
                        }

                        override fun onClickSave() {
                            ConfirmDialogFragment.createAlertDialog(
                                this@LogEditActivity,
                                null,
                                getString(R.string.message_register_drivelog)
                            ) { response ->
                                if (response) {
                                    viewModel.saveCurrentLog()
                                    finish()
                                }
                            }.show()
                        }

                        override fun onClickDelete() {
                            ConfirmDialogFragment.createAlertDialog(
                                this@LogEditActivity,
                                null,
                                getString(R.string.message_remove_drivelog)
                            ) { response ->
                                Log.d(TAG, "response is $response")

                                if (response) {
                                    viewModel.deleteCurrentLog()
                                    finish()
                                }
                            }.show()
                        }
                    },
                    driveLogEditViewModel = viewModel,
                )
            }
        }

    }
}