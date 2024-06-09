package com.project.veganlife.lifecheck.ui.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.project.veganlife.R
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.databinding.FragmentLifeCheckWeeklyBinding
import com.project.veganlife.lifecheck.data.model.LifeCheckWeeklyCalorieResponse
import com.project.veganlife.lifecheck.ui.viewmodel.LifeCheckViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class LifeCheckWeeklyFragment : Fragment() {

    private var _binding: FragmentLifeCheckWeeklyBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LifeCheckViewModel by viewModels({ requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLifeCheckWeeklyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeWeeklyCalorieData()
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }

    private fun observeWeeklyCalorieData() {
        viewModel.weeklyCalorieData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ApiResult.Success -> {
                    setupBarChart(result.data)
                }

                is ApiResult.Error ->
                    Log.d("weeklyCalorieData Error", result.description)

                is ApiResult.Exception ->
                    Log.d(
                        "weeklyCalorieData Exception",
                        result.e.message ?: "No message available"
                    )
            }
        }
    }

    private fun setupBarChart(weeklyData: LifeCheckWeeklyCalorieResponse) {
        // 데이터셋
        val barEntries: MutableList<BarEntry> = mutableListOf()

        weeklyData.periodicCalorie.forEachIndexed { index, data ->
            barEntries.add(
                BarEntry(
                    index.toFloat(),
                    floatArrayOf(
                        data.breakfast.toFloat(),
                        data.lunch.toFloat(),
                        data.dinner.toFloat(),
                        data.snack.toFloat()
                    )
                )
            )
        }

        val set = BarDataSet(barEntries, "").apply {
            // 스택 이름
            stackLabels = arrayOf("아침", "점심", "저녁", "간식")
            // 색상(스택 순서)
            colors = listOf(
                ContextCompat.getColor(requireContext(), R.color.base2),
                ContextCompat.getColor(requireContext(), R.color.base1),
                ContextCompat.getColor(requireContext(), R.color.base3),
                ContextCompat.getColor(requireContext(), R.color.point1),
            )
            // 막대 위에 수치 그리지 않기
            setDrawValues(false)
        }

        val barData = BarData(set).apply {
            barWidth = 0.6f // 막대 너비
        }

        // 전체 데이터 포인트의 합계 및 평균 계산
        val totalSum = weeklyData.periodicCalorie.flatMap {
            listOf(
                it.breakfast,
                it.lunch,
                it.dinner,
                it.snack
            )
        }.sum()
        val totalDataPoints = weeklyData.periodicCalorie.size
        val dailyAverage = totalSum / totalDataPoints

        setKcalUI(totalSum, dailyAverage)

        // Y축에 일평균 LimitLine 추가
        val avgLimitLine = LimitLine(dailyAverage.toFloat()).apply {
            lineColor = ContextCompat.getColor(requireContext(), R.color.base1)
            lineWidth = 1f
            enableDashedLine(10f, 5f, 0f) // 점선으로 설정 (선 길이, 공간 길이, 페이즈)
        }

        val xLabel = getWeeklyDate(
            viewModel.selectedWeeklyStartDate.value,
            viewModel.selectedWeeklyEndDate.value
        )

        with(binding.barchartLifecheckWeekly) {
            // 기존 데이터 초기화
            data?.clearValues()
            clear()
            // 기존 LimitLine 제거
            axisLeft.removeAllLimitLines()
            
            // 바 차트에 데이터 설정
            data = barData
            description.isEnabled = false // 설명 제거
            xAxis.apply {
                // X축 포맷터 적용
                valueFormatter = IndexAxisValueFormatter(xLabel)
                // X축 위치 하단 설정
                position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
                // X축 격자선 제거
                setDrawGridLines(false)
                // 라벨 글시 생상
                textColor = ContextCompat.getColor(requireContext(), R.color.base1)
                textSize = 12f
            }

            axisLeft.apply {
                // 평균 선 추가
                axisLeft.addLimitLine(avgLimitLine)
                // Y축 왼쪽 라인 제거
                setDrawAxisLine(false)
                // Y축 격자선 제거
                setDrawGridLines(false)
                // Y축 왼쪽의 수치 라벨 제거
                setDrawLabels(false)
                // 막대가 차트의 시작점에 맞도록 조정
                axisMinimum = 0f
            }

            // 오른쪽 Y축 제거
            axisRight.isEnabled = false
            // 범례 표시
            legend.isEnabled = true

            // 범례(Legend) 설정
            legend.apply {
                // 범례를 오른쪽 아래에 위치
                verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
                horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
                // 차트 외부에 범례
                setDrawInside(false)
                // 범례 아이콘의 크기
                formSize = 10f
                // 범례 텍스트의 크기
                textSize = 12f
                // 범례 항목 사이의 X축 간격
                xEntrySpace = 20f
            }

            // 차트와 범례사이 간격
            extraBottomOffset = 20f
            // 막대 너비 조정
            setFitBars(true)
            // 배경색 변경
            setBackgroundColor(
                ContextCompat.getColor(context, R.color.white)
            )
            // 차트 새로고침
            invalidate()
        }
    }

    private fun setKcalUI(total: Int, average: Int) {
        binding.run {
            tvLifecheckWeeklyTotalKcal.text = total.toString()
            tvLifecheckWeeklyAvgKcal.text = "일 평균: $average"
        }
    }

    private fun getWeeklyDate(startDate: String?, endDate: String?): Array<String> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val displayFormat = SimpleDateFormat("E d", Locale.getDefault())
        val startCalendar = Calendar.getInstance()
        val endCalendar = Calendar.getInstance()

        val labels = mutableListOf<String>()

        try {
            if (startDate != null && endDate != null) {
                startCalendar.time = dateFormat.parse(startDate)!!
                endCalendar.time = dateFormat.parse(endDate)!!

                while (!startCalendar.after(endCalendar)) {
                    labels.add(displayFormat.format(startCalendar.time))
                    startCalendar.add(Calendar.DAY_OF_MONTH, 1)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return labels.toTypedArray()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}