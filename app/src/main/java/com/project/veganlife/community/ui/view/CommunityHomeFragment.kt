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
    private var accessToken = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjaGxya2R1czM1QG5hdGUuY29tIiwiZXhwIjoxNzEyNTAyNzk1LCJpYXQiOjE3MTE4OTc5OTV9.7IPaGTNM6V2HDHWRaNU5Z-kzx9njeUGDgF3dE8XzJkM"

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
        setWriteButton()
    }

    private fun setWriteButton() {
        binding.efabCommunityhomeWrite.setOnClickListener {
            //피드 작성, 레시피 작성 두 버튼 뜨기
        }
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
