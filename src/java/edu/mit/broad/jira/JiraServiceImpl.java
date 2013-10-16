package edu.mit.broad.jira;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.MultiPartMediaTypes;
import com.sun.jersey.multipart.file.FileDataBodyPart;
import com.sun.jersey.multipart.impl.MultiPartWriter;
import edu.mit.broad.jira.customfields.CustomField;
import edu.mit.broad.jira.customfields.CustomFieldDefinition;
import edu.mit.broad.jira.customfields.CustomFieldJsonParser;
import edu.mit.broad.jira.issue.CreateFields;
import edu.mit.broad.jira.issue.CreateIssueRequest;
import edu.mit.broad.jira.issue.IssueFieldsResponse;
import edu.mit.broad.jira.issue.IssueResolutionResponse;
import edu.mit.broad.jira.issue.JiraIssue;
import edu.mit.broad.jira.issue.UpdateIssueRequest;
import edu.mit.broad.jira.issue.Visibility;
import edu.mit.broad.jira.issue.comment.AddCommentRequest;
import edu.mit.broad.jira.issue.comment.AddCommentResponse;
import edu.mit.broad.jira.issue.link.AddIssueLinkRequest;
import edu.mit.broad.jira.issue.transition.IssueTransitionListResponse;
import edu.mit.broad.jira.issue.transition.IssueTransitionRequest;
import edu.mit.broad.jira.issue.transition.Transition;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


@SuppressWarnings({"serial", "UnusedDeclaration"})
public class JiraServiceImpl extends AbstractJsonJerseyClientService implements JiraService {

    private JiraConfig jiraConfig;

    private final static Log log = LogFactory.getLog(JiraServiceImpl.class);

    private String baseUrl;

    public JiraServiceImpl() {
        this(JiraConfig.getInstance());
    }

    public JiraServiceImpl(JiraConfig jiraConfig) {
        this.jiraConfig = jiraConfig;
    }

    public static JiraServiceImpl getTestInstance() {
        return new JiraServiceImpl(JiraConfig.getTestInstance());
    }

    @Override
    protected void customizeConfig(ClientConfig clientConfig) {
        supportJson(clientConfig);
    }

    @Override
    protected void customizeClient(Client client) {
        specifyHttpAuthCredentials(client, jiraConfig);
    }

    private String getBaseUrl() {
        if (baseUrl == null) {
            baseUrl = jiraConfig.getUrlBase() + "/rest/api/2";
        }
        return baseUrl;
    }

    // Temporary container class to get ticket ID (key) from the server.
    private static class JiraIssueData {
        private String key;

        public void setId(String id) {
        }

        public void setKey(String key) {
            this.key = key;
        }

        public void setSelf(String self) {
        }

        public String getKey() {
            return key;
        }

        JiraIssueData() {
        }
    }

    private static class JiraSearchIssueData extends JiraIssueData {

        private String expand;
        private String summary;
        private String description;
        private Map<String, Object> extraFields = new HashMap<>();

        private Date dueDate;

        private JiraSearchIssueData() {
        }
    }

    @Override
    public JiraIssue createIssue(String projectPrefix, String reporter, CreateFields.IssueType issueType,
                                 String summary, String description,
                                 Collection<CustomField> customFields) throws IOException {

        CreateIssueRequest issueRequest =
                CreateIssueRequest.create(projectPrefix, reporter, issueType, summary, description, customFields);

        String urlString = getBaseUrl() + "/issue/";
        log.debug("createIssue URL is " + urlString);

        /**
         * To debug this, use curl like so:
         * $ curl  -u squid:squid -X POST -d @curl -H "Content-Type: application/json" http://vsquid00.broadinstitute.org:8020/rest/api/2/issue/
         *
         * Where @curl is a file name and contains something like:
         * {"fields":{"description":"Description created from Mercury","project":{"key":"LCSET"},"customfield_10020":"doofus","customfield_10011":"9999","summary":"Summary created from Mercury","issuetype":{"name":"Whole Exome (HybSel)"}}}
         */

        WebResource webResource = getJerseyClient().resource(urlString);

        JiraIssueData data = post(webResource, issueRequest, new GenericType<JiraIssueData>() {});
        return new JiraIssue(data.key, this);
    }

