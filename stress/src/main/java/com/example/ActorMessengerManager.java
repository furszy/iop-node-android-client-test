package com.example;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.exceptions.CantSendMessageException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.ActorProfile;

import org.iop.ns.chat.structure.test.MessageReceiver;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by mati on 18/08/16.
 */
public class ActorMessengerManager implements MessageReceiver {

    private Core core;

    private List<ActorProfile> remoteProfiles;
    private int count = 0;

    public ActorMessengerManager(Core core) {
        this.core = core;
        remoteProfiles = new ArrayList<>();
        core.setReceiver(this);
    }

    public void startActorStressTest(){
        core.chatNetworkServicePluginRoot.requestActorProfilesList();
        synchronized (this) {
            while (remoteProfiles.isEmpty()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        ActorProfile remoteProfile = pickRamdomProfile();
        try {
            count++;
            core.chatNetworkServicePluginRoot.sendNewMessage(core.getProfile(),remoteProfile,"holas "+count);
        } catch (CantSendMessageException e) {
            e.printStackTrace();
        }

    }

    private ActorProfile pickRamdomProfile(){
        Random random = new Random();
        return remoteProfiles.get(random.nextInt(remoteProfiles.size()));
    }

    public void addRemotesProfiles(List<ActorProfile> list){
        synchronized (this) {
            remoteProfiles.addAll(list);
            notifyAll();
        }
    }


    @Override
    public void onMessageReceived(String sender, String chatMetadataRecord) {
        System.out.println(chatMetadataRecord);

        try {
            core.chatNetworkServicePluginRoot.sendNewMessage(core.getProfile(),getRemoteProfile(sender),"holas "+count);
        } catch (CantSendMessageException e) {
            e.printStackTrace();
        }
    }

    private ActorProfile getRemoteProfile(String pk){
        for (ActorProfile remoteProfile : remoteProfiles) {
            if (remoteProfile.getIdentityPublicKey().equals(pk)){
                return remoteProfile;
            }
        }
        return null;
    }

    @Override
    public void onActorListReceived(List<ActorProfile> list) {
        addRemotesProfiles(list);
    }

    @Override
    public void onActorRegistered(ActorProfile actorProfile) {
        startActorStressTest();
    }
}
