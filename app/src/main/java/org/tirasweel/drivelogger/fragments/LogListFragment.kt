package org.tirasweel.drivelogger.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import org.tirasweel.drivelogger.BuildConfig
import org.tirasweel.drivelogger.R
import org.tirasweel.drivelogger.db.DriveLog
import java.util.*

class LogListFragment : Fragment() {

    companion object {
        private val TAG: String =
            "${BuildConfig.APPLICATION_ID}.LogListFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_log_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)

                val config = RealmConfiguration.Builder(schema = setOf(DriveLog::class))
                    .build()
                val realm: Realm = Realm.open(config)
                val driveLogs = realm.query<DriveLog>().find()

                driveLogs.forEach {
                    val createdDate = Date(it.createdDate)
                    Log.d(TAG, "$createdDate")
                }

                adapter = LogRecyclerViewAdapter(
                    driveLogs,
                    object : LogListClickListener {
                        override fun onItemClick(log: DriveLog) {
                            TODO("Not yet implemented")
                        }
                    }
                )
            }
        }
        return view
    }
}