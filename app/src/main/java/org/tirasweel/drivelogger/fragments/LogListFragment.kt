package org.tirasweel.drivelogger.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query

import org.tirasweel.drivelogger.R
import org.tirasweel.drivelogger.db.DriveLog

class LogListFragment : Fragment() {

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

                adapter = LogRecyclerViewAdapter(driveLogs)
            }
        }
        return view
    }
}