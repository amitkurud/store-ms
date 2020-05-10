package com.siriusxi.cloud.infra.auth.server.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Legacy Authorization Server does not support a custom name for the user parameter, so we'll need
 * to extend the default. By default, it uses the attribute ***REMOVED***@code user_name***REMOVED***, though it would be
 * better to adhere to the ***REMOVED***@code sub***REMOVED*** property defined in the <a target="_blank"
 * href="https://tools.ietf.org/html/rfc7519">JWT Specification</a>.
 */
class SubjectAttributeUserTokenConverter extends DefaultUserAuthenticationConverter ***REMOVED***
    @Override
    public Map<String, ?> convertUserAuthentication(Authentication authentication) ***REMOVED***
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("sub", authentication.getName());
        if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) ***REMOVED***
            response.put(AUTHORITIES, AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
    ***REMOVED***
        return response;
***REMOVED***
***REMOVED***
