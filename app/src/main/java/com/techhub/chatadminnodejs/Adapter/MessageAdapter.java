package com.techhub.chatadminnodejs.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.squareup.picasso.Picasso;
import com.techhub.chatadminnodejs.OBJ.Message;
import com.techhub.chatadminnodejs.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thiennhat on 05/10/2017.
 */

public  class  MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{

    private static final int MY_MESS=0,OTHER_MESS=1;

    private ArrayList<Message> mMessages;
    private Context context;

    public MessageAdapter(ArrayList<Message> mMessages, Context context) {
        this.mMessages = mMessages;
        this.context = context;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {



        if(viewType==MY_MESS){
            return new MessageViewHolder(LayoutInflater.from(context).inflate(R.layout.item_rclmainmhc,parent,false));
        }else{
            return new MessageViewHolder(LayoutInflater.from(context).inflate(R.layout.item_rclmainmhcrecv,parent,false));
        }


    }
    public void add(Message message){
        mMessages.add(message);
        notifyItemInserted(mMessages.size()-1);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Message message=mMessages.get(position);

            holder.tvten.setText(message.name);
       holder.tvmes.setText(message.lastmessage);
        if(!message.url.equals("null")) {
            holder.imvmes.setVisibility(View.VISIBLE);
            Picasso.with(context).load(message.url).error(R.drawable.error).placeholder(R.drawable.progress_animation).into(holder.imvmes);
        }
        else{
            holder.imvmes.setVisibility(View.GONE);
        }



    }

    @Override
    public int getItemCount() {
        return mMessages==null ? 0 : mMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message item=mMessages.get(position);
        if(item.usermess.equals("false")) return MY_MESS;
        else return OTHER_MESS;
    }

    class MessageViewHolder extends RecyclerView.ViewHolder{
        TextView tvten,tvmes;
        ImageView imvmes;

        MessageViewHolder(View itemView){
            super(itemView);

            tvten=(TextView)itemView.findViewById(R.id.idnguoiguitin);
            tvmes=(TextView)itemView.findViewById(R.id.textnguoigui);
            imvmes=(ImageView)itemView.findViewById(R.id.imvtinnhan);
        }


    }
}


/*public class MessageAdapter extends BaseAdapter {

    private ArrayList<Message> listmessage;
    private Context context;


    public MessageAdapter(ArrayList<Message> listmessage, Context context) {
        this.listmessage = listmessage;
        this.context = context;


    }



    @Override
    public int getCount() {
        return listmessage.size();
    }

    @Override
    public Object getItem(int position) {
        return listmessage.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class Viewmessagelhd{
        TextView txtten,txtmessage;


    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        Viewmessagelhd viewmessagelhd = null;
        if(convertView ==null){
            viewmessagelhd=new Viewmessagelhd();

            if(listmessage.get(position).isSend){
                LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView=inflater.inflate(R.layout.item_rclmainmhc,null);
                viewmessagelhd.txtten=(TextView)convertView.findViewById(R.id.idnguoiguitin);
                viewmessagelhd.txtmessage=(TextView)convertView.findViewById(R.id.textnguoigui);

                convertView.setTag(viewmessagelhd);

            }else{
                LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView=inflater.inflate(R.layout.item_rclmainmhcrecv,null);
                viewmessagelhd.txtten=(TextView)convertView.findViewById(R.id.idnguoiguitin);
                viewmessagelhd.txtmessage=(TextView)convertView.findViewById(R.id.textnguoigui);
                convertView.setTag(viewmessagelhd);



            }



        }
        else {
            viewmessagelhd=(Viewmessagelhd)convertView.getTag();
        }

        Message message=(Message)getItem(position);
        viewmessagelhd.txtten.setText(message.getTenUser());
        viewmessagelhd.txtmessage.setText(message.getTinNhan());
        return  convertView;
    }
}*/
