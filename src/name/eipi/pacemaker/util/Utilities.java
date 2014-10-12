package name.eipi.pacemaker.util;

import name.eipi.pacemaker.models.BaseEntity;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dbdon_000 on 27/09/2014.
 */
public class Utilities {

    private static String CR = "\r\n";
    
    public static <T extends BaseEntity> String toFancyString(Collection<T> coll) {

        if (coll == null) {
            return "\u2205";
        }
        StringBuilder sb = new StringBuilder();
        try {
            if (!coll.isEmpty()) {

                Collection<Collection<Element>> elements = new ArrayList<Collection<Element>>();
                Method[] methods = coll.iterator().next().getClass().getMethods();

                Integer maxHorizBorder = 1;

                Map<String, Integer> maxLengthsMap = new HashMap<>();

                for (T t : coll) {
                    Collection element = new ArrayList<>();
                    Integer horizBorder = 1;
                    for (Method m : methods) {
                        String name = m.getName();
                        if (name.startsWith("get") && !name.equals("getClass")) {
                            String fieldName = name.replaceFirst("get", "");
                            Object value = m.invoke(t);
                            Integer nameLength = fieldName.length();
                            Integer valLength = value == null ? 0 : value.toString().length();
                            Integer maxLength = nameLength >= valLength ? nameLength + 2 : valLength + 2;
                            if (maxLengthsMap.containsKey(fieldName)) {
                                if (maxLength > maxLengthsMap.get(fieldName)) {
                                    maxLengthsMap.put(fieldName, maxLength);
                                }
                            } else {
                                maxLengthsMap.put(fieldName, maxLength);
                            }
                            element.add(new Element(fieldName, value == null ? "" : value.toString(), null));
                            horizBorder += maxLength + 1;
                        }
                    }
                    maxHorizBorder = horizBorder > maxHorizBorder ? horizBorder : maxHorizBorder;
                    elements.add(element);
                }

                // Top Line
                appendHoriz(sb, maxHorizBorder).append(CR);

                // Headings
                for (Element e : elements.iterator().next()) {
                    appendVert(sb).append(padAndBorder(e.name, maxLengthsMap.get(e.name)));
                }
                appendVert(sb).append(CR);
                appendVert(appendHoriz(appendVert(sb), maxHorizBorder - 2)).append(CR);

                // Data
                for (Collection<Element> objs : elements) {
                    for (Element element : objs) {
                        appendVert(sb).append(padAndBorder(element.value, maxLengthsMap.get(element.name)));
                    }
                    appendVert(sb).append(CR);
                }
                // Bottom Line
                appendVert(appendHoriz(appendVert(sb), maxHorizBorder - 2)).append(CR);

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
            Method[] methods = obj.getClass().getMethods();
            Integer horizBorder = 1;
            for (Method m : methods) {
                String name = m.getName();
                if (name.startsWith("get") && !name.equals("getClass")) {
                    String fieldName = name.replaceFirst("get", "");
                    Object value = m.invoke(obj);
                    Integer nameLength = fieldName.length();
                    Integer valLength = value == null ? 0 : value.toString().length();
                    Integer maxLength = nameLength >= valLength ? nameLength + 2: valLength + 2;
                    elements.add(new Element(fieldName, value == null ? "" : value.toString(), maxLength));
                    horizBorder += maxLength + 1;
                }

            }

            // Top Line
            appendHoriz(sb, horizBorder).append(CR);

            // Headings
            for (Element e : elements) {
                appendVert(sb).append(padAndBorder(e.name, e.length));
            }
            appendVert(sb).append(CR);
            appendVert(appendHoriz(appendVert(sb), horizBorder - 2)).append(CR);

            // Data
            for (Element e : elements) {
                appendVert(sb).append(padAndBorder(e.value, e.length));
            }
            appendVert(sb).append(CR);

            // Bottom Line
            appendVert(appendHoriz(appendVert(sb), horizBorder - 2)).append(CR);


        } catch (Throwable t) {
            t.printStackTrace();
        }
        return sb.toString();
    }

    private static StringBuilder appendHoriz(StringBuilder sb, Integer n) {
        for (int i = 0; i < n; i++) {
            sb.append('_');
        }
        return sb;
    }

    private static StringBuilder appendVert(StringBuilder sb) {
        return sb.append('|');
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
