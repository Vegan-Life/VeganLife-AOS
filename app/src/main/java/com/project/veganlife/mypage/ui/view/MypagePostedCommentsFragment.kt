package com.project.veganlife.mypage.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.veganlife.databinding.FragmentMypagePostedCommentsBinding
import com.project.veganlife.mypage.ui.adapter.MypagePostedCommentAdapter
import com.project.veganlife.mypage.ui.viewmodel.MypagePostedPagingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MypagePostedCommentsFragment : Fragment() {
    private var _binding: FragmentMypagePostedCommentsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MypagePostedCommentAdapter

    private val viewModel: MypagePostedPagingViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMypagePostedCommentsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbarListener()
        setPostedFeed()

        // ui
        setPostedFeedList()
    }

    private fun setToolbarListener() {
        binding.toolbarMypageToolbar.run {
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun setPostedFeed() {
        viewModel.getPostedFeed("comment")
    }

    private fun setPostedFeedList() {
        lifecycleScope.launch {

            // Adapter에 Context 전달
            adapter = MypagePostedCommentAdapter()
            binding.rvMypageComment.layoutManager = LinearLayoutManager(requireContext())
            binding.rvMypageComment.adapter = adapter

            viewModel.posted.collectLatest { pagingData ->
                pagingData?.let {
                    adapter.submitData(it)

                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}