package org.iop.stress_app.structure.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * This class must return an array of bytes with a selected logo.
 * In this version, this will return a IoP Logo in bytes.
 * Created by Manuel Perez P. (darkpriestrelative@gmail.com) on 25/08/16.
 */
public class IoPBytesArray {

    /**
     * Represents the path when the image is stored.
     * In this case, I'll set the image stored in resources folder
     */
    private final static String IOP_LOGO_PATH = "/images/iop.png";

    /**
     * This method returns an array of bytes representing an image stored in the IOP_LOGO_PATH.
     * @return
     */
    public static byte[] getIoPBytesArray(){
        try{
            InputStream fis = IoPBytesArray.class.getResourceAsStream(IOP_LOGO_PATH);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                //Writes to this byte array output stream
                bos.write(buf, 0, readNum);
                System.out.println("read " + readNum + " bytes,");
            }
            return bos.toByteArray();
        } catch (Exception e){
            return new byte[]{-119, 80, 78, 71, 13, 10, 26, 10, 0, 0, 0, 13, 73, 72, 68, 82,
                    0, 0, 0, 15, 0, 0, 0, 15, 8, 6, 0, 0, 0, 59, -42, -107,
                    74, 0, 0, 0, 64, 73, 68, 65, 84, 120, -38, 99, 96, -62, 14, -2,
                    99, -63, 68, 1, 100, -59, -1, -79, -120, 17, -44, -8, 31, -121, 28, 81,
                    26, -1, -29, 113, 13, 78, -51, 100, -125, -1, -108, 24, 64, 86, -24, -30,
                    11, 101, -6, -37, 76, -106, -97, 25, 104, 17, 96, -76, 77, 97, 20, -89,
                    109, -110, 114, 21, 0, -82, -127, 56, -56, 56, 76, -17, -42, 0, 0, 0,
                    0, 73, 69, 78, 68, -82, 66, 96, -126};
        }

    }

    public static String getIopLogoPath(){
        return IOP_LOGO_PATH;
    }

}
