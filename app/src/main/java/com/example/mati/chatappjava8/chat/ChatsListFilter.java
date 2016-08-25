package com.example.mati.chatappjava8.chat;

import android.widget.Filter;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.ActorProfile;
import com.example.mati.chatappjava8.list.ListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by josejcb (josejcb89@gmail.com) on 8/20/2016.
 */
public class ChatsListFilter
        extends Filter {
    //
    private List<ChatsList> data;
    private ChatsListAdapter adapter;
    ArrayList<ChatsList> nlist = new ArrayList<>();

    public ChatsListFilter(List<ChatsList> data, ChatsListAdapter adapter) {
        this.data = data;
        this.adapter = adapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        String filterString = constraint.toString().toLowerCase();
        adapter.setFilterString(filterString);
        FilterResults results = new FilterResults();
        int count = data.size();
        String filterableString;
        ChatsList resource;

        for (int i = 0; i < count; i++) {
            resource = data.get(i);
            filterableString = resource.getContactName();
            if (filterableString.toLowerCase().contains(filterString)) {
                nlist.add(data.get(i));
            }
        }

        results.values = nlist;
        results.count = nlist.size();

        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        adapter.changeDataSet((List<ChatsList>) filterResults.values);
        adapter.notifyDataSetChanged();
    }
}