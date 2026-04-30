# Spring MVC - Annotations Summary

<details>
  <summary><b>Controller Annotations</b></summary>
  <br/>

  **Controller Types:**
  + <code>@Controller</code> - Marks class as MVC controller (returns views)
  + <code>@RestController</code> - Marks class as REST controller (returns data, combines @Controller + @ResponseBody)

  **Request Mapping:**
  + <code>@RequestMapping</code> - Maps HTTP requests to handler methods (class or method level)
  + <code>@GetMapping</code> - Shortcut for @RequestMapping(method = RequestMethod.GET)
  + <code>@PostMapping</code> - Shortcut for @RequestMapping(method = RequestMethod.POST)
  + <code>@PutMapping</code> - Shortcut for @RequestMapping(method = RequestMethod.PUT)
  + <code>@PatchMapping</code> - Shortcut for @RequestMapping(method = RequestMethod.PATCH)
  + <code>@DeleteMapping</code> - Shortcut for @RequestMapping(method = RequestMethod.DELETE)

  **Request Parameters:**
  + <code>@RequestBody</code> - Binds HTTP request body to method parameter
  + <code>@ResponseBody</code> - Serializes return value to HTTP response body
  + <code>@PathVariable</code> - Extracts values from URI path
  + <code>@RequestParam</code> - Extracts query parameters from URL
  + <code>@RequestHeader</code> - Extracts HTTP header values
  + <code>@CookieValue</code> - Extracts cookie values
  + <code>@ModelAttribute</code> - Binds request parameters to object

  **Response Configuration:**
  + <code>@ResponseStatus</code> - Sets HTTP response status code
  + <code>ResponseEntity</code> - Provides full control over HTTP response (status, headers, body)

  **Read section:** `controllers-section.txt`, `request-response-handling-section.txt`

</details>

<details>
  <summary><b>Exception Handling Annotations</b></summary>
  <br/>

  **Exception Handlers:**
  + <code>@ExceptionHandler</code> - Handles exceptions at controller level
  + <code>@ControllerAdvice</code> - Global exception handling across all controllers
  + <code>@RestControllerAdvice</code> - Combines @ControllerAdvice + @ResponseBody for REST APIs
  + <code>ResponseEntityExceptionHandler</code> - Base class for handling Spring MVC exceptions

  **Common Exceptions:**
  + **MethodArgumentNotValidException** - Thrown when @Valid validation fails
  + **HttpMessageNotReadableException** - Invalid JSON/XML in request body
  + **HttpRequestMethodNotSupportedException** - Wrong HTTP method
  + **MissingServletRequestParameterException** - Missing required parameter
  + **TypeMismatchException** - Type conversion fails
  + **HttpMediaTypeNotSupportedException** - Unsupported Content-Type

  **Read section:** `exception-handling-section.txt`

</details>

<details>
  <summary><b>Validation Annotations</b></summary>
  <br/>

  **Validation Trigger:**
  + <code>@Valid</code> - Triggers validation on method parameter
  + <code>@Validated</code> - Triggers validation with validation groups

  **Null/Empty Checks:**
  + <code>@NotNull</code> - Value cannot be null
  + <code>@NotEmpty</code> - String/Collection cannot be null or empty
  + <code>@NotBlank</code> - String cannot be null, empty, or whitespace only

  **Size Constraints:**
  + <code>@Size</code> - String/Collection size constraints (min, max)
  + <code>@Min</code> - Numeric minimum value
  + <code>@Max</code> - Numeric maximum value
  + <code>@DecimalMin</code> - Decimal minimum value
  + <code>@DecimalMax</code> - Decimal maximum value
  + <code>@Positive</code> - Must be positive (> 0)
  + <code>@PositiveOrZero</code> - Must be positive or zero (>= 0)
  + <code>@Negative</code> - Must be negative (< 0)
  + <code>@NegativeOrZero</code> - Must be negative or zero (<= 0)

  **Pattern Matching:**
  + <code>@Pattern</code> - String must match regex pattern
  + <code>@Email</code> - Valid email format

  **Date/Time Validation:**
  + <code>@Past</code> - Date must be in the past
  + <code>@PastOrPresent</code> - Date must be in the past or present
  + <code>@Future</code> - Date must be in the future
  + <code>@FutureOrPresent</code> - Date must be in the future or present

  **Boolean Validation:**
  + <code>@AssertTrue</code> - Must be true
  + <code>@AssertFalse</code> - Must be false

  **Custom Validation:**
  + <code>@Constraint</code> - Marks custom validation annotation
  + <code>ConstraintValidator</code> - Interface for custom validator implementation

  **Read section:** `validation-section.txt`

</details>

<details>
  <summary><b>Interceptor Annotations</b></summary>
  <br/>

  **Interceptor Interface:**
  + <code>HandlerInterceptor</code> - Interface for creating interceptors
  + **preHandle()** - Called before controller execution (return true to continue, false to stop)
  + **postHandle()** - Called after controller, before view rendering
  + **afterCompletion()** - Called after view rendering (cleanup)

  **Configuration:**
  + <code>WebMvcConfigurer.addInterceptors()</code> - Register interceptors
  + <code>InterceptorRegistry</code> - Registry for adding interceptors
  + **addPathPatterns()** - Specify paths to intercept
  + **excludePathPatterns()** - Specify paths to exclude
  + **order()** - Set execution order

  **Use Cases:**
  + Authentication and authorization
  + Logging and auditing
  + Performance monitoring
  + Rate limiting
  + Request/response modification

  **Read section:** `interceptors-section.txt`

</details>

<details>
  <summary><b>Filter Annotations</b></summary>
  <br/>

  **Filter Interface:**
  + <code>Filter</code> - Java Servlet filter interface
  + **doFilter()** - Filter method (must call chain.doFilter())
  + **init()** - Initialization method
  + **destroy()** - Cleanup method

  **Registration:**
  + <code>@Component</code> - Register filter as Spring bean
  + <code>@Order</code> - Set filter execution order
  + <code>@WebFilter</code> - Servlet 3.0 filter annotation (requires @ServletComponentScan)
  + <code>FilterRegistrationBean</code> - Programmatic filter registration

  **Filters vs Interceptors:**
  + **Filters** - Servlet container level, all requests (including static resources)
  + **Interceptors** - Spring MVC level, only controller requests

  **Read section:** `filters-vs-interceptors-section.txt`

</details>

<details>
  <summary><b>Content Negotiation Annotations</b></summary>
  <br/>

  **Content Type Configuration:**
  + <code>@produces</code> - Restricts which formats method can return
  + <code>@Consumes</code> - Restricts which formats method can accept
  + <code>MediaType</code> - Constants for media types (APPLICATION_JSON, APPLICATION_XML, etc.)

  **Configuration:**
  + <code>WebMvcConfigurer.configureContentNegotiation()</code> - Configure content negotiation
  + <code>ContentNegotiationConfigurer</code> - Configurer for content negotiation
  + **favorParameter()** - Enable query parameter strategy
  + **favorPathExtension()** - Enable path extension strategy (deprecated)
  + **ignoreAcceptHeader()** - Ignore Accept header
  + **defaultContentType()** - Set default content type
  + **mediaType()** - Register media types

  **Message Converters:**
  + <code>HttpMessageConverter</code> - Interface for converting HTTP messages
  + <code>MappingJackson2HttpMessageConverter</code> - JSON converter (default)
  + <code>Jaxb2RootElementHttpMessageConverter</code> - XML converter

  **Read section:** `content-negotiation-section.txt`

</details>

<details>
  <summary><b>CORS Annotations</b></summary>
  <br/>

  **CORS Configuration:**
  + <code>@CrossOrigin</code> - Enable CORS on controller or method level
  + **origins** - Allowed origins (domains)
  + **methods** - Allowed HTTP methods
  + **allowedHeaders** - Headers client can send
  + **exposedHeaders** - Headers client can read from response
  + **allowCredentials** - Allow cookies/authentication
  + **maxAge** - Cache preflight response (seconds)

  **Global Configuration:**
  + <code>WebMvcConfigurer.addCorsMappings()</code> - Configure CORS globally
  + <code>CorsRegistry</code> - Registry for CORS mappings
  + <code>CorsConfiguration</code> - CORS configuration class
  + <code>CorsConfigurationSource</code> - Source for CORS configuration

  **Filter:**
  + <code>CorsFilter</code> - Custom CORS filter for fine-grained control

  **Headers:**
  + **Access-Control-Allow-Origin** - Which origins can access
  + **Access-Control-Allow-Methods** - Which HTTP methods allowed
  + **Access-Control-Allow-Headers** - Which headers allowed
  + **Access-Control-Max-Age** - How long to cache preflight response

  **Read section:** `cors-configuration-section.txt`

</details>

<details>
  <summary><b>Async Processing Annotations</b></summary>
  <br/>

  **Async Configuration:**
  + <code>@EnableAsync</code> - Enable async processing
  + <code>@Async</code> - Execute method asynchronously
  + <code>AsyncConfigurer</code> - Interface for configuring async processing
  + <code>ThreadPoolTaskExecutor</code> - Thread pool for async operations

  **Return Types:**
  + <code>Callable&lt;T&gt;</code> - Returns result asynchronously from background thread
  + <code>DeferredResult&lt;T&gt;</code> - Provides more control over async processing
  + <code>CompletableFuture&lt;T&gt;</code> - Powerful async composition
  + <code>WebAsyncTask&lt;T&gt;</code> - Callable with timeout and error handling
  + <code>SseEmitter</code> - Server-Sent Events for real-time updates
  + <code>Flux&lt;T&gt;</code> - Reactive stream (requires Spring WebFlux)

  **Configuration Methods:**
  + **getAsyncExecutor()** - Configure custom thread pool
  + **getAsyncUncaughtExceptionHandler()** - Handle uncaught exceptions
  + **setCorePoolSize()** - Set core pool size
  + **setMaxPoolSize()** - Set maximum pool size
  + **setQueueCapacity()** - Set queue capacity
  + **setThreadNamePrefix()** - Set thread name prefix

  **Read section:** `async-request-processing-section.txt`

</details>

---

## Controllers

<details>
  <summary>What are Controllers?</summary>
  <br/>

  Controllers handle HTTP requests and return responses in Spring MVC applications.

  **Responsibilities:**
  + Handle incoming HTTP requests
  + Process request data
  + Call service layer
  + Return HTTP responses
  + Map URLs to methods

  **Types of controllers:**
  + **@Controller** - Returns views (HTML pages)
  + **@RestController** - Returns data (JSON/XML)

  **Basic example:**

  ```java
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
      
      @GetMapping
      public List<User> getAllUsers() {
          return userService.findAll();
      }
      
      @GetMapping("/{id}")
      public User getUser(@PathVariable Long id) {
          return userService.findById(id);
      }
      
      @PostMapping
      public User createUser(@RequestBody User user) {
          return userService.create(user);
      }
  }
  ```

</details>

<details>
  <summary>@Controller vs @RestController</summary>
  <br/>

  **@Controller** returns views, **@RestController** returns data.

  **@Controller (Traditional MVC):**

  ```java
  @Controller
  @RequestMapping("/users")
  public class UserController {
      
      @GetMapping
      public String listUsers(Model model) {
          List<User> users = userService.findAll();
          model.addAttribute("users", users);
          return "users/list";  // Returns view name (users/list.html)
      }
      
      @GetMapping("/{id}")
      public String viewUser(@PathVariable Long id, Model model) {
          User user = userService.findById(id);
          model.addAttribute("user", user);
          return "users/view";  // Returns view name
      }
  }
  ```

  **@RestController (REST API):**

  ```java
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
      
      @GetMapping
      public List<User> getAllUsers() {
          return userService.findAll();
          // Returns JSON: [{"id":1,"name":"John"},...]
      }
      
      @GetMapping("/{id}")
      public User getUser(@PathVariable Long id) {
          return userService.findById(id);
          // Returns JSON: {"id":1,"name":"John"}
      }
  }
  ```

  **@RestController = @Controller + @ResponseBody:**

  ```java
  // These are equivalent:
  
  // Option 1: @RestController
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
      @GetMapping
      public List<User> getUsers() {
          return userService.findAll();
      }
  }
  
  // Option 2: @Controller + @ResponseBody
  @Controller
  @RequestMapping("/api/users")
  public class UserController {
      @GetMapping
      @ResponseBody  // Needed on each method
      public List<User> getUsers() {
          return userService.findAll();
      }
  }
  ```

  **When to use each:**
  + **@Controller** - Server-side rendering (Thymeleaf, JSP)
  + **@RestController** - REST APIs (JSON/XML responses)

</details>

<details>
  <summary>@RequestMapping</summary>
  <br/>

  **@RequestMapping** maps HTTP requests to handler methods.

  **Class-level mapping:**

  ```java
  @RestController
  @RequestMapping("/api/users")  // Base path for all methods
  public class UserController {
      
      @GetMapping  // Maps to GET /api/users
      public List<User> getAllUsers() {
          return userService.findAll();
      }
      
      @GetMapping("/{id}")  // Maps to GET /api/users/{id}
      public User getUser(@PathVariable Long id) {
          return userService.findById(id);
      }
  }
  ```

  **Method-level mapping:**

  ```java
  @RestController
  public class UserController {
      
      @RequestMapping(value = "/api/users", method = RequestMethod.GET)
      public List<User> getAllUsers() {
          return userService.findAll();
      }
      
      @RequestMapping(value = "/api/users/{id}", method = RequestMethod.GET)
      public User getUser(@PathVariable Long id) {
          return userService.findById(id);
      }
  }
  ```

  **Multiple paths:**

  ```java
  @RestController
  public class UserController {
      
      // Maps to both /api/users and /api/members
      @GetMapping({"/api/users", "/api/members"})
      public List<User> getUsers() {
          return userService.findAll();
      }
  }
  ```

  **Path patterns:**

  ```java
  @RestController
  @RequestMapping("/api")
  public class UserController {
      
      // Exact match: /api/users
      @GetMapping("/users")
      public List<User> getUsers() {
          return userService.findAll();
      }
      
      // Path variable: /api/users/123
      @GetMapping("/users/{id}")
      public User getUser(@PathVariable Long id) {
          return userService.findById(id);
      }
      
      // Multiple variables: /api/users/123/orders/456
      @GetMapping("/users/{userId}/orders/{orderId}")
      public Order getOrder(@PathVariable Long userId, @PathVariable Long orderId) {
          return orderService.findByUserAndId(userId, orderId);
      }
      
      // Wildcard: /api/users/any/path
      @GetMapping("/users/*")
      public String wildcard() {
          return "Matches /api/users/anything";
      }
      
      // Multiple wildcards: /api/users/any/nested/path
      @GetMapping("/users/**")
      public String multiWildcard() {
          return "Matches /api/users/any/nested/path";
      }
      
      // Regex: /api/users/123 (only numbers)
      @GetMapping("/users/{id:[0-9]+}")
      public User getUserWithRegex(@PathVariable Long id) {
          return userService.findById(id);
      }
  }
  ```

</details>

<details>
  <summary>HTTP Method Annotations</summary>
  <br/>

  **Shortcut annotations for HTTP methods:**

  ```java
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
      
      // GET - Retrieve resources
      @GetMapping
      public List<User> getAllUsers() {
          return userService.findAll();
      }
      
      @GetMapping("/{id}")
      public User getUser(@PathVariable Long id) {
          return userService.findById(id);
      }
      
      // POST - Create new resource
      @PostMapping
      public User createUser(@RequestBody User user) {
          return userService.create(user);
      }
      
      // PUT - Update entire resource
      @PutMapping("/{id}")
      public User updateUser(@PathVariable Long id, @RequestBody User user) {
          return userService.update(id, user);
      }
      
      // PATCH - Partial update
      @PatchMapping("/{id}")
      public User patchUser(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
          return userService.patch(id, updates);
      }
      
      // DELETE - Delete resource
      @DeleteMapping("/{id}")
      public void deleteUser(@PathVariable Long id) {
          userService.delete(id);
      }
  }
  ```

  **Equivalent @RequestMapping:**

  ```java
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
      
      @RequestMapping(method = RequestMethod.GET)
      public List<User> getAllUsers() {
          return userService.findAll();
      }
      
      @RequestMapping(method = RequestMethod.POST)
      public User createUser(@RequestBody User user) {
          return userService.create(user);
      }
      
      @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
      public User updateUser(@PathVariable Long id, @RequestBody User user) {
          return userService.update(id, user);
      }
      
      @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
      public void deleteUser(@PathVariable Long id) {
          userService.delete(id);
      }
  }
  ```

  **HTTP method semantics:**

  | Method | Purpose | Idempotent | Safe | Request Body | Response Body |
  |--------|---------|------------|------|--------------|---------------|
  | GET | Retrieve | ✅ Yes | ✅ Yes | ❌ No | ✅ Yes |
  | POST | Create | ❌ No | ❌ No | ✅ Yes | ✅ Yes |
  | PUT | Update (full) | ✅ Yes | ❌ No | ✅ Yes | ✅ Yes |
  | PATCH | Update (partial) | ❌ No | ❌ No | ✅ Yes | ✅ Yes |
  | DELETE | Delete | ✅ Yes | ❌ No | ❌ No | ❌ Optional |

</details>

<details>
  <summary>Request Parameters</summary>
  <br/>

  **@RequestParam** extracts query parameters.

  **Basic usage:**

  ```java
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
      
      // GET /api/users?name=john
      @GetMapping
      public List<User> searchUsers(@RequestParam String name) {
          return userService.findByName(name);
      }
      
      // Multiple parameters: GET /api/users?name=john&age=25
      @GetMapping("/search")
      public List<User> searchUsers(
          @RequestParam String name,
          @RequestParam Integer age
      ) {
          return userService.findByNameAndAge(name, age);
      }
  }
  ```

  **Optional parameters:**

  ```java
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
      
      // Optional parameter with default value
      @GetMapping
      public List<User> getUsers(
          @RequestParam(required = false, defaultValue = "0") int page,
          @RequestParam(required = false, defaultValue = "20") int size
      ) {
          return userService.findAll(page, size);
      }
      
      // Using Optional
      @GetMapping("/search")
      public List<User> searchUsers(@RequestParam Optional<String> name) {
          return name.map(userService::findByName)
                     .orElse(userService.findAll());
      }
  }
  ```

  **Parameter name mapping:**

  ```java
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
      
      // GET /api/users?user_name=john
      @GetMapping
      public List<User> searchUsers(
          @RequestParam("user_name") String username
      ) {
          return userService.findByUsername(username);
      }
  }
  ```

  **Map all parameters:**

  ```java
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
      
      // GET /api/users?name=john&age=25&city=NYC
      @GetMapping
      public List<User> searchUsers(@RequestParam Map<String, String> params) {
          String name = params.get("name");
          String age = params.get("age");
          String city = params.get("city");
          return userService.search(params);
      }
  }
  ```

</details>

<details>
  <summary>Path Variables</summary>
  <br/>

  **@PathVariable** extracts values from URL path.

  **Basic usage:**

  ```java
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
      
      // GET /api/users/123
      @GetMapping("/{id}")
      public User getUser(@PathVariable Long id) {
          return userService.findById(id);
      }
      
      // Multiple path variables: GET /api/users/123/orders/456
      @GetMapping("/{userId}/orders/{orderId}")
      public Order getOrder(
          @PathVariable Long userId,
          @PathVariable Long orderId
      ) {
          return orderService.findByUserAndId(userId, orderId);
      }
  }
  ```

  **Variable name mapping:**

  ```java
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
      
      // GET /api/users/123
      @GetMapping("/{user_id}")
      public User getUser(@PathVariable("user_id") Long userId) {
          return userService.findById(userId);
      }
  }
  ```

  **Optional path variables:**

  ```java
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
      
      // Matches both /api/users and /api/users/123
      @GetMapping({"", "/{id}"})
      public ResponseEntity<?> getUsers(@PathVariable(required = false) Long id) {
          if (id == null) {
              return ResponseEntity.ok(userService.findAll());
          }
          return ResponseEntity.ok(userService.findById(id));
      }
  }
  ```

  **Path variable with regex:**

  ```java
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
      
      // Only accepts numeric IDs: GET /api/users/123
      @GetMapping("/{id:[0-9]+}")
      public User getUserById(@PathVariable Long id) {
          return userService.findById(id);
      }
      
      // Only accepts alphanumeric usernames: GET /api/users/john123
      @GetMapping("/{username:[a-zA-Z0-9]+}")
      public User getUserByUsername(@PathVariable String username) {
          return userService.findByUsername(username);
      }
  }
  ```

</details>

