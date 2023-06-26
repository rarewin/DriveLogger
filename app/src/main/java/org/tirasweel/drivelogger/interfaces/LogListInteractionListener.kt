package org.tirasweel.drivelogger.interfaces

import org.tirasweel.drivelogger.db.DriveLog

interface LogListInteractionListener {
    fun onItemClick(log: DriveLog)
}