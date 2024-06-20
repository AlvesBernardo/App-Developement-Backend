package com.appdevelopement.passinggrade.pages

// this is hakan code i was just not able to pull it

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.appdevelopement.passinggrade.R
import com.google.android.material.tabs.TabLayout

class SheetManagementFragment : androidx.fragment.app.Fragment() {

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.fragment_sheet_managment, container, false)
    val viewPager: ViewPager = view.findViewById(R.id.viewPager)
    val tabLayout: TabLayout = view.findViewById(R.id.tabLayout)

    val createSheetButton: Button = view.findViewById(R.id.createEditSheetButton)

    createSheetButton.setOnClickListener {
      val fragment = GradingSheetFragment()
      requireActivity().supportFragmentManager.beginTransaction().apply {
        replace(R.id.fragment_container, fragment)
        addToBackStack(null)
        commit()
      }
    }
    viewPager.adapter = SheetsPagerAdapter(childFragmentManager)
    tabLayout.setupWithViewPager(viewPager)

    return view
  }

  class SheetsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private val tabTitles =
        arrayOf("Create/Edit sheet", "Read Gradesheet", "Import file", "Assign course")

    override fun getCount(): Int {
      return tabTitles.size
    }

    override fun getItem(position: Int): androidx.fragment.app.Fragment {
      return ContentFragment.newInstance(tabTitles[position])
    }

    override fun getPageTitle(position: Int): CharSequence? {
      return tabTitles[position]
    }
  }

  class ContentFragment : androidx.fragment.app.Fragment() {
    private var tabTitle: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      arguments?.let { tabTitle = it.getString(ARG_TAB_TITLE) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      val view = inflater.inflate(R.layout.fragment_content, container, false)
      val textView: TextView = view.findViewById(R.id.textView)
      textView.text =
          when (tabTitle) {
            "Create/Edit sheet" -> "Create/Edit sheet functionality"
            "Read Gradesheet" -> "Read Gradesheet functionality"
            "Import file" -> "Import file functionality"
            "Assign course" -> "Assign course functionality"
            else -> ""
          }
      return view
    }

    companion object {
      private const val ARG_TAB_TITLE = "tab_title"

      @JvmStatic
      fun newInstance(tabTitle: String) =
          ContentFragment().apply {
            arguments = Bundle().apply { putString(ARG_TAB_TITLE, tabTitle) }
          }
    }
  }
}
