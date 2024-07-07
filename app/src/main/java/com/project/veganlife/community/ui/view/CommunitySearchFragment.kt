package com.project.veganlife.community.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.project.veganlife.R
import com.project.veganlife.databinding.FragmentCommunitySearchBinding

class CommunitySearchFragment : Fragment() {
    private lateinit var binding : FragmentCommunitySearchBinding
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

    }

}
