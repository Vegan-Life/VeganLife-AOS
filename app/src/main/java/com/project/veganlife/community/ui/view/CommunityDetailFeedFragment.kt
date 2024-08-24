package com.project.veganlife.community.ui.view

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.project.veganlife.community.data.model.Post
import com.project.veganlife.databinding.FragmentCommunityDetailFeedBinding

class CommunityDetailFeedFragment : Fragment() {
    private val binding: FragmentCommunityDetailFeedBinding by lazy {
        FragmentCommunityDetailFeedBinding.inflate(
            layoutInflater
        )
    }
    
    private var post: Post? = null
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        event()
    }

    private fun init() {
        val postId = arguments?.getLong("postId") ?: -1
        getPost(postId)

        setFeedDetailUi()
    }

    private fun getPost(postId: Long) {
        TODO("viewmodel로부터 피드 상세 조회 api 활용해서 detail데이터 받아오기")
    }

    private fun setFeedDetailUi() {
        if (post != null) {
            binding.tvCommunityDetailFeedTitle.text = post!!.title
            binding.tvCommunityDetailFeedDescription.text = post!!.content
            binding.tvCommunityDetailFeedDateTime.text = post!!.createdAt.subSequence(0, 10)
            setImageViewPager(post!!.imageUrls)
            setTags(post!!.tags)
        }

    }

    private fun setImageViewPager(imageUrls: List<String>) {
        TODO("viewpager에 imageurl로 image 달아주기")
    }

    private fun setTags(tags: List<String>) {
        binding.rvCommunityDetailFeedKeyword
        TODO("태그 adapter달아주기")
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
