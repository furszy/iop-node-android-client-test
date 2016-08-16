package com.example.mati.chatappjava8.list;

import android.view.View;
import android.widget.TextView;

import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.interfaces.FermatHeader;
import com.example.mati.chatappjava8.R;
import com.example.mati.chatappjava8.chat.FermatViewHolder;

/**
 * Created by mati on 16/08/16.
 */
public class ActorHolder extends FermatViewHolder {

    TextView textView_name;

    /**
     * Constructor
     *
     * @param itemView
     */
    protected ActorHolder(View itemView) {
        super(itemView);
        this.textView_name = (TextView) itemView.findViewById(R.id.txt_name);
    }
}
