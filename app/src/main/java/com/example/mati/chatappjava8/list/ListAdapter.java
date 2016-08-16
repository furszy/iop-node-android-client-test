package com.example.mati.chatappjava8.list;

import android.content.Context;
import android.view.View;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.ActorProfile;
import com.example.mati.chatappjava8.R;
import com.example.mati.chatappjava8.chat.FermatAdapter;

import java.util.List;

/**
 * Created by mati on 16/08/16.
 */
public class ListAdapter extends FermatAdapter<ActorProfile,ActorHolder> {

    public ListAdapter(Context context) {
        super(context);
    }

    protected ListAdapter(Context context, List<ActorProfile> dataSet) {
        super(context, dataSet);
    }

    @Override
    protected ActorHolder createHolder(View itemView, int type) {
        return new ActorHolder(itemView);
    }

    @Override
    protected int getCardViewResource() {
        return R.layout.actor_list_row;
    }

    @Override
    protected void bindHolder(ActorHolder holder, ActorProfile data, int position) {
        holder.textView_name.setText(data.getName());
    }
}
