# Spring Core

## Dependency Injection

<details>
  <summary>Constructor vs Setter vs Field Injection</summary>
  <br/>

  Dependency Injection (DI) is a design pattern where Spring provides dependencies to a class instead of the class creating them itself.

  **Why Use Dependency Injection?**

  + **Loose Coupling:** Classes don't create their dependencies
  + **Testability:** Easy to mock dependencies in tests
  + **Maintainability:** Easy to change implementations
  + **Reusability:** Components can be reused with different dependencies

  **Three Types of Dependency Injection:**

  ### 1. Constructor Injection (Recommended ✅)

  Dependencies are provided through the class constructor.

  ```java
  @Service
  public class UserService {
      private final UserRepository userRepository;
      private final EmailService emailService;
      
      // Constructor injection
      @Autowired  // Optional in Spring 4.3+ if only one constructor
      public UserService(UserRepository userRepository, EmailService emailService) {
          this.userRepository = userRepository;
          this.emailService = emailService;
      }
      
      public void registerUser(User user) {
          userRepository.save(user);
          emailService.sendWelcomeEmail(user.getEmail());
      }
  }
  ```

  **Advantages:**
  + ✅ **Immutable:** Dependencies are final (thread-safe)
  + ✅ **Required dependencies:** Compiler ensures all dependencies are provided
  + ✅ **Testability:** Easy to create instances in tests without Spring
  + ✅ **No reflection:** Clear what dependencies are needed
  + ✅ **Prevents circular dependencies:** Fails fast at startup

  ### 2. Setter Injection

  Dependencies are provided through setter methods.

  ```java
  @Service
  public class OrderService {
      private OrderRepository orderRepository;
      private PaymentService paymentService;
      
      // Setter injection
      @Autowired
      public void setOrderRepository(OrderRepository orderRepository) {
          this.orderRepository = orderRepository;
      }
      
      @Autowired
      public void setPaymentService(PaymentService paymentService) {
          this.paymentService = paymentService;
      }
      
      public void processOrder(Order order) {
          orderRepository.save(order);
          paymentService.processPayment(order.getAmount());
      }
  }
  ```

  **Advantages:**
  + ✅ **Optional dependencies:** Can have default values
  + ✅ **Reconfiguration:** Dependencies can be changed after creation
  + ✅ **Circular dependencies:** Can resolve some circular dependency issues

  **Disadvantages:**
  + ❌ **Mutable:** Dependencies can be changed (not thread-safe)
  + ❌ **Incomplete object:** Object can exist without all dependencies
  + ❌ **Verbose:** More code than constructor injection

  **When to use:**
  + Optional dependencies with sensible defaults
  + Legacy code that requires reconfiguration

  ### 3. Field Injection (Not Recommended ❌)

  Dependencies are injected directly into fields using `@Autowired`.

  ```java
  @Service
  public class ProductService {
      @Autowired
      private ProductRepository productRepository;
      
      @Autowired
      private InventoryService inventoryService;
      
      public void addProduct(Product product) {
          productRepository.save(product);
          inventoryService.updateStock(product.getId(), product.getQuantity());
      }
  }
  ```

  **Advantages:**
  + ✅ **Concise:** Less boilerplate code
  + ✅ **Quick:** Fast to write

  **Disadvantages:**
  + ❌ **Hard to test:** Cannot create instance without Spring
  + ❌ **Hidden dependencies:** Not clear what dependencies are needed
  + ❌ **Immutability:** Cannot make fields final
  + ❌ **Tight coupling:** Depends on Spring framework
  + ❌ **Circular dependencies:** Harder to detect

  **Why it's not recommended:**
  ```java
  @Test
  public void testAddProduct() {
      // ❌ Cannot test without Spring
      ProductService service = new ProductService();
      // productRepository and inventoryService are null!
      
      // Need to use reflection or Spring test context
  }
  ```
  
</details>

<details>
  <summary>Comparison Table</summary>
  <br/>

  | Feature | Constructor | Setter | Field |
  |---------|-------------|--------|-------|
  | **Immutability** | ✅ Yes (final) | ❌ No | ❌ No |
  | **Required dependencies** | ✅ Yes | ❌ No | ❌ No |
  | **Testability** | ✅ Easy | ⚠️ Medium | ❌ Hard |
  | **Circular dependencies** | ❌ Fails fast | ✅ Can resolve | ⚠️ Hidden |
  | **Code verbosity** | ⚠️ More code | ⚠️ Most code | ✅ Least code |
  | **Spring coupling** | ✅ Low | ✅ Low | ❌ High |
  | **Recommended** | ✅ Yes | ⚠️ Rarely | ❌ No |
  
</details>

<details>
  <summary>Circular Dependency Problem</summary>
  <br/>

  ```java
  // ❌ Circular dependency with constructor injection
  @Service
  public class ServiceA {
      private final ServiceB serviceB;
      
      public ServiceA(ServiceB serviceB) {
          this.serviceB = serviceB;
      }
  }
  
  @Service
  public class ServiceB {
      private final ServiceA serviceA;
      
      public ServiceB(ServiceA serviceA) {
          this.serviceA = serviceA;
      }
  }
  // Error: The dependencies of some beans in the application context form a cycle
  ```

  **Solutions:**

  **1. Redesign (Best):**
  ```java
  // Extract common logic to a third service
  @Service
  public class ServiceA {
      private final CommonService commonService;
      
      public ServiceA(CommonService commonService) {
          this.commonService = commonService;
      }
  }
  
  @Service
  public class ServiceB {
      private final CommonService commonService;
      
      public ServiceB(CommonService commonService) {
          this.commonService = commonService;
      }
  }
  ```

  **2. Use @Lazy (Temporary fix):**
  ```java
  @Service
  public class ServiceA {
      private final ServiceB serviceB;
      
      public ServiceA(@Lazy ServiceB serviceB) {
          this.serviceB = serviceB;
      }
  }
  ```
  
</details>

<details>
  <summary>Key Takeaways</summary>
  <br/>

  + **Always prefer constructor injection** for required dependencies
  + Make injected fields **final** for immutability
  + Use **setter injection** only for optional dependencies
  + **Avoid field injection** - it makes testing difficult
  + Constructor injection **prevents circular dependencies** by failing fast
  + Use **Lombok's @RequiredArgsConstructor** to reduce boilerplate
  + If you have circular dependencies, **redesign your classes**
  
</details>


## IoC Container

<details>
  <summary>What is Inversion of Control?</summary>
  <br/>

  The IoC (Inversion of Control) Container is the core of Spring Framework that manages object creation, configuration, and lifecycle.

  **What is Inversion of Control?**

  Traditional programming: Your code creates and manages objects.
  IoC: Spring creates and manages objects for you.

  ```java
  // Without IoC - You control object creation
  public class OrderController {
      private OrderService orderService = new OrderService();
      private PaymentService paymentService = new PaymentService();
  }
  
  // With IoC - Spring controls object creation
  @RestController
  public class OrderController {
      private final OrderService orderService;
      private final PaymentService paymentService;
      
      public OrderController(OrderService orderService, PaymentService paymentService) {
          this.orderService = orderService;
          this.paymentService = paymentService;
      }
  }
  ```

  **Benefits:**
  + Loose coupling between components
  + Easier testing (inject mocks)
  + Centralized configuration
  + Better maintainability

</details>

<details>
  <summary>Accessing ApplicationContext in Beans</summary>
  <br/>

  **Method 1: Constructor Injection (Recommended)**

  ```java
  @Component
  public class BeanProvider {
      private final ApplicationContext context;
      
      public BeanProvider(ApplicationContext context) {
          this.context = context;
      }
      
      public <T> T getBean(Class<T> beanClass) {
          return context.getBean(beanClass);
      }
  }
  ```

  **Method 2: ApplicationContextAware Interface**

  ```java
  @Component
  public class BeanProvider implements ApplicationContextAware {
      private ApplicationContext context;
      
      @Override
      public void setApplicationContext(ApplicationContext context) {
          this.context = context;
      }
      
      public <T> T getBean(Class<T> beanClass) {
          return context.getBean(beanClass);
      }
  }
  ```
  
</details>

<details>
  <summary>Multiple Beans of Same Type</summary>
  <br/>

  **Using @Primary:**

  ```java
  @Configuration
  public class DatabaseConfig {
      
      @Bean
      @Primary  // Default bean
      public DataSource primaryDataSource() {
          return DataSourceBuilder.create()
              .url("jdbc:mysql://localhost:3306/primary")
              .build();
      }
      
      @Bean
      public DataSource secondaryDataSource() {
          return DataSourceBuilder.create()
              .url("jdbc:mysql://localhost:3306/secondary")
              .build();
      }
  }
  
  @Service
  public class UserService {
      private final DataSource dataSource;
      
      // primaryDataSource injected automatically
      public UserService(DataSource dataSource) {
          this.dataSource = dataSource;
      }
  }
  ```

  **Using @Qualifier:**

  ```java
  @Service
  public class ReportService {
      private final DataSource primaryDb;
      private final DataSource secondaryDb;
      
      public ReportService(
          @Qualifier("primaryDataSource") DataSource primaryDb,
          @Qualifier("secondaryDataSource") DataSource secondaryDb
      ) {
          this.primaryDb = primaryDb;
          this.secondaryDb = secondaryDb;
      }
  }
  ```
  
</details>

<details>
  <summary>Environment Profiles</summary>
  <br/>

  Use @Profile to register beans conditionally based on environment.

  ```java
  @Configuration
  public class AppConfig {
      
      @Bean
      @Profile("dev")
      public DataSource devDataSource() {
          return new EmbeddedDatabaseBuilder()
              .setType(EmbeddedDatabaseType.H2)
              .build();
      }
      
      @Bean
      @Profile("prod")
      public DataSource prodDataSource() {
          return DataSourceBuilder.create()
              .url("jdbc:mysql://prod-server:3306/db")
              .username("admin")
              .password("secret")
              .build();
      }
  }
  ```

  Activate profile in application.properties:
  ```properties
  spring.profiles.active=dev
  ```

  Or via command line:
  ```bash
  java -jar app.jar --spring.profiles.active=prod
  ```
  
</details>

<details>
  <summary>Bean Creation Timing</summary>
  <br/>

  **Singleton beans (default):** Created at application startup

  ```java
  @Component
  public class UserService {
      public UserService() {
          System.out.println("Created at startup");
      }
  }
  ```

  **Lazy beans:** Created on first use

  ```java
  @Component
  @Lazy
  public class HeavyService {
      public HeavyService() {
          System.out.println("Created on first use");
      }
  }
  ```
  
</details>

<details>
  <summary>ApplicationContext Lifecycle</summary>
  <br/>

  **Closing ApplicationContext:**

  ```java
  // Manual close
  ConfigurableApplicationContext context = 
      new AnnotationConfigApplicationContext(AppConfig.class);
  try {
      UserService service = context.getBean(UserService.class);
      service.doSomething();
  } finally {
      context.close();
  }
  
  // Try-with-resources
  try (ConfigurableApplicationContext context = 
          new AnnotationConfigApplicationContext(AppConfig.class)) {
      UserService service = context.getBean(UserService.class);
      service.doSomething();
  }
  
  // Register shutdown hook (closes on JVM shutdown)
  ConfigurableApplicationContext context = 
      new AnnotationConfigApplicationContext(AppConfig.class);
  context.registerShutdownHook();
  ```

  **Note:** Spring Boot automatically manages ApplicationContext lifecycle.
  
</details>

