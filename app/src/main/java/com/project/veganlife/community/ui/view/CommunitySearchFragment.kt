package com.project.veganlife.community.ui.view

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.project.veganlife.R
import com.project.veganlife.community.ui.view.search.CommunitySearchAfterFragment
import com.project.veganlife.community.ui.view.search.CommunitySearchBeforeFragment
import com.project.veganlife.community.ui.view.search.CommunitySearchDuringFragment
import com.project.veganlife.community.ui.viewmodel.CommunitySearchViewModel
import com.project.veganlife.community.ui.viewmodel.FeedsGetViewModel
import com.project.veganlife.community.ui.viewmodel.PageStatus
import com.project.veganlife.community.ui.viewmodel.RecentSearchesViewModel
import com.project.veganlife.databinding.FragmentCommunitySearchBinding
import dagger.hilt.android.AndroidEntryPoint

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class CommunitySearchFragment : Fragment() {
    private lateinit var binding: FragmentCommunitySearchBinding
    private val recentSearchesViewModel: RecentSearchesViewModel by activityViewModels()
    private val communitySearchViewModel: CommunitySearchViewModel by activityViewModels()
    private val feedsGetViewModel: FeedsGetViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCommunitySearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observePageStatus()

        setSearchEditText()

        setToolbarBackstack()
    }

    private fun observePageStatus() {
        communitySearchViewModel.pageStatus.observe(viewLifecycleOwner) {
            Log.i("##INFO", "observePageStatus: $it")
        }
    }

    private fun setToolbarBackstack() {
        binding.includeCommunitySearchToolbar.toolbarCommunitySearchToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }


    private fun setSearchEditText() {
        binding.includeCommunitySearchToolbar.etCommunitySearchToolbarSearchBox.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    if (communitySearchViewModel.pageStatus.value != PageStatus.DURING) {
                        childFragmentManager.commit {
                            replace<CommunitySearchDuringFragment>(R.id.fragmentContainerView)
                        }
                        return
                    }

                    if(text.isNullOrEmpty()) {
                        childFragmentManager.commit {
                            replace<CommunitySearchBeforeFragment>(R.id.fragmentContainerView)
                        }
                    }

                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })

        //todo: 검색 시
        binding.includeCommunitySearchToolbar.etCommunitySearchToolbarSearchBox.setOnEditorActionListener { textView, i, keyEvent ->
            if (keyEvent == null || keyEvent.action != KeyEvent.ACTION_DOWN) {
                false
            } else {
                val currentSearch = textView.text.toString()
                search(currentSearch)
                saveRecentSearch(currentSearch)

                childFragmentManager.commit {
                    replace<CommunitySearchAfterFragment>(R.id.fragmentContainerView)
                }
                true
            }
        }
    }

    private fun saveRecentSearch(currentSearch: String) {
        recentSearchesViewModel.saveStringList(currentSearch)
    }

    private fun search(text: String) {
        Log.i("##INFO", "search: 실행됨")
        feedsGetViewModel.getFeedsByTag(text)
    }


}
