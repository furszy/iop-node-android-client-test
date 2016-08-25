package com.example.mati.chatappjava8.chat;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;

import com.example.mati.chatappjava8.R;
import com.example.mati.chatappjava8.profile.ImagesUtils;

import org.iop.ns.chat.enums.MessageStatus;
import org.iop.ns.chat.enums.TypeMessage;

import java.util.List;

/**
 * Created by josej on 8/24/2016.
 */
public class ChatsListAdapter
        extends FermatAdapter<ChatsList,ChatsListHolder>
        implements Filterable {

    List<ChatsList> filteredData;
    private String filterString;

    public ChatsListAdapter(Context context) {
        super(context);
    }

    protected ChatsListAdapter(Context context, List<ChatsList> dataSet) {
        super(context, dataSet);
    }

    @Override
    protected ChatsListHolder createHolder(View itemView, int type) {
        return new ChatsListHolder(itemView);
    }

    @Override
    protected int getCardViewResource() {
        return R.layout.chats_list_row;
    }

    @Override
    protected void bindHolder(ChatsListHolder holder, ChatsList data, int position) {
        if (data.getImgProfile()!=null){
            if (data.getImgProfile().length>0){
                holder.chatImage.setImageDrawable(ImagesUtils.getRoundedBitmap(context.getResources(), BitmapFactory.decodeByteArray(data.getImgProfile(),0,data.getImgProfile().length)));
            }
        }else
            holder.chatImage.setImageDrawable(ImagesUtils.getRoundedBitmap(context.getResources(), R.drawable.image_profile));

        holder.contactName.setText(data.getContactName());
        if (data.getLastMessage().contains("Typing")) {
            holder.lastMessage.setTextColor(Color.parseColor("#FF33A900"));
        } else {
            holder.lastMessage.setTextColor(Color.parseColor("#757575"));
        }
        holder.lastMessage.setText(data.getLastMessage());
        holder.dateMessage.setText(data.getDateMessage());
        holder.imageTick.setImageResource(0);
        if (data.getTypeMessage().equals(TypeMessage.OUTGOING.toString())) {
            holder.imageTick.setVisibility(View.VISIBLE);
            if (data.getStatus().equals(MessageStatus.SEND.toString())) {
                holder.imageTick.setImageResource(R.drawable.cht_ticksent);
            }
            else if (data.getStatus().equals(MessageStatus.DELIVERED.toString()) || data.getStatus().equals(MessageStatus.RECEIVE.toString())) {
                holder.imageTick.setImageResource(R.drawable.cht_tickdelivered);
            }
            else if (data.getStatus().equals(MessageStatus.READ.toString())) {
                holder.imageTick.setImageResource(R.drawable.cht_tickread);
            }
            else if (data.getStatus().equals(MessageStatus.CANNOT_SEND.toString())) {
                holder.imageTick.setImageResource(R.drawable.cht_close);
            }
        } else
            holder.imageTick.setVisibility(View.GONE);
        if (data.getNoReadMsgs() > 0) {
            if(data.getNoReadMsgs().toString().length()==1)
                holder.noReadMsgs.setText(data.getNoReadMsgs().toString());
            else  holder.noReadMsgs.setText(data.getNoReadMsgs());
            holder.noReadMsgs.setVisibility(View.VISIBLE);
        } else
            holder.noReadMsgs.setVisibility(View.GONE);


    }

    public void setData(List<ChatsList> data) {
        this.filteredData = data;
    }

    public Filter getFilter() {
        return new ChatsListFilter(dataSet, this);
    }

    public void setFilterString(String filterString) {
        this.filterString = filterString;
    }

    public String getFilterString() {
        return filterString;
    }
}