package br.com.ericsson.smartnotification.utils;

import java.util.Random;
import java.util.UUID;

public final class Util {

    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private Util() {
    }

    public static String gerateId() {
        return UUID.randomUUID().toString();
    }

    public static String randomAlphaNumeric(int size) {
        StringBuilder builder = new StringBuilder();
        Random r = new Random();
        while (size-- != 0) {
            int character = r.nextInt() * ALPHA_NUMERIC_STRING.length();
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString().toLowerCase();
    }

}
