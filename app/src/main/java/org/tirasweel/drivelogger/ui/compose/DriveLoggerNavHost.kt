package org.tirasweel.drivelogger.ui.compose

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.tirasweel.drivelogger.DriveLogEdit
import org.tirasweel.drivelogger.DriveLogList
import org.tirasweel.drivelogger.R
import org.tirasweel.drivelogger.RefuelLogList
import org.tirasweel.drivelogger.activities.ScreenMode
import org.tirasweel.drivelogger.classes.SortOrderType
import org.tirasweel.drivelogger.db.DriveLog
import org.tirasweel.drivelogger.interfaces.LogListInteractionListener
import org.tirasweel.drivelogger.ui.compose.bottomnav.DriveLoggerBottomNavigationClickListener
import org.tirasweel.drivelogger.ui.compose.drivelogedit.DriveLogEditScreen
import org.tirasweel.drivelogger.ui.compose.drivelogedit.DriveLogEditScreenClickListener
import org.tirasweel.drivelogger.ui.compose.driveloglist.DriveLogListScreen
import org.tirasweel.drivelogger.ui.compose.driveloglist.DriveLogListTopAppBarClickListener
import org.tirasweel.drivelogger.ui.compose.refuellist.RefuelListScreen
import org.tirasweel.drivelogger.viewmodels.DriveLogViewModel
import timber.log.Timber
import java.io.File

@Composable
fun DriveLoggerNavHost(
    navController: NavHostController,
    driveLogViewModel: DriveLogViewModel,
    modifier: Modifier = Modifier,
) {
    var currentScreenMode by remember { mutableStateOf(ScreenMode.DriveLoggingScreen) }

    NavHost(
        navController = navController,
        startDestination = DriveLogList.route,
        modifier = modifier,
    ) {

        val bottomNavigationClickListener = object : DriveLoggerBottomNavigationClickListener {
            override fun onModeChanged(mode: ScreenMode) {
                if (mode == currentScreenMode) {
                    Timber.d("clicked same mode with current one: $mode")
                    return
                } else {
                    currentScreenMode = mode
                    navController.navigate(mode.destination.route)
                }
            }
        }

        // 走行ログ一覧
        composable(
            route = DriveLogList.route,
        ) {
            val context = LocalContext.current
            val exportFile = context.getExternalFilesDir("DriveLogs")
                ?.let { dir ->
                    File(dir, "export.json")
                }

            DriveLogListScreen(
                driveLogViewModel = driveLogViewModel,
                clickListener = object : LogListInteractionListener {
                    override fun onFabAddClicked() {
                        driveLogViewModel.logFormState.resetLogForm()
                        navController.navigate(DriveLogEdit.route)
                    }

                    override fun onItemClicked(log: DriveLog) {
                        navController.navigate(DriveLogEdit.route(logId = log.id))
                    }

                    override fun onConfirmOverwriteExport(response: Boolean) {
                        if (response) {
                            exportFile?.let {
                                driveLogViewModel.exportDriveLogLists(it)
                                Toast.makeText(
                                    context,
                                    R.string.message_export_file_successful,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                },
                appBarClickListener = object : DriveLogListTopAppBarClickListener {
                    override fun onClickExport() {
                        if (exportFile?.exists() == true) {
                            driveLogViewModel.uiState.isConfirmDialogForOverwriteExport.value = true
                        } else {
                            exportFile?.let {
                                driveLogViewModel.exportDriveLogLists(it)
                                Toast.makeText(
                                    context,
                                    R.string.message_export_file_successful,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }

                    override fun onSortOrderChanged(sortOrderType: SortOrderType) {
                        driveLogViewModel.updateDriveLogList()
                    }
                },
                bottomNavigationClickListener = bottomNavigationClickListener,
            )
        }

        // 走行ログ編集画面
        composable(
            route = DriveLogEdit.routeWithArgs,
            arguments = DriveLogEdit.arguments,
        ) { navBackStackEntry ->
            val logId =
                navBackStackEntry.arguments?.getString(DriveLogEdit.logIdArg)?.toLongOrNull()

            logId?.let { id ->
                driveLogViewModel.logFormState.setEditingDriveLog(id)
            } ?: driveLogViewModel.logFormState.resetLogForm()

            DriveLogEditScreen(
                driveLogViewModel = driveLogViewModel,
                clickListener = object : DriveLogEditScreenClickListener {
                    override fun onClickBack() {
                        if (driveLogViewModel.logFormState.isEdited()) {
                            // 編集箇所があれば破棄してよいか確認する
                            driveLogViewModel.uiState
                                .isConfirmDialogForDiscardModificationDisplayed
                                .value = true
                        } else {
                            // 編集箇所がなければ何も確認せずに戻る
                            navController.popBackStack()
                        }
                    }

                    override fun onClickSave() {
                        if (driveLogViewModel.logFormState.isEdited()) {
                            // 編集箇所があれば確認する
                            driveLogViewModel.uiState.isConfirmDialogForOverwriteLog.value = true
                        } else {
                            Timber.e("invalid transition")
                        }
                    }

                    override fun onClickDelete() {
                        driveLogViewModel.uiState.isConfirmDialogForDeleteLogDisplayed.value = true
                    }

                    override fun onConfirmDiscardModification(confirm: Boolean) {
                        if (confirm) {
                            // 変更を破棄して前の画面に戻る
                            navController.popBackStack()
                        }
                    }

                    override fun onConfirmOverwrite(confirm: Boolean) {
                        if (confirm) {
                            // 変更を保存して前の画面に戻る
                            driveLogViewModel.saveCurrentLog()
                            navController.popBackStack()
                        }
                    }

                    override fun onConfirmDelete(confirm: Boolean) {
                        if (confirm) {
                            // 現在のログを削除して前の画面に戻る
                            driveLogViewModel.deleteEditingLog()
                            navController.popBackStack()
                        }
                    }
                },
                bottomNavigationClickListener = bottomNavigationClickListener,
            )
        }

        // 給油記録リスト
        composable(
            route = RefuelLogList.route,
        ) {
            RefuelListScreen(
                bottomNavigationClickListener = bottomNavigationClickListener,
            )
        }
    }
}