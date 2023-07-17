package org.tirasweel.drivelogger.compose

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.tirasweel.drivelogger.DriveLogEdit
import org.tirasweel.drivelogger.DriveLogList
import org.tirasweel.drivelogger.db.DriveLog
import org.tirasweel.drivelogger.interfaces.LogListInteractionListener
import org.tirasweel.drivelogger.viewmodels.DriveLogViewModel

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
                            Log.e("DriveLoggerNavHost", "invalid transition")
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