package org.tirasweel.drivelogger.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import org.tirasweel.drivelogger.BuildConfig
import org.tirasweel.drivelogger.R
import org.tirasweel.drivelogger.databinding.ActivityLogEditBinding
import org.tirasweel.drivelogger.fragments.LogEditFragment

class LogEditActivity : AppCompatActivity() {

    companion object {
        private val TAG: String =
            "${BuildConfig.APPLICATION_ID}.LogEditActivity"
    }

    enum class IntentKey {
        OpenMode,
        Date,
        Mileage
    }

    private lateinit var binding: ActivityLogEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLogEditBinding.inflate(layoutInflater)

        val bundle = intent.extras

        val mode = bundle?.get(IntentKey.OpenMode.name) as LogEditFragment.OpenMode?
        val date = bundle?.getLong(IntentKey.Date.name)
        val mileage = bundle?.getLong(IntentKey.Mileage.name)

        Log.d(TAG, "mode: $mode, date: $date, mileage: $mileage")

        if (mode == null) {
            TODO("Not yet implemented (OpenMode is required)")
        }

        setContentView(binding.root)

        supportFragmentManager.commit {
            add(
                R.id.logEditFragmentContainer,
                LogEditFragment.newInstance(mode)
            )
        }
    }
}