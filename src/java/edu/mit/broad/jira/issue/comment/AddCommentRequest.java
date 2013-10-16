package edu.mit.broad.jira.issue.comment;


import java.io.Serializable;

import edu.mit.broad.jira.issue.Visibility;

public class AddCommentRequest implements Serializable {

    private String body;

    private Visibility visibility;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }


    public static AddCommentRequest create(String body, Visibility.Type type, Visibility.Value value) {

        AddCommentRequest ret = new AddCommentRequest();

        ret.setBody(body);

        Visibility visibility = Visibility.create(type, value);

        ret.setVisibility(visibility);

        return ret;
    }


    public static AddCommentRequest create(String body) {

        AddCommentRequest ret = new AddCommentRequest();

        ret.setBody(body);
        return ret;
    }
}
