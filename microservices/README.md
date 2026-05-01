## Core Concepts

<details>
  <summary>Microservices vs Monolith</summary>
  <br/>

  **Monolithic Architecture:**

  Single deployable unit containing all application functionality.

  ```
  ┌─────────────────────────────────┐
  │      Monolithic Application     │
  │                                 │
  │  ┌──────────┐  ┌──────────┐   │
  │  │   User   │  │  Order   │   │
  │  │ Service  │  │ Service  │   │
  │  └──────────┘  └──────────┘   │
  │                                 │
  │  ┌──────────┐  ┌──────────┐   │
  │  │ Product  │  │ Payment  │   │
  │  │ Service  │  │ Service  │   │
  │  └──────────┘  └──────────┘   │
  │                                 │
  │      Single Database            │
  └─────────────────────────────────┘
  ```

  **Microservices Architecture:**

  Multiple independent services, each with its own database.

  ```
  ┌──────────┐    ┌──────────┐    ┌──────────┐
  │   User   │    │  Order   │    │ Product  │
  │ Service  │    │ Service  │    │ Service  │
  │    DB    │    │    DB    │    │    DB    │
  └──────────┘    └──────────┘    └──────────┘
       │               │                │
       └───────────────┼────────────────┘
                       │
                  ┌────────┐
                  │   API  │
                  │ Gateway│
                  └────────┘
  ```

  **Comparison:**

  | Aspect | Monolith | Microservices |
  |--------|----------|---------------|
  | **Deployment** | Single unit | Independent services |
  | **Scaling** | Scale entire app | Scale individual services |
  | **Technology** | Single stack | Polyglot (multiple stacks) |
  | **Database** | Shared database | Database per service |
  | **Development** | Simple initially | Complex coordination |
  | **Testing** | Easier (single app) | Complex (distributed) |
  | **Failure** | Entire app fails | Isolated failures |
  | **Team** | Single team | Multiple teams |
  | **Deployment Speed** | Slower (entire app) | Faster (individual services) |

  **When to use Monolith:**
  + Small team (< 10 developers)
  + Simple application
  + Tight deadlines
  + Limited resources
  + Unclear requirements
  + Startup/MVP phase

  **When to use Microservices:**
  + Large team (> 10 developers)
  + Complex application
  + Need independent scaling
  + Different technology requirements
  + Frequent deployments
  + Clear bounded contexts

  **Example - E-commerce:**

  **Monolith:**
  ```java
  @SpringBootApplication
  public class EcommerceApplication {
      // All services in one application
      @Autowired UserService userService;
      @Autowired OrderService orderService;
      @Autowired ProductService productService;
      @Autowired PaymentService paymentService;
      
      // Single database
      @Autowired DataSource dataSource;
  }
  ```

  **Microservices:**
  ```java
  // User Service (Port 8081)
  @SpringBootApplication
  public class UserServiceApplication {
      @Autowired UserRepository userRepository;
  }
  
  // Order Service (Port 8082)
  @SpringBootApplication
  public class OrderServiceApplication {
      @Autowired OrderRepository orderRepository;
      @Autowired RestTemplate restTemplate; // Call other services
  }
  
  // Product Service (Port 8083)
  @SpringBootApplication
  public class ProductServiceApplication {
      @Autowired ProductRepository productRepository;
  }
  ```

</details>

<details>
  <summary>Service Decomposition</summary>
  <br/>

  **Service decomposition** is breaking down a monolith into microservices.

  **Decomposition strategies:**

  **1. Decompose by Business Capability:**

  ```
  E-commerce Application
  │
  ├── User Management
  │   ├── Registration
  │   ├── Authentication
  │   └── Profile Management
  │
  ├── Product Catalog
  │   ├── Product Search
  │   ├── Product Details
  │   └── Inventory Management
  │
  ├── Order Management
  │   ├── Cart
  │   ├── Checkout
  │   └── Order Tracking
  │
  └── Payment Processing
      ├── Payment Gateway
      └── Refunds
  ```

  **2. Decompose by Subdomain (Domain-Driven Design):**

  ```
  ┌─────────────────────────────────────┐
  │         Bounded Contexts            │
  ├─────────────────────────────────────┤
  │                                     │
  │  ┌──────────────┐  ┌─────────────┐│
  │  │   Customer   │  │   Product   ││
  │  │   Context    │  │   Context   ││
  │  │              │  │             ││
  │  │ - User       │  │ - Product   ││
  │  │ - Profile    │  │ - Inventory ││
  │  │ - Address    │  │ - Category  ││
  │  └──────────────┘  └─────────────┘│
  │                                     │
  │  ┌──────────────┐  ┌─────────────┐│
  │  │    Order     │  │   Payment   ││
  │  │   Context    │  │   Context   ││
  │  │              │  │             ││
  │  │ - Order      │  │ - Payment   ││
  │  │ - OrderItem  │  │ - Invoice   ││
  │  │ - Shipping   │  │ - Refund    ││
  │  └──────────────┘  └─────────────┘│
  └─────────────────────────────────────┘
  ```

  **3. Decompose by Use Case:**

  ```
  User Stories → Services
  
  "As a customer, I want to browse products"
  → Product Catalog Service
  
  "As a customer, I want to add items to cart"
  → Shopping Cart Service
  
  "As a customer, I want to checkout"
  → Order Service + Payment Service
  
  "As a customer, I want to track my order"
  → Order Tracking Service
  ```

  **Bounded Context Example:**

  ```java
  // Customer Context
  @Entity
  public class Customer {
      private Long id;
      private String name;
      private String email;
      private List<Address> addresses;
  }
  
  // Order Context (different Customer representation)
  @Entity
  public class Order {
      private Long id;
      private Long customerId;  // Reference, not embedded
      private String customerName;  // Denormalized
      private List<OrderItem> items;
  }
  
  // Product Context
  @Entity
  public class Product {
      private Long id;
      private String name;
      private BigDecimal price;
      private Integer stock;
  }
  ```

  **Service size guidelines:**

  ```
  Too Small:
  - Single function/method
  - Too many inter-service calls
  - High network overhead

  Too Large:
  - Multiple business capabilities
  - Large team needed
  - Difficult to deploy independently

  Right Size:
  - Single business capability
  - 2-5 developers can maintain
  - Can be deployed independently
  - Clear boundaries
  ```

</details>

<details>
  <summary>Communication Patterns</summary>
  <br/>

  **Synchronous Communication:**

  **REST API:**

  ```java
  // Order Service calls Product Service
  @Service
  public class OrderService {
      @Autowired
      private RestTemplate restTemplate;
      
      public Order createOrder(OrderRequest request) {
          // Call Product Service to check stock
          String url = "http://product-service/api/products/" + request.getProductId();
          Product product = restTemplate.getForObject(url, Product.class);
          
          if (product.getStock() < request.getQuantity()) {
              throw new InsufficientStockException();
          }
          
          // Create order
          Order order = new Order();
          order.setProductId(request.getProductId());
          order.setQuantity(request.getQuantity());
          return orderRepository.save(order);
      }
  }
  ```

  **gRPC:**

  ```protobuf
  // product.proto
  syntax = "proto3";
  
  service ProductService {
      rpc GetProduct(ProductRequest) returns (ProductResponse);
      rpc CheckStock(StockRequest) returns (StockResponse);
  }
  
  message ProductRequest {
      int64 product_id = 1;
  }
  
  message ProductResponse {
      int64 id = 1;
      string name = 2;
      double price = 3;
      int32 stock = 4;
  }
  ```

  ```java
  // Java client
  @Service
  public class OrderService {
      @Autowired
      private ProductServiceGrpc.ProductServiceBlockingStub productStub;
      
      public Order createOrder(OrderRequest request) {
          StockRequest stockRequest = StockRequest.newBuilder()
              .setProductId(request.getProductId())
              .setQuantity(request.getQuantity())
              .build();
          
          StockResponse response = productStub.checkStock(stockRequest);
          
          if (!response.getAvailable()) {
              throw new InsufficientStockException();
          }
          
          return orderRepository.save(order);
      }
  }
  ```

  **Asynchronous Communication:**

  **Message Queue (RabbitMQ):**

  ```java
  // Order Service publishes event
  @Service
  public class OrderService {
      @Autowired
      private RabbitTemplate rabbitTemplate;
      
      public Order createOrder(OrderRequest request) {
          Order order = orderRepository.save(new Order(request));
          
          // Publish event
          OrderCreatedEvent event = new OrderCreatedEvent(
              order.getId(),
              order.getCustomerId(),
              order.getTotal()
          );
          
          rabbitTemplate.convertAndSend(
              "order.exchange",
              "order.created",
              event
          );
          
          return order;
      }
  }
  
  // Inventory Service consumes event
  @Service
  public class InventoryService {
      @RabbitListener(queues = "inventory.queue")
      public void handleOrderCreated(OrderCreatedEvent event) {
          // Update inventory
          inventoryRepository.decreaseStock(
              event.getProductId(),
              event.getQuantity()
          );
      }
  }
  ```

  **Event Streaming (Kafka):**

  ```java
  // Order Service produces event
  @Service
  public class OrderService {
      @Autowired
      private KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;
      
      public Order createOrder(OrderRequest request) {
          Order order = orderRepository.save(new Order(request));
          
          // Produce event
          OrderCreatedEvent event = new OrderCreatedEvent(order);
          kafkaTemplate.send("order-events", event);
          
          return order;
      }
  }
  
  // Multiple services can consume
  @Service
  public class NotificationService {
      @KafkaListener(topics = "order-events", groupId = "notification-group")
      public void handleOrderCreated(OrderCreatedEvent event) {
          // Send notification
          emailService.sendOrderConfirmation(event);
      }
  }
  
  @Service
  public class AnalyticsService {
      @KafkaListener(topics = "order-events", groupId = "analytics-group")
      public void handleOrderCreated(OrderCreatedEvent event) {
          // Update analytics
          analyticsRepository.recordOrder(event);
      }
  }
  ```

  **Comparison:**

  | Pattern | Use Case | Pros | Cons |
  |---------|----------|------|------|
  | **REST** | CRUD operations | Simple, widely supported | Tight coupling, blocking |
  | **gRPC** | High performance | Fast, type-safe | Complex setup |
  | **Message Queue** | Async processing | Decoupled, reliable | Eventual consistency |
  | **Event Streaming** | Event sourcing | Scalable, replay events | Complex, eventual consistency |

</details>

<details>
  <summary>API Gateway</summary>
  <br/>

  **API Gateway** is a single entry point for all client requests.

  **Responsibilities:**
  + Routing requests to services
  + Authentication and authorization
  + Rate limiting
  + Load balancing
  + Request/response transformation
  + Caching
  + Logging and monitoring

  **Architecture:**

  ```
  ┌─────────┐
  │ Client  │
  └────┬────┘
       │
       ▼
  ┌────────────┐
  │    API     │
  │  Gateway   │
  └─────┬──────┘
        │
        ├──────────┬──────────┬──────────┐
        ▼          ▼          ▼          ▼
  ┌─────────┐ ┌─────────┐ ┌─────────┐ ┌─────────┐
  │  User   │ │ Order   │ │Product  │ │Payment  │
  │ Service │ │ Service │ │Service  │ │Service  │
  └─────────┘ └─────────┘ └─────────┘ └─────────┘
  ```

  **Spring Cloud Gateway:**

  ```java
  @Configuration
  public class GatewayConfig {
      
      @Bean
      public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
          return builder.routes()
              // User Service
              .route("user-service", r -> r
                  .path("/api/users/**")
                  .filters(f -> f
                      .stripPrefix(1)
                      .addRequestHeader("X-Gateway", "true")
                  )
                  .uri("lb://user-service")
              )
              
              // Order Service
              .route("order-service", r -> r
                  .path("/api/orders/**")
                  .filters(f -> f
                      .stripPrefix(1)
                      .circuitBreaker(c -> c
                          .setName("orderCircuitBreaker")
                          .setFallbackUri("forward:/fallback/orders")
                      )
                  )
                  .uri("lb://order-service")
              )
              
              // Product Service
              .route("product-service", r -> r
                  .path("/api/products/**")
                  .filters(f -> f
                      .stripPrefix(1)
                      .retry(config -> config
                          .setRetries(3)
                          .setBackoff(Duration.ofMillis(100), Duration.ofMillis(1000), 2, true)
                      )
                  )
                  .uri("lb://product-service")
              )
              .build();
      }
  }
  ```

  **Authentication Filter:**

  ```java
  @Component
  public class AuthenticationFilter implements GlobalFilter, Ordered {
      
      @Autowired
      private JwtUtil jwtUtil;
      
      @Override
      public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
          ServerHttpRequest request = exchange.getRequest();
          
          // Skip authentication for public endpoints
          if (isPublicEndpoint(request.getPath().toString())) {
              return chain.filter(exchange);
          }
          
          // Extract token
          String token = extractToken(request);
          if (token == null || !jwtUtil.validateToken(token)) {
              exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
              return exchange.getResponse().setComplete();
          }
          
          // Add user info to headers
          String userId = jwtUtil.getUserId(token);
          ServerHttpRequest modifiedRequest = request.mutate()
              .header("X-User-Id", userId)
              .build();
          
          return chain.filter(exchange.mutate().request(modifiedRequest).build());
      }
      
      @Override
      public int getOrder() {
          return -100; // High priority
      }
  }
  ```

  **Rate Limiting:**

  ```java
  @Configuration
  public class RateLimitConfig {
      
      @Bean
      public KeyResolver userKeyResolver() {
          return exchange -> {
              String userId = exchange.getRequest()
                  .getHeaders()
                  .getFirst("X-User-Id");
              return Mono.just(userId != null ? userId : "anonymous");
          };
      }
      
      @Bean
      public RouteLocator rateLimitedRoutes(RouteLocatorBuilder builder) {
          return builder.routes()
              .route("rate-limited", r -> r
                  .path("/api/**")
                  .filters(f -> f
                      .requestRateLimiter(config -> config
                          .setRateLimiter(redisRateLimiter())
                          .setKeyResolver(userKeyResolver())
                      )
                  )
                  .uri("lb://backend-service")
              )
              .build();
      }
      
      @Bean
      public RedisRateLimiter redisRateLimiter() {
          return new RedisRateLimiter(10, 20); // 10 requests per second, burst 20
      }
  }
  ```

  **Response Caching:**

  ```java
  @Component
  public class CacheFilter implements GlobalFilter, Ordered {
      
      @Autowired
      private RedisTemplate<String, String> redisTemplate;
      
      @Override
      public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
          ServerHttpRequest request = exchange.getRequest();
          
          // Only cache GET requests
          if (!request.getMethod().equals(HttpMethod.GET)) {
              return chain.filter(exchange);
          }
          
          String cacheKey = "cache:" + request.getPath();
          String cachedResponse = redisTemplate.opsForValue().get(cacheKey);
          
          if (cachedResponse != null) {
              // Return cached response
              exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
              DataBuffer buffer = exchange.getResponse()
                  .bufferFactory()
                  .wrap(cachedResponse.getBytes());
              return exchange.getResponse().writeWith(Mono.just(buffer));
          }
          
          // Continue and cache response
          return chain.filter(exchange);
      }
      
      @Override
      public int getOrder() {
          return 0;
      }
  }
  ```

</details>

<details>
  <summary>Service Discovery</summary>
  <br/>

  **Service discovery** allows services to find and communicate with each other dynamically.

  **Why needed:**
  + Services can be deployed on different hosts/ports
  + Services can scale up/down dynamically
  + Services can fail and restart
  + No hardcoded URLs

  **Service Registry Pattern:**

  ```
  ┌─────────────────────────────────────┐
  │      Service Registry (Eureka)      │
  │                                     │
  │  User Service:    localhost:8081   │
  │  Order Service:   localhost:8082   │
  │  Product Service: localhost:8083   │
  └─────────────────────────────────────┘
           ▲                    │
           │ Register           │ Discover
           │                    ▼
  ┌─────────────┐        ┌─────────────┐
  │   Service   │        │   Service   │
  │  Instance   │───────▶│   Client    │
  └─────────────┘  Call  └─────────────┘
  ```

  **Eureka Server:**

  ```java
  @SpringBootApplication
  @EnableEurekaServer
  public class EurekaServerApplication {
      public static void main(String[] args) {
          SpringApplication.run(EurekaServerApplication.class, args);
      }
  }
  ```

  ```yaml
  # application.yml
  server:
    port: 8761
  
  eureka:
    client:
      register-with-eureka: false
      fetch-registry: false
    server:
      enable-self-preservation: false
  ```

  **Eureka Client (Service Registration):**

  ```java
  @SpringBootApplication
  @EnableDiscoveryClient
  public class OrderServiceApplication {
      public static void main(String[] args) {
          SpringApplication.run(OrderServiceApplication.class, args);
      }
  }
  ```

  ```yaml
  # application.yml
  spring:
    application:
      name: order-service
  
  server:
    port: 8082
  
  eureka:
    client:
      service-url:
        defaultZone: http://localhost:8761/eureka/
    instance:
      prefer-ip-address: true
      lease-renewal-interval-in-seconds: 30
      lease-expiration-duration-in-seconds: 90
  ```

  **Service Discovery with Load Balancing:**

  ```java
  @Configuration
  public class RestTemplateConfig {
      
      @Bean
      @LoadBalanced  // Enable client-side load balancing
      public RestTemplate restTemplate() {
          return new RestTemplate();
      }
  }
  
  @Service
  public class OrderService {
      
      @Autowired
      private RestTemplate restTemplate;
      
      public Order createOrder(OrderRequest request) {
          // Use service name instead of URL
          String url = "http://product-service/api/products/" + request.getProductId();
          Product product = restTemplate.getForObject(url, Product.class);
          
          // RestTemplate automatically:
          // 1. Discovers product-service instances from Eureka
          // 2. Load balances between instances
          // 3. Handles failures
          
          return orderRepository.save(new Order(product));
      }
  }
  ```

  **Feign Client (Declarative REST Client):**

  ```java
  @FeignClient(name = "product-service")
  public interface ProductClient {
      
      @GetMapping("/api/products/{id}")
      Product getProduct(@PathVariable Long id);
      
      @PostMapping("/api/products")
      Product createProduct(@RequestBody ProductRequest request);
      
      @PutMapping("/api/products/{id}/stock")
      void updateStock(@PathVariable Long id, @RequestParam Integer quantity);
  }
  
  @Service
  public class OrderService {
      
      @Autowired
      private ProductClient productClient;
      
      public Order createOrder(OrderRequest request) {
          // Simple method call, Feign handles:
          // - Service discovery
          // - Load balancing
          // - HTTP request/response
          Product product = productClient.getProduct(request.getProductId());
          
          return orderRepository.save(new Order(product));
      }
  }
  ```

  **Kubernetes Service Discovery:**

  ```yaml
  # product-service.yaml
  apiVersion: v1
  kind: Service
  metadata:
    name: product-service
  spec:
    selector:
      app: product
    ports:
      - port: 80
        targetPort: 8080
    type: ClusterIP
  
  ---
  apiVersion: apps/v1
  kind: Deployment
  metadata:
    name: product-deployment
  spec:
    replicas: 3
    selector:
      matchLabels:
        app: product
    template:
      metadata:
        labels:
          app: product
      spec:
        containers:
        - name: product
          image: product-service:1.0
          ports:
          - containerPort: 8080
  ```

  ```java
  // In Kubernetes, use service name as hostname
  @Service
  public class OrderService {
      
      @Autowired
      private RestTemplate restTemplate;
      
      public Order createOrder(OrderRequest request) {
          // Kubernetes DNS resolves service name
          String url = "http://product-service/api/products/" + request.getProductId();
          Product product = restTemplate.getForObject(url, Product.class);
          
          return orderRepository.save(new Order(product));
      }
  }
  ```

</details>

