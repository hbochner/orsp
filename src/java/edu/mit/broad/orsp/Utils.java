package edu.mit.broad.orsp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: loaner
 * Date: 10/17/13
 * Time: 12:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class Utils {
    static Pattern indexedPat = Pattern.compile("(\\w+)\\[(\\d+)]");

    static public Object getNested(Object in, String name) {
        if (in == null) {
            return null;
        }

        int inx = name.indexOf(".");
        if (inx > 0) {
            String term = name.substring(0, inx);
            String rest = name.substring(inx + 1);
            return getNested(getNested(in, term), rest);
        }
        if (! (in instanceof Map)) {
            throw new Error("getNested works only on maps");
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> map = (Map<String, Object>) in;

        Matcher matcher = indexedPat.matcher(name);
        if (matcher.matches()) {
            @SuppressWarnings("unchecked")
            List<Object> list = (List<Object>) map.get(matcher.group(1));
            return list.get(Integer.parseInt(matcher.group(2)));
        }

        return map.get(name);
    }

    static public Map<String, Object> mapContainer(String key, Object value) {
        Map<String, Object> map = new HashMap<>(1);
        map.put(key, value);

        return map;
    }
}
