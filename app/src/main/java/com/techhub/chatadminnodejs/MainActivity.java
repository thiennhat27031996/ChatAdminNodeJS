package com.techhub.chatadminnodejs;

import android.content.DialogInterface;
import android.content.Intent;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.techhub.chatadminnodejs.Adapter.ListUserMessageAdapter;
import com.techhub.chatadminnodejs.OBJ.Message;

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

    ArrayList<Message> arrayListusermess;
    ListUserMessageAdapter listUserMessageAdapter;

    NavigationView navigationView;
    ListView listViewmenumhc,lvphong;
    DrawerLayout drawerLayout;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_room=new ArrayList<>();
    private String name;
    private DatabaseReference root= FirebaseDatabase.getInstance().getReference().child("Message");
    private DatabaseReference mUserDatabase;
    private DatabaseReference mdatasend;


    private com.github.nkzawa.socketio.client.Socket mSocket;
    {
        try{
            mSocket= IO.socket("http://192.168.56.1:3000");

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

    private void Anhxa() {

        toolbarmhc=(Toolbar)findViewById(R.id.toolbarmhc);


        //firebase



        listViewmenumhc=(ListView)findViewById(R.id.lvmenutrangchu);
        navigationView=(NavigationView)findViewById(R.id.navigationview);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawerlayout);
        lvphong=(ListView)findViewById(R.id.lvmhc);

        arrayListusermess=new ArrayList<>();
        listUserMessageAdapter=new ListUserMessageAdapter(arrayListusermess,getApplicationContext());

        lvphong.setAdapter(listUserMessageAdapter);

        request_username();


        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Set<String> set=new HashSet<String>();
                Iterator i=dataSnapshot.getChildren().iterator();
                arrayListusermess.clear();
                while(i.hasNext()){
                    arrayListusermess.add(new Message(((DataSnapshot)i.next()).getKey(),"",true));
                }

                listUserMessageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        lvphong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               Intent intent=new Intent(getApplicationContext(),ChatActivity.class);
                intent.putExtra("from_user_id",arrayListusermess.get(i).getTenUser());
                intent.putExtra("user_name",name);
                startActivity(intent);
                //Toast.makeText(getApplicationContext(),,Toast.LENGTH_LONG).show();
            }
        });
    }

    private void request_username() {
        name="Admin";
    }



}
