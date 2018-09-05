/*
 * @formatter:off
 * Copyright 2018 the original author or authors.
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
package xyz.devlxl.chaos.support.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;
import org.springframework.util.Assert;

/**
 * Convenience base class for aggregate roots that exposes a {@link #registerEvent(Object)} to capture domain events and
 * expose them via {@link #domainEvents())}. The implementation is using the general event publication mechanism implied
 * by {@link DomainEvents} and {@link AfterDomainEventPublication}.
 * <p>
 * Difference with {@link org.springframework.data.domain.AbstractAggregateRoot}:
 * <ul>
 * <li>Limit the event class to {@link DomainEvent}
 * <li>Add some methods to aid unit testing
 * </ul>
 * 
 * @author Liu Xiaolei
 * @see org.springframework.data.domain.AbstractAggregateRoot
 */
public abstract class AbstractAggregateRoot<A extends AbstractAggregateRoot<A>> {

    private transient final @Transient List<DomainEvent> domainEvents = new ArrayList<>();

    /**
     * Registers the given event object for publication on a call to a Spring Data repository's save methods.
     * 
     * @param event must not be {@literal null}.
     * @return the event that has been added.
     * @see #andEvent(DomainEvent)
     */
    protected <T extends DomainEvent> T registerEvent(T event) {

        Assert.notNull(event, "Domain event must not be null!");

        this.domainEvents.add(event);
        return event;
    }

    /**
     * Clears all domain events currently held. Usually invoked by the infrastructure in place in Spring Data
     * repositories.
     */
    @AfterDomainEventPublication
    protected void clearDomainEvents() {
        this.domainEvents.clear();
    }

    /**
     * All domain events currently captured by the aggregate.
     */
    @DomainEvents
    protected Collection<DomainEvent> domainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    /**
     * Adds all events contained in the given aggregate to the current one.
     * 
     * @param aggregate must not be {@literal null}.
     * @return the aggregate
     */
    @SuppressWarnings("unchecked")
    protected final A andEventsFrom(A aggregate) {

        Assert.notNull(aggregate, "Aggregate must not be null!");

        this.domainEvents.addAll(aggregate.domainEvents());

        return (A)this;
    }

    /**
     * Adds the given event to the aggregate for later publication when calling a Spring Data repository's save-method.
     * Does the same as {@link #registerEvent(DomainEvent)} but returns the aggregate instead of the event.
     * 
     * @param event must not be {@literal null}.
     * @return the aggregate
     * @see #registerEvent(DomainEvent)
     */
    @SuppressWarnings("unchecked")
    protected final A andEvent(DomainEvent event) {

        registerEvent(event);

        return (A)this;
    }

    /**
     * Just to aid unit testing. Confirm if the event has been captured.
     * 
     * @param event
     * @param occurredOnAdjustment The adjustment range of the event occurrence time. It's unit is millisecond, and
     *        using a value less than or equal to zero to indicate no adjustment. Set this parameter to reduce or ignore
     *        the interference of the event occurrence time when judging
     * @return whether the event has been captured
     */
    public boolean hasCapturedEvent(DomainEvent event, long occurredOnAdjustment) {
        if (occurredOnAdjustment <= 0) {
            return this.domainEvents.contains(event);
        } else {
            return this.domainEvents.stream().anyMatch(eventOnStream -> {
                return eventOnStream.equalsExcludedOccurTime(event)
                    && System.currentTimeMillis() - eventOnStream.occurredOn().getTime() <= occurredOnAdjustment;
            });
        }
    }
}
