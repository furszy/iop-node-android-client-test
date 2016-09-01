package org.iop.stress_app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.iop.stress_app.structure.StressAppManager;
import org.iop.stress_app.structure.utils.IoPBytesArray;

/**
 * Created by Manuel Perez P. (darkpriestrelative@gmail.com) on 22/08/16.
 */
public class Main extends Application {

    /**
     * This method configure the app main view
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        //Set the FXML file
        StressAppManager stressAppManager = new StressAppManager();
        Scene scene = new Scene(stressAppManager.getParent(), 500, 320);

        stage.setTitle("IoP Stress App");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.getIcons().add(new Image(getClass().getResourceAsStream(IoPBytesArray.getIopLogoPath())));
        stage.setOnCloseRequest(e -> System.exit(0));
        stage.show();
    }

    /**
     * Main method
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("* STRESS FERMAT - Network Client - Version 1.0 (2016) *");
        System.out.println("* www.fermat.org *");
        //Launch the app
        launch(args);
    }
}
