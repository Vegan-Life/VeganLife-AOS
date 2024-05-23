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
import com.project.veganlife.community.data.model.Feeds
import com.project.veganlife.community.ui.adapter.FeedsAdapter
import com.project.veganlife.community.ui.viewmodel.FeedsGetViewModel
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.databinding.FragmentCommunityHomeBinding
import com.project.veganlife.utils.ui.DisplayUtils
import dagger.hilt.android.AndroidEntryPoint

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
        feedsAdapter = FeedsAdapter()
        binding.rvCommunityhomeFeed.adapter = feedsAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // scroll to top
        binding.ibCommunityhomeGoToTop.setOnClickListener {
            rvScrollToTop()
        }

        //피드 데이터 넣어주기
        feedsGetViewModel.feeds.observe(viewLifecycleOwner) {apiResult ->
            when(apiResult) {
                is ApiResult.Error -> {
                    val responseDesc = apiResult.description
                    Log.d("daily Error", responseDesc)
                }

                is ApiResult.Exception -> {
                    Log.d("daily Exception", apiResult.e.message ?: "No message available")
                }

                is ApiResult.Success -> {
                    val feeds = apiResult.data

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
    }

    private fun setFeedButton() {
        if (!fabOpen) {
            openButton(binding.efabCommunityhomeWriteFeed,96f)
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
        feedsAdapter.submitList(feeds.content)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
