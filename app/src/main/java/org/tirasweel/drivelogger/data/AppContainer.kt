package org.tirasweel.drivelogger.data

import io.realm.kotlin.Realm
import org.tirasweel.drivelogger.interfaces.DriveLogsRepository
import org.tirasweel.drivelogger.interfaces.RealmDriveLogsRepository
import org.tirasweel.drivelogger.utils.RealmUtil

interface AppContainer {
    val driveLogsRepository: DriveLogsRepository
}

class DefaultAppContainer : AppContainer {

    private val realm: Realm by lazy { RealmUtil.createRealm() }

    override val driveLogsRepository: DriveLogsRepository by lazy {
        RealmDriveLogsRepository(realm)
    }
}