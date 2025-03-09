package com.project.veganlife.community.ui.view.search

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
import com.project.veganlife.community.ui.adapter.OnItemClickedListener
import com.project.veganlife.community.ui.adapter.RecentSearchAdapter
import com.project.veganlife.community.ui.view.CommunitySearchFragment
import com.project.veganlife.community.ui.viewmodel.CommunitySearchViewModel
import com.project.veganlife.community.ui.viewmodel.PageStatus
import com.project.veganlife.community.ui.viewmodel.RecentSearchesViewModel
import com.project.veganlife.databinding.FragmentCommunitySearchBeforeBinding


@RequiresApi(Build.VERSION_CODES.O)
class CommunitySearchBeforeFragment : Fragment() {
    private lateinit var binding: FragmentCommunitySearchBeforeBinding
    private val recentSearchesViewModel: RecentSearchesViewModel by activityViewModels()
    private val communitySearchViewModel: CommunitySearchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCommunitySearchBeforeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        communitySearchViewModel.pageStatus.value = PageStatus.BEFORE
        setRecentSearchRV()
    }

    /**
     * 최근 검색어
     */
    private fun setRecentSearchRV() {
        val onDeleteClicked = object: OnItemClickedListener {
            override fun onItemClicked(position: Int, item: Any) {
                recentSearchesViewModel.delete(position)
            }

        }

        val onItemClicked = object: OnItemClickedListener{
            override fun onItemClicked(position: Int, item: Any) {
                val editText =
                    (parentFragment as CommunitySearchFragment).searchBinding.includeCommunitySearchToolbar.etCommunitySearchToolbarSearchBox
                if (item is String) {
                    editText.setText(item)
                    editText.requestFocus()
                    editText.setSelection(item.length)
                    val imm = getSystemService(requireContext(), InputMethodManager::class.java)
                    imm?.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
                }

            }
        }
        val adapter = RecentSearchAdapter(onDeleteClicked, onItemClicked)
        binding.rvCommunitySearchRecent.adapter = adapter


        recentSearchesViewModel.recentSearchList.observe(viewLifecycleOwner) {
            Log.i("##INFO", "setRecentSearchRV: 왜왜")
            //최근 검색어가 없다면
            if (it.isEmpty()) {
                binding.layoutCommunityEmpty.visibility = View.VISIBLE
                binding.rvCommunitySearchRecent.visibility = View.GONE
            }
            //최근 검색어가 있다면
            else {
                binding.layoutCommunityEmpty.visibility = View.GONE
                binding.rvCommunitySearchRecent.visibility = View.VISIBLE

                adapter.submitList(it.reversed())
            }

        }

    }

}