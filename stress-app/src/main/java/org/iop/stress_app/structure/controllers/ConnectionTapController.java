package org.iop.stress_app.structure.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Manuel Perez P. (darkpriestrelative@gmail.com) on 22/08/16.
 */
public class ConnectionTapController implements Initializable{

    /**
     * This method handle the click event on <b>Start</b> button
     * @param event
     */
    @FXML
    protected void handleStartButtonAction(ActionEvent event) {
        System.out.println(event);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
