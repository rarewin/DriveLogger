package org.tirasweel.drivelogger

import androidx.navigation.NamedNavArgument
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
    val arguments: List<NamedNavArgument> = listOf(
        navArgument(logIdArg) {
            type = NavType.StringType
            nullable = true
            defaultValue = null
        }
    )
}

object RefuelLogList : DriveLoggerDestination {
    override val route = "refuel_log_list"
}

object RefuelLogEdit : DriveLoggerDestination {
    override val route = "refuel_log_edit"

    fun route(logId: Long) = "$route?$logIdArg=$logId"

    const val logIdArg = "log_id"
    val routeWithArgs = "$route?$logIdArg={$logIdArg}"
    val arguments: List<NamedNavArgument> = listOf(
        navArgument(logIdArg) {
            type = NavType.StringType
            nullable = true
            defaultValue = null
        }
    )
}

val driveLogScreenList = listOf(DriveLogList, DriveLogEdit, RefuelLogList, RefuelLogEdit)