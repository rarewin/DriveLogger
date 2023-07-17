package org.tirasweel.drivelogger.compose

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
                        driveLogViewModel.clearDriveLog()
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
                driveLogViewModel.setDriveLog(id)
            } ?: driveLogViewModel.clearDriveLog()

            DriveLogEditScreen(
                driveLogViewModel = driveLogViewModel,
                clickListener = object : DriveLogEditScreenClickListener {
                    override fun onClickBack() {
                        navController.popBackStack()
                    }

                    override fun onClickSave() {
                        if (driveLogViewModel.isEdited()) {
                            driveLogViewModel.uiState.isConfirmDialogForSaveLog.value = true
                        } else {
                            driveLogViewModel.saveCurrentLog()
                            navController.popBackStack()
                        }
                    }

                    override fun onClickDelete() {
                        TODO("Not yet implemented")
                    }
                }
            )
        }
    }
}