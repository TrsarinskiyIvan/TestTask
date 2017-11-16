package com.mycompany.testtask;

public class TestTask {

    public static void main(String[] args) throws InterruptedException {

        Main main = new Main();

        Thread mainThread = new Thread(main);
        mainThread.start();

        Thread providerThred;
        for (int i = 0; i < 3; i++) {
            providerThred = new Thread(new Provider(args[i]));
            providerThred.start();
        }


    }

}
