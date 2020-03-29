package com.siriusxi.ms.store.util.http;

import org.springframework.http.HttpStatus;
import java.time.ZonedDateTime;

public record HttpErrorInfo(
        ZonedDateTime timestamp,
        String path,
        HttpStatus httpStatus,
        String message) ***REMOVED***

    public HttpErrorInfo(HttpStatus httpStatus, String path, String message) ***REMOVED***
        this(ZonedDateTime.now(), path, httpStatus, message);
***REMOVED***
***REMOVED***
