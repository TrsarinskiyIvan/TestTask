package com.mycompany.testtask;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main implements Runnable {

    private Worker worker = new Worker();
    private ServerSocket serverSocket;

    public Main() {
        try {
            serverSocket = new ServerSocket(9876);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized Socket getSocket() {
        try {
            return serverSocket.accept();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    @Override
    public void run() {

        for (int i = 0; i < 3; i++) {

            Thread tmp = new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        Socket socket = null;
                        socket = getSocket();

                        InputStream input = socket.getInputStream();
                        OutputStream output = socket.getOutputStream();

                        byte[] dataByte = new byte[4];
                        for (int i = 0; i < 4; i++) {
                            dataByte[i] = (byte) input.read();
                        }
                        int sizeData = ByteBuffer.wrap(dataByte).getInt();
                        byte[] data = new byte[sizeData-1];

                        for (int i = 0; i < data.length; i++) {
                            data[i] = (byte) input.read();
                        }

                        String feedBack = new String(data);
                        feedBack = worker.toHash(feedBack);

                        data = feedBack.getBytes();

                        ByteBuffer buffer = ByteBuffer.allocate(4);
                        buffer = buffer.putInt(data.length);
                        dataByte = buffer.array();

                        for (int i = 0; i < dataByte.length; i++) {
                            output.write(dataByte[i]);
                        }

                        output.flush();

                        for (int i = 0; i < data.length; i++) {
                            output.write(data[i]);
                        }
                        output.flush();
                        output.close();
                        input.close();
                        socket.close();

                    } catch (IOException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            });

            tmp.start();
            try {
                tmp.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        try {
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Main ended");
    }

}
