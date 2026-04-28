## Auto-configuration

<details>
  <summary>What is Auto-configuration?</summary>
  <br/>

  Auto-configuration automatically configures Spring application based on dependencies in classpath.

  **Without Auto-configuration:**

  ```java
  @Configuration
  public class DataSourceConfig {
      @Bean
      public DataSource dataSource() {
          HikariConfig config = new HikariConfig();
          config.setJdbcUrl("jdbc:mysql://localhost:3306/db");
          config.setUsername("root");
          config.setPassword("password");
          return new HikariDataSource(config);
      }
      
      @Bean
      public JdbcTemplate jdbcTemplate(DataSource dataSource) {
          return new JdbcTemplate(dataSource);
      }
  }
  ```

  **With Auto-configuration:**

  ```properties
  # application.properties
  spring.datasource.url=jdbc:mysql://localhost:3306/db
  spring.datasource.username=root
  spring.datasource.password=password
  ```

  Spring Boot automatically creates DataSource and JdbcTemplate beans!

  **Benefits:**
  + Reduces boilerplate configuration
  + Convention over configuration
  + Intelligent defaults
  + Easy to override

</details>

<details>
  <summary>How Auto-configuration Works</summary>
  <br/>

  **1. @SpringBootApplication enables auto-configuration:**

  ```java
  @SpringBootApplication  // Contains @EnableAutoConfiguration
  public class Application {
      public static void main(String[] args) {
          SpringApplication.run(Application.class, args);
      }
  }
  ```

  **2. Spring Boot scans META-INF/spring.factories:**

  ```
  org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
  org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,\
  org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration,\
  org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration
  ```

  **3. Conditional annotations determine if configuration applies:**

  ```java
  @Configuration
  @ConditionalOnClass(DataSource.class)  // Only if DataSource in classpath
  @ConditionalOnMissingBean(DataSource.class)  // Only if no DataSource bean exists
  @EnableConfigurationProperties(DataSourceProperties.class)
  public class DataSourceAutoConfiguration {
      
      @Bean
      public DataSource dataSource(DataSourceProperties properties) {
          return DataSourceBuilder.create()
              .url(properties.getUrl())
              .username(properties.getUsername())
              .password(properties.getPassword())
              .build();
      }
  }
  ```

  **Execution Flow:**

  ```
  1. Application starts
     ↓
  2. @EnableAutoConfiguration triggers
     ↓
  3. Load auto-configuration classes from spring.factories
     ↓
  4. Evaluate @Conditional annotations
     ↓
  5. Create beans if conditions met
     ↓
  6. Application ready
  ```

</details>

<details>
  <summary>Common Auto-configurations</summary>
  <br/>

  **DataSource Auto-configuration:**

  ```xml
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-jpa</artifactId>
  </dependency>
  ```

  Automatically configures:
  + DataSource
  + EntityManagerFactory
  + TransactionManager
  + JpaRepositories

  **Web MVC Auto-configuration:**

  ```xml
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
  </dependency>
  ```

  Automatically configures:
  + DispatcherServlet
  + Embedded Tomcat
  + Jackson for JSON
  + Error handling
  + Static resources

  **Security Auto-configuration:**

  ```xml
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-security</artifactId>
  </dependency>
  ```

  Automatically configures:
  + Default security filter chain
  + Basic authentication
  + Default login page
  + CSRF protection

</details>

<details>
  <summary>Viewing Auto-configurations</summary>
  <br/>

  **Method 1: Enable debug logging:**

  ```properties
  # application.properties
  debug=true
  ```

  **Output:**
  ```
  ============================
  CONDITIONS EVALUATION REPORT
  ============================

  Positive matches:
  -----------------
     DataSourceAutoConfiguration matched:
        - @ConditionalOnClass found required classes 'DataSource', 'EmbeddedDatabaseType'
        - @ConditionalOnMissingBean (types: DataSource) did not find any beans

  Negative matches:
  -----------------
     RedisAutoConfiguration:
        Did not match:
           - @ConditionalOnClass did not find required class 'RedisConnectionFactory'
  ```

  **Method 2: Actuator endpoint:**

  ```xml
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-actuator</artifactId>
  </dependency>
  ```

  ```properties
  management.endpoints.web.exposure.include=conditions
  ```

  Access: `http://localhost:8080/actuator/conditions`

  **Method 3: Programmatically:**

  ```java
  @SpringBootApplication
  public class Application {
      public static void main(String[] args) {
          ConfigurableApplicationContext context = 
              SpringApplication.run(Application.class, args);
          
          String[] beanNames = context.getBeanDefinitionNames();
          Arrays.stream(beanNames).sorted().forEach(System.out::println);
      }
  }
  ```

</details>

<details>
  <summary>Customizing Auto-configuration</summary>
  <br/>

  **1. Override with properties:**

  ```properties
  # application.properties
  spring.datasource.url=jdbc:mysql://localhost:3306/mydb
  spring.datasource.hikari.maximum-pool-size=20
  spring.datasource.hikari.connection-timeout=30000
  
  server.port=9090
  server.servlet.context-path=/api
  
  spring.jackson.date-format=yyyy-MM-dd HH:mm:ss
  spring.jackson.time-zone=UTC
  ```

  **2. Provide your own bean:**

  ```java
  @Configuration
  public class CustomDataSourceConfig {
      
      @Bean
      public DataSource dataSource() {
          // Your custom DataSource
          // Auto-configuration backs off due to @ConditionalOnMissingBean
          HikariConfig config = new HikariConfig();
          config.setJdbcUrl("jdbc:mysql://custom-host:3306/db");
          config.setMaximumPoolSize(50);
          return new HikariDataSource(config);
      }
  }
  ```

  **3. Customize auto-configured bean:**

  ```java
  @Configuration
  public class WebMvcConfig implements WebMvcConfigurer {
      
      @Override
      public void addCorsMappings(CorsRegistry registry) {
          registry.addMapping("/**")
              .allowedOrigins("http://localhost:3000")
              .allowedMethods("GET", "POST", "PUT", "DELETE");
      }
      
      @Override
      public void addInterceptors(InterceptorRegistry registry) {
          registry.addInterceptor(new LoggingInterceptor());
      }
  }
  ```

  **4. Use @ConfigurationProperties:**

  ```java
  @ConfigurationProperties(prefix = "app.datasource")
  public class CustomDataSourceProperties {
      private String url;
      private String username;
      private String password;
      private int maxPoolSize = 10;
      
      // getters and setters
  }
  
  @Configuration
  @EnableConfigurationProperties(CustomDataSourceProperties.class)
  public class DataSourceConfig {
      
      @Bean
      public DataSource dataSource(CustomDataSourceProperties props) {
          HikariConfig config = new HikariConfig();
          config.setJdbcUrl(props.getUrl());
          config.setUsername(props.getUsername());
          config.setPassword(props.getPassword());
          config.setMaximumPoolSize(props.getMaxPoolSize());
          return new HikariDataSource(config);
      }
  }
  ```

  ```properties
  app.datasource.url=jdbc:mysql://localhost:3306/db
  app.datasource.username=root
  app.datasource.password=secret
  app.datasource.max-pool-size=20
  ```

</details>

<details>
  <summary>Excluding Auto-configurations</summary>
  <br/>

  **Method 1: Using @SpringBootApplication:**

  ```java
  @SpringBootApplication(exclude = {
      DataSourceAutoConfiguration.class,
      SecurityAutoConfiguration.class
  })
  public class Application {
      public static void main(String[] args) {
          SpringApplication.run(Application.class, args);
      }
  }
  ```

  **Method 2: Using properties:**

  ```properties
  spring.autoconfigure.exclude=\
    org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,\
    org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
  ```

  **Method 3: Using @EnableAutoConfiguration:**

  ```java
  @Configuration
  @EnableAutoConfiguration(exclude = {
      DataSourceAutoConfiguration.class
  })
  public class AppConfig {
  }
  ```

  **When to exclude:**
  + Don't need the feature (e.g., no database)
  + Providing custom implementation
  + Conflicts with other configurations
  + Testing specific scenarios

</details>

<details>
  <summary>Conditional Annotations</summary>
  <br/>

  **Class-based conditions:**

  ```java
  @Configuration
  @ConditionalOnClass(DataSource.class)  // Class exists in classpath
  public class DatabaseConfig {
      @Bean
      public DatabaseService databaseService() {
          return new DatabaseService();
      }
  }
  
  @Configuration
  @ConditionalOnMissingClass("com.example.CustomService")  // Class not in classpath
  public class DefaultConfig {
  }
  ```

  **Bean-based conditions:**

  ```java
  @Configuration
  public class CacheConfig {
      
      @Bean
      @ConditionalOnBean(DataSource.class)  // DataSource bean exists
      public CacheManager cacheManager() {
          return new CaffeineCacheManager();
      }
      
      @Bean
      @ConditionalOnMissingBean(CacheManager.class)  // No CacheManager bean
      public CacheManager defaultCacheManager() {
          return new ConcurrentMapCacheManager();
      }
  }
  ```

  **Property-based conditions:**

  ```java
  @Configuration
  @ConditionalOnProperty(
      name = "app.feature.enabled",
      havingValue = "true",
      matchIfMissing = false
  )
  public class FeatureConfig {
      @Bean
      public FeatureService featureService() {
          return new FeatureService();
      }
  }
  ```

  **Resource-based conditions:**

  ```java
  @Configuration
  @ConditionalOnResource(resources = "classpath:config.properties")
  public class ConfigBasedSetup {
  }
  ```

  **Web application conditions:**

  ```java
  @Configuration
  @ConditionalOnWebApplication  // Only in web applications
  public class WebConfig {
  }
  
  @Configuration
  @ConditionalOnNotWebApplication  // Only in non-web applications
  public class BatchConfig {
  }
  ```

  **Expression-based conditions:**

  ```java
  @Configuration
  @ConditionalOnExpression("${app.feature.enabled:false} and ${app.env} == 'prod'")
  public class ProdFeatureConfig {
  }
  ```

</details>

