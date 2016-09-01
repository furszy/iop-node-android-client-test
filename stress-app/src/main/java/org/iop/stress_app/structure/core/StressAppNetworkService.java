package org.iop.stress_app.structure.core;

import com.bitdubai.fermat_api.CantStartPluginException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.ErrorManager;
import com.bitdubai.fermat_api.layer.all_definition.network_service.enums.NetworkServiceType;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.abstract_classes.AbstractNetworkService;
import org.iop.ns.chat.ChatNetworkServicePluginRoot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mati on 23/08/16.
 * Esta clase es como la layer solo que genera los ns, le carga los managaer (db,filesystem,errorManager,etc) y se lo agrega a un Core corriendo
 *
 */
public class StressAppNetworkService {

    /**
     * Core instance
     * Es una instancia de el core de toda la app, va a existir uno cliente por eso es uno solo
     */
    private StressAppCore stressAppCore;
    /**
     * NetworkServices
     */
    private HashMap<NetworkServiceType,AbstractNetworkService> abstractNetworkServiceList;

    /**
     * Represents a list of ChatNetworkServicePluginRoot in case that we want to create multiple instances of this NS
     */
    private List<ChatNetworkServicePluginRoot> chatNetworkServicePluginRootList;

    private List<ChatNetworkServicePluginRoot> startedChatNS;

    public StressAppNetworkService(StressAppCore stressAppCore) {
        abstractNetworkServiceList = new HashMap<>();
        this.stressAppCore = stressAppCore;
        this.chatNetworkServicePluginRootList = new ArrayList<>();
        this.startedChatNS = new ArrayList<>();
    }


    /**
     *
     * @param networkServicePluginRootClassName
     */
    public void addNetworkService(String networkServicePluginRootClassName){
        //lo pongo así por ahora para ir probando
        if (networkServicePluginRootClassName.equals("ChatNetworkServicePluginRoot")){
            ChatNetworkServicePluginRoot chatNetworkServicePluginRoot = new ChatNetworkServicePluginRoot();
            abstractNetworkServiceList.put(chatNetworkServicePluginRoot.getNetworkServiceType(),chatNetworkServicePluginRoot);
        }else{
//            AbstractNetworkService abstractNetworkService = loadNetworkService(networkServicePluginRootClassName);
        }
        //Add new chat NS to the list
        chatNetworkServicePluginRootList.add(new ChatNetworkServicePluginRoot());
    }

    public void startNetworkServices(){
        for (AbstractNetworkService abstractNetworkService : abstractNetworkServiceList.values()) {
            try {
                //todo: esto no deberia hacer así.., mejorar más adelante
                stressAppCore.addLayer(abstractNetworkService);
                stressAppCore.addErrorManager(abstractNetworkService);
                stressAppCore.addDatabase(abstractNetworkService);
                stressAppCore.addFileSystem(abstractNetworkService);
                abstractNetworkService.start();
            } catch (CantStartPluginException e) {
                e.printStackTrace();
            }
        }
    }

    public void startChatNetworkServices(){
        chatNetworkServicePluginRootList.forEach(nsChat->{
            try {
                stressAppCore.addLayer(nsChat);
                stressAppCore.addErrorManager(nsChat);
                stressAppCore.addDatabase(nsChat);
                stressAppCore.addFileSystem(nsChat);
                nsChat.setStressApp(true);
                nsChat.start();
                startedChatNS.add(nsChat);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * This method returns the abstractNetworkServiceList
     * @return
     */
    public HashMap<NetworkServiceType, AbstractNetworkService> getAbstractNetworkServiceList() {
        return abstractNetworkServiceList;
    }

    /**
     * This method returns the chatNetworkServicePluginRootList
     * @return
     */
    public List<ChatNetworkServicePluginRoot> getChatNetworkServicePluginRootList() {
        return startedChatNS;
    }

    private void loadAllNetworkServicesInExtDirectory(){

    }

}
