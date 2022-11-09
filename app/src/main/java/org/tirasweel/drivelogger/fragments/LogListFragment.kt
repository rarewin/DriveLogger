package org.tirasweel.drivelogger.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.Sort
import org.tirasweel.drivelogger.BuildConfig
import org.tirasweel.drivelogger.R
import org.tirasweel.drivelogger.databinding.FragmentLogListBinding
import org.tirasweel.drivelogger.db.DriveLog
import org.tirasweel.drivelogger.utils.RealmUtil

class LogListFragment : Fragment() {

    interface LogListInteractionListener {
        fun onItemClick(log: DriveLog)
    }

    companion object {
        private const val TAG: String =
            "${BuildConfig.APPLICATION_ID}.LogListFragment"
    }

    private var actualBinding: FragmentLogListBinding? = null

    private var listener: LogListInteractionListener? = null

    private val binding
        get() = actualBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        actualBinding = FragmentLogListBinding.inflate(inflater, container, false)

        binding.loglistToolbar.apply {
            inflateMenu(R.menu.list_menu_items)
        }

        binding.logListSwipeRefresh.setOnRefreshListener {
            updateList()
            binding.logListSwipeRefresh.isRefreshing = false
        }

        updateList()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        actualBinding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is LogListInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("OnFragmentInteractionListener is not implemented")
        }
    }

    override fun onResume() {
        super.onResume()
        updateList()
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    private fun updateList() {
        // Set the adapter
        with(binding.logList) {
            layoutManager = LinearLayoutManager(context)

            val realm = RealmUtil.createRealm()
            val driveLogs = realm.query<DriveLog>().sort("date", Sort.ASCENDING).find()

            adapter = LogRecyclerViewAdapter(driveLogs, listener)
        }
    }
}