package com.onurkol.app.browser.tools;

import java.util.Random;

public class RandomGenerator {
    private static final String dataRandString = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String dataRandAll = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz|!£$%&/=@#";
    private static final String dataRandInteger = "0123456789";
    private static Random RANDOM = new Random();

    public static String randomString() {
        StringBuilder sb = new StringBuilder(5);
        for (int i = 0; i < 5; i++)
            sb.append(dataRandString.charAt(RANDOM.nextInt(dataRandString.length())));
        return sb.toString();
    }
    public static String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(dataRandString.charAt(RANDOM.nextInt(dataRandString.length())));
        return sb.toString();
    }
    public static String random() {
        StringBuilder sb = new StringBuilder(5);
        for (int i = 0; i < 5; i++)
            sb.append(dataRandAll.charAt(RANDOM.nextInt(dataRandAll.length())));
        return sb.toString();
    }
    public static String random(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(dataRandAll.charAt(RANDOM.nextInt(dataRandAll.length())));
        return sb.toString();
    }
    public static int randomInteger() {
        StringBuilder sb = new StringBuilder(5);
        for (int i = 0; i < 5; i++)
            sb.append(dataRandInteger.charAt(RANDOM.nextInt(dataRandInteger.length())));
        return Integer.parseInt(sb.toString());
    }
    public static int randomInteger(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(dataRandInteger.charAt(RANDOM.nextInt(dataRandInteger.length())));
        return Integer.parseInt(sb.toString());
    }
}
