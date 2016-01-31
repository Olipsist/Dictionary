package com.olipsist.dictionary;


import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.olipsist.dictionary.dao.MyDbHelper;
import com.olipsist.dictionary.fragment.DetailFragment;
import com.olipsist.dictionary.fragment.FavFragment;
import com.olipsist.dictionary.fragment.RootFragment;
import com.olipsist.dictionary.util.FragmentPageAdapter;


import java.util.Locale;

public class MainActivity extends AppCompatActivity implements DetailFragment.OnFragmentInteractionListener{

    private SQLiteDatabase db;
    private MyDbHelper helper;
    private ViewPager viewPager;
    private TextToSpeech tts;
    private TabLayout tabLayout;
    final static int INTENT_CHECK_TTS = 0;
    private FragmentPageAdapter adapter;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ads(findViewById(R.id.adView));

//        #Check Database Version
//        helper = new MyDbHelper(getApplicationContext());
//        SharedPreferences sp = getSharedPreferences("dbVersion", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sp.edit();
//        int version = sp.getInt("version",-1);
//        if(version<5){
//            helper.setForcedUpgrade(5);
//            editor.putInt("version", 5);
//            editor.commit();
//        }

        adapter = new FragmentPageAdapter(getSupportFragmentManager(),MainActivity.this);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        // Iterate over all tabs and set the custom view
        customTabLayout();

        fab = (FloatingActionButton) findViewById(R.id.myFAB);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                backToSearch();

            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position>0){
                    InputMethodManager imm = (InputMethodManager) MainActivity.this.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(viewPager.getWindowToken(), 0);
                    showFloatBtn();
                }else{
                    hideFloatBtn();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

//        #check TTS on device
//        Intent intent = new Intent();
//        intent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
//        startActivityForResult(intent, INTENT_CHECK_TTS);

    }

    @Override
    public void onFragmentInteraction(String name, String value) {

        switch (name) {

            case "DETAIL":

                    showFloatBtn();

                break;

            case "TTS":

                    if(Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        tts.speak(value, TextToSpeech.QUEUE_FLUSH, null, null);
                    } else {
                        tts.speak(value, TextToSpeech.QUEUE_FLUSH, null);
                    }

                break;
            case "FAVOURITE":

                    FavFragment favFragment = (FavFragment) getSupportFragmentManager().findFragmentById(R.id.viewpager);
                    favFragment.reflashView();

                break;
        }

    }

    @Override
    public void onBackPressed() {
        RootFragment rootFragment = (RootFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + viewPager.getCurrentItem());
//        Log.i("TAG",rootFragment.getTag());

//        #check for show content in R.id.search_content_view
        if(viewPager.getCurrentItem()==0){
            rootFragment.showContentSearchView();

        }else{
            rootFragment.showContentFavView();
        }

//        #Check empty child
        if (!rootFragment.onBackPress()){
            if(viewPager.getCurrentItem()!=0){
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
                tabLayout.setupWithViewPager(viewPager);
                customTabLayout();

            }else{
                super.onBackPressed();
            }
        }
        hideFloatBtn();
    }

    public void goSettingActivity(MenuItem item){
        Intent intent = new Intent(getApplication(),SettingActivity.class);
        startActivity(intent);
    }

    private void updateDatabase(){
        helper = new MyDbHelper(getApplicationContext());
        db = helper.getWritableDatabase();
    }

    private void ads(View rootView){

        AdRequest.Builder adBuilder = new AdRequest.Builder();
        adBuilder.addTestDevice("B13BB59F7FDD1D5AED31EDB6794C6ECB");

        AdRequest adRequest = adBuilder.build();
        final AdView adView = (AdView) rootView;
        adView.loadAd(adRequest);

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);
                adView.setVisibility(View.GONE);
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                adView.setVisibility(View.VISIBLE);
            }
        });

    }

    private void customTabLayout(){
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(adapter.getTabView(i));
        }
    }

    private void backToSearch(){
        RootFragment rootFragment = (RootFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + 0);
        if(viewPager.getCurrentItem()!=0) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
            tabLayout.setupWithViewPager(viewPager);
            customTabLayout();
        }
        rootFragment.onBackPress();
        rootFragment.showContentSearchView();
        EditText searchEditText = (EditText) rootFragment.getView().findViewById(R.id.searchEditText);
        searchEditText.requestFocus();
        searchEditText.setText("");
        InputMethodManager imm = (InputMethodManager) this.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT);
        hideFloatBtn();
    }

    private void hideFloatBtn(){
        if(fab.getVisibility()==View.VISIBLE) {
            RootFragment rootFragment = (RootFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + 0);
            if (rootFragment.getChildFragmentManager().getBackStackEntryCount()==0&&viewPager.getCurrentItem()==0) {
                TranslateAnimation animation = new TranslateAnimation(0, 0, Animation.RELATIVE_TO_SELF, 1000);
                animation.setDuration(500);
                animation.setFillAfter(false);
                animation.setInterpolator(new AnticipateInterpolator());
                fab.startAnimation(animation);
                fab.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void showFloatBtn(){
        if(fab.getVisibility()==View.INVISIBLE){
            TranslateAnimation animation = new TranslateAnimation(0,0,700,Animation.RELATIVE_TO_SELF);
            animation.setDuration(700);
            animation.setFillAfter(true);
            animation.setInterpolator(new AnticipateOvershootInterpolator(2f));
            fab.startAnimation(animation);
            fab.setVisibility(View.VISIBLE);
        }
    }

    //    private void installTTS(){
//        Intent intent = new Intent();
//        intent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
//        startActivity(intent);
//    }

    //    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == INTENT_CHECK_TTS) {
//            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
//
//                tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
//                    @Override
//                    public void onInit(int status) {
//                        if(status == TextToSpeech.SUCCESS)
//                            tts.setLanguage(Locale.US);
//                    }
//                });
//            } else {
//
//                installTTS();
//            }
//        }
//    }

}
