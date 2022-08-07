package org.tirasweel.drivelogger.fragments

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.realm.kotlin.query.RealmResults

import org.tirasweel.drivelogger.databinding.FragmentLogListBinding
import org.tirasweel.drivelogger.db.DriveLog

class LogRecyclerViewAdapter(
    private val values: RealmResults<DriveLog>
) : RecyclerView.Adapter<LogRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentLogListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentLogListBinding) : RecyclerView.ViewHolder(binding.root) {
    }

}