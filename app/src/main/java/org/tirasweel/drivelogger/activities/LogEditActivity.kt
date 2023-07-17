package org.tirasweel.drivelogger.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
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
import org.tirasweel.drivelogger.viewmodels.DriveLogViewModel

class LogEditActivity : ComponentActivity() {

    companion object {
        private const val TAG: String =
            "${BuildConfig.APPLICATION_ID}.LogEditActivity"
    }

    enum class IntentKey {
        DriveLogId,
    }

    enum class ResultKey {
        Result,
    }

    enum class ActivityResult {
        LogCreated,
        LogUpdated,
        LogDeleted,
        Canceled,
        Error,
    }

    private val viewModel: DriveLogViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = intent.extras

        bundle?.getLong(IntentKey.DriveLogId.name)?.let { id ->
            Log.d(TAG, "ID: $id")
            viewModel.setDriveLog(id)
        }

        onBackPressedDispatcher.addCallback(this) {
            confirmBack()
        }

        setContent {
            DriveLoggerTheme {
                DriveLogEditScreen(
                    modifier = Modifier.fillMaxWidth(),
                    clickListener = object : DriveLogEditScreenClickListener {
                        override fun onClickBack() {
                            Log.d(TAG, "onBackPressed")
                            confirmBack()
                        }

                        override fun onClickSave() {
                            ConfirmDialogFragment.createAlertDialog(
                                this@LogEditActivity,
                                null,
                                getString(R.string.message_save_drivelog)
                            ) { response ->
                                if (response) {
                                    viewModel.saveCurrentLog()
                                    val intent = Intent().apply {
                                        putExtra(
                                            ResultKey.Result.name,
                                            ActivityResult.LogCreated.ordinal
                                        )
                                    }
                                    setResult(Activity.RESULT_OK, intent)
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
                                    val intent = Intent().apply {
                                        putExtra(
                                            ResultKey.Result.name,
                                            ActivityResult.LogDeleted.ordinal
                                        )
                                    }
                                    setResult(Activity.RESULT_OK, intent)
                                    finish()
                                }
                            }.show()
                        }
                    },
                    driveLogViewModel = viewModel,
                )
            }
        }
    }

    private fun confirmBack() {
        // 編集されていなければ何も聞かずに編集終了
        if (!viewModel.isEdited()) {
            val intent = Intent().apply {
                putExtra(ResultKey.Result.name, ActivityResult.Canceled.ordinal)
            }
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        ConfirmDialogFragment.createAlertDialog(
            this,
            null,
            getString(R.string.message_discard_modification_drivelog)
        ) { response ->
            if (response) {
                val intent = Intent().apply {
                    putExtra(ResultKey.Result.name, ActivityResult.LogCreated.ordinal)
                }
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }.show()
    }
}