package com.example;

import java.util.Scanner;

/**
 * Created by mati on 18/08/16.
 */
public class Main {


    public static void main(String[] args){
        System.out.println("***********************************************************************");
        System.out.println("* STRESS FERMAT - Network Client - Version 1.0 (2016)                            *");
        System.out.println("* www.fermat.org                                                      *");
        System.out.println("***********************************************************************");
        System.out.println("");


        System.out.println("Ingrese numero de conexiones a usar: ");

        Scanner scanner = new Scanner(System.in);
        int conections = scanner.nextInt();

        CoreManager coreManager = new CoreManager();
        coreManager.setCoreCount(conections);
        coreManager.startStressTest();

    }

}
