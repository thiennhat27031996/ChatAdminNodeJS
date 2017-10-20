package com.techhub.chatadminnodejs.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.techhub.chatadminnodejs.Fragment.FragmentMessage;
import com.techhub.chatadminnodejs.Fragment.FragmentMessageOffline;
import com.techhub.chatadminnodejs.Fragment.FragmentUnreadMessage;

/**
 * Created by thiennhat on 17/10/2017.
 */

public class FragmentAdapter extends FragmentPagerAdapter {
    private String[] tabitem=new String[]{"Online User","Unread Online User"};
    Context context;
    private int pagecount=2;
    public FragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context=context;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case  0:
                FragmentMessage fragmentMessage=new FragmentMessage();
                return fragmentMessage;

            case  1:
                FragmentUnreadMessage fragmentUnreadMessage=new FragmentUnreadMessage();
                return  fragmentUnreadMessage;

            default:
                return null;
        }


    }

    @Override
    public int getCount() {
        return pagecount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabitem[position];
    }
}

