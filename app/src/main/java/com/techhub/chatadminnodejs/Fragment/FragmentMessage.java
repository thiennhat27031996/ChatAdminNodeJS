package com.techhub.chatadminnodejs.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.techhub.chatadminnodejs.Adapter.MessageSeenAdapter;
import com.techhub.chatadminnodejs.ChatActivity;
import com.techhub.chatadminnodejs.OBJ.MessageSeenModel;
import com.techhub.chatadminnodejs.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thiennhat on 17/10/2017.
 */

public class FragmentMessage extends Fragment {
    private ListView listviewUsermess;
    private static List<MessageSeenModel> resultMessageSeenmodel;
    private static MessageSeenAdapter messageSeenAdapter;
    private FirebaseDatabase databaseUsermessMain;
    private DatabaseReference databaseUsermessMainreference;
    private DatabaseReference databaseUsermessMainreferenceMessagedelete;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_message,container,false);
        Anhxa(view);
        updateList();
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void Anhxa(View view) {
        listviewUsermess=(ListView)view.findViewById(R.id.lvuser);
        resultMessageSeenmodel=new ArrayList<>();
        messageSeenAdapter=new MessageSeenAdapter(resultMessageSeenmodel,getContext());
        listviewUsermess.setAdapter(messageSeenAdapter);

        databaseUsermessMain=FirebaseDatabase.getInstance();
        databaseUsermessMainreference=databaseUsermessMain.getReference("OnlineMess");
        databaseUsermessMainreferenceMessagedelete=FirebaseDatabase.getInstance().getReference("MessageSeen");





        listviewUsermess.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                databaseUsermessMainreference.child(resultMessageSeenmodel.get(i).name).child("seen").setValue("true");
                Intent intent = new Intent(getContext(), ChatActivity.class);
                intent.putExtra("from_user_id",resultMessageSeenmodel.get(i).name);
                intent.putExtra("user_name", "Admin");
                intent.putExtra("index",i);
                // Clickmenu=true;
                startActivity(intent);
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
                        databaseUsermessMainreference.child(resultMessageSeenmodel.get(position).name).removeValue();
                        databaseUsermessMainreferenceMessagedelete.child(resultMessageSeenmodel.get(position).name).removeValue();
                        dialog.dismiss();
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

                 resultMessageSeenmodel.add(dataSnapshot.getValue(MessageSeenModel.class));
                 messageSeenAdapter.notifyDataSetChanged();


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                MessageSeenModel messageSeenModel=dataSnapshot.getValue(MessageSeenModel.class);


                if(resultMessageSeenmodel.size()>0) {
                    int index1 = getItemIndex(messageSeenModel);
                    resultMessageSeenmodel.set(index1, messageSeenModel);
                    messageSeenAdapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                MessageSeenModel messageSeenModel=dataSnapshot.getValue(MessageSeenModel.class);

                if(resultMessageSeenmodel.size()>0) {
                    int index = getItemIndex(messageSeenModel);
                    resultMessageSeenmodel.remove(index);
                    messageSeenAdapter.notifyDataSetChanged();
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
