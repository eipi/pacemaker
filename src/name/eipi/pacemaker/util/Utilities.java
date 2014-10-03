package name.eipi.pacemaker.util;

import name.eipi.pacemaker.models.BaseEntity;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by dbdon_000 on 27/09/2014.
 */
public class Utilities {

    public static <T extends BaseEntity> String toFancyString(Collection<T> coll) {

        if (coll == null) {
            return "\u2205";
        }
        StringBuilder sb = new StringBuilder();
        try {
            if (!coll.isEmpty()) {

                Collection<Element> elements = new ArrayList<Element>();
                Method[] methods = coll.iterator().next().getClass().getDeclaredMethods();

                Integer maxHorizBorder = 1;

                for (T t : coll) {
                    Integer horizBorder = 1;
                    for (Method m : methods) {
                        if (m.getName().startsWith("get")) {
                            String fieldName = m.getName().replaceFirst("get", "");

                            Object value = m.invoke(t);
                            Integer nameLength = fieldName.length();
                            Integer valLength = value == null ? 0 : value.toString().length();
                            Integer maxLength = nameLength >= valLength ? nameLength + 2 : valLength + 2;
                            elements.add(new Element(fieldName,
                                    value == null ? "" : value.toString(),
                                    maxLength));
                            horizBorder += maxLength;
                        }

                    }
                    maxHorizBorder = horizBorder > maxHorizBorder ? horizBorder : maxHorizBorder;
                }


                appendHoriz(sb, maxHorizBorder).append("\r\n");
                Element title = elements.iterator().next();
                    sb.append(padAndBorder(title.name, title.length));
                appendHoriz(sb.append("\r\n"), maxHorizBorder).append("\r\n");
                for (Element e : elements) {
                    sb.append(padAndBorder(e.value, e.length));
                }
                appendHoriz(sb.append("\r\n"), maxHorizBorder).append("\r\n");

            }


        } catch (Throwable t) {
            t.printStackTrace();
        }
        return sb.toString();
    }

    public static String toFancyString(BaseEntity obj) {
        if (obj == null) {
            return "\u2205";
        }
        StringBuilder sb = new StringBuilder();
        try {
            Collection<Element> elements = new ArrayList<Element>();
            Method[] methods = obj.getClass().getDeclaredMethods();
            Integer horizBorder = 1;
            for (Method m : methods) {
                if (m.getName().startsWith("get")) {
                    String fieldName = m.getName().replaceFirst("get", "");
                    Object value = m.invoke(obj);
                    Integer nameLength = fieldName.length();
                    Integer valLength = value == null ? 0 : value.toString().length();
                    Integer maxLength = nameLength >= valLength ? nameLength + 2 : valLength + 2;
                    elements.add(new Element(fieldName,
                            value == null ? "" : value.toString(),
                            maxLength));
                    horizBorder += maxLength;
                }

            }

            appendHoriz(sb, horizBorder).append("\r\n");
            for (Element e : elements) {
                sb.append(padAndBorder(e.name, e.length));
            }
            appendHoriz(sb.append("\r\n"), horizBorder).append("\r\n");
            for (Element e : elements) {
                sb.append(padAndBorder(e.value, e.length));
            }
            appendHoriz(sb.append("\r\n"), horizBorder).append("\r\n");


        } catch (Throwable t) {
            t.printStackTrace();
        }
        return sb.toString();
    }

    private static StringBuilder appendHoriz(StringBuilder sb, Integer n) {
        for (int i = 0; i < n; i++) {
            sb.append("_");
        }
        return sb;
    }

    private static StringBuilder appendSpace(StringBuilder sb, Integer n) {
        for (int i = 0; i < n; i++) {
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
        return sb.reverse().toString();

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
