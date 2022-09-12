package org.tirasweel.drivelogger.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.Sort
import org.tirasweel.drivelogger.BuildConfig
import org.tirasweel.drivelogger.R
import org.tirasweel.drivelogger.databinding.FragmentLogEditBinding
import org.tirasweel.drivelogger.db.DriveLog
import org.tirasweel.drivelogger.utils.RealmUtil
import java.util.*

class LogEditFragment : Fragment() {
    companion object {
        private val TAG: String =
            "${BuildConfig.APPLICATION_ID}.LogEditFragment"

        /**
         * ログ編集フラグメントのインスタンス生成
         *
         * @param id  ID(nullなら新規作成)
         */
        fun newInstance(id: Long?): LogEditFragment {
            val fragment = LogEditFragment()

            Log.d(TAG, "ID is $id")

            val arguments = Bundle().apply {
                if (id != null) {
                    putLong(BundleKey.LogId.name, id)
                }
            }

            fragment.arguments = arguments

            return fragment
        }
    }

    enum class BundleKey {
        LogId
    }

    private var actualBinding: FragmentLogEditBinding? = null

    private val binding
        get() = actualBinding!!

    private var logId: Long? = null
    private var driveLog: DriveLog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            val id = it.getLong(BundleKey.LogId.name)
            if (id != 0L) {
                logId = id

                // IDからRealmのログデータを取得しておく
                val realm = RealmUtil.createRealm()
                driveLog = realm.query<DriveLog>("id == $0", logId).find().first()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        actualBinding = FragmentLogEditBinding.inflate(inflater, container, false)

        setupToolbar()

        driveLog?.let { log ->
            binding.inputDate.setText("${log.date}")
            binding.inputMileage.setText("${log.milliMileage}")
        }

        return binding.root
    }

    /**
     * メニューアイコン設定
     *
     * @param menuId  メニューのID
     */
    private fun isIconEnabled(menuId: Int?): Boolean {
        return when (menuId) {
            R.id.menu_register_log -> true  // 常に有効
            R.id.menu_delete_log -> (logId != null)  // 編集時のみ有効
            else -> {
                throw IllegalArgumentException("$menuId is not supported by this function")
            }
        }
    }

    /**
     * ツールバー設定をする
     */
    private fun setupToolbar() {
        binding.toolbar.apply {
            inflateMenu(R.menu.menu_items)

            val arrowBackIcon =
                ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_arrow_back_24, null)
            navigationIcon = arrowBackIcon

            setNavigationOnClickListener {
                back()
            }

            listOf(R.id.menu_register_log, R.id.menu_delete_log).forEach { id ->

                val enabled = isIconEnabled(id)

                menu.findItem(id).apply {
                    isVisible = enabled
                    isEnabled = enabled
                }
            }

            setOnMenuItemClickListener { item ->
                when (item?.itemId) {
                    R.id.menu_register_log -> {
                        Log.d(TAG, "log id: $logId")
                        if (logId == null) {
                            createNewLog()
                            activity?.finish()
                        }
                    }
                    R.id.menu_delete_log -> {
                    }
                    else -> {
                        throw IllegalStateException("$item is unexpected here")
                    }
                }

                true
            }
        }
    }

    /**
     * ログを作成する
     *
     * @todo ユーティリティ的な場所に移動
     */
    private fun createNewLog() {

        val longTime = binding.inputDate.text.toString().toLongOrNull() ?: 0
        val mileage = binding.inputMileage.text.toString().toLongOrNull() ?: 0

        val realm = RealmUtil.createRealm()

        realm.writeBlocking {
            val maxIdLog =
                realm.query<DriveLog>().sort("id", Sort.DESCENDING).limit(1).find()
            val maxId = maxIdLog.firstOrNull()?.id
            val newId = maxId?.plus(1) ?: 1L

            Log.d(TAG, "newId: $newId")

            val newDriveLog = DriveLog().apply {
                id = newId
                createdDate = Calendar.getInstance().timeInMillis
                updatedDate = Calendar.getInstance().timeInMillis
                date = longTime
                milliMileage = mileage * 1000
            }

            copyToRealm(newDriveLog)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        actualBinding = null
    }

    /**
     * 戻るを押されたとき. @todo 編集していればダイアログを表示する
     */
    private fun back() {
        activity?.finish()
    }

}
