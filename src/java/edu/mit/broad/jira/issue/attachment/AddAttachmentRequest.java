/*
 * The Broad Institute
 * SOFTWARE COPYRIGHT NOTICE AGREEMENT
 * This software and its documentation are copyright (2013) by the
 * Broad Institute/Massachusetts Institute of Technology. All rights are
 * reserved.
 *
 * This software is supplied without any warranty or guaranteed support
 * whatsoever. Neither the Broad Institute nor MIT can be responsible for its
 * use, misuse, or functionality.
 */

package edu.mit.broad.jira.issue.attachment;

import java.io.File;
import java.io.Serializable;

public class AddAttachmentRequest implements Serializable {

    private File attachment;

    public File getAttachment() {
        return attachment;
    }

    public void setAttachment(File attachment) {
        this.attachment = attachment;
    }

    public static AddAttachmentRequest create(File attachment) {
        AddAttachmentRequest ret = new AddAttachmentRequest();
        ret.setAttachment(attachment);
        return ret;
    }
}