<details>
  <summary>Creating Custom Auto-configuration</summary>
  <br/>

  **1. Create auto-configuration class:**

  ```java
  @Configuration
  @ConditionalOnClass(MyService.class)
  @ConditionalOnMissingBean(MyService.class)
  @EnableConfigurationProperties(MyServiceProperties.class)
  public class MyServiceAutoConfiguration {
      
      @Bean
      public MyService myService(MyServiceProperties properties) {
          return new MyService(properties.getApiKey(), properties.getTimeout());
      }
  }
  ```

  **2. Create properties class:**

  ```java
  @ConfigurationProperties(prefix = "myservice")
  public class MyServiceProperties {
      private String apiKey;
      private int timeout = 5000;
      
      // getters and setters
  }
  ```

  **3. Register in META-INF/spring.factories:**

  ```
  # src/main/resources/META-INF/spring.factories
  org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
  com.example.autoconfigure.MyServiceAutoConfiguration
  ```

  **4. Usage in other projects:**

  ```xml
  <dependency>
      <groupId>com.example</groupId>
      <artifactId>myservice-spring-boot-starter</artifactId>
      <version>1.0.0</version>
  </dependency>
  ```

  ```properties
  # application.properties
  myservice.api-key=abc123
  myservice.timeout=10000
  ```

  ```java
  @Service
  public class UserService {
      
      @Autowired
      private MyService myService;  // Auto-configured!
      
      public void doSomething() {
          myService.call();
      }
  }
  ```

</details>

<details>
  <summary>Auto-configuration Order</summary>
  <br/>

  **Control execution order:**

  ```java
  @Configuration
  @AutoConfigureAfter(DataSourceAutoConfiguration.class)
  public class MyDatabaseConfig {
      // Runs after DataSourceAutoConfiguration
  }
  
  @Configuration
  @AutoConfigureBefore(WebMvcAutoConfiguration.class)
  public class MyWebConfig {
      // Runs before WebMvcAutoConfiguration
  }
  
  @Configuration
  @AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
  public class HighPriorityConfig {
      // Runs early
  }
  ```

  **Example use case:**

  ```java
  @Configuration
  @AutoConfigureAfter(DataSourceAutoConfiguration.class)
  @ConditionalOnBean(DataSource.class)
  public class DatabaseMigrationConfig {
      
      @Bean
      public FlywayMigrationInitializer flywayInitializer(DataSource dataSource) {
          // Runs after DataSource is configured
          return new FlywayMigrationInitializer(dataSource);
      }
  }
  ```

</details>

<details>
  <summary>Common Customization Scenarios</summary>
  <br/>

  **1. Custom Jackson configuration:**

  ```java
  @Configuration
  public class JacksonConfig {
      
      @Bean
      public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
          return builder -> {
              builder.simpleDateFormat("yyyy-MM-dd HH:mm:ss");
              builder.serializationInclusion(JsonInclude.Include.NON_NULL);
              builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
          };
      }
  }
  ```

  **2. Custom error handling:**

  ```java
  @Configuration
  public class ErrorConfig {
      
      @Bean
      public ErrorAttributes errorAttributes() {
          return new DefaultErrorAttributes() {
              @Override
              public Map<String, Object> getErrorAttributes(WebRequest request, 
                      ErrorAttributeOptions options) {
                  Map<String, Object> errorAttributes = super.getErrorAttributes(request, options);
                  errorAttributes.put("timestamp", LocalDateTime.now());
                  errorAttributes.put("customField", "customValue");
                  return errorAttributes;
              }
          };
      }
  }
  ```

  **3. Custom embedded server:**

  ```java
  @Configuration
  public class TomcatConfig {
      
      @Bean
      public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatCustomizer() {
          return factory -> {
              factory.setPort(9090);
              factory.setContextPath("/api");
              factory.addConnectorCustomizers(connector -> {
                  connector.setMaxPostSize(10485760); // 10MB
              });
          };
      }
  }
  ```

  **4. Multiple DataSources:**

  ```java
  @Configuration
  public class MultiDataSourceConfig {
      
      @Bean
      @Primary
      @ConfigurationProperties("spring.datasource.primary")
      public DataSource primaryDataSource() {
          return DataSourceBuilder.create().build();
      }
      
      @Bean
      @ConfigurationProperties("spring.datasource.secondary")
      public DataSource secondaryDataSource() {
          return DataSourceBuilder.create().build();
      }
  }
  ```

  ```properties
  spring.datasource.primary.url=jdbc:mysql://localhost:3306/db1
  spring.datasource.primary.username=user1
  
  spring.datasource.secondary.url=jdbc:mysql://localhost:3306/db2
  spring.datasource.secondary.username=user2
  ```

</details>

<details>
  <summary>Best Practices</summary>
  <br/>

  ```java
  // ✅ DO: Use properties for configuration
  spring.datasource.url=jdbc:mysql://localhost:3306/db
  spring.datasource.hikari.maximum-pool-size=20
  
  // ✅ DO: Provide custom bean when needed
  @Bean
  public DataSource dataSource() {
      // Custom configuration
      return new HikariDataSource(config);
  }
  
  // ✅ DO: Use @ConditionalOnMissingBean for defaults
  @Bean
  @ConditionalOnMissingBean
  public CacheManager cacheManager() {
      return new ConcurrentMapCacheManager();
  }
  
  // ✅ DO: Use @ConfigurationProperties for complex config
  @ConfigurationProperties(prefix = "app")
  public class AppProperties {
      private String apiKey;
      private int timeout;
  }
  
  // ❌ DON'T: Override auto-configuration unnecessarily
  @Bean
  public DataSource dataSource() {
      // Don't do this if properties are enough
      return DataSourceBuilder.create()
          .url("jdbc:mysql://localhost:3306/db")
          .build();
  }
  
  // ❌ DON'T: Exclude auto-configurations without reason
  @SpringBootApplication(exclude = {
      DataSourceAutoConfiguration.class  // Why exclude if you need it?
  })
  
  // ✅ DO: Enable debug to understand auto-configuration
  debug=true
  
  // ✅ DO: Use actuator to monitor auto-configurations
  management.endpoints.web.exposure.include=conditions
  
  // ✅ DO: Document custom auto-configurations
  /**
   * Auto-configures MyService when:
   * - MyService class is on classpath
   * - No MyService bean is defined
   * - Property myservice.enabled is true (default)
   */
  @Configuration
  @ConditionalOnClass(MyService.class)
  @ConditionalOnMissingBean(MyService.class)
  @ConditionalOnProperty(name = "myservice.enabled", matchIfMissing = true)
  public class MyServiceAutoConfiguration {
  }
  ```

  **Key Points**
  <br/>

  + Auto-configuration **automatically configures** beans based on classpath
  + Uses **@Conditional** annotations to determine when to apply
  + Registered in **META-INF/spring.factories**
  + **Backs off** when you provide your own beans
  + Customize via **properties** or **custom beans**
  + Exclude with **@SpringBootApplication(exclude=...)**
  + View active configurations with **debug=true**
  + Use **@ConditionalOnMissingBean** for default implementations
  + Create custom auto-configuration for **reusable starters**
  + Control order with **@AutoConfigureAfter/@AutoConfigureBefore**
  + Test auto-configurations with **@SpringBootTest**
  + Always prefer **properties over code** for simple customization

</details>

## Starters

<details>
  <summary>What are Spring Boot Starters?</summary>
  <br/>

  Starters are dependency descriptors that bundle related dependencies together for specific functionality.

  **Without Starter:**

  ```xml
  <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-web</artifactId>
  </dependency>
  <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-webmvc</artifactId>
  </dependency>
  <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-databind</artifactId>
  </dependency>
  <dependency>
      <groupId>org.apache.tomcat.embed</groupId>
      <artifactId>tomcat-embed-core</artifactId>
  </dependency>
  <!-- Many more dependencies... -->
  ```

  **With Starter:**

  ```xml
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
  </dependency>
  ```

  **Benefits:**
  + Single dependency for complete functionality
  + Compatible versions managed automatically
  + Reduces dependency conflicts
  + Includes auto-configuration
  + Faster project setup

</details>

<details>
  <summary>Core Starters</summary>
  <br/>

  **spring-boot-starter**

  Core starter including auto-configuration, logging, and YAML support.

  ```xml
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter</artifactId>
  </dependency>
  ```

  **Includes:**
  + spring-boot
  + spring-boot-autoconfigure
  + spring-boot-starter-logging
  + spring-core
  + snakeyaml (YAML support)

  **Use when:** Building any Spring Boot application (included in all other starters)

  ---

  **spring-boot-starter-test**

  Testing starter with JUnit, Mockito, AssertJ, and Spring Test.

  ```xml
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
      <scope>test</scope>
  </dependency>
  ```

  **Includes:**
  + JUnit 5
  + Spring Test & Spring Boot Test
  + Mockito
  + AssertJ
  + Hamcrest
  + JSONassert
  + JsonPath

  **Example:**

  ```java
  @SpringBootTest
  class UserServiceTest {
      
      @Autowired
      private UserService userService;
      
      @MockBean
      private UserRepository userRepository;
      
      @Test
      void testFindUser() {
          when(userRepository.findById(1L))
              .thenReturn(Optional.of(new User("John")));
          
          User user = userService.findUser(1L);
          assertEquals("John", user.getName());
      }
  }
  ```

</details>

<details>
  <summary>Web Starters</summary>
  <br/>

  **spring-boot-starter-web**

  Build web applications including RESTful APIs using Spring MVC.

  ```xml
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
  </dependency>
  ```

  **Includes:**
  + Spring MVC
  + Embedded Tomcat
  + Jackson (JSON)
  + Hibernate Validator
  + spring-boot-starter (core)

  **Example:**

  ```java
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
      
      @GetMapping("/{id}")
      public User getUser(@PathVariable Long id) {
          return userService.findUser(id);
      }
      
      @PostMapping
      public User createUser(@Valid @RequestBody User user) {
          return userService.save(user);
      }
  }
  ```

  ---

  **spring-boot-starter-webflux**

  Build reactive web applications using Spring WebFlux.

  ```xml
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-webflux</artifactId>
  </dependency>
  ```

  **Includes:**
  + Spring WebFlux
  + Reactor Core
  + Netty (embedded server)
  + Jackson

  **Example:**

  ```java
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
      
      @GetMapping
      public Flux<User> getAllUsers() {
          return userService.findAll();
      }
      
      @GetMapping("/{id}")
      public Mono<User> getUser(@PathVariable Long id) {
          return userService.findById(id);
      }
  }
  ```

  ---

  **spring-boot-starter-thymeleaf**

  Server-side template engine for web applications.

  ```xml
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-thymeleaf</artifactId>
  </dependency>
  ```

  **Example:**

  ```java
  @Controller
  public class HomeController {
      
      @GetMapping("/")
      public String home(Model model) {
          model.addAttribute("message", "Welcome!");
          return "home";  // templates/home.html
      }
  }
  ```

