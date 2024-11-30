package com.project.veganlife.recipe.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.project.veganlife.R
import com.project.veganlife.databinding.FragmentRecipeDetailInfoBinding
import com.project.veganlife.recipe.data.model.RecipeDetailDescription
import com.project.veganlife.recipe.ui.adapter.RecipeDetailDescriptionAdapter
import com.project.veganlife.recipe.ui.adapter.RecipeDetailIngredientAdapter
import com.project.veganlife.recipe.ui.viewmodel.RecipeViewmodel
import com.project.veganlife.recipe.ui.viewmodel.RecipeSharedViewmodel
import com.project.veganlife.utils.ui.VeganTypeChange.Companion.changeBackground
import com.project.veganlife.utils.ui.VeganTypeChange.Companion.changeVeganType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipeDetailInfoFragment : Fragment() {
    private var _binding: FragmentRecipeDetailInfoBinding? = null
    private val binding get() = _binding!!

    private lateinit var ingredientAdapter: RecipeDetailIngredientAdapter
    private lateinit var descriptionAdapter: RecipeDetailDescriptionAdapter

    private val sharedViewmodel: RecipeSharedViewmodel by activityViewModels()

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

        // rv 셋팅
        setRecyclerviewAdpater()

        // Indicator & viewPager2 연결
        binding.diRecipeIndicator.attachTo(binding.vpRecipeImage)

        // 수정 버튼 클릭 시 화면 이동
        binding.tvRecipeModify.setOnClickListener {
            findNavController().navigate(R.id.action_recipeDetailInfoFragment_to_recipeWriteFragment)
        }

        // 삭제 버튼 클릭 시 화면 이동
        binding.tvRecipeDelete.setOnClickListener {

        }


        binding.apply {
            // 확장 레이아웃 클릭 리스너
            clRecipeIngredientTap.setOnClickListener {
                if(rvRecipeIngredient.visibility == View.VISIBLE) {
                    rvRecipeIngredient.visibility = View.GONE
                    btnRecipeIngredient.animate().setDuration(200).rotation(180f)
                } else {
                    rvRecipeIngredient.visibility = View.VISIBLE
                    btnRecipeIngredient.animate().setDuration(200).rotation(0f)
                }
            }
            clRecipeDescriptionTap.setOnClickListener {
                if(rvRecipeDescription.visibility == View.VISIBLE) {
                    rvRecipeDescription.visibility = View.GONE
                    btnRecipeDescription.animate().setDuration(200).rotation(180f)
                } else {
                    rvRecipeDescription.visibility = View.VISIBLE
                    btnRecipeDescription.animate().setDuration(200).rotation(0f)
                }
            }

            btnRecipeLike.setOnClickListener {

            }
        }
    }

    private fun setToolbarListener() {
        binding.toolbarRecipeToolbar.run {
            setNavigationOnClickListener {
                findNavController().popBackStack()

                // 레시피 좋아요 api 연동
            }
        }
    }

    private fun setUi() {
        binding.apply {
            // 레시피 작성자와 사용자 닉네임 비교 후 ( 수정, 삭제 ) 버튼 관리
            sharedViewmodel.nickName.observe(viewLifecycleOwner) { isSame ->
                if(isSame) {
                    tvRecipeModify.visibility = View.INVISIBLE
                    tvRecipeDelete.visibility = View.INVISIBLE
                } else {
                    tvRecipeModify.visibility = View.VISIBLE
                    tvRecipeDelete.visibility = View.VISIBLE
                }
            }

            sharedViewmodel.recipeDetailContent.observe(viewLifecycleOwner) { data ->
                tvRecipeRecipeName.text = data?.recipeTitle
                tvRecipeNickname.text = data?.author?.nickname
                tvRecipeVeganType.text = data?.author?.vegetarianType

                if(data!!.isLiked) btnRecipeLike.setImageResource(R.drawable.all_like_full_recipe)
                else btnRecipeLike.setImageResource(R.drawable.all_like_empty_recipe)

                when (data.recipeTypes.size) {
                    1 -> {
                        tvRecipeAbleVeganTypeOne.text = changeVeganType(data.recipeTypes.get(0))
                        tvRecipeAbleVeganTypeOne.setBackgroundResource(changeBackground(data.recipeTypes.get(0)))
                    }

                    2 -> {
                        tvRecipeAbleVeganTypeOne.text = changeVeganType(data.recipeTypes.get(0))
                        tvRecipeAbleVeganTypeOne.setBackgroundResource(changeBackground(data.recipeTypes.get(0)))

                        tvRecipeAbleVeganTypeTwo.text = changeVeganType(data.recipeTypes.get(1))
                        tvRecipeAbleVeganTypeTwo.setBackgroundResource(changeBackground(data.recipeTypes.get(1)))
                    }
                }
            }
        }
    }

    private fun setRecyclerviewAdpater() {
        ingredientAdapter = RecipeDetailIngredientAdapter()
        descriptionAdapter = RecipeDetailDescriptionAdapter()

        binding.rvRecipeIngredient.adapter = ingredientAdapter
        binding.rvRecipeDescription.adapter = descriptionAdapter

        lifecycleScope.launch {
            sharedViewmodel.recipeDetailContent.observe(viewLifecycleOwner) { data ->
                ingredientAdapter.submitList(data?.ingredients)
            }

            sharedViewmodel.recipeDetailContent.observe(viewLifecycleOwner) { data ->
                val description = data!!.descriptions.mapIndexed { idx, description ->
                    RecipeDetailDescription(
                        number = idx + 1,
                        description = description
                    )
                }
                descriptionAdapter.submitList(description)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}