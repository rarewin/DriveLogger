package org.tirasweel.drivelogger.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.Sort
import org.tirasweel.drivelogger.BuildConfig
import org.tirasweel.drivelogger.R
import org.tirasweel.drivelogger.databinding.FragmentLogEditBinding
import org.tirasweel.drivelogger.db.DriveLog
import org.tirasweel.drivelogger.utils.ConfirmDialogFragment
import org.tirasweel.drivelogger.utils.DateFormatConverter.Companion.toLocalDateString
import org.tirasweel.drivelogger.utils.DateFormatConverter.Companion.toLocaleDateString
import org.tirasweel.drivelogger.utils.DatePickerFragment
import org.tirasweel.drivelogger.utils.RealmUtil
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

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

    /**
     * 編集前のドライブログ
     */
    private var driveLog: DriveLog? = null

    /**
     * ログの日付
     */
    private var logDate: Long? = null

    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        realm = RealmUtil.createRealm()

        arguments?.getLong(BundleKey.LogId.name, -1L)?.let { id ->
            if (id < 0) {
                return@let
            }

            // IDからRealmのログデータを取得しておく
            driveLog = realm.query<DriveLog>("id == $0", id).find().firstOrNull()
        }

        // 戻るボタンでの動作
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            Log.d(TAG, "onBackPressed")
            confirmBack()
        }.isEnabled = true
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        actualBinding = FragmentLogEditBinding.inflate(inflater, container, false)

        setupToolbar()

        // ログの内容を表示する. 新規作成なら今日の日付だけ入れておく.
        driveLog?.let { log ->
            realm.run {
                logDate = log.date

                val date = Date(log.date).toLocaleDateString()
                binding.inputDate.setText(date)

                val milliMileage = log.milliMileage

                binding.inputMileage.setText("${milliMileage / 1000.0}")

                log.fuelEfficient?.let {
                    binding.inputFuelEfficient.setText("$it")
                }

                log.totalMilliMileage?.let {
                    binding.inputTotalMileage.setText("${it / 1000.0}")
                }

                binding.inputMemo.setText(log.memo)
            }
        } ?: run {
            val date = Calendar.getInstance().timeInMillis
            logDate = date

            val dateString = Date(date).toLocaleDateString()
            binding.inputDate.setText(dateString)
        }

        childFragmentManager.setFragmentResultListener(
            DatePickerFragment.Keys.REQUEST.key,
            this,
            this
        )

        binding.inputDate.setOnClickListener { _ -> /* view -> */
            val datePicker = DatePickerFragment()
            val bundle = Bundle()

            bundle.apply {
                logDate?.let { date ->
                    val cal = Calendar.getInstance()
                    cal.timeInMillis = date

                    val year = cal.get(Calendar.YEAR)
                    val month = cal.get(Calendar.MONTH) + 1  // 1月が0
                    val day = cal.get(Calendar.DAY_OF_MONTH)

                    Log.d(TAG, "$year-$month-$day")

                    putInt(DatePickerFragment.Keys.YEAR.name, year)
                    putInt(DatePickerFragment.Keys.MONTH.name, month)
                    putInt(DatePickerFragment.Keys.DAY.name, day)
                } ?: error("logDate is empty")
            }
            datePicker.arguments = bundle
            datePicker.show(this.childFragmentManager, "datePicker")
        }

//        binding.inputMileage.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                if (binding.inputMileage.text.toString().toDoubleOrNull() == null) {
//                    binding.inputMileage.error = getString(R.string.hint_required)
//                }
//            }
//
//            override fun afterTextChanged(s: Editable?) {}
//        })

        binding.inputMileage.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                if (binding.inputMileage.text.toString().toDoubleOrNull() == null) {
                    binding.inputMileage.error = getString(R.string.hint_required)
                }
            }
        }

