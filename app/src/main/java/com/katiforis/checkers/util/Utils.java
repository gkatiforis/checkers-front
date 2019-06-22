package com.katiforis.checkers.util;

import java.security.SecureRandom;
import java.util.Date;

public class Utils {

    public static String getRandomString(int len){
        final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }

    public static long getDiffInSeconds(Date d1, Date d2){
        long diff = d2.getTime() - d1.getTime();
        long diffSeconds = diff / 1000;
        return diffSeconds;
    }
}
