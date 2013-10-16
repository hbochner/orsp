package edu.mit.broad.jira.issue.transition;

import java.util.Collection;

import javax.annotation.Nullable;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import edu.mit.broad.jira.customfields.CustomField;
import edu.mit.broad.jira.issue.UpdateFields;

@JsonSerialize(using = IssueTransitionSerializer.class)
public class IssueTransitionRequest {

    // assumes we only want to update custom fields, which for the current GPLIM-488 and GPLIM-371 use cases is true
    private final UpdateFields fields = new UpdateFields();

    private final Transition transition;

    @Nullable
    private final String comment;

    public IssueTransitionRequest(Transition transition, @Nullable String comment) {
        this.transition = transition;
        this.comment = comment;
    }


    public IssueTransitionRequest(Transition transition, Collection<CustomField> customFields,
                                  @Nullable String comment) {
        this.transition = transition;
        if (customFields != null) {
            fields.getCustomFields().addAll(customFields);
        }
        this.comment = comment;
    }

    public UpdateFields getFields() {
        return fields;
    }

    public Transition getTransition() {
        return transition;
    }

    @Nullable
    public String getComment() {
        return comment;
    }
}
