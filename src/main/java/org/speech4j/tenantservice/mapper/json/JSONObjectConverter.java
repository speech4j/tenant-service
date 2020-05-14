package org.speech4j.tenantservice.mapper.json;

import org.json.JSONObject;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class JSONObjectConverter implements AttributeConverter<JSONObject, String> {
    @Override
    public String convertToDatabaseColumn(JSONObject jsonData) {
        return jsonData.toString();
    }

    @Override
    public JSONObject convertToEntityAttribute(String jsonDataAsJson) {
        return new JSONObject(jsonDataAsJson);
    }
}
