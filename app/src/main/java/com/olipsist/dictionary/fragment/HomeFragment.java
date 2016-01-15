package com.olipsist.dictionary.fragment;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;

import com.olipsist.dictionary.R;
import com.olipsist.dictionary.dao.MyDbHelper;
import com.olipsist.dictionary.util.MyCursorAdapter;


public class HomeFragment extends RootFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ListView resultListView;
    private EditText searchEditText;
    private SQLiteDatabase db;
    private MyDbHelper helper;
    private Context context;
    private MyCursorAdapter adapter;
    // TODO: Rename and change types of parameters
    private String mParam2;


//    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        initView(inflater.getContext(), rootView);
        openDatabase();
        Cursor cursor = helper.findAllWord(db);
        cursor.moveToFirst();
        adapter = new MyCursorAdapter(inflater.getContext(),cursor,0);
        resultListView.setAdapter(adapter);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        adapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {

                return helper.findWordByString(db, constraint.toString());
            }
        });

        resultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursorTest = adapter.getCursor();
                cursorTest.moveToPosition(position);

                String idWord = cursorTest.getString(cursorTest.getColumnIndex("id"));
                String word = cursorTest.getString(cursorTest.getColumnIndex("esearch"));

                DetailFragment detailFragment = DetailFragment.newInstance(word, idWord);
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

                transaction.replace(R.id.rootViewHome, detailFragment);
                transaction.addToBackStack("HOME");
                transaction.commit();

            }
        });

        return rootView;
    }

    private void initView(Context context, View rootView){
        resultListView = (ListView)rootView.findViewById(R.id.resultListView_Home);
        searchEditText = (EditText)rootView.findViewById(R.id.searchEditText);
        this.context = context;
        helper = new MyDbHelper(context);
    }

    private void openDatabase(){
        db = helper.getWritableDatabase();
    }

    private void closeDatabase(){
        db.close();
    }

    public void testBack(){
        Log.i("TEST BACK","TEST BACK");
    }
}
