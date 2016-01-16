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

/**
 * Created by Olipsist on 1/6/2016.
 */
public class FragmentPageAdapter extends FragmentPagerAdapter {

    Context context;
    final int pageCount = 2;
    String tabTitle[] = new String[]{"Search","Favourite"};
    int imageTitle[] = new int[]{R.drawable.ic_search_black_24px, R.drawable.ic_star_black_24px};

    public FragmentPageAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment resultFragment = null;

        switch (position){
            case 0: resultFragment = SearchFragment.newInstance(null, null);
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
        SpannableString sb = new SpannableString("   " + tabTitle[position]);
        Resources res = context.getResources();
        Drawable drawable = null;
        drawable = ContextCompat.getDrawable(context,R.drawable.ic_stars_black_24dp);
        drawable.setBounds(0, 0, 50, 50);

        ImageSpan imageSpan = new ImageSpan(drawable,ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan,0,1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return sb;
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
