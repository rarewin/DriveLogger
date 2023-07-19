package org.tirasweel.drivelogger.classes

import org.tirasweel.drivelogger.interfaces.DriveLogList
import org.tirasweel.drivelogger.interfaces.RefuelLogList

/**
 * Navigationで移動するスクリーン
 */
enum class NavigationScreen {

    /** ドライブログ */
    DriveLogScreen,

    /** 給油ログ */
    RefuelLogScreen;

    /**
     * ルートを取得する
     * @return ルートの文字列
     */
    fun toRoute(): String = when (this) {
        DriveLogScreen -> DriveLogList.route
        RefuelLogScreen -> RefuelLogList.route
    }
}
