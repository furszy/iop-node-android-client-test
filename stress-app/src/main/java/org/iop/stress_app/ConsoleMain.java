package org.iop.stress_app;

import org.iop.stress_app.structure.console.ConsoleMainController;

/**
 * Created by Manuel Perez P. (darkpriestrelative@gmail.com) on 01/09/16.
 */
public class ConsoleMain {



    public static void main(String[] args){

        ConsoleMainController consoleMainController = new ConsoleMainController(args);
        consoleMainController.executeApp();
    }


}