<details>
  <summary>ApplicationContext Startup Process</summary>
  <br/>

  ```
  1. Load Configuration
     - Read @Configuration classes or XML
     - Scan for @Component, @Service, @Repository
  
  2. Create BeanDefinitions
     - Register all bean definitions
     - Resolve dependencies
  
  3. Instantiate Beans
     - Create singleton beans (unless @Lazy)
     - Follow dependency order
  
  4. Inject Dependencies
     - Constructor injection
     - Setter injection
     - Field injection
  
  5. Initialize Beans
     - @PostConstruct methods
     - Custom init methods
  
  6. Application Ready
     - All beans initialized
     - Ready to use
  ```
  
</details>

<details>
  <summary>Best Practices</summary>
  <br/>

  ```java
  // ✅ DO: Use ApplicationContext (not BeanFactory)
  ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
  
  // ✅ DO: Let Spring inject dependencies
  @Service
  public class UserService {
      private final UserRepository repository;
      
      public UserService(UserRepository repository) {
          this.repository = repository;
      }
  }
  
  // ❌ DON'T: Manually get beans in business logic
  @Service
  public class BadService {
      @Autowired
      private ApplicationContext context;
      
      public void doSomething() {
          UserService service = context.getBean(UserService.class);  // Bad!
      }
  }
  
  // ✅ DO: Use @Primary for default bean
  @Bean
  @Primary
  public DataSource primaryDataSource() {
      return new HikariDataSource();
  }
  
  // ✅ DO: Use @Qualifier for specific bean
  public UserService(@Qualifier("primaryDataSource") DataSource dataSource) {
      this.dataSource = dataSource;
  }
  
  // ✅ DO: Use @Profile for environment-specific beans
  @Bean
  @Profile("prod")
  public DataSource prodDataSource() {
      return DataSourceBuilder.create().build();
  }
  ```
  **Key Points**
  <br/>

  + ApplicationContext is the main Spring container
  + Use ApplicationContext, not BeanFactory
  + Singleton beans created at startup (eager)
  + Let Spring inject dependencies (don't use getBean() in business logic)
  + Use @Primary for default bean when multiple exist
  + Use @Qualifier to specify which bean to inject
  + Use @Profile for environment-specific configuration
  + Spring Boot automatically creates and manages ApplicationContext
</details>

<details>
<summary>ApplicationContext Types</summary>
<br/>

  **1. AnnotationConfigApplicationContext**

  Most common in modern Spring applications. Uses Java-based configuration.

  ```java
  @Configuration
  @ComponentScan("com.example")
  public class AppConfig {
      @Bean
      public UserService userService() {
          return new UserService(userRepository());
      }
      
      @Bean
      public UserRepository userRepository() {
          return new UserRepository();
      }
  }
  
  // Create context
  ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
  UserService service = context.getBean(UserService.class);
  ```

  **2. ClassPathXmlApplicationContext**

  Legacy approach using XML configuration.

  ```xml
  <!-- applicationContext.xml -->
  <beans>
      <bean id="userRepository" class="com.example.UserRepository"/>
      <bean id="userService" class="com.example.UserService">
          <constructor-arg ref="userRepository"/>
      </bean>
  </beans>
  ```

  ```java
  ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
  UserService service = context.getBean(UserService.class);
  ```

  **3. WebApplicationContext**

  Used in Spring MVC web applications. Spring Boot creates this automatically.

  ```java
  @SpringBootApplication
  public class Application {
      public static void main(String[] args) {
          // WebApplicationContext created automatically
          SpringApplication.run(Application.class, args);
      }
  }
  ```
</details>

<details>
<summary>Getting Beans from ApplicationContext</summary>
<br/>

  ```java
  ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
  
  // 1. By type
  UserService service = context.getBean(UserService.class);
  
  // 2. By name
  UserService service2 = (UserService) context.getBean("userService");
  
  // 3. By name and type
  UserService service3 = context.getBean("userService", UserService.class);
  
  // 4. Check if bean exists
  boolean exists = context.containsBean("userService");
  
  // 5. Get all beans of type
  Map<String, UserService> beans = context.getBeansOfType(UserService.class);
  ```

  **Note:** In practice, let Spring inject dependencies instead of manually getting beans.
</details>





<details>
 <summary>BeanFactory vs ApplicationContext</summary>
  Spring provides two types of IoC containers:

  | Feature | BeanFactory | ApplicationContext |
  |---------|-------------|-------------------|
  | Bean loading | Lazy (on demand) | Eager (at startup) |
  | Internationalization | ❌ No | ✅ Yes |
  | Event publishing | ❌ No | ✅ Yes |
  | Annotation support | ❌ Limited | ✅ Full |
  | AOP integration | ❌ Manual | ✅ Automatic |
  | Use case | Legacy/embedded | Modern applications |

  **BeanFactory Example:**
  ```java
  BeanFactory factory = new XmlBeanFactory(new ClassPathResource("beans.xml"));
  UserService service = (UserService) factory.getBean("userService");
  // Bean created only when getBean() is called (lazy)
  ```

  **ApplicationContext Example:**
  ```java
  ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
  UserService service = context.getBean(UserService.class);
  // All singleton beans created at startup (eager)
  ```

  **Recommendation:** Always use ApplicationContext in modern applications.
</details>


## Bean Scopes

<details>
  <summary>Bean Scopes Overview</summary>
  <br/>

  Bean scope defines the lifecycle and visibility of a bean in the Spring container.

  **Available Scopes:**

  | Scope | Instances | Lifecycle | Use Case |
  |-------|-----------|-----------|----------|
  | singleton | 1 per container | Application lifetime | Stateless services, repositories |
  | prototype | New each time | Per request | Stateful objects, commands |
  | request | 1 per HTTP request | HTTP request | Request-specific data |
  | session | 1 per HTTP session | HTTP session | User session data |
  | application | 1 per ServletContext | Application lifetime | Global application state |
  | websocket | 1 per WebSocket | WebSocket lifetime | WebSocket session data |

  **Note:** request, session, application, and websocket scopes are only available in web applications.

</details>

<details>
  <summary>Singleton Scope (Default)</summary>
  <br/>

  **One instance per Spring container.** This is the default scope.

  ```java
  @Component
  @Scope("singleton")  // Optional, this is default
  public class UserService {
      private final UserRepository repository;
      
      public UserService(UserRepository repository) {
          this.repository = repository;
      }
      
      public User findUser(Long id) {
          return repository.findById(id);
      }
  }
  ```

  **Characteristics:**
  + Created once when ApplicationContext starts (eager initialization)
  + Shared across entire application
  + Thread-safe if stateless
  + Best for stateless services

  **Example:**

  ```java
  @SpringBootApplication
  public class Application {
      public static void main(String[] args) {
          ApplicationContext context = SpringApplication.run(Application.class, args);
          
          UserService service1 = context.getBean(UserService.class);
          UserService service2 = context.getBean(UserService.class);
          
          System.out.println(service1 == service2);  // true - same instance
      }
  }
  ```

  **When to use:**
  + Services, repositories, controllers
  + Configuration classes
  + Stateless components
  + Thread-safe objects

  **Thread Safety:**

  ```java
  // ✅ Thread-safe singleton (stateless)
  @Service
  public class OrderService {
      private final OrderRepository repository;  // Immutable dependency
      
      public OrderService(OrderRepository repository) {
          this.repository = repository;
      }
      
      public Order createOrder(OrderRequest request) {
          // No instance variables modified
          return repository.save(new Order(request));
      }
  }
  
  // ❌ NOT thread-safe singleton (stateful)
  @Service
  public class BadOrderService {
      private OrderRequest currentRequest;  // Mutable state - BAD!
      
      public Order createOrder(OrderRequest request) {
          this.currentRequest = request;  // Race condition!
          return processOrder();
      }
  }
  ```

</details>

<details>
  <summary>Prototype Scope</summary>
  <br/>

  **New instance created every time the bean is requested.**

  ```java
  @Component
  @Scope("prototype")
  public class ReportGenerator {
      private String reportData;
      
      public void setReportData(String data) {
          this.reportData = data;
      }
      
      public String generateReport() {
          return "Report: " + reportData;
      }
  }
  ```

  **Characteristics:**
  + New instance created each time
  + Not managed after creation (Spring doesn't call @PreDestroy)
  + Can have state (not shared)
  + More memory overhead

  **Example:**

  ```java
  @SpringBootApplication
  public class Application {
      public static void main(String[] args) {
          ApplicationContext context = SpringApplication.run(Application.class, args);
          
          ReportGenerator gen1 = context.getBean(ReportGenerator.class);
          ReportGenerator gen2 = context.getBean(ReportGenerator.class);
          
          System.out.println(gen1 == gen2);  // false - different instances
      }
  }
  ```

  **When to use:**
  + Stateful objects
  + Objects that maintain state per operation
  + Command objects
  + Objects with different configurations

  **Prototype with Singleton:**

  ```java
  // Problem: Singleton holding prototype
  @Service
  public class OrderService {
      @Autowired
      private ReportGenerator reportGenerator;  // Only injected once!
      
      public String generateReport() {
          return reportGenerator.generateReport();  // Same instance always
      }
  }
  
  // Solution 1: Inject ApplicationContext
  @Service
  public class OrderService {
      private final ApplicationContext context;
      
      public OrderService(ApplicationContext context) {
          this.context = context;
      }
      
      public String generateReport() {
          ReportGenerator generator = context.getBean(ReportGenerator.class);
          return generator.generateReport();  // New instance each time
      }
  }
  
  // Solution 2: Use ObjectFactory
  @Service
  public class OrderService {
      private final ObjectFactory<ReportGenerator> generatorFactory;
      
      public OrderService(ObjectFactory<ReportGenerator> generatorFactory) {
          this.generatorFactory = generatorFactory;
      }
      
      public String generateReport() {
          ReportGenerator generator = generatorFactory.getObject();
          return generator.generateReport();  // New instance each time
      }
  }
  
  // Solution 3: Use @Lookup (method injection)
  @Service
  public abstract class OrderService {
      public String generateReport() {
          ReportGenerator generator = createReportGenerator();
          return generator.generateReport();
      }
      
      @Lookup
      protected abstract ReportGenerator createReportGenerator();
  }
  ```

</details>

<details>
  <summary>Request Scope (Web Only)</summary>
  <br/>

  **One instance per HTTP request.** Only available in web applications.

  ```java
  @Component
  @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
  public class RequestContext {
      private String requestId;
      private String userId;
      
      public void setRequestId(String requestId) {
          this.requestId = requestId;
      }
      
      public String getRequestId() {
          return requestId;
      }
      
      public void setUserId(String userId) {
          this.userId = userId;
      }
      
      public String getUserId() {
          return userId;
      }
  }
  ```

  **Or using annotation:**

  ```java
  @Component
  @RequestScope
  public class RequestContext {
      private String requestId;
      private String userId;
      
      // getters and setters
  }
  ```

  **Characteristics:**
  + New instance for each HTTP request
  + Destroyed when request completes
  + Shared within single request
  + Requires proxy mode for injection into singletons

  **Example Usage:**

  ```java
  @RestController
  public class UserController {
      private final RequestContext requestContext;
      
      public UserController(RequestContext requestContext) {
          this.requestContext = requestContext;  // Proxy injected
      }
      
      @GetMapping("/users/{id}")
      public User getUser(@PathVariable Long id, HttpServletRequest request) {
          requestContext.setRequestId(request.getHeader("X-Request-ID"));
          requestContext.setUserId(String.valueOf(id));
          
          return userService.findUser(id);
      }
  }
  
  @Service
  public class UserService {
      private final RequestContext requestContext;
      
      public UserService(RequestContext requestContext) {
          this.requestContext = requestContext;
      }
      
      public User findUser(Long id) {
          String requestId = requestContext.getRequestId();
          System.out.println("Processing request: " + requestId);
          return repository.findById(id);
      }
  }
  ```

  **When to use:**
  + Request-specific data (request ID, correlation ID)
  + User information for current request
  + Request metadata
  + Temporary data needed across multiple components in same request

  **Why proxyMode is needed:**

  ```java
  // Without proxy: ERROR!
  @Component
  @RequestScope  // No proxyMode
  public class RequestContext { }
  
  @Service  // Singleton
  public class UserService {
      @Autowired
      private RequestContext context;  // Error: Cannot inject request-scoped bean into singleton
  }
  
  // With proxy: Works!
  @Component
  @Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
  public class RequestContext { }
  
  @Service  // Singleton
  public class UserService {
      @Autowired
      private RequestContext context;  // OK: Proxy injected, delegates to actual bean
  }
  ```

</details>

<details>
  <summary>Session Scope (Web Only)</summary>
  <br/>

  **One instance per HTTP session.** Only available in web applications.

  ```java
  @Component
  @Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
  public class ShoppingCart {
      private List<CartItem> items = new ArrayList<>();
      
      public void addItem(CartItem item) {
          items.add(item);
      }
      
      public List<CartItem> getItems() {
          return items;
      }
      
      public void clear() {
          items.clear();
      }
  }
  ```

  **Or using annotation:**

  ```java
  @Component
  @SessionScope
  public class ShoppingCart {
      private List<CartItem> items = new ArrayList<>();
      
      // methods
  }
  ```

  **Characteristics:**
  + One instance per user session
  + Persists across multiple requests from same user
  + Destroyed when session expires
  + Requires proxy mode for injection into singletons

  **Example Usage:**

  ```java
  @RestController
  public class CartController {
      private final ShoppingCart cart;
      
      public CartController(ShoppingCart cart) {
          this.cart = cart;  // Proxy injected
      }
      
      @PostMapping("/cart/add")
      public ResponseEntity<Void> addToCart(@RequestBody CartItem item) {
          cart.addItem(item);
          return ResponseEntity.ok().build();
      }
      
      @GetMapping("/cart")
      public List<CartItem> getCart() {
          return cart.getItems();  // Same cart for this user's session
      }
      
      @DeleteMapping("/cart")
      public ResponseEntity<Void> clearCart() {
          cart.clear();
          return ResponseEntity.ok().build();
      }
  }
  ```

  **When to use:**
  + Shopping cart
  + User preferences
  + Multi-step forms
  + User session data
  + Temporary user state

  **Session Timeout:**

  ```properties
  # application.properties
  server.servlet.session.timeout=30m
  ```

</details>

<details>
  <summary>Application Scope (Web Only)</summary>
  <br/>

  **One instance per ServletContext.** Similar to singleton but specific to web applications.

  ```java
  @Component
  @ApplicationScope
  public class ApplicationMetrics {
      private final AtomicLong requestCount = new AtomicLong(0);
      private final AtomicLong errorCount = new AtomicLong(0);
      
      public void incrementRequestCount() {
          requestCount.incrementAndGet();
      }
      
      public void incrementErrorCount() {
          errorCount.incrementAndGet();
      }
      
      public long getRequestCount() {
          return requestCount.get();
      }
      
      public long getErrorCount() {
          return errorCount.get();
      }
  }
  ```

  **Characteristics:**
  + One instance per web application
  + Shared across all sessions and requests
  + Similar to singleton but web-specific
  + Lifecycle tied to ServletContext

  **When to use:**
  + Application-wide counters
  + Global configuration
  + Shared cache
  + Application metrics

</details>

<details>
  <summary>Scope Comparison</summary>
  <br/>

  ```java
  @Configuration
  public class ScopeExamples {
      
      // Singleton - one instance for entire application
      @Bean
      @Scope("singleton")
      public ConfigService configService() {
          return new ConfigService();
      }
      
      // Prototype - new instance every time
      @Bean
      @Scope("prototype")
      public ReportGenerator reportGenerator() {
          return new ReportGenerator();
      }
      
      // Request - one instance per HTTP request
      @Bean
      @RequestScope
      public RequestLogger requestLogger() {
          return new RequestLogger();
      }
      
      // Session - one instance per HTTP session
      @Bean
      @SessionScope
      public UserPreferences userPreferences() {
          return new UserPreferences();
      }
      
      // Application - one instance per ServletContext
      @Bean
      @ApplicationScope
      public AppMetrics appMetrics() {
          return new AppMetrics();
      }
  }
  ```

  **Lifecycle Comparison:**

  ```
  Application Start
  ├── Singleton beans created
  └── Application scope beans created
  
  HTTP Request 1 (User A, Session 1)
  ├── Request scope bean created
  ├── Session scope bean created (first request in session)
  └── Request ends → Request scope bean destroyed
  
  HTTP Request 2 (User A, Session 1)
  ├── Request scope bean created (new)
  ├── Session scope bean reused (same session)
  └── Request ends → Request scope bean destroyed
  
  HTTP Request 3 (User B, Session 2)
  ├── Request scope bean created (new)
  ├── Session scope bean created (new session)
  └── Request ends → Request scope bean destroyed
  
  Session 1 expires
  └── Session scope bean destroyed
  
  Application Shutdown
  ├── Singleton beans destroyed
  └── Application scope beans destroyed
  ```

</details>

<details>
  <summary>Best Practices</summary>
  <br/>

  ```java
  // ✅ DO: Use singleton for stateless services
  @Service
  public class UserService {
      private final UserRepository repository;
      
      public UserService(UserRepository repository) {
          this.repository = repository;
      }
  }
  
  // ✅ DO: Use prototype for stateful objects
  @Component
  @Scope("prototype")
  public class OrderProcessor {
      private Order currentOrder;
      
      public void process(Order order) {
          this.currentOrder = order;
          // Process order
      }
  }
  
  // ✅ DO: Use request scope for request-specific data
  @Component
  @RequestScope
  public class RequestContext {
      private String correlationId;
      // Request-specific data
  }
  
  // ✅ DO: Use session scope for user session data
  @Component
  @SessionScope
  public class ShoppingCart {
      private List<Item> items = new ArrayList<>();
      // User's cart
  }
  
  // ❌ DON'T: Store mutable state in singleton
  @Service
  public class BadService {
      private String currentUser;  // BAD! Not thread-safe
      
      public void setCurrentUser(String user) {
          this.currentUser = user;  // Race condition!
      }
  }
  
  // ✅ DO: Use proxyMode for narrower scopes in singletons
  @Component
  @Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
  public class RequestContext { }
  
  // ❌ DON'T: Inject narrower scope without proxy
  @Component
  @RequestScope  // No proxyMode - will fail!
  public class BadRequestContext { }
  ```

  **Key Points**
  <br/>

  + `Singleton` is the default scope (one instance per container)
  + `Prototype` creates new instance each time (not managed after creation)
  + `Request` scope is per HTTP request (web only)
  + `Session` scope is per HTTP session (web only)
  + `Application` scope is per ServletContext (web only)
  + Use `proxyMode` when injecting narrower scopes into singletons
  + Singleton beans should be **stateless** for thread safety
  + Prototype beans are **not destroyed** by Spring (@PreDestroy not called)
  + Request/Session scopes require **web application context**

</details>

## Bean Lifecycle

<details>
  <summary>Bean Lifecycle Overview</summary>
  <br/>

  Spring manages the complete lifecycle of beans from creation to destruction.

  **Bean Lifecycle Phases:**

  ```
  1. Instantiation
     ↓
  2. Populate Properties (Dependency Injection)
     ↓
  3. BeanNameAware.setBeanName()
     ↓
  4. BeanFactoryAware.setBeanFactory()
     ↓
  5. ApplicationContextAware.setApplicationContext()
     ↓
  6. BeanPostProcessor.postProcessBeforeInitialization()
     ↓
  7. @PostConstruct
     ↓
  8. InitializingBean.afterPropertiesSet()
     ↓
  9. Custom init-method
     ↓
  10. BeanPostProcessor.postProcessAfterInitialization()
     ↓
  11. Bean Ready to Use
     ↓
  12. @PreDestroy
     ↓
  13. DisposableBean.destroy()
     ↓
  14. Custom destroy-method
  ```

  **Most Common Lifecycle Hooks:**
  + `@PostConstruct` - After dependency injection
  + `@PreDestroy` - Before bean destruction
  + Custom init/destroy methods

</details>

<details>
  <summary>@PostConstruct - Initialization</summary>
  <br/>

  **Executed after dependency injection is complete.**

  ```java
  @Service
  public class UserService {
      private final UserRepository repository;
      private Cache<Long, User> cache;
      
      public UserService(UserRepository repository) {
          this.repository = repository;
          // cache is null here
      }
      
      @PostConstruct
      public void init() {
          // All dependencies injected, safe to initialize
          this.cache = CacheBuilder.newBuilder()
              .maximumSize(1000)
              .build();
          System.out.println("UserService initialized");
      }
      
      public User findUser(Long id) {
          return cache.get(id, () -> repository.findById(id));
      }
  }
  ```

  **When to use:**
  + Initialize resources after dependency injection
  + Load configuration or data
  + Set up connections
  + Validate dependencies

  **Example - Database Connection:**

  ```java
  @Component
  public class DatabaseConnection {
      @Value("${db.url}")
      private String dbUrl;
      
      @Value("${db.username}")
      private String username;
      
      private Connection connection;
      
      @PostConstruct
      public void connect() {
          try {
              connection = DriverManager.getConnection(dbUrl, username, password);
              System.out.println("Database connected");
          } catch (SQLException e) {
              throw new RuntimeException("Failed to connect to database", e);
          }
      }
      
      public Connection getConnection() {
          return connection;
      }
  }
  ```

  **Rules:**
  + Method can have any name
  + Must be void return type
  + Cannot have parameters
  + Can throw checked exceptions
  + Called only once per bean instance

</details>

<details>
  <summary>@PreDestroy - Cleanup</summary>
  <br/>

  **Executed before bean is destroyed.**

  ```java
  @Service
  public class FileProcessor {
      private ExecutorService executor;
      
      @PostConstruct
      public void init() {
          executor = Executors.newFixedThreadPool(10);
          System.out.println("Executor service started");
      }
      
      @PreDestroy
      public void cleanup() {
          if (executor != null) {
              executor.shutdown();
              try {
                  if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                      executor.shutdownNow();
                  }
                  System.out.println("Executor service stopped");
              } catch (InterruptedException e) {
                  executor.shutdownNow();
              }
          }
      }
      
      public void processFile(File file) {
          executor.submit(() -> {
              // Process file
          });
      }
  }
  ```

  **When to use:**
  + Close connections (database, network)
  + Release resources (files, threads)
  + Save state
  + Clean up temporary data

  **Example - Cache Cleanup:**

  ```java
  @Component
  public class CacheManager {
      private Cache<String, Object> cache;
      
      @PostConstruct
      public void init() {
          cache = CacheBuilder.newBuilder().build();
      }
      
      @PreDestroy
      public void cleanup() {
          if (cache != null) {
              cache.invalidateAll();
              cache.cleanUp();
              System.out.println("Cache cleared");
          }
      }
  }
  ```

  **Rules:**
  + Method can have any name
  + Must be void return type
  + Cannot have parameters
  + Can throw checked exceptions
  + Not called for prototype-scoped beans

  **Important:** @PreDestroy is NOT called for prototype beans!

  ```java
  @Component
  @Scope("prototype")
  public class PrototypeBean {
      @PreDestroy
      public void cleanup() {
          // This will NEVER be called!
          System.out.println("Cleaning up");
      }
  }
  ```

</details>

<details>
  <summary>InitializingBean Interface</summary>
  <br/>

  **Alternative to @PostConstruct using interface.**

  ```java
  @Service
  public class EmailService implements InitializingBean {
      private final EmailConfig config;
      private MailSender mailSender;
      
      public EmailService(EmailConfig config) {
          this.config = config;
      }
      
      @Override
      public void afterPropertiesSet() throws Exception {
          // Called after all properties are set
          mailSender = new MailSender(config.getHost(), config.getPort());
          System.out.println("Email service initialized");
      }
      
      public void sendEmail(String to, String message) {
          mailSender.send(to, message);
      }
  }
  ```

  **Comparison with @PostConstruct:**

  | Feature | @PostConstruct | InitializingBean |
  |---------|---------------|------------------|
  | Coupling | Low (JSR-250) | High (Spring interface) |
  | Checked exceptions | ✅ Yes | ✅ Yes |
  | Multiple methods | ✅ Yes | ❌ No |
  | Recommended | ✅ Yes | ⚠️ Rarely |

  **Recommendation:** Prefer @PostConstruct over InitializingBean.

</details>

<details>
  <summary>DisposableBean Interface</summary>
  <br/>

  **Alternative to @PreDestroy using interface.**

  ```java
  @Service
  public class ConnectionPool implements DisposableBean {
      private List<Connection> connections;
      
      @PostConstruct
      public void init() {
          connections = new ArrayList<>();
          // Create connections
      }
      
      @Override
      public void destroy() throws Exception {
          // Called before bean destruction
          for (Connection conn : connections) {
              conn.close();
          }
          System.out.println("Connection pool closed");
      }
  }
  ```

  **Comparison with @PreDestroy:**

  | Feature | @PreDestroy | DisposableBean |
  |---------|------------|----------------|
  | Coupling | Low (JSR-250) | High (Spring interface) |
  | Checked exceptions | ✅ Yes | ✅ Yes |
  | Multiple methods | ✅ Yes | ❌ No |
  | Recommended | ✅ Yes | ⚠️ Rarely |

  **Recommendation:** Prefer @PreDestroy over DisposableBean.

</details>

<details>
  <summary>Custom Init and Destroy Methods</summary>
  <br/>

  **Define custom initialization and destruction methods in @Bean.**

  ```java
  public class DataSource {
      private Connection connection;
      
      // Custom init method
      public void initialize() {
          connection = createConnection();
          System.out.println("DataSource initialized");
      }
      
      // Custom destroy method
      public void close() {
          if (connection != null) {
              connection.close();
              System.out.println("DataSource closed");
          }
      }
      
      private Connection createConnection() {
          // Create connection
          return null;
      }
  }
  
  @Configuration
  public class AppConfig {
      @Bean(initMethod = "initialize", destroyMethod = "close")
      public DataSource dataSource() {
          return new DataSource();
      }
  }
  ```

  **When to use:**
  + Third-party classes (cannot add annotations)
  + Legacy code
  + Explicit method naming

  **Example - Thread Pool:**

  ```java
  public class TaskExecutor {
      private ExecutorService executor;
      
      public void start() {
          executor = Executors.newFixedThreadPool(5);
          System.out.println("Executor started");
      }
      
      public void shutdown() {
          if (executor != null) {
              executor.shutdown();
              System.out.println("Executor stopped");
          }
      }
      
      public void execute(Runnable task) {
          executor.execute(task);
      }
  }
  
  @Configuration
  public class AppConfig {
      @Bean(initMethod = "start", destroyMethod = "shutdown")
      public TaskExecutor taskExecutor() {
          return new TaskExecutor();
      }
  }
  ```

  **Auto-detection of destroy method:**

  Spring automatically detects `close()` or `shutdown()` methods.

  ```java
  @Bean  // destroyMethod auto-detected
  public DataSource dataSource() {
      return new HikariDataSource();  // Has close() method
  }
  
  @Bean(destroyMethod = "")  // Disable auto-detection
  public DataSource dataSource() {
      return new HikariDataSource();
  }
  ```

</details>

<details>
  <summary>Lifecycle Method Execution Order</summary>
  <br/>

  **When multiple lifecycle methods exist:**

  ```java
  @Component
  @Scope("singleton")
  public class LifecycleBean implements InitializingBean, DisposableBean {
      
      public LifecycleBean() {
          System.out.println("1. Constructor called");
      }
      
      @PostConstruct
      public void postConstruct() {
          System.out.println("2. @PostConstruct called");
      }
      
      @Override
      public void afterPropertiesSet() {
          System.out.println("3. afterPropertiesSet() called");
      }
      
      public void customInit() {
          System.out.println("4. customInit() called");
      }
      
      // Bean is now ready to use
      
      @PreDestroy
      public void preDestroy() {
          System.out.println("5. @PreDestroy called");
      }
      
      @Override
      public void destroy() {
          System.out.println("6. destroy() called");
      }
      
      public void customDestroy() {
          System.out.println("7. customDestroy() called");
      }
  }
  
  @Configuration
  public class AppConfig {
      @Bean(initMethod = "customInit", destroyMethod = "customDestroy")
      public LifecycleBean lifecycleBean() {
          return new LifecycleBean();
      }
  }
  ```

  **Output:**
  ```
  1. Constructor called
  2. @PostConstruct called
  3. afterPropertiesSet() called
  4. customInit() called
  5. @PreDestroy called
  6. destroy() called
  7. customDestroy() called
  ```

  **Recommendation:** Use only one initialization and one destruction method to avoid confusion.

</details>

<details>
  <summary>Lifecycle with Different Scopes</summary>
  <br/>

  **Singleton Scope:**

  ```java
  @Component
  public class SingletonBean {
      @PostConstruct
      public void init() {
          System.out.println("Singleton init - called once at startup");
      }
      
      @PreDestroy
      public void cleanup() {
          System.out.println("Singleton cleanup - called at shutdown");
      }
  }
  ```

  **Prototype Scope:**

  ```java
  @Component
  @Scope("prototype")
  public class PrototypeBean {
      @PostConstruct
      public void init() {
          System.out.println("Prototype init - called each time bean is created");
      }
      
      @PreDestroy
      public void cleanup() {
          System.out.println("Prototype cleanup - NEVER CALLED!");
      }
  }
  ```

  **Request Scope:**

  ```java
  @Component
  @RequestScope
  public class RequestBean {
      @PostConstruct
      public void init() {
          System.out.println("Request init - called per HTTP request");
      }
      
      @PreDestroy
      public void cleanup() {
          System.out.println("Request cleanup - called when request ends");
      }
  }
  ```

  **Key Points:**
  + Singleton: Init at startup, destroy at shutdown
  + Prototype: Init each time, destroy NEVER called
  + Request/Session: Init per request/session, destroy when request/session ends

</details>

<details>
  <summary>Common Use Cases</summary>
  <br/>

  **1. Database Connection Pool:**

  ```java
  @Component
  public class ConnectionPool {
      private HikariDataSource dataSource;
      
      @PostConstruct
      public void init() {
          HikariConfig config = new HikariConfig();
          config.setJdbcUrl("jdbc:mysql://localhost:3306/db");
          config.setMaximumPoolSize(10);
          dataSource = new HikariDataSource(config);
      }
      
      @PreDestroy
      public void cleanup() {
          if (dataSource != null) {
              dataSource.close();
          }
      }
  }
  ```

  **2. Cache Initialization:**

  ```java
  @Service
  public class ProductService {
      private final ProductRepository repository;
      private Map<Long, Product> cache;
      
      public ProductService(ProductRepository repository) {
          this.repository = repository;
      }
      
      @PostConstruct
      public void loadCache() {
          cache = new HashMap<>();
          List<Product> products = repository.findAll();
          products.forEach(p -> cache.put(p.getId(), p));
          System.out.println("Loaded " + cache.size() + " products into cache");
      }
      
      public Product findProduct(Long id) {
          return cache.get(id);
      }
  }
  ```

  **3. Scheduled Task Setup:**

  ```java
  @Component
  public class ScheduledTasks {
      private ScheduledExecutorService scheduler;
      
      @PostConstruct
      public void startScheduler() {
          scheduler = Executors.newScheduledThreadPool(1);
          scheduler.scheduleAtFixedRate(
              () -> System.out.println("Task executed"),
              0, 1, TimeUnit.MINUTES
          );
      }
      
      @PreDestroy
      public void stopScheduler() {
          if (scheduler != null) {
              scheduler.shutdown();
          }
      }
  }
  ```

  **4. Configuration Validation:**

  ```java
  @Component
  public class AppConfig {
      @Value("${api.key}")
      private String apiKey;
      
      @Value("${api.url}")
      private String apiUrl;
      
      @PostConstruct
      public void validate() {
          if (apiKey == null || apiKey.isEmpty()) {
              throw new IllegalStateException("API key is required");
          }
          if (apiUrl == null || apiUrl.isEmpty()) {
              throw new IllegalStateException("API URL is required");
          }
          System.out.println("Configuration validated");
      }
  }
  ```

</details>

<details>
  <summary>Best Practices</summary>
  <br/>

  ```java
  // ✅ DO: Use @PostConstruct for initialization
  @Service
  public class GoodService {
      private Cache cache;
      
      @PostConstruct
      public void init() {
          cache = CacheBuilder.newBuilder().build();
      }
  }
  
  // ❌ DON'T: Initialize in constructor (dependencies may not be ready)
  @Service
  public class BadService {
      private Cache cache;
      
      @Autowired
      private ConfigService config;
      
      public BadService() {
          // config is null here!
          cache = CacheBuilder.newBuilder()
              .maximumSize(config.getCacheSize())  // NullPointerException!
              .build();
      }
  }
  
  // ✅ DO: Use @PreDestroy for cleanup
  @Service
  public class GoodService {
      private Connection connection;
      
      @PreDestroy
      public void cleanup() {
          if (connection != null) {
              connection.close();
          }
      }
  }
  
  // ❌ DON'T: Forget to clean up resources
  @Service
  public class BadService {
      private Connection connection;
      
      // No cleanup - connection leak!
  }
  
  // ✅ DO: Handle exceptions in lifecycle methods
  @Component
  public class GoodComponent {
      @PostConstruct
      public void init() {
          try {
              // Initialization logic
          } catch (Exception e) {
              throw new RuntimeException("Initialization failed", e);
          }
      }
  }
  
  // ✅ DO: Keep lifecycle methods simple
  @Service
  public class GoodService {
      @PostConstruct
      public void init() {
          // Simple initialization
          loadConfiguration();
      }
      
      private void loadConfiguration() {
          // Complex logic in separate method
      }
  }
  
  // ❌ DON'T: Use lifecycle methods in prototype beans for cleanup
  @Component
  @Scope("prototype")
  public class BadPrototype {
      @PreDestroy
      public void cleanup() {
          // This will NEVER be called!
      }
  }
  ```

  **Key Points**
  <br/>

  + `@PostConstruct` runs after dependency injection is complete
  + `@PreDestroy` runs before bean destruction
  + Use @PostConstruct for initialization (not constructor)
  + Use @PreDestroy for resource cleanup
  + @PreDestroy is **NOT called** for prototype-scoped beans
  + Prefer **@PostConstruct/@PreDestroy** over InitializingBean/DisposableBean
  + Use **initMethod/destroyMethod** for third-party classes
  + Lifecycle methods must be **void** and have **no parameters**
  + Multiple lifecycle methods execute in specific order
  + Always handle exceptions in lifecycle methods

</details>

## AOP (Aspect-Oriented Programming)

<details>
  <summary>What is AOP?</summary>
  <br/>

  AOP (Aspect-Oriented Programming) allows you to separate cross-cutting concerns from business logic.

  **What are Cross-Cutting Concerns?**

  Functionality that spans multiple parts of an application:
  + Logging
  + Security
  + Transaction management
  + Performance monitoring
  + Error handling
  + Caching

  **Without AOP:**

  ```java
  @Service
  public class UserService {
      public User createUser(User user) {
          // Logging
          logger.info("Creating user: " + user.getName());
          
          // Security check
          if (!securityContext.hasPermission("CREATE_USER")) {
              throw new SecurityException("Access denied");
          }
          
          // Performance monitoring
          long startTime = System.currentTimeMillis();
          
          // Actual business logic
          User savedUser = userRepository.save(user);
          
          // Performance monitoring
          long endTime = System.currentTimeMillis();
          logger.info("Execution time: " + (endTime - startTime) + "ms");
          
          return savedUser;
      }
  }
  ```

  **With AOP:**

  ```java
  @Service
  public class UserService {
      @Loggable
      @Secured("CREATE_USER")
      @PerformanceMonitored
      public User createUser(User user) {
          // Only business logic
          return userRepository.save(user);
      }
  }
  ```

  **Benefits:**
  + Clean separation of concerns
  + Reusable cross-cutting logic
  + Less code duplication
  + Easier maintenance

</details>

<details>
  <summary>AOP Core Concepts</summary>
  <br/>

  **Key Terms:**

  | Term | Description | Example |
  |------|-------------|---------|
  | **Aspect** | Module containing cross-cutting logic | Logging aspect, security aspect |
  | **Join Point** | Point in program execution | Method execution, exception thrown |
  | **Pointcut** | Expression matching join points | All methods in service layer |
  | **Advice** | Action taken at join point | Before, after, around method |
  | **Target Object** | Object being advised | UserService instance |
  | **Weaving** | Linking aspects with objects | Compile-time, load-time, runtime |

  **Visual Example:**

  ```
  Target Object: UserService
  ├── Method: createUser()
  │   ├── Join Point: Method execution
  │   ├── Pointcut: execution(* com.example.service.*.*(..))
  │   └── Advice: Log before and after
  └── Aspect: LoggingAspect
      └── Weaving: Runtime (Spring AOP)
  ```

</details>

<details>
  <summary>Advice Types: @Before, @After, @AfterReturning, @AfterThrowing, @Around</summary>
  <br/>

  **1. @Before - Execute before method**

  ```java
  @Aspect
  @Component
  public class LoggingAspect {
      
      @Before("execution(* com.example.service.*.*(..))")
      public void logBefore(JoinPoint joinPoint) {
          String methodName = joinPoint.getSignature().getName();
          System.out.println("Before: " + methodName);
      }
  }
  ```

  **2. @After - Execute after method (always)**

  ```java
  @Aspect
  @Component
  public class LoggingAspect {
      
      @After("execution(* com.example.service.*.*(..))")
      public void logAfter(JoinPoint joinPoint) {
          String methodName = joinPoint.getSignature().getName();
          System.out.println("After: " + methodName);
      }
  }
  ```

  **3. @AfterReturning - Execute after successful return**

  ```java
  @Aspect
  @Component
  public class LoggingAspect {
      
      @AfterReturning(
          pointcut = "execution(* com.example.service.*.*(..))",
          returning = "result"
      )
      public void logAfterReturning(JoinPoint joinPoint, Object result) {
          String methodName = joinPoint.getSignature().getName();
          System.out.println("After returning: " + methodName + ", result: " + result);
      }
  }
  ```

  **4. @AfterThrowing - Execute after exception**

  ```java
  @Aspect
  @Component
  public class ExceptionAspect {
      
      @AfterThrowing(
          pointcut = "execution(* com.example.service.*.*(..))",
          throwing = "exception"
      )
      public void logException(JoinPoint joinPoint, Exception exception) {
          String methodName = joinPoint.getSignature().getName();
          System.out.println("Exception in: " + methodName + ", error: " + exception.getMessage());
      }
  }
  ```

  **5. @Around - Execute around method (most powerful)**

  ```java
  @Aspect
  @Component
  public class PerformanceAspect {
      
      @Around("execution(* com.example.service.*.*(..))")
      public Object measurePerformance(ProceedingJoinPoint joinPoint) throws Throwable {
          long startTime = System.currentTimeMillis();
          
          // Execute the method
          Object result = joinPoint.proceed();
          
          long endTime = System.currentTimeMillis();
          String methodName = joinPoint.getSignature().getName();
          System.out.println(methodName + " took " + (endTime - startTime) + "ms");
          
          return result;
      }
  }
  ```

  **Execution Order:**

  ```
  @Around (before part)
  ↓
  @Before
  ↓
  Method Execution
  ↓
  @AfterReturning (if success) OR @AfterThrowing (if exception)
  ↓
  @After (always)
  ↓
  @Around (after part)
  ```

</details>

<details>
  <summary>Pointcut Expressions</summary>
  <br/>

  **Basic Syntax:**

  ```java
  execution(modifiers? return-type declaring-type? method-name(params) throws?)
  ```

  **Common Patterns:**

  ```java
  // All methods in UserService
  execution(* com.example.service.UserService.*(..))
  
  // All methods in service package
  execution(* com.example.service.*.*(..))
  
  // All methods in service package and subpackages
  execution(* com.example.service..*.*(..))
  
  // All public methods
  execution(public * *(..))
  
  // All methods returning User
  execution(com.example.model.User *(..))
  
  // All methods starting with "find"
  execution(* find*(..))
  
  // All methods with single String parameter
  execution(* *(String))
  
  // All methods with any parameters
  execution(* *(..))
  
  // All methods in classes annotated with @Service
  within(@org.springframework.stereotype.Service *)
  
  // All methods annotated with @Transactional
  @annotation(org.springframework.transaction.annotation.Transactional)
  ```

  **Combining Pointcuts:**

  ```java
  @Aspect
  @Component
  public class CombinedAspect {
      
      // Define reusable pointcuts
      @Pointcut("execution(* com.example.service.*.*(..))")
      public void serviceMethods() {}
      
      @Pointcut("execution(* com.example.repository.*.*(..))")
      public void repositoryMethods() {}
      
      @Pointcut("@annotation(org.springframework.transaction.annotation.Transactional)")
      public void transactionalMethods() {}
      
      // Combine with AND
      @Before("serviceMethods() && transactionalMethods()")
      public void beforeTransactionalService(JoinPoint joinPoint) {
          System.out.println("Before transactional service method");
      }
      
      // Combine with OR
      @Before("serviceMethods() || repositoryMethods()")
      public void beforeServiceOrRepository(JoinPoint joinPoint) {
          System.out.println("Before service or repository method");
      }
      
      // Combine with NOT
      @Before("serviceMethods() && !transactionalMethods()")
      public void beforeNonTransactionalService(JoinPoint joinPoint) {
          System.out.println("Before non-transactional service method");
      }
  }
  ```

</details>

<details>
  <summary>Common Use Case: Logging</summary>
  <br/>

  ```java
  @Aspect
  @Component
  @Slf4j
  public class LoggingAspect {
      
      @Around("execution(* com.example.service.*.*(..))")
      public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
          String className = joinPoint.getTarget().getClass().getSimpleName();
          String methodName = joinPoint.getSignature().getName();
          Object[] args = joinPoint.getArgs();
          
          log.info("Calling {}.{} with args: {}", className, methodName, Arrays.toString(args));
          
          try {
              Object result = joinPoint.proceed();
              log.info("{}.{} returned: {}", className, methodName, result);
              return result;
          } catch (Exception e) {
              log.error("{}.{} threw exception: {}", className, methodName, e.getMessage());
              throw e;
          }
      }
  }
  ```

  **Output:**
  ```
  INFO: Calling UserService.createUser with args: [User(name=John)]
  INFO: UserService.createUser returned: User(id=1, name=John)
  ```

</details>

<details>
  <summary>Common Use Case: Performance Monitoring</summary>
  <br/>

  ```java
  @Aspect
  @Component
  @Slf4j
  public class PerformanceAspect {
      
      @Around("execution(* com.example.service.*.*(..))")
      public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
          long startTime = System.currentTimeMillis();
          
          Object result = joinPoint.proceed();
          
          long executionTime = System.currentTimeMillis() - startTime;
          String methodName = joinPoint.getSignature().toShortString();
          
          if (executionTime > 1000) {
              log.warn("SLOW: {} took {}ms", methodName, executionTime);
          } else {
              log.info("{} took {}ms", methodName, executionTime);
          }
          
          return result;
      }
  }
  ```

  **Output:**
  ```
  INFO: UserService.findUser() took 45ms
  WARN: SLOW: OrderService.processOrder() took 2340ms
  ```

</details>

<details>
  <summary>Common Use Case: Security/Authorization</summary>
  <br/>

  **Custom Annotation:**

  ```java
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  public @interface RequiresRole {
      String value();
  }
  ```

  **Aspect:**

  ```java
  @Aspect
  @Component
  public class SecurityAspect {
      
      @Autowired
      private SecurityContext securityContext;
      
      @Before("@annotation(requiresRole)")
      public void checkAuthorization(JoinPoint joinPoint, RequiresRole requiresRole) {
          String requiredRole = requiresRole.value();
          String currentUserRole = securityContext.getCurrentUserRole();
          
          if (!currentUserRole.equals(requiredRole)) {
              throw new SecurityException("Access denied. Required role: " + requiredRole);
          }
      }
  }
  ```

  **Usage:**

  ```java
  @Service
  public class UserService {
      
      @RequiresRole("ADMIN")
      public void deleteUser(Long userId) {
          userRepository.deleteById(userId);
      }
      
      @RequiresRole("USER")
      public User getProfile(Long userId) {
          return userRepository.findById(userId);
      }
  }
  ```

</details>

<details>
  <summary>Common Use Case: Transaction Management</summary>
  <br/>

  ```java
  @Aspect
  @Component
  @Slf4j
  public class TransactionAspect {
      
      @Around("@annotation(org.springframework.transaction.annotation.Transactional)")
      public Object manageTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
          String methodName = joinPoint.getSignature().getName();
          
          log.info("Starting transaction for: {}", methodName);
          
          try {
              Object result = joinPoint.proceed();
              log.info("Transaction committed for: {}", methodName);
              return result;
          } catch (Exception e) {
              log.error("Transaction rolled back for: {}", methodName);
              throw e;
          }
      }
  }
  ```

  **Note:** Spring already provides @Transactional. This is just an example of how it works internally.

</details>

<details>
  <summary>Common Use Case: Caching</summary>
  <br/>

  **Custom Annotation:**

  ```java
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  public @interface Cacheable {
      String key();
  }
  ```

  **Aspect:**

  ```java
  @Aspect
  @Component
  public class CachingAspect {
      
      private final Map<String, Object> cache = new ConcurrentHashMap<>();
      
      @Around("@annotation(cacheable)")
      public Object cacheResult(ProceedingJoinPoint joinPoint, Cacheable cacheable) throws Throwable {
          Object[] args = joinPoint.getArgs();
          String cacheKey = cacheable.key() + ":" + Arrays.toString(args);
          
          // Check cache
          if (cache.containsKey(cacheKey)) {
              System.out.println("Cache hit: " + cacheKey);
              return cache.get(cacheKey);
          }
          
          // Execute method
          System.out.println("Cache miss: " + cacheKey);
          Object result = joinPoint.proceed();
          
          // Store in cache
          cache.put(cacheKey, result);
          return result;
      }
  }
  ```

  **Usage:**

  ```java
  @Service
  public class ProductService {
      
      @Cacheable(key = "product")
      public Product findProduct(Long id) {
          return productRepository.findById(id);
      }
  }
  ```

</details>

<details>
  <summary>Common Use Case: Exception Handling</summary>
  <br/>

  ```java
  @Aspect
  @Component
  @Slf4j
  public class ExceptionHandlingAspect {
      
      @AfterThrowing(
          pointcut = "execution(* com.example.service.*.*(..))",
          throwing = "exception"
      )
      public void handleException(JoinPoint joinPoint, Exception exception) {
          String className = joinPoint.getTarget().getClass().getSimpleName();
          String methodName = joinPoint.getSignature().getName();
          Object[] args = joinPoint.getArgs();
          
          log.error("Exception in {}.{} with args: {}", 
              className, methodName, Arrays.toString(args), exception);
          
          // Send alert, save to database, etc.
          alertService.sendAlert("Exception in " + className + "." + methodName);
      }
  }
  ```

</details>

<details>
  <summary>Accessing Method Parameters</summary>
  <br/>

  ```java
  @Aspect
  @Component
  public class ParameterAspect {
      
      @Before("execution(* com.example.service.UserService.createUser(..)) && args(user)")
      public void validateUser(User user) {
          if (user.getName() == null || user.getName().isEmpty()) {
              throw new IllegalArgumentException("User name cannot be empty");
          }
          System.out.println("Validating user: " + user.getName());
      }
      
      @Around("execution(* com.example.service.*.*(..)) && args(id,..)")
      public Object logId(ProceedingJoinPoint joinPoint, Long id) throws Throwable {
          System.out.println("Method called with ID: " + id);
          return joinPoint.proceed();
      }
  }
  ```

</details>

<details>
  <summary>Enabling AOP in Spring Boot</summary>
  <br/>

  **1. Add Dependency:**

  ```xml
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-aop</artifactId>
  </dependency>
  ```

  **2. Enable AspectJ (Optional - enabled by default in Spring Boot):**

  ```java
  @SpringBootApplication
  @EnableAspectJAutoProxy
  public class Application {
      public static void main(String[] args) {
          SpringApplication.run(Application.class, args);
      }
  }
  ```

  **3. Create Aspect:**

  ```java
  @Aspect
  @Component
  public class MyAspect {
      @Before("execution(* com.example.service.*.*(..))")
      public void beforeMethod(JoinPoint joinPoint) {
          System.out.println("Before: " + joinPoint.getSignature().getName());
      }
  }
  ```

</details>

<details>
  <summary>Best Practices</summary>
  <br/>

  ```java
  // ✅ DO: Use @Around for full control
  @Around("execution(* com.example.service.*.*(..))")
  public Object aroundMethod(ProceedingJoinPoint joinPoint) throws Throwable {
      // Before logic
      Object result = joinPoint.proceed();
      // After logic
      return result;
  }
  
  // ✅ DO: Define reusable pointcuts
  @Pointcut("execution(* com.example.service.*.*(..))")
  public void serviceMethods() {}
  
  @Before("serviceMethods()")
  public void beforeService() { }
  
  // ✅ DO: Use specific pointcuts
  @Before("execution(* com.example.service.UserService.createUser(..))")
  public void beforeCreateUser() { }
  
  // ❌ DON'T: Use overly broad pointcuts
  @Before("execution(* *(..))")  // Matches EVERYTHING!
  public void beforeEverything() { }
  
  // ✅ DO: Handle exceptions in @Around
  @Around("serviceMethods()")
  public Object handleExceptions(ProceedingJoinPoint joinPoint) throws Throwable {
      try {
          return joinPoint.proceed();
      } catch (Exception e) {
          log.error("Exception caught", e);
          throw e;
      }
  }
  
  // ❌ DON'T: Forget to call proceed() in @Around
  @Around("serviceMethods()")
  public Object badAround(ProceedingJoinPoint joinPoint) {
      // Method never executes!
      return null;
  }
  
  // ✅ DO: Use custom annotations for clarity
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  public @interface Audited { }
  
  @Before("@annotation(Audited)")
  public void audit() { }
  
  // ✅ DO: Keep aspects focused on one concern
  @Aspect
  @Component
  public class LoggingAspect {
      // Only logging logic
  }
  
  @Aspect
  @Component
  public class SecurityAspect {
      // Only security logic
  }
  ```

  **Key Points**
  <br/>

  + AOP separates **cross-cutting concerns** from business logic
  + Use **@Aspect** to define an aspect class
  + **@Before** runs before method execution
  + **@After** runs after method (always, even if exception)
  + **@AfterReturning** runs after successful return
  + **@AfterThrowing** runs after exception
  + **@Around** is most powerful (can control method execution)
  + Use **pointcut expressions** to match methods
  + Define **reusable pointcuts** with @Pointcut
  + Common use cases: logging, security, performance, caching, transactions
  + Always call **proceed()** in @Around advice
  + Keep aspects **focused** on single concern
  + Use **custom annotations** for better readability

</details>

## Spring Profiles

<details>
  <summary>What are Spring Profiles?</summary>
  <br/>

  Spring Profiles allow you to configure different beans and properties for different environments (dev, test, prod).

  **Why Use Profiles?**

  + Different database configurations per environment
  + Enable/disable features based on environment
  + Different logging levels
  + Mock services in development
  + Environment-specific security settings

  **Without Profiles:**

  ```java
  @Configuration
  public class DatabaseConfig {
      @Bean
      public DataSource dataSource() {
          // Hard-coded for one environment
          return DataSourceBuilder.create()
              .url("jdbc:mysql://localhost:3306/dev_db")
              .build();
      }
  }
  ```

  **With Profiles:**

  ```java
  @Configuration
  public class DatabaseConfig {
      
      @Bean
      @Profile("dev")
      public DataSource devDataSource() {
          return DataSourceBuilder.create()
              .url("jdbc:mysql://localhost:3306/dev_db")
              .build();
      }
      
      @Bean
      @Profile("prod")
      public DataSource prodDataSource() {
          return DataSourceBuilder.create()
              .url("jdbc:mysql://prod-server:3306/prod_db")
              .build();
      }
  }
  ```

</details>

<details>
  <summary>Activating Profiles</summary>
  <br/>

  **1. In application.properties:**

  ```properties
  spring.profiles.active=dev
  ```

  **2. In application.yml:**

  ```yaml
  spring:
    profiles:
      active: dev
  ```

  **3. Command Line:**

  ```bash
  java -jar app.jar --spring.profiles.active=prod
  
  # Multiple profiles
  java -jar app.jar --spring.profiles.active=prod,monitoring
  ```

  **4. Environment Variable:**

  ```bash
  export SPRING_PROFILES_ACTIVE=prod
  java -jar app.jar
  ```

  **5. Programmatically:**

  ```java
  @SpringBootApplication
  public class Application {
      public static void main(String[] args) {
          SpringApplication app = new SpringApplication(Application.class);
          app.setAdditionalProfiles("dev");
          app.run(args);
      }
  }
  ```

  **6. In Tests:**

  ```java
  @SpringBootTest
  @ActiveProfiles("test")
  public class UserServiceTest {
      @Test
      public void testCreateUser() {
          // Test with 'test' profile active
      }
  }
  ```

</details>

<details>
  <summary>Profile-Specific Beans</summary>
  <br/>

  **Method 1: @Profile on @Bean methods:**

  ```java
  @Configuration
  public class AppConfig {
      
      @Bean
      @Profile("dev")
      public DataSource devDataSource() {
          return new EmbeddedDatabaseBuilder()
              .setType(EmbeddedDatabaseType.H2)
              .build();
      }
      
      @Bean
      @Profile("prod")
      public DataSource prodDataSource() {
          return DataSourceBuilder.create()
              .url("jdbc:mysql://prod-server:3306/db")
              .username("admin")
              .password("secret")
              .build();
      }
  }
  ```

  **Method 2: @Profile on @Configuration classes:**

  ```java
  @Configuration
  @Profile("dev")
  public class DevConfig {
      @Bean
      public DataSource dataSource() {
          return new EmbeddedDatabaseBuilder()
              .setType(EmbeddedDatabaseType.H2)
              .build();
      }
  }
  
  @Configuration
  @Profile("prod")
  public class ProdConfig {
      @Bean
      public DataSource dataSource() {
          return DataSourceBuilder.create()
              .url("jdbc:mysql://prod-server:3306/db")
              .build();
      }
  }
  ```

  **Method 3: @Profile on @Component classes:**

  ```java
  @Service
  @Profile("dev")
  public class MockEmailService implements EmailService {
      @Override
      public void sendEmail(String to, String message) {
          System.out.println("Mock email to: " + to);
      }
  }
  
  @Service
  @Profile("prod")
  public class RealEmailService implements EmailService {
      @Override
      public void sendEmail(String to, String message) {
          // Send real email via SMTP
      }
  }
  ```

</details>

<details>
  <summary>Profile Expressions</summary>
  <br/>

  **NOT operator (!):**

  ```java
  @Configuration
  @Profile("!prod")
  public class NonProdConfig {
      // Active in all profiles EXCEPT prod
      @Bean
      public DebugService debugService() {
          return new DebugService();
      }
  }
  ```

  **OR operator (|):**

  ```java
  @Configuration
  @Profile("dev | test")
  public class DevOrTestConfig {
      // Active in dev OR test
      @Bean
      public MockService mockService() {
          return new MockService();
      }
  }
  ```

  **AND operator (&):**

  ```java
  @Configuration
  @Profile("prod & monitoring")
  public class ProdMonitoringConfig {
      // Active only when BOTH prod AND monitoring are active
      @Bean
      public MetricsService metricsService() {
          return new MetricsService();
      }
  }
  ```

  **Complex expressions:**

  ```java
  @Configuration
  @Profile("(dev | test) & !ci")
  public class ComplexConfig {
      // Active in (dev OR test) AND NOT ci
  }
  ```

</details>

<details>
  <summary>Profile-Specific Properties Files</summary>
  <br/>

  **File naming convention:**

  ```
  application.properties          # Default properties
  application-dev.properties      # Dev profile
  application-test.properties     # Test profile
  application-prod.properties     # Prod profile
  ```

  **application.properties (default):**

  ```properties
  spring.application.name=myapp
  server.port=8080
  ```

  **application-dev.properties:**

  ```properties
  spring.datasource.url=jdbc:h2:mem:testdb
  spring.datasource.username=sa
  spring.datasource.password=
  logging.level.root=DEBUG
  ```

  **application-prod.properties:**

  ```properties
  spring.datasource.url=jdbc:mysql://prod-server:3306/db
  spring.datasource.username=admin
  spring.datasource.password=${DB_PASSWORD}
  logging.level.root=WARN
  ```

  **Activate profile:**

  ```properties
  # application.properties
  spring.profiles.active=dev
  ```

  **YAML format:**

  ```yaml
  # application.yml
  spring:
    application:
      name: myapp
    profiles:
      active: dev
  
  ---
  # application-dev.yml
  spring:
    config:
      activate:
        on-profile: dev
    datasource:
      url: jdbc:h2:mem:testdb
      username: sa
      password:
  
  ---
  # application-prod.yml
  spring:
    config:
      activate:
        on-profile: prod
    datasource:
      url: jdbc:mysql://prod-server:3306/db
      username: admin
      password: ${DB_PASSWORD}
  ```

</details>

<details>
  <summary>Multiple Profiles</summary>
  <br/>

  **Activate multiple profiles:**

  ```properties
  spring.profiles.active=dev,monitoring,debug
  ```

  **Example configuration:**

  ```java
  @Configuration
  @Profile("dev")
  public class DevConfig {
      @Bean
      public DataSource dataSource() {
          return new EmbeddedDatabaseBuilder().build();
      }
  }
  
  @Configuration
  @Profile("monitoring")
  public class MonitoringConfig {
      @Bean
      public MetricsCollector metricsCollector() {
          return new MetricsCollector();
      }
  }
  
  @Configuration
  @Profile("debug")
  public class DebugConfig {
      @Bean
      public DebugInterceptor debugInterceptor() {
          return new DebugInterceptor();
      }
  }
  ```

  **Result:** All three configurations are active simultaneously.

</details>

<details>
  <summary>Default Profile</summary>
  <br/>

  **Set default profile (used when no profile is active):**

  ```properties
  spring.profiles.default=dev
  ```

  **Using @Profile("default"):**

  ```java
  @Configuration
  @Profile("default")
  public class DefaultConfig {
      @Bean
      public DataSource dataSource() {
          // Used when no profile is explicitly set
          return new EmbeddedDatabaseBuilder().build();
      }
  }
  ```

  **Fallback behavior:**

  ```java
  @Configuration
  public class AppConfig {
      
      @Bean
      @Profile("prod")
      public DataSource prodDataSource() {
          return DataSourceBuilder.create()
              .url("jdbc:mysql://prod-server:3306/db")
              .build();
      }
      
      @Bean
      @Profile("!prod")  // Active in all profiles except prod
      public DataSource devDataSource() {
          return new EmbeddedDatabaseBuilder().build();
      }
  }
  ```

</details>

<details>
  <summary>Checking Active Profile Programmatically</summary>
  <br/>

  ```java
  @Service
  public class ProfileService {
      
      @Autowired
      private Environment environment;
      
      public void checkProfile() {
          String[] activeProfiles = environment.getActiveProfiles();
          System.out.println("Active profiles: " + Arrays.toString(activeProfiles));
          
          if (environment.acceptsProfiles(Profiles.of("dev"))) {
              System.out.println("Dev profile is active");
          }
          
          if (environment.acceptsProfiles(Profiles.of("prod"))) {
              System.out.println("Prod profile is active");
          }
      }
  }
  ```

  **Conditional logic based on profile:**

  ```java
  @Service
  public class EmailService {
      
      @Autowired
      private Environment environment;
      
      public void sendEmail(String to, String message) {
          if (environment.acceptsProfiles(Profiles.of("dev"))) {
              System.out.println("Mock email to: " + to);
          } else {
              // Send real email
              smtpClient.send(to, message);
          }
      }
  }
  ```

</details>

<details>
  <summary>Common Profile Patterns</summary>
  <br/>

  **1. Database Configuration:**

  ```java
  @Configuration
  public class DatabaseConfig {
      
      @Bean
      @Profile("dev")
      public DataSource devDataSource() {
          return new EmbeddedDatabaseBuilder()
              .setType(EmbeddedDatabaseType.H2)
              .addScript("schema.sql")
              .addScript("test-data.sql")
              .build();
      }
      
      @Bean
      @Profile("test")
      public DataSource testDataSource() {
          return new EmbeddedDatabaseBuilder()
              .setType(EmbeddedDatabaseType.H2)
              .addScript("schema.sql")
              .build();
      }
      
      @Bean
      @Profile("prod")
      public DataSource prodDataSource() {
          HikariConfig config = new HikariConfig();
          config.setJdbcUrl(System.getenv("DB_URL"));
          config.setUsername(System.getenv("DB_USER"));
          config.setPassword(System.getenv("DB_PASSWORD"));
          config.setMaximumPoolSize(20);
          return new HikariDataSource(config);
      }
  }
  ```

  **2. External Services:**

  ```java
  @Service
  @Profile("dev")
  public class MockPaymentService implements PaymentService {
      @Override
      public PaymentResult processPayment(Payment payment) {
          System.out.println("Mock payment: " + payment.getAmount());
          return new PaymentResult(true, "MOCK-" + UUID.randomUUID());
      }
  }
  
  @Service
  @Profile("prod")
  public class StripePaymentService implements PaymentService {
      @Override
      public PaymentResult processPayment(Payment payment) {
          // Real Stripe API call
          return stripeClient.charge(payment);
      }
  }
  ```

  **3. Logging Configuration:**

  ```properties
  # application-dev.properties
  logging.level.root=DEBUG
  logging.level.com.example=TRACE
  logging.pattern.console=%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n
  
  # application-prod.properties
  logging.level.root=WARN
  logging.level.com.example=INFO
  logging.file.name=/var/log/myapp.log
  ```

  **4. Feature Flags:**

  ```java
  @Configuration
  @Profile("beta")
  public class BetaFeaturesConfig {
      @Bean
      public NewFeatureService newFeatureService() {
          return new NewFeatureService();
      }
  }
  
  @RestController
  public class FeatureController {
      
      @Autowired(required = false)
      private NewFeatureService newFeatureService;
      
      @GetMapping("/feature")
      public ResponseEntity<String> getFeature() {
          if (newFeatureService != null) {
              return ResponseEntity.ok(newFeatureService.getFeature());
          }
          return ResponseEntity.status(404).body("Feature not available");
      }
  }
  ```

</details>

<details>
  <summary>Profile Groups (Spring Boot 2.4+)</summary>
  <br/>

  **Define profile groups:**

  ```properties
  # application.properties
  spring.profiles.group.production=prod,monitoring,security
  spring.profiles.group.development=dev,debug,mock-services
  ```

  **Activate a group:**

  ```bash
  java -jar app.jar --spring.profiles.active=production
  # This activates: prod, monitoring, security
  ```

  **YAML format:**

  ```yaml
  spring:
    profiles:
      group:
        production:
          - prod
          - monitoring
          - security
        development:
          - dev
          - debug
          - mock-services
  ```

  **Usage:**

  ```bash
  # Activate all development profiles at once
  java -jar app.jar --spring.profiles.active=development
  ```

</details>

<details>
  <summary>Best Practices</summary>
  <br/>

  ```java
  // ✅ DO: Use clear profile names
  @Profile("dev")
  @Profile("prod")
  @Profile("test")
  
  // ❌ DON'T: Use ambiguous names
  @Profile("profile1")
  @Profile("config2")
  
  // ✅ DO: Use profile-specific properties files
  application-dev.properties
  application-prod.properties
  
  // ✅ DO: Use environment variables for sensitive data
  @Bean
  @Profile("prod")
  public DataSource prodDataSource() {
      return DataSourceBuilder.create()
          .url(System.getenv("DB_URL"))
          .username(System.getenv("DB_USER"))
          .password(System.getenv("DB_PASSWORD"))
          .build();
  }
  
  // ❌ DON'T: Hard-code sensitive data
  @Bean
  @Profile("prod")
  public DataSource badDataSource() {
      return DataSourceBuilder.create()
          .url("jdbc:mysql://prod-server:3306/db")
          .username("admin")
          .password("secret123")  // BAD!
          .build();
  }
  
  // ✅ DO: Use @Profile on configuration classes
  @Configuration
  @Profile("dev")
  public class DevConfig {
      // All beans in this class are dev-only
  }
  
  // ✅ DO: Use NOT operator for non-production
  @Configuration
  @Profile("!prod")
  public class NonProdConfig {
      // Active in all profiles except prod
  }
  
  // ✅ DO: Use profile groups for related profiles
  spring.profiles.group.production=prod,monitoring,security
  
  // ✅ DO: Set default profile
  spring.profiles.default=dev
  
  // ✅ DO: Use @ActiveProfiles in tests
  @SpringBootTest
  @ActiveProfiles("test")
  public class MyTest { }
  
  // ❌ DON'T: Mix profile logic with business logic
  @Service
  public class BadService {
      @Autowired
      private Environment environment;
      
      public void doSomething() {
          if (environment.acceptsProfiles(Profiles.of("dev"))) {
              // Business logic should not depend on profiles
          }
      }
  }
  ```

  **Key Points**
  <br/>

  + Profiles allow **environment-specific** configurations
  + Activate profiles via **properties, command line, or environment variables**
  + Use **@Profile** on beans, methods, or configuration classes
  + Profile expressions support **NOT (!), OR (|), AND (&)** operators
  + Use **profile-specific properties files**: application-{profile}.properties
  + Multiple profiles can be **active simultaneously**
  + Set **default profile** with spring.profiles.default
  + Use **profile groups** to activate multiple profiles at once (Spring Boot 2.4+)
  + Check active profiles with **Environment.getActiveProfiles()**
  + Use **@ActiveProfiles** in tests
  + Common use cases: database config, external services, logging, feature flags
  + Never hard-code **sensitive data** - use environment variables
  + Keep profile logic in **configuration**, not business logic

</details>

## Spring Core Annotations Summary

<details>
  <summary>Quick Reference by Category</summary>
  <br/>

  **Bean Definition:**
  - `@Component`, `@Service`, `@Repository`, `@Controller`, `@RestController`
  - `@Configuration`, `@Bean`, `@ComponentScan`

  **Dependency Injection:**
  - `@Autowired`, `@Qualifier`, `@Primary`, `@Value`

  **Scope & Lifecycle:**
  - `@Scope`, `@RequestScope`, `@SessionScope`, `@ApplicationScope`
  - `@PostConstruct`, `@PreDestroy`, `@Lazy`, `@DependsOn`

  **Configuration:**
  - `@Profile`, `@PropertySource`, `@Import`, `@Conditional`

  **AOP:**
  - `@Aspect`, `@Before`, `@After`, `@Around`, `@Pointcut`

  **Spring Boot:**
  - `@SpringBootApplication`, `@ConfigurationProperties`, `@EnableAutoConfiguration`

  **Validation:**
  - `@Valid`, `@NotNull`, `@NotEmpty`, `@Size`, `@Email`

  **Async & Scheduling:**
  - `@Async`, `@Scheduled`, `@EnableAsync`, `@EnableScheduling`

  **Transaction:**
  - `@Transactional`, `@EnableTransactionManagement`

  **Caching:**
  - `@Cacheable`, `@CachePut`, `@CacheEvict`, `@EnableCaching`

  **Testing:**
  - `@SpringBootTest`, `@WebMvcTest`, `@DataJpaTest`, `@MockBean`, `@ActiveProfiles`

</details>

<details>
  <summary><b>Component Scanning & Bean Definition:</b> <code>@Component</code>, <code>@Service</code>, <code>@Repository</code>, <code>@Controller</code>, <code>@RestController</code>, <code>@Configuration</code>, <code>@Bean</code>, <code>@ComponentScan</code></summary>
  <br/>

  | Annotation | Purpose | Example |
  |------------|---------|---------|
  | `@Component` | Generic stereotype for any Spring-managed component | `@Component public class MyComponent { }` |
  | `@Service` | Specialization of @Component for service layer | `@Service public class UserService { }` |
  | `@Repository` | Specialization of @Component for data access layer | `@Repository public class UserRepository { }` |
  | `@Controller` | Specialization of @Component for MVC controllers | `@Controller public class UserController { }` |
  | `@RestController` | Combines @Controller + @ResponseBody | `@RestController public class ApiController { }` |
  | `@Configuration` | Indicates class contains @Bean definitions | `@Configuration public class AppConfig { }` |
  | `@Bean` | Declares a method produces a bean | `@Bean public DataSource dataSource() { }` |
  | `@ComponentScan` | Configures component scanning | `@ComponentScan("com.example")` |

</details>

<details>
  <summary><b>Dependency Injection:</b> <code>@Autowired</code>, <code>@Inject</code>, <code>@Qualifier</code>, <code>@Primary</code>, <code>@Resource</code>, <code>@Value</code></summary>
  <br/>

  | Annotation | Purpose | Example |
  |------------|---------|---------|
  | `@Autowired` | Injects dependency automatically | `@Autowired private UserService service;` |
  | `@Inject` | JSR-330 alternative to @Autowired | `@Inject private UserService service;` |
  | `@Qualifier` | Specifies which bean to inject | `@Qualifier("primary") DataSource ds;` |
  | `@Primary` | Marks bean as primary choice | `@Bean @Primary public DataSource ds() { }` |
  | `@Resource` | JSR-250 injection by name | `@Resource(name="userService") private UserService service;` |
  | `@Value` | Injects values from properties | `@Value("${app.name}") private String name;` |

</details>

<details>
  <summary><b>Bean Scope & Lifecycle:</b> <code>@Scope</code>, <code>@RequestScope</code>, <code>@SessionScope</code>, <code>@ApplicationScope</code>, <code>@PostConstruct</code>, <code>@PreDestroy</code>, <code>@Lazy</code>, <code>@DependsOn</code></summary>
  <br/>

  | Annotation | Purpose | Example |
  |------------|---------|---------|
  | `@Scope` | Defines bean scope | `@Scope("prototype")` |
  | `@RequestScope` | Bean per HTTP request | `@RequestScope public class RequestContext { }` |
  | `@SessionScope` | Bean per HTTP session | `@SessionScope public class ShoppingCart { }` |
  | `@ApplicationScope` | Bean per ServletContext | `@ApplicationScope public class AppMetrics { }` |
  | `@PostConstruct` | Method called after dependency injection | `@PostConstruct public void init() { }` |
  | `@PreDestroy` | Method called before bean destruction | `@PreDestroy public void cleanup() { }` |
  | `@Lazy` | Delays bean initialization | `@Lazy @Component public class HeavyService { }` |
  | `@DependsOn` | Specifies bean initialization order | `@DependsOn("dataSource")` |

</details>

<details>
  <summary><b>Configuration & Profiles:</b> <code>@Profile</code>, <code>@PropertySource</code>, <code>@PropertySources</code>, <code>@Import</code>, <code>@ImportResource</code>, <code>@Conditional</code>, <code>@ConditionalOnProperty</code>, <code>@ConditionalOnClass</code></summary>
  <br/>

  | Annotation | Purpose | Example |
  |------------|---------|---------|
  | `@Profile` | Activates bean for specific profile | `@Profile("dev")` |
  | `@PropertySource` | Loads properties file | `@PropertySource("classpath:app.properties")` |
  | `@PropertySources` | Multiple property sources | `@PropertySources({@PropertySource("a.properties")})` |
  | `@Import` | Imports other configuration classes | `@Import(DatabaseConfig.class)` |
  | `@ImportResource` | Imports XML configuration | `@ImportResource("classpath:beans.xml")` |
  | `@Conditional` | Conditional bean registration | `@Conditional(OnWindowsCondition.class)` |
  | `@ConditionalOnProperty` | Conditional on property value | `@ConditionalOnProperty("feature.enabled")` |
  | `@ConditionalOnClass` | Conditional on class presence | `@ConditionalOnClass(DataSource.class)` |

</details>

<details>
  <summary><b>AOP:</b> <code>@Aspect</code>, <code>@Before</code>, <code>@After</code>, <code>@AfterReturning</code>, <code>@AfterThrowing</code>, <code>@Around</code>, <code>@Pointcut</code>, <code>@EnableAspectJAutoProxy</code></summary>
  <br/>

  | Annotation | Purpose | Example |
  |------------|---------|---------|
  | `@Aspect` | Declares an aspect class | `@Aspect @Component public class LoggingAspect { }` |
  | `@Before` | Advice before method execution | `@Before("execution(* com.example.*.*(..))") ` |
  | `@After` | Advice after method execution | `@After("execution(* com.example.*.*(..))") ` |
  | `@AfterReturning` | Advice after successful return | `@AfterReturning(pointcut="...", returning="result")` |
  | `@AfterThrowing` | Advice after exception | `@AfterThrowing(pointcut="...", throwing="ex")` |
  | `@Around` | Advice around method execution | `@Around("execution(* com.example.*.*(..))")` |
  | `@Pointcut` | Defines reusable pointcut | `@Pointcut("execution(* com.example.*.*(..))")` |
  | `@EnableAspectJAutoProxy` | Enables AspectJ support | `@EnableAspectJAutoProxy` |

</details>

<details>
  <summary><b>Spring Boot:</b> <code>@SpringBootApplication</code>, <code>@EnableAutoConfiguration</code>, <code>@ConfigurationProperties</code>, <code>@EnableConfigurationProperties</code>, <code>@ConditionalOnBean</code>, <code>@ConditionalOnMissingBean</code>, <code>@ConditionalOnWebApplication</code></summary>
  <br/>

  | Annotation | Purpose | Example |
  |------------|---------|---------|
  | `@SpringBootApplication` | Combines @Configuration, @EnableAutoConfiguration, @ComponentScan | `@SpringBootApplication public class App { }` |
  | `@EnableAutoConfiguration` | Enables Spring Boot auto-configuration | `@EnableAutoConfiguration` |
  | `@ConfigurationProperties` | Binds properties to POJO | `@ConfigurationProperties(prefix="app")` |
  | `@EnableConfigurationProperties` | Enables @ConfigurationProperties | `@EnableConfigurationProperties(AppProps.class)` |
  | `@ConditionalOnBean` | Conditional on bean existence | `@ConditionalOnBean(DataSource.class)` |
  | `@ConditionalOnMissingBean` | Conditional on bean absence | `@ConditionalOnMissingBean(DataSource.class)` |
  | `@ConditionalOnWebApplication` | Conditional on web application | `@ConditionalOnWebApplication` |

</details>

<details>
  <summary><b>Validation:</b> <code>@Valid</code>, <code>@Validated</code>, <code>@NotNull</code>, <code>@NotEmpty</code>, <code>@NotBlank</code>, <code>@Size</code>, <code>@Min</code>, <code>@Max</code>, <code>@Email</code>, <code>@Pattern</code></summary>
  <br/>

  | Annotation | Purpose | Example |
  |------------|---------|---------|
  | `@Valid` | Triggers validation | `public void create(@Valid User user)` |
  | `@Validated` | Spring's variant of @Valid | `@Validated public class UserService { }` |
  | `@NotNull` | Field cannot be null | `@NotNull private String name;` |
  | `@NotEmpty` | Collection/String not empty | `@NotEmpty private String email;` |
  | `@NotBlank` | String not blank | `@NotBlank private String username;` |
  | `@Size` | Size constraints | `@Size(min=2, max=50) private String name;` |
  | `@Min` / `@Max` | Numeric min/max | `@Min(18) private int age;` |
  | `@Email` | Valid email format | `@Email private String email;` |
  | `@Pattern` | Regex pattern | `@Pattern(regexp="[0-9]+") private String phone;` |

</details>

<details>
  <summary><b>Scheduling & Async:</b> <code>@EnableScheduling</code>, <code>@Scheduled</code>, <code>@EnableAsync</code>, <code>@Async</code></summary>
  <br/>

  | Annotation | Purpose | Example |
  |------------|---------|---------|
  | `@EnableScheduling` | Enables scheduled tasks | `@EnableScheduling` |
  | `@Scheduled` | Schedules method execution | `@Scheduled(fixedRate = 5000)` |
  | `@EnableAsync` | Enables async execution | `@EnableAsync` |
  | `@Async` | Executes method asynchronously | `@Async public void sendEmail() { }` |

</details>

<details>
  <summary><b>Transaction:</b> <code>@Transactional</code>, <code>@EnableTransactionManagement</code></summary>
  <br/>

  | Annotation | Purpose | Example |
  |------------|---------|---------|
  | `@Transactional` | Declares transaction boundaries | `@Transactional public void save(User user) { }` |
  | `@EnableTransactionManagement` | Enables transaction support | `@EnableTransactionManagement` |

  **@Transactional Attributes:**

  | Attribute | Purpose | Example |
  |-----------|---------|---------|
  | `propagation` | Transaction propagation | `@Transactional(propagation = Propagation.REQUIRED)` |
  | `isolation` | Transaction isolation | `@Transactional(isolation = Isolation.READ_COMMITTED)` |
  | `readOnly` | Read-only transaction | `@Transactional(readOnly = true)` |
  | `timeout` | Transaction timeout | `@Transactional(timeout = 30)` |
  | `rollbackFor` | Rollback on exception | `@Transactional(rollbackFor = Exception.class)` |
  | `noRollbackFor` | Don't rollback on exception | `@Transactional(noRollbackFor = ValidationException.class)` |

</details>

<details>
  <summary><b>Caching:</b> <code>@EnableCaching</code>, <code>@Cacheable</code>, <code>@CachePut</code>, <code>@CacheEvict</code>, <code>@Caching</code>, <code>@CacheConfig</code></summary>
  <br/>

  | Annotation | Purpose | Example |
  |------------|---------|---------|
  | `@EnableCaching` | Enables caching support | `@EnableCaching` |
  | `@Cacheable` | Caches method result | `@Cacheable("users")` |
  | `@CachePut` | Updates cache | `@CachePut(value="users", key="#user.id")` |
  | `@CacheEvict` | Removes from cache | `@CacheEvict(value="users", key="#id")` |
  | `@Caching` | Groups multiple cache operations | `@Caching(evict={@CacheEvict("users")})` |
  | `@CacheConfig` | Class-level cache configuration | `@CacheConfig(cacheNames="users")` |

</details>

<details>
  <summary><b>Testing:</b> <code>@SpringBootTest</code>, <code>@WebMvcTest</code>, <code>@DataJpaTest</code>, <code>@MockBean</code>, <code>@SpyBean</code>, <code>@TestConfiguration</code>, <code>@ActiveProfiles</code>, <code>@DirtiesContext</code>, <code>@Sql</code></summary>
  <br/>

  | Annotation | Purpose | Example |
  |------------|---------|---------|
  | `@SpringBootTest` | Loads full application context | `@SpringBootTest public class AppTest { }` |
  | `@WebMvcTest` | Tests MVC controllers | `@WebMvcTest(UserController.class)` |
  | `@DataJpaTest` | Tests JPA repositories | `@DataJpaTest public class UserRepoTest { }` |
  | `@MockBean` | Adds mock to context | `@MockBean private UserService service;` |
  | `@SpyBean` | Adds spy to context | `@SpyBean private UserService service;` |
  | `@TestConfiguration` | Test-specific configuration | `@TestConfiguration public class TestConfig { }` |
  | `@ActiveProfiles` | Activates profiles for test | `@ActiveProfiles("test")` |
  | `@DirtiesContext` | Marks context as dirty | `@DirtiesContext` |
  | `@Sql` | Executes SQL scripts | `@Sql("/test-data.sql")` |


</details>

<details>
  <summary>Most Commonly Used Annotations</summary>
  <br/>

  **Top 20 annotations you'll use daily:**

  1. `@SpringBootApplication` - Main application class
  2. `@RestController` - REST API controllers
  3. `@Service` - Service layer
  4. `@Repository` - Data access layer
  5. `@Autowired` - Dependency injection
  6. `@GetMapping` / `@PostMapping` / `@PutMapping` / `@DeleteMapping` - HTTP endpoints
  7. `@RequestBody` - Request body binding
  8. `@PathVariable` - URL path variables
  9. `@RequestParam` - Query parameters
  10. `@Configuration` - Configuration classes
  11. `@Bean` - Bean definitions
  12. `@Value` - Property injection
  13. `@Transactional` - Transaction management
  14. `@Valid` - Validation
  15. `@Profile` - Environment profiles
  16. `@Qualifier` - Bean selection
  17. `@Async` - Asynchronous execution
  18. `@Scheduled` - Scheduled tasks
  19. `@Cacheable` - Caching
  20. `@SpringBootTest` - Testing

</details>