</details>

<details>
  <summary>Data Access Starters</summary>
  <br/>

  **spring-boot-starter-data-jpa**

  JPA with Hibernate for relational databases.

  ```xml
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-jpa</artifactId>
  </dependency>
  ```

  **Includes:**
  + Spring Data JPA
  + Hibernate
  + Spring ORM
  + Transaction support

  **Example:**

  ```java
  @Entity
  public class User {
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;
      private String name;
      private String email;
  }
  
  @Repository
  public interface UserRepository extends JpaRepository<User, Long> {
      List<User> findByName(String name);
      Optional<User> findByEmail(String email);
  }
  ```

  ---

  **spring-boot-starter-data-mongodb**

  MongoDB document database support.

  ```xml
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-mongodb</artifactId>
  </dependency>
  ```

  **Example:**

  ```java
  @Document(collection = "users")
  public class User {
      @Id
      private String id;
      private String name;
      private String email;
  }
  
  @Repository
  public interface UserRepository extends MongoRepository<User, String> {
      List<User> findByName(String name);
  }
  ```

  ---

  **spring-boot-starter-data-redis**

  Redis key-value store support.

  ```xml
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-redis</artifactId>
  </dependency>
  ```

  **Example:**

  ```java
  @Service
  public class CacheService {
      
      @Autowired
      private RedisTemplate<String, Object> redisTemplate;
      
      public void save(String key, Object value) {
          redisTemplate.opsForValue().set(key, value);
      }
      
      public Object get(String key) {
          return redisTemplate.opsForValue().get(key);
      }
  }
  ```

  ---

  **spring-boot-starter-jdbc**

  JDBC with connection pooling (without JPA).

  ```xml
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-jdbc</artifactId>
  </dependency>
  ```

  **Example:**

  ```java
  @Repository
  public class UserRepository {
      
      @Autowired
      private JdbcTemplate jdbcTemplate;
      
      public User findById(Long id) {
          return jdbcTemplate.queryForObject(
              "SELECT * FROM users WHERE id = ?",
              new BeanPropertyRowMapper<>(User.class),
              id
          );
      }
  }
  ```

</details>

<details>
  <summary>Security Starter</summary>
  <br/>

  **spring-boot-starter-security**

  Spring Security for authentication and authorization.

  ```xml
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-security</artifactId>
  </dependency>
  ```

  **Includes:**
  + Spring Security Core
  + Spring Security Config
  + Spring Security Web
  + Default security configuration

  **Default behavior:**
  + Secures all endpoints
  + Generates default password (printed in console)
  + Provides login page
  + Enables CSRF protection

  **Example:**

  ```java
  @Configuration
  @EnableWebSecurity
  public class SecurityConfig {
      
      @Bean
      public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
          http
              .authorizeHttpRequests(auth -> auth
                  .requestMatchers("/public/**").permitAll()
                  .requestMatchers("/admin/**").hasRole("ADMIN")
                  .anyRequest().authenticated()
              )
              .formLogin(form -> form
                  .loginPage("/login")
                  .permitAll()
              )
              .logout(logout -> logout.permitAll());
          
          return http.build();
      }
      
      @Bean
      public PasswordEncoder passwordEncoder() {
          return new BCryptPasswordEncoder();
      }
  }
  ```

</details>

<details>
  <summary>Messaging Starters</summary>
  <br/>

  **spring-boot-starter-amqp**

  RabbitMQ messaging support.

  ```xml
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-amqp</artifactId>
  </dependency>
  ```

  **Example:**

  ```java
  @Service
  public class MessageProducer {
      
      @Autowired
      private RabbitTemplate rabbitTemplate;
      
      public void sendMessage(String message) {
          rabbitTemplate.convertAndSend("myQueue", message);
      }
  }
  
  @Component
  public class MessageConsumer {
      
      @RabbitListener(queues = "myQueue")
      public void receiveMessage(String message) {
          System.out.println("Received: " + message);
      }
  }
  ```

  ---

  **spring-boot-starter-kafka**

  Apache Kafka messaging support.

  ```xml
  <dependency>
      <groupId>org.springframework.kafka</groupId>
      <artifactId>spring-kafka</artifactId>
  </dependency>
  ```

  **Example:**

  ```java
  @Service
  public class KafkaProducer {
      
      @Autowired
      private KafkaTemplate<String, String> kafkaTemplate;
      
      public void sendMessage(String topic, String message) {
          kafkaTemplate.send(topic, message);
      }
  }
  
  @Component
  public class KafkaConsumer {
      
      @KafkaListener(topics = "myTopic", groupId = "myGroup")
      public void listen(String message) {
          System.out.println("Received: " + message);
      }
  }
  ```

</details>

<details>
  <summary>Monitoring & Management Starters</summary>
  <br/>

  **spring-boot-starter-actuator**

  Production-ready features for monitoring and management.

  ```xml
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-actuator</artifactId>
  </dependency>
  ```

  **Provides endpoints:**
  + `/actuator/health` - Application health
  + `/actuator/info` - Application info
  + `/actuator/metrics` - Application metrics
  + `/actuator/env` - Environment properties
  + `/actuator/loggers` - Logger configuration
  + `/actuator/beans` - All Spring beans

  **Configuration:**

  ```properties
  management.endpoints.web.exposure.include=health,info,metrics
  management.endpoint.health.show-details=always
  
  info.app.name=My Application
  info.app.version=1.0.0
  ```

  **Custom health indicator:**

  ```java
  @Component
  public class CustomHealthIndicator implements HealthIndicator {
      
      @Override
      public Health health() {
          boolean isHealthy = checkExternalService();
          
          if (isHealthy) {
              return Health.up()
                  .withDetail("service", "available")
                  .build();
          }
          return Health.down()
              .withDetail("service", "unavailable")
              .build();
      }
  }
  ```

</details>

<details>
  <summary>Validation Starter</summary>
  <br/>

  **spring-boot-starter-validation**

  Bean Validation with Hibernate Validator.

  ```xml
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-validation</artifactId>
  </dependency>
  ```

  **Includes:**
  + Hibernate Validator
  + Jakarta Bean Validation API
  + EL implementation

  **Example:**

  ```java
  public class User {
      
      @NotNull(message = "Name is required")
      @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
      private String name;
      
      @NotBlank(message = "Email is required")
      @Email(message = "Invalid email format")
      private String email;
      
      @Min(value = 18, message = "Must be at least 18 years old")
      private int age;
  }
  
  @RestController
  public class UserController {
      
      @PostMapping("/users")
      public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
          return ResponseEntity.ok(userService.save(user));
      }
  }
  ```

</details>

<details>
  <summary>Cache Starters</summary>
  <br/>

  **spring-boot-starter-cache**

  Caching abstraction support.

  ```xml
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-cache</artifactId>
  </dependency>
  ```

  **Example:**

  ```java
  @Configuration
  @EnableCaching
  public class CacheConfig {
      
      @Bean
      public CacheManager cacheManager() {
          return new ConcurrentMapCacheManager("users", "products");
      }
  }
  
  @Service
  public class UserService {
      
      @Cacheable(value = "users", key = "#id")
      public User findUser(Long id) {
          // Expensive database call
          return userRepository.findById(id);
      }
      
      @CacheEvict(value = "users", key = "#id")
      public void deleteUser(Long id) {
          userRepository.deleteById(id);
      }
  }
  ```

  ---

  **With Redis:**

  ```xml
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-redis</artifactId>
  </dependency>
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-cache</artifactId>
  </dependency>
  ```

  ```java
  @Configuration
  @EnableCaching
  public class RedisCacheConfig {
      
      @Bean
      public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
          return RedisCacheManager.builder(connectionFactory)
              .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig()
                  .entryTtl(Duration.ofMinutes(10)))
              .build();
      }
  }
  ```

</details>

<details>
  <summary>Mail Starter</summary>
  <br/>

  **spring-boot-starter-mail**

  Email sending support.

  ```xml
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-mail</artifactId>
  </dependency>
  ```

  **Configuration:**

  ```properties
  spring.mail.host=smtp.gmail.com
  spring.mail.port=587
  spring.mail.username=your-email@gmail.com
  spring.mail.password=your-password
  spring.mail.properties.mail.smtp.auth=true
  spring.mail.properties.mail.smtp.starttls.enable=true
  ```

  **Example:**

  ```java
  @Service
  public class EmailService {
      
      @Autowired
      private JavaMailSender mailSender;
      
      public void sendEmail(String to, String subject, String text) {
          SimpleMailMessage message = new SimpleMailMessage();
          message.setTo(to);
          message.setSubject(subject);
          message.setText(text);
          mailSender.send(message);
      }
      
      public void sendHtmlEmail(String to, String subject, String html) {
          MimeMessage message = mailSender.createMimeMessage();
          MimeMessageHelper helper = new MimeMessageHelper(message, true);
          helper.setTo(to);
          helper.setSubject(subject);
          helper.setText(html, true);
          mailSender.send(message);
      }
  }
  ```

</details>

<details>
  <summary>Batch Processing Starter</summary>
  <br/>

  **spring-boot-starter-batch**

  Spring Batch for batch processing.

  ```xml
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-batch</artifactId>
  </dependency>
  ```

  **Example:**

  ```java
  @Configuration
  @EnableBatchProcessing
  public class BatchConfig {
      
      @Bean
      public Job importUserJob(JobRepository jobRepository, Step step1) {
          return new JobBuilder("importUserJob", jobRepository)
              .start(step1)
              .build();
      }
      
      @Bean
      public Step step1(JobRepository jobRepository, 
                        PlatformTransactionManager transactionManager,
                        ItemReader<User> reader,
                        ItemProcessor<User, User> processor,
                        ItemWriter<User> writer) {
          return new StepBuilder("step1", jobRepository)
              .<User, User>chunk(10, transactionManager)
              .reader(reader)
              .processor(processor)
              .writer(writer)
              .build();
      }
  }
  ```

