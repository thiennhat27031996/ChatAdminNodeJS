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

public class MainActivity extends AppCompatActivity {

    Toolbar toolbarmhc;
    RecyclerView recyclerViewnhantin;
    NavigationView navigationView;
    ListView listViewmenutinnhan;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Anhxa();
        Actionbar();
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
        recyclerViewnhantin=(RecyclerView)findViewById(R.id.rclmainchat);
        listViewmenutinnhan=(ListView)findViewById(R.id.lvmenutinnhan);
        navigationView=(NavigationView)findViewById(R.id.navigationview);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawerlayout);
    }
}
