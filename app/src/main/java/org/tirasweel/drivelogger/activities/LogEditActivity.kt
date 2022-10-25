package org.tirasweel.drivelogger.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import org.tirasweel.drivelogger.BuildConfig
import org.tirasweel.drivelogger.databinding.ActivityLogEditBinding

class LogEditActivity : AppCompatActivity() {

    companion object {
        private const val TAG: String =
            "${BuildConfig.APPLICATION_ID}.LogEditActivity"
    }

    enum class IntentKey {
        OpenMode,
        DriveLogId,
        Date,
        Mileage
    }

    private lateinit var binding: ActivityLogEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLogEditBinding.inflate(layoutInflater)

        val bundle = intent.extras

        val id = bundle?.getLong(IntentKey.DriveLogId.name)

        Log.d(TAG, "ID: $id")

        setContentView(binding.root)

//        supportFragmentManager.commit {
//            add(
//                R.id.logEditFragmentContainer,
//                LogEditFragment.newInstance(id)
//            )
//        }
    }
}