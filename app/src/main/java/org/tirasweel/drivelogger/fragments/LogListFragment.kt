package org.tirasweel.drivelogger.fragments

import android.content.Context
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

    interface LogListInteractionListener {
        fun onItemClick(log: DriveLog)
    }

    companion object {
        private val TAG: String =
            "${BuildConfig.APPLICATION_ID}.LogListFragment"
    }

    private var listener: LogListInteractionListener? = null

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

                driveLogs.forEach { log ->
                    val createdDate = Date(log.createdDate)
                    Log.d(TAG, "$createdDate")
                }

                adapter = LogRecyclerViewAdapter(driveLogs, listener)
            }
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is LogListInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("OnFragmentInteractionListener is not implemented")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}