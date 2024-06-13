package com.project.veganlife.mypage.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.veganlife.databinding.FragmentMypageScrapsRecipeBinding
import com.project.veganlife.mypage.ui.adapter.MypageScrapedRecipeAdapter
import com.project.veganlife.mypage.ui.viewmodel.MypageScrapedRecipeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MypageScrapsRecipeFragment : Fragment() {
    private var _binding: FragmentMypageScrapsRecipeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MypageScrapedRecipeAdapter

    private val viewModel: MypageScrapedRecipeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMypageScrapsRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbarListener()

        getScrapedRecipe()

        // ui
        setScrapedRecipe()

        setRecipeLikeBackground()
    }

    private fun setToolbarListener() {
        binding.toolbarMypageToolbar.run {
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun getScrapedRecipe() {
        viewModel.getScrapedRecipe()
    }

    private fun setScrapedRecipe() {
        lifecycleScope.launch {
            adapter = MypageScrapedRecipeAdapter(viewModel)
            binding.apply {
                rvMypageRecipe.layoutManager = LinearLayoutManager(requireContext())
                rvMypageRecipe.adapter = adapter
            }

            viewModel.scrapedRecipe.collectLatest { pagingData ->
                pagingData?.let {
                    adapter.submitData(it)
                }
            }
        }
    }

    private fun setRecipeLikeBackground() {
        viewModel.apply {
            recipeLikeResponse.observe(viewLifecycleOwner) {
                adapter.refresh()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}