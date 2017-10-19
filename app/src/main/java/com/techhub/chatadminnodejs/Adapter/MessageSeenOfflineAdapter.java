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

import com.techhub.chatadminnodejs.OBJ.MessageSeenModel;
import com.techhub.chatadminnodejs.R;

import java.util.List;

/**
 * Created by thiennhat on 19/10/2017.
 */

public class MessageSeenOfflineAdapter  extends BaseAdapter {

    private List<MessageSeenModel> list;
    Context context;

    public MessageSeenOfflineAdapter(List<MessageSeenModel> list, Context context) {
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
        TextView tvuser,tvlastmessage;



    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        MessageSeenAdapter.Viewloaisphld viewloaisphld =null;
        if(view==null)
        {
            viewloaisphld= new MessageSeenAdapter.Viewloaisphld();
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view =inflater.inflate(R.layout.item_lv_menuusermessoffline,null);
            viewloaisphld.tvlastmessage=(TextView)view.findViewById(R.id.tvlassmessageoffline);
            viewloaisphld.tvuser=(TextView) view.findViewById(R.id.tvtenuseroffline);


            view.setTag(viewloaisphld);

        }
        else
        {
            viewloaisphld =(MessageSeenAdapter.Viewloaisphld)view.getTag();


        }

        final MessageSeenModel messageseen=(MessageSeenModel) getItem(position);
        viewloaisphld.tvuser.setText(messageseen.name);
        viewloaisphld.tvlastmessage.setText(messageseen.lastmessage);
        viewloaisphld.tvlastmessage.setEllipsize(TextUtils.TruncateAt.END);


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


        if(messageseen.seen.equals("true")){

            viewloaisphld.tvuser.setTypeface(viewloaisphld.tvuser.getTypeface(), Typeface.NORMAL);
            viewloaisphld.tvuser.setTextColor(ContextCompat.getColor(context, R.color.messSeen));
            viewloaisphld.tvlastmessage.setTextColor(ContextCompat.getColor(context, R.color.messSeen));

        }else{

            viewloaisphld.tvuser.setTypeface(viewloaisphld.tvuser.getTypeface(),Typeface.BOLD);
            viewloaisphld.tvuser.setTextColor(ContextCompat.getColor(context, R.color.messSeenYet));
            viewloaisphld.tvlastmessage.setTextColor(ContextCompat.getColor(context, R.color.messSeenYet));
        }

        //picasso cho phep khi load anh thi co anh nen va hein error khi k load duoc

        return view;
    }
}


