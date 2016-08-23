package org.iop.stress_app.structure.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.iop.stress_app.structure.core.StressAppCoreManager;
import org.iop.stress_app.structure.views.IntegerSpinner;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Manuel Perez P. (darkpriestrelative@gmail.com) on 22/08/16.
 */
public class ConnectionTapController implements Initializable{

    /**
     * Represents the JavaFx textField containing the connections number.
     */
    @FXML private IntegerSpinner connectionsSpinner;

    /**
     * Represents the number of tests to test.
     */
    private int connections = 0;

    /**
     * Represents if the test has stated.
     */
    private boolean isStart = false;

    /**
     * Represents the StressAppCoreManager.
     */
    private StressAppCoreManager coreManager = new StressAppCoreManager();

    private static int DEFAULT_CONNECTIONS_TO_SET = 5;

    @FXML private Text actionTarget;
    /**
     * This method handle the click event on <b>Start</b> button
     * @param event
     */
    @FXML
    protected void handleStartButtonAction(ActionEvent event) {
        executeTest();
    }

    /**
     * This method set the test conditions and execute it
     */
    private void executeTest(){
        try{
            int connections;
            try{
                connections = connectionsSpinner.getNumber();
            } catch (Exception e){
                connections = DEFAULT_CONNECTIONS_TO_SET;
            }
            if (this.connections==0){
                this.connections = connections;
            }
            if (!isStart) {
                System.out.println("* STRESS FERMAT - Network Client - Version 1.0 (2016) *");
                System.out.println("* www.fermat.org *");

                coreManager.setCoreCount(this.connections);
                coreManager.startStressTest();
                isStart = true;
                //Notify the user the test
                actionTarget.setText("Testing with "+this.connections+" connections");
            }else{
                int addConnections = connections-this.connections;
                coreManager.addConnections(addConnections);
                //Notify the user the test
                actionTarget.setText("Added "+connections+" connections");
            }

        } catch (Exception e){
            //Notify the user that we got an error
            actionTarget.setText("The test is failed");
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Not implemented in this version
    }
}
