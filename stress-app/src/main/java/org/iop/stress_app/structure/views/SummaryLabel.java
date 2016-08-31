package org.iop.stress_app.structure.views;

import javafx.scene.control.Label;
import org.iop.stress_app.structure.enums.ReportType;

/**
 * Created by Manuel Perez P. (darkpriestrelative@gmail.com) on 26/08/16.
 */
public class SummaryLabel extends Label {

    /**
     * Counters
     */
    private int nsStarted = 0;
    private int actorsCreated = 0;
    private int actorRegistered = 0;
    private int exceptionDetected = 0;
    private int messagesSent = 0;
    private int failedMessages = 0;
    private int successfulMessages = 0;
    private int receivedMessages = 0;
    private int clientsConnected = 0;
    private int requestListSent = 0;
    private int requestListReceived = 0;
    private int respondMessages = 0;

    /**
     * This method increase a counter and print in the UI the summary
     * @param reportType
     */
    public void report(ReportType reportType){
        switch (reportType){
            case ACTOR_CREATED:
                actorsCreated++;
                break;
            case ACTOR_REGISTERED:
                actorRegistered++;
                break;
            case CLIENT_CONNECTED:
                clientsConnected++;
                break;
            case EXCEPTION_DETECTED:
                exceptionDetected++;
                break;
            case FAILED_MESSAGES:
                failedMessages++;
                break;
            case MESSAGE_SENT:
                messagesSent++;
                break;
            case NS_STARED:
                nsStarted++;
                break;
            case REQUEST_LIST_SENT:
                requestListSent++;
                break;
            case REQUEST_LIST_RECEIVED:
                requestListReceived++;
                break;
            case RECEIVED_MESSAGE:
                receivedMessages++;
                break;
            case RESPOND_MESSAGES:
                respondMessages++;
                break;
            case SUCCESSFUL_MESSAGE:
                successfulMessages++;
                break;
        }
        setReport();
    }

    /**
     * This method set the text in the UI.
     */
    private void setReport(){
        String report = "SUMMARY:\n" +
                "\n" +
                "- Actors Created: " + actorsCreated + "\n" +
                "- Actors Registered: " + actorRegistered + "\n" +
                "- Clients Connected: " + clientsConnected + "\n" +
                "- Exceptions Detected (*): " + exceptionDetected + "\n" +
                "- Failed Messages: " + failedMessages + "\n" +
                "- Messages Sent: " + messagesSent + "\n" +
                "- Network Service Started: " + nsStarted + "\n" +
                "- Request List Sent: " + requestListSent + "\n" +
                "- Request List Received: " + requestListReceived + "\n" +
                "- Received Messages: " + receivedMessages + "\n" +
                "- Respond Messages: " + respondMessages + "\n" +
                "- Successful Messages: " + successfulMessages + "\n" +
                "---------------\n" +
                "(*) Exceptions detected in actor test execution.";
        System.out.println(report);
        setText(report);
    }
}
