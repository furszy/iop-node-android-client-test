package org.iop.stress_app.structure.controllers.main;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.iop.stress_app.structure.controllers.AbstractMainController;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Manuel Perez P. (darkpriestrelative@gmail.com) on 23/08/16.
 */
public class MainBottomController extends AbstractMainController {

    /**
     * Represents the status label showed in the bottom
     */
    @FXML
    Label statusLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Set the initial text
        statusLabel.setText("Iop Stress app has started");
    }
}
