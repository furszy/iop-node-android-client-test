package com.example.mati.chatappjava8.chat;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.ActorProfile;
import com.example.mati.app_core.Core;
import com.example.mati.chatappjava8.IntentConstants;
import com.example.mati.chatappjava8.R;
import com.example.mati.chatappjava8.commons.Notifications;

import org.iop.ns.chat.structure.test.MessageReceiver;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class ChatActivity2 extends AppCompatActivity implements MessageReceiver {

    private EditText messageET;
    private RecyclerView messagesContainer;
    private Button sendBtn;
    private ChatAdapter2 adapter;
    private ArrayList<ChatMessage> chatHistory;

    private ActorProfile remote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().hasExtra(IntentConstants.PROFILE_RECEIVER)) {
            remote = (ActorProfile) getIntent().getSerializableExtra(IntentConstants.PROFILE_RECEIVER);
            Core.getInstance().setLastRemoteProfile(remote);
        }else {
            remote = Core.getInstance().getLastRemoteProfile();
        }

        if(remote==null){
            new AlertDialog.Builder(this).setTitle("Remote profile null, please go back to the actors list and pick one before chat").show();
        }else {

            setContentView(R.layout.activity_chat_2);
            initControls();

        }

        Core.getInstance().setReceiver(this);

    }

    private void initControls() {
        messagesContainer = (RecyclerView) findViewById(R.id.messagesContainer);
        messagesContainer.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        messageET = (EditText) findViewById(R.id.messageEdit);
        sendBtn = (Button) findViewById(R.id.chatSendButton);

        TextView meLabel = (TextView) findViewById(R.id.meLbl);
        TextView companionLabel = (TextView) findViewById(R.id.friendLabel);
        RelativeLayout container = (RelativeLayout) findViewById(R.id.container);
        companionLabel.setText(remote.getName());// Hard Coded
        loadDummyHistory();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String messageText = messageET.getText().toString();
                if (TextUtils.isEmpty(messageText)) {
                    return;
                }

                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setId(122);//dummy
                chatMessage.setMessage(messageText);
                chatMessage.setDate(DateFormat.getDateTimeInstance().format(new Date()));
                chatMessage.setMe(true);

                messageET.setText("");

                displayMessage(chatMessage);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Core.getInstance().getChatNetworkServicePluginRoot().sendNewMessage(
                                    Core.getInstance().getInstance().getProfile(),
                                    remote,
                                    messageText
                            );
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });
    }

    public void displayMessage(final ChatMessage message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.addItem(message);
                adapter.notifyDataSetChanged();
                scroll();
            }
        });

    }

    private void scroll() {
        messagesContainer.scrollToPosition(adapter.getItemCount() - 1);
    }

    private void loadDummyHistory(){

        chatHistory = new ArrayList<ChatMessage>();

        ChatMessage msg = new ChatMessage();
        msg.setId(1);
        msg.setMe(false);
        msg.setMessage("Hola");
        msg.setDate(DateFormat.getDateTimeInstance().format(new Date()));
        chatHistory.add(msg);
        ChatMessage msg1 = new ChatMessage();
        msg1.setId(2);
        msg1.setMe(false);
        msg1.setMessage("como andas??");
        msg1.setDate(DateFormat.getDateTimeInstance().format(new Date()));
        chatHistory.add(msg1);

        adapter = new ChatAdapter2(ChatActivity2.this, new ArrayList<ChatMessage>());
        messagesContainer.setAdapter(adapter);

        for(int i=0; i<chatHistory.size(); i++) {
            ChatMessage message = chatHistory.get(i);
            displayMessage(message);
        }
    }


    @Override
    public void onMessageReceived(String senderPk,String content) {
        if (remote.getIdentityPublicKey().equals(senderPk)) {
            Random random = new Random();
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setId(random.nextInt());//dummy
            chatMessage.setMessage(content);
            chatMessage.setDate(DateFormat.getDateTimeInstance().format(new Date()));
            chatMessage.setMe(false);
            displayMessage(chatMessage);
        }else {
            Notifications.pushNotification(this,content);
        }
    }

    @Override
    public void onActorListReceived(List<ActorProfile> list) {

    }

}