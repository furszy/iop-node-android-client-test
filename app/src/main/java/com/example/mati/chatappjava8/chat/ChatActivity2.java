package com.example.mati.chatappjava8.chat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.clients.exceptions.CantSendMessageException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.ActorProfile;
import com.example.mati.app_core.Core;
import com.example.mati.chatappjava8.IntentConstants;
import com.example.mati.chatappjava8.R;
import com.example.mati.chatappjava8.commons.Notifications;

import org.iop.ns.chat.structure.ChatMetadataRecord;
import org.iop.ns.chat.structure.test.MessageReceiver;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ChatActivity2 extends AppCompatActivity implements MessageReceiver {

    private EditText messageET;
    private RecyclerView messagesContainer;
    private LinearLayoutManager layoutManager;
    private Button sendBtn;
    private ChatAdapter2 adapter;
    private ActorProfile remote;

    private List<ChatMetadataRecord> listMessages;


    private boolean isSearchingRemote = false;
    private String remotePk;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat_2);
        if (getIntent().hasExtra(IntentConstants.PROFILE_RECEIVER)) {
            remote = (ActorProfile) getIntent().getSerializableExtra(IntentConstants.PROFILE_RECEIVER);
            Core.getInstance().setLastRemoteProfile(remote);
        }if (getIntent().hasExtra(IntentConstants.PROFILE_RECEIVER_NOTIFICATION_PK)){
            String pk = getIntent().getStringExtra(IntentConstants.PROFILE_RECEIVER_NOTIFICATION_PK);
            remote = Core.getInstance().getRemoteProfile(pk);
            if (remote==null){
                Core.getInstance().getChatNetworkServicePluginRoot().requestActorProfilesList(10000, 0, Core.getInstance().getProfile().getIdentityPublicKey());
                isSearchingRemote = true;
                remotePk = pk;
            }
        } else {
            remote = Core.getInstance().getLastRemoteProfile();
        }

        if(remote==null){
            findViewById(R.id.black_screen).setVisibility(View.VISIBLE);
            findViewById(R.id.chat_screen).setVisibility(View.GONE);
        }else {
            initControls();
//
            /**
             * subscribe example
             */
            try {
                Core.getInstance().getChatNetworkServicePluginRoot().subscribeActorOnlineEvent(remote.getIdentityPublicKey());
            } catch (CantSendMessageException e) {
                e.printStackTrace();
            }


            /**
             * Save actor as contact if i not have previous talk
             */
            if (!listMessages.isEmpty()){
                try {
                    Core.getInstance().getChatNetworkServicePluginRoot().saveContact(remote);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        Core.getInstance().setReceiver(this);

    }

    private void initControls() {
        messagesContainer = (RecyclerView) findViewById(R.id.messagesContainer);
        this.layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        messagesContainer.setLayoutManager(layoutManager);
        messageET = (EditText) findViewById(R.id.messageEdit);
        messageET.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    sendBtn.performClick();
                    return true;
                }
                return false;
            }
        });
        sendBtn = (Button) findViewById(R.id.chatSendButton);

        TextView companionLabel = (TextView) findViewById(R.id.friendLabel);
        companionLabel.setText(remote.getName());// Hard Coded
        loadHistory();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String messageText = messageET.getText().toString();
                if (TextUtils.isEmpty(messageText)) {
                    return;
                }

                final ChatMessage chatMessage = new ChatMessage();
                chatMessage.setMessage(messageText);
                chatMessage.setDate(DateFormat.getDateTimeInstance().format(new Date()));
                chatMessage.setMe(true);

                messageET.setText("");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //este id tenes que guardarlo y chequear si no falló por el metodo onMessageFail
                            UUID mensajeId = Core.getInstance().getChatNetworkServicePluginRoot().sendMessage(
                                    messageText,
                                    Core.getInstance().getInstance().getProfile().getIdentityPublicKey(),
                                    remote.getIdentityPublicKey()
                            );
                            //acá se le deberia setear el id, si no lo hace fijate que onda
                            chatMessage.setId(mensajeId);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                displayMessage(chatMessage);
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

    @Override
    protected void onResume() {
        super.onResume();
        Core.getInstance().setReceiver(this);
    }

    private void scroll() {
        messagesContainer.scrollToPosition(adapter.getItemCount() - 1);
    }

    private void loadHistory(){

        adapter = new ChatAdapter2(ChatActivity2.this, new ArrayList<ChatMessage>());
        messagesContainer.setAdapter(adapter);

        try {

            String localPK = Core.getInstance().getProfile().getIdentityPublicKey();
            listMessages = Core.getInstance().getChatNetworkServicePluginRoot().listMessages(
                    localPK,
                    remote.getIdentityPublicKey(),
                    null,
                    null
            );

            for (ChatMetadataRecord record : listMessages) {
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setId(record.getId());
                chatMessage.setMessage(record.getMessage());
                chatMessage.setDate(DateFormat.getDateTimeInstance().format(record.getDate()));
                chatMessage.setMe(localPK.equals(record.getLocalActorPublicKey()));
                displayMessage(chatMessage);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onMessageReceived(String senderPk,ChatMetadataRecord content) {

        if (remote!=null && remote.getIdentityPublicKey().equals(senderPk)) {
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setId(content.getId());
            chatMessage.setMessage(content.getMessage());
            chatMessage.setDate(DateFormat.getDateTimeInstance().format(content.getDate()));
            chatMessage.setMe(false);
            displayMessage(chatMessage);
        }else {
            Notifications.pushNotification(this,content.getMessage(),senderPk);
        }
    }

    @Override
    public void onActorListReceived(List<ActorProfile> list) {
        Core.getInstance().addRemotesUsers(list);
        if (isSearchingRemote){
            remote = Core.getInstance().getRemoteProfile(remotePk);
            if (remote!=null){
                findViewById(R.id.black_screen).setVisibility(View.GONE);
                findViewById(R.id.chat_screen).setVisibility(View.VISIBLE);
                initControls();
            }
        }
    }

    @Override
    public void onActorRegistered(ActorProfile actorProfile) {

    }

    @Override
    public void onMessageFail(UUID messageId) {
        Log.i(this.getClass().getName(),"onMessageFail: acá tengo que mostrar como que el mensaje falló");
    }

    @Override
    public void onActorOffline(String remotePkGoOffline) {
        Log.i(getClass().getName(),"onActorOffline: "+remotePkGoOffline);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            Core.getInstance().getChatNetworkServicePluginRoot().unSubscribeOnlineEvent(remote.getIdentityPublicKey());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}