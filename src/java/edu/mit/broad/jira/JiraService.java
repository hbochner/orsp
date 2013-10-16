package edu.mit.broad.jira;

import edu.mit.broad.jira.customfields.CustomField;
import edu.mit.broad.jira.customfields.CustomFieldDefinition;
import edu.mit.broad.jira.issue.CreateFields;
import edu.mit.broad.jira.issue.IssueFieldsResponse;
import edu.mit.broad.jira.issue.JiraIssue;
import edu.mit.broad.jira.issue.Visibility;
import edu.mit.broad.jira.issue.link.AddIssueLinkRequest;
import edu.mit.broad.jira.issue.transition.IssueTransitionListResponse;
import edu.mit.broad.jira.issue.transition.Transition;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Map;

public interface JiraService extends Serializable {

    SimpleDateFormat JIRA_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Create an issue with a project prefix specified by projectPrefix; i.e. for this method projectPrefix would be 'TP' and not 'TP-5' for
     * the JIRA project TestProject having prefix TP.
     *
     * @param projectPrefix Jira prefix used for this project.
     * @param issueType     The type of issue within this project.
     * @param summary       The summary text.
     * @param description   The description text.
     * @param customFields  All custom fields.
     * @return The issue created.
     * @throws java.io.IOException Any errors.
     */
    JiraIssue createIssue(String projectPrefix, String reporter,
                          CreateFields.IssueType issueType, String summary,
                          String description, Collection<CustomField> customFields) throws IOException;

    /**
     * Updates an issue, modifying the custom fields supplied.
     *
     * @param key          the key of the JIRA issue to update
     * @param customFields the fields to modify
     */
    void updateIssue(String key, Collection<CustomField> customFields) throws IOException;

    /**
     * Get the JiraIssue object for a JIRA key.
     *
     * @param key the key
     * @return the issue object for the key
     */
    JiraIssue getIssue(String key) throws IOException;

    /**
     * Add a publicly visible comment to the specified issue.
     *
     * @param key  The comment's key.
     * @param body The body of the comment.
     */
    void addComment(String key, String body) throws IOException;

    /**
     * Add a comment to the specified issue whose visibility is restricted by the {@link Visibility} specifiers.
     *
     * @param key             The comment's key.
     * @param body            The body of the comment.
     * @param visibilityType  The type for the visibility.
     * @param visibilityValue The value for the visibility.
     * @throws java.io.IOException
     */
    void addComment(String key, String body, Visibility.Type visibilityType, Visibility.Value visibilityValue) throws
            IOException;

    /**
     * Add a comment to the specified issue whose visibility is restricted by the {@link Visibility} specifiers.
     *
     * @param key             The comment's key.
     * @param body            The body of the comment.
     * @param visibilityType  The type for the visibility.
     * @param visibilityValue The value for the visibility.
     * @throws java.io.IOException
     */
    void addAttachment(String key, File file) throws IOException;

    /**
     * Finds all the custom fields for the submission request of the given project and issue type
     *
     * @param project   The project.
     * @param issueType The issue type.
     * @return A {@link java.util.Map} of the custom fields found for the project/issuetype combination.  To make it easy to
     *         reference, the field map is indexed by the field name.
     */
    public Map<String, CustomFieldDefinition> getRequiredFields(@Nonnull CreateFields.Project project,
                                                                @Nonnull CreateFields.IssueType issueType) throws
            IOException;

    /**
     * createTicketUrl is a helper class that generates a clickable Url to allow a user to browse the Jira Ticket
     * in a  browser
     *
     * @param jiraTicketName A String that is a unique jira ticket Key.
     * @return The url for the jira ticket.
     */
    public String createTicketUrl(String jiraTicketName);

