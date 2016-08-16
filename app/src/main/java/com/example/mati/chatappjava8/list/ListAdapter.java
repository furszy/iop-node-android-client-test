package com.example.mati.chatappjava8.list;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.View;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.ActorProfile;
import com.example.mati.chatappjava8.R;
import com.example.mati.chatappjava8.chat.FermatAdapter;
import com.example.mati.chatappjava8.profile.ImagesUtils;

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
        if (data.getPhoto()!=null){
            if (data.getPhoto().length>0){
                holder.img_photo.setImageDrawable(ImagesUtils.getRoundedBitmap(context.getResources(), BitmapFactory.decodeByteArray(data.getPhoto(),0,data.getPhoto().length)));
            }
        }
    }
}
