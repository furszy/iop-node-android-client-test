package org.iop.stress_app.structure.views;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

/**
 * This class extends from JavaFx BorderPane and sets default values for the object
 * Includes a Label and a textArea bounded.
 * Created by Manuel Perez P. (darkpriestrelative@gmail.com) on 23/08/16.
 */
public class InfoTextArea extends BorderPane {

    /**
     * Represents the textArea
     */
    private final TextArea textArea;

    /**
     * Represents the label
     */
    private final Label label;

    /**
     * Default constructor
     */
    public InfoTextArea() {
        super();
        textArea = new TextArea();
        label = new Label();
        setDefaultValues();
        this.setTop(label);
        this.setCenter(textArea);
    }

    /**
     * This method sets default values for this object
     */
    private void setDefaultValues(){
        //TextArea default values
        textArea.setPrefWidth(316);
        textArea.setPrefHeight(100);
        textArea.setPrefRowCount(10);
        textArea.setPrefColumnCount(100);
        textArea.setWrapText(true);

        //Label default values
        label.setPrefWidth(100);
        label.setWrapText(true);
        label.setText("Information");
    }

    /**
     * This method append a new line of text in textArea
     * @param text
     */
    public void append(final String text){
        if(text!=null||text.isEmpty()){
            textArea.setText(textArea.getText()+text+"\n");
        }
    }
}
