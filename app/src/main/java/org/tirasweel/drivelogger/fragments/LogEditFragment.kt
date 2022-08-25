package org.tirasweel.drivelogger.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import org.tirasweel.drivelogger.BuildConfig
import org.tirasweel.drivelogger.R
import org.tirasweel.drivelogger.databinding.FragmentLogEditBinding
import org.tirasweel.drivelogger.db.DriveLog
import java.util.*

class LogEditFragment : Fragment() {
    companion object {
        private val TAG: String =
            "${BuildConfig.APPLICATION_ID}.LogEditFragment"
    }

    private var actualBinding: FragmentLogEditBinding? = null

    private val binding
        get() = actualBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        actualBinding = FragmentLogEditBinding.inflate(inflater, container, false)

        binding.toolbar.apply {
            inflateMenu(R.menu.menu_items)

            val arrowBackIcon =
                ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_arrow_back_24, null)
            navigationIcon = arrowBackIcon

            setNavigationOnClickListener {
                back()
            }

            menu.findItem(R.id.menu_register_log).apply {
                isVisible = true
                isEnabled = true
            }

            setOnMenuItemClickListener { item ->
                when (item?.itemId) {
                    R.id.menu_register_log -> {
                        Toast.makeText(this.context, "TEST", Toast.LENGTH_LONG).show()
                        createNewLog()
                    }
                    else -> {
                        throw IllegalStateException("$item is unexpected here")
                    }
                }

                true
            }
        }


        return binding.root
    }

    /**
     * ログを作成する
     *
     * @todo ユーティリティ的な場所に移動
     */
    fun createNewLog() {

        val longTime = binding.inputDate.text.toString().toLongOrNull() ?: 0
        val mileage = binding.inputMileage.text.toString().toLongOrNull() ?: 0

        val config = RealmConfiguration.Builder(schema = setOf(DriveLog::class))
            .build()
        val realm: Realm = Realm.open(config)

        val newDriveLog = DriveLog().apply {
            createdDate = Calendar.getInstance().timeInMillis
            updatedDate = Calendar.getInstance().timeInMillis
            date = longTime
            milliMileage = mileage * 1000
        }

        realm.writeBlocking {
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
