package org.iop.stress_app.structure.core;

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

    public StressAppCoreManager() {
        this.stressAppCoreList = new ArrayList<>();
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
            stressAppCore.start();
            if(networkServiceStart){
                StressAppNetworkService stressAppNetworkService = new StressAppNetworkService(stressAppCore);
                for(int i=0; i<nsToStart; i++){
                    stressAppNetworkService.addNetworkService("ChatNetworkServicePluginRoot");
                }
                stressAppNetworkService.startNetworkServices();
                //after we created the NS, we'll gonna try to create actors
                if(actorCreation){
                    StressAppActor stressAppActor = new StressAppActor(stressAppNetworkService);
                    for(int j=0; j<actorsToCreate ; j++){
                        stressAppActor.addActor();
                    }
                    stressAppActor.createAndRegisterActors();
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
