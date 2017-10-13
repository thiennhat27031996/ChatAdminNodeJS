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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.techhub.chatadminnodejs.Adapter.ListUserMessageAdapter;
import com.techhub.chatadminnodejs.ClassUse.CheckinternetToat;
import com.techhub.chatadminnodejs.OBJ.Message;
import com.techhub.chatadminnodejs.OBJ.MessageSeen;

import org.jsoup.nodes.Comment;

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
    private DatabaseReference messSeen=FirebaseDatabase.getInstance().getReference().child("MessageSeen");
    private DatabaseReference mUserDatabase;
    private DatabaseReference rootunread=FirebaseDatabase.getInstance().getReference().child("UnreadMessage");
    private DatabaseReference mdatasend;
    static boolean Clickmenu=false;
    static boolean onstart=false;
    static boolean onstop=false;
    static boolean start=false;



    private com.github.nkzawa.socketio.client.Socket mSocket;
    {
        try{
            mSocket= IO.socket("http://19444.168.56.1:3000");

        }catch (URISyntaxException e){}

    }

    @Override
    protected void onStart() {
        super.onStart();
        onstart=true;
        onstop=false;

    }

    @Override
    protected void onStop() {

        super.onStop();
        onstop=true;
        onstart=false;


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        Anhxa();
        Actionbar();
        getTokenid();


        Firebase2();
        start=true;


//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);





    }





    private void dataFirebase2(final DataSnapshot dataSnapshot){



        arrayListusermess.add(new MessageSeen(dataSnapshot.getKey(),"",false));
        listUserMessageAdapter.notifyDataSetChanged();




                                                                                                                                                    //CheckinternetToat.toastcheckinternet(MainActivity.this,newComment.toString());




            }








            //messSeen.child(dataSnapshot.getKey()).orderByKey().limitToLast(1).toString();



















 private void Firebase2(){

     messSeen.addChildEventListener(new ChildEventListener() {
         @Override
         public void onChildAdded(DataSnapshot dataSnapshot, String s) {
             //thêm client mới là add
             // arrayListusermess.clear();
             dataFirebase2(dataSnapshot);
             //childDataSnapshot.child(--ENTER THE KEY NAME eg. firstname or email etc.--).getValue());   //gives the value for given keyname
             // }
             //  getMesslast(dataSnapshot);



         }

         @Override
         public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            // dataFirebase2(dataSnapshot);
             // arrayListusermess.clear();
             //dataFirebase2(dataSnapshot);


             //thêm tin nhắn mới là change



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






     rootunread.addChildEventListener(new ChildEventListener() {
         @Override
         public void onChildAdded(DataSnapshot dataSnapshot, String s) {

         }

         @Override
         public void onChildChanged(DataSnapshot dataSnapshot, String s) {

             for(int i=0;i<arrayListusermess.size();i++){
                 if(arrayListusermess.get(i).getTenUser().equals(dataSnapshot.getKey())){
                     final int vitri=i;
                    // CheckinternetToat.toastcheckinternet(MainActivity.this,dataSnapshot.getChildr);

                     rootunread.child(dataSnapshot.getKey()).addChildEventListener(new ChildEventListener() {
                         @Override
                         public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                         }

                         @Override
                         public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                             if(dataSnapshot.child("seen").getValue().toString().equals("true")){
                                 arrayListusermess.get(vitri).setSeen(true);
                             }else{
                                 arrayListusermess.get(vitri).setSeen(false);
                             }



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













                     for (final DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {


                     }

                 }
             }


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











     rootunread.addValueEventListener(new ValueEventListener() {
         @Override
         public void onDataChange(DataSnapshot dataSnapshot) {
             for (final DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                     rootunread.addChildEventListener(new ChildEventListener() {
                         @Override
                         public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                             for(int i = 0; i<arrayListusermess.size();i++) {

                                 if (arrayListusermess.get(i).getTenUser().equals(dataSnapshot.getKey())) {
                                     // CheckinternetToat.toastcheckinternet(MainActivity.this,dataSnapshot.getKey());
                                     final int vitri=i;
                                     rootunread.child(arrayListusermess.get(vitri).getTenUser()).orderByKey().limitToLast(1).addChildEventListener(new ChildEventListener() {
                                         @Override
                                         public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                                             if(onstart==true && onstop==false) {
                                                // rootunread.child(arrayListusermess.get(vitri).getTenUser()).child(dataSnapshot.getKey()).child("seen").setValue(false);
                                               //  arrayListusermess.get(vitri).setSeen(false);
                                                 //  CheckinternetToat.toastcheckinternet(MainActivity.this,dataSnapshot.child("lastmessage").getValue().toString());
                                                 if(dataSnapshot.child("seen").getValue().toString().equals("true")){
                                                     arrayListusermess.get(vitri).setSeen(true);
                                                 }
                                                 else{
                                                     arrayListusermess.get(vitri).setSeen(false);
                                                 }


                                                 arrayListusermess.get(vitri).setLastMess(dataSnapshot.child("lastmessage").getValue().toString());
                                                 listUserMessageAdapter.notifyDataSetChanged();


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


                             }





                         }

                         @Override
                         public void onChildChanged(final DataSnapshot dataSnapshot1, String s) {
                             for(int i = 0; i<arrayListusermess.size();i++) {
                                 if (arrayListusermess.get(i).getTenUser().equals(dataSnapshot1.getKey())) {
                                    // CheckinternetToat.toastcheckinternet(MainActivity.this,dataSnapshot.getKey());
                                    final int vitri=i;
                                     rootunread.child(arrayListusermess.get(vitri).getTenUser()).orderByKey().limitToLast(1).addChildEventListener(new ChildEventListener() {
                                         @Override
                                         public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                             if(onstart==true && onstop==false) {
                                                 //rootunread.child(arrayListusermess.get(vitri).getTenUser()).child(dataSnapshot.getKey()).child("seen").setValue(false);
                                                // arrayListusermess.get(vitri).setSeen(false);
                                                 //  CheckinternetToat.toastcheckinternet(MainActivity.this,dataSnapshot.child("lastmessage").getValue().toString());
                                                 arrayListusermess.get(vitri).setLastMess(dataSnapshot.child("lastmessage").getValue().toString());
                                                 listUserMessageAdapter.notifyDataSetChanged();
                                             }

                                         }

                                         @Override
                                         public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                                             if(onstart==true && onstop==false) {
                                                  rootunread.child(arrayListusermess.get(vitri).getTenUser()).child(dataSnapshot.getKey()).child("seen").setValue(false);
                                                 arrayListusermess.get(vitri).setSeen(false);
                                                 //  CheckinternetToat.toastcheckinternet(MainActivity.this,dataSnapshot.child("lastmessage").getValue().toString());
                                                 arrayListusermess.get(vitri).setLastMess(dataSnapshot.child("lastmessage").getValue().toString());
                                                 listUserMessageAdapter.notifyDataSetChanged();
                                             }

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
                         public void onChildRemoved(DataSnapshot dataSnapshot) {

                         }

                         @Override
                         public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                         }

                         @Override
                         public void onCancelled(DatabaseError databaseError) {

                         }
                     });//




















             }




         }

         @Override
         public void onCancelled(DatabaseError databaseError) {

         }
     });
















     lvphong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
             arrayListusermess.get(i).setSeen(true);
             Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
             intent.putExtra("from_user_id", arrayListusermess.get(i).getTenUser());
             intent.putExtra("user_name", name);
             intent.putExtra("index",i);
             startActivity(intent);
             //Toast.makeText(getApplicationContext(),,Toast.LENGTH_LONG).show();
         }
     });





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






    private void request_username() {
        name="Admin";
    }



}
