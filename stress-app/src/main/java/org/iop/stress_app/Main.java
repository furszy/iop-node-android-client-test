package org.iop.stress_app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

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
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/TabPane.fxml"));

        Scene scene = new Scene(root, 320, 260);

        stage.setTitle("IoP Stress App");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/iop.png")));
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
