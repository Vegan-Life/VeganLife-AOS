package com.project.veganlife.recipe.ui.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.project.veganlife.R
import com.project.veganlife.databinding.FragmentRecipeDetailInfoBinding
import com.project.veganlife.recipe.data.model.RecipeDetailDescription
import com.project.veganlife.recipe.ui.adapter.RecipeDetailDescriptionAdapter
import com.project.veganlife.recipe.ui.adapter.RecipeDetailInfoImageViewAdapter
import com.project.veganlife.recipe.ui.adapter.RecipeDetailIngredientAdapter
import com.project.veganlife.recipe.ui.viewmodel.RecipeDetailViewModel
import com.project.veganlife.recipe.ui.viewmodel.RecipeViewmodel
import com.project.veganlife.utils.ui.VeganTypeChange.Companion.changeBackground
import com.project.veganlife.utils.ui.VeganTypeChange.Companion.changeVeganType
import com.project.veganlife.utils.ui.VeganTypeChange.Companion.changeVeganTypeTextColor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipeDetailInfoFragment : Fragment() {
    private var _binding: FragmentRecipeDetailInfoBinding? = null
    private val binding get() = _binding!!

    private lateinit var ingredientAdapter: RecipeDetailIngredientAdapter
    private lateinit var descriptionAdapter: RecipeDetailDescriptionAdapter
    private lateinit var viewPagerAdapter: RecipeDetailInfoImageViewAdapter

    private val recipeDetailViewModel: RecipeDetailViewModel by viewModels()
    private val recipeViewModel: RecipeViewmodel by viewModels()
    private val args: RecipeDetailInfoFragmentArgs by navArgs()

    // ViewPager2 콜백 변수
    private val viewPagerCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            adjustViewPagerHeight(position)
        }
    }

    var isLikeState = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isLikeState = args.recipe.isLiked

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipeDetailInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 툴바 셋팅
        setToolbarListener()

        // ui
        setUi()

        // Indicator & viewPager2 연결
        recipeDetailViewModel.recipeDetailContent.observe(viewLifecycleOwner) { data ->
            if (data.imageUrls.size == 1) binding.diRecipeIndicator.visibility = View.INVISIBLE
            else binding.diRecipeIndicator.attachTo(binding.vpRecipeImage)
        }

        binding.apply {
            // 수정 버튼 클릭 시 화면 이동
            tvRecipeModify.setOnClickListener {
                val action =
                    RecipeDetailInfoFragmentDirections.actionRecipeDetailInfoFragmentToRecipeWriteFragment(
                        recipeId = args.recipe,
                        isEditing = true,
                        recipeIngredientDescription = recipeDetailViewModel.recipeDetailContent.value!!
                    )
                findNavController().navigate(action)
            }

            // 삭제 버튼 클릭 시 화면 이동
            tvRecipeDelete.setOnClickListener {
                val dialog = RecipeDeleteDialogFragment(args.recipe.id)
                dialog.show(parentFragmentManager, "deleteRecipe")
            }

            // 좋아요 버튼 클릭 시
            btnRecipeLike.setOnClickListener {
                isLikeState = !isLikeState
                if (isLikeState) btnRecipeLike.setImageResource(R.drawable.all_like_full_recipe)
                else btnRecipeLike.setImageResource(R.drawable.all_like_empty_recipe)
            }
        }
    }

    private fun setToolbarListener() {
        binding.toolbarRecipeToolbar.run {
            setNavigationOnClickListener {
                if (isLikeState) recipeViewModel.likeRecipe(args.recipe.id)
                else recipeViewModel.likeCancelRecipe(args.recipe.id)

                findNavController().previousBackStackEntry?.savedStateHandle?.set(
                    "recipe_updated", true
                )

                findNavController().navigateUp()
            }
        }
    }

    private fun setUi() {
        binding.apply {
            args.recipe.apply {
                // 레시피 작성자와 사용자 닉네임 비교 후 ( 수정, 삭제 ) 버튼 관리
                val userNickname = recipeDetailViewModel.getValue()
                val recipeAuthor = author.nickname

                if (userNickname.trim() == recipeAuthor.trim()) {
                    tvRecipeModify.visibility = View.VISIBLE
                    tvRecipeDelete.visibility = View.VISIBLE
                } else {
                    tvRecipeModify.visibility = View.GONE
                    tvRecipeDelete.visibility = View.GONE
                }

                tvRecipeRecipeName.text = recipeTitle
                tvRecipeNickname.text = author.nickname

                tvRecipeVeganType.apply {
                    text = changeVeganType(author.vegetarianType)
                    setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            changeVeganTypeTextColor(author.vegetarianType)
                        )
                    )
                    setBackgroundResource(changeBackground(author.vegetarianType))
                }

                if (isLiked) btnRecipeLike.setImageResource(R.drawable.all_like_full_recipe)
                else btnRecipeLike.setImageResource(R.drawable.all_like_empty_recipe)

                val typeOne = args.recipe.recipeTypes.getOrNull(0)
                val typeTwo = args.recipe.recipeTypes.getOrNull(1)

                typeOne?.let {
                    tvRecipeAbleVeganTypeOne.apply {
                        text = changeVeganType(it)
                        setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                changeVeganTypeTextColor(it)
                            )
                        )
                        setBackgroundResource(changeBackground(it))
                    }
                }

                typeTwo?.let {
                    tvRecipeAbleVeganTypeTwo.apply {
                        text = changeVeganType(it)
                        setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                changeVeganTypeTextColor(it)
                            )
                        )
                        setBackgroundResource(changeBackground(it))
                    }
                }

            }
        }
        // rv 셋팅
        setRecyclerviewAdpater()

        // 익스팬더블 layout 셋팅
        setExpandableLayout()
    }

    private fun setRecyclerviewAdpater() {
        ingredientAdapter = RecipeDetailIngredientAdapter()
        descriptionAdapter = RecipeDetailDescriptionAdapter()
        viewPagerAdapter = RecipeDetailInfoImageViewAdapter()

        binding.apply {
            rvRecipeIngredient.adapter = ingredientAdapter
            rvRecipeDescription.adapter = descriptionAdapter
            vpRecipeImage.adapter = viewPagerAdapter

            vpRecipeImage.registerOnPageChangeCallback(viewPagerCallback)
        }

        lifecycleScope.launch {
            recipeDetailViewModel.recipeDetailContent.observe(viewLifecycleOwner) { data ->
                data?.let {
                    // 이미지 리스트 업데이트
                    setImageViewPager(it.imageUrls)

                    // 재료 리스트 업데이트
                    ingredientAdapter.submitList(it.ingredients)

                    // 설명 리스트 업데이트
                    val description = it.descriptions.mapIndexed { idx, description ->
                        RecipeDetailDescription(
                            number = idx + 1,
                            description = description
                        )
                    }
                    descriptionAdapter.submitList(description)
                }
            }
        }

    }

    private fun setImageViewPager(imageUrls: List<String>) {
        if (imageUrls.isEmpty()) {
            binding.vpRecipeImage.visibility = View.INVISIBLE
        } else {
            binding.vpRecipeImage.visibility = View.VISIBLE
            viewPagerAdapter.submitList(imageUrls)

            adjustViewPagerHeight(0) // 첫 번째 페이지의 높이 조정
        }
    }

    private fun adjustViewPagerHeight(position: Int) {
        val recyclerView = binding.vpRecipeImage.getChildAt(0) as RecyclerView
        val view = recyclerView.layoutManager?.findViewByPosition(position)
        view?.post {
            val wMeasureSpec =
                View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY)
            val hMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            view.measure(wMeasureSpec, hMeasureSpec)

            if (recyclerView.layoutParams.height != view.measuredHeight) {
                recyclerView.layoutParams.height = view.measuredHeight
            }
        }
    }

    private fun setExpandableLayout() {
        binding.apply {
            // 확장 레이아웃 클릭 리스너
            clRecipeIngredientTap.setOnClickListener {
                if (rvRecipeIngredient.visibility == View.VISIBLE) {
                    rvRecipeIngredient.visibility = View.GONE
                    btnRecipeIngredient.animate().setDuration(200).rotation(180f)
                } else {
                    rvRecipeIngredient.visibility = View.VISIBLE
                    btnRecipeIngredient.animate().setDuration(200).rotation(0f)
                }
            }
            clRecipeDescriptionTap.setOnClickListener {
                if (rvRecipeDescription.visibility == View.VISIBLE) {
                    rvRecipeDescription.visibility = View.GONE
                    btnRecipeDescription.animate().setDuration(200).rotation(180f)
                } else {
                    rvRecipeDescription.visibility = View.VISIBLE
                    btnRecipeDescription.animate().setDuration(200).rotation(0f)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}