package org.iop.stress_app.structure.core;

import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.exceptions.CantSendMessageException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.ActorProfile;
import javafx.application.Platform;
import org.iop.ns.chat.ChatNetworkServicePluginRoot;
import org.iop.ns.chat.structure.ChatMetadataRecord;
import org.iop.ns.chat.structure.test.MessageReceiver;
import org.iop.stress_app.structure.enums.ReportType;
import org.iop.stress_app.structure.exceptions.CannotSelectARandomActorException;
import org.iop.stress_app.structure.utils.IoPBytesArray;
import org.iop.stress_app.structure.views.SummaryLabel;

import java.util.*;

/**
 * Created by Manuel Perez P. (darkpriestrelative@gmail.com) on 25/08/16.
 */
public class StressAppActor implements MessageReceiver{

    /**
     * Default values
     */
    private final static int MAX = 1000;
    private final static int OFFSET = 0;
    private final static int RESPONDS = 4;
    private final static int MAX_LOOPS_ON_RANDOM_ACTORS = 10;

    /**
     * Represents the StressAppNetworkService instance
     */
    private StressAppNetworkService stressAppNetworkService;

    /**
     * Represents the list of actor profiles created
     */
    private List<ActorProfile> actorProfileList;

    /**
     * Represents the Map with the NS and actorList
     */
    private Map<ChatNetworkServicePluginRoot, List<ActorProfile>> nsMap;

    private Map<String , ChatNetworkServicePluginRoot> nsPublicKeyMap;

    private Map<String, Integer> messagesCount;

    private Map<String, ActorProfile> actorsMap;

    private boolean messageTestStarted = false;

    private SummaryLabel summaryLabel;

    private int randomSelectorIndex = 0;

    /**
     * Counters
     */
    private int nsStarted = 0;
    private int actorsCreated = 0;
    private int actorRegistered = 0;
    private int messagesSent = 0;
    private int failedMessages = 0;
    private int successfulMessages = 0;
    private int receivedMessages = 0;

    /**
     * Default constructor with parameters
     * @param stressAppNetworkService
     */
    public StressAppActor(
            StressAppNetworkService stressAppNetworkService,
            SummaryLabel summaryLabel) {
        this.stressAppNetworkService = stressAppNetworkService;
        this.actorProfileList = new ArrayList<>();
        this.nsMap = new HashMap<>();
        this.nsPublicKeyMap = new HashMap<>();
        this.summaryLabel = summaryLabel;
    }

    /**
     * This method creates an actor profile and adds it to a List
     */
    public void addActor(){
        actorProfileList.add(new ActorProfile());
    }

    /**
     * This method creates and register actor for each NS.
     */
    public void createAndRegisterActors(){
        stressAppNetworkService.getAbstractNetworkServiceList().forEach((type,ns)->{
            System.out.println("Network Service type: "+type);
            nsPublicKeyMap.put(ns.getPublicKey(), (ChatNetworkServicePluginRoot) ns);
            ((ChatNetworkServicePluginRoot) ns).setMessageReceiver(this);
            int actorCounter = 0;
            for(ActorProfile actor : actorProfileList){
                createAndRegisterActor(actor,(ChatNetworkServicePluginRoot) ns, actorCounter);
                actorCounter++;
                actorsCreated++;
                report(ReportType.ACTOR_CREATED);
            }
            nsStarted++;
            report(ReportType.NS_STARED);
        });
    }

