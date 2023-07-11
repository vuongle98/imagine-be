package com.vuongle.imagine.utils;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class StringUtils {

    public static String removeAccents(String text) {
        String temp = Normalizer.normalize(text, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        temp = pattern.matcher(temp).replaceAll("");
        return temp.replaceAll("đ", "d");
    }
//    {
//        return Normalizer
//                .normalize(text, Normalizer.Form.NFD)
//                .replaceAll("[^\\p{ASCII}]", "").replaceAll("[?!@#$%^&*()=+{}:;\\.\\,\\'\\?\\>\\<\\/\"|\\[\\]]\\\\*", "");
//
//    }


    public static String preprocessFilePath(String text) {
        text = text.replaceAll("[^a-zA-Z0-9-_. ]", " ");
        return text;
    }
}
