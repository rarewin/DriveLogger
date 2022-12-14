package org.tirasweel.drivelogger.fragments

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.realm.kotlin.query.RealmResults
import org.tirasweel.drivelogger.BuildConfig
import org.tirasweel.drivelogger.databinding.DrivelogItemBinding
import org.tirasweel.drivelogger.db.DriveLog
import org.tirasweel.drivelogger.utils.DateFormatConverter.Companion.toLocaleDateString
import java.util.*

class LogRecyclerViewAdapter(
    private val values: RealmResults<DriveLog>,
    private val clickListener: LogListFragment.LogListInteractionListener?
) : RecyclerView.Adapter<LogRecyclerViewAdapter.ViewHolder>() {

    companion object {
        private const val TAG: String =
            "${BuildConfig.APPLICATION_ID}.LogRecyclerViewAdapter"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DrivelogItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            item = values[position]

            Log.d(TAG, "item [position: $position ID:${item?.id}]")

            textViewId.text = item?.id.toString()
            textViewDate.text = Date(item?.date ?: 0).toLocaleDateString()
            textViewMileage.text = "${(item?.milliMileage?.toFloat() ?: 0.0f) / 1000.0} km"

            view.setOnClickListener {
                item?.let { item ->
                    clickListener?.onItemClick(item)
                }
            }
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: DrivelogItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var item: DriveLog? = null

        val textViewId = binding.id
        val textViewDate = binding.date
        val textViewMileage = binding.mileage

        val view = binding.root
    }
}