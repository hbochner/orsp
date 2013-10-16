package edu.mit.broad.orsp;

import edu.mit.broad.jira.JiraService;
import edu.mit.broad.jira.customfields.CustomField;
import edu.mit.broad.jira.customfields.CustomFieldDefinition;
import edu.mit.broad.jira.issue.CreateFields;
import edu.mit.broad.jira.issue.JiraIssue;

import java.io.IOException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: hbochner
 * Date: 10/3/13
 * Time: 10:07 AM
 * To change this template use File | Settings | File Templates.
 */
public class JiraProxy
{
    static Map<String, CustomFieldDefinition> fieldDefs   = null;
    static Map<String, CustomFieldDefinition> fieldByName = new HashMap<>();
    static String[]                           fieldNames  = null;
    JiraService       jira     = OrspJiraService.getInstance();
    JiraIssue         theIssue = null;
    String            key      = null;
    List<CustomField> changes  = null;
//    boolean loaded = false;

    public JiraProxy(String key) {
        this.key = key;
    }

    public JiraProxy() {
    }

    private void initFields()
            throws IOException
    {
        if (fieldDefs == null) {
            fieldDefs = jira.getCustomFields();
            fieldNames = new String[fieldDefs.size()];
            int i = 0;
            for (CustomFieldDefinition fld : fieldDefs.values()) {
                fieldNames[i++] = fld.getJiraCustomFieldId();
                fieldByName.put(fld.getName().toLowerCase(), fld);
            }
        }
    }

    private void loadIfNeeded()
            throws IOException
    {
        if (theIssue == null) {
            initFields();
            if (key != null) {
                theIssue = jira.getIssueInfo(key, fieldNames);
            }
        }
    }

    public boolean knownField(String fieldName) {
        return fieldByName.containsKey(fieldName);
    }

    public Object getObject(String fieldName)
            throws IOException
    {
        loadIfNeeded();
        if (theIssue == null) {
            return null;
        }

        CustomFieldDefinition dfn = fieldByName.get(fieldName.toLowerCase());
        if (dfn == null) {
            throw new IOException("unrecognized field name '" + fieldName + "'");
        }

        Object obj = theIssue.getFieldValue(dfn.getJiraCustomFieldId());
        return obj;
    }

    private void noteChange(String fieldName, Object value) {
        CustomFieldDefinition dfn = fieldByName.get(fieldName.toLowerCase());
        CustomField field = new CustomField(dfn, value);
        if (changes == null) {
            changes = new ArrayList<>();
        }
        changes.add(field);
    }

    public String getString(String fieldName)
            throws IOException
    {
        Object obj = getObject(fieldName);
        if (!(obj instanceof String)) {
            // log an error
            return null;
        }

        return (String) obj;
    }

    private boolean stringChanged(String oldValue, String value) {
        if (value == null && oldValue == null) {
            return false;
        }
        if (value == null || oldValue == null) {
            return true;
        }
        if (value.equals(oldValue)) {
            return false;
        }
        return true;
    }

    private boolean multiChanged(String[] old, String[] values) {
        if (old == null) {
            return true;
        }

        Set<String> oldSet = new HashSet<>(Arrays.asList(old));
        Set<String> newSet = new HashSet<>(Arrays.asList(values));

        return !oldSet.equals(newSet);
    }

    public void setString(String fieldName, String value)
            throws IOException
    {
        String oldValue = getString(fieldName);
        if (stringChanged(oldValue, value)) {
            noteChange(fieldName, value);
        }
    }

    public Map<String, String> getMap(String fieldName)
            throws IOException
    {
        Object obj = getObject(fieldName);
        if (obj == null) {
            return null;
        }

        if (!(obj instanceof Map)) {
            // log an error
//            return null;
            throw new IOException("didn't get expected map for '" + fieldName + "'");
        }
        @SuppressWarnings("unchecked")
        Map<String, String> map = (Map) obj;

        return map;
    }

    public String getValueString(String fieldName)
            throws IOException
    {
        Map<String, String> map = getMap(fieldName);
        if (map == null) {
            return null;
        }

        return map.get("value");
    }

    public void setValueString(String fieldName, String value)
            throws IOException
    {
        String oldValue = getValueString(fieldName);
        if (stringChanged(oldValue, value)) {
            noteChange(fieldName, valueMap(value));
        }
    }

    public void setNameString(String fieldName, String value)
            throws IOException
    {
        Map<String, String> oldMap = getMap(fieldName);
        String oldValue = null;
        if (oldMap != null) {
            oldValue = oldMap.get("name");
        }
        if (stringChanged(oldValue, value)) {
            noteChange(fieldName, keyedMap("name", value));
        }
    }

    // NB: should fetch available selection options using createMeta or editmeta
//    auto-add "Not Specified"?
    public String[] getMultiValueString(String fieldName)
            throws IOException
    {
        Object obj = getObject(fieldName);
        if (obj == null) {
            return null;
        }

        if (!(obj instanceof List)) {
            // log an error
//            return null;
            throw new IOException("didn't get expected list for '" + fieldName + "'");
        }

        @SuppressWarnings("unchecked")
        List<Map<String, String>> list = (List) obj;
        String[] result = new String[list.size()];
        int i = 0;
        for (Map<String, String> map : list) {
            result[i++] = map.get("value");
        }

        return result;
    }

    private Map<String, String> keyedMap(String key, String value) {
        Map<String, String> result = new HashMap<>(1);
        result.put(key, value);
        return result;
    }

    private Map<String, String> valueMap(String value) {
        return keyedMap("value", value);
    }

    private Map<String, String> nameMap(String value) {
        return keyedMap("name", value);
    }

    public void setMultiValueString(String fieldName, String[] values)
            throws IOException
    {
        String[] old = getMultiValueString(fieldName);
        if (multiChanged(old, values)) {
            List<Object> list = new ArrayList<>();
            for (String val : values) {
                list.add(valueMap(val));
            }
            noteChange(fieldName, list);
        }
    }

    public void update()
            throws IOException
    {
        if (changes == null) {
            return;
        }

        jira.updateIssue(key, changes);
    }

    public String addIssue()
            throws IOException
    {
        // need to pick out summary field, and do it separately, because of bad API
        String summary = "summary";
        for (int i = 0; i < changes.size(); i++) {
            if (changes.get(i).getFieldDefinition().getJiraCustomFieldId().equals("summary")) {
                summary = (String) changes.get(i).getValue();
                changes.remove(i);
                break;
            }
        }
        theIssue = jira.createIssue("ORSP",
                                    null, // dummy value for reporter
                                    CreateFields.IssueType.ORSP_NHSR,
                                    summary, null,
                                    changes
                                   );
        key = theIssue.getKey();
        return key;
    }
}
