package com.project.veganlife.community.ui.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.project.veganlife.community.ui.viewmodel.FeedsGetViewModel
import com.project.veganlife.community.ui.viewmodel.RecentSearchesViewModel
import com.project.veganlife.databinding.FragmentCommunitySearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CommunitySearchFragment : Fragment() {
    private lateinit var binding: FragmentCommunitySearchBinding
    private val recentSearchesViewModel: RecentSearchesViewModel by viewModels()
    private val feedsGetViewModel: FeedsGetViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCommunitySearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecentSearchRV()

        setSearchEditText()

//        setSearchResultRV()
    }

    private fun setSearchResultRV() {
        viewLifecycleOwner.lifecycleScope.launch {
            feedsGetViewModel.feedList.collectLatest { pagingData ->
                pagingData?.let { feedPagingData ->
                    Log.i("##INFO", "observeFeeds: $pagingData")

//                    setRecyclerViewAdapter()
//
//                    communityFeedAdapter.submitData(feedPagingData)
                }
            }
        }
    }

    private fun setSearchEditText() {
        binding.includeCommunitySearchToolbar.etCommunitySearchToolbarSearchBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                setRelatedSearchRV(text.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        binding.includeCommunitySearchToolbar.etCommunitySearchToolbarSearchBox.setOnEditorActionListener { textView, i, keyEvent ->
            val currentSearch = textView.text.toString()
            search(currentSearch)
            recentSearchesViewModel.saveStringList(currentSearch)
            true
        }
    }

    private fun search(text: String) {
        feedsGetViewModel.getFeedsByTag(text)
    }

    /**
     * 입력한 검색어와 연관 검색어
     */
    private fun setRelatedSearchRV(keyword: String) {
        TODO("Not yet implemented")
    }

    /**
     * 최근 검색어
     */
    private fun setRecentSearchRV() {
        recentSearchesViewModel.recentSearchList.observe(viewLifecycleOwner) {
            val adapter = RecentSearchAdapter()
            binding.rvCommunitySearchRecent.adapter = adapter

            adapter.submitList(it)
        }
    }

}
