package com.parithidb.taskiassessment.ui.event

import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.parithidb.taskiassessment.databinding.FragmentEventsByDateBinding
import com.parithidb.taskiassessment.util.cancelEventReminder
import com.parithidb.taskiassessment.util.scheduleEventReminder
import dagger.hilt.android.AndroidEntryPoint
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class EventsByDateFragment : Fragment() {
    private lateinit var binding: FragmentEventsByDateBinding
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
        binding = FragmentEventsByDateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvEventsByDate.layoutManager = LinearLayoutManager(requireContext())
        binding.rvEventsByDate.adapter = adapter

        val today = LocalDate.now()
        updateDate(today)

        binding.etDate.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select a date")
                .setSelection(today.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli())
                .build()

            datePicker.show(parentFragmentManager, "datePicker")

            datePicker.addOnPositiveButtonClickListener { selection ->
                val localDate =
                    Instant.ofEpochMilli(selection).atZone(ZoneId.systemDefault()).toLocalDate()
                updateDate(localDate)
            }
        }
    }

    private fun updateDate(date: LocalDate) {
        binding.etDate.setText(date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")))

        val zone = ZoneId.systemDefault()
        val startOfDay = date.atStartOfDay(zone).toInstant().toEpochMilli()
        val endOfDay = date.plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli() - 1

        viewModel.getEventsByDate(startOfDay, endOfDay).observe(viewLifecycleOwner) { events ->
            if (!events.isNullOrEmpty()) {
                binding.tvNoEvents.visibility = View.GONE
                adapter.submitList(events)
            } else {
                binding.tvNoEvents.visibility = View.VISIBLE
                adapter.submitList(emptyList())
            }
        }
    }
}
