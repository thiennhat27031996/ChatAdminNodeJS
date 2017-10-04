package com.techhub.chatadminnodejs;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

public class ChatActivity extends AppCompatActivity {
    Toolbar toolbarmhchat;
    RecyclerView recyclerViewnhantin;
    NavigationView navigationViewchat;
    ListView listViewmenutinnhan;
    DrawerLayout drawerLayoutchat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Anhxa();
        Actionbar();
    }


    private void Actionbar() {
        setSupportActionBar(toolbarmhchat);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarmhchat.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbarmhchat.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayoutchat.openDrawer(GravityCompat.START);

            }
        });
    }

    private void Anhxa() {
        toolbarmhchat=(Toolbar)findViewById(R.id.toolbarmhchat);
        recyclerViewnhantin=(RecyclerView)findViewById(R.id.rclmainchat);
        listViewmenutinnhan=(ListView)findViewById(R.id.lvmenutinnhan);
        navigationViewchat=(NavigationView)findViewById(R.id.navigationviewchat);
        drawerLayoutchat=(DrawerLayout)findViewById(R.id.drawerlayoutchat);
    }
}