<details>
  <summary>Request Body</summary>
  <br/>

  **@RequestBody** binds HTTP request body to object.

  **Basic usage:**

  ```java
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
      
      // POST /api/users
      // Body: {"username":"john","email":"john@example.com"}
      @PostMapping
      public User createUser(@RequestBody User user) {
          return userService.create(user);
      }
      
      // PUT /api/users/123
      // Body: {"username":"john_updated","email":"john@example.com"}
      @PutMapping("/{id}")
      public User updateUser(
          @PathVariable Long id,
          @RequestBody User user
      ) {
          return userService.update(id, user);
      }
  }
  ```

  **DTO (Data Transfer Object):**

  ```java
  public class CreateUserRequest {
      private String username;
      private String email;
      private String password;
      
      // Getters and setters
  }
  
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
      
      @PostMapping
      public User createUser(@RequestBody CreateUserRequest request) {
          User user = new User();
          user.setUsername(request.getUsername());
          user.setEmail(request.getEmail());
          user.setPassword(passwordEncoder.encode(request.getPassword()));
          return userService.create(user);
      }
  }
  ```

  **Validation:**

  ```java
  public class CreateUserRequest {
      @NotBlank(message = "Username is required")
      @Size(min = 3, max = 50)
      private String username;
      
      @NotBlank(message = "Email is required")
      @Email(message = "Invalid email format")
      private String email;
      
      @NotBlank(message = "Password is required")
      @Size(min = 8, message = "Password must be at least 8 characters")
      private String password;
      
      // Getters and setters
  }
  
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
      
      @PostMapping
      public User createUser(@Valid @RequestBody CreateUserRequest request) {
          return userService.create(request);
      }
  }
  ```

  **Partial update with Map:**

  ```java
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
      
      // PATCH /api/users/123
      // Body: {"email":"newemail@example.com"}
      @PatchMapping("/{id}")
      public User patchUser(
          @PathVariable Long id,
          @RequestBody Map<String, Object> updates
      ) {
          return userService.patch(id, updates);
      }
  }
  ```

</details>

<details>
  <summary>Response Status</summary>
  <br/>

  **@ResponseStatus** sets HTTP response status.

  **Basic usage:**

  ```java
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
      
      // Returns 201 Created
      @PostMapping
      @ResponseStatus(HttpStatus.CREATED)
      public User createUser(@RequestBody User user) {
          return userService.create(user);
      }
      
      // Returns 204 No Content
      @DeleteMapping("/{id}")
      @ResponseStatus(HttpStatus.NO_CONTENT)
      public void deleteUser(@PathVariable Long id) {
          userService.delete(id);
      }
  }
  ```

  **Using ResponseEntity:**

  ```java
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
      
      @GetMapping("/{id}")
      public ResponseEntity<User> getUser(@PathVariable Long id) {
          User user = userService.findById(id);
          
          if (user == null) {
              return ResponseEntity.notFound().build();  // 404
          }
          
          return ResponseEntity.ok(user);  // 200
      }
      
      @PostMapping
      public ResponseEntity<User> createUser(@RequestBody User user) {
          User created = userService.create(user);
          
          URI location = ServletUriComponentsBuilder
              .fromCurrentRequest()
              .path("/{id}")
              .buildAndExpand(created.getId())
              .toUri();
          
          return ResponseEntity.created(location).body(created);  // 201
      }
      
      @PutMapping("/{id}")
      public ResponseEntity<User> updateUser(
          @PathVariable Long id,
          @RequestBody User user
      ) {
          if (!userService.exists(id)) {
              return ResponseEntity.notFound().build();  // 404
          }
          
          User updated = userService.update(id, user);
          return ResponseEntity.ok(updated);  // 200
      }
      
      @DeleteMapping("/{id}")
      public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
          if (!userService.exists(id)) {
              return ResponseEntity.notFound().build();  // 404
          }
          
          userService.delete(id);
          return ResponseEntity.noContent().build();  // 204
      }
  }
  ```

  **Common HTTP status codes:**

  | Code | Status | Use Case |
  |------|--------|----------|
  | 200 | OK | Successful GET, PUT, PATCH |
  | 201 | Created | Successful POST |
  | 204 | No Content | Successful DELETE |
  | 400 | Bad Request | Invalid request data |
  | 401 | Unauthorized | Authentication required |
  | 403 | Forbidden | No permission |
  | 404 | Not Found | Resource not found |
  | 409 | Conflict | Duplicate resource |
  | 500 | Internal Server Error | Server error |

</details>

<details>
  <summary>Headers</summary>
  <br/>

  **@RequestHeader** extracts HTTP headers.

  **Basic usage:**

  ```java
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
      
      @GetMapping
      public List<User> getUsers(
          @RequestHeader("Authorization") String authToken
      ) {
          // Use auth token
          return userService.findAll();
      }
      
      // Multiple headers
      @GetMapping("/info")
      public String getInfo(
          @RequestHeader("User-Agent") String userAgent,
          @RequestHeader("Accept-Language") String language
      ) {
          return "User-Agent: " + userAgent + ", Language: " + language;
      }
  }
  ```

  **Optional headers:**

  ```java
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
      
      @GetMapping
      public List<User> getUsers(
          @RequestHeader(value = "X-Custom-Header", required = false) String customHeader
      ) {
          return userService.findAll();
      }
      
      // With default value
      @GetMapping("/search")
      public List<User> searchUsers(
          @RequestHeader(value = "X-Page-Size", defaultValue = "20") int pageSize
      ) {
          return userService.findAll(pageSize);
      }
  }
  ```

  **All headers:**

  ```java
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
      
      @GetMapping("/headers")
      public Map<String, String> getHeaders(@RequestHeader Map<String, String> headers) {
          return headers;
      }
      
      // Using HttpHeaders
      @GetMapping("/headers-object")
      public HttpHeaders getHeaders(@RequestHeader HttpHeaders headers) {
          return headers;
      }
  }
  ```

  **Set response headers:**

  ```java
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
      
      @GetMapping("/{id}")
      public ResponseEntity<User> getUser(@PathVariable Long id) {
          User user = userService.findById(id);
          
          HttpHeaders headers = new HttpHeaders();
          headers.add("X-Custom-Header", "CustomValue");
          headers.add("X-User-Id", id.toString());
          
          return ResponseEntity.ok()
              .headers(headers)
              .body(user);
      }
  }
  ```

</details>

<details>
  <summary>Complete CRUD Example</summary>
  <br/>

  **Full REST controller with all operations:**

  ```java
  @RestController
  @RequestMapping("/api/users")
  @RequiredArgsConstructor
  public class UserController {
      
      private final UserService userService;
      
      // GET /api/users - Get all users
      @GetMapping
      public ResponseEntity<List<User>> getAllUsers(
          @RequestParam(defaultValue = "0") int page,
          @RequestParam(defaultValue = "20") int size,
          @RequestParam(defaultValue = "id") String sortBy
      ) {
          List<User> users = userService.findAll(page, size, sortBy);
          return ResponseEntity.ok(users);
      }
      
      // GET /api/users/{id} - Get user by ID
      @GetMapping("/{id}")
      public ResponseEntity<User> getUser(@PathVariable Long id) {
          return userService.findById(id)
              .map(ResponseEntity::ok)
              .orElse(ResponseEntity.notFound().build());
      }
      
      // GET /api/users/search - Search users
      @GetMapping("/search")
      public ResponseEntity<List<User>> searchUsers(
          @RequestParam(required = false) String username,
          @RequestParam(required = false) String email
      ) {
          List<User> users = userService.search(username, email);
          return ResponseEntity.ok(users);
      }
      
      // POST /api/users - Create new user
      @PostMapping
      public ResponseEntity<User> createUser(@Valid @RequestBody CreateUserRequest request) {
          User created = userService.create(request);
          
          URI location = ServletUriComponentsBuilder
              .fromCurrentRequest()
              .path("/{id}")
              .buildAndExpand(created.getId())
              .toUri();
          
          return ResponseEntity.created(location).body(created);
      }
      
      // PUT /api/users/{id} - Update user (full)
      @PutMapping("/{id}")
      public ResponseEntity<User> updateUser(
          @PathVariable Long id,
          @Valid @RequestBody UpdateUserRequest request
      ) {
          return userService.update(id, request)
              .map(ResponseEntity::ok)
              .orElse(ResponseEntity.notFound().build());
      }
      
      // PATCH /api/users/{id} - Update user (partial)
      @PatchMapping("/{id}")
      public ResponseEntity<User> patchUser(
          @PathVariable Long id,
          @RequestBody Map<String, Object> updates
      ) {
          return userService.patch(id, updates)
              .map(ResponseEntity::ok)
              .orElse(ResponseEntity.notFound().build());
      }
      
      // DELETE /api/users/{id} - Delete user
      @DeleteMapping("/{id}")
      public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
          if (!userService.exists(id)) {
              return ResponseEntity.notFound().build();
          }
          
          userService.delete(id);
          return ResponseEntity.noContent().build();
      }
      
      // GET /api/users/{id}/orders - Get user's orders
      @GetMapping("/{id}/orders")
      public ResponseEntity<List<Order>> getUserOrders(@PathVariable Long id) {
          if (!userService.exists(id)) {
              return ResponseEntity.notFound().build();
          }
          
          List<Order> orders = orderService.findByUserId(id);
          return ResponseEntity.ok(orders);
      }
  }
  ```

</details>

<details>
  <summary>Best Practices</summary>
  <br/>

  ```java
  // ✅ DO: Use @RestController for REST APIs
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
  }
  
  // ✅ DO: Use specific HTTP method annotations
  @GetMapping("/{id}")
  @PostMapping
  @PutMapping("/{id}")
  @DeleteMapping("/{id}")
  
  // ❌ DON'T: Use generic @RequestMapping
  @RequestMapping(value = "/{id}", method = RequestMethod.GET)  // Verbose
  
  // ✅ DO: Use path variables for resource IDs
  @GetMapping("/users/{id}")
  public User getUser(@PathVariable Long id) {
  }
  
  // ✅ DO: Use query parameters for filtering/pagination
  @GetMapping("/users")
  public List<User> getUsers(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size
  ) {
  }
  
  // ✅ DO: Use DTOs for request/response
  @PostMapping("/users")
  public User createUser(@RequestBody CreateUserRequest request) {
  }
  
  // ❌ DON'T: Expose entities directly
  @PostMapping("/users")
  public User createUser(@RequestBody User user) {  // Exposes all fields
  }
  
  // ✅ DO: Validate input
  @PostMapping("/users")
  public User createUser(@Valid @RequestBody CreateUserRequest request) {
  }
  
  // ✅ DO: Use ResponseEntity for flexible responses
  @GetMapping("/{id}")
  public ResponseEntity<User> getUser(@PathVariable Long id) {
      return userService.findById(id)
          .map(ResponseEntity::ok)
          .orElse(ResponseEntity.notFound().build());
  }
  
  // ✅ DO: Return appropriate HTTP status codes
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public User createUser(@RequestBody User user) {
  }
  
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteUser(@PathVariable Long id) {
  }
  
  // ✅ DO: Use meaningful endpoint names
  @GetMapping("/users/active")  // Clear
  @GetMapping("/users/search")  // Clear
  
  // ❌ DON'T: Use unclear names
  @GetMapping("/users/get")  // Redundant
  @GetMapping("/users/data")  // Vague
  
  // ✅ DO: Follow REST conventions
  GET    /api/users       - List all users
  GET    /api/users/{id}  - Get specific user
  POST   /api/users       - Create user
  PUT    /api/users/{id}  - Update user (full)
  PATCH  /api/users/{id}  - Update user (partial)
  DELETE /api/users/{id}  - Delete user
  
  // ✅ DO: Use nested resources appropriately
  GET /api/users/{userId}/orders  // User's orders
  GET /api/users/{userId}/orders/{orderId}  // Specific order
  
  // ❌ DON'T: Nest too deeply
  GET /api/users/{userId}/orders/{orderId}/items/{itemId}/details  // Too deep
  
  // ✅ DO: Handle errors gracefully
  @GetMapping("/{id}")
  public ResponseEntity<User> getUser(@PathVariable Long id) {
      try {
          User user = userService.findById(id);
          return ResponseEntity.ok(user);
      } catch (UserNotFoundException e) {
          return ResponseEntity.notFound().build();
      }
  }
  
  // ✅ DO: Use constructor injection
  @RestController
  @RequiredArgsConstructor
  public class UserController {
      private final UserService userService;
  }
  
  // ❌ DON'T: Use field injection
  @RestController
  public class UserController {
      @Autowired
      private UserService userService;  // Harder to test
  }
  
  // ✅ DO: Keep controllers thin
  @PostMapping
  public User createUser(@RequestBody CreateUserRequest request) {
      return userService.create(request);  // Delegate to service
  }
  
  // ❌ DON'T: Put business logic in controllers
  @PostMapping
  public User createUser(@RequestBody CreateUserRequest request) {
      // Validation logic
      // Business logic
      // Database operations
      // All in controller - BAD!
  }
  ```

  **Summary:**
  + Use **@RestController** for REST APIs (returns JSON/XML)
  + Use **@Controller** for server-side rendering (returns views)
  + Use **specific HTTP method annotations** (@GetMapping, @PostMapping, etc.)
  + Use **@PathVariable** for resource IDs in URL path
  + Use **@RequestParam** for filtering, pagination, sorting
  + Use **@RequestBody** for request payload
  + Use **@Valid** for input validation
  + Use **ResponseEntity** for flexible responses
  + Return **appropriate HTTP status codes**
  + Use **DTOs** instead of exposing entities
  + Follow **REST conventions** for endpoint naming
  + Keep **controllers thin** - delegate to service layer
  + Use **constructor injection** for dependencies
  + Handle **errors gracefully**
  + Don't **nest resources too deeply**

</details>

## Request/Response Handling

<details>
  <summary>@RequestBody</summary>
  <br/>

  **@RequestBody** binds HTTP request body to Java object.

  **Basic usage:**

  ```java
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
      
      // POST /api/users
      // Body: {"username":"john","email":"john@example.com"}
      @PostMapping
      public User createUser(@RequestBody User user) {
          return userService.create(user);
      }
  }
  ```

  **With validation:**

  ```java
  public class CreateUserRequest {
      @NotBlank(message = "Username is required")
      @Size(min = 3, max = 50)
      private String username;
      
      @NotBlank
      @Email
      private String email;
      
      // Getters and setters
  }
  
  @PostMapping
  public User createUser(@Valid @RequestBody CreateUserRequest request) {
      return userService.create(request);
  }
  ```

  **How it works:**
  + Uses **HttpMessageConverter** to deserialize JSON/XML to Java object
  + Default converter: **MappingJackson2HttpMessageConverter** (for JSON)
  + Reads entire request body
  + Can only be used once per request

</details>

<details>
  <summary>@ResponseBody</summary>
  <br/>

  **@ResponseBody** serializes Java object to HTTP response body.

  **Basic usage:**

  ```java
  @Controller
  @RequestMapping("/api/users")
  public class UserController {
      
      @GetMapping("/{id}")
      @ResponseBody  // Converts User to JSON
      public User getUser(@PathVariable Long id) {
          return userService.findById(id);
      }
  }
  ```

  **@RestController = @Controller + @ResponseBody:**

  ```java
  // These are equivalent:
  
  // Option 1: @RestController (preferred)
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
      @GetMapping("/{id}")
      public User getUser(@PathVariable Long id) {
          return userService.findById(id);
      }
  }
  
  // Option 2: @Controller + @ResponseBody
  @Controller
  @RequestMapping("/api/users")
  public class UserController {
      @GetMapping("/{id}")
      @ResponseBody
      public User getUser(@PathVariable Long id) {
          return userService.findById(id);
      }
  }
  ```

  **How it works:**
  + Uses **HttpMessageConverter** to serialize object to JSON/XML
  + Bypasses view resolution
  + Sets Content-Type header automatically

</details>

<details>
  <summary>@PathVariable</summary>
  <br/>

  **@PathVariable** extracts values from URI path.

  **Basic usage:**

  ```java
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
      
      // GET /api/users/123
      @GetMapping("/{id}")
      public User getUser(@PathVariable Long id) {
          return userService.findById(id);
      }
      
      // GET /api/users/123/orders/456
      @GetMapping("/{userId}/orders/{orderId}")
      public Order getOrder(
          @PathVariable Long userId,
          @PathVariable Long orderId
      ) {
          return orderService.findByUserAndId(userId, orderId);
      }
  }
  ```

  **Variable name mapping:**

  ```java
  // When path variable name differs from parameter name
  @GetMapping("/{user_id}")
  public User getUser(@PathVariable("user_id") Long userId) {
      return userService.findById(userId);
  }
  ```

  **Optional path variables:**

  ```java
  // Matches both /api/users and /api/users/123
  @GetMapping({"", "/{id}"})
  public ResponseEntity<?> getUsers(@PathVariable(required = false) Long id) {
      if (id == null) {
          return ResponseEntity.ok(userService.findAll());
      }
      return ResponseEntity.ok(userService.findById(id));
  }
  ```

  **With regex validation:**

  ```java
  // Only numeric IDs: /api/users/123
  @GetMapping("/{id:[0-9]+}")
  public User getUserById(@PathVariable Long id) {
      return userService.findById(id);
  }
  ```

</details>

<details>
  <summary>@RequestParam</summary>
  <br/>

  **@RequestParam** extracts query parameters from URL.

  **Basic usage:**

  ```java
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
      
      // GET /api/users?name=john
      @GetMapping
      public List<User> searchUsers(@RequestParam String name) {
          return userService.findByName(name);
      }
      
      // GET /api/users?name=john&age=25
      @GetMapping("/search")
      public List<User> searchUsers(
          @RequestParam String name,
          @RequestParam Integer age
      ) {
          return userService.findByNameAndAge(name, age);
      }
  }
  ```

  **Optional parameters:**

  ```java
  // With default value
  @GetMapping
  public List<User> getUsers(
      @RequestParam(required = false, defaultValue = "0") int page,
      @RequestParam(required = false, defaultValue = "20") int size
  ) {
      return userService.findAll(page, size);
  }
  
  // Using Optional
  @GetMapping("/search")
  public List<User> searchUsers(@RequestParam Optional<String> name) {
      return name.map(userService::findByName)
                 .orElse(userService.findAll());
  }
  ```

  **Parameter name mapping:**

  ```java
  // GET /api/users?user_name=john
  @GetMapping
  public List<User> searchUsers(
      @RequestParam("user_name") String username
  ) {
      return userService.findByUsername(username);
  }
  ```

  **Map all parameters:**

  ```java
  // GET /api/users?name=john&age=25&city=NYC
  @GetMapping
  public List<User> searchUsers(@RequestParam Map<String, String> params) {
      return userService.search(params);
  }
  ```

</details>

<details>
  <summary>@RequestHeader</summary>
  <br/>

  **@RequestHeader** extracts HTTP headers.

  **Basic usage:**

  ```java
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
      
      @GetMapping
      public List<User> getUsers(
          @RequestHeader("Authorization") String authToken
      ) {
          // Use auth token
          return userService.findAll();
      }
      
      @GetMapping("/info")
      public String getInfo(
          @RequestHeader("User-Agent") String userAgent,
          @RequestHeader("Accept-Language") String language
      ) {
          return "User-Agent: " + userAgent + ", Language: " + language;
      }
  }
  ```

  **Optional headers:**

  ```java
  @GetMapping
  public List<User> getUsers(
      @RequestHeader(value = "X-Custom-Header", required = false) String customHeader,
      @RequestHeader(value = "X-Page-Size", defaultValue = "20") int pageSize
  ) {
      return userService.findAll(pageSize);
  }
  ```

  **All headers:**

  ```java
  @GetMapping("/headers")
  public Map<String, String> getHeaders(@RequestHeader Map<String, String> headers) {
      return headers;
  }
  
  // Using HttpHeaders
  @GetMapping("/headers-object")
  public HttpHeaders getHeaders(@RequestHeader HttpHeaders headers) {
      return headers;
  }
  ```

</details>

<details>
  <summary>@CookieValue</summary>
  <br/>

  **@CookieValue** extracts cookie values.

  **Basic usage:**

  ```java
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
      
      @GetMapping
      public List<User> getUsers(
          @CookieValue("sessionId") String sessionId
      ) {
          // Use session ID
          return userService.findAll();
      }
  }
  ```

  **Optional cookie:**

  ```java
  @GetMapping
  public List<User> getUsers(
      @CookieValue(value = "sessionId", required = false) String sessionId,
      @CookieValue(value = "theme", defaultValue = "light") String theme
  ) {
      return userService.findAll();
  }
  ```

</details>

