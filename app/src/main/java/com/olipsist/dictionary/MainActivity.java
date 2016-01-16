package com.olipsist.dictionary;

import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.olipsist.dictionary.dao.MyDbHelper;
import com.olipsist.dictionary.fragment.DetailFragment;
import com.olipsist.dictionary.fragment.FavFragment;
import com.olipsist.dictionary.fragment.RootFragment;
import com.olipsist.dictionary.util.FragmentPageAdapter;

public class MainActivity extends AppCompatActivity implements DetailFragment.OnFragmentInteractionListener{

    private SQLiteDatabase db;
    private MyDbHelper helper;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        updateDatabase();

        FragmentPageAdapter adapter = new FragmentPageAdapter(getSupportFragmentManager(),MainActivity.this);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        // Iterate over all tabs and set the custom view
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(adapter.getTabView(i));
        }
    }

    private void updateDatabase(){
        helper = new MyDbHelper(getApplicationContext());
        db = helper.getWritableDatabase();
    }

    @Override
    public void onFragmentInteraction(String name) {
        FavFragment favFragment = (FavFragment) getSupportFragmentManager().findFragmentById(R.id.viewpager);
        FragmentManager fm = favFragment.getChildFragmentManager();
        favFragment.reflashView();


    }

    @Override
    public void onBackPressed() {
        RootFragment rootFragment = (RootFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + viewPager.getCurrentItem());
        if (!rootFragment.onBackPress()){
            super.onBackPressed();
        }
    }

}
