package com.olipsist.dictionary.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.olipsist.dictionary.R;
import com.olipsist.dictionary.fragment.FavFragment;
import com.olipsist.dictionary.fragment.SearchFragment;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Olipsist on 1/6/2016.
 */
public class FragmentPageAdapter extends FragmentPagerAdapter {

    Context context;
    final int pageCount = 2;
    String tabTitle[] = new String[]{"Search", "Favourite"};
    int imageTitle[] = new int[]{R.drawable.ic_search_black_24px, R.drawable.ic_star_black_24px};
    public ArrayList<Fragment> pages = new ArrayList<>();

    public FragmentPageAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        pages.add(SearchFragment.newInstance(null, null));
        pages.add(FavFragment.newInstance(null, null));
    }

    @Override
    public Fragment getItem(int position) {
//        Fragment resultFragment = null;
//
//        switch (position){
//            case 0: resultFragment = SearchFragment.newInstance(null, null);
//                break;
//            case 1: resultFragment = FavFragment.newInstance(null, null);
//                break;
//        }

        return pages.get(position);
    }

    @Override
    public int getCount() {
        return pages.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitle[position];
    }

    public View getTabView(int position) {
        // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
        View v = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        TextView tv = (TextView) v.findViewById(R.id.cusTabTextView);
        tv.setText(tabTitle[position]);
        ImageView img = (ImageView) v.findViewById(R.id.cusTabImageView);
        img.setImageResource(imageTitle[position]);
        return v;
    }
}

