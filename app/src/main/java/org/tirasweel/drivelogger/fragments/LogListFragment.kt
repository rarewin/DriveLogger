package org.tirasweel.drivelogger.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.Sort
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.tirasweel.drivelogger.BuildConfig
import org.tirasweel.drivelogger.R
import org.tirasweel.drivelogger.databinding.FragmentLogListBinding
import org.tirasweel.drivelogger.db.DriveLog
import org.tirasweel.drivelogger.utils.ConfirmDialogFragment
import org.tirasweel.drivelogger.utils.RealmUtil
import java.io.File
import java.io.FileWriter

class LogListFragment : Fragment() {

    interface LogListInteractionListener {
        fun onItemClick(log: DriveLog)
    }

    companion object {
        private const val TAG: String =
            "${BuildConfig.APPLICATION_ID}.LogListFragment"
    }

    /**
     * @brief リストのソート順
     */
    enum class SortOrderType {
        AscendingDate,
        DescendingDate;

        /**
         * @brief ソート順(RealmのSortで)
         */
        val order
            get() = when (this) {
                AscendingDate -> Sort.ASCENDING
                DescendingDate -> Sort.DESCENDING
            }

        /**
         * @brief ソートに使用するプロパティ
         */
        val property
            get() = when (this) {
                AscendingDate, DescendingDate -> "date"
            }

        /**
         * @brief チェックを入れるアイテムID
         */
        val menuId
            get() = when (this) {
                AscendingDate -> R.id.list_menu_sort_date_ascending
                DescendingDate -> R.id.list_menu_sort_date_descending
            }
    }


    private var actualBinding: FragmentLogListBinding? = null

    private var listener: LogListInteractionListener? = null

    private val binding
        get() = actualBinding!!

    /**
     * @brief ソート順
     */
    private var sortOrder: SortOrderType = SortOrderType.DescendingDate  // TODO: デフォルト値の検討と保存の検討

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        actualBinding = FragmentLogListBinding.inflate(inflater, container, false)

        binding.loglistToolbar.apply {
            inflateMenu(R.menu.list_menu_items)

            menu.findItem(sortOrder.menuId).isChecked = true  // TODO: ソート方法の記憶

            setOnMenuItemClickListener { item ->
                when (item?.itemId) {
                    R.id.list_menu_sort_log -> {
                        Log.d(TAG, "sort")
                    }
                    R.id.list_menu_sort_date_ascending -> {
                        item.isChecked = true
                        sortOrder = SortOrderType.AscendingDate
                        Log.d(TAG, "ascending")
                        updateList()
                    }
                    R.id.list_menu_sort_date_descending -> {
                        item.isChecked = true
                        sortOrder = SortOrderType.DescendingDate
                        Log.d(TAG, "descending")
                        updateList()
                    }

                    R.id.list_menu_import_export -> {
                        Log.d(TAG, "import/export")
                    }

                    R.id.list_menu_export -> {
                        Log.d(TAG, "open export dialog")
                        executeExport()
                    }

                    R.id.list_menu_import -> {
                        Log.d(TAG, "open import dialog")
                        executeImport()
                    }

                    else -> {
                        throw IllegalStateException("$item is unexpected here")
                    }
                }
                true
            }
        }

        binding.logListSwipeRefresh.setOnRefreshListener {
            updateList()
            binding.logListSwipeRefresh.isRefreshing = false
        }

        updateList()

        return binding.root
    }

    private fun executeImport() {
        context?.getExternalFilesDir("DriveLogs")?.let { dir ->
            val file = File(dir, "export.json")

            if (!file.exists()) {
                Log.d(TAG, "no exported file")
                return@let
            }

            Log.d(TAG, "found exported file")
        }
    }

    private fun executeExport() {
        context?.getExternalFilesDir("DriveLogs")?.let { dir ->
            val file = File(dir, "export.json")

            val export = {
                val writer = FileWriter(file)

                val realm = RealmUtil.createRealm()
                val driveLogs = realm.query<DriveLog>().find()

                driveLogs.forEach { log ->
                    writer.write(Json.encodeToString(log))
                }

                writer.close()

                Toast.makeText(context, R.string.message_export_file_successful, Toast.LENGTH_LONG)
                    .show()
            }

            // 既にファイルが存在する場合、ダイアログで確認する.
            if (file.exists()) {
                val dialog = ConfirmDialogFragment.newInstance(
                    this,
                    null,
                    getString(R.string.message_export_file_already_exists)
                ) { response ->
                    if (response) {
                        export()
                    } else {
                        return@newInstance
                    }
                }
                dialog.show(childFragmentManager, "OVERWRITE_CHECK_EXPORT_FILE")
            } else {
                export()
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        actualBinding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is LogListInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("OnFragmentInteractionListener is not implemented")
        }
    }

    override fun onResume() {
        super.onResume()
        updateList()
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    private fun updateList() {
        // Set the adapter
        with(binding.logList) {
            layoutManager = LinearLayoutManager(context)

            val realm = RealmUtil.createRealm()
            val driveLogs = realm.query<DriveLog>().sort(sortOrder.property, sortOrder.order).find()

            adapter = LogRecyclerViewAdapter(driveLogs, listener)
        }
    }

}