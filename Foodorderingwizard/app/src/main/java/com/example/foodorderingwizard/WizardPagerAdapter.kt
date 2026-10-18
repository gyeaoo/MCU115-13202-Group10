package com.example.foodorderingwizard

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class WizardPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount() = 4

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> MainMealFragment()
        1 -> SideDishesFragment()
        2 -> DrinkFragment()
        else -> ConfirmFragment()
    }
}