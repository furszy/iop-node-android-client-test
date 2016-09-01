package org.iop.stress_app.structure.enums;

import com.bitdubai.fermat_api.layer.all_definition.enums.interfaces.FermatEnum;

/**
 * Created by Manuel Perez P. (darkpriestrelative@gmail.com) on 26/08/16.
 */
public enum ReportType implements FermatEnum {

    /**
     * To make the code more readable, please keep the elements in the Enum sorted alphabetically.
     */
    ACTOR_CREATED("AC"),
    ACTOR_REGISTERED("AR"),
    CLIENT_CONNECTED("CC"),
    EXCEPTION_DETECTED("ED"),
    FAILED_MESSAGES("FM"),
    MESSAGE_SENT("MS"),
    NS_STARED("NS"),
    RECEIVED_MESSAGE("RM"),
    REQUEST_LIST_RECEIVED("RLR"),
    REQUEST_LIST_SENT("RLS"),
    RESPOND_MESSAGES("RXM"),
    SUCCESSFUL_MESSAGE("SM")
            ;

    String code;

    ReportType(String code) {
        this.code = code;
    }

    public static ReportType getByCode(String code) throws IllegalArgumentException{
        switch (code){
            case "AC":
                return ACTOR_CREATED;
            case "AR":
                return ACTOR_REGISTERED;
            case "CC":
                return CLIENT_CONNECTED;
            case "FM":
                return FAILED_MESSAGES;
            case "MS":
                return MESSAGE_SENT;
            case "NS":
                return NS_STARED;
            case "RLR":
                return REQUEST_LIST_RECEIVED;
            case "RLS":
                return REQUEST_LIST_SENT;
            case "RM":
                return RECEIVED_MESSAGE;
            case "RXM":
                return RESPOND_MESSAGES;
            case "SM":
                return SUCCESSFUL_MESSAGE;
            default:
                throw new IllegalArgumentException(
                        "The code"+code+" is not valid for the ReportType enum."
                );
        }
    }

    @Override
    public String getCode() {
        return this.code;
    }
}
