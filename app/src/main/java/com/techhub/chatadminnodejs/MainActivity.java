package com.techhub.chatadminnodejs;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.socketio.client.IO;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.techhub.chatadminnodejs.Adapter.ListUserMessageAdapter;
import com.techhub.chatadminnodejs.Adapter.MessageSeenAdapter;
import com.techhub.chatadminnodejs.ClassUse.CheckinternetToat;
import com.techhub.chatadminnodejs.OBJ.Message;
import com.techhub.chatadminnodejs.OBJ.MessageSeen;
import com.techhub.chatadminnodejs.OBJ.MessageSeenModel;

import org.jsoup.nodes.Comment;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import me.leolin.shortcutbadger.ShortcutBadger;

import static android.R.string.yes;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbarmhc;

     static ArrayList<MessageSeen> arrayListusermess;
    static ListUserMessageAdapter listUserMessageAdapter;

    NavigationView navigationView;
    ListView listViewmenumhc,lvphong;
    DrawerLayout drawerLayout;
private ListView listviewUsermess;
    private List<MessageSeenModel> resultMessageSeenmodel;
    private MessageSeenAdapter messageSeenAdapter;

    private String name;

    private DatabaseReference mUserDatabase;
    private DatabaseReference rootunread=FirebaseDatabase.getInstance().getReference().child("UnreadMessage");
    private DatabaseReference mdatasend;
    private FirebaseDatabase databaseUsermessMain;
    private DatabaseReference databaseUsermessMainreference;
    static boolean Clickmenu=false;
    static boolean onstop=false;


// during onCreate:




    private com.github.nkzawa.socketio.client.Socket mSocket;
    {
        try{
            mSocket= IO.socket("http://19444.168.56.1:3000");

        }catch (URISyntaxException e){}

    }

    @Override
    protected void onStop() {
        super.onStop();
        offline();

    }

    @Override
    protected void onStart() {
        super.onStart();
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
        setContentView(R.layout.activity_main);




        Anhxa();
        Actionbar();
        getTokenid();

        updateList();








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
                    if (checkCorrectAnswer.equals("false")) {
                        questionsolved = questionsolved + 1;
                    }


                }
                ShortcutBadger.applyCount(getApplicationContext(), questionsolved);
                if(questionsolved==0){
                    ShortcutBadger.removeCount(getApplicationContext());//for 1.1.4+
                }

                // CheckinternetToat.toastcheckinternet(ChatActivity.this,String.valueOf(questionsolved));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
        listViewmenumhc = (ListView) findViewById(R.id.lvmenutrangchu);
        navigationView = (NavigationView) findViewById(R.id.navigationview);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);



        resultMessageSeenmodel=new ArrayList<>();
        listviewUsermess=(ListView) findViewById(R.id.rcvuser);
        //recyclerViewuser.setHasFixedSize(true);
       // LinearLayoutManager llm=new LinearLayoutManager(this);
       // llm.setOrientation(LinearLayoutManager.VERTICAL);

       // recyclerViewuser.setLayoutManager(llm);
        messageSeenAdapter=new MessageSeenAdapter(resultMessageSeenmodel,getApplicationContext());
        listviewUsermess.setAdapter(messageSeenAdapter);

        ///firebase
        databaseUsermessMain=FirebaseDatabase.getInstance();
        databaseUsermessMainreference=databaseUsermessMain.getReference("OnlineMess");





        listviewUsermess.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                databaseUsermessMainreference.child(resultMessageSeenmodel.get(i).name).child("seen").setValue("true");
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra("from_user_id",resultMessageSeenmodel.get(i).name);
                intent.putExtra("user_name", "Admin");
                intent.putExtra("index",i);
               // Clickmenu=true;
                startActivity(intent);
                //Toast.makeText(getApplicationContext(),,Toast.LENGTH_LONG).show();
            }
        });


    }


    private void updateList(){
        databaseUsermessMainreference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
               //CheckinternetToat.toastcheckinternet(MainActivity.this,dataSnapshot.getValue(MessageSeenModel.class).toString());
               resultMessageSeenmodel.add(dataSnapshot.getValue(MessageSeenModel.class));
                messageSeenAdapter.notifyDataSetChanged();





            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {


                MessageSeenModel messageSeenModel=dataSnapshot.getValue(MessageSeenModel.class);


                if(resultMessageSeenmodel.size()>0) {
                    int index1 = getItemIndex(messageSeenModel);
                    //   listviewUsermess.smoothScrollToPositionFromTop(0,index1);

                    resultMessageSeenmodel.set(index1, messageSeenModel);
                    messageSeenAdapter.notifyDataSetChanged();
                    //  listviewUsermess.setSelection(index1);
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
