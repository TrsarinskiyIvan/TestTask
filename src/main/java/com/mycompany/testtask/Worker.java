package com.mycompany.testtask;

import com.google.common.hash.Hashing;
import java.nio.charset.StandardCharsets;

public class Worker {

    public String toHash(String s) {
        return Hashing.sha256().hashString(s, StandardCharsets.UTF_8).toString();
    }

}
