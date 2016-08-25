package com.example.mati.chatappjava8.list;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.ActorProfile;
import com.example.mati.chatappjava8.R;
import com.example.mati.chatappjava8.chat.FermatAdapter;
import com.example.mati.chatappjava8.profile.ImagesUtils;

import java.util.List;
import java.util.Random;

/**
 * Created by mati on 16/08/16.
 */
public class ListAdapter
        extends FermatAdapter<ActorProfile,ActorHolder>
        implements Filterable {

    List<ActorProfile> filteredData;
    private String filterString;

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
        if(data.getStatus() != null && data.getStatus().getCode().equalsIgnoreCase("ON"))
            holder.textView_name.setTextColor(Color.parseColor("#47BF73"));
        else
            holder.textView_name.setTextColor(Color.RED);
        if (data.getPhoto()!=null){
            if (data.getPhoto().length>0){
                holder.img_photo.setImageDrawable(ImagesUtils.getRoundedBitmap(context.getResources(), BitmapFactory.decodeByteArray(data.getPhoto(),0,data.getPhoto().length)));
            }
        }else
            holder.img_photo.setImageDrawable(ImagesUtils.getRoundedBitmap(context.getResources(), R.drawable.image_profile));
//        Random random = new Random();
//        int ran = random.nextInt(50);

//        if (ran<15) {
//            holder.itemView.setBackgroundColor(Color.CYAN);
//        }else if(ran>=15 && ran<=30){
//            holder.itemView.setBackgroundColor(Color.YELLOW);
//        }else
//            holder.itemView.setBackgroundColor(Color.MAGENTA);
    }

    public void setData(List<ActorProfile> data) {
        this.filteredData = data;
    }

    public Filter getFilter() {
        return new ListFilter(dataSet, this);
    }

    public void setFilterString(String filterString) {
        this.filterString = filterString;
    }

    public String getFilterString() {
        return filterString;
    }
}
