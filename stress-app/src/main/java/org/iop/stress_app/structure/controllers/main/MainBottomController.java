package org.iop.stress_app.structure.controllers.main;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.iop.stress_app.structure.controllers.AbstractMainController;
import org.iop.stress_app.structure.views.InfoTextArea;
import org.iop.stress_app.structure.views.IntegerSpinner;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * Created by Manuel Perez P. (darkpriestrelative@gmail.com) on 23/08/16.
 */
public class MainBottomController extends AbstractMainController {

    private final static String BORDER_TAB_PANE_ID = "tabBorderPane";
    private final static String TAB_PANE_ID = "tabPane";
    private final static String INTEGER_SPINNER_ID = "tabSpinner";
    private final static String DEVICES_TAB_ID = "devicesTab";
    private final static String INFO_TEXT_AREA_ID = "coreInfoArea";

    /**
     * Devices Children Maps
     */
    HashMap<String, Node> parentNodes;
    HashMap<String, Node> borderPaneNodes;
    HashMap<String, Node> tabPaneNodes;

    /**
     * Represents the main container present in this controller
     */
    @FXML
    HBox mainBottom;
    /**
     * Represents the status label showed in the bottom
     */
    @FXML
    Label statusLabel;

    /**
     * External objects
     */
    /**
     * Represents the info text area to modify
     */
    InfoTextArea coreInfoArea;

    /**
     * Represents the spinner with the number of devices to start
     */
    IntegerSpinner devicesIntegerSpinner;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Set the initial text
        statusLabel.setText("Iop Stress app has started");

    }

    /**
     * This method maps the children from a parent
     * @param parent
     * @return
     */
    private HashMap<String, Node> mapChildren(Parent parent){
        HashMap<String, Node> childrenMap = new HashMap<>();
        ObservableList<Node> children = parent.getChildrenUnmodifiable();
        children.forEach(child->childrenMap.put(child.getId(),child));
        return childrenMap;
    }

    /**
     * This method handle the click event on <b>Start</b> button
     * @param event
     */
    @FXML
    protected void handleStartButtonAction(ActionEvent event) {
        executeAction();
    }

    /**
     * This method maps all the children maps
     */
    private void mapAllDeviceChildren(){
        //Todo: this is a test
        //Look for the MainBottom parent
        if(parentNodes==null){
            //Mapping all the children
            parentNodes = mapChildren(mainBottom.getScene().getRoot());
        }
        //System.out.println(parentNodes);
        if(borderPaneNodes==null){
            //Now, we need the BorderPane
            Node borderPane = parentNodes.get(BORDER_TAB_PANE_ID);
            borderPaneNodes = mapChildren((Parent) borderPane);
        }
        //System.out.println(borderPaneNodes);
        if(tabPaneNodes==null){
            //Now, we need the BorderPane
            Node tabPane = borderPaneNodes.get(TAB_PANE_ID);
            //System.out.println(tabPane);
            ObservableList<Node> children = ((Parent) tabPane).getChildrenUnmodifiable();
            //Todo: to improve
            //for some reason the TabPane that I need is in index 1
            Node node = children.get(1);
            //System.out.println(node);
            ObservableList<Node> innerNodes=((Parent)node).getChildrenUnmodifiable();
            Node innerNode = innerNodes.get(0);
            //System.out.println(innerNodes);
            tabPaneNodes = mapChildren((Parent) innerNode);
        }
        //System.out.println(tabPaneNodes);
    }

    /**
     * This method gets the
     * @return
     */
    private InfoTextArea getInfoTextArea(){
        mapAllDeviceChildren();
        Node infoTextAreaNode = tabPaneNodes.get(INFO_TEXT_AREA_ID);
        if(infoTextAreaNode instanceof InfoTextArea){
            InfoTextArea infoTextArea = (InfoTextArea) infoTextAreaNode;
            return infoTextArea;
        }
        return null;
    }

    private IntegerSpinner getDeviceIntegerSpinner(){
        mapAllDeviceChildren();
        Node integerSpinnerNode = tabPaneNodes.get(INTEGER_SPINNER_ID);
        if(integerSpinnerNode instanceof IntegerSpinner){
            IntegerSpinner integerSpinner = (IntegerSpinner) integerSpinnerNode;
            return integerSpinner;
        }
        return null;
    }

    /**
     * This method executes an action
     */
    private void executeAction(){

        //Get the Core Tab Spinner
        if(devicesIntegerSpinner==null){
            devicesIntegerSpinner = getDeviceIntegerSpinner();
        }
        //We only execute the test if the view is available
        if(devicesIntegerSpinner!=null){
            int devicesToStart = devicesIntegerSpinner.getNumber();
            appendDevicesText("Devices to start: "+devicesToStart);
        }

    }

    private void appendDevicesText(String text){
        if(coreInfoArea==null){
            coreInfoArea = getInfoTextArea();
        }
        coreInfoArea.append(text);
    }
}
