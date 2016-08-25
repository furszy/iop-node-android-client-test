package org.iop.stress_app.structure.enums;

import com.bitdubai.fermat_api.layer.all_definition.enums.interfaces.FermatEnum;

/**
 * Created by Manuel Perez P. (darkpriestrelative@gmail.com) on 24/08/16.
 */
public enum TestType implements FermatEnum {

    /**
     * To make the code more readable, please keep the elements in the Enum sorted alphabetically.
     */
    ACTORS_TEST("ATEST"),
    DEVICES_TEST("DTEST"),
    NS_TEST("NTEST")
    ;

    String code;

    TestType(String code) {
        this.code = code;
    }

    public static TestType getByCode(String code) throws IllegalArgumentException{
        switch (code){
            case "ATEST":
                return ACTORS_TEST;
            case "DTEST":
                return DEVICES_TEST;
            case "NS_TEST":
                return NS_TEST;
            default:
                throw new IllegalArgumentException(
                        "The code"+code+" is not valid for the TestType enum."
                );
        }
    }

    @Override
    public String getCode() {
        return this.code;
    }
}