<details>
  <summary>Best Practices</summary>
  <br/>

  ```java
  // ✅ DO: Design services around business capabilities
  // User Service - handles user management
  // Order Service - handles order processing
  // Product Service - handles product catalog
  
  // ✅ DO: Use database per service
  @Configuration
  public class OrderServiceConfig {
      @Bean
      public DataSource orderDataSource() {
          return DataSourceBuilder.create()
              .url("jdbc:postgresql://localhost:5432/order_db")
              .build();
      }
  }
  
  // ✅ DO: Use API Gateway for client requests
  // Clients → API Gateway → Services
  // Not: Clients → Services directly
  
  // ✅ DO: Implement service discovery
  @EnableDiscoveryClient
  public class OrderServiceApplication { }
  
  // ✅ DO: Use asynchronous communication when possible
  @Service
  public class OrderService {
      @Autowired
      private KafkaTemplate<String, OrderEvent> kafkaTemplate;
      
      public Order createOrder(OrderRequest request) {
          Order order = orderRepository.save(new Order(request));
          kafkaTemplate.send("order-events", new OrderCreatedEvent(order));
          return order;
      }
  }
  
  // ✅ DO: Implement circuit breakers
  @CircuitBreaker(name = "productService", fallbackMethod = "getProductFallback")
  public Product getProduct(Long id) {
      return productClient.getProduct(id);
  }
  
  // ✅ DO: Use correlation IDs for tracing
  @Component
  public class CorrelationIdFilter implements Filter {
      @Override
      public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
          String correlationId = UUID.randomUUID().toString();
          MDC.put("correlationId", correlationId);
          chain.doFilter(request, response);
          MDC.clear();
      }
  }
  
  // ✅ DO: Implement health checks
  @RestController
  public class HealthController {
      @GetMapping("/health")
      public ResponseEntity<String> health() {
          return ResponseEntity.ok("UP");
      }
  }
  
  // ❌ DON'T: Share databases between services
  // Each service should have its own database
  
  // ❌ DON'T: Make synchronous calls in a chain
  // Service A → Service B → Service C → Service D (slow!)
  // Use async messaging instead
  
  // ❌ DON'T: Create too many small services
  // Balance between too many and too few services
  
  // ❌ DON'T: Ignore data consistency
  // Use Saga pattern or event sourcing for distributed transactions
  
  // ✅ DO: Version your APIs
  @GetMapping("/api/v1/products")
  public List<Product> getProductsV1() { }
  
  @GetMapping("/api/v2/products")
  public List<ProductV2> getProductsV2() { }
  
  // ✅ DO: Implement proper logging
  @Slf4j
  @Service
  public class OrderService {
      public Order createOrder(OrderRequest request) {
          log.info("Creating order for customer: {}", request.getCustomerId());
          Order order = orderRepository.save(new Order(request));
          log.info("Order created: {}", order.getId());
          return order;
      }
  }
  ```

  **Summary:**
  + **Microservices** are independent, deployable services
  + **Decompose** by business capability or domain
  + Use **API Gateway** as single entry point
  + Implement **service discovery** for dynamic routing
  + Use **async communication** when possible
  + Each service has its **own database**
  + Implement **circuit breakers** for resilience
  + Use **correlation IDs** for distributed tracing
  + **Version** your APIs
  + Implement proper **logging and monitoring**

</details>

## Configuration Management

<details>
  <summary>Spring Cloud Config</summary>
  <br/>

  **Spring Cloud Config** provides centralized configuration management for distributed systems.

  **Architecture:**

  ```
  ┌─────────────────────────────────────┐
  │     Git Repository (Config)         │
  │  - application.yml                  │
  │  - user-service.yml                 │
  │  - order-service.yml                │
  └──────────────┬──────────────────────┘
                 │
                 ▼
  ┌─────────────────────────────────────┐
  │     Config Server (Port 8888)       │
  └──────────────┬──────────────────────┘
                 │
       ┌─────────┼─────────┐
       ▼         ▼         ▼
  ┌────────┐ ┌────────┐ ┌────────┐
  │  User  │ │ Order  │ │Product │
  │Service │ │Service │ │Service │
  └────────┘ └────────┘ └────────┘
  ```

  **Config Server Setup:**

  ```xml
  <!-- pom.xml -->
  <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-config-server</artifactId>
  </dependency>
  ```

  ```java
  @SpringBootApplication
  @EnableConfigServer
  public class ConfigServerApplication {
      public static void main(String[] args) {
          SpringApplication.run(ConfigServerApplication.class, args);
      }
  }
  ```

  ```yaml
  # application.yml
  server:
    port: 8888
  
  spring:
    cloud:
      config:
        server:
          git:
            uri: https://github.com/your-org/config-repo
            default-label: main
            search-paths: '{application}'
            clone-on-start: true
          # Or use local file system
          # native:
          #   search-locations: file:///path/to/config
  ```

  **Config Repository Structure:**

  ```
  config-repo/
  ├── application.yml          # Common config for all services
  ├── application-dev.yml      # Dev environment
  ├── application-prod.yml     # Prod environment
  ├── user-service.yml         # User service specific
  ├── user-service-dev.yml
  ├── user-service-prod.yml
  ├── order-service.yml
  └── order-service-prod.yml
  ```

  **application.yml (Common):**

  ```yaml
  # Common configuration for all services
  logging:
    level:
      root: INFO
  
  management:
    endpoints:
      web:
        exposure:
          include: health,info,metrics
  ```

  **user-service.yml:**

  ```yaml
  # User service specific configuration
  server:
    port: 8081
  
  spring:
    datasource:
      url: jdbc:postgresql://localhost:5432/user_db
      username: user
      password: '{cipher}AQA...'  # Encrypted
  
  app:
    jwt:
      secret: '{cipher}AQB...'
      expiration: 86400000
  ```

</details>

<details>
  <summary>Config Client Setup</summary>
  <br/>

  **Config Client (Microservice):**

  ```xml
  <!-- pom.xml -->
  <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-config</artifactId>
  </dependency>
  ```

  ```yaml
  # bootstrap.yml (loaded before application.yml)
  spring:
    application:
      name: user-service
    cloud:
      config:
        uri: http://localhost:8888
        fail-fast: true
        retry:
          max-attempts: 6
          initial-interval: 1000
          multiplier: 1.1
    profiles:
      active: dev
  ```

  ```java
  @SpringBootApplication
  public class UserServiceApplication {
      public static void main(String[] args) {
          SpringApplication.run(UserServiceApplication.class, args);
      }
  }
  
  @RestController
  @RefreshScope  // Enable config refresh
  public class UserController {
      
      @Value("${app.message:Default Message}")
      private String message;
      
      @GetMapping("/message")
      public String getMessage() {
          return message;
      }
  }
  ```

  **Configuration Priority:**

  ```
  1. application.yml (in config repo)
  2. application-{profile}.yml (in config repo)
  3. {service-name}.yml (in config repo)
  4. {service-name}-{profile}.yml (in config repo)
  5. application.yml (in service)
  6. application-{profile}.yml (in service)
  ```

</details>

<details>
  <summary>Config Refresh</summary>
  <br/>

  **Refresh configuration without restart:**

  **1. Using @RefreshScope:**

  ```java
  @RestController
  @RefreshScope  // Beans will be recreated on refresh
  public class ConfigController {
      
      @Value("${app.message}")
      private String message;
      
      @Value("${app.feature.enabled}")
      private boolean featureEnabled;
      
      @GetMapping("/config")
      public Map<String, Object> getConfig() {
          return Map.of(
              "message", message,
              "featureEnabled", featureEnabled
          );
      }
  }
  ```

  **2. Manual Refresh:**

  ```bash
  # Trigger refresh endpoint
  curl -X POST http://localhost:8081/actuator/refresh
  ```

  ```yaml
  # Enable refresh endpoint
  management:
    endpoints:
      web:
        exposure:
          include: refresh
  ```

  **3. Spring Cloud Bus (Automatic Refresh):**

  ```xml
  <!-- pom.xml -->
  <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-bus-amqp</artifactId>
  </dependency>
  ```

  ```yaml
  # application.yml
  spring:
    rabbitmq:
      host: localhost
      port: 5672
      username: guest
      password: guest
  
  management:
    endpoints:
      web:
        exposure:
          include: busrefresh
  ```

  ```bash
  # Refresh all services at once
  curl -X POST http://localhost:8888/actuator/busrefresh
  ```

  **4. Config Change Webhook:**

  ```yaml
  # Config Server
  spring:
    cloud:
      config:
        server:
          git:
            uri: https://github.com/your-org/config-repo
          monitor:
            github:
              enabled: true
  ```

  **GitHub Webhook:**
  - Go to repository settings → Webhooks
  - Add webhook: `http://config-server:8888/monitor`
  - Content type: `application/json`
  - Events: `push`

</details>

<details>
  <summary>Encryption & Security</summary>
  <br/>

  **Encrypt sensitive data:**

  **1. Setup Encryption Key:**

  ```yaml
  # Config Server application.yml
  encrypt:
    key: my-secret-encryption-key
  
  # Or use key store
  encrypt:
    key-store:
      location: classpath:server.jks
      password: keystorepass
      alias: mytestkey
      secret: changeme
  ```

  **2. Encrypt Values:**

  ```bash
  # Encrypt a value
  curl http://localhost:8888/encrypt -d "mysecretpassword"
  # Returns: AQA1234567890abcdef...
  
  # Decrypt a value
  curl http://localhost:8888/decrypt -d "AQA1234567890abcdef..."
  # Returns: mysecretpassword
  ```

  **3. Use Encrypted Values:**

  ```yaml
  # user-service.yml
  spring:
    datasource:
      password: '{cipher}AQA1234567890abcdef...'
  
  app:
    jwt:
      secret: '{cipher}AQB9876543210fedcba...'
  ```

  **4. Asymmetric Encryption (RSA):**

  ```bash
  # Generate key pair
  keytool -genkeypair -alias mytestkey -keyalg RSA \
    -dname "CN=Config Server,OU=Unit,O=Org,L=City,S=State,C=US" \
    -keypass changeme -keystore server.jks -storepass keystorepass
  ```

  ```yaml
  # Config Server
  encrypt:
    key-store:
      location: classpath:server.jks
      password: keystorepass
      alias: mytestkey
  ```

</details>

<details>
  <summary>Profiles & Environment-Specific Config</summary>
  <br/>

  **Multiple environments:**

  ```yaml
  # application.yml (default)
  app:
    name: User Service
    version: 1.0.0
  
  ---
  # application-dev.yml
  spring:
    config:
      activate:
        on-profile: dev
  
  app:
    environment: Development
    debug: true
  
  logging:
    level:
      root: DEBUG
  
  ---
  # application-prod.yml
  spring:
    config:
      activate:
        on-profile: prod
  
  app:
    environment: Production
    debug: false
  
  logging:
    level:
      root: WARN
  ```

  **Service-specific profiles:**

  ```yaml
  # user-service-dev.yml
  server:
    port: 8081
  
  spring:
    datasource:
      url: jdbc:postgresql://localhost:5432/user_db_dev
      username: dev_user
      password: dev_pass
  
  ---
  # user-service-prod.yml
  server:
    port: 8081
  
  spring:
    datasource:
      url: jdbc:postgresql://prod-db:5432/user_db
      username: prod_user
      password: '{cipher}AQA...'
  ```

  **Activate profile:**

  ```bash
  # Command line
  java -jar user-service.jar --spring.profiles.active=prod
  
  # Environment variable
  export SPRING_PROFILES_ACTIVE=prod
  java -jar user-service.jar
  ```

  ```yaml
  # bootstrap.yml
  spring:
    profiles:
      active: ${ENVIRONMENT:dev}
  ```

</details>

<details>
  <summary>Configuration Properties</summary>
  <br/>

  **Type-safe configuration:**

  ```java
  @Configuration
  @ConfigurationProperties(prefix = "app")
  @RefreshScope
  public class AppProperties {
      
      private String name;
      private String version;
      private Jwt jwt;
      private Feature feature;
      
      public static class Jwt {
          private String secret;
          private long expiration;
          
          // Getters and setters
      }
      
      public static class Feature {
          private boolean enabled;
          private int maxRetries;
          
          // Getters and setters
      }
      
      // Getters and setters
  }
  ```

  ```yaml
  # Configuration
  app:
    name: User Service
    version: 1.0.0
    jwt:
      secret: my-secret-key
      expiration: 86400000
    feature:
      enabled: true
      max-retries: 3
  ```

  ```java
  @Service
  public class UserService {
      
      @Autowired
      private AppProperties appProperties;
      
      public String generateToken(User user) {
          String secret = appProperties.getJwt().getSecret();
          long expiration = appProperties.getJwt().getExpiration();
          
          return Jwts.builder()
              .setSubject(user.getUsername())
              .setExpiration(new Date(System.currentTimeMillis() + expiration))
              .signWith(SignatureAlgorithm.HS512, secret)
              .compact();
      }
  }
  ```

  **Validation:**

  ```java
  @Configuration
  @ConfigurationProperties(prefix = "app")
  @Validated
  public class AppProperties {
      
      @NotBlank
      private String name;
      
      @Min(1)
      @Max(100)
      private int maxConnections;
      
      @Valid
      private Jwt jwt;
      
      public static class Jwt {
          @NotBlank
          private String secret;
          
          @Min(1000)
          private long expiration;
          
          // Getters and setters
      }
      
      // Getters and setters
  }
  ```

</details>

<details>
  <summary>Externalized Configuration</summary>
  <br/>

  **Configuration sources (priority order):**

  1. Command line arguments
  2. Environment variables
  3. Config server
  4. application.yml in jar
  5. Default values

  **Environment variables:**

  ```bash
  # Set environment variables
  export DATABASE_URL=jdbc:postgresql://prod-db:5432/user_db
  export DATABASE_USERNAME=prod_user
  export DATABASE_PASSWORD=prod_pass
  export JWT_SECRET=my-secret-key
  ```

  ```yaml
  # Use environment variables
  spring:
    datasource:
      url: ${DATABASE_URL}
      username: ${DATABASE_USERNAME}
      password: ${DATABASE_PASSWORD}
  
  app:
    jwt:
      secret: ${JWT_SECRET}
  ```

  **Kubernetes ConfigMap:**

  ```yaml
  # configmap.yaml
  apiVersion: v1
  kind: ConfigMap
  metadata:
    name: user-service-config
  data:
    application.yml: |
      server:
        port: 8080
      spring:
        datasource:
          url: jdbc:postgresql://postgres:5432/user_db
      app:
        feature:
          enabled: true
  ```

  ```yaml
  # deployment.yaml
  apiVersion: apps/v1
  kind: Deployment
  metadata:
    name: user-service
  spec:
    template:
      spec:
        containers:
        - name: user-service
          image: user-service:1.0
          volumeMounts:
          - name: config
            mountPath: /config
        volumes:
        - name: config
          configMap:
            name: user-service-config
  ```

  **Kubernetes Secrets:**

  ```yaml
  # secret.yaml
  apiVersion: v1
  kind: Secret
  metadata:
    name: user-service-secret
  type: Opaque
  data:
    database-password: cHJvZF9wYXNz  # base64 encoded
    jwt-secret: bXktc2VjcmV0LWtleQ==
  ```

  ```yaml
  # deployment.yaml
  apiVersion: apps/v1
  kind: Deployment
  spec:
    template:
      spec:
        containers:
        - name: user-service
          env:
          - name: DATABASE_PASSWORD
            valueFrom:
              secretKeyRef:
                name: user-service-secret
                key: database-password
          - name: JWT_SECRET
            valueFrom:
              secretKeyRef:
                name: user-service-secret
                key: jwt-secret
  ```

</details>

<details>
  <summary>Best Practices</summary>
  <br/>

  ```java
  // ✅ DO: Use @RefreshScope for dynamic config
  @RestController
  @RefreshScope
  public class ConfigController {
      @Value("${app.message}")
      private String message;
  }
  
  // ✅ DO: Use @ConfigurationProperties for type-safe config
  @ConfigurationProperties(prefix = "app")
  public class AppProperties {
      private String name;
      private Jwt jwt;
      // Getters and setters
  }
  
  // ✅ DO: Encrypt sensitive data
  spring:
    datasource:
      password: '{cipher}AQA...'
  
  // ✅ DO: Use profiles for different environments
  spring:
    profiles:
      active: ${ENVIRONMENT:dev}
  
  // ✅ DO: Provide default values
  @Value("${app.timeout:5000}")
  private int timeout;
  
  // ✅ DO: Validate configuration
  @ConfigurationProperties(prefix = "app")
  @Validated
  public class AppProperties {
      @NotBlank
      private String name;
      
      @Min(1)
      private int maxConnections;
  }
  
  // ✅ DO: Use Spring Cloud Bus for automatic refresh
  management:
    endpoints:
      web:
        exposure:
          include: busrefresh
  
  // ✅ DO: Version your configuration
  # config-repo/
  # ├── v1/
  # │   └── user-service.yml
  # └── v2/
  #     └── user-service.yml
  
  // ❌ DON'T: Store secrets in plain text
  spring:
    datasource:
      password: mysecretpassword  # Bad!
  
  // ❌ DON'T: Hardcode configuration in code
  private static final String DATABASE_URL = "jdbc:...";  // Bad!
  
  // ❌ DON'T: Use @Value for complex configuration
  @Value("${app.jwt.secret}")
  private String jwtSecret;
  @Value("${app.jwt.expiration}")
  private long jwtExpiration;
  // Use @ConfigurationProperties instead
  
  // ✅ DO: Monitor config changes
  @Component
  public class ConfigChangeListener {
      @EventListener
      public void handleRefresh(RefreshScopeRefreshedEvent event) {
          log.info("Configuration refreshed: {}", event.getName());
      }
  }
  
  // ✅ DO: Use fail-fast for critical config
  spring:
    cloud:
      config:
        fail-fast: true  // Fail startup if config server unavailable
  
  // ✅ DO: Implement retry logic
  spring:
    cloud:
      config:
        retry:
          max-attempts: 6
          initial-interval: 1000
          multiplier: 1.1
  ```

  **Summary:**
  + **Spring Cloud Config** provides centralized configuration
  + Use **@RefreshScope** for dynamic config updates
  + **Encrypt** sensitive data with `{cipher}` prefix
  + Use **profiles** for environment-specific config
  + Use **@ConfigurationProperties** for type-safe config
  + Use **Spring Cloud Bus** for automatic refresh
  + **Externalize** configuration (env vars, ConfigMaps, Secrets)
  + **Validate** configuration with `@Validated`
  + **Monitor** config changes with event listeners
  + Use **fail-fast** and **retry** for resilience

</details>

## Distributed Logging & Tracing

<details>
  <summary>Correlation IDs</summary>
  <br/>

  **Correlation ID** (or Trace ID) tracks requests across multiple services.

  **Why needed:**
  + Track request flow across services
  + Debug distributed transactions
  + Monitor performance
  + Troubleshoot errors

  **Flow:**

  ```
  Client Request
       │
       ▼
  ┌─────────────┐  Correlation-ID: abc-123
  │ API Gateway │────────────────────────┐
  └─────────────┘                        │
       │                                 ▼
       │ Correlation-ID: abc-123   ┌──────────┐
       ├──────────────────────────▶│   User   │
       │                           │ Service  │
       │                           └──────────┘
       │                                 │
       │ Correlation-ID: abc-123         │
       └──────────────────────────┐      │
                                  ▼      ▼
                            ┌──────────┐ ┌──────────┐
                            │  Order   │ │ Product  │
                            │ Service  │ │ Service  │
                            └──────────┘ └──────────┘
  
  All logs include: [abc-123] Log message
  ```

  **Implementation with Filter:**

  ```java
  @Component
  @Order(Ordered.HIGHEST_PRECEDENCE)
  public class CorrelationIdFilter implements Filter {
      
      private static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
      private static final String CORRELATION_ID_LOG_VAR = "correlationId";
      
      @Override
      public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
              throws IOException, ServletException {
          
          HttpServletRequest httpRequest = (HttpServletRequest) request;
          HttpServletResponse httpResponse = (HttpServletResponse) response;
          
          // Get or generate correlation ID
          String correlationId = httpRequest.getHeader(CORRELATION_ID_HEADER);
          if (correlationId == null || correlationId.isEmpty()) {
              correlationId = UUID.randomUUID().toString();
          }
          
          // Add to MDC (Mapped Diagnostic Context)
          MDC.put(CORRELATION_ID_LOG_VAR, correlationId);
          
          // Add to response header
          httpResponse.setHeader(CORRELATION_ID_HEADER, correlationId);
          
          try {
              chain.doFilter(request, response);
          } finally {
              // Clean up MDC
              MDC.clear();
          }
      }
  }
  ```

  **Logback Configuration:**

  ```xml
  <!-- logback-spring.xml -->
  <configuration>
      <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
          <encoder>
              <pattern>
                  %d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level [%X{correlationId}] %logger{36} - %msg%n
              </pattern>
          </encoder>
      </appender>
      
      <root level="INFO">
          <appender-ref ref="CONSOLE"/>
      </root>
  </configuration>
  ```

  **Log Output:**

  ```
  2024-01-15 10:30:45 [http-nio-8081-exec-1] INFO  [abc-123] c.e.UserController - Creating user: john
  2024-01-15 10:30:45 [http-nio-8081-exec-1] INFO  [abc-123] c.e.UserService - User created: 1
  2024-01-15 10:30:46 [http-nio-8082-exec-2] INFO  [abc-123] c.e.OrderController - Creating order for user: 1
  2024-01-15 10:30:46 [http-nio-8082-exec-2] INFO  [abc-123] c.e.OrderService - Order created: 101
  ```

  **Propagate to downstream services:**

  ```java
  @Configuration
  public class RestTemplateConfig {
      
      @Bean
      public RestTemplate restTemplate() {
          RestTemplate restTemplate = new RestTemplate();
          
          // Add interceptor to propagate correlation ID
          restTemplate.setInterceptors(Collections.singletonList(
              (request, body, execution) -> {
                  String correlationId = MDC.get("correlationId");
                  if (correlationId != null) {
                      request.getHeaders().add("X-Correlation-ID", correlationId);
                  }
                  return execution.execute(request, body);
              }
          ));
          
          return restTemplate;
      }
  }
  ```

  **Feign Client:**

  ```java
  @Component
  public class FeignCorrelationIdInterceptor implements RequestInterceptor {
      
      @Override
      public void apply(RequestTemplate template) {
          String correlationId = MDC.get("correlationId");
          if (correlationId != null) {
              template.header("X-Correlation-ID", correlationId);
          }
      }
  }
  ```

  **WebClient (Reactive):**

  ```java
  @Configuration
  public class WebClientConfig {
      
      @Bean
      public WebClient webClient() {
          return WebClient.builder()
              .filter((request, next) -> {
                  String correlationId = MDC.get("correlationId");
                  if (correlationId != null) {
                      ClientRequest filtered = ClientRequest.from(request)
                          .header("X-Correlation-ID", correlationId)
                          .build();
                      return next.exchange(filtered);
                  }
                  return next.exchange(request);
              })
              .build();
      }
  }
  ```

