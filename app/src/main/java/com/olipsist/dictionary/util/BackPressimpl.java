package com.olipsist.dictionary.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

/**
 * Created by Olipsist on 1/15/2016.
 */
public class BackPressimpl implements BackPressListener {

    private Fragment parentFragment;

    public BackPressimpl(Fragment parentFragment){
        this.parentFragment = parentFragment;
    }

    @Override
    public boolean onBackPress() {
        if (parentFragment == null){
            return false;
        }

        int countChild = parentFragment.getChildFragmentManager().getBackStackEntryCount();

        if(countChild == 0){

            return false;
        }else{
            FragmentManager childFm = parentFragment.getChildFragmentManager();
            BackPressListener childFragment = (BackPressListener)childFm.getFragments().get(0);

            if(!childFragment.onBackPress()){
                childFm.popBackStackImmediate();
            }

            return true;
        }
    }
}
