package com.parithidb.taskiassessment.ui.event.upcomingEvents

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.parithidb.taskiassessment.data.database.entities.EventEntity
import com.parithidb.taskiassessment.databinding.FragmentUpcomingEventsBinding
import com.parithidb.taskiassessment.ui.event.EventAdapter
import com.parithidb.taskiassessment.ui.event.EventsFragmentDirections
import com.parithidb.taskiassessment.ui.event.EventsViewModel
import com.parithidb.taskiassessment.util.cancelEventReminder
import com.parithidb.taskiassessment.util.scheduleEventReminder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpcomingEventsFragment : Fragment() {
    private lateinit var binding: FragmentUpcomingEventsBinding
    private val viewModel: EventsViewModel by viewModels()
    private val adapter = EventAdapter(
        onEditClick = { event ->
            val action = EventsFragmentDirections
                .actionEventsFragmentToAddEditEventFragment(
                    eventId = event.eventId,
                    eventTitle = event.eventTitle,
                    eventDescription = event.eventDescription,
                    eventDateAndTime = event.eventDateAndTime
                )
            findNavController().navigate(action)
        },
        onDeleteClick = { eventId ->
            AlertDialog.Builder(requireContext())
                .setTitle("Delete event")
                .setMessage("Are you sure you want to delete this event?")
                .setPositiveButton("Yes", DialogInterface.OnClickListener { _, _ ->
                    viewModel.deleteEventById(eventId)
                    cancelEventReminder(requireContext(), eventId)
                })
                .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }).show()
        },
        onAlarmToggle = {event, isChecked ->
            viewModel.updateAlarmStatus(event.eventId, isChecked)
            if (isChecked) {
                scheduleEventReminder(requireContext(), event)
            } else {
                cancelEventReminder(requireContext(), event.eventId)
            }
        }
    )


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpcomingEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getUpcomingEvents().observe(viewLifecycleOwner, this::handleUpcomingEvents)

        binding.rvUpcomingEvents.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUpcomingEvents.adapter = adapter
    }

    private fun handleUpcomingEvents(eventEntities: List<EventEntity>?) {
        if (!eventEntities.isNullOrEmpty()) {
            binding.tvNoUpcomingEvents.visibility = View.GONE
            adapter.submitList(eventEntities)
        } else {
            binding.tvNoUpcomingEvents.visibility = View.VISIBLE
            adapter.submitList(emptyList())
        }
    }
}
