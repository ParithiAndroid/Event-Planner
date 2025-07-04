package com.parithidb.taskiassessment.ui.event.addEdit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parithidb.taskiassessment.data.database.entities.EventEntity
import com.parithidb.taskiassessment.data.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditEventViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val eventId: Int = savedStateHandle["eventId"] ?: -1

    fun insertEvent(eventEntity: EventEntity) {
        viewModelScope.launch {
            eventRepository.insertEvent(eventEntity)
        }
    }

    fun updateEvent(event: EventEntity) = viewModelScope.launch {
        eventRepository.updateEvent(event)
    }

    fun updateAlarmStatus(eventId: Int, isEnabled: Boolean) {
        viewModelScope.launch {
            eventRepository.updateAlarmStatus(eventId, isEnabled)
        }
    }
}