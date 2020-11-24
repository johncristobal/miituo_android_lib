package com.miituo.miituolibrary.activities.adapters;

import android.content.Context;
import android.graphics.Typeface;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class PagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;
    public static Context context;
    public static long tiempo;
    public static ViewPager pager;

    public PagerAdapter(FragmentManager fm, int NumOfTabs, Context c, long tiempo, ViewPager vp) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        context = c;
        this.tiempo = tiempo;
        pager = vp;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                TabFragment1 tab1 = new TabFragment1();
                //TabFragment1 tab1 = new TabFragment1(context,tipo);
                return tab1;
            case 1:
                TabFragment2 tab2 = new TabFragment2();
                //TabFragment2 tab2 = new TabFragment2(tipo);
                return tab2;
            /*case 2:
                TabFragment3 tab3 = new TabFragment3();
                return tab3;*/
            case 2:
                TabFragment4 tab4 = new TabFragment4();
                //TabFragment4 tab4 = new TabFragment4(context,tipo);
                return tab4;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}

