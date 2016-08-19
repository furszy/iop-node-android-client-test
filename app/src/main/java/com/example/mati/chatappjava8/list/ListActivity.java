package com.example.mati.chatappjava8.list;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ListActivity extends AppCompatActivity
        implements MessageReceiver, FermatListItemListeners<ActorProfile>
        /*,SwipeRefreshLayout.OnRefreshListener */{

    RecyclerView recyclerView;
    ListAdapter listAdapter;
    List<ActorProfile> listActors;
    private DrawerLayout drawerLayout;
    private LinearLayoutManager linearLayoutManager;
    private NavigationView navigationView;
    private SwipeRefreshLayout swipeRefresh;
    private int max = 10;
    private int offset = 0;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    ExecutorService executorService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        navigationView = (NavigationView) findViewById(R.id.navigation);
//        navigationView.inflateMenu(R.menu.navigation_menu);
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
        init();

        Core.getInstance().setReceiver(this);

        this.recyclerView = (RecyclerView) findViewById(R.id.recycler);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        this.recyclerView.setLayoutManager(linearLayoutManager);
        listActors = new ArrayList<>();
        listAdapter = new ListAdapter(getApplicationContext(),listActors);
        listAdapter.setFermatListEventListener(this);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    visibleItemCount = linearLayoutManager.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();
                    offset = totalItemCount;
                    final int lastItem = pastVisiblesItems + visibleItemCount;
                    if (lastItem == totalItemCount) {
                        onRefreshList();
                    }
                }
            }
        });
        this.recyclerView.setLayoutManager(linearLayoutManager);
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setAdapter(listAdapter);
        this.recyclerView.scrollToPosition(0);
        //Set up swipeRefresher
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipeRefresh.setColorSchemeColors(Color.BLUE, Color.BLUE);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                offset = 0;
                onRefreshList();
            }
        });
        onRefreshList();
    }

    private void init(){
        if (executorService==null){
            executorService = Executors.newFixedThreadPool(2);
        }else{
            if (executorService.isShutdown()){
                executorService =  Executors.newFixedThreadPool(2);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    @Override
    protected void onStop() {
        executorService.shutdownNow();
        super.onStop();
    }

    public void onRefreshList() {
        try {
            swipeRefresh.setRefreshing(false);
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    Core.getInstance().getChatNetworkServicePluginRoot().requestActorProfilesList(max, offset);
                }
            });
        } catch (Exception e) {
            System.out.println(e.toString());
        }
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
    public void onMessageReceived(String senderPk,String chatMetadataRecord) {
        Notifications.pushNotification(this,chatMetadataRecord, senderPk);
    }


    @Override
    public void onActorListReceived(final List<ActorProfile> list) {
        if (list.size()==0){
            Log.i(this.getComponentName().getClassName(),"ActorList empty");
        }
        for (ActorProfile actorProfile : list) {
            Log.i(this.getComponentName().getClassName(),actorProfile.toString());
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (offset == 0) {
                    if (listActors != null) {
                        listActors.clear();
                        listActors.addAll(list);
                    } else {
                        listActors = list;
                    }
                    listAdapter.changeDataSet(listActors);
                    listAdapter.notifyDataSetChanged();
                } else {
                    List<ActorProfile> temp = list;
                    for (ActorProfile info : temp)
                        if (notInList(info)) {
                            listActors.add(info);
                        }
                    listAdapter.notifyItemRangeInserted(offset, listActors.size() - 1);
                }
            }
        });
        offset = listActors.size();
    }

    private boolean notInList(ActorProfile info) {
        for (ActorProfile contact : listActors) {
            if (contact.getIdentityPublicKey().equals(info.getIdentityPublicKey()))
                return false;
        }
        return true;
    }

    @Override
    public void onActorRegistered(ActorProfile actorProfile) {

    }

    @Override
    public void onItemClickListener(ActorProfile data, int position) {
        if (Core.getInstance().getProfile()!=null) {
            Intent intent = new Intent(getApplicationContext(), ChatActivity2.class);
            intent.putExtra(IntentConstants.PROFILE_RECEIVER, data);
            startActivity(intent);
        }else Toast.makeText(this,"No profile created,\nplease go to the profile screen and create one",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLongItemClickListener(ActorProfile data, int position) {

    }
}
