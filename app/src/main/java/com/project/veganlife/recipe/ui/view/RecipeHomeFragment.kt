package com.project.veganlife.recipe.ui.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.veganlife.R
import com.project.veganlife.databinding.FragmentRecipeHomeBinding
import com.project.veganlife.recipe.data.model.RecipeFeedContent
import com.project.veganlife.recipe.ui.adapter.RecipeHomeAdapter
import com.project.veganlife.recipe.ui.viewmodel.RecipeHomeViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipeHomeFragment : Fragment(), RecipeHomeAdapter.OnItemClickListener {
    private var _binding: FragmentRecipeHomeBinding? = null
    private val binding get() = _binding!!

    private val viewmodel: RecipeHomeViewmodel by viewModels()

    private lateinit var recipeHomeAdapter: RecipeHomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipeHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 툴바 설정
        setToolbarMove()

        // 최상단일 시 스크롤버튼 숨김
        hideGoToScroll()

        // Scroll to top
        binding.fbRecipeScrollUp.setOnClickListener { goToTopScroll() }

        // 리싸이클러뷰 셋팅
        setRecipeFeedssList()

        viewmodel.getAllRecipeFeedsList()

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>("recipe_updated")
            ?.observe(viewLifecycleOwner) { updated ->
                if (updated) viewmodel.getAllRecipeFeedsList()
            }

        // 비건 타입 버튼 필터
        getRecipeList()

        binding.btnRecipeWrite.setOnClickListener {
            val action = RecipeHomeFragmentDirections.actionRecipeHomeFragmentToRecipeWriteFragment(
                recipeId = null,
                isEditing = false,
                recipeIngredientDescription = null
            )
            findNavController().navigate(action)
        }
    }

    private fun setToolbarMove() {
        binding.tbRecipeToolbar.setOnMenuItemClickListener { menu ->
            when (menu.itemId) {
                R.id.recipe_search -> {
                    findNavController().navigate(R.id.action_recipeHomeFragment_to_recipeSearchFragment)
                }

                R.id.recipe_notification -> {
                    findNavController().navigate(R.id.action_recipeHomeFragment_to_alarmFragment)
                }
            }
            false
        }
    }

    private fun goToTopScroll() {
        binding.rvRecipeRecipeList.smoothScrollToPosition(0)
    }

    private fun hideGoToScroll() {
        binding.apply {
            rvRecipeRecipeList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    fbRecipeScrollUp.visibility =
                        if (layoutManager.findFirstVisibleItemPosition() == 0 || recipeHomeAdapter.itemCount == 0)
                            View.GONE else View.VISIBLE
                }
            })
        }
    }

    private fun getRecipeList() {
        viewmodel.apply {
            binding.rgRecipeVeganType.setOnCheckedChangeListener { _, i ->
                when (i) {
                    R.id.rb_recipe_home_all_type -> getAllRecipeFeedsList()

                    R.id.rb_recipe_home_vegan -> getRecipeFeedByTypeList("VEGAN")

                    R.id.rb_recipe_home_lacto -> getRecipeFeedByTypeList("LACTO")

                    R.id.rb_recipe_home_ovo -> getRecipeFeedByTypeList("OVO")

                    R.id.rb_recipe_home_lacto_ovo -> getRecipeFeedByTypeList("LACTO_OVO")

                    R.id.rb_recipe_home_pesco -> getRecipeFeedByTypeList("PESCO")
                }
                goToTopScroll()
            }
        }
    }

    override fun onItemCLicked(item: RecipeFeedContent) {
        val action =
            RecipeHomeFragmentDirections.actionRecipeHomeFragmentToRecipeDetailInfoFragment(
                recipe = item
            )
        findNavController().navigate(action)
    }

    private fun setRecyclerviewAdapter() {
        recipeHomeAdapter = RecipeHomeAdapter(this)
        binding.rvRecipeRecipeList.adapter = recipeHomeAdapter

        recipeHomeAdapter.addLoadStateListener { loadState ->
            val isEndOfPaginationReached = loadState.append.endOfPaginationReached
            if (isEndOfPaginationReached) {
                binding.llRecipeNoContents.isVisible = recipeHomeAdapter.itemCount == 0
            } else {
                binding.llRecipeNoContents.isVisible = false
            }
        }
    }

    private fun setRecipeFeedssList() {
        setRecyclerviewAdapter()

        viewLifecycleOwner.lifecycleScope.launch {
            viewmodel.recipeFeedList.collectLatest { pagingData ->
                pagingData?.let { recipePagingData ->
                    recipeHomeAdapter.submitData(recipePagingData)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}