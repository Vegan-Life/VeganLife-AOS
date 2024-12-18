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
import com.project.veganlife.data.model.ApiResult
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
        // 어댑터 초기화 시 롱클릭 리스너 전달
        adapter =
            LifeCheckMealDataAdapter(object : LifeCheckMealDataAdapter.OnItemLongClickListener {
                override fun onItemLongClicked(id: Long) {

                    val dialog = LifeCheckCustomDialogFragment.newInstance(id)
                    dialog.show(parentFragmentManager, "LifeCheckCustomDialogFragment")
                }
            })
        binding.rvLifecheckMenuSearch.layoutManager = LinearLayoutManager(context)
        binding.rvLifecheckMenuSearch.adapter = adapter
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.mealData.collectLatest {
                    // 현재 선택된 ownerType 확인
                    val isMemberSelected = binding.btnLifecheckMenuSearchMyMenu.isSelected
                    // 어댑터에 롱클릭 활성화 여부 설정
                    adapter.setLongClickEnabled(isMemberSelected)
                    adapter.submitData(it)
                }

                viewModel.mealDataById.observe(viewLifecycleOwner) { result ->
                    when (result) {
                        is ApiResult.Success -> {

                        }

                        is ApiResult.Error -> {

                        }

                        is ApiResult.Exception -> {

                        }
                    }
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
                // 롱클릭 비활성화
                adapter.setLongClickEnabled(false)
            }

            btnLifecheckMenuSearchMyMenu.setOnClickListener {
                it.isSelected = true
                btnLifecheckMenuSearchAllMenu.isSelected = false
                searchMyMenu()
                updateButtonUI()
                // 롱클릭 활성화
                adapter.setLongClickEnabled(true)
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