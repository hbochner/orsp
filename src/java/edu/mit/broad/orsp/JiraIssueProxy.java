package edu.mit.broad.orsp;

import java.io.IOException;
import java.util.HashMap;
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

    public String getFieldId(String name)
        throws IOException
    {
        initFields();
        String id = idByName.get(name);
        if (id == null) {
            throw new IOException("unrecognized field '" + name + "'");
        }

        return id;
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
}