</details>

<details>
  <summary>Comparison Table</summary>
  <br/>

  | Starter | Purpose | Key Dependencies | Use Case |
  |---------|---------|------------------|----------|
  | **spring-boot-starter-web** | REST APIs, Web apps | Spring MVC, Tomcat, Jackson | Building RESTful services |
  | **spring-boot-starter-data-jpa** | JPA/Hibernate | Spring Data JPA, Hibernate | Relational database access |
  | **spring-boot-starter-security** | Authentication/Authorization | Spring Security | Securing applications |
  | **spring-boot-starter-test** | Testing | JUnit, Mockito, Spring Test | Unit and integration tests |
  | **spring-boot-starter-actuator** | Monitoring | Actuator endpoints | Production monitoring |
  | **spring-boot-starter-data-mongodb** | MongoDB | Spring Data MongoDB | NoSQL document database |
  | **spring-boot-starter-data-redis** | Redis | Spring Data Redis | Caching, session storage |
  | **spring-boot-starter-amqp** | RabbitMQ | Spring AMQP | Message queuing |
  | **spring-boot-starter-validation** | Bean Validation | Hibernate Validator | Input validation |
  | **spring-boot-starter-cache** | Caching | Spring Cache | Application caching |
  | **spring-boot-starter-mail** | Email | JavaMail | Sending emails |
  | **spring-boot-starter-webflux** | Reactive web | Spring WebFlux, Reactor | Reactive applications |
  | **spring-boot-starter-batch** | Batch processing | Spring Batch | Large data processing |

</details>

<details>
  <summary>Creating Custom Starter</summary>
  <br/>

  **1. Create starter project structure:**

  ```
  my-service-spring-boot-starter/
  ├── pom.xml
  └── src/main/
      ├── java/
      │   └── com/example/starter/
      │       ├── MyServiceAutoConfiguration.java
      │       ├── MyServiceProperties.java
      │       └── MyService.java
      └── resources/
          └── META-INF/
              └── spring.factories
  ```

  **2. Create service:**

  ```java
  public class MyService {
      private final String apiKey;
      private final int timeout;
      
      public MyService(String apiKey, int timeout) {
          this.apiKey = apiKey;
          this.timeout = timeout;
      }
      
      public String call(String endpoint) {
          // Service implementation
          return "Response from " + endpoint;
      }
  }
  ```

  **3. Create properties:**

  ```java
  @ConfigurationProperties(prefix = "myservice")
  public class MyServiceProperties {
      private String apiKey;
      private int timeout = 5000;
      private boolean enabled = true;
      
      // getters and setters
  }
  ```

  **4. Create auto-configuration:**

  ```java
  @Configuration
  @ConditionalOnClass(MyService.class)
  @ConditionalOnProperty(name = "myservice.enabled", havingValue = "true", matchIfMissing = true)
  @EnableConfigurationProperties(MyServiceProperties.class)
  public class MyServiceAutoConfiguration {
      
      @Bean
      @ConditionalOnMissingBean
      public MyService myService(MyServiceProperties properties) {
          return new MyService(properties.getApiKey(), properties.getTimeout());
      }
  }
  ```

  **5. Register in spring.factories:**

  ```
  # META-INF/spring.factories
  org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
  com.example.starter.MyServiceAutoConfiguration
  ```

  **6. POM configuration:**

  ```xml
  <project>
      <groupId>com.example</groupId>
      <artifactId>my-service-spring-boot-starter</artifactId>
      <version>1.0.0</version>
      
      <dependencies>
          <dependency>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-starter</artifactId>
          </dependency>
          <dependency>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-configuration-processor</artifactId>
              <optional>true</optional>
          </dependency>
      </dependencies>
  </project>
  ```

  **7. Usage in other projects:**

  ```xml
  <dependency>
      <groupId>com.example</groupId>
      <artifactId>my-service-spring-boot-starter</artifactId>
      <version>1.0.0</version>
  </dependency>
  ```

  ```properties
  myservice.api-key=abc123
  myservice.timeout=10000
  myservice.enabled=true
  ```

  ```java
  @Service
  public class UserService {
      
      @Autowired
      private MyService myService;  // Auto-configured!
      
      public void doSomething() {
          String response = myService.call("/endpoint");
      }
  }
  ```

</details>

<details>
  <summary>Best Practices</summary>
  <br/>

  ```java
  // ✅ DO: Use appropriate starter for functionality
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
  </dependency>
  
  // ✅ DO: Exclude unnecessary dependencies
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
      <exclusions>
          <exclusion>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-starter-tomcat</artifactId>
          </exclusion>
      </exclusions>
  </dependency>
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-jetty</artifactId>
  </dependency>
  
  // ✅ DO: Use test starter with test scope
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
      <scope>test</scope>
  </dependency>
  
  // ❌ DON'T: Mix web starters
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
  </dependency>
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-webflux</artifactId>  <!-- Conflict! -->
  </dependency>
  
  // ✅ DO: Name custom starters properly
  my-service-spring-boot-starter  // Good
  my-service-starter              // Bad
  
  // ✅ DO: Document custom starter properties
  /**
   * Configuration properties for MyService.
   * 
   * @ConfigurationProperties(prefix = "myservice")
   */
  @ConfigurationProperties(prefix = "myservice")
  public class MyServiceProperties {
      /**
       * API key for authentication (required)
       */
      private String apiKey;
      
      /**
       * Request timeout in milliseconds (default: 5000)
       */
      private int timeout = 5000;
  }
  
  // ✅ DO: Provide sensible defaults
  @ConfigurationProperties(prefix = "myservice")
  public class MyServiceProperties {
      private int timeout = 5000;  // Default value
      private boolean enabled = true;  // Enabled by default
  }
  
  // ✅ DO: Use @ConditionalOnMissingBean for flexibility
  @Bean
  @ConditionalOnMissingBean
  public MyService myService(MyServiceProperties properties) {
      return new MyService(properties);
  }
  ```

  **Key Points**
  <br/>

  + Starters are **dependency descriptors** that bundle related dependencies
  + Single dependency provides **complete functionality**
  + Includes **auto-configuration** for zero-config setup
  + **spring-boot-starter-web** for REST APIs and web apps
  + **spring-boot-starter-data-jpa** for database access with JPA
  + **spring-boot-starter-security** for authentication/authorization
  + **spring-boot-starter-test** for testing (use test scope)
  + **spring-boot-starter-actuator** for production monitoring
  + Custom starters follow naming: **{name}-spring-boot-starter**
  + Use **@ConditionalOnMissingBean** for overridable defaults
  + Exclude dependencies when switching implementations (e.g., Tomcat → Jetty)
  + Don't mix conflicting starters (web vs webflux)
  + Document custom starter properties with JavaDoc
  + Provide **sensible defaults** in @ConfigurationProperties
  + Register auto-configuration in **META-INF/spring.factories**

</details>

## Application Properties

<details>
  <summary>Properties vs YAML</summary>
  <br/>

  Spring Boot supports both `.properties` and `.yml` formats for configuration.

  **application.properties:**

  ```properties
  server.port=8080
  server.servlet.context-path=/api
  
  spring.datasource.url=jdbc:mysql://localhost:3306/db
  spring.datasource.username=root
  spring.datasource.password=secret
  spring.datasource.hikari.maximum-pool-size=20
  spring.datasource.hikari.connection-timeout=30000
  
  logging.level.root=INFO
  logging.level.com.example=DEBUG
  ```

  **application.yml (same configuration):**

  ```yaml
  server:
    port: 8080
    servlet:
      context-path: /api
  
  spring:
    datasource:
      url: jdbc:mysql://localhost:3306/db
      username: root
      password: secret
      hikari:
        maximum-pool-size: 20
        connection-timeout: 30000
  
  logging:
    level:
      root: INFO
      com.example: DEBUG
  ```

  **Comparison:**

  | Feature | Properties | YAML |
  |---------|-----------|------|
  | **Readability** | Less readable (flat structure) | More readable (hierarchical) |
  | **Hierarchy** | Flat with dots | Nested indentation |
  | **Lists** | Indexed [0], [1] | Native array syntax |
  | **Duplication** | Repeat prefixes | No repetition |
  | **IDE Support** | Better autocomplete | Good support |
  | **Parsing** | Faster | Slightly slower |
  | **Syntax errors** | More forgiving | Strict (indentation matters) |

  **List handling:**

  ```properties
  # Properties
  app.servers[0]=server1
  app.servers[1]=server2
  app.servers[2]=server3
  ```

  ```yaml
  # YAML
  app:
    servers:
      - server1
      - server2
      - server3
  ```

  **When to use:**
  + **Properties**: Simple config, better IDE autocomplete, team unfamiliar with YAML
  + **YAML**: Complex hierarchies, lists, multiple profiles in one file, better readability

  **Note:** If both files exist, properties file takes precedence over YAML.

</details>

<details>
  <summary>Profile-Specific Properties</summary>
  <br/>

  Spring Boot allows different configurations for different environments (dev, test, prod) using profiles.

  **File structure:**

  ```
  application.properties          # Default (always loaded)
  application-dev.properties      # Dev profile
  application-test.properties     # Test profile
  application-prod.properties     # Prod profile
  ```

  **application.properties (base config):**

  ```properties
  # Common properties for all profiles
  spring.application.name=myapp
  spring.profiles.active=dev
  ```

  **application-dev.properties:**

  ```properties
  server.port=8080
  spring.datasource.url=jdbc:h2:mem:testdb
  spring.datasource.username=sa
  spring.datasource.password=
  logging.level.root=DEBUG
  ```

  **application-prod.properties:**

  ```properties
  server.port=80
  spring.datasource.url=jdbc:mysql://prod-server:3306/db
  spring.datasource.username=admin
  spring.datasource.password=${DB_PASSWORD}
  logging.level.root=WARN
  ```

  **Activating profiles:**

  ```bash
  # Command line
  java -jar app.jar --spring.profiles.active=prod
  
  # Environment variable
  export SPRING_PROFILES_ACTIVE=prod
  
  # In application.properties
  spring.profiles.active=dev
  
  # Multiple profiles
  spring.profiles.active=prod,monitoring
  ```

  **YAML (single file with multiple profiles):**

  ```yaml
  spring:
    application:
      name: myapp
    profiles:
      active: dev
  
  ---
  spring:
    config:
      activate:
        on-profile: dev
  server:
    port: 8080
  spring:
    datasource:
      url: jdbc:h2:mem:testdb
  
  ---
  spring:
    config:
      activate:
        on-profile: prod
  server:
    port: 80
  spring:
    datasource:
      url: jdbc:mysql://prod-server:3306/db
  ```

  **Benefits:**
  + Separate configuration per environment
  + Easy switching between environments
  + No code changes needed
  + Can activate multiple profiles simultaneously

