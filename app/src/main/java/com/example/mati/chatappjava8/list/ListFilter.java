package com.example.mati.chatappjava8.list;

import android.widget.Filter;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.ActorProfile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by josejcb (josejcb89@gmail.com) on 8/20/2016.
 */
public class ListFilter
        extends Filter {
    //
    private List<ActorProfile> data;
    private ListAdapter adapter;
    ArrayList<ActorProfile> nlist = new ArrayList<>();

    public ListFilter(List<ActorProfile> data, ListAdapter adapter) {
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
        ActorProfile resource;

        for (int i = 0; i < count; i++) {
            resource = data.get(i);
            filterableString = resource.getAlias();
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
        adapter.changeDataSet((List<ActorProfile>) filterResults.values);
        adapter.notifyDataSetChanged();
    }
}