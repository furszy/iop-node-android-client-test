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
    private boolean actorCreation = false;

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
        }
    }

    public void stopStressTest(){
        for (StressAppCore stressAppCore : stressAppCoreList) {
            stressAppCore.shutdown();
        }
        threadPoolExecutor.shutdown();
    }

    public void setNetworkServiceStart(boolean networkServiceStart) {
        this.networkServiceStart = networkServiceStart;
    }

    public void setActorCreation(boolean actorCreation) {
        this.actorCreation = actorCreation;
    }
}
