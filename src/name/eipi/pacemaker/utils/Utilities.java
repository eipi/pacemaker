package name.eipi.pacemaker.utils;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import name.eipi.pacemaker.controllers.DataLodge;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by dbdon_000 on 27/09/2014.
 */
public class Utilities {

    public static String toFancyString(Object obj) {
        if (obj == null) {
            return "\u2205";
        }
        StringBuilder sb = new StringBuilder();
        try {
            Collection<Element> elements = new ArrayList<Element>();
            Field[] fields = obj.getClass().getDeclaredFields();
            Integer vertBorder = 1;
            for (Field f : fields) {
                Object value = f.get(obj);
                Integer nameLength = f.getName().length();
                Integer valLength = value == null ? 0 : value.toString().length();
                Integer maxLength = nameLength >= valLength ? nameLength + 2 : valLength + 2;
                elements.add(new Element(f.getName(),
                        value == null ? "" : value.toString(),
                        maxLength));
                vertBorder += maxLength;
            }

            appendHoriz(sb, vertBorder).append("\r\n");
            appendHoriz(sb, 1);
            for (Element e : elements) {
                sb.append(padAndBorder(e.name, e.length));
            }
            appendHoriz(sb, vertBorder).append("\r\n");
            appendHoriz(sb, 1);
            for (Element e : elements) {
                sb.append(padAndBorder(e.value, e.length));
            }
            appendHoriz(sb, vertBorder).append("\r\n");


        } catch (IllegalAccessException iEx) {
            //ignore;
        }
        return sb.toString();
    }

    private static StringBuilder appendHoriz(StringBuilder sb, Integer n) {
        for (int i = 0; i  < n; i++) {
            sb.append("_");
        }
        return sb;
    }

    private static StringBuilder appendSpace(StringBuilder sb, Integer n) {
        for (int i = 0; i  < n; i++) {
            sb.append(" ");
        }
        return sb;
    }

    private static String padAndBorder(String s, Integer length) {
        StringBuilder sb = new StringBuilder(s);
        appendSpace(sb, 1);
        sb.reverse();
        while (sb.length() < length) {
            appendSpace(sb, 1);
        }
        return sb.reverse().append("|").toString();

    }

}

class Element {
    String name;
    String value;
    Integer length;

    Element(String name, String value, Integer length) {
        this.name = name;
        this.value = value;
        this.length = length;
    }
}
