package com.afinos.api.helper;

import java.util.Arrays;

/**
 * @Copy Right 2012-2016, Afinos, Inc., or its affiliates
 * @Author: Afinos Team
 **/
public class CharHelper {
    public static String sort(String str) {
        char[] ar = str.toCharArray();
        Arrays.sort(ar);
        return String.valueOf(ar);
    }
}
