package edu.mit.broad.orsp;

import edu.mit.broad.jira.JiraConfig;
import edu.mit.broad.jira.JiraService;
import edu.mit.broad.jira.JiraServiceImpl;

/**
 * Created with IntelliJ IDEA.
 * User: hbochner
 * Date: 10/2/13
 * Time: 3:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class OrspJiraService
       extends JiraServiceImpl
{
    private static JiraServiceImpl impl = null;

    /* Ultimately this should be ThreadLocal, and get name and password from a properties file.
       Or use Dependency Injection.
     */
    public static JiraService getInstance() {
        if (impl == null) {
            impl = new JiraServiceImpl(JiraConfig.ORSP_DEV);
        }

        return impl;
    }
}
