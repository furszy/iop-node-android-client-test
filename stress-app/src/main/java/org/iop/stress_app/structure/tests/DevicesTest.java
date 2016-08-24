package org.iop.stress_app.structure.tests;

import javafx.scene.control.Button;
import org.iop.stress_app.structure.core.StressAppCoreManager;
import org.iop.stress_app.structure.enums.TestType;
import org.iop.stress_app.structure.interfaces.StressTest;
import org.iop.stress_app.structure.views.InfoTextArea;
import org.iop.stress_app.structure.views.IntegerSpinner;

/**
 * Created by Manuel Perez P. (darkpriestrelative@gmail.com) on 24/08/16.
 */
public class DevicesTest implements StressTest {

    private final static int DEFAULT_CONNECTIONS_TO_SET = 5;
    private int connections = 0;
    private boolean isStart = false;
    /**
     * Represents the StressAppCoreManager.
     */
    protected StressAppCoreManager coreManager = new StressAppCoreManager();

    /**
     * Represents the control where the user sets the number of devices to start
     */
    private final IntegerSpinner devicesIntegerSpinner;

    /**
     * Represents the control where we gonna show to the user some text info
     */
    private final InfoTextArea devicesTextArea;

    /**
     * Represents the button start control

     */
    private final Button startButton;

    /**
     * Default contructor
     * @param devicesIntegerSpinner
     * @param devicesTextArea
     * @param startButton
     */
    public DevicesTest(
            IntegerSpinner devicesIntegerSpinner,
            InfoTextArea devicesTextArea,
            Button startButton) {
        this.devicesIntegerSpinner = devicesIntegerSpinner;
        this.devicesTextArea = devicesTextArea;
        this.startButton = startButton;
    }

    @Override
    public TestType getTestType() {
        return TestType.DEVICES_TEST;
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

    public void appendText(String text){
        devicesTextArea.append(text);
    }

    /**
     * This method change the text from the tab button
     */
    public void changeButtonText(){
        this.startButton.setText("Add");
    }
}
