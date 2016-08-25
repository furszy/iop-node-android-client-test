package org.iop.client;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by mati on 24/08/16.
 */
public class MainTest {


    public static void main(String[] args){

        File file = new File("ext_classes");

        if (!file.exists()){
            file.mkdir();
        }

        URLClassLoader child = null;
        for (File file1 : file.listFiles()) {
            try {
                System.out.println(file1.getAbsolutePath());
                child = new URLClassLoader(new URL[]{file1.toURL()}, file.getClass().getClassLoader());
                System.out.println(file.getAbsolutePath());

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

//
//        ClassLoader classLoader = new Cldejalo


    }

}
