package com.olipsist.dictionary.fragment;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.olipsist.dictionary.R;
import com.olipsist.dictionary.dao.MyDbHelper;
import com.olipsist.dictionary.util.MyCursorAdapter;


public class FavFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private MyDbHelper helper;
    private SQLiteDatabase db;
    private ListView resultListView;
    private MyCursorAdapter adapter;
    private OnFragmentInteractionListener mListener;

    public FavFragment() {
        // Required empty public constructor
    }


    public static FavFragment newInstance(String param1, String param2) {
        FavFragment fragment = new FavFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_fav, container, false);
        initView(inflater.getContext(), rootView);
        reflashView();

        resultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursorTest =  adapter.getCursor();
                cursorTest.moveToPosition(position);

                DetailFragment detailFragment = DetailFragment.newInstance(cursorTest.getString(cursorTest.getColumnIndex("esearch")),String.valueOf(position));
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(R.id.rootViewFav, detailFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return rootView;
    }

    private void initView(Context context, View rootView){
        helper = new MyDbHelper(context);
        db = helper.getWritableDatabase();
        resultListView = (ListView) rootView.findViewById(R.id.resultListView_Fav);
    }

    public void reflashView(){
        if(adapter != null){
            adapter.getCursor().close();
        }
        Cursor cursor = helper.findAllFevWord(db);
        cursor.moveToFirst();
        adapter = new MyCursorAdapter(this.getContext(),cursor,0);
        resultListView.setAdapter(adapter);
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
        // TODO: Update argument type and name
        void onFragmentInteraction(String name);
    }
}
