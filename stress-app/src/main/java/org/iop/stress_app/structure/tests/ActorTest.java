package org.iop.stress_app.structure.tests;

import javafx.scene.control.Button;
import org.iop.stress_app.structure.enums.TestType;
import org.iop.stress_app.structure.interfaces.AbstractStressTest;
import org.iop.stress_app.structure.views.InfoTextArea;
import org.iop.stress_app.structure.views.IntegerSpinner;

/**
 * Created by Manuel Perez P. (darkpriestrelative@gmail.com) on 25/08/16.
 */
public class ActorTest extends AbstractStressTest {

    /**
     * Represents the control where the user sets the number of NS to start
     */
    private final IntegerSpinner nsIntegerSpinner;

    /**
     * Represents the control where the user sets the number of actors to rceate
     */
    private final IntegerSpinner actorIntegerSpinner;

    /**
     * Represents the number of Ns to start
     */
    private int actors;

    /**
     * Represents the number of Ns to start
     */
    private int networkServices;

    /**
     * Default constructor
     *
     * @param startButton
     * @param infoTextArea
     * @param devicesIntegerSpinner
     */
    public ActorTest(
            Button startButton,
            InfoTextArea infoTextArea,
            IntegerSpinner devicesIntegerSpinner,
            IntegerSpinner nsIntegerSpinner,
            IntegerSpinner actorIntegerSpinner
            ) {
        super(startButton, TestType.ACTORS_TEST, infoTextArea, devicesIntegerSpinner);
        this.actorIntegerSpinner = actorIntegerSpinner;
        this.nsIntegerSpinner = nsIntegerSpinner;
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

                this.networkServices = nsIntegerSpinner.getNumber();
                this.actors = actorIntegerSpinner.getNumber();
                coreManager.setCoreCount(this.connections);
                coreManager.setNetworkServiceStart(true);
                coreManager.setNsToStart(networkServices);
                coreManager.setActorCreation(true);
                coreManager.setActorsToCreate(actors);
                coreManager.startStressTest();
                isStart = true;

                //Notify the user the test
                appendText("Testing with "+this.connections+" connections");
                appendText("Started "+networkServices+" network services");
                appendText("Created "+actors+" actors");
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
                appendText("Added "+networkServices+" network services to every connection");
                appendText("Added "+actors+" actors to each network service");

            }

        } catch (Exception e){
            //Notify the user that we got an error
            appendText("The test is failed");
            e.printStackTrace();
        }
    }
}
