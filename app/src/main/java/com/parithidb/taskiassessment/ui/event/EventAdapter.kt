package com.parithidb.taskiassessment.ui.event

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.parithidb.taskiassessment.data.database.entities.EventEntity
import com.parithidb.taskiassessment.databinding.ItemEventBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EventAdapter(
    private val onEditClick: (EventEntity) -> Unit,
    private val onDeleteClick: (Int) -> Unit,
    private val onAlarmToggle: (EventEntity, Boolean) -> Unit
) : ListAdapter<EventEntity, EventAdapter.ViewHolder>(EventDiffCallback()) {

    class EventDiffCallback : DiffUtil.ItemCallback<EventEntity>() {
        override fun areItemsTheSame(oldItem: EventEntity, newItem: EventEntity): Boolean {
            return oldItem.eventId == newItem.eventId
        }

        override fun areContentsTheSame(oldItem: EventEntity, newItem: EventEntity): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.binding.tvTitle.text = item.eventTitle
            holder.binding.tvDescription.text = item.eventDescription
            val dateTime = Date(item.eventDateAndTime)
            val formatter = SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault())
            holder.binding.tvDateAndTime.text = formatter.format(dateTime)

            holder.binding.ivEdit.setOnClickListener {
                onEditClick(item)
            }

            holder.binding.ivDelete.setOnClickListener {
                onDeleteClick(item.eventId)
            }
            if (System.currentTimeMillis() >= item.eventDateAndTime) {
                holder.binding.alarmSwitch.visibility = View.GONE
            } else {
                holder.binding.alarmSwitch.visibility = View.VISIBLE
            }

            holder.binding.alarmSwitch.setOnCheckedChangeListener(null)
            holder.binding.alarmSwitch.isChecked = item.isAlarmEnabled

            holder.binding.alarmSwitch.setOnCheckedChangeListener { _, isChecked ->
                onAlarmToggle(item, isChecked)
            }

        }
    }

    inner class ViewHolder(val binding: ItemEventBinding) :
        RecyclerView.ViewHolder(binding.root)
}