package com.parithidb.taskiassessment.ui.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.parithidb.taskiassessment.R
import com.parithidb.taskiassessment.databinding.FragmentEventsBinding
import com.parithidb.taskiassessment.ui.event.upcomingEvents.UpcomingEventsFragment

class EventsFragment : Fragment() {
    private lateinit var binding: FragmentEventsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViewPager()
        setUpTabLayout()
        setUpAddEvent()
    }

    private fun setUpViewPager() {
        val adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = 2
            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> UpcomingEventsFragment()
                    else -> EventsByDateFragment()
                }
            }
        }
        binding.viewPager.adapter = adapter
    }

    private fun setUpTabLayout() {
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = if (position == 0) "Upcoming Events" else "Events By Date"
        }.attach()
    }

    private fun setUpAddEvent() {
        binding.fabAddEvent.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_eventsFragment_to_addEditEventFragment)
        }
    }

}