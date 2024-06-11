package edu.put.clg

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import java.lang.IllegalArgumentException

class VPA(manager: FragmentManager, email: String) : FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragmentManager: FragmentManager = manager
    private val email: String = email

    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ScanPage()
            1 -> ContactPage(email)
            2 -> HoldingFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Check Code"
            1 -> "Contact"
            2 -> "Recent Checks"
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }

}
