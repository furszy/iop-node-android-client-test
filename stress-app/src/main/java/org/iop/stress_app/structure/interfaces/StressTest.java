package org.iop.stress_app.structure.interfaces;

import org.iop.stress_app.structure.enums.TestType;

/**
 * Created by Manuel Perez P. (darkpriestrelative@gmail.com) on 24/08/16.
 */
public interface StressTest {

    TestType getTestType();

    void executeTest();

    void appendText(String text);

    void changeButtonText();

}
