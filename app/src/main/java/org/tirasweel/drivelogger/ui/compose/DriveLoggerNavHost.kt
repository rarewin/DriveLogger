package org.tirasweel.drivelogger.ui.compose

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.tirasweel.drivelogger.DriveLogEdit
import org.tirasweel.drivelogger.DriveLogList
import org.tirasweel.drivelogger.R
import org.tirasweel.drivelogger.classes.SortOrderType
import org.tirasweel.drivelogger.db.DriveLog
import org.tirasweel.drivelogger.interfaces.LogListInteractionListener
import org.tirasweel.drivelogger.ui.compose.drivelogedit.DriveLogEditScreen
import org.tirasweel.drivelogger.ui.compose.drivelogedit.DriveLogEditScreenClickListener
import org.tirasweel.drivelogger.ui.compose.driveloglist.DriveLogListScreen
import org.tirasweel.drivelogger.ui.compose.driveloglist.DriveLogListTopAppBarClickListener
import org.tirasweel.drivelogger.viewmodels.DriveLogViewModel
import timber.log.Timber

@Composable
fun DriveLoggerNavHost(
    navController: NavHostController,
    driveLogViewModel: DriveLogViewModel,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = DriveLogList.route,
        modifier = modifier,
    ) {

        // 走行ログ一覧
        composable(
            route = DriveLogList.route,
        ) {
            val context = LocalContext.current

            // エクスポート用ランチャー
            val exportLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.CreateDocument("application/json")
            ) { uri ->
                uri?.let {
                    try {
                        context.contentResolver.openOutputStream(it)?.use { outputStream ->
                            driveLogViewModel.exportDriveLogLists(outputStream)
                            Toast.makeText(
                                context,
                                R.string.message_export_file_successful,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } catch (e: Exception) {
                        Timber.e(e, "Failed to export drive logs")
                        Toast.makeText(
                            context,
                            "Export failed",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }

            // インポート用ランチャー
            val importLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.OpenDocument()
            ) { uri ->
                uri?.let {
                    try {
                        context.contentResolver.openInputStream(it)?.use { inputStream ->
                            driveLogViewModel.importDriveLogLists(inputStream)
                            Toast.makeText(
                                context,
                                R.string.message_import_file_successful,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } catch (e: Exception) {
                        Timber.e(e, "Failed to import drive logs")
                        Toast.makeText(
                            context,
                            "Import failed",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
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

                    override fun onConfirmOverwriteExport(confirm: Boolean) {
                        if (confirm) {
                            exportLauncher.launch("drivelogs_export.json")
                        }
                    }
                },
                appBarClickListener = object : DriveLogListTopAppBarClickListener {
                    override fun onClickExport() {
                        exportLauncher.launch("drivelogs_export.json")
                    }

                    override fun onClickImport() {
                        importLauncher.launch(arrayOf("application/json"))
                    }

                    override fun onSortOrderChanged(sortOrderType: SortOrderType) {
                        driveLogViewModel.updateDriveLogList()
                    }
                }
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
                }
            )
        }
    }
}