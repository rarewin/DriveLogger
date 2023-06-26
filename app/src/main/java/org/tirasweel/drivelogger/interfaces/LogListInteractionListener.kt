package org.tirasweel.drivelogger.interfaces

import org.tirasweel.drivelogger.db.DriveLog

interface LogListInteractionListener {

    /**
     * ログ追加のFloatingActionButtonがクリックされたときのリスナー
     */
    fun onFabAddClicked()

    /**
     * 既存のログのリストがクリックされたときのリスナー
     */
    fun onItemClicked(log: DriveLog)
}