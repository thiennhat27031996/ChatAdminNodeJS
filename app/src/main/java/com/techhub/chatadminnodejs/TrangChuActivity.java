package com.techhub.chatadminnodejs;

import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
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



        String deviceToken= FirebaseInstanceId.getInstance().getToken();
        mUserDatabase=FirebaseDatabase.getInstance().getReference().child("DeviceAndroid");
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        final String current_user_id=userinfo.getKeyUserid();

        mUserDatabase.child(current_user_id).child("device_token").setValue(deviceToken);
        mUserDatabase.child(current_user_id).child("user_id").setValue(current_user_id);





    }

    private void Anhxa() {
        userinfo=new Userinfo(this);
        tvcardcout=(TextView)findViewById(R.id.tvcartnb);
        tvcardcout.setVisibility(View.INVISIBLE);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);

        ViewPager viewPager=(ViewPager)findViewById(R.id.viewpagemhc);
        viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(),this));
        tabLayout =(TabLayout)findViewById(R.id.tablayoutmenu);
        tabLayout.setupWithViewPager(viewPager);

    }
}