    @Override
    public JiraIssue getIssue(String key) {

        return new JiraIssue(key, this);

    }

    @Override
    public JiraIssue getIssueInfo(String key, String... fields) throws IOException {
        String urlString = getBaseUrl() + "/issue/" + key;

        StringBuilder fieldList = new StringBuilder("summary,description,duedate");

        if (null != fields) {
            for (String currField : fields) {
                fieldList.append(",").append(currField);
            }
        }

        WebResource webResource = getJerseyClient().resource(urlString).queryParam("fields", fieldList.toString());

        String queryResponse = webResource.get(String.class);

        JiraSearchIssueData data = parseSearch(queryResponse, fields);

        JiraIssue issueResult = new JiraIssue(key, this, data.summary, data.description, data.dueDate);
        if (null != fields) {
            for (String currField : fields) {
                issueResult.addFieldValue(currField, data.extraFields.get(currField));
            }
        }

        return issueResult;
    }

    @SuppressWarnings("rawtypes")
    private JiraSearchIssueData parseSearch(String queryResponse, String... searchFields) throws IOException {

        JiraSearchIssueData parsedResults = new JiraSearchIssueData();

        final Map root = new ObjectMapper().readValue(queryResponse, Map.class);
        parsedResults.setKey((String) root.get("key"));
        final Map fields = (Map) root.get("fields");

        parsedResults.description = (String) fields.get("description");
        parsedResults.summary = (String) fields.get("summary");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dueDateValue = (String) fields.get("duedate");
        try {
            if (StringUtils.isNotBlank(dueDateValue)) {
                parsedResults.dueDate = dateFormat.parse(dueDateValue);
            }
        } catch (ParseException pe) {
            log.error("Unable to parse the Due Date for Jira Issue " + parsedResults.getKey());
        }

        if (searchFields != null) {
            for (String currField : searchFields) {
                parsedResults.extraFields.put(currField, fields.get(currField));
            }
        }

        return parsedResults;
    }

    @Override
    public void updateIssue(String key, Collection<CustomField> customFields) throws IOException {
        UpdateIssueRequest request = new UpdateIssueRequest(key, customFields);
        WebResource webResource = getJerseyClient().resource(request.getUrl(getBaseUrl()));
        put(webResource, request);
    }

    @Override
    public void addComment(String key, String body) throws IOException {
        addComment(key, body, null, null);
    }

    @Override
    public void addComment(String key, String body, Visibility.Type visibilityType,
                           Visibility.Value visibilityValue) throws IOException {

        AddCommentRequest addCommentRequest;

        if (visibilityType != null && visibilityValue != null) {
            addCommentRequest = AddCommentRequest.create(body, visibilityType, visibilityValue);
        } else {
            addCommentRequest = AddCommentRequest.create(body);
        }


        String urlString = getBaseUrl() + "/issue/" + key + "/comment";

        log.debug("addComment URL is " + urlString);

        WebResource webResource = getJerseyClient().resource(urlString);

        // don't really care about the response, not sure why JIRA sends us back so much stuff...
        post(webResource, addCommentRequest, new GenericType<AddCommentResponse>() {
        });

    }

    @Override
    public void addLink(AddIssueLinkRequest.LinkType type,
                        String sourceIssueIn,
                        String targetIssueIn) throws IOException {
        addLink(type, sourceIssueIn, targetIssueIn, null, null, null);
    }

    @Override
    public void addLink(AddIssueLinkRequest.LinkType type, String sourceIssueIn,
                        String targetIssueIn,
                        String commentBody,
                        Visibility.Type availabilityType,
                        Visibility.Value availabilityValue) throws IOException {

        AddIssueLinkRequest linkRequest;

        if (commentBody != null && availabilityType != null && availabilityValue != null) {
            linkRequest = AddIssueLinkRequest.create(type, sourceIssueIn, targetIssueIn,
                    commentBody, availabilityType, availabilityValue);
        } else {
            linkRequest = AddIssueLinkRequest.create(type, sourceIssueIn, targetIssueIn);
        }

        String urlString = getBaseUrl() + "/issueLink";

        WebResource webResource = getJerseyClient().resource(urlString);
        post(webResource, linkRequest);
    }

