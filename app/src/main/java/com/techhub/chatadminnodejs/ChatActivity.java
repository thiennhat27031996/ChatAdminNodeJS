package com.techhub.chatadminnodejs;

import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.engineio.client.Socket;
import com.github.nkzawa.socketio.client.IO;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.techhub.chatadminnodejs.Adapter.MessageAdapter;
import com.techhub.chatadminnodejs.OBJ.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    Toolbar toolbarmhchat;
    RecyclerView recyclerViewnhantin;
    NavigationView navigationViewchat;
    ListView listViewmenutinnhan,lv;
    DrawerLayout drawerLayoutchat;
    EditText edtnoidungtinnhanjv;
    Button btnguitinnhanjv;


    ArrayList<String> mangusername;
    ArrayList<Message> mangchat;
    MessageAdapter messageAdapter;
    String adminuser="Admin";
    String usn="";

    DatabaseReference root;



    private com.github.nkzawa.socketio.client.Socket mSocket;
    {
        try{
            mSocket= IO.socket("http://192.168.56.1:3000");

        }catch (URISyntaxException e){}

    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Anhxa();
        Firebase();
       // ConnectSocketio();
        Actionbar();
    }

    private void Firebase() {

        root = FirebaseDatabase.getInstance().getReference();
        btnguitinnhanjv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chat=edtnoidungtinnhanjv.getText().toString();
                root.push().setValue(chat);
            }
        });
        //lay string tren dtb
        FirebaseListAdapter<String> adapter=new FirebaseListAdapter<String>(this,String.class,R.layout.item_rclmainmhc,root) {
            @Override
            protected void populateView(View v, String model, int position) {
                TextView textView=(TextView)v.findViewById(R.id.textnguoigui);
                textView.setText(model);

            }
        };
        lv.setAdapter(adapter);







    }



    private void ConnectSocketio() {
        mSocket.connect();

        mSocket.on("svguiusn",onNew_dsusn);
        mSocket.on("serverguichat",onNew_guitinchat);

        mSocket.emit("clientguiuser",adminuser);

        btnguitinnhanjv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mSocket.emit("clienguichat",edtnoidungtinnhanjv.getText().toString());











            }
        });
    }




    private Emitter.Listener onNew_guitinchat = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String noidung,id;

                    try {
                        noidung = data.getString("tinchat");
                        id=data.getString("idchat");
                        Toast.makeText(getApplicationContext(),noidung.toString()+id.toString(),Toast.LENGTH_LONG).show();

                        if(!noidung.toString().equals(edtnoidungtinnhanjv.getText().toString())){
                            Message message=new Message(id,noidung,false);
                            mangchat.add(message);

                        }
                        else{
                            Message message1=new Message("Admin : ",edtnoidungtinnhanjv.getText().toString(),true);
                            mangchat.add(message1);

                        }
                        messageAdapter.notifyDataSetChanged();

                        edtnoidungtinnhanjv.setText("");
                        recyclerViewnhantin.scrollToPosition(messageAdapter.getItemCount()-1);


                    } catch (JSONException e) {
                        return;
                    }
                }
            });
        }
    };


    private Emitter.Listener onNew_dsusn = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    JSONArray noidung;
                    mangusername.removeAll(mangusername);

                    try {
                        noidung = data.getJSONArray("danhsach");
                        Toast.makeText(getApplicationContext(),noidung.toString(),Toast.LENGTH_LONG).show();
                        for(int i=0;i<noidung.length();i++){
                                    mangusername.add(noidung.get(i).toString());
                            }
                        ArrayAdapter adapter=new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,mangusername);
                        listViewmenutinnhan.setAdapter(adapter);
                        adapter.notifyDataSetChanged();





                    } catch (JSONException e) {
                        return;
                    }
                }
            });
        }
    };






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
        recyclerViewnhantin=(RecyclerView) findViewById(R.id.rclmainchat);
        listViewmenutinnhan=(ListView)findViewById(R.id.lvmenutinnhan);
        navigationViewchat=(NavigationView)findViewById(R.id.navigationviewchat);
        drawerLayoutchat=(DrawerLayout)findViewById(R.id.drawerlayoutchat);
        edtnoidungtinnhanjv=(EditText)findViewById(R.id.edtnoidungtinnhan);
        btnguitinnhanjv=(Button)findViewById(R.id.btnguitinnhan);
        lv=(ListView)findViewById(R.id.lv);
        recyclerViewnhantin.setHasFixedSize(true);
        recyclerViewnhantin.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));


        mangchat=new ArrayList<>();
       // messageAdapter=new MessageAdapter(mangchat,getApplicationContext());
        mangusername=new ArrayList<String>();
       // recyclerViewnhantin.setAdapter(messageAdapter);




    }
}
