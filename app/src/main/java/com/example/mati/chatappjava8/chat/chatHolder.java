package com.example.mati.chatappjava8.chat;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mati.chatappjava8.R;


/**
 * Created by mati on 2016.02.03..
 */
public class chatHolder extends FermatViewHolder {

    public TextView txtMessage;
    public TextView txtInfo;
    public LinearLayout content;
    public LinearLayout contentWithBG;
    public ImageView imageTick;


    /**
     * Constructor
     *
     * @param itemView
     */
    protected chatHolder(View itemView) {
        super(itemView);

        txtMessage = (TextView) itemView.findViewById(R.id.txtMessage);
        txtInfo = (TextView) itemView.findViewById(R.id.txtInfo);
        content = (LinearLayout) itemView.findViewById(R.id.content);
        contentWithBG = (LinearLayout) itemView.findViewById(R.id.contentWithBackground);
        imageTick = (ImageView) itemView.findViewById(R.id.imageTick);
    }

    public TextView getTxtMessage() {
        return txtMessage;
    }

    public TextView getTxtInfo() {
        return txtInfo;
    }

    public LinearLayout getContent() {
        return content;
    }

    public LinearLayout getContentWithBG() {
        return contentWithBG;
    }
}
