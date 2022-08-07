package org.tirasweel.drivelogger.fragments

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import io.realm.kotlin.query.RealmResults

import org.tirasweel.drivelogger.databinding.FragmentLogBinding
import org.tirasweel.drivelogger.db.DriveLog

class LogRecyclerViewAdapter(
    private val values: RealmResults<DriveLog>
) : RecyclerView.Adapter<LogRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentLogBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = item.id.toString()
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentLogBinding) : RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content

        override fun toString(): String {
            return super.toString() + " '${contentView.text}'"
        }
    }

}