package com.example.mati.chatappjava8.profile;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.ActorProfile;
import com.example.mati.app_core.Core;
import com.example.mati.chatappjava8.R;
import com.example.mati.chatappjava8.commons.NavigationListener;
import com.example.mati.chatappjava8.commons.Notifications;

import org.iop.ns.chat.structure.ChatMetadataRecord;
import org.iop.ns.chat.structure.test.MessageReceiver;

import java.util.List;
import java.util.UUID;

public class ProfileActivity extends AppCompatActivity implements MessageReceiver {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//                Intent intent = new Intent(getApplicationContext(), ChatActivity2.class);
//                startActivity(intent);
//            }
//        });



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


        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().add(R.id.fragment_container,CreateIntraUserIdentityFragment.newInstance(Core.getInstance().getChatNetworkServicePluginRoot())).commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Core.getInstance().setReceiver(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    public void onMessageReceived(String sender, ChatMetadataRecord chatMetadataRecord) {
        Notifications.pushNotification(this,chatMetadataRecord.getMessage(), sender);
    }

    @Override
    public void onActorListReceived(List<ActorProfile> list) {

    }

    @Override
    public void onActorRegistered(ActorProfile actorProfile) {

    }

    @Override
    public void onMessageFail(UUID messageId) {

    }
}
