package com.mycompany.testtask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Provider implements Runnable {

    private String path;
    private InputStream input;
    private OutputStream output;

    public Provider(String path) {
        this.path = path;
    }

    @Override
    public void run() {

        Socket socket = null;

        try {
            socket = new Socket("localhost", 9876);
            input = socket.getInputStream();
            output = socket.getOutputStream();

            sendData();
            reciveData();
            
            input.close();
            output.close();
            socket.close();

        } catch (IOException ex) {
            Logger.getLogger(Provider.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Provider ended");
    }

    public void sendData() {

        File file = new File(path);
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Provider.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {

            int sizeData = (int) file.length();
            ByteBuffer sizeBuffer = ByteBuffer.allocate(4);
            sizeBuffer.putInt(sizeData);

            byte[] sizeToArr = sizeBuffer.array();

            output.write(sizeToArr);
            output.flush();
            int b;
            for (int i = 0; i < sizeData; i++) {
                b = fis.read();
                output.write(b);
            }
            output.flush();

        } catch (IOException ex) {
            Logger.getLogger(Provider.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ex) {
                    Logger.getLogger(Provider.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }

    public void reciveData() {

        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(path, true);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Provider.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {

            byte[] sizeArr = new byte[4];

            for (int i = 0; i < 4; i++) {
                sizeArr[i] = (byte) input.read();
            }

            int sizeInt = ByteBuffer.wrap(sizeArr).getInt();

            for (int i = 0; i < sizeInt; i++) {
                fos.write(input.read());
            }

            output.flush();

        } catch (IOException ex) {
            Logger.getLogger(Provider.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ex) {
                    Logger.getLogger(Provider.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
