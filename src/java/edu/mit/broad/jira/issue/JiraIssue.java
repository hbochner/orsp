package edu.mit.broad.jira.issue;

import edu.mit.broad.jira.JiraService;
import edu.mit.broad.jira.customfields.CustomField;
import edu.mit.broad.jira.customfields.CustomFieldDefinition;
import edu.mit.broad.jira.issue.link.AddIssueLinkRequest;
import edu.mit.broad.jira.issue.transition.IssueTransitionListResponse;
import edu.mit.broad.jira.issue.transition.Transition;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("UnusedDeclaration")
public class JiraIssue implements Serializable {

    private static final long serialVersionUID = -1807653684838317883L;
    private final JiraService jiraService;

    private final String key;

    private String summary;
    private String description;
    private Date dueDate;

    private final Map<String, Object> extraFields = new HashMap<>();

    public JiraIssue(String key, JiraService jiraService, String summary, String description, Date dueDate) {
        this(key, jiraService);

        this.summary = summary;
        this.description = description;
        this.dueDate = dueDate;
    }

    public JiraIssue(String key, JiraService jiraService) {
        this.key = key;
        this.jiraService = jiraService;
    }

    public String getKey() {
        return key;
    }

    public String getSummary() throws IOException {
        populateDateIfNeeded();
        return summary;
    }

    private void populateDateIfNeeded() throws IOException {
        if (summary == null) {
            JiraIssue tempIssue = jiraService.getIssueInfo(key, (String[]) null);
            summary = tempIssue.getSummary();
            description = tempIssue.getDescription();
            dueDate = tempIssue.getDueDate();
        }
    }

    public void setSummary(@Nonnull String summary) {
        this.summary = summary;
    }

    public String getDescription() throws IOException {
        populateDateIfNeeded();
        return description;
    }

    public void setDescription(@Nonnull String description) {
        this.description = description;
    }

    public Date getDueDate() throws IOException {
        populateDateIfNeeded();
        return dueDate;
    }

    public void setDueDate(@Nonnull Date dueDate) {
        this.dueDate = dueDate;
    }

    public Object getFieldValue(@Nonnull String fieldName) throws IOException {

        populateDateIfNeeded();
        if (!extraFields.containsKey(fieldName)) {
            JiraIssue tempIssue = jiraService.getIssueInfo(key, fieldName);
            extraFields.put(fieldName, tempIssue.getFieldValue(fieldName));
        }

        return extraFields.get(fieldName);
    }

    public Map<String, Object> getFieldValues(@Nonnull String... fieldNames) throws IOException {

        populateDateIfNeeded();
        if (!extraFields.keySet().containsAll(Arrays.asList(fieldNames))) {
            JiraIssue tempIssue = jiraService.getIssueInfo(key, fieldNames);
            extraFields.putAll(tempIssue.getFieldValues(fieldNames));
        }

        return extraFields;
    }

    public <TV> void addFieldValue(String filedName, TV value) {

        extraFields.put(filedName, value);
    }

    /**
     * Updates an issue, modifying the custom fields supplied.
     *
     * @param customFields the fields to modify
     */
    public void updateIssue(Collection<CustomField> customFields) throws IOException {
        jiraService.updateIssue(key, customFields);
    }

    /**
     * Add a publicly visible comment to this issue.
     *
     * @param body the comment text. If empty, no change will occur.
     */
    public void addComment(String body) throws IOException {
        if (!StringUtils.isBlank(body)) {
            jiraService.addComment(key, body);
        }
    }

    /**
     * Add a link between this and another issue.
     *
     * @param type          the type of link to create
     * @param targetIssueIn the issue to link this one to
     * @throws java.io.IOException
     */
    public void addLink(AddIssueLinkRequest.LinkType type, String targetIssueIn) throws IOException {
        jiraService.addLink(type, key, targetIssueIn);
    }

    /**
     * Add a link between this and another issue, using the Related link type.
     *
     * @param targetIssueIn the issue to link this one to
     * @throws java.io.IOException
     */
    public void addLink(String targetIssueIn) throws IOException {
        addLink(AddIssueLinkRequest.LinkType.Related, targetIssueIn);
    }

    // Workaround for BSP users whose username includes their email address. This is an interim fix until
    // we can fixup existing usernames.
    private static final String BROADINSTITUTE_ORG = "@broadinstitute.org";

    /**
     * Add a watcher to this issue.
     *
     * @param watcherId the username of the watcher; must be a valida JIRA user
     * @throws java.io.IOException
     */
    public void addWatcher(String watcherId) throws IOException {
        try {
            jiraService.addWatcher(key, watcherId);
        } catch (Exception e) {
            if (watcherId.endsWith(BROADINSTITUTE_ORG)) {
                // Retry after stripping off the email suffix.
                jiraService.addWatcher(key, watcherId.replace(BROADINSTITUTE_ORG, ""));
            }
        }
    }

    /**
     * @return a list of all available workflow transitions for this ticket in its current state
     */
    public IssueTransitionListResponse findAvailableTransitions() {
        return jiraService.findAvailableTransitions(key);
    }

    /**
     * Transition a given Jira Ticket
     *
     * @param transition the target transition state
     */
    public void postNewTransition(Transition transition) throws IOException {
        jiraService.postNewTransition(key, transition, null);
    }

    public Map<String, CustomFieldDefinition> getCustomFields(String... fieldNames) throws IOException {
        return jiraService.getCustomFields(fieldNames);
    }

    public void setCustomFieldUsingTransition(CustomField.SubmissionField field,
                                              Object value, String transitionName) throws IOException {
        Transition transition = jiraService.findAvailableTransitionByName(key, transitionName);
        Map<String, CustomFieldDefinition> definitionMap = getCustomFields(field.getFieldName());
        List<CustomField> customFields = Collections.singletonList(new CustomField(definitionMap, field, value));
        jiraService.postNewTransition(key, transition, customFields, null);
    }

    @Override
    public String toString() {
        return "JiraIssue{" +
               "key='" + key + '\'' +
               '}';
    }
}
