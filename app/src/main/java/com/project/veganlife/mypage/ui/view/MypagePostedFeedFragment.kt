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
import com.project.veganlife.databinding.FragmentMypagePostedFeedBinding
import com.project.veganlife.mypage.ui.adapter.MypagePostedFeedAdapter
import com.project.veganlife.mypage.ui.viewmodel.MypagePostedPagingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MypagePostedFeedFragment : Fragment() {
    private var _binding: FragmentMypagePostedFeedBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MypagePostedFeedAdapter
    private val viewModel: MypagePostedPagingViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMypagePostedFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbarListener()
        getPostedFeed()

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

    private fun getPostedFeed() {
        viewModel.getPostedFeed("feed")
    }

    private fun setPostedFeedList() {
        lifecycleScope.launch {

            // Adapter에 Context 전달
            adapter = MypagePostedFeedAdapter()
            binding.rvMypageFeed.layoutManager = LinearLayoutManager(requireContext())
            binding.rvMypageFeed.adapter = adapter

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