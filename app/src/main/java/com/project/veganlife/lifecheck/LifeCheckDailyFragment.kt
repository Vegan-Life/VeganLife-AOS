package com.project.veganlife.lifecheck

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.anychart.APIlib
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.project.veganlife.databinding.FragmentLifeCheckDailyBinding

class LifeCheckDailyFragment : Fragment() {

    private var _binding: FragmentLifeCheckDailyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLifeCheckDailyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCarbohydrateChart()
        setupProteinChart()
        setupFatChart()
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }

    private fun setupCarbohydrateChart() {
        // AnyChartView를 활성화
        APIlib.getInstance().setActiveAnyChartView(binding.anychartLifecheckDailyCarbohydrate)

        // 탄수화물의 권장 섭취량과 실제 섭취량
        val recommendCarbohydrates = 200f
        val intakeCarbohydrates = 130f

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

    private fun setupProteinChart() {
        APIlib.getInstance().setActiveAnyChartView(binding.anychartLifecheckDailyProtein)

        val recommendProtein = 200f
        val intakeProtein = 130f

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

    private fun setupFatChart() {
        APIlib.getInstance().setActiveAnyChartView(binding.anychartLifecheckDailyFat)

        val recommendFat = 200f
        val intakeFat = 130f

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