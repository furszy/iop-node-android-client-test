package org.iop.stress_app.structure.enums;

import com.bitdubai.fermat_api.layer.all_definition.enums.interfaces.FermatEnum;
import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;
import org.iop.client.version_1.util.HardcodeConstants;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Manuel Perez P. (darkpriestrelative@gmail.com) on 30/08/16.
 */
public enum DefaultServerIP implements FermatEnum {

    DEFAULT_SERVER_IP("DSI",0,HardcodeConstants.SERVER_IP_DEFAULT,"Default server (recommended)"),
    FURSZY_SERVER("FS",1,"186.23.58.203", "@furzy's node"),
    NODE_198("NO",2,"193.234.224.198", "Markus' Node 198"),
    NODE_223("NT",3,"193.234.224.223", "Markus' Node 223"),
    //Please, let this option at last with the last index.
    USER_DEFINED("UD",4,"...", "Defined by you"),
    ;

    String code;
    int index;
    String ip;
    String friendlyName;

    private static final DefaultServerIP DEFAULT_NODE = DEFAULT_SERVER_IP;

    DefaultServerIP(String code, int index, String ip, String friendlyName) {
        this.code = code;
        this.index = index;
        this.ip = ip;
        this.friendlyName = friendlyName;
    }

    public static DefaultServerIP getByCode(String code) throws InvalidParameterException {

        for (DefaultServerIP vault : DefaultServerIP.values()) {
            if (vault.getCode().equals(code))
                return vault;
        }
        throw new InvalidParameterException(
                InvalidParameterException.DEFAULT_MESSAGE,
                null,
                "Code Received: " + code,
                "This code is not valid for the CryptoCurrencyVault enum.");
    }

    public static DefaultServerIP getByIndex(int index) throws InvalidParameterException {

        for (DefaultServerIP vault : DefaultServerIP.values()) {
            if (vault.getIndex()==index)
                return vault;
        }
        throw new InvalidParameterException(
                InvalidParameterException.DEFAULT_MESSAGE,
                null, "Index Received: " + index,
                "This index is not valid for the CryptoCurrencyVault enum.");
    }

    public int getIndex(){
        return this.index;
    }

    public String getCode() {
        return this.code;
    }

    public String getServerIp(){
        return this.ip;
    }

    public static DefaultServerIP getDefaultServerIp(){
        return DEFAULT_NODE;
    }

    @Override
    public String toString() {
        return friendlyName+"-"+ip;
    }

    public static List<DefaultServerIP> getValuesList(){
        DefaultServerIP[] ipList = DefaultServerIP.values();
        return Arrays.asList(ipList);
    }
}
