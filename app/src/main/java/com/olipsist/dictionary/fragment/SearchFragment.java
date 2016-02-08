package com.olipsist.dictionary.fragment;

import android.animation.Animator;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.olipsist.dictionary.R;
import com.olipsist.dictionary.dao.MyDbHelper;
import com.olipsist.dictionary.util.CheckCharLang;
import com.olipsist.dictionary.util.MyCursorAdapter;


public class SearchFragment extends RootFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ListView resultListView;
    private EditText searchEditText;
    private SQLiteDatabase db;
    private MyDbHelper helper;
    private Context context;
    private MyCursorAdapter adapter;
    private ImageButton backspaceButton;

    private String mParam1;
    private String mParam2;



    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        initView(inflater.getContext(), rootView);

        db = helper.getWritableDatabase();
        Cursor cursor = helper.initCursor(db);
        cursor.moveToFirst();
        adapter = new MyCursorAdapter(inflater.getContext(),cursor,0);
        resultListView.setAdapter(adapter);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(String.valueOf(s));
                if(count>0){
                    resultListView.setVisibility(View.VISIBLE);
                }else{
                    resultListView.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        searchEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    searchEditText.clearFocus();
                    hideKeyboard();
                    return true;
                }

                return false;
            }
        });

        adapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                if (constraint.toString().isEmpty()) {
                    return null;
                }
                return helper.findWordByString(db, constraint.toString().trim());
            }
        });

        resultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //hide softkeyboard
//                if (searchEditText != null) {
//                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
//                }
                hideKeyboard();

                Cursor cursorTest = adapter.getCursor();
                cursorTest.moveToPosition(position);

                String idWord = cursorTest.getString(cursorTest.getColumnIndex("id"));
                String word = cursorTest.getString(cursorTest.getColumnIndex("esearch"));

                DetailFragment detailFragment = DetailFragment.newInstance(word, idWord);

                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.in, R.anim.out);
                transaction.replace(R.id.rootViewSearch, detailFragment);
                transaction.addToBackStack("HOME");
                transaction.commit();
            }
        });

        resultListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
            }
        });


        backspaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEditText.setText("");
                searchEditText.requestFocus();
                InputMethodManager imm = (InputMethodManager)inflater.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        return rootView;
    }

    private void initView(Context context, View rootView){
        resultListView = (ListView)rootView.findViewById(R.id.resultListView_Home);
        searchEditText = (EditText)rootView.findViewById(R.id.searchEditText);
        backspaceButton = (ImageButton)rootView.findViewById(R.id.backspaceButton);
        this.context = context;
        helper = new MyDbHelper(context);
    }

    private void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
    }


}
