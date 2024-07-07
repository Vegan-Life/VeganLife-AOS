package com.project.veganlife.community.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.project.veganlife.community.ui.viewmodel.RecentSearchesViewModel
import com.project.veganlife.databinding.FragmentCommunitySearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommunitySearchFragment : Fragment() {
    private lateinit var binding: FragmentCommunitySearchBinding
    private val recentSearchesViewModel: RecentSearchesViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCommunitySearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecentSearchRV()
        setRelatedSearchRV()
    }

    /**
     * 입력한 검색어와 연관 검색어
     */
    private fun setRelatedSearchRV() {
        TODO("Not yet implemented")
    }

    /**
     * 최근 검색어
     */
    private fun setRecentSearchRV() {
        recentSearchesViewModel.recentSearchList.observe(viewLifecycleOwner) {
            val adapter = RecentSearchAdapter()
            binding.rvCommunitySearchRecent.adapter = adapter

            adapter.submitList(it)
        }
    }

}
