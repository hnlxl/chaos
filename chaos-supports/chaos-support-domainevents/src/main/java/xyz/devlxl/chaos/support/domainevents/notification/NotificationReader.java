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
package xyz.devlxl.chaos.support.domainevents.notification;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import xyz.devlxl.chaos.base.JsonReader;

/**
 * * A notification's JSON representation's reader. TODO not test
 * <p>
 * Base on <code>com.saasovation.common.notification.NotificationReader<code> in the project
 * <a href="https://github.com/VaughnVernon/IDDD_Samples">https://github.com/VaughnVernon/IDDD_Samples</a>. Retain most
 * of its design ideas, but use Jackson instead of Gson, use Optional instead of null, and strictly control the path and
 * the value's type. In addition, sort out the order of methods.
 * <p>
 * Note: An instance of this class is a wrapper of a specific notification, not a component that can be injected. But
 * when constructing it, you can inject other components into it.
 * 
 * @author Liu Xiaolei
 * @date 2018/09/19
 * @see JsonReader
 */
@Getter(AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
@Accessors(fluent = true)
public class NotificationReader extends JsonReader {
    /** field name of event body */
    private final static String F_N_EVENT = "event";
    /** field name of ID of both event and notification */
    private final static String F_N_ID = "notificationId";
    /** field name of occurredOn */
    private final static String F_N_OCCURREDON = "occurredOn";
    /** field name of event type */
    private final static String F_N_EVENT_TYPE = "typeName";
    /** field name of version */
    private final static String F_N_VERSION = "version";

    private JsonNode eventNode;

    public NotificationReader(ObjectMapper aObjectMapper, String aNotificationJsonRepresentation) {
        super(aObjectMapper, aNotificationJsonRepresentation);
        this.eventNode(this.jsonNode().path(F_N_EVENT));
    }

    public NotificationReader(ObjectMapper aObjectMapper, JsonNode aNotificationJsonNode) {
        super(aObjectMapper, aNotificationJsonNode);
        this.eventNode(this.jsonNode().path(F_N_EVENT));
    }

    public Optional<String> eventStringValue(String... aKeys) {
        return this.stringValue(this.eventNode(), aKeys);
    }

    public String eventTextValue(String... aKeys) {
        return this.textValue(this.eventNode(), aKeys);
    }

    public Optional<BigDecimal> eventBigDecimalValue(String... aKeys) {
        return this.bigDecimalValue(this.eventNode(), aKeys);
    }

    public Optional<Double> eventDoubleValue(String... aKeys) {
        return this.doubleValue(this.eventNode(), aKeys);
    }

    public Optional<Float> eventFloatValue(String... aKeys) {
        return this.floatValue(this.eventNode(), aKeys);
    }

    public Optional<Integer> eventIntegerValue(String... aKeys) {
        return this.integerValue(this.eventNode(), aKeys);
    }

    public Optional<Long> eventLongValue(String... aKeys) {
        return this.longValue(this.eventNode(), aKeys);
    }

    public Optional<Boolean> eventBooleanValue(String... aKeys) {
        return this.booleanValue(this.eventNode(), aKeys);
    }

    public Boolean booleanValueNotStrict(String... aKeys) throws InvalidPathException {
        return this.booleanValueNotStrict(this.eventNode(), false, aKeys);
    }

    public Boolean booleanValueNotStrict(Boolean defaultValue, String... aKeys) throws InvalidPathException {
        return this.booleanValueNotStrict(this.eventNode(), defaultValue, aKeys);
    }

    public Optional<Date> eventDateValue(String... aKeys) {
        return this.dateValue(this.eventNode(), aKeys);
    }

    public long notificationId() {
        long notificationId = this.longValue(F_N_ID).get();

        return notificationId;
    }

    public String notificationIdAsString() {
        String notificationId = this.stringValue(F_N_ID).get();

        return notificationId;
    }

    public Date occurredOn() {
        Date date = this.dateValue(F_N_OCCURREDON).get();

        return date;
    }

    public String typeName() {
        String typeName = this.stringValue(F_N_EVENT_TYPE).get();

        return typeName;
    }

    @Deprecated
    public int version() {
        // TODO Not yet designed the version of notification
        int version = this.integerValue(F_N_VERSION).get();

        return version;
    }
}