package com.mycompany.testtask;

public class TestTask {

    public static void main(String[] args) throws InterruptedException {

        Main main = new Main();
        Provider provider = new Provider("/home/ivan/file1");

        Thread mainThread = new Thread(main);
        Thread providerThread = new Thread(provider);

        mainThread.start();
        providerThread.start();
        
        providerThread.join();
        mainThread.join();
        
    }

}
