package org.iop.stress_app.structure.views;

import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Region;
import org.iop.stress_app.structure.enums.DefaultServerIP;
import org.iop.stress_app.structure.utils.IPAddressChecker;

import java.util.Optional;

/**
 * Created by Manuel Perez P. (darkpriestrelative@gmail.com) on 30/08/16.
 */
public class DynamicChoiceSelector extends ChoiceDialog {

    /**
     * Represents the default ip to work
     */
    private final String  DEFAULT_IP = DefaultServerIP.getDefaultServerIp().getServerIp();

    /**
     * Default constructor
     */
    public DynamicChoiceSelector(){
        super(null,DefaultServerIP.getValuesList());
        setTitle("Stress App - Node Ip Selection");
        setHeaderText("Select a Home Node IP (close this dialog to use default)");
        setContentText("Choose an option:");
    }

    /**
     * This method show a ChoiceDialog and wait for the result
     * @return
     */
    public String showAndWaitResult(){
        //Show the choice dialog and wait for the user action
        Optional<DefaultServerIP> result = showAndWait();
        if(result.isPresent()){
            //System.out.println("DynamicChoiceSelector result "+result);
            //Getting the result
            DefaultServerIP userSelection = result.get();
            //The user selects that he/she defines the IP
            if(userSelection.getIndex()==DefaultServerIP.USER_DEFINED.getIndex()){
                System.out.println("The user will defined the IP");
                return showAndWaitUserDefinedIPDialog();
            }
            //System.out.println("IP "+userSelection.getServerIp());
            return userSelection.getServerIp();
        }
        //If the user doesn't give an answer, I'll return the default IP
        return DEFAULT_IP;
    }

    /**
     * This method show and wait the result for a TextInputDialog waiting for the user defined IP
     * @return
     */
    private String showAndWaitUserDefinedIPDialog(){
        //Setting the UI for this TextInputDialog
        TextInputDialog textInputDialog = new TextInputDialog(DEFAULT_IP);
        textInputDialog.setTitle("Stress App - Node Ip Selection");
        textInputDialog.setHeaderText("Please, insert a valid IP in the box");
        textInputDialog.setContentText("I Only accept 123.123.123.123 format");
        //Showing and waiting for the result
        Optional<String> result = textInputDialog.showAndWait();
        String userDefinedIp;
        if(result.isPresent()){
            userDefinedIp = result.get();
            if(userDefinedIp!=null||!userDefinedIp.isEmpty()){
                //If the IP format is invalid I'll return the default IP
                if(IPAddressChecker.isValidIpAddress(userDefinedIp)){
                    return userDefinedIp;
                } else{
                    showAndWaitErrorAlert(userDefinedIp);
                }
            }
        }
        return DEFAULT_IP;
    }

    private void showAndWaitErrorAlert(String ipSubmitted){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Stress App - Error Notification");
        alert.setHeaderText("You submitted an incorrect IP format");
        alert.setContentText("Sorry, '"+ipSubmitted+"' does't match with the proper format.\n" +
                "Don't worry!, I'll set for you the default IP '"+DEFAULT_IP+"'");
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }

}
