package com.example.session.role;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class RoleTypeDeserializer extends JsonDeserializer<RoleType> {

    @Override
    public RoleType deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        String value = parser
                .getValueAsString()
                .trim()
                .replaceAll("\\s+", "_")
                .toUpperCase();

        try {
            return RoleType.valueOf("ROLE_" + value);
        } catch (IllegalArgumentException iae) {
            throw new IllegalArgumentException("Invalid role type: " + value);
        }
    }
}
