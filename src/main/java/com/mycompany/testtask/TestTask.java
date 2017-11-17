package com.mycompany.testtask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestTask {

    public static void main(String[] args) throws InterruptedException {

        ExecutorService es = Executors.newFixedThreadPool(4);

        es.execute(new Main());

        for (int i = 0; i < 3; i++) {
            es.execute(new Thread(new Provider(args[i])));
        }

        es.shutdown();
    }

}
