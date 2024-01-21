package com.project.veganlife.lifecheck

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.project.veganlife.databinding.FragmentLifecheckHomeBinding

class LifeCheckHomeFragment : Fragment() {

    private var _binding: FragmentLifecheckHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLifecheckHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPager()
    }

    private fun setupViewPager() {

        val adapter = LifeCheckHomeViewPagerAdapter(this)
        binding.vpLifecheckHome.adapter = adapter

        TabLayoutMediator(
            binding.tablayoutLifecheckHome,
            binding.vpLifecheckHome
        ) { tab, position ->
            tab.text = when (position) {
                0 -> "일"
                1 -> "주"
                2 -> "월"
                else -> "년"
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}