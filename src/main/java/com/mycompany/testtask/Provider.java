package com.mycompany.testtask;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
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

            socket.close();

        } catch (IOException ex) {
            Logger.getLogger(Provider.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Provider ended");
    }

    public void sendData() {

        FileInputStream fis = null;

        try {
            fis = new FileInputStream(path);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Provider.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {

            int currentByte;

            while ((currentByte = fis.read()) != -1) {
                output.write(currentByte);
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
        System.out.println("Data were sent");
    }

    public void reciveData() {

        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(path, true);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Provider.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {

            while (true) {

                int size = input.available();
                if (size != 0) {

                    int currentByte = input.read();
                    fos.write(currentByte);

                } else {
                    fos.flush();
                    break;
                }

            }

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
        System.out.println("Recived");
    }
}
