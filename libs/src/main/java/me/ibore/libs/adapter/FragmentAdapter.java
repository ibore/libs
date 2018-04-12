package me.ibore.libs.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * description:
 * author: Ibore Xie
 * date: 2018-01-21 13:12
 * website: ibore.me
 */
@Deprecated
public class FragmentAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> mFragments;
    private List<String> mPageTitles = new ArrayList<>();

    public FragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.mFragments = fragments;
    }

    public FragmentAdapter(FragmentManager fm, List<Fragment> fragments, List<String> pageTitles) {
        super(fm);
        this.mFragments = fragments;
        this.mPageTitles = pageTitles;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (null != mPageTitles) return mPageTitles.get(position);
        return super.getPageTitle(position);
    }

    public void setPageTitles(List<String> pageTitles) {
        this.mPageTitles = pageTitles;
    }
}
