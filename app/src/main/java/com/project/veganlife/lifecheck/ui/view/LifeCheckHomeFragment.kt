package com.project.veganlife.lifecheck.ui.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.databinding.FragmentLifecheckHomeBinding
import com.project.veganlife.lifecheck.ui.viewmodel.LifeCheckViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class LifeCheckHomeFragment : Fragment() {

    private var _binding: FragmentLifecheckHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LifeCheckViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLifecheckHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        setupDateSelection()
        if (savedInstanceState == null) {
            setInitialDate()
        }
        setupDateButtons()
        observeSelectedDate()
        observeDailyIntakeData()
    }

    private fun setupViewPager() {

        val adapter = LifeCheckHomeViewPagerAdapter(this)
        binding.vpLifecheckHome.adapter = adapter

        TabLayoutMediator(
            binding.tablayoutLifecheckHome,
            binding.vpLifecheckHome
        ) { tab, position ->
            tab.text = when (position) {
                0 -> "일"
                1 -> "주"
                2 -> "월"
                else -> "년"
            }
        }.attach()
    }

    private fun observeSelectedDate() {
        viewModel.selectedDate.observe(viewLifecycleOwner) {
            updateRightButtonState()
        }
    }

    private fun observeDailyIntakeData() {
        viewModel.dailyIntakeData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ApiResult.Success -> {
                    binding.tvLifecheckHomeCalorie.text = result.data.calorie.toString()
                }

                is ApiResult.Error ->
                    Log.d("dailyIntakeData Error", result.description)

                is ApiResult.Exception ->
                    Log.d(
                        "dailyIntakeData Exception",
                        result.e.message ?: "No message available"
                    )
            }
        }
    }

    // 날짜 선택 버튼 설정
    private fun setupDateSelection() {
        binding.tvLifecheckHomeDay.setOnClickListener {
            showDatePickerDialog()
        }
        binding.tvLifecheckHomeDate.setOnClickListener {
            showDatePickerDialog()
        }
    }

    // 초기 날짜를 오늘 날짜로 설정
    private fun setInitialDate() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val todayDate = String.format("%04d-%02d-%02d", year, month + 1, day)
        val todayDay = getDayOfWeek(year, month, day)

        binding.tvLifecheckHomeDate.text = todayDate
        binding.tvLifecheckHomeDay.text = todayDay
        viewModel.updateDate(todayDate)
    }


    // 날짜 선택 달력 토출
    private fun showDatePickerDialog() {
        val selectedDate = viewModel.selectedDate.value ?: getCurrentDateString()
        val calendar = Calendar.getInstance()
        val year: Int
        val month: Int
        val day: Int

        if (selectedDate.isNotEmpty()) {
            val dateParts = selectedDate.split("-")
            year = dateParts[0].toInt()
            month = dateParts[1].toInt() - 1 // Calendar 월은 0부터 시작
            day = dateParts[2].toInt()
        } else {
            year = calendar.get(Calendar.YEAR)
            month = calendar.get(Calendar.MONTH)
            day = calendar.get(Calendar.DAY_OF_MONTH)
        }

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDateStr =
                    String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)

                binding.tvLifecheckHomeDate.text = selectedDateStr
                binding.tvLifecheckHomeDay.text =
                    getDayOfWeek(selectedYear, selectedMonth, selectedDay)
                viewModel.updateDate(selectedDateStr)
            }, year, month, day
        )
        // 미래 날짜 선택 제한
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis

        datePickerDialog.show()
    }

    // 특정 날짜의 요일을 구하기
    private fun getDayOfWeek(year: Int, month: Int, day: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        return when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY -> "일요일"
            Calendar.MONDAY -> "월요일"
            Calendar.TUESDAY -> "화요일"
            Calendar.WEDNESDAY -> "수요일"
            Calendar.THURSDAY -> "목요일"
            Calendar.FRIDAY -> "금요일"
            Calendar.SATURDAY -> "토요일"
            else -> ""
        }
    }

    // 현재 날짜 문자열 반환
    private fun getCurrentDateString(): String {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return String.format("%04d-%02d-%02d", year, month + 1, day)
    }

    // 날짜 전환 버튼 세팅
    private fun setupDateButtons() {
        binding.btnLifecheckHomeDateLeft.setOnClickListener {
            changeDateBy(-1)
        }
        binding.btnLifecheckHomeDateRight.setOnClickListener {
            changeDateBy(1)
        }
    }

    // 날짜 전환
    private fun changeDateBy(days: Int) {
        val selectedDate = viewModel.selectedDate.value ?: getCurrentDateString()
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()

        try {
            val date = sdf.parse(selectedDate)
            calendar.time = date
            calendar.add(Calendar.DAY_OF_MONTH, days)
            val newDate = sdf.format(calendar.time)
            binding.tvLifecheckHomeDate.text = newDate
            binding.tvLifecheckHomeDay.text = getDayOfWeek(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            viewModel.updateDate(newDate)
            updateRightButtonState()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // 미래 날짜로 이동을 제한
    private fun updateRightButtonState() {
        val today = getCurrentDateString()
        val selectedDate = viewModel.selectedDate.value

        binding.btnLifecheckHomeDateRight.isEnabled = selectedDate != today
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}