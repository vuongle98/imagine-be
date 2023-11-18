package com.vuongle.imagine.utils;

import java.text.Normalizer;
import java.util.Locale;
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

    private static final Pattern NON_LATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    public static String toSlug(String input) {
        String noWhitespace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(noWhitespace, Normalizer.Form.NFD);
        String slug = NON_LATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH);
    }
}
