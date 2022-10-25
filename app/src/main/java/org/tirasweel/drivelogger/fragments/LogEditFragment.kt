package org.tirasweel.drivelogger.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.Sort
import org.tirasweel.drivelogger.BuildConfig
import org.tirasweel.drivelogger.R
import org.tirasweel.drivelogger.databinding.FragmentLogEditBinding
import org.tirasweel.drivelogger.db.DriveLog
import org.tirasweel.drivelogger.utils.DateFormatConverter.Companion.toLocaleDateString
import org.tirasweel.drivelogger.utils.DatePickerFragment
import org.tirasweel.drivelogger.utils.RealmUtil
import java.util.*

class LogEditFragment : Fragment(), FragmentResultListener {
    companion object {
        private const val TAG: String =
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

    /**
     * ドライブログ
     */
    private var driveLog: DriveLog? = null

    /**
     * 反映前のドライブログ
     */
    private var tmpDriveLog: DriveLog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.getLong(BundleKey.LogId.name)?.let { id ->
            logId = id

            // IDからRealmのログデータを取得しておく
            val realm = RealmUtil.createRealm()
            driveLog = realm.query<DriveLog>("id == $0", logId).find().firstOrNull()
        }

        tmpDriveLog = driveLog?.let { driveLog ->
            DriveLog(driveLog)  // コピーする (もっと良い手ない? 昔はRealmに関数があったみたいだがl
        } ?: DriveLog().apply {
//            this.id = getNewDriveLogId() // TODO: ここで取得せず作成タイミングで取得した方がよさげ
//            logId = this.id
            date = Calendar.getInstance().timeInMillis  // 現在時刻を入れておく
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        actualBinding = FragmentLogEditBinding.inflate(inflater, container, false)

        setupToolbar()

        tmpDriveLog?.let { log ->
            val date = Date(log.date).toLocaleDateString()
            binding.inputDate.setText("$date")
        } ?: error("tmpDriveLog is null")

//        driveLog?.let { log ->
//            binding.inputDate.setText("${log.date}")
//            binding.inputMileage.setText("${log.milliMileage}")
//        } ?: run {
//            val today = Date(Calendar.getInstance().timeInMillis).toLocaleDateString()
//            binding.inputDate.setText(today)
//        }

        childFragmentManager.setFragmentResultListener(
            DatePickerFragment.Keys.REQUEST.key,
            this,
            this
        )

        binding.inputDate.setOnClickListener { /* view -> */
            val datePicker = DatePickerFragment()
            val bundle = Bundle()

            bundle.apply {
                tmpDriveLog?.let { driveLog ->

                    val cal = Calendar.getInstance()
                    cal.timeInMillis = driveLog.date

                    val year = cal.get(Calendar.YEAR)
                    val month = cal.get(Calendar.MONTH) + 1  // 1月が0
                    val day = cal.get(Calendar.DAY_OF_MONTH)

                    Log.d(TAG, "$year-$month-$day")

                    putInt(DatePickerFragment.Keys.YEAR.name, year)
                    putInt(DatePickerFragment.Keys.MONTH.name, month)
                    putInt(DatePickerFragment.Keys.DAY.name, day)
                } ?: error("tmpDriveLog is empty")
            }
            datePicker.arguments = bundle
            datePicker.show(this.childFragmentManager, "datePicker")
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

    private fun getNewDriveLogId(): Long {
        val realm = RealmUtil.createRealm()
        val maxIdLog =
            realm.query<DriveLog>().sort("id", Sort.DESCENDING).limit(1).find()
        val maxId = maxIdLog.firstOrNull()?.id

        return maxId?.plus(1) ?: 1L
    }

    private fun createOrUpdateDriveLog() {

    }

    /**
     * ログを作成する
     *
     * @todo ユーティリティ的な場所に移動
     */
    private fun createNewLog() {

        // val mileage = binding.inputMileage.text.toString().toLongOrNull() ?: 0

        val realm = RealmUtil.createRealm()

        val newId = getNewDriveLogId()
        Log.d(TAG, "newId: $newId")

        realm.writeBlocking {
            tmpDriveLog?.let { tmpDriveLog ->
                val newDriveLog = DriveLog(tmpDriveLog).apply {
                    id = newId
                    createdDate = Calendar.getInstance().timeInMillis
                    updatedDate = Calendar.getInstance().timeInMillis
                }

                copyToRealm(newDriveLog)
            } ?: error("tmpDriveLog is null")
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

    /**
     * 子フラグメントの結果を取得する
     */
    override fun onFragmentResult(requestKey: String, result: Bundle) {
        when (requestKey) {
            // DatePicker
            DatePickerFragment.Keys.REQUEST.key -> {
                val year = result.getInt(DatePickerFragment.Keys.YEAR.key)
                val month = result.getInt(DatePickerFragment.Keys.MONTH.key)
                val day = result.getInt(DatePickerFragment.Keys.DAY.key)

                Log.d(TAG, "DatePicker Result: $year-$month-$day")

                val cal = Calendar.getInstance()  // UTCになってるl?
                cal.timeZone = TimeZone.getDefault()
                cal.set(year, month, day)
                val dateString = cal.time.toLocaleDateString()
                binding.inputDate.setText(dateString)

                tmpDriveLog?.date = cal.timeInMillis

                Log.d(TAG, "date will be changed: ${driveLog?.date} -> ${tmpDriveLog?.date}")
            }
            else -> {
                throw IllegalArgumentException("unknown result \"$requestKey\"")
            }
        }
    }
}
