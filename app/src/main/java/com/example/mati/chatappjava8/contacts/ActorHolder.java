package com.example.mati.chatappjava8.contacts;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mati.chatappjava8.R;
import com.example.mati.chatappjava8.chat.FermatViewHolder;

/**
 * Created by mati on 16/08/16.
 */
public class ActorHolder extends FermatViewHolder {

    TextView textView_name;
    ImageView img_photo;

    /**
     * Constructor
     *
     * @param itemView
     */
    protected ActorHolder(View itemView) {
        super(itemView);
        this.textView_name = (TextView) itemView.findViewById(R.id.txt_name);
        this.img_photo = (ImageView) itemView.findViewById(R.id.img_photo);
    }
}
