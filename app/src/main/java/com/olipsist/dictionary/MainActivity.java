package com.olipsist.dictionary;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.olipsist.dictionary.dao.MyDbHelper;
import com.olipsist.dictionary.fragment.DetailFragment;
import com.olipsist.dictionary.fragment.FavFragment;
import com.olipsist.dictionary.fragment.RootFragment;
import com.olipsist.dictionary.fragment.SearchFragment;
import com.olipsist.dictionary.util.FragmentPageAdapter;


import java.util.Locale;

public class MainActivity extends AppCompatActivity implements DetailFragment.OnFragmentInteractionListener{

    private SQLiteDatabase db;
    private MyDbHelper helper;
    private ViewPager viewPager;
    private TextToSpeech tts;
    final static int INTENT_CHECK_TTS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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


//        #check TTS on device
        Intent intent = new Intent();
        intent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(intent, INTENT_CHECK_TTS);
    }

    @Override
    public void onFragmentInteraction(String name, String value) {

        switch (name) {

            case "DETAIL":

                FavFragment favFragment = (FavFragment) getSupportFragmentManager().findFragmentById(R.id.viewpager);
                favFragment.reflashView();

                break;

            case "TTS":

                    if(Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        tts.speak(value, TextToSpeech.QUEUE_FLUSH, null, null);
                    } else {
                        tts.speak(value, TextToSpeech.QUEUE_FLUSH, null);
                    }

                break;
        }

    }

    @Override
    public void onBackPressed() {
        RootFragment rootFragment = (RootFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + viewPager.getCurrentItem());
        Log.i("TAG",rootFragment.getTag());

//        #check for show content in R.id.search_content_view
        if(viewPager.getCurrentItem()==0){
            rootFragment.showContentSearchView();
        }

//        #Check empty child
        if (!rootFragment.onBackPress()){
            super.onBackPressed();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == INTENT_CHECK_TTS) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {

                tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if(status == TextToSpeech.SUCCESS)
                            tts.setLanguage(Locale.US);
                    }
                });
            } else {

                installTTS();
            }
        }
    }

    public void goSettingActivity(MenuItem item){
        Intent intent = new Intent(getApplication(),SettingActivity.class);
        startActivity(intent);
    }

    private void installTTS(){
        Intent intent = new Intent();
        intent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
        startActivity(intent);
    }

    private void updateDatabase(){
        helper = new MyDbHelper(getApplicationContext());
        db = helper.getWritableDatabase();
    }

}
