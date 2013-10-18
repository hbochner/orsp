package edu.mit.broad.orsp;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: hbochner
 * Date: 10/18/13
 * Time: 9:58 AM
 */
public class DynaIssueFacade {
    static private Map<String, FieldDescription> fields;
    static private Map<String, String> typeByKey = new HashMap<>();

    private JiraIssueProxy proxy;
    public String key;

    private void init()
        throws IOException
    {
        if (proxy == null) {
            proxy = new JiraIssueProxy();
            proxy.setKey(key);
        }
        if (fields == null) {
            URL url = this.getClass().getResource("fields.properties");
            if (url == null) {
                // log error
                throw new IOException("can't find fields.properties");
            }
            Properties properties = new Properties();
            properties.load(url.openStream());

            Map<String, FieldDescription> flds = new HashMap<>();
            for (String pkey: properties.stringPropertyNames()) {
                String value = properties.getProperty(pkey);

                int inx = pkey.indexOf(".");
                // test for bad key
                String kind = pkey.substring(0, inx);
                String name = pkey.substring(inx + 1);

                if ("issuetype".equals(kind)) {
                    typeByKey.put(name, value);
                } else if ("field".equals(kind)) {
                    Map<String, Object> meta = proxy.getFieldMetaData(name);
                    FieldDescription desc = new FieldDescription(meta);
                    flds.put(value.toLowerCase(), desc);
                } else {
                    // log a warning
                }
            }
            fields = flds;
        }
    }

    public Object get(String name)
        throws IOException
    {
        init();

        String prop = null;
        int inx = name.indexOf("-");
        if (inx > 0) {
            prop = name.substring(inx + 1);
            name = name.substring(inx);
        }
        FieldDescription desc = fields.get(name);
        if (desc == null) {
            // log an error
            throw new IOException("unrecognized field name '" + name + "'");
        }

        Object value = proxy.getObject(desc.id, prop);

        return value;
    }

    class FieldDescription {
        // jira fieldid
        String id;
        Map<String, Object> meta;
        boolean isMulti;
        List<Map<String, Object>> options;

        public FieldDescription(Map<String, Object> meta) {
            this.id = (String) meta.get("fieldId");
            this.meta = meta;
            // To DO: initialize isMulti and options
        }

        String getId() {
            return id;
        }

        boolean isMulti() {
            return isMulti;
        }

        List<Map<String, Object>> getOptions() {
            return options;
        }
    }
}
