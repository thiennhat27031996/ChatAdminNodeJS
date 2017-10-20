package com.techhub.chatadminnodejs.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.techhub.chatadminnodejs.Adapter.MessageSeenAdapter;
import com.techhub.chatadminnodejs.Adapter.MessageSeenOfflineAdapter;
import com.techhub.chatadminnodejs.ChatActivity;
import com.techhub.chatadminnodejs.ClassUse.CheckinternetToat;
import com.techhub.chatadminnodejs.OBJ.MessageSeenModel;
import com.techhub.chatadminnodejs.Pref.Userinfo;
import com.techhub.chatadminnodejs.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thiennhat on 19/10/2017.
 */

public class FragmentMessageOffline extends Fragment {
    private ListView listviewUsermess;
    private Userinfo userinfo;
    private static List<MessageSeenModel> resultMessageSeenmodel;
    private static MessageSeenOfflineAdapter messageSeenOfflineAdapter;
    private FirebaseDatabase databaseUsermessMain;
    private DatabaseReference databaseUsermessMainreference;
    private DatabaseReference databaseUsermessMainreferenceMessagedelete;
    private LinearLayout lnhavemessage;
    private RelativeLayout lnnomessage;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_message_offline,container,false);


        if(  CheckinternetToat.haveNetworkConnection(getContext())){
            Anhxa(view);
            updateList();
        }else {
            CheckinternetToat.alertchecktb(getContext(), "No have Network Connection", "Try again later");
        }

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void Anhxa(View view) {
        userinfo=new Userinfo(getContext());
        listviewUsermess=(ListView)view.findViewById(R.id.lvuseroffline);
        lnhavemessage=(LinearLayout)view.findViewById(R.id.lnhavemessageoffline);
        lnnomessage=(RelativeLayout)view.findViewById(R.id.lnnohavemessageoffline);
        resultMessageSeenmodel=new ArrayList<>();
        messageSeenOfflineAdapter=new MessageSeenOfflineAdapter(resultMessageSeenmodel,getContext());
        listviewUsermess.setAdapter(messageSeenOfflineAdapter);

        databaseUsermessMain= FirebaseDatabase.getInstance();
        databaseUsermessMainreference=databaseUsermessMain.getReference(userinfo.getKeyUserid()).child("OnlineMess");
        databaseUsermessMainreferenceMessagedelete=FirebaseDatabase.getInstance().getReference(userinfo.getKeyUserid()).child("MessageSeen");





        listviewUsermess.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(  CheckinternetToat.haveNetworkConnection(getContext())){
                    databaseUsermessMainreference.child(resultMessageSeenmodel.get(i).name).child("seen").setValue("true");
                    Intent intent = new Intent(getContext(), ChatActivity.class);
                    intent.putExtra("from_user_id",resultMessageSeenmodel.get(i).name);
                    intent.putExtra("user_name", "Admin");
                    intent.putExtra("index",i);
                    // Clickmenu=true;
                    startActivity(intent);
                }else {
                    CheckinternetToat.alertchecktb(getContext(), "No have Network Connection", "Try again later");
                }


                //Toast.makeText(getApplicationContext(),,Toast.LENGTH_LONG).show();
            }
        });
        listviewUsermess.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                builder.setTitle("Confirm");
                builder.setMessage("Are you sure you want to delete the selected item?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(  CheckinternetToat.haveNetworkConnection(getContext())){
                            databaseUsermessMainreference.child(resultMessageSeenmodel.get(position).name).removeValue();
                            databaseUsermessMainreferenceMessagedelete.child(resultMessageSeenmodel.get(position).name).removeValue();
                            dialog.dismiss();
                        }else {
                            CheckinternetToat.alertchecktb(getContext(), "No have Network Connection", "Try again later");
                            dialog.dismiss();
                        }

                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });
                AlertDialog alert=builder.create();
                alert.show();
                return false;
            }
        });

    }
    private void updateList(){
        databaseUsermessMainreference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()) {

                    String checkCorrectAnswer = dataSnapshot.child("online").getValue(String.class);
                    if (checkCorrectAnswer.equals("offline")) {
                        resultMessageSeenmodel.add(dataSnapshot.getValue(MessageSeenModel.class));
                        messageSeenOfflineAdapter.notifyDataSetChanged();
                    }
                    if (resultMessageSeenmodel.size() == 0) {
                        lnnomessage.setVisibility(View.VISIBLE);
                        lnhavemessage.setVisibility(View.GONE);
                    } else {
                        lnnomessage.setVisibility(View.GONE);
                        lnhavemessage.setVisibility(View.VISIBLE);
                    }
                }



            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                MessageSeenModel messageSeenModel = dataSnapshot.getValue(MessageSeenModel.class);


                String checkCorrectAnswer = dataSnapshot.child("online").getValue(String.class);

                // String checkCorrectAnswerseen = dataSnapshot.child("seen").getValue(String.class);
                if (checkCorrectAnswer.equals("online")) {

                    if (resultMessageSeenmodel.size() > 0) {
                        int index1 = getItemIndex(messageSeenModel);
                        if (index1 > -1) {
                            resultMessageSeenmodel.remove(index1);
                            messageSeenOfflineAdapter.notifyDataSetChanged();
                        }
                    }
                }
                if (checkCorrectAnswer.equals("offline")) {
                    if (resultMessageSeenmodel.size() > 0) {
                        int index1 = getItemIndex(messageSeenModel);
                        if (index1 > -1) {
                            resultMessageSeenmodel.set(index1, messageSeenModel);
                            messageSeenOfflineAdapter.notifyDataSetChanged();
                        }
                    }


                }
                if(resultMessageSeenmodel.size()==0){
                    lnnomessage.setVisibility(View.VISIBLE);
                    lnhavemessage.setVisibility(View.GONE);
                }
                else{
                    lnnomessage.setVisibility(View.GONE);
                    lnhavemessage.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                MessageSeenModel messageSeenModel=dataSnapshot.getValue(MessageSeenModel.class);

                if(resultMessageSeenmodel.size()>0) {
                    int index = getItemIndex(messageSeenModel);
                    if(index>-1) {
                        resultMessageSeenmodel.remove(index);
                        messageSeenOfflineAdapter.notifyDataSetChanged();
                    }
                }
                if(resultMessageSeenmodel.size()==0){
                    lnnomessage.setVisibility(View.VISIBLE);
                    lnhavemessage.setVisibility(View.GONE);
                }
                else{
                    lnnomessage.setVisibility(View.GONE);
                    lnhavemessage.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private static int getItemIndex(MessageSeenModel usermess){
        int index=-1;
        for(int i=0;i<resultMessageSeenmodel.size();i++) {
            if(resultMessageSeenmodel.get(i).name.equals(usermess.name)){
                index=i;
                break;
            }
        }
        return index;

    }
}
