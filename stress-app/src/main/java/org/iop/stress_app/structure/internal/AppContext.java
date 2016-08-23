package org.iop.stress_app.structure.internal;

import org.iop.stress_app.structure.core.StressAppCoreManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by mati on 23/08/16.
 */
public class AppContext {

    public static AppContext context = new AppContext();

    private HashMap<String,Object> contextValues;

    /**
     * Represents the StressAppCoreManager.
     */
    protected StressAppCoreManager coreManager = new StressAppCoreManager();


    private AppContext() {
        context = new AppContext();
    }


    public Object getContextObject(String key){
        return contextValues.get(key);
    }

    public void putContextObject(String key, Object o){
        contextValues.put(key,o);
    }

    public StressAppCoreManager getCoreManager() {
        return coreManager;
    }
}
