package com.whollyframework.util.json;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

public class CustomNullKeySerializer extends JsonSerializer<Object> {

	public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
			JsonProcessingException {
		jgen.writeFieldName("");
	}

}
