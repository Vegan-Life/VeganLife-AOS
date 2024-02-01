package com.project.veganlife.community.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.veganlife.community.ui.adapter.FeedsAdapter
import com.project.veganlife.community.ui.viewmodel.FeedsGetViewModel
import com.project.veganlife.databinding.FragmentCommunityHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommunityHomeFragment : Fragment() {
    private var _binding: FragmentCommunityHomeBinding? = null
    private val binding get() = _binding!!

    private val feedsGetViewModel: FeedsGetViewModel by viewModels()
    private lateinit var feedsAdapter: FeedsAdapter
    private var accessToken = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJrY2s4NTA3NUBuYXZlci5jb20iLCJleHAiOjE3MDY4MjAyNjUsImlhdCI6MTcwNjgxMzA2NX0.JGWL_LBvau2l-ypmedIhCXWsYVI6UOJ1z7Ig4BC4qZw"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCommunityHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        feedsAdapter = FeedsAdapter()
        feedsGetViewModel.getFeeds(accessToken)
        feedsGetViewModel.feedsGet.observe(viewLifecycleOwner) { response ->
            feedsAdapter.submitList(response.content)
        }
        initFeedsRecyclerView()
    }

    private fun initFeedsRecyclerView() {
        binding.rvCommunityhomeFeed.apply {
            adapter = feedsAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
