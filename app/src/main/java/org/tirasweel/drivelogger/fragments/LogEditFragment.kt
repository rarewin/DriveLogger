package org.tirasweel.drivelogger.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.viewModels
import org.tirasweel.drivelogger.BuildConfig
import org.tirasweel.drivelogger.R
import org.tirasweel.drivelogger.compose.DriveLogEditScreen
import org.tirasweel.drivelogger.compose.DriveLogEditScreenClickListener
import org.tirasweel.drivelogger.ui.theme.DriveLoggerTheme
import org.tirasweel.drivelogger.utils.ConfirmDialogFragment
import org.tirasweel.drivelogger.utils.DateFormatConverter.Companion.toLocaleDateString
import org.tirasweel.drivelogger.utils.DatePickerFragment
import org.tirasweel.drivelogger.viewmodels.DriveLogEditViewModel
import java.util.Calendar
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

    private val viewModel: DriveLogEditViewModel by viewModels()

    enum class BundleKey {
        LogId
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.getLong(BundleKey.LogId.name, -1L)?.let { id ->
            if (id < 0) {
                return@let
            }

            viewModel.setDriveLog(id)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        childFragmentManager.setFragmentResultListener(
            DatePickerFragment.Keys.REQUEST.key,
            this,
            this
        )

        return ComposeView(requireContext()).apply {
            // Dispose of the Composition when the view's LifecycleOwner
            // is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                DriveLoggerTheme {
                    DriveLogEditScreen(
                        modifier = Modifier.fillMaxWidth(),
                        clickListener = object : DriveLogEditScreenClickListener {
                            override fun onClickBack() {
                                Log.d(TAG, "onBackPressed")
                                confirmBack()
                            }

                            override fun onClickSave() {
                                val dialog = ConfirmDialogFragment.newInstance(
                                    this@LogEditFragment,
                                    null,
                                    getString(R.string.message_register_drivelog)
                                ) { response ->
                                    if (response) {
                                        viewModel.saveCurrentLog()
                                        activity?.finish()
                                    }
                                }

                                dialog.show(childFragmentManager, "REGISTER_LOG")
                            }

                            override fun onClickDate() {
                                val datePicker = DatePickerFragment()
                                val bundle = Bundle()

                                bundle.apply {
                                    viewModel.date.value.let { date ->
                                        val cal = Calendar.getInstance()
                                        cal.timeInMillis = date

                                        val year = cal.get(Calendar.YEAR)
                                        val month = cal.get(Calendar.MONTH) + 1  // 1月が0
                                        val day = cal.get(Calendar.DAY_OF_MONTH)

                                        Log.d(TAG, "$year-$month-$day")

                                        putInt(DatePickerFragment.Keys.YEAR.name, year)
                                        putInt(DatePickerFragment.Keys.MONTH.name, month)
                                        putInt(DatePickerFragment.Keys.DAY.name, day)
                                    }
                                }
                                datePicker.arguments = bundle
                                datePicker.show(childFragmentManager, "datePicker")
                            }

                            override fun onClickDelete() {
                                val dialog = ConfirmDialogFragment.newInstance(
                                    this@LogEditFragment,
                                    null,
                                    getString(R.string.message_remove_drivelog)
                                ) { response ->
                                    Log.d(TAG, "response is $response")

                                    if (response) {
                                        viewModel.deleteCurrentLog()
                                        activity?.finish()
                                    }
                                }

                                dialog.show(childFragmentManager, "DELETE_LOG")
                            }
                        },
                        driveLogEditViewModel = viewModel,
                    )
                }
            }
        }
    }

    /**
     * 戻る確認
     */
    private fun confirmBack() {

        // 編集されていなければ何も聞かずに編集終了
        if (!viewModel.isEdited()) {
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

                viewModel.setTextDate(dateString)
            }

            else -> {
                throw IllegalArgumentException("unknown result \"$requestKey\"")
            }
        }
    }
}
