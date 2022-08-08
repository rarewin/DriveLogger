package org.tirasweel.drivelogger.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.tirasweel.drivelogger.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

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
}