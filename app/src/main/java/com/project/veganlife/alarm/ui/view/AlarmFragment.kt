package com.project.veganlife.alarm.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.veganlife.alarm.ui.adapter.AlarmAdapter
import com.project.veganlife.alarm.ui.viewmodel.AlarmListViewModel
import com.project.veganlife.alarm.ui.viewmodel.AlarmViewModel
import com.project.veganlife.databinding.FragmentAlarmBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AlarmFragment : Fragment() {
    private var _binding: FragmentAlarmBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: AlarmAdapter

    private val alarmViewModel: AlarmViewModel by activityViewModels()
    private val alarmListViewModel: AlarmListViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlarmBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // toolbar
        setToolbarListener()

        getAlarmData()
        setAlarmList()

        // 알람 확인 후
        alarmViewModel.closeAlarm()
    }

    private fun setToolbarListener() {
        binding.toolbarSignupToolbar.run {
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun getAlarmData() {
        alarmListViewModel.getAlarmData()
    }

    private fun setAlarmList() {
        lifecycleScope.launch {

            // Adapter에 Context 전달
            adapter = AlarmAdapter()
            binding.rvAlarmNotice.layoutManager = LinearLayoutManager(context)
            binding.rvAlarmNotice.adapter = adapter

            alarmListViewModel.alarmList.collectLatest { pagingData ->
                pagingData?.let {
                    adapter.submitData(it)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}