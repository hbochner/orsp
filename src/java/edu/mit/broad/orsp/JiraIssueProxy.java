package edu.mit.broad.orsp;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: loaner
 * Date: 10/16/13
 * Time: 4:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class JiraIssueProxy {
    JiraRestService jira;

    public JiraIssueProxy(JiraRestService service) {
        jira = service;
    }

    public JiraIssueProxy()
        throws IOException
    {
        this(new JiraRestService());
    }


}
