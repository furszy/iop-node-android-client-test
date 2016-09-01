package org.iop.stress_app.structure.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Manuel Perez P. (darkpriestrelative@gmail.com) on 30/08/16.
 */
public class IPAddressChecker {

    /**
     * Represents the pattern to check
     */
    private static final String IP_ADDRESS_PATTERN =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    /**
     * This method will check if the String given as argument matches with IP_ADDRESS_PATTERN
     * @param ipAddress
     * @return true if the pattern matches.
     */
    public static boolean isValidIpAddress(final String ipAddress){
        Pattern pattern = Pattern.compile(IP_ADDRESS_PATTERN);
        Matcher matcher = pattern.matcher(ipAddress);
        return matcher.matches();
    }
}
