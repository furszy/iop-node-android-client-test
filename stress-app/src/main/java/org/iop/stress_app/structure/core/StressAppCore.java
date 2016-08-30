package org.iop.stress_app.structure.core;

import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.ErrorManager;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.EventManager;
import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;
import com.bitdubai.fermat_api.layer.all_definition.network_service.enums.NetworkServiceType;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
import com.bitdubai.fermat_osa_addon.layer.linux.database_system.developer.bitdubai.version_1.PluginDatabaseSystemLinuxAddonRoot;
import com.bitdubai.fermat_osa_addon.layer.linux.file_system.developer.bitdubai.version_1.PluginFileSystemLinuxAddonRoot;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.enums.UpdateTypes;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.abstract_classes.AbstractNetworkService;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.ActorProfile;
import com.bitdubai.fermat_pip_addon.layer.platform_service.error_manager.developer.bitdubai.version_1.ErrorManagerPlatformServiceAddonRoot;
import com.bitdubai.fermat_pip_addon.layer.platform_service.event_manager.developer.bitdubai.version_1.EventManagerPlatformServiceAddonRoot;
import com.fermat_p2p_layer.version_1.P2PLayerPluginRoot;

import javafx.application.Platform;
import org.iop.client.version_1.IoPClientPluginRoot;
import org.iop.ns.chat.ChatNetworkServicePluginRoot;
import org.iop.ns.chat.structure.test.MessageReceiver;
import org.iop.stress_app.structure.enums.ReportType;
import org.iop.stress_app.structure.views.SummaryLabel;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by Manuel Perez P. (darkpriestrelative@gmail.com) on 22/08/16.
 */
public class StressAppCore {

    ChatNetworkServicePluginRoot chatNetworkServicePluginRoot;
    IoPClientPluginRoot ioPClientPluginRoot;
    private ActorProfile profile;
    private ActorProfile lastRemoteProfile;

    //TODO: to improve, this is not right
    private SummaryLabel summaryLabel;

    private boolean networkServiceStart = false;
    private boolean actorCreation = false;

    //de acá en más

    /**
     * Layer
     */
    private P2PLayerPluginRoot p2PLayerPluginRoot;

    /**
     * File system
     */
    private PluginFileSystemLinuxAddonRoot pluginFileSystemLinuxAddonRoot;

    /**
     * Error manager
     */
    private ErrorManagerPlatformServiceAddonRoot errorManagerPlatformServiceAddonRoot;

    /**
     * Event manager
     */
    private EventManagerPlatformServiceAddonRoot eventManagerPlatformServiceAddonRoot;

    /**
     *  Database
     */
    private PluginDatabaseSystemLinuxAddonRoot pluginDatabaseSystemAndroidAddonRoot;


    public StressAppCore() {

    }

