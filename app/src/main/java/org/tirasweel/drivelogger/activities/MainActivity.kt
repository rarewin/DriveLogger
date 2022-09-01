package org.tirasweel.drivelogger.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.tirasweel.drivelogger.databinding.ActivityMainBinding
import org.tirasweel.drivelogger.db.DriveLog
import org.tirasweel.drivelogger.fragments.LogEditFragment
import org.tirasweel.drivelogger.fragments.LogListFragment

class MainActivity : AppCompatActivity(),
    LogListFragment.LogListInteractionListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.fabNewDriveLog.setOnClickListener { _ ->
            val intent = Intent(this@MainActivity, LogEditActivity::class.java).apply {
                putExtra(LogEditActivity.IntentKey.OpenMode.name, LogEditFragment.OpenMode.New)
            }

            startActivity(intent)
        }
    }

    override fun onItemClick(log: DriveLog) {

        val intent = Intent(this@MainActivity, LogEditActivity::class.java).apply {
            putExtra(LogEditActivity.IntentKey.OpenMode.name, LogEditFragment.OpenMode.Update)
            putExtra(LogEditActivity.IntentKey.Date.name, log.date)
            putExtra(LogEditActivity.IntentKey.Mileage.name, log.milliMileage)
        }

        startActivity(intent)
    }
}