</details>

<details>
  <summary>Externalized Configuration</summary>
  <br/>

  Spring Boot allows configuration from multiple sources. When the same property is defined in multiple places, Spring Boot uses a specific priority order.

  **Priority order (highest to lowest):**

  ```
  1. Command line arguments                    (--server.port=9090)
  2. SPRING_APPLICATION_JSON                   (JSON in env var)
  3. ServletConfig/ServletContext parameters   (web.xml)
  4. JNDI attributes                           (java:comp/env)
  5. Java System properties                    (-Dserver.port=9090)
  6. OS environment variables                  (SERVER_PORT=9090)
  7. application-{profile}.properties outside jar
  8. application-{profile}.properties inside jar
  9. application.properties outside jar
  10. application.properties inside jar
  11. @PropertySource annotations
  12. Default properties
  ```

  **Higher priority overrides lower priority.**

  **Examples:**

  **1. Command line arguments (highest priority):**

  ```bash
  java -jar app.jar --server.port=9090 --spring.profiles.active=prod
  ```

  **2. Environment variables:**

  ```bash
  # Set environment variables
  export SERVER_PORT=9090
  export SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/db
  export SPRING_DATASOURCE_PASSWORD=secret
  
  # Run application
  java -jar app.jar
  ```

  **Naming convention:** `spring.datasource.url` → `SPRING_DATASOURCE_URL`
  + Replace dots with underscores
  + Convert to uppercase

  **3. System properties:**

  ```bash
  java -Dserver.port=9090 -Dspring.profiles.active=prod -jar app.jar
  ```

  **4. External config file:**

  ```bash
  # Specify custom location
  java -jar app.jar --spring.config.location=file:/config/application.properties
  
  # Add additional location (doesn't replace default)
  java -jar app.jar --spring.config.additional-location=file:/config/
  ```

  **5. Config directory structure (automatic detection):**

  ```
  /app/
  ├── config/
  │   └── application.properties  # Priority 1 (highest)
  ├── application.properties      # Priority 2
  └── app.jar
      └── application.properties  # Priority 3 (lowest)
  ```

  **Why externalize?**
  + Change config without rebuilding
  + Different configs per environment
  + Keep secrets out of source code
  + Override defaults easily

</details>

<details>
  <summary><code>@Value</code> Annotation</summary>
  <br/>

  `@Value` injects property values directly into Spring beans.

  **application.properties:**

  ```properties
  app.name=MyApp
  app.timeout=5000
  app.max-users=100
  app.features=feature1,feature2,feature3
  app.api.url=https://api.example.com
  ```

  **Basic usage:**

  ```java
  @Component
  public class AppConfig {
      
      // Simple property injection
      @Value("${app.name}")
      private String appName;
      
      // Numeric values
      @Value("${app.timeout}")
      private int timeout;
      
      // Default value (if property not found)
      @Value("${app.max-users:50}")
      private int maxUsers;
      
      // List from comma-separated values
      @Value("${app.features}")
      private List<String> features;
      
      // SpEL expression
      @Value("#{${app.timeout} * 2}")
      private int doubleTimeout;
      
      // System property
      @Value("${java.home}")
      private String javaHome;
      
      // Environment variable
      @Value("${PATH}")
      private String path;
  }
  ```

  **Constructor injection (recommended):**

  ```java
  @Component
  public class AppConfig {
      
      private final String appName;
      private final int timeout;
      
      public AppConfig(
          @Value("${app.name}") String appName,
          @Value("${app.timeout}") int timeout
      ) {
          this.appName = appName;
          this.timeout = timeout;
      }
  }
  ```

  **When to use:**
  + Simple property injection
  + Few properties
  + Quick prototyping

  **Limitations:**
  + No type safety
  + No validation
  + Hard to test
  + Not suitable for complex configuration

</details>

<details>
  <summary><code>@ConfigurationProperties</code></summary>
  <br/>

  `@ConfigurationProperties` provides type-safe configuration binding for complex properties. **Preferred over @Value for complex configuration.**

  **application.properties:**

  ```properties
  app.name=MyApp
  app.timeout=5000
  app.database.url=jdbc:mysql://localhost:3306/db
  app.database.username=root
  app.database.max-connections=20
  app.features[0]=feature1
  app.features[1]=feature2
  ```

  **Configuration class:**

  ```java
  @ConfigurationProperties(prefix = "app")
  @Validated  // Enable validation
  public class AppProperties {
      
      @NotBlank(message = "App name is required")
      private String name;
      
      @Min(value = 1000, message = "Timeout must be at least 1000ms")
      private int timeout;
      
      private Database database;
      private List<String> features;
      
      // Nested configuration
      public static class Database {
          private String url;
          private String username;
          private int maxConnections;
          
          // getters and setters
      }
      
      // getters and setters
  }
  ```

  **Enable configuration properties:**

  ```java
  @Configuration
  @EnableConfigurationProperties(AppProperties.class)
  public class AppConfig {
  }
  
  // Or use @ConfigurationPropertiesScan
  @SpringBootApplication
  @ConfigurationPropertiesScan
  public class Application {
  }
  ```

  **Usage in service:**

  ```java
  @Service
  public class MyService {
      
      private final AppProperties appProperties;
      
      // Constructor injection
      public MyService(AppProperties appProperties) {
          this.appProperties = appProperties;
      }
      
      public void doSomething() {
          String name = appProperties.getName();
          int timeout = appProperties.getTimeout();
          String dbUrl = appProperties.getDatabase().getUrl();
      }
  }
  ```

  **YAML version (cleaner for nested properties):**

  ```yaml
  app:
    name: MyApp
    timeout: 5000
    database:
      url: jdbc:mysql://localhost:3306/db
      username: root
      max-connections: 20
    features:
      - feature1
      - feature2
  ```

  **Advantages over @Value:**
  + ✅ Type-safe (compile-time checking)
  + ✅ Validation support (@Validated, JSR-303)
  + ✅ Better for complex, nested properties
  + ✅ Easier to test (POJO)
  + ✅ IDE autocomplete support
  + ✅ Relaxed binding (app.api-key = app.apiKey = APP_API_KEY)
  + ✅ Immutable with constructor binding

  **Immutable configuration (recommended):**

  ```java
  @ConfigurationProperties(prefix = "app")
  @ConstructorBinding  // Spring Boot 2.x
  public class AppProperties {
      
      private final String name;
      private final int timeout;
      
      public AppProperties(String name, int timeout) {
          this.name = name;
          this.timeout = timeout;
      }
      
      // Only getters, no setters
      public String getName() { return name; }
      public int getTimeout() { return timeout; }
  }
  ```

</details>

<details>
  <summary>Environment Variables</summary>
  <br/>

  Environment variables are commonly used in production for configuration, especially in containerized environments.

  **Naming convention:**

  Spring Boot uses **relaxed binding** to map environment variables to properties:

  ```properties
  # Properties file
  spring.datasource.url=jdbc:mysql://localhost:3306/db
  server.port=8080
  app.api-key=secret
  ```

  ```bash
  # Environment variables (uppercase, underscores replace dots and dashes)
  SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/db
  SERVER_PORT=8080
  APP_API_KEY=secret
  ```

  **Conversion rules:**
  + Dots (`.`) → Underscores (`_`)
  + Dashes (`-`) → Underscores (`_`)
  + Lowercase → Uppercase

  **Setting environment variables:**

  ```bash
  # Linux/Mac
  export SPRING_PROFILES_ACTIVE=prod
  export SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/db
  export SPRING_DATASOURCE_PASSWORD=secret
  java -jar app.jar
  
  # Windows
  set SPRING_PROFILES_ACTIVE=prod
  set SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/db
  java -jar app.jar
  ```

  **Docker example:**

  ```dockerfile
  # Dockerfile
  FROM openjdk:17-jdk-slim
  COPY target/app.jar app.jar
  ENTRYPOINT ["java", "-jar", "app.jar"]
  ```

  ```yaml
  # docker-compose.yml
  version: '3.8'
  services:
    app:
      image: myapp:latest
      environment:
        - SPRING_PROFILES_ACTIVE=prod
        - SPRING_DATASOURCE_URL=jdbc:mysql://db:3306/mydb
        - SPRING_DATASOURCE_USERNAME=root
        - SPRING_DATASOURCE_PASSWORD=secret
        - SERVER_PORT=8080
      ports:
        - "8080:8080"
  ```

  **Kubernetes ConfigMap:**

  ```yaml
  apiVersion: v1
  kind: ConfigMap
  metadata:
    name: app-config
  data:
    SPRING_PROFILES_ACTIVE: "prod"
    SERVER_PORT: "8080"
    SPRING_DATASOURCE_URL: "jdbc:mysql://db:3306/mydb"
  ---
  apiVersion: v1
  kind: Pod
  metadata:
    name: myapp
  spec:
    containers:
    - name: app
      image: myapp:latest
      envFrom:
      - configMapRef:
          name: app-config
  ```

  **Kubernetes Secret (for sensitive data):**

  ```yaml
  apiVersion: v1
  kind: Secret
  metadata:
    name: app-secret
  type: Opaque
  data:
    SPRING_DATASOURCE_PASSWORD: c2VjcmV0  # base64 encoded
  ---
  apiVersion: v1
  kind: Pod
  metadata:
    name: myapp
  spec:
    containers:
    - name: app
      image: myapp:latest
      envFrom:
      - secretRef:
          name: app-secret
  ```

  **Why use environment variables?**
  + Standard in cloud/container environments
  + Easy to change without rebuilding
  + Supported by all deployment platforms
  + Keeps secrets out of code

</details>

