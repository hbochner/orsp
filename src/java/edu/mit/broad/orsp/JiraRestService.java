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
import java.util.Map;
import java.util.Properties;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.reflect.*;

/**
 * Created with IntelliJ IDEA.
 * User: loaner
 * Date: 10/16/13
 * Time: 12:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class JiraRestService {
    private String jiraUrl;
    private String userName;
    private String password;
    private String baseUrl;
    private Client jerseyClient;

    static Type mapType = new TypeToken<Map<String, Object>>() {}.getType();

    public JiraRestService()
        throws IOException
    {
        readProperties("file:/Users/loaner/lib/jira.properties");
    }

    private void readProperties(String fileName)
        throws IOException
    {
        // will later find the properties in the jar file
        URL url = new URL(fileName);
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
            baseUrl = jiraUrl + "/rest/api/2/";
        }
        return baseUrl;
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
        resource.queryParam("expand", "projects.issuetypes.fields");

        return doGet(resource, "get field descriptions");
    }
}
