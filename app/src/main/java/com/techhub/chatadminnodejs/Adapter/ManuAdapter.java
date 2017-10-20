package com.techhub.chatadminnodejs.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.daasuu.bl.BubbleLayout;
import com.techhub.chatadminnodejs.OBJ.Menu;
import com.techhub.chatadminnodejs.OBJ.MessageSeenModel;
import com.techhub.chatadminnodejs.R;

import java.util.List;

/**
 * Created by thiennhat on 20/10/2017.
 */

public class ManuAdapter extends BaseAdapter {

    private List<Menu> list;
    Context context;

    public ManuAdapter(List<Menu> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {

        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public static class Viewloaisphld{
        TextView tvtenitem,tvcoutitem;
        BubbleLayout bubbleLayout;




    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ManuAdapter.Viewloaisphld viewloaisphld =null;
        if(view==null)
        {
            viewloaisphld= new ManuAdapter.Viewloaisphld();
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view =inflater.inflate(R.layout.item_lv_menu,null);
            viewloaisphld.tvtenitem=(TextView)view.findViewById(R.id.tvtenitem);
            viewloaisphld.tvcoutitem=(TextView) view.findViewById(R.id.tvcoutitem);
            viewloaisphld.bubbleLayout=(BubbleLayout)view.findViewById(R.id.paddingtvcount) ;


            view.setTag(viewloaisphld);

        }
        else
        {
            viewloaisphld =(ManuAdapter.Viewloaisphld)view.getTag();


        }

        final Menu messageseen=(Menu) getItem(position);
        viewloaisphld.tvtenitem.setText(messageseen.getName());
        viewloaisphld.tvcoutitem.setText(messageseen.getNumbercart());



      /*  final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(messageseen.seen.equals("false")) {
                    // Do something after 5s = 5000ms
                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Ringtone r = RingtoneManager.getRingtone(context, notification);
                    r.play();
                }

            }
        }, 2000);*/


        if(messageseen.getNumbercart().equals("0")){
            viewloaisphld.tvcoutitem.setVisibility(View.GONE);
            viewloaisphld.bubbleLayout.setVisibility(View.GONE);
        }else{
            viewloaisphld.tvcoutitem.setVisibility(View.VISIBLE);
            viewloaisphld.bubbleLayout.setVisibility(View.VISIBLE);
        }

        //picasso cho phep khi load anh thi co anh nen va hein error khi k load duoc

        return view;
    }
}