</details>

<details>
  <summary>MDC (Mapped Diagnostic Context)</summary>
  <br/>

  **MDC** stores contextual information in a thread-local map.

  **Basic Usage:**

  ```java
  @Service
  @Slf4j
  public class UserService {
      
      public User createUser(UserRequest request) {
          // Add context to MDC
          MDC.put("userId", request.getUsername());
          MDC.put("operation", "createUser");
          
          try {
              log.info("Creating user");
              User user = userRepository.save(new User(request));
              log.info("User created successfully");
              return user;
          } catch (Exception e) {
              log.error("Failed to create user", e);
              throw e;
          } finally {
              // Clean up MDC
              MDC.remove("userId");
              MDC.remove("operation");
          }
      }
  }
  ```

  **Log Output:**

  ```
  2024-01-15 10:30:45 [http-nio-8081-exec-1] INFO  [abc-123] [userId=john] [operation=createUser] c.e.UserService - Creating user
  2024-01-15 10:30:45 [http-nio-8081-exec-1] INFO  [abc-123] [userId=john] [operation=createUser] c.e.UserService - User created successfully
  ```

  **Logback Pattern:**

  ```xml
  <pattern>
      %d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level [%X{correlationId}] [%X{userId}] [%X{operation}] %logger{36} - %msg%n
  </pattern>
  ```

  **MDC with Async:**

  ```java
  @Configuration
  @EnableAsync
  public class AsyncConfig implements AsyncConfigurer {
      
      @Override
      public Executor getAsyncExecutor() {
          ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
          executor.setCorePoolSize(5);
          executor.setMaxPoolSize(10);
          executor.setQueueCapacity(25);
          executor.setThreadNamePrefix("async-");
          
          // Propagate MDC to async threads
          executor.setTaskDecorator(new MdcTaskDecorator());
          executor.initialize();
          return executor;
      }
  }
  
  public class MdcTaskDecorator implements TaskDecorator {
      
      @Override
      public Runnable decorate(Runnable runnable) {
          // Capture MDC from parent thread
          Map<String, String> contextMap = MDC.getCopyOfContextMap();
          
          return () -> {
              try {
                  // Set MDC in child thread
                  if (contextMap != null) {
                      MDC.setContextMap(contextMap);
                  }
                  runnable.run();
              } finally {
                  MDC.clear();
              }
          };
      }
  }
  ```

</details>

<details>
  <summary>Centralized Logging (ELK Stack)</summary>
  <br/>

  **ELK Stack:**
  + **Elasticsearch** - Store and search logs
  + **Logstash** - Process and transform logs
  + **Kibana** - Visualize logs

  **Architecture:**

  ```
  ┌──────────┐    ┌──────────┐    ┌──────────┐
  │   User   │    │  Order   │    │ Product  │
  │ Service  │    │ Service  │    │ Service  │
  └────┬─────┘    └────┬─────┘    └────┬─────┘
       │               │               │
       │ Logs          │ Logs          │ Logs
       ▼               ▼               ▼
  ┌────────────────────────────────────────┐
  │            Logstash                    │
  │  (Collect, Parse, Transform)           │
  └────────────────┬───────────────────────┘
                   │
                   ▼
  ┌────────────────────────────────────────┐
  │         Elasticsearch                  │
  │  (Store, Index, Search)                │
  └────────────────┬───────────────────────┘
                   │
                   ▼
  ┌────────────────────────────────────────┐
  │            Kibana                      │
  │  (Visualize, Dashboard, Alerts)        │
  └────────────────────────────────────────┘
  ```

  **Logstash Configuration:**

  ```ruby
  # logstash.conf
  input {
    tcp {
      port => 5000
      codec => json_lines
    }
  }
  
  filter {
    # Parse JSON logs
    json {
      source => "message"
    }
    
    # Add timestamp
    date {
      match => ["timestamp", "ISO8601"]
    }
    
    # Extract fields
    mutate {
      add_field => {
        "service" => "%{[fields][service]}"
        "environment" => "%{[fields][environment]}"
      }
    }
  }
  
  output {
    elasticsearch {
      hosts => ["elasticsearch:9200"]
      index => "logs-%{+YYYY.MM.dd}"
    }
    
    stdout {
      codec => rubydebug
    }
  }
  ```

  **Logback with Logstash:**

  ```xml
  <!-- pom.xml -->
  <dependency>
      <groupId>net.logstash.logback</groupId>
      <artifactId>logstash-logback-encoder</artifactId>
      <version>7.3</version>
  </dependency>
  ```

  ```xml
  <!-- logback-spring.xml -->
  <configuration>
      <appender name="LOGSTASH" class="net.logstash.logback.appender.LogstashTcpSocketAppender">
          <destination>logstash:5000</destination>
          <encoder class="net.logstash.logback.encoder.LogstashEncoder">
              <customFields>{"service":"user-service","environment":"production"}</customFields>
          </encoder>
      </appender>
      
      <root level="INFO">
          <appender-ref ref="LOGSTASH"/>
      </root>
  </configuration>
  ```

  **Structured Logging:**

  ```java
  @Slf4j
  @Service
  public class UserService {
      
      public User createUser(UserRequest request) {
          // Structured logging with key-value pairs
          log.info("Creating user: username={}, email={}", 
              request.getUsername(), 
              request.getEmail());
          
          User user = userRepository.save(new User(request));
          
          log.info("User created: userId={}, username={}", 
              user.getId(), 
              user.getUsername());
          
          return user;
      }
  }
  ```

  **JSON Log Output:**

  ```json
  {
    "@timestamp": "2024-01-15T10:30:45.123Z",
    "level": "INFO",
    "service": "user-service",
    "environment": "production",
    "correlationId": "abc-123",
    "thread": "http-nio-8081-exec-1",
    "logger": "com.example.UserService",
    "message": "User created",
    "userId": "1",
    "username": "john"
  }
  ```

</details>

<details>
  <summary>Distributed Tracing (Zipkin/Jaeger)</summary>
  <br/>

  **Distributed tracing** tracks requests across services with timing information.

  **Spring Cloud Sleuth + Zipkin:**

  ```xml
  <!-- pom.xml -->
  <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-sleuth</artifactId>
  </dependency>
  <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-sleuth-zipkin</artifactId>
  </dependency>
  ```

  ```yaml
  # application.yml
  spring:
    application:
      name: user-service
    sleuth:
      sampler:
        probability: 1.0  # Sample 100% of requests (use 0.1 for 10% in prod)
    zipkin:
      base-url: http://zipkin:9411
      sender:
        type: web
  ```

  **Automatic Instrumentation:**

  ```java
  @RestController
  @Slf4j
  public class UserController {
      
      @Autowired
      private RestTemplate restTemplate;
      
      @GetMapping("/users/{id}")
      public User getUser(@PathVariable Long id) {
          // Sleuth automatically adds trace/span IDs to logs
          log.info("Getting user: {}", id);
          
          // Trace propagates to downstream service
          Order order = restTemplate.getForObject(
              "http://order-service/orders?userId=" + id,
              Order.class
          );
          
          return userService.getUser(id);
      }
  }
  ```

  **Log Output with Trace IDs:**

  ```
  2024-01-15 10:30:45 [user-service,abc123,def456,true] INFO  c.e.UserController - Getting user: 1
  
  Format: [service-name, trace-id, span-id, exportable]
  - trace-id: Unique ID for entire request flow
  - span-id: Unique ID for this service call
  - exportable: Whether to send to Zipkin
  ```

  **Custom Spans:**

  ```java
  @Service
  public class UserService {
      
      @Autowired
      private Tracer tracer;
      
      public User createUser(UserRequest request) {
          // Create custom span
          Span span = tracer.nextSpan().name("createUser").start();
          
          try (Tracer.SpanInScope ws = tracer.withSpan(span)) {
              span.tag("username", request.getUsername());
              span.tag("email", request.getEmail());
              
              User user = userRepository.save(new User(request));
              
              span.tag("userId", user.getId().toString());
              span.annotate("User created successfully");
              
              return user;
          } catch (Exception e) {
              span.tag("error", e.getMessage());
              throw e;
          } finally {
              span.end();
          }
      }
  }
  ```

  **Zipkin UI:**

  ```
  http://localhost:9411
  
  Shows:
  - Request timeline
  - Service dependencies
  - Latency breakdown
  - Error traces
  ```

</details>

<details>
  <summary>Log Aggregation Patterns</summary>
  <br/>

  **1. Sidecar Pattern:**

  ```yaml
  # Kubernetes deployment with Filebeat sidecar
  apiVersion: apps/v1
  kind: Deployment
  metadata:
    name: user-service
  spec:
    template:
      spec:
        containers:
        # Main application
        - name: user-service
          image: user-service:1.0
          volumeMounts:
          - name: logs
            mountPath: /var/log/app
        
        # Filebeat sidecar
        - name: filebeat
          image: docker.elastic.co/beats/filebeat:7.15.0
          volumeMounts:
          - name: logs
            mountPath: /var/log/app
          - name: filebeat-config
            mountPath: /usr/share/filebeat/filebeat.yml
            subPath: filebeat.yml
        
        volumes:
        - name: logs
          emptyDir: {}
        - name: filebeat-config
          configMap:
            name: filebeat-config
  ```

  **2. Log Forwarder:**

  ```yaml
  # Fluentd DaemonSet
  apiVersion: apps/v1
  kind: DaemonSet
  metadata:
    name: fluentd
  spec:
    template:
      spec:
        containers:
        - name: fluentd
          image: fluent/fluentd:v1.14
          volumeMounts:
          - name: varlog
            mountPath: /var/log
          - name: varlibdockercontainers
            mountPath: /var/lib/docker/containers
            readOnly: true
        volumes:
        - name: varlog
          hostPath:
            path: /var/log
        - name: varlibdockercontainers
          hostPath:
            path: /var/lib/docker/containers
  ```

  **3. Direct Shipping:**

  ```xml
  <!-- Logback with direct Elasticsearch shipping -->
  <appender name="ELASTIC" class="com.internetitem.logback.elasticsearch.ElasticsearchAppender">
      <url>http://elasticsearch:9200/_bulk</url>
      <index>logs-%date{yyyy-MM-dd}</index>
      <type>log</type>
      <loggerName>es-logger</loggerName>
      <errorLoggerName>es-error-logger</errorLoggerName>
      <connectTimeout>30000</connectTimeout>
      <errorsToStderr>false</errorsToStderr>
      <includeCallerData>false</includeCallerData>
      <logsToStderr>false</logsToStderr>
      <maxQueueSize>104857600</maxQueueSize>
      <maxRetries>3</maxRetries>
      <readTimeout>30000</readTimeout>
      <sleepTime>250</sleepTime>
      <rawJsonMessage>false</rawJsonMessage>
      <includeMdc>true</includeMdc>
      <maxMessageSize>100</maxMessageSize>
      <authentication class="com.internetitem.logback.elasticsearch.config.BasicAuthentication">
          <username>elastic</username>
          <password>changeme</password>
      </authentication>
  </appender>
  ```

</details>

<details>
  <summary>Best Practices</summary>
  <br/>

  ```java
  // ✅ DO: Use correlation IDs for request tracking
  @Component
  public class CorrelationIdFilter implements Filter {
      @Override
      public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
          String correlationId = UUID.randomUUID().toString();
          MDC.put("correlationId", correlationId);
          try {
              chain.doFilter(request, response);
          } finally {
              MDC.clear();
          }
      }
  }
  
  // ✅ DO: Use structured logging
  log.info("User created: userId={}, username={}, email={}", 
      user.getId(), user.getUsername(), user.getEmail());
  
  // ❌ DON'T: Use string concatenation
  log.info("User created: " + user.getId() + ", " + user.getUsername());
  
  // ✅ DO: Log at appropriate levels
  log.trace("Detailed debug information");
  log.debug("Debug information");
  log.info("Important business events");
  log.warn("Warning conditions");
  log.error("Error conditions", exception);
  
  // ✅ DO: Include context in logs
  log.info("Processing order: orderId={}, userId={}, total={}", 
      order.getId(), order.getUserId(), order.getTotal());
  
  // ✅ DO: Propagate correlation ID to downstream services
  @Bean
  public RestTemplate restTemplate() {
      RestTemplate restTemplate = new RestTemplate();
      restTemplate.setInterceptors(Collections.singletonList(
          (request, body, execution) -> {
              String correlationId = MDC.get("correlationId");
              if (correlationId != null) {
                  request.getHeaders().add("X-Correlation-ID", correlationId);
              }
              return execution.execute(request, body);
          }
      ));
      return restTemplate;
  }
  
  // ✅ DO: Use MDC for contextual information
  MDC.put("userId", user.getId().toString());
  MDC.put("operation", "createOrder");
  try {
      // Business logic
  } finally {
      MDC.clear();
  }
  
  // ✅ DO: Use distributed tracing
  spring:
    sleuth:
      sampler:
        probability: 0.1  # Sample 10% in production
    zipkin:
      base-url: http://zipkin:9411
  
  // ✅ DO: Centralize logs
  # Use ELK, Splunk, or cloud logging services
  
  // ✅ DO: Set log retention policies
  # Elasticsearch index lifecycle management
  # Delete logs older than 30 days
  
  // ❌ DON'T: Log sensitive data
  log.info("User password: {}", user.getPassword());  // Bad!
  log.info("Credit card: {}", payment.getCreditCard());  // Bad!
  
  // ✅ DO: Mask sensitive data
  log.info("User email: {}", maskEmail(user.getEmail()));
  log.info("Credit card: {}", maskCreditCard(payment.getCreditCard()));
  
  // ✅ DO: Use async logging for performance
  <appender name="ASYNC" class="ch.qos.logback.classic.AsyncAppender">
      <appender-ref ref="FILE"/>
      <queueSize>512</queueSize>
      <discardingThreshold>0</discardingThreshold>
  </appender>
  
  // ✅ DO: Monitor log volume
  # Alert if log volume spikes (indicates errors)
  # Alert if log volume drops (indicates service down)
  ```

  **Summary:**
  + Use **correlation IDs** to track requests across services
  + Use **MDC** for contextual information
  + Use **structured logging** (key-value pairs)
  + **Centralize logs** with ELK, Splunk, or cloud services
  + Use **distributed tracing** (Zipkin, Jaeger)
  + **Propagate** correlation IDs to downstream services
  + Log at **appropriate levels** (INFO, WARN, ERROR)
  + **Mask sensitive data** in logs
  + Use **async logging** for performance
  + Set **log retention policies**
  + **Monitor** log volume and patterns

</details>

## Resilience Patterns

<details>
  <summary>Circuit Breaker Pattern</summary>
  <br/>

  **Circuit Breaker** prevents cascading failures by stopping calls to failing services.

  **States:**

  ```
  ┌─────────┐
  │ CLOSED  │ ◄─── Normal operation
  └────┬────┘
       │ Failures exceed threshold
       ▼
  ┌─────────┐
  │  OPEN   │ ◄─── Reject all requests
  └────┬────┘
       │ After timeout
       ▼
  ┌──────────┐
  │ HALF_OPEN│ ◄─── Test if service recovered
  └──────────┘
       │
       ├─ Success → CLOSED
       └─ Failure → OPEN
  ```

  **Resilience4j Implementation:**

  ```java
  // Dependencies
  <dependency>
      <groupId>io.github.resilience4j</groupId>
      <artifactId>resilience4j-spring-boot2</artifactId>
  </dependency>
  ```

  ```yaml
  # application.yml
  resilience4j:
    circuitbreaker:
      instances:
        productService:
          register-health-indicator: true
          sliding-window-size: 10
          minimum-number-of-calls: 5
          permitted-number-of-calls-in-half-open-state: 3
          wait-duration-in-open-state: 10s
          failure-rate-threshold: 50
          slow-call-rate-threshold: 100
          slow-call-duration-threshold: 2s
  ```

  ```java
  @Service
  @Slf4j
  public class OrderService {
      
      @Autowired
      private ProductClient productClient;
      
      @CircuitBreaker(name = "productService", fallbackMethod = "getProductFallback")
      public Product getProduct(Long productId) {
          log.info("Calling product service for product: {}", productId);
          return productClient.getProduct(productId);
      }
      
      // Fallback method (same signature + Throwable)
      public Product getProductFallback(Long productId, Throwable throwable) {
          log.error("Circuit breaker activated for product: {}", productId, throwable);
          
          // Return cached data or default
          return Product.builder()
              .id(productId)
              .name("Product Unavailable")
              .price(BigDecimal.ZERO)
              .build();
      }
  }
  ```

  **Manual Circuit Breaker:**

  ```java
  @Service
  public class OrderService {
      
      @Autowired
      private CircuitBreakerRegistry circuitBreakerRegistry;
      
      public Product getProduct(Long productId) {
          CircuitBreaker circuitBreaker = circuitBreakerRegistry
              .circuitBreaker("productService");
          
          return circuitBreaker.executeSupplier(() -> {
              return productClient.getProduct(productId);
          });
      }
  }
  ```

  **Circuit Breaker Events:**

  ```java
  @Component
  public class CircuitBreakerEventListener {
      
      @Autowired
      private CircuitBreakerRegistry circuitBreakerRegistry;
      
      @PostConstruct
      public void init() {
          circuitBreakerRegistry.circuitBreaker("productService")
              .getEventPublisher()
              .onStateTransition(event -> {
                  log.info("Circuit breaker state changed: {} -> {}",
                      event.getStateTransition().getFromState(),
                      event.getStateTransition().getToState());
              })
              .onFailureRateExceeded(event -> {
                  log.warn("Failure rate exceeded: {}%", event.getFailureRate());
              })
              .onSlowCallRateExceeded(event -> {
                  log.warn("Slow call rate exceeded: {}%", event.getSlowCallRate());
              });
      }
  }
  ```

</details>

<details>
  <summary>Retry Pattern</summary>
  <br/>

  **Retry** automatically retries failed operations.

  **Retry strategies:**
  + **Fixed delay** - Wait same time between retries
  + **Exponential backoff** - Increase wait time exponentially
  + **Random jitter** - Add randomness to avoid thundering herd

  **Resilience4j Retry:**

  ```yaml
  # application.yml
  resilience4j:
    retry:
      instances:
        productService:
          max-attempts: 3
          wait-duration: 1s
          enable-exponential-backoff: true
          exponential-backoff-multiplier: 2
          retry-exceptions:
            - java.net.ConnectException
            - java.net.SocketTimeoutException
          ignore-exceptions:
            - com.example.BusinessException
  ```

  ```java
  @Service
  public class OrderService {
      
      @Retry(name = "productService", fallbackMethod = "getProductFallback")
      public Product getProduct(Long productId) {
          log.info("Attempting to get product: {}", productId);
          return productClient.getProduct(productId);
      }
      
      public Product getProductFallback(Long productId, Exception e) {
          log.error("All retry attempts failed for product: {}", productId, e);
          return getDefaultProduct(productId);
      }
  }
  ```

  **Custom Retry Logic:**

  ```java
  @Service
  public class OrderService {
      
      public Product getProductWithRetry(Long productId) {
          int maxRetries = 3;
          int attempt = 0;
          long waitTime = 1000; // 1 second
          
          while (attempt < maxRetries) {
              try {
                  return productClient.getProduct(productId);
              } catch (Exception e) {
                  attempt++;
                  if (attempt >= maxRetries) {
                      throw new ServiceUnavailableException("Product service unavailable", e);
                  }
                  
                  log.warn("Retry attempt {} failed, waiting {}ms", attempt, waitTime);
                  
                  try {
                      Thread.sleep(waitTime);
                  } catch (InterruptedException ie) {
                      Thread.currentThread().interrupt();
                      throw new RuntimeException(ie);
                  }
                  
                  // Exponential backoff
                  waitTime *= 2;
              }
          }
          
          throw new ServiceUnavailableException("Product service unavailable");
      }
  }
  ```

  **Spring Retry:**

  ```java
  @Configuration
  @EnableRetry
  public class RetryConfig {
  }
  
  @Service
  public class OrderService {
      
      @Retryable(
          value = {ConnectException.class, SocketTimeoutException.class},
          maxAttempts = 3,
          backoff = @Backoff(delay = 1000, multiplier = 2)
      )
      public Product getProduct(Long productId) {
          return productClient.getProduct(productId);
      }
      
      @Recover
      public Product recover(Exception e, Long productId) {
          log.error("Recovery method called for product: {}", productId, e);
          return getDefaultProduct(productId);
      }
  }
  ```

  **Retry with Jitter:**

  ```java
  @Service
  public class OrderService {
      
      private final Random random = new Random();
      
      public Product getProductWithJitter(Long productId) {
          int maxRetries = 3;
          int attempt = 0;
          long baseWaitTime = 1000;
          
          while (attempt < maxRetries) {
              try {
                  return productClient.getProduct(productId);
              } catch (Exception e) {
                  attempt++;
                  if (attempt >= maxRetries) {
                      throw new ServiceUnavailableException("Product service unavailable", e);
                  }
                  
                  // Exponential backoff with jitter
                  long waitTime = (long) (baseWaitTime * Math.pow(2, attempt));
                  long jitter = random.nextInt(1000); // 0-1000ms random jitter
                  long totalWait = waitTime + jitter;
                  
                  log.warn("Retry attempt {} failed, waiting {}ms", attempt, totalWait);
                  
                  try {
                      Thread.sleep(totalWait);
                  } catch (InterruptedException ie) {
                      Thread.currentThread().interrupt();
                      throw new RuntimeException(ie);
                  }
              }
          }
          
          throw new ServiceUnavailableException("Product service unavailable");
      }
  }
  ```