    @Override
    public void addWatcher(String key, String watcherId) throws IOException {
        WebResource webResource = getJerseyClient().resource(getBaseUrl() + "/issue/" + key + "/watchers");
        post(webResource, watcherId);
    }

    @Override
    public void addAttachment(String key, File file) throws IOException {

        ClientConfig clientConfig = new DefaultClientConfig();
        customizeConfig(clientConfig);
        clientConfig.getClasses().add(MultiPartWriter.class);

        Client client = Client.create(clientConfig);
        customizeClient(client);

        final WebResource attachmentsResource = client.resource(getBaseUrl() + "/issue/" + key + "/attachments");
        final WebResource.Builder builder = attachmentsResource.type(MultiPartMediaTypes.createFormData());
        builder.header("X-Atlassian-Token", "nocheck");

        FormDataMultiPart formDataMultiPart = new FormDataMultiPart();

        FileDataBodyPart bodyPart = new FileDataBodyPart("file", file);
        formDataMultiPart.bodyPart(bodyPart);

        builder.post(formDataMultiPart);
    }

    @Override
    public Map<String, CustomFieldDefinition> getRequiredFields(@Nonnull CreateFields.Project project,
                                                                @Nonnull CreateFields.IssueType issueType) throws
            IOException {

        String urlString = getBaseUrl() + "/issue/createmeta";

        String jsonResponse = getJerseyClient().resource(urlString)
                .queryParam("projectKeys", project.getKey())
                .queryParam("issueTypeNames", issueType.getJiraName())
                .queryParam("expand", "projects.issuetypes.fields")
                .get(String.class);

        return CustomFieldJsonParser.parseRequiredFields(jsonResponse);
    }

    @Override
    public Map<String, CustomFieldDefinition> getCustomFields(String... fieldNames) throws IOException {
        String urlString = getBaseUrl() + "/field";

        String jsonResponse = getJerseyClient().resource(urlString).get(String.class);
        Map<String, CustomFieldDefinition> customFieldDefinitionMap = CustomFieldJsonParser
                .parseCustomFields(jsonResponse);

        if (fieldNames.length == 0) {
            return customFieldDefinitionMap;
        }

        Set<String> fieldNamesSet = new HashSet<>(Arrays.asList(fieldNames));
        Map<String, CustomFieldDefinition> filteredMap = new HashMap<>();
        for (Map.Entry<String, CustomFieldDefinition> entry : customFieldDefinitionMap.entrySet()) {
            if (fieldNamesSet.contains(entry.getKey())) {
                filteredMap.put(entry.getKey(), entry.getValue());
            }
        }

        return filteredMap;

    }

    @Override
    public String createTicketUrl(String jiraTicketName) {
        // Until such a time as I can remove this method, it's delegated to Jira Config
        return JiraConfig.createTicketUrl(jiraTicketName);
    }

    @Override
    public IssueTransitionListResponse findAvailableTransitions(String jiraIssueKey) {
        String urlString = getBaseUrl() + "/issue/" + jiraIssueKey + "/transitions";

        WebResource webResource =
                getJerseyClient().resource(urlString).queryParam("expand", "transitions.fields");

        return get(webResource, new GenericType<IssueTransitionListResponse>() {
        });
    }

    @Override
    public Transition findAvailableTransitionByName(String jiraIssueKey, String transitionName) {
        IssueTransitionListResponse availableTransitions = findAvailableTransitions(jiraIssueKey);

        for (Transition transition : availableTransitions.getTransitions()) {
            if (transition.getName().equals(transitionName)) {
                return transition;
            }
        }

        return null;
    }

