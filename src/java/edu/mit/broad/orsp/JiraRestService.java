package edu.mit.broad.orsp;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.reflect.*;

/**
 * Created with IntelliJ IDEA.
 * User: hbochner
 * Date: 10/16/13
 * Time: 12:44 PM
 */
public class JiraRestService {
    private String jiraUrl;
    private String userName;
    private String password;
    private String baseUrl;

    private String defaultProject;
    private Client jerseyClient;

    static Type mapType = new TypeToken<Map<String, Object>>() {}.getType();

    public JiraRestService()
            throws IOException
    {
        readProperties("jira.properties");
    }

    private void readProperties(String fileName)
            throws IOException
    {
        URL url = this.getClass().getResource(fileName);
        InputStream in = url.openStream();
        if (in == null) {
            throw new IOException("can't find properties file '"
                                          + fileName + "'");
        }
        Properties props = new Properties();
        props.load(in);
        jiraUrl = props.getProperty("jira");
        userName = props.getProperty("user");
        password = props.getProperty("password");
        defaultProject = props.getProperty("project");
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getJiraUrl() {

        return jiraUrl;
    }

    public void setJiraUrl(String jiraUrl) {
        this.jiraUrl = jiraUrl;
    }

    public String getBaseUrl() {
        if (baseUrl == null) {
            baseUrl = jiraUrl;
            // only add slash if it isn't there, so that we're robust against
            // variations in the properties
            if (!jiraUrl.endsWith("/")) {
                baseUrl += "/";
            }
            baseUrl += "rest/api/2/";
        }
        return baseUrl;
    }

    public String getDefaultProject() {
        return defaultProject;
    }

    public Client getJerseyClient() {

        if (jerseyClient == null) {
            DefaultClientConfig clientConfig = new DefaultClientConfig();
            jerseyClient = Client.create(clientConfig);
            jerseyClient.addFilter(new HTTPBasicAuthFilter(userName, password));
        }
        return jerseyClient;
    }

    /*
     * this version can take query params
     */
    public WebResource getter(String method, String key) {
        String url = getBaseUrl() + method;
        if (key != null) {
            url += "/" + key;
        }
        return getJerseyClient().resource(url);
    }

    public WebResource.Builder setJson(WebResource resource) {
        return resource.type(MediaType.APPLICATION_JSON_TYPE)
                       .accept(MediaType.APPLICATION_JSON_TYPE);
    }

    /*
     * this version is set for JSON input and output
     */
    public WebResource.Builder putOrPoster(String method, String key) {
        return setJson(getter(method, key));
    }

    public void checkStatus(ClientResponse cr, String kind)
            throws IOException
    {
        int status = cr.getStatus();
        if (status >= 300) {
            String msg = cr.getEntity(String.class);
            if (msg == null) {
                msg = "";
            }
            throw new IOException(kind + " failed: " + msg);
        }
    }

    public Map<String, Object> doGet(WebResource resource, String kind)
            throws IOException
    {
        ClientResponse cr = setJson(resource).get(ClientResponse.class);
        checkStatus(cr, kind);

        Gson gson = new Gson();
        Map<String, Object> data
                = gson.fromJson(cr.getEntity(String.class), mapType);

        return data;
    }

    public Map<String, Object> getIssue(String key)
            throws IOException
    {
        WebResource resource = getter("issue", key);
        resource = resource.queryParam("fields", "*all");

        return doGet(resource, "getIssue");
    }

    public Map<String, Object> getFieldDescriptions(String project,
                                                    String issueType)
            throws IOException
    {
        WebResource resource = getter("issue/createmeta", null);
        if (project != null) {
            resource = resource.queryParam("projectKeys", project);
        }
        if (issueType != null) {
            resource = resource.queryParam("issuetypeNames", issueType);
        }
        resource = resource.queryParam("expand", "projects.issuetypes.fields");

        Map<String, Object> data = doGet(resource, "get field descriptions");
        Map<String, Object> result = new HashMap<>();

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> types
                = (List<Map<String, Object>>)
                Utils.getNested(data, "projects[0].issuetypes");
        for (Map<String, Object> type : types) {
            @SuppressWarnings("unchecked")
            Map<String, Object> fields
                    = (Map<String, Object>) type.get("fields");
            if (fields == null) {
                continue;
            }

            for (Map.Entry entry : fields.entrySet()) {
                result.put((String) entry.getKey(), entry.getValue());
            }
        }

        return result;
    }

    public void doPut(String method, String key, Object data, String kind)
            throws IOException
    {
        Gson gson = new Gson();
        String json = gson.toJson(data);
        ClientResponse cr = putOrPoster(method, key).put(ClientResponse.class, json);
        checkStatus(cr, kind);
    }

    public Map<String, Object> doPost(String method, String key, Object data, String kind)
            throws IOException
    {
        Gson gson = new Gson();
        String json = gson.toJson(data);
        ClientResponse cr = putOrPoster(method, key).post(ClientResponse.class, json);
        checkStatus(cr, kind);

        Map<String, Object> result = null;
        if (cr.hasEntity()) {
            result = gson.fromJson(cr.getEntity(String.class), mapType);
        }

        return result;
    }

    public void updateIssue(String key, Map<String, Object> changes)
            throws IOException
    {
        Map<String, Object> data = Utils.mapContainer("fields", changes);
        doPut("issue", key, data, "update issue");
    }


    public String createIssue(String projectKey, String type, Map<String, Object> fields)
            throws IOException
    {
        // copy the input map so we can add things without changing the caller's copy
        Map<String, Object> data = new HashMap<>(fields);
        data.put("project", Utils.mapContainer("key", projectKey));
        data.put("issuetype", Utils.mapContainer("name", type));
        data = Utils.mapContainer("fields", data);

        Map<String, Object> result = doPost("issue", null, data, "create issue");

        String key = null;
        if (result != null) {
            key = (String) result.get("key");
        }

        return key;
    }
}
