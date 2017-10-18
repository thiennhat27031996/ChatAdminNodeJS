package com.techhub.chatadminnodejs.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.techhub.chatadminnodejs.ChatActivity;
import com.techhub.chatadminnodejs.OBJ.Message;
import com.techhub.chatadminnodejs.OBJ.MessageSeen;
import com.techhub.chatadminnodejs.OBJ.MessageSeenModel;
import com.techhub.chatadminnodejs.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NTN on 14/10/2017.
 */

public class MessageSeenAdapter extends BaseAdapter {

    private List<MessageSeenModel>  list;
    Context context;

    public MessageSeenAdapter(List<MessageSeenModel> list, Context context) {
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
        Viewloaisphld viewloaisphld =null;
        if(view==null)
        {
            viewloaisphld= new Viewloaisphld();
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view =inflater.inflate(R.layout.item_lv_menuusermess,null);
            viewloaisphld.tvlastmessage=(TextView)view.findViewById(R.id.tvlassmessage);
            viewloaisphld.tvuser=(TextView) view.findViewById(R.id.tvtenuser);


            view.setTag(viewloaisphld);

        }
        else
        {
            viewloaisphld =(Viewloaisphld)view.getTag();


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




/*public class MessageSeenAdapter extends RecyclerView.Adapter<MessageSeenAdapter.MessageSeenViewHolder>{

    Context context;
    private List<MessageSeenModel> list;

    public MessageSeenAdapter(List<MessageSeenModel> list) {
        this.list = list;
    }

    @Override
    public MessageSeenViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MessageSeenViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lv_menuusermess,parent,false));
    }

    @Override
    public void onBindViewHolder(final MessageSeenViewHolder holder, int position) {
        final MessageSeenModel messageSeenModel=list.get(position);

        holder.tvuser.setText(messageSeenModel.name);
        holder.tvlastmessage.setText(messageSeenModel.lastmessage);

        /*holder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                for(int i=0;i<getItemCount();i++) {
                    contextMenu.add(holder.getAdapterPosition(), i, 0, messageSeenModel.name);

                }

            }
        });*/



    /*}

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MessageSeenViewHolder extends RecyclerView.ViewHolder{

        TextView tvuser,tvlastmessage;

        public MessageSeenViewHolder(View itemView) {
            super(itemView);
            tvuser=(TextView)itemView.findViewById(R.id.tvtenuser);
            tvlastmessage=(TextView)itemView.findViewById(R.id.tvlassmessage);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent= new Intent(context, ChatActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//set de xoa loi khi chuyen inten
                    intent.putExtra("from_user_id",list.get(getPosition()).name);//getpositon lay vitri

                    context.startActivity(intent);
                }
            });

        }
    }
}*/
