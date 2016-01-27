package com.olipsist.dictionary.fragment;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.olipsist.dictionary.R;
import com.olipsist.dictionary.dao.MyDbHelper;
import com.olipsist.dictionary.util.DetailArrayAdapter;
import com.olipsist.dictionary.util.DetailWord;

import java.util.ArrayList;

public class DetailFragment extends RootFragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String wordStr;
    private String wordId;
    private MyDbHelper helper;
    private SQLiteDatabase db;
    private DetailArrayAdapter adapter;
    private ListView detailListView;
    private TextView wordTextView;
    private MaterialFavoriteButton favoriteButton;
    private ImageButton speakButton;

    private OnFragmentInteractionListener mListener;

    public DetailFragment() {
        // Required empty public constructor
    }


    public static DetailFragment newInstance(String wordStr, String wordId) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, wordStr);
        args.putString(ARG_PARAM2, wordId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            wordStr = getArguments().getString(ARG_PARAM1);
            wordId = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        initView(inflater.getContext(), rootView);
        ArrayList<DetailWord> arrayListDetail = prepareData(wordStr);
        adapter = new DetailArrayAdapter(inflater.getContext(),arrayListDetail);
        detailListView.setAdapter(adapter);
        wordTextView.setText(wordStr);

//        #Hide Old Content On R.id.search_content_view
        hideOldContentView(container);

        favoriteButton.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {
            @Override
            public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                String favValue;
                if (favoriteButton.isFavorite()) {
                    favValue = "A";
                } else {
                    favValue = "W";
                }
                helper.setFavUpdate(db, wordId, favValue);
                mListener.onFragmentInteraction("DETAIL", null);
            }
        });

//        speakButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mListener.onFragmentInteraction("TTS", wordStr);
//            }
//        });

        return rootView;
    }

    private void initView(Context context, View rootView){
        wordTextView = (TextView) rootView.findViewById(R.id.wordTextView_detail);
        detailListView = (ListView) rootView.findViewById(R.id.listView_detail);
        favoriteButton = (MaterialFavoriteButton) rootView.findViewById(R.id.fevToggleButton_detail);
//        speakButton = (ImageButton) rootView.findViewById(R.id.speakButton);
        helper = new MyDbHelper(context);
        db =  helper.getWritableDatabase();
    }

    private ArrayList<DetailWord> prepareData(String word){

//        #FindTypeOfWord
        Cursor cursorCat = helper.findTypeOfWord(db,wordStr);
        cursorCat.moveToFirst();
        ArrayList<String> arrayCat = new ArrayList<>();
        for(int i = 0;i< cursorCat.getCount();i++){
            arrayCat.add(cursorCat.getString(cursorCat.getColumnIndex("cat")));
            cursorCat.moveToNext();
        }
        cursorCat.close();

//        #prepareData
        ArrayList<DetailWord> resultArray = new ArrayList<>();
        for(String cat:arrayCat){
            Cursor cursor = helper.findWordsDetailByString(db, word, cat);
            cursor.moveToFirst();
            String builderEntry="";
            for(int i = 0;i<cursor.getCount();i++){
                builderEntry += cursor.getString(cursor.getColumnIndex("entry2"))+", ";
                if(cursor.getString(cursor.getColumnIndex("fav")).equals("A")){
                    favoriteButton.setFavorite(true,false);
                }
                cursor.moveToNext();
            }
            builderEntry = builderEntry.substring(0,builderEntry.length()-2);
            DetailWord detailWord = new DetailWord(cat, builderEntry);
            resultArray.add(detailWord);
            cursor.close();
        }

        return resultArray;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String name, String value);
    }

    private void hideOldContentView(ViewGroup container){

        View contentView = container.findViewById(R.id.viewpager);
        if(contentView!=null) {
            contentView.setVisibility(View.INVISIBLE);
        }
    }
}
