package com.example.mati.chatappjava8.list;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.ActorProfile;
import com.example.mati.app_core.Core;
import com.example.mati.chatappjava8.IntentConstants;
import com.example.mati.chatappjava8.R;
import com.example.mati.chatappjava8.chat.ChatActivity2;
import com.example.mati.chatappjava8.chat.FermatListItemListeners;
import com.example.mati.chatappjava8.commons.NavigationListener;
import com.example.mati.chatappjava8.commons.Notifications;

import org.iop.ns.chat.structure.test.MessageReceiver;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity implements MessageReceiver, FermatListItemListeners<ActorProfile> {

    RecyclerView recyclerView;
    ListAdapter listAdapter;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.inflateMenu(R.menu.navigation_menu);
        navigationView.setNavigationItemSelectedListener(new NavigationListener(this));
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
//            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        Core.getInstance().setReceiver(this);

        this.recyclerView = (RecyclerView) findViewById(R.id.recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        this.recyclerView.setLayoutManager(linearLayoutManager);
        List<ActorProfile>  list = new ArrayList<>();
        listAdapter = new ListAdapter(getApplicationContext(),list);
        listAdapter.setFermatListEventListener(this);
        this.recyclerView.setAdapter(listAdapter);


        new Thread(new Runnable() {
            @Override
            public void run() {
                Core.getInstance().getChatNetworkServicePluginRoot().requestActorProfilesList();
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMessageReceived(String senderPk,String chatMetadataRecord) {
        Notifications.pushNotification(this,chatMetadataRecord);
    }


    @Override
    public void onActorListReceived(final List<ActorProfile> list) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listAdapter.changeDataSet(list);
                listAdapter.notifyDataSetChanged();
//                recyclerView.setAdapter(listAdapter);
            }
        });

    }

    @Override
    public void onItemClickListener(ActorProfile data, int position) {
        Intent intent = new Intent(getApplicationContext(),ChatActivity2.class);
        intent.putExtra(IntentConstants.PROFILE_RECEIVER,data);
        startActivity(intent);
    }

    @Override
    public void onLongItemClickListener(ActorProfile data, int position) {

    }
}
