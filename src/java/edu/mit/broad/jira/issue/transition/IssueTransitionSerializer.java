package edu.mit.broad.jira.issue.transition;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import edu.mit.broad.jira.customfields.UpdateJiraIssueUpdateSerializer;


public class IssueTransitionSerializer extends JsonSerializer<IssueTransitionRequest> {

    @Override
    public void serialize(IssueTransitionRequest value, JsonGenerator jsonGenerator, SerializerProvider provider) throws
            IOException {

        jsonGenerator.writeStartObject();

        if (!value.getFields().getCustomFields().isEmpty()) {
            jsonGenerator.writeFieldName("fields");
            jsonGenerator.writeStartObject();
            UpdateJiraIssueUpdateSerializer.writeCustomFields(value.getFields().getCustomFields(), jsonGenerator);
            jsonGenerator.writeEndObject();
        }

        jsonGenerator.writeFieldName("transition");
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("id", value.getTransition().getId());
        jsonGenerator.writeEndObject();

        if (value.getComment() != null) {
            writeComment(jsonGenerator, value.getComment());
        }

        jsonGenerator.writeEndObject();

    }

    private void writeComment(JsonGenerator jsonGenerator, String comment) throws IOException {
        if (comment != null) {
            jsonGenerator.writeFieldName("update");

            jsonGenerator.writeStartObject();
            jsonGenerator.writeArrayFieldStart("comment");

            jsonGenerator.writeStartObject();
            jsonGenerator.writeFieldName("add");

            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("body", comment);

            jsonGenerator.writeEndObject();
            jsonGenerator.writeEndObject();
            jsonGenerator.writeEndArray();
            jsonGenerator.writeEndObject();
        }
    }
}
