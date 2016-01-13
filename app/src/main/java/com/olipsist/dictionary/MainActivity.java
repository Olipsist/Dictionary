package com.olipsist.dictionary;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.olipsist.dictionary.dao.MyDbHelper;
import com.olipsist.dictionary.fragment.DetailFragment;
import com.olipsist.dictionary.fragment.FavFragment;
import com.olipsist.dictionary.fragment.HomeFragment;
import com.olipsist.dictionary.util.FragmentPageAdapter;

public class MainActivity extends AppCompatActivity implements DetailFragment.OnFragmentInteractionListener,FavFragment.OnFragmentInteractionListener{

    private SQLiteDatabase db;
    private MyDbHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        updateDatabase();

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new FragmentPageAdapter(getSupportFragmentManager(),MainActivity.this));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void updateDatabase(){
        helper = new MyDbHelper(getApplicationContext());
        db = helper.getWritableDatabase();
    }

    @Override
    public void onFragmentInteraction(String name) {
        Log.i("TEST INTERFACE",name);
        FavFragment favFragment = (FavFragment) getSupportFragmentManager().findFragmentById(R.id.viewpager);
        favFragment.reflashView();
    }
}
