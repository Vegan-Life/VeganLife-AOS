package com.project.veganlife.home.ui.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.project.veganlife.R
import com.project.veganlife.alarm.ui.viewmodel.AlarmViewModel
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.databinding.FragmentHomeBinding
import com.project.veganlife.home.ui.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()
    private val alarmViewModel: AlarmViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // logic
        setToolbarMoveToAlarm()
        // SSE 구독
        alarmViewModel.connectSse()

        // 프로필 정보 Get
        getProfileInfo()
        getProfileInfoValue()

        // 권장 섭취량
        getRecommendedIntake()
        getRecommendedIntakeValue()

        // 일일 섭취량
        getDailyIntake()
        setDailyIntake()

        // 탄단지 pie chart
        setIntakePieChart()

        // ui
        setProfile()

        setRecommendedIntakeUi()
        setDailyIntakeUi()

        setRestKcalUi()
    }

    private fun setToolbarMoveToAlarm() {
        binding.tbHomeToolbar.setOnMenuItemClickListener { menu ->
            when(menu.itemId) {
                R.id.menu_lifecheck_home_alarm -> {
                    findNavController().navigate(R.id.action_homeFragment_to_alarmFragment)
                }

            }
            false
        }

    }

    private fun setProfile() {
        homeViewModel.apply {
            binding.apply {
                nickname.observe(viewLifecycleOwner) { nickname ->
                    tvHomeNickname.text = nickname
                }

                recipeNickname.observe(viewLifecycleOwner) { recipeNickname ->
                    tvHomeRecipeName.text = recipeNickname
                }

                profilePhoto.observe(viewLifecycleOwner) { photo ->
                    if (photo != null) {
                        Log.d("photo", profilePhoto.value.toString())
                        Glide.with(requireContext()).load(profilePhoto.value).apply(
                            RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)
                        ) // 캐시 사용하지 않도록 설정
                            .into(ivHomeProfile)
                    } else {
                        ivHomeProfile.setBackgroundResource(R.drawable.all_profile_basic)
                        ivHomeProfile.borderWidth = 0
                    }
                }
            }
        }
    }

    private fun getProfileInfoValue() {
        homeViewModel.apply {
            profile.observe(viewLifecycleOwner) { apiResult ->
                when(apiResult) {
                    is ApiResult.Error -> {
                        val intakeResponse = apiResult.description
                        Log.d("recommended Error", intakeResponse)
                    }

                    is ApiResult.Exception -> {
                        Log.d("recommended Exception", apiResult.e.message ?: "No message available")
                    }
                    is ApiResult.Success -> {
                        val response = apiResult.data
                        getNickname_Photo(response)
                    }
                }
            }
        }
    }

    private fun getProfileInfo() {
        homeViewModel.getProfile()
    }

    private fun getRecommendedIntake() {
        homeViewModel.getRecommendedIntake()
    }

    private fun getDailyIntake() {
        homeViewModel.getDailyIntake()
    }

    private fun getRecommendedIntakeValue() {
        homeViewModel.apply {
            resultRecommendedIntake.observe(viewLifecycleOwner) { apiResult ->
                when (apiResult) {
                    is ApiResult.Error -> {
                        val intakeResponse = apiResult.description
                        Log.d("recommended Error", intakeResponse)
                    }

                    is ApiResult.Exception -> {
                        Log.d(
                            "recommended Exception",
                            apiResult.e.message ?: "No message available"
                        )
                    }

                    is ApiResult.Success -> {
                        val intakeResponse = apiResult.data
                        setRecommendedIntakeValue(intakeResponse)
                    }
                }
            }
        }
    }

    private fun setRecommendedIntakeUi() {
        homeViewModel.apply {
            binding.apply {
                recommendedCalorie.observe(viewLifecycleOwner) { calorie ->
                    cpHomeProgressbar.binding.tvHomeTotalKcal.text = "${calorie}kcal"
                }

                recommendedCarbs.observe(viewLifecycleOwner) { carbs ->
                    pcHomeCarbohydrate.tvHomeCarbohydrateTotal.text = "/${carbs}g"
                }

                recommendedProtein.observe(viewLifecycleOwner) { protein ->
                    pcHomeProtein.tvHomeProteinTotal.text = "/${protein}g"
                }

                recommendedFat.observe(viewLifecycleOwner) { fat ->
                    pcHomeFat.tvHomeFatTotal.text = "/${fat}g"
                }
            }
        }
    }

    private fun setDailyIntake() {
        homeViewModel.apply {
            resultDailyIntake.observe(viewLifecycleOwner) { apiResult ->
                when (apiResult) {
                    is ApiResult.Error -> {
                        val intakeResponse = apiResult.description
                        Log.d("daily Error", intakeResponse)
                    }

                    is ApiResult.Exception -> {
                        Log.d("daily Exception", apiResult.e.message ?: "No message available")
                    }

                    is ApiResult.Success -> {
                        val intakeResponse = apiResult.data

                        setDailyIntkeValue(intakeResponse)
                    }
                }
            }
        }
    }

    private fun setDailyIntakeUi() {
        homeViewModel.apply {
            binding.apply {
                dailyCalorie.observe(viewLifecycleOwner) { calorie ->
                    cpHomeProgressbar.binding.tvHomeNowKcal.text = "${calorie}kcal"
                    if (calorie != null) {
                        cpHomeProgressbar.setCurrentKcalLength(calorie)
                    }
                }

                carbs.observe(viewLifecycleOwner) { carbs ->
                    pcHomeCarbohydrate.tvHomeCarbohydrateRest.text = "${carbs}g"
                }

                protein.observe(viewLifecycleOwner) { protein ->
                    pcHomeProtein.tvHomeProteinRest.text = "${protein}g"
                }

                fat.observe(viewLifecycleOwner) { fat ->
                    pcHomeFat.tvHomeFatRest.text = "${fat}g"
                }

                // 프로그래스바 현재 칼로리 설정
                restCaloriePercent.observe(viewLifecycleOwner) { percent ->
                    when (percent) {
                        in 21..100 -> {
                            cpHomeProgressbar.binding.apply {
                                tvHomeNowKcal.visibility = View.VISIBLE
                                pbHomeProgressbar.visibility = View.VISIBLE
                            }
                            cpHomeProgressbar.setCurrentKcal(percent)
                        }

                        else -> {
                            cpHomeProgressbar.binding.tvHomeNowKcal.visibility = View.INVISIBLE
                            cpHomeProgressbar.binding.pbHomeProgressbar.visibility = View.INVISIBLE
                        }
                    }
                }
            }
        }
    }

    private fun setRestKcalUi() {
        homeViewModel.apply {
            binding.apply {
                restCalorie.observe(viewLifecycleOwner) { rest ->
                    setRestCalorieColor()
                }

                restCalorieText.observe(viewLifecycleOwner) { text ->
                    tvHomeRestKcalFront.text = resources.getString(text)
                }

                restCalorie.observe(viewLifecycleOwner) { value ->
                    tvHomeRestKcal.text = "${value}kcal"
                }

                restCalorieColor.observe(viewLifecycleOwner) { textcolor ->
                    tvHomeRestKcal.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            textcolor
                        )
                    )
                }

                tvHomeRestKcalBack.text = resources.getString(R.string.all_rest_kcal_is)
            }
        }
    }

    private fun setIntakePieChart() {
        homeViewModel.apply {
            binding.apply {
                val carbsDataList = ArrayList<PieEntry>()
                val proteinDataList = ArrayList<PieEntry>()
                val fatDataList = ArrayList<PieEntry>()

                var isCarbsUpdate = true
                carbs.observe(viewLifecycleOwner) { carbs ->
                    if(isCarbsUpdate) {
                        carbsDataList.add(PieEntry(carbs.toFloat(), "현재 섭취량"))
                        isCarbsUpdate = false
                    }
                }

                var isRestCarbsUpdate = true
                restCarbs.observe(viewLifecycleOwner) { restCarbs ->
                    if(isRestCarbsUpdate) {
                        if (restCarbs > 0) {
                            carbsDataList.add(PieEntry(restCarbs.toFloat(), "남은 섭취량"))
                            isRestCarbsUpdate = false
                        } else {
                            carbsDataList.add(PieEntry(0F, "남은 섭취량"))
                            isRestCarbsUpdate = false
                        }
                    }
                }

                var isProteinUpdate = true
                protein.observe(viewLifecycleOwner) { protein ->
                    if (isProteinUpdate) {
                        proteinDataList.add(PieEntry(protein.toFloat(), "현재 섭취량"))
                        isProteinUpdate = false
                    }
                }

                var isRestProteinUpdate = true
                restProtein.observe(viewLifecycleOwner) { restProtein ->
                    if(isRestProteinUpdate) {
                        if (restProtein > 0) {
                            proteinDataList.add(PieEntry(restProtein.toFloat(), "남은 섭취량"))
                            for (i in proteinDataList) Log.d("rest protein 1", i.value.toString())
                            isRestProteinUpdate = false
                        }
                        else {
                            proteinDataList.add(PieEntry(0F, "남은 섭취량"))
                            for (i in proteinDataList) Log.d("rest protein 2", i.value.toString())
                            isRestProteinUpdate = false

                        }
                    }
                }

                var isFatUpdate = true
                fat.observe(viewLifecycleOwner) { fat ->
                    if(isFatUpdate) {
                        fatDataList.add(PieEntry(fat.toFloat(), "현재 섭취량"))
                        isFatUpdate = false
                    }
                }

                var isRestFatUpdate = true
                restFat.observe(viewLifecycleOwner) { restFat ->
                    if(isRestFatUpdate){
                        if (restFat > 0) {
                            fatDataList.add(PieEntry(restFat.toFloat(), "남은 섭취량"))
                            isRestFatUpdate = false
                        } else {
                            fatDataList.add(PieEntry(0F, "남은 섭취량"))
                            isRestFatUpdate = false
                        }
                    }
                }

                // 탄수화물 pie_chart 배경 색상
                carbsBackgroundColor.observe(viewLifecycleOwner) { carbsBackgroundColor ->
                    if (carbsBackgroundColor != null && carbsBackgroundColor != 0) {

                        val dataSet = PieDataSet(carbsDataList, "")
                        dataSet.colors = listOf(
                            ContextCompat.getColor(requireContext(), carbsBackgroundColor),
                            ContextCompat.getColor(requireContext(), R.color.gray3)
                        )

                        dataSet.setDrawValues(false)
                        val piedata = PieData(dataSet)
                        pcHomeCarbohydrate.pcHomePiechartCarbohydrate.apply {
                            data = piedata
                            description.isEnabled = false
                            legend.isEnabled = false
                            isDrawHoleEnabled = false
                            isRotationEnabled = false
                            setDrawEntryLabels(false)
                            setTouchEnabled(false)

                            invalidate()
                        }
                    }
                }

                // 단백질 pie_chart 배경 색상
                proteinBackgroundColor.observe(viewLifecycleOwner) { proteinBackgroundColor ->
                    if (proteinBackgroundColor != null) {

                        val dataSet = PieDataSet(proteinDataList, "")
                        dataSet.colors = listOf(
                            ContextCompat.getColor(requireContext(), proteinBackgroundColor),
                            ContextCompat.getColor(requireContext(), R.color.gray3)
                        )

                        dataSet.setDrawValues(false)
                        val piedata = PieData(dataSet)
                        pcHomeProtein.pcHomePiechartProtein.apply {
                            data = piedata
                            description.isEnabled = false
                            legend.isEnabled = false
                            isDrawHoleEnabled = false
                            isRotationEnabled = false
                            setDrawEntryLabels(false)
                            setTouchEnabled(false)

                            invalidate()
                        }
                    }
                }

                // 지방 pie_chart 배경 색상
                fatBackgroundColor.observe(viewLifecycleOwner) { fatBackgroundColor ->
                    if (fatBackgroundColor != null) {
                        val dataSet = PieDataSet(fatDataList, "")
                        dataSet.colors = listOf(
                            ContextCompat.getColor(requireContext(), fatBackgroundColor),
                            ContextCompat.getColor(requireContext(), R.color.gray3)
                        )

                        dataSet.setDrawValues(false)
                        val piedata = PieData(dataSet)
                        pcHomeFat.pcHomePiechartFat.apply {
                            data = piedata
                            description.isEnabled = false
                            legend.isEnabled = false
                            isDrawHoleEnabled = false
                            isRotationEnabled = false
                            setDrawEntryLabels(false)
                            setTouchEnabled(false)

                            invalidate()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}