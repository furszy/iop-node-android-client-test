package com.example.mati.chatappjava8.commons;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;

import com.example.mati.chatappjava8.chat.ChatActivity2;
import com.example.mati.chatappjava8.contacts.ContactsActivity;
import com.example.mati.chatappjava8.list.ListActivity;
import com.example.mati.chatappjava8.profile.ProfileActivity;

import java.lang.ref.WeakReference;

/**
 * Created by mati on 16/08/16.
 */
public class NavigationListener implements NavigationView.OnNavigationItemSelectedListener {

    WeakReference<Activity> activity;

    public NavigationListener(Activity activity) {
        this.activity = new WeakReference<Activity>(activity);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        String title = menuItem.getTitle().toString();
        Class<?> clazz = null;
        switch (title){
            case "Profile":
                clazz = ProfileActivity.class;
                break;
            case "Actors":
                clazz = ListActivity.class;
                break;
            case "Chat":
                clazz = ChatActivity2.class;
                break;
            case "Contacts":
                clazz = ContactsActivity.class;
                break;
        }
//        menuItem.setChecked(true);
//        drawerLayout.closeDrawers();
        Intent intent = new Intent(activity.get(), clazz);
        activity.get().startActivity(intent);
        return true;
    }
}
