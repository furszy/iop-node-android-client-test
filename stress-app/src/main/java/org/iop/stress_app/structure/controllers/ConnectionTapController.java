package org.iop.stress_app.structure.controllers;

/**
 * This class implements the controller for ConnectionTab.fxml
 * Created by Manuel Perez P. (darkpriestrelative@gmail.com) on 22/08/16.
 */
public class ConnectionTapController extends AbstractTabController{

    /**
     * This method set the test conditions and execute it
     */
    protected void executeTest(){
        try{
            int connections;
            try{
                connections = tabSpinner.getNumber();
            } catch (Exception e){
                connections = DEFAULT_CONNECTIONS_TO_SET;
            }
            if (this.connections==0){
                this.connections = connections;
            }
            if (!isStart) {

                coreManager.setCoreCount(this.connections);
                coreManager.startStressTest();
                isStart = true;
                //Notify the user the test
                actionTarget.setText("Testing with "+this.connections+" connections");
                //Change the button text
                changeButtonText();
            }else{
                int addConnections = this.connections;
                if(addConnections<=0){
                    //Notify the user the test
                    actionTarget.setText("No connections added");
                    return;
                }
                coreManager.addConnections(addConnections);
                //Notify the user the test
                this.connections+=connections;
                actionTarget.setText("Working with "+this.connections+" connections");

            }

        } catch (Exception e){
            //Notify the user that we got an error
            actionTarget.setText("The test is failed");
            e.printStackTrace();
        }
    }

}
