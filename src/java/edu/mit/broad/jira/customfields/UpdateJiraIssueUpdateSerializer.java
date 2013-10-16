package edu.mit.broad.jira.customfields;

import java.io.IOException;
import java.util.Collection;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import edu.mit.broad.jira.issue.UpdateFields;

/**
 * This copy from Mercury takes the fields jor jira fields and turns them into a JSON object using the json generator.
 */
public class UpdateJiraIssueUpdateSerializer extends JsonSerializer<UpdateFields> {

    @Override
    public void serialize(UpdateFields fields, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        writeCustomFields(fields.getCustomFields(), jsonGenerator);
        jsonGenerator.writeEndObject();
    }

    public static void writeCustomFields(Collection<CustomField> customFields, JsonGenerator jsonGenerator) throws
            IOException {
        for (CustomField customField : customFields) {
            String fieldId = customField.getFieldDefinition().getJiraCustomFieldId();
            jsonGenerator.writeObjectField(fieldId, customField.getValue());
        }
    }
}
