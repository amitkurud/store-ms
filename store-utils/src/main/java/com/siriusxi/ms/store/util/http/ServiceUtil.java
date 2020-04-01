package com.siriusxi.ms.store.util.http;

import lombok.extern.log4j.Log4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
@Log4j
public class ServiceUtil ***REMOVED***
    private final String port;

    private String serviceAddress = null;

    @Autowired
    public ServiceUtil(
            @Value("$***REMOVED***server.port***REMOVED***") String port) ***REMOVED***
        this.port = port;
***REMOVED***

    public String getServiceAddress() ***REMOVED***
        if (serviceAddress == null) ***REMOVED***
            serviceAddress = findMyHostname() + "/" + findMyIpAddress() + ":" + port;
    ***REMOVED***
        return serviceAddress;
***REMOVED***

    private String findMyHostname() ***REMOVED***
        try ***REMOVED***
            return InetAddress.getLocalHost().getHostName();
    ***REMOVED*** catch (UnknownHostException e) ***REMOVED***
            return "unknown host name";
    ***REMOVED***
***REMOVED***

    private String findMyIpAddress() ***REMOVED***
        try ***REMOVED***
            return InetAddress.getLocalHost().getHostAddress();
    ***REMOVED*** catch (UnknownHostException e) ***REMOVED***
            return "unknown IP address";
    ***REMOVED***
***REMOVED***

***REMOVED***
