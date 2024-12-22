package com.project.veganlife.community.ui.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.project.veganlife.community.ui.adapter.GalleryAdapter
import com.project.veganlife.community.ui.adapter.KeywordAutoCompleteAdapter
import com.project.veganlife.community.ui.adapter.TagListAdapter
import com.project.veganlife.community.ui.viewmodel.CommunityWriteFeedViewModel
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.databinding.FragmentCommunityWriteEditFeedBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommunityWriteFeedFragment : Fragment() {
    private val binding: FragmentCommunityWriteEditFeedBinding by lazy {
        FragmentCommunityWriteEditFeedBinding.inflate(
            layoutInflater
        )
    }
    private val viewModel: CommunityWriteFeedViewModel by viewModels()

    private lateinit var galleryAdapter: GalleryAdapter
    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) {
            Log.i("##INFO", "$it: ")
            if (it.size > 5) {
                Toast.makeText(requireContext(), "최대 5개의 사진만 등록할 수 있습니다.", Toast.LENGTH_SHORT)
                    .show()
            }
            val imageUris = it.take(5) // 최대 5개로 제한

            viewModel.setImageUris(imageUris)
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
        galleryAdapter = GalleryAdapter { position ->
            viewModel.removePartAt(position)
        }
        binding.rvCommunityWriteEditFeedPhoto.adapter = galleryAdapter
        viewModel.imageUris.observe(viewLifecycleOwner) {
            galleryAdapter.submitList(it)
        }
        //결과값 보여주기
        viewModel.response.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
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

        setPopularTag()
        setKeywordAutoComplete()
    }

    private fun setKeywordAutoComplete() {
        val adapter = KeywordAutoCompleteAdapter()
        binding.rvWriteEditFeedKeywordAutoComplete.adapter = adapter
        viewModel.keywordAutoCompleteList.observe(viewLifecycleOwner) {apiResult ->
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
                galleryLauncher.launch("image/*")
            }
            toolbarCommunityWriteEditFeed.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            btnFeedUpload.setOnClickListener {
                val keywords = viewModel.keywordList.value ?: emptyList()
                val title = etCommunityWriteEditTitle.text.toString()
                val content = etCommunityWriteEditFeedContent.text.toString()
                val images = viewModel.imageUris.value ?: emptyList()

                if (isPostValid(title, content)) {
                    viewModel.createPost(requireContext(), keywords, title, content, images)
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
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    layoutPopularKeyword.visibility = if (s.isNullOrEmpty()) View.VISIBLE else View.GONE
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
        val adapter = TagListAdapter()
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