<details>
  <summary>Secrets Management</summary>
  <br/>

  **Never hardcode secrets in properties files!** Use secure methods to manage sensitive data.

  **1. Environment variables (recommended for containers):**

  ```bash
  # Set secret as environment variable
  export DB_PASSWORD=secret
  export API_KEY=abc123
  java -jar app.jar
  ```

  ```properties
  # Reference in properties file
  spring.datasource.password=${DB_PASSWORD}
  app.api.key=${API_KEY}
  ```

  **2. External properties file (outside JAR):**

  ```bash
  # Keep secrets in external file
  java -jar app.jar --spring.config.additional-location=file:/secrets/secrets.properties
  ```

  ```properties
  # /secrets/secrets.properties (not in version control)
  spring.datasource.password=secret
  app.api.key=abc123
  ```

  **3. Spring Cloud Config Server (centralized):**

  ```xml
  <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-config</artifactId>
  </dependency>
  ```

  ```properties
  # bootstrap.properties
  spring.cloud.config.uri=http://config-server:8888
  spring.cloud.config.username=user
  spring.cloud.config.password=pass
  spring.application.name=myapp
  ```

  Config server stores secrets securely and provides them to applications.

  **4. HashiCorp Vault (enterprise solution):**

  ```xml
  <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-vault-config</artifactId>
  </dependency>
  ```

  ```properties
  spring.cloud.vault.uri=https://vault:8200
  spring.cloud.vault.token=s.token
  spring.cloud.vault.kv.enabled=true
  spring.cloud.vault.kv.backend=secret
  ```

  Vault provides:
  + Dynamic secrets
  + Encryption as a service
  + Audit logging
  + Secret rotation

  **5. AWS Secrets Manager / Azure Key Vault:**

  ```xml
  <!-- AWS -->
  <dependency>
      <groupId>com.amazonaws.secretsmanager</groupId>
      <artifactId>aws-secretsmanager-jdbc</artifactId>
  </dependency>
  ```

  ```properties
  spring.datasource.url=jdbc-secretsmanager:mysql://localhost:3306/db
  spring.datasource.username=admin
  ```

  **Best practices:**
  + ✅ Use environment variables in containers
  + ✅ Use external config files (outside JAR)
  + ✅ Use secret management tools (Vault, AWS Secrets Manager)
  + ✅ Encrypt secrets at rest
  + ✅ Rotate secrets regularly
  + ✅ Add secrets files to `.gitignore`
  + ❌ Never commit secrets to version control
  + ❌ Never log secrets
  + ❌ Never hardcode secrets in code

</details>

<details>
  <summary>Property Placeholders</summary>
  <br/>

  Property placeholders allow you to reference other properties and provide default values.

  **Referencing other properties:**

  ```properties
  app.name=MyApp
  app.version=1.0.0
  app.description=${app.name} version ${app.version}
  # Result: "MyApp version 1.0.0"
  
  # Nested references
  app.base-url=https://api.example.com
  app.users-endpoint=${app.base-url}/users
  app.orders-endpoint=${app.base-url}/orders
  ```

  **Default values (if property not found):**

  ```properties
  # Syntax: ${property:defaultValue}
  app.timeout=${APP_TIMEOUT:5000}
  app.max-users=${MAX_USERS:100}
  app.retry-count=${RETRY_COUNT:3}
  
  # Environment-specific with fallback
  db.url=${DB_URL:jdbc:h2:mem:testdb}
  db.username=${DB_USER:sa}
  db.password=${DB_PASSWORD:}
  ```

  **Usage in code:**

  ```java
  @Value("${app.description}")
  private String description;  // "MyApp version 1.0.0"
  
  @Value("${app.timeout}")
  private int timeout;  // 5000 if APP_TIMEOUT not set
  
  @Value("${app.feature.enabled:true}")
  private boolean featureEnabled;  // true by default
  ```

  **Complex placeholders:**

  ```properties
  # Conditional based on profile
  app.mode=${spring.profiles.active}
  app.config-file=config-${spring.profiles.active}.json
  
  # System properties
  app.java-version=${java.version}
  app.user-home=${user.home}
  
  # Multiple fallbacks
  app.host=${APP_HOST:${HOSTNAME:localhost}}
  ```

  **Why use placeholders?**
  + Avoid duplication
  + Centralize common values
  + Provide sensible defaults
  + Environment-specific overrides
  + Easier maintenance

</details>

<details>
  <summary>Best Practices</summary>
  <br/>

  ```properties
  # ✅ DO: Use meaningful property names
  app.api.timeout=5000
  app.api.max-retries=3
  
  # ❌ DON'T: Use cryptic names
  app.t=5000
  app.mr=3
  
  # ✅ DO: Provide defaults
  app.timeout=${APP_TIMEOUT:5000}
  
  # ✅ DO: Use environment variables for secrets
  spring.datasource.password=${DB_PASSWORD}
  
  # ❌ DON'T: Hardcode secrets
  spring.datasource.password=secret123
  
  # ✅ DO: Use @ConfigurationProperties for complex config
  @ConfigurationProperties(prefix = "app")
  public class AppProperties { }
  
  # ✅ DO: Validate properties
  @ConfigurationProperties(prefix = "app")
  @Validated
  public class AppProperties {
      @NotBlank
      private String apiKey;
      
      @Min(1000)
      private int timeout;
  }
  
  # ✅ DO: Use profile-specific files
  application-dev.properties
  application-prod.properties
  
  # ✅ DO: Document custom properties
  # Application timeout in milliseconds
  app.timeout=5000
  ```

  **Key Points**
  <br/>

  + **YAML** is more readable for complex hierarchies
  + **Properties** files have better IDE support
  + Use **profile-specific** files for different environments
  + **Externalize** configuration (env vars, command line, external files)
  + Priority: Command line > Env vars > External files > Internal files
  + Use **@ConfigurationProperties** for complex, type-safe configuration
  + Use **@Value** for simple property injection
  + Never hardcode **secrets** - use environment variables
  + Provide **default values** with `${PROPERTY:default}`
  + Environment variables use **UPPERCASE_WITH_UNDERSCORES**
  + Validate properties with **@Validated** and JSR-303 annotations
  + External config location: `--spring.config.location=file:/path/`
  + Use **Spring Cloud Config** for centralized configuration

</details>

## Actuator

<details>
  <summary>What is Spring Boot Actuator?</summary>
  <br/>

  Actuator provides production-ready features for monitoring and managing Spring Boot applications.

  **Add dependency:**

  ```xml
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-actuator</artifactId>
  </dependency>
  ```

  **What it provides:**
  + Health checks
  + Application metrics
  + Environment information
  + HTTP trace
  + Thread dumps
  + Heap dumps
  + Logger configuration

  **Default endpoints:**
  + `/actuator` - List of available endpoints
  + `/actuator/health` - Application health status
  + `/actuator/info` - Application information

  **Why use Actuator?**
  + Monitor application health in production
  + Collect metrics for performance analysis
  + Debug issues without redeploying
  + Integration with monitoring tools (Prometheus, Grafana)

</details>

<details>
  <summary>Enabling Actuator Endpoints</summary>
  <br/>

  By default, only `/health` and `/info` are exposed over HTTP.

  **Expose all endpoints:**

  ```properties
  # application.properties
  management.endpoints.web.exposure.include=*
  ```

  **Expose specific endpoints:**

  ```properties
  management.endpoints.web.exposure.include=health,info,metrics,env
  ```

  **Exclude endpoints:**

  ```properties
  management.endpoints.web.exposure.include=*
  management.endpoints.web.exposure.exclude=shutdown,threaddump
  ```

  **Change base path:**

  ```properties
  # Default: /actuator
  management.endpoints.web.base-path=/monitoring
  # Access: http://localhost:8080/monitoring/health
  ```

  **Change port:**

  ```properties
  # Run actuator on different port
  management.server.port=9090
  # Access: http://localhost:9090/actuator/health
  ```

  **Security:**

  ```java
  @Configuration
  public class ActuatorSecurityConfig {
      
      @Bean
      public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
          http
              .authorizeHttpRequests(auth -> auth
                  .requestMatchers("/actuator/health").permitAll()
                  .requestMatchers("/actuator/**").hasRole("ADMIN")
                  .anyRequest().authenticated()
              );
          return http.build();
      }
  }
  ```

</details>

<details>
  <summary>Health Endpoint</summary>
  <br/>

  The `/actuator/health` endpoint shows application health status.

  **Basic configuration:**

  ```properties
  # Show detailed health information
  management.endpoint.health.show-details=always
  
  # Show details only when authorized
  management.endpoint.health.show-details=when-authorized
  
  # Show components
  management.endpoint.health.show-components=always
  ```

  **Response example:**

  ```json
  {
    "status": "UP",
    "components": {
      "db": {
        "status": "UP",
        "details": {
          "database": "MySQL",
          "validationQuery": "isValid()"
        }
      },
      "diskSpace": {
        "status": "UP",
        "details": {
          "total": 499963174912,
          "free": 336889516032,
          "threshold": 10485760
        }
      },
      "ping": {
        "status": "UP"
      }
    }
  }
  ```

  **Health statuses:**
  + `UP` - Application is healthy
  + `DOWN` - Application is unhealthy
  + `OUT_OF_SERVICE` - Temporarily unavailable
  + `UNKNOWN` - Status cannot be determined

  **Custom health indicator:**

  ```java
  @Component
  public class CustomHealthIndicator implements HealthIndicator {
      
      @Override
      public Health health() {
          boolean isHealthy = checkExternalService();
          
          if (isHealthy) {
              return Health.up()
                  .withDetail("service", "available")
                  .withDetail("responseTime", "50ms")
                  .build();
          }
          
          return Health.down()
              .withDetail("service", "unavailable")
              .withDetail("error", "Connection timeout")
              .build();
      }
      
      private boolean checkExternalService() {
          // Check external service
          return true;
      }
  }
  ```

  **Grouped health indicators:**

  ```properties
  # Create custom health groups
  management.endpoint.health.group.liveness.include=livenessProbe
  management.endpoint.health.group.readiness.include=readinessProbe,db
  ```

  Access: `/actuator/health/liveness`, `/actuator/health/readiness`

  **Kubernetes liveness/readiness probes:**

  ```yaml
  apiVersion: v1
  kind: Pod
  spec:
    containers:
    - name: app
      livenessProbe:
        httpGet:
          path: /actuator/health/liveness
          port: 8080
        initialDelaySeconds: 30
        periodSeconds: 10
      readinessProbe:
        httpGet:
          path: /actuator/health/readiness
          port: 8080
        initialDelaySeconds: 10
        periodSeconds: 5
  ```

