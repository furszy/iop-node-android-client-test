package org.iop.stress_app.structure.views;

import javafx.beans.binding.NumberBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * Created by Manuel Perez P. (darkpriestrelative@gmail.com) on 22/08/16.
 * Based on NumberSpinner created by Thomas Bolz
 * You can check on: https://dzone.com/articles/javafx-numbertextfield-and
 */
public class IntegerSpinner extends HBox {


    public static final String ARROW = "NumberSpinnerArrow";
    public static final String NUMBER_FIELD = "NumberField";
    //public static final String NUMBER_SPINNER = "NumberSpinner";
    public static final String SPINNER_BUTTON_UP = "SpinnerButtonUp";
    public static final String SPINNER_BUTTON_DOWN = "SpinnerButtonDown";
    private final String BUTTONS_BOX = "ButtonsBox";
    private NumberTextField numberField;
    private ObjectProperty<Integer> stepWidthProperty = new SimpleObjectProperty<>();
    private final double ARROW_SIZE = 4;
    private final Button incrementButton;
    private final Button decrementButton;
    private final NumberBinding buttonHeight;
    private final NumberBinding spacing;
    private Integer min = 0;
    private Integer max = 10;

    /**
     * Default constructor
     */
    public IntegerSpinner(){
        this(0,1);
    }

    /**
     * Constructor with parameters
     * @param value
     * @param stepWidth
     */
    public IntegerSpinner(Integer value, Integer stepWidth){
        this(value, stepWidth, 0, 10, NumberFormat.getInstance());
    }

    /**
     * Constructor with parameters
     * @param value
     * @param stepWidth
     * @param min
     * @param max
     */
    public IntegerSpinner(Integer value, Integer stepWidth, Integer min, Integer max){
        this(value, stepWidth, min, max, NumberFormat.getInstance());
    }

    /**
     * Constructor with parameters that sets all the values required for this class
     * @param value
     * @param stepWidth
     * @param max
     * @param min
     * @param nf
     */
    public IntegerSpinner(
            Integer value,
            Integer stepWidth,
            Integer max,
            Integer min,
            NumberFormat nf){

        super();
        //this.setId(NUMBER_SPINNER);
        this.stepWidthProperty.set(stepWidth);

        //Set max and min
        this.max = max;
        this.min = min;

        // TextField
        numberField = new NumberTextField(new BigDecimal(value), nf);
        numberField.setId(NUMBER_FIELD);

        // Enable arrow keys for dec/inc
        numberField.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.DOWN) {
                    decrement();
                    keyEvent.consume();
                }
                if (keyEvent.getCode() == KeyCode.UP) {
                    increment();
                    keyEvent.consume();
                }
            }
        });

        // Painting the up and down arrows
        Path arrowUp = new Path();
        arrowUp.setId(ARROW);
        arrowUp.getElements().addAll(new MoveTo(-ARROW_SIZE, 0), new LineTo(ARROW_SIZE, 0),
                new LineTo(0, -ARROW_SIZE), new LineTo(-ARROW_SIZE, 0));
        // mouse clicks should be forwarded to the underlying button
        arrowUp.setMouseTransparent(true);

        Path arrowDown = new Path();
        arrowDown.setId(ARROW);
        arrowDown.getElements().addAll(new MoveTo(-ARROW_SIZE, 0), new LineTo(ARROW_SIZE, 0),
                new LineTo(0, ARROW_SIZE), new LineTo(-ARROW_SIZE, 0));
        arrowDown.setMouseTransparent(true);

        // the spinner buttons scale with the textfield size
        // TODO: the following approach leads to the desired result, but it is
        // not fully understood why and obviously it is not quite elegant
        buttonHeight = numberField.heightProperty().subtract(3).divide(2);
        // give unused space in the buttons VBox to the incrementButton
        spacing = numberField.heightProperty().subtract(2).subtract(buttonHeight.multiply(2));

        // inc/dec buttons
        VBox buttons = new VBox();
        buttons.setId(BUTTONS_BOX);
        incrementButton = new Button();
        incrementButton.setId(SPINNER_BUTTON_UP);
        incrementButton.prefWidthProperty().bind(numberField.heightProperty());
        incrementButton.minWidthProperty().bind(numberField.heightProperty());
        incrementButton.maxHeightProperty().bind(buttonHeight.add(spacing));
        incrementButton.prefHeightProperty().bind(buttonHeight.add(spacing));
        incrementButton.minHeightProperty().bind(buttonHeight.add(spacing));
        incrementButton.setFocusTraversable(false);
        incrementButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent ae) {
                increment();
                ae.consume();
            }
        });

        // Paint arrow path on button using a StackPane
        StackPane incPane = new StackPane();
        incPane.getChildren().addAll(incrementButton, arrowUp);
        incPane.setAlignment(Pos.CENTER);

        decrementButton = new Button();
        decrementButton.setId(SPINNER_BUTTON_DOWN);
        decrementButton.prefWidthProperty().bind(numberField.heightProperty());
        decrementButton.minWidthProperty().bind(numberField.heightProperty());
        decrementButton.maxHeightProperty().bind(buttonHeight);
        decrementButton.prefHeightProperty().bind(buttonHeight);
        decrementButton.minHeightProperty().bind(buttonHeight);

        decrementButton.setFocusTraversable(false);
        decrementButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent ae) {
                decrement();
                ae.consume();
            }
        });

        StackPane decPane = new StackPane();
        decPane.getChildren().addAll(decrementButton, arrowDown);
        decPane.setAlignment(Pos.CENTER);

        buttons.getChildren().addAll(incPane, decPane);
        this.getChildren().addAll(numberField, buttons);
    }

    /**
     * increment number value by stepWidth
     */
    private void increment() {
        Integer value = numberField.getInteger();
        value = value + stepWidthProperty.get();
        if(checkBorders(value)){
            numberField.setInteger(value);
        }
    }

    /**
     * decrement number value by stepWidth
     */
    private void decrement() {
        Integer value = numberField.getInteger();
        value = value - stepWidthProperty.get();
        if(checkBorders(value)){
            numberField.setInteger(value);
        }
    }

    public final void setNumber(Integer value) {
        if(checkBorders(value)){
            numberField.setInteger(value);
        } else {
            numberField.setInteger(min);
        }
    }

    public ObjectProperty<Integer> numberProperty() {
        Integer value = numberField.numberProperty().getValue().intValueExact();
        ObjectProperty<Integer> numberProperty = new SimpleObjectProperty<>();
        numberProperty.setValue(value);
        return numberProperty;
    }

    /**
     * This method returns the Integer set in the view
     * @return
     */
    public final Integer getNumber() {
        if(checkBorders()){
            return numberField.getInteger();
        } else {
            return min;
        }

    }

    /**
     * This method returns check if the value is between borders
     * @return
     */
    private boolean checkBorders(){
        return checkBorders(numberField.getInteger());
    }

    /**
     * This method checks if the value to set inside the borders
     * @param value
     * @return
     */
    private boolean checkBorders(Integer value){
        if((value.compareTo(min)>=0)&&(value.compareTo(max)<=0)){
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method sets the min value
     * @param min
     */
    public void setMin(Integer min) {
        this.min = min;
    }

    /**
     * This method sets the max value
     * @param max
     */
    public void setMax(Integer max) {
        this.max = max;
    }

    /**
     * This method returns the min value
     * @return
     */
    public Integer getMin() {
        return min;
    }

    /**
     * This method returns the max value
     * @return
     */
    public Integer getMax() {
        return max;
    }
}
