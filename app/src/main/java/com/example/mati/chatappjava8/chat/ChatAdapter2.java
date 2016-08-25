package com.example.mati.chatappjava8.chat;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.mati.chatappjava8.R;

import java.util.List;

/**
 * Created by mati on 2016.02.03..
 */
public class ChatAdapter2 extends FermatAdapter<ChatMessage,chatHolder> {




    protected ChatAdapter2(Context context) {
        super(context);
    }

    public ChatAdapter2(Context context, List<ChatMessage> dataSet) {
        super(context, dataSet);
    }

    @Override
    protected chatHolder createHolder(View itemView, int type) {
        return new chatHolder(itemView);
    }

    @Override
    protected int getCardViewResource() {
        return R.layout.list_item_chat_message;
    }

    @Override
    protected void bindHolder(chatHolder holder, ChatMessage data, int position) {
        boolean myMsg = data.getIsme() ;//Just a dummy check
        //to simulate whether it me or other sender
        setAlignment(holder, myMsg);
        holder.txtMessage.setText(data.getMessage());
        holder.txtInfo.setText(data.getDate());
        if (data.getIsFail() == true) {
            holder.imageTick.setImageResource(R.drawable.cht_close);
            holder.imageTick.setVisibility(View.VISIBLE);
        } else {
            holder.imageTick.setImageResource(0);
            holder.imageTick.setVisibility(View.GONE);
        }
    }




    private void setAlignment(chatHolder holder, boolean isMe) {
        if (!isMe) {
            holder.contentWithBG.setBackgroundResource(R.drawable.in_message_bg);

            LinearLayout.LayoutParams layoutParams =
                    (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.contentWithBG.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp =
                    (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.content.setLayoutParams(lp);
            layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.txtMessage.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.txtInfo.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.txtInfo.setLayoutParams(layoutParams);
        } else {
            holder.contentWithBG.setBackgroundResource(R.drawable.out_message_bg);

            LinearLayout.LayoutParams layoutParams =
                    (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.contentWithBG.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp =
                    (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            holder.content.setLayoutParams(lp);
            layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.txtMessage.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.txtInfo.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.txtInfo.setLayoutParams(layoutParams);
        }
    }
}
