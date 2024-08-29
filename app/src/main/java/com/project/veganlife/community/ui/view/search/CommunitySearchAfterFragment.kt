package com.project.veganlife.community.ui.view.search

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.project.veganlife.R
import com.project.veganlife.community.data.model.PostPreview
import com.project.veganlife.community.ui.adapter.CommunityFeedAdapter
import com.project.veganlife.community.ui.viewmodel.CommunitySearchViewModel
import com.project.veganlife.community.ui.viewmodel.FeedsGetViewModel
import com.project.veganlife.community.ui.viewmodel.PageStatus
import com.project.veganlife.databinding.FragmentCommunitySearchAfterBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class CommunitySearchAfterFragment : Fragment(), CommunityFeedAdapter.OnItemClickListener {
    private lateinit var binding: FragmentCommunitySearchAfterBinding
    private val feedsGetViewModel: FeedsGetViewModel by activityViewModels()
    private val communitySearchViewModel: CommunitySearchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCommunitySearchAfterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        communitySearchViewModel.pageStatus.value = PageStatus.AFTER
        setSearchResultRV()
    }

    /**
     * 검색 결과 표시, 검색 결과 없음 이미지 표시
     */
    private fun setSearchResultRV() {
        val adapter = CommunityFeedAdapter(this)
        binding.rvCommunitySearchFeed.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            feedsGetViewModel.postPreviewList.collectLatest { pagingData ->
                pagingData?.let { feedPagingData ->

                    binding.rvCommunitySearchFeed.visibility = View.VISIBLE
                    adapter.submitData(feedPagingData)
                }
            }
        }
    }

    override fun onItemClicked(item: PostPreview) {
        val bundle = Bundle().apply {
            putInt("postId", item.id)
        }
        findNavController().navigate(R.id.action_communityHomeFragment_to_communityDetailFeedFragment, bundle)
    }
}