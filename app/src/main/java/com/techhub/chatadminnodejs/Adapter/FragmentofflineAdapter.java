package com.techhub.chatadminnodejs.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.techhub.chatadminnodejs.Fragment.FragmentMessage;
import com.techhub.chatadminnodejs.Fragment.FragmentMessageOffline;
import com.techhub.chatadminnodejs.Fragment.FragmentUnreadMessage;
import com.techhub.chatadminnodejs.Fragment.FragmentUnreadMessageOffline;

/**
 * Created by thiennhat on 19/10/2017.
 */

public class FragmentofflineAdapter extends FragmentPagerAdapter {
    private String[] tabitem=new String[]{"Offline User","Unread Offline User"};
    Context context;
    private int pagecount=2;
    public FragmentofflineAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context=context;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case  0:
                FragmentMessageOffline fragmentMessageOffline=new FragmentMessageOffline();
                return fragmentMessageOffline;

            case  1:
                FragmentUnreadMessageOffline fragmentUnreadMessageOffline=new FragmentUnreadMessageOffline();
                return  fragmentUnreadMessageOffline;

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
