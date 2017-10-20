package com.techhub.chatadminnodejs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TimeFormatException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.nkzawa.socketio.client.IO;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.techhub.chatadminnodejs.Adapter.MessageAdapter;
import com.techhub.chatadminnodejs.ClassUse.CheckinternetToat;
import com.techhub.chatadminnodejs.OBJ.Message;
import com.techhub.chatadminnodejs.Pref.Userinfo;
import com.techhub.chatadminnodejs.Pref.Usersession;

import org.lunainc.chatbar.ViewChatBar;

import java.io.IOException;
import java.net.URISyntaxException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class ChatActivity extends AppCompatActivity {
    Toolbar toolbarmhchat;
    RecyclerView recyclerViewnhantin;
    NavigationView navigationViewchat;
    ListView listViewmenutinnhan;
    DrawerLayout drawerLayoutchat;
    TextView tvbagecout,toolbartitle,toolbaronline;
    ImageView icononlinechat;
   // EditText edtnoidungtinnhanjv;
   // Button btnguitinnhanjv;
    ViewChatBar chatbar;
    ImageButton btnthemanh;
    SwipeRefreshLayout swipeRefreshLayout;
    Userinfo userinfo;
    Usersession usersession;


    ArrayList<Message> mangchat;
    MessageAdapter messageAdapter;
    String adminuser="Admin";
    private String user_name,room_name,temp_key,temp_keyroot2,room_namesv;
    private DatabaseReference rootmessen;

    private FirebaseDatabase databaseUsermessChat;
    private DatabaseReference databaseUsermessChatreference;
    private FirebaseDatabase databaseUsermessMain;
    private DatabaseReference databaseUsermessChatreferenceMain;
    private FirebaseStorage mstorage;
    private StorageReference mStoragethemanh;
    private DatabaseReference databaseUsermessMainreference;
    private DatabaseReference databaseUsermessMainreferenceMessagedelete;
    private Uri filepath;
    private boolean delete=false;
    private static final    int GALLERY_INTENT=2;
    private static final int TOTAL_ITEM_LOAD=10;
    private int mCurrentpage=1;
    private int itemPos=0;
    private String mLastKey="";
    private String mPrevKey="";
    private boolean clickxoa=false;


    private com.github.nkzawa.socketio.client.Socket mSocket;
    {
        try{
            mSocket= IO.socket("http://192.13368.0.105:3000");

        }catch (URISyntaxException e){}

    }

    @Override
    protected void onStop() {
        super.onStop();
        updateseen();





    }

    @Override
    protected void onStart() {
        super.onStart();
        updateseen();



    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);



        Actionbar();
        Anhxa();
        if(  CheckinternetToat.haveNetworkConnection(ChatActivity.this)){
            ConnectSocketio();
            Actionbar();
            Firebase2();
            SendImagefirebase();
            Getcoutunreadmess();
        }else {
            CheckinternetToat.alertchecktb(ChatActivity.this, "No have Network Connection", "Try again later");
        }



    }

    private void SendImagefirebase() {
        mstorage=FirebaseStorage.getInstance();
        mStoragethemanh= mstorage.getReference();

        btnthemanh.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,"Chon Hinh"),GALLERY_INTENT);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_INTENT && resultCode == RESULT_OK && data != null && data.getData() !=null){
            //CheckinternetToat.toastcheckinternet(ChatActivity.this,"successvao");

            room_name=getIntent().getExtras().get("from_user_id").toString();
             filepath=data.getData();
            rootmessen=FirebaseDatabase.getInstance().getReference().child(userinfo.getKeyUserid()).child("MessageSeen").child(room_name);
            //curent send mess
            final DatabaseReference db=FirebaseDatabase.getInstance().getReference().child(userinfo.getKeyUserid()).child("OnlineMess").child(room_name);


            try{
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),filepath);
                if(filepath!=null){
                    final ProgressDialog progressDialog= new ProgressDialog(this);
                    progressDialog.setTitle("Uploading...");
                    progressDialog.show();

                    StorageReference ref=mStoragethemanh.child(userinfo.getKeyUserid()).child("MessageSeen").child(room_name+"/"+ UUID.randomUUID().toString());
                    ref.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            String url=taskSnapshot.getDownloadUrl().toString();

                            Date d=new Date();
                            SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
                            String time=sdf.format(d);




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
                            map4.put("url",url);
                            map4.put("time",time);
                            Map<String,Object> map5=new HashMap<String,Object>();
                            map5.put("lastmessage","[Image]");
                            map5.put("name",room_name);
                            map5.put("seen","true");
                            map5.put("key",temp_keyroot2);
                            db.updateChildren(map5);
                            message_rootseen.updateChildren(map4);

                        }
                    }) .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            CheckinternetToat.toastcheckinternet(ChatActivity.this,"Problem!,try again later");
                            progressDialog.dismiss();

                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progess=(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progess+"%");
                        }
                    });

                }
                //anh da chose xog tai day

            }
            catch (IOException e){
                e.printStackTrace();
            }






        }
    }

    private void Getcoutunreadmess() {

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(userinfo.getKeyUserid()).child("OnlineMess");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int questionsolved = 0;
                    int a;
                    for (DataSnapshot snapShot : dataSnapshot.getChildren()) {

                        String checkCorrectAnswer = snapShot.child("seen").getValue(String.class);
                        String checkname = snapShot.child("name").getValue(String.class);
                        if (checkCorrectAnswer.equals("false") && !checkname.equals(room_name)) {

                            questionsolved = questionsolved + 1;

                        }
                        //  a=questionsolved;

                    }

                    //    CheckinternetToat.toastcheckinternet(ChatActivity.this,"a:"+a +"b:"+questionsolved);
                    tvbagecout.setText(String.valueOf(questionsolved));
                    if (tvbagecout.getText().equals("0")) {
                        tvbagecout.setVisibility(View.INVISIBLE);
                    } else {
                        tvbagecout.setVisibility(View.VISIBLE);
                    }
                    // CheckinternetToat.toastcheckinternet(ChatActivity.this,String.valueOf(questionsolved));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void Firebase2(){

//set title
        room_name=getIntent().getExtras().get("from_user_id").toString();
        toolbartitle.setText(room_name);
       // toolbarmhchat.setTitle(room_name);
        //lay data trong room
        rootmessen=FirebaseDatabase.getInstance().getReference().child(userinfo.getKeyUserid()).child("MessageSeen").child(room_name);
        //curent send mess
        final DatabaseReference db=FirebaseDatabase.getInstance().getReference().child(userinfo.getKeyUserid()).child("OnlineMess").child(room_name);

        chatbar.setSendClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(  CheckinternetToat.haveNetworkConnection(ChatActivity.this)){
                    Date d=new Date();
                    SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
                    String time=sdf.format(d);
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
                    map4.put("url","null");
                    map4.put("time",time);
                    Map<String,Object> map5=new HashMap<String,Object>();
                    map5.put("lastmessage",chatbar.getMessageText());
                    map5.put("name",room_name);
                    map5.put("seen","true");
                    map5.put("key",temp_keyroot2);
                    db.updateChildren(map5);
                    message_rootseen.updateChildren(map4);

                    chatbar.setClearMessage(true);
                }else {
                    CheckinternetToat.alertchecktb(ChatActivity.this, "No have Network Connection", "Try again later");
                }

            }
        });


        databaseUsermessChat=FirebaseDatabase.getInstance();
        databaseUsermessChatreference=databaseUsermessChat.getReference(userinfo.getKeyUserid()).child("MessageSeen").child(room_name);

        databaseUsermessMain=FirebaseDatabase.getInstance();
        databaseUsermessChatreferenceMain=databaseUsermessMain.getReference(userinfo.getKeyUserid()).child("OnlineMess").child(room_name);


        updateList();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if(  CheckinternetToat.haveNetworkConnection(ChatActivity.this)){
                    mCurrentpage++;
                    itemPos=0;
                    updateMoreList();
                }else {
                    CheckinternetToat.alertchecktb(ChatActivity.this, "No have Network Connection", "Try again later");
                }

            }
        });






        getOnlinecheck();






    }

    private void getOnlinecheck() {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(userinfo.getKeyUserid()).child("OnlineMess").child(room_name);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               // dataSnapshot.child()
                if(dataSnapshot.exists()) {
                    String online = dataSnapshot.child("online").getValue().toString();
                    toolbaronline.setText(online);
                    if(online.equals("online")){
                        icononlinechat.setVisibility(View.VISIBLE);
                    }else{
                       icononlinechat.setVisibility(View.GONE);
                    }
                }
                 //CheckinternetToat.toastcheckinternet(ChatActivity.this,dataSnapshot.child("online").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateMoreList() {
        Query databaseUsermessChatreferencelast=databaseUsermessChatreference.orderByKey().endAt(mLastKey).limitToLast(10);
        databaseUsermessChatreferencelast.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String messageKey=dataSnapshot.getKey();


                if(!mPrevKey.equals(messageKey) && !mLastKey.equals(messageKey)){
                    mangchat.add(itemPos++,dataSnapshot.getValue(Message.class));
                }else{
                    mPrevKey=mLastKey;
                }
                if(itemPos==1){

                    mLastKey=messageKey;
                }
                if(itemPos==0){
                    CheckinternetToat.toastcheckinternet(ChatActivity.this,"No more Message");
                }

               // Log.d("ToTalkey","Last key:"+mLastKey+"| prevkey :"+mPrevKey+"|message key:"+messageKey +"|item pos " +itemPos);





                messageAdapter.notifyDataSetChanged();
               // recyclerViewnhantin.scrollToPosition(mangchat.size()-1);

                swipeRefreshLayout.setRefreshing(false);
               // CheckinternetToat.toastcheckinternet(getApplicationContext(),mLastKey + mPrevKey + itemPos);

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

    private void updateseen(){
        if(delete==false) {
            databaseUsermessChatreferenceMain.child("seen").setValue("true");
        }
    }
    private void updateList(){
        Query databaseUsermessChatreferencelast=databaseUsermessChatreference.limitToLast(mCurrentpage * TOTAL_ITEM_LOAD);
        databaseUsermessChatreferencelast.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                itemPos++;
                if(itemPos==1){
                    String messageKey=dataSnapshot.getKey();
                    mLastKey=messageKey;
                    mPrevKey=messageKey;
                }
                mangchat.add(dataSnapshot.getValue(Message.class));

                messageAdapter.notifyDataSetChanged();
                recyclerViewnhantin.scrollToPosition(mangchat.size()-1);

                swipeRefreshLayout.setRefreshing(false);

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
            if(mangchat.get(i).key.equals(usermess.key)){
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menuxoachat,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuxoa:
                if(  CheckinternetToat.haveNetworkConnection(ChatActivity.this)){
                    delete=false;
                    AlertDialog.Builder builder=new AlertDialog.Builder(ChatActivity.this);
                    builder.setTitle("Confirm");
                    builder.setMessage("Are you sure you want to delete the selected item?");

                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, int which) {
                            if(  CheckinternetToat.haveNetworkConnection(ChatActivity.this)){
                                final ProgressDialog progressDialog= new ProgressDialog(ChatActivity.this);
                                progressDialog.setTitle("Delete...");
                                progressDialog.show();

                                databaseUsermessMainreferenceMessagedelete.child(room_name).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for(DataSnapshot child :dataSnapshot.getChildren()){
                                            String url=child.child("url").getValue().toString();



                                            if(!url.equals("null")){

                                                StorageReference ref=FirebaseStorage.getInstance().getReferenceFromUrl(url);

                                                ref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                    }
                                                });
                                                //delete=delete+1;
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        //Do something after 100ms

                                        delete=true;
                                        progressDialog.dismiss();
                                        databaseUsermessMainreference.child(room_name).removeValue();
                                        databaseUsermessMainreferenceMessagedelete.child(room_name).removeValue();

                                        finish();

                                    }
                                }, 3000);
                                dialog.dismiss();
                            }else {
                                CheckinternetToat.alertchecktb(ChatActivity.this, "No have Network Connection", "Try again later");
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
                }else {
                    CheckinternetToat.alertchecktb(ChatActivity.this, "No have Network Connection", "Try again later");
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void Actionbar() {


        toolbarmhchat=(Toolbar)findViewById(R.id.toolbarmhchat);
        toolbarmhchat.setTitle("");
        setSupportActionBar(toolbarmhchat);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater inflater=(LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View action_bar_view=inflater.inflate(R.layout.chat_custombar,null);
        actionBar.setCustomView(action_bar_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarmhchat.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(  CheckinternetToat.haveNetworkConnection(ChatActivity.this)){
                    startActivity(new Intent(ChatActivity.this,TrangChuActivity.class));
                    finish();
                    overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
                }else {
                    CheckinternetToat.alertchecktb(ChatActivity.this, "No have Network Connection", "Try again later");
                }




            }
        });
    }
    @Override
    public void onBackPressed() {

        finish();
        overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
        return;
    }

    private void Anhxa() {

        userinfo=new Userinfo(this);
        recyclerViewnhantin=(RecyclerView) findViewById(R.id.rclmainchat);
        listViewmenutinnhan=(ListView)findViewById(R.id.lvmenutinnhan);
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.refreshlayout);
        navigationViewchat=(NavigationView)findViewById(R.id.navigationviewchat);
        drawerLayoutchat=(DrawerLayout)findViewById(R.id.drawerlayoutchat);
        tvbagecout=(TextView)findViewById(R.id.tvcartnb) ;
        tvbagecout.setVisibility(View.INVISIBLE);
        btnthemanh=(ImageButton)findViewById(R.id.btnthemanh);
        toolbartitle=(TextView)findViewById(R.id.tvtoolbartitle);
        toolbaronline=(TextView)findViewById(R.id.tvtoolbaronline);
        icononlinechat=(ImageView) findViewById(R.id.icononlinechat);
        icononlinechat.setImageResource(R.drawable.onlineicon);
        icononlinechat.setVisibility(View.GONE);
        recyclerViewnhantin.setHasFixedSize(true);
        recyclerViewnhantin.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));
        chatbar=(ViewChatBar) findViewById(R.id.chatbar);
        chatbar.setMessageBoxHint("Aa");
        user_name="Admin";




        mangchat=new ArrayList<>();
        messageAdapter=new MessageAdapter(mangchat,getApplicationContext());
        recyclerViewnhantin.setAdapter(messageAdapter);


        databaseUsermessMain=FirebaseDatabase.getInstance();
        databaseUsermessMainreference=databaseUsermessMain.getReference(userinfo.getKeyUserid()).child("OnlineMess");
        databaseUsermessMainreferenceMessagedelete=FirebaseDatabase.getInstance().getReference(userinfo.getKeyUserid()).child("MessageSeen");







    }
}
