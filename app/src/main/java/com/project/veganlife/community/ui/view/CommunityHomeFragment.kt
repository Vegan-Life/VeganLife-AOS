package com.project.veganlife.community.ui.view

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.veganlife.R
import com.project.veganlife.community.data.model.Feeds
import com.project.veganlife.community.ui.adapter.FeedsAdapter
import com.project.veganlife.community.ui.viewmodel.FeedsGetViewModel
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.databinding.FragmentCommunityHomeBinding
import com.project.veganlife.utils.ui.DisplayUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class CommunityHomeFragment : Fragment() {

    private var fabOpen = false
    private var _binding: FragmentCommunityHomeBinding? = null
    private val binding get() = _binding!!

    private val feedsGetViewModel: FeedsGetViewModel by viewModels()
    private lateinit var feedsAdapter: FeedsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommunityHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //shimmer시작
        binding.shimmerCommunityhomeFeed.startShimmer()

        // scroll to top
        binding.ibCommunityhomeGoToTop.setOnClickListener {
            rvScrollToTop()
        }

        //scroll위치 리스너 -> 맨 위면 fab안보이도록
        binding.rvCommunityhomeFeed.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (firstVisibleItemPosition == 0) {
                    binding.ibCommunityhomeGoToTop.visibility = View.GONE
                } else {
                    binding.ibCommunityhomeGoToTop.visibility = View.VISIBLE
                }
            }
        })


        //피드 데이터 넣어주기
        feedsGetViewModel.feeds.observe(viewLifecycleOwner) { apiResult ->
            when (apiResult) {
                is ApiResult.Error -> {
                    val responseDesc = apiResult.description
                    Log.d("daily Error", responseDesc)
                }

                is ApiResult.Exception -> {
                    Log.d("daily Exception", apiResult.e.message ?: "No message available")
                }

                is ApiResult.Success -> {
                    val feeds = apiResult.data

                    binding.shimmerCommunityhomeFeed.stopShimmer()
                    binding.shimmerCommunityhomeFeed.visibility = View.GONE
                    //adapter에 it넣어주기
                    setFeedsAdapter(feeds)
                    //피드 게시글 여부에 따라 보여줄 화면 선택   
                    setFeedView(feeds)
                }
            }


        }

        //글쓰기 버튼 누르면 피드/레시피 버튼 등장
        binding.efabCommunityhomeWrite.setOnClickListener {

            setRecipeButton()
            setFeedButton()

            fabOpen = !fabOpen

        }

        //태그 버튼에 따라 필터링
        binding.radioGroupCommunityHomeTag.setOnCheckedChangeListener { radioGroup, i ->
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
    }

    private fun setFeedButton() {
        if (!fabOpen) {
            openButton(binding.efabCommunityhomeWriteFeed, 96f)
        } else {
            closeButton(binding.efabCommunityhomeWriteFeed)
        }
    }

    private fun setRecipeButton() {
        if (!fabOpen) {
            openButton(binding.efabCommunityhomeWriteRecipe, 52f)
        } else {
            closeButton(binding.efabCommunityhomeWriteRecipe)
        }
    }

    private fun closeButton(button: Button) {
        ObjectAnimator.ofFloat(button, "translationY", 0f)
            .apply { start() }
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

    private fun setFeedView(feeds: Feeds) {
        if (feeds.content.isEmpty()) {
            binding.linearLayoutNoContents.visibility = View.VISIBLE
            binding.rvCommunityhomeFeed.visibility = View.GONE
        } else {
            binding.linearLayoutNoContents.visibility = View.GONE
            binding.rvCommunityhomeFeed.visibility = View.VISIBLE
        }
    }

    private fun setFeedsAdapter(feeds: Feeds) {
        //새 어댑터로 교체해주기
        feedsAdapter = FeedsAdapter()
        binding.rvCommunityhomeFeed.adapter = feedsAdapter
        feedsAdapter.submitList(feeds.content)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
