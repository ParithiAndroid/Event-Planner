package com.parithidb.taskiassessment.ui.event.addEdit

import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.datepicker.MaterialDatePicker
import com.parithidb.taskiassessment.data.database.entities.EventEntity
import com.parithidb.taskiassessment.databinding.FragmentAddEditEventBinding
import com.parithidb.taskiassessment.util.cancelEventReminder
import com.parithidb.taskiassessment.util.scheduleEventReminder
import dagger.hilt.android.AndroidEntryPoint
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class AddEditEventFragment : Fragment() {
    private lateinit var binding: FragmentAddEditEventBinding
    private val viewModel: AddEditEventViewModel by viewModels()
    private val args: AddEditEventFragmentArgs by navArgs()


    private var selectedDateMillis: Long? = null
    private var selectedHour: Int? = null
    private var selectedMinute: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddEditEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            })

        val isEditMode = args.eventId != -1

        (requireActivity() as AppCompatActivity).supportActionBar?.title =
            if (isEditMode) "Edit Event" else "Add Event"
        binding.btnSave.text = if (isEditMode) "Update Event" else "Save Event"

        if (isEditMode) {
            binding.etTitle.setText(args.eventTitle)
            binding.etDescription.setText(args.eventDescription)

            val localDateTime = Instant.ofEpochMilli(args.eventDateAndTime)
                .atZone(ZoneId.systemDefault()).toLocalDateTime()

            selectedDateMillis =
                localDateTime.toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant()
                    .toEpochMilli()
            selectedHour = localDateTime.hour
            selectedMinute = localDateTime.minute

            val dateFormatted =
                localDateTime.toLocalDate().format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
            val timeFormatted = localDateTime.format(DateTimeFormatter.ofPattern("hh:mm a"))

            binding.etDate.setText(dateFormatted)
            binding.etTime.setText(timeFormatted)

        }

        setupDateAndTimePickers()
        setupSaveButton()
    }

    private fun setupDateAndTimePickers() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select event date")
            .build()

        binding.etDate.setOnClickListener {
            datePicker.show(parentFragmentManager, "datePicker")
        }

        datePicker.addOnPositiveButtonClickListener { selection ->
            selectedDateMillis = selection
            val date = Instant.ofEpochMilli(selection)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
            binding.etDate.setText(date.format(DateTimeFormatter.ofPattern("dd MMM yyyy")))
        }

        binding.etTime.setOnClickListener {
            val cal = Calendar.getInstance()
            TimePickerDialog(requireContext(), { _, hour, minute ->
                selectedHour = hour
                selectedMinute = minute

                val isPM = hour >= 12
                val hour12 = if (hour % 12 == 0) 12 else hour % 12
                val amPm = if (isPM) "PM" else "AM"

                binding.etTime.setText(String.format("%02d:%02d %s", hour12, minute, amPm))
            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show()
        }
    }

    private fun setupSaveButton() {
        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString().trim()
            val description = binding.etDescription.text.toString().trim()

            when {
                title.isEmpty() -> {
                    binding.etTitle.error = "Title is required"
                    return@setOnClickListener
                }

                description.isEmpty() -> {
                    binding.etDescription.error = "Description is required"
                    return@setOnClickListener
                }

                selectedDateMillis == null -> {
                    binding.etDate.error = "Date is required"
                    return@setOnClickListener
                }

                selectedHour == null || selectedMinute == null -> {
                    binding.etTime.error = "Time is required"
                    return@setOnClickListener
                }
            }

            val epoch = getEpochMillis(selectedDateMillis!!, selectedHour!!, selectedMinute!!)

            val event = EventEntity(
                eventId = if (args.eventId != -1) args.eventId else 0,
                eventTitle = title,
                eventDescription = description,
                eventDateAndTime = epoch
            )

            if (args.eventId != -1) {
                viewModel.updateEvent(event)
                Toast.makeText(requireContext(), "Event updated", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.insertEvent(event)
                Toast.makeText(requireContext(), "Event added", Toast.LENGTH_SHORT).show()
            }

            findNavController().popBackStack()
        }
    }


    private fun getEpochMillis(dateMillis: Long, hour: Int, minute: Int): Long {
        val instant = Instant.ofEpochMilli(dateMillis)
        val zone = ZoneId.systemDefault()
        val localDate = instant.atZone(zone).toLocalDate()
        val localDateTime = localDate.atTime(hour, minute)
        return localDateTime.atZone(zone).toInstant().toEpochMilli()
    }

}