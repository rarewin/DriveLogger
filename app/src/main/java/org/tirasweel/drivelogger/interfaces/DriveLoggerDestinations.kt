package org.tirasweel.drivelogger.interfaces

import androidx.navigation.NavType
import androidx.navigation.navArgument

interface Destination {
    val title: String
    val route: String
}

object DriveLogList : Destination {
    override val title = "Drive Log"
    override val route = "log_list"
}

object DriveLogEdit : Destination {
    override val title = "Drive Log"
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

object RefuelLogList : Destination {
    override val title = "Refuel"
    override val route = "refuel_list"
}

val driveLogScreenList = listOf(DriveLogList, DriveLogEdit, RefuelLogList)