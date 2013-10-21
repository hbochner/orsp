package edu.mit.broad.orsp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: hbochner
 * Date: 10/16/13
 * Time: 4:38 PM
 */
public class JiraIssueProxy {
    JiraRestService jira;
    String key;
    Map<String, Object> theIssue;
    Map<String, Object> changes;

    static Map<String, Map<String, Object>> fieldByName;
    static Map<String, String> idByName;

    public JiraIssueProxy(JiraRestService service) {
        jira = service;
    }

    public JiraIssueProxy()
        throws IOException
    {
        this(new JiraRestService());
    }

    private void initFields()
        throws IOException
    {
        if (fieldByName == null) {
            Map<String, Map<String, Object>> fields = new HashMap<>();
            Map<String, String> ids = new HashMap<>();
            Map<String, Object> data = jira.getFieldDescriptions("ORSP", null);
            for (Map.Entry entry: data.entrySet()) {
                @SuppressWarnings("unchecked")
                Map<String, Object> map = (Map<String, Object>) entry.getValue();
                // store the fieldId in the structure for our convenience
                map.put("fieldId", entry.getKey());
                String name = (String) map.get("name");
                fields.put(name.toLowerCase(), map);
                ids.put(name, (String) entry.getKey());
            }
            fieldByName = fields;
            idByName = ids;
        }
    }

    private void loadIfNeeded()
        throws IOException
    {
        if (theIssue == null && key != null) {
            Map<String, Object> data = jira.getIssue(key);
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) data.get("fields");
            theIssue = map;
        }
    }

    public Map<String, Object> getFieldMetaData(String name)
        throws IOException
    {
        initFields();
        return fieldByName.get(name);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private Object getObjectById(String id)
        throws IOException
    {
        loadIfNeeded();
        if (theIssue == null) {
            return null;
        }

        return  theIssue.get(id);
    }

    public Object getObject(String id, String property)
        throws IOException
    {
        Object obj = getObjectById(id);
        if (obj == null) {
            return null;
        }
        if (property != null) {
            if (! (obj instanceof Map)) {
                throw new IOException("property look-up on non-map");
            }
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) obj;
            obj = map.get(property);
        }

        return obj;
    }

    static public Map<String, Object> mapContainer(String key, Object value) {
        Map<String, Object> map = new HashMap<>(1);
        map.put(key, value);

        return map;
    }

    public List<Object> getMulti(String id, String property)
        throws IOException
    {
        Object obj = getObjectById(id);
        if (obj == null) {
            return null;
        }

        if (! (obj instanceof List)) {
            throw new IOException("non-list for multi value");
        }

        @SuppressWarnings("unchecked")
        List<Object> val = (List<Object>) obj;

        if (property != null) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> maps = (List<Map<String, Object>>) obj;
            List<Object> tmp = new ArrayList<>(val.size());
            for (Map<String, Object> item: maps) {
                tmp.add(mapContainer(property, item.get(property)));
            }
            val = tmp;
        }

        return val;
    }

    private void noteChange(String name, Object value) {
        if (changes == null) {
            changes = new HashMap<>();
        }

        changes.put(name,, value);
    }

    private static boolean singleChanged(Object a, Object b) {
        if (a != null) {
            return a.equals(b);
        }
        if (b != null) {
            return false;
        }
        return true;
    }

    public void setObject(String name, Object value, String property)
        throws IOException
    {
        Object old = getObject(name, property);
        if (singleChanged(old, value)) {
            noteChange(name, value);
        }
    }
}
