package org.tirasweel.drivelogger.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.tirasweel.drivelogger.databinding.ActivityMainBinding
import org.tirasweel.drivelogger.db.DriveLog
import org.tirasweel.drivelogger.fragments.LogListFragment

class MainActivity : AppCompatActivity(),
    LogListFragment.LogListInteractionListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.fabNewDriveLog.setOnClickListener { view ->
            val intent = Intent(this@MainActivity, LogEditActivity::class.java)

            startActivity(intent)
        }
    }

    override fun onItemClick(log: DriveLog) {
        TODO("Not yet implemented")
    }
}