package com.project.veganlife.lifecheck.ui.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.veganlife.R
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.databinding.FragmentLifeCheckDietDetailBinding
import com.project.veganlife.lifecheck.ui.adapter.LifeCheckDietDetailAdapter
import com.project.veganlife.lifecheck.ui.adapter.LifeCheckDietDetailImageAdapter
import com.project.veganlife.lifecheck.ui.viewmodel.LifeCheckViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LifeCheckDietDetailFragment : Fragment() {

    private var _binding: FragmentLifeCheckDietDetailBinding? = null
    private val binding get() = _binding!!

    private val args: LifeCheckDietDetailFragmentArgs by navArgs()
    private val viewModel: LifeCheckViewModel by viewModels()
    private lateinit var dietDetailAdapter: LifeCheckDietDetailAdapter
    private lateinit var imageAdapter: LifeCheckDietDetailImageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLifeCheckDietDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initMealId()
        setupToolbar()
        setupRecyclerView()
        setupViewPager()
        observeMealLogDetail()
    }

    private fun initMealId() {
        val mealLogId = args.mealLogId
        viewModel.fetchMealLogDetail(mealLogId)
    }

    private fun setupToolbar() {
        binding.toolbarLifecheckDietDetail.apply {
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun setupRecyclerView() {
        dietDetailAdapter = LifeCheckDietDetailAdapter()
        binding.rvLifecheckDietDetail.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = dietDetailAdapter
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    LinearLayoutManager(context).orientation
                )
            )
        }
    }

    private fun setupViewPager() {
        imageAdapter = LifeCheckDietDetailImageAdapter()
        binding.apply {
            vpLifecheckDietDetailImage.adapter = imageAdapter
            diLifecheckDietDetail.attachTo(binding.vpLifecheckDietDetailImage)
        }
    }

    private fun observeMealLogDetail() {
        viewModel.mealLogDetail.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { result ->
                when (result) {
                    is ApiResult.Success -> {
                        val mealDetail = result.data
                        binding.toolbarLifecheckDietDetail.title =
                            when (mealDetail.mealType) {
                                "BREAKFAST" -> getString(R.string.lifecheck_breakfast)
                                "LUNCH" -> getString(R.string.lifecheck_lunch)
                                "DINNER" -> getString(R.string.lifecheck_dinner)
                                "BREAKFAST_SNACK" -> getString(R.string.lifecheck_morning_snack)
                                "LUNCH_SNACK" -> getString(R.string.lifecheck_afternoon_snack)
                                "DINNER_SNACK" -> getString(R.string.lifecheck_dinner_snack)
                                else -> "식단 상세 조회"
                            }

                        val nutrients = mealDetail.totalIntakeNutrients
                        binding.tvLifecheckDietDetailTotalKcalValue.text =
                            nutrients.calorie.toString()
                        binding.etLifecheckDietDetailCarbohydrate.text = "${nutrients.carbs}g"
                        binding.etLifecheckDietDetailProtein.text = "${nutrients.protein}g"
                        binding.etLifecheckDietDetailFat.text = "${nutrients.fat}g"

                        val mappedMeals = mealDetail.meals.map { meal ->
                            meal.mealData.copy(amount = meal.intake)
                        }

                        dietDetailAdapter.submitList(mappedMeals)
                        imageAdapter.submitList(mealDetail.imageUrls)
                    }

                    is ApiResult.Error -> Log.e("Error", result.description)
                    is ApiResult.Exception -> Log.e(
                        "Exception",
                        result.e.message ?: "Unknown error"
                    )
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}