package edu.mit.broad.jira;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import edu.mit.broad.jira.issue.CreateFields;


/**
 * When applied to an {@link Enum} with
 * {@link org.codehaus.jackson.map.annotate.JsonSerialize}, will convert any underscores
 * in the {@link Enum} instance name to a blank character during JSON serialization.
 * <p/>
 * Also, because labopsjira tickets seem to use a lot of parentheses, we have
 * to deal with this.  We do so by translating "OPENPAREN" to mean "(" and
 * "CLOSEPAREN" to mean ")" in the enum.
 * <p/>
 * Note this class does not perform deserialization, nor is there currently a need for that.
 */
public class JsonLabopsJiraIssueTypeSerializer extends JsonSerializer<CreateFields.IssueType> {

    @Override
    /**
     * Replace all underscores in the value with a blank
     */
    public void serialize(CreateFields.IssueType issueType, JsonGenerator jgen, SerializerProvider provider) throws
            IOException {
        jgen.writeStartObject();
        jgen.writeFieldName("name");
        jgen.writeString(issueType.getJiraName());
        jgen.writeEndObject();
    }
}
