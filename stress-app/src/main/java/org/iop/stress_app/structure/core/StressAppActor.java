package org.iop.stress_app.structure.core;

import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.ActorProfile;
import org.iop.ns.chat.ChatNetworkServicePluginRoot;
import org.iop.stress_app.structure.utils.IoPBytesArray;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Manuel Perez P. (darkpriestrelative@gmail.com) on 25/08/16.
 */
public class StressAppActor {

    /**
     * Represents the StressAppNetworkService instance
     */
    private StressAppNetworkService stressAppNetworkService;

    /**
     * Represents the list of actor profiles created
     */
    private List<ActorProfile> actorProfileList;

    /**
     * Default constructor with parameters
     * @param stressAppNetworkService
     */
    public StressAppActor(StressAppNetworkService stressAppNetworkService) {
        this.stressAppNetworkService = stressAppNetworkService;
        this.actorProfileList = new ArrayList<>();
    }

    /**
     * This method creates an actor profile and adds it to a List
     */
    public void addActor(){
        actorProfileList.add(new ActorProfile());
    }

    public void createAndRegisterActors(){
        stressAppNetworkService.getAbstractNetworkServiceList().forEach((type,ns)->{
            System.out.println("Network Service type: "+type);
            actorProfileList.forEach(actor->createAndRegisterActor(actor,(ChatNetworkServicePluginRoot) ns));
        });
    }

    /**
     * This method creates an actor and register
     * @param profile
     * @param networkServicePluginRoot
     */
    private void createAndRegisterActor(
            ActorProfile profile,
            ChatNetworkServicePluginRoot networkServicePluginRoot){
        try{
            long threadId = Thread.currentThread().getId();
            profile.setIdentityPublicKey(UUID.randomUUID().toString());
            System.out.println("I will try to register an actor with pk " + profile.getIdentityPublicKey());
            profile.setActorType(Actors.CHAT.getCode());
            profile.setName("Chat Actor " + threadId);
            profile.setAlias("Alias chat " + threadId);
            //This represents a valid image
            profile.setPhoto(IoPBytesArray.getIoPBytesArray());
            profile.setNsIdentityPublicKey(networkServicePluginRoot.getPublicKey());
            profile.setExtraData("Test extra data");
            networkServicePluginRoot.registerActor(profile, 0, 0);
        } catch(Exception e){
            e.printStackTrace();
        }

    }
    
}
