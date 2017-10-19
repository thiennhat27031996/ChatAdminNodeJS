package com.techhub.chatadminnodejs;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techhub.chatadminnodejs.Adapter.FragmentAdapter;
import com.techhub.chatadminnodejs.Adapter.FragmentofflineAdapter;
import com.techhub.chatadminnodejs.ClassUse.CheckinternetToat;
import com.techhub.chatadminnodejs.Pref.Userinfo;
import com.techhub.chatadminnodejs.Pref.Usersession;

import me.leolin.shortcutbadger.ShortcutBadger;

public class TrangChuOfflineActivity extends AppCompatActivity {




        DrawerLayout drawerLayout;

        NavigationView navigationView;
        TabLayout tabLayout;
        private Usersession usersession;
        private Userinfo userinfo;
        TextView tvcardcout,tvselectonline,tvcardcoutfloatbuttonoffline;
        Button btnlogout;
        FloatingActionButton floatingActionButtononline;



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
            setContentView(R.layout.activity_trang_chu_offline);
            Anhxa();
            if(  CheckinternetToat.haveNetworkConnection(TrangChuOfflineActivity.this)){
            getTokenid();
            Getcoutunreadmess();
            }else {
                CheckinternetToat.alertchecktb(TrangChuOfflineActivity.this, "No have Network Connection", "Try again later");
            }


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
                    int questionsolvedoffline=0;
                    int questionsolvedfloatoffline=0;
                    if(  CheckinternetToat.haveNetworkConnection(TrangChuOfflineActivity.this)){
                        for(DataSnapshot snapShot   :dataSnapshot.getChildren()){

                            String checkCorrectAnswer = snapShot.child("seen").getValue(String.class);
                            String checkCorrectAnsweoffline=snapShot.child("online").getValue(String.class);
                            if(checkCorrectAnswer.equals("false")){
                                questionsolved = questionsolved + 1;
                            }
                            if (checkCorrectAnswer.equals("false") && checkCorrectAnsweoffline.equals("offline")) {
                                questionsolvedoffline=questionsolvedoffline+1;
                            }
                            if (checkCorrectAnswer.equals("false") && checkCorrectAnsweoffline.equals("online")) {
                                questionsolvedfloatoffline=questionsolvedfloatoffline+1;
                            }


                        }
                        tvcardcoutfloatbuttonoffline.setText(String.valueOf(questionsolvedfloatoffline));
                        if(tvcardcoutfloatbuttonoffline.getText().equals("0")){
                            tvcardcoutfloatbuttonoffline.setVisibility(View.GONE);
                        }
                        else{
                            tvcardcoutfloatbuttonoffline.setVisibility(View.VISIBLE);
                        }
                        tvcardcout.setText(String.valueOf(questionsolvedoffline));
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
                    }else {
                        CheckinternetToat.alertchecktb(TrangChuOfflineActivity.this, "No have Network Connection", "Try again later");
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
            tvcardcout=(TextView)findViewById(R.id.tvcartnboffline);
            tvcardcout.setVisibility(View.INVISIBLE);
            tvselectonline=(TextView)findViewById(R.id.tvselectonlineoffline);
            floatingActionButtononline=(FloatingActionButton)findViewById(R.id.floatingbuttonselectoffline);
            tvselectonline.setText("offline");
            tvcardcoutfloatbuttonoffline=(TextView)findViewById(R.id.tvcartnbfloatbuttonoffline);
            tvcardcoutfloatbuttonoffline.setVisibility(View.GONE);
            //drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
            ViewPager viewPager=(ViewPager)findViewById(R.id.viewpagemhcoffline);

            viewPager.setAdapter(new FragmentofflineAdapter(getSupportFragmentManager(),this));




            tabLayout =(TabLayout)findViewById(R.id.tablayoutmenuoffline);
            tabLayout.setupWithViewPager(viewPager);
           /* btnlogout=(Button)findViewById(R.id.btnlogout);
            btnlogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userinfo.clearUserinfo();
                    usersession.setLoggedin(false);
                    Intent intent=new Intent(com.techhub.chatadminnodejs.TrangChuActivity.this,SplashAcivity.class);
                    startActivity(intent);
                    finish();
                }
            });*/

            floatingActionButtononline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(  CheckinternetToat.haveNetworkConnection(TrangChuOfflineActivity.this)){
                        Intent intent=new Intent(TrangChuOfflineActivity.this,TrangChuActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_left);
                        finish();
                    }else {
                        CheckinternetToat.alertchecktb(TrangChuOfflineActivity.this, "No have Network Connection", "Try again later");
                    }



                }
            });



        }
    }

