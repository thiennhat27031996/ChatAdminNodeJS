package com.techhub.chatadminnodejs;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.techhub.chatadminnodejs.Adapter.FragmentAdapter;
import com.techhub.chatadminnodejs.Pref.Userinfo;
import com.techhub.chatadminnodejs.Pref.Usersession;

import org.w3c.dom.Text;

import me.leolin.shortcutbadger.ShortcutBadger;

public class TrangChuActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;

    NavigationView navigationView;
    TabLayout tabLayout;
    private Usersession usersession;
    private Userinfo userinfo;
    TextView tvcardcout;
    Button btnlogout;


    private DatabaseReference mUserDatabase;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chu);
        Anhxa();
        getTokenid();
        Getcoutunreadmess();
        getTokenid();
    }

    private void online(){
    }
    private void offline(){
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
                tvcardcout.setText(String.valueOf(questionsolved));
                if(tvcardcout.getText().equals("0")){
                    tvcardcout.setVisibility(View.INVISIBLE);
                }
                else{
                    tvcardcout.setVisibility(View.VISIBLE);
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

    }

    private void Anhxa() {
        userinfo=new Userinfo(this);
        usersession=new Usersession(this);
        tvcardcout=(TextView)findViewById(R.id.tvcartnb);
        tvcardcout.setVisibility(View.INVISIBLE);


        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);

        final ViewPager viewPager=(ViewPager)findViewById(R.id.viewpagemhc);
        viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(),this));
        tabLayout =(TabLayout)findViewById(R.id.tablayoutmenu);
        tabLayout.setupWithViewPager(viewPager);
        btnlogout=(Button)findViewById(R.id.btnlogout);
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userinfo.clearUserinfo();
                usersession.setLoggedin(false);
                Intent intent=new Intent(TrangChuActivity.this,SplashAcivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
}
