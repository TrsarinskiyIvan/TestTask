package com.mycompany.testtask;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main implements Runnable {

    private Worker worker = new Worker();

    @Override
    public void run() {

        ServerSocket serverSocket = null;
        Socket socket = null;

        try {
            serverSocket = new ServerSocket(9876);
            socket = serverSocket.accept();

            InputStream input = socket.getInputStream();
            OutputStream output = socket.getOutputStream();
            int size;

            while (true) {

                size = input.available();
                if (size != 0) {

                    int currentByte = input.read();
                    output.write(currentByte);

                } else {
                    output.flush();
                    break;
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Main ended");
    }

}
