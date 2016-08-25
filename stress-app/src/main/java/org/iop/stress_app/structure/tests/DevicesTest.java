package org.iop.stress_app.structure.tests;

import javafx.scene.control.Button;
import org.iop.stress_app.structure.core.StressAppCoreManager;
import org.iop.stress_app.structure.enums.TestType;
import org.iop.stress_app.structure.interfaces.AbstractStressTest;
import org.iop.stress_app.structure.interfaces.StressTest;
import org.iop.stress_app.structure.views.InfoTextArea;
import org.iop.stress_app.structure.views.IntegerSpinner;

/**
 * Created by Manuel Perez P. (darkpriestrelative@gmail.com) on 24/08/16.
 */
public class DevicesTest extends AbstractStressTest {

    /**
     * Default constructor
     * @param devicesIntegerSpinner
     * @param devicesTextArea
     * @param startButton
     */
    public DevicesTest(
            IntegerSpinner devicesIntegerSpinner,
            InfoTextArea devicesTextArea,
            Button startButton
            ) {
        super(startButton, TestType.DEVICES_TEST, devicesTextArea, devicesIntegerSpinner);
    }

    @Override
    public void executeTest() {
        try{
            int connections;
            try{
                connections = devicesIntegerSpinner.getNumber();
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
                appendText("Testing with "+this.connections+" connections");
                //Change the button text
                changeButtonText();
            }else{
                int addConnections = this.connections;
                if(addConnections<=0){
                    //Notify the user the test
                    appendText("No connections added");
                    return;
                }
                coreManager.addConnections(addConnections);
                //Notify the user the test
                this.connections+=connections;
                appendText("Working with "+this.connections+" connections");

            }

        } catch (Exception e){
            //Notify the user that we got an error
            appendText("The test is failed");
            e.printStackTrace();
        }
    }

}
