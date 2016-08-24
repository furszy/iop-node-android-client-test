package org.iop.stress_app.structure;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

/**
 * Created by Manuel Perez P. (darkpriestrelative@gmail.com) on 24/08/16.
 */
public class StressAppManager {

    /**
     * Represents the main view
     */
    private final static String MAIN_FXML = "/fxml/Main.fxml";
    private final Parent root;

    public StressAppManager() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(MAIN_FXML));
        root = loader.load();
    }

    /**
     * This method returns the main parent
     * @return
     */
    public Parent getParent() {
        return root;
    }
}