    /**
     * This method creates an actor and register
     * @param profile
     * @param networkServicePluginRoot
     */
    private void createAndRegisterActor(
            ActorProfile profile,
            ChatNetworkServicePluginRoot networkServicePluginRoot,
            int actorCounter){
        try{
            long threadId = Thread.currentThread().getId();
            profile.setIdentityPublicKey(UUID.randomUUID().toString());
            System.out.println("I will try to register an actor with pk " + profile.getIdentityPublicKey());
            profile.setActorType(Actors.CHAT.getCode());
            profile.setName("Chat Actor " + threadId +"- "+actorCounter);
            profile.setAlias("Alias chat " + threadId +"- "+actorCounter);
            //This represents a valid image
            profile.setPhoto(IoPBytesArray.getIoPBytesArray());
            profile.setNsIdentityPublicKey(networkServicePluginRoot.getPublicKey());
            profile.setExtraData("Test extra data");
            networkServicePluginRoot.registerActor(profile, 0, 0);
            List<ActorProfile> actorList = nsMap.get(networkServicePluginRoot);
            if(actorList ==null){
                actorList = new ArrayList<>();
            }
            actorList.add(profile);
            nsMap.put(networkServicePluginRoot, actorList);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * This method request an actor list
     */
    public void requestActorList(){
        nsMap.keySet().forEach(ns->
                nsMap.get(ns).forEach(profile->{
                    ns.requestActorProfilesList(MAX,OFFSET,profile.getNsIdentityPublicKey());
                    report(ReportType.REQUEST_LIST_SENT);
                        }));

    }

    public void messageTest(List<ActorProfile> list){
        if(messageTestStarted){
            return ;
        }
        messageTestStarted = true;
        messagesCount = new HashMap<>();
        actorsMap = new HashMap<>();
        list.forEach(actor->actorsMap.put(actor.getIdentityPublicKey(), actor));
        ActorProfile actorReceiver;
        String messageToSend;
        for(ChatNetworkServicePluginRoot ns : nsMap.keySet()){
            for(ActorProfile actorSender : nsMap.get(ns)){
                try{
                    actorReceiver = selectRandomActorProfile(list, actorSender);
                    messageToSend = "StressAppActor sends you a "+generateRandomHexString();
                    System.out.println("**** StressAppActor "+actorSender.getIdentityPublicKey()+"\n is trying to send: "+messageToSend+" to "+actorReceiver.getIdentityPublicKey());
                    ns.sendMessage(messageToSend, actorSender.getIdentityPublicKey(), actorReceiver.getIdentityPublicKey());
                    messagesCount.put(actorSender.getIdentityPublicKey(), 0);
                    actorsMap.put(actorSender.getIdentityPublicKey(),actorSender);
                    messagesSent++;
                    report(ReportType.MESSAGE_SENT);
                    System.out.println("*** StressAppActor has registered "+messagesSent+" messages sent");
                } catch (Exception e){
                    System.out.println(actorSender.getIdentityPublicKey()+" cannot send a message");
                    e.printStackTrace();
                    failedMessages++;
                    report(ReportType.FAILED_MESSAGES);
                }
            }
        }
        messageTestStarted = false;
    }

    /**
     * This method selects and returns a random from the given list
     * @param list
     * @return
     */
    private ActorProfile selectRandomActorProfile(List<ActorProfile> list,
                                                  ActorProfile actorSender)
            throws CannotSelectARandomActorException {
        if(randomSelectorIndex>=MAX_LOOPS_ON_RANDOM_ACTORS){
            throw new CannotSelectARandomActorException("The number of tries, "+randomSelectorIndex+" is upper than the limit");
        }
        int randomIndex = (int) (((double)list.size())*Math.random());
        ActorProfile actorSelected = list.get(randomIndex);
        if(actorSelected.getIdentityPublicKey().equals(actorSender.getIdentityPublicKey())){
            randomSelectorIndex++;
            actorSelected = selectRandomActorProfile(list, actorSender);
        }
        /*if(actorSelected.getNsIdentityPublicKey().equals(actorSender.getNsIdentityPublicKey())){
            randomSelectorIndex++;
            actorSelected = selectRandomActorProfile(list, actorSender);
        }*/
        return actorSelected;
    }

    /**
     * This method creates a random Hex String
     * @return
     */
    private String generateRandomHexString(){
        return Long.toHexString(Double.doubleToLongBits(Math.random()));
    }

    /**
     * The following methods will implement the MessageReceiver interface and will be used to
     * get the NS messages.
     */

    @Override
    public void onMessageReceived(String sender, ChatMetadataRecord chatMetadataRecord) {
        System.out.println("*** StressAppActor has received a message from "+sender);
        receivedMessages++;
        System.out.println("*** StressAppActor has registered "+receivedMessages+" received messages");
        report(ReportType.RECEIVED_MESSAGE);
        String receiverPk = chatMetadataRecord.getRemoteActorPublicKey();
        int responds = messagesCount.get(receiverPk);
        if(responds<RESPONDS){
            ActorProfile actorSender = actorsMap.get(receiverPk);
            ActorProfile actorReceiver = actorsMap.get(chatMetadataRecord.getLocalActorPublicKey());
            ChatNetworkServicePluginRoot networkServicePluginRoot = nsPublicKeyMap.get(actorSender.getNsIdentityPublicKey());
            String messageToSend = "StressAppActor responds you a "+generateRandomHexString();
            System.out.println("*** StressAppActor is trying to respond "+messageToSend);
            try {
                networkServicePluginRoot.sendNewMessage(actorSender, actorReceiver, messageToSend, false);
                messagesSent++;
                System.out.println("*** StressAppActor has registered "+messagesSent+" messages sent");
                report(ReportType.MESSAGE_SENT);
            } catch (CantSendMessageException e) {
                System.out.println(actorSender.getIdentityPublicKey()+" cannot respond a message");
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActorListReceived(List<ActorProfile> list) {
        System.out.println(list);
        if(!messageTestStarted){
            messageTest(list);
        }
        report(ReportType.REQUEST_LIST_RECEIVED);
    }

    @Override
    public void onActorRegistered(ActorProfile actorProfile) {
        actorRegistered++;
        System.out.println("*** StressAppActor has registered "+actorRegistered+" actors");
        report(ReportType.ACTOR_REGISTERED);
    }

    @Override
    public void onMessageFail(UUID messageId) {
        failedMessages++;
        System.out.println("*** StressAppActor has registered "+failedMessages+" failed messages");
        report(ReportType.FAILED_MESSAGES);

    }

    @Override
    public void onActorOffline(String remotePkGoOffline) {
        //TODO: to implement and notify to UI
    }

    private void report(ReportType reportType){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                summaryLabel.report(reportType);
            }
        });
    }

}
