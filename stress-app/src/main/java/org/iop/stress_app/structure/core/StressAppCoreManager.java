package org.iop.stress_app.structure.core;

import org.iop.stress_app.structure.views.SummaryLabel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Manuel Perez P. (darkpriestrelative@gmail.com) on 22/08/16.
 */
public class StressAppCoreManager {

    private List<StressAppCore> stressAppCoreList;
    private int coreCount;

    private ExecutorService threadPoolExecutor;

    private boolean networkServiceStart = false;
    private int nsToStart = 0;
    private boolean actorCreation = false;
    private int actorsToCreate = 0;

    private int total = 0;

    private SummaryLabel summaryLabel;

    public StressAppCoreManager() {
        this.stressAppCoreList = new ArrayList<>();
    }

    public StressAppCoreManager(SummaryLabel summaryLabel) {
        this.stressAppCoreList = new ArrayList<>();
        this.summaryLabel = summaryLabel;
    }

    public void setCoreCount(int coreCount){
        this.coreCount = coreCount;
    }

    public void startStressTest(){
        for (int i=0;i<coreCount;i++){
            stressAppCoreList.add(new StressAppCore());
        }
        threadPoolExecutor = Executors.newFixedThreadPool(coreCount);
        for (StressAppCore stressAppCore : stressAppCoreList) {
            //stressAppCore.setNetworkServiceStart(networkServiceStart);
            threadPoolExecutor.submit(new CoreTask(stressAppCore));
        }
    }

    public void addConnections(int addConnections) {
        coreCount+=addConnections;
        ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(addConnections);
        for (int i=0;i<addConnections;i++){
            StressAppCore stressAppCore = new StressAppCore();
            stressAppCoreList.add(stressAppCore);
            threadPoolExecutor.submit(new CoreTask(stressAppCore));

        }
    }


    class CoreTask implements Runnable{

        private StressAppCore stressAppCore;

        public CoreTask(StressAppCore stressAppCore) {
            this.stressAppCore = stressAppCore;
        }

        @Override
        public void run() {
            if(summaryLabel!=null){
                stressAppCore.setSummaryLabel(summaryLabel);
            }
            stressAppCore.start();
            if(networkServiceStart){
                StressAppNetworkService stressAppNetworkService = new StressAppNetworkService(stressAppCore);
                for(int i=0; i<nsToStart; i++){
                    stressAppNetworkService.addNetworkService("ChatNetworkServicePluginRoot");
                }
                //stressAppNetworkService.startNetworkServices();
                //Starting only NS chat
                stressAppNetworkService.startChatNetworkServices();
                //after we created the NS, we'll gonna try to create actors
                if(actorCreation){
                    StressAppActor stressAppActor = new StressAppActor(
                            stressAppNetworkService,
                            summaryLabel);
                    for(int j=0; j<actorsToCreate ; j++){
                        stressAppActor.addActor();
                        total++;
                    }
                    stressAppActor.createAndRegisterActors();
                    //Now we gonna request a list of actors - false indicates that we want to request a list by client
                    stressAppActor.requestActorList(false);

                }
            }
        }
    }

    public void stopStressTest(){
        for (StressAppCore stressAppCore : stressAppCoreList) {
            stressAppCore.shutdown();
        }
        threadPoolExecutor.shutdown();
    }

    public void setNsToStart(int nsToStart){
        this.nsToStart = nsToStart;
    }

    public void setActorsToCreate(int actorsToCreate){
        this.actorsToCreate = actorsToCreate;
    }

    public void setNetworkServiceStart(boolean networkServiceStart) {
        this.networkServiceStart = networkServiceStart;
    }

    public void setActorCreation(boolean actorCreation) {
        this.actorCreation = actorCreation;
    }
}
