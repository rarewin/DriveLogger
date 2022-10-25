package org.tirasweel.drivelogger.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import org.tirasweel.drivelogger.BuildConfig
import org.tirasweel.drivelogger.databinding.ActivityMainBinding
import org.tirasweel.drivelogger.db.DriveLog
import org.tirasweel.drivelogger.fragments.LogListFragment

class MainActivity : AppCompatActivity(),
    LogListFragment.LogListInteractionListener {

    companion object {
        private val TAG: String =
            "${BuildConfig.APPLICATION_ID}.MainActivity"
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.fabNewDriveLog.setOnClickListener { _ ->
            val intent = Intent(this@MainActivity, LogEditActivity::class.java)
            Log.d(TAG, "create new drive log")
            startActivity(intent)
        }
    }

    override fun onItemClick(log: DriveLog) {

        val intent = Intent(this@MainActivity, LogEditActivity::class.java).apply {
            putExtra(LogEditActivity.IntentKey.DriveLogId.name, log.id)
        }

        startActivity(intent)
    }
}