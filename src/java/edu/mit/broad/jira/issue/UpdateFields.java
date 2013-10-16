package edu.mit.broad.jira.issue;

import edu.mit.broad.jira.customfields.CustomField;
import edu.mit.broad.jira.customfields.UpdateJiraIssueUpdateSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Collection;
import java.util.HashSet;

/**
 * Fields that can be sent to JIRA in an update request.
 *
 */
@JsonSerialize(using = UpdateJiraIssueUpdateSerializer.class)
public class UpdateFields {

    private final Collection<CustomField> customFields = new HashSet<>();

    public Collection<CustomField> getCustomFields() {
        return customFields;
    }
}
