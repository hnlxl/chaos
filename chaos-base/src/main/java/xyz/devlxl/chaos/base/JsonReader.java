/*
 * @formatter:off
 * Copyright 2012,2013 Vaughn Vernon
 * Copyright 2018 teclxl
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * @formatter:on
 */
package xyz.devlxl.chaos.base;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * A JSON's reader. TODO not test
 * <p>
 * Base on <code>com.saasovation.common.media.AbstractJSONMediaReader<code> in the project
 * <a href="https://github.com/VaughnVernon/IDDD_Samples">https://github.com/VaughnVernon/IDDD_Samples</a>. Retain most
 * of its design ideas, but use Jackson instead of Gson, increase tool methods for subclasses, temporarily cancel the
 * "@key" mode, use Optional instead of null, and strictly control the path and the value's type. In addition, sort out
 * the order of methods.
 * <p>
 * Note: An instance of this class is a wrapper of a specific JSON, not a component that can be injected. But when
 * constructing it, you can inject other components into it.
 * 
 * @author Liu Xiaolei
 * @date 2018/09/19
 */
@Getter(AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
@Accessors(fluent = true)
public class JsonReader {
    private JsonNode jsonNode;
    private ObjectMapper objectMapper;

    public JsonReader(ObjectMapper aObjectMapper, JsonNode aJsonNode) {
        super();
        this.objectMapper(aObjectMapper);
        this.jsonNode(aJsonNode);
    }

    public JsonReader(ObjectMapper aObjectMapper, String aJsonRepresentation) {
        super();
        this.objectMapper(aObjectMapper);
        try {
            this.jsonNode(aObjectMapper.readTree(aJsonRepresentation));
        } catch (IOException e) {
            throw new JsonSerializationException(e);
        }
    }

    public Optional<String> stringValue(String... aKeys) throws InvalidPathException {
        return this.stringValue(this.jsonNode(), aKeys);
    }

    protected Optional<String> stringValue(JsonNode aStartingJsonNode, String... aKeys) throws InvalidPathException {
        JsonNode node = this.navigateTo(aStartingJsonNode, aKeys);
        checkValueNode(node);
        return !node.isNull() ? Optional.of(node.asText()) : Optional.empty();
    }

    public String textValue(String... aKeys) {
        return this.textValue(this.jsonNode(), aKeys);
    }

    /**
     * Method that will return a string representation of the value at the specified path point.
     * <p>
     * It always returns a value. Returns an empty string if the path point is not a value point, or a "null" string if
     * the value is null, otherwise returns the valid string representation of the value
     * 
     * @param aStartingJsonNode
     * @param aKeys
     * @return
     */
    protected String textValue(JsonNode aStartingJsonNode, String... aKeys) {
        JsonNode node = this.navigateTo(aStartingJsonNode, aKeys);
        return node.asText();
    }

    public Optional<BigDecimal> bigDecimalValue(String... aKeys) {
        return this.bigDecimalValue(this.jsonNode(), aKeys);
    }

    protected Optional<BigDecimal> bigDecimalValue(JsonNode aStartingJsonNode, String... aKeys) {
        try {
            return this.stringValue(aStartingJsonNode, aKeys).map(BigDecimal::new);
        } catch (NumberFormatException e) {
            throw new InvalidValueException();
        }
    }

    public Optional<Double> doubleValue(String... aKeys) throws InvalidPathException, InvalidValueException {
        return this.doubleValue(this.jsonNode(), aKeys);
    }

    protected Optional<Double> doubleValue(JsonNode aStartingJsonNode, String... aKeys)
        throws InvalidPathException, InvalidValueException {
        try {
            return this.stringValue(aStartingJsonNode, aKeys).map(Double::parseDouble);
        } catch (NumberFormatException e) {
            throw new InvalidValueException();
        }
    }

    public Optional<Float> floatValue(String... aKeys) throws InvalidPathException, InvalidValueException {
        return this.floatValue(this.jsonNode(), aKeys);
    }

    protected Optional<Float> floatValue(JsonNode aStartingJsonNode, String... aKeys)
        throws InvalidPathException, InvalidValueException {
        try {
            return this.stringValue(aStartingJsonNode, aKeys).map(Float::parseFloat);
        } catch (NumberFormatException e) {
            throw new InvalidValueException();
        }
    }

    public Optional<Integer> integerValue(String... aKeys) throws InvalidPathException, InvalidValueException {
        return this.integerValue(this.jsonNode(), aKeys);
    }

    protected Optional<Integer> integerValue(JsonNode aStartingJsonNode, String... aKeys)
        throws InvalidPathException, InvalidValueException {
        try {
            return this.stringValue(aStartingJsonNode, aKeys).map(Integer::parseInt);
        } catch (NumberFormatException e) {
            throw new InvalidValueException();
        }
    }

    public Optional<Long> longValue(String... aKeys) throws InvalidPathException, InvalidValueException {
        return this.longValue(this.jsonNode(), aKeys);
    }

    protected Optional<Long> longValue(JsonNode aStartingJsonNode, String... aKeys)
        throws InvalidPathException, InvalidValueException {
        try {
            return this.stringValue(aStartingJsonNode, aKeys).map(Long::parseLong);
        } catch (NumberFormatException e) {
            throw new InvalidValueException();
        }
    }

    public Optional<Boolean> booleanValue(String... aKeys) throws InvalidPathException, InvalidValueException {
        return this.booleanValue(this.jsonNode(), aKeys);
    }

    protected Optional<Boolean> booleanValue(JsonNode aStartingJsonNode, String... aKeys)
        throws InvalidPathException, InvalidValueException {
        JsonNode node = this.navigateTo(aStartingJsonNode, aKeys);
        checkValueNode(node);
        if (node.isBoolean()) {
            return Optional.of(node.asBoolean());
        } else if (node.isNull()) {
            return Optional.empty();
        } else {
            throw new InvalidValueException();
        }
    }

    public Boolean booleanValueNotStrict(String... aKeys) throws InvalidPathException {
        return this.booleanValueNotStrict(this.jsonNode(), false, aKeys);
    }

    public Boolean booleanValueNotStrict(Boolean defaultValue, String... aKeys) throws InvalidPathException {
        return this.booleanValueNotStrict(this.jsonNode(), defaultValue, aKeys);
    }

    protected Boolean booleanValueNotStrict(JsonNode aStartingJsonNode, Boolean defaultValue, String... aKeys)
        throws InvalidPathException {
        JsonNode node = this.navigateTo(aStartingJsonNode, aKeys);
        checkValueNode(node);
        return node.asBoolean(defaultValue);
    }

    public Optional<Date> dateValue(String... aKeys) throws InvalidPathException, InvalidValueException {
        return this.dateValue(this.jsonNode(), aKeys);
    }

    protected Optional<Date> dateValue(JsonNode aStartingJsonNode, String... aKeys)
        throws InvalidPathException, InvalidValueException {
        if (objectMapper.getDateFormat() != null) {
            return this.stringValue(aStartingJsonNode, aKeys).map(strValue -> {
                try {
                    return objectMapper.getDateFormat().parse(strValue);
                } catch (ParseException e) {
                    throw new InvalidValueException();
                }
            });
        } else {
            return this.longValue(aStartingJsonNode, aKeys).map(Date::new);
        }
    }

    protected JsonNode navigateTo(JsonNode aStartingJsonNode, String... aKeys) {
        if (aKeys.length == 0) {
            throw new IllegalArgumentException("Must specify one or more keys.");
        } else if (aKeys.length == 1 && (aKeys[0].startsWith("/") || aKeys[0].contains("."))) {
            aKeys = this.parsePath(aKeys[0]);
        }

        JsonNode node = aStartingJsonNode;
        for (String aKey : aKeys) {
            node = node.path(aKey);
        }

        return node;
    }

    private String[] parsePath(String aPropertiesPath) {
        boolean startsWithSlash = aPropertiesPath.startsWith("/");

        String[] propertyNames = null;

        if (startsWithSlash) {
            propertyNames = aPropertiesPath.substring(1).split("/");
        } else {
            propertyNames = aPropertiesPath.split("\\.");
        }

        return propertyNames;
    }

    protected void checkValueNode(JsonNode aJsonNode) throws InvalidPathException {
        if (!aJsonNode.isValueNode()) {
            throw new InvalidPathException();
        }
    }

    public static class InvalidPathException extends RuntimeException {
        private static final long serialVersionUID = 1L;
    }

    public static class InvalidValueException extends RuntimeException {
        private static final long serialVersionUID = 1L;
    }
}
