# Description
This is a Spring security (jar) library for securing API-full web
applications (Restfull applications).

Note: your we application need not be necessarily developed
 in spring(-web) as spring security is highly pluggable in any 
 Servlet base web application.
 
## Default security configuration
The security definition (routes security declarations) lies
in `src/main/resources/token-security.xml`.

_I am using spring security namespace config's - 
I prefer keeping these cross concerns apart from code_.
 
### Routes config
 The config defines three routes securities
 
 * `/public/**` **non secured** resources (static, helpers, ... that are 
    accessible from a non secured web application area)
 * `/private/**` **secured**, accessible by _Client Credentials_ OAuth2
    *grant_type* tokens (generally described as Service Accounts). Remember that
    these tokens are issued using only applications _clientId_ and _clientSecret_.
    
    These kind of _logged_ entities are expeced (in the xml config file `token-security.xml`) 
    to have _APPLICATION_ Role. This role is extracted from the `realm_access.roles` (keycloak) token claim 
    (remember that keycloak generates JWT tokens format)

    The `/private/**` are generally reserved when systems want to access their resources
    (API to API calls for example)
 * `/**` **secured** accessible by users as their **resources** (classical Restfull resources).
    such endpoints are accessible by tokens obtained from an OAuth2 or an OpenId Connect server
    using _standard web flows_ (Authorization Code or Implicit flow) or (not recommanded) _Password credentials
    flow_

## Using the lib in your web module

1. Add the project as a jar dependency
2. configure the servlet in your `web.xml` as follows
```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <web-app>
    <!-- Spring security config -->
   	<filter>
   		<filter-name>springSecurityFilterChain</filter-name>
   		<filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
   	</filter>
   	<filter-mapping>
   		<filter-name>springSecurityFilterChain</filter-name>
   		<url-pattern>/*</url-pattern>
   	</filter-mapping>   
   	<listener>
   		<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
   	</listener>
   	<context-param>
   		<param-name>contextConfigLocation</param-name>
   		<param-value>
   	        classpath:/token-security.xml
   	    </param-value>
   	</context-param>
    </web-app>
```
3. Do not forget to specify there config parameter when starting you application 
 
 - `keycloak.publicCertsUrl` keycloak public certs endpoint
  for example *http://localhost:8080/auth/realms/myrealm/protocol/openid-connect/certs*
 - `keycloak.realmUrl` keycloak realm URL (as needed by the keycloak admin lib for token verification)
  for example *http://localhost:8080/auth/realms/myrealm*

_Important to note that in this library, we are not using the keycloak introspection endpoint as 
calling a remote http server on every received token may be rapidly an issue, especially in full API micro service
applications ... loosing the benefit of the JWT format auto-signed tokens._

_Sign keys retrieved from the keycloak and generated public keys objects are cached._ 

