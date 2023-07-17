package org.tirasweel.drivelogger

import androidx.navigation.NavType
import androidx.navigation.navArgument

interface DriveLoggerDestination {
    val route: String
}

object DriveLogList : DriveLoggerDestination {
    override val route = "log_list"
}

object DriveLogEdit : DriveLoggerDestination {
    override val route = "log_edit"

    fun route(logId: Long) = "$route?$logIdArg=$logId"

    const val logIdArg = "log_id"
    val routeWithArgs = "$route?$logIdArg={$logIdArg}"
    val arguments = listOf(
        navArgument(logIdArg) {
            nullable = true
            type = NavType.StringType
        }
    )
}

val driveLogScreenList = listOf(DriveLogList, DriveLogEdit)