package jp.techacademy.tomotaka.aruga.apiapp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

//このクラスはFragmentStateAdapterを継承しており、フラグメントのリストを管理し、ViewPager2にフラグメントを提供します。

//FragmentActivityは、Androidのクラスで、フラグメントを含むアクティビティを扱うためのものです。
// フラグメントは、アクティビティ内で再利用可能な部分（UI部品とそれに関連するロジック）を表します。
class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    val titleIds = listOf(R.string.tab_title_api, R.string.tab_title_favorite)

    public val fragments = listOf(ApiFragment(), FavoriteFragment())

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}