<details>
  <summary>@ModelAttribute</summary>
  <br/>

  **@ModelAttribute** binds request parameters to object.

  **Basic usage:**

  ```java
  public class UserSearchCriteria {
      private String username;
      private String email;
      private Integer minAge;
      private Integer maxAge;
      
      // Getters and setters
  }
  
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
      
      // GET /api/users/search?username=john&email=john@example.com&minAge=18&maxAge=65
      @GetMapping("/search")
      public List<User> searchUsers(@ModelAttribute UserSearchCriteria criteria) {
          return userService.search(criteria);
      }
  }
  ```

  **Form submission:**

  ```java
  @Controller
  @RequestMapping("/users")
  public class UserController {
      
      @GetMapping("/new")
      public String showForm(Model model) {
          model.addAttribute("user", new User());
          return "users/form";
      }
      
      @PostMapping
      public String createUser(@ModelAttribute User user) {
          userService.create(user);
          return "redirect:/users";
      }
  }
  ```

</details>

<details>
  <summary>ResponseEntity</summary>
  <br/>

  **ResponseEntity** provides full control over HTTP response.

  **Basic usage:**

  ```java
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
      
      @GetMapping("/{id}")
      public ResponseEntity<User> getUser(@PathVariable Long id) {
          User user = userService.findById(id);
          
          if (user == null) {
              return ResponseEntity.notFound().build();  // 404
          }
          
          return ResponseEntity.ok(user);  // 200
      }
  }
  ```

  **With status codes:**

  ```java
  @PostMapping
  public ResponseEntity<User> createUser(@RequestBody User user) {
      User created = userService.create(user);
      
      URI location = ServletUriComponentsBuilder
          .fromCurrentRequest()
          .path("/{id}")
          .buildAndExpand(created.getId())
          .toUri();
      
      return ResponseEntity.created(location).body(created);  // 201
  }
  
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
      if (!userService.exists(id)) {
          return ResponseEntity.notFound().build();  // 404
      }
      
      userService.delete(id);
      return ResponseEntity.noContent().build();  // 204
  }
  ```

  **With headers:**

  ```java
  @GetMapping("/{id}")
  public ResponseEntity<User> getUser(@PathVariable Long id) {
      User user = userService.findById(id);
      
      HttpHeaders headers = new HttpHeaders();
      headers.add("X-Custom-Header", "CustomValue");
      headers.add("X-User-Id", id.toString());
      
      return ResponseEntity.ok()
          .headers(headers)
          .body(user);
  }
  ```

  **Common methods:**

  ```java
  // 200 OK
  ResponseEntity.ok(body)
  ResponseEntity.ok().build()
  
  // 201 Created
  ResponseEntity.created(location).body(body)
  
  // 204 No Content
  ResponseEntity.noContent().build()
  
  // 400 Bad Request
  ResponseEntity.badRequest().body(error)
  
  // 404 Not Found
  ResponseEntity.notFound().build()
  
  // 500 Internal Server Error
  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error)
  
  // Custom status
  ResponseEntity.status(HttpStatus.ACCEPTED).body(body)
  ```

</details>

<details>
  <summary>HttpServletRequest and HttpServletResponse</summary>
  <br/>

  **Direct access to servlet request and response.**

  **HttpServletRequest:**

  ```java
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
      
      @GetMapping
      public List<User> getUsers(HttpServletRequest request) {
          // Get request info
          String method = request.getMethod();
          String uri = request.getRequestURI();
          String remoteAddr = request.getRemoteAddr();
          
          // Get headers
          String userAgent = request.getHeader("User-Agent");
          
          // Get parameters
          String name = request.getParameter("name");
          
          // Get attributes
          Object attr = request.getAttribute("someAttribute");
          
          return userService.findAll();
      }
  }
  ```

  **HttpServletResponse:**

  ```java
  @GetMapping("/download")
  public void downloadFile(HttpServletResponse response) throws IOException {
      // Set headers
      response.setContentType("application/pdf");
      response.setHeader("Content-Disposition", "attachment; filename=file.pdf");
      
      // Write to response
      byte[] data = fileService.getFileData();
      response.getOutputStream().write(data);
      response.getOutputStream().flush();
  }
  ```

</details>

<details>
  <summary>Best Practices</summary>
  <br/>

  ```java
  // ✅ DO: Use @RequestBody for JSON/XML payloads
  @PostMapping
  public User createUser(@RequestBody User user) {
  }
  
  // ✅ DO: Use @PathVariable for resource identifiers
  @GetMapping("/{id}")
  public User getUser(@PathVariable Long id) {
  }
  
  // ✅ DO: Use @RequestParam for filtering/pagination
  @GetMapping
  public List<User> getUsers(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size
  ) {
  }
  
  // ✅ DO: Validate input with @Valid
  @PostMapping
  public User createUser(@Valid @RequestBody CreateUserRequest request) {
  }
  
  // ✅ DO: Use DTOs instead of entities
  @PostMapping
  public User createUser(@RequestBody CreateUserRequest request) {
      // Map DTO to entity
  }
  
  // ❌ DON'T: Expose entities directly
  @PostMapping
  public User createUser(@RequestBody User user) {  // Exposes all fields
  }
  
  // ✅ DO: Use ResponseEntity for flexible responses
  @GetMapping("/{id}")
  public ResponseEntity<User> getUser(@PathVariable Long id) {
      return userService.findById(id)
          .map(ResponseEntity::ok)
          .orElse(ResponseEntity.notFound().build());
  }
  
  // ✅ DO: Provide default values for optional parameters
  @GetMapping
  public List<User> getUsers(
      @RequestParam(defaultValue = "0") int page
  ) {
  }
  
  // ✅ DO: Use meaningful parameter names
  @GetMapping("/search")
  public List<User> searchUsers(
      @RequestParam String username,
      @RequestParam String email
  ) {
  }
  
  // ❌ DON'T: Use unclear names
  @GetMapping("/search")
  public List<User> searchUsers(
      @RequestParam String s1,
      @RequestParam String s2
  ) {
  }
  
  // ✅ DO: Use Optional for truly optional parameters
  @GetMapping("/search")
  public List<User> searchUsers(@RequestParam Optional<String> name) {
      return name.map(userService::findByName)
                 .orElse(userService.findAll());
  }
  
  // ✅ DO: Return appropriate status codes
  @PostMapping
  public ResponseEntity<User> createUser(@RequestBody User user) {
      User created = userService.create(user);
      return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }
  
  // ✅ DO: Set Location header for created resources
  @PostMapping
  public ResponseEntity<User> createUser(@RequestBody User user) {
      User created = userService.create(user);
      URI location = ServletUriComponentsBuilder
          .fromCurrentRequest()
          .path("/{id}")
          .buildAndExpand(created.getId())
          .toUri();
      return ResponseEntity.created(location).body(created);
  }
  
  // ✅ DO: Use @RequestHeader for authentication tokens
  @GetMapping
  public List<User> getUsers(
      @RequestHeader("Authorization") String token
  ) {
  }
  
  // ✅ DO: Handle missing parameters gracefully
  @GetMapping
  public List<User> getUsers(
      @RequestParam(required = false) String name
  ) {
      if (name == null) {
          return userService.findAll();
      }
      return userService.findByName(name);
  }
  ```

  **Summary:**
  + Use **@RequestBody** for JSON/XML request payloads
  + Use **@ResponseBody** to serialize response (or @RestController)
  + Use **@PathVariable** for resource identifiers in URL path
  + Use **@RequestParam** for query parameters (filtering, pagination)
  + Use **@RequestHeader** for HTTP headers (authentication, etc.)
  + Use **@CookieValue** for cookie values
  + Use **@ModelAttribute** for form data binding
  + Use **ResponseEntity** for full control over HTTP response
  + Use **@Valid** for input validation
  + Use **DTOs** instead of exposing entities
  + Provide **default values** for optional parameters
  + Return **appropriate HTTP status codes**
  + Set **Location header** for created resources
  + Use **Optional** for truly optional parameters
  + Handle **missing parameters** gracefully

</details>

## Exception Handling

<details>
  <summary>@ExceptionHandler</summary>
  <br/>

  **@ExceptionHandler** handles exceptions thrown by controller methods.

  **Basic usage:**

  ```java
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
      
      @GetMapping("/{id}")
      public User getUser(@PathVariable Long id) {
          return userService.findById(id);
          // Throws UserNotFoundException if not found
      }
      
      @ExceptionHandler(UserNotFoundException.class)
      public ResponseEntity<String> handleUserNotFound(UserNotFoundException ex) {
          return ResponseEntity
              .status(HttpStatus.NOT_FOUND)
              .body(ex.getMessage());
      }
  }
  ```

  **Multiple exceptions:**

  ```java
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
      
      @ExceptionHandler({UserNotFoundException.class, OrderNotFoundException.class})
      public ResponseEntity<String> handleNotFound(RuntimeException ex) {
          return ResponseEntity
              .status(HttpStatus.NOT_FOUND)
              .body(ex.getMessage());
      }
      
      @ExceptionHandler(IllegalArgumentException.class)
      public ResponseEntity<String> handleBadRequest(IllegalArgumentException ex) {
          return ResponseEntity
              .status(HttpStatus.BAD_REQUEST)
              .body(ex.getMessage());
      }
  }
  ```

  **Scope:**
  + Handles exceptions only within the same controller
  + Not shared across controllers
  + Use @ControllerAdvice for global handling

</details>

<details>
  <summary>@ControllerAdvice</summary>
  <br/>

  **@ControllerAdvice** provides global exception handling across all controllers.

  **Basic usage:**

  ```java
  @RestControllerAdvice
  public class GlobalExceptionHandler {
      
      @ExceptionHandler(UserNotFoundException.class)
      public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
          ErrorResponse error = new ErrorResponse(
              HttpStatus.NOT_FOUND.value(),
              ex.getMessage(),
              LocalDateTime.now()
          );
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
      }
      
      @ExceptionHandler(IllegalArgumentException.class)
      public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException ex) {
          ErrorResponse error = new ErrorResponse(
              HttpStatus.BAD_REQUEST.value(),
              ex.getMessage(),
              LocalDateTime.now()
          );
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
      }
      
      @ExceptionHandler(Exception.class)
      public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
          ErrorResponse error = new ErrorResponse(
              HttpStatus.INTERNAL_SERVER_ERROR.value(),
              "An unexpected error occurred",
              LocalDateTime.now()
          );
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
      }
  }
  ```

  **Error response DTO:**

  ```java
  public class ErrorResponse {
      private int status;
      private String message;
      private LocalDateTime timestamp;
      
      // Constructor, getters, setters
  }
  ```

</details>

<details>
  <summary>@RestControllerAdvice</summary>
  <br/>

  **@RestControllerAdvice = @ControllerAdvice + @ResponseBody**

  **Comparison:**

  ```java
  // Option 1: @RestControllerAdvice (preferred for REST APIs)
  @RestControllerAdvice
  public class GlobalExceptionHandler {
      
      @ExceptionHandler(UserNotFoundException.class)
      public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
      }
  }
  
  // Option 2: @ControllerAdvice + @ResponseBody
  @ControllerAdvice
  public class GlobalExceptionHandler {
      
      @ExceptionHandler(UserNotFoundException.class)
      @ResponseBody
      public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
      }
  }
  ```

  **When to use:**
  + **@RestControllerAdvice** - REST APIs (returns JSON/XML)
  + **@ControllerAdvice** - Traditional MVC (returns views)

</details>

<details>
  <summary>ResponseEntityExceptionHandler</summary>
  <br/>

  **ResponseEntityExceptionHandler** provides default handling for Spring MVC exceptions.

  **Basic usage:**

  ```java
  @RestControllerAdvice
  public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
      
      // Handle custom exceptions
      @ExceptionHandler(UserNotFoundException.class)
      public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
          ErrorResponse error = new ErrorResponse(
              HttpStatus.NOT_FOUND.value(),
              ex.getMessage(),
              LocalDateTime.now()
          );
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
      }
      
      // Override Spring MVC exception handling
      @Override
      protected ResponseEntity<Object> handleMethodArgumentNotValid(
          MethodArgumentNotValidException ex,
          HttpHeaders headers,
          HttpStatusCode status,
          WebRequest request
      ) {
          Map<String, String> errors = new HashMap<>();
          ex.getBindingResult().getFieldErrors().forEach(error -> 
              errors.put(error.getField(), error.getDefaultMessage())
          );
          
          ErrorResponse errorResponse = new ErrorResponse(
              HttpStatus.BAD_REQUEST.value(),
              "Validation failed",
              LocalDateTime.now(),
              errors
          );
          
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
      }
  }
  ```

  **Built-in exceptions handled:**
  + **MethodArgumentNotValidException** - @Valid validation fails
  + **HttpMessageNotReadableException** - Invalid JSON
  + **HttpRequestMethodNotSupportedException** - Wrong HTTP method
  + **MissingServletRequestParameterException** - Missing required parameter
  + **TypeMismatchException** - Type conversion fails
  + **HttpMediaTypeNotSupportedException** - Unsupported Content-Type

</details>

<details>
  <summary>Validation Exception Handling</summary>
  <br/>

  **Handle validation errors from @Valid.**

  **Entity with validation:**

  ```java
  public class CreateUserRequest {
      @NotBlank(message = "Username is required")
      @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
      private String username;
      
      @NotBlank(message = "Email is required")
      @Email(message = "Invalid email format")
      private String email;
      
      @NotBlank(message = "Password is required")
      @Size(min = 8, message = "Password must be at least 8 characters")
      private String password;
      
      // Getters and setters
  }
  ```

  **Controller:**

  ```java
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
      
      @PostMapping
      public User createUser(@Valid @RequestBody CreateUserRequest request) {
          return userService.create(request);
      }
  }
  ```

  **Exception handler:**

  ```java
  @RestControllerAdvice
  public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
      
      @Override
      protected ResponseEntity<Object> handleMethodArgumentNotValid(
          MethodArgumentNotValidException ex,
          HttpHeaders headers,
          HttpStatusCode status,
          WebRequest request
      ) {
          Map<String, String> errors = new HashMap<>();
          
          ex.getBindingResult().getFieldErrors().forEach(error -> 
              errors.put(error.getField(), error.getDefaultMessage())
          );
          
          ValidationErrorResponse response = new ValidationErrorResponse(
              HttpStatus.BAD_REQUEST.value(),
              "Validation failed",
              LocalDateTime.now(),
              errors
          );
          
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
      }
  }
  ```

  **Error response:**

  ```java
  public class ValidationErrorResponse {
      private int status;
      private String message;
      private LocalDateTime timestamp;
      private Map<String, String> errors;
      
      // Constructor, getters, setters
  }
  ```

  **Example response:**

  ```json
  {
    "status": 400,
    "message": "Validation failed",
    "timestamp": "2024-01-15T10:30:00",
    "errors": {
      "username": "Username is required",
      "email": "Invalid email format",
      "password": "Password must be at least 8 characters"
    }
  }
  ```

</details>

<details>
  <summary>Custom Exceptions</summary>
  <br/>

  **Create custom exceptions for business logic.**

  **Custom exception:**

  ```java
  public class UserNotFoundException extends RuntimeException {
      public UserNotFoundException(Long id) {
          super("User not found with id: " + id);
      }
      
      public UserNotFoundException(String message) {
          super(message);
      }
  }
  
  public class DuplicateUserException extends RuntimeException {
      public DuplicateUserException(String username) {
          super("User already exists with username: " + username);
      }
  }
  
  public class InvalidPasswordException extends RuntimeException {
      public InvalidPasswordException(String message) {
          super(message);
      }
  }
  ```

  **Service layer:**

  ```java
  @Service
  public class UserService {
      
      public User findById(Long id) {
          return userRepository.findById(id)
              .orElseThrow(() -> new UserNotFoundException(id));
      }
      
      public User create(CreateUserRequest request) {
          if (userRepository.existsByUsername(request.getUsername())) {
              throw new DuplicateUserException(request.getUsername());
          }
          
          // Create user
      }
  }
  ```

  **Exception handler:**

  ```java
  @RestControllerAdvice
  public class GlobalExceptionHandler {
      
      @ExceptionHandler(UserNotFoundException.class)
      public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
          ErrorResponse error = new ErrorResponse(
              HttpStatus.NOT_FOUND.value(),
              ex.getMessage(),
              LocalDateTime.now()
          );
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
      }
      
      @ExceptionHandler(DuplicateUserException.class)
      public ResponseEntity<ErrorResponse> handleDuplicateUser(DuplicateUserException ex) {
          ErrorResponse error = new ErrorResponse(
              HttpStatus.CONFLICT.value(),
              ex.getMessage(),
              LocalDateTime.now()
          );
          return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
      }
  }
  ```

</details>

<details>
  <summary>Complete Exception Handling Example</summary>
  <br/>

  **Global exception handler:**

  ```java
  @RestControllerAdvice
  @Slf4j
  public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
      
      // Custom business exceptions
      @ExceptionHandler(UserNotFoundException.class)
      public ResponseEntity<ErrorResponse> handleUserNotFound(
          UserNotFoundException ex,
          WebRequest request
      ) {
          log.error("User not found: {}", ex.getMessage());
          
          ErrorResponse error = ErrorResponse.builder()
              .status(HttpStatus.NOT_FOUND.value())
              .message(ex.getMessage())
              .timestamp(LocalDateTime.now())
              .path(request.getDescription(false))
              .build();
          
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
      }
      
      @ExceptionHandler(DuplicateUserException.class)
      public ResponseEntity<ErrorResponse> handleDuplicateUser(
          DuplicateUserException ex,
          WebRequest request
      ) {
          log.error("Duplicate user: {}", ex.getMessage());
          
          ErrorResponse error = ErrorResponse.builder()
              .status(HttpStatus.CONFLICT.value())
              .message(ex.getMessage())
              .timestamp(LocalDateTime.now())
              .path(request.getDescription(false))
              .build();
          
          return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
      }
      
      // Validation errors
      @Override
      protected ResponseEntity<Object> handleMethodArgumentNotValid(
          MethodArgumentNotValidException ex,
          HttpHeaders headers,
          HttpStatusCode status,
          WebRequest request
      ) {
          Map<String, String> errors = new HashMap<>();
          ex.getBindingResult().getFieldErrors().forEach(error -> 
              errors.put(error.getField(), error.getDefaultMessage())
          );
          
          ValidationErrorResponse response = ValidationErrorResponse.builder()
              .status(HttpStatus.BAD_REQUEST.value())
              .message("Validation failed")
              .timestamp(LocalDateTime.now())
              .path(request.getDescription(false))
              .errors(errors)
              .build();
          
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
      }
      
      // Generic exception
      @ExceptionHandler(Exception.class)
      public ResponseEntity<ErrorResponse> handleGenericException(
          Exception ex,
          WebRequest request
      ) {
          log.error("Unexpected error: ", ex);
          
          ErrorResponse error = ErrorResponse.builder()
              .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
              .message("An unexpected error occurred")
              .timestamp(LocalDateTime.now())
              .path(request.getDescription(false))
              .build();
          
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
      }
  }
  ```

  **Error response classes:**

  ```java
  @Data
  @Builder
  public class ErrorResponse {
      private int status;
      private String message;
      private LocalDateTime timestamp;
      private String path;
  }
  
  @Data
  @Builder
  public class ValidationErrorResponse {
      private int status;
      private String message;
      private LocalDateTime timestamp;
      private String path;
      private Map<String, String> errors;
  }
  ```

</details>

