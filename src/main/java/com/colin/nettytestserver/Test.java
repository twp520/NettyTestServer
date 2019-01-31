package com.colin.nettytestserver;

public class Test {
    public static void main(String[] args) {
        try {
            new EachServer().start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
