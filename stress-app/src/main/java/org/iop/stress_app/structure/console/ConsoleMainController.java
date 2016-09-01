package org.iop.stress_app.structure.console;

import org.iop.client.version_1.util.HardcodeConstants;
import org.iop.stress_app.structure.utils.IPAddressChecker;

import java.util.Arrays;

/**
 * Created by Manuel Perez P. (darkpriestrelative@gmail.com) on 01/09/16.
 */
public class ConsoleMainController {

    /**
     * Default values
     */
    private static final int DEFAULT_DEVICES_TO_START = 5;
    private static final int DEFAULT_NS_TO_START = 5;
    private static final int DEFAULT_ACTORS_TO_CREATE = 5;
    private static final String DEFAULT_NODE_IP = HardcodeConstants.SERVER_IP_DEFAULT;

    /**
     * Index
     */
    private static final int DEVICES_INDEX = 0;
    private static final int NS_INDEX = 1;
    private static final int ACTORS_INDEX = 2;

    /**
     * Arguments
     */
    private final String[]  requiredArguments = new String[]{"-d","-n","-a","--devices","--ns","--actors","-i","--ip"};

    private final String[] submittedArguments;

    public ConsoleMainController(String[] submittedArguments) {
        this.submittedArguments = submittedArguments;
    }

    public void executeApp(){
        System.out.println("IoP - Java stress app - shell edition 1.0");
        //If no arguments submitted I'll print the options and return
        if(submittedArguments.length==0){
            printDefaultArguments();
            return;
        }
        int[] numericArguments = getValidArguments();
        String ip = getNodeIp();

        System.out.println("Arguments submitted:");
        System.out.println(Arrays.toString(submittedArguments));
        System.out.println("I'll start the test with:");
        System.out.println("Devices -> "+numericArguments[DEVICES_INDEX]);
        System.out.println("Network Services -> "+numericArguments[NS_INDEX]);
        System.out.println("Actors -> "+numericArguments[ACTORS_INDEX]);
        System.out.println("Node IP -> "+ip);

    }

    private int[] getValidArguments(){
        int[] validArguments = new int[3];
        try{
            //Valid for devices -d
            int dIndex = contents(submittedArguments, requiredArguments[0]);
            if(dIndex>=0){
                String devices = submittedArguments[dIndex+1];
                if(devices!=null&&!checkIfOption(devices)){
                    validArguments[DEVICES_INDEX] = parseToInt(devices, DEFAULT_DEVICES_TO_START);
                }
            }

            //Valid for ns -n
            int nIndex = contents(submittedArguments, requiredArguments[1]);
            if(nIndex>=0){
                String ns = submittedArguments[nIndex+1];
                if(ns!=null&&!checkIfOption(ns)){
                    validArguments[NS_INDEX] = parseToInt(ns, DEFAULT_NS_TO_START);
                }
            }

            //Valid for actors -a
            int aIndex = contents(submittedArguments, requiredArguments[2]);
            if(aIndex>=0){
                String actors = submittedArguments[aIndex+1];
                if(actors!=null&&!checkIfOption(actors)){
                    validArguments[ACTORS_INDEX] = parseToInt(actors, DEFAULT_ACTORS_TO_CREATE);
                }
            }

            //Valid for devices --devices
            if(validArguments[DEVICES_INDEX]==0){
                dIndex = contents(submittedArguments, requiredArguments[3]);
                if(dIndex>=0){
                    String devices = submittedArguments[dIndex+1];
                    if(devices!=null&&!checkIfOption(devices)){
                        validArguments[DEVICES_INDEX] = parseToInt(devices, DEFAULT_DEVICES_TO_START);
                    }
                }
            }

            //Valid for ns --ns
            if(validArguments[NS_INDEX]==0){
                nIndex = contents(submittedArguments, requiredArguments[4]);
                if(nIndex>=0){
                    String ns = submittedArguments[nIndex+1];
                    if(ns!=null&&!checkIfOption(ns)){
                        validArguments[NS_INDEX] = parseToInt(ns, DEFAULT_NS_TO_START);
                    }
                }
            }

            if(validArguments[ACTORS_INDEX]==0){
                aIndex = contents(submittedArguments, requiredArguments[5]);
                if(aIndex>=0){
                    String actors = submittedArguments[aIndex+1];
                    if(actors!=null&&!checkIfOption(actors)){
                        validArguments[ACTORS_INDEX] = parseToInt(actors, DEFAULT_ACTORS_TO_CREATE);
                    }
                }
            }
        } catch (Exception e){
            System.out.println("There's an exception "+e+"\nDon't worry, I'll set default values");
        }

        return checkValidArguments(validArguments);
    }

    private int[] checkValidArguments(int[] argumentsParsed){
        if(argumentsParsed[DEVICES_INDEX]==0){
            argumentsParsed[DEVICES_INDEX] = DEFAULT_DEVICES_TO_START;
        }
        if(argumentsParsed[NS_INDEX]==0){
            argumentsParsed[NS_INDEX] = DEFAULT_NS_TO_START;
        }
        if(argumentsParsed[ACTORS_INDEX]==0){
            argumentsParsed[ACTORS_INDEX] = DEFAULT_ACTORS_TO_CREATE;
        }
        return argumentsParsed;
    }

    private String getNodeIp(){
        int ipIndex = contents(submittedArguments, requiredArguments[6]);
        if(ipIndex>=0){
            String ip = submittedArguments[ipIndex+1];
            if(ip!=null&&!ip.isEmpty()&&!checkIfOption(ip)){
                if(IPAddressChecker.isValidIpAddress(ip)){
                    return ip;
                } else {
                    return DEFAULT_NODE_IP;
                }
            }
        }

        ipIndex = contents(submittedArguments, requiredArguments[7]);
        if(ipIndex>=0){
            String ip = submittedArguments[ipIndex+1];
            if(ip!=null&&!ip.isEmpty()&&!checkIfOption(ip)){
                if(IPAddressChecker.isValidIpAddress(ip)){
                    return ip;
                } else {
                    return DEFAULT_NODE_IP;
                }
            }
            return DEFAULT_NODE_IP;
        }
        return DEFAULT_NODE_IP;
    }

    /**
     * This method parse a String to int, if an error occurred, will return a default value
     * @param argument
     * @param defaultValue
     * @return
     */
    private int parseToInt(String argument, int defaultValue){
        try{
            int value = Integer.parseInt(argument);
            if(value<1){
                return defaultValue;
            }
            return value;
        } catch (Exception e){
            return defaultValue;
        }
    }

    private boolean checkIfOption(String argument){
        return argument.contains("-");
    }

    private int contents(int[] array, int value){
        int index = 0;
        for(int element : array){
            if(element == value){
                return index;
            }
            index++;
        }
        return -1;
    }

    private int contents(String[] array, String value){
        int index = 0;
        for(String element : array){
            if(element.equals(value)){
                return index;
            }
            index++;
        }
        return -1;
    }

    /**
     * This method will print the arguments required
     */
    private void printDefaultArguments(){
        System.out.println("The arguments required for this app are:");
        System.out.println("-d or --devices indicates the number of devices to emulate");
        System.out.println("-n or --ns indicates the number of Network Services to start");
        System.out.println("-a or --actors indicates the number of actors to create");
        System.out.println("-i or --ip indicates the Node IP address");
    }
}
