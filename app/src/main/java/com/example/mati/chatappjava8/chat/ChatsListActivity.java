package com.example.mati.chatappjava8.chat;

import android.app.SearchManager;
import android.content.Context;
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
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.ActorProfile;
import com.example.mati.app_core.Core;
import com.example.mati.chatappjava8.IntentConstants;
import com.example.mati.chatappjava8.R;
import com.example.mati.chatappjava8.commons.NavigationListener;
import com.example.mati.chatappjava8.commons.Notifications;
import com.example.mati.chatappjava8.list.ListAdapter;

import org.iop.ns.chat.structure.ChatMetadataRecord;
import org.iop.ns.chat.structure.test.MessageReceiver;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by josej on 8/24/2016.
 */
public class ChatsListActivity extends AppCompatActivity
        implements MessageReceiver, FermatListItemListeners<ChatsList>
        /*,SwipeRefreshLayout.OnRefreshListener */{

    RecyclerView recyclerView;
    ChatsListAdapter listAdapter;
    private ProgressBar progressBar;
    List<ChatsList> listChats;
    private DrawerLayout drawerLayout;
    private LinearLayoutManager linearLayoutManager;
    private NavigationView navigationView;
    private SwipeRefreshLayout swipeRefresh;
    private int max = 10;
    private int offset = 0;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    ExecutorService executorService;
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;

    private AtomicBoolean isRefreshing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        isRefreshing = new AtomicBoolean(false);

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

        this.recyclerView = (RecyclerView) findViewById(R.id.recycler_chats);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        this.recyclerView.setLayoutManager(linearLayoutManager);
        listChats = new ArrayList<>();
        listAdapter = new ChatsListAdapter(getApplicationContext(),listChats);
        listAdapter.setFermatListEventListener(this);
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                if (dy > 0) {
//                    visibleItemCount = linearLayoutManager.getChildCount();
//                    totalItemCount = linearLayoutManager.getItemCount();
//                    pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();
//                    offset = totalItemCount;
//                    final int lastItem = pastVisiblesItems + visibleItemCount;
//                    if (lastItem == totalItemCount) {
//                        onRefreshList();
//                    }
//                }
//            }
//        });
        this.recyclerView.setLayoutManager(linearLayoutManager);
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setAdapter(listAdapter);
        this.recyclerView.scrollToPosition(0);

        if (listChats.isEmpty()){
            findViewById(R.id.black_screen).setVisibility(View.VISIBLE);
        }

        onRefreshList();
    }

    private void init(){
        Core.getInstance().setReceiver(this);
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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
            searchView.setQueryHint("Search...");
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    if (newText.equals(searchView.getQuery().toString())) {
                        Log.i("onQueryTextChange", newText);
//                        listAdapter.changeDataSet(listActors);
                        listAdapter.getFilter().filter(newText);
                    }
                    return false;
                }
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_search:
                // Not implemented here
                return false;
            default:
                break;
        }
        searchView.setOnQueryTextListener(queryTextListener);

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMessageReceived(String senderPk,ChatMetadataRecord chatMetadataRecord) {
        Notifications.pushNotification(this, chatMetadataRecord.getMessage(), senderPk);
    }

    @Override
    public void onMessageFail(UUID messageId) {

    }

    @Override
    public void onActorListReceived(final List<ActorProfile> list) {
    }


    @Override
    public void onActorRegistered(ActorProfile actorProfile) {
    }

    @Override
    public void onActorOffline(String remotePkGoOffline) {
        Log.i(getClass().getName(),"onActorOffline: "+remotePkGoOffline);
    }

    @Override
    public void onItemClickListener(ChatsList data, int position) {
        if (Core.getInstance().getProfile()!=null) {
//            Intent intent = new Intent(getApplicationContext(), ChatActivity2.class);
//            intent.putExtra(IntentConstants.PROFILE_RECEIVER, data);
//            startActivity(intent);
        }else Toast.makeText(this,"No profile created,\nplease go to the profile screen and create one",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLongItemClickListener(ChatsList data, int position) {

    }
}
