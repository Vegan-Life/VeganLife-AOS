package com.project.veganlife.community.ui.view

import android.content.Context
import android.hardware.display.DisplayManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.project.veganlife.community.data.model.Post
import com.project.veganlife.community.ui.adapter.PostImagesViewPagerAdapter
import com.project.veganlife.community.ui.adapter.TagListAdapter
import com.project.veganlife.community.ui.viewmodel.PostViewModel
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.databinding.FragmentCommunityDetailFeedBinding
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class CommunityDetailFeedFragment : Fragment() {
    private val binding: FragmentCommunityDetailFeedBinding by lazy {
        FragmentCommunityDetailFeedBinding.inflate(
            layoutInflater
        )
    }

    private var post: Post? = null

    private val postViewModel: PostViewModel by activityViewModels()
    private lateinit var tagListAdapter: TagListAdapter
    private lateinit var viewPagerAdapter: PostImagesViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val postId = arguments?.getInt("postId") ?: -1
        getPost(postId)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        event()
    }

    private fun init() {
        binding.layoutContentLoading.visibility = View.VISIBLE
        binding.contentLoading.show()
        binding.contentScrollView.visibility = View.GONE


        tagListAdapter = TagListAdapter()
        binding.rvCommunityDetailFeedKeyword.adapter = tagListAdapter
        val flexBoxLayoutManager = FlexboxLayoutManager(requireContext()).apply {
            flexDirection = FlexDirection.ROW
        }
        binding.rvCommunityDetailFeedKeyword.layoutManager = flexBoxLayoutManager

        viewPagerAdapter = PostImagesViewPagerAdapter()
        binding.vpCommunityDetailFeedImage.adapter = viewPagerAdapter
    }

    private fun getPost(postId: Int) {
        postViewModel.getPost(postId)
        postViewModel.post.observe(viewLifecycleOwner) { postApiResult ->
            when (postApiResult) {
                is ApiResult.Error -> {
                    val intakeResponse = postApiResult.description
                    Log.d("daily Error", intakeResponse)
                }

                is ApiResult.Exception -> {
                    Log.d("daily Exception", postApiResult.e.message ?: "No message available")
                }

                is ApiResult.Success -> {
                    binding.layoutContentLoading.visibility = View.GONE
                    binding.contentLoading.hide()
                    binding.contentScrollView.visibility = View.VISIBLE

                    post = postApiResult.data

                    setFeedDetailUi()
                    setAuthorDetail()
                }
            }
        }

    }

    private fun setFeedDetailUi() {
        if (post != null) {
            binding.tvCommunityDetailFeedTitle.text = post!!.title
            binding.tvCommunityDetailFeedDescription.text = post!!.content
            binding.tvCommunityDetailFeedDateTime.text = formatDateTime(post!!.createdAt)
            setImageViewPager(post!!.imageUrls)
            setTags(post!!.tags)
        }

    }

    private fun formatDateTime(input: String): String {
        // 1. 2024-08-23T08:38 부분만 추출
        val dateTimePart = input.substring(0, 16)

        // 2. LocalDateTime으로 변환 (초와 나노초 제외)
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
        val dateTime = LocalDateTime.parse(dateTimePart, inputFormatter)

        // 3. 원하는 출력 포맷 설정
        val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd a hh:mm", Locale.getDefault())

        // 4. 변환된 LocalDateTime을 원하는 형식으로 포맷
        return dateTime.format(outputFormatter)
    }

    private fun setImageViewPager(imageUrls: List<String>) {
        viewPagerAdapter.submitList(imageUrls)
    }

    private fun setTags(tags: List<String>) {
        tagListAdapter.submitList(tags)
    }

    private fun setAuthorDetail() {
        if (post != null) {
            binding.tvCommunityDetailFeedNickname.text = post!!.author
            Glide.with(requireContext())
                .load(post!!.profileImageUrl)
                .apply(
                    RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)
                ) // 캐시 사용하지 않도록 설정
                .into(binding.civCommunityDetailFeedProfile)
            binding.tvCommunityDetailFeedVeganType.text = post!!.vegetarianType
        }
    }

    private fun event() {

    }
}
