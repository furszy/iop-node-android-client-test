package org.iop.ns.chat;

import com.bitdubai.fermat_api.layer.all_definition.common.system.annotations.NeededAddonReference;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.LayerReference;
import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginDeveloperReference;
import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginReference;
import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginVersionReference;
import com.bitdubai.fermat_api.layer.all_definition.enums.*;
import com.bitdubai.fermat_api.layer.all_definition.events.EventSource;
import com.bitdubai.fermat_api.layer.all_definition.network_service.enums.NetworkServiceType;
import com.bitdubai.fermat_api.layer.all_definition.util.Version;
import com.bitdubai.fermat_api.layer.core.PluginInfo;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantCreateDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantOpenDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.DatabaseNotFoundException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.clients.exceptions.CantSendMessageException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.DiscoveryQueryParameters;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.abstract_classes.AbstractActorNetworkService;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.entities.NetworkServiceMessage;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.exceptions.ActorAlreadyRegisteredException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.exceptions.CantRegisterActorException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.ActorProfile;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.util.GsonProvider;

import org.iop.ns.chat.structure.ChatMetadataRecord;
import org.iop.ns.chat.structure.ChatMetadataRecordDAO;
import org.iop.ns.chat.structure.ChatNetworkServiceDataBaseConstants;
import org.iop.ns.chat.structure.ChatNetworkServiceDatabaseFactory;
import org.iop.ns.chat.structure.ContactsDAO;
import org.iop.ns.chat.structure.test.MessageReceiver;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Gabriel Araujo 15/02/16.
 */
@PluginInfo(difficulty = PluginInfo.Dificulty.HIGH, maintainerMail = "acosta_rodrigo@hotmail.com", createdBy = "acostarodrigo", layer = Layers.NETWORK_SERVICE, platform = Platforms.CHAT_PLATFORM, plugin = Plugins.CHAT_NETWORK_SERVICE)
public class ChatNetworkServicePluginRoot extends AbstractActorNetworkService {


    private Database dataBaseCommunication;
    private ChatMetadataRecordDAO chatMetadataRecordDAO;
    private ContactsDAO contactsDAO;
    private MessageReceiver messageReceiver;

    private boolean stressApp = false;

    //esto se que va en db pero bueno, cero ganas de hacer una db despues de 10 horas laburando
    private HashMap<UUID,String> actorOnlineEventSubscribed;

    /**
     * Constructor with parameters
     */
    public ChatNetworkServicePluginRoot() {
        super(
                new PluginVersionReference(new Version()),
                EventSource.NETWORK_SERVICE_CHAT,
                NetworkServiceType.CHAT
        );
        //this is only
        this.pluginVersionReference.setPluginDeveloperReference(
                new PluginDeveloperReference(
                        new PluginReference(
                                new LayerReference(Layers.NETWORK_SERVICE),Plugins.CHAT_ACTOR),
                        Developers.BITDUBAI));

    }


