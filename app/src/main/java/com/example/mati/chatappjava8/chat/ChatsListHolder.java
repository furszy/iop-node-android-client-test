package com.example.mati.chatappjava8.chat;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mati.chatappjava8.R;

/**
 * Created by josej on 8/24/2016.
 */
public class ChatsListHolder extends FermatViewHolder {

    public ImageView  chatImage ;
    public TextView contactName;
    public TextView lastMessage ;
    public TextView dateMessage;
    public TextView noReadMsgs;
    public ImageView imageTick;

    /**
     * Constructor
     *
     * @param itemView
     */
    protected ChatsListHolder(View itemView) {
        super(itemView);

        chatImage = (ImageView) itemView.findViewById(R.id.chatImage);
        contactName = (TextView) itemView.findViewById(R.id.contactName);
        lastMessage = (TextView) itemView.findViewById(R.id.lastMessage);
        dateMessage = (TextView) itemView.findViewById(R.id.dateMessage);
        noReadMsgs = (TextView) itemView.findViewById(R.id.noReadMsgs);
        imageTick = (ImageView) itemView.findViewById(R.id.imageTick);
    }

    public ImageView getImage() {
        return chatImage;
    }

    public TextView getContactName() {
        return contactName;
    }

    public TextView getLastMessage() {
        return lastMessage;
    }

    public TextView getDateMessage() {
        return dateMessage;
    }

    public TextView getNoReadMsgs() {
        return noReadMsgs;
    }

    public ImageView getImageTick() {
        return imageTick;
    }

}
