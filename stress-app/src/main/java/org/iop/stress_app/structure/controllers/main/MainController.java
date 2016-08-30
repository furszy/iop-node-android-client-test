package org.iop.stress_app.structure.controllers.main;

import org.iop.client.version_1.util.HardcodeConstants;
import org.iop.stress_app.structure.controllers.AbstractMainController;
import org.iop.stress_app.structure.views.DynamicChoiceSelector;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class is the Main view controller, please, add here all the logic to init the view
 * Created by Manuel Perez P. (darkpriestrelative@gmail.com) on 23/08/16.
 */
public class MainController extends AbstractMainController {

    /**
     * In this method I'll be calling the procedure to ask to the user what IP he/she wants to work with.
     * After that, this address will be added to the IoP-client as SERVER_IP_DEFAULT
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Showing a dialog to select an IP
        DynamicChoiceSelector dynamicChoiceSelector = new DynamicChoiceSelector();
        String nodeIP = dynamicChoiceSelector.showAndWaitResult();
        System.out.println("I will work with the IP "+nodeIP);

        //Set the IP to work with.
        HardcodeConstants.setServerIpDefault(nodeIP);
    }
}