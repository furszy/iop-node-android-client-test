package org.iop.stress_app.structure.interfaces;

import javafx.scene.control.Button;
import org.iop.stress_app.structure.core.StressAppCoreManager;
import org.iop.stress_app.structure.enums.TestType;
import org.iop.stress_app.structure.views.InfoTextArea;
import org.iop.stress_app.structure.views.IntegerSpinner;

/**
 * Created by Manuel Perez P. (darkpriestrelative@gmail.com) on 24/08/16.
 */
public abstract class AbstractStressTest implements StressTest {

    /**
     * Default values
     */
    protected final static int DEFAULT_CONNECTIONS_TO_SET = 5;
    protected int connections = 0;
    protected boolean isStart = false;

    /**
     * Represents the StressAppCoreManager.
     */
    protected StressAppCoreManager coreManager = new StressAppCoreManager();

    /**
     * Represents the control where the user sets the number of devices to start
     */
    protected final IntegerSpinner devicesIntegerSpinner;

    /**
     * Represents the button start control
     */
    private final Button startButton;

    /**
     * Represents the TestType
     */
    private final TestType testType;

    /**
     * Represents the InfoTextArea
     */
    private final InfoTextArea infoTextArea;

    /**
     * Default constructor
     * @param startButton
     * @param testType
     * @param infoTextArea
     */
    public AbstractStressTest(
            Button startButton,
            TestType testType,
            InfoTextArea infoTextArea,
            IntegerSpinner devicesIntegerSpinner) {
        this.startButton = startButton;
        this.testType = testType;
        this.infoTextArea = infoTextArea;
        this.devicesIntegerSpinner = devicesIntegerSpinner;
    }

    /**
     * This method change the text from the tab button
     */
    @Override
    public void changeButtonText(){
        this.startButton.setText("Add");
    }

    /**
     * This method returns the test Type
     * @return
     */
    @Override
    public TestType getTestType() {
        return testType;
    }

    /**
     * This method append a text line in the Info text Area
     * @param text
     */
    public void appendText(String text){
        infoTextArea.append(text);
    }

}
