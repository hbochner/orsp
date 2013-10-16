package edu.mit.broad.jira.issue;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

public class JsonLabopsJiraVisibilitySerializer extends JsonSerializer<Visibility> {

    @Override
    public void serialize(Visibility visibility, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();

        jsonGenerator.writeObjectField("value", visibility.getValue().getName());
        jsonGenerator.writeObjectField("type", visibility.getType());
        jsonGenerator.writeEndObject();
    }
}
