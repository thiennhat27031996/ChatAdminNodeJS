package com.techhub.chatadminnodejs;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.socketio.client.IO;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.techhub.chatadminnodejs.Adapter.ListUserMessageAdapter;
import com.techhub.chatadminnodejs.ClassUse.CheckinternetToat;
import com.techhub.chatadminnodejs.OBJ.Message;
import com.techhub.chatadminnodejs.OBJ.MessageSeen;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbarmhc;

     static ArrayList<MessageSeen> arrayListusermess;
    static ListUserMessageAdapter listUserMessageAdapter;

    NavigationView navigationView;
    ListView listViewmenumhc,lvphong;
    DrawerLayout drawerLayout;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_room=new ArrayList<>();
    private String name;
    private DatabaseReference root= FirebaseDatabase.getInstance().getReference().child("Message");
    private DatabaseReference messSeen=FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mUserDatabase;
    private DatabaseReference mdatasend;
    static boolean Clickmenu=false;



    private com.github.nkzawa.socketio.client.Socket mSocket;
    {
        try{
            mSocket= IO.socket("http://19444.168.56.1:3000");

        }catch (URISyntaxException e){}

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Anhxa();
        Actionbar();
        getTokenid();







    }

    private void getTokenid() {


        mdatasend=FirebaseDatabase.getInstance().getReference().child("notifications");

        String deviceToken= FirebaseInstanceId.getInstance().getToken();
        mUserDatabase=FirebaseDatabase.getInstance().getReference().child("DeviceAndroid");
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        final String current_user_id=currentFirebaseUser.getUid();

        mUserDatabase.child(current_user_id).child("device_token").setValue(deviceToken);
        mUserDatabase.child(current_user_id).child("user_id").setValue(current_user_id);





    }

    private void Actionbar() {
        setSupportActionBar(toolbarmhc);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarmhc.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbarmhc.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);

            }
        });
    }

    private void getMesslast(final DataSnapshot dataSnapshot1){
        messSeen.child("MessageSeen").child(dataSnapshot1.getKey()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


               // Toast.makeText(getApplicationContext(),dataSnapshot1.getKey(),Toast.LENGTH_LONG).show();
                Iterator i=dataSnapshot.getChildren().iterator();

                while(i.hasNext()){
                    String keyseen=(String)((DataSnapshot)i.next()).getValue();

                  String  chat_msg=(String)((DataSnapshot)i.next()).getValue();
                   String chat_user_name=(String)((DataSnapshot)i.next()).getValue();
                   String seen1=(String)((DataSnapshot)i.next()).getValue();



                  //Toast.makeText(getApplicationContext(),chat_msg + ":" +chat_user_name+ ":"+seen1,Toast.LENGTH_LONG).show();
                    for(int j=0;j<arrayListusermess.size();j++) {
                        if (seen1.equals("true") && arrayListusermess.get(j).getTenUser().equals(dataSnapshot1.getKey())) {
                            arrayListusermess.get(j).setLastMess(chat_msg);
                            arrayListusermess.get(j).setSeen(true);
                        }
                       if( seen1.equals("false") && arrayListusermess.get(j).getTenUser().equals(dataSnapshot1.getKey())){
                            arrayListusermess.get(j).setLastMess(chat_msg);
                            arrayListusermess.get(j).setSeen(false);
                        }else{

                       }

                        listUserMessageAdapter.notifyDataSetChanged();
                    }

                }



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

    private void Anhxa() {

        toolbarmhc = (Toolbar) findViewById(R.id.toolbarmhc);


        //firebase


        listViewmenumhc = (ListView) findViewById(R.id.lvmenutrangchu);
        navigationView = (NavigationView) findViewById(R.id.navigationview);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        lvphong = (ListView) findViewById(R.id.lvmhc);

        arrayListusermess = new ArrayList<>();
        listUserMessageAdapter = new ListUserMessageAdapter(arrayListusermess, getApplicationContext());

        lvphong.setAdapter(listUserMessageAdapter);

        request_username();

        messSeen.child("MessageSeen").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //arrayListusermess.clear();

                arrayListusermess.add(new MessageSeen(dataSnapshot.getKey(),msg, true));
                listUserMessageAdapter.notifyDataSetChanged();

                //String tenuser=dataSnapshot.getKey();
                //Toast.makeText(getApplicationContext(),dataSnapshot.getChildren().toString(),Toast.LENGTH_LONG).show();


               getMesslast(dataSnapshot);








                 /*String  chat_msg1=(String)((DataSnapshot)i.next()).getValue();
                  String  chat_user_name1=(String)((DataSnapshot)i.next()).getValue();
                  String  seen=(String)((DataSnapshot)i.next()).getValue();;

                    if(seen.equals("true")){
                        arrayListusermess.add(new MessageSeen(dataSnapshot.getKey(),chat_msg1, true));
                    }else {
                        arrayListusermess.add(new MessageSeen(dataSnapshot.getKey(),chat_msg1, false));
                    }

                    listUserMessageAdapter.notifyDataSetChanged();*/




                // recyclerViewnhantin.scrollToPosition(messageAdapter.getItemCount()-1);






                //Toast.makeText(getApplicationContext(),dataSnapshot.getKey(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //arrayListusermess.clear();

               // arrayListusermess.add(new MessageSeen(dataSnapshot.getKey(),msg, true));
               // listUserMessageAdapter.notifyDataSetChanged();
               // Toast.makeText(getApplicationContext(),dataSnapshot.getKey(),Toast.LENGTH_LONG).show();
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


       /* messSeen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<String>();
                Iterator i = dataSnapshot.getChildren().iterator();
                arrayListusermess.clear();
               // Toast.makeText(getApplicationContext(),((DataSnapshot) i.next()).getKey(),Toast.LENGTH_LONG).show();

                while (i.hasNext()) {
                    messSeen.child(((DataSnapshot) i.next()).getKey()).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                           // onchildchange(dataSnapshot);
                            //onchild(dataSnapshot);
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                            onchild(dataSnapshot);
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


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });*/



        lvphong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                arrayListusermess.get(i).setSeen(true);
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra("from_user_id", arrayListusermess.get(i).getTenUser());
                intent.putExtra("user_name", name);
                intent.putExtra("index",i);
                Clickmenu=true;
                startActivity(intent);
                //Toast.makeText(getApplicationContext(),,Toast.LENGTH_LONG).show();
            }
        });






    }
    String lasmes,msg,namea,seen;

    private void  onchildchange(DataSnapshot dataSnapshot){
        Iterator i=dataSnapshot.getChildren().iterator();
        arrayListusermess.remove(dataSnapshot.getChildren());
        while(i.hasNext()){
            lasmes=(String)((DataSnapshot)i.next()).getValue();
            msg=(String)((DataSnapshot)i.next()).getValue();
            seen=(String)((DataSnapshot)i.next()).getValue();

            arrayListusermess.add(new MessageSeen(namea,msg, true));
            listUserMessageAdapter.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(),lasmes + ":" +msg+ ":"+seen,Toast.LENGTH_LONG).show();
        }

    }

    private void onchild(DataSnapshot dataSnapshot){
        Iterator i=dataSnapshot.getChildren().iterator();
        arrayListusermess.clear();
        while(i.hasNext()){

            namea=(String)((DataSnapshot)i.next()).getValue();
            seen=(String)((DataSnapshot)i.next()).getValue();
            lasmes=(String)((DataSnapshot)i.next()).getValue();
            arrayListusermess.add(new MessageSeen(namea,lasmes, true));
            listUserMessageAdapter.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(),lasmes +":"+seen+""+name,Toast.LENGTH_LONG).show();
        }
    }


    private void request_username() {
        name="Admin";
    }



}
