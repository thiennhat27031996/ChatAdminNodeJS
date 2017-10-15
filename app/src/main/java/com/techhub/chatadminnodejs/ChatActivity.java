package com.techhub.chatadminnodejs;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.engineio.client.Socket;
import com.github.nkzawa.socketio.client.IO;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techhub.chatadminnodejs.Adapter.ListUserMessageAdapter;
import com.techhub.chatadminnodejs.Adapter.MessageAdapter;
import com.techhub.chatadminnodejs.ClassUse.CheckinternetToat;
import com.techhub.chatadminnodejs.OBJ.Message;
import com.techhub.chatadminnodejs.OBJ.MessageSeen;
import com.techhub.chatadminnodejs.OBJ.MessageSeenModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lunainc.chatbar.ViewChatBar;
import org.w3c.dom.Text;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    Toolbar toolbarmhchat;
    RecyclerView recyclerViewnhantin;
    NavigationView navigationViewchat;
    ListView listViewmenutinnhan;
    DrawerLayout drawerLayoutchat;
    TextView tvbagecout;
   // EditText edtnoidungtinnhanjv;
   // Button btnguitinnhanjv;
    ViewChatBar chatbar;


    ArrayList<Message> mangchat;
    MessageAdapter messageAdapter;
    String adminuser="Admin";
    private String user_name,room_name,temp_key,temp_keyroot2,room_namesv;
    private DatabaseReference rootmessen;

    private FirebaseDatabase databaseUsermessChat;
    private DatabaseReference databaseUsermessChatreference;
    private FirebaseDatabase databaseUsermessMain;
    private DatabaseReference databaseUsermessChatreferenceMain;





    private com.github.nkzawa.socketio.client.Socket mSocket;
    {
        try{
            mSocket= IO.socket("http://192.13368.0.105:3000");

        }catch (URISyntaxException e){}

    }

    @Override
    protected void onStop() {
        super.onStop();
        offline();
        updateseen();



    }

    @Override
    protected void onStart() {
        super.onStart();
        updateseen();
        online();


    }
    private void online(){
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("DeviceAndroid");

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        final String current_user_id=currentFirebaseUser.getUid();

        ref1.child(current_user_id).child("online").setValue("true");



    }
    private void offline(){
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("DeviceAndroid");

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        final String current_user_id=currentFirebaseUser.getUid();

        ref1.child(current_user_id).child("online").setValue("false");



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        Anhxa();
        ConnectSocketio();
        Actionbar();
        Firebase2();


        Getcoutunreadmess();

    }

    private void Getcoutunreadmess() {

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("OnlineMess");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int questionsolved = 0;
                for(DataSnapshot snapShot   :dataSnapshot.getChildren()){

                    String checkCorrectAnswer = snapShot.child("seen").getValue(String.class);
                    String checkname=snapShot.child("name").getValue(String.class);
                    if (checkCorrectAnswer.equals("false") && !checkname.equals(room_name) ) {
                        questionsolved = questionsolved + 1;
                    }

                }
                tvbagecout.setText(String.valueOf(questionsolved));
                if(tvbagecout.getText().equals("0")){
                    tvbagecout.setVisibility(View.INVISIBLE);
                }
                else{
                    tvbagecout.setVisibility(View.VISIBLE);
                }
               // CheckinternetToat.toastcheckinternet(ChatActivity.this,String.valueOf(questionsolved));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void Firebase2(){

//set title
        room_name=getIntent().getExtras().get("from_user_id").toString();
        toolbarmhchat.setTitle(room_name);
        //lay data trong room
        rootmessen=FirebaseDatabase.getInstance().getReference().child("MessageSeen").child(room_name);
        //curent send mess
        final DatabaseReference db=FirebaseDatabase.getInstance().getReference().child("OnlineMess").child(room_name);
        chatbar.setSendClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //rootsenn
                Map<String,Object> map3=new HashMap<String,Object>();
                temp_keyroot2=rootmessen.push().getKey();
                rootmessen.updateChildren(map3);
                DatabaseReference message_rootseen=rootmessen.child(temp_keyroot2);
                Map<String,Object> map4=new HashMap<String,Object>();
                map4.put("lastmessage",chatbar.getMessageText());
                map4.put("name","Admin");
                map4.put("seen","true");
                map4.put("key",temp_keyroot2);
                map4.put("usermess","false");
                Map<String,Object> map5=new HashMap<String,Object>();
                map5.put("lastmessage",chatbar.getMessageText());
                map5.put("name",room_name);
                map5.put("seen","true");
                map5.put("key",temp_keyroot2);
                db.updateChildren(map5);
                message_rootseen.updateChildren(map4);

                chatbar.setClearMessage(true);
            }
        });


        databaseUsermessChat=FirebaseDatabase.getInstance();
        databaseUsermessChatreference=databaseUsermessChat.getReference("MessageSeen").child(room_name);

        databaseUsermessMain=FirebaseDatabase.getInstance();
        databaseUsermessChatreferenceMain=databaseUsermessMain.getReference("OnlineMess").child(room_name);


        updateList();









    }

    private void updateseen(){
        databaseUsermessChatreferenceMain.child("seen").setValue("true");
    }
    private void updateList(){
        databaseUsermessChatreference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //CheckinternetToat.toastcheckinternet(MainActivity.this,dataSnapshot.getValue(MessageSeenModel.class).toString());
                mangchat.add(dataSnapshot.getValue(Message.class));
                messageAdapter.notifyDataSetChanged();
                recyclerViewnhantin.scrollToPosition(mangchat.size()-1);


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                Message message=dataSnapshot.getValue(Message.class);

                int index =getItemIndex(message);
                mangchat.set(index,message);
                messageAdapter.notifyItemChanged(index);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                Message message=dataSnapshot.getValue(Message.class);

                int index =getItemIndex(message);
                mangchat.remove(index);
                messageAdapter.notifyItemRemoved(index);

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private int getItemIndex(Message usermess){
        int index=-1;
        for(int i=0;i<mangchat.size();i++) {
            if(mangchat.get(i).name.equals(usermess.name)){
                index=i;
                break;
            }
        }
        return index;

    }






















































    private void ConnectSocketio() {
       // mSocket.connect();

       // mSocket.on("svguiusn",onNew_dsusn);
        //mSocket.on("serverguichat",onNew_guitinchat);








    }








    private void Actionbar() {

        setSupportActionBar(toolbarmhchat);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarmhchat.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    finish();



            }
        });
    }
    @Override
    public void onBackPressed() {

        return;
    }

    private void Anhxa() {
        toolbarmhchat=(Toolbar)findViewById(R.id.toolbarmhchat);
        recyclerViewnhantin=(RecyclerView) findViewById(R.id.rclmainchat);
        listViewmenutinnhan=(ListView)findViewById(R.id.lvmenutinnhan);
        navigationViewchat=(NavigationView)findViewById(R.id.navigationviewchat);
        drawerLayoutchat=(DrawerLayout)findViewById(R.id.drawerlayoutchat);
        tvbagecout=(TextView)findViewById(R.id.tvcartnb) ;
        tvbagecout.setVisibility(View.INVISIBLE);

        recyclerViewnhantin.setHasFixedSize(true);
        recyclerViewnhantin.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));
        chatbar=(ViewChatBar) findViewById(R.id.chatbar);
        chatbar.setMessageBoxHint("Aa");
        user_name="Admin";




        mangchat=new ArrayList<>();
        messageAdapter=new MessageAdapter(mangchat,getApplicationContext());
        recyclerViewnhantin.setAdapter(messageAdapter);








    }
}