</details>

<details>
  <summary>Bulkhead Pattern</summary>
  <br/>

  **Bulkhead** isolates resources to prevent cascading failures.

  **Thread Pool Bulkhead:**

  ```yaml
  # application.yml
  resilience4j:
    bulkhead:
      instances:
        productService:
          max-concurrent-calls: 10
          max-wait-duration: 1s
    
    thread-pool-bulkhead:
      instances:
        productService:
          max-thread-pool-size: 10
          core-thread-pool-size: 5
          queue-capacity: 20
          keep-alive-duration: 20ms
  ```

  ```java
  @Service
  public class OrderService {
      
      @Bulkhead(name = "productService", type = Bulkhead.Type.THREADPOOL)
      public CompletableFuture<Product> getProductAsync(Long productId) {
          return CompletableFuture.supplyAsync(() -> {
              return productClient.getProduct(productId);
          });
      }
  }
  ```

  **Semaphore Bulkhead:**

  ```java
  @Service
  public class OrderService {
      
      @Bulkhead(name = "productService", fallbackMethod = "getProductFallback")
      public Product getProduct(Long productId) {
          return productClient.getProduct(productId);
      }
      
      public Product getProductFallback(Long productId, BulkheadFullException e) {
          log.error("Bulkhead is full for product: {}", productId);
          return getDefaultProduct(productId);
      }
  }
  ```

  **Custom Thread Pool:**

  ```java
  @Configuration
  public class ThreadPoolConfig {
      
      @Bean("productServiceExecutor")
      public ThreadPoolTaskExecutor productServiceExecutor() {
          ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
          executor.setCorePoolSize(5);
          executor.setMaxPoolSize(10);
          executor.setQueueCapacity(20);
          executor.setThreadNamePrefix("product-service-");
          executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
          executor.initialize();
          return executor;
      }
      
      @Bean("paymentServiceExecutor")
      public ThreadPoolTaskExecutor paymentServiceExecutor() {
          ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
          executor.setCorePoolSize(3);
          executor.setMaxPoolSize(5);
          executor.setQueueCapacity(10);
          executor.setThreadNamePrefix("payment-service-");
          executor.initialize();
          return executor;
      }
  }
  
  @Service
  public class OrderService {
      
      @Autowired
      @Qualifier("productServiceExecutor")
      private ThreadPoolTaskExecutor productExecutor;
      
      @Autowired
      @Qualifier("paymentServiceExecutor")
      private ThreadPoolTaskExecutor paymentExecutor;
      
      public CompletableFuture<Product> getProduct(Long productId) {
          return CompletableFuture.supplyAsync(() -> {
              return productClient.getProduct(productId);
          }, productExecutor);
      }
      
      public CompletableFuture<Payment> processPayment(PaymentRequest request) {
          return CompletableFuture.supplyAsync(() -> {
              return paymentClient.processPayment(request);
          }, paymentExecutor);
      }
  }
  ```

</details>

<details>
  <summary>Timeout Pattern</summary>
  <br/>

  **Timeout** prevents indefinite waiting for responses.

  **Resilience4j TimeLimiter:**

  ```yaml
  # application.yml
  resilience4j:
    timelimiter:
      instances:
        productService:
          timeout-duration: 2s
          cancel-running-future: true
  ```

  ```java
  @Service
  public class OrderService {
      
      @TimeLimiter(name = "productService", fallbackMethod = "getProductFallback")
      public CompletableFuture<Product> getProduct(Long productId) {
          return CompletableFuture.supplyAsync(() -> {
              return productClient.getProduct(productId);
          });
      }
      
      public CompletableFuture<Product> getProductFallback(Long productId, TimeoutException e) {
          log.error("Timeout getting product: {}", productId);
          return CompletableFuture.completedFuture(getDefaultProduct(productId));
      }
  }
  ```

  **RestTemplate Timeout:**

  ```java
  @Configuration
  public class RestTemplateConfig {
      
      @Bean
      public RestTemplate restTemplate() {
          SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
          factory.setConnectTimeout(2000);  // 2 seconds
          factory.setReadTimeout(5000);     // 5 seconds
          return new RestTemplate(factory);
      }
  }
  ```

  **WebClient Timeout:**

  ```java
  @Configuration
  public class WebClientConfig {
      
      @Bean
      public WebClient webClient() {
          HttpClient httpClient = HttpClient.create()
              .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 2000)
              .responseTimeout(Duration.ofSeconds(5));
          
          return WebClient.builder()
              .clientConnector(new ReactorClientHttpConnector(httpClient))
              .build();
      }
  }
  ```

  **Feign Timeout:**

  ```yaml
  # application.yml
  feign:
    client:
      config:
        product-service:
          connect-timeout: 2000
          read-timeout: 5000
  ```

</details>

<details>
  <summary>Rate Limiting</summary>
  <br/>

  **Rate limiting** controls the number of requests to prevent overload.

  **Resilience4j RateLimiter:**

  ```yaml
  # application.yml
  resilience4j:
    ratelimiter:
      instances:
        productService:
          limit-for-period: 10
          limit-refresh-period: 1s
          timeout-duration: 0s
  ```

  ```java
  @Service
  public class OrderService {
      
      @RateLimiter(name = "productService", fallbackMethod = "getProductFallback")
      public Product getProduct(Long productId) {
          return productClient.getProduct(productId);
      }
      
      public Product getProductFallback(Long productId, RequestNotPermitted e) {
          log.error("Rate limit exceeded for product: {}", productId);
          throw new TooManyRequestsException("Too many requests, please try again later");
      }
  }
  ```

  **Token Bucket Algorithm:**

  ```java
  @Component
  public class TokenBucketRateLimiter {
      
      private final int capacity;
      private final int refillRate;
      private int tokens;
      private long lastRefillTime;
      
      public TokenBucketRateLimiter(int capacity, int refillRate) {
          this.capacity = capacity;
          this.refillRate = refillRate;
          this.tokens = capacity;
          this.lastRefillTime = System.currentTimeMillis();
      }
      
      public synchronized boolean tryConsume() {
          refill();
          
          if (tokens > 0) {
              tokens--;
              return true;
          }
          
          return false;
      }
      
      private void refill() {
          long now = System.currentTimeMillis();
          long timePassed = now - lastRefillTime;
          int tokensToAdd = (int) (timePassed * refillRate / 1000);
          
          if (tokensToAdd > 0) {
              tokens = Math.min(capacity, tokens + tokensToAdd);
              lastRefillTime = now;
          }
      }
  }
  ```

  **Redis-based Rate Limiter:**

  ```java
  @Component
  public class RedisRateLimiter {
      
      @Autowired
      private RedisTemplate<String, String> redisTemplate;
      
      public boolean isAllowed(String key, int maxRequests, Duration window) {
          String redisKey = "rate_limit:" + key;
          long now = System.currentTimeMillis();
          long windowStart = now - window.toMillis();
          
          // Remove old entries
          redisTemplate.opsForZSet().removeRangeByScore(redisKey, 0, windowStart);
          
          // Count requests in window
          Long count = redisTemplate.opsForZSet().count(redisKey, windowStart, now);
          
          if (count < maxRequests) {
              // Add current request
              redisTemplate.opsForZSet().add(redisKey, String.valueOf(now), now);
              redisTemplate.expire(redisKey, window);
              return true;
          }
          
          return false;
      }
  }
  
  @Service
  public class OrderService {
      
      @Autowired
      private RedisRateLimiter rateLimiter;
      
      public Product getProduct(Long productId, String userId) {
          if (!rateLimiter.isAllowed(userId, 10, Duration.ofMinutes(1))) {
              throw new TooManyRequestsException("Rate limit exceeded");
          }
          
          return productClient.getProduct(productId);
      }
  }
  ```

</details>

<details>
  <summary>Combining Patterns</summary>
  <br/>

  **Multiple resilience patterns together:**

  ```yaml
  # application.yml
  resilience4j:
    circuitbreaker:
      instances:
        productService:
          sliding-window-size: 10
          failure-rate-threshold: 50
          wait-duration-in-open-state: 10s
    
    retry:
      instances:
        productService:
          max-attempts: 3
          wait-duration: 1s
    
    bulkhead:
      instances:
        productService:
          max-concurrent-calls: 10
    
    timelimiter:
      instances:
        productService:
          timeout-duration: 2s
  ```

  ```java
  @Service
  public class OrderService {
      
      // Order matters: TimeLimiter → CircuitBreaker → Retry → Bulkhead
      @TimeLimiter(name = "productService")
      @CircuitBreaker(name = "productService", fallbackMethod = "getProductFallback")
      @Retry(name = "productService")
      @Bulkhead(name = "productService")
      public CompletableFuture<Product> getProduct(Long productId) {
          return CompletableFuture.supplyAsync(() -> {
              log.info("Calling product service for product: {}", productId);
              return productClient.getProduct(productId);
          });
      }
      
      public CompletableFuture<Product> getProductFallback(Long productId, Throwable throwable) {
          log.error("Fallback triggered for product: {}", productId, throwable);
          return CompletableFuture.completedFuture(getDefaultProduct(productId));
      }
      
      private Product getDefaultProduct(Long productId) {
          return Product.builder()
              .id(productId)
              .name("Product Unavailable")
              .price(BigDecimal.ZERO)
              .available(false)
              .build();
      }
  }
  ```

  **Resilience4j Decorators:**

  ```java
  @Service
  public class OrderService {
      
      @Autowired
      private CircuitBreakerRegistry circuitBreakerRegistry;
      
      @Autowired
      private RetryRegistry retryRegistry;
      
      @Autowired
      private BulkheadRegistry bulkheadRegistry;
      
      public Product getProduct(Long productId) {
          Supplier<Product> supplier = () -> productClient.getProduct(productId);
          
          // Decorate with multiple patterns
          Supplier<Product> decoratedSupplier = Decorators.ofSupplier(supplier)
              .withCircuitBreaker(circuitBreakerRegistry.circuitBreaker("productService"))
              .withRetry(retryRegistry.retry("productService"))
              .withBulkhead(bulkheadRegistry.bulkhead("productService"))
              .withFallback(Arrays.asList(Exception.class), 
                  e -> getDefaultProduct(productId))
              .decorate();
          
          return decoratedSupplier.get();
      }
  }
  ```

</details>

<details>
  <summary>Best Practices</summary>
  <br/>

  ```java
  // ✅ DO: Use circuit breaker for external service calls
  @CircuitBreaker(name = "productService", fallbackMethod = "getProductFallback")
  public Product getProduct(Long productId) {
      return productClient.getProduct(productId);
  }
  
  // ✅ DO: Implement retry with exponential backoff
  @Retry(name = "productService")
  public Product getProduct(Long productId) {
      return productClient.getProduct(productId);
  }
  
  // ✅ DO: Use bulkhead to isolate resources
  @Bulkhead(name = "productService", type = Bulkhead.Type.THREADPOOL)
  public CompletableFuture<Product> getProductAsync(Long productId) {
      return CompletableFuture.supplyAsync(() -> productClient.getProduct(productId));
  }
  
  // ✅ DO: Set timeouts for all external calls
  @TimeLimiter(name = "productService")
  public CompletableFuture<Product> getProduct(Long productId) {
      return CompletableFuture.supplyAsync(() -> productClient.getProduct(productId));
  }
  
  // ✅ DO: Implement rate limiting
  @RateLimiter(name = "productService")
  public Product getProduct(Long productId) {
      return productClient.getProduct(productId);
  }
  
  // ✅ DO: Provide meaningful fallback responses
  public Product getProductFallback(Long productId, Throwable throwable) {
      log.error("Fallback for product: {}", productId, throwable);
      return getCachedProduct(productId)
          .orElse(getDefaultProduct(productId));
  }
  
  // ✅ DO: Monitor circuit breaker metrics
  @Component
  public class CircuitBreakerMetrics {
      @Autowired
      private CircuitBreakerRegistry registry;
      
      @Scheduled(fixedRate = 60000)
      public void logMetrics() {
          registry.getAllCircuitBreakers().forEach(cb -> {
              CircuitBreaker.Metrics metrics = cb.getMetrics();
              log.info("Circuit Breaker: {}, State: {}, Failure Rate: {}%",
                  cb.getName(),
                  cb.getState(),
                  metrics.getFailureRate());
          });
      }
  }
  
  // ✅ DO: Combine patterns appropriately
  @TimeLimiter(name = "productService")
  @CircuitBreaker(name = "productService", fallbackMethod = "fallback")
  @Retry(name = "productService")
  public CompletableFuture<Product> getProduct(Long productId) {
      return CompletableFuture.supplyAsync(() -> productClient.getProduct(productId));
  }
  
  // ❌ DON'T: Use retry without circuit breaker
  // Can overwhelm failing service
  
  // ❌ DON'T: Set timeout longer than circuit breaker wait duration
  // Circuit breaker should open before timeout
  
  // ❌ DON'T: Ignore fallback responses
  // Always provide meaningful fallback
  
  // ❌ DON'T: Use same thread pool for all services
  // Use bulkhead to isolate resources
  
  // ✅ DO: Test resilience patterns
  @Test
  public void testCircuitBreaker() {
      // Simulate failures
      for (int i = 0; i < 10; i++) {
          try {
              orderService.getProduct(1L);
          } catch (Exception e) {
              // Expected
          }
      }
      
      // Circuit should be open
      CircuitBreaker cb = circuitBreakerRegistry.circuitBreaker("productService");
      assertEquals(CircuitBreaker.State.OPEN, cb.getState());
  }
  ```

  **Summary:**
  + **Circuit Breaker** - Prevent cascading failures
  + **Retry** - Automatically retry failed operations
  + **Bulkhead** - Isolate resources to prevent cascading failures
  + **Timeout** - Prevent indefinite waiting
  + **Rate Limiting** - Control request rate
  + **Combine patterns** for comprehensive resilience
  + **Monitor metrics** to track resilience
  + **Test patterns** to ensure they work correctly
  + **Provide fallbacks** for graceful degradation

</details>

## Distributed Systems

<details>
  <summary>CAP Theorem</summary>
  <br/>

  **CAP Theorem** states that a distributed system can only guarantee 2 out of 3 properties:

  **C - Consistency:** All nodes see the same data at the same time
  **A - Availability:** Every request receives a response (success or failure)
  **P - Partition Tolerance:** System continues to operate despite network partitions

  **Trade-offs:**

  ```
  ┌─────────────────────────────────────┐
  │         CAP Theorem Triangle        │
  │                                     │
  │              Consistency            │
  │                  /\                 │
  │                 /  \                │
  │                /    \               │
  │               /  CP  \              │
  │              /        \             │
  │             /          \            │
  │            /    CA      \           │
  │           /              \          │
  │          /                \         │
  │         /                  \        │
  │        /                    \       │
  │       /         AP           \      │
  │      /________________________\     │
  │  Availability          Partition   │
  │                        Tolerance    │
  └─────────────────────────────────────┘
  ```

  **CA (Consistency + Availability):**
  - Traditional RDBMS (single node)
  - Not partition tolerant
  - Example: PostgreSQL, MySQL (single instance)

  **CP (Consistency + Partition Tolerance):**
  - Sacrifices availability during partition
  - Strong consistency guaranteed
  - Examples: MongoDB, HBase, Redis, Zookeeper

  **AP (Availability + Partition Tolerance):**
  - Sacrifices consistency during partition
  - Eventually consistent
  - Examples: Cassandra, DynamoDB, CouchDB

  **Real-world example:**

  ```java
  // CP System (MongoDB) - Consistency over Availability
  @Service
  public class OrderService {
      
      @Autowired
      private MongoTemplate mongoTemplate;
      
      public Order createOrder(OrderRequest request) {
          // Write to primary node
          // If primary is down, write fails (unavailable)
          // But data is always consistent
          Order order = new Order(request);
          return mongoTemplate.save(order);
      }
  }
  
  // AP System (Cassandra) - Availability over Consistency
  @Service
  public class OrderService {
      
      @Autowired
      private CassandraTemplate cassandraTemplate;
      
      public Order createOrder(OrderRequest request) {
          // Write to any available node
          // Always succeeds (available)
          // But may read stale data temporarily
          Order order = new Order(request);
          return cassandraTemplate.insert(order);
      }
  }
  ```

  **Choosing the right system:**

  | Use Case | Choice | Reason |
  |----------|--------|--------|
  | Banking transactions | CP | Consistency critical |
  | Social media posts | AP | Availability critical |
  | E-commerce inventory | CP | Prevent overselling |
  | User profiles | AP | Stale data acceptable |
  | Shopping cart | AP | Always available |
  | Payment processing | CP | Consistency required |

</details>

<details>
  <summary>Eventual Consistency</summary>
  <br/>

  **Eventual Consistency:** System will become consistent over time, but may be temporarily inconsistent.

  **BASE Properties (vs ACID):**

  **B**asically **A**vailable: System appears to work most of the time
  **S**oft state: State may change over time without input
  **E**ventual consistency: System will become consistent eventually

  **Example scenario:**

  ```
  Time: T0
  User updates profile in US datacenter
  ┌──────────────┐
  │  US Region   │  Name: "John Doe"
  └──────────────┘
  
  Time: T1 (immediately after)
  ┌──────────────┐     ┌──────────────┐
  │  US Region   │     │  EU Region   │
  │ Name: "John" │     │ Name: "John  │
  │              │────▶│  Doe" (old)  │
  └──────────────┘     └──────────────┘
  Replication in progress...
  
  Time: T2 (after replication)
  ┌──────────────┐     ┌──────────────┐
  │  US Region   │     │  EU Region   │
  │ Name: "John" │     │ Name: "John" │
  └──────────────┘     └──────────────┘
  Eventually consistent!
  ```

  **Implementation patterns:**

  **1. Read-Your-Writes Consistency:**

  ```java
  @Service
  public class UserService {
      
      @Autowired
      private UserRepository primaryRepo;
      
      @Autowired
      private UserRepository replicaRepo;
      
      public User updateUser(Long userId, UserRequest request) {
          // Write to primary
          User user = primaryRepo.save(new User(request));
          
          // Store write timestamp
          cache.put("user:" + userId + ":write-time", System.currentTimeMillis());
          
          return user;
      }
      
      public User getUser(Long userId) {
          Long writeTime = cache.get("user:" + userId + ":write-time");
          
          if (writeTime != null && 
              System.currentTimeMillis() - writeTime < 5000) {
              // Recent write, read from primary
              return primaryRepo.findById(userId);
          }
          
          // Old data, read from replica (faster)
          return replicaRepo.findById(userId);
      }
  }
  ```

  **2. Version Vectors:**

  ```java
  @Entity
  public class User {
      @Id
      private Long id;
      
      private String name;
      
      @ElementCollection
      private Map<String, Long> versionVector = new HashMap<>();
      // {"us-datacenter": 5, "eu-datacenter": 3}
      
      public boolean isNewerThan(User other) {
          for (Map.Entry<String, Long> entry : this.versionVector.entrySet()) {
              Long otherVersion = other.versionVector.get(entry.getKey());
              if (otherVersion == null || entry.getValue() > otherVersion) {
                  return true;
              }
          }
          return false;
      }
  }
  ```

  **3. Conflict Resolution:**

  ```java
  @Service
  public class ConflictResolver {
      
      public User resolveConflict(User version1, User version2) {
          // Last-Write-Wins (LWW)
          if (version1.getUpdatedAt().isAfter(version2.getUpdatedAt())) {
              return version1;
          }
          return version2;
      }
      
      public User mergeConflict(User version1, User version2) {
          // Custom merge logic
          User merged = new User();
          merged.setId(version1.getId());
          
          // Take newer name
          merged.setName(
              version1.getUpdatedAt().isAfter(version2.getUpdatedAt())
                  ? version1.getName()
                  : version2.getName()
          );
          
          // Merge tags (union)
          Set<String> allTags = new HashSet<>();
          allTags.addAll(version1.getTags());
          allTags.addAll(version2.getTags());
          merged.setTags(allTags);
          
          return merged;
      }
  }
  ```

</details>

