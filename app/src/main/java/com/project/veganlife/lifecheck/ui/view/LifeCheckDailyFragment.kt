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
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
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
            Log.d("dailyRecommendedIntakeData", result.toString())
            when (result) {
                is ApiResult.Success -> {
                    updateRecommendedIntakeTextView(result.data)
                    setupCarbohydrateChart(dailyIntake, result.data)
                    setupProteinChart(dailyIntake, result.data)
                    setupFatChart(dailyIntake, result.data)
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

    private fun setupCarbohydrateChart(
        dailyIntake: DailyIntakeResponse,
        recommendedIntake: RecommendedIntakeResponse
    ) {
        setupAnyChart(
            binding.anychartLifecheckDailyCarbohydrate,
            dailyIntake.carbs,
            recommendedIntake.dailyCarbs,
            R.color.gredient_end
        )
    }

    private fun setupProteinChart(
        dailyIntake: DailyIntakeResponse,
        recommendedIntake: RecommendedIntakeResponse
    ) {
        setupAnyChart(
            binding.anychartLifecheckDailyProtein,
            dailyIntake.protein,
            recommendedIntake.dailyProtein,
            R.color.base1
        )
    }

    private fun setupFatChart(
        dailyIntake: DailyIntakeResponse,
        recommendedIntake: RecommendedIntakeResponse
    ) {
        setupAnyChart(
            binding.anychartLifecheckDailyFat,
            dailyIntake.fat,
            recommendedIntake.dailyFat,
            R.color.point1
        )
    }

    // AnyChart 속성 설정
    private fun setupAnyChart(chartView: AnyChartView, intake: Int, recommend: Int, color: Int) {
        APIlib.getInstance().setActiveAnyChartView(chartView)

        val dataEntries = mutableListOf<DataEntry>().apply {
            add(ValueDataEntry("현재 섭취량", intake.toFloat()))
            add(ValueDataEntry("남은 섭취량", recommend.toFloat() - intake.toFloat()))
        }

        // 초과 색상
        val exceedColor = resources.getColor(R.color.no, null)
        // 기본 색상
        val normalColor = resources.getColor(color, null)
        val backgroundColor =
            String.format("#%06X", 0xFFFFFF and resources.getColor(R.color.gray3, null))
        // 권장 섭취량 초과인 경우
        val fillColor = if (intake > recommend) String.format(
            "#%06X",
            0xFFFFFF and exceedColor
        ) else String.format("#%06X", 0xFFFFFF and normalColor)

        val pieChart = AnyChart.pie().apply {
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}