package com.project.veganlife.community.ui.view

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.project.veganlife.community.ui.adapter.FeedsAdapter
import com.project.veganlife.community.ui.viewmodel.FeedsGetViewModel
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
            binding.rvCommunityhomeFeed.smoothScrollToPosition(0)
        }

        //피드 데이터 넣어주기
        feedsGetViewModel.feeds.observe(viewLifecycleOwner) {
            //adapter에 it넣어주기
            feedsAdapter.submitList(it.content)
            Log.d("community", it.content.toString())

            //피드 게시글 여부에 따라 보여줄 화면 선택
            if (it.content.isEmpty()) {
                binding.linearLayoutNoContents.visibility = View.VISIBLE
                binding.rvCommunityhomeFeed.visibility = View.GONE
            } else {
                binding.linearLayoutNoContents.visibility = View.GONE
                binding.rvCommunityhomeFeed.visibility = View.VISIBLE
            }

        }

        //글쓰기 버튼 누르면 피드/레시피 버튼 등장
        binding.efabCommunityhomeWrite.setOnClickListener {

            //피드 - 레시피  -> 4dp
            //레시피 - 글쓰기 -> 12dp
            if (!fabOpen) {
                ObjectAnimator.ofFloat(
                    binding.efabCommunityhomeWriteRecipe,
                    "translationY",
                    -1 * DisplayUtils.dpToPx(requireContext(), 52f)
                ).apply { start() }
                ObjectAnimator.ofFloat(
                    binding.efabCommunityhomeWriteFeed,
                    "translationY",
                    -1 * DisplayUtils.dpToPx(requireContext(), 96f)
                ).apply { start() }
            } else {
                ObjectAnimator.ofFloat(binding.efabCommunityhomeWriteRecipe, "translationY", 0f)
                    .apply { start() }
                ObjectAnimator.ofFloat(binding.efabCommunityhomeWriteFeed, "translationY", 0f)
                    .apply { start() }
            }

            fabOpen = !fabOpen

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