</details>

<details>
  <summary>Metrics Endpoint</summary>
  <br/>

  The `/actuator/metrics` endpoint provides application metrics.

  **List all metrics:**

  ```bash
  curl http://localhost:8080/actuator/metrics
  ```

  ```json
  {
    "names": [
      "jvm.memory.used",
      "jvm.memory.max",
      "jvm.gc.pause",
      "http.server.requests",
      "system.cpu.usage",
      "process.uptime"
    ]
  }
  ```

  **View specific metric:**

  ```bash
  curl http://localhost:8080/actuator/metrics/jvm.memory.used
  ```

  ```json
  {
    "name": "jvm.memory.used",
    "measurements": [
      {
        "statistic": "VALUE",
        "value": 157286400
      }
    ],
    "availableTags": [
      {
        "tag": "area",
        "values": ["heap", "nonheap"]
      }
    ]
  }
  ```

  **Common metrics:**
  + `jvm.memory.used` - JVM memory usage
  + `jvm.memory.max` - Maximum JVM memory
  + `jvm.gc.pause` - Garbage collection pauses
  + `http.server.requests` - HTTP request metrics
  + `system.cpu.usage` - CPU usage
  + `process.uptime` - Application uptime
  + `logback.events` - Log events count

  **Custom metrics:**

  ```java
  @Service
  public class OrderService {
      
      private final MeterRegistry meterRegistry;
      private final Counter orderCounter;
      
      public OrderService(MeterRegistry meterRegistry) {
          this.meterRegistry = meterRegistry;
          this.orderCounter = Counter.builder("orders.created")
              .description("Total orders created")
              .tag("type", "online")
              .register(meterRegistry);
      }
      
      public Order createOrder(Order order) {
          Order saved = orderRepository.save(order);
          orderCounter.increment();
          return saved;
      }
      
      @Timed(value = "order.processing.time", description = "Time to process order")
      public void processOrder(Long orderId) {
          // Process order
      }
  }
  ```

  **Timer example:**

  ```java
  @Service
  public class PaymentService {
      
      private final MeterRegistry meterRegistry;
      
      public PaymentService(MeterRegistry meterRegistry) {
          this.meterRegistry = meterRegistry;
      }
      
      public void processPayment(Payment payment) {
          Timer.Sample sample = Timer.start(meterRegistry);
          
          try {
              // Process payment
              Thread.sleep(100);
          } catch (Exception e) {
              // Handle error
          } finally {
              sample.stop(Timer.builder("payment.processing.time")
                  .tag("status", "success")
                  .register(meterRegistry));
          }
      }
  }
  ```

  **Gauge example (dynamic value):**

  ```java
  @Component
  public class CacheMetrics {
      
      private final Cache cache;
      
      public CacheMetrics(MeterRegistry meterRegistry, Cache cache) {
          this.cache = cache;
          
          Gauge.builder("cache.size", cache, Cache::size)
              .description("Current cache size")
              .register(meterRegistry);
      }
  }
  ```

</details>

<details>
  <summary>Info Endpoint</summary>
  <br/>

  The `/actuator/info` endpoint displays application information.

  **Configuration:**

  ```properties
  # application.properties
  info.app.name=My Application
  info.app.description=Spring Boot Application
  info.app.version=1.0.0
  info.app.encoding=@project.build.sourceEncoding@
  info.app.java.version=@java.version@
  ```

  **Response:**

  ```json
  {
    "app": {
      "name": "My Application",
      "description": "Spring Boot Application",
      "version": "1.0.0",
      "encoding": "UTF-8",
      "java": {
        "version": "17.0.1"
      }
    }
  }
  ```

  **Custom info contributor:**

  ```java
  @Component
  public class CustomInfoContributor implements InfoContributor {
      
      @Override
      public void contribute(Info.Builder builder) {
          Map<String, Object> details = new HashMap<>();
          details.put("activeUsers", 150);
          details.put("serverTime", LocalDateTime.now());
          details.put("environment", "production");
          
          builder.withDetail("custom", details);
      }
  }
  ```

  **Git information (automatic):**

  ```xml
  <plugin>
      <groupId>pl.project13.maven</groupId>
      <artifactId>git-commit-id-plugin</artifactId>
  </plugin>
  ```

  ```properties
  management.info.git.mode=full
  ```

  **Response with Git info:**

  ```json
  {
    "git": {
      "branch": "main",
      "commit": {
        "id": "abc123",
        "time": "2024-01-15T10:30:00Z"
      }
    }
  }
  ```

</details>

<details>
  <summary>Other Useful Endpoints</summary>
  <br/>

  **Environment endpoint (`/actuator/env`):**

  Shows all environment properties.

  ```properties
  management.endpoints.web.exposure.include=env
  ```

  **Loggers endpoint (`/actuator/loggers`):**

  View and modify logger levels at runtime.

  ```bash
  # View all loggers
  curl http://localhost:8080/actuator/loggers
  
  # View specific logger
  curl http://localhost:8080/actuator/loggers/com.example.service
  
  # Change log level at runtime
  curl -X POST http://localhost:8080/actuator/loggers/com.example.service \
    -H "Content-Type: application/json" \
    -d '{"configuredLevel": "DEBUG"}'
  ```

  **Beans endpoint (`/actuator/beans`):**

  Lists all Spring beans in the application.

  **Mappings endpoint (`/actuator/mappings`):**

  Shows all `@RequestMapping` paths.

  **Thread dump (`/actuator/threaddump`):**

  Generates thread dump for debugging.

  **Heap dump (`/actuator/heapdump`):**

  Downloads heap dump file for memory analysis.

  **Shutdown endpoint (`/actuator/shutdown`):**

  Gracefully shuts down the application (disabled by default).

  ```properties
  management.endpoint.shutdown.enabled=true
  ```

  ```bash
  curl -X POST http://localhost:8080/actuator/shutdown
  ```

</details>

<details>
  <summary>Prometheus Integration</summary>
  <br/>

  Expose metrics in Prometheus format for monitoring.

  **Add dependency:**

  ```xml
  <dependency>
      <groupId>io.micrometer</groupId>
      <artifactId>micrometer-registry-prometheus</artifactId>
  </dependency>
  ```

  **Configuration:**

  ```properties
  management.endpoints.web.exposure.include=prometheus
  management.metrics.export.prometheus.enabled=true
  ```

  **Access metrics:**

  ```bash
  curl http://localhost:8080/actuator/prometheus
  ```

  **Prometheus configuration:**

  ```yaml
  # prometheus.yml
  scrape_configs:
    - job_name: 'spring-boot-app'
      metrics_path: '/actuator/prometheus'
      static_configs:
        - targets: ['localhost:8080']
  ```

  **Grafana dashboard:**

  Import Spring Boot dashboard (ID: 4701) in Grafana to visualize metrics.

</details>

<details>
  <summary>Best Practices</summary>
  <br/>

  ```properties
  # ✅ DO: Secure actuator endpoints
  management.endpoints.web.exposure.include=health,info,metrics
  
  # ✅ DO: Use different port for actuator in production
  management.server.port=9090
  
  # ✅ DO: Show health details only when authorized
  management.endpoint.health.show-details=when-authorized
  
  # ❌ DON'T: Expose all endpoints in production
  management.endpoints.web.exposure.include=*  # Dangerous!
  
  # ✅ DO: Create custom health indicators
  @Component
  public class DatabaseHealthIndicator implements HealthIndicator {
      @Override
      public Health health() {
          // Check database connection
      }
  }
  
  # ✅ DO: Add custom metrics for business logic
  Counter.builder("orders.created")
      .tag("type", "online")
      .register(meterRegistry);
  
  # ✅ DO: Use liveness/readiness probes in Kubernetes
  management.endpoint.health.group.liveness.include=livenessProbe
  management.endpoint.health.group.readiness.include=readinessProbe,db
  
  # ✅ DO: Integrate with monitoring tools
  # Prometheus, Grafana, New Relic, Datadog
  
  # ❌ DON'T: Expose sensitive information in /info
  info.database.password=secret  # Bad!
  
  # ✅ DO: Enable only needed endpoints
  management.endpoints.web.exposure.include=health,info,metrics,prometheus
  ```

  **Key Points**
  <br/>

  + Actuator provides **production-ready** monitoring features
  + Default endpoints: `/actuator/health`, `/actuator/info`
  + Expose endpoints with `management.endpoints.web.exposure.include`
  + **Secure** actuator endpoints (use Spring Security)
  + `/health` shows application health status (UP, DOWN, OUT_OF_SERVICE)
  + Create **custom health indicators** for external dependencies
  + `/metrics` provides application metrics (JVM, HTTP, custom)
  + Use **@Timed**, **Counter**, **Gauge** for custom metrics
  + `/info` displays application information
  + Change log levels at runtime with `/loggers`
  + Integrate with **Prometheus** and **Grafana** for monitoring
  + Use **liveness/readiness** probes for Kubernetes
  + Run actuator on **separate port** in production
  + Never expose **all endpoints** in production
  + Use `/shutdown` carefully (disabled by default)

</details>

## Spring Boot DevTools

<details>
  <summary>What is Spring Boot DevTools?</summary>
  <br/>

  DevTools provides development-time features to improve developer productivity.

  **Add dependency:**

  ```xml
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-devtools</artifactId>
      <optional>true</optional>
  </dependency>
  ```

  **Note:** `<optional>true</optional>` prevents DevTools from being included in production builds.

  **Features:**
  + **Automatic restart** - Restarts application when classpath changes
  + **Live reload** - Refreshes browser automatically
  + **Property defaults** - Development-friendly defaults
  + **Remote debugging** - Debug applications running remotely
  + **H2 console** - Automatic H2 database console

  **Why use DevTools?**
  + Faster development cycle
  + No manual restart needed
  + Instant feedback on code changes
  + Better development experience

  **Automatic exclusion in production:**

  DevTools is automatically disabled when running as a packaged application (`java -jar`).

</details>

