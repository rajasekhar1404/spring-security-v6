# Spring security
- customizable authentication and access-control framework.
- Authentication is how we verify the identity of who is trying to access a particular resource.
- Authorization is determining who is allowed to access a particular resource.

- ## CSRF (Cross Site Request Forgery)
  > Assume that your bank’s website provides a form
  that allows transferring money from the currently logged in user to another bank account.
  Now pretend you authenticate to your bank’s website and then,
  without logging out, visit an evil website.
  You like to win money, so you click on the submit button.
  In the process, you have unintentionally transferred $100 to a malicious user.
  This happens because, while the evil website cannot see your cookies,
  the cookies associated with your bank are still sent along with the request.

  > The predominant and most comprehensive way to protect against CSRF attacks is to use the Synchronizer Token Pattern,
When an HTTP request is submitted,
the server must look up the expected CSRF token and compare it against the actual CSRF token in the HTTP request.
If the values do not match, the HTTP request should be rejected.

  > Use CSRF protection for any request that could be processed by a browser by normal users.
If you are creating a service that is used only by non-browser clients,
you likely want to disable CSRF protection.

- ## CORS (Cross-Origin Resource Sharing)
  > Cross-Origin Resource Sharing (CORS) is an HTTP-header based mechanism
  that allows a server to indicate any origins (domain, scheme, or port) other than its own
  from which a browser should permit loading resources.

  > CORS also relies on a mechanism by which browsers make a "preflight" request
to the server hosting the cross-origin resource,
in order to check that the server will permit the actual request.
In that preflight, the browser sends headers that indicate the HTTP method and headers
that will be used in the actual request.

### Keywords
- __SecurityContextHolder__: The **SecurityContextHolder** is where Spring Security stores the details of who is authenticated.
- __SecurityContext__: Is obtained from the **SecurityContextHolder** and contains the Authentication of the currently authenticated user.
- __Authentication__: Can be the input to **AuthenticationManager** to provide the credentials a user has provided to authenticate or the current user from the **SecurityContext**.
- __GrantedAuthority__: An authority that is granted to the principal on the **Authentication** (i.e. roles, scopes, etc.)
- __AuthenticationManager__: The API that defines how Spring Security’s **Filters** perform authentication.
- __ProviderManager__: The most common implementation of **AuthenticationManager**.
- __AuthenticationProvider__: Used by **ProviderManager** to perform a specific type of authentication.

### Reactive security flow:
- When we add spring-boot-starter-security dependency to our Spring Boot project, 
the package will automatically create chain of WebFilter that every request needs to go through. 
We can configure the chain by creating bean of SecurityWebFilterChain.
- When the we send HTTP POST request to /login that has basic authentication on its header, 
the request will be processed by AuthenticationWebFilter.
- ![immage](https://miro.medium.com/v2/resize:fit:1100/format:webp/1*dWbRyXfGxTOeb_36GtQxfg.png)
- __ServerAuthenticationConverter__: ServerAuthenticationConverter is a FunctionalInterface that is responsible to convert data on HTTP request to Authentication object. 
In basic authentication flow, Spring Security use **ServerHttpBasicAuthenticationConverter** as implementation of ServerAuthenticationConverter.
ServerHttpBasicAuthenticationConverter will return UsernamePasswordAuthenticationToken instance with authenticated field set as false at first and pass it back to AuthenticaitonWebFilter. 
Later the username and password will be checked by ReactiveAuthenticationManager. 
If matched, authenticated field will be set to true.
- __ReactiveUserDetailsService__: ReactiveUserDetailsService is an interface that has only one method to find UserDetails by given Username. 
The implementation of ReactiveUserDetailsService usually take the valid credentials from database and return implementation of UserDetails.
- __PasswordEncoder__: This PasswordEncoder will be used to match between given password on authentication request and hashed password in UserDetailsService object.
- __ReactiveAuthenticationManager__: ReactiveAuthenticationManager is an interface that will be implemented on a class that’s responsible to decide whether the authentication request is valid. 
By default, when we declare .formLogin() to modify SecurityWebFilterChain it will use UserDetailsRepositoryReactiveAuthenticationManager class, 
one of ReactiveAuthenticationManager inheritance, by default.
This class will take unauthenticated Authentication object from AuthenticationConverter, and matched the given password and hashed password using PasswordEncoder. 
The byproduct of this process is authenticated Authentication object if the authenticate method success, 
otherwise it will return error.
- __AuthenticationHandler__: After ReactiveAuthenticationManager stack finished, 
the AuthenticationWebFilter will call AuthenticationHandler. 
In this case, we have two handlers to handle successful and fail authentication attempt. 
They are WebFilterChainServerAuthenticationSuccessHandler class that implement ServerAuthenticationSuccessHandler and class that implement ServerAuthenticationFailureHandler respectively.
- If the request passed SecurityWebFilterChain successfully, the WebFilterChainServerAuthenticationSuccessHandler pass the request to POST /login controller. We can get the Authentication object from publisher’s context by adding Authentication parameter to controller.

- References:
- https://docs.spring.io/spring-security/reference/reactive/configuration/webflux.html
- https://docs.spring.io/spring-security/reference/servlet/architecture.html
- https://medium.com/gdplabs/deep-dive-into-spring-security-authentication-implementation-on-webflux-part-i-basic-1a467049900a