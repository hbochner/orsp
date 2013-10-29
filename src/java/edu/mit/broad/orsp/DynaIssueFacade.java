package edu.mit.broad.orsp;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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
    static private Map<String, String> typeByKey  = new HashMap<>();
    static private Map<String, String> typeByName = new HashMap<>();

    private JiraIssueProxy proxy;
    private String         key;
    private String         issueType;

    public DynaIssueFacade() {
    }

    public DynaIssueFacade(String user, String password)
            throws IOException
    {
        JiraRestService jira = new JiraRestService();
        jira.setUserName(user);
        jira.setPassword(password);
        proxy = new JiraIssueProxy(jira);
    }

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
            for (String pkey : properties.stringPropertyNames()) {
                String value = properties.getProperty(pkey);

                int inx = pkey.indexOf(".");
                // test for bad key
                String kind = pkey.substring(0, inx);
                String name = pkey.substring(inx + 1);

                if ("issuetype".equals(kind)) {
                    typeByKey.put(name, value);
                    typeByName.put(value, name);
                } else if ("field".equals(kind)) {
                    Map<String, Object> meta = proxy.getFieldMetaData(value.toLowerCase());
                    if (meta == null) {
                        throw new IOException("unrecognized field '" + value + "'");
                    }
                    FieldDescription desc = new FieldDescription(meta);
                    flds.put(name, desc);
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
            name = name.substring(0, inx);
        }
        FieldDescription desc = fields.get(name);
        if (desc == null) {
            // log an error
            throw new IOException("unrecognized field name '" + name + "'");
        }

        Object value;
        if (desc.isMulti) {
            value = proxy.getMulti(desc.id, prop);
        } else {
            value = proxy.getObject(desc.id, prop);
        }

        return value;
    }

    public Map<String, FieldDescription> getDesc() {
        return fields;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
        if (proxy != null) {
            proxy.setKey(key);
        }
    }

    public void setType(String type)
            throws IOException
    {
        init();

        issueType = typeByKey.get(type);
        if (issueType == null) {
            throw new Error("unrecognized issue type key '" + type + "'");
        }
    }

    public void setFields(Map<String, Object> input)
            throws Exception
    {
        init();

        for (String name : input.keySet()) {
            Object obj = input.get(name);

            String prop = null;
            int inx = name.indexOf("-");
            if (inx > 0) {
                prop = name.substring(inx + 1);
                name = name.substring(0, inx);
            }

            FieldDescription desc = fields.get(name);
            if (desc == null) {
                // web forms routinely submit items that aren't fields,
                // so don't even log this
                continue;
            }

            if (desc.isMulti) {
                if (obj instanceof String) {
                    obj = new String[]{(String) obj};
                }
                String[] value = (String[]) obj;
                proxy.setMulti(desc.id, value, prop);
            } else {
                proxy.setObject(desc.id, obj, prop);
            }
        }
    }

    public void update()
            throws IOException
    {
        proxy.update();
    }

    public String add()
            throws IOException
    {
        init();

        if (issueType == null) {
            throw new IOException("issue type has not been set");
        }

        key = proxy.add(issueType);
        return key;
    }

    public void addComment(String comment)
            throws IOException
    {
        init();
        proxy.addComment(comment);
    }

    public void addAttachment(File file, String mediaType)
            throws IOException
    {
        init();
        proxy.addAttachment(file, mediaType);
    }

    public void addAttachments(List<MultipartFile> files)
            throws IOException
    {
        init();

        String tempDir = System.getProperty("java.io.tmpdir");
        for (MultipartFile file: files) {
            String name = file.getOriginalFilename();
            if (name == null || "".equals(name)) {
                continue;
            }
            if (name.indexOf("/") >= 0) {
                name = name.replace("/", "%2F");
            }
            File tmp = new File(tempDir, name);
            file.transferTo(tmp);
            try {
                proxy.addAttachment(tmp, file.getContentType());
            } finally {
                tmp.delete();
            }
        }
    }

    class FieldDescription {
        // jira fieldid
        String                    id;
        Map<String, Object>       meta;
        boolean                   isMulti;
        List<Map<String, Object>> options;

        public FieldDescription(Map<String, Object> meta) {
            this.id = (String) meta.get("fieldId");
            this.meta = meta;
            isMulti = "array".equals(Utils.getNested(meta, "schema.type"));
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> tmp = (List<Map<String, Object>>) meta.get("allowedValues");
            options = tmp;
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

    public int issueCount(Map<String, Object> params)
            throws IOException
    {
        init();
        return proxy.searchCount("project = ORSP");
    }

    public List<Map<String, Object>> search(Map<String, Object> params, int max, int offset)
            throws IOException
    {
        init();
        // later translate input params to jql
        List<Map<String, Object>> result = proxy.issueSearch("project = ORSP", max, offset);

        if (result != null) {
            for (Map<String, Object> item: result) {
                item.put("typeKey",
                         typeByName.get(Utils.getNested(item, "fields.issuetype.name")));
            }
        }

        return result;
    }
}
