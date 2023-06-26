package org.tirasweel.drivelogger.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import org.tirasweel.drivelogger.BuildConfig
import org.tirasweel.drivelogger.db.DriveLog
import org.tirasweel.drivelogger.viewmodels.DriveLogListViewModel

class LogListFragment : Fragment() {

    interface LogListInteractionListener {
        fun onItemClick(log: DriveLog)
    }

    companion object {
        private const val TAG: String =
            "${BuildConfig.APPLICATION_ID}.LogListFragment"
    }

    private val viewModel: DriveLogListViewModel by viewModels()

    private var listener: LogListInteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        binding.logListSwipeRefresh.setOnRefreshListener {
//            updateList()
//            binding.logListSwipeRefresh.isRefreshing = false
//        }

        // updateList()

        return ComposeView(requireContext()).apply {
            // Dispose of the Composition when the view's LifecycleOwner
            // is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.reloadDriveLogs()
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