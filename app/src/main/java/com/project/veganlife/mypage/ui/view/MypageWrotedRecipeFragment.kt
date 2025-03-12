package com.project.veganlife.mypage.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.cachedIn
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.veganlife.R
import com.project.veganlife.databinding.FragmentMypageWrotedRecipeBinding
import com.project.veganlife.mypage.ui.adapter.MypageWrotedRecipeAdapter
import com.project.veganlife.mypage.ui.viewmodel.MypageWrotedPagingViewModel
import com.project.veganlife.recipe.data.model.RecipeFeedContent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MypageWrotedRecipeFragment : Fragment(), MypageWrotedRecipeAdapter.OnItemClickListener {
    private var _binding: FragmentMypageWrotedRecipeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MypageWrotedRecipeAdapter
    private val viewModel: MypageWrotedPagingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMypageWrotedRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbarListener()
        getWrotedRecipe()

        // ui
        setPostedFeedList()
    }

    private fun setToolbarListener() {
        binding.toolbarMypageToolbar.run {
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun getWrotedRecipe() {
        viewModel.getWrotedRecipe()
    }

    private fun setPostedFeedList() {
        // Adapter에 Context 전달
        adapter = MypageWrotedRecipeAdapter(this)
        binding.rvMypageFeed.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMypageFeed.adapter = adapter

        lifecycleScope.launch {
            viewModel.wrotedRecipe
                .cachedIn(viewLifecycleOwner.lifecycleScope)
                .collectLatest { pagingData ->
                    adapter.submitData(pagingData)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemCLicked(item: RecipeFeedContent) {
        val action = MypageWrotedRecipeFragmentDirections.actionGlobalToRecipeDetailInfoFragment(
            recipe = item)
        findNavController().navigate(action)
    }
}