package org.tirasweel.drivelogger.fragments

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.tirasweel.drivelogger.BuildConfig
import org.tirasweel.drivelogger.compose.DriveLogRowComposeView
import org.tirasweel.drivelogger.db.DriveLog


class DriveLogsAdapter(
    private val clickListener: LogListFragment.LogListInteractionListener?
) : ListAdapter<DriveLog, DriveLogsAdapter.DriveLogViewHolder>(
    DIFF_CALLBACK
) {

    class DriveLogViewHolder(
        private val driveLogRowComposeView: DriveLogRowComposeView,
        private val clickListener: LogListFragment.LogListInteractionListener?
    ) : RecyclerView.ViewHolder(driveLogRowComposeView) {
        fun bindTo(driveLog: DriveLog) {
            driveLogRowComposeView.driveLog = driveLog
        }
    }

    companion object {
        private const val TAG: String = "${BuildConfig.APPLICATION_ID}.LogRecyclerViewAdapter"
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DriveLog>() {
            override fun areItemsTheSame(oldItem: DriveLog, newItem: DriveLog): Boolean {
                TODO("Not yet implemented")
            }

            override fun areContentsTheSame(oldItem: DriveLog, newItem: DriveLog): Boolean {
                TODO("Not yet implemented")
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DriveLogViewHolder {
        val driveLogRowComposeView =
            DriveLogRowComposeView(parent.context, clickListener = clickListener)

        return DriveLogViewHolder(driveLogRowComposeView, clickListener)
    }

    override fun onBindViewHolder(holder: DriveLogViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }
}
