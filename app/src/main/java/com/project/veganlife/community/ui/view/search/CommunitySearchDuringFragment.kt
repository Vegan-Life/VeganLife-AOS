package com.project.veganlife.community.ui.view.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.project.veganlife.R
import com.project.veganlife.community.ui.viewmodel.CommunitySearchViewModel
import com.project.veganlife.community.ui.viewmodel.PageStatus
import com.project.veganlife.databinding.FragmentCommunitySearchDuringBinding

class CommunitySearchDuringFragment : Fragment() {
    private lateinit var binding: FragmentCommunitySearchDuringBinding
    private val communitySearchViewModel: CommunitySearchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCommunitySearchDuringBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        communitySearchViewModel.pageStatus.value = PageStatus.DURING
    }

    /**
     * 입력한 검색어와 연관 검색어
     */
    private fun setRelatedSearchRV(keyword: String) {
        //todo

    }

}