package com.project.veganlife.lifecheck

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class LifeCheckHomeViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val fragments = listOf(
        LifeCheckDailyFragment(),
        LifeCheckWeeklyFragment(),
        LifeCheckMonthlyFragment(),
        LifeCheckYearlyFragment()
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}