    @Override
    protected void onActorNetworkServiceStart() {

        try {

            initializeDb();

            chatMetadataRecordDAO = new ChatMetadataRecordDAO(dataBaseCommunication);
            contactsDAO = new ContactsDAO(dataBaseCommunication,pluginFileSystem,pluginId);

            actorOnlineEventSubscribed = new HashMap<>();

            //declare a schedule to process waiting request message
//            this.startTimer();


        } catch (Exception e) {
            reportError(UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
        }


    }

    /**
     * This method initialize the database
     */
    private void initializeDb(){

        UUID hardcodedPluginId = UUID.fromString("2d54279f-8b3e-4330-9338-0bc3837a0e92");

        try {
            /*
             * Open new database connection
             */
            this.dataBaseCommunication = this.pluginDatabaseSystem.openDatabase(hardcodedPluginId, ChatNetworkServiceDataBaseConstants.DATA_BASE_NAME);

        } catch (CantOpenDatabaseException cantOpenDatabaseException) {

            /*
             * The database exists but cannot be open. I can not handle this situation.
             */
            reportError(UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN, cantOpenDatabaseException);

        } catch (DatabaseNotFoundException e) {

            /*
             * The database no exist may be the first time the plugin is running on this device,
             * We need to create the new database
             */
            ChatNetworkServiceDatabaseFactory communicationNetworkServiceDatabaseFactory = new ChatNetworkServiceDatabaseFactory(pluginDatabaseSystem,getErrorManager());

            try {

                /*
                 * We create the new database
                 */
                this.dataBaseCommunication = communicationNetworkServiceDatabaseFactory.createDatabase(hardcodedPluginId, ChatNetworkServiceDataBaseConstants.DATA_BASE_NAME);

            } catch (CantCreateDatabaseException cantOpenDatabaseException) {

                /*
                 * The database cannot be created. I can not handle this situation.
                 */
                reportError(UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, cantOpenDatabaseException);

            }
        }

    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    public void onNewMessageReceived(NetworkServiceMessage newFermatMessageReceive) {
        try {
            System.out.println("----------------------------\n" + "CONVIERTIENDO MENSAJE ENTRANTE A GSON: " + newFermatMessageReceive.getContent() + "\n-------------------------------------------------");

            ChatMetadataRecord chatRecord = GsonProvider.getGson().fromJson(newFermatMessageReceive.getContent(), ChatMetadataRecord.class);
            chatMetadataRecordDAO.persistMessage(chatRecord);

            if (messageReceiver!=null){


                messageReceiver.onMessageReceived(newFermatMessageReceive.getSenderPublicKey(),chatRecord);
            }

        } catch (Exception e) {

            /**
             * TODO: this is only for test the stress app
             */
            if(stressApp){
                ChatMetadataRecord chatRecord = GsonProvider.getGson().fromJson(newFermatMessageReceive.getContent(), ChatMetadataRecord.class);
                if (messageReceiver!=null){
                    messageReceiver.onMessageReceived(newFermatMessageReceive.getSenderPublicKey(),chatRecord);
                }
            } else {
                System.err.println("Message received: "+newFermatMessageReceive);
                e.printStackTrace();
                reportError(UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            }

        }
    }

    @Override
    public void onSentMessage(UUID messageId) {

        System.out.println("sent id = "+messageId);
    }

    public String getNetWorkServicePublicKey() {
        return getIdentity().getPublicKey();
    }

    public UUID sendMessage(final String message,
                            final String identityPublicKey,
                            final String actorPublicKey) {

        try {
            ActorProfile sender = new ActorProfile();
            sender.setActorType(Actors.CHAT.getCode());
            sender.setIdentityPublicKey(identityPublicKey);

            ActorProfile receiver = new ActorProfile();
            receiver.setActorType(Actors.CHAT.getCode());
            receiver.setIdentityPublicKey(actorPublicKey);

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

//            UUID messageId = UUID.randomUUID();

            ChatMetadataRecord messageToSave = new ChatMetadataRecord(
                    identityPublicKey,
                    actorPublicKey,
                    timestamp,
                    message
            );

            UUID uuid = UUID.randomUUID();

            messageToSave.setId(uuid);

            UUID messageId = sendNewMessage(
                    sender,
                    receiver,
                    GsonProvider.getGson().toJson(messageToSave),
                    //I'll set true for testing
                    true
            );


            chatMetadataRecordDAO.persistMessage(messageToSave);

            return messageId;
        } catch (Exception e) {
            System.out.println(e);
            reportError(UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            return null;
        }
    }

    public List<ChatMetadataRecord> listMessages(String localPk, String remotePk, Integer max, Integer offset) throws Exception {
        return chatMetadataRecordDAO.listMessages(localPk, remotePk, max, offset);
    }

    @Override
    protected void onActorNetworkServiceRegistered() {

        System.out.println("method onNetworkServiceRegistered: chatNS");

    }

    public void setMessageReceiver(MessageReceiver messageReceiver) {
        this.messageReceiver = messageReceiver;
    }

    public void requestActorProfilesList(int max, int offset, String requesterPublicKey) {

        try {
            UUID packageId = discoveryActorProfiles(new DiscoveryQueryParameters(
                    null,
                    NetworkServiceType.ACTOR_CHAT,
                    Actors.CHAT.getCode(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    true,
                    null,
                    max,
                    offset,
                    true
            ), requesterPublicKey);
        } catch (CantSendMessageException e) {
            e.printStackTrace();
        }
    }

    public void requestActorProfile(String name,int max, int offset,String requesterPublicKey) {

        try {
            UUID packageId = discoveryActorProfiles(new DiscoveryQueryParameters(
                    null,
                    NetworkServiceType.ACTOR_CHAT,
                    Actors.CHAT.getCode(),
                    name,
                    null,
                    null,
                    null,
                    null,
                    true,
                    null,
                    max,
                    offset,
                    true
            ), requesterPublicKey);
        } catch (CantSendMessageException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActorRegistered(ActorProfile actorProfile) {

        System.out.println("im registered : "+ actorProfile);
        messageReceiver.onActorRegistered(actorProfile);
    }

    @Override
    public void handleNetworkClientActorListReceivedEvent(UUID packageId, List<ActorProfile> actorProfiles) {

        System.out.println("Chat OnNetworkServiceActorListReceived...");
        if (messageReceiver!=null){
            messageReceiver.onActorListReceived(actorProfiles);
        }
    }

    @Override
    public synchronized void onMessageFail(UUID messageId) {
        super.onMessageFail(messageId);
        messageReceiver.onMessageFail(messageId);
    }

    @Override
    public void onNodeEventArrive(UUID eventPackageId) {
        super.onNodeEventArrive(eventPackageId);
        messageReceiver.onActorOffline(actorOnlineEventSubscribed.get(eventPackageId));
    }

    public void subscribeActorOnlineEvent(String remotePk) throws CantSendMessageException {
        actorOnlineEventSubscribed.put(subscribeActorOnline(remotePk), remotePk);
    }

    public void unSubscribeOnlineEvent(String remotePk) throws CantSendMessageException{
        UUID prevSubscribePackageSent = null;
        for (Map.Entry<UUID, String> entry : actorOnlineEventSubscribed.entrySet()) {
            if (entry.getValue().equals(remotePk)){
                prevSubscribePackageSent = entry.getKey();
                break;
            }
        }
        unSubscribeActorOnline(prevSubscribePackageSent);
    }

    public void registerProfile(ActorProfile actorProfile){
        if (actorProfile!=null) {
            try {
                registerActor(actorProfile, 0, 0);
            } catch (ActorAlreadyRegisteredException e) {
                e.printStackTrace();
            } catch (CantRegisterActorException e) {
                e.printStackTrace();
            }
        }
    }



    public void saveContact(ActorProfile actorProfile) throws Exception {
        contactsDAO.persistContact(actorProfile);
    }

    public List<ActorProfile> getContacts() throws Exception {
        return contactsDAO.listContacts();
    }

    public void setStressApp(boolean stressApp){
        this.stressApp = stressApp;
    }

}
