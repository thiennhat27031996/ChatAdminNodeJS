package com.techhub.chatadminnodejs;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.daasuu.bl.BubbleLayout;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.techhub.chatadminnodejs.Adapter.FragmentAdapter;
import com.techhub.chatadminnodejs.Adapter.FragmentofflineAdapter;
import com.techhub.chatadminnodejs.Adapter.ManuAdapter;
import com.techhub.chatadminnodejs.ClassUse.CheckinternetToat;
import com.techhub.chatadminnodejs.OBJ.Menu;
import com.techhub.chatadminnodejs.Pref.Userinfo;
import com.techhub.chatadminnodejs.Pref.Usersession;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import me.leolin.shortcutbadger.ShortcutBadger;

public class TrangChuActivity extends AppCompatActivity {
    static DrawerLayout drawerLayout;
    Toolbar toolbartrangchu;
    NavigationView navigationView;
    TabLayout tabLayout;
    ListView lvmenutrangchu;
    private Usersession usersession;
    private Userinfo userinfo;
    TextView tvcardcout,tvcoutmenutrangchu;
    private  List<Menu> listmenu;
    private  ManuAdapter manuAdapter;
    BubbleLayout bubbleLayout;





    private DatabaseReference mUserDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chu);
        Anhxa();
        if(  CheckinternetToat.haveNetworkConnection(TrangChuActivity.this)){
            getToolbarmenu();
            getTokenid();
            Getcoutunreadmess();
            getItemMenu();
        }else{
            CheckinternetToat.alertchecktb(TrangChuActivity.this,"No have Network Connection","Try again later");

        }


    }

    private void getItemMenu() {
        listmenu.add(0,new Menu("Offline User","0"));
        listmenu.add(1,new Menu("Help","0"));
        listmenu.add(2,new Menu("About","0"));
        listmenu.add(3,new Menu("Logout","0"));
        manuAdapter.notifyDataSetChanged();


        lvmenutrangchu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    if(  CheckinternetToat.haveNetworkConnection(TrangChuActivity.this)){
                        Intent intent=new Intent(TrangChuActivity.this,TrangChuOfflineActivity.class);
                        startActivity(intent);
                        final Handler handler=new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                drawerLayout.closeDrawer(GravityCompat.START);
                            }
                        },1500);

                        overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_left);
                    }else{
                        CheckinternetToat.alertchecktb(TrangChuActivity.this,"No have Network Connection","Try again later");

                    }
                }else if(position==1){

                }else if(position==2){

                }else if(position==3){
                    if(CheckinternetToat.haveNetworkConnection(TrangChuActivity.this)){

                        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(userinfo.getKeyUserid()).child("DeviceAndroid");
                        ref.removeValue();
                        userinfo.clearUserinfo();
                        usersession.setLoggedin(false);
                        Intent intent=new Intent(TrangChuActivity.this,SplashAcivity.class);
                        startActivity(intent);
                        final Handler handler=new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                drawerLayout.closeDrawer(GravityCompat.START);
                            }
                        },1500);
                        finish();
                    }
                    else{
                        CheckinternetToat.alertchecktb(TrangChuActivity.this,"No have Network Connection","Try again later");

                    }
                }
            }
        });


    }

    private void getToolbarmenu() {
        setSupportActionBar(toolbartrangchu);
        toolbartrangchu.setTitle("Online User");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbartrangchu.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbartrangchu.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void Getcoutunreadmess() {

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(userinfo.getKeyUserid()).child("OnlineMess");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int questionsolved = 0;
                    int questionsolvedonline = 0;
                    int questionsolvedfloat = 0;
                    if (CheckinternetToat.haveNetworkConnection(TrangChuActivity.this)) {
                        for (DataSnapshot snapShot : dataSnapshot.getChildren()) {
                            String checkCorrectAnswer = snapShot.child("seen").getValue(String.class);
                            String checkCorrectAnswerOnline = snapShot.child("online").getValue(String.class);
                            if (checkCorrectAnswer.equals("false")) {
                                questionsolved = questionsolved + 1;
                            }
                            if (checkCorrectAnswer.equals("false") && checkCorrectAnswerOnline.equals("online")) {
                                questionsolvedonline = questionsolvedonline + 1;
                            }
                            if (checkCorrectAnswer.equals("false") && checkCorrectAnswerOnline.equals("offline")) {
                                questionsolvedfloat = questionsolvedfloat + 1;
                            }
                        }
                        tvcardcout.setText(String.valueOf(questionsolvedonline));
                        tvcoutmenutrangchu.setText(String.valueOf(questionsolvedfloat));
                        if(tvcoutmenutrangchu.getText().equals("0")){
                            bubbleLayout.setVisibility(View.GONE);
                        }else{
                            bubbleLayout.setVisibility(View.VISIBLE);
                        }

                        listmenu.get(0).setNumbercart(String.valueOf(questionsolvedfloat));
                        manuAdapter.notifyDataSetChanged();
                        if (tvcardcout.getText().equals("0")) {
                            tvcardcout.setVisibility(View.INVISIBLE);
                        } else {
                            tvcardcout.setVisibility(View.VISIBLE);
                        }


                        ShortcutBadger.applyCount(getApplicationContext(), questionsolved);
                        if (questionsolved == 0) {
                            ShortcutBadger.removeCount(getApplicationContext());//for 1.1.4+
                        }

                    } else {
                        CheckinternetToat.alertchecktb(TrangChuActivity.this, "No have Network Connection", "Try again later");
                    }
                    // CheckinternetToat.toastcheckinternet(ChatActivity.this,String.valueOf(questionsolved));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void getTokenid() {

    }
    @Override
    public void onBackPressed() {

        final AlertDialog.Builder builder=new AlertDialog.Builder(TrangChuActivity.this);
        builder.setTitle("Confirm");
        builder.setMessage("Are you sure you want to delete the selected item?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                        finishAffinity();;
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
        return;
    }

    private void Anhxa() {
        userinfo=new Userinfo(this);
        usersession=new Usersession(this);
        toolbartrangchu=(Toolbar)findViewById(R.id.toolbartrangchu);
        toolbartrangchu.setTitle("");
        navigationView=(NavigationView)findViewById(R.id.navigationviewtrangchu);
        lvmenutrangchu=(ListView)findViewById(R.id.lvmenutrangchu);
        bubbleLayout=(BubbleLayout)findViewById(R.id.paddingtvcounttrangchu) ;
        tvcoutmenutrangchu=(TextView)findViewById(R.id.tvcoutitemtrangchumenu);




        tvcardcout=(TextView)findViewById(R.id.tvcartnb);
        tvcardcout.setVisibility(View.INVISIBLE);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        ViewPager viewPager=(ViewPager)findViewById(R.id.viewpagemhc);

        viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(),this));
        tabLayout =(TabLayout)findViewById(R.id.tablayoutmenu);
        tabLayout.setupWithViewPager(viewPager);


        listmenu=new ArrayList<>();
        manuAdapter=new ManuAdapter(listmenu,getApplicationContext());
        lvmenutrangchu.setAdapter(manuAdapter);












    }
}