<details>
  <summary>Distributed Transactions - Saga Pattern</summary>
  <br/>

  **Saga Pattern:** Manages distributed transactions using a sequence of local transactions.

  **Problem:**

  ```java
  // This doesn't work in microservices!
  @Transactional
  public void createOrder(OrderRequest request) {
      orderService.createOrder(request);      // Order Service DB
      inventoryService.reserveStock(request); // Inventory Service DB
      paymentService.processPayment(request); // Payment Service DB
      // If payment fails, how to rollback order and inventory?
  }
  ```

  **Saga Types:**

  **1. Choreography-based Saga (Event-driven):**

  ```java
  // Order Service
  @Service
  public class OrderService {
      
      @Autowired
      private KafkaTemplate<String, OrderEvent> kafkaTemplate;
      
      public Order createOrder(OrderRequest request) {
          // Step 1: Create order
          Order order = orderRepository.save(new Order(request));
          
          // Publish event
          kafkaTemplate.send("order-events", 
              new OrderCreatedEvent(order.getId(), order.getItems()));
          
          return order;
      }
      
      @KafkaListener(topics = "payment-events")
      public void handlePaymentFailed(PaymentFailedEvent event) {
          // Compensating transaction
          Order order = orderRepository.findById(event.getOrderId());
          order.setStatus(OrderStatus.CANCELLED);
          orderRepository.save(order);
      }
  }
  
  // Inventory Service
  @Service
  public class InventoryService {
      
      @KafkaListener(topics = "order-events")
      public void handleOrderCreated(OrderCreatedEvent event) {
          try {
              // Step 2: Reserve stock
              inventoryRepository.reserveStock(event.getItems());
              
              // Publish success event
              kafkaTemplate.send("inventory-events",
                  new StockReservedEvent(event.getOrderId()));
          } catch (InsufficientStockException e) {
              // Publish failure event
              kafkaTemplate.send("inventory-events",
                  new StockReservationFailedEvent(event.getOrderId()));
          }
      }
      
      @KafkaListener(topics = "payment-events")
      public void handlePaymentFailed(PaymentFailedEvent event) {
          // Compensating transaction
          inventoryRepository.releaseStock(event.getOrderId());
      }
  }
  
  // Payment Service
  @Service
  public class PaymentService {
      
      @KafkaListener(topics = "inventory-events")
      public void handleStockReserved(StockReservedEvent event) {
          try {
              // Step 3: Process payment
              paymentRepository.processPayment(event.getOrderId());
              
              // Publish success event
              kafkaTemplate.send("payment-events",
                  new PaymentCompletedEvent(event.getOrderId()));
          } catch (PaymentException e) {
              // Publish failure event
              kafkaTemplate.send("payment-events",
                  new PaymentFailedEvent(event.getOrderId()));
          }
      }
  }
  ```

  **2. Orchestration-based Saga (Centralized):**

  ```java
  // Saga Orchestrator
  @Service
  public class OrderSagaOrchestrator {
      
      @Autowired
      private OrderService orderService;
      
      @Autowired
      private InventoryService inventoryService;
      
      @Autowired
      private PaymentService paymentService;
      
      public void executeOrderSaga(OrderRequest request) {
          String sagaId = UUID.randomUUID().toString();
          
          try {
              // Step 1: Create order
              Order order = orderService.createOrder(request);
              saveSagaState(sagaId, "ORDER_CREATED", order.getId());
              
              // Step 2: Reserve stock
              inventoryService.reserveStock(order.getId(), request.getItems());
              saveSagaState(sagaId, "STOCK_RESERVED", order.getId());
              
              // Step 3: Process payment
              paymentService.processPayment(order.getId(), request.getPayment());
              saveSagaState(sagaId, "PAYMENT_COMPLETED", order.getId());
              
              // Success!
              orderService.completeOrder(order.getId());
              
          } catch (InsufficientStockException e) {
              // Compensate: Cancel order
              compensateOrderCreation(sagaId);
              
          } catch (PaymentException e) {
              // Compensate: Release stock and cancel order
              compensateStockReservation(sagaId);
              compensateOrderCreation(sagaId);
          }
      }
      
      private void compensateOrderCreation(String sagaId) {
          SagaState state = getSagaState(sagaId);
          orderService.cancelOrder(state.getOrderId());
      }
      
      private void compensateStockReservation(String sagaId) {
          SagaState state = getSagaState(sagaId);
          inventoryService.releaseStock(state.getOrderId());
      }
  }
  ```

  **Saga State Machine:**

  ```java
  @Entity
  public class SagaState {
      @Id
      private String sagaId;
      
      private String currentStep;
      private SagaStatus status;
      private Long orderId;
      private LocalDateTime createdAt;
      private LocalDateTime updatedAt;
      
      @ElementCollection
      private List<String> completedSteps = new ArrayList<>();
      
      @ElementCollection
      private List<String> compensatedSteps = new ArrayList<>();
  }
  
  public enum SagaStatus {
      STARTED,
      ORDER_CREATED,
      STOCK_RESERVED,
      PAYMENT_COMPLETED,
      COMPLETED,
      COMPENSATING,
      COMPENSATED,
      FAILED
  }
  ```

</details>

<details>
  <summary>Event Sourcing</summary>
  <br/>

  **Event Sourcing:** Store all changes as a sequence of events instead of current state.

  **Traditional approach:**

  ```java
  // Store current state only
  @Entity
  public class Account {
      @Id
      private Long id;
      private BigDecimal balance;  // Current balance only
  }
  
  // Lost history: How did we get to this balance?
  ```

  **Event Sourcing approach:**

  ```java
  // Store all events
  @Entity
  public class AccountEvent {
      @Id
      private Long id;
      
      private Long accountId;
      private String eventType;  // CREATED, DEPOSITED, WITHDRAWN
      private BigDecimal amount;
      private LocalDateTime timestamp;
      private Long version;
  }
  
  // Events:
  // 1. AccountCreated(accountId=1, balance=0)
  // 2. MoneyDeposited(accountId=1, amount=100)
  // 3. MoneyWithdrawn(accountId=1, amount=30)
  // 4. MoneyDeposited(accountId=1, amount=50)
  // Current balance = 0 + 100 - 30 + 50 = 120
  ```

  **Implementation:**

  ```java
  // Events
  public interface AccountEvent {
      Long getAccountId();
      LocalDateTime getTimestamp();
  }
  
  public class AccountCreatedEvent implements AccountEvent {
      private Long accountId;
      private String owner;
      private LocalDateTime timestamp;
  }
  
  public class MoneyDepositedEvent implements AccountEvent {
      private Long accountId;
      private BigDecimal amount;
      private LocalDateTime timestamp;
  }
  
  public class MoneyWithdrawnEvent implements AccountEvent {
      private Long accountId;
      private BigDecimal amount;
      private LocalDateTime timestamp;
  }
  
  // Event Store
  @Repository
  public interface EventStore {
      void save(AccountEvent event);
      List<AccountEvent> getEvents(Long accountId);
      List<AccountEvent> getEventsSince(Long accountId, Long version);
  }
  
  // Aggregate (reconstructed from events)
  public class Account {
      private Long id;
      private String owner;
      private BigDecimal balance;
      private Long version;
      
      public static Account fromEvents(List<AccountEvent> events) {
          Account account = new Account();
          
          for (AccountEvent event : events) {
              account.apply(event);
          }
          
          return account;
      }
      
      private void apply(AccountEvent event) {
          if (event instanceof AccountCreatedEvent) {
              AccountCreatedEvent e = (AccountCreatedEvent) event;
              this.id = e.getAccountId();
              this.owner = e.getOwner();
              this.balance = BigDecimal.ZERO;
              this.version = 0L;
              
          } else if (event instanceof MoneyDepositedEvent) {
              MoneyDepositedEvent e = (MoneyDepositedEvent) event;
              this.balance = this.balance.add(e.getAmount());
              this.version++;
              
          } else if (event instanceof MoneyWithdrawnEvent) {
              MoneyWithdrawnEvent e = (MoneyWithdrawnEvent) event;
              this.balance = this.balance.subtract(e.getAmount());
              this.version++;
          }
      }
  }
  
  // Service
  @Service
  public class AccountService {
      
      @Autowired
      private EventStore eventStore;
      
      public void deposit(Long accountId, BigDecimal amount) {
          // Load events
          List<AccountEvent> events = eventStore.getEvents(accountId);
          
          // Reconstruct current state
          Account account = Account.fromEvents(events);
          
          // Create new event
          MoneyDepositedEvent event = new MoneyDepositedEvent(
              accountId, amount, LocalDateTime.now()
          );
          
          // Save event
          eventStore.save(event);
      }
      
      public Account getAccount(Long accountId) {
          List<AccountEvent> events = eventStore.getEvents(accountId);
          return Account.fromEvents(events);
      }
      
      public Account getAccountAtTime(Long accountId, LocalDateTime timestamp) {
          List<AccountEvent> events = eventStore.getEvents(accountId)
              .stream()
              .filter(e -> e.getTimestamp().isBefore(timestamp))
              .collect(Collectors.toList());
          
          return Account.fromEvents(events);
      }
  }
  ```

  **Benefits:**
  + Complete audit trail
  + Time travel (reconstruct state at any point)
  + Event replay
  + Debug and troubleshoot
  + Analytics and reporting

  **Drawbacks:**
  + Complex queries
  + Storage overhead
  + Performance (need snapshots)

  **Snapshots (optimization):**

  ```java
  @Entity
  public class AccountSnapshot {
      @Id
      private Long id;
      
      private Long accountId;
      private BigDecimal balance;
      private Long version;
      private LocalDateTime timestamp;
  }
  
  @Service
  public class AccountService {
      
      public Account getAccount(Long accountId) {
          // Load latest snapshot
          AccountSnapshot snapshot = snapshotRepository
              .findLatestByAccountId(accountId);
          
          Account account;
          if (snapshot != null) {
              account = Account.fromSnapshot(snapshot);
              
              // Load events since snapshot
              List<AccountEvent> events = eventStore
                  .getEventsSince(accountId, snapshot.getVersion());
              
              // Apply events
              events.forEach(account::apply);
          } else {
              // No snapshot, load all events
              List<AccountEvent> events = eventStore.getEvents(accountId);
              account = Account.fromEvents(events);
          }
          
          return account;
      }
      
      @Scheduled(cron = "0 0 * * * *")  // Every hour
      public void createSnapshots() {
          // Create snapshots for active accounts
          List<Long> accountIds = accountRepository.findActiveAccountIds();
          
          for (Long accountId : accountIds) {
              Account account = getAccount(accountId);
              
              AccountSnapshot snapshot = new AccountSnapshot();
              snapshot.setAccountId(accountId);
              snapshot.setBalance(account.getBalance());
              snapshot.setVersion(account.getVersion());
              snapshot.setTimestamp(LocalDateTime.now());
              
              snapshotRepository.save(snapshot);
          }
      }
  }
  ```

</details>

<details>
  <summary>CQRS (Command Query Responsibility Segregation)</summary>
  <br/>

  **CQRS:** Separate read and write operations into different models.

  **Traditional approach:**

  ```java
  // Same model for read and write
  @Entity
  public class Order {
      @Id
      private Long id;
      private Long customerId;
      private List<OrderItem> items;
      private BigDecimal total;
      private OrderStatus status;
  }
  
  @Service
  public class OrderService {
      // Write
      public Order createOrder(OrderRequest request) {
          return orderRepository.save(new Order(request));
      }
      
      // Read
      public Order getOrder(Long id) {
          return orderRepository.findById(id);
      }
      
      // Complex read
      public List<OrderSummary> getOrderSummaries(Long customerId) {
          // Complex joins and aggregations
          return orderRepository.findOrderSummaries(customerId);
      }
  }
  ```

  **CQRS approach:**

  ```java
  // Write Model (Command)
  @Entity
  @Table(name = "orders")
  public class Order {
      @Id
      private Long id;
      private Long customerId;
      
      @OneToMany
      private List<OrderItem> items;
      
      private BigDecimal total;
      private OrderStatus status;
      private LocalDateTime createdAt;
  }
  
  // Read Model (Query) - Denormalized
  @Entity
  @Table(name = "order_summaries")
  public class OrderSummary {
      @Id
      private Long id;
      
      private Long orderId;
      private Long customerId;
      private String customerName;
      private Integer itemCount;
      private BigDecimal total;
      private String status;
      private LocalDateTime createdAt;
  }
  
  // Command Service (Write)
  @Service
  public class OrderCommandService {
      
      @Autowired
      private OrderRepository orderRepository;
      
      @Autowired
      private EventPublisher eventPublisher;
      
      public Order createOrder(CreateOrderCommand command) {
          Order order = new Order();
          order.setCustomerId(command.getCustomerId());
          order.setItems(command.getItems());
          order.setTotal(calculateTotal(command.getItems()));
          order.setStatus(OrderStatus.PENDING);
          
          order = orderRepository.save(order);
          
          // Publish event for read model update
          eventPublisher.publish(new OrderCreatedEvent(order));
          
          return order;
      }
      
      public void updateOrderStatus(UpdateOrderStatusCommand command) {
          Order order = orderRepository.findById(command.getOrderId());
          order.setStatus(command.getStatus());
          orderRepository.save(order);
          
          eventPublisher.publish(new OrderStatusUpdatedEvent(order));
      }
  }
  
  // Query Service (Read)
  @Service
  public class OrderQueryService {
      
      @Autowired
      private OrderSummaryRepository orderSummaryRepository;
      
      public OrderSummary getOrderSummary(Long orderId) {
          return orderSummaryRepository.findByOrderId(orderId);
      }
      
      public List<OrderSummary> getCustomerOrders(Long customerId) {
          return orderSummaryRepository.findByCustomerId(customerId);
      }
      
      public Page<OrderSummary> searchOrders(OrderSearchCriteria criteria, Pageable pageable) {
          return orderSummaryRepository.search(criteria, pageable);
      }
  }
  
  // Event Handler (Update Read Model)
  @Service
  public class OrderEventHandler {
      
      @Autowired
      private OrderSummaryRepository orderSummaryRepository;
      
      @Autowired
      private CustomerService customerService;
      
      @EventListener
      public void handleOrderCreated(OrderCreatedEvent event) {
          Order order = event.getOrder();
          Customer customer = customerService.getCustomer(order.getCustomerId());
          
          OrderSummary summary = new OrderSummary();
          summary.setOrderId(order.getId());
          summary.setCustomerId(order.getCustomerId());
          summary.setCustomerName(customer.getName());
          summary.setItemCount(order.getItems().size());
          summary.setTotal(order.getTotal());
          summary.setStatus(order.getStatus().name());
          summary.setCreatedAt(order.getCreatedAt());
          
          orderSummaryRepository.save(summary);
      }
      
      @EventListener
      public void handleOrderStatusUpdated(OrderStatusUpdatedEvent event) {
          OrderSummary summary = orderSummaryRepository
              .findByOrderId(event.getOrder().getId());
          
          summary.setStatus(event.getOrder().getStatus().name());
          orderSummaryRepository.save(summary);
      }
  }
  ```

  **CQRS with separate databases:**

  ```java
  @Configuration
  public class DatabaseConfig {
      
      // Write Database (PostgreSQL)
      @Bean
      @Primary
      public DataSource writeDataSource() {
          return DataSourceBuilder.create()
              .url("jdbc:postgresql://write-db:5432/orders")
              .build();
      }
      
      // Read Database (MongoDB for fast queries)
      @Bean
      public MongoTemplate readMongoTemplate() {
          return new MongoTemplate(
              new MongoClient("read-db", 27017),
              "orders_read"
          );
      }
  }
  
  @Service
  public class OrderQueryService {
      
      @Autowired
      private MongoTemplate mongoTemplate;
      
      public List<OrderSummary> getCustomerOrders(Long customerId) {
          Query query = new Query(Criteria.where("customerId").is(customerId));
          return mongoTemplate.find(query, OrderSummary.class);
      }
      
      public List<OrderSummary> searchOrders(String keyword) {
          // Full-text search in MongoDB
          TextCriteria criteria = TextCriteria.forDefaultLanguage()
              .matchingAny(keyword);
          Query query = TextQuery.queryText(criteria);
          return mongoTemplate.find(query, OrderSummary.class);
      }
  }
  ```

  **Benefits:**
  + Optimized read and write models
  + Scale read and write independently
  + Complex queries without affecting writes
  + Multiple read models for different use cases

  **Drawbacks:**
  + Increased complexity
  + Eventual consistency
  + Data synchronization overhead

</details>

<details>
  <summary>Best Practices</summary>
  <br/>

  ```java
  // ✅ DO: Understand CAP trade-offs
  // Choose CP for financial transactions
  // Choose AP for social media, caching
  
  // ✅ DO: Implement idempotency for eventual consistency
  @Service
  public class OrderService {
      public void processOrder(String idempotencyKey, OrderRequest request) {
          if (processedKeys.contains(idempotencyKey)) {
              return;  // Already processed
          }
          
          // Process order
          Order order = createOrder(request);
          
          // Mark as processed
          processedKeys.add(idempotencyKey);
      }
  }
  
  // ✅ DO: Use Saga pattern for distributed transactions
  // Choreography for simple flows
  // Orchestration for complex flows
  
  // ✅ DO: Implement compensating transactions
  @Service
  public class SagaOrchestrator {
      public void executeOrderSaga(OrderRequest request) {
          try {
              orderService.createOrder(request);
              inventoryService.reserveStock(request);
              paymentService.processPayment(request);
          } catch (Exception e) {
              // Compensate
              inventoryService.releaseStock(request);
              orderService.cancelOrder(request);
          }
      }
  }
  
  // ✅ DO: Use Event Sourcing for audit trail
  @Service
  public class AccountService {
      public void deposit(Long accountId, BigDecimal amount) {
          MoneyDepositedEvent event = new MoneyDepositedEvent(accountId, amount);
          eventStore.save(event);
      }
  }
  
  // ✅ DO: Create snapshots for Event Sourcing performance
  @Scheduled(cron = "0 0 * * * *")
  public void createSnapshots() {
      // Create snapshots every hour
  }
  
  // ✅ DO: Use CQRS for complex read requirements
  // Separate write model (normalized)
  // Separate read model (denormalized)
  
  // ✅ DO: Handle eventual consistency in UI
  // Show "Processing..." message
  // Poll for updates
  // Use WebSockets for real-time updates
  
  // ✅ DO: Implement conflict resolution
  @Service
  public class ConflictResolver {
      public User resolve(User v1, User v2) {
          // Last-Write-Wins, Custom merge, etc.
          return v1.getUpdatedAt().isAfter(v2.getUpdatedAt()) ? v1 : v2;
      }
  }
  
  // ❌ DON'T: Expect strong consistency in distributed systems
  // Embrace eventual consistency
  
  // ❌ DON'T: Use distributed transactions (2PC) in microservices
  // Use Saga pattern instead
  
  // ❌ DON'T: Store only current state if audit trail is needed
  // Use Event Sourcing
  
  // ✅ DO: Monitor saga execution
  @Component
  public class SagaMonitor {
      @Scheduled(fixedRate = 60000)
      public void checkStuckSagas() {
          List<SagaState> stuck = sagaRepository
              .findByStatusAndCreatedAtBefore(
                  SagaStatus.STARTED,
                  LocalDateTime.now().minusMinutes(5)
              );
          
          stuck.forEach(saga -> {
              log.warn("Stuck saga detected: {}", saga.getSagaId());
              // Trigger compensation or retry
          });
      }
  }
  ```

  **Summary:**
  + **CAP Theorem** - Choose 2 of 3: Consistency, Availability, Partition Tolerance
  + **Eventual Consistency** - System becomes consistent over time (BASE properties)
  + **Saga Pattern** - Manage distributed transactions with compensating transactions
  + **Event Sourcing** - Store all changes as events for complete audit trail
  + **CQRS** - Separate read and write models for optimization
  + Use **Choreography** for simple sagas, **Orchestration** for complex ones
  + Implement **idempotency** for reliable event processing
  + Create **snapshots** for Event Sourcing performance
  + **Monitor** saga execution and handle stuck sagas
  + Embrace **eventual consistency** in distributed systems

</details>

## Messaging

<details>
  <summary>Apache Kafka - Topics and Partitions</summary>
  <br/>

  **Apache Kafka** is a distributed event streaming platform for high-throughput, fault-tolerant messaging.

  **Core Concepts:**

  ```
  ┌─────────────────────────────────────────────────────────┐
  │                    Kafka Cluster                        │
  │                                                         │
  │  Topic: "orders"                                        │
  │  ┌─────────────┬─────────────┬─────────────┐          │
  │  │ Partition 0 │ Partition 1 │ Partition 2 │          │
  │  ├─────────────┼─────────────┼─────────────┤          │
  │  │ [M1][M2][M3]│ [M4][M5][M6]│ [M7][M8][M9]│          │
  │  │ offset: 0-2 │ offset: 0-2 │ offset: 0-2 │          │
  │  └─────────────┴─────────────┴─────────────┘          │
  │                                                         │
  │  Replication Factor: 3                                  │
  │  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐   │
  │  │  Broker 1   │  │  Broker 2   │  │  Broker 3   │   │
  │  │  (Leader)   │  │  (Replica)  │  │  (Replica)  │   │
  │  └─────────────┘  └─────────────┘  └─────────────┘   │
  └─────────────────────────────────────────────────────────┘
  ```

  **Topics:**
  - Logical channel for messages
  - Similar to database table
  - Messages are immutable
  - Retained for configured time (default 7 days)

  **Partitions:**
  - Topics split into partitions for parallelism
  - Each partition is ordered sequence
  - Messages in partition have unique offset
  - Partitions distributed across brokers

  **Configuration:**

  ```yaml
  # application.yml
  spring:
    kafka:
      bootstrap-servers: localhost:9092
      producer:
        key-serializer: org.apache.kafka.common.serialization.StringSerializer
        value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
        acks: all  # Wait for all replicas
        retries: 3
        properties:
          linger.ms: 10  # Batch messages for 10ms
          batch.size: 16384  # 16KB batch size
          compression.type: snappy
      consumer:
        key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
        value-deserializer: org.springframework.kafka.support.serializer.JsonDeserializer
        group-id: order-service-group
        auto-offset-reset: earliest
        enable-auto-commit: false
        properties:
          spring.json.trusted.packages: "*"
  ```

  **Creating Topics:**

  ```java
  @Configuration
  public class KafkaTopicConfig {
      
      @Bean
      public NewTopic orderTopic() {
          return TopicBuilder.name("orders")
              .partitions(3)  // 3 partitions for parallelism
              .replicas(3)    // 3 replicas for fault tolerance
              .config(TopicConfig.RETENTION_MS_CONFIG, "604800000")  // 7 days
              .config(TopicConfig.COMPRESSION_TYPE_CONFIG, "snappy")
              .build();
      }
      
      @Bean
      public NewTopic orderEventsTopic() {
          return TopicBuilder.name("order-events")
              .partitions(5)
              .replicas(2)
              .compact()  // Log compaction (keep latest per key)
              .build();
      }
  }
  ```

  **Partition Key Strategy:**

  ```java
  @Service
  public class OrderProducer {
      
      @Autowired
      private KafkaTemplate<String, OrderEvent> kafkaTemplate;
      
      public void sendOrderEvent(OrderEvent event) {
          // Partition by customer ID - all orders from same customer go to same partition
          String key = String.valueOf(event.getCustomerId());
          
          kafkaTemplate.send("orders", key, event);
          // Messages with same key always go to same partition (ordering guaranteed)
      }
      
      public void sendWithCustomPartition(OrderEvent event) {
          // Custom partition selection
          int partition = event.getOrderId() % 3;  // Round-robin across 3 partitions
          
          kafkaTemplate.send("orders", partition, 
              String.valueOf(event.getCustomerId()), event);
      }
  }
  ```

