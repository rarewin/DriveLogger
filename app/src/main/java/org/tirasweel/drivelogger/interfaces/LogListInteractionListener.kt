package org.tirasweel.drivelogger.interfaces

import org.tirasweel.drivelogger.db.DriveLog

interface LogListInteractionListener {

    /** ログ追加のFloatingActionButtonがクリックされたときのリスナー */
    fun onFabAddClicked()

    /**
     * 既存のログのリストがクリックされたときのリスナー
     * @param log  ログのインスタンス
     */
    fun onItemClicked(log: DriveLog)

    /**
     * エクスポートファイルの上書き確認
     * @param response  返答 (true: OK, false: cancel)
     */
    fun onConfirmOverwriteExport(response: Boolean)
}