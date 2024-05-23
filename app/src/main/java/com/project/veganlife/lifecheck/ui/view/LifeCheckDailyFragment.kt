package com.project.veganlife.lifecheck.ui.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.anychart.APIlib
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.DailyIntakeResponse
import com.project.veganlife.databinding.FragmentLifeCheckDailyBinding
import com.project.veganlife.lifecheck.ui.viewmodel.LifeCheckViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LifeCheckDailyFragment : Fragment() {

    private var _binding: FragmentLifeCheckDailyBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LifeCheckViewModel by viewModels({ requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLifeCheckDailyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeSelectedDate()
        observeDailyIntakeData()
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }

    private fun observeSelectedDate() {
        viewModel.selectedDate.observe(viewLifecycleOwner) { date ->
            viewModel.fetchDailyIntake(date)
        }
    }

    private fun observeDailyIntakeData() {
        viewModel.dailyIntakeData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ApiResult.Success -> {
                    setupCarbohydrateChart(result.data)
                    setupProteinChart(result.data)
                    setupFatChart(result.data)
                    updateIntakeTextView(result.data)
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

    private fun updateIntakeTextView(intakeData: DailyIntakeResponse) {
        binding.tvLifecheckDailyCarbohydrateIntake.text = "${intakeData.carbs}g"
        binding.tvLifecheckDailyProteinIntake.text = "${intakeData.protein}g"
        binding.tvLifecheckDailyFatIntake.text = "${intakeData.fat}g"
    }

    private fun setupCarbohydrateChart(intakeData: DailyIntakeResponse) {
        // AnyChartView를 활성화
        APIlib.getInstance().setActiveAnyChartView(binding.anychartLifecheckDailyCarbohydrate)

        // 탄수화물의 권장 섭취량과 실제 섭취량
        val recommendCarbohydrates = 200f
        val intakeCarbohydrates = intakeData.carbs.toFloat()

        // 차트에 표시할 데이터를 생성
        val dataCarbohydrate = mutableListOf<DataEntry>().apply {
            add(ValueDataEntry("현재 섭취량", intakeCarbohydrates))
            add(ValueDataEntry("남은 섭취량", recommendCarbohydrates - intakeCarbohydrates))
        }

        // 차트의 색상 팔레트를 정의
        val carbohydrateFillColors = arrayOf("#50A56F", "#D2D1D4")

        // Pie 차트를 생성하고 구성
        val carbohydratePieChart = AnyChart.pie().apply {
            // 차트의 배경색
            background().fill("#E8E8EA")
            // 차트의 패딩을 제거
            padding(0, 0, 0, 0)
            // 데이터를 차트에 설정
            data(dataCarbohydrate)
            // 레이블을 비활성화
            labels(false)
            // 범례를 비활성화
            legend(false)
            // 크레딧을 비활성화
            credits(false)
            // 색상 팔레트를 적용
            palette(carbohydrateFillColors)
        }

        binding.anychartLifecheckDailyCarbohydrate.setChart(carbohydratePieChart)
    }

    private fun setupProteinChart(intakeData: DailyIntakeResponse) {
        APIlib.getInstance().setActiveAnyChartView(binding.anychartLifecheckDailyProtein)

        val recommendProtein = 200f
        val intakeProtein = intakeData.protein.toFloat()

        val dataProtein = mutableListOf<DataEntry>().apply {
            add(ValueDataEntry("현재 섭취량", intakeProtein))
            add(ValueDataEntry("남은 섭취량", recommendProtein - intakeProtein))
        }

        val proteinFillColors = arrayOf("#7DAC66", "#D2D1D4")

        val proteinPieChart = AnyChart.pie().apply {
            background().fill("#E8E8EA")
            padding(0, 0, 0, 0)
            data(dataProtein)
            labels(false)
            legend(false)
            credits(false)
            palette(proteinFillColors)
        }

        binding.anychartLifecheckDailyProtein.setChart(proteinPieChart)
    }

    private fun setupFatChart(intakeData: DailyIntakeResponse) {
        APIlib.getInstance().setActiveAnyChartView(binding.anychartLifecheckDailyFat)

        val recommendFat = 200f
        val intakeFat = intakeData.fat.toFloat()

        val dataFat = mutableListOf<DataEntry>().apply {
            add(ValueDataEntry("현재 섭취량", intakeFat))
            add(ValueDataEntry("남은 섭취량", recommendFat - intakeFat))
        }

        val fatFillColors = arrayOf("#D4CF69", "#D2D1D4")

        val fatPieChart = AnyChart.pie().apply {
            background().fill("#E8E8EA")
            padding(0, 0, 0, 0)
            data(dataFat)
            labels(false)
            legend(false)
            credits(false)
            palette(fatFillColors)
        }

        binding.anychartLifecheckDailyFat.setChart(fatPieChart)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}