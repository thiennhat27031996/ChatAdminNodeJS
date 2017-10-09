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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbarmhc;

    NavigationView navigationView;
    ListView listViewmenumhc,lvphong;
    DrawerLayout drawerLayout;
    Button btnaddrom;
    EditText edttenphong;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_room=new ArrayList<>();
    private String name;
    private DatabaseReference root= FirebaseDatabase.getInstance().getReference().child("Message");



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

        listViewmenumhc=(ListView)findViewById(R.id.lvmenutrangchu);
        navigationView=(NavigationView)findViewById(R.id.navigationview);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawerlayout);
        lvphong=(ListView)findViewById(R.id.lvmhc);
        btnaddrom=(Button)findViewById(R.id.btnthemphong);
        edttenphong=(EditText)findViewById(R.id.edttenphong);

        arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list_of_room);
        lvphong.setAdapter(arrayAdapter);

        request_username();

        btnaddrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,Object> map=new HashMap<String,Object>();
                map.put(edttenphong.getText().toString(),"");
                root.updateChildren(map);


            }
        });
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Set<String> set=new HashSet<String>();
                Iterator i=dataSnapshot.getChildren().iterator();
                while(i.hasNext()){
                    set.add(((DataSnapshot)i.next()).getKey());
                }
                list_of_room.clear();
                list_of_room.addAll(set);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        lvphong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getApplicationContext(),ChatActivity.class);
                intent.putExtra("room_name",((TextView)view).getText().toString());
                intent.putExtra("user_name",name);
                startActivity(intent);
            }
        });
    }

    private void request_username() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Enrername:");
        final EditText inputname=new EditText(this);
        builder.setView(inputname);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                name=inputname.getText().toString();
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                request_username();

            }
        });
        builder.show();
    }

}
