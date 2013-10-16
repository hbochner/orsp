package edu.mit.broad.jira.issue;


import edu.mit.broad.jira.customfields.CustomField;

import java.util.Collection;


@SuppressWarnings("UnusedDeclaration")
public class CreateIssueRequest {

    private CreateFields fields;

    public CreateFields getFields() {
        return fields;
    }

    public void setFields(CreateFields fields) {
        this.fields = fields;
    }


    public CreateIssueRequest() {
        this.fields = new CreateFields();
    }

    public CreateIssueRequest(Collection<CustomField> customFields) {
        this();
        if (customFields != null) {
            fields.getCustomFields().addAll(customFields);
        }
    }

    public static CreateIssueRequest create(
            String key, String reporter, CreateFields.IssueType issueType, String summary, String description,
            Collection<CustomField> customFields) {

        CreateIssueRequest ret = new CreateIssueRequest(customFields);

        CreateFields fields = ret.getFields();

        fields.getProject().setKey(key);

        if (reporter != null) {
            fields.getReporter().setName(reporter);
        } else {
            fields.setReporter(null);
        }

        fields.setIssueType(issueType);
        fields.setSummary(summary);
        fields.setDescription(description);

        return ret;
    }
}
