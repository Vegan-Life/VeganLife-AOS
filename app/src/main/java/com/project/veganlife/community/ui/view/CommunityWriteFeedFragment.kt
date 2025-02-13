package com.project.veganlife.community.ui.view

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.project.veganlife.community.data.model.Post
import com.project.veganlife.community.ui.adapter.GalleryAdapter
import com.project.veganlife.community.ui.adapter.KeywordAutoCompleteAdapter
import com.project.veganlife.community.ui.adapter.TagListAdapter
import com.project.veganlife.community.ui.viewmodel.CommunityWriteFeedViewModel
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.databinding.FragmentCommunityWriteEditFeedBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommunityWriteFeedFragment : Fragment() {

    private var isEditMode: Boolean = false

    private val binding: FragmentCommunityWriteEditFeedBinding by lazy {
        FragmentCommunityWriteEditFeedBinding.inflate(
            layoutInflater
        )
    }
    private val viewModel: CommunityWriteFeedViewModel by viewModels()

    private lateinit var galleryAdapter: GalleryAdapter
    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(5)) { uriList ->
            viewModel.images.value?.let {
                if (it.size + uriList.size <= 5) {
                    for (uri in uriList) {
                        viewModel.addImage(uri)
                    }

                } else {
                    Toast.makeText(requireContext(), "최대 5개의 사진을 등록할 수 있습니다.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("post", Post::class.java)?.let { post ->
                isEditMode = true
                viewModel.initOldPostData(post) // 기존 데이터 뷰모델에 전달
            }
        } else {
            arguments?.getParcelable<Post>("post")?.let { post ->
                isEditMode = true
                viewModel.initOldPostData(post) // 기존 데이터 뷰모델에 전달
            }
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

        init()
        event()
    }

    private fun init() {
        // 버튼 텍스트 다르게
        binding.btnFeedUpload.text = if (isEditMode) "수정하기" else "업로드"
        binding.toolbarCommunityWriteEditFeed.title = if (isEditMode) "피드 작성" else "피드 수정"

        // 수정으로 넘어온 post데이터 넣어주기
        viewModel.oldPost.observe(viewLifecycleOwner) {
            binding.apply {
                etCommunityWriteEditTitle.setText(it.title)
                etCommunityWriteEditFeedContent.setText(it.content)
            }

        }
        // 사진 리스트 어댑터
        galleryAdapter = GalleryAdapter { position ->
            viewModel.removeImage(position)
        }
        binding.rvCommunityWriteEditFeedPhoto.adapter = galleryAdapter
        viewModel.images.observe(viewLifecycleOwner) {
            galleryAdapter.submitList(it)
            galleryAdapter.notifyDataSetChanged()
        }
        //결과값 보여주기
        viewModel.response.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResult.Success -> {
                    Toast.makeText(requireContext(), "게시물이 등록됐습니다.", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }

                is ApiResult.Error -> {
                    Log.e(
                        "##ERROR",
                        "createPost ERROR: ${it.errorCode}, ${it.description}",
                    )

                    Toast.makeText(
                        requireContext(),
                        "게시물 등록에 실패했습니다. 다시 시도해주세요.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is ApiResult.Exception -> {
                    Log.e("##ERROR", "createPost EXCEPTION: ${it.e.stackTraceToString()}")
                    Toast.makeText(
                        requireContext(),
                        "게시물 등록에 실패했습니다. 다시 시도해주세요.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }

        //결과값 보여주기
        viewModel.updateResponse.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResult.Success -> {
                    Toast.makeText(requireContext(), "게시물이 수정됐습니다.", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }

                is ApiResult.Error -> {
                    Log.e(
                        "##ERROR",
                        "createPost ERROR: ${it.errorCode}, ${it.description}",
                    )

                    Toast.makeText(
                        requireContext(),
                        "게시물 수정에 실패했습니다. 다시 시도해주세요.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is ApiResult.Exception -> {
                    Log.e("##ERROR", "updatePost EXCEPTION: ${it.e.stackTraceToString()}")
                    Toast.makeText(
                        requireContext(),
                        "게시물 수정에 실패했습니다. 다시 시도해주세요.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }

        //입력 중일 경우 에러 없애주기
        binding.apply {
            etCommunityWriteEditTitle.addTextChangedListener {
                etCommunityWriteEditTitle.error = null
            }

            etCommunityWriteEditFeedContent.addTextChangedListener {
                etCommunityWriteEditFeedContent.error = null
            }
        }

        //키워드 입력창에 입력 후 엔터 누르면 등록
        binding.etCommunityWriteEditKeywordSearchBox.setOnEditorActionListener { textView, i, keyEvent ->
            val keyword = textView.text.toString()

            // 텍스트 내용이 비어있다면...
            if (keyword.isEmpty()) {

                // 토스트 메세지를 띄우고, 창 내용을 비운다
                Toast.makeText(requireContext(), "정보를 입력해주세요", Toast.LENGTH_SHORT).show()
                textView.clearFocus()
                textView.isFocusable = false
                textView.isFocusableInTouchMode = true
                textView.isFocusable = true
            } else {
                viewModel.addKeyword(keyword)

                textView.clearFocus()
                textView.isFocusable = false
                textView.isFocusableInTouchMode = true
                textView.isFocusable = true
                textView.text = ""
            }


            true
        }

        setKeywordList()
        setPopularTag()
        setKeywordAutoComplete()
    }

    private fun setKeywordList() {
        val adapter = TagListAdapter()
        binding.rvCommunityWriteEditKeywordFeed.adapter = adapter
        viewModel.keywordList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun setKeywordAutoComplete() {
        val adapter = KeywordAutoCompleteAdapter() { popularTag: String ->
            //클릭할 경우 viewmodel의 keywordlist에 추가
            viewModel.addKeyword(popularTag)
        }
        binding.rvWriteEditFeedKeywordAutoComplete.adapter = adapter
        viewModel.keywordAutoCompleteList.observe(viewLifecycleOwner) { apiResult ->
            when (apiResult) {
                is ApiResult.Error -> {
                    val popularTagsResponse = apiResult.description
                    Log.d("daily Error", popularTagsResponse)
                }

                is ApiResult.Exception -> {
                    Log.d("daily Exception", apiResult.e.message ?: "No message available")
                }

                is ApiResult.Success -> {
                    Log.i("##INFO", "setKeywordAutoComplete: ${apiResult.data}")
                    adapter.submitList(apiResult.data)
                }
            }
        }
    }

    private fun event() {
        binding.apply {
            ibCommunityWriteEditFeedUploadPhoto.setOnClickListener {
                galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
            toolbarCommunityWriteEditFeed.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            btnFeedUpload.setOnClickListener {
                val keywords = viewModel.keywordList.value ?: emptyList()
                val title = etCommunityWriteEditTitle.text.toString()
                val content = etCommunityWriteEditFeedContent.text.toString()

                if (isPostValid(title, content)) {
                    if (isEditMode) {
                        viewModel.updatePost(requireContext(), viewModel.oldPost.value?.id!!.toInt(), keywords, title, content, viewModel.getExistingImageUrls(), viewModel.getNewImages())
                    } else {
                        viewModel.createPost(requireContext(), keywords, title, content, viewModel.getNewImages())
                    }
                }
            }


            // 포커스 변경 리스너
            etCommunityWriteEditKeywordSearchBox.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus && etCommunityWriteEditKeywordSearchBox.text.isEmpty()) {
                    layoutPopularKeyword.visibility = View.VISIBLE
                } else {
                    layoutPopularKeyword.visibility = View.GONE
                    rvWriteEditFeedKeywordAutoComplete.visibility = View.GONE
                }
            }

            // 텍스트 변경 리스너
            etCommunityWriteEditKeywordSearchBox.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    layoutPopularKeyword.visibility =
                        if (s.isNullOrEmpty()) View.VISIBLE else View.GONE
                    //텍스트 있으면 자동완성 보이기
                    rvWriteEditFeedKeywordAutoComplete.visibility = if (s.isNullOrEmpty()) {
                        View.GONE
                    } else {
                        viewModel.getKeywordAutoComplete(s.toString())
                        View.VISIBLE
                    }

                }

                override fun afterTextChanged(s: Editable?) {}
            })

        }
    }

    private fun setPopularTag() {
        val adapter = TagListAdapter { popularTag: String ->
            //클릭할 경우 viewmodel의 keywordlist에 추가
            viewModel.addKeyword(popularTag)
        }
        binding.rvWriteEditFeedPopularKeyword.adapter = adapter

        viewModel.popularTagList.observe(viewLifecycleOwner) { apiResult ->
            when (apiResult) {
                is ApiResult.Error -> {
                    val popularTagsResponse = apiResult.description
                    Log.d("daily Error", popularTagsResponse)
                }

                is ApiResult.Exception -> {
                    Log.d("daily Exception", apiResult.e.message ?: "No message available")
                }

                is ApiResult.Success -> {
                    val popularTagsResponse = apiResult.data
                    //인기 태그 중 5개만 보여주기
                    adapter.submitList(popularTagsResponse.topTags.take(5))
                }
            }

        }
    }

    private fun isPostValid(title: String, content: String): Boolean {
        return if (title.isEmpty() || title.isBlank()) {
            binding.etCommunityWriteEditTitle.error = "제목을 입력해주세요."
            false
        } else if (content.isEmpty() || content.isBlank()) {
            binding.etCommunityWriteEditTitle.error = "내용을 입력해주세요."
            false
        } else {
            true
        }
    }
}
