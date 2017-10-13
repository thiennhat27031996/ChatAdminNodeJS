package com.techhub.chatadminnodejs.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.techhub.chatadminnodejs.OBJ.Message;
import com.techhub.chatadminnodejs.OBJ.MessageSeen;
import com.techhub.chatadminnodejs.R;

import java.util.ArrayList;

/**
 * Created by NTN on 10/10/2017.
 */

public class ListUserMessageAdapter extends BaseAdapter {
    ArrayList<MessageSeen> arrayListmess;
    Context context;


    public ListUserMessageAdapter(ArrayList<MessageSeen> arrayListmess, Context context) {
        this.arrayListmess = arrayListmess;
        this.context = context;
    }

    @Override
    public int getCount() {
        return arrayListmess.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayListmess.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    public static class Viewmessuser{
        TextView tvtenuser,tvlastmessage;


    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Viewmessuser viewmessuser=null;
        if(view==null){
            viewmessuser=new Viewmessuser();
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view =inflater.inflate(R.layout.item_lv_menuusermess,null);
            viewmessuser.tvtenuser=(TextView)view.findViewById(R.id.tvtenuser);
            viewmessuser.tvlastmessage=(TextView)view.findViewById(R.id.tvlassmessage);

            view.setTag(viewmessuser);
        }else{
            viewmessuser=(Viewmessuser)view.getTag();
        }
        MessageSeen messageSeen=(MessageSeen) getItem(i);
        viewmessuser.tvtenuser.setText(messageSeen.getTenUser());
        viewmessuser.tvlastmessage.setText(messageSeen.getLastMess());
        viewmessuser.tvlastmessage.setMaxWidth(300);
        viewmessuser.tvlastmessage.setMaxLines(1);
        viewmessuser.tvlastmessage.setEllipsize(TextUtils.TruncateAt.END);



        if(messageSeen.isSeen()){
            viewmessuser.tvtenuser.setTypeface(viewmessuser.tvtenuser.getTypeface(),Typeface.NORMAL);
            viewmessuser.tvtenuser.setTextColor(ContextCompat.getColor(context, R.color.messSeen));
            viewmessuser.tvlastmessage.setTextColor(ContextCompat.getColor(context, R.color.messSeen));
        }else{
            viewmessuser.tvtenuser.setTypeface(viewmessuser.tvtenuser.getTypeface(),Typeface.BOLD);
            viewmessuser.tvtenuser.setTextColor(ContextCompat.getColor(context, R.color.messSeenYet));
            viewmessuser.tvlastmessage.setTextColor(ContextCompat.getColor(context, R.color.messSeenYet));
        }

        return view;
    }
}
