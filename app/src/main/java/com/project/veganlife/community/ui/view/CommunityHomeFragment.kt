package com.project.veganlife.community.ui.view

import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.veganlife.R
import com.project.veganlife.community.ui.adapter.CommunityFeedAdapter
import com.project.veganlife.community.ui.viewmodel.FeedsGetViewModel
import com.project.veganlife.databinding.FragmentCommunityHomeBinding
import com.project.veganlife.utils.ui.DisplayUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class CommunityHomeFragment : Fragment() {

    private var fabOpen = false
    private var _binding: FragmentCommunityHomeBinding? = null
    private val binding get() = _binding!!

    private val feedsGetViewModel: FeedsGetViewModel by viewModels()
    private lateinit var communityFeedAdapter: CommunityFeedAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommunityHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        feedsGetViewModel.getFeeds()

        // Start shimmer animation
        binding.shimmerCommunityhomeFeed.startShimmer()

        setupFeedList()

        // Scroll to top
        binding.ibCommunityhomeGoToTop.setOnClickListener { rvScrollToTop() }

        // Scroll listener to hide FAB at top
        binding.rvCommunityhomeFeed.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                binding.ibCommunityhomeGoToTop.visibility =
                    if (layoutManager.findFirstVisibleItemPosition() == 0) View.GONE else View.VISIBLE
            }
        })

        // Toggle write FAB buttons
        binding.efabCommunityhomeWrite.setOnClickListener { toggleFabButtons() }

        // Tag button filter
        binding.radioGroupCommunityHomeTag.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.btn_community_home_tag_all -> {
                    feedsGetViewModel.getFeeds()
                }

                R.id.btn_community_home_tag_worry -> {
                    feedsGetViewModel.getFeedsByTag(resources.getString(R.string.community_tag_worry_txt))
                }

                R.id.btn_community_home_tag_together -> {
                    feedsGetViewModel.getFeedsByTag(resources.getString(R.string.community_tag_together_txt))
                }

                R.id.btn_community_home_tag_recipe -> {
                    feedsGetViewModel.getFeedsByTag(resources.getString(R.string.community_tag_recipe_txt))
                }
            }
        }

        binding.toolbarCommunityhome.setOnMenuItemClickListener { item ->
            when(item.itemId) {
                R.id.community_notification -> {
                    //todo: notification
                    true
                }
                R.id.community_search -> {
                    //todo: search동작
                    true
                }
                else -> false
            }
        }


    }

    private fun toggleFabButtons() {
        if (fabOpen) {
            closeButton(binding.efabCommunityhomeWriteFeed)
            closeButton(binding.efabCommunityhomeWriteRecipe)
        } else {
            openButton(binding.efabCommunityhomeWriteFeed, 96f)
            openButton(binding.efabCommunityhomeWriteRecipe, 52f)
        }
        fabOpen = !fabOpen
    }

    private fun closeButton(button: Button) {
        ObjectAnimator.ofFloat(button, "translationY", 0f).apply { start() }
    }

    private fun openButton(button: Button, transitionY: Float) {
        ObjectAnimator.ofFloat(
            button,
            "translationY",
            -1 * DisplayUtils.dpToPx(requireContext(), transitionY)
        ).apply { start() }
    }

    private fun rvScrollToTop() {
        binding.rvCommunityhomeFeed.smoothScrollToPosition(0)
    }

    private fun setFeedView(adapter: CommunityFeedAdapter) {
        Log.i("##INFO", "setFeedView: 도달")
        if (adapter.itemCount == 0) {
            binding.linearLayoutNoContents.visibility = View.VISIBLE
            binding.rvCommunityhomeFeed.visibility = View.GONE
        } else {
            binding.linearLayoutNoContents.visibility = View.GONE
            binding.rvCommunityhomeFeed.visibility = View.VISIBLE
        }
    }

    private fun setupFeedList() {

        communityFeedAdapter = CommunityFeedAdapter()
        binding.rvCommunityhomeFeed.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = communityFeedAdapter
        }

        communityFeedAdapter.addLoadStateListener {
            if(it.append.endOfPaginationReached) {
                binding.linearLayoutNoContents.isVisible = communityFeedAdapter.itemCount == 0
            } else {
                binding.linearLayoutNoContents.isVisible = false
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {

            feedsGetViewModel.feedList.collectLatest { pagingData ->
                pagingData?.let { feedPagingData ->
                    Log.i("##INFO", "observeFeeds: $pagingData")

                    binding.shimmerCommunityhomeFeed.stopShimmer()
                    binding.shimmerCommunityhomeFeed.visibility = View.GONE

                    communityFeedAdapter.submitData(feedPagingData)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