//        binding.root.setOnKeyListener { v, keyCode, event ->
//            Log.d(TAG, "KEY: ${v}, ${keyCode}, ${event}")
//
//            if (event.action == KeyEvent.ACTION_DOWN) {
//                when (keyCode) {
//                    KeyEvent.KEYCODE_BACK -> {
//                        confirmBack()
//                        return@setOnKeyListener true
//                    }
//                    else -> {
//                        return@setOnKeyListener false
//                    }
//                }
//            }
//
//            return@setOnKeyListener false
//        }

        binding.root.isFocusableInTouchMode = true
        binding.root.requestFocus()

        return binding.root
    }


    /**
     * メニューアイコン設定
     *
     * @param menuId  メニューのID
     */
    private fun isIconEnabled(menuId: Int?): Boolean {
        return when (menuId) {
            R.id.edit_menu_register_log -> true  // 常に有効
            R.id.edit_menu_delete_log -> (driveLog != null)  // 編集時のみ有効
            else -> {
                throw IllegalArgumentException("$menuId is not supported by this function")
            }
        }
    }


    /**
     * @brief 現在の編集内容とdriveLogを比較して, 編集されているかチェックする
     */
    private fun isEdited(): Boolean = driveLog?.let { log ->
        val edited = getEditedDriveLog()

        return !(log.date == edited.date
                && log.milliMileage == edited.milliMileage
                && log.fuelEfficient == edited.fuelEfficient
                && log.memo == edited.memo)
    } ?: true

    /**
     * 戻る確認
     */
    private fun confirmBack() {

        // 編集されていなければ何も聞かずに編集終了
        if (!isEdited()) {
            activity?.finish()
        }

        val dialog = ConfirmDialogFragment.newInstance(
            this@LogEditFragment,
            null,
            getString(R.string.message_discard_modification_drivelog)
        ) { response ->
            if (response) {
                activity?.finish()
            }
        }
        dialog.show(childFragmentManager, "DISCARD_CHANGES")
    }

    /**
     * ツールバー設定をする
     */
    private fun setupToolbar() {
        binding.toolbar.apply {
            inflateMenu(R.menu.edit_menu_items)

            val arrowBackIcon =
                ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_arrow_back_24, null)
            navigationIcon = arrowBackIcon

            setNavigationOnClickListener {
                confirmBack()
            }

            listOf(R.id.edit_menu_register_log, R.id.edit_menu_delete_log).forEach { id ->

                val enabled = isIconEnabled(id)

                menu.findItem(id).apply {
                    isVisible = enabled
                    isEnabled = enabled
                }
            }

            setOnMenuItemClickListener { item ->
                when (item?.itemId) {
                    R.id.edit_menu_register_log -> {

                        // 変換
                        val editedLog = try {
                            getEditedDriveLog()
                        } catch (e: Throwable) {
                            Log.e(TAG, "$e")
                            Toast.makeText(
                                activity,
                                R.string.message_invalid_input,
                                Toast.LENGTH_SHORT
                            ).show()

                            return@setOnMenuItemClickListener true
                        }

                        val dialog = ConfirmDialogFragment.newInstance(
                            this@LogEditFragment,
                            null,
                            getString(R.string.message_register_drivelog)
                        ) { response ->

                            if (response) {

                                driveLog?.let {

                                    realm.writeBlocking {

                                        findLatest(it)?.let { log ->
                                            Log.d(TAG, "date: ${log.date.toLocalDateString()}")

                                            log.updatedDate = Calendar.getInstance().timeInMillis
                                            log.date = editedLog.date
                                            log.milliMileage = editedLog.milliMileage
                                            log.fuelEfficient = editedLog.fuelEfficient
                                            log.totalMilliMileage = editedLog.totalMilliMileage
                                            log.memo = editedLog.memo
                                        }
                                    }
                                } ?: run {
                                    val newId = getNewDriveLogId()
                                    Log.d(TAG, "newId: $newId")

                                    realm.writeBlocking {
                                        val newDriveLog = editedLog.apply {
                                            id = newId
                                            createdDate = Calendar.getInstance().timeInMillis
                                            updatedDate = Calendar.getInstance().timeInMillis
                                        }

                                        copyToRealm(newDriveLog)
                                    }
                                }

                                activity?.finish()
                            }
                        }

                        dialog.show(childFragmentManager, "REGISTER_LOG")
                    }
                    R.id.edit_menu_delete_log -> {
                        val dialog = ConfirmDialogFragment.newInstance(
                            this@LogEditFragment,
                            null,
                            getString(R.string.message_remove_drivelog)
                        ) { response ->
                            Log.d(TAG, "response is $response")

                            if (response) {
                                realm.writeBlocking {
                                    driveLog?.let { log ->
                                        findLatest(log)?.let {
                                            delete(it)
                                        }
                                    } ?: error("driveLog is null?")
                                }

                                activity?.finish()
                            }
                        }

                        dialog.show(childFragmentManager, "DELETE_LOG")
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
     * @brief 現在編集中の内容からDriveLogを生成する
     * @return 生成されたDriveLogインスタンス
     */
    private fun getEditedDriveLog(): DriveLog {
        val edited = DriveLog()

        edited.apply {
            date = logDate ?: throw java.lang.IllegalArgumentException("logDate is null")

            val mileage: Double =
                binding.inputMileage.text.toString()
                    .toDoubleOrNull()
                    ?: throw java.lang.IllegalArgumentException("failed to convert into to double")
            milliMileage = (mileage * 1000.0).toLong()

            fuelEfficient =
                binding.inputFuelEfficient.text.toString().toDoubleOrNull()

            binding.inputTotalMileage.text.toString().toDoubleOrNull()?.let {
                totalMilliMileage = (it * 1000).toLong()
            }

            memo = binding.inputMemo.text.toString()

            if ((milliMileage < 0)
                || (fuelEfficient?.let { (it < 0) } == true)
                || (totalMilliMileage?.let { (it < 0) } == true)
            ) {
                throw java.lang.IllegalArgumentException("unexpected value")
            }
        }

        return edited

    }

    private fun getNewDriveLogId(): Long {
        val maxIdLog =
            realm.query<DriveLog>().sort("id", Sort.DESCENDING).limit(1).find()
        val maxId = maxIdLog.firstOrNull()?.id

        return maxId?.plus(1) ?: 1L
    }

    override fun onDestroyView() {
        super.onDestroyView()
        actualBinding = null
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

                logDate = cal.timeInMillis

                // Log.d(TAG, "date will be changed: ${driveLog?.date} -> ${tmpDriveLog?.date}")
            }
            else -> {
                throw IllegalArgumentException("unknown result \"$requestKey\"")
            }
        }
    }
}
