package org.tirasweel.drivelogger.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import org.tirasweel.drivelogger.R
import org.tirasweel.drivelogger.databinding.ActivityLogEditBinding
import org.tirasweel.drivelogger.fragments.LogEditFragment

class LogEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLogEditBinding.inflate(layoutInflater)

        setContentView(binding.root)

        supportFragmentManager.commit {
            add(R.id.logEditFragmentContainer, LogEditFragment())
        }
    }
}