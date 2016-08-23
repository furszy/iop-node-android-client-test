package org.iop.stress_app.structure.controllers;


/**
 * This class implements the controller for NetworkServiceTab.fxml
 * Created by Manuel Perez P. (darkpriestrelative@gmail.com) on 22/08/16.
 */
public class NetworkServiceTabController extends AbstractTabController {

    /**
     * This method set the test conditions and execute it
     */
    @Override
    protected void executeTest() {
        try{
            int connections;
            try{
                //In this case this represents a connection for every Network service
                connections = tabSpinner.getNumber();
            } catch (Exception e){
                connections = DEFAULT_CONNECTIONS_TO_SET;
            }
            if (this.connections==0){
                this.connections = connections;
            }
            if (!isStart) {

                coreManager.setCoreCount(this.connections);
                //We set as available the Network Service test
                coreManager.setNetworkServiceStart(true);
                coreManager.startStressTest();
                isStart = true;
                //Notify the user the test
                actionTarget.setText("Testing with "+this.connections+" Network Services");
                //Change the button text
                changeButtonText();
            }else{
                int addConnections = this.connections;
                if(addConnections<=0){
                    //Notify the user the test
                    actionTarget.setText("No Network Services added");
                    return;
                }
                coreManager.addConnections(addConnections);
                //Notify the user the test
                this.connections+=connections;
                actionTarget.setText("Working with "+this.connections+" Network Services");

            }

        } catch (Exception e){
            //Notify the user that we got an error
            actionTarget.setText("The test is failed");
            e.printStackTrace();
        }
    }
}
