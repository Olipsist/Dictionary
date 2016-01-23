package com.olipsist.dictionary.fragment;

import android.support.v4.app.Fragment;
import android.view.View;

import com.olipsist.dictionary.R;
import com.olipsist.dictionary.util.BackPressListener;
import com.olipsist.dictionary.util.BackPressimpl;

/**
 * Created by Olipsist on 1/15/2016.
 */
public class RootFragment extends Fragment implements BackPressListener {

    @Override
    public boolean onBackPress() {
        return new BackPressimpl(this).onBackPress();
    }

    public void showContentSearchView(){

    }

}