    public void start(){

        System.out.println("- Starting core ...");

        try {
            //file system
            pluginFileSystemLinuxAddonRoot = new PluginFileSystemLinuxAddonRoot();
            pluginFileSystemLinuxAddonRoot.start();

            //error manager
            errorManagerPlatformServiceAddonRoot = new ErrorManagerPlatformServiceAddonRoot();
            errorManagerPlatformServiceAddonRoot.start();

            //event manager
            eventManagerPlatformServiceAddonRoot = new EventManagerPlatformServiceAddonRoot();
            eventManagerPlatformServiceAddonRoot.setErrorManager((ErrorManager) errorManagerPlatformServiceAddonRoot.getManager());
            eventManagerPlatformServiceAddonRoot.start();

            //Database addon
            pluginDatabaseSystemAndroidAddonRoot = new PluginDatabaseSystemLinuxAddonRoot();
            pluginDatabaseSystemAndroidAddonRoot.start();

            //layer
            p2PLayerPluginRoot = new P2PLayerPluginRoot();
            p2PLayerPluginRoot.setEventManager((EventManager) eventManagerPlatformServiceAddonRoot.getManager());
            p2PLayerPluginRoot.setPluginDatabaseSystem((PluginDatabaseSystem) pluginDatabaseSystemAndroidAddonRoot.getManager());
            System.out.println("Layer pk:"+p2PLayerPluginRoot.getId());
            p2PLayerPluginRoot.start();

            //node
            ioPClientPluginRoot = new IoPClientPluginRoot();
            ioPClientPluginRoot.setPluginFileSystem((PluginFileSystem) pluginFileSystemLinuxAddonRoot.getManager());
            ioPClientPluginRoot.setEventManager((EventManager) eventManagerPlatformServiceAddonRoot.getManager());
            ioPClientPluginRoot.setP2PLayerManager(p2PLayerPluginRoot);
            ioPClientPluginRoot.start();

//            //console ns
//            if(networkServiceStart){
//                chatNetworkServicePluginRoot = new ChatNetworkServicePluginRoot();
//                chatNetworkServicePluginRoot.setP2PLayerManager(p2PLayerPluginRoot);
//                chatNetworkServicePluginRoot.setPluginFileSystem((PluginFileSystem) pluginFileSystemLinuxAddonRoot.getManager());
//                chatNetworkServicePluginRoot.setPluginDatabaseSystem((PluginDatabaseSystem) pluginDatabaseSystemAndroidAddonRoot.getManager());
//                chatNetworkServicePluginRoot.setErrorManager((ErrorManager) errorManagerPlatformServiceAddonRoot.getManager());
//                chatNetworkServicePluginRoot.start();
//            }

            System.out.println("FERMAT - Network StressAppCore - started satisfactory...");

            while (!ioPClientPluginRoot.isConnected()) {
                System.out.println("Not connected yet - Thread Id: "+Thread.currentThread().getId());
                Thread.sleep(100);
            }
            System.out.println("******** Client connected ********");
            if(summaryLabel!=null){
                reportClientConnected();
            }

            //actorMessengerManager = new ActorMessengerManager(this);

            //Register an actor for testing
//            if(actorCreation){
//                profile = new ActorProfile();
//                profile.setIdentityPublicKey(UUID.randomUUID().toString());
//                System.out.println("I will try to register an actor with pk " + profile.getIdentityPublicKey());
//                profile.setActorType(Actors.CHAT.getCode());
//                profile.setName("Juan_" + Thread.currentThread().getId());
//                profile.setAlias("Alias chat");
//                //This represents a valid image
//                profile.setPhoto(new byte[]{-119, 80, 78, 71, 13, 10, 26, 10, 0, 0, 0, 13, 73, 72, 68, 82,
//                        0, 0, 0, 15, 0, 0, 0, 15, 8, 6, 0, 0, 0, 59, -42, -107,
//                        74, 0, 0, 0, 64, 73, 68, 65, 84, 120, -38, 99, 96, -62, 14, -2,
//                        99, -63, 68, 1, 100, -59, -1, -79, -120, 17, -44, -8, 31, -121, 28, 81,
//                        26, -1, -29, 113, 13, 78, -51, 100, -125, -1, -108, 24, 64, 86, -24, -30,
//                        11, 101, -6, -37, 76, -106, -97, 25, 104, 17, 96, -76, 77, 97, 20, -89,
//                        109, -110, 114, 21, 0, -82, -127, 56, -56, 56, 76, -17, -42, 0, 0, 0,
//                        0, 73, 69, 78, 68, -82, 66, 96, -126});
//                profile.setNsIdentityPublicKey(chatNetworkServicePluginRoot.getNetWorkServicePublicKey());
//                profile.setExtraData("Test extra data");
//                chatNetworkServicePluginRoot.registerActor(profile, 0, 0);
//            }

        }catch (Exception e){
            System.out.println("StressAppCore Exception "+e);
            e.printStackTrace();
        }
    }

    /**
     * Chequear si nos sirven todos estos metodos...
     */
    public void setProfile(ActorProfile profile) {
        if (profile!=null) {
            this.profile = profile;
            chatNetworkServicePluginRoot.registerProfile(profile);
        }else{
            try {
                chatNetworkServicePluginRoot.updateRegisteredActor(profile, UpdateTypes.FULL);
            } catch (Exception e){
                System.out.println("StressAppCore Exception "+e);
                e.printStackTrace();
            }
        }
    }
    public ChatNetworkServicePluginRoot getChatNetworkServicePluginRoot() {
        return chatNetworkServicePluginRoot;
    }
    public void setReceiver(MessageReceiver eventReceiver){
        chatNetworkServicePluginRoot.setMessageReceiver(eventReceiver);
    }
    public ActorProfile getProfile() {
        return profile;
    }
    public void setLastRemoteProfile(ActorProfile lastRemoteProfile) {
        this.lastRemoteProfile = lastRemoteProfile;
    }
    public ActorProfile getLastRemoteProfile() {
        return this.lastRemoteProfile;
    }
    public boolean isConnected(){
        return ioPClientPluginRoot.isConnected();
    }
    public void shutdown() {
        ioPClientPluginRoot.disconnect();
    }
    public void setNetworkServiceStart(boolean networkServiceStart) {
        this.networkServiceStart = networkServiceStart;
    }
    /**
     *  Fin de metodos a chequear..
     */


    /**
     * De acá en adelante...
     */
    public void addLayer(AbstractNetworkService ns) {
        ns.setP2PLayerManager(p2PLayerPluginRoot);
    }
    public void addFileSystem(AbstractNetworkService ns) {
        ns.setPluginFileSystem((PluginFileSystem) pluginFileSystemLinuxAddonRoot.getManager());
    }
    public void addErrorManager(AbstractNetworkService ns) {
        ns.setErrorManager((ErrorManager) errorManagerPlatformServiceAddonRoot.getManager());
    }
    public void addDatabase(AbstractNetworkService networkService) {
        networkService.setPluginDatabaseSystem((PluginDatabaseSystem) pluginDatabaseSystemAndroidAddonRoot.getManager());
    }

    /**
     * The following methods is to notify to the UI that a client is connected,
     * This is for test and requires a proper implementation.
     */
    public void setSummaryLabel(SummaryLabel summaryLabel){
        this.summaryLabel=summaryLabel;
    }

    private void reportClientConnected(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                summaryLabel.report(ReportType.CLIENT_CONNECTED);
            }
        });
    }
}
