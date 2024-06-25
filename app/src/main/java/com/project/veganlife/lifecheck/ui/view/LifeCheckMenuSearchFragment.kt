package com.project.veganlife.lifecheck.ui.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.veganlife.R
import com.project.veganlife.databinding.FragmentLifeCheckMenuSearchBinding
import com.project.veganlife.lifecheck.ui.adapter.LifeCheckMealDataAdapter
import com.project.veganlife.lifecheck.ui.viewmodel.LifeCheckViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LifeCheckMenuSearchFragment : Fragment() {

    private var _binding: FragmentLifeCheckMenuSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LifeCheckViewModel by activityViewModels()
    private lateinit var adapter: LifeCheckMealDataAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLifeCheckMenuSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupRecyclerView()
        observeViewModel()
        setupListener()
        searchAllMenu()
        setupRegisterButton()
    }

    private fun setupToolbar() {
        binding.toolbarLifecheckMenuSearchToolbar.title = viewModel.selectedDietType.value
    }

    private fun setupRecyclerView() {
        adapter = LifeCheckMealDataAdapter()
        binding.rvLifecheckMenuSearch.layoutManager = LinearLayoutManager(context)
        binding.rvLifecheckMenuSearch.adapter = adapter
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.mealData.collectLatest {
                    adapter.submitData(it)
                }
            }
        }
    }

    private fun setupListener() {
        binding.run {
            btnLifecheckMenuSearchAllMenu.setOnClickListener {
                it.isSelected = true
                btnLifecheckMenuSearchMyMenu.isSelected = false
                searchAllMenu()
                updateButtonUI()
            }

            btnLifecheckMenuSearchMyMenu.setOnClickListener {
                it.isSelected = true
                btnLifecheckMenuSearchAllMenu.isSelected = false
                searchMyMenu()
                updateButtonUI()
            }

            etLifecheckMenuSearchBox.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    updateSearchMenu(s.toString())
                }
            })
        }
    }

    private fun updateButtonUI() {
        binding.run {
            if (btnLifecheckMenuSearchAllMenu.isSelected) {
                btnLifecheckMenuSearchAllMenu.setBackgroundResource(R.drawable.lifecheck_menu_search_rect_gray1_base3)
                btnLifecheckMenuSearchAllMenu.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.base3
                    )
                )
                btnLifecheckMenuSearchMyMenu.setBackgroundResource(R.drawable.lifecheck_menu_search_rect_gray1_subgray2)
                btnLifecheckMenuSearchMyMenu.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.sub_gray2
                    )
                )
            } else {
                btnLifecheckMenuSearchAllMenu.setBackgroundResource(R.drawable.lifecheck_menu_search_rect_gray1_subgray2)
                btnLifecheckMenuSearchAllMenu.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.sub_gray2
                    )
                )
                btnLifecheckMenuSearchMyMenu.setBackgroundResource(R.drawable.lifecheck_menu_search_rect_gray1_base3)
                btnLifecheckMenuSearchMyMenu.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.base3
                    )
                )
            }
        }
    }

    private fun searchAllMenu() {
        val keyword = binding.etLifecheckMenuSearchBox.text.toString()
        viewModel.searchMealData(keyword, "ALL")
    }

    private fun searchMyMenu() {
        val keyword = binding.etLifecheckMenuSearchBox.text.toString()
        viewModel.searchMealData(keyword, "MEMBER")
    }

    private fun updateSearchMenu(keyword: String) {
        val ownerType = if (binding.btnLifecheckMenuSearchAllMenu.isSelected) {
            "ALL"
        } else {
            "MEMBER"
        }
        viewModel.searchMealData(keyword, ownerType)
    }

    private fun setupRegisterButton() {
        binding.tvLifecheckMenuSearchMenuInput.setOnClickListener {
            findNavController().navigate(R.id.action_lifeCheckMenuSearchFragment_to_lifeCheckMenuAddFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}