package com.olipsist.dictionary;

import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.olipsist.dictionary.dao.MyDbHelper;
import com.olipsist.dictionary.util.FragmentPageAdapter;

public class MainActivity extends AppCompatActivity {

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

}
