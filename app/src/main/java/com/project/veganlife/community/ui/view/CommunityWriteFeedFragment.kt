package com.project.veganlife.community.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.project.veganlife.community.ui.viewmodel.CommunityWriteFeedViewModel
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
    }

    private fun event() {
        binding.apply {
            //TODO(사진 리스트 추가해주기)
            //TODO(키워드 리스트 추가해주기)
            toolbarCommunityWriteEditFeed.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            btnFeedUpload.setOnClickListener {
                val keywords = viewModel.keywordList.value ?: emptyList()
                val title = etCommunityWriteEditTitle.text.toString()
                val content = etCommunityWriteEditFeedContent.text.toString()
                val images = viewModel.imagesFile.value ?: emptyList()

                if (isPostValid(title, content)) {
                    viewModel.createPost(keywords, title, content, images)
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
