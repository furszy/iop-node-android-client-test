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
import java.util.Map;
import java.util.UUID;

public class ChatActivity2 extends AppCompatActivity implements MessageReceiver {

    private EditText messageET;
    private RecyclerView messagesContainer;
    private Button sendBtn;
    private ChatAdapter2 adapter;
    private ArrayList<ChatMessage> chatHistory;
    private ActorProfile remote;


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
                Core.getInstance().getChatNetworkServicePluginRoot().requestActorProfilesList(10000, 0);
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

        }

        Core.getInstance().setReceiver(this);

    }

    private void initControls() {
        messagesContainer = (RecyclerView) findViewById(R.id.messagesContainer);
        messagesContainer.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
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
                            UUID mensajeId = Core.getInstance().getChatNetworkServicePluginRoot().sendNewMessage(
                                    Core.getInstance().getInstance().getProfile(),
                                    remote,
                                    messageText,
                                    true
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

    private void loadDummyHistory(){

        chatHistory = new ArrayList<ChatMessage>();

        ChatMessage msg = new ChatMessage();
        msg.setId(UUID.randomUUID());
        msg.setMe(false);
        msg.setMessage("Hola");
        msg.setDate(DateFormat.getDateTimeInstance().format(new Date()));
        chatHistory.add(msg);
        ChatMessage msg1 = new ChatMessage();
        msg1.setId(UUID.randomUUID());
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
        if (remote!=null && remote.getIdentityPublicKey().equals(senderPk)) {
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setId(UUID.randomUUID());//dummy
            chatMessage.setMessage(content);
            chatMessage.setDate(DateFormat.getDateTimeInstance().format(new Date()));
            chatMessage.setMe(false);
            displayMessage(chatMessage);
        }else {
            Notifications.pushNotification(this,content,senderPk);
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

}