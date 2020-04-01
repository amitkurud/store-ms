package com.siriusxi.ms.store.util.http;

import com.siriusxi.ms.store.util.exceptions.InvalidInputException;
import com.siriusxi.ms.store.util.exceptions.NotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@RestControllerAdvice
@Log4j2
class GlobalControllerExceptionHandler ***REMOVED***

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public @ResponseBody
    HttpErrorInfo handleNotFoundExceptions(ServerHttpRequest request, Exception ex) ***REMOVED***

        return createHttpErrorInfo(NOT_FOUND, request, ex);
***REMOVED***

    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler(InvalidInputException.class)
    public @ResponseBody
    HttpErrorInfo handleInvalidInputException(ServerHttpRequest request, Exception ex) ***REMOVED***

        return createHttpErrorInfo(UNPROCESSABLE_ENTITY, request, ex);
***REMOVED***

    private HttpErrorInfo createHttpErrorInfo(HttpStatus httpStatus, ServerHttpRequest request, Exception ex) ***REMOVED***
        final String path = request.getPath().pathWithinApplication().value();
        final String message = ex.getMessage();

        log.debug("Returning HTTP status: ***REMOVED******REMOVED*** for path: ***REMOVED******REMOVED***, message: ***REMOVED******REMOVED***", httpStatus, path, message);
        return new HttpErrorInfo(httpStatus, path, message);
***REMOVED***
***REMOVED***