<details>
  <summary>Best Practices</summary>
  <br/>

  ```java
  // ✅ DO: Use @RestControllerAdvice for global exception handling
  @RestControllerAdvice
  public class GlobalExceptionHandler {
  }
  
  // ✅ DO: Create custom exceptions for business logic
  public class UserNotFoundException extends RuntimeException {
      public UserNotFoundException(Long id) {
          super("User not found with id: " + id);
      }
  }
  
  // ✅ DO: Return appropriate HTTP status codes
  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }
  
  // ✅ DO: Include timestamp and path in error response
  ErrorResponse error = ErrorResponse.builder()
      .status(HttpStatus.NOT_FOUND.value())
      .message(ex.getMessage())
      .timestamp(LocalDateTime.now())
      .path(request.getDescription(false))
      .build();
  
  // ✅ DO: Log exceptions
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
      log.error("Unexpected error: ", ex);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }
  
  // ✅ DO: Handle validation errors properly
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request
  ) {
      Map<String, String> errors = new HashMap<>();
      ex.getBindingResult().getFieldErrors().forEach(error -> 
          errors.put(error.getField(), error.getDefaultMessage())
      );
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
  }
  
  // ✅ DO: Extend ResponseEntityExceptionHandler for Spring exceptions
  @RestControllerAdvice
  public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
  }
  
  // ❌ DON'T: Expose sensitive information in error messages
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception ex) {
      // BAD: Exposes stack trace
      return ResponseEntity.status(500).body(ex.getStackTrace());
  }
  
  // ✅ DO: Use generic message for unexpected errors
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception ex) {
      log.error("Error: ", ex);  // Log details
      return ResponseEntity.status(500).body("An unexpected error occurred");
  }
  
  // ✅ DO: Use specific exception handlers before generic ones
  @ExceptionHandler(UserNotFoundException.class)  // Specific
  public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
  }
  
  @ExceptionHandler(Exception.class)  // Generic (fallback)
  public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
  }
  
  // ✅ DO: Return consistent error response format
  public class ErrorResponse {
      private int status;
      private String message;
      private LocalDateTime timestamp;
      private String path;
  }
  
  // ✅ DO: Use @ExceptionHandler for controller-specific exceptions
  @RestController
  public class UserController {
      
      @ExceptionHandler(UserNotFoundException.class)
      public ResponseEntity<String> handleUserNotFound(UserNotFoundException ex) {
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
      }
  }
  
  // ✅ DO: Handle multiple exceptions in one handler
  @ExceptionHandler({UserNotFoundException.class, OrderNotFoundException.class})
  public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }
  ```

  **Summary:**
  + Use **@RestControllerAdvice** for global exception handling
  + Use **@ExceptionHandler** for controller-specific exceptions
  + Extend **ResponseEntityExceptionHandler** for Spring MVC exceptions
  + Create **custom exceptions** for business logic
  + Return **appropriate HTTP status codes**
  + Include **timestamp and path** in error responses
  + **Log exceptions** for debugging
  + Handle **validation errors** properly
  + Use **generic messages** for unexpected errors (don't expose stack traces)
  + Return **consistent error response format**
  + Use **specific handlers** before generic ones
  + Don't **expose sensitive information** in error messages

</details>

## Interceptors

<details>
  <summary>What are Interceptors?</summary>
  <br/>

  **Interceptors** intercept HTTP requests before/after controller execution.

  **Use cases:**
  + Authentication and authorization
  + Logging and auditing
  + Request/response modification
  + Performance monitoring
  + Cross-cutting concerns

  **Execution flow:**

  ```
  Request → Filter → Interceptor.preHandle() → Controller → Interceptor.postHandle() → View → Interceptor.afterCompletion() → Response
  ```

  **Key points:**
  + Interceptors work at Spring MVC level
  + Have access to Handler (controller method)
  + Can access ModelAndView
  + Can prevent request from reaching controller

</details>

<details>
  <summary>HandlerInterceptor Interface</summary>
  <br/>

  **HandlerInterceptor** provides three callback methods.

  **Interface methods:**

  ```java
  public interface HandlerInterceptor {
      
      // Called before controller method execution
      // Return true to continue, false to stop
      default boolean preHandle(
          HttpServletRequest request,
          HttpServletResponse response,
          Object handler
      ) throws Exception {
          return true;
      }
      
      // Called after controller method execution, before view rendering
      // Only called if preHandle returns true
      default void postHandle(
          HttpServletRequest request,
          HttpServletResponse response,
          Object handler,
          ModelAndView modelAndView
      ) throws Exception {
      }
      
      // Called after view rendering (or after exception)
      // Always called if preHandle was called
      default void afterCompletion(
          HttpServletRequest request,
          HttpServletResponse response,
          Object handler,
          Exception ex
      ) throws Exception {
      }
  }
  ```

  **Method execution:**
  + **preHandle**: Before controller execution
  + **postHandle**: After controller, before view rendering
  + **afterCompletion**: After view rendering (cleanup)

</details>

<details>
  <summary>Basic Interceptor Example</summary>
  <br/>

  **Create interceptor:**

  ```java
  @Component
  @Slf4j
  public class LoggingInterceptor implements HandlerInterceptor {
      
      @Override
      public boolean preHandle(
          HttpServletRequest request,
          HttpServletResponse response,
          Object handler
      ) throws Exception {
          log.info("Request URL: {}", request.getRequestURL());
          log.info("HTTP Method: {}", request.getMethod());
          log.info("Handler: {}", handler);
          
          // Store start time for performance monitoring
          request.setAttribute("startTime", System.currentTimeMillis());
          
          return true;  // Continue to controller
      }
      
      @Override
      public void postHandle(
          HttpServletRequest request,
          HttpServletResponse response,
          Object handler,
          ModelAndView modelAndView
      ) throws Exception {
          log.info("Response Status: {}", response.getStatus());
      }
      
      @Override
      public void afterCompletion(
          HttpServletRequest request,
          HttpServletResponse response,
          Object handler,
          Exception ex
      ) throws Exception {
          long startTime = (Long) request.getAttribute("startTime");
          long endTime = System.currentTimeMillis();
          long executionTime = endTime - startTime;
          
          log.info("Request completed in {} ms", executionTime);
          
          if (ex != null) {
              log.error("Request failed with exception: ", ex);
          }
      }
  }
  ```

  **Register interceptor:**

  ```java
  @Configuration
  public class WebMvcConfig implements WebMvcConfigurer {
      
      @Autowired
      private LoggingInterceptor loggingInterceptor;
      
      @Override
      public void addInterceptors(InterceptorRegistry registry) {
          registry.addInterceptor(loggingInterceptor)
                  .addPathPatterns("/**")  // Apply to all paths
                  .excludePathPatterns("/health", "/actuator/**");  // Exclude paths
      }
  }
  ```

</details>

<details>
  <summary>Authentication Interceptor</summary>
  <br/>

  **Intercept requests to check authentication.**

  **Create interceptor:**

  ```java
  @Component
  @Slf4j
  public class AuthenticationInterceptor implements HandlerInterceptor {
      
      @Autowired
      private JwtTokenProvider tokenProvider;
      
      @Override
      public boolean preHandle(
          HttpServletRequest request,
          HttpServletResponse response,
          Object handler
      ) throws Exception {
          // Get token from header
          String token = request.getHeader("Authorization");
          
          if (token == null || !token.startsWith("Bearer ")) {
              log.warn("Missing or invalid Authorization header");
              response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
              response.getWriter().write("Unauthorized: Missing or invalid token");
              return false;  // Stop request
          }
          
          // Extract and validate token
          String jwt = token.substring(7);
          
          if (!tokenProvider.validateToken(jwt)) {
              log.warn("Invalid JWT token");
              response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
              response.getWriter().write("Unauthorized: Invalid token");
              return false;
          }
          
          // Extract user info and store in request
          String username = tokenProvider.getUsernameFromToken(jwt);
          request.setAttribute("username", username);
          
          log.info("User {} authenticated successfully", username);
          return true;  // Continue to controller
      }
  }
  ```

  **Register interceptor:**

  ```java
  @Configuration
  public class WebMvcConfig implements WebMvcConfigurer {
      
      @Autowired
      private AuthenticationInterceptor authenticationInterceptor;
      
      @Override
      public void addInterceptors(InterceptorRegistry registry) {
          registry.addInterceptor(authenticationInterceptor)
                  .addPathPatterns("/api/**")  // Protect API endpoints
                  .excludePathPatterns("/api/auth/**", "/api/public/**");  // Exclude auth endpoints
      }
  }
  ```

</details>

<details>
  <summary>Rate Limiting Interceptor</summary>
  <br/>

  **Limit request rate per user/IP.**

  **Create interceptor:**

  ```java
  @Component
  @Slf4j
  public class RateLimitInterceptor implements HandlerInterceptor {
      
      private final Map<String, List<Long>> requestCounts = new ConcurrentHashMap<>();
      private static final int MAX_REQUESTS = 100;
      private static final long TIME_WINDOW = 60000; // 1 minute
      
      @Override
      public boolean preHandle(
          HttpServletRequest request,
          HttpServletResponse response,
          Object handler
      ) throws Exception {
          String clientId = getClientId(request);
          long currentTime = System.currentTimeMillis();
          
          // Get or create request list for client
          List<Long> requests = requestCounts.computeIfAbsent(
              clientId, 
              k -> new ArrayList<>()
          );
          
          // Remove old requests outside time window
          requests.removeIf(time -> currentTime - time > TIME_WINDOW);
          
          // Check rate limit
          if (requests.size() >= MAX_REQUESTS) {
              log.warn("Rate limit exceeded for client: {}", clientId);
              response.setStatus(HttpServletResponse.SC_TOO_MANY_REQUESTS);
              response.getWriter().write("Rate limit exceeded. Try again later.");
              return false;
          }
          
          // Add current request
          requests.add(currentTime);
          
          return true;
      }
      
      private String getClientId(HttpServletRequest request) {
          // Use user ID if authenticated, otherwise use IP
          String username = (String) request.getAttribute("username");
          return username != null ? username : request.getRemoteAddr();
      }
  }
  ```

</details>

<details>
  <summary>Performance Monitoring Interceptor</summary>
  <br/>

  **Monitor request execution time.**

  **Create interceptor:**

  ```java
  @Component
  @Slf4j
  public class PerformanceInterceptor implements HandlerInterceptor {
      
      private static final String START_TIME = "startTime";
      private static final long SLOW_REQUEST_THRESHOLD = 1000; // 1 second
      
      @Override
      public boolean preHandle(
          HttpServletRequest request,
          HttpServletResponse response,
          Object handler
      ) throws Exception {
          request.setAttribute(START_TIME, System.currentTimeMillis());
          return true;
      }
      
      @Override
      public void afterCompletion(
          HttpServletRequest request,
          HttpServletResponse response,
          Object handler,
          Exception ex
      ) throws Exception {
          long startTime = (Long) request.getAttribute(START_TIME);
          long endTime = System.currentTimeMillis();
          long executionTime = endTime - startTime;
          
          String method = request.getMethod();
          String uri = request.getRequestURI();
          int status = response.getStatus();
          
          if (executionTime > SLOW_REQUEST_THRESHOLD) {
              log.warn("SLOW REQUEST: {} {} - {} ms - Status: {}", 
                  method, uri, executionTime, status);
          } else {
              log.info("{} {} - {} ms - Status: {}", 
                  method, uri, executionTime, status);
          }
      }
  }
  ```

</details>

<details>
  <summary>Request/Response Modification</summary>
  <br/>

  **Modify request headers or response.**

  **Add custom headers:**

  ```java
  @Component
  public class CustomHeaderInterceptor implements HandlerInterceptor {
      
      @Override
      public boolean preHandle(
          HttpServletRequest request,
          HttpServletResponse response,
          Object handler
      ) throws Exception {
          // Add custom request attribute
          request.setAttribute("requestId", UUID.randomUUID().toString());
          return true;
      }
      
      @Override
      public void postHandle(
          HttpServletRequest request,
          HttpServletResponse response,
          Object handler,
          ModelAndView modelAndView
      ) throws Exception {
          // Add custom response headers
          String requestId = (String) request.getAttribute("requestId");
          response.setHeader("X-Request-Id", requestId);
          response.setHeader("X-Powered-By", "Spring Boot");
          response.setHeader("X-Response-Time", String.valueOf(System.currentTimeMillis()));
      }
  }
  ```

</details>

<details>
  <summary>Multiple Interceptors</summary>
  <br/>

  **Register multiple interceptors with order.**

  **Configuration:**

  ```java
  @Configuration
  public class WebMvcConfig implements WebMvcConfigurer {
      
      @Autowired
      private LoggingInterceptor loggingInterceptor;
      
      @Autowired
      private AuthenticationInterceptor authenticationInterceptor;
      
      @Autowired
      private RateLimitInterceptor rateLimitInterceptor;
      
      @Autowired
      private PerformanceInterceptor performanceInterceptor;
      
      @Override
      public void addInterceptors(InterceptorRegistry registry) {
          // Order matters! Executed in registration order
          
          // 1. Logging (first)
          registry.addInterceptor(loggingInterceptor)
                  .addPathPatterns("/**")
                  .order(1);
          
          // 2. Authentication
          registry.addInterceptor(authenticationInterceptor)
                  .addPathPatterns("/api/**")
                  .excludePathPatterns("/api/auth/**", "/api/public/**")
                  .order(2);
          
          // 3. Rate limiting
          registry.addInterceptor(rateLimitInterceptor)
                  .addPathPatterns("/api/**")
                  .order(3);
          
          // 4. Performance monitoring (last)
          registry.addInterceptor(performanceInterceptor)
                  .addPathPatterns("/**")
                  .order(4);
      }
  }
  ```

  **Execution order:**

  ```
  Request
    ↓
  LoggingInterceptor.preHandle()
    ↓
  AuthenticationInterceptor.preHandle()
    ↓
  RateLimitInterceptor.preHandle()
    ↓
  PerformanceInterceptor.preHandle()
    ↓
  Controller
    ↓
  PerformanceInterceptor.postHandle()
    ↓
  RateLimitInterceptor.postHandle()
    ↓
  AuthenticationInterceptor.postHandle()
    ↓
  LoggingInterceptor.postHandle()
    ↓
  View Rendering
    ↓
  LoggingInterceptor.afterCompletion()
    ↓
  AuthenticationInterceptor.afterCompletion()
    ↓
  RateLimitInterceptor.afterCompletion()
    ↓
  PerformanceInterceptor.afterCompletion()
    ↓
  Response
  ```

</details>

<details>
  <summary>Path Patterns</summary>
  <br/>

  **Configure which paths interceptor applies to.**

  **Pattern examples:**

  ```java
  @Configuration
  public class WebMvcConfig implements WebMvcConfigurer {
      
      @Override
      public void addInterceptors(InterceptorRegistry registry) {
          
          // Apply to all paths
          registry.addInterceptor(loggingInterceptor)
                  .addPathPatterns("/**");
          
          // Apply to specific paths
          registry.addInterceptor(authInterceptor)
                  .addPathPatterns("/api/**")
                  .addPathPatterns("/admin/**");
          
          // Exclude specific paths
          registry.addInterceptor(authInterceptor)
                  .addPathPatterns("/api/**")
                  .excludePathPatterns("/api/auth/**")
                  .excludePathPatterns("/api/public/**")
                  .excludePathPatterns("/health", "/actuator/**");
          
          // Multiple patterns
          registry.addInterceptor(corsInterceptor)
                  .addPathPatterns("/api/users/**", "/api/orders/**")
                  .excludePathPatterns("/api/users/public/**");
          
          // Specific file extensions
          registry.addInterceptor(staticResourceInterceptor)
                  .addPathPatterns("/**/*.js", "/**/*.css", "/**/*.html");
      }
  }
  ```

  **Pattern syntax:**
  + `/**` - All paths
  + `/api/**` - All paths under /api
  + `/api/users/*` - One level under /api/users
  + `/api/users/**` - All levels under /api/users
  + `/**/*.js` - All .js files

</details>

<details>
  <summary>Accessing Handler Information</summary>
  <br/>

  **Get information about the controller method.**

  **Handler inspection:**

  ```java
  @Component
  @Slf4j
  public class HandlerInspectionInterceptor implements HandlerInterceptor {
      
      @Override
      public boolean preHandle(
          HttpServletRequest request,
          HttpServletResponse response,
          Object handler
      ) throws Exception {
          
          if (handler instanceof HandlerMethod) {
              HandlerMethod handlerMethod = (HandlerMethod) handler;
              
              // Get controller class
              Class<?> controllerClass = handlerMethod.getBeanType();
              log.info("Controller: {}", controllerClass.getSimpleName());
              
              // Get method
              Method method = handlerMethod.getMethod();
              log.info("Method: {}", method.getName());
              
              // Get method parameters
              MethodParameter[] parameters = handlerMethod.getMethodParameters();
              log.info("Parameters: {}", parameters.length);
              
              // Check for annotations
              if (method.isAnnotationPresent(RequiresPermission.class)) {
                  RequiresPermission annotation = method.getAnnotation(RequiresPermission.class);
                  String permission = annotation.value();
                  log.info("Required permission: {}", permission);
                  
                  // Check permission
                  if (!hasPermission(request, permission)) {
                      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                      return false;
                  }
              }
          }
          
          return true;
      }
      
      private boolean hasPermission(HttpServletRequest request, String permission) {
          // Check user permissions
          return true;
      }
  }
  ```

</details>

<details>
  <summary>Best Practices</summary>
  <br/>

  ```java
  // ✅ DO: Implement HandlerInterceptor
  @Component
  public class LoggingInterceptor implements HandlerInterceptor {
  }
  
  // ✅ DO: Return true to continue, false to stop
  @Override
  public boolean preHandle(...) {
      if (isValid) {
          return true;  // Continue to controller
      }
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return false;  // Stop request
  }
  
  // ✅ DO: Use preHandle for authentication/authorization
  @Override
  public boolean preHandle(...) {
      if (!isAuthenticated(request)) {
          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          return false;
      }
      return true;
  }
  
  // ✅ DO: Use afterCompletion for cleanup
  @Override
  public void afterCompletion(...) {
      // Clean up resources
      // Log completion
      // Always executed (even if exception occurs)
  }
  
  // ✅ DO: Store data in request attributes
  @Override
  public boolean preHandle(...) {
      request.setAttribute("startTime", System.currentTimeMillis());
      request.setAttribute("username", username);
      return true;
  }
  
  // ✅ DO: Use path patterns to apply selectively
  registry.addInterceptor(authInterceptor)
          .addPathPatterns("/api/**")
          .excludePathPatterns("/api/auth/**");
  
  // ✅ DO: Set execution order
  registry.addInterceptor(loggingInterceptor).order(1);
  registry.addInterceptor(authInterceptor).order(2);
  
  // ✅ DO: Handle exceptions gracefully
  @Override
  public boolean preHandle(...) {
      try {
          // Interceptor logic
          return true;
      } catch (Exception e) {
          log.error("Interceptor error: ", e);
          response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
          return false;
      }
  }
  
  // ✅ DO: Check handler type before casting
  if (handler instanceof HandlerMethod) {
      HandlerMethod handlerMethod = (HandlerMethod) handler;
      // Use handlerMethod
  }
  
  // ✅ DO: Use interceptors for cross-cutting concerns
  // - Authentication/Authorization
  // - Logging
  // - Performance monitoring
  // - Rate limiting
  // - Request/response modification
  
  // ❌ DON'T: Put business logic in interceptors
  @Override
  public boolean preHandle(...) {
      // BAD: Business logic belongs in service layer
      userService.updateLastLogin(userId);
      return true;
  }
  
  // ❌ DON'T: Modify request body in interceptor
  // Use Filter or @RequestBody with custom deserializer instead
  
  // ✅ DO: Log important information
  @Override
  public boolean preHandle(...) {
      log.info("Request: {} {}", request.getMethod(), request.getRequestURI());
      return true;
  }
  
  // ✅ DO: Set appropriate HTTP status codes
  if (!isAuthorized) {
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);  // 403
      return false;
  }
  ```

  **Summary:**
  + Interceptors work at **Spring MVC level**
  + Use **preHandle** for authentication, authorization, validation
  + Use **postHandle** for response modification (before view rendering)
  + Use **afterCompletion** for cleanup and logging
  + Return **true** to continue, **false** to stop request
  + Store data in **request attributes** to share between methods
  + Use **path patterns** to apply interceptors selectively
  + Set **execution order** with `.order()`
  + Check **handler type** before casting to HandlerMethod
  + Use for **cross-cutting concerns** (auth, logging, monitoring)
  + Don't put **business logic** in interceptors
  + Handle **exceptions gracefully**
  + Log **important information**
  + Set **appropriate HTTP status codes**

</details>

## Validation

<details>
  <summary>Bean Validation Basics</summary>
  <br/>

  **Bean Validation** (JSR 380) provides declarative validation using annotations.

  **Add dependency:**

  ```xml
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-validation</artifactId>
  </dependency>
  ```

  **Basic validation annotations:**

  ```java
  public class CreateUserRequest {
      
      @NotNull(message = "Username cannot be null")
      @NotBlank(message = "Username cannot be blank")
      @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
      private String username;
      
      @NotBlank(message = "Email is required")
      @Email(message = "Invalid email format")
      private String email;
      
      @NotBlank(message = "Password is required")
      @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
      @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).*$", 
               message = "Password must contain at least one digit, one lowercase and one uppercase letter")
      private String password;
      
      @Min(value = 18, message = "Age must be at least 18")
      @Max(value = 120, message = "Age must be less than 120")
      private Integer age;
      
      @Past(message = "Birth date must be in the past")
      private LocalDate birthDate;
      
      // Getters and setters
  }
  ```

</details>

<details>
  <summary>@Valid Annotation</summary>
  <br/>

  **@Valid** triggers validation on method parameters.

  **Controller with validation:**

  ```java
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
      
      @PostMapping
      public ResponseEntity<User> createUser(@Valid @RequestBody CreateUserRequest request) {
          User user = userService.create(request);
          return ResponseEntity.status(HttpStatus.CREATED).body(user);
      }
      
      @PutMapping("/{id}")
      public ResponseEntity<User> updateUser(
          @PathVariable Long id,
          @Valid @RequestBody UpdateUserRequest request
      ) {
          User user = userService.update(id, request);
          return ResponseEntity.ok(user);
      }
  }
  ```

  **What happens:**
  + Spring validates the request object before method execution
  + If validation fails, throws **MethodArgumentNotValidException**
  + Exception contains all validation errors
  + Method is not executed if validation fails

</details>

<details>
  <summary>Common Validation Annotations</summary>
  <br/>

  **Null/Empty checks:**

  ```java
  @NotNull        // Value cannot be null
  @NotEmpty       // String/Collection cannot be null or empty
  @NotBlank       // String cannot be null, empty, or whitespace only
  
  public class UserRequest {
      @NotNull
      private String username;  // Cannot be null
      
      @NotEmpty
      private String email;  // Cannot be null or ""
      
      @NotBlank
      private String password;  // Cannot be null, "", or "   "
  }
  ```

  **Size constraints:**

  ```java
  @Size(min = 3, max = 50)     // String/Collection size
  @Min(18)                      // Numeric minimum
  @Max(120)                     // Numeric maximum
  @DecimalMin("0.0")           // Decimal minimum
  @DecimalMax("100.0")         // Decimal maximum
  
  public class UserRequest {
      @Size(min = 3, max = 50)
      private String username;
      
      @Min(18)
      @Max(120)
      private Integer age;
      
      @DecimalMin("0.0")
      @DecimalMax("100.0")
      private BigDecimal discount;
  }
  ```

  **Pattern matching:**

  ```java
  @Pattern(regexp = "^[A-Za-z0-9]+$")  // Alphanumeric only
  @Email                                // Valid email format
  
  public class UserRequest {
      @Pattern(regexp = "^[A-Za-z0-9]+$", message = "Username must be alphanumeric")
      private String username;
      
      @Email(message = "Invalid email format")
      private String email;
      
      @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number")
      private String phoneNumber;
  }
  ```

  **Date/Time validation:**

  ```java
  @Past           // Date must be in the past
  @PastOrPresent  // Date must be in the past or present
  @Future         // Date must be in the future
  @FutureOrPresent // Date must be in the future or present
  
  public class UserRequest {
      @Past
      private LocalDate birthDate;
      
      @FutureOrPresent
      private LocalDate appointmentDate;
  }
  ```

  **Boolean validation:**

  ```java
  @AssertTrue   // Must be true
  @AssertFalse  // Must be false
  
  public class UserRequest {
      @AssertTrue(message = "You must accept terms and conditions")
      private Boolean acceptedTerms;
  }
  ```

  **Numeric validation:**

  ```java
  @Positive       // Must be positive (> 0)
  @PositiveOrZero // Must be positive or zero (>= 0)
  @Negative       // Must be negative (< 0)
  @NegativeOrZero // Must be negative or zero (<= 0)
  
  public class OrderRequest {
      @Positive
      private BigDecimal price;
      
      @PositiveOrZero
      private Integer quantity;
  }
  ```

</details>

<details>
  <summary>Nested Object Validation</summary>
  <br/>

  **@Valid** on nested objects triggers cascading validation.

  **Nested objects:**

  ```java
  public class CreateOrderRequest {
      
      @NotNull
      @Valid  // Validates Address object
      private Address shippingAddress;
      
      @NotNull
      @Valid  // Validates each OrderItem in the list
      @Size(min = 1, message = "Order must have at least one item")
      private List<OrderItem> items;
      
      // Getters and setters
  }
  
  public class Address {
      @NotBlank
      @Size(max = 100)
      private String street;
      
      @NotBlank
      @Size(max = 50)
      private String city;
      
      @NotBlank
      @Pattern(regexp = "^\\d{5}$", message = "Invalid zip code")
      private String zipCode;
      
      // Getters and setters
  }
  
  public class OrderItem {
      @NotNull
      private Long productId;
      
      @Positive
      private Integer quantity;
      
      @Positive
      private BigDecimal price;
      
      // Getters and setters
  }
  ```

  **Controller:**

  ```java
  @RestController
  @RequestMapping("/api/orders")
  public class OrderController {
      
      @PostMapping
      public ResponseEntity<Order> createOrder(@Valid @RequestBody CreateOrderRequest request) {
          // All nested objects are validated
          Order order = orderService.create(request);
          return ResponseEntity.status(HttpStatus.CREATED).body(order);
      }
  }
  ```

</details>

<details>
  <summary>Custom Validators</summary>
  <br/>

  **Create custom validation annotation.**

  **Step 1: Create annotation:**

  ```java
  @Target({ElementType.FIELD, ElementType.PARAMETER})
  @Retention(RetentionPolicy.RUNTIME)
  @Constraint(validatedBy = UniqueUsernameValidator.class)
  @Documented
  public @interface UniqueUsername {
      
      String message() default "Username already exists";
      
      Class<?>[] groups() default {};
      
      Class<? extends Payload>[] payload() default {};
  }
  ```

  **Step 2: Create validator:**

  ```java
  @Component
  public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {
      
      @Autowired
      private UserRepository userRepository;
      
      @Override
      public void initialize(UniqueUsername constraintAnnotation) {
          // Initialization logic if needed
      }
      
      @Override
      public boolean isValid(String username, ConstraintValidatorContext context) {
          if (username == null) {
              return true;  // Let @NotNull handle null values
          }
          
          return !userRepository.existsByUsername(username);
      }
  }
  ```

  **Step 3: Use annotation:**

  ```java
  public class CreateUserRequest {
      
      @NotBlank
      @Size(min = 3, max = 50)
      @UniqueUsername  // Custom validator
      private String username;
      
      @NotBlank
      @Email
      private String email;
      
      // Getters and setters
  }
  ```

</details>

<details>
  <summary>Class-Level Validation</summary>
  <br/>

  **Validate multiple fields together.**

  **Create annotation:**

  ```java
  @Target(ElementType.TYPE)
  @Retention(RetentionPolicy.RUNTIME)
  @Constraint(validatedBy = PasswordMatchesValidator.class)
  @Documented
  public @interface PasswordMatches {
      
      String message() default "Passwords do not match";
      
      Class<?>[] groups() default {};
      
      Class<? extends Payload>[] payload() default {};
  }
  ```

  **Create validator:**

  ```java
  public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {
      
      @Override
      public boolean isValid(Object obj, ConstraintValidatorContext context) {
          if (obj instanceof CreateUserRequest) {
              CreateUserRequest request = (CreateUserRequest) obj;
              return request.getPassword() != null && 
                     request.getPassword().equals(request.getConfirmPassword());
          }
          return false;
      }
  }
  ```

  **Use annotation:**

  ```java
  @PasswordMatches  // Class-level validation
  public class CreateUserRequest {
      
      @NotBlank
      @Size(min = 8)
      private String password;
      
      @NotBlank
      private String confirmPassword;
      
      // Getters and setters
  }
  ```

</details>

<details>
  <summary>Validation Groups</summary>
  <br/>

  **Use different validation rules for different scenarios.**

  **Define groups:**

  ```java
  public interface CreateValidation {}
  public interface UpdateValidation {}
  ```

  **Apply groups:**

  ```java
  public class UserRequest {
      
      @Null(groups = CreateValidation.class, message = "ID must be null for creation")
      @NotNull(groups = UpdateValidation.class, message = "ID is required for update")
      private Long id;
      
      @NotBlank(groups = {CreateValidation.class, UpdateValidation.class})
      @Size(min = 3, max = 50)
      private String username;
      
      @NotBlank(groups = CreateValidation.class, message = "Password is required")
      @Size(min = 8)
      private String password;
      
      // Getters and setters
  }
  ```

  **Controller:**

  ```java
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
      
      @PostMapping
      public ResponseEntity<User> createUser(
          @Validated(CreateValidation.class) @RequestBody UserRequest request
      ) {
          User user = userService.create(request);
          return ResponseEntity.status(HttpStatus.CREATED).body(user);
      }
      
      @PutMapping("/{id}")
      public ResponseEntity<User> updateUser(
          @PathVariable Long id,
          @Validated(UpdateValidation.class) @RequestBody UserRequest request
      ) {
          User user = userService.update(id, request);
          return ResponseEntity.ok(user);
      }
  }
  ```

</details>

<details>
  <summary>Programmatic Validation</summary>
  <br/>

  **Validate objects programmatically using Validator.**

  **Service with validation:**

  ```java
  @Service
  public class UserService {
      
      @Autowired
      private Validator validator;
      
      @Autowired
      private UserRepository userRepository;
      
      public User create(CreateUserRequest request) {
          // Programmatic validation
          Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(request);
          
          if (!violations.isEmpty()) {
              StringBuilder sb = new StringBuilder();
              for (ConstraintViolation<CreateUserRequest> violation : violations) {
                  sb.append(violation.getMessage()).append("\n");
              }
              throw new ValidationException(sb.toString());
          }
          
          // Create user
          User user = new User();
          user.setUsername(request.getUsername());
          user.setEmail(request.getEmail());
          return userRepository.save(user);
      }
  }
  ```

  **Validate specific property:**

  ```java
  @Service
  public class UserService {
      
      @Autowired
      private Validator validator;
      
      public void updateUsername(Long userId, String newUsername) {
          // Validate only username property
          Set<ConstraintViolation<User>> violations = 
              validator.validateValue(User.class, "username", newUsername);
          
          if (!violations.isEmpty()) {
              throw new ValidationException("Invalid username");
          }
          
          // Update username
      }
  }
  ```

</details>

<details>
  <summary>Handling Validation Errors</summary>
  <br/>

  **Handle validation errors in exception handler.**

  **Exception handler:**

  ```java
  @RestControllerAdvice
  public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
      
      @Override
      protected ResponseEntity<Object> handleMethodArgumentNotValid(
          MethodArgumentNotValidException ex,
          HttpHeaders headers,
          HttpStatusCode status,
          WebRequest request
      ) {
          Map<String, String> errors = new HashMap<>();
          
          // Field errors
          ex.getBindingResult().getFieldErrors().forEach(error -> 
              errors.put(error.getField(), error.getDefaultMessage())
          );
          
          // Global errors (class-level validation)
          ex.getBindingResult().getGlobalErrors().forEach(error -> 
              errors.put(error.getObjectName(), error.getDefaultMessage())
          );
          
          ValidationErrorResponse response = ValidationErrorResponse.builder()
              .status(HttpStatus.BAD_REQUEST.value())
              .message("Validation failed")
              .timestamp(LocalDateTime.now())
              .errors(errors)
              .build();
          
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
      }
  }
  ```

  **Error response:**

  ```java
  @Data
  @Builder
  public class ValidationErrorResponse {
      private int status;
      private String message;
      private LocalDateTime timestamp;
      private Map<String, String> errors;
  }
  ```

  **Example response:**

  ```json
  {
    "status": 400,
    "message": "Validation failed",
    "timestamp": "2024-01-15T10:30:00",
    "errors": {
      "username": "Username must be between 3 and 50 characters",
      "email": "Invalid email format",
      "password": "Password must be at least 8 characters"
    }
  }
  ```

</details>

<details>
  <summary>Best Practices</summary>
  <br/>

  ```java
  // ✅ DO: Use @Valid to trigger validation
  @PostMapping
  public User createUser(@Valid @RequestBody CreateUserRequest request) {
  }
  
  // ✅ DO: Use DTOs for validation
  public class CreateUserRequest {
      @NotBlank
      @Size(min = 3, max = 50)
      private String username;
  }
  
  // ❌ DON'T: Validate entities directly
  @PostMapping
  public User createUser(@Valid @RequestBody User user) {  // Exposes entity
  }
  
  // ✅ DO: Provide meaningful error messages
  @NotBlank(message = "Username is required")
  @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
  private String username;
  
  // ❌ DON'T: Use default messages
  @NotBlank
  @Size(min = 3, max = 50)
  private String username;
  
  // ✅ DO: Use @NotBlank for strings (checks null, empty, and whitespace)
  @NotBlank
  private String username;
  
  // ❌ DON'T: Use @NotNull for strings
  @NotNull  // Allows empty strings
  private String username;
  
  // ✅ DO: Use @Valid for nested objects
  @Valid
  private Address address;
  
  // ✅ DO: Create custom validators for complex validation
  @UniqueUsername
  private String username;
  
  // ✅ DO: Use validation groups for different scenarios
  @Validated(CreateValidation.class)
  @RequestBody UserRequest request
  
  // ✅ DO: Use class-level validation for multi-field validation
  @PasswordMatches
  public class CreateUserRequest {
      private String password;
      private String confirmPassword;
  }
  
  // ✅ DO: Handle validation errors globally
  @RestControllerAdvice
  public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
      @Override
      protected ResponseEntity<Object> handleMethodArgumentNotValid(...) {
      }
  }
  
  // ✅ DO: Return structured error response
  {
    "status": 400,
    "message": "Validation failed",
    "errors": {
      "username": "Username is required",
      "email": "Invalid email format"
    }
  }
  
  // ✅ DO: Validate at controller layer
  @PostMapping
  public User createUser(@Valid @RequestBody CreateUserRequest request) {
  }
  
  // ✅ DO: Use programmatic validation when needed
  Set<ConstraintViolation<User>> violations = validator.validate(user);
  
  // ✅ DO: Combine multiple validation annotations
  @NotBlank
  @Size(min = 3, max = 50)
  @Pattern(regexp = "^[A-Za-z0-9]+$")
  private String username;
  ```

  **Summary:**
  + Use **@Valid** to trigger validation on request objects
  + Use **DTOs** for validation (not entities)
  + Provide **meaningful error messages**
  + Use **@NotBlank** for strings (not @NotNull)
  + Use **@Valid** for nested object validation
  + Create **custom validators** for complex validation logic
  + Use **validation groups** for different scenarios
  + Use **class-level validation** for multi-field validation
  + Handle validation errors **globally** with @RestControllerAdvice
  + Return **structured error responses**
  + Validate at **controller layer**
  + Use **programmatic validation** when needed
  + **Combine multiple annotations** for comprehensive validation

</details>

## Filters vs Interceptors

<details>
  <summary>What are Filters?</summary>
  <br/>

  **Filters** are part of Java Servlet specification, work at servlet container level.

  **Key characteristics:**
  + Part of Java EE (javax.servlet.Filter)
  + Work at servlet container level (before Spring MVC)
  + Can modify request/response
  + Applied to all requests (including static resources)
  + No access to Spring context by default

  **Basic filter:**

  ```java
  @Component
  public class LoggingFilter implements Filter {
      
      @Override
      public void init(FilterConfig filterConfig) throws ServletException {
          // Initialization logic
      }
      
      @Override
      public void doFilter(
          ServletRequest request,
          ServletResponse response,
          FilterChain chain
      ) throws IOException, ServletException {
          HttpServletRequest httpRequest = (HttpServletRequest) request;
          
          System.out.println("Request: " + httpRequest.getRequestURI());
          
          // Continue filter chain
          chain.doFilter(request, response);
          
          System.out.println("Response completed");
      }
      
      @Override
      public void destroy() {
          // Cleanup logic
      }
  }
  ```

</details>

<details>
  <summary>What are Interceptors?</summary>
  <br/>

  **Interceptors** are Spring MVC components, work at Spring MVC level.

  **Key characteristics:**
  + Part of Spring MVC (HandlerInterceptor)
  + Work at Spring MVC level (after DispatcherServlet)
  + Have access to Handler (controller method)
  + Can access Spring beans
  + Applied only to controller requests

  **Basic interceptor:**

  ```java
  @Component
  public class LoggingInterceptor implements HandlerInterceptor {
      
      @Override
      public boolean preHandle(
          HttpServletRequest request,
          HttpServletResponse response,
          Object handler
      ) throws Exception {
          System.out.println("Before controller: " + request.getRequestURI());
          return true;
      }
      
      @Override
      public void postHandle(
          HttpServletRequest request,
          HttpServletResponse response,
          Object handler,
          ModelAndView modelAndView
      ) throws Exception {
          System.out.println("After controller, before view");
      }
      
      @Override
      public void afterCompletion(
          HttpServletRequest request,
          HttpServletResponse response,
          Object handler,
          Exception ex
      ) throws Exception {
          System.out.println("After view rendering");
      }
  }
  ```

</details>

<details>
  <summary>Key Differences</summary>
  <br/>

  **Comparison table:**

  | Feature | Filter | Interceptor |
  |---------|--------|-------------|
  | **Specification** | Java EE (Servlet) | Spring MVC |
  | **Level** | Servlet container | Spring MVC |
  | **Execution** | Before DispatcherServlet | After DispatcherServlet |
  | **Scope** | All requests (including static) | Only controller requests |
  | **Access to Handler** | ❌ No | ✅ Yes |
  | **Access to ModelAndView** | ❌ No | ✅ Yes |
  | **Spring Beans** | ⚠️ Limited | ✅ Full access |
  | **Exception Handling** | try-catch | @ExceptionHandler |
  | **Methods** | doFilter() | preHandle(), postHandle(), afterCompletion() |
  | **Order Control** | @Order or FilterRegistrationBean | InterceptorRegistry.order() |

  **Execution order:**

  ```
  Request
    ↓
  Filter 1
    ↓
  Filter 2
    ↓
  DispatcherServlet
    ↓
  Interceptor 1 (preHandle)
    ↓
  Interceptor 2 (preHandle)
    ↓
  Controller
    ↓
  Interceptor 2 (postHandle)
    ↓
  Interceptor 1 (postHandle)
    ↓
  View Rendering
    ↓
  Interceptor 1 (afterCompletion)
    ↓
  Interceptor 2 (afterCompletion)
    ↓
  Filter 2
    ↓
  Filter 1
    ↓
  Response
  ```

</details>

<details>
  <summary>Filter Example</summary>
  <br/>

  **Request/Response logging filter:**

  ```java
  @Component
  @Order(1)
  @Slf4j
  public class RequestResponseLoggingFilter implements Filter {
      
      @Override
      public void doFilter(
          ServletRequest request,
          ServletResponse response,
          FilterChain chain
      ) throws IOException, ServletException {
          HttpServletRequest httpRequest = (HttpServletRequest) request;
          HttpServletResponse httpResponse = (HttpServletResponse) response;
          
          // Log request
          log.info("Request: {} {}", httpRequest.getMethod(), httpRequest.getRequestURI());
          log.info("Remote Address: {}", httpRequest.getRemoteAddr());
          
          // Wrap response to capture status
          ContentCachingResponseWrapper responseWrapper = 
              new ContentCachingResponseWrapper(httpResponse);
          
          long startTime = System.currentTimeMillis();
          
          // Continue filter chain
          chain.doFilter(request, responseWrapper);
          
          long duration = System.currentTimeMillis() - startTime;
          
          // Log response
          log.info("Response: {} - {} ms", responseWrapper.getStatus(), duration);
          
          // Copy response body
          responseWrapper.copyBodyToResponse();
      }
  }
  ```

  **CORS filter:**

  ```java
  @Component
  @Order(Ordered.HIGHEST_PRECEDENCE)
  public class CorsFilter implements Filter {
      
      @Override
      public void doFilter(
          ServletRequest request,
          ServletResponse response,
          FilterChain chain
      ) throws IOException, ServletException {
          HttpServletResponse httpResponse = (HttpServletResponse) response;
          HttpServletRequest httpRequest = (HttpServletRequest) request;
          
          // Set CORS headers
          httpResponse.setHeader("Access-Control-Allow-Origin", "*");
          httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
          httpResponse.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
          httpResponse.setHeader("Access-Control-Max-Age", "3600");
          
          // Handle preflight request
          if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
              httpResponse.setStatus(HttpServletResponse.SC_OK);
              return;
          }
          
          chain.doFilter(request, response);
      }
  }
  ```

</details>

<details>
  <summary>Interceptor Example</summary>
  <br/>

  **Authentication interceptor:**

  ```java
  @Component
  @Slf4j
  public class AuthenticationInterceptor implements HandlerInterceptor {
      
      @Autowired
      private JwtTokenProvider tokenProvider;
      
      @Autowired
      private UserService userService;
      
      @Override
      public boolean preHandle(
          HttpServletRequest request,
          HttpServletResponse response,
          Object handler
      ) throws Exception {
          // Check if handler is a controller method
          if (!(handler instanceof HandlerMethod)) {
              return true;
          }
          
          HandlerMethod handlerMethod = (HandlerMethod) handler;
          
          // Check if method requires authentication
          if (!handlerMethod.hasMethodAnnotation(RequiresAuth.class)) {
              return true;
          }
          
          // Get token from header
          String token = request.getHeader("Authorization");
          
          if (token == null || !token.startsWith("Bearer ")) {
              response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
              return false;
          }
          
          // Validate token
          String jwt = token.substring(7);
          if (!tokenProvider.validateToken(jwt)) {
              response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
              return false;
          }
          
          // Load user and store in request
          String username = tokenProvider.getUsernameFromToken(jwt);
          User user = userService.findByUsername(username);
          request.setAttribute("currentUser", user);
          
          return true;
      }
  }
  ```

</details>

<details>
  <summary>When to Use Filters</summary>
  <br/>

  **Use Filters for:**

  **1. Request/Response modification:**

  ```java
  @Component
  public class CompressionFilter implements Filter {
      
      @Override
      public void doFilter(
          ServletRequest request,
          ServletResponse response,
          FilterChain chain
      ) throws IOException, ServletException {
          HttpServletResponse httpResponse = (HttpServletResponse) response;
          
          // Wrap response for compression
          GZIPResponseWrapper wrappedResponse = new GZIPResponseWrapper(httpResponse);
          
          chain.doFilter(request, wrappedResponse);
          
          wrappedResponse.finish();
      }
  }
  ```

  **2. Character encoding:**

  ```java
  @Component
  public class CharacterEncodingFilter implements Filter {
      
      @Override
      public void doFilter(
          ServletRequest request,
          ServletResponse response,
          FilterChain chain
      ) throws IOException, ServletException {
          request.setCharacterEncoding("UTF-8");
          response.setCharacterEncoding("UTF-8");
          
          chain.doFilter(request, response);
      }
  }
  ```

  **3. CORS handling:**

  ```java
  @Component
  public class CorsFilter implements Filter {
      
      @Override
      public void doFilter(
          ServletRequest request,
          ServletResponse response,
          FilterChain chain
      ) throws IOException, ServletException {
          HttpServletResponse httpResponse = (HttpServletResponse) response;
          
          httpResponse.setHeader("Access-Control-Allow-Origin", "*");
          httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
          
          chain.doFilter(request, response);
      }
  }
  ```

  **4. Request logging (all requests including static):**

  ```java
  @Component
  public class RequestLoggingFilter implements Filter {
      
      @Override
      public void doFilter(
          ServletRequest request,
          ServletResponse response,
          FilterChain chain
      ) throws IOException, ServletException {
          HttpServletRequest httpRequest = (HttpServletRequest) request;
          
          log.info("Request: {} {}", httpRequest.getMethod(), httpRequest.getRequestURI());
          
          chain.doFilter(request, response);
      }
  }
  ```

  **5. Security (authentication/authorization at servlet level):**

  ```java
  @Component
  public class SecurityFilter implements Filter {
      
      @Override
      public void doFilter(
          ServletRequest request,
          ServletResponse response,
          FilterChain chain
      ) throws IOException, ServletException {
          HttpServletRequest httpRequest = (HttpServletRequest) request;
          HttpServletResponse httpResponse = (HttpServletResponse) response;
          
          // Check IP whitelist
          String remoteAddr = httpRequest.getRemoteAddr();
          if (!isWhitelisted(remoteAddr)) {
              httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
              return;
          }
          
          chain.doFilter(request, response);
      }
      
      private boolean isWhitelisted(String ip) {
          // Check whitelist
          return true;
      }
  }
  ```

</details>

<details>
  <summary>When to Use Interceptors</summary>
  <br/>

  **Use Interceptors for:**

  **1. Authentication/Authorization (controller-specific):**

  ```java
  @Component
  public class AuthInterceptor implements HandlerInterceptor {
      
      @Override
      public boolean preHandle(
          HttpServletRequest request,
          HttpServletResponse response,
          Object handler
      ) throws Exception {
          if (handler instanceof HandlerMethod) {
              HandlerMethod method = (HandlerMethod) handler;
              
              // Check method annotation
              if (method.hasMethodAnnotation(RequiresRole.class)) {
                  RequiresRole annotation = method.getMethodAnnotation(RequiresRole.class);
                  String requiredRole = annotation.value();
                  
                  // Check user role
                  if (!hasRole(request, requiredRole)) {
                      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                      return false;
                  }
              }
          }
          
          return true;
      }
      
      private boolean hasRole(HttpServletRequest request, String role) {
          // Check user role
          return true;
      }
  }
  ```

  **2. Performance monitoring:**

  ```java
  @Component
  public class PerformanceInterceptor implements HandlerInterceptor {
      
      @Override
      public boolean preHandle(
          HttpServletRequest request,
          HttpServletResponse response,
          Object handler
      ) throws Exception {
          request.setAttribute("startTime", System.currentTimeMillis());
          return true;
      }
      
      @Override
      public void afterCompletion(
          HttpServletRequest request,
          HttpServletResponse response,
          Object handler,
          Exception ex
      ) throws Exception {
          long startTime = (Long) request.getAttribute("startTime");
          long duration = System.currentTimeMillis() - startTime;
          
          if (handler instanceof HandlerMethod) {
              HandlerMethod method = (HandlerMethod) handler;
              log.info("Controller: {}.{} - {} ms",
                  method.getBeanType().getSimpleName(),
                  method.getMethod().getName(),
                  duration);
          }
      }
  }
  ```

  **3. Accessing ModelAndView:**

  ```java
  @Component
  public class ModelInterceptor implements HandlerInterceptor {
      
      @Override
      public void postHandle(
          HttpServletRequest request,
          HttpServletResponse response,
          Object handler,
          ModelAndView modelAndView
      ) throws Exception {
          if (modelAndView != null) {
              // Add common attributes to model
              modelAndView.addObject("currentYear", LocalDate.now().getYear());
              modelAndView.addObject("appVersion", "1.0.0");
          }
      }
  }
  ```

  **4. Handler-specific logic:**

  ```java
  @Component
  public class AuditInterceptor implements HandlerInterceptor {
      
      @Autowired
      private AuditService auditService;
      
      @Override
      public void afterCompletion(
          HttpServletRequest request,
          HttpServletResponse response,
          Object handler,
          Exception ex
      ) throws Exception {
          if (handler instanceof HandlerMethod) {
              HandlerMethod method = (HandlerMethod) handler;
              
              // Check if method should be audited
              if (method.hasMethodAnnotation(Audited.class)) {
                  User user = (User) request.getAttribute("currentUser");
                  
                  auditService.log(
                      user.getId(),
                      method.getMethod().getName(),
                      request.getRequestURI(),
                      response.getStatus()
                  );
              }
          }
      }
  }
  ```

</details>

<details>
  <summary>Filter Registration</summary>
  <br/>

  **Method 1: @Component annotation:**

  ```java
  @Component
  @Order(1)
  public class MyFilter implements Filter {
      
      @Override
      public void doFilter(
          ServletRequest request,
          ServletResponse response,
          FilterChain chain
      ) throws IOException, ServletException {
          // Filter logic
          chain.doFilter(request, response);
      }
  }
  ```

  **Method 2: FilterRegistrationBean:**

  ```java
  @Configuration
  public class FilterConfig {
      
      @Bean
      public FilterRegistrationBean<MyFilter> myFilter() {
          FilterRegistrationBean<MyFilter> registration = new FilterRegistrationBean<>();
          registration.setFilter(new MyFilter());
          registration.addUrlPatterns("/api/*");
          registration.setOrder(1);
          registration.setName("myFilter");
          return registration;
      }
  }
  ```

  **Method 3: @WebFilter (with @ServletComponentScan):**

  ```java
  @WebFilter(urlPatterns = "/api/*", filterName = "myFilter")
  public class MyFilter implements Filter {
      
      @Override
      public void doFilter(
          ServletRequest request,
          ServletResponse response,
          FilterChain chain
      ) throws IOException, ServletException {
          chain.doFilter(request, response);
      }
  }
  
  // In main application class
  @SpringBootApplication
  @ServletComponentScan
  public class Application {
      public static void main(String[] args) {
          SpringApplication.run(Application.class, args);
      }
  }
  ```

</details>

<details>
  <summary>Interceptor Registration</summary>
  <br/>

  **Register in WebMvcConfigurer:**

  ```java
  @Configuration
  public class WebMvcConfig implements WebMvcConfigurer {
      
      @Autowired
      private AuthInterceptor authInterceptor;
      
      @Autowired
      private LoggingInterceptor loggingInterceptor;
      
      @Override
      public void addInterceptors(InterceptorRegistry registry) {
          // Register interceptors with order
          registry.addInterceptor(loggingInterceptor)
                  .addPathPatterns("/**")
                  .order(1);
          
          registry.addInterceptor(authInterceptor)
                  .addPathPatterns("/api/**")
                  .excludePathPatterns("/api/auth/**", "/api/public/**")
                  .order(2);
      }
  }
  ```

</details>

<details>
  <summary>Best Practices</summary>
  <br/>

  ```java
  // ✅ DO: Use Filters for servlet-level concerns
  // - Request/response modification
  // - Character encoding
  // - CORS
  // - Compression
  // - Security (IP filtering)
  
  // ✅ DO: Use Interceptors for Spring MVC concerns
  // - Authentication/authorization (controller-specific)
  // - Performance monitoring
  // - Accessing ModelAndView
  // - Handler-specific logic
  // - Auditing
  
  // ✅ DO: Use Filter for all requests (including static resources)
  @Component
  public class CorsFilter implements Filter {
      // Applies to all requests
  }
  
  // ✅ DO: Use Interceptor for controller requests only
  @Component
  public class AuthInterceptor implements HandlerInterceptor {
      // Applies only to controller methods
  }
  
  // ✅ DO: Access Spring beans in Interceptors
  @Component
  public class AuthInterceptor implements HandlerInterceptor {
      @Autowired
      private UserService userService;  // Easy access to Spring beans
  }
  
  // ⚠️ CAUTION: Accessing Spring beans in Filters requires @Component
  @Component
  public class MyFilter implements Filter {
      @Autowired
      private SomeService service;  // Works with @Component
  }
  
  // ✅ DO: Use Filter for CORS (needs to run before Spring Security)
  @Component
  @Order(Ordered.HIGHEST_PRECEDENCE)
  public class CorsFilter implements Filter {
  }
  
  // ✅ DO: Use Interceptor to access handler information
  if (handler instanceof HandlerMethod) {
      HandlerMethod method = (HandlerMethod) handler;
      // Access controller class, method, annotations
  }
  
  // ✅ DO: Set execution order
  // Filters
  @Order(1)
  public class Filter1 implements Filter {}
  
  // Interceptors
  registry.addInterceptor(interceptor1).order(1);
  registry.addInterceptor(interceptor2).order(2);
  
  // ✅ DO: Call chain.doFilter() in Filters
  @Override
  public void doFilter(...) {
      // Before logic
      chain.doFilter(request, response);  // Must call this!
      // After logic
  }
  
  // ✅ DO: Return true/false in Interceptor preHandle
  @Override
  public boolean preHandle(...) {
      if (isValid) {
          return true;  // Continue
      }
      return false;  // Stop
  }
  
  // ✅ DO: Use path patterns appropriately
  // Filter - URL patterns
  registration.addUrlPatterns("/api/*", "/admin/*");
  
  // Interceptor - Ant-style patterns
  registry.addInterceptor(interceptor)
          .addPathPatterns("/api/**")
          .excludePathPatterns("/api/public/**");
  ```

  **Summary:**
  + **Filters** work at **servlet container level** (before Spring MVC)
  + **Interceptors** work at **Spring MVC level** (after DispatcherServlet)
  + Use **Filters** for request/response modification, encoding, CORS, compression
  + Use **Interceptors** for authentication, authorization, performance monitoring, auditing
  + **Filters** apply to **all requests** (including static resources)
  + **Interceptors** apply only to **controller requests**
  + **Interceptors** have access to **Handler** and **ModelAndView**
  + **Interceptors** have **full access to Spring beans**
  + **Filters** execute **before** Interceptors
  + Use **@Order** for Filter ordering, **InterceptorRegistry.order()** for Interceptor ordering
  + **Filters** use **doFilter()**, **Interceptors** use **preHandle/postHandle/afterCompletion**

</details>

## Content Negotiation

<details>
  <summary>What is Content Negotiation?</summary>
  <br/>

  **Content Negotiation** allows the server to return different representations (JSON, XML, etc.) of the same resource based on client preferences.

  **How it works:**
  + Client specifies preferred format in **Accept** header
  + Server returns response in requested format
  + Spring MVC automatically handles conversion

  **Example:**

  ```
  Request with Accept: application/json
  → Server returns JSON
  
  Request with Accept: application/xml
  → Server returns XML
  ```

  **Benefits:**
  + Single endpoint serves multiple formats
  + Client chooses preferred format
  + Automatic conversion by Spring

</details>

<details>
  <summary>Accept Header</summary>
  <br/>

  **Accept header** specifies client's preferred response format.

  **Common media types:**

  ```
  Accept: application/json          → JSON
  Accept: application/xml           → XML
  Accept: text/html                 → HTML
  Accept: text/plain                → Plain text
  Accept: application/pdf           → PDF
  Accept: */*                       → Any format
  ```

  **Multiple formats with priority:**

  ```
  Accept: application/json, application/xml;q=0.9, */*;q=0.8
  ```

  + `q` parameter indicates quality/priority (0.0 to 1.0)
  + Higher value = higher priority
  + Default q=1.0 if not specified

  **Example:**

  ```bash
  # Request JSON
  curl -H "Accept: application/json" http://localhost:8080/api/users/1
  
  # Request XML
  curl -H "Accept: application/xml" http://localhost:8080/api/users/1
  ```

</details>

<details>
  <summary>JSON Support (Default)</summary>
  <br/>

  **JSON is enabled by default in Spring Boot.**

  **Dependency (included in spring-boot-starter-web):**

  ```xml
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-json</artifactId>
  </dependency>
  ```

  **Controller:**

  ```java
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
      
      @GetMapping("/{id}")
      public User getUser(@PathVariable Long id) {
          return userService.findById(id);
      }
      
      @GetMapping
      public List<User> getAllUsers() {
          return userService.findAll();
      }
  }
  ```

  **Request:**

  ```bash
  curl -H "Accept: application/json" http://localhost:8080/api/users/1
  ```

  **Response:**

  ```json
  {
    "id": 1,
    "username": "john",
    "email": "john@example.com",
    "createdAt": "2024-01-15T10:30:00"
  }
  ```

</details>

<details>
  <summary>XML Support</summary>
  <br/>

  **Add XML dependency:**

  ```xml
  <dependency>
      <groupId>com.fasterxml.jackson.dataformat</groupId>
      <artifactId>jackson-dataformat-xml</artifactId>
  </dependency>
  ```

  **Entity with XML annotations:**

  ```java
  @Entity
  @XmlRootElement(name = "user")
  @XmlAccessorType(XmlAccessType.FIELD)
  public class User {
      
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      @XmlElement
      private Long id;
      
      @XmlElement
      private String username;
      
      @XmlElement
      private String email;
      
      @XmlElement
      private LocalDateTime createdAt;
      
      // Getters and setters
  }
  ```

  **Controller (same as JSON):**

  ```java
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
      
      @GetMapping("/{id}")
      public User getUser(@PathVariable Long id) {
          return userService.findById(id);
      }
  }
  ```

  **Request:**

  ```bash
  curl -H "Accept: application/xml" http://localhost:8080/api/users/1
  ```

  **Response:**

  ```xml
  <user>
      <id>1</id>
      <username>john</username>
      <email>john@example.com</email>
      <createdAt>2024-01-15T10:30:00</createdAt>
  </user>
  ```

</details>

<details>
  <summary>Produces Annotation</summary>
  <br/>

  **@produces** restricts which formats a method can return.

  **JSON only:**

  ```java
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
      
      @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
      public User getUserJson(@PathVariable Long id) {
          return userService.findById(id);
      }
  }
  ```

  **XML only:**

  ```java
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_XML_VALUE)
  public User getUserXml(@PathVariable Long id) {
      return userService.findById(id);
  }
  ```

  **Multiple formats:**

  ```java
  @GetMapping(
      value = "/{id}",
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
  )
  public User getUser(@PathVariable Long id) {
      return userService.findById(id);
  }
  ```

  **Class-level produces:**

  ```java
  @RestController
  @RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
  public class UserController {
      
      // All methods return JSON by default
      @GetMapping("/{id}")
      public User getUser(@PathVariable Long id) {
          return userService.findById(id);
      }
      
      // Override for specific method
      @GetMapping(value = "/{id}/xml", produces = MediaType.APPLICATION_XML_VALUE)
      public User getUserXml(@PathVariable Long id) {
          return userService.findById(id);
      }
  }
  ```

</details>

<details>
  <summary>Consumes Annotation</summary>
  <br/>

  **@Consumes** restricts which formats a method can accept.

  **JSON only:**

  ```java
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
      
      @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
      public User createUserJson(@RequestBody User user) {
          return userService.create(user);
      }
  }
  ```

  **XML only:**

  ```java
  @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE)
  public User createUserXml(@RequestBody User user) {
      return userService.create(user);
  }
  ```

  **Multiple formats:**

  ```java
  @PostMapping(
      consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
  )
  public User createUser(@RequestBody User user) {
      return userService.create(user);
  }
  ```

  **Request examples:**

  ```bash
  # JSON request
  curl -X POST \
    -H "Content-Type: application/json" \
    -H "Accept: application/json" \
    -d '{"username":"john","email":"john@example.com"}' \
    http://localhost:8080/api/users
  
  # XML request
  curl -X POST \
    -H "Content-Type: application/xml" \
    -H "Accept: application/xml" \
    -d '<user><username>john</username><email>john@example.com</email></user>' \
    http://localhost:8080/api/users
  ```

</details>

<details>
  <summary>Path Extension Strategy</summary>
  <br/>

  **Use file extension in URL to specify format.**

  **Enable path extension (deprecated but still used):**

  ```java
  @Configuration
  public class WebMvcConfig implements WebMvcConfigurer {
      
      @Override
      public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
          configurer
              .favorPathExtension(true)  // Deprecated in Spring 5.3
              .favorParameter(false)
              .ignoreAcceptHeader(false)
              .defaultContentType(MediaType.APPLICATION_JSON)
              .mediaType("json", MediaType.APPLICATION_JSON)
              .mediaType("xml", MediaType.APPLICATION_XML);
      }
  }
  ```

  **Usage:**

  ```bash
  # JSON
  curl http://localhost:8080/api/users/1.json
  
  # XML
  curl http://localhost:8080/api/users/1.xml
  ```

  **Note:** Path extension strategy is deprecated in Spring 5.3+ due to security concerns. Use Accept header or query parameter instead.

</details>

<details>
  <summary>Query Parameter Strategy</summary>
  <br/>

  **Use query parameter to specify format.**

  **Enable query parameter:**

  ```java
  @Configuration
  public class WebMvcConfig implements WebMvcConfigurer {
      
      @Override
      public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
          configurer
              .favorParameter(true)
              .parameterName("format")  // Default is "format"
              .ignoreAcceptHeader(false)
              .defaultContentType(MediaType.APPLICATION_JSON)
              .mediaType("json", MediaType.APPLICATION_JSON)
              .mediaType("xml", MediaType.APPLICATION_XML);
      }
  }
  ```

  **Usage:**

  ```bash
  # JSON
  curl http://localhost:8080/api/users/1?format=json
  
  # XML
  curl http://localhost:8080/api/users/1?format=xml
  ```

</details>

<details>
  <summary>Custom Content Negotiation Configuration</summary>
  <br/>

  **Configure content negotiation strategy:**

  ```java
  @Configuration
  public class WebMvcConfig implements WebMvcConfigurer {
      
      @Override
      public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
          configurer
              // Disable path extension (recommended)
              .favorPathExtension(false)
              
              // Enable query parameter
              .favorParameter(true)
              .parameterName("mediaType")
              
              // Use Accept header (recommended)
              .ignoreAcceptHeader(false)
              
              // Default content type
              .defaultContentType(MediaType.APPLICATION_JSON)
              
              // Register media types
              .mediaType("json", MediaType.APPLICATION_JSON)
              .mediaType("xml", MediaType.APPLICATION_XML)
              .mediaType("html", MediaType.TEXT_HTML);
      }
  }
  ```

  **Priority order:**
  1. Query parameter (if enabled)
  2. Path extension (if enabled, deprecated)
  3. Accept header
  4. Default content type

</details>

<details>
  <summary>Custom Message Converter</summary>
  <br/>

  **Create custom converter for specific format.**

  **CSV converter example:**

  ```java
  public class CsvHttpMessageConverter extends AbstractHttpMessageConverter<List<User>> {
      
      public CsvHttpMessageConverter() {
          super(new MediaType("text", "csv"));
      }
      
      @Override
      protected boolean supports(Class<?> clazz) {
          return List.class.isAssignableFrom(clazz);
      }
      
      @Override
      protected List<User> readInternal(
          Class<? extends List<User>> clazz,
          HttpInputMessage inputMessage
      ) throws IOException {
          // Parse CSV to List<User>
          return new ArrayList<>();
      }
      
      @Override
      protected void writeInternal(
          List<User> users,
          HttpOutputMessage outputMessage
      ) throws IOException {
          // Write List<User> as CSV
          String csv = users.stream()
              .map(user -> String.format("%d,%s,%s", 
                  user.getId(), user.getUsername(), user.getEmail()))
              .collect(Collectors.joining("\n"));
          
          outputMessage.getBody().write(csv.getBytes());
      }
  }
  ```

  **Register converter:**

  ```java
  @Configuration
  public class WebMvcConfig implements WebMvcConfigurer {
      
      @Override
      public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
          converters.add(new CsvHttpMessageConverter());
      }
      
      @Override
      public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
          configurer
              .defaultContentType(MediaType.APPLICATION_JSON)
              .mediaType("json", MediaType.APPLICATION_JSON)
              .mediaType("xml", MediaType.APPLICATION_XML)
              .mediaType("csv", new MediaType("text", "csv"));
      }
  }
  ```

  **Usage:**

  ```bash
  curl -H "Accept: text/csv" http://localhost:8080/api/users
  ```

</details>

<details>
  <summary>Complete Example</summary>
  <br/>

  **Entity:**

  ```java
  @Entity
  @XmlRootElement(name = "user")
  @XmlAccessorType(XmlAccessType.FIELD)
  public class User {
      
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;
      
      private String username;
      private String email;
      
      // Getters and setters
  }
  ```

  **Controller:**

  ```java
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
      
      @Autowired
      private UserService userService;
      
      // Supports both JSON and XML
      @GetMapping(
          value = "/{id}",
          produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
      )
      public ResponseEntity<User> getUser(@PathVariable Long id) {
          return userService.findById(id)
              .map(ResponseEntity::ok)
              .orElse(ResponseEntity.notFound().build());
      }
      
      @GetMapping(
          produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
      )
      public List<User> getAllUsers() {
          return userService.findAll();
      }
      
      @PostMapping(
          consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
          produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
      )
      public ResponseEntity<User> createUser(@RequestBody User user) {
          User created = userService.create(user);
          return ResponseEntity.status(HttpStatus.CREATED).body(created);
      }
  }
  ```

  **Configuration:**

  ```java
  @Configuration
  public class WebMvcConfig implements WebMvcConfigurer {
      
      @Override
      public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
          configurer
              .favorParameter(true)
              .parameterName("format")
              .ignoreAcceptHeader(false)
              .defaultContentType(MediaType.APPLICATION_JSON)
              .mediaType("json", MediaType.APPLICATION_JSON)
              .mediaType("xml", MediaType.APPLICATION_XML);
      }
  }
  ```

  **Requests:**

  ```bash
  # JSON via Accept header
  curl -H "Accept: application/json" http://localhost:8080/api/users/1
  
  # XML via Accept header
  curl -H "Accept: application/xml" http://localhost:8080/api/users/1
  
  # JSON via query parameter
  curl http://localhost:8080/api/users/1?format=json
  
  # XML via query parameter
  curl http://localhost:8080/api/users/1?format=xml
  
  # POST JSON
  curl -X POST \
    -H "Content-Type: application/json" \
    -H "Accept: application/json" \
    -d '{"username":"john","email":"john@example.com"}' \
    http://localhost:8080/api/users
  
  # POST XML
  curl -X POST \
    -H "Content-Type: application/xml" \
    -H "Accept: application/xml" \
    -d '<user><username>john</username><email>john@example.com</email></user>' \
    http://localhost:8080/api/users
  ```

</details>

<details>
  <summary>Best Practices</summary>
  <br/>

  ```java
  // ✅ DO: Use Accept header for content negotiation (recommended)
  curl -H "Accept: application/json" http://localhost:8080/api/users/1
  
  // ✅ DO: Support multiple formats in single endpoint
  @GetMapping(
      value = "/{id}",
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
  )
  public User getUser(@PathVariable Long id) {
  }
  
  // ✅ DO: Set default content type
  configurer.defaultContentType(MediaType.APPLICATION_JSON)
  
  // ✅ DO: Use @produces to restrict response format
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public User getUser(@PathVariable Long id) {
  }
  
  // ✅ DO: Use @Consumes to restrict request format
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public User createUser(@RequestBody User user) {
  }
  
  // ✅ DO: Enable query parameter for browser testing
  configurer.favorParameter(true).parameterName("format")
  
  // ❌ DON'T: Use path extension (deprecated and security risk)
  configurer.favorPathExtension(true)  // Deprecated
  
  // ✅ DO: Add XML dependency for XML support
  <dependency>
      <groupId>com.fasterxml.jackson.dataformat</groupId>
      <artifactId>jackson-dataformat-xml</artifactId>
  </dependency>
  
  // ✅ DO: Use @XmlRootElement for XML entities
  @XmlRootElement(name = "user")
  public class User {
  }
  
  // ✅ DO: Return 406 Not Acceptable for unsupported formats
  // Spring does this automatically
  
  // ✅ DO: Use MediaType constants
  MediaType.APPLICATION_JSON_VALUE
  MediaType.APPLICATION_XML_VALUE
  MediaType.TEXT_HTML_VALUE
  
  // ❌ DON'T: Hardcode media type strings
  produces = "application/json"  // Use MediaType constant instead
  
  // ✅ DO: Configure content negotiation in WebMvcConfigurer
  @Configuration
  public class WebMvcConfig implements WebMvcConfigurer {
      @Override
      public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
      }
  }
  
  // ✅ DO: Test with different Accept headers
  curl -H "Accept: application/json" ...
  curl -H "Accept: application/xml" ...
  
  // ✅ DO: Handle unsupported media types gracefully
  @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
  public ResponseEntity<String> handleNotAcceptable(HttpMediaTypeNotAcceptableException ex) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
          .body("Supported formats: JSON, XML");
  }
  ```

  **Summary:**
  + **Content negotiation** allows serving multiple formats from single endpoint
  + Client specifies format via **Accept header** (recommended)
  + **JSON** is enabled by default in Spring Boot
  + Add **jackson-dataformat-xml** dependency for XML support
  + Use **@produces** to restrict response formats
  + Use **@Consumes** to restrict request formats
  + Configure via **WebMvcConfigurer.configureContentNegotiation()**
  + **Query parameter** strategy useful for browser testing
  + **Path extension** strategy is deprecated (security risk)
  + Default content type is **application/json**
  + Spring returns **406 Not Acceptable** for unsupported formats
  + Use **MediaType constants** instead of hardcoded strings
  + Priority: Query parameter → Path extension → Accept header → Default

</details>

## CORS Configuration

<details>
  <summary>What is CORS?</summary>
  <br/>

  **CORS (Cross-Origin Resource Sharing)** allows web applications from one domain to access resources from another domain.

  **Same-Origin Policy:**
  + Browsers block requests from different origins by default
  + Origin = protocol + domain + port
  + `http://example.com:8080` ≠ `http://example.com:3000` (different port)
  + `http://example.com` ≠ `https://example.com` (different protocol)

  **CORS headers:**
  + **Access-Control-Allow-Origin** - Which origins can access
  + **Access-Control-Allow-Methods** - Which HTTP methods allowed
  + **Access-Control-Allow-Headers** - Which headers allowed
  + **Access-Control-Max-Age** - How long to cache preflight response

  **Preflight request:**
  + Browser sends OPTIONS request before actual request
  + Checks if server allows the cross-origin request
  + Only for certain requests (POST with custom headers, PUT, DELETE)

</details>

<details>
  <summary>@CrossOrigin Annotation</summary>
  <br/>

  **@CrossOrigin** enables CORS on controller or method level.

  **Method-level:**

  ```java
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
      
      @CrossOrigin(origins = "http://localhost:3000")
      @GetMapping("/{id}")
      public User getUser(@PathVariable Long id) {
          return userService.findById(id);
      }
  }
  ```

  **Controller-level:**

  ```java
  @RestController
  @RequestMapping("/api/users")
  @CrossOrigin(origins = "http://localhost:3000")
  public class UserController {
      
      // All methods allow CORS from http://localhost:3000
      @GetMapping("/{id}")
      public User getUser(@PathVariable Long id) {
          return userService.findById(id);
      }
      
      @PostMapping
      public User createUser(@RequestBody User user) {
          return userService.create(user);
      }
  }
  ```

  **Multiple origins:**

  ```java
  @CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200"})
  @GetMapping("/{id}")
  public User getUser(@PathVariable Long id) {
      return userService.findById(id);
  }
  ```

  **Allow all origins:**

  ```java
  @CrossOrigin(origins = "*")
  @GetMapping("/{id}")
  public User getUser(@PathVariable Long id) {
      return userService.findById(id);
  }
  ```

</details>

<details>
  <summary>@CrossOrigin Configuration Options</summary>
  <br/>

  **All configuration options:**

  ```java
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
      
      @CrossOrigin(
          origins = {"http://localhost:3000", "http://localhost:4200"},
          methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT},
          allowedHeaders = {"Authorization", "Content-Type"},
          exposedHeaders = {"X-Custom-Header"},
          allowCredentials = "true",
          maxAge = 3600
      )
      @GetMapping("/{id}")
      public User getUser(@PathVariable Long id) {
          return userService.findById(id);
      }
  }
  ```

  **Configuration breakdown:**
  + **origins** - Allowed origins (domains)
  + **methods** - Allowed HTTP methods
  + **allowedHeaders** - Headers client can send
  + **exposedHeaders** - Headers client can read from response
  + **allowCredentials** - Allow cookies/authentication
  + **maxAge** - Cache preflight response (seconds)

</details>

<details>
  <summary>Global CORS Configuration</summary>
  <br/>

  **Configure CORS globally for all endpoints.**

  **Method 1: WebMvcConfigurer:**

  ```java
  @Configuration
  public class WebMvcConfig implements WebMvcConfigurer {
      
      @Override
      public void addCorsMappings(CorsRegistry registry) {
          registry.addMapping("/api/**")
                  .allowedOrigins("http://localhost:3000")
                  .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                  .allowedHeaders("*")
                  .allowCredentials(true)
                  .maxAge(3600);
      }
  }
  ```

  **Multiple path patterns:**

  ```java
  @Configuration
  public class WebMvcConfig implements WebMvcConfigurer {
      
      @Override
      public void addCorsMappings(CorsRegistry registry) {
          // API endpoints
          registry.addMapping("/api/**")
                  .allowedOrigins("http://localhost:3000", "http://localhost:4200")
                  .allowedMethods("GET", "POST", "PUT", "DELETE")
                  .allowedHeaders("*")
                  .allowCredentials(true)
                  .maxAge(3600);
          
          // Public endpoints
          registry.addMapping("/public/**")
                  .allowedOrigins("*")
                  .allowedMethods("GET")
                  .allowedHeaders("*")
                  .maxAge(3600);
      }
  }
  ```

  **Allow all origins (development only):**

  ```java
  @Configuration
  public class WebMvcConfig implements WebMvcConfigurer {
      
      @Override
      public void addCorsMappings(CorsRegistry registry) {
          registry.addMapping("/**")
                  .allowedOrigins("*")
                  .allowedMethods("*")
                  .allowedHeaders("*")
                  .maxAge(3600);
      }
  }
  ```

</details>

<details>
  <summary>CORS Filter</summary>
  <br/>

  **Create custom CORS filter for fine-grained control.**

  **Basic CORS filter:**

  ```java
  @Component
  @Order(Ordered.HIGHEST_PRECEDENCE)
  public class CorsFilter implements Filter {
      
      @Override
      public void doFilter(
          ServletRequest request,
          ServletResponse response,
          FilterChain chain
      ) throws IOException, ServletException {
          HttpServletResponse httpResponse = (HttpServletResponse) response;
          HttpServletRequest httpRequest = (HttpServletRequest) request;
          
          // Set CORS headers
          httpResponse.setHeader("Access-Control-Allow-Origin", "*");
          httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
          httpResponse.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
          httpResponse.setHeader("Access-Control-Max-Age", "3600");
          
          // Handle preflight request
          if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
              httpResponse.setStatus(HttpServletResponse.SC_OK);
              return;
          }
          
          chain.doFilter(request, response);
      }
  }
  ```

  **Advanced CORS filter with origin validation:**

  ```java
  @Component
  @Order(Ordered.HIGHEST_PRECEDENCE)
  @Slf4j
  public class CorsFilter implements Filter {
      
      private static final List<String> ALLOWED_ORIGINS = Arrays.asList(
          "http://localhost:3000",
          "http://localhost:4200",
          "https://example.com"
      );
      
      @Override
      public void doFilter(
          ServletRequest request,
          ServletResponse response,
          FilterChain chain
      ) throws IOException, ServletException {
          HttpServletResponse httpResponse = (HttpServletResponse) response;
          HttpServletRequest httpRequest = (HttpServletRequest) request;
          
          String origin = httpRequest.getHeader("Origin");
          
          // Check if origin is allowed
          if (origin != null && ALLOWED_ORIGINS.contains(origin)) {
              httpResponse.setHeader("Access-Control-Allow-Origin", origin);
              httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
          }
          
          httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
          httpResponse.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, X-Requested-With");
          httpResponse.setHeader("Access-Control-Expose-Headers", "X-Total-Count, X-Page-Number");
          httpResponse.setHeader("Access-Control-Max-Age", "3600");
          
          // Handle preflight request
          if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
              log.debug("Preflight request from origin: {}", origin);
              httpResponse.setStatus(HttpServletResponse.SC_OK);
              return;
          }
          
          chain.doFilter(request, response);
      }
  }
  ```

</details>

<details>
  <summary>CORS with Spring Security</summary>
  <br/>

  **Configure CORS with Spring Security.**

  **Security configuration:**

  ```java
  @Configuration
  @EnableWebSecurity
  public class SecurityConfig {
      
      @Bean
      public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
          http
              .cors(cors -> cors.configurationSource(corsConfigurationSource()))
              .csrf(csrf -> csrf.disable())
              .authorizeHttpRequests(auth -> auth
                  .requestMatchers("/api/public/**").permitAll()
                  .requestMatchers("/api/**").authenticated()
              );
          
          return http.build();
      }
      
      @Bean
      public CorsConfigurationSource corsConfigurationSource() {
          CorsConfiguration configuration = new CorsConfiguration();
          configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
          configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
          configuration.setAllowedHeaders(Arrays.asList("*"));
          configuration.setAllowCredentials(true);
          configuration.setMaxAge(3600L);
          
          UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
          source.registerCorsConfiguration("/api/**", configuration);
          return source;
      }
  }
  ```

  **With WebMvcConfigurer:**

  ```java
  @Configuration
  public class WebMvcConfig implements WebMvcConfigurer {
      
      @Override
      public void addCorsMappings(CorsRegistry registry) {
          registry.addMapping("/api/**")
                  .allowedOrigins("http://localhost:3000")
                  .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                  .allowedHeaders("*")
                  .allowCredentials(true)
                  .maxAge(3600);
      }
  }
  
  @Configuration
  @EnableWebSecurity
  public class SecurityConfig {
      
      @Bean
      public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
          http
              .cors(Customizer.withDefaults())  // Use CORS from WebMvcConfigurer
              .csrf(csrf -> csrf.disable())
              .authorizeHttpRequests(auth -> auth
                  .requestMatchers("/api/**").authenticated()
              );
          
          return http.build();
      }
  }
  ```

</details>

<details>
  <summary>Environment-Specific CORS</summary>
  <br/>

  **Configure CORS based on environment.**

  **application.properties:**

  ```properties
  # Development
  cors.allowed-origins=http://localhost:3000,http://localhost:4200
  cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS
  cors.allowed-headers=*
  cors.allow-credentials=true
  cors.max-age=3600
  ```

  **Configuration class:**

  ```java
  @Configuration
  public class CorsConfig implements WebMvcConfigurer {
      
      @Value("${cors.allowed-origins}")
      private String[] allowedOrigins;
      
      @Value("${cors.allowed-methods}")
      private String[] allowedMethods;
      
      @Value("${cors.allowed-headers}")
      private String[] allowedHeaders;
      
      @Value("${cors.allow-credentials}")
      private boolean allowCredentials;
      
      @Value("${cors.max-age}")
      private long maxAge;
      
      @Override
      public void addCorsMappings(CorsRegistry registry) {
          registry.addMapping("/api/**")
                  .allowedOrigins(allowedOrigins)
                  .allowedMethods(allowedMethods)
                  .allowedHeaders(allowedHeaders)
                  .allowCredentials(allowCredentials)
                  .maxAge(maxAge);
      }
  }
  ```

  **Profile-specific configuration:**

  ```properties
  # application-dev.properties
  cors.allowed-origins=http://localhost:3000,http://localhost:4200
  
  # application-prod.properties
  cors.allowed-origins=https://example.com,https://www.example.com
  ```

</details>

<details>
  <summary>Preflight Request Handling</summary>
  <br/>

  **Preflight request** is an OPTIONS request sent by browser before actual request.

  **When preflight is triggered:**
  + Custom headers (e.g., Authorization)
  + Content-Type other than application/x-www-form-urlencoded, multipart/form-data, text/plain
  + HTTP methods: PUT, DELETE, PATCH

  **Preflight request:**

  ```
  OPTIONS /api/users/1 HTTP/1.1
  Origin: http://localhost:3000
  Access-Control-Request-Method: DELETE
  Access-Control-Request-Headers: Authorization
  ```

  **Preflight response:**

  ```
  HTTP/1.1 200 OK
  Access-Control-Allow-Origin: http://localhost:3000
  Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS
  Access-Control-Allow-Headers: Authorization, Content-Type
  Access-Control-Max-Age: 3600
  ```

  **Handle preflight in controller:**

  ```java
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
      
      @RequestMapping(value = "/{id}", method = RequestMethod.OPTIONS)
      public ResponseEntity<?> handlePreflight() {
          return ResponseEntity.ok().build();
      }
      
      @DeleteMapping("/{id}")
      @CrossOrigin(origins = "http://localhost:3000")
      public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
          userService.delete(id);
          return ResponseEntity.noContent().build();
      }
  }
  ```

  **Note:** Spring handles preflight automatically when using @CrossOrigin or global CORS configuration.

</details>

<details>
  <summary>CORS with Credentials</summary>
  <br/>

  **Allow cookies and authentication headers.**

  **Configuration:**

  ```java
  @Configuration
  public class WebMvcConfig implements WebMvcConfigurer {
      
      @Override
      public void addCorsMappings(CorsRegistry registry) {
          registry.addMapping("/api/**")
                  .allowedOrigins("http://localhost:3000")  // Cannot use "*" with credentials
                  .allowedMethods("GET", "POST", "PUT", "DELETE")
                  .allowedHeaders("*")
                  .allowCredentials(true)  // Allow cookies
                  .maxAge(3600);
      }
  }
  ```

  **Important:**
  + Cannot use `allowedOrigins("*")` with `allowCredentials(true)`
  + Must specify exact origins
  + Browser will send cookies with cross-origin requests

  **Client-side (JavaScript):**

  ```javascript
  // Fetch API
  fetch('http://localhost:8080/api/users', {
    method: 'GET',
    credentials: 'include',  // Include cookies
    headers: {
      'Content-Type': 'application/json'
    }
  });
  
  // Axios
  axios.get('http://localhost:8080/api/users', {
    withCredentials: true  // Include cookies
  });
  ```

</details>

<details>
  <summary>Testing CORS</summary>
  <br/>

  **Test CORS configuration.**

  **Using curl:**

  ```bash
  # Test preflight request
  curl -X OPTIONS \
    -H "Origin: http://localhost:3000" \
    -H "Access-Control-Request-Method: DELETE" \
    -H "Access-Control-Request-Headers: Authorization" \
    -v \
    http://localhost:8080/api/users/1
  
  # Test actual request
  curl -X GET \
    -H "Origin: http://localhost:3000" \
    -v \
    http://localhost:8080/api/users/1
  ```

  **Check response headers:**

  ```
  Access-Control-Allow-Origin: http://localhost:3000
  Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS
  Access-Control-Allow-Headers: Authorization, Content-Type
  Access-Control-Max-Age: 3600
  ```

  **Browser DevTools:**
  + Open Network tab
  + Look for OPTIONS request (preflight)
  + Check response headers
  + Look for CORS errors in Console

</details>

<details>
  <summary>Best Practices</summary>
  <br/>

  ```java
  // ✅ DO: Use global CORS configuration for consistency
  @Configuration
  public class WebMvcConfig implements WebMvcConfigurer {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
          registry.addMapping("/api/**")
                  .allowedOrigins("http://localhost:3000")
                  .allowedMethods("GET", "POST", "PUT", "DELETE")
                  .allowedHeaders("*")
                  .allowCredentials(true)
                  .maxAge(3600);
      }
  }
  
  // ✅ DO: Specify exact origins in production
  .allowedOrigins("https://example.com", "https://www.example.com")
  
  // ❌ DON'T: Use wildcard in production
  .allowedOrigins("*")  // Security risk
  
  // ✅ DO: Use environment-specific configuration
  @Value("${cors.allowed-origins}")
  private String[] allowedOrigins;
  
  // ✅ DO: Specify allowed methods explicitly
  .allowedMethods("GET", "POST", "PUT", "DELETE")
  
  // ❌ DON'T: Allow all methods in production
  .allowedMethods("*")
  
  // ✅ DO: Set appropriate max age for preflight caching
  .maxAge(3600)  // 1 hour
  
  // ✅ DO: Use allowCredentials only when needed
  .allowCredentials(true)
  
  // ❌ DON'T: Use wildcard with credentials
  .allowedOrigins("*")
  .allowCredentials(true)  // This will fail!
  
  // ✅ DO: Use @CrossOrigin for method-specific CORS
  @CrossOrigin(origins = "http://localhost:3000")
  @GetMapping("/{id}")
  public User getUser(@PathVariable Long id) {
  }
  
  // ✅ DO: Handle preflight requests (Spring does this automatically)
  // No need to manually handle OPTIONS requests
  
  // ✅ DO: Use CORS filter for complex scenarios
  @Component
  @Order(Ordered.HIGHEST_PRECEDENCE)
  public class CorsFilter implements Filter {
  }
  
  // ✅ DO: Configure CORS before Spring Security
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
      http.cors(Customizer.withDefaults())  // CORS first
          .csrf(csrf -> csrf.disable());
      return http.build();
  }
  
  // ✅ DO: Expose custom headers if needed
  .exposedHeaders("X-Total-Count", "X-Page-Number")
  
  // ✅ DO: Test CORS configuration
  curl -X OPTIONS -H "Origin: http://localhost:3000" -v http://localhost:8080/api/users
  
  // ✅ DO: Log CORS issues for debugging
  @Slf4j
  public class CorsFilter implements Filter {
      log.debug("CORS request from origin: {}", origin);
  }
  
  // ✅ DO: Use specific path patterns
  registry.addMapping("/api/**")  // Only API endpoints
  
  // ❌ DON'T: Apply CORS to all endpoints unnecessarily
  registry.addMapping("/**")  // Too broad
  ```

  **Summary:**
  + **CORS** allows cross-origin requests from browsers
  + Use **@CrossOrigin** for method/controller-level configuration
  + Use **WebMvcConfigurer.addCorsMappings()** for global configuration
  + Use **CORS filter** for complex scenarios
  + **Preflight request** (OPTIONS) checks if cross-origin request is allowed
  + Cannot use **wildcard** (`*`) with **credentials**
  + Specify **exact origins** in production
  + Set **maxAge** to cache preflight responses
  + Configure **CORS before Spring Security**
  + Use **environment-specific** configuration
  + **Test CORS** with curl or browser DevTools
  + **Expose custom headers** if client needs to read them
  + Use **allowCredentials** only when needed (cookies, auth)

</details>

## Async Request Processing

<details>
  <summary>Why Async Processing?</summary>
  <br/>

  **Async processing** allows handling long-running requests without blocking server threads.

  **Synchronous (blocking):**
  + Request thread waits for operation to complete
  + Thread is blocked and cannot handle other requests
  + Limited scalability (limited thread pool)

  **Asynchronous (non-blocking):**
  + Request thread is released immediately
  + Operation runs in background thread
  + Thread can handle other requests
  + Better scalability and resource utilization

  **Use cases:**
  + Long-running database queries
  + External API calls
  + File processing
  + Report generation
  + Email sending

  **Benefits:**
  + Better thread utilization
  + Higher throughput
  + Improved scalability
  + Non-blocking I/O

</details>

<details>
  <summary>Callable</summary>
  <br/>

  **Callable** returns result asynchronously from background thread.

  **Basic usage:**

  ```java
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
      
      @Autowired
      private UserService userService;
      
      @GetMapping("/{id}")
      public Callable<User> getUser(@PathVariable Long id) {
          return () -> {
              // Runs in separate thread
              Thread.sleep(2000);  // Simulate long operation
              return userService.findById(id);
          };
      }
  }
  ```

  **How it works:**
  1. Request thread returns Callable immediately
  2. Spring executes Callable in background thread
  3. Request thread is released to handle other requests
  4. When Callable completes, response is sent to client

  **With exception handling:**

  ```java
  @GetMapping("/{id}")
  public Callable<User> getUser(@PathVariable Long id) {
      return () -> {
          try {
              Thread.sleep(2000);
              return userService.findById(id);
          } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
              throw new RuntimeException("Request interrupted", e);
          }
      };
  }
  ```

</details>

<details>
  <summary>DeferredResult</summary>
  <br/>

  **DeferredResult** provides more control over async processing.

  **Basic usage:**

  ```java
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
      
      @Autowired
      private UserService userService;
      
      @GetMapping("/{id}")
      public DeferredResult<User> getUser(@PathVariable Long id) {
          DeferredResult<User> deferredResult = new DeferredResult<>();
          
          // Process in background thread
          CompletableFuture.supplyAsync(() -> userService.findById(id))
              .thenAccept(deferredResult::setResult)
              .exceptionally(ex -> {
                  deferredResult.setErrorResult(ex);
                  return null;
              });
          
          return deferredResult;
      }
  }
  ```

  **With timeout:**

  ```java
  @GetMapping("/{id}")
  public DeferredResult<User> getUser(@PathVariable Long id) {
      DeferredResult<User> deferredResult = new DeferredResult<>(5000L);  // 5 second timeout
      
      // Set timeout handler
      deferredResult.onTimeout(() -> 
          deferredResult.setErrorResult(new TimeoutException("Request timeout"))
      );
      
      // Set completion handler
      deferredResult.onCompletion(() -> 
          System.out.println("Request completed")
      );
      
      // Process asynchronously
      CompletableFuture.supplyAsync(() -> userService.findById(id))
          .thenAccept(deferredResult::setResult)
          .exceptionally(ex -> {
              deferredResult.setErrorResult(ex);
              return null;
          });
      
      return deferredResult;
  }
  ```

  **Event-driven example:**

  ```java
  @RestController
  @RequestMapping("/api/notifications")
  public class NotificationController {
      
      private final Map<String, DeferredResult<String>> pendingRequests = new ConcurrentHashMap<>();
      
      @GetMapping("/subscribe/{userId}")
      public DeferredResult<String> subscribe(@PathVariable String userId) {
          DeferredResult<String> deferredResult = new DeferredResult<>(30000L);
          
          // Store for later completion
          pendingRequests.put(userId, deferredResult);
          
          // Remove on completion
          deferredResult.onCompletion(() -> pendingRequests.remove(userId));
          deferredResult.onTimeout(() -> pendingRequests.remove(userId));
          
          return deferredResult;
      }
      
      @PostMapping("/send/{userId}")
      public ResponseEntity<String> sendNotification(
          @PathVariable String userId,
          @RequestBody String message
      ) {
          DeferredResult<String> deferredResult = pendingRequests.get(userId);
          
          if (deferredResult != null) {
              deferredResult.setResult(message);
              return ResponseEntity.ok("Notification sent");
          }
          
          return ResponseEntity.notFound().build();
      }
  }
  ```

</details>

<details>
  <summary>@Async Annotation</summary>
  <br/>

  **@Async** executes method asynchronously in background thread.

  **Enable async processing:**

  ```java
  @Configuration
  @EnableAsync
  public class AsyncConfig {
  }
  ```

  **Service with @Async:**

  ```java
  @Service
  public class UserService {
      
      @Async
      public CompletableFuture<User> findByIdAsync(Long id) {
          // Runs in separate thread
          User user = userRepository.findById(id).orElseThrow();
          return CompletableFuture.completedFuture(user);
      }
      
      @Async
      public void sendWelcomeEmail(User user) {
          // Runs asynchronously, no return value
          emailService.send(user.getEmail(), "Welcome!");
      }
  }
  ```

  **Controller:**

  ```java
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
      
      @Autowired
      private UserService userService;
      
      @GetMapping("/{id}")
      public CompletableFuture<User> getUser(@PathVariable Long id) {
          return userService.findByIdAsync(id);
      }
      
      @PostMapping
      public ResponseEntity<User> createUser(@RequestBody User user) {
          User created = userService.create(user);
          
          // Send email asynchronously (fire and forget)
          userService.sendWelcomeEmail(created);
          
          return ResponseEntity.status(HttpStatus.CREATED).body(created);
      }
  }
  ```

</details>

<details>
  <summary>CompletableFuture</summary>
  <br/>

  **CompletableFuture** provides powerful async composition.

  **Basic usage:**

  ```java
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
      
      @Autowired
      private UserService userService;
      
      @GetMapping("/{id}")
      public CompletableFuture<User> getUser(@PathVariable Long id) {
          return CompletableFuture.supplyAsync(() -> userService.findById(id));
      }
  }
  ```

  **Chaining operations:**

  ```java
  @GetMapping("/{id}/profile")
  public CompletableFuture<UserProfile> getUserProfile(@PathVariable Long id) {
      return CompletableFuture.supplyAsync(() -> userService.findById(id))
          .thenApply(user -> profileService.getProfile(user))
          .thenApply(profile -> profileService.enrichProfile(profile));
  }
  ```

  **Combining multiple async operations:**

  ```java
  @GetMapping("/{id}/dashboard")
  public CompletableFuture<Dashboard> getDashboard(@PathVariable Long id) {
      CompletableFuture<User> userFuture = 
          CompletableFuture.supplyAsync(() -> userService.findById(id));
      
      CompletableFuture<List<Order>> ordersFuture = 
          CompletableFuture.supplyAsync(() -> orderService.findByUserId(id));
      
      CompletableFuture<List<Notification>> notificationsFuture = 
          CompletableFuture.supplyAsync(() -> notificationService.findByUserId(id));
      
      return CompletableFuture.allOf(userFuture, ordersFuture, notificationsFuture)
          .thenApply(v -> {
              User user = userFuture.join();
              List<Order> orders = ordersFuture.join();
              List<Notification> notifications = notificationsFuture.join();
              return new Dashboard(user, orders, notifications);
          });
  }
  ```

  **Exception handling:**

  ```java
  @GetMapping("/{id}")
  public CompletableFuture<User> getUser(@PathVariable Long id) {
      return CompletableFuture.supplyAsync(() -> userService.findById(id))
          .exceptionally(ex -> {
              log.error("Error fetching user: ", ex);
              throw new RuntimeException("Failed to fetch user", ex);
          });
  }
  ```

</details>

<details>
  <summary>Custom Async Executor</summary>
  <br/>

  **Configure custom thread pool for async operations.**

  **Configuration:**

  ```java
  @Configuration
  @EnableAsync
  public class AsyncConfig implements AsyncConfigurer {
      
      @Override
      public Executor getAsyncExecutor() {
          ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
          executor.setCorePoolSize(5);
          executor.setMaxPoolSize(10);
          executor.setQueueCapacity(100);
          executor.setThreadNamePrefix("async-");
          executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
          executor.initialize();
          return executor;
      }
      
      @Override
      public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
          return (ex, method, params) -> 
              log.error("Async method {} threw exception: ", method.getName(), ex);
      }
  }
  ```

  **Multiple executors:**

  ```java
  @Configuration
  @EnableAsync
  public class AsyncConfig {
      
      @Bean(name = "taskExecutor")
      public Executor taskExecutor() {
          ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
          executor.setCorePoolSize(5);
          executor.setMaxPoolSize(10);
          executor.setQueueCapacity(100);
          executor.setThreadNamePrefix("task-");
          executor.initialize();
          return executor;
      }
      
      @Bean(name = "emailExecutor")
      public Executor emailExecutor() {
          ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
          executor.setCorePoolSize(2);
          executor.setMaxPoolSize(5);
          executor.setQueueCapacity(50);
          executor.setThreadNamePrefix("email-");
          executor.initialize();
          return executor;
      }
  }
  ```

  **Use specific executor:**

  ```java
  @Service
  public class UserService {
      
      @Async("taskExecutor")
      public CompletableFuture<User> findByIdAsync(Long id) {
          return CompletableFuture.completedFuture(userRepository.findById(id).orElseThrow());
      }
      
      @Async("emailExecutor")
      public void sendEmail(String to, String subject, String body) {
          emailService.send(to, subject, body);
      }
  }
  ```

</details>

<details>
  <summary>WebAsyncTask</summary>
  <br/>

  **WebAsyncTask** provides timeout and error handling for Callable.

  **Basic usage:**

  ```java
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
      
      @GetMapping("/{id}")
      public WebAsyncTask<User> getUser(@PathVariable Long id) {
          Callable<User> callable = () -> {
              Thread.sleep(2000);
              return userService.findById(id);
          };
          
          return new WebAsyncTask<>(5000L, callable);  // 5 second timeout
      }
  }
  ```

  **With timeout and error handlers:**

  ```java
  @GetMapping("/{id}")
  public WebAsyncTask<User> getUser(@PathVariable Long id) {
      Callable<User> callable = () -> {
          Thread.sleep(2000);
          return userService.findById(id);
      };
      
      WebAsyncTask<User> asyncTask = new WebAsyncTask<>(5000L, callable);
      
      // Timeout handler
      asyncTask.onTimeout(() -> {
          log.warn("Request timeout for user: {}", id);
          return null;
      });
      
      // Completion handler
      asyncTask.onCompletion(() -> 
          log.info("Request completed for user: {}", id)
      );
      
      // Error handler
      asyncTask.onError(() -> {
          log.error("Request error for user: {}", id);
          return null;
      });
      
      return asyncTask;
  }
  ```

  **With custom executor:**

  ```java
  @GetMapping("/{id}")
  public WebAsyncTask<User> getUser(@PathVariable Long id) {
      Callable<User> callable = () -> userService.findById(id);
      
      return new WebAsyncTask<>(5000L, "taskExecutor", callable);
  }
  ```

</details>

<details>
  <summary>Server-Sent Events (SSE)</summary>
  <br/>

  **SSE** allows server to push updates to client.

  **Basic SSE:**

  ```java
  @RestController
  @RequestMapping("/api/events")
  public class EventController {
      
      @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
      public Flux<String> streamEvents() {
          return Flux.interval(Duration.ofSeconds(1))
              .map(sequence -> "Event " + sequence + " at " + LocalDateTime.now());
      }
  }
  ```

  **SSE with SseEmitter:**

  ```java
  @RestController
  @RequestMapping("/api/notifications")
  public class NotificationController {
      
      @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
      public SseEmitter streamNotifications() {
          SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
          
          // Send events asynchronously
          CompletableFuture.runAsync(() -> {
              try {
                  for (int i = 0; i < 10; i++) {
                      emitter.send(SseEmitter.event()
                          .name("notification")
                          .data("Notification " + i)
                          .id(String.valueOf(i)));
                      
                      Thread.sleep(1000);
                  }
                  emitter.complete();
              } catch (Exception e) {
                  emitter.completeWithError(e);
              }
          });
          
          return emitter;
      }
  }
  ```

  **Real-time updates:**

  ```java
  @RestController
  @RequestMapping("/api/orders")
  public class OrderController {
      
      private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
      
      @GetMapping(value = "/status/{orderId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
      public SseEmitter streamOrderStatus(@PathVariable String orderId) {
          SseEmitter emitter = new SseEmitter(300000L);  // 5 minute timeout
          
          emitters.put(orderId, emitter);
          
          emitter.onCompletion(() -> emitters.remove(orderId));
          emitter.onTimeout(() -> emitters.remove(orderId));
          emitter.onError(e -> emitters.remove(orderId));
          
          return emitter;
      }
      
      @PostMapping("/{orderId}/status")
      public ResponseEntity<String> updateOrderStatus(
          @PathVariable String orderId,
          @RequestBody String status
      ) {
          SseEmitter emitter = emitters.get(orderId);
          
          if (emitter != null) {
              try {
                  emitter.send(SseEmitter.event()
                      .name("status-update")
                      .data(status));
              } catch (IOException e) {
                  emitters.remove(orderId);
              }
          }
          
          return ResponseEntity.ok("Status updated");
      }
  }
  ```

</details>

<details>
  <summary>Best Practices</summary>
  <br/>

  ```java
  // ✅ DO: Use Callable for simple async operations
  @GetMapping("/{id}")
  public Callable<User> getUser(@PathVariable Long id) {
      return () -> userService.findById(id);
  }
  
  // ✅ DO: Use DeferredResult for event-driven scenarios
  @GetMapping("/subscribe")
  public DeferredResult<String> subscribe() {
      DeferredResult<String> result = new DeferredResult<>(30000L);
      // Store and complete later
      return result;
  }
  
  // ✅ DO: Use CompletableFuture for complex async composition
  @GetMapping("/{id}/dashboard")
  public CompletableFuture<Dashboard> getDashboard(@PathVariable Long id) {
      return CompletableFuture.allOf(userFuture, ordersFuture)
          .thenApply(v -> new Dashboard(...));
  }
  
  // ✅ DO: Configure custom thread pool
  @Bean(name = "taskExecutor")
  public Executor taskExecutor() {
      ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
      executor.setCorePoolSize(5);
      executor.setMaxPoolSize(10);
      executor.initialize();
      return executor;
  }
  
  // ✅ DO: Set timeouts for async operations
  DeferredResult<User> result = new DeferredResult<>(5000L);
  WebAsyncTask<User> task = new WebAsyncTask<>(5000L, callable);
  
  // ✅ DO: Handle timeouts
  deferredResult.onTimeout(() -> 
      deferredResult.setErrorResult(new TimeoutException())
  );
  
  // ✅ DO: Handle errors
  CompletableFuture.supplyAsync(() -> userService.findById(id))
      .exceptionally(ex -> {
          log.error("Error: ", ex);
          throw new RuntimeException(ex);
      });
  
  // ✅ DO: Use @Async for fire-and-forget operations
  @Async
  public void sendEmail(String to, String subject) {
      emailService.send(to, subject);
  }
  
  // ✅ DO: Enable async processing
  @Configuration
  @EnableAsync
  public class AsyncConfig {
  }
  
  // ✅ DO: Use appropriate executor for different tasks
  @Async("emailExecutor")
  public void sendEmail() { }
  
  @Async("taskExecutor")
  public CompletableFuture<User> findUser() { }
  
  // ✅ DO: Clean up resources
  deferredResult.onCompletion(() -> cleanup());
  emitter.onCompletion(() -> emitters.remove(id));
  
  // ✅ DO: Use SSE for real-time updates
  @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter streamEvents() {
  }
  
  // ❌ DON'T: Block in async methods
  @Async
  public CompletableFuture<User> findUser() {
      Thread.sleep(5000);  // BAD: Blocks thread
      return CompletableFuture.completedFuture(user);
  }
  
  // ✅ DO: Use non-blocking operations
  @Async
  public CompletableFuture<User> findUser() {
      return CompletableFuture.supplyAsync(() -> userService.findById(id));
  }
  
  // ❌ DON'T: Use @Async on private methods
  @Async
  private void sendEmail() { }  // Won't work (proxy limitation)
  
  // ✅ DO: Use @Async on public methods
  @Async
  public void sendEmail() { }
  
  // ✅ DO: Configure rejection policy
  executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
  
  // ✅ DO: Monitor thread pool
  executor.setThreadNamePrefix("async-");
  log.info("Active threads: {}", executor.getActiveCount());
  ```

  **Summary:**
  + **Async processing** improves scalability by releasing request threads
  + Use **Callable** for simple async operations
  + Use **DeferredResult** for event-driven scenarios with more control
  + Use **CompletableFuture** for complex async composition
  + Use **@Async** for fire-and-forget operations
  + Use **WebAsyncTask** for timeout and error handling
  + Use **SSE** for real-time server-to-client updates
  + Configure **custom thread pool** for better control
  + Set **timeouts** for async operations
  + Handle **errors and timeouts** properly
  + Clean up **resources** on completion
  + Use **non-blocking** operations in async methods
  + **@Async** only works on public methods (proxy limitation)
  + Monitor **thread pool** metrics
  + Use appropriate **executor** for different task types

</details>
