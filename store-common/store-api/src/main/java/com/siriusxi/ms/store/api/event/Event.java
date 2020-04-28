package com.siriusxi.ms.store.api.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;
import static lombok.AccessLevel.NONE;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter(NONE)
public class Event<K, T> ***REMOVED***

  private Event.Type eventType;
  private K key;
  private T data;
  private LocalDateTime eventCreatedAt;

  public Event(Type eventType, K key, T data) ***REMOVED***
    this.eventType = eventType;
    this.key = key;
    this.data = data;
    this.eventCreatedAt = now();
***REMOVED***

  public enum Type ***REMOVED***
    CREATE,
    DELETE
***REMOVED***
***REMOVED***
