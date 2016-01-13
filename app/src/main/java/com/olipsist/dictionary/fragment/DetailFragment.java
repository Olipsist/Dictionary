package com.olipsist.dictionary.fragment;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.olipsist.dictionary.R;
import com.olipsist.dictionary.dao.MyDbHelper;
import com.olipsist.dictionary.util.DetailArrayAdapter;
import com.olipsist.dictionary.util.DetailCursorAdapter;
import com.olipsist.dictionary.util.DetailWord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String wordStr;
    private String wordId;
    private MyDbHelper helper;
    private SQLiteDatabase db;
    private DetailArrayAdapter adapter;
    private ListView detailListView;
    private TextView wordTextView;
    private ToggleButton toggleButton;

//    private OnFragmentInteractionListener mListener;

    public DetailFragment() {
        // Required empty public constructor
    }


    public static DetailFragment newInstance(String param1, String param2) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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

        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String favValue;
                if (toggleButton.isChecked()){
                    favValue = "A";
                }else{
                    favValue = "W";
                }
                helper.setFavUpdate(db,wordId, favValue);
            }
        });

        return rootView;
    }

    private void initView(Context context, View rootView){
        wordTextView = (TextView) rootView.findViewById(R.id.wordTextView_detail);
        detailListView = (ListView) rootView.findViewById(R.id.listView_detail);
        toggleButton = (ToggleButton) rootView.findViewById(R.id.fevToggleButton_Detail);
        helper = new MyDbHelper(context);
        db =  helper.getWritableDatabase();


    }

    private ArrayList<DetailWord> prepareData(String word){

//        #FindTypeOfWord
        Cursor cursorCat = helper.findTypeOfWord(db,wordStr);
        cursorCat.moveToFirst();
        ArrayList<String> arrayCat = new ArrayList<>();
        for(int i = 0;i< cursorCat.getCount();i++){
            arrayCat.add(cursorCat.getString(cursorCat.getColumnIndex("ecat")));
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
                builderEntry += cursor.getString(cursor.getColumnIndex("tentry"))+", ";
                if(cursor.getString(cursor.getColumnIndex("fev")).equals("A")){
                    toggleButton.setChecked(true);
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

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p/>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
