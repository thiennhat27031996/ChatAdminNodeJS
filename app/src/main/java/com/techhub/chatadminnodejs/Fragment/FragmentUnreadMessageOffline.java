package com.techhub.chatadminnodejs.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.techhub.chatadminnodejs.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thiennhat on 19/10/2017.
 */

public class FragmentUnreadMessageOffline extends Fragment {
    private ListView listviewUserunreadmess;
    private List<MessageSeenModel> resultMessageSeenmodel;
    private MessageSeenOfflineAdapter messageSeenAdapter;
    private FirebaseDatabase databaseUsermessMain=FirebaseDatabase.getInstance();
    private DatabaseReference databaseUsermessMainreference=databaseUsermessMain.getReference("OnlineMess");;
    private LinearLayout lnhavemessage;
    private RelativeLayout lnnomessage;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_unreadmessageoffline,container,false);
        if(  CheckinternetToat.haveNetworkConnection(getContext())){
            Anhxa(view);
            updateList();
        }else {
            CheckinternetToat.alertchecktb(getContext(), "No have Network Connection", "Try again later");
        }
        return view;
    }
    private void Anhxa(View view) {
        listviewUserunreadmess=(ListView)view.findViewById(R.id.lvuserunreadoffline);
        lnhavemessage=(LinearLayout)view.findViewById(R.id.lnhavemessageunreadoffline);
        lnnomessage=(RelativeLayout)view.findViewById(R.id.lnnohavemessageunreadoffline);
        resultMessageSeenmodel=new ArrayList<>();
        messageSeenAdapter=new MessageSeenOfflineAdapter(resultMessageSeenmodel,getContext());
        listviewUserunreadmess.setAdapter(messageSeenAdapter);

        //databaseUsermessMain
        //databaseUsermessMainreference





        listviewUserunreadmess.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

    }
    private void updateList(){
        databaseUsermessMainreference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {



                String checkCorrectAnsweroffline = dataSnapshot.child("online").getValue(String.class);

                String checkCorrectAnswer = dataSnapshot.child("seen").getValue(String.class);
                if (checkCorrectAnswer.equals("false") && checkCorrectAnsweroffline.equals("offline")) {
                    //CheckinternetToat.toastcheckinternet(MainActivity.this,snapShot.getKey());
                    resultMessageSeenmodel.add(dataSnapshot.getValue(MessageSeenModel.class));
                    messageSeenAdapter.notifyDataSetChanged();
                }
                if(resultMessageSeenmodel.size()==0){
                    lnnomessage.setVisibility(View.VISIBLE);
                    lnhavemessage.setVisibility(View.GONE);
                }
                else{
                    lnnomessage.setVisibility(View.GONE);
                    lnhavemessage.setVisibility(View.VISIBLE);
                }


                //  }


                //CheckinternetToat.toastcheckinternet(MainActivity.this,dataSnapshot.getValue(MessageSeenModel.class).toString());

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                MessageSeenModel messageSeenModel=dataSnapshot.getValue(MessageSeenModel.class);
                String checkCorrectAnsweroffline = dataSnapshot.child("online").getValue(String.class);
                String checkCorrectAnswer = dataSnapshot.child("seen").getValue(String.class);
                if (checkCorrectAnswer.equals("false")&& checkCorrectAnsweroffline.equals("offline")) {
                    if(resultMessageSeenmodel.size()>0) {
                        int index1 = getItemIndex(messageSeenModel);
                        //   listviewUsermess.smoothScrollToPositionFromTop(0,index1);

                        if(index1>-1) {
                            resultMessageSeenmodel.set(index1, messageSeenModel);
                            messageSeenAdapter.notifyDataSetChanged();
                        }
                        else{
                            resultMessageSeenmodel.add(dataSnapshot.getValue(MessageSeenModel.class));
                            messageSeenAdapter.notifyDataSetChanged();
                        }
                        //  listviewUsermess.setSelection(index1);
                    }
                    if(resultMessageSeenmodel.size()==0){
                        resultMessageSeenmodel.add(dataSnapshot.getValue(MessageSeenModel.class));
                        messageSeenAdapter.notifyDataSetChanged();
                    }
                }
                else{
                    if(resultMessageSeenmodel.size()>0) {

                        int index = getItemIndex(messageSeenModel);
                        if(index>-1) {
                            resultMessageSeenmodel.remove(index);
                            messageSeenAdapter.notifyDataSetChanged();
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
                        messageSeenAdapter.notifyDataSetChanged();
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
    private int getItemIndex(MessageSeenModel usermess){
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
