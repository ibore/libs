package me.ibore.widget;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * description:
 * author: Ibore Xie
 * date: 2018-01-21 13:11
 * website: ibore.me
 */

public class ViewAdapter extends PagerAdapter {

    private List<View> mViews;
    private List<String> mPageTitles = new ArrayList<>();

    public ViewAdapter(List<View> views) {
        this.mViews = views;
    }

    public ViewAdapter(List<View> views, List<String> pageTitles) {
        this.mViews = views;
        this.mPageTitles = pageTitles;
    }

    @Override
    public int getCount() {
        return mViews.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mViews.get(position));
        return mViews.get(position);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
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