    /**
     * getCustomFields returns all possible custom fields in the system for a given JIRA project
     *
     * @param fieldNames A vararg list of the fields to return by name, this filters the full list of custom fields.
     *                   If no fieldNames are specified the full list is returned.
     * @return A {@link java.util.Map} of the custom fields found for the project/issuetype combination.  To make it easy to
     *         reference, the field map is indexed by the field name.
     * @throws java.io.IOException
     */
    public Map<String, CustomFieldDefinition> getCustomFields(String... fieldNames) throws IOException;


    /**
     * addLink provides a user with the ability to, tell the Jira system to create a new link to another jira ticket
     *
     * @param type          An instance of {@link org.broadinstitute.gpinformatics.infrastructure.jira.issue.link.AddIssueLinkRequest.LinkType}
     *                      that defines what time of Link this will be represented as
     * @param sourceIssueIn A {@link String} that represents the unique key of the Jira ticket that will be the Source
     *                      of the link
     * @param targetIssueIn A {@link String} that represents the unique key of the Jira ticket that will be the Target
     *                      of the link
     * @throws java.io.IOException
     */
    void addLink(AddIssueLinkRequest.LinkType type, String sourceIssueIn, String targetIssueIn) throws IOException;

    /**
     * addLink provides a user with the ability to, tell the Jira system to create a new link to another jira ticket
     *
     * @param type              An instance of {@link org.broadinstitute.gpinformatics.infrastructure.jira.issue.link.AddIssueLinkRequest.LinkType}
     *                          that defines what time of Link this will be represented as
     * @param sourceIssueIn     A {@link String} that represents the unique key of the Jira ticket that will be the Source
     *                          of the link
     * @param targetIssueIn     A {@link String} that represents the unique key of the Jira ticket that will be the Target
     *                          of the link
     * @param commentBody       A {@link String} that represents the body of a comment that will be associated with the new
     *                          link
     * @param availabilityType  An instance of {@link Visibility.Type} that represents the type visibility that the
     *                          link to be created should have
     * @param availabilityValue An instance of {@link Visibility.Value} that represents what users are eligible for
     *                          visibility to the link
     * @throws java.io.IOException
     */
    void addLink(AddIssueLinkRequest.LinkType type, String sourceIssueIn, String targetIssueIn, String commentBody,
                 Visibility.Type availabilityType, Visibility.Value availabilityValue) throws IOException;

    /**
     * addWatcher provides a user with the ability to set a user ID and set it as a watcher on a Jira Ticket
     *
     * @param key       A {@link String} that represents the unique key of the Jira ticket for which a user is attempting
     *                  to add a watcher to
     * @param watcherId A {@link String} that represents the Login ID of a user who will be added as a watcher
     * @throws java.io.IOException
     */
    void addWatcher(String key, String watcherId) throws IOException;

    IssueTransitionListResponse findAvailableTransitions(String jiraIssueKey);

    Transition findAvailableTransitionByName(String jiraIssueKey, String transitionName);

    void postNewTransition(String jiraIssueKey, Transition transition, String comment) throws IOException;


    /**
     * Modify fields in the JIRA issue using a transition post operation.  This is required to update some fields in
     * JIRA.
     *
     * @param jiraIssueKey the issue to update
     * @param transition   the transition state to use
     * @param customFields the fields to update
     * @param comment      a comment to add, or null if none
     * @throws java.io.IOException
     */
    void postNewTransition(String jiraIssueKey, Transition transition, Collection<CustomField> customFields,
                           @Nullable String comment) throws IOException;

    IssueFieldsResponse getIssueFields(String jiraIssueKey,
                                       Collection<CustomFieldDefinition> customFieldDefinitions) throws IOException;

    String getResolution(String jiraIssueKey) throws IOException;

    /**
     * Check and see if the user is an exact match for a JIRA user, and has an active account.
     *
     * @param username the username to look for
     * @return true if user is valid to use in JIRA API calls.
     */
    boolean isValidUser(String username);

    JiraIssue getIssueInfo(String key, String... fields) throws IOException;

    String getUserNameForEmail(String emailAddress);
}