</details>

<details>
  <summary>Apache Kafka - Producers</summary>
  <br/>

  **Producers** send messages to Kafka topics.

  **Basic Producer:**

  ```java
  @Service
  @Slf4j
  public class OrderProducer {
      
      @Autowired
      private KafkaTemplate<String, OrderEvent> kafkaTemplate;
      
      public void sendOrder(OrderEvent event) {
          kafkaTemplate.send("orders", event);
      }
      
      public void sendOrderWithCallback(OrderEvent event) {
          kafkaTemplate.send("orders", event)
              .addCallback(
                  result -> log.info("Message sent successfully: offset={}", 
                      result.getRecordMetadata().offset()),
                  ex -> log.error("Failed to send message", ex)
              );
      }
      
      public CompletableFuture<SendResult<String, OrderEvent>> sendOrderAsync(OrderEvent event) {
          return kafkaTemplate.send("orders", 
              String.valueOf(event.getCustomerId()), 
              event);
      }
  }
  ```

  **Producer with Headers:**

  ```java
  @Service
  public class OrderProducer {
      
      @Autowired
      private KafkaTemplate<String, OrderEvent> kafkaTemplate;
      
      public void sendWithHeaders(OrderEvent event) {
          ProducerRecord<String, OrderEvent> record = new ProducerRecord<>(
              "orders",
              String.valueOf(event.getCustomerId()),
              event
          );
          
          // Add headers
          record.headers().add("event-type", "ORDER_CREATED".getBytes());
          record.headers().add("source", "order-service".getBytes());
          record.headers().add("correlation-id", UUID.randomUUID().toString().getBytes());
          record.headers().add("timestamp", String.valueOf(System.currentTimeMillis()).getBytes());
          
          kafkaTemplate.send(record);
      }
  }
  ```

  **Transactional Producer:**

  ```java
  @Configuration
  public class KafkaProducerConfig {
      
      @Bean
      public ProducerFactory<String, OrderEvent> producerFactory() {
          Map<String, Object> config = new HashMap<>();
          config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
          config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
          config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
          
          // Transactional settings
          config.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "order-producer-1");
          config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
          config.put(ProducerConfig.ACKS_CONFIG, "all");
          
          return new DefaultKafkaProducerFactory<>(config);
      }
  }
  
  @Service
  public class OrderService {
      
      @Autowired
      private KafkaTemplate<String, OrderEvent> kafkaTemplate;
      
      @Autowired
      private OrderRepository orderRepository;
      
      @Transactional
      public void createOrder(OrderRequest request) {
          // Save to database
          Order order = orderRepository.save(new Order(request));
          
          // Send to Kafka (transactional)
          kafkaTemplate.executeInTransaction(ops -> {
              ops.send("orders", new OrderCreatedEvent(order));
              ops.send("notifications", new NotificationEvent(order));
              return true;
          });
          
          // Both DB and Kafka commit together or rollback together
      }
  }
  ```

  **Batch Producer:**

  ```java
  @Service
  public class BatchOrderProducer {
      
      @Autowired
      private KafkaTemplate<String, OrderEvent> kafkaTemplate;
      
      public void sendBatch(List<OrderEvent> events) {
          List<ProducerRecord<String, OrderEvent>> records = events.stream()
              .map(event -> new ProducerRecord<String, OrderEvent>(
                  "orders",
                  String.valueOf(event.getCustomerId()),
                  event
              ))
              .collect(Collectors.toList());
          
          // Send all records
          records.forEach(kafkaTemplate::send);
          
          // Flush to ensure all sent
          kafkaTemplate.flush();
      }
  }
  ```

  **Producer Configuration Options:**

  ```java
  @Configuration
  public class KafkaProducerConfig {
      
      @Bean
      public Map<String, Object> producerConfigs() {
          Map<String, Object> props = new HashMap<>();
          
          // Connection
          props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
          
          // Serialization
          props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
          props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
          
          // Reliability
          props.put(ProducerConfig.ACKS_CONFIG, "all");  // Wait for all replicas
          props.put(ProducerConfig.RETRIES_CONFIG, 3);
          props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
          
          // Performance
          props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);  // 16KB
          props.put(ProducerConfig.LINGER_MS_CONFIG, 10);  // Wait 10ms to batch
          props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
          props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);  // 32MB
          
          // Timeout
          props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 30000);
          props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 120000);
          
          return props;
      }
  }
  ```

</details>

<details>
  <summary>Apache Kafka - Consumers and Consumer Groups</summary>
  <br/>

  **Consumers** read messages from Kafka topics.

  **Consumer Groups:**
  - Multiple consumers work together
  - Each partition assigned to one consumer in group
  - Enables parallel processing
  - Automatic rebalancing

  ```
  Topic: "orders" (3 partitions)
  ┌─────────────┬─────────────┬─────────────┐
  │ Partition 0 │ Partition 1 │ Partition 2 │
  └──────┬──────┴──────┬──────┴──────┬───────┘
         │             │             │
         ▼             ▼             ▼
  ┌───────────┐ ┌───────────┐ ┌───────────┐
  │Consumer 1 │ │Consumer 2 │ │Consumer 3 │
  └───────────┘ └───────────┘ └───────────┘
       Consumer Group: "order-processors"
  
  If Consumer 2 fails:
  ┌─────────────┬─────────────┬─────────────┐
  │ Partition 0 │ Partition 1 │ Partition 2 │
  └──────┬──────┴──────┬──────┴──────┬───────┘
         │             │             │
         ▼             ▼             ▼
  ┌───────────┐       X         ┌───────────┐
  │Consumer 1 │               │Consumer 3 │
  │(P0 + P1)  │               │   (P2)    │
  └───────────┘               └───────────┘
  ```

  **Basic Consumer:**

  ```java
  @Service
  @Slf4j
  public class OrderConsumer {
      
      @KafkaListener(
          topics = "orders",
          groupId = "order-service-group"
      )
      public void consumeOrder(OrderEvent event) {
          log.info("Received order: {}", event.getOrderId());
          processOrder(event);
      }
      
      @KafkaListener(
          topics = "orders",
          groupId = "order-service-group",
          containerFactory = "kafkaListenerContainerFactory"
      )
      public void consumeWithMetadata(
          @Payload OrderEvent event,
          @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
          @Header(KafkaHeaders.OFFSET) long offset,
          @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long timestamp
      ) {
          log.info("Received order {} from partition {} at offset {} (timestamp: {})",
              event.getOrderId(), partition, offset, timestamp);
          processOrder(event);
      }
  }
  ```

  **Multiple Consumers in Group:**

  ```java
  // Consumer 1
  @Service
  public class OrderConsumer1 {
      @KafkaListener(
          topics = "orders",
          groupId = "order-processors",
          id = "consumer-1"
      )
      public void consume(OrderEvent event) {
          log.info("Consumer 1 processing: {}", event.getOrderId());
      }
  }
  
  // Consumer 2
  @Service
  public class OrderConsumer2 {
      @KafkaListener(
          topics = "orders",
          groupId = "order-processors",
          id = "consumer-2"
      )
      public void consume(OrderEvent event) {
          log.info("Consumer 2 processing: {}", event.getOrderId());
      }
  }
  
  // Consumer 3
  @Service
  public class OrderConsumer3 {
      @KafkaListener(
          topics = "orders",
          groupId = "order-processors",
          id = "consumer-3"
      )
      public void consume(OrderEvent event) {
          log.info("Consumer 3 processing: {}", event.getOrderId());
      }
  }
  // Each consumer gets different partitions automatically
  ```

  **Concurrent Consumers:**

  ```java
  @Configuration
  public class KafkaConsumerConfig {
      
      @Bean
      public ConcurrentKafkaListenerContainerFactory<String, OrderEvent> 
          kafkaListenerContainerFactory() {
          
          ConcurrentKafkaListenerContainerFactory<String, OrderEvent> factory =
              new ConcurrentKafkaListenerContainerFactory<>();
          
          factory.setConsumerFactory(consumerFactory());
          factory.setConcurrency(3);  // 3 consumer threads
          factory.getContainerProperties().setAckMode(AckMode.MANUAL);
          
          return factory;
      }
  }
  
  @Service
  public class OrderConsumer {
      
      @KafkaListener(
          topics = "orders",
          groupId = "order-service",
          concurrency = "3"  // 3 concurrent consumers
      )
      public void consume(OrderEvent event) {
          log.info("Thread {} processing order {}", 
              Thread.currentThread().getName(), 
              event.getOrderId());
      }
  }
  ```

  **Consumer with Error Handling:**

  ```java
  @Service
  @Slf4j
  public class OrderConsumer {
      
      @KafkaListener(topics = "orders", groupId = "order-service")
      public void consume(OrderEvent event) {
          try {
              processOrder(event);
          } catch (RetryableException e) {
              log.warn("Retryable error, will retry: {}", e.getMessage());
              throw e;  // Retry
          } catch (NonRetryableException e) {
              log.error("Non-retryable error, sending to DLQ: {}", e.getMessage());
              sendToDeadLetterQueue(event);
          }
      }
      
      @KafkaListener(topics = "orders.DLQ", groupId = "dlq-processor")
      public void consumeDeadLetter(OrderEvent event) {
          log.error("Processing dead letter: {}", event.getOrderId());
          // Manual intervention or special handling
      }
  }
  
  @Configuration
  public class KafkaErrorHandlingConfig {
      
      @Bean
      public ConcurrentKafkaListenerContainerFactory<String, OrderEvent> 
          kafkaListenerContainerFactory() {
          
          ConcurrentKafkaListenerContainerFactory<String, OrderEvent> factory =
              new ConcurrentKafkaListenerContainerFactory<>();
          
          factory.setConsumerFactory(consumerFactory());
          
          // Retry configuration
          factory.setCommonErrorHandler(
              new DefaultErrorHandler(
                  new DeadLetterPublishingRecoverer(kafkaTemplate()),
                  new FixedBackOff(1000L, 3L)  // Retry 3 times with 1s delay
              )
          );
          
          return factory;
      }
  }
  ```

  **Batch Consumer:**

  ```java
  @Service
  public class BatchOrderConsumer {
      
      @KafkaListener(
          topics = "orders",
          groupId = "batch-processor",
          containerFactory = "batchFactory"
      )
      public void consumeBatch(List<OrderEvent> events) {
          log.info("Processing batch of {} orders", events.size());
          
          // Process batch efficiently
          orderService.processBatch(events);
      }
  }
  
  @Configuration
  public class KafkaBatchConfig {
      
      @Bean
      public ConcurrentKafkaListenerContainerFactory<String, OrderEvent> batchFactory() {
          ConcurrentKafkaListenerContainerFactory<String, OrderEvent> factory =
              new ConcurrentKafkaListenerContainerFactory<>();
          
          factory.setConsumerFactory(consumerFactory());
          factory.setBatchListener(true);  // Enable batch mode
          factory.getContainerProperties().setAckMode(AckMode.BATCH);
          
          return factory;
      }
      
      @Bean
      public ConsumerFactory<String, OrderEvent> consumerFactory() {
          Map<String, Object> props = new HashMap<>();
          props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
          props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 100);  // Batch size
          props.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, 1024);  // Wait for 1KB
          props.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 500);  // Or 500ms
          
          return new DefaultKafkaConsumerFactory<>(props);
      }
  }
  ```

</details>

<details>
  <summary>Apache Kafka - Offset Management</summary>
  <br/>

  **Offset** is the position of a message in a partition.

  **Offset Commit Strategies:**

  ```
  Partition: [M0][M1][M2][M3][M4][M5][M6][M7]
  Offset:     0   1   2   3   4   5   6   7
                              ↑
                        Current Offset: 3
                        (Next read: M4)
  ```

  **Auto Commit (Default):**

  ```yaml
  spring:
    kafka:
      consumer:
        enable-auto-commit: true
        auto-commit-interval: 5000  # Commit every 5 seconds
  ```

  ```java
  @Service
  public class OrderConsumer {
      
      @KafkaListener(topics = "orders", groupId = "auto-commit-group")
      public void consume(OrderEvent event) {
          // Process message
          processOrder(event);
          
          // Offset automatically committed every 5 seconds
          // Risk: If crash before commit, messages reprocessed
      }
  }
  ```

  **Manual Commit:**

  ```yaml
  spring:
    kafka:
      consumer:
        enable-auto-commit: false
  ```

  ```java
  @Configuration
  public class KafkaConsumerConfig {
      
      @Bean
      public ConcurrentKafkaListenerContainerFactory<String, OrderEvent> 
          kafkaListenerContainerFactory() {
          
          ConcurrentKafkaListenerContainerFactory<String, OrderEvent> factory =
              new ConcurrentKafkaListenerContainerFactory<>();
          
          factory.setConsumerFactory(consumerFactory());
          factory.getContainerProperties().setAckMode(AckMode.MANUAL);
          
          return factory;
      }
  }
  
  @Service
  public class OrderConsumer {
      
      @KafkaListener(topics = "orders", groupId = "manual-commit-group")
      public void consume(OrderEvent event, Acknowledgment ack) {
          try {
              // Process message
              processOrder(event);
              
              // Manually commit offset after successful processing
              ack.acknowledge();
              
          } catch (Exception e) {
              // Don't commit - message will be reprocessed
              log.error("Failed to process order, will retry", e);
          }
      }
  }
  ```

  **Manual Batch Commit:**

  ```java
  @Service
  public class BatchOrderConsumer {
      
      @KafkaListener(
          topics = "orders",
          groupId = "batch-commit-group",
          containerFactory = "batchFactory"
      )
      public void consumeBatch(List<OrderEvent> events, Acknowledgment ack) {
          try {
              // Process all messages in batch
              for (OrderEvent event : events) {
                  processOrder(event);
              }
              
              // Commit offset for entire batch
              ack.acknowledge();
              
          } catch (Exception e) {
              // Don't commit - entire batch will be reprocessed
              log.error("Failed to process batch", e);
          }
      }
  }
  ```

  **Seeking to Specific Offset:**

  ```java
  @Service
  public class OrderConsumer {
      
      @Autowired
      private KafkaListenerEndpointRegistry registry;
      
      public void seekToOffset(String topic, int partition, long offset) {
          MessageListenerContainer container = registry
              .getListenerContainer("order-consumer");
          
          container.stop();
          
          ConsumerSeekCallback callback = (consumer, exception) -> {
              TopicPartition topicPartition = new TopicPartition(topic, partition);
              consumer.seek(topicPartition, offset);
          };
          
          ((AbstractMessageListenerContainer<?, ?>) container)
              .getContainerProperties()
              .setConsumerRebalanceListener(new ConsumerAwareRebalanceListener() {
                  @Override
                  public void onPartitionsAssigned(Consumer<?, ?> consumer, 
                      Collection<TopicPartition> partitions) {
                      
                      for (TopicPartition partition : partitions) {
                          consumer.seek(partition, offset);
                      }
                  }
              });
          
          container.start();
      }
      
      public void seekToBeginning(String topic) {
          // Reprocess all messages from beginning
          MessageListenerContainer container = registry
              .getListenerContainer("order-consumer");
          
          container.stop();
          ((AbstractMessageListenerContainer<?, ?>) container)
              .getContainerProperties()
              .setConsumerRebalanceListener(new ConsumerAwareRebalanceListener() {
                  @Override
                  public void onPartitionsAssigned(Consumer<?, ?> consumer, 
                      Collection<TopicPartition> partitions) {
                      consumer.seekToBeginning(partitions);
                  }
              });
          container.start();
      }
  }
  ```

  **Offset Reset Strategy:**

  ```yaml
  spring:
    kafka:
      consumer:
        auto-offset-reset: earliest  # or latest, none
  ```

  ```
  auto-offset-reset: earliest
  - Start from beginning if no committed offset
  - Use case: Reprocess all historical data
  
  auto-offset-reset: latest
  - Start from end if no committed offset
  - Use case: Only process new messages
  
  auto-offset-reset: none
  - Throw exception if no committed offset
  - Use case: Strict offset management
  ```

  **Storing Offsets Externally:**

  ```java
  @Service
  public class OrderConsumer {
      
      @Autowired
      private OffsetRepository offsetRepository;
      
      @KafkaListener(
          topics = "orders",
          groupId = "external-offset-group",
          containerFactory = "manualFactory"
      )
      public void consume(
          OrderEvent event,
          @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
          @Header(KafkaHeaders.OFFSET) long offset,
          Acknowledgment ack
      ) {
          try {
              // Process message and store offset in database (transactional)
              processOrderAndStoreOffset(event, partition, offset);
              
              // Commit Kafka offset
              ack.acknowledge();
              
          } catch (Exception e) {
              log.error("Failed to process, will retry", e);
          }
      }
      
      @Transactional
      public void processOrderAndStoreOffset(OrderEvent event, int partition, long offset) {
          // Process order
          orderService.processOrder(event);
          
          // Store offset in database
          OffsetInfo offsetInfo = new OffsetInfo();
          offsetInfo.setTopic("orders");
          offsetInfo.setPartition(partition);
          offsetInfo.setOffset(offset);
          offsetInfo.setProcessedAt(LocalDateTime.now());
          
          offsetRepository.save(offsetInfo);
          
          // Both operations commit together
      }
  }
  ```

</details>

