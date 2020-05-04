package com.siriusxi.ms.store.pcs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.siriusxi.ms.store.api.event.Event;
import lombok.extern.log4j.Log4j2;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Log4j2
class IsSameEvent extends TypeSafeMatcher<String> ***REMOVED***

  private final ObjectMapper mapper = new ObjectMapper();

  private final Event expectedEvent;

  private IsSameEvent(Event expectedEvent) ***REMOVED***
    this.expectedEvent = expectedEvent;
***REMOVED***

  public static Matcher<String> sameEventExceptCreatedAt(Event expectedEvent) ***REMOVED***
    return new IsSameEvent(expectedEvent);
***REMOVED***

  @Override
  protected boolean matchesSafely(String eventAsJson) ***REMOVED***

    if (expectedEvent == null) return false;

    log.trace("Convert the following json string to a map: ***REMOVED******REMOVED***", eventAsJson);
    Map mapEvent = convertJsonStringToMap(eventAsJson);
    mapEvent.remove("eventCreatedAt");

    Map mapExpectedEvent = getMapWithoutCreatedAt(expectedEvent);

    log.trace("Got the map: ***REMOVED******REMOVED***", mapEvent);
    log.trace("Compare to the expected map: ***REMOVED******REMOVED***", mapExpectedEvent);
    return mapEvent.equals(mapExpectedEvent);
***REMOVED***

  @Override
  public void describeTo(Description description) ***REMOVED***
    String expectedJson = convertObjectToJsonString(expectedEvent);
    description.appendText("expected to look like " + expectedJson);
***REMOVED***

  private Map getMapWithoutCreatedAt(Event event) ***REMOVED***
    Map mapEvent = convertObjectToMap(event);
    mapEvent.remove("eventCreatedAt");
    return mapEvent;
***REMOVED***

  private Map convertObjectToMap(Object object) ***REMOVED***
    JsonNode node = mapper.convertValue(object, JsonNode.class);
    return mapper.convertValue(node, Map.class);
***REMOVED***

  private String convertObjectToJsonString(Object object) ***REMOVED***
    try ***REMOVED***
      return mapper.writeValueAsString(object);
***REMOVED*** catch (JsonProcessingException e) ***REMOVED***
      throw new RuntimeException(e);
***REMOVED***
***REMOVED***

  private Map convertJsonStringToMap(String eventAsJson) ***REMOVED***
    try ***REMOVED***
      return mapper.readValue(eventAsJson, new TypeReference<HashMap>() ***REMOVED******REMOVED***);
***REMOVED*** catch (IOException e) ***REMOVED***
      throw new RuntimeException(e);
***REMOVED***
***REMOVED***
***REMOVED***
