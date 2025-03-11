package com.project.veganlife.recipe.ui.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.project.veganlife.databinding.FragmentRecipeSearchBinding
import com.project.veganlife.recipe.data.model.RecipeFeedContent
import com.project.veganlife.recipe.ui.adapter.RecipeRecentSearchAdapter
import com.project.veganlife.recipe.ui.adapter.RecipeSearchedAdapter
import com.project.veganlife.recipe.ui.viewmodel.RecipeSearchViewmodel
import com.project.veganlife.utils.KeyboardUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipeSearchFragment : Fragment(), RecipeRecentSearchAdapter.OnItemClickListener,
    RecipeSearchedAdapter.OnItemClickListener {
    private var _binding: FragmentRecipeSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RecipeSearchViewmodel by viewModels()

    private lateinit var recentSearchAdapter: RecipeRecentSearchAdapter
    private lateinit var searchResultsAdapter: RecipeSearchedAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbarListener()

        setupObservers()

        setupSearchListener()
    }

    private fun setToolbarListener() {
        binding.toolbarRecipeSearchToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    // 🔹 어댑터 설정
    private fun setupAdapters() {
        recentSearchAdapter = RecipeRecentSearchAdapter(
            onDeleteClick = { position -> deleteRecentSearch(position) },
            recipeRecentSearchItemClickListener = this
        )

        searchResultsAdapter = RecipeSearchedAdapter(this)

        binding.apply {
            rvRecipeRelatedList.adapter = recentSearchAdapter
            rvRecipeSearchedList.adapter = searchResultsAdapter
        }

        // `loadStateListener` 추가
        searchResultsAdapter.addLoadStateListener { loadStates ->
            val hasResults = searchResultsAdapter.itemCount > 0
            if (hasResults) {
                updateUI(
                    hasRecentSearches = viewModel.recentSearches.value?.isNotEmpty() ?: false,
                    hasResults = hasResults
                )
            } else {
                updateUI(
                    hasRecentSearches = false,
                    hasResults = hasResults
                )
            }
        }
    }

    // 🔹 뷰모델의 데이터 관찰 및 UI 업데이트
    private fun setupObservers() {
        setupAdapters()

        viewModel.recentSearches.observe(viewLifecycleOwner) { recentSearchesList ->
            recentSearchAdapter.submitList(recentSearchesList)
            updateUI(hasRecentSearches = recentSearchesList.isNotEmpty(), hasResults = false)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.searchResults.collectLatest { pagingData ->
                Log.d("##RecipeSearch", "searchResults updated: ${pagingData}")
                pagingData.let { researchRecipe ->
                    searchResultsAdapter.submitData(researchRecipe)
                }
            }
        }
    }

    // 🔹 EditText에서 Enter 키 이벤트 감지 후 검색 실행
    private fun setupSearchListener() {
        binding.etRecipeSearchToolbarSearchBox.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                true
            } else {
                false
            }
        }

        // 🔹 돋보기 아이콘 클릭 시 검색 실행
        binding.etRecipeSearchToolbarSearchBox.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd =
                    binding.etRecipeSearchToolbarSearchBox.compoundDrawablesRelative[2] // 오른쪽 아이콘
                if (drawableEnd != null && event.rawX >= (binding.etRecipeSearchToolbarSearchBox.right - drawableEnd.bounds.width())) {
                    v.performClick()
                    performSearch()
                    return@setOnTouchListener true
                }
            }
            false
        }
    }

    private fun updateUI(hasRecentSearches: Boolean, hasResults: Boolean) {
        Log.d("ui 로그", "hasRecentSearches: $hasRecentSearches, hasResults: $hasResults")
        binding.apply {
            clRecipeEmpty.visibility =
                if (!hasRecentSearches && !hasResults) View.VISIBLE else View.GONE
            rvRecipeRelatedList.visibility =
                if (hasRecentSearches && !hasResults) View.VISIBLE else View.GONE
            rvRecipeSearchedList.visibility =
                if (hasResults) View.VISIBLE else View.GONE
        }
    }

    // 🔹 실제 검색 실행 함수
    private fun performSearch() {
        val keyword = binding.etRecipeSearchToolbarSearchBox.text.toString().trim()
        if (keyword.isNotEmpty()) {
            viewModel.searchRecipe(keyword)
            KeyboardUtils.hideKeyboard(requireActivity()) // 키보드 숨기기
        }
    }

    // 🔹 최근 검색어 삭제
    private fun deleteRecentSearch(position: Int) {
        val currentList = viewModel.recentSearches.value?.toMutableList()
        currentList!!.removeAt(position)
        viewModel.saveRecentSearch(currentList.toList())
    }

    // item 클릭 후 해당 text를 editText에 값을 표기하고, 레시피 검색 api를 통신할 함수
    override fun onItemCLicked(item: String) {
        binding.etRecipeSearchToolbarSearchBox.setText(item) // 검색어 입력창에 text 적용
        viewModel.searchRecipe(item) // 검색 실행
    }

    override fun onItemCLicked(item: RecipeFeedContent) {
        val action =
            RecipeSearchFragmentDirections.actionRecipeSearchFragmentToRecipeDetailInfoFragment(item)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}