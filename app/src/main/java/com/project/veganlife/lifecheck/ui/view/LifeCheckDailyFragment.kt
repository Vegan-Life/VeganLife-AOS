package com.project.veganlife.lifecheck.ui.view

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.anychart.APIlib
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Pie
import com.project.veganlife.R
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.DailyIntakeResponse
import com.project.veganlife.data.model.RecommendedIntakeResponse
import com.project.veganlife.databinding.FragmentLifeCheckDailyBinding
import com.project.veganlife.lifecheck.ui.viewmodel.LifeCheckViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LifeCheckDailyFragment : Fragment() {

    private var _binding: FragmentLifeCheckDailyBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LifeCheckViewModel by viewModels({ requireParentFragment() })

    private lateinit var carbohydrateChart: AnyChartView
    private lateinit var proteinChart: AnyChartView
    private lateinit var fatChart: AnyChartView

    private val carbohydratePie: Pie by lazy { AnyChart.pie() }
    private val proteinPie: Pie by lazy { AnyChart.pie() }
    private val fatPie: Pie by lazy { AnyChart.pie() }

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
        observeSelectedDate()
        observeDailyIntakeData()
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }

    private fun setupCarbohydrateChart() {
        carbohydrateChart = binding.anychartLifecheckDailyCarbohydrate
        APIlib.getInstance().setActiveAnyChartView(carbohydrateChart)

        initializeAnyChart(carbohydrateChart, carbohydratePie, R.color.gredient_end)
    }

    private fun setupProteinChart() {
        proteinChart = binding.anychartLifecheckDailyProtein
        APIlib.getInstance().setActiveAnyChartView(proteinChart)

        initializeAnyChart(proteinChart, proteinPie, R.color.base1)
    }

    private fun setupFatChart() {
        fatChart = binding.anychartLifecheckDailyFat
        APIlib.getInstance().setActiveAnyChartView(fatChart)

        initializeAnyChart(fatChart, fatPie, R.color.point1)
    }

    private fun observeSelectedDate() {
        viewModel.selectedDate.observe(viewLifecycleOwner) { date ->
            viewModel.fetchDailyIntake(date)
        }
    }

    private fun observeDailyIntakeData() {
        viewModel.fetchRecommendedIntake()
        viewModel.dailyIntakeData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ApiResult.Success -> {
                    observeRecommendedIntakeData(result.data)
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

    private fun observeRecommendedIntakeData(dailyIntake: DailyIntakeResponse) {
        viewModel.recommendedIntakeData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ApiResult.Success -> {
                    updateRecommendedIntakeTextView(result.data)
                    updateAnyChartData(
                        carbohydrateChart,
                        carbohydratePie,
                        dailyIntake.carbs,
                        result.data.dailyCarbs,
                        R.color.gredient_end
                    )
                    updateAnyChartData(
                        proteinChart,
                        proteinPie,
                        dailyIntake.protein,
                        result.data.dailyProtein,
                        R.color.base1
                    )
                    updateAnyChartData(
                        fatChart,
                        fatPie,
                        dailyIntake.fat,
                        result.data.dailyFat,
                        R.color.point1
                    )
                    updateCalorieStatus(dailyIntake, result.data)
                    updateCalorieStatus(dailyIntake, result.data)
                }

                is ApiResult.Error ->
                    Log.d("dailyRecommendedIntakeData Error", result.description)

                is ApiResult.Exception ->
                    Log.d(
                        "dailyRecommendedIntakeData Exception",
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

    private fun updateRecommendedIntakeTextView(recommendData: RecommendedIntakeResponse) {
        binding.tvLifecheckDailyCarbohydrateRecommend.text = "/${recommendData.dailyCarbs}g"
        binding.tvLifecheckDailyProteinRecommend.text = "/${recommendData.dailyProtein}g"
        binding.tvLifecheckDailyFatRecommend.text = "/${recommendData.dailyFat}g"
    }

    // AnyChart 속성 설정
    private fun initializeAnyChart(chartView: AnyChartView, pieChart: Pie, color: Int) {

        val dataEntries = mutableListOf<DataEntry>().apply {
            add(ValueDataEntry("현재 섭취량", 0))
            add(ValueDataEntry("남은 섭취량", 100))
        }

        // 기본 색상
        val normalColor = resources.getColor(color, null)
        val backgroundColor =
            String.format("#%06X", 0xFFFFFF and resources.getColor(R.color.gray3, null))
        // 권장 섭취량 초과인 경우
        val fillColor = String.format("#%06X", 0xFFFFFF and normalColor)

        pieChart.apply {
            // 차트 배경색
            background().fill("#E8E8EA")
            // 차트 패딩 제거
            padding(0, 0, 0, 0)
            // 데이터를 차트에 설정
            data(dataEntries)
            // 레이블을 비활성화
            labels(false)
            // 범례를 비활성화
            legend(false)
            // 크레딧을 비활성화
            credits(false)
            // 차트 색상 팔레트
            palette(arrayOf(fillColor, backgroundColor))
        }

        chartView.setChart(pieChart)
        chartView.invalidate()
    }

    private fun updateAnyChartData(
        chartView: AnyChartView,
        pieChart: Pie,
        intake: Int,
        recommend: Int,
        color: Int
    ) {
        APIlib.getInstance().setActiveAnyChartView(chartView)
        val exceedColor = resources.getColor(R.color.no, null)
        val normalColor = resources.getColor(color, null)
        val backgroundColor =
            String.format("#%06X", 0xFFFFFF and resources.getColor(R.color.gray3, null))

        val fillColor = if (intake > recommend) String.format(
            "#%06X",
            0xFFFFFF and exceedColor
        ) else String.format("#%06X", 0xFFFFFF and normalColor)

        val newData = listOf(
            ValueDataEntry("현재 섭취량", intake.toFloat()),
            ValueDataEntry("남은 섭취량", maxOf(recommend - intake, 0).toFloat())
        )

        pieChart.apply {
            data(newData)
            palette(arrayOf(fillColor, backgroundColor))
        }
    }

    private fun updateCalorieStatus(
        dailyIntake: DailyIntakeResponse,
        recommendedIntake: RecommendedIntakeResponse
    ) {
        dailyIntake.let { daily ->
            recommendedIntake.let { recommend ->
                val remainingCalories = recommend.dailyCalorie - daily.calorie
                val statusText: String
                val textColor: Int
                val start: Int
                val end: Int

                if (remainingCalories >= 0) {
                    statusText =
                        getString(R.string.all_rest_kcal) + " ${remainingCalories}kcal" + getString(
                            R.string.all_rest_kcal_is
                        )
                    textColor = ContextCompat.getColor(requireContext(), R.color.base3)
                    start = statusText.indexOf("${remainingCalories}kcal")
                    end = start + "${remainingCalories}kcal".length
                } else {
                    statusText =
                        getString(R.string.all_over_rest_kcal) + " ${-remainingCalories}kcal" + getString(
                            R.string.all_rest_kcal_is
                        )
                    textColor = ContextCompat.getColor(requireContext(), R.color.no)
                    start = statusText.indexOf("${-remainingCalories}kcal")
                    end = start + "${remainingCalories}kcal".length
                }

                val spannable: Spannable = SpannableString(statusText)
                spannable.setSpan(
                    ForegroundColorSpan(textColor),
                    start,
                    end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                binding.tvLifecheckDailyKcal.text = spannable
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}