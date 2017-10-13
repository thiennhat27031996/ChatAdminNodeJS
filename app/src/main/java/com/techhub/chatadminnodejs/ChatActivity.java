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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lunainc.chatbar.ViewChatBar;

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
   // EditText edtnoidungtinnhanjv;
   // Button btnguitinnhanjv;
    ViewChatBar chatbar;



    ArrayList<String> mangusername;
    ArrayList<Message> mangchat;
    MessageAdapter messageAdapter;
    String adminuser="Admin";
    private String user_name,room_name,temp_key,temp_keyroot2,room_namesv;
    private DatabaseReference root;
    private DatabaseReference rootmessen;
    private DatabaseReference rootunread;
    private DatabaseReference online;
    private DatabaseReference mdatasend;
    private DatabaseReference mdoiseen;
    static boolean chatactivitytofiticlick=false;
    static boolean onstop=false;
    static boolean onstart=false;
    static String chatactivitinottyfi;
    private int index=0;

    private com.github.nkzawa.socketio.client.Socket mSocket;
    {
        try{
            mSocket= IO.socket("http://192.13368.0.105:3000");

        }catch (URISyntaxException e){}

    }

    @Override
    protected void onStop() {
        super.onStop();
        onstop=true;
        onstart=false;
        Seenmethod();

    }

    @Override
    protected void onStart() {
        super.onStart();
        onstart=true;
        onstop=false;
        Seenmethod();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        Anhxa();
       // Firebase();
        ConnectSocketio();
        Actionbar();
        Firebase2();

    }





    private void Firebase2(){
//set title
        room_name=getIntent().getExtras().get("from_user_id").toString();
        toolbarmhchat.setTitle(room_name);
        //lay data trong room
        rootmessen=FirebaseDatabase.getInstance().getReference().child("MessageSeen").child(toolbarmhchat.getTitle().toString());
        rootunread=FirebaseDatabase.getInstance().getReference().child("UnreadMessage").child(toolbarmhchat.getTitle().toString());
       // online=FirebaseDatabase.getInstance().getReference().child("Client").child(room_name);

        //curent send mess
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
                message_rootseen.updateChildren(map4);

                chatbar.setClearMessage(true);
                //  Toast.makeText(getApplicationContext(),temp_key,Toast.LENGTH_LONG).show();
            }
        });





        rootmessen.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                append_chat_converseen2(dataSnapshot);

                unread(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                append_chat_converseen2(dataSnapshot);
                unread(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });







    }

    private void unread(final DataSnapshot dataSnapshot2) {
        if(onstart==true && onstop==false) {
            rootunread.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot1) {


                    for (DataSnapshot childDataSnapshot : dataSnapshot1.getChildren()) {

                        if (onstart == true && onstop == false && toolbarmhchat.getTitle().toString().equals(childDataSnapshot.child("name").getValue().toString())) {
                            rootunread.orderByKey().limitToLast(1).addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                                    //CheckinternetToat.toastcheckinternet(ChatActivity.this,dataSnapshot.child("lastmessage").getValue().toString());
                                    rootunread.child(dataSnapshot.getKey()).child("seen").setValue(true);

                                }

                                @Override
                                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                }

                                @Override
                                public void onChildRemoved(DataSnapshot dataSnapshot) {

                                }

                                @Override
                                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                        }
                        else{
                            rootunread.orderByKey().limitToLast(1).addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    //CheckinternetToat.toastcheckinternet(ChatActivity.this,dataSnapshot.child("lastmessage").getValue().toString());
                                    rootunread.child(dataSnapshot.getKey()).child("seen").setValue(false);

                                }

                                @Override
                                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                }

                                @Override
                                public void onChildRemoved(DataSnapshot dataSnapshot) {

                                }

                                @Override
                                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void append_chat_converseen2(DataSnapshot dataSnapshot){

        String tit=toolbarmhchat.getTitle().toString();
            //CheckinternetToat.toastcheckinternet(ChatActivity.this,dataSnapshot.child("name").getValue().toString());
            if(dataSnapshot.child("name").getValue().toString().equals("Admin")){
                Message message1=new Message("",dataSnapshot.child("lastmessage").getValue().toString(),true);
                mangchat.add(message1);
            }

            else{
                if(MainActivity.onstop==true){
                    for(int i=0;i<MainActivity.arrayListusermess.size();i++){
                        if(MainActivity.arrayListusermess.get(i).equals(tit)){
                            MainActivity.arrayListusermess.get(i).setLastMess(dataSnapshot.child("lastmessage").getValue().toString());
                          //  MainActivity.arrayListusermess.get(i).setSeen(false);
                            MainActivity.listUserMessageAdapter.notifyDataSetChanged();
                        }
                        else{

                        }
                    }
                   // CheckinternetToat.toastcheckinternet(ChatActivity.this,"true");
                }
                else{
                    //CheckinternetToat.toastcheckinternet(ChatActivity.this,"false");

                }
                Message message1=new Message("",dataSnapshot.child("lastmessage").getValue().toString(),false);
                mangchat.add(message1);

            }
            messageAdapter.notifyDataSetChanged();

            recyclerViewnhantin.scrollToPosition(messageAdapter.getItemCount()-1);




    }

    private void Seenmethod() {
        final String tool=toolbarmhchat.getTitle().toString();
        mdatasend=FirebaseDatabase.getInstance().getReference().child("OnlineMess");



                //CheckinternetToat.toastcheckinternet(ChatActivity.this,dataSnapshot.child(tool).child("online").getValue().toString());

                    if(onstop==true) {
                        mdatasend.child(toolbarmhchat.getTitle().toString()).child("online").setValue(false);
                    }else{
                        mdatasend.child(toolbarmhchat.getTitle().toString()).child("online").setValue(true);
                    }
                    //CIntent intent = new Intent(MainActivity.this, ChatActivity.class);
                    //startActivity(intent);
                    //finish();

            }











































    private String clientkey_push,client_user_id;
    private String client_online;
    private void checkonline(DataSnapshot dataSnapshot){
        Iterator i=dataSnapshot.getChildren().iterator();
        while(i.hasNext()){
            clientkey_push=(String)((DataSnapshot)i.next()).getValue();
            client_online=(String)((DataSnapshot)i.next()).getValue();
            client_user_id=(String)((DataSnapshot)i.next()).getValue();
            if(client_online !=null && client_online!="true"){
                Toast.makeText(getApplicationContext(),"user đã thoát",Toast.LENGTH_LONG).show();
            }
        }


    }












    private void ConnectSocketio() {
       // mSocket.connect();

       // mSocket.on("svguiusn",onNew_dsusn);
        //mSocket.on("serverguichat",onNew_guitinchat);








    }




    private Emitter.Listener onNew_guitinchat = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String noidung,id;

                    try {
                        noidung = data.getString("tinchat");
                        id=data.getString("idchat");
                        Toast.makeText(getApplicationContext(),noidung.toString()+id.toString(),Toast.LENGTH_LONG).show();

                        /*if(!noidung.toString().equals(edtnoidungtinnhanjv.getText().toString())){
                            Message message=new Message(id,noidung,false);
                            mangchat.add(message);

                        }
                        else{
                           // Message message1=new Message("Admin : ",edtnoidungtinnhanjv.getText().toString(),true);
                           // mangchat.add(message1);

                        }*/
                        messageAdapter.notifyDataSetChanged();

                      //  edtnoidungtinnhanjv.setText("");
                        recyclerViewnhantin.scrollToPosition(messageAdapter.getItemCount()-1);


                    } catch (JSONException e) {
                        return;
                    }
                }
            });
        }
    };


    private Emitter.Listener onNew_dsusn = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    JSONArray noidung;
                    mangusername.removeAll(mangusername);

                    try {
                        noidung = data.getJSONArray("danhsach");
                        Toast.makeText(getApplicationContext(),noidung.toString(),Toast.LENGTH_LONG).show();
                        for(int i=0;i<noidung.length();i++){
                                    mangusername.add(noidung.get(i).toString());
                            }
                        ArrayAdapter adapter=new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,mangusername);
                        listViewmenutinnhan.setAdapter(adapter);
                        adapter.notifyDataSetChanged();





                    } catch (JSONException e) {
                        return;
                    }
                }
            });
        }
    };






    private void Actionbar() {

        setSupportActionBar(toolbarmhchat);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarmhchat.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  //  finishAffinity();
                    chatactivitytofiticlick=true;
                    //Intent intent = new Intent(ChatActivity.this,MainActivity.class);
                   // startActivity(intent);
                    finish();



            }
        });
    }
    @Override
    public void onBackPressed() {
       // finishAffinity();
       //// chatactivitytofiticlick=true;
       // Intent intent = new Intent(ChatActivity.this,MainActivity.class);
      //  startActivity(intent);
        finish();
        // do something on back.
        return;
    }

    private void Anhxa() {
        toolbarmhchat=(Toolbar)findViewById(R.id.toolbarmhchat);
        recyclerViewnhantin=(RecyclerView) findViewById(R.id.rclmainchat);
        listViewmenutinnhan=(ListView)findViewById(R.id.lvmenutinnhan);
        navigationViewchat=(NavigationView)findViewById(R.id.navigationviewchat);
        drawerLayoutchat=(DrawerLayout)findViewById(R.id.drawerlayoutchat);

        recyclerViewnhantin.setHasFixedSize(true);
        recyclerViewnhantin.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));
        chatbar=(ViewChatBar) findViewById(R.id.chatbar);
        chatbar.setMessageBoxHint("Aa");
        user_name="Admin";



        mangchat=new ArrayList<>();
        messageAdapter=new MessageAdapter(mangchat,getApplicationContext());
        mangusername=new ArrayList<String>();
        recyclerViewnhantin.setAdapter(messageAdapter);








    }
}