    @Override
    public void postNewTransition(String jiraIssueKey, Transition transition, String comment) throws IOException {
        postNewTransition(jiraIssueKey, transition, null, comment);
    }


    @Override
    public void postNewTransition(String jiraIssueKey, Transition transition, Collection<CustomField> customFields,
                                  @Nullable String comment) throws IOException {
        IssueTransitionRequest jiraIssueTransition = new IssueTransitionRequest(transition, customFields, comment);

        String urlString = getBaseUrl() + "/issue/" + jiraIssueKey + "/transitions";
        WebResource webResource = getJerseyClient().resource(urlString);
        post(webResource, jiraIssueTransition);
    }

    // Validate the current user by using the JIRA user/search API.  We have a match if we get a response back
    // whose name exactly matches the name we're searching for.  The API excludes inactive users by default.
    @Override
    public boolean isValidUser(String username) {
        String urlString = getBaseUrl() + "/user/search";

        String jsonResponse = getJerseyClient().resource(urlString).
                queryParam("username", username).
                queryParam("maxResults", "1000").get(String.class);
        try {
            @SuppressWarnings("unchecked")
            List<Map<String, String>> response = new ObjectMapper().readValue(jsonResponse, List.class);
            for (Map<String, String> properties : response) {
                String foundName = properties.get("name");
                // JIRA usernames are not case sensitive.
                if (foundName.equalsIgnoreCase(username)) {
                    return true;
                }
            }
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Look up a JIRA user by their email address.
     *
     * @param emailAddress the address to look up
     * @return the user name, or null if no user was found.
     */
    @Override
    public String getUserNameForEmail(String emailAddress) {
        String urlString = getBaseUrl() + "/user/search";

        String jsonResponse = getJerseyClient().resource(urlString).
                queryParam("username", emailAddress).
                queryParam("maxResults", "1").get(String.class);
        try {
            @SuppressWarnings("unchecked")
            List<Map<String, String>> response = new ObjectMapper().readValue(jsonResponse, List.class);
            for (Map<String, String> properties : response) {
                String foundName = properties.get("name");
                String foundEmail = properties.get("emailAddress");
                // JIRA usernames are not case sensitive.
                if (!StringUtils.isEmpty(foundName) && foundEmail.equalsIgnoreCase(emailAddress)) {
                    return foundName;
                }
            }
        } catch (IOException e) {
            // Fall through.
        }
        return null;
    }

    @Override
    public IssueFieldsResponse getIssueFields(
        String jiraIssueKey, Collection<CustomFieldDefinition> customFieldDefinitions) throws IOException {

        List<String> fieldIds = new ArrayList<>();

        for (CustomFieldDefinition customFieldDefinition : customFieldDefinitions) {
            fieldIds.add(customFieldDefinition.getJiraCustomFieldId());
        }

        String fieldArgs = StringUtils.join(fieldIds, ",");
        String url = getBaseUrl() + "/issue/" + jiraIssueKey + "?fields=" + fieldArgs;
        log.info(url);
        WebResource webResource =
                getJerseyClient().resource(getBaseUrl() + "/issue/" + jiraIssueKey).queryParam("fields", fieldArgs);

        return get(webResource, new GenericType<IssueFieldsResponse>() {
        });
    }

    @Override
    public String getResolution(String jiraIssueKey) throws IOException {
        String url = getBaseUrl() + "/issue/" + jiraIssueKey;
        log.info(url);
        WebResource webResource =
                getJerseyClient().resource(getBaseUrl() + "/issue/" + jiraIssueKey).queryParam("fields", "resolution");

        IssueResolutionResponse issueResolutionResponse = get(webResource, new GenericType<IssueResolutionResponse>() {
        });

        Map<String, IssueResolutionResponse.Resolution> fields = issueResolutionResponse.getFields();

        if (fields == null || fields.isEmpty()) {
            return null;
        }

        IssueResolutionResponse.Resolution value = fields.entrySet().iterator().next().getValue();

        if (value == null) {
            return null;
        }

        return value.getName();
    }
}
