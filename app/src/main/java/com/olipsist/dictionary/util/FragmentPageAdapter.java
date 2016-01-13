package com.olipsist.dictionary.util;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.olipsist.dictionary.fragment.FavFragment;
import com.olipsist.dictionary.fragment.HomeFragment;

/**
 * Created by Olipsist on 1/6/2016.
 */
public class FragmentPageAdapter extends FragmentPagerAdapter {

    Context context;
    final int pageCount = 2;
    String tabTitle[] = new String[]{"Home","FEV"};

    public FragmentPageAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment resultFragment = null;

        switch (position){
            case 0: resultFragment = HomeFragment.newInstance(null,null);
                break;
            case 1: resultFragment = FavFragment.newInstance(null, null);
                break;
        }

        return resultFragment;
    }

    @Override
    public int getCount() {
        return pageCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitle[position];
    }
}
