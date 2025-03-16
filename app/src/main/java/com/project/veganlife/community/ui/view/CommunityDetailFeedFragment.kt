package com.project.veganlife.community.ui.view

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.TextAppearanceSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.project.veganlife.R
import com.project.veganlife.community.data.model.Comment
import com.project.veganlife.community.data.model.CreateResponse
import com.project.veganlife.community.data.model.Post
import com.project.veganlife.community.ui.adapter.CommentsAdapter
import com.project.veganlife.community.ui.adapter.OnReplyCommentClickListener
import com.project.veganlife.community.ui.adapter.PostImagesViewPagerAdapter
import com.project.veganlife.community.ui.adapter.TagListAdapter
import com.project.veganlife.community.ui.viewmodel.PostViewModel
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.databinding.FragmentCommunityDetailFeedBinding
import com.project.veganlife.utils.formatDateTime
import com.project.veganlife.utils.ui.VeganTypeChange.Companion.changeBackground
import com.project.veganlife.utils.ui.VeganTypeChange.Companion.changeVeganType
import com.project.veganlife.utils.ui.VeganTypeChange.Companion.changeVeganTypeTextColor
import dagger.hilt.android.AndroidEntryPoint

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class CommunityDetailFeedFragment : Fragment(), OnReplyCommentClickListener {

    private val binding: FragmentCommunityDetailFeedBinding by lazy {
        FragmentCommunityDetailFeedBinding.inflate(layoutInflater)
    }

    private var post: Post? = null
    private val postViewModel: PostViewModel by activityViewModels()
    private lateinit var tagListAdapter: TagListAdapter
    private lateinit var viewPagerAdapter: PostImagesViewPagerAdapter
    private lateinit var commentListAdapter: CommentsAdapter

    private var commentId: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // postId를 arguments에서 가져와 데이터 로드
        val postId = arguments?.getInt("postId") ?: -1
        postViewModel.getPost(postId)
        postViewModel.getMyProfile()


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 초기화 작업
        init()
        event()

        // ViewModel의 데이터 관찰
        observePostData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun init() {
        // 로딩 표시
        binding.layoutContentLoading.visibility = View.VISIBLE
        binding.contentLoading.show()
        binding.contentScrollView.visibility = View.GONE

        // 태그 리스트 어댑터 설정
        tagListAdapter = TagListAdapter { popularTag: String ->

        }
        binding.rvCommunityDetailFeedKeyword.adapter = tagListAdapter
        binding.rvCommunityDetailFeedKeyword.layoutManager =
            FlexboxLayoutManager(requireContext()).apply {
                flexDirection = FlexDirection.ROW
            }

        // ViewPager 어댑터 및 콜백 등록
        viewPagerAdapter = PostImagesViewPagerAdapter()
        binding.vpCommunityDetailFeedImage.adapter = viewPagerAdapter
        binding.diCommunityDetailFeed.attachTo(binding.vpCommunityDetailFeedImage)

        //댓글 어댑터 설정
        commentListAdapter = CommentsAdapter(this)
        binding.rvCommunityDetailFeedComments.adapter = commentListAdapter

        //내 게시글인 경우 나오는 메뉴 클릭 리스너 등록
        binding.toolbarCommunityDetailFeed.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.item_edit -> {
                    Log.i("##INFO", "수정, $post")
                    findNavController().navigate(
                        R.id.action_communityDetailFeedFragment_to_communityWriteFeedFragment,
                        bundleOf("post" to post!!)
                    )
                }

                R.id.item_delete -> {
                    Log.i("##INFO", "삭제")
                    AlertDialog.Builder(requireContext()).apply {
                        setTitle("삭제")
                        setMessage("게시글을 삭제하시겠습니까?")
                        setPositiveButton("확인") { _, i ->
                            findNavController().navigateUp()
                            if (post != null) {
                                postViewModel.deletePost(post!!.id.toInt())
                            }
                        }
                        setNegativeButton("취소") { _, _ -> }

                        show()
                    }
                }
            }
            true
        }
    }


    private fun createCommentLocally(commentText: String, createResponse: CreateResponse) {
        val newComment = Comment(
            id = createResponse.commentId.toLong(),
            author = getMyNickname(), // 현재 로그인된 사용자 이름
            content = commentText,
            createdAt = createResponse.createdAt, // 현재 시간 문자열
            subComments = null
        )

        val updatedComments = commentListAdapter.currentList.toMutableList()

        if (commentId != null) {
            // 대댓글 처리
            val targetCommentIndex = updatedComments.indexOfFirst { it.id == commentId }
            if (targetCommentIndex != -1) {
                val targetComment = updatedComments[targetCommentIndex]
                val updatedSubComments = (targetComment.subComments ?: emptyList()).toMutableList()
                updatedSubComments.add(newComment)

                updatedComments[targetCommentIndex] = targetComment.copy(
                    subComments = updatedSubComments
                )
            }
        } else {
            // 최상위 댓글로 추가
            updatedComments.add(newComment)
        }

        // Adapter에 새로운 리스트 반영
        commentListAdapter.submitList(updatedComments)
    }


    private fun createComment(postId: Long?, commentId: Long?, comment: String) {
        if (postId == null) {
            Log.e("##ERROR", "createComment: post id가 null입니다.")
        } else {
            postViewModel.createComment(postId, commentId, comment) {
                if (it is ApiResult.Success) {
                    //얘는 subcomment가 아닌 일반 comment만 해당하는 것 같은디
                    createCommentLocally(comment, it.data)
                } else {
                    Log.e("##ERROR", "createComment: 댓글 작성 실패")
                }
            }
        }

    }

    private fun getMyNickname(): String {
        postViewModel.myProfile.value?.let {
            if (it is ApiResult.Success) {
                return it.data.nickname
            } else {
                return "nickname"
            }
        }
        return "nickname"
    }

    private fun event() {
        binding.toolbarCommunityDetailFeed.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
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
            showSoftInput()
        }

        //댓글 editText 엔터 입력 설정
        val editText =
            binding.includeCommunityDetailFeedCommentInputBox.etCommunityDetailFeedCommentInputBox
        editText.setOnEditorActionListener { _, actionId, _ ->
            // 엔터키(IME_ACTION_DONE)인지 확인
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val comment = editText.text.toString()
                if (comment.isNotBlank()) {
                    Log.i("##INFO", "댓글 id: $commentId")
                    createComment(post?.id, commentId, comment)

                    binding.tvCommunityDetailFeedComments
                    editText.setText("")
                    hideSoftInput()
                    binding.layoutReplayToWho.visibility = View.GONE
                }

                true
            } else {
                false
            }
        }

        //~님에게 댓글 다는 중 라벨 x 클릭시
        binding.btnCancleReplay.setOnClickListener {
            binding.layoutReplayToWho.visibility = View.GONE
            //todo: 마지막 댓글 가려짐
            val params = binding.rvCommunityDetailFeedComments.layoutParams as MarginLayoutParams
            params.setMargins(0, 0, 0, 0)
            binding.rvCommunityDetailFeedComments.layoutParams = params
            //todo: 대댓글 대상 해제
            commentId = null
        }
    }

    private fun observePostData() {

        postViewModel.combinedLiveData.observe(viewLifecycleOwner) { (profile, post) ->
            if (profile != null && post != null) {

                binding.layoutContentLoading.visibility = View.GONE
                binding.contentLoading.hide()
                binding.contentScrollView.visibility = View.VISIBLE

                this.post = post
                Log.i("##INFO", "observePostData: $post")

                updateUIWithPostData(post, profile.nickname)
            }
        }

    }

    private fun updateUIWithPostData(post: Post, nickname: String) {
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

        //내 게시물일 경우 수정 및 삭제 메뉴 보여주기
        if (nickname == post.author) {
            Log.i("##INFO", "내 게시물 -> 메뉴 보여주기")
            binding.toolbarCommunityDetailFeed.menu.clear()
            binding.toolbarCommunityDetailFeed.inflateMenu(R.menu.menu_community_detail)
        } else {
            binding.toolbarCommunityDetailFeed.menu.clear()
        }
    }

    private fun setComments(comments: List<Comment>) {
        commentListAdapter.submitList(comments)
    }

    private fun setImageViewPager(imageUrls: List<String>) {
        if (imageUrls.isEmpty()) {
            binding.vpCommunityDetailFeedImage.visibility = View.GONE
            binding.diCommunityDetailFeed.visibility = View.GONE
        } else {
            binding.vpCommunityDetailFeedImage.visibility = View.VISIBLE
            viewPagerAdapter.submitList(imageUrls)
//            //todo????
//            adjustViewPagerHeight(0) // 첫 번째 페이지의 높이 조정
            binding.diCommunityDetailFeed.visibility = View.VISIBLE
        }

    }

    private fun setTags(tags: List<String>) {
        tagListAdapter.submitList(tags)
    }

    private fun setAuthorDetail(post: Post) {
        binding.tvCommunityDetailFeedNickname.text = post.author
        //[2025-03-16] 가연 : 프사 이미지 null일시 기본 이미지로 표시하기
        if (!post.profileImageUrl.isNullOrEmpty()) {
            Glide.with(requireContext())
                .load(post.profileImageUrl)
                .apply(
                    RequestOptions
                        .diskCacheStrategyOf(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .signature(ObjectKey(System.currentTimeMillis().toString()))
                )
                .into(binding.civCommunityDetailFeedProfile)
        } else {
            binding.civCommunityDetailFeedProfile.setImageResource(R.drawable.all_profile_basic)
        }


        binding.tvCommunityDetailFeedVeganType.apply {
            text = changeVeganType(post.vegetarianType)
            setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    changeVeganTypeTextColor(post.vegetarianType)
                )
            )
            setBackgroundResource(changeBackground(post.vegetarianType))
        }
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

    override fun onButtonClick(view: View, item: Comment) {
        showSoftInput()
        //대댓글 다는 중임을 알리는 텍스트뷰
        binding.tvReplayToWho.text = "${item.author}님에게 답글을 남기는 중..."
        binding.layoutReplayToWho.visibility = View.VISIBLE
        //todo: 마지막 댓글 가려짐
        val params = binding.rvCommunityDetailFeedComments.layoutParams as MarginLayoutParams
        params.setMargins(0, 0, 0, binding.layoutReplayToWho.height)
        binding.rvCommunityDetailFeedComments.layoutParams = params

        //edittext에 아이디 태그해주기
        tagAuthor(item.author)
        commentId = item.id
    }

    private fun tagAuthor(author: String) {
        val string = "@${author} "
        val spannableString = SpannableString(string)

        val textAppearanceSpan = TextAppearanceSpan(context, R.style.CommentTag)
        spannableString.setSpan(
            textAppearanceSpan,
            0, string.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.includeCommunityDetailFeedCommentInputBox.etCommunityDetailFeedCommentInputBox.apply {
            setText(spannableString)
            setSelection(spannableString.length)
        }
    }

    private fun showSoftInput() {
        val editText =
            binding.includeCommunityDetailFeedCommentInputBox.etCommunityDetailFeedCommentInputBox
        editText.requestFocus()
        val imm = getSystemService(requireContext(), InputMethodManager::class.java)
        imm?.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun hideSoftInput() {
        val imm = getSystemService(requireContext(), InputMethodManager::class.java)
        imm?.hideSoftInputFromWindow(requireView().windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}
