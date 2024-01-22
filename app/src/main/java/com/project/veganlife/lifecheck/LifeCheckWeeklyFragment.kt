package com.project.veganlife.lifecheck

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.project.veganlife.R
import com.project.veganlife.databinding.FragmentLifeCheckWeeklyBinding

class LifeCheckWeeklyFragment : Fragment() {

    private var _binding: FragmentLifeCheckWeeklyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLifeCheckWeeklyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBarChart()
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }

    private fun setupBarChart() {
        // 데이터셋
        val barEntries: MutableList<BarEntry> = mutableListOf()

        // 예시 데이터(일주일)
        val dataPoints = listOf(
            floatArrayOf(150f, 800f, 200f, 70f), // 월요일
            floatArrayOf(100f, 300f, 500f, 10f), // 화요일
            floatArrayOf(200f, 400f, 400f, 40f), // 수요일
            floatArrayOf(130f, 600f, 600f, 50f), // 목요일
            floatArrayOf(250f, 500f, 100f, 60f), // 금요일
            floatArrayOf(150f, 100f, 800f, 90f), // 토요일
            floatArrayOf(110f, 600f, 500f, 10f), // 일요일
        )

        // 데이터 포인트바 차트에 추가
        dataPoints.forEachIndexed { index, floats ->
            barEntries.add(BarEntry(index.toFloat(), floats))
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
        val totalSum = dataPoints.flatMap { it.toList() }.sum()
        val totalDataPoints = dataPoints.size // 각 요일에 4개의 데이터 포인트가 있다고 가정
        val dailyAverage = totalSum / totalDataPoints

        // Y축에 일평균 LimitLine 추가
        val avgLimitLine = LimitLine(dailyAverage).apply {
            lineColor = ContextCompat.getColor(requireContext(), R.color.base1)
            lineWidth = 0f
            enableDashedLine(10f, 5f, 0f) // 점선으로 설정 (선 길이, 공간 길이, 페이즈)
        }

        with(binding.barchartLifecheckWeekly) {
            // 바 차트에 데이터 설정
            data = barData
            description.isEnabled = false // 설명 제거
            xAxis.apply {
                // X축 포맷터 적용
                valueFormatter = IndexAxisValueFormatter(
                    arrayOf(
                        "월 25",
                        "화 26",
                        "수 27",
                        "목 28",
                        "금 29",
                        "토 30",
                        "일 31"
                    )
                )
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}