<details>
  <summary>RabbitMQ - Exchanges, Queues, and Routing</summary>
  <br/>

  **RabbitMQ** is a message broker that implements AMQP (Advanced Message Queuing Protocol).

  **Core Components:**

  ```
  ┌──────────┐      ┌──────────────┐      ┌─────────┐      ┌──────────┐
  │ Producer │─────▶│   Exchange   │─────▶│ Queue   │─────▶│ Consumer │
  └──────────┘      └──────────────┘      └─────────┘      └──────────┘
                           │
                      Routing Key
                      Binding Key
  ```

  **Exchanges:**
  - Receives messages from producers
  - Routes messages to queues based on routing rules
  - Types: Direct, Fanout, Topic, Headers

  **Queues:**
  - Stores messages until consumed
  - FIFO (First In, First Out)
  - Can be durable or transient

  **Bindings:**
  - Links between exchanges and queues
  - Defines routing rules

  **Exchange Types:**

  **1. Direct Exchange:**

  ```
  Producer ─(routing_key: "error")─▶ Direct Exchange
                                           │
                    ┌──────────────────────┼──────────────────────┐
                    │ (binding: "error")   │ (binding: "info")    │
                    ▼                      ▼                      ▼
              ┌──────────┐          ┌──────────┐          ┌──────────┐
              │  Error   │          │   Info   │          │  Debug   │
              │  Queue   │          │  Queue   │          │  Queue   │
              └──────────┘          └──────────┘          └──────────┘
  
  Message with routing_key "error" goes to Error Queue only
  ```

  ```java
  @Configuration
  public class RabbitMQDirectConfig {
      
      @Bean
      public DirectExchange directExchange() {
          return new DirectExchange("logs.direct");
      }
      
      @Bean
      public Queue errorQueue() {
          return new Queue("error.queue", true);  // Durable
      }
      
      @Bean
      public Queue infoQueue() {
          return new Queue("info.queue", true);
      }
      
      @Bean
      public Binding errorBinding() {
          return BindingBuilder
              .bind(errorQueue())
              .to(directExchange())
              .with("error");  // Routing key
      }
      
      @Bean
      public Binding infoBinding() {
          return BindingBuilder
              .bind(infoQueue())
              .to(directExchange())
              .with("info");
      }
  }
  
  @Service
  public class LogProducer {
      
      @Autowired
      private RabbitTemplate rabbitTemplate;
      
      public void sendErrorLog(String message) {
          rabbitTemplate.convertAndSend("logs.direct", "error", message);
      }
      
      public void sendInfoLog(String message) {
          rabbitTemplate.convertAndSend("logs.direct", "info", message);
      }
  }
  ```

  **2. Fanout Exchange (Pub/Sub):**

  ```
  Producer ────▶ Fanout Exchange
                      │
         ┌────────────┼────────────┐
         │            │            │
         ▼            ▼            ▼
    ┌────────┐   ┌────────┐   ┌────────┐
    │Queue 1 │   │Queue 2 │   │Queue 3 │
    └────────┘   └────────┘   └────────┘
  
  Message broadcast to ALL queues (ignores routing key)
  ```

  ```java
  @Configuration
  public class RabbitMQFanoutConfig {
      
      @Bean
      public FanoutExchange fanoutExchange() {
          return new FanoutExchange("notifications.fanout");
      }
      
      @Bean
      public Queue emailQueue() {
          return new Queue("email.queue");
      }
      
      @Bean
      public Queue smsQueue() {
          return new Queue("sms.queue");
      }
      
      @Bean
      public Queue pushQueue() {
          return new Queue("push.queue");
      }
      
      @Bean
      public Binding emailBinding() {
          return BindingBuilder.bind(emailQueue()).to(fanoutExchange());
      }
      
      @Bean
      public Binding smsBinding() {
          return BindingBuilder.bind(smsQueue()).to(fanoutExchange());
      }
      
      @Bean
      public Binding pushBinding() {
          return BindingBuilder.bind(pushQueue()).to(fanoutExchange());
      }
  }
  
  @Service
  public class NotificationProducer {
      
      @Autowired
      private RabbitTemplate rabbitTemplate;
      
      public void sendNotification(String message) {
          // Broadcast to all queues
          rabbitTemplate.convertAndSend("notifications.fanout", "", message);
      }
  }
  ```

  **3. Topic Exchange (Pattern Matching):**

  ```
  Producer ─(routing_key: "order.created.us")─▶ Topic Exchange
                                                      │
                    ┌─────────────────────────────────┼─────────────────────┐
                    │ (binding: "order.*")            │ (binding: "*.*.us") │
                    ▼                                 ▼                     ▼
              ┌──────────┐                      ┌──────────┐        ┌──────────┐
              │  Order   │                      │   US     │        │  All     │
              │  Queue   │                      │  Queue   │        │  Queue   │
              └──────────┘                      └──────────┘        └──────────┘
  
  * matches exactly one word
  # matches zero or more words
  ```

  ```java
  @Configuration
  public class RabbitMQTopicConfig {
      
      @Bean
      public TopicExchange topicExchange() {
          return new TopicExchange("events.topic");
      }
      
      @Bean
      public Queue orderQueue() {
          return new Queue("order.queue");
      }
      
      @Bean
      public Queue usQueue() {
          return new Queue("us.queue");
      }
      
      @Bean
      public Queue allQueue() {
          return new Queue("all.queue");
      }
      
      @Bean
      public Binding orderBinding() {
          return BindingBuilder
              .bind(orderQueue())
              .to(topicExchange())
              .with("order.*");  // Matches: order.created, order.updated, etc.
      }
      
      @Bean
      public Binding usBinding() {
          return BindingBuilder
              .bind(usQueue())
              .to(topicExchange())
              .with("*.*.us");  // Matches: order.created.us, user.updated.us, etc.
      }
      
      @Bean
      public Binding allBinding() {
          return BindingBuilder
              .bind(allQueue())
              .to(topicExchange())
              .with("#");  // Matches everything
      }
  }
  
  @Service
  public class EventProducer {
      
      @Autowired
      private RabbitTemplate rabbitTemplate;
      
      public void sendOrderCreated(String region) {
          String routingKey = "order.created." + region;
          rabbitTemplate.convertAndSend("events.topic", routingKey, "Order created");
      }
      
      public void sendUserUpdated(String region) {
          String routingKey = "user.updated." + region;
          rabbitTemplate.convertAndSend("events.topic", routingKey, "User updated");
      }
  }
  ```

  **Consumer:**

  ```java
  @Service
  @Slf4j
  public class OrderConsumer {
      
      @RabbitListener(queues = "order.queue")
      public void consumeOrder(String message) {
          log.info("Received order message: {}", message);
      }
      
      @RabbitListener(queues = "us.queue")
      public void consumeUsEvents(String message) {
          log.info("Received US event: {}", message);
      }
      
      @RabbitListener(queues = "all.queue")
      public void consumeAllEvents(String message) {
          log.info("Received event: {}", message);
      }
  }
  ```

</details>

<details>
  <summary>Message Patterns</summary>
  <br/>

  **1. Publish/Subscribe (Pub/Sub):**

  One message delivered to multiple consumers.

  ```
  Publisher ────▶ Topic/Exchange
                      │
         ┌────────────┼────────────┐
         │            │            │
         ▼            ▼            ▼
    Subscriber 1  Subscriber 2  Subscriber 3
  
  All subscribers receive the same message
  ```

  **Kafka Implementation:**

  ```java
  // Producer
  @Service
  public class OrderEventPublisher {
      
      @Autowired
      private KafkaTemplate<String, OrderEvent> kafkaTemplate;
      
      public void publishOrderCreated(OrderEvent event) {
          kafkaTemplate.send("order-events", event);
      }
  }
  
  // Multiple consumers (different groups)
  @Service
  public class EmailNotificationService {
      @KafkaListener(topics = "order-events", groupId = "email-service")
      public void handleOrderCreated(OrderEvent event) {
          sendEmail(event);
      }
  }
  
  @Service
  public class InventoryService {
      @KafkaListener(topics = "order-events", groupId = "inventory-service")
      public void handleOrderCreated(OrderEvent event) {
          updateInventory(event);
      }
  }
  
  @Service
  public class AnalyticsService {
      @KafkaListener(topics = "order-events", groupId = "analytics-service")
      public void handleOrderCreated(OrderEvent event) {
          recordAnalytics(event);
      }
  }
  ```

  **RabbitMQ Implementation:**

  ```java
  // Fanout exchange for pub/sub
  @Configuration
  public class PubSubConfig {
      @Bean
      public FanoutExchange orderExchange() {
          return new FanoutExchange("order.events");
      }
      
      @Bean
      public Queue emailQueue() {
          return new Queue("email.queue");
      }
      
      @Bean
      public Queue inventoryQueue() {
          return new Queue("inventory.queue");
      }
      
      @Bean
      public Binding emailBinding() {
          return BindingBuilder.bind(emailQueue()).to(orderExchange());
      }
      
      @Bean
      public Binding inventoryBinding() {
          return BindingBuilder.bind(inventoryQueue()).to(orderExchange());
      }
  }
  ```

  **2. Point-to-Point:**

  One message delivered to exactly one consumer.

  ```
  Producer ────▶ Queue ────▶ Consumer 1
                   │
                   └────▶ Consumer 2 (if Consumer 1 busy)
  
  Only one consumer processes each message
  ```

  **Kafka Implementation:**

  ```java
  // Multiple consumers in same group (load balancing)
  @Service
  public class OrderProcessor1 {
      @KafkaListener(topics = "orders", groupId = "order-processors")
      public void process(OrderEvent event) {
          log.info("Processor 1 handling: {}", event.getOrderId());
      }
  }
  
  @Service
  public class OrderProcessor2 {
      @KafkaListener(topics = "orders", groupId = "order-processors")
      public void process(OrderEvent event) {
          log.info("Processor 2 handling: {}", event.getOrderId());
      }
  }
  // Each message processed by only one consumer
  ```

  **RabbitMQ Implementation:**

  ```java
  @Configuration
  public class PointToPointConfig {
      @Bean
      public Queue taskQueue() {
          return new Queue("tasks.queue");
      }
  }
  
  @Service
  public class TaskProducer {
      @Autowired
      private RabbitTemplate rabbitTemplate;
      
      public void sendTask(String task) {
          rabbitTemplate.convertAndSend("tasks.queue", task);
      }
  }
  
  @Service
  public class TaskConsumer1 {
      @RabbitListener(queues = "tasks.queue")
      public void processTask(String task) {
          log.info("Consumer 1 processing: {}", task);
      }
  }
  
  @Service
  public class TaskConsumer2 {
      @RabbitListener(queues = "tasks.queue")
      public void processTask(String task) {
          log.info("Consumer 2 processing: {}", task);
      }
  }
  ```

  **3. Request-Reply:**

  Synchronous communication over async messaging.

  ```
  Client ─(request)─▶ Request Queue ─▶ Server
    ▲                                     │
    │                                     │
    └──(reply)─── Reply Queue ◀───────────┘
  ```

  **Kafka Implementation:**

  ```java
  @Service
  public class OrderService {
      
      @Autowired
      private KafkaTemplate<String, OrderRequest> kafkaTemplate;
      
      public CompletableFuture<OrderResponse> createOrder(OrderRequest request) {
          String correlationId = UUID.randomUUID().toString();
          
          ProducerRecord<String, OrderRequest> record = new ProducerRecord<>(
              "order-requests",
              correlationId,
              request
          );
          record.headers().add("reply-topic", "order-responses".getBytes());
          record.headers().add("correlation-id", correlationId.getBytes());
          
          CompletableFuture<OrderResponse> future = new CompletableFuture<>();
          pendingRequests.put(correlationId, future);
          
          kafkaTemplate.send(record);
          
          return future;
      }
      
      @KafkaListener(topics = "order-responses")
      public void handleResponse(
          OrderResponse response,
          @Header("correlation-id") String correlationId
      ) {
          CompletableFuture<OrderResponse> future = pendingRequests.remove(correlationId);
          if (future != null) {
              future.complete(response);
          }
      }
  }
  
  @Service
  public class OrderProcessor {
      
      @Autowired
      private KafkaTemplate<String, OrderResponse> kafkaTemplate;
      
      @KafkaListener(topics = "order-requests")
      public void processOrder(
          OrderRequest request,
          @Header("correlation-id") String correlationId,
          @Header("reply-topic") String replyTopic
      ) {
          // Process order
          OrderResponse response = processOrder(request);
          
          // Send response
          ProducerRecord<String, OrderResponse> record = new ProducerRecord<>(
              replyTopic,
              correlationId,
              response
          );
          record.headers().add("correlation-id", correlationId.getBytes());
          
          kafkaTemplate.send(record);
      }
  }
  ```

  **RabbitMQ Implementation:**

  ```java
  @Service
  public class OrderService {
      
      @Autowired
      private RabbitTemplate rabbitTemplate;
      
      public OrderResponse createOrder(OrderRequest request) {
          // Send request and wait for response
          OrderResponse response = (OrderResponse) rabbitTemplate
              .convertSendAndReceive("order-requests", request);
          
          return response;
      }
  }
  
  @Service
  public class OrderProcessor {
      
      @RabbitListener(queues = "order-requests")
      public OrderResponse processOrder(OrderRequest request) {
          // Process order
          Order order = orderRepository.save(new Order(request));
          
          // Return response (automatically sent to reply queue)
          return new OrderResponse(order.getId(), "SUCCESS");
      }
  }
  ```

</details>

<details>
  <summary>Idempotency - Handling Duplicate Messages</summary>
  <br/>

  **Idempotency** ensures processing a message multiple times has the same effect as processing it once.

  **Why needed:**
  - Network failures cause retries
  - Consumer crashes before committing offset
  - At-least-once delivery guarantees

  **Strategies:**

  **1. Idempotency Keys:**

  ```java
  @Service
  public class OrderService {
      
      @Autowired
      private OrderRepository orderRepository;
      
      @Autowired
      private ProcessedMessageRepository processedMessageRepository;
      
      @KafkaListener(topics = "orders")
      @Transactional
      public void processOrder(OrderEvent event, @Header(KafkaHeaders.OFFSET) long offset) {
          String idempotencyKey = event.getOrderId() + "-" + offset;
          
          // Check if already processed
          if (processedMessageRepository.existsByKey(idempotencyKey)) {
              log.info("Message already processed: {}", idempotencyKey);
              return;  // Skip
          }
          
          // Process order
          Order order = orderRepository.save(new Order(event));
          
          // Mark as processed
          ProcessedMessage processed = new ProcessedMessage();
          processed.setKey(idempotencyKey);
          processed.setProcessedAt(LocalDateTime.now());
          processedMessageRepository.save(processed);
          
          // Both operations commit together (transactional)
      }
  }
  
  @Entity
  public class ProcessedMessage {
      @Id
      private String key;
      private LocalDateTime processedAt;
      
      @Index
      private LocalDateTime expiresAt;  // For cleanup
  }
  ```

  **2. Natural Idempotency:**

  ```java
  @Service
  public class UserService {
      
      @KafkaListener(topics = "user-updates")
      public void updateUser(UserUpdateEvent event) {
          // Idempotent by nature - last update wins
          User user = userRepository.findById(event.getUserId());
          user.setName(event.getName());
          user.setEmail(event.getEmail());
          userRepository.save(user);
          
          // Processing multiple times has same result
      }
  }
  ```

  **3. Version-based Idempotency:**

  ```java
  @Entity
  public class Product {
      @Id
      private Long id;
      
      private String name;
      private BigDecimal price;
      
      @Version
      private Long version;  // Optimistic locking
  }
  
  @Service
  public class ProductService {
      
      @KafkaListener(topics = "product-updates")
      public void updateProduct(ProductUpdateEvent event) {
          try {
              Product product = productRepository.findById(event.getProductId());
              
              // Only update if version matches
              if (product.getVersion().equals(event.getExpectedVersion())) {
                  product.setPrice(event.getNewPrice());
                  productRepository.save(product);  // Version incremented
              } else {
                  log.warn("Version mismatch, skipping update");
              }
              
          } catch (OptimisticLockException e) {
              log.warn("Concurrent update detected, skipping");
          }
      }
  }
  ```

  **4. Distributed Lock:**

  ```java
  @Service
  public class PaymentService {
      
      @Autowired
      private RedissonClient redissonClient;
      
      @KafkaListener(topics = "payments")
      public void processPayment(PaymentEvent event) {
          String lockKey = "payment:" + event.getPaymentId();
          RLock lock = redissonClient.getLock(lockKey);
          
          try {
              // Try to acquire lock (wait 10s, auto-release after 30s)
              if (lock.tryLock(10, 30, TimeUnit.SECONDS)) {
                  try {
                      // Check if already processed
                      if (paymentRepository.existsById(event.getPaymentId())) {
                          log.info("Payment already processed");
                          return;
                      }
                      
                      // Process payment
                      Payment payment = new Payment(event);
                      paymentRepository.save(payment);
                      
                  } finally {
                      lock.unlock();
                  }
              } else {
                  log.warn("Could not acquire lock for payment: {}", event.getPaymentId());
              }
          } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
          }
      }
  }
  ```

  **5. Deduplication Window:**

  ```java
  @Service
  public class OrderService {
      
      @Autowired
      private RedisTemplate<String, String> redisTemplate;
      
      @KafkaListener(topics = "orders")
      public void processOrder(OrderEvent event) {
          String dedupeKey = "order:" + event.getOrderId();
          
          // Try to set key with expiration (5 minutes)
          Boolean isNew = redisTemplate.opsForValue()
              .setIfAbsent(dedupeKey, "processing", 5, TimeUnit.MINUTES);
          
          if (Boolean.TRUE.equals(isNew)) {
              // First time seeing this message
              try {
                  processOrderInternal(event);
              } catch (Exception e) {
                  // Remove key on failure to allow retry
                  redisTemplate.delete(dedupeKey);
                  throw e;
              }
          } else {
              log.info("Duplicate message detected: {}", event.getOrderId());
          }
      }
  }
  ```

  **6. Message Fingerprinting:**

  ```java
  @Service
  public class EventService {
      
      @Autowired
      private ProcessedEventRepository processedEventRepository;
      
      @KafkaListener(topics = "events")
      @Transactional
      public void processEvent(Event event) {
          // Create fingerprint from message content
          String fingerprint = createFingerprint(event);
          
          // Check if already processed
          if (processedEventRepository.existsByFingerprint(fingerprint)) {
              log.info("Duplicate event detected");
              return;
          }
          
          // Process event
          processEventInternal(event);
          
          // Store fingerprint
          ProcessedEvent processed = new ProcessedEvent();
          processed.setFingerprint(fingerprint);
          processed.setProcessedAt(LocalDateTime.now());
          processedEventRepository.save(processed);
      }
      
      private String createFingerprint(Event event) {
          String content = event.getEventId() + 
                          event.getEventType() + 
                          event.getTimestamp() + 
                          event.getPayload();
          
          return DigestUtils.sha256Hex(content);
      }
  }
  ```

</details>

<details>
  <summary>Best Practices</summary>
  <br/>

  ```java
  // ✅ DO: Use appropriate message pattern
  // Pub/Sub for broadcasting events
  // Point-to-Point for task distribution
  // Request-Reply for synchronous operations
  
  // ✅ DO: Implement idempotency
  @Service
  public class OrderService {
      @KafkaListener(topics = "orders")
      @Transactional
      public void processOrder(OrderEvent event) {
          if (alreadyProcessed(event.getOrderId())) {
              return;  // Skip duplicate
          }
          processOrderInternal(event);
          markAsProcessed(event.getOrderId());
      }
  }
  
  // ✅ DO: Use manual offset commit for critical operations
  @KafkaListener(topics = "payments")
  public void processPayment(PaymentEvent event, Acknowledgment ack) {
      try {
          processPaymentInternal(event);
          ack.acknowledge();  // Commit only after success
      } catch (Exception e) {
          // Don't commit - will retry
      }
  }
  
  // ✅ DO: Implement dead letter queues
  @Configuration
  public class KafkaErrorConfig {
      @Bean
      public ConcurrentKafkaListenerContainerFactory<String, OrderEvent> 
          kafkaListenerContainerFactory() {
          
          factory.setCommonErrorHandler(
              new DefaultErrorHandler(
                  new DeadLetterPublishingRecoverer(kafkaTemplate()),
                  new FixedBackOff(1000L, 3L)
              )
          );
          return factory;
      }
  }
  
  // ✅ DO: Add correlation IDs for tracing
  @Service
  public class OrderProducer {
      public void sendOrder(OrderEvent event) {
          ProducerRecord<String, OrderEvent> record = new ProducerRecord<>("orders", event);
          record.headers().add("correlation-id", UUID.randomUUID().toString().getBytes());
          kafkaTemplate.send(record);
      }
  }
  
  // ✅ DO: Use appropriate partition key
  @Service
  public class OrderProducer {
      public void sendOrder(OrderEvent event) {
          // Partition by customer ID for ordering guarantee
          String key = String.valueOf(event.getCustomerId());
          kafkaTemplate.send("orders", key, event);
      }
  }
  
  // ✅ DO: Configure appropriate retention
  @Bean
  public NewTopic orderTopic() {
      return TopicBuilder.name("orders")
          .partitions(3)
          .replicas(3)
          .config(TopicConfig.RETENTION_MS_CONFIG, "604800000")  // 7 days
          .build();
  }
  
  // ✅ DO: Handle poison messages
  @KafkaListener(topics = "orders")
  public void processOrder(OrderEvent event) {
      try {
          processOrderInternal(event);
      } catch (UnrecoverableException e) {
          sendToDeadLetterQueue(event);
          // Don't retry
      }
  }
  
  // ✅ DO: Monitor consumer lag
  @Component
  public class ConsumerLagMonitor {
      @Scheduled(fixedRate = 60000)
      public void checkLag() {
          // Monitor lag and alert if too high
          long lag = getConsumerLag();
          if (lag > 10000) {
              alertOps("High consumer lag: " + lag);
          }
      }
  }
  
  // ❌ DON'T: Use auto-commit for critical operations
  // Risk: Message marked as processed before actual processing
  
  // ❌ DON'T: Ignore duplicate messages
  // Always implement idempotency
  
  // ❌ DON'T: Use synchronous messaging for high-volume events
  // Use async messaging (Kafka, RabbitMQ)
  
  // ❌ DON'T: Create too many partitions
  // Balance between parallelism and overhead
  
  // ✅ DO: Use batch processing for high throughput
  @KafkaListener(topics = "orders", containerFactory = "batchFactory")
  public void processBatch(List<OrderEvent> events) {
      orderService.processBatch(events);
  }
  
  // ✅ DO: Implement circuit breaker for external calls
  @Service
  public class OrderConsumer {
      @KafkaListener(topics = "orders")
      @CircuitBreaker(name = "paymentService")
      public void processOrder(OrderEvent event) {
          paymentService.processPayment(event);
      }
  }
  ```

  **Summary:**
  + **Kafka** - High-throughput event streaming (topics, partitions, consumer groups, offset management)
  + **RabbitMQ** - Message broker with flexible routing (exchanges, queues, bindings)
  + **Pub/Sub** - Broadcast messages to multiple consumers
  + **Point-to-Point** - Load balance messages across consumers
  + **Request-Reply** - Synchronous communication over async messaging
  + **Idempotency** - Handle duplicate messages safely (idempotency keys, natural idempotency, versioning)
  + Use **manual commit** for critical operations
  + Implement **dead letter queues** for poison messages
  + Add **correlation IDs** for distributed tracing
  + Choose appropriate **partition key** for ordering
  + **Monitor consumer lag** and alert on issues
  + Implement **circuit breakers** for resilience

</details>

## Microservices Patterns

<details>
  <summary>Outbox Pattern</summary>
  <br/>

  **Outbox Pattern** ensures reliable event publishing when updating a database.

  **Problem:**

  ```java
  // ❌ Not reliable!
  @Service
  public class OrderService {
      @Transactional
      public void createOrder(OrderRequest request) {
          // 1. Save to database
          Order order = orderRepository.save(new Order(request));
          
          // 2. Publish event
          kafkaTemplate.send("orders", new OrderCreatedEvent(order));
          
          // Problem: If Kafka fails, database committed but event lost!
      }
  }
  ```

  **Solution:**

  ```java
  // Outbox table
  @Entity
  @Table(name = "outbox")
  public class OutboxEvent {
      @Id
      private String id;
      
      private String aggregateType;  // "Order"
      private String aggregateId;    // "12345"
      private String eventType;      // "OrderCreated"
      
      @Column(columnDefinition = "TEXT")
      private String payload;
      
      private LocalDateTime createdAt;
      private boolean published;
  }
  
  // Save event in same transaction
  @Service
  public class OrderService {
      
      @Transactional
      public void createOrder(OrderRequest request) {
          // 1. Save order
          Order order = orderRepository.save(new Order(request));
          
          // 2. Save event to outbox (same transaction)
          OutboxEvent event = new OutboxEvent();
          event.setId(UUID.randomUUID().toString());
          event.setAggregateType("Order");
          event.setAggregateId(order.getId().toString());
          event.setEventType("OrderCreated");
          event.setPayload(toJson(new OrderCreatedEvent(order)));
          event.setPublished(false);
          
          outboxRepository.save(event);
          
          // Both commit together atomically!
      }
  }
  
  // Separate publisher
  @Service
  public class OutboxPublisher {
      
      @Scheduled(fixedDelay = 1000)
      @Transactional
      public void publishEvents() {
          List<OutboxEvent> unpublished = outboxRepository
              .findByPublishedFalse(PageRequest.of(0, 100));
          
          for (OutboxEvent event : unpublished) {
              try {
                  kafkaTemplate.send(
                      event.getAggregateType() + "-events",
                      event.getPayload()
                  ).get();
                  
                  event.setPublished(true);
                  outboxRepository.save(event);
              } catch (Exception e) {
                  // Retry later
              }
          }
      }
  }
  ```

  **Benefits:**
  + Atomicity - Database and event storage in same transaction
  + Reliability - Events never lost
  + At-least-once delivery guaranteed

</details>

<details>
  <summary>Strangler Fig Pattern</summary>
  <br/>

  **Strangler Fig Pattern** gradually migrates from monolith to microservices.

  **Strategy:**

  ```
  Phase 1: Monolith handles everything
  ┌─────────────────────────────┐
  │        Monolith             │
  │  ┌────┐ ┌────┐ ┌────┐      │
  │  │User│ │Order│ │Pay│      │
  │  └────┘ └────┘ └────┘      │
  └─────────────────────────────┘
  
  Phase 2: Extract one service
  ┌──────────┐    ┌─────────────┐
  │  Proxy   │───▶│  Monolith   │
  └────┬─────┘    │ ┌────┐┌────┐│
       │          │ │Order││Pay││
       │          │ └────┘└────┘│
       │          └─────────────┘
       └─────────▶┌─────────────┐
                  │ User Service│
                  └─────────────┘
  
  Phase 3: Extract more services
  ┌──────────┐    ┌─────────────┐
  │  Proxy   │───▶│  Monolith   │
  └─┬──┬──┬──┘    │   ┌────┐   │
    │  │  │       │   │Pay││
    │  │  │       │   └────┘   │
    │  │  │       └─────────────┘
    │  │  └──────▶┌─────────────┐
    │  │          │ User Service│
    │  │          └─────────────┘
    │  └─────────▶┌─────────────┐
    │             │Order Service│
    │             └─────────────┘
    └────────────▶┌─────────────┐
                  │Pay Service  │
                  └─────────────┘
  
  Phase 4: Retire monolith
  ┌──────────┐
  │  Proxy   │
  └─┬──┬──┬──┘
    │  │  │
    ▼  ▼  ▼
   [User][Order][Payment]
  ```

  **Implementation:**

  ```java
  // API Gateway routes requests
  @Configuration
  public class StranglerRouting {
      
      @Bean
      public RouteLocator routes(RouteLocatorBuilder builder) {
          return builder.routes()
              // New: Route to User microservice
              .route("user-service", r -> r
                  .path("/api/users/**")
                  .uri("lb://user-service")
              )
              
              // New: Route to Order microservice
              .route("order-service", r -> r
                  .path("/api/orders/**")
                  .uri("lb://order-service")
              )
              
              // Old: Everything else to monolith
              .route("monolith", r -> r
                  .path("/api/**")
                  .uri("http://monolith:8080")
              )
              .build();
      }
  }
  
  // Feature toggle for gradual rollout
  @Service
  public class OrderService {
      
      @Value("${feature.new-order-service.enabled}")
      private boolean useNewService;
      
      public Order createOrder(OrderRequest request) {
          if (useNewService) {
              return newOrderService.createOrder(request);
          } else {
              return legacyOrderService.createOrder(request);
          }
      }
  }
  ```

  **Steps:**
  1. Add proxy/gateway in front of monolith
  2. Extract one service at a time
  3. Route new requests to new service
  4. Migrate data gradually
  5. Retire monolith when empty

</details>

<details>
  <summary>Backend for Frontend (BFF)</summary>
  <br/>

  **BFF Pattern** creates separate API gateways for different client types.

  **Architecture:**

  ```
  ┌─────────┐     ┌─────────────┐     ┌──────────┐
  │   Web   │────▶│   Web BFF   │────▶│  User    │
  │ Browser │     └─────────────┘  │  │ Service  │
  └─────────┘                      │  └──────────┘
                                   │
  ┌─────────┐     ┌─────────────┐ │  ┌──────────┐
  │ Mobile  │────▶│  Mobile BFF │─┼─▶│  Order   │
  │   App   │     └─────────────┘ │  │ Service  │
  └─────────┘                      │  └──────────┘
                                   │
  ┌─────────┐     ┌─────────────┐ │  ┌──────────┐
  │ Partner │────▶│ Partner BFF │─┴─▶│ Product  │
  │   API   │     └─────────────┘    │ Service  │
  └─────────┘                        └──────────┘
  ```

  **Why BFF:**
  - Different clients need different data
  - Mobile needs less data (bandwidth)
  - Web needs more detailed responses
  - Partners need specific API contracts

  **Web BFF:**

  ```java
  @RestController
  @RequestMapping("/api/web")
  public class WebBFFController {
      
      @Autowired
      private UserService userService;
      
      @Autowired
      private OrderService orderService;
      
      @Autowired
      private ProductService productService;
      
      // Web needs full details
      @GetMapping("/dashboard")
      public WebDashboard getDashboard(@AuthenticationPrincipal User user) {
          // Aggregate data from multiple services
          UserProfile profile = userService.getProfile(user.getId());
          List<Order> recentOrders = orderService.getRecentOrders(user.getId(), 10);
          List<Product> recommendations = productService.getRecommendations(user.getId());
          List<Notification> notifications = notificationService.getUnread(user.getId());
          
          return WebDashboard.builder()
              .profile(profile)
              .recentOrders(recentOrders)
              .recommendations(recommendations)
              .notifications(notifications)
              .build();
      }
  }
  ```

  **Mobile BFF:**

  ```java
  @RestController
  @RequestMapping("/api/mobile")
  public class MobileBFFController {
      
      @Autowired
      private UserService userService;
      
      @Autowired
      private OrderService orderService;
      
      // Mobile needs minimal data
      @GetMapping("/dashboard")
      public MobileDashboard getDashboard(@AuthenticationPrincipal User user) {
          // Less data for mobile
          UserSummary profile = userService.getSummary(user.getId());
          List<OrderSummary> recentOrders = orderService.getOrderSummaries(user.getId(), 5);
          int unreadCount = notificationService.getUnreadCount(user.getId());
          
          return MobileDashboard.builder()
              .userName(profile.getName())
              .orderCount(recentOrders.size())
              .unreadNotifications(unreadCount)
              .recentOrders(recentOrders)
              .build();
      }
  }
  ```

  **Partner BFF:**

  ```java
  @RestController
  @RequestMapping("/api/partner")
  public class PartnerBFFController {
      
      // Partner API with specific contract
      @PostMapping("/orders")
      public PartnerOrderResponse createOrder(
          @RequestHeader("X-Partner-ID") String partnerId,
          @RequestBody PartnerOrderRequest request
      ) {
          // Validate partner
          Partner partner = partnerService.validate(partnerId);
          
          // Transform to internal format
          OrderRequest internalRequest = transformToInternal(request, partner);
          
          // Create order
          Order order = orderService.createOrder(internalRequest);
          
          // Transform to partner format
          return transformToPartnerFormat(order);
      }
  }
  ```

</details>

<details>
  <summary>Anti-Corruption Layer (ACL)</summary>
  <br/>

  **Anti-Corruption Layer** translates between different domain models.

  **Problem:**

  ```
  Your Clean Domain ◀──X──▶ Legacy System
                      (Different models, terminology, structure)
  ```

  **Solution:**

  ```
  Your Domain ◀──▶ ACL ◀──▶ Legacy System
              (Translation layer)
  ```

  **Implementation:**

  ```java
  // Your domain model
  @Entity
  public class Order {
      private Long id;
      private Long customerId;
      private List<OrderItem> items;
      private BigDecimal total;
      private OrderStatus status;
      private LocalDateTime createdAt;
  }
  
  // Legacy system model (different structure)
  public class LegacyOrder {
      private String orderNumber;
      private String customerCode;
      private String[] productCodes;
      private double amount;
      private int statusCode;
      private String orderDate;
  }
  
  // Anti-Corruption Layer
  @Service
  public class LegacyOrderAdapter {
      
      @Autowired
      private LegacySystemClient legacyClient;
      
      public Order getOrder(Long orderId) {
          // Call legacy system
          LegacyOrder legacyOrder = legacyClient.getOrder(orderId.toString());
          
          // Translate to your domain model
          return translateToDomain(legacyOrder);
      }
      
      public void createOrder(Order order) {
          // Translate from your domain to legacy format
          LegacyOrder legacyOrder = translateToLegacy(order);
          
          // Call legacy system
          legacyClient.createOrder(legacyOrder);
      }
      
      private Order translateToDomain(LegacyOrder legacy) {
          Order order = new Order();
          order.setId(Long.parseLong(legacy.getOrderNumber()));
          order.setCustomerId(parseCustomerId(legacy.getCustomerCode()));
          order.setItems(parseItems(legacy.getProductCodes()));
          order.setTotal(BigDecimal.valueOf(legacy.getAmount()));
          order.setStatus(mapStatus(legacy.getStatusCode()));
          order.setCreatedAt(parseDate(legacy.getOrderDate()));
          return order;
      }
      
      private LegacyOrder translateToLegacy(Order order) {
          LegacyOrder legacy = new LegacyOrder();
          legacy.setOrderNumber(order.getId().toString());
          legacy.setCustomerCode("CUST-" + order.getCustomerId());
          legacy.setProductCodes(extractProductCodes(order.getItems()));
          legacy.setAmount(order.getTotal().doubleValue());
          legacy.setStatusCode(mapStatusCode(order.getStatus()));
          legacy.setOrderDate(formatDate(order.getCreatedAt()));
          return legacy;
      }
      
      private OrderStatus mapStatus(int statusCode) {
          switch (statusCode) {
              case 1: return OrderStatus.PENDING;
              case 2: return OrderStatus.CONFIRMED;
              case 3: return OrderStatus.SHIPPED;
              case 4: return OrderStatus.DELIVERED;
              case 9: return OrderStatus.CANCELLED;
              default: return OrderStatus.UNKNOWN;
          }
      }
  }
  
  // Your service uses clean domain model
  @Service
  public class OrderService {
      
      @Autowired
      private LegacyOrderAdapter legacyAdapter;
      
      public Order getOrder(Long orderId) {
          // Work with clean domain model
          return legacyAdapter.getOrder(orderId);
      }
  }
  ```

  **Benefits:**
  + Protects your domain from legacy complexity
  + Isolates translation logic
  + Easier to replace legacy system later
  + Clean domain model

</details>

<details>
  <summary>Sidecar Pattern</summary>
  <br/>

  **Sidecar Pattern** deploys helper components alongside main service.

  **Architecture:**

  ```
  ┌─────────────────────────────┐
  │          Pod/Container      │
  │                             │
  │  ┌──────────────────────┐  │
  │  │   Main Application   │  │
  │  │   (Order Service)    │  │
  │  └──────────────────────┘  │
  │            │                │
  │            ▼                │
  │  ┌──────────────────────┐  │
  │  │   Sidecar Container  │  │
  │  │  - Logging           │  │
  │  │  - Monitoring        │  │
  │  │  - Security          │  │
  │  │  - Service Mesh      │  │
  │  └──────────────────────┘  │
  └─────────────────────────────┘
  ```

  **Use Cases:**

  **1. Logging Sidecar:**

  ```yaml
  # Kubernetes deployment
  apiVersion: apps/v1
  kind: Deployment
  metadata:
    name: order-service
  spec:
    template:
      spec:
        containers:
        # Main application
        - name: order-service
          image: order-service:1.0
          ports:
          - containerPort: 8080
          volumeMounts:
          - name: logs
            mountPath: /var/log
        
        # Logging sidecar
        - name: log-shipper
          image: fluent-bit:latest
          volumeMounts:
          - name: logs
            mountPath: /var/log
          env:
          - name: ELASTICSEARCH_HOST
            value: "elasticsearch:9200"
        
        volumes:
        - name: logs
          emptyDir: {}
  ```

  **2. Service Mesh Sidecar (Envoy):**

  ```yaml
  apiVersion: apps/v1
  kind: Deployment
  metadata:
    name: order-service
  spec:
    template:
      metadata:
        annotations:
          sidecar.istio.io/inject: "true"  # Auto-inject Envoy sidecar
      spec:
        containers:
        - name: order-service
          image: order-service:1.0
          ports:
          - containerPort: 8080
        
        # Envoy sidecar (auto-injected by Istio)
        # Handles:
        # - Traffic management
        # - Load balancing
        # - Circuit breaking
        # - Mutual TLS
        # - Observability
  ```

  **3. Configuration Sidecar:**

  ```java
  // Main application
  @SpringBootApplication
  public class OrderServiceApplication {
      public static void main(String[] args) {
          SpringApplication.run(OrderServiceApplication.class, args);
      }
  }
  
  // Sidecar refreshes config
  @Component
  public class ConfigRefreshSidecar {
      
      @Scheduled(fixedRate = 30000)
      public void refreshConfig() {
          // Fetch latest config from config server
          Map<String, String> newConfig = configClient.getLatestConfig();
          
          // Update application properties
          updateEnvironment(newConfig);
          
          // Notify application of changes
          applicationEventPublisher.publishEvent(new ConfigRefreshEvent());
      }
  }
  ```

  **4. Security Sidecar:**

  ```yaml
  containers:
  # Main application
  - name: order-service
    image: order-service:1.0
  
  # OAuth proxy sidecar
  - name: oauth-proxy
    image: oauth2-proxy:latest
    args:
    - --upstream=http://localhost:8080
    - --provider=google
    - --email-domain=company.com
    ports:
    - containerPort: 4180
  ```

  **Benefits:**
  + Separation of concerns
  + Reusable across services
  + Independent deployment
  + Language agnostic

</details>

<details>
  <summary>API Composition Pattern</summary>
  <br/>

  **API Composition** aggregates data from multiple services.

  **Problem:**

  ```
  Client needs:
  - Order details
  - Customer info
  - Product details
  - Shipping status
  
  But data is in 4 different services!
  ```

  **Solution:**

  ```
  Client ──▶ API Composer ──┬──▶ Order Service
                            ├──▶ Customer Service
                            ├──▶ Product Service
                            └──▶ Shipping Service
  ```

  **Implementation:**

  ```java
  @RestController
  @RequestMapping("/api/orders")
  public class OrderCompositionController {
      
      @Autowired
      private OrderService orderService;
      
      @Autowired
      private CustomerService customerService;
      
      @Autowired
      private ProductService productService;
      
      @Autowired
      private ShippingService shippingService;
      
      @GetMapping("/{orderId}/details")
      public OrderDetails getOrderDetails(@PathVariable Long orderId) {
          // 1. Get order
          Order order = orderService.getOrder(orderId);
          
          // 2. Get customer (parallel)
          CompletableFuture<Customer> customerFuture = CompletableFuture
              .supplyAsync(() -> customerService.getCustomer(order.getCustomerId()));
          
          // 3. Get products (parallel)
          CompletableFuture<List<Product>> productsFuture = CompletableFuture
              .supplyAsync(() -> {
                  List<Long> productIds = order.getItems().stream()
                      .map(OrderItem::getProductId)
                      .collect(Collectors.toList());
                  return productService.getProducts(productIds);
              });
          
          // 4. Get shipping (parallel)
          CompletableFuture<ShippingInfo> shippingFuture = CompletableFuture
              .supplyAsync(() -> shippingService.getShippingInfo(orderId));
          
          // 5. Wait for all
          CompletableFuture.allOf(customerFuture, productsFuture, shippingFuture).join();
          
          // 6. Compose response
          return OrderDetails.builder()
              .order(order)
              .customer(customerFuture.join())
              .products(productsFuture.join())
              .shipping(shippingFuture.join())
              .build();
      }
  }
  ```

  **With Resilience:**

  ```java
  @Service
  public class ResilientOrderComposer {
      
      @CircuitBreaker(name = "orderComposer", fallbackMethod = "getOrderDetailsFallback")
      @Retry(name = "orderComposer")
      @Timeout(duration = 5, unit = ChronoUnit.SECONDS)
      public OrderDetails getOrderDetails(Long orderId) {
          Order order = orderService.getOrder(orderId);
          
          // Parallel calls with fallbacks
          Customer customer = getCustomerWithFallback(order.getCustomerId());
          List<Product> products = getProductsWithFallback(order.getItems());
          ShippingInfo shipping = getShippingWithFallback(orderId);
          
          return new OrderDetails(order, customer, products, shipping);
      }
      
      private Customer getCustomerWithFallback(Long customerId) {
          try {
              return customerService.getCustomer(customerId);
          } catch (Exception e) {
              // Return minimal customer info
              return Customer.builder()
                  .id(customerId)
                  .name("Customer #" + customerId)
                  .build();
          }
      }
      
      private OrderDetails getOrderDetailsFallback(Long orderId, Exception e) {
          // Return partial data
          Order order = orderService.getOrder(orderId);
          return OrderDetails.builder()
              .order(order)
              .error("Some details unavailable")
              .build();
      }
  }
  ```

  **GraphQL Alternative:**

  ```java
  @Component
  public class OrderGraphQLResolver implements GraphQLQueryResolver {
      
      public Order order(Long id) {
          return orderService.getOrder(id);
      }
      
      // Nested resolvers
      public Customer customer(Order order) {
          return customerService.getCustomer(order.getCustomerId());
      }
      
      public List<Product> products(Order order) {
          List<Long> productIds = order.getItems().stream()
              .map(OrderItem::getProductId)
              .collect(Collectors.toList());
          return productService.getProducts(productIds);
      }
      
      public ShippingInfo shipping(Order order) {
          return shippingService.getShippingInfo(order.getId());
      }
  }
  ```

  ```graphql
  # Client queries exactly what it needs
  query {
    order(id: 123) {
      id
      total
      customer {
        name
        email
      }
      products {
        name
        price
      }
      shipping {
        status
        estimatedDelivery
      }
    }
  }
  ```

</details>

<details>
  <summary>Best Practices</summary>
  <br/>

  ```java
  // ✅ DO: Use Outbox Pattern for reliable events
  @Transactional
  public void createOrder(OrderRequest request) {
      Order order = orderRepository.save(new Order(request));
      outboxRepository.save(new OutboxEvent(order));
  }
  
  // ✅ DO: Use BFF for different clients
  // /api/web/dashboard - Full data for web
  // /api/mobile/dashboard - Minimal data for mobile
  
  // ✅ DO: Use Anti-Corruption Layer for legacy systems
  @Service
  public class LegacyAdapter {
      public Order getOrder(Long id) {
          LegacyOrder legacy = legacyClient.getOrder(id);
          return translateToDomain(legacy);
      }
  }
  
  // ✅ DO: Use Strangler Fig for migration
  // Gradually extract services from monolith
  // Route new features to microservices
  
  // ✅ DO: Use API Composition with resilience
  @CircuitBreaker(fallbackMethod = "fallback")
  public OrderDetails getDetails(Long orderId) {
      // Aggregate from multiple services
  }
  
  // ✅ DO: Use Sidecar for cross-cutting concerns
  // Logging, monitoring, security in sidecar
  
  // ❌ DON'T: Make synchronous calls in chain
  // Service A → Service B → Service C (slow!)
  // Use async messaging or parallel calls
  
  // ❌ DON'T: Share database between services
  // Each service should own its data
  
  // ❌ DON'T: Expose internal domain model
  // Use DTOs and ACL for external APIs
  ```

  **Summary:**
  + **Outbox Pattern** - Reliable event publishing with database updates
  + **Strangler Fig** - Gradual migration from monolith to microservices
  + **BFF** - Separate API gateways for different client types
  + **Anti-Corruption Layer** - Translate between different domain models
  + **Sidecar** - Deploy helper components alongside main service
  + **API Composition** - Aggregate data from multiple services
  + Use **parallel calls** for better performance
  + Implement **fallbacks** for resilience
  + **Isolate** legacy system complexity with ACL
  + **Gradually migrate** using Strangler Fig pattern

</details>

