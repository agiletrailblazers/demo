package com.atb.demo.stagger;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TypeUtils {
    private static final List<String> basicTypes;

    static {
        String[] a = {
                "string", "boolean", "Date", "int", "Array", "long", "List", "void", "float", "double"
        };
        basicTypes = Arrays.asList(a);
    }

    private static final Pattern pattern = Pattern.compile("^(Array|List)\\[(\\w+)]$");

    public static final Pattern genericPattern = Pattern.compile("^(.*)<.*>$");

    public static String getTrueType(String dataType) {
        if(dataType == null) {
            return null;
        }
        String t;
        Matcher m = pattern.matcher(dataType);
        if (m.find()) {
            t = m.group(2);
        } else {
            m = genericPattern.matcher(dataType);
            if (m.find()) {
                try {
                    t = m.group(1);
                } catch (IllegalStateException e) {
                    System.out.println(dataType);
                    return dataType;
                }
            } else {
                t = dataType;
            }
        }
        if (basicTypes.contains(t)) {
            t = null;
        }

        return t;
    }

    public static String filterBasicTypes(String linkType) {
        if (basicTypes.contains(linkType)) {
            return null;
        }
        return linkType;
    }

    public static String upperCaseFirstCharacter(String inputString) {
        return inputString.substring(0, 1).toUpperCase() + inputString.substring(1);
    }

    public static String AsArrayType(String elementType) {
        return "Array[" + elementType + "]";
    }
}