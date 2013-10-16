package edu.mit.broad.jira.issue;

import edu.mit.broad.jira.customfields.CustomField;

import java.util.Collection;


@SuppressWarnings("UnusedDeclaration")
public class UpdateIssueRequest {

    private final String key;

    private UpdateFields fields = new UpdateFields();

    public UpdateIssueRequest(String key, Collection<CustomField> customFields) {
        this.key = key;
        if (customFields != null) {
            fields.getCustomFields().addAll(customFields);
        }
    }

    public String getUrl(String baseUrl) {
        return baseUrl + "/issue/" + key;
    }

    public UpdateFields getFields() {
        return fields;
    }

    public void setFields(UpdateFields fields) {
        this.fields = fields;
    }
}
