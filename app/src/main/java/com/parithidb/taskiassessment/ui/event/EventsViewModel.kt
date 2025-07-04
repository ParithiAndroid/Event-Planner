package com.parithidb.taskiassessment.ui.event

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parithidb.taskiassessment.data.database.entities.EventEntity
import com.parithidb.taskiassessment.data.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    fun getUpcomingEvents(): LiveData<List<EventEntity>> {
        return eventRepository.getUpcomingEvents()
    }

    fun getEventsByDate(startOfDay: Long, endOfDay: Long): LiveData<List<EventEntity>> {
        return eventRepository.getEventsByDate(startOfDay, endOfDay)
    }

    fun deleteEventById(eventId: Int) {
        viewModelScope.launch {
            eventRepository.deleteEventById(eventId)
        }
    }

    fun updateAlarmStatus(eventId: Int, isEnabled: Boolean) {
        viewModelScope.launch {
            eventRepository.updateAlarmStatus(eventId, isEnabled)
        }
    }

}