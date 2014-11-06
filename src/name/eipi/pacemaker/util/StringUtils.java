package name.eipi.pacemaker.util;

import name.eipi.pacemaker.models.Activity;
import name.eipi.pacemaker.models.BaseEntity;
import name.eipi.pacemaker.models.User;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by dbdon_000 on 27/09/2014.
 */
public class StringUtils {

    private static final String CR = "\r\n";
    private static final String GET = "get";
    private static final String GET_CLASS = "getClass";
    private static final Integer PAD = 2;
    private static final String VERT = "|";
    private static final String HORIZ = "-";
    private static final String CORNER = "+";

    /**
     * Create a fancy String representation of a collection of BaseEntities.
     *
     * @param coll
     * @param <T>
     * @return
     */
    public static <T extends BaseEntity> String toFancyString(Collection<T> coll) {

        if (coll == null || coll.isEmpty() || (coll.size() == 1 && coll.iterator().next() == null)) {
            return "";
        }
        StringBuilder sb = new StringBuilder(CR);

        try {
            Collection<Collection<Element>> elements = new ArrayList<Collection<Element>>();

            T firstOne = coll.iterator().next();
            String title = firstOne.getClass().getSimpleName();

            if (coll.size() > 1) {
                if (title.endsWith("y")) {
                    sb.append(title.substring(0, title.length() - 1) + "ies");
                } else if (!title.endsWith("s")) {
                    sb.append(title).append("s");
                }
            } else {
                sb.append(title);
            }
            sb.append(CR);

            List<Method> display = applyCustomOrderingAndFiltering(firstOne);


            Integer maxHorizBorder = 1;

            Map<String, Integer> maxLengthsMap = new HashMap<>();

            for (T t : coll) {
                if (t != null) {
                    Collection fields = new ArrayList<>();
                    Integer horizBorder = 1;
                    for (Method m : display) {
                        String fieldName = m.getName().substring(GET.length());
                        Object value = m.invoke(t);
                        Integer nameLength = fieldName.length();
                        Integer valLength = value == null ? 0 : value.toString().length();
                        Integer maxLength = nameLength >= valLength ? nameLength : valLength;
                        maxLength += PAD;
                        if (maxLengthsMap.containsKey(fieldName)) {
                            if (maxLength > maxLengthsMap.get(fieldName)) {
                                maxLengthsMap.put(fieldName, maxLength);

                            } else {
                                maxLength = maxLengthsMap.get(fieldName);
                            }
                        } else {
                            maxLengthsMap.put(fieldName, maxLength);
                        }
                        fields.add(new Element(fieldName, value == null ? "" : value.toString(), null));
                        horizBorder += maxLength + 1;

                    }
                    maxHorizBorder = horizBorder > maxHorizBorder ? horizBorder : maxHorizBorder;
                    elements.add(fields);
                }
            }

            // Top Line
            sb.append(CORNER);
            appendHoriz(sb, maxHorizBorder - 2);
            sb.append(CORNER).append(CR);

            // Headings
            for (Element e : elements.iterator().next()) {
                appendVert(sb).append(padAndBorder(e.name.toUpperCase(), maxLengthsMap.get(e.name)));
            }
            appendVert(sb).append(CR);
            appendVert(appendHoriz(appendVert(sb), maxHorizBorder - PAD)).append(CR);

            // Data
            for (Collection<Element> objs : elements) {
                for (Element element : objs) {
                    appendVert(sb).append(padAndBorder(element.value, maxLengthsMap.get(element.name)));
                }
                appendVert(sb).append(CR);
            }
            // Bottom Line
            sb.append(CORNER);
            appendHoriz(sb, maxHorizBorder - 2);
            sb.append(CORNER);

        } catch (Throwable t) {
            t.printStackTrace();
        }
        return sb.toString();
    }


    /**
     * Convenience method for single objects.
     */
    public static <T extends BaseEntity> String toFancyString(T obj) {
        Collection<T> c = new ArrayList<>();
        c.add(obj);
        return toFancyString(c);
    }

    private static StringBuilder appendHoriz(StringBuilder sb, Integer n) {
        for (int i = 0; i < n; i++) {
            sb.append(HORIZ);
        }
        return sb;
    }

    private static StringBuilder appendVert(StringBuilder sb) {
        return sb.append(VERT);
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

    private static <T extends BaseEntity> List<Method> applyCustomOrderingAndFiltering(T t) {

        List<Method> list = new ArrayList<>();

        Method[] classMethods = t.getClass().getMethods();
        Map<String, Method> methodIndex = new HashMap<>();
        for (Method m : classMethods) {
            String name = m.getName();
            if (name.startsWith(GET) && !name.equals(GET_CLASS)) {
                String fieldName = name.substring(GET.length());
                methodIndex.put(fieldName, m);

            }
        }

        // BaseEntity Id always comes first.
        list.add(methodIndex.get("Id"));

        if (t instanceof User) {
            final String[] fields = {"LastName", "FirstName", "Email", "Password", "Activities"};
            for (String field : fields) {
                list.add(methodIndex.get(field));
            }
        } else if (t instanceof Activity) {
            final String[] fields = {"Type", "Location", "Distance", "Routes", "StartTime", "Duration"};
            for (String field : fields) {
                list.add(methodIndex.get(field));
            }
        } else {
            // No custom ordering, just use whatever.
            for (String field : methodIndex.keySet()) {
                if (!"Id".equals(field)) {
                    list.add(methodIndex.get(field));
                }
            }
        }
        return list;
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