<details>
  <summary>Automatic Restart</summary>
  <br/>

  DevTools monitors classpath changes and automatically restarts the application.

  **How it works:**

  1. You modify a Java file
  2. IDE compiles the file (or you run `mvn compile`)
  3. DevTools detects the change
  4. Application restarts automatically

  **Two classloaders:**
  + **Base classloader** - Loads third-party JARs (doesn't change)
  + **Restart classloader** - Loads your application code (reloads on change)

  This makes restarts much faster than full application restart.

  **Triggering restart:**

  **IntelliJ IDEA:**
  + Build → Build Project (`Ctrl+F9` / `Cmd+F9`)
  + Or enable "Build project automatically" in Settings

  **Eclipse:**
  + Automatic (saves trigger compilation)

  **VS Code:**
  + Save file (if auto-build enabled)

  **Maven:**
  ```bash
  mvn compile
  ```

  **What triggers restart:**
  + Java source files (`.java`)
  + Resources files (`.properties`, `.yml`)
  + Static resources (if not excluded)

  **Excluding resources from restart:**

  ```properties
  # application.properties
  spring.devtools.restart.exclude=static/**,public/**,templates/**
  ```

  **Additional paths to watch:**

  ```properties
  spring.devtools.restart.additional-paths=../other-project/src/main/java
  ```

  **Disable restart:**

  ```properties
  spring.devtools.restart.enabled=false
  ```

  **Programmatic restart:**

  ```java
  @RestController
  public class RestartController {
      
      @Autowired
      private RestartEndpoint restartEndpoint;
      
      @PostMapping("/restart")
      public void restart() {
          restartEndpoint.restart();
      }
  }
  ```

</details>

<details>
  <summary>Live Reload</summary>
  <br/>

  Live Reload automatically refreshes the browser when resources change.

  **How it works:**

  1. DevTools starts embedded LiveReload server
  2. Browser connects to LiveReload server
  3. When files change, server notifies browser
  4. Browser refreshes automatically

  **Enable Live Reload:**

  ```properties
  # application.properties (enabled by default)
  spring.devtools.livereload.enabled=true
  spring.devtools.livereload.port=35729
  ```

  **Browser setup:**

  **Option 1: Browser extension (recommended)**
  + Install LiveReload extension for Chrome/Firefox
  + Click extension icon to enable

  **Option 2: Manual script (for testing)**
  ```html
  <script src="http://localhost:35729/livereload.js"></script>
  ```

  **What triggers live reload:**
  + HTML files
  + CSS files
  + JavaScript files
  + Images
  + Templates (Thymeleaf, Freemarker)

  **Excluding paths:**

  ```properties
  spring.devtools.restart.exclude=static/**,public/**
  ```

  **Disable live reload:**

  ```properties
  spring.devtools.livereload.enabled=false
  ```

  **Use case:**

  Perfect for frontend development - change HTML/CSS and see results immediately without manual refresh.

</details>

<details>
  <summary>Property Defaults</summary>
  <br/>

  DevTools applies development-friendly property defaults.

  **Automatic defaults:**

  ```properties
  # Caching disabled for templates
  spring.thymeleaf.cache=false
  spring.freemarker.cache=false
  spring.groovy.template.cache=false
  spring.mustache.cache=false
  
  # Web resources caching disabled
  spring.web.resources.cache.period=0
  spring.web.resources.chain.cache=false
  
  # H2 console enabled
  spring.h2.console.enabled=true
  ```

  **Why these defaults?**
  + See template changes immediately
  + No need to clear cache during development
  + H2 console available for database inspection

  **Override defaults:**

  ```properties
  # application.properties
  spring.thymeleaf.cache=true  # Override DevTools default
  ```

  **Custom property defaults:**

  Create `META-INF/spring-devtools.properties`:

  ```properties
  # Custom defaults for your team
  logging.level.com.example=DEBUG
  server.port=8081
  ```

</details>

<details>
  <summary>Global Settings</summary>
  <br/>

  Configure DevTools globally for all projects on your machine.

  **Location:**

  ```
  ~/.spring-boot-devtools.properties  (Linux/Mac)
  C:\Users\{user}\.spring-boot-devtools.properties  (Windows)
  ```

  **Example global settings:**

  ```properties
  # ~/.spring-boot-devtools.properties
  spring.devtools.restart.trigger-file=.reloadtrigger
  spring.devtools.livereload.enabled=true
  spring.devtools.restart.poll-interval=2s
  spring.devtools.restart.quiet-period=1s
  ```

  **Trigger file (recommended for large projects):**

  ```properties
  spring.devtools.restart.trigger-file=.reloadtrigger
  ```

  With trigger file:
  1. Make code changes (no restart)
  2. Touch trigger file when ready
  3. Application restarts

  ```bash
  # Create/update trigger file
  touch .reloadtrigger
  ```

  **Why use trigger file?**
  + Control when restart happens
  + Avoid multiple restarts during bulk changes
  + Better for large projects

</details>

<details>
  <summary>Remote Applications</summary>
  <br/>

  DevTools can connect to remote applications for debugging and hot-swapping.

  **Enable remote support:**

  ```properties
  # application.properties
  spring.devtools.remote.secret=mysecret
  ```

  **Run remote application:**

  ```bash
  java -jar myapp.jar --spring.devtools.remote.secret=mysecret
  ```

  **Connect from IDE:**

  Run `RemoteSpringApplication` with remote URL:

  ```java
  public class RemoteApp {
      public static void main(String[] args) {
          RemoteSpringApplication.run(
              "http://remote-server:8080",
              args
          );
      }
  }
  ```

  **Features:**
  + Remote restart
  + Remote live reload
  + Remote debugging

  **Security warning:**

  Never enable remote DevTools in production! Use only in development/staging environments.

  ```properties
  # Disable in production
  spring.devtools.remote.secret=
  ```

</details>

<details>
  <summary>H2 Console</summary>
  <br/>

  DevTools automatically enables H2 database console.

  **Access H2 console:**

  ```
  http://localhost:8080/h2-console
  ```

  **Configuration:**

  ```properties
  # application.properties
  spring.h2.console.enabled=true
  spring.h2.console.path=/h2-console
  
  # H2 database settings
  spring.datasource.url=jdbc:h2:mem:testdb
  spring.datasource.driverClassName=org.h2.Driver
  spring.datasource.username=sa
  spring.datasource.password=
  ```

  **Login details:**
  + JDBC URL: `jdbc:h2:mem:testdb`
  + User Name: `sa`
  + Password: (leave empty)

  **Why use H2 console?**
  + Inspect database tables
  + Run SQL queries
  + Debug data issues
  + Quick database testing

</details>

<details>
  <summary>IDE Configuration</summary>
  <br/>

  **IntelliJ IDEA:**

  **Option 1: Manual build**
  + Make changes
  + Press `Ctrl+F9` (Windows) or `Cmd+F9` (Mac)
  + Application restarts

  **Option 2: Automatic build**
  1. Settings → Build, Execution, Deployment → Compiler
  2. Enable "Build project automatically"
  3. Press `Ctrl+Shift+A` → Search "Registry"
  4. Enable `compiler.automake.allow.when.app.running`

  **Eclipse:**

  Works automatically - saving triggers compilation and restart.

  **VS Code:**

  1. Install "Spring Boot Extension Pack"
  2. Enable auto-save or save manually
  3. Application restarts on save

  **Maven:**

  ```bash
  # Terminal 1: Run application
  mvn spring-boot:run
  
  # Terminal 2: Compile on changes
  mvn compile
  ```

</details>

<details>
  <summary>Performance Tuning</summary>
  <br/>

  **Adjust restart timing:**

  ```properties
  # Wait time before restart (default: 400ms)
  spring.devtools.restart.quiet-period=1s
  
  # Polling interval for file changes (default: 1s)
  spring.devtools.restart.poll-interval=2s
  ```

  **Exclude large directories:**

  ```properties
  spring.devtools.restart.exclude=static/**,public/**,node_modules/**
  ```

  **Use trigger file for large projects:**

  ```properties
  spring.devtools.restart.trigger-file=.reloadtrigger
  ```

  **Disable features you don't need:**

  ```properties
  # Disable live reload if not using
  spring.devtools.livereload.enabled=false
  
  # Disable restart if too slow
  spring.devtools.restart.enabled=false
  ```

</details>

<details>
  <summary>Best Practices</summary>
  <br/>

  ```xml
  <!-- ✅ DO: Mark as optional -->
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-devtools</artifactId>
      <optional>true</optional>
  </dependency>
  
  <!-- ✅ DO: Exclude from production builds -->
  <plugin>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-maven-plugin</artifactId>
      <configuration>
          <excludeDevtools>true</excludeDevtools>
      </configuration>
  </plugin>
  ```

  ```properties
  # ✅ DO: Use trigger file for large projects
  spring.devtools.restart.trigger-file=.reloadtrigger
  
  # ✅ DO: Exclude static resources from restart
  spring.devtools.restart.exclude=static/**,public/**,templates/**
  
  # ❌ DON'T: Enable remote DevTools in production
  spring.devtools.remote.secret=  # Keep empty in prod
  
  # ✅ DO: Disable DevTools in production
  # (Automatic when running as packaged JAR)
  
  # ✅ DO: Configure IDE for automatic build
  # IntelliJ: Enable "Build project automatically"
  # Eclipse: Works automatically
  
  # ✅ DO: Use LiveReload for frontend development
  spring.devtools.livereload.enabled=true
  
  # ✅ DO: Adjust timing for your project size
  spring.devtools.restart.quiet-period=1s
  spring.devtools.restart.poll-interval=2s
  ```

  **Key Points**
  <br/>

  + DevTools improves **development productivity**
  + **Automatic restart** when code changes (faster than full restart)
  + **Live reload** refreshes browser automatically
  + Uses **two classloaders** for fast restart
  + Trigger restart: Build project in IDE or `mvn compile`
  + **Exclude** static resources from restart for better performance
  + **Live reload** requires browser extension or script
  + Provides **development-friendly defaults** (caching disabled)
  + **H2 console** enabled automatically
  + Use **trigger file** for large projects
  + Configure **global settings** in `~/.spring-boot-devtools.properties`
  + **Remote DevTools** for debugging remote applications (dev/staging only)
  + Mark dependency as **optional** to exclude from production
  + Automatically **disabled** in packaged JAR
  + Never enable **remote DevTools** in production
  + Configure IDE for **automatic build** for best experience

</details>
