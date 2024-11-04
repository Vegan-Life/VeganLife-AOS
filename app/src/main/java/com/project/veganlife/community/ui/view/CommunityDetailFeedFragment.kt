package com.project.veganlife.community.ui.view

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.project.veganlife.community.data.model.Comment
import com.project.veganlife.community.data.model.Post
import com.project.veganlife.community.ui.adapter.ButtonType
import com.project.veganlife.community.ui.adapter.CommentsAdapter
import com.project.veganlife.community.ui.adapter.OnItemButtonClickListener
import com.project.veganlife.community.ui.adapter.PostImagesViewPagerAdapter
import com.project.veganlife.community.ui.adapter.TagListAdapter
import com.project.veganlife.community.ui.viewmodel.PostViewModel
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.databinding.FragmentCommunityDetailFeedBinding
import com.project.veganlife.utils.formatDateTime
import dagger.hilt.android.AndroidEntryPoint

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class CommunityDetailFeedFragment : Fragment(), OnItemButtonClickListener {

    private val binding: FragmentCommunityDetailFeedBinding by lazy {
        FragmentCommunityDetailFeedBinding.inflate(layoutInflater)
    }

    private var post: Post? = null
    private val postViewModel: PostViewModel by activityViewModels()
    private lateinit var tagListAdapter: TagListAdapter
    private lateinit var viewPagerAdapter: PostImagesViewPagerAdapter
    private lateinit var commentListAdapter: CommentsAdapter

    // ViewPager2 콜백 변수
    private val viewPagerCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            adjustViewPagerHeight(position)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 초기화 작업
        init()
        event()

        // postId를 arguments에서 가져와 데이터 로드
        val postId = arguments?.getInt("postId") ?: -1
        getPost(postId)

        // ViewModel의 데이터 관찰
        observePostData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // ViewPager2 콜백 해제
        binding.vpCommunityDetailFeedImage.unregisterOnPageChangeCallback(viewPagerCallback)
    }

    private fun init() {
        // 로딩 표시
        binding.layoutContentLoading.visibility = View.VISIBLE
        binding.contentLoading.show()
        binding.contentScrollView.visibility = View.GONE

        // 태그 리스트 어댑터 설정
        tagListAdapter = TagListAdapter()
        binding.rvCommunityDetailFeedKeyword.adapter = tagListAdapter
        binding.rvCommunityDetailFeedKeyword.layoutManager =
            FlexboxLayoutManager(requireContext()).apply {
                flexDirection = FlexDirection.ROW
            }

        // ViewPager 어댑터 및 콜백 등록
        viewPagerAdapter = PostImagesViewPagerAdapter()
        binding.vpCommunityDetailFeedImage.adapter = viewPagerAdapter
        binding.vpCommunityDetailFeedImage.registerOnPageChangeCallback(viewPagerCallback)

        //댓글 어댑터 설정
        commentListAdapter = CommentsAdapter(this)
        binding.rvCommunityDetailFeedComments.adapter = commentListAdapter
    }

    private fun event() {
        // 좋아요 버튼 클릭 시
        binding.ivCommunityDetailFeedLikes.setOnClickListener { view ->
            post?.let { post ->
                // isSelected 상태를 먼저 토글
                view.isSelected = !view.isSelected

                // 현재 isSelected 상태에 따라 좋아요 또는 좋아요 취소 요청
                if (view.isSelected) {
                    postViewModel.likePost(post.id.toInt())
                } else {
                    postViewModel.unlikePost(post.id.toInt())
                }

                // 현재 좋아요 수를 가져와서 증가/감소 처리
                val likeCount = binding.tvCommunityDetailFeedLikes.text.toString().toInt()
                binding.tvCommunityDetailFeedLikes.text =
                    (likeCount + if (view.isSelected) 1 else -1).toString()
            }
        }


        //댓글 버튼 클릭 시
        binding.ivCommunityDetailFeedComments.setOnClickListener {
            val editText =
                binding.includeCommunityDetailFeedCommentInputBox.etCommunityDetailFeedCommentInputBox
            editText.requestFocus()
            val imm = getSystemService(requireContext(), InputMethodManager::class.java)
            imm?.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun getPost(postId: Int) {
        postViewModel.getPost(postId)
    }

    private fun observePostData() {
        // ViewModel의 LiveData를 관찰하여 데이터 업데이트
        postViewModel.post.observe(viewLifecycleOwner) { postApiResult ->
            when (postApiResult) {
                is ApiResult.Error -> {
                    Log.d("daily Error", postApiResult.description)
                }

                is ApiResult.Exception -> {
                    Log.d("daily Exception", postApiResult.e.message ?: "No message available")
                }

                is ApiResult.Success -> {
                    binding.layoutContentLoading.visibility = View.GONE
                    binding.contentLoading.hide()
                    binding.contentScrollView.visibility = View.VISIBLE

                    post = postApiResult.data
                    updateUIWithPostData()
                }
            }
        }
    }

    private fun updateUIWithPostData() {
        post?.let { post ->
            binding.tvCommunityDetailFeedTitle.text = post.title
            binding.tvCommunityDetailFeedDescription.text = post.content
            binding.tvCommunityDetailFeedDateTime.text = formatDateTime(post.createdAt)
            binding.tvCommunityDetailFeedLikes.text = post.likeCount.toString()
            binding.tvCommunityDetailFeedComments.text = post.commentCount.toString()
            binding.ivCommunityDetailFeedLikes.isSelected = post.isLike

            setImageViewPager(post.imageUrls)
            setTags(post.tags)
            setAuthorDetail(post)
            setComments(post.comments)
        }
    }

    private fun setComments(comments: List<Comment>) {
        commentListAdapter.submitList(comments)
    }

    private fun setImageViewPager(imageUrls: List<String>) {
        viewPagerAdapter.submitList(imageUrls)
        //todo????
        adjustViewPagerHeight(0) // 첫 번째 페이지의 높이 조정
    }

    private fun setTags(tags: List<String>) {
        tagListAdapter.submitList(tags)
    }

    private fun setAuthorDetail(post: Post) {
        binding.tvCommunityDetailFeedNickname.text = post.author
        Glide.with(requireContext())
            .load(post.profileImageUrl)
            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
            .into(binding.civCommunityDetailFeedProfile)

        binding.tvCommunityDetailFeedVeganType.text = post.vegetarianType
    }

    private fun adjustViewPagerHeight(position: Int) {
        val recyclerView = binding.vpCommunityDetailFeedImage.getChildAt(0) as RecyclerView
        val view = recyclerView.layoutManager?.findViewByPosition(position)
        view?.post {
            val wMeasureSpec =
                View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY)
            val hMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            view.measure(wMeasureSpec, hMeasureSpec)

            if (recyclerView.layoutParams.height != view.measuredHeight) {
                recyclerView.layoutParams.height = view.measuredHeight
            }
        }
    }

    override fun onButtonClick(view: View, item: Comment, buttonType: ButtonType) {
        when (buttonType) {
            ButtonType.LIKE -> {
                post?.let { post ->
                    // isSelected 상태를 먼저 토글
                    view.isSelected = !view.isSelected

                    // 현재 isSelected 상태에 따라 좋아요 또는 좋아요 취소 요청
                    if (view.isSelected) {
                        postViewModel.likeComment(post.id, item.id)
                    } else {
                        postViewModel.unlikeComment(post.id, item.id)
                    }
                }

            }

            ButtonType.REPLY -> {
                //todo: 대댓글 창 열기
            }
        }
    }
}
