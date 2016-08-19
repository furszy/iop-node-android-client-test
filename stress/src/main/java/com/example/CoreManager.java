package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by mati on 18/08/16.
 */
public class CoreManager {

    private List<Core> coreList;
    private int coreCount;


    private ExecutorService threadPoolExecutor;

    public CoreManager() {
        this.coreList = new ArrayList<>();
    }

    public void setCoreCount(int coreCount){
        this.coreCount = coreCount;
    }

    public void startStressTest(){
        for (int i=0;i<coreCount;i++){
            coreList.add(new Core());
        }
        threadPoolExecutor = Executors.newFixedThreadPool(coreCount);
        for (Core core : coreList) {
            threadPoolExecutor.submit(new CoreTask(core));
        }
    }

    public void addConnections(int addConnections) {
        coreCount+=addConnections;
        ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(addConnections);
        for (int i=0;i<addConnections;i++){
            Core core = new Core();
            coreList.add(core);
            threadPoolExecutor.submit(new CoreTask(core));

        }
    }


    class CoreTask implements Runnable{

        private Core core;

        public CoreTask(Core core) {
            this.core = core;
        }

        @Override
        public void run() {
            core.start();
        }
    }

    public void stopStressTest(){
        for (Core core : coreList) {
            core.shutdown();
        }
        threadPoolExecutor.shutdown();
    }

}
