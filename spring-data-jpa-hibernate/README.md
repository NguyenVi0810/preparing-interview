# Spring Data JPA / Hibernate - Annotations Summary

<details>
  <summary><b>Entity Basics Annotations</b></summary>
  <br/>

  **Entity Definition:**
  + <code>@Entity</code> - Marks a class as a JPA entity
  + <code>@Table</code> - Specifies table name, schema, indexes, and constraints

  **Primary Key:**
  + <code>@Id</code> - Marks the primary key field
  + <code>@GeneratedValue</code> - Specifies primary key generation strategy
  + <code>@SequenceGenerator</code> - Configures sequence generator for SEQUENCE strategy
  + <code>@TableGenerator</code> - Configures table generator for TABLE strategy
  + <code>@GenericGenerator</code> - Custom ID generator (e.g., UUID)

  **Column Mapping:**
  + <code>@Column</code> - Customizes column mapping (name, nullable, unique, length, etc.)
  + <code>@Transient</code> - Marks field as non-persistent (not stored in database)
  + <code>@Lob</code> - Marks field as large object (TEXT, CLOB, BLOB)
  + <code>@Enumerated</code> - Maps enum fields (STRING or ORDINAL)
  + <code>@Temporal</code> - Maps legacy Date/Calendar fields (DATE, TIME, TIMESTAMP)

  **Composite Keys:**
  + <code>@EmbeddedId</code> - Marks embedded composite key
  + <code>@Embeddable</code> - Marks class as embeddable composite key
  + <code>@IdClass</code> - Specifies composite key class

  **Read section:** `entity-basics-section.txt`

</details>

<details>
  <summary><b>Entity Lifecycle Annotations</b></summary>
  <br/>

  **Lifecycle Callbacks:**
  + <code>@PrePersist</code> - Executed before INSERT operation
  + <code>@PostPersist</code> - Executed after INSERT operation
  + <code>@PreUpdate</code> - Executed before UPDATE operation
  + <code>@PostUpdate</code> - Executed after UPDATE operation
  + <code>@PreRemove</code> - Executed before DELETE operation
  + <code>@PostRemove</code> - Executed after DELETE operation
  + <code>@PostLoad</code> - Executed after entity is loaded from database

  **Entity States:**
  + **Transient** - New entity, not managed, not in database
  + **Persistent** - Managed by EntityManager, changes tracked automatically
  + **Detached** - Was managed, no longer tracked, changes not saved
  + **Removed** - Marked for deletion, will be deleted on commit

  **EntityManager Operations:**
  + `persist()` - Transient → Persistent (save new entity)
  + `merge()` - Detached → Persistent (reattach entity)
  + `remove()` - Persistent → Removed (delete entity)
  + `detach()` - Persistent → Detached (stop tracking)
  + `refresh()` - Reload entity from database
  + `flush()` - Synchronize persistence context with database

  **Read section:** `entity-lifecycle-section.txt`

</details>

<details>
  <summary><b>Repository Pattern Annotations</b></summary>
  <br/>

  **Repository Interfaces:**
  + `Repository` - Marker interface (no methods)
  + `CrudRepository` - Basic CRUD operations
  + `PagingAndSortingRepository` - Adds pagination and sorting
  + `JpaRepository` - JPA-specific methods and batch operations
  + `JpaSpecificationExecutor` - Dynamic query building with Specifications

  **Query Methods:**
  + <code>@Query</code> - Custom JPQL or native SQL query
  + <code>@Modifying</code> - Marks query as UPDATE or DELETE operation
  + <code>@Param</code> - Names query parameter
  + <code>@Transactional</code> - Required for modifying operations

  **Custom Repository:**
  + <code>@Repository</code> - Marks custom repository implementation
  + <code>@PersistenceContext</code> - Injects EntityManager

  **Query Method Keywords:**
  + `findBy`, `readBy`, `getBy`, `queryBy` - Find entities
  + `countBy` - Count entities
  + `existsBy` - Check existence
  + `deleteBy`, `removeBy` - Delete entities
  + `And`, `Or` - Logical operators
  + `GreaterThan`, `LessThan`, `Between` - Comparison operators
  + `Like`, `Containing`, `StartingWith`, `EndingWith` - String matching
  + `In`, `NotIn` - Collection membership
  + `IsNull`, `IsNotNull` - Null checks
  + `True`, `False` - Boolean checks
  + `OrderBy` - Sorting
  + `First`, `Top` - Limit results
  + `Distinct` - Remove duplicates

  **Read section:** `repository-pattern-section.txt`

</details>

<details>
  <summary><b>Entity Relationships Annotations</b></summary>
  <br/>

  **Relationship Types:**
  + <code>@OneToOne</code> - One entity relates to one other entity
  + <code>@OneToMany</code> - One entity relates to many other entities
  + <code>@ManyToOne</code> - Many entities relate to one entity
  + <code>@ManyToMany</code> - Many entities relate to many other entities

  **Relationship Configuration:**
  + <code>@JoinColumn</code> - Specifies foreign key column (owning side)
  + <code>@JoinTable</code> - Specifies join table for @ManyToMany
  + <code>mappedBy</code> - Specifies inverse side of bidirectional relationship
  + <code>@MapsId</code> - Shares primary key between entities

  **Fetch Types:**
  + <code>FetchType.LAZY</code> - Load related entities on demand (default for @OneToMany, @ManyToMany)
  + <code>FetchType.EAGER</code> - Load related entities immediately (default for @ManyToOne, @OneToOne)

  **Cascade Types:**
  + <code>CascadeType.PERSIST</code> - Save children when saving parent
  + <code>CascadeType.MERGE</code> - Merge children when merging parent
  + <code>CascadeType.REMOVE</code> - Delete children when deleting parent
  + <code>CascadeType.REFRESH</code> - Refresh children when refreshing parent
  + <code>CascadeType.DETACH</code> - Detach children when detaching parent
  + <code>CascadeType.ALL</code> - All cascade operations

  **Other:**
  + <code>orphanRemoval</code> - Delete children when removed from collection
  + <code>optional</code> - Whether relationship can be null

  **Read section:** `entity-relationships-section.txt`

</details>

<details>
  <summary><b>Fetch Types</b></summary>
  <br/>

  **Fetch Types:**
  + <code>FetchType.LAZY</code> - Load related entities on-demand (default for @OneToMany, @ManyToMany)
  + <code>FetchType.EAGER</code> - Load related entities immediately (default for @ManyToOne, @OneToOne)

  **Lazy Loading Solutions:**
  + **Fetch Join** - Use JPQL `LEFT JOIN FETCH` to load in single query
  + **@EntityGraph** - Specify which relationships to load
  + **Hibernate.initialize()** - Force initialization within transaction
  + **DTO Projection** - Fetch only needed data
  + **@BatchSize** - Reduce N+1 problem by batch loading

  **Common Issues:**
  + **LazyInitializationException** - Accessing lazy collection outside transaction
  + **N+1 Problem** - Loading collection triggers N additional queries
  + **Proxy Objects** - Hibernate creates proxy for lazy loading

  **Annotations:**
  + <code>@EntityGraph</code> - Specify fetch plan for query
  + <code>@NamedEntityGraph</code> - Define reusable entity graph
  + <code>@NamedAttributeNode</code> - Specify attributes to fetch
  + <code>@BatchSize</code> - Batch fetch lazy collections

  **Configuration:**
  + `spring.jpa.open-in-view` - Keep session open until view rendering (not recommended)

  **Read section:** `fetch-types-section.txt`

</details>

## Fetch Types

<details>
  <summary>LAZY vs EAGER Loading</summary>
  <br/>

  Fetch type determines when related entities are loaded from the database.

  **LAZY Loading:**
  + Related entities loaded on-demand (when accessed)
  + Default for @OneToMany and @ManyToMany
  + Better performance (loads only what you need)
  + Requires active session/transaction

  **EAGER Loading:**
  + Related entities loaded immediately with parent
  + Default for @ManyToOne and @OneToOne
  + Can cause performance issues (loads unnecessary data)
  + Works outside transaction

  **Default fetch types:**

  ```
  @OneToOne   → EAGER
  @ManyToOne  → EAGER
  @OneToMany  → LAZY
  @ManyToMany → LAZY
  ```

</details>

<details>
  <summary>LAZY Loading Example</summary>
  <br/>

  **Entity with LAZY relationship:**

  ```java
  @Entity
  public class User {
      
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;
      
      private String username;
      
      @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
      private List<Order> orders = new ArrayList<>();
  }
  
  @Entity
  public class Order {
      
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;
      
      private BigDecimal total;
      
      @ManyToOne
      @JoinColumn(name = "user_id")
      private User user;
  }
  ```

  **LAZY loading behavior:**

  ```java
  @Service
  public class UserService {
      
      @Autowired
      private UserRepository userRepository;
      
      @Transactional
      public void lazyLoadingExample() {
          // Load user - executes SELECT for User only
          User user = userRepository.findById(1L).orElseThrow();
          // SQL: SELECT * FROM users WHERE id = 1
          
          System.out.println(user.getUsername());  // Works fine
          
          // Access orders - triggers second query
          List<Order> orders = user.getOrders();
          // SQL: SELECT * FROM orders WHERE user_id = 1
          
          System.out.println(orders.size());  // Now orders are loaded
      }
  }
  ```

  **Benefits of LAZY:**
  + Loads only needed data
  + Better performance for large collections
  + Reduces memory usage

  **Drawbacks of LAZY:**
  + Requires active session
  + Can cause LazyInitializationException
  + Can cause N+1 problem

</details>

<details>
  <summary>EAGER Loading Example</summary>
  <br/>

  **Entity with EAGER relationship:**

  ```java
  @Entity
  public class Order {
      
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;
      
      private BigDecimal total;
      
      @ManyToOne(fetch = FetchType.EAGER)
      @JoinColumn(name = "user_id")
      private User user;
  }
  ```

  **EAGER loading behavior:**

  ```java
  @Service
  public class OrderService {
      
      @Autowired
      private OrderRepository orderRepository;
      
      public void eagerLoadingExample() {
          // Load order - executes JOIN to load User immediately
          Order order = orderRepository.findById(1L).orElseThrow();
          // SQL: SELECT o.*, u.* FROM orders o 
          //      LEFT JOIN users u ON o.user_id = u.id 
          //      WHERE o.id = 1
          
          // User already loaded - no additional query
          System.out.println(order.getUser().getUsername());
      }
  }
  ```

  **Benefits of EAGER:**
  + No LazyInitializationException
  + Works outside transaction
  + Simpler code

  **Drawbacks of EAGER:**
  + Loads unnecessary data
  + Performance issues with large collections
  + Can cause memory issues
  + Multiple EAGER relationships cause Cartesian product

</details>

<details>
  <summary>Proxy Objects</summary>
  <br/>

  Hibernate uses proxy objects for LAZY loading.

  **What is a proxy?**
  + Placeholder object that looks like the real entity
  + Contains only the ID initially
  + Loads actual data when you access fields
  + Created by Hibernate using bytecode manipulation

  **Proxy example:**

  ```java
  @Entity
  public class Order {
      
      @Id
      private Long id;
      
      @ManyToOne(fetch = FetchType.LAZY)
      @JoinColumn(name = "user_id")
      private User user;  // This is a proxy
  }
  
  @Transactional
  public void proxyExample() {
      Order order = orderRepository.findById(1L).orElseThrow();
      
      User user = order.getUser();
      // user is a proxy object (User$HibernateProxy$...)
      
      System.out.println(user.getClass().getName());
      // Output: com.example.User$HibernateProxy$abc123
      
      // Accessing field triggers database query
      System.out.println(user.getUsername());
      // SQL: SELECT * FROM users WHERE id = ?
  }
  ```

  **Check if object is proxy:**

  ```java
  @Transactional
  public void checkProxy() {
      Order order = orderRepository.findById(1L).orElseThrow();
      User user = order.getUser();
      
      // Method 1: Check class name
      boolean isProxy = user.getClass().getName().contains("HibernateProxy");
      
      // Method 2: Use Hibernate utility
      boolean isProxy2 = user instanceof HibernateProxy;
      
      // Method 3: Check if initialized
      boolean isInitialized = Hibernate.isInitialized(user);
  }
  ```

  **Force proxy initialization:**

  ```java
  @Transactional
  public void initializeProxy() {
      Order order = orderRepository.findById(1L).orElseThrow();
      User user = order.getUser();
      
      // Force initialization
      Hibernate.initialize(user);
      
      // Now user is fully loaded
  }
  ```

  **getReferenceById() returns proxy:**

  ```java
  @Transactional
  public void referenceExample() {
      // Returns proxy without database query
      User userProxy = entityManager.getReference(User.class, 1L);
      
      // No query executed yet
      System.out.println("Got reference");
      
      // Query executed when accessing field
      System.out.println(userProxy.getUsername());
      // SQL: SELECT * FROM users WHERE id = 1
  }
  ```

</details>

<details>
  <summary>LazyInitializationException</summary>
  <br/>

  **LazyInitializationException** occurs when accessing LAZY collection outside transaction.

  **Common scenario:**

  ```java
  @Service
  public class UserService {
      
      @Autowired
      private UserRepository userRepository;
      
      @Transactional
      public User getUser(Long id) {
          return userRepository.findById(id).orElseThrow();
      }  // Transaction ends here, session closes
  }
  
  @RestController
  public class UserController {
      
      @Autowired
      private UserService userService;
      
      @GetMapping("/users/{id}")
      public UserDTO getUser(@PathVariable Long id) {
          User user = userService.getUser(id);  // User is now detached
          
          // LazyInitializationException!
          List<Order> orders = user.getOrders();
          // Error: could not initialize proxy - no Session
          
          return new UserDTO(user);
      }
  }
  ```

  **Why it happens:**
  + LAZY collection not loaded within transaction
  + Transaction ends, session closes
  + Accessing collection outside session fails

</details>

<details>
  <summary>Solution 1: Fetch Join</summary>
  <br/>

  Use JPQL fetch join to load related entities.

  **Repository with fetch join:**

  ```java
  public interface UserRepository extends JpaRepository<User, Long> {
      
      @Query("SELECT u FROM User u LEFT JOIN FETCH u.orders WHERE u.id = :id")
      Optional<User> findByIdWithOrders(@Param("id") Long id);
      
      @Query("SELECT u FROM User u LEFT JOIN FETCH u.orders")
      List<User> findAllWithOrders();
      
      // Multiple joins
      @Query("SELECT u FROM User u " +
             "LEFT JOIN FETCH u.orders o " +
             "LEFT JOIN FETCH o.items " +
             "WHERE u.id = :id")
      Optional<User> findByIdWithOrdersAndItems(@Param("id") Long id);
  }
  ```

  **Usage:**

  ```java
  @Service
  public class UserService {
      
      @Autowired
      private UserRepository userRepository;
      
      @Transactional
      public User getUserWithOrders(Long id) {
          return userRepository.findByIdWithOrders(id).orElseThrow();
      }  // Orders are loaded, no LazyInitializationException
  }
  
  @RestController
  public class UserController {
      
      @GetMapping("/users/{id}")
      public UserDTO getUser(@PathVariable Long id) {
          User user = userService.getUserWithOrders(id);
          
          // Works fine - orders already loaded
          List<Order> orders = user.getOrders();
          
          return new UserDTO(user);
      }
  }
  ```

</details>

<details>
  <summary>Solution 2: Entity Graph</summary>
  <br/>

  Use @EntityGraph to specify which relationships to load.

  **Define entity graph:**

  ```java
  @Entity
  @NamedEntityGraph(
      name = "User.orders",
      attributeNodes = @NamedAttributeNode("orders")
  )
  public class User {
      
      @Id
      private Long id;
      
      @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
      private List<Order> orders;
  }
  ```

  **Use entity graph in repository:**

  ```java
  public interface UserRepository extends JpaRepository<User, Long> {
      
      @EntityGraph(value = "User.orders")
      Optional<User> findById(Long id);
      
      @EntityGraph(attributePaths = {"orders"})
      List<User> findAll();
      
      @EntityGraph(attributePaths = {"orders", "orders.items"})
      Optional<User> findByUsername(String username);
  }
  ```

  **Usage:**

  ```java
  @Service
  public class UserService {
      
      @Autowired
      private UserRepository userRepository;
      
      @Transactional
      public User getUser(Long id) {
          return userRepository.findById(id).orElseThrow();
          // Orders loaded due to @EntityGraph
      }
  }
  ```

</details>

<details>
  <summary>Solution 3: Force Initialization</summary>
  <br/>

  Access collection within transaction to force loading.

  **Method 1: Access collection:**

  ```java
  @Service
  public class UserService {
      
      @Autowired
      private UserRepository userRepository;
      
      @Transactional
      public User getUser(Long id) {
          User user = userRepository.findById(id).orElseThrow();
          
          // Force load orders
          user.getOrders().size();
          
          return user;
      }
  }
  ```

  **Method 2: Use Hibernate.initialize():**

  ```java
  @Service
  public class UserService {
      
      @Autowired
      private UserRepository userRepository;
      
      @Transactional
      public User getUser(Long id) {
          User user = userRepository.findById(id).orElseThrow();
          
          // Force initialization
          Hibernate.initialize(user.getOrders());
          
          return user;
      }
  }
  ```

  **Method 3: Iterate collection:**

  ```java
  @Transactional
  public User getUser(Long id) {
      User user = userRepository.findById(id).orElseThrow();
      
      // Force load by iterating
      for (Order order : user.getOrders()) {
          // Just accessing is enough
      }
      
      return user;
  }
  ```

</details>

<details>
  <summary>Solution 4: Open Session in View</summary>
  <br/>

  Keep session open until view rendering completes.

  **Enable in application.properties:**

  ```properties
  spring.jpa.open-in-view=true
  ```

  **How it works:**
  + Opens Hibernate session at start of request
  + Keeps session open until response is sent
  + Allows lazy loading in controller/view layer

  **Pros:**
  + Simple solution
  + No code changes needed
  + Works with existing code

  **Cons:**
  + Performance issues (long-lived sessions)
  + Database connections held longer
  + Hides lazy loading problems
  + Not recommended for production

  **Better approach - disable and fix properly:**

  ```properties
  spring.jpa.open-in-view=false
  ```

</details>

<details>
  <summary>Solution 5: DTO Projection</summary>
  <br/>

  Use DTOs to fetch only needed data.

  **DTO class:**

  ```java
  public class UserDTO {
      private Long id;
      private String username;
      private List<OrderDTO> orders;
      
      public UserDTO(Long id, String username) {
          this.id = id;
          this.username = username;
      }
  }
  
  public class OrderDTO {
      private Long id;
      private BigDecimal total;
      
      public OrderDTO(Long id, BigDecimal total) {
          this.id = id;
          this.total = total;
      }
  }
  ```

  **Repository with DTO projection:**

  ```java
  public interface UserRepository extends JpaRepository<User, Long> {
      
      @Query("SELECT new com.example.dto.UserDTO(u.id, u.username) " +
             "FROM User u WHERE u.id = :id")
      UserDTO findUserDTOById(@Param("id") Long id);
      
      @Query("SELECT new com.example.dto.OrderDTO(o.id, o.total) " +
             "FROM Order o WHERE o.user.id = :userId")
      List<OrderDTO> findOrderDTOsByUserId(@Param("userId") Long userId);
  }
  ```

  **Service:**

  ```java
  @Service
  public class UserService {
      
      @Autowired
      private UserRepository userRepository;
      
      @Transactional(readOnly = true)
      public UserDTO getUserWithOrders(Long id) {
          UserDTO user = userRepository.findUserDTOById(id);
          List<OrderDTO> orders = userRepository.findOrderDTOsByUserId(id);
          user.setOrders(orders);
          return user;
      }
  }
  ```

</details>

<details>
  <summary>N+1 Problem</summary>
  <br/>

  N+1 problem occurs when loading collection triggers additional queries.

  **Example of N+1 problem:**

  ```java
  @Service
  public class UserService {
      
      @Autowired
      private UserRepository userRepository;
      
      @Transactional
      public void printAllUsersWithOrders() {
          List<User> users = userRepository.findAll();
          // SQL: SELECT * FROM users (1 query)
          
          for (User user : users) {
              System.out.println(user.getUsername());
              System.out.println(user.getOrders().size());
              // SQL: SELECT * FROM orders WHERE user_id = ? (N queries)
          }
          
          // Total: 1 + N queries (if 100 users, 101 queries!)
      }
  }
  ```

  **Solution 1: Fetch join:**

  ```java
  @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.orders")
  List<User> findAllWithOrders();
  
  @Transactional
  public void printAllUsersWithOrders() {
      List<User> users = userRepository.findAllWithOrders();
      // SQL: SELECT u.*, o.* FROM users u LEFT JOIN orders o ON u.id = o.user_id
      // Only 1 query!
      
      for (User user : users) {
          System.out.println(user.getOrders().size());
      }
  }
  ```

  **Solution 2: Entity graph:**

  ```java
  @EntityGraph(attributePaths = {"orders"})
  List<User> findAll();
  ```

  **Solution 3: Batch fetching:**

  ```java
  @Entity
  public class User {
      
      @OneToMany(mappedBy = "user")
      @BatchSize(size = 10)  // Load 10 users' orders at once
      private List<Order> orders;
  }
  ```

</details>

<details>
  <summary>Best Practices</summary>
  <br/>

  ```java
  // ✅ DO: Use LAZY by default
  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  private List<Order> orders;
  
  // ❌ DON'T: Use EAGER for collections
  @OneToMany(fetch = FetchType.EAGER)
  private List<Order> orders;  // Performance issues
  
  // ✅ DO: Use fetch join when you need related entities
  @Query("SELECT u FROM User u LEFT JOIN FETCH u.orders WHERE u.id = :id")
  Optional<User> findByIdWithOrders(@Param("id") Long id);
  
  // ✅ DO: Use @EntityGraph for flexible loading
  @EntityGraph(attributePaths = {"orders"})
  Optional<User> findById(Long id);
  
  // ✅ DO: Force initialization within transaction
  @Transactional
  public User getUser(Long id) {
      User user = userRepository.findById(id).orElseThrow();
      user.getOrders().size();  // Force load
      return user;
  }
  
  // ❌ DON'T: Access lazy collection outside transaction
  public User getUser(Long id) {
      User user = userRepository.findById(id).orElseThrow();
      return user;
  }
  // Later: user.getOrders() → LazyInitializationException!
  
  // ✅ DO: Use DTO projection for read-only operations
  @Query("SELECT new com.example.dto.UserDTO(u.id, u.username) FROM User u")
  List<UserDTO> findAllUserDTOs();
  
  // ✅ DO: Use @Transactional(readOnly = true) for read operations
  @Transactional(readOnly = true)
  public User getUser(Long id) {
      return userRepository.findById(id).orElseThrow();
  }
  
  // ✅ DO: Use batch fetching to reduce N+1 problem
  @OneToMany(mappedBy = "user")
  @BatchSize(size = 10)
  private List<Order> orders;
  
  // ❌ DON'T: Use Open Session in View in production
  spring.jpa.open-in-view=false  // Disable it
  
  // ✅ DO: Use DISTINCT with fetch join for collections
  @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.orders")
  List<User> findAllWithOrders();
  
  // ✅ DO: Check if collection is initialized
  if (Hibernate.isInitialized(user.getOrders())) {
      // Safe to access
  }
  
  // ✅ DO: Use multiple queries instead of complex joins
  @Transactional(readOnly = true)
  public UserDTO getUserWithOrders(Long id) {
      User user = userRepository.findById(id).orElseThrow();
      List<Order> orders = orderRepository.findByUserId(id);
      return new UserDTO(user, orders);
  }
  ```

  **Key Points**
  <br/>

  + **LAZY**: Load on-demand (default for @OneToMany, @ManyToMany)
  + **EAGER**: Load immediately (default for @ManyToOne, @OneToOne)
  + **Proxy**: Placeholder object for lazy loading
  + **LazyInitializationException**: Accessing lazy collection outside transaction
  + Use **fetch join** to load related entities in single query
  + Use **@EntityGraph** for flexible entity loading
  + Use **Hibernate.initialize()** to force loading within transaction
  + Use **DTO projection** for read-only operations
  + **N+1 problem**: Loading collection triggers N additional queries
  + Use **@BatchSize** to reduce N+1 problem
  + Use **DISTINCT** with fetch join to avoid duplicates
  + **Disable Open Session in View** in production
  + Always use **@Transactional** when accessing lazy collections
  + Use **@Transactional(readOnly = true)** for read operations
  + Check if initialized with **Hibernate.isInitialized()**
  + Prefer **LAZY** over EAGER for better performance
  + Use **fetch join** or **entity graph** instead of changing to EAGER

</details>

## Cascade Types

<details>
  <summary>What is Cascading?</summary>
  <br/>

  Cascading propagates operations from parent entity to related child entities.

  **Why use cascading?**
  + Automatically manage related entities
  + Reduce boilerplate code
  + Maintain data consistency
  + Simplify entity lifecycle management

  **Example without cascading:**

  ```java
  @Transactional
  public void createUserWithOrders() {
      User user = new User("john");
      Order order1 = new Order(new BigDecimal("99.99"));
      Order order2 = new Order(new BigDecimal("49.99"));
      
      // Must save each entity manually
      userRepository.save(user);
      orderRepository.save(order1);
      orderRepository.save(order2);
  }
  ```

  **Example with cascading:**

  ```java
  @Entity
  public class User {
      @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
      private List<Order> orders;
  }
  
  @Transactional
  public void createUserWithOrders() {
      User user = new User("john");
      Order order1 = new Order(new BigDecimal("99.99"));
      Order order2 = new Order(new BigDecimal("49.99"));
      
      user.addOrder(order1);
      user.addOrder(order2);
      
      userRepository.save(user);  // Saves user AND orders automatically
  }
  ```

</details>

<details>
  <summary>CascadeType.PERSIST</summary>
  <br/>

  **PERSIST** cascades the persist operation (save new entities).

  **When parent is persisted, children are also persisted.**

  **Example:**

  ```java
  @Entity
  public class User {
      
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;
      
      private String username;
      
      @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
      private List<Order> orders = new ArrayList<>();
      
      public void addOrder(Order order) {
          orders.add(order);
          order.setUser(this);
      }
  }
  
  @Entity
  public class Order {
      
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;
      
      private BigDecimal total;
      
      @ManyToOne
      @JoinColumn(name = "user_id")
      private User user;
  }
  ```

  **Usage:**

  ```java
  @Service
  public class UserService {
      
      @Autowired
      private UserRepository userRepository;
      
      @Transactional
      public void createUserWithOrders() {
          // Create user (Transient)
          User user = new User("john");
          
          // Create orders (Transient)
          Order order1 = new Order(new BigDecimal("99.99"));
          Order order2 = new Order(new BigDecimal("49.99"));
          
          // Add orders to user
          user.addOrder(order1);
          user.addOrder(order2);
          
          // Save user - orders are automatically persisted
          userRepository.save(user);
          // SQL: INSERT INTO users ...
          // SQL: INSERT INTO orders ... (order1)
          // SQL: INSERT INTO orders ... (order2)
      }
  }
  ```

  **When to use:**
  + Parent-child relationships where children don't exist without parent
  + Creating new entities together
  + Simplifying save operations

  **Important notes:**
  + Only cascades persist() operation
  + Does NOT cascade updates or deletes
  + Children must be in Transient state

</details>

<details>
  <summary>CascadeType.MERGE</summary>
  <br/>

  **MERGE** cascades the merge operation (reattach detached entities).

  **When parent is merged, children are also merged.**

  **Example:**

  ```java
  @Entity
  public class User {
      
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;
      
      private String username;
      
      @OneToMany(mappedBy = "user", cascade = CascadeType.MERGE)
      private List<Order> orders = new ArrayList<>();
  }
  ```

  **Usage:**

  ```java
  @Service
  public class UserService {
      
      @Autowired
      private UserRepository userRepository;
      
      @Transactional
      public User updateUserWithOrders(User detachedUser) {
          // detachedUser and its orders are Detached
          
          // Modify user
          detachedUser.setUsername("john_updated");
          
          // Modify orders
          for (Order order : detachedUser.getOrders()) {
              order.setTotal(order.getTotal().multiply(new BigDecimal("1.1")));
          }
          
          // Merge user - orders are automatically merged
          User managedUser = userRepository.save(detachedUser);
          // SQL: UPDATE users SET username = ? WHERE id = ?
          // SQL: UPDATE orders SET total = ? WHERE id = ? (for each order)
          
          return managedUser;
      }
  }
  ```

  **Common scenario - REST API:**

  ```java
  @RestController
  public class UserController {
      
      @Autowired
      private UserService userService;
      
      @PutMapping("/users/{id}")
      public User updateUser(@PathVariable Long id, @RequestBody User user) {
          // user is Detached (comes from JSON)
          user.setId(id);
          return userService.updateUser(user);
      }
  }
  
  @Service
  public class UserService {
      
      @Transactional
      public User updateUser(User detachedUser) {
          // Merge cascades to orders
          return userRepository.save(detachedUser);
      }
  }
  ```

  **When to use:**
  + Updating detached entities
  + REST APIs receiving JSON data
  + Reattaching entities from cache

  **Important notes:**
  + Only cascades merge() operation
  + Works with Detached entities
  + Returns new managed instance

</details>

<details>
  <summary>CascadeType.REMOVE</summary>
  <br/>

  **REMOVE** cascades the remove operation (delete entities).

  **When parent is deleted, children are also deleted.**

  **Example:**

  ```java
  @Entity
  public class User {
      
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;
      
      private String username;
      
      @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
      private List<Order> orders = new ArrayList<>();
  }
  ```

  **Usage:**

  ```java
  @Service
  public class UserService {
      
      @Autowired
      private UserRepository userRepository;
      
      @Transactional
      public void deleteUser(Long userId) {
          User user = userRepository.findById(userId).orElseThrow();
          
          // Delete user - orders are automatically deleted
          userRepository.delete(user);
          // SQL: DELETE FROM orders WHERE user_id = ?
          // SQL: DELETE FROM users WHERE id = ?
      }
  }
  ```

  **Dangerous example:**

  ```java
  @Entity
  public class Order {
      
      @ManyToOne(cascade = CascadeType.REMOVE)  // DANGEROUS!
      @JoinColumn(name = "user_id")
      private User user;
  }
  
  @Transactional
  public void deleteOrder(Long orderId) {
      Order order = orderRepository.findById(orderId).orElseThrow();
      
      // Delete order - user is also deleted!
      orderRepository.delete(order);
      // SQL: DELETE FROM orders WHERE id = ?
      // SQL: DELETE FROM users WHERE id = ?  // Deletes user!
  }
  ```

  **When to use:**
  + Parent-child relationships where children should be deleted with parent
  + Composition relationships (strong ownership)
  + Examples: User → Orders, Post → Comments

  **When NOT to use:**
  + @ManyToOne relationships (usually dangerous)
  + Shared entities (e.g., Product in Order)
  + Entities referenced by multiple parents

  **Important notes:**
  + Be careful with @ManyToOne cascade REMOVE
  + Can cause unintended deletions
  + Consider using orphanRemoval instead

</details>

<details>
  <summary>CascadeType.REFRESH</summary>
  <br/>

  **REFRESH** cascades the refresh operation (reload from database).

  **When parent is refreshed, children are also refreshed.**

  **Example:**

  ```java
  @Entity
  public class User {
      
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;
      
      private String username;
      
      @OneToMany(mappedBy = "user", cascade = CascadeType.REFRESH)
      private List<Order> orders = new ArrayList<>();
  }
  ```

  **Usage:**

  ```java
  @Service
  public class UserService {
      
      @Autowired
      private UserRepository userRepository;
      
      @PersistenceContext
      private EntityManager entityManager;
      
      @Transactional
      public void refreshExample() {
          User user = userRepository.findById(1L).orElseThrow();
          
          // Modify user and orders in memory
          user.setUsername("modified");
          user.getOrders().get(0).setTotal(new BigDecimal("999.99"));
          
          // Refresh user - orders are also refreshed
          entityManager.refresh(user);
          // SQL: SELECT * FROM users WHERE id = ?
          // SQL: SELECT * FROM orders WHERE user_id = ?
          
          // Changes are discarded
          System.out.println(user.getUsername());  // Original value
          System.out.println(user.getOrders().get(0).getTotal());  // Original value
      }
  }
  ```

  **When to use:**
  + Discard in-memory changes
  + Reload data after external updates
  + Synchronize with database state

  **Important notes:**
  + Rarely used in practice
  + Discards all unsaved changes
  + Triggers SELECT queries

</details>

<details>
  <summary>CascadeType.DETACH</summary>
  <br/>

  **DETACH** cascades the detach operation (remove from persistence context).

  **When parent is detached, children are also detached.**

  **Example:**

  ```java
  @Entity
  public class User {
      
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;
      
      private String username;
      
      @OneToMany(mappedBy = "user", cascade = CascadeType.DETACH)
      private List<Order> orders = new ArrayList<>();
  }
  ```

  **Usage:**

  ```java
  @Service
  public class UserService {
      
      @PersistenceContext
      private EntityManager entityManager;
      
      @Transactional
      public void detachExample() {
          User user = entityManager.find(User.class, 1L);  // Persistent
          
          // user and orders are Persistent
          System.out.println(entityManager.contains(user));  // true
          System.out.println(entityManager.contains(user.getOrders().get(0)));  // true
          
          // Detach user - orders are also detached
          entityManager.detach(user);
          
          // user and orders are Detached
          System.out.println(entityManager.contains(user));  // false
          System.out.println(entityManager.contains(user.getOrders().get(0)));  // false
          
          // Changes are not tracked
          user.setUsername("modified");  // NOT saved
          user.getOrders().get(0).setTotal(new BigDecimal("999.99"));  // NOT saved
      }
  }
  ```

  **When to use:**
  + Manually control entity lifecycle
  + Prevent automatic updates
  + Optimize memory usage

  **Important notes:**
  + Rarely used explicitly
  + Happens automatically when transaction ends
  + Changes to detached entities are not saved

</details>

<details>
  <summary>CascadeType.ALL</summary>
  <br/>

  **ALL** cascades all operations (PERSIST, MERGE, REMOVE, REFRESH, DETACH).

  **Most commonly used cascade type for parent-child relationships.**

  **Example:**

  ```java
  @Entity
  public class User {
      
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;
      
      private String username;
      
      @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
      private List<Order> orders = new ArrayList<>();
      
      public void addOrder(Order order) {
          orders.add(order);
          order.setUser(this);
      }
      
      public void removeOrder(Order order) {
          orders.remove(order);
          order.setUser(null);
      }
  }
  ```

  **Usage:**

  ```java
  @Service
  public class UserService {
      
      @Autowired
      private UserRepository userRepository;
      
      @Transactional
      public void cascadeAllExample() {
          // 1. PERSIST - Create user with orders
          User user = new User("john");
          Order order1 = new Order(new BigDecimal("99.99"));
          Order order2 = new Order(new BigDecimal("49.99"));
          
          user.addOrder(order1);
          user.addOrder(order2);
          
          userRepository.save(user);  // Saves user and orders
          
          // 2. MERGE - Update user and orders
          user.setUsername("john_updated");
          user.getOrders().get(0).setTotal(new BigDecimal("199.99"));
          
          userRepository.save(user);  // Updates user and orders
          
          // 3. REMOVE - Delete user and orders
          userRepository.delete(user);  // Deletes user and orders
      }
  }
  ```

  **When to use:**
  + Strong parent-child relationships
  + Children don't exist without parent
  + Examples: User → Profile, Order → OrderItems, Post → Comments

  **When NOT to use:**
  + Shared entities (e.g., Product in Order)
  + @ManyToOne relationships
  + Entities with independent lifecycle

  **Important notes:**
  + Most convenient but most powerful
  + Use carefully to avoid unintended operations
  + Combine with orphanRemoval for complete lifecycle management

</details>

<details>
  <summary>Multiple Cascade Types</summary>
  <br/>

  You can specify multiple cascade types.

  **Example:**

  ```java
  @Entity
  public class User {
      
      // Cascade PERSIST and MERGE only
      @OneToMany(
          mappedBy = "user",
          cascade = {CascadeType.PERSIST, CascadeType.MERGE}
      )
      private List<Order> orders = new ArrayList<>();
  }
  ```

  **Usage:**

  ```java
  @Transactional
  public void multipleCascadeExample() {
      User user = new User("john");
      Order order = new Order(new BigDecimal("99.99"));
      
      user.addOrder(order);
      
      // PERSIST cascades - order is saved
      userRepository.save(user);
      
      // MERGE cascades - order is updated
      user.setUsername("john_updated");
      order.setTotal(new BigDecimal("199.99"));
      userRepository.save(user);
      
      // REMOVE does NOT cascade - only user is deleted
      userRepository.delete(user);
      // Order remains in database!
  }
  ```

  **Common combinations:**

  ```java
  // Save and update, but don't delete
  @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private List<Order> orders;
  
  // Save only, no updates or deletes
  @OneToMany(cascade = CascadeType.PERSIST)
  private List<Order> orders;
  
  // Everything except remove
  @OneToMany(cascade = {
      CascadeType.PERSIST,
      CascadeType.MERGE,
      CascadeType.REFRESH,
      CascadeType.DETACH
  })
  private List<Order> orders;
  ```

</details>

<details>
  <summary>Cascade vs orphanRemoval</summary>
  <br/>

  **CascadeType.REMOVE** and **orphanRemoval** are different.

  **CascadeType.REMOVE:**
  + Deletes children when parent is deleted
  + Does NOT delete children when removed from collection

  **orphanRemoval:**
  + Deletes children when removed from collection
  + Does NOT delete children when parent is deleted (unless combined with cascade)

  **Example:**

  ```java
  @Entity
  public class User {
      
      // Only CascadeType.REMOVE
      @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
      private List<Order> orders1;
      
      // Only orphanRemoval
      @OneToMany(mappedBy = "user", orphanRemoval = true)
      private List<Order> orders2;
      
      // Both (recommended for parent-child)
      @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
      private List<Order> orders3;
  }
  ```

  **Behavior comparison:**

  ```java
  // Scenario 1: Delete parent
  @Transactional
  public void deleteParent() {
      User user = userRepository.findById(1L).orElseThrow();
      userRepository.delete(user);
  }
  
  // CascadeType.REMOVE: orders1 are deleted ✅
  // orphanRemoval: orders2 are NOT deleted ❌
  // Both: orders3 are deleted ✅
  
  // Scenario 2: Remove child from collection
  @Transactional
  public void removeChild() {
      User user = userRepository.findById(1L).orElseThrow();
      user.getOrders1().remove(0);
      user.getOrders2().remove(0);
      user.getOrders3().remove(0);
      userRepository.save(user);
  }
  
  // CascadeType.REMOVE: order NOT deleted ❌
  // orphanRemoval: order deleted ✅
  // Both: order deleted ✅
  ```

  **Recommendation:**

  ```java
  // For parent-child relationships, use both
  @OneToMany(
      mappedBy = "user",
      cascade = CascadeType.ALL,
      orphanRemoval = true
  )
  private List<Order> orders;
  ```

</details>

<details>
  <summary>Cascade Direction</summary>
  <br/>

  Cascade direction matters - only cascades from owning side to inverse side.

  **Correct usage:**

  ```java
  @Entity
  public class User {
      
      // Cascade from User to Orders ✅
      @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
      private List<Order> orders;
  }
  
  @Entity
  public class Order {
      
      @ManyToOne
      @JoinColumn(name = "user_id")
      private User user;
  }
  ```

  **Dangerous usage:**

  ```java
  @Entity
  public class Order {
      
      // Cascade from Order to User ❌ DANGEROUS!
      @ManyToOne(cascade = CascadeType.REMOVE)
      @JoinColumn(name = "user_id")
      private User user;
  }
  
  @Transactional
  public void deleteOrder() {
      Order order = orderRepository.findById(1L).orElseThrow();
      orderRepository.delete(order);
      // Deletes order AND user! All user's orders are lost!
  }
  ```

  **Best practices:**

  ```java
  // ✅ DO: Cascade from parent to children
  @Entity
  public class User {
      @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
      private List<Order> orders;
  }
  
  // ❌ DON'T: Cascade from child to parent
  @Entity
  public class Order {
      @ManyToOne(cascade = CascadeType.REMOVE)  // DANGEROUS!
      private User user;
  }
  
  // ✅ DO: Cascade PERSIST/MERGE on @ManyToOne if needed
  @Entity
  public class Order {
      @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
      private User user;  // Safe - won't delete user
  }
  ```

</details>

<details>
  <summary>Best Practices</summary>
  <br/>

  ```java
  // ✅ DO: Use CascadeType.ALL with orphanRemoval for parent-child
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Order> orders;
  
  // ✅ DO: Use specific cascade types when needed
  @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private List<Order> orders;
  
  // ❌ DON'T: Use CascadeType.REMOVE on @ManyToOne
  @ManyToOne(cascade = CascadeType.REMOVE)  // DANGEROUS!
  private User user;
  
  // ❌ DON'T: Use CascadeType.ALL on @ManyToOne
  @ManyToOne(cascade = CascadeType.ALL)  // DANGEROUS!
  private User user;
  
  // ✅ DO: Use helper methods to maintain both sides
  public void addOrder(Order order) {
      orders.add(order);
      order.setUser(this);
  }
  
  public void removeOrder(Order order) {
      orders.remove(order);
      order.setUser(null);
  }
  
  // ✅ DO: Initialize collections
  @OneToMany(cascade = CascadeType.ALL)
  private List<Order> orders = new ArrayList<>();
  
  // ✅ DO: Use cascade for composition relationships
  @Entity
  public class Order {
      @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
      private List<OrderItem> items;  // OrderItems belong to Order
  }
  
  // ❌ DON'T: Use cascade for association relationships
  @Entity
  public class Order {
      @ManyToOne(cascade = CascadeType.ALL)
      private Product product;  // Product is shared, don't cascade!
  }
  
  // ✅ DO: Be explicit about cascade types
  @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private List<Order> orders;
  
  // ❌ DON'T: Use cascade blindly
  @OneToMany(cascade = CascadeType.ALL)  // Think about implications!
  private List<Order> orders;
  
  // ✅ DO: Test cascade behavior
  @Test
  public void testCascadeDelete() {
      User user = createUserWithOrders();
      userRepository.delete(user);
      
      // Verify orders are deleted
      assertEquals(0, orderRepository.count());
  }
  ```

  **Key Points**
  <br/>

  + **CascadeType.PERSIST** - Save children when saving parent
  + **CascadeType.MERGE** - Update children when updating parent
  + **CascadeType.REMOVE** - Delete children when deleting parent
  + **CascadeType.REFRESH** - Reload children when reloading parent
  + **CascadeType.DETACH** - Detach children when detaching parent
  + **CascadeType.ALL** - All cascade operations
  + Use **CascadeType.ALL + orphanRemoval** for parent-child relationships
  + **Never use CascadeType.REMOVE** on @ManyToOne (dangerous!)
  + **Never use CascadeType.ALL** on @ManyToOne (dangerous!)
  + Cascade **PERSIST/MERGE** on @ManyToOne is usually safe
  + **orphanRemoval** deletes children when removed from collection
  + **CascadeType.REMOVE** deletes children when parent is deleted
  + Use **both** for complete lifecycle management
  + Cascade from **parent to children**, not child to parent
  + Use cascade for **composition** (strong ownership)
  + Don't use cascade for **association** (shared entities)
  + Always use **helper methods** to maintain bidirectional relationships
  + **Test cascade behavior** to avoid surprises
  + Be **explicit** about cascade types - don't use ALL blindly

</details>

## Query Methods

<details>
  <summary>What are Query Methods?</summary>
  <br/>

  Query methods allow you to define queries by method names or annotations.

  **Three types of query methods:**
  + **Derived queries** - Spring generates query from method name
  + **@Query annotation** - Custom JPQL or native SQL queries
  + **Named queries** - Predefined queries in entity or XML

  **Benefits:**
  + Type-safe queries
  + Less boilerplate code
  + Compile-time validation
  + Easy to read and maintain

</details>

<details>
  <summary>Derived Query Methods</summary>
  <br/>

  Spring Data JPA generates queries automatically from method names.

  **Basic pattern:**

  ```
  findBy + PropertyName + Condition
  ```

  **Simple examples:**

  ```java
  public interface UserRepository extends JpaRepository<User, Long> {
      
      // Find by single field
      User findByUsername(String username);
      Optional<User> findByEmail(String email);
      
      // Find multiple results
      List<User> findByLastName(String lastName);
      
      // Find with multiple conditions (AND)
      User findByUsernameAndEmail(String username, String email);
      
      // Find with OR condition
      List<User> findByUsernameOrEmail(String username, String email);
  }
  ```

  **Generated SQL:**

  ```java
  // findByUsername("john")
  // SELECT * FROM users WHERE username = 'john'
  
  // findByUsernameAndEmail("john", "john@example.com")
  // SELECT * FROM users WHERE username = 'john' AND email = 'john@example.com'
  
  // findByUsernameOrEmail("john", "john@example.com")
  // SELECT * FROM users WHERE username = 'john' OR email = 'john@example.com'
  ```

</details>

<details>
  <summary>Comparison Operators</summary>
  <br/>

  **Numeric comparisons:**

  ```java
  public interface UserRepository extends JpaRepository<User, Long> {
      
      // Greater than
      List<User> findByAgeGreaterThan(Integer age);
      
      // Greater than or equal
      List<User> findByAgeGreaterThanEqual(Integer age);
      
      // Less than
      List<User> findByAgeLessThan(Integer age);
      
      // Less than or equal
      List<User> findByAgeLessThanEqual(Integer age);
      
      // Between
      List<User> findByAgeBetween(Integer startAge, Integer endAge);
      
      // Not equal
      List<User> findByAgeNot(Integer age);
  }
  ```

  **Usage:**

  ```java
  // Find users older than 18
  List<User> adults = userRepository.findByAgeGreaterThanEqual(18);
  
  // Find users between 18 and 65
  List<User> workingAge = userRepository.findByAgeBetween(18, 65);
  ```

</details>

<details>
  <summary>String Matching</summary>
  <br/>

  **String operations:**

  ```java
  public interface UserRepository extends JpaRepository<User, Long> {
      
      // Like (requires % in parameter)
      List<User> findByUsernameLike(String pattern);
      
      // Containing (auto adds %)
      List<User> findByUsernameContaining(String text);
      
      // Starting with
      List<User> findByUsernameStartingWith(String prefix);
      
      // Ending with
      List<User> findByUsernameEndingWith(String suffix);
      
      // Ignore case
      User findByUsernameIgnoreCase(String username);
      
      // Containing + ignore case
      List<User> findByUsernameContainingIgnoreCase(String text);
  }
  ```

  **Usage:**

  ```java
  // Find usernames containing "john"
  List<User> users = userRepository.findByUsernameContaining("john");
  // SQL: WHERE username LIKE '%john%'
  
  // Find usernames starting with "admin"
  List<User> admins = userRepository.findByUsernameStartingWith("admin");
  // SQL: WHERE username LIKE 'admin%'
  
  // Case-insensitive search
  User user = userRepository.findByUsernameIgnoreCase("JOHN");
  // SQL: WHERE LOWER(username) = LOWER('JOHN')
  ```

</details>

<details>
  <summary>Collection and Null Operations</summary>
  <br/>

  **Collection operations:**

  ```java
  public interface UserRepository extends JpaRepository<User, Long> {
      
      // In collection
      List<User> findByUsernameIn(List<String> usernames);
      List<User> findByAgeIn(Collection<Integer> ages);
      
      // Not in collection
      List<User> findByUsernameNotIn(List<String> usernames);
  }
  ```

  **Null checks:**

  ```java
  public interface UserRepository extends JpaRepository<User, Long> {
      
      // Is null
      List<User> findByEmailIsNull();
      
      // Is not null
      List<User> findByEmailIsNotNull();
  }
  ```

  **Boolean checks:**

  ```java
  public interface UserRepository extends JpaRepository<User, Long> {
      
      // True
      List<User> findByActiveTrue();
      
      // False
      List<User> findByActiveFalse();
  }
  ```

  **Usage:**

  ```java
  // Find users by multiple usernames
  List<String> names = Arrays.asList("john", "alice", "bob");
  List<User> users = userRepository.findByUsernameIn(names);
  // SQL: WHERE username IN ('john', 'alice', 'bob')
  
  // Find users without email
  List<User> noEmail = userRepository.findByEmailIsNull();
  // SQL: WHERE email IS NULL
  
  // Find active users
  List<User> active = userRepository.findByActiveTrue();
  // SQL: WHERE active = true
  ```

</details>

<details>
  <summary>Sorting and Limiting</summary>
  <br/>

  **Order by:**

  ```java
  public interface UserRepository extends JpaRepository<User, Long> {
      
      // Order by single field ascending
      List<User> findByLastNameOrderByFirstNameAsc(String lastName);
      
      // Order by single field descending
      List<User> findByLastNameOrderByFirstNameDesc(String lastName);
      
      // Order by multiple fields
      List<User> findByLastNameOrderByFirstNameAscAgeDesc(String lastName);
  }
  ```

  **Limit results:**

  ```java
  public interface UserRepository extends JpaRepository<User, Long> {
      
      // First result
      User findFirstByOrderByCreatedAtDesc();
      
      // Top N results
      List<User> findTop3ByOrderByCreatedAtDesc();
      List<User> findTop10ByLastName(String lastName);
      
      // First N results (same as Top)
      List<User> findFirst5ByOrderByAgeDesc();
  }
  ```

  **Usage:**

  ```java
  // Get latest user
  User latest = userRepository.findFirstByOrderByCreatedAtDesc();
  
  // Get top 3 oldest users
  List<User> oldest = userRepository.findTop3ByOrderByAgeDesc();
  
  // Get users by last name, sorted by first name
  List<User> smiths = userRepository.findByLastNameOrderByFirstNameAsc("Smith");
  ```

</details>

<details>
  <summary>Count, Exists, and Delete</summary>
  <br/>

  **Count queries:**

  ```java
  public interface UserRepository extends JpaRepository<User, Long> {
      
      // Count by field
      long countByLastName(String lastName);
      
      // Count with condition
      long countByAgeGreaterThan(Integer age);
      
      // Count active users
      long countByActiveTrue();
  }
  ```

  **Exists queries:**

  ```java
  public interface UserRepository extends JpaRepository<User, Long> {
      
      // Check existence
      boolean existsByUsername(String username);
      
      // Check with condition
      boolean existsByEmailAndActiveTrue(String email);
  }
  ```

  **Delete queries:**

  ```java
  public interface UserRepository extends JpaRepository<User, Long> {
      
      // Delete by field
      void deleteByUsername(String username);
      
      // Delete with condition (returns count)
      long deleteByActiveAndLastLoginBefore(boolean active, LocalDateTime date);
      
      // Remove (same as delete)
      long removeByLastName(String lastName);
  }
  ```

  **Usage:**

  ```java
  // Count users by last name
  long count = userRepository.countByLastName("Smith");
  
  // Check if username exists
  boolean exists = userRepository.existsByUsername("john");
  
  // Delete inactive users
  @Transactional
  public void deleteInactiveUsers() {
      long deleted = userRepository.deleteByActiveFalse();
      System.out.println("Deleted " + deleted + " users");
  }
  ```

</details>

<details>
  <summary>Distinct Queries</summary>
  <br/>

  **Remove duplicates:**

  ```java
  public interface UserRepository extends JpaRepository<User, Long> {
      
      // Distinct results
      List<User> findDistinctByLastName(String lastName);
      
      // Distinct with multiple conditions
      List<User> findDistinctByLastNameAndFirstName(String lastName, String firstName);
  }
  ```

  **Usage:**

  ```java
  // Find distinct users by last name
  List<User> users = userRepository.findDistinctByLastName("Smith");
  // SQL: SELECT DISTINCT * FROM users WHERE last_name = 'Smith'
  ```

</details>

<details>
  <summary>@Query Annotation - JPQL</summary>
  <br/>

  Use `@Query` for complex queries that cannot be expressed with method names.

  **Basic JPQL queries:**

  ```java
  public interface UserRepository extends JpaRepository<User, Long> {
      
      // Simple query with positional parameter
      @Query("SELECT u FROM User u WHERE u.email = ?1")
      User findByEmailAddress(String email);
      
      // Named parameters (recommended)
      @Query("SELECT u FROM User u WHERE u.username = :username")
      User findByUsername(@Param("username") String username);
      
      // Multiple parameters
      @Query("SELECT u FROM User u WHERE u.username = :username AND u.email = :email")
      User findByUsernameAndEmail(
          @Param("username") String username,
          @Param("email") String email
      );
      
      // Like query
      @Query("SELECT u FROM User u WHERE u.username LIKE %:username%")
      List<User> searchByUsername(@Param("username") String username);
  }
  ```

  **Join queries:**

  ```java
  public interface UserRepository extends JpaRepository<User, Long> {
      
      // Join with related entity
      @Query("SELECT u FROM User u JOIN u.orders o WHERE o.status = :status")
      List<User> findUsersWithOrderStatus(@Param("status") String status);
      
      // Fetch join (load related entities)
      @Query("SELECT u FROM User u LEFT JOIN FETCH u.orders WHERE u.id = :id")
      User findByIdWithOrders(@Param("id") Long id);
      
      // Multiple joins
      @Query("SELECT u FROM User u " +
             "JOIN u.orders o " +
             "JOIN o.items i " +
             "WHERE i.product.name = :productName")
      List<User> findUsersByProductName(@Param("productName") String productName);
  }
  ```

  **Aggregate functions:**

  ```java
  public interface UserRepository extends JpaRepository<User, Long> {
      
      // Count
      @Query("SELECT COUNT(u) FROM User u WHERE u.active = true")
      long countActiveUsers();
      
      // Sum
      @Query("SELECT SUM(o.total) FROM Order o WHERE o.user.id = :userId")
      BigDecimal getTotalOrderAmount(@Param("userId") Long userId);
      
      // Average
      @Query("SELECT AVG(u.age) FROM User u WHERE u.active = true")
      Double getAverageAge();
      
      // Max/Min
      @Query("SELECT MAX(u.createdAt) FROM User u")
      LocalDateTime getLatestRegistrationDate();
  }
  ```

</details>

<details>
  <summary>@Query Annotation - Native SQL</summary>
  <br/>

  Use native SQL for database-specific features.

  **Basic native queries:**

  ```java
  public interface UserRepository extends JpaRepository<User, Long> {
      
      // Native query with positional parameter
      @Query(value = "SELECT * FROM users WHERE email = ?1", nativeQuery = true)
      User findByEmailNative(String email);
      
      // Native query with named parameter
      @Query(value = "SELECT * FROM users WHERE username = :username", 
             nativeQuery = true)
      User findByUsernameNative(@Param("username") String username);
      
      // Native query with multiple parameters
      @Query(value = "SELECT * FROM users WHERE username = :username AND email = :email",
             nativeQuery = true)
      User findByUsernameAndEmailNative(
          @Param("username") String username,
          @Param("email") String email
      );
  }
  ```

  **Database-specific features:**

  ```java
  public interface UserRepository extends JpaRepository<User, Long> {
      
      // PostgreSQL full-text search
      @Query(value = "SELECT * FROM users WHERE to_tsvector(username) @@ to_tsquery(:query)",
             nativeQuery = true)
      List<User> fullTextSearch(@Param("query") String query);
      
      // MySQL JSON query
      @Query(value = "SELECT * FROM users WHERE JSON_EXTRACT(metadata, '$.role') = :role",
             nativeQuery = true)
      List<User> findByJsonRole(@Param("role") String role);
      
      // Window function
      @Query(value = "SELECT *, ROW_NUMBER() OVER (ORDER BY created_at DESC) as row_num " +
                     "FROM users WHERE active = true",
             nativeQuery = true)
      List<User> findActiveUsersWithRowNumber();
  }
  ```

  **Native query with pagination:**

  ```java
  public interface UserRepository extends JpaRepository<User, Long> {
      
      @Query(value = "SELECT * FROM users WHERE age > :age",
             nativeQuery = true)
      Page<User> findByAgeGreaterThan(@Param("age") Integer age, Pageable pageable);
  }
  ```

</details>

<details>
  <summary>@Modifying Queries</summary>
  <br/>

  Use `@Modifying` for UPDATE and DELETE queries.

  **Update queries:**

  ```java
  public interface UserRepository extends JpaRepository<User, Long> {
      
      // Update single field
      @Modifying
      @Query("UPDATE User u SET u.active = :active WHERE u.id = :id")
      int updateUserActive(@Param("id") Long id, @Param("active") boolean active);
      
      // Update multiple fields
      @Modifying
      @Query("UPDATE User u SET u.username = :username, u.email = :email WHERE u.id = :id")
      int updateUser(
          @Param("id") Long id,
          @Param("username") String username,
          @Param("email") String email
      );
      
      // Bulk update
      @Modifying
      @Query("UPDATE User u SET u.active = false WHERE u.lastLogin < :date")
      int deactivateInactiveUsers(@Param("date") LocalDateTime date);
  }
  ```

  **Delete queries:**

  ```java
  public interface UserRepository extends JpaRepository<User, Long> {
      
      // Delete by condition
      @Modifying
      @Query("DELETE FROM User u WHERE u.active = false")
      int deleteInactiveUsers();
      
      // Delete with parameter
      @Modifying
      @Query("DELETE FROM User u WHERE u.lastLogin < :date")
      int deleteUsersInactiveSince(@Param("date") LocalDateTime date);
  }
  ```

  **Native update/delete:**

  ```java
  public interface UserRepository extends JpaRepository<User, Long> {
      
      // Native update
      @Modifying
      @Query(value = "UPDATE users SET email = :email WHERE id = :id",
             nativeQuery = true)
      int updateEmailNative(@Param("id") Long id, @Param("email") String email);
      
      // Native delete
      @Modifying
      @Query(value = "DELETE FROM users WHERE active = false",
             nativeQuery = true)
      int deleteInactiveUsersNative();
  }
  ```

  **Usage:**

  ```java
  @Service
  public class UserService {
      
      @Autowired
      private UserRepository userRepository;
      
      @Transactional
      public void deactivateUser(Long userId) {
          int updated = userRepository.updateUserActive(userId, false);
          System.out.println("Updated " + updated + " users");
      }
      
      @Transactional
      public void cleanupInactiveUsers() {
          LocalDateTime cutoffDate = LocalDateTime.now().minusMonths(6);
          int deleted = userRepository.deleteUsersInactiveSince(cutoffDate);
          System.out.println("Deleted " + deleted + " inactive users");
      }
  }
  ```

  **Important notes:**
  + Always use `@Transactional` with `@Modifying`
  + Returns number of affected rows
  + Bypasses entity lifecycle callbacks
  + Doesn't update persistence context automatically

</details>

<details>
  <summary>DTO Projections</summary>
  <br/>

  Use projections to fetch only needed fields.

  **DTO class:**

  ```java
  public class UserDTO {
      private Long id;
      private String username;
      private String email;
      
      public UserDTO(Long id, String username, String email) {
          this.id = id;
          this.username = username;
          this.email = email;
      }
      
      // Getters
  }
  ```

  **Query with DTO projection:**

  ```java
  public interface UserRepository extends JpaRepository<User, Long> {
      
      // Constructor expression
      @Query("SELECT new com.example.dto.UserDTO(u.id, u.username, u.email) " +
             "FROM User u WHERE u.active = true")
      List<UserDTO> findActiveUsersDTO();
      
      // With parameters
      @Query("SELECT new com.example.dto.UserDTO(u.id, u.username, u.email) " +
             "FROM User u WHERE u.lastName = :lastName")
      List<UserDTO> findByLastNameDTO(@Param("lastName") String lastName);
  }
  ```

  **Interface projection:**

  ```java
  public interface UserSummary {
      Long getId();
      String getUsername();
      String getEmail();
  }
  
  public interface UserRepository extends JpaRepository<User, Long> {
      
      // Spring Data creates proxy implementation
      List<UserSummary> findByActiveTrue();
      
      UserSummary findByUsername(String username);
  }
  ```

</details>

<details>
  <summary>Named Queries</summary>
  <br/>

  Named queries are predefined queries in entity class or XML.

  **Define in entity:**

  ```java
  @Entity
  @NamedQuery(
      name = "User.findByEmailAddress",
      query = "SELECT u FROM User u WHERE u.email = :email"
  )
  @NamedQueries({
      @NamedQuery(
          name = "User.findActiveUsers",
          query = "SELECT u FROM User u WHERE u.active = true"
      ),
      @NamedQuery(
          name = "User.findByAgeRange",
          query = "SELECT u FROM User u WHERE u.age BETWEEN :minAge AND :maxAge"
      )
  })
  public class User {
      @Id
      private Long id;
      private String username;
      private String email;
      private boolean active;
      private Integer age;
  }
  ```

  **Use in repository:**

  ```java
  public interface UserRepository extends JpaRepository<User, Long> {
      
      // Spring Data automatically finds named query
      User findByEmailAddress(@Param("email") String email);
      
      List<User> findActiveUsers();
      
      List<User> findByAgeRange(@Param("minAge") Integer minAge, @Param("maxAge") Integer maxAge);
  }
  ```

  **Named native query:**

  ```java
  @Entity
  @NamedNativeQuery(
      name = "User.findByEmailNative",
      query = "SELECT * FROM users WHERE email = :email",
      resultClass = User.class
  )
  public class User {
      // Entity fields
  }
  
  public interface UserRepository extends JpaRepository<User, Long> {
      User findByEmailNative(@Param("email") String email);
  }
  ```

  **When to use named queries:**
  + Complex queries used in multiple places
  + Queries validated at startup
  + Better organization for large applications
  + Easier to test and maintain

</details>

<details>
  <summary>Best Practices</summary>
  <br/>

  ```java
  // ✅ DO: Use derived queries for simple cases
  User findByUsername(String username);
  List<User> findByAgeGreaterThan(Integer age);
  
  // ✅ DO: Use @Query for complex queries
  @Query("SELECT u FROM User u JOIN u.orders o WHERE o.total > :amount")
  List<User> findUsersWithLargeOrders(@Param("amount") BigDecimal amount);
  
  // ✅ DO: Use named parameters
  @Query("SELECT u FROM User u WHERE u.username = :username")
  User findByUsername(@Param("username") String username);
  
  // ❌ DON'T: Use positional parameters (less readable)
  @Query("SELECT u FROM User u WHERE u.username = ?1")
  User findByUsername(String username);
  
  // ✅ DO: Use Optional for single results
  Optional<User> findByUsername(String username);
  
  // ❌ DON'T: Return null
  User findByUsername(String username);  // Can return null
  
  // ✅ DO: Use existsBy for existence checks
  boolean existsByUsername(String username);
  
  // ❌ DON'T: Load entity to check existence
  User user = userRepository.findByUsername(username);
  if (user != null) { ... }
  
  // ✅ DO: Use countBy for counting
  long countByActive(boolean active);
  
  // ❌ DON'T: Load all to count
  List<User> users = userRepository.findByActive(true);
  int count = users.size();
  
  // ✅ DO: Use @Transactional with @Modifying
  @Transactional
  @Modifying
  @Query("UPDATE User u SET u.active = false WHERE u.id = :id")
  int deactivateUser(@Param("id") Long id);
  
  // ✅ DO: Use DTO projections for read-only data
  @Query("SELECT new com.example.dto.UserDTO(u.id, u.username) FROM User u")
  List<UserDTO> findAllUserDTOs();
  
  // ✅ DO: Use fetch join to avoid N+1
  @Query("SELECT u FROM User u LEFT JOIN FETCH u.orders WHERE u.id = :id")
  User findByIdWithOrders(@Param("id") Long id);
  
  // ✅ DO: Use native queries for database-specific features
  @Query(value = "SELECT * FROM users WHERE to_tsvector(username) @@ to_tsquery(:query)",
         nativeQuery = true)
  List<User> fullTextSearch(@Param("query") String query);
  
  // ✅ DO: Use Pageable for large result sets
  Page<User> findByLastName(String lastName, Pageable pageable);
  
  // ✅ DO: Use descriptive method names
  List<User> findByLastNameAndActiveTrue(String lastName);
  
  // ❌ DON'T: Use unclear names
  List<User> findUsers(String name);
  ```

  **Key Points**
  <br/>

  + **Derived queries** - Spring generates query from method name
  + **@Query** - Custom JPQL or native SQL queries
  + **Named queries** - Predefined queries in entity or XML
  + Use **derived queries** for simple cases
  + Use **@Query** for complex queries
  + Use **named parameters** (@Param) instead of positional
  + Use **Optional** for single results
  + Use **existsBy** instead of findBy for existence checks
  + Use **countBy** instead of loading all entities
  + Use **@Modifying** with @Transactional for UPDATE/DELETE
  + Use **DTO projections** for read-only data
  + Use **fetch join** to avoid N+1 problem
  + Use **native queries** for database-specific features
  + Use **Pageable** for large result sets
  + Method naming: findBy, countBy, existsBy, deleteBy + field + condition
  + Conditions: And, Or, GreaterThan, LessThan, Between, Like, In, IsNull
  + String matching: Like, Containing, StartingWith, EndingWith, IgnoreCase
  + Sorting: OrderBy + field + Asc/Desc
  + Limiting: First, Top + number
  + Always use **@Transactional** with @Modifying queries

</details>

## HQL vs JPQL vs Native SQL

<details>
  <summary>Overview</summary>
  <br/>

  Three query languages for database access in JPA/Hibernate.

  **HQL (Hibernate Query Language):**
  + Hibernate-specific query language
  + Object-oriented query language
  + Works with entities, not tables

  **JPQL (Java Persistence Query Language):**
  + JPA standard query language
  + Subset of HQL
  + Portable across JPA providers

  **Native SQL:**
  + Database-specific SQL
  + Works with tables and columns
  + Full database features available

  **Relationship:**

  ```
  HQL (Hibernate-specific)
   ↓
  JPQL (JPA standard, subset of HQL)
   ↓
  Native SQL (Database-specific)
  ```

</details>

<details>
  <summary>JPQL (Java Persistence Query Language)</summary>
  <br/>

  JPQL is the JPA standard query language.

  **Characteristics:**
  + Object-oriented (uses entity names and properties)
  + Database-independent
  + Portable across JPA providers
  + Type-safe
  + Supports entity relationships

  **Basic JPQL queries:**

  ```java
  @Repository
  public class UserRepository {
      
      @PersistenceContext
      private EntityManager entityManager;
      
      // Simple query
      public List<User> findAll() {
          return entityManager
              .createQuery("SELECT u FROM User u", User.class)
              .getResultList();
      }
      
      // Query with parameter
      public User findByUsername(String username) {
          return entityManager
              .createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
              .setParameter("username", username)
              .getSingleResult();
      }
      
      // Query with multiple parameters
      public List<User> findByAgeRange(Integer minAge, Integer maxAge) {
          return entityManager
              .createQuery("SELECT u FROM User u WHERE u.age BETWEEN :minAge AND :maxAge", User.class)
              .setParameter("minAge", minAge)
              .setParameter("maxAge", maxAge)
              .getResultList();
      }
  }
  ```

  **JPQL with joins:**

  ```java
  // Inner join
  public List<User> findUsersWithOrders() {
      return entityManager
          .createQuery("SELECT u FROM User u JOIN u.orders o", User.class)
          .getResultList();
  }
  
  // Left join
  public List<User> findAllUsersWithOrders() {
      return entityManager
          .createQuery("SELECT u FROM User u LEFT JOIN u.orders o", User.class)
          .getResultList();
  }
  
  // Fetch join (load related entities)
  public User findByIdWithOrders(Long id) {
      return entityManager
          .createQuery("SELECT u FROM User u LEFT JOIN FETCH u.orders WHERE u.id = :id", User.class)
          .setParameter("id", id)
          .getSingleResult();
  }
  ```

  **JPQL aggregate functions:**

  ```java
  // Count
  public long countActiveUsers() {
      return entityManager
          .createQuery("SELECT COUNT(u) FROM User u WHERE u.active = true", Long.class)
          .getSingleResult();
  }
  
  // Sum
  public BigDecimal getTotalOrderAmount(Long userId) {
      return entityManager
          .createQuery("SELECT SUM(o.total) FROM Order o WHERE o.user.id = :userId", BigDecimal.class)
          .setParameter("userId", userId)
          .getSingleResult();
  }
  
  // Average
  public Double getAverageAge() {
      return entityManager
          .createQuery("SELECT AVG(u.age) FROM User u", Double.class)
          .getSingleResult();
  }
  ```

</details>

<details>
  <summary>HQL (Hibernate Query Language)</summary>
  <br/>

  HQL is Hibernate's query language (superset of JPQL).

  **Characteristics:**
  + All JPQL features plus Hibernate-specific extensions
  + Object-oriented
  + More powerful than JPQL
  + Hibernate-specific (not portable)

  **HQL-specific features:**

  ```java
  @Repository
  public class UserRepository {
      
      @PersistenceContext
      private EntityManager entityManager;
      
      // Cast to Hibernate Session
      private Session getSession() {
          return entityManager.unwrap(Session.class);
      }
      
      // HQL with Hibernate Session
      public List<User> findActiveUsers() {
          return getSession()
              .createQuery("FROM User u WHERE u.active = true", User.class)
              .list();
      }
      
      // HQL pagination
      public List<User> findUsersWithPagination(int page, int size) {
          return getSession()
              .createQuery("FROM User u ORDER BY u.createdAt DESC", User.class)
              .setFirstResult(page * size)
              .setMaxResults(size)
              .list();
      }
  }
  ```

  **HQL-specific syntax:**

  ```java
  // Implicit SELECT (HQL only)
  public List<User> findAll() {
      return getSession()
          .createQuery("FROM User", User.class)  // No SELECT needed
          .list();
  }
  
  // Implicit WHERE (HQL only)
  public List<User> findByUsername(String username) {
      return getSession()
          .createQuery("FROM User u WHERE username = :username", User.class)
          .setParameter("username", username)
          .list();
  }
  ```

  **HQL update/delete:**

  ```java
  @Transactional
  public int updateUserStatus(Long userId, boolean active) {
      return getSession()
          .createQuery("UPDATE User u SET u.active = :active WHERE u.id = :id")
          .setParameter("active", active)
          .setParameter("id", userId)
          .executeUpdate();
  }
  
  @Transactional
  public int deleteInactiveUsers() {
      return getSession()
          .createQuery("DELETE FROM User u WHERE u.active = false")
          .executeUpdate();
  }
  ```

</details>

<details>
  <summary>Native SQL</summary>
  <br/>

  Native SQL uses database-specific SQL syntax.

  **Characteristics:**
  + Database-specific
  + Works with tables and columns (not entities)
  + Full database features available
  + Not portable
  + Best performance for complex queries

  **Basic native SQL:**

  ```java
  @Repository
  public class UserRepository {
      
      @PersistenceContext
      private EntityManager entityManager;
      
      // Simple native query
      public List<User> findAll() {
          return entityManager
              .createNativeQuery("SELECT * FROM users", User.class)
              .getResultList();
      }
      
      // Native query with parameter
      public User findByUsername(String username) {
          return (User) entityManager
              .createNativeQuery("SELECT * FROM users WHERE username = :username", User.class)
              .setParameter("username", username)
              .getSingleResult();
      }
      
      // Native query with multiple parameters
      public List<User> findByAgeRange(Integer minAge, Integer maxAge) {
          return entityManager
              .createNativeQuery("SELECT * FROM users WHERE age BETWEEN :minAge AND :maxAge", User.class)
              .setParameter("minAge", minAge)
              .setParameter("maxAge", maxAge)
              .getResultList();
      }
  }
  ```

  **Database-specific features:**

  ```java
  // PostgreSQL full-text search
  public List<User> fullTextSearch(String query) {
      return entityManager
          .createNativeQuery(
              "SELECT * FROM users WHERE to_tsvector(username || ' ' || email) @@ to_tsquery(:query)",
              User.class
          )
          .setParameter("query", query)
          .getResultList();
  }
  
  // MySQL JSON query
  public List<User> findByJsonRole(String role) {
      return entityManager
          .createNativeQuery(
              "SELECT * FROM users WHERE JSON_EXTRACT(metadata, '$.role') = :role",
              User.class
          )
          .setParameter("role", role)
          .getResultList();
  }
  
  // Window functions
  public List<Object[]> getUsersWithRanking() {
      return entityManager
          .createNativeQuery(
              "SELECT username, age, RANK() OVER (ORDER BY age DESC) as rank " +
              "FROM users WHERE active = true"
          )
          .getResultList();
  }
  ```

  **Native SQL with result mapping:**

  ```java
  // Scalar results (not entities)
  public List<Object[]> getUserSummary() {
      return entityManager
          .createNativeQuery("SELECT username, email, age FROM users")
          .getResultList();
  }
  
  // Single column result
  public List<String> getAllUsernames() {
      return entityManager
          .createNativeQuery("SELECT username FROM users")
          .getResultList();
  }
  ```

</details>

<details>
  <summary>Key Differences</summary>
  <br/>

  **Syntax differences:**

  ```java
  // JPQL - uses entity names and properties
  "SELECT u FROM User u WHERE u.username = :username"
  
  // HQL - can omit SELECT
  "FROM User u WHERE u.username = :username"
  
  // Native SQL - uses table and column names
  "SELECT * FROM users WHERE username = :username"
  ```

  **Join syntax:**

  ```java
  // JPQL/HQL - uses entity relationships
  "SELECT u FROM User u JOIN u.orders o WHERE o.total > 100"
  
  // Native SQL - explicit JOIN with foreign keys
  "SELECT u.* FROM users u JOIN orders o ON u.id = o.user_id WHERE o.total > 100"
  ```

  **Comparison table:**

  | Feature | JPQL | HQL | Native SQL |
  |---------|------|-----|------------|
  | Standard | JPA standard | Hibernate-specific | Database-specific |
  | Portability | High | Medium | Low |
  | Syntax | Entity-based | Entity-based | Table-based |
  | Features | Basic | Extended | Full database |
  | Performance | Good | Good | Best |
  | Type safety | Yes | Yes | No |
  | Learning curve | Easy | Easy | Medium |
  | Use case | Standard queries | Hibernate apps | Complex/DB-specific |

</details>

<details>
  <summary>When to Use Each Query Type</summary>
  <br/>

  **Decision flowchart:**

  ```
  Need database-specific feature?
    ├─ Yes → Use Native SQL
    └─ No → Need Hibernate-specific feature?
        ├─ Yes → Use HQL
        └─ No → Use JPQL (default choice)
  ```

  **Use JPQL when:**
  + You need database portability ✅
  + Working with standard JPA ✅
  + Query involves entity relationships ✅
  + Type safety is important ✅
  + Query is simple to moderate complexity ✅

  **Use HQL when:**
  + Using Hibernate (not just JPA) ✅
  + Need Hibernate-specific features ✅
  + Want more concise syntax ✅
  + Working with Hibernate Session API ✅

  **Use Native SQL when:**
  + Need database-specific features ✅
  + Complex queries with better performance ✅
  + Working with legacy database ✅
  + JPQL/HQL cannot express the query ✅
  + Need full control over SQL ✅

  **JPQL Examples:**

  ```java
  // ✅ GOOD: Simple entity query
  @Query("SELECT u FROM User u WHERE u.active = true")
  List<User> findActiveUsers();
  
  // ✅ GOOD: Query with relationships
  @Query("SELECT u FROM User u JOIN u.orders o WHERE o.status = :status")
  List<User> findUsersWithOrderStatus(@Param("status") String status);
  
  // ✅ GOOD: Fetch join to avoid N+1
  @Query("SELECT u FROM User u LEFT JOIN FETCH u.orders WHERE u.id = :id")
  User findByIdWithOrders(@Param("id") Long id);
  
  // ✅ GOOD: Aggregate functions
  @Query("SELECT COUNT(u) FROM User u WHERE u.active = true")
  long countActiveUsers();
  ```

  **HQL Examples:**

  ```java
  // ✅ GOOD: Concise syntax (no SELECT)
  Session session = entityManager.unwrap(Session.class);
  List<User> users = session
      .createQuery("FROM User u WHERE u.active = true", User.class)
      .list();
  
  // ✅ GOOD: Hibernate-specific pagination
  List<User> users = session
      .createQuery("FROM User u ORDER BY u.createdAt DESC", User.class)
      .setFirstResult(0)
      .setMaxResults(10)
      .list();
  
  // ✅ GOOD: Bulk operations
  int updated = session
      .createQuery("UPDATE User u SET u.active = false WHERE u.lastLogin < :date")
      .setParameter("date", cutoffDate)
      .executeUpdate();
  ```

  **Native SQL Examples:**

  ```java
  // ✅ GOOD: Database-specific features (PostgreSQL full-text search)
  @Query(value = "SELECT * FROM users WHERE to_tsvector(username) @@ to_tsquery(:query)",
         nativeQuery = true)
  List<User> fullTextSearch(@Param("query") String query);
  
  // ✅ GOOD: Complex joins with performance optimization
  @Query(value = "SELECT u.* FROM users u " +
                 "INNER JOIN orders o ON u.id = o.user_id " +
                 "WHERE o.created_at > :date " +
                 "GROUP BY u.id " +
                 "HAVING COUNT(o.id) > 5",
         nativeQuery = true)
  List<User> findActiveCustomers(@Param("date") LocalDateTime date);
  
  // ✅ GOOD: Window functions
  @Query(value = "SELECT *, ROW_NUMBER() OVER (PARTITION BY department ORDER BY salary DESC) as rank " +
                 "FROM employees",
         nativeQuery = true)
  List<Object[]> getEmployeeRanking();
  
  // ✅ GOOD: Recursive CTE (PostgreSQL)
  @Query(value = "WITH RECURSIVE subordinates AS ( " +
                 "  SELECT id, name, manager_id FROM employees WHERE id = :managerId " +
                 "  UNION ALL " +
                 "  SELECT e.id, e.name, e.manager_id FROM employees e " +
                 "  INNER JOIN subordinates s ON s.id = e.manager_id " +
                 ") SELECT * FROM subordinates",
         nativeQuery = true)
  List<Object[]> findSubordinates(@Param("managerId") Long managerId);
  ```

  **Comparison:**

  | Aspect | JPQL | HQL | Native SQL |
  |--------|------|-----|------------|
  | **Portability** | ✅ High | ⚠️ Medium | ❌ Low |
  | **Type Safety** | ✅ Yes | ✅ Yes | ❌ No |
  | **Performance** | ✅ Good | ✅ Good | ✅ Best |
  | **Features** | ⚠️ Basic | ✅ Extended | ✅ Full |
  | **Complexity** | ✅ Easy | ✅ Easy | ⚠️ Medium |
  | **Maintenance** | ✅ Easy | ✅ Easy | ⚠️ Harder |
  | **Use Case** | Standard queries | Hibernate apps | DB-specific |

  **Advantages & Disadvantages:**

  **JPQL:**
  + ✅ Database-independent
  + ✅ Works with any JPA provider
  + ✅ Type-safe
  + ✅ Automatic entity mapping
  + ✅ Supports entity relationships
  + ❌ Limited database-specific features
  + ❌ Can be verbose for complex queries

  **HQL:**
  + ✅ All JPQL features plus more
  + ✅ More concise syntax
  + ✅ Better integration with Hibernate
  + ✅ Additional Hibernate features
  + ❌ Hibernate-specific (not portable)
  + ❌ Requires Hibernate dependency

  **Native SQL:**
  + ✅ Full database features
  + ✅ Best performance for complex queries
  + ✅ Direct control over SQL
  + ✅ Database-specific optimizations
  + ❌ Not portable across databases
  + ❌ No type safety
  + ❌ Manual result mapping
  + ❌ Harder to maintain

</details>

<details>
  <summary>Performance Comparison</summary>
  <br/>

  **Performance characteristics:**

  ```java
  // JPQL - Good performance, automatic optimization
  @Query("SELECT u FROM User u WHERE u.age > :age")
  List<User> findByAgeJPQL(@Param("age") Integer age);
  
  // HQL - Similar to JPQL
  Session session = entityManager.unwrap(Session.class);
  List<User> users = session
      .createQuery("FROM User u WHERE u.age > :age", User.class)
      .setParameter("age", age)
      .list();
  
  // Native SQL - Best performance, no overhead
  @Query(value = "SELECT * FROM users WHERE age > :age", nativeQuery = true)
  List<User> findByAgeNative(@Param("age") Integer age);
  ```

  **When Native SQL is faster:**
  + Complex joins with many tables
  + Database-specific optimizations
  + Bulk operations
  + Reporting queries
  + Data warehouse queries

  **When JPQL/HQL is sufficient:**
  + Simple queries
  + Entity-based queries
  + Queries with relationships
  + Standard CRUD operations

</details>

<details>
  <summary>Migration Between Query Types</summary>
  <br/>

  **JPQL to Native SQL:**

  ```java
  // JPQL
  @Query("SELECT u FROM User u JOIN u.orders o WHERE o.total > :amount")
  List<User> findUsersJPQL(@Param("amount") BigDecimal amount);
  
  // Equivalent Native SQL
  @Query(value = "SELECT DISTINCT u.* FROM users u " +
                 "JOIN orders o ON u.id = o.user_id " +
                 "WHERE o.total > :amount",
         nativeQuery = true)
  List<User> findUsersNative(@Param("amount") BigDecimal amount);
  ```

  **Native SQL to JPQL:**

  ```java
  // Native SQL
  @Query(value = "SELECT * FROM users WHERE username LIKE :pattern",
         nativeQuery = true)
  List<User> findUsersNative(@Param("pattern") String pattern);
  
  // Equivalent JPQL
  @Query("SELECT u FROM User u WHERE u.username LIKE :pattern")
  List<User> findUsersJPQL(@Param("pattern") String pattern);
  ```

</details>

<details>
  <summary>Best Practices</summary>
  <br/>

  ```java
  // ✅ DO: Use JPQL for standard queries
  @Query("SELECT u FROM User u WHERE u.active = true")
  List<User> findActiveUsers();
  
  // ✅ DO: Use Native SQL for database-specific features
  @Query(value = "SELECT * FROM users WHERE to_tsvector(username) @@ to_tsquery(:query)",
         nativeQuery = true)
  List<User> fullTextSearch(@Param("query") String query);
  
  // ✅ DO: Use named parameters
  @Query("SELECT u FROM User u WHERE u.username = :username")
  User findByUsername(@Param("username") String username);
  
  // ❌ DON'T: Use Native SQL for simple queries
  @Query(value = "SELECT * FROM users WHERE username = :username", nativeQuery = true)
  User findByUsername(@Param("username") String username);
  
  // ✅ DO: Use JPQL for portability
  @Query("SELECT u FROM User u WHERE u.age > :age")
  List<User> findByAge(@Param("age") Integer age);
  
  // ✅ DO: Use fetch join to avoid N+1
  @Query("SELECT u FROM User u LEFT JOIN FETCH u.orders WHERE u.id = :id")
  User findByIdWithOrders(@Param("id") Long id);
  
  // ✅ DO: Use Native SQL for complex reporting
  @Query(value = "SELECT DATE(created_at) as date, COUNT(*) as count " +
                 "FROM users " +
                 "WHERE created_at > :startDate " +
                 "GROUP BY DATE(created_at) " +
                 "ORDER BY date DESC",
         nativeQuery = true)
  List<Object[]> getUserRegistrationStats(@Param("startDate") LocalDateTime startDate);
  
  // ✅ DO: Document why Native SQL is used
  /**
   * Uses native SQL for PostgreSQL full-text search.
   * Cannot be expressed in JPQL.
   */
  @Query(value = "SELECT * FROM users WHERE to_tsvector(username) @@ to_tsquery(:query)",
         nativeQuery = true)
  List<User> fullTextSearch(@Param("query") String query);
  ```

  **Decision flowchart:**

  ```
  Need database-specific feature?
    ├─ Yes → Use Native SQL
    └─ No → Need Hibernate-specific feature?
        ├─ Yes → Use HQL
        └─ No → Use JPQL (default choice)
  ```

  **Key Points**
  <br/>

  + **JPQL** - JPA standard, portable, entity-based, use by default
  + **HQL** - Hibernate-specific, superset of JPQL, more features
  + **Native SQL** - Database-specific, full features, best performance
  + Use **JPQL** for standard queries and portability
  + Use **HQL** when using Hibernate and need extra features
  + Use **Native SQL** for database-specific features or complex queries
  + **JPQL/HQL** work with entities and properties
  + **Native SQL** works with tables and columns
  + **JPQL** is most portable, **Native SQL** is least portable
  + **Native SQL** has best performance for complex queries
  + Use **named parameters** (@Param) in all query types
  + Use **fetch join** in JPQL/HQL to avoid N+1 problem
  + Document why **Native SQL** is used (for maintainability)
  + Consider **portability** vs **performance** trade-offs
  + Start with **JPQL**, move to **Native SQL** only when needed

</details>

## Criteria API

<details>
  <summary>What is Criteria API?</summary>
  <br/>

  Criteria API is a type-safe, programmatic way to build JPA queries.

  **Characteristics:**
  + Type-safe (compile-time checking)
  + Programmatic query building
  + Dynamic query construction
  + No string-based queries
  + IDE auto-completion support

  **Why use Criteria API?**
  + Build queries dynamically at runtime
  + Type-safe (catch errors at compile time)
  + Refactoring-friendly
  + Complex conditional queries
  + Avoid SQL injection

  **Comparison:**

  ```java
  // JPQL - String-based (not type-safe)
  @Query("SELECT u FROM User u WHERE u.username = :username")
  User findByUsername(@Param("username") String username);
  
  // Criteria API - Type-safe
  CriteriaBuilder cb = entityManager.getCriteriaBuilder();
  CriteriaQuery<User> query = cb.createQuery(User.class);
  Root<User> user = query.from(User.class);
  query.select(user).where(cb.equal(user.get("username"), username));
  ```

</details>

<details>
  <summary>Basic Criteria Query</summary>
  <br/>

  **Simple query example:**

  ```java
  @Repository
  public class UserRepository {
      
      @PersistenceContext
      private EntityManager entityManager;
      
      // Find all users
      public List<User> findAll() {
          CriteriaBuilder cb = entityManager.getCriteriaBuilder();
          CriteriaQuery<User> query = cb.createQuery(User.class);
          Root<User> user = query.from(User.class);
          query.select(user);
          
          return entityManager.createQuery(query).getResultList();
      }
      
      // Find by username
      public User findByUsername(String username) {
          CriteriaBuilder cb = entityManager.getCriteriaBuilder();
          CriteriaQuery<User> query = cb.createQuery(User.class);
          Root<User> user = query.from(User.class);
          
          query.select(user)
               .where(cb.equal(user.get("username"), username));
          
          return entityManager.createQuery(query).getSingleResult();
      }
  }
  ```

  **Key components:**
  + **CriteriaBuilder** - Factory for query components
  + **CriteriaQuery** - Query definition
  + **Root** - Query root (FROM clause)
  + **Predicate** - WHERE conditions

</details>

<details>
  <summary>Comparison Predicates</summary>
  <br/>

  **Numeric comparisons:**

  ```java
  public List<User> findByAgeRange(Integer minAge, Integer maxAge) {
      CriteriaBuilder cb = entityManager.getCriteriaBuilder();
      CriteriaQuery<User> query = cb.createQuery(User.class);
      Root<User> user = query.from(User.class);
      
      // Greater than
      Predicate greaterThan = cb.greaterThan(user.get("age"), minAge);
      
      // Less than
      Predicate lessThan = cb.lessThan(user.get("age"), maxAge);
      
      // Between
      Predicate between = cb.between(user.get("age"), minAge, maxAge);
      
      query.select(user).where(between);
      
      return entityManager.createQuery(query).getResultList();
  }
  ```

  **String comparisons:**

  ```java
  public List<User> searchByUsername(String searchTerm) {
      CriteriaBuilder cb = entityManager.getCriteriaBuilder();
      CriteriaQuery<User> query = cb.createQuery(User.class);
      Root<User> user = query.from(User.class);
      
      // Like
      Predicate like = cb.like(user.get("username"), "%" + searchTerm + "%");
      
      // Equal (case-sensitive)
      Predicate equal = cb.equal(user.get("username"), searchTerm);
      
      // Equal (case-insensitive)
      Predicate equalIgnoreCase = cb.equal(
          cb.lower(user.get("username")),
          searchTerm.toLowerCase()
      );
      
      query.select(user).where(like);
      
      return entityManager.createQuery(query).getResultList();
  }
  ```

  **Null checks:**

  ```java
  public List<User> findUsersWithoutEmail() {
      CriteriaBuilder cb = entityManager.getCriteriaBuilder();
      CriteriaQuery<User> query = cb.createQuery(User.class);
      Root<User> user = query.from(User.class);
      
      // Is null
      Predicate isNull = cb.isNull(user.get("email"));
      
      // Is not null
      Predicate isNotNull = cb.isNotNull(user.get("email"));
      
      query.select(user).where(isNull);
      
      return entityManager.createQuery(query).getResultList();
  }
  ```

</details>

<details>
  <summary>Logical Operators</summary>
  <br/>

  **AND, OR, NOT:**

  ```java
  public List<User> findActiveAdults() {
      CriteriaBuilder cb = entityManager.getCriteriaBuilder();
      CriteriaQuery<User> query = cb.createQuery(User.class);
      Root<User> user = query.from(User.class);
      
      // AND
      Predicate active = cb.equal(user.get("active"), true);
      Predicate adult = cb.greaterThanOrEqualTo(user.get("age"), 18);
      Predicate and = cb.and(active, adult);
      
      // OR
      Predicate or = cb.or(active, adult);
      
      // NOT
      Predicate not = cb.not(active);
      
      query.select(user).where(and);
      
      return entityManager.createQuery(query).getResultList();
  }
  ```

  **Multiple conditions:**

  ```java
  public List<User> findUsers(String username, Integer minAge, Boolean active) {
      CriteriaBuilder cb = entityManager.getCriteriaBuilder();
      CriteriaQuery<User> query = cb.createQuery(User.class);
      Root<User> user = query.from(User.class);
      
      List<Predicate> predicates = new ArrayList<>();
      
      if (username != null) {
          predicates.add(cb.like(user.get("username"), "%" + username + "%"));
      }
      
      if (minAge != null) {
          predicates.add(cb.greaterThanOrEqualTo(user.get("age"), minAge));
      }
      
      if (active != null) {
          predicates.add(cb.equal(user.get("active"), active));
      }
      
      query.select(user)
           .where(cb.and(predicates.toArray(new Predicate[0])));
      
      return entityManager.createQuery(query).getResultList();
  }
  ```

</details>

<details>
  <summary>Dynamic Query Building</summary>
  <br/>

  **Dynamic search with optional filters:**

  ```java
  public class UserSearchCriteria {
      private String username;
      private String email;
      private Integer minAge;
      private Integer maxAge;
      private Boolean active;
      
      // Getters and setters
  }
  
  @Repository
  public class UserRepository {
      
      @PersistenceContext
      private EntityManager entityManager;
      
      public List<User> search(UserSearchCriteria criteria) {
          CriteriaBuilder cb = entityManager.getCriteriaBuilder();
          CriteriaQuery<User> query = cb.createQuery(User.class);
          Root<User> user = query.from(User.class);
          
          List<Predicate> predicates = new ArrayList<>();
          
          // Add predicates only if criteria is provided
          if (criteria.getUsername() != null && !criteria.getUsername().isEmpty()) {
              predicates.add(cb.like(
                  user.get("username"),
                  "%" + criteria.getUsername() + "%"
              ));
          }
          
          if (criteria.getEmail() != null && !criteria.getEmail().isEmpty()) {
              predicates.add(cb.like(
                  user.get("email"),
                  "%" + criteria.getEmail() + "%"
              ));
          }
          
          if (criteria.getMinAge() != null) {
              predicates.add(cb.greaterThanOrEqualTo(
                  user.get("age"),
                  criteria.getMinAge()
              ));
          }
          
          if (criteria.getMaxAge() != null) {
              predicates.add(cb.lessThanOrEqualTo(
                  user.get("age"),
                  criteria.getMaxAge()
              ));
          }
          
          if (criteria.getActive() != null) {
              predicates.add(cb.equal(user.get("active"), criteria.getActive()));
          }
          
          // Combine all predicates with AND
          if (!predicates.isEmpty()) {
              query.where(cb.and(predicates.toArray(new Predicate[0])));
          }
          
          return entityManager.createQuery(query).getResultList();
      }
  }
  ```

</details>

<details>
  <summary>Joins</summary>
  <br/>

  **Inner join:**

  ```java
  public List<User> findUsersWithOrders() {
      CriteriaBuilder cb = entityManager.getCriteriaBuilder();
      CriteriaQuery<User> query = cb.createQuery(User.class);
      Root<User> user = query.from(User.class);
      
      // Inner join
      Join<User, Order> orders = user.join("orders");
      
      query.select(user).distinct(true);
      
      return entityManager.createQuery(query).getResultList();
  }
  ```

  **Left join:**

  ```java
  public List<User> findAllUsersWithOrders() {
      CriteriaBuilder cb = entityManager.getCriteriaBuilder();
      CriteriaQuery<User> query = cb.createQuery(User.class);
      Root<User> user = query.from(User.class);
      
      // Left join
      Join<User, Order> orders = user.join("orders", JoinType.LEFT);
      
      query.select(user).distinct(true);
      
      return entityManager.createQuery(query).getResultList();
  }
  ```

  **Join with conditions:**

  ```java
  public List<User> findUsersWithLargeOrders(BigDecimal minAmount) {
      CriteriaBuilder cb = entityManager.getCriteriaBuilder();
      CriteriaQuery<User> query = cb.createQuery(User.class);
      Root<User> user = query.from(User.class);
      
      Join<User, Order> orders = user.join("orders");
      
      query.select(user)
           .where(cb.greaterThan(orders.get("total"), minAmount))
           .distinct(true);
      
      return entityManager.createQuery(query).getResultList();
  }
  ```

  **Fetch join (avoid N+1):**

  ```java
  public User findByIdWithOrders(Long id) {
      CriteriaBuilder cb = entityManager.getCriteriaBuilder();
      CriteriaQuery<User> query = cb.createQuery(User.class);
      Root<User> user = query.from(User.class);
      
      // Fetch join
      user.fetch("orders", JoinType.LEFT);
      
      query.select(user)
           .where(cb.equal(user.get("id"), id));
      
      return entityManager.createQuery(query).getSingleResult();
  }
  ```

</details>

<details>
  <summary>Sorting</summary>
  <br/>

  **Order by:**

  ```java
  public List<User> findAllSorted(String sortBy, boolean ascending) {
      CriteriaBuilder cb = entityManager.getCriteriaBuilder();
      CriteriaQuery<User> query = cb.createQuery(User.class);
      Root<User> user = query.from(User.class);
      
      query.select(user);
      
      // Dynamic sorting
      if (ascending) {
          query.orderBy(cb.asc(user.get(sortBy)));
      } else {
          query.orderBy(cb.desc(user.get(sortBy)));
      }
      
      return entityManager.createQuery(query).getResultList();
  }
  ```

  **Multiple sort fields:**

  ```java
  public List<User> findAllSortedMultiple() {
      CriteriaBuilder cb = entityManager.getCriteriaBuilder();
      CriteriaQuery<User> query = cb.createQuery(User.class);
      Root<User> user = query.from(User.class);
      
      query.select(user)
           .orderBy(
               cb.asc(user.get("lastName")),
               cb.asc(user.get("firstName"))
           );
      
      return entityManager.createQuery(query).getResultList();
  }
  ```

</details>

<details>
  <summary>Pagination</summary>
  <br/>

  **Limit and offset:**

  ```java
  public List<User> findWithPagination(int page, int size) {
      CriteriaBuilder cb = entityManager.getCriteriaBuilder();
      CriteriaQuery<User> query = cb.createQuery(User.class);
      Root<User> user = query.from(User.class);
      
      query.select(user)
           .orderBy(cb.desc(user.get("createdAt")));
      
      return entityManager.createQuery(query)
          .setFirstResult(page * size)
          .setMaxResults(size)
          .getResultList();
  }
  ```

  **With count query:**

  ```java
  public Page<User> findAllPaginated(Pageable pageable) {
      CriteriaBuilder cb = entityManager.getCriteriaBuilder();
      
      // Data query
      CriteriaQuery<User> query = cb.createQuery(User.class);
      Root<User> user = query.from(User.class);
      query.select(user);
      
      List<User> users = entityManager.createQuery(query)
          .setFirstResult((int) pageable.getOffset())
          .setMaxResults(pageable.getPageSize())
          .getResultList();
      
      // Count query
      CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
      Root<User> countRoot = countQuery.from(User.class);
      countQuery.select(cb.count(countRoot));
      
      Long total = entityManager.createQuery(countQuery).getSingleResult();
      
      return new PageImpl<>(users, pageable, total);
  }
  ```

</details>

<details>
  <summary>Aggregate Functions</summary>
  <br/>

  **Count, Sum, Avg, Max, Min:**

  ```java
  // Count
  public long countActiveUsers() {
      CriteriaBuilder cb = entityManager.getCriteriaBuilder();
      CriteriaQuery<Long> query = cb.createQuery(Long.class);
      Root<User> user = query.from(User.class);
      
      query.select(cb.count(user))
           .where(cb.equal(user.get("active"), true));
      
      return entityManager.createQuery(query).getSingleResult();
  }
  
  // Sum
  public BigDecimal getTotalOrderAmount(Long userId) {
      CriteriaBuilder cb = entityManager.getCriteriaBuilder();
      CriteriaQuery<BigDecimal> query = cb.createQuery(BigDecimal.class);
      Root<Order> order = query.from(Order.class);
      
      query.select(cb.sum(order.get("total")))
           .where(cb.equal(order.get("user").get("id"), userId));
      
      return entityManager.createQuery(query).getSingleResult();
  }
  
  // Average
  public Double getAverageAge() {
      CriteriaBuilder cb = entityManager.getCriteriaBuilder();
      CriteriaQuery<Double> query = cb.createQuery(Double.class);
      Root<User> user = query.from(User.class);
      
      query.select(cb.avg(user.get("age")));
      
      return entityManager.createQuery(query).getSingleResult();
  }
  
  // Max
  public Integer getMaxAge() {
      CriteriaBuilder cb = entityManager.getCriteriaBuilder();
      CriteriaQuery<Integer> query = cb.createQuery(Integer.class);
      Root<User> user = query.from(User.class);
      
      query.select(cb.max(user.get("age")));
      
      return entityManager.createQuery(query).getSingleResult();
  }
  ```

  **Group by:**

  ```java
  public List<Object[]> countUsersByAge() {
      CriteriaBuilder cb = entityManager.getCriteriaBuilder();
      CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
      Root<User> user = query.from(User.class);
      
      query.multiselect(
          user.get("age"),
          cb.count(user)
      )
      .groupBy(user.get("age"))
      .orderBy(cb.desc(user.get("age")));
      
      return entityManager.createQuery(query).getResultList();
  }
  ```

</details>

<details>
  <summary>Subqueries</summary>
  <br/>

  **Subquery example:**

  ```java
  public List<User> findUsersWithAboveAverageAge() {
      CriteriaBuilder cb = entityManager.getCriteriaBuilder();
      CriteriaQuery<User> query = cb.createQuery(User.class);
      Root<User> user = query.from(User.class);
      
      // Subquery for average age
      Subquery<Double> subquery = query.subquery(Double.class);
      Root<User> subUser = subquery.from(User.class);
      subquery.select(cb.avg(subUser.get("age")));
      
      // Main query
      query.select(user)
           .where(cb.greaterThan(user.get("age"), subquery));
      
      return entityManager.createQuery(query).getResultList();
  }
  ```

  **Exists subquery:**

  ```java
  public List<User> findUsersWithOrders() {
      CriteriaBuilder cb = entityManager.getCriteriaBuilder();
      CriteriaQuery<User> query = cb.createQuery(User.class);
      Root<User> user = query.from(User.class);
      
      // Subquery
      Subquery<Order> subquery = query.subquery(Order.class);
      Root<Order> order = subquery.from(Order.class);
      subquery.select(order)
              .where(cb.equal(order.get("user"), user));
      
      // Main query with EXISTS
      query.select(user)
           .where(cb.exists(subquery));
      
      return entityManager.createQuery(query).getResultList();
  }
  ```

</details>

<details>
  <summary>Metamodel (Type-Safe)</summary>
  <br/>

  **Generate metamodel classes:**

  ```java
  // User entity
  @Entity
  public class User {
      @Id
      private Long id;
      private String username;
      private String email;
      private Integer age;
  }
  
  // Generated metamodel (User_.java)
  @StaticMetamodel(User.class)
  public class User_ {
      public static volatile SingularAttribute<User, Long> id;
      public static volatile SingularAttribute<User, String> username;
      public static volatile SingularAttribute<User, String> email;
      public static volatile SingularAttribute<User, Integer> age;
  }
  ```

  **Use metamodel for type safety:**

  ```java
  public List<User> findByUsername(String username) {
      CriteriaBuilder cb = entityManager.getCriteriaBuilder();
      CriteriaQuery<User> query = cb.createQuery(User.class);
      Root<User> user = query.from(User.class);
      
      // Without metamodel (not type-safe)
      query.select(user)
           .where(cb.equal(user.get("username"), username));
      
      // With metamodel (type-safe)
      query.select(user)
           .where(cb.equal(user.get(User_.username), username));
      
      return entityManager.createQuery(query).getResultList();
  }
  ```

  **Benefits of metamodel:**
  + Compile-time type checking
  + IDE auto-completion
  + Refactoring support
  + No typos in field names

</details>

<details>
  <summary>Specifications (Spring Data JPA)</summary>
  <br/>

  Spring Data JPA Specifications provide reusable Criteria API queries.

  **Create specifications:**

  ```java
  public class UserSpecifications {
      
      public static Specification<User> hasUsername(String username) {
          return (root, query, cb) -> 
              cb.equal(root.get("username"), username);
      }
      
      public static Specification<User> hasEmail(String email) {
          return (root, query, cb) -> 
              cb.equal(root.get("email"), email);
      }
      
      public static Specification<User> ageGreaterThan(Integer age) {
          return (root, query, cb) -> 
              cb.greaterThan(root.get("age"), age);
      }
      
      public static Specification<User> isActive() {
          return (root, query, cb) -> 
              cb.isTrue(root.get("active"));
      }
      
      public static Specification<User> usernameLike(String pattern) {
          return (root, query, cb) -> 
              cb.like(root.get("username"), "%" + pattern + "%");
      }
  }
  ```

  **Use specifications:**

  ```java
  public interface UserRepository extends JpaRepository<User, Long>, 
                                          JpaSpecificationExecutor<User> {
  }
  
  @Service
  public class UserService {
      
      @Autowired
      private UserRepository userRepository;
      
      public List<User> searchUsers(String username, Integer minAge, Boolean active) {
          Specification<User> spec = Specification.where(null);
          
          if (username != null) {
              spec = spec.and(UserSpecifications.usernameLike(username));
          }
          
          if (minAge != null) {
              spec = spec.and(UserSpecifications.ageGreaterThan(minAge));
          }
          
          if (active != null && active) {
              spec = spec.and(UserSpecifications.isActive());
          }
          
          return userRepository.findAll(spec);
      }
  }
  ```

</details>

<details>
  <summary>Best Practices</summary>
  <br/>

  ```java
  // ✅ DO: Use Criteria API for dynamic queries
  public List<User> search(UserSearchCriteria criteria) {
      CriteriaBuilder cb = entityManager.getCriteriaBuilder();
      CriteriaQuery<User> query = cb.createQuery(User.class);
      Root<User> user = query.from(User.class);
      
      List<Predicate> predicates = new ArrayList<>();
      
      if (criteria.getUsername() != null) {
          predicates.add(cb.like(user.get("username"), "%" + criteria.getUsername() + "%"));
      }
      
      if (!predicates.isEmpty()) {
          query.where(cb.and(predicates.toArray(new Predicate[0])));
      }
      
      return entityManager.createQuery(query).getResultList();
  }
  
  // ✅ DO: Use metamodel for type safety
  query.where(cb.equal(user.get(User_.username), username));
  
  // ❌ DON'T: Use string literals (not type-safe)
  query.where(cb.equal(user.get("username"), username));
  
  // ✅ DO: Use Specifications for reusable queries
  Specification<User> spec = UserSpecifications.hasUsername(username)
      .and(UserSpecifications.isActive());
  
  // ✅ DO: Use fetch join to avoid N+1
  user.fetch("orders", JoinType.LEFT);
  
  // ✅ DO: Use distinct with joins
  query.select(user).distinct(true);
  
  // ✅ DO: Build predicates conditionally
  List<Predicate> predicates = new ArrayList<>();
  if (condition) {
      predicates.add(cb.equal(user.get("field"), value));
  }
  query.where(cb.and(predicates.toArray(new Predicate[0])));
  
  // ❌ DON'T: Use Criteria API for simple static queries
  // Use @Query or derived query methods instead
  
  // ✅ DO: Use Criteria API for complex dynamic queries
  // When query structure changes based on input
  
  // ✅ DO: Reuse CriteriaBuilder
  CriteriaBuilder cb = entityManager.getCriteriaBuilder();
  
  // ✅ DO: Use proper join types
  Join<User, Order> orders = user.join("orders", JoinType.LEFT);
  ```

  **Key Points**
  <br/>

  + **Criteria API** - Type-safe, programmatic query building
  + Use for **dynamic queries** with conditional filters
  + **CriteriaBuilder** - Factory for query components
  + **CriteriaQuery** - Query definition
  + **Root** - Query root (FROM clause)
  + **Predicate** - WHERE conditions
  + Use **metamodel** for compile-time type safety
  + Use **Specifications** for reusable query logic
  + Use **fetch join** to avoid N+1 problem
  + Use **distinct()** with joins to avoid duplicates
  + Build predicates **conditionally** for dynamic queries
  + Use **and()**, **or()**, **not()** for logical operators
  + Use **join()** for relationships
  + Use **orderBy()** for sorting
  + Use **setFirstResult()** and **setMaxResults()** for pagination
  + Use **count()**, **sum()**, **avg()**, **max()**, **min()** for aggregates
  + Use **subquery()** for complex queries
  + **Don't use** Criteria API for simple static queries
  + **Do use** Criteria API for complex dynamic queries
  + Criteria API is more **verbose** than JPQL but **type-safe**

</details>

## Transactions

<details>
  <summary>What are Transactions?</summary>
  <br/>

  A transaction is a unit of work that either completes entirely or not at all.

  **ACID Properties:**
  + **Atomicity** - All or nothing (complete success or complete failure)
  + **Consistency** - Data remains in valid state
  + **Isolation** - Concurrent transactions don't interfere
  + **Durability** - Committed changes are permanent

  **Why use transactions?**
  + Ensure data consistency
  + Handle failures gracefully
  + Maintain data integrity
  + Support concurrent operations

  **Example without transaction:**

  ```java
  // ❌ BAD: No transaction
  public void transferMoney(Long fromId, Long toId, BigDecimal amount) {
      Account from = accountRepository.findById(fromId).orElseThrow();
      Account to = accountRepository.findById(toId).orElseThrow();
      
      from.setBalance(from.getBalance().subtract(amount));
      accountRepository.save(from);
      
      // If exception occurs here, money is lost!
      
      to.setBalance(to.getBalance().add(amount));
      accountRepository.save(to);
  }
  ```

  **Example with transaction:**

  ```java
  // ✅ GOOD: With transaction
  @Transactional
  public void transferMoney(Long fromId, Long toId, BigDecimal amount) {
      Account from = accountRepository.findById(fromId).orElseThrow();
      Account to = accountRepository.findById(toId).orElseThrow();
      
      from.setBalance(from.getBalance().subtract(amount));
      accountRepository.save(from);
      
      // If exception occurs, entire transaction rolls back
      
      to.setBalance(to.getBalance().add(amount));
      accountRepository.save(to);
  }
  ```

</details>

<details>
  <summary><code>@Transactional</code> Annotation</summary>
  <br/>

  `@Transactional` marks a method or class to run within a transaction.

  **Basic usage:**

  ```java
  @Service
  public class UserService {
      
      @Autowired
      private UserRepository userRepository;
      
      // Method-level transaction
      @Transactional
      public User createUser(String username, String email) {
          User user = new User(username, email);
          return userRepository.save(user);
      }
      
      // Read-only transaction
      @Transactional(readOnly = true)
      public User getUser(Long id) {
          return userRepository.findById(id).orElseThrow();
      }
  }
  ```

  **Class-level transaction:**

  ```java
  @Service
  @Transactional  // All methods are transactional
  public class UserService {
      
      public User createUser(String username, String email) {
          // Transactional
      }
      
      @Transactional(readOnly = true)  // Override class-level
      public User getUser(Long id) {
          // Read-only transaction
      }
  }
  ```

  **When transaction starts and ends:**

  ```java
  @Transactional
  public void method() {
      // Transaction starts here
      
      // Database operations
      
      // Transaction commits here (if no exception)
      // Transaction rolls back here (if exception)
  }
  ```

</details>

<details>
  <summary>Propagation Levels</summary>
  <br/>

  Propagation defines how transactions relate to each other.

  **Propagation types:**

  ```
  REQUIRED (default)  - Use existing or create new
  REQUIRES_NEW        - Always create new (suspend existing)
  MANDATORY           - Must have existing (throw exception if none)
  SUPPORTS            - Use existing if available (non-transactional if none)
  NOT_SUPPORTED       - Execute non-transactionally (suspend existing)
  NEVER               - Execute non-transactionally (throw exception if exists)
  NESTED              - Execute within nested transaction
  ```

  **1. REQUIRED (default):**

  ```java
  @Service
  public class UserService {
      
      @Transactional(propagation = Propagation.REQUIRED)
      public void method1() {
          // Creates new transaction if none exists
          // Uses existing transaction if one exists
          method2();  // Uses same transaction
      }
      
      @Transactional(propagation = Propagation.REQUIRED)
      public void method2() {
          // Uses transaction from method1
      }
  }
  ```

  **2. REQUIRES_NEW:**

  ```java
  @Service
  public class OrderService {
      
      @Autowired
      private AuditService auditService;
      
      @Transactional
      public void createOrder(Order order) {
          orderRepository.save(order);
          
          // Log audit in separate transaction
          auditService.logAudit("Order created");
          
          // If order fails, audit is still saved
      }
  }
  
  @Service
  public class AuditService {
      
      @Transactional(propagation = Propagation.REQUIRES_NEW)
      public void logAudit(String message) {
          // Creates new transaction (suspends existing)
          // Commits independently
          auditRepository.save(new Audit(message));
      }
  }
  ```

  **3. MANDATORY:**

  ```java
  @Service
  public class UserService {
      
      @Transactional(propagation = Propagation.MANDATORY)
      public void updateUser(User user) {
          // Must be called within existing transaction
          // Throws exception if no transaction exists
          userRepository.save(user);
      }
  }
  
  @Service
  public class OrderService {
      
      @Transactional
      public void processOrder(Order order) {
          // Has transaction
          userService.updateUser(order.getUser());  // Works
      }
      
      public void processOrderNoTx(Order order) {
          // No transaction
          userService.updateUser(order.getUser());  // Exception!
      }
  }
  ```

  **4. SUPPORTS:**

  ```java
  @Service
  public class UserService {
      
      @Transactional(propagation = Propagation.SUPPORTS)
      public User getUser(Long id) {
          // Uses transaction if exists
          // Runs without transaction if none exists
          return userRepository.findById(id).orElseThrow();
      }
  }
  ```

  **5. NOT_SUPPORTED:**

  ```java
  @Service
  public class ReportService {
      
      @Transactional(propagation = Propagation.NOT_SUPPORTED)
      public Report generateReport() {
          // Always runs without transaction
          // Suspends existing transaction if any
          // Good for long-running operations
          return createReport();
      }
  }
  ```

  **6. NEVER:**

  ```java
  @Service
  public class CacheService {
      
      @Transactional(propagation = Propagation.NEVER)
      public void clearCache() {
          // Must NOT be called within transaction
          // Throws exception if transaction exists
          cache.clear();
      }
  }
  ```

  **7. NESTED:**

  ```java
  @Service
  public class OrderService {
      
      @Transactional
      public void processOrder(Order order) {
          orderRepository.save(order);
          
          try {
              // Nested transaction (savepoint)
              inventoryService.updateInventory(order);
          } catch (Exception e) {
              // Nested transaction rolls back
              // Main transaction continues
          }
      }
  }
  
  @Service
  public class InventoryService {
      
      @Transactional(propagation = Propagation.NESTED)
      public void updateInventory(Order order) {
          // Creates savepoint within existing transaction
          // Can rollback to savepoint without affecting parent
      }
  }
  ```

</details>

<details>
  <summary>Isolation Levels</summary>
  <br/>

  Isolation level defines how transaction changes are visible to other transactions.

  **Isolation levels (from least to most strict):**

  ```
  READ_UNCOMMITTED - Can read uncommitted changes (dirty reads)
  READ_COMMITTED   - Can only read committed changes (default)
  REPEATABLE_READ  - Same read returns same result
  SERIALIZABLE     - Complete isolation (slowest)
  ```

  **Concurrency problems:**

  ```
  Dirty Read         - Reading uncommitted changes
  Non-Repeatable Read - Same query returns different results
  Phantom Read       - New rows appear in range query
  ```

  **1. READ_UNCOMMITTED:**

  ```java
  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public List<User> getUsers() {
      // Can read uncommitted changes from other transactions
      // Fastest but least safe
      // Allows: Dirty reads, non-repeatable reads, phantom reads
      return userRepository.findAll();
  }
  ```

  **2. READ_COMMITTED (default):**

  ```java
  @Transactional(isolation = Isolation.READ_COMMITTED)
  public User getUser(Long id) {
      // Can only read committed changes
      // Prevents: Dirty reads
      // Allows: Non-repeatable reads, phantom reads
      return userRepository.findById(id).orElseThrow();
  }
  ```

  **3. REPEATABLE_READ:**

  ```java
  @Transactional(isolation = Isolation.REPEATABLE_READ)
  public void processUser(Long id) {
      User user1 = userRepository.findById(id).orElseThrow();
      
      // Other transaction updates user
      
      User user2 = userRepository.findById(id).orElseThrow();
      
      // user1 and user2 have same data (repeatable read)
      // Prevents: Dirty reads, non-repeatable reads
      // Allows: Phantom reads
  }
  ```

  **4. SERIALIZABLE:**

  ```java
  @Transactional(isolation = Isolation.SERIALIZABLE)
  public void criticalOperation() {
      // Complete isolation
      // Transactions execute as if serial
      // Prevents: Dirty reads, non-repeatable reads, phantom reads
      // Slowest performance
  }
  ```

  **Comparison table:**

  | Isolation Level | Dirty Read | Non-Repeatable Read | Phantom Read | Performance |
  |----------------|------------|---------------------|--------------|-------------|
  | READ_UNCOMMITTED | ✅ Possible | ✅ Possible | ✅ Possible | Fastest |
  | READ_COMMITTED | ❌ Prevented | ✅ Possible | ✅ Possible | Fast |
  | REPEATABLE_READ | ❌ Prevented | ❌ Prevented | ✅ Possible | Slow |
  | SERIALIZABLE | ❌ Prevented | ❌ Prevented | ❌ Prevented | Slowest |

</details>

<details>
  <summary>Rollback Rules</summary>
  <br/>

  Rollback rules define which exceptions trigger transaction rollback.

  **Default behavior:**
  + **RuntimeException** and **Error** → Rollback
  + **Checked exceptions** → No rollback (commit)

  **Custom rollback rules:**

  ```java
  @Service
  public class UserService {
      
      // Rollback on all exceptions
      @Transactional(rollbackFor = Exception.class)
      public void createUser(User user) throws Exception {
          userRepository.save(user);
          // Rolls back on any exception
      }
      
      // Rollback on specific exception
      @Transactional(rollbackFor = CustomException.class)
      public void updateUser(User user) throws CustomException {
          userRepository.save(user);
          // Rolls back only on CustomException
      }
      
      // No rollback on specific exception
      @Transactional(noRollbackFor = ValidationException.class)
      public void processUser(User user) {
          userRepository.save(user);
          // Commits even if ValidationException occurs
      }
      
      // Multiple exceptions
      @Transactional(
          rollbackFor = {IOException.class, SQLException.class},
          noRollbackFor = {ValidationException.class}
      )
      public void complexOperation(User user) {
          // Custom rollback rules
      }
  }
  ```

  **Rollback examples:**

  ```java
  @Service
  public class OrderService {
      
      // Default: Rolls back on RuntimeException
      @Transactional
      public void createOrder1(Order order) {
          orderRepository.save(order);
          throw new RuntimeException("Error");  // Rolls back
      }
      
      // Default: Does NOT rollback on checked exception
      @Transactional
      public void createOrder2(Order order) throws Exception {
          orderRepository.save(order);
          throw new Exception("Error");  // Does NOT rollback (commits)
      }
      
      // Custom: Rolls back on checked exception
      @Transactional(rollbackFor = Exception.class)
      public void createOrder3(Order order) throws Exception {
          orderRepository.save(order);
          throw new Exception("Error");  // Rolls back
      }
  }
  ```

  **Programmatic rollback:**

  ```java
  @Service
  public class UserService {
      
      @Transactional
      public void updateUser(User user) {
          try {
              userRepository.save(user);
              
              if (someCondition) {
                  // Force rollback
                  TransactionAspectSupport.currentTransaction().setRollbackOnly();
              }
          } catch (Exception e) {
              // Transaction will rollback
              throw e;
          }
      }
  }
  ```

</details>

<details>
  <summary>Read-Only Transactions</summary>
  <br/>

  Read-only transactions optimize performance for queries.

  **Benefits:**
  + Performance optimization
  + Prevents accidental writes
  + Database can optimize query execution
  + Hibernate skips dirty checking

  **Usage:**

  ```java
  @Service
  public class UserService {
      
      // Read-only transaction
      @Transactional(readOnly = true)
      public User getUser(Long id) {
          return userRepository.findById(id).orElseThrow();
      }
      
      // Read-only with lazy loading
      @Transactional(readOnly = true)
      public User getUserWithOrders(Long id) {
          User user = userRepository.findById(id).orElseThrow();
          user.getOrders().size();  // Force load lazy collection
          return user;
      }
      
      // Read-only list query
      @Transactional(readOnly = true)
      public List<User> getAllUsers() {
          return userRepository.findAll();
      }
  }
  ```

  **When to use:**
  + GET operations
  + Query methods
  + Report generation
  + Data export

  **When NOT to use:**
  + Create operations
  + Update operations
  + Delete operations

</details>

<details>
  <summary>Timeout</summary>
  <br/>

  Timeout defines maximum transaction duration.

  **Usage:**

  ```java
  @Service
  public class UserService {
      
      // Timeout in seconds
      @Transactional(timeout = 5)
      public void updateUser(User user) {
          // Transaction must complete within 5 seconds
          // Throws exception if timeout exceeded
          userRepository.save(user);
      }
      
      // Long-running operation
      @Transactional(timeout = 60)
      public void batchUpdate(List<User> users) {
          // 60 seconds timeout
          users.forEach(userRepository::save);
      }
  }
  ```

  **When to use:**
  + Prevent long-running transactions
  + Avoid database lock contention
  + Ensure timely response

</details>

<details>
  <summary>Transaction Best Practices</summary>
  <br/>

  ```java
  // ✅ DO: Use @Transactional on service layer
  @Service
  public class UserService {
      @Transactional
      public void createUser(User user) {
          userRepository.save(user);
      }
  }
  
  // ❌ DON'T: Use @Transactional on repository layer
  @Repository
  public interface UserRepository extends JpaRepository<User, Long> {
      // Spring Data already handles transactions
  }
  
  // ✅ DO: Use readOnly for queries
  @Transactional(readOnly = true)
  public User getUser(Long id) {
      return userRepository.findById(id).orElseThrow();
  }
  
  // ✅ DO: Keep transactions short
  @Transactional
  public void updateUser(User user) {
      userRepository.save(user);
      // Don't call external APIs here
  }
  
  // ❌ DON'T: Call external services in transaction
  @Transactional
  public void badMethod(User user) {
      userRepository.save(user);
      externalApiClient.call();  // BAD: Holds transaction open
  }
  
  // ✅ DO: Use REQUIRES_NEW for independent operations
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void logAudit(String message) {
      auditRepository.save(new Audit(message));
  }
  
  // ✅ DO: Specify rollback rules for checked exceptions
  @Transactional(rollbackFor = Exception.class)
  public void method() throws Exception {
      // Rolls back on checked exceptions
  }
  
  // ✅ DO: Use appropriate isolation level
  @Transactional(isolation = Isolation.READ_COMMITTED)
  public void normalOperation() {
      // Default isolation is usually sufficient
  }
  
  @Transactional(isolation = Isolation.SERIALIZABLE)
  public void criticalOperation() {
      // Use only when necessary (performance impact)
  }
  
  // ✅ DO: Handle exceptions properly
  @Transactional
  public void method() {
      try {
          userRepository.save(user);
      } catch (Exception e) {
          log.error("Error", e);
          throw e;  // Re-throw to trigger rollback
      }
  }
  
  // ❌ DON'T: Swallow exceptions
  @Transactional
  public void badMethod() {
      try {
          userRepository.save(user);
      } catch (Exception e) {
          log.error("Error", e);
          // Exception swallowed - transaction commits!
      }
  }
  
  // ✅ DO: Use timeout for long operations
  @Transactional(timeout = 30)
  public void batchOperation() {
      // Prevents indefinite locks
  }
  ```

</details>

<details>
  <summary>Common Pitfalls</summary>
  <br/>

  **1. Self-invocation doesn't work:**

  ```java
  @Service
  public class UserService {
      
      public void method1() {
          // No transaction
          method2();  // @Transactional is ignored!
      }
      
      @Transactional
      public void method2() {
          // Transaction doesn't start (self-invocation)
      }
  }
  
  // Solution: Inject self or use separate service
  @Service
  public class UserService {
      
      @Autowired
      private UserService self;
      
      public void method1() {
          self.method2();  // Works!
      }
      
      @Transactional
      public void method2() {
          // Transaction starts
      }
  }
  ```

  **2. Private methods don't work:**

  ```java
  @Service
  public class UserService {
      
      @Transactional  // Ignored!
      private void method() {
          // @Transactional doesn't work on private methods
      }
  }
  ```

  **3. Exception handling:**

  ```java
  // ❌ BAD: Exception swallowed
  @Transactional
  public void badMethod() {
      try {
          userRepository.save(user);
      } catch (Exception e) {
          // Transaction commits (exception not propagated)
      }
  }
  
  // ✅ GOOD: Exception propagated
  @Transactional
  public void goodMethod() {
      try {
          userRepository.save(user);
      } catch (Exception e) {
          log.error("Error", e);
          throw e;  // Transaction rolls back
      }
  }
  ```

  **4. Checked exceptions:**

  ```java
  // ❌ BAD: Checked exception doesn't rollback
  @Transactional
  public void badMethod() throws Exception {
      userRepository.save(user);
      throw new Exception();  // Transaction commits!
  }
  
  // ✅ GOOD: Specify rollback rule
  @Transactional(rollbackFor = Exception.class)
  public void goodMethod() throws Exception {
      userRepository.save(user);
      throw new Exception();  // Transaction rolls back
  }
  ```

</details>

<details>
  <summary>Key Points</summary>
  <br/>

  **@Transactional:**
  + Marks method/class to run within transaction
  + Use on **service layer** (not repository)
  + Use **readOnly = true** for queries
  + Keep transactions **short**

  **Propagation:**
  + **REQUIRED** (default) - Use existing or create new
  + **REQUIRES_NEW** - Always create new (independent)
  + **MANDATORY** - Must have existing
  + **SUPPORTS** - Optional transaction
  + **NOT_SUPPORTED** - No transaction
  + **NEVER** - Must not have transaction
  + **NESTED** - Nested transaction (savepoint)

  **Isolation:**
  + **READ_UNCOMMITTED** - Fastest, least safe
  + **READ_COMMITTED** (default) - Good balance
  + **REPEATABLE_READ** - Prevents non-repeatable reads
  + **SERIALIZABLE** - Complete isolation, slowest

  **Rollback:**
  + **RuntimeException** → Rollback (default)
  + **Checked exceptions** → No rollback (default)
  + Use **rollbackFor** to rollback on checked exceptions
  + Use **noRollbackFor** to prevent rollback

  **Best Practices:**
  + Use **@Transactional** on service layer
  + Use **readOnly = true** for queries
  + Keep transactions **short**
  + Don't call **external services** in transaction
  + Use **REQUIRES_NEW** for independent operations
  + Specify **rollbackFor** for checked exceptions
  + Use appropriate **isolation level**
  + Handle exceptions **properly** (don't swallow)
  + Use **timeout** for long operations
  + Avoid **self-invocation**
  + Don't use on **private methods**

</details>

## Locking

<details>
  <summary>What is Locking?</summary>
  <br/>

  Locking prevents concurrent modification conflicts in multi-user environments.

  **Why use locking?**
  + Prevent lost updates
  + Maintain data consistency
  + Handle concurrent access
  + Avoid race conditions

  **Problem without locking:**

  ```java
  // User A reads product (stock = 10)
  Product product = productRepository.findById(1L).orElseThrow();
  
  // User B reads same product (stock = 10)
  Product product = productRepository.findById(1L).orElseThrow();
  
  // User A decrements stock (stock = 9)
  product.setStock(product.getStock() - 1);
  productRepository.save(product);
  
  // User B decrements stock (stock = 9) - Lost update!
  product.setStock(product.getStock() - 1);
  productRepository.save(product);
  
  // Final stock = 9 (should be 8)
  ```

  **Two types of locking:**
  + **Optimistic Locking** - Assumes conflicts are rare, checks at commit
  + **Pessimistic Locking** - Assumes conflicts are common, locks immediately

</details>

<details>
  <summary>Optimistic Locking (@Version)</summary>
  <br/>

  Optimistic locking uses a version field to detect concurrent modifications.

  **How it works:**
  + Entity has a version field (@Version)
  + Version increments on each update
  + Update fails if version changed (OptimisticLockException)

  **Entity with @Version:**

  ```java
  @Entity
  public class Product {
      
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;
      
      private String name;
      private Integer stock;
      
      @Version
      private Long version;  // Managed by JPA
      
      // Constructors, getters, setters
  }
  ```

  **How version works:**

  ```java
  // Initial state: version = 0
  Product product = new Product("Laptop", 10);
  productRepository.save(product);
  // After save: version = 0
  
  // Update 1
  product.setStock(9);
  productRepository.save(product);
  // After save: version = 1
  
  // Update 2
  product.setStock(8);
  productRepository.save(product);
  // After save: version = 2
  ```

  **Concurrent modification detection:**

  ```java
  @Service
  public class ProductService {
      
      @Autowired
      private ProductRepository productRepository;
      
      @Transactional
      public void updateStock(Long productId, Integer quantity) {
          // User A: Load product (version = 1)
          Product product = productRepository.findById(productId).orElseThrow();
          
          // User B: Load same product (version = 1)
          // User B: Update stock (version = 2)
          
          // User A: Try to update
          product.setStock(product.getStock() - quantity);
          productRepository.save(product);
          // SQL: UPDATE products SET stock = ?, version = 2 WHERE id = ? AND version = 1
          // Fails! Version is now 2, not 1
          // Throws OptimisticLockException
      }
  }
  ```

  **Handle OptimisticLockException:**

  ```java
  @Service
  public class ProductService {
      
      @Autowired
      private ProductRepository productRepository;
      
      @Transactional
      public void updateStockWithRetry(Long productId, Integer quantity) {
          int maxRetries = 3;
          int attempt = 0;
          
          while (attempt < maxRetries) {
              try {
                  Product product = productRepository.findById(productId).orElseThrow();
                  product.setStock(product.getStock() - quantity);
                  productRepository.save(product);
                  return;  // Success
              } catch (OptimisticLockException e) {
                  attempt++;
                  if (attempt >= maxRetries) {
                      throw new RuntimeException("Failed after " + maxRetries + " attempts", e);
                  }
                  // Retry
              }
          }
      }
  }
  ```

</details>

<details>
  <summary>Optimistic Locking - When to Use</summary>
  <br/>

  **Use optimistic locking when:**
  + Conflicts are rare ✅
  + Read operations are frequent ✅
  + Performance is important ✅
  + Long-running transactions ✅
  + Web applications (stateless) ✅

  **Don't use optimistic locking when:**
  + Conflicts are common ❌
  + High contention on same records ❌
  + Cannot handle retry logic ❌

  **Example use cases:**
  + E-commerce product updates
  + User profile updates
  + Blog post editing
  + Configuration changes

  **Advantages:**
  + No database locks
  + Better performance
  + No deadlocks
  + Scalable

  **Disadvantages:**
  + Requires retry logic
  + Can fail at commit time
  + User may lose work

</details>

<details>
  <summary>Pessimistic Locking</summary>
  <br/>

  Pessimistic locking acquires database locks to prevent concurrent access.

  **How it works:**
  + Locks row immediately when read
  + Other transactions wait or fail
  + Lock released when transaction commits/rolls back

  **Lock modes:**

  ```
  PESSIMISTIC_READ  - Shared lock (others can read, not write)
  PESSIMISTIC_WRITE - Exclusive lock (others cannot read or write)
  PESSIMISTIC_FORCE_INCREMENT - Exclusive lock + increment version
  ```

  **PESSIMISTIC_WRITE (most common):**

  ```java
  @Repository
  public interface ProductRepository extends JpaRepository<Product, Long> {
      
      @Lock(LockModeType.PESSIMISTIC_WRITE)
      @Query("SELECT p FROM Product p WHERE p.id = :id")
      Optional<Product> findByIdWithLock(@Param("id") Long id);
  }
  
  @Service
  public class ProductService {
      
      @Autowired
      private ProductRepository productRepository;
      
      @Transactional
      public void updateStock(Long productId, Integer quantity) {
          // Acquires exclusive lock
          Product product = productRepository.findByIdWithLock(productId).orElseThrow();
          // SQL: SELECT * FROM products WHERE id = ? FOR UPDATE
          
          // Other transactions wait here
          
          product.setStock(product.getStock() - quantity);
          productRepository.save(product);
          
          // Lock released when transaction commits
      }
  }
  ```

  **Using EntityManager:**

  ```java
  @Service
  public class ProductService {
      
      @PersistenceContext
      private EntityManager entityManager;
      
      @Transactional
      public void updateStock(Long productId, Integer quantity) {
          // Pessimistic write lock
          Product product = entityManager.find(
              Product.class,
              productId,
              LockModeType.PESSIMISTIC_WRITE
          );
          
          product.setStock(product.getStock() - quantity);
          entityManager.merge(product);
      }
  }
  ```

  **PESSIMISTIC_READ:**

  ```java
  @Repository
  public interface ProductRepository extends JpaRepository<Product, Long> {
      
      @Lock(LockModeType.PESSIMISTIC_READ)
      @Query("SELECT p FROM Product p WHERE p.id = :id")
      Optional<Product> findByIdWithReadLock(@Param("id") Long id);
  }
  
  @Service
  public class ProductService {
      
      @Transactional
      public Product getProduct(Long productId) {
          // Shared lock (others can read, not write)
          return productRepository.findByIdWithReadLock(productId).orElseThrow();
          // SQL: SELECT * FROM products WHERE id = ? FOR SHARE (PostgreSQL)
          // SQL: SELECT * FROM products WHERE id = ? LOCK IN SHARE MODE (MySQL)
      }
  }
  ```

  **PESSIMISTIC_FORCE_INCREMENT:**

  ```java
  @Entity
  public class Product {
      @Id
      private Long id;
      
      @Version
      private Long version;
      
      private Integer stock;
  }
  
  @Repository
  public interface ProductRepository extends JpaRepository<Product, Long> {
      
      @Lock(LockModeType.PESSIMISTIC_FORCE_INCREMENT)
      @Query("SELECT p FROM Product p WHERE p.id = :id")
      Optional<Product> findByIdWithForceIncrement(@Param("id") Long id);
  }
  
  @Service
  public class ProductService {
      
      @Transactional
      public void updateStock(Long productId, Integer quantity) {
          // Exclusive lock + increment version
          Product product = productRepository.findByIdWithForceIncrement(productId).orElseThrow();
          
          product.setStock(product.getStock() - quantity);
          productRepository.save(product);
          
          // Version incremented even if no changes
      }
  }
  ```

</details>

<details>
  <summary>Pessimistic Locking - When to Use</summary>
  <br/>

  **Use pessimistic locking when:**
  + Conflicts are common ✅
  + High contention on same records ✅
  + Cannot afford retry logic ✅
  + Critical operations (money transfer) ✅
  + Short transactions ✅

  **Don't use pessimistic locking when:**
  + Conflicts are rare ❌
  + Long-running transactions ❌
  + High read volume ❌
  + Can cause deadlocks ❌

  **Example use cases:**
  + Bank account transfers
  + Inventory management
  + Ticket booking
  + Seat reservation

  **Advantages:**
  + Guaranteed consistency
  + No retry logic needed
  + Prevents conflicts upfront

  **Disadvantages:**
  + Reduced concurrency
  + Potential deadlocks
  + Performance impact
  + Locks held during transaction

</details>

<details>
  <summary>Lock Timeout</summary>
  <br/>

  Set timeout for acquiring locks.

  **Using @QueryHints:**

  ```java
  @Repository
  public interface ProductRepository extends JpaRepository<Product, Long> {
      
      @Lock(LockModeType.PESSIMISTIC_WRITE)
      @QueryHints({
          @QueryHint(name = "javax.persistence.lock.timeout", value = "5000")
      })
      @Query("SELECT p FROM Product p WHERE p.id = :id")
      Optional<Product> findByIdWithLock(@Param("id") Long id);
      // Timeout after 5 seconds
  }
  ```

  **Using EntityManager:**

  ```java
  @Service
  public class ProductService {
      
      @PersistenceContext
      private EntityManager entityManager;
      
      @Transactional
      public void updateStock(Long productId, Integer quantity) {
          Map<String, Object> properties = new HashMap<>();
          properties.put("javax.persistence.lock.timeout", 5000);
          
          Product product = entityManager.find(
              Product.class,
              productId,
              LockModeType.PESSIMISTIC_WRITE,
              properties
          );
          
          product.setStock(product.getStock() - quantity);
      }
  }
  ```

  **Handle timeout:**

  ```java
  @Service
  public class ProductService {
      
      @Transactional
      public void updateStock(Long productId, Integer quantity) {
          try {
              Product product = productRepository.findByIdWithLock(productId).orElseThrow();
              product.setStock(product.getStock() - quantity);
              productRepository.save(product);
          } catch (LockTimeoutException e) {
              throw new RuntimeException("Could not acquire lock", e);
          }
      }
  }
  ```

</details>

<details>
  <summary>Optimistic vs Pessimistic Locking</summary>
  <br/>

  **Comparison:**

  | Aspect | Optimistic | Pessimistic |
  |--------|-----------|-------------|
  | **When locks** | At commit | At read |
  | **Database locks** | No | Yes |
  | **Performance** | Better | Worse |
  | **Concurrency** | Higher | Lower |
  | **Conflicts** | Detected late | Prevented early |
  | **Retry logic** | Required | Not required |
  | **Deadlocks** | No | Possible |
  | **Use case** | Low contention | High contention |
  | **Transaction length** | Long | Short |

  **Example comparison:**

  ```java
  // Optimistic locking
  @Entity
  public class Product {
      @Id
      private Long id;
      
      @Version
      private Long version;
      
      private Integer stock;
  }
  
  @Transactional
  public void updateStockOptimistic(Long id, Integer quantity) {
      Product product = productRepository.findById(id).orElseThrow();
      // No lock acquired
      
      product.setStock(product.getStock() - quantity);
      productRepository.save(product);
      // Check version at commit
      // Throws OptimisticLockException if version changed
  }
  
  // Pessimistic locking
  @Transactional
  public void updateStockPessimistic(Long id, Integer quantity) {
      Product product = productRepository.findByIdWithLock(id).orElseThrow();
      // Lock acquired immediately
      // Other transactions wait
      
      product.setStock(product.getStock() - quantity);
      productRepository.save(product);
      // Lock released at commit
  }
  ```

</details>

<details>
  <summary>Combining Both Locks</summary>
  <br/>

  You can use both optimistic and pessimistic locking together.

  **Entity with @Version:**

  ```java
  @Entity
  public class Product {
      
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;
      
      private String name;
      private Integer stock;
      
      @Version
      private Long version;  // Optimistic locking
  }
  ```

  **Repository with pessimistic lock:**

  ```java
  @Repository
  public interface ProductRepository extends JpaRepository<Product, Long> {
      
      @Lock(LockModeType.PESSIMISTIC_WRITE)  // Pessimistic locking
      @Query("SELECT p FROM Product p WHERE p.id = :id")
      Optional<Product> findByIdWithLock(@Param("id") Long id);
  }
  ```

  **Service using both:**

  ```java
  @Service
  public class ProductService {
      
      @Transactional
      public void updateStock(Long productId, Integer quantity) {
          // Pessimistic lock acquired
          Product product = productRepository.findByIdWithLock(productId).orElseThrow();
          
          product.setStock(product.getStock() - quantity);
          productRepository.save(product);
          
          // Version checked at commit (optimistic)
          // Both locks provide double protection
      }
  }
  ```

  **When to combine:**
  + Extra safety for critical operations
  + Prevent both concurrent access and lost updates
  + Financial transactions
  + Inventory management

</details>

<details>
  <summary>Best Practices</summary>
  <br/>

  ```java
  // ✅ DO: Use optimistic locking by default
  @Entity
  public class Product {
      @Id
      private Long id;
      
      @Version
      private Long version;
  }
  
  // ✅ DO: Handle OptimisticLockException
  @Transactional
  public void updateProduct(Product product) {
      try {
          productRepository.save(product);
      } catch (OptimisticLockException e) {
          // Retry or inform user
          throw new ConcurrentModificationException("Product was modified by another user");
      }
  }
  
  // ✅ DO: Use pessimistic locking for high contention
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT p FROM Product p WHERE p.id = :id")
  Optional<Product> findByIdWithLock(@Param("id") Long id);
  
  // ✅ DO: Set lock timeout
  @QueryHints({
      @QueryHint(name = "javax.persistence.lock.timeout", value = "5000")
  })
  
  // ✅ DO: Keep pessimistic lock duration short
  @Transactional
  public void updateStock(Long id, Integer quantity) {
      Product product = productRepository.findByIdWithLock(id).orElseThrow();
      product.setStock(product.getStock() - quantity);
      productRepository.save(product);
      // Transaction ends quickly
  }
  
  // ❌ DON'T: Hold pessimistic lock for long time
  @Transactional
  public void badMethod(Long id) {
      Product product = productRepository.findByIdWithLock(id).orElseThrow();
      
      // BAD: External API call while holding lock
      externalService.call();
      
      product.setStock(product.getStock() - 1);
      productRepository.save(product);
  }
  
  // ✅ DO: Use PESSIMISTIC_WRITE for updates
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Optional<Product> findByIdWithLock(Long id);
  
  // ✅ DO: Use PESSIMISTIC_READ for reads (if needed)
  @Lock(LockModeType.PESSIMISTIC_READ)
  Optional<Product> findByIdWithReadLock(Long id);
  
  // ✅ DO: Choose appropriate locking strategy
  // Low contention → Optimistic locking
  // High contention → Pessimistic locking
  // Critical operations → Pessimistic locking
  
  // ✅ DO: Test for deadlocks
  // Pessimistic locking can cause deadlocks
  // Always acquire locks in same order
  
  // ✅ DO: Use @Version with Long or Integer
  @Version
  private Long version;  // Recommended
  
  @Version
  private Integer version;  // Also works
  
  // ❌ DON'T: Manually modify version field
  product.setVersion(5);  // BAD: Let JPA manage it
  ```

  **Key Points**
  <br/>

  **Optimistic Locking:**
  + Uses **@Version** field
  + No database locks
  + Checks version at **commit time**
  + Throws **OptimisticLockException** if conflict
  + Better **performance**
  + Higher **concurrency**
  + Requires **retry logic**
  + Use for **low contention**

  **Pessimistic Locking:**
  + Uses database locks (**FOR UPDATE**)
  + Locks at **read time**
  + Blocks other transactions
  + Three modes: **READ**, **WRITE**, **FORCE_INCREMENT**
  + Lower **performance**
  + Lower **concurrency**
  + No retry logic needed
  + Use for **high contention**
  + Can cause **deadlocks**

  **Lock Modes:**
  + **PESSIMISTIC_READ** - Shared lock (others can read)
  + **PESSIMISTIC_WRITE** - Exclusive lock (others wait)
  + **PESSIMISTIC_FORCE_INCREMENT** - Exclusive lock + increment version

  **When to Use:**
  + **Optimistic** - Web apps, low contention, long transactions
  + **Pessimistic** - High contention, critical operations, short transactions

  **Best Practices:**
  + Use **optimistic locking by default**
  + Handle **OptimisticLockException**
  + Use **pessimistic locking** for high contention
  + Set **lock timeout**
  + Keep pessimistic lock duration **short**
  + Don't hold locks during **external calls**
  + Test for **deadlocks**
  + Let JPA manage **@Version** field

</details>

## N+1 Problem

<details>
  <summary>What is the N+1 Problem?</summary>
  <br/>

  The N+1 problem occurs when loading a collection triggers N additional queries.

  **Problem:**
  + 1 query to load parent entities
  + N queries to load related entities (one per parent)
  + Total: 1 + N queries

  **Example:**

  ```java
  @Entity
  public class User {
      @Id
      private Long id;
      private String username;
      
      @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
      private List<Order> orders;
  }
  
  @Entity
  public class Order {
      @Id
      private Long id;
      private BigDecimal total;
      
      @ManyToOne
      @JoinColumn(name = "user_id")
      private User user;
  }
  ```

  **N+1 Problem in action:**

  ```java
  @Service
  public class UserService {
      
      @Transactional
      public void printUsersWithOrders() {
          List<User> users = userRepository.findAll();
          // Query 1: SELECT * FROM users
          
          for (User user : users) {
              System.out.println(user.getUsername());
              System.out.println(user.getOrders().size());
              // Query 2: SELECT * FROM orders WHERE user_id = 1
              // Query 3: SELECT * FROM orders WHERE user_id = 2
              // Query 4: SELECT * FROM orders WHERE user_id = 3
              // ... N queries (one per user)
          }
          
          // Total: 1 + N queries
          // If 100 users, 101 queries!
      }
  }
  ```

  **Why it's a problem:**
  + Poor performance
  + Database overload
  + Slow response times
  + Scalability issues

</details>

<details>
  <summary>Detecting N+1 Problem</summary>
  <br/>

  **Method 1: Enable SQL logging:**

  ```properties
  # application.properties
  spring.jpa.show-sql=true
  spring.jpa.properties.hibernate.format_sql=true
  logging.level.org.hibernate.SQL=DEBUG
  logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
  ```

  **Method 2: Use Hibernate statistics:**

  ```properties
  # application.properties
  spring.jpa.properties.hibernate.generate_statistics=true
  ```

  ```java
  @Service
  public class UserService {
      
      @PersistenceContext
      private EntityManager entityManager;
      
      @Transactional
      public void detectN1() {
          Session session = entityManager.unwrap(Session.class);
          SessionFactory sessionFactory = session.getSessionFactory();
          Statistics stats = sessionFactory.getStatistics();
          stats.clear();
          
          List<User> users = userRepository.findAll();
          for (User user : users) {
              user.getOrders().size();
          }
          
          System.out.println("Queries executed: " + stats.getPrepareStatementCount());
          // Shows total number of queries
      }
  }
  ```

  **Method 3: Use query logging tools:**
  + P6Spy
  + Datasource-proxy
  + Spring Boot Actuator

</details>

<details>
  <summary>Solution 1: Fetch Join (JPQL)</summary>
  <br/>

  Fetch join loads related entities in a single query.

  **Repository with fetch join:**

  ```java
  @Repository
  public interface UserRepository extends JpaRepository<User, Long> {
      
      // Fetch join
      @Query("SELECT u FROM User u LEFT JOIN FETCH u.orders")
      List<User> findAllWithOrders();
      
      // Fetch join with WHERE clause
      @Query("SELECT u FROM User u LEFT JOIN FETCH u.orders WHERE u.id = :id")
      Optional<User> findByIdWithOrders(@Param("id") Long id);
      
      // Multiple fetch joins
      @Query("SELECT u FROM User u " +
             "LEFT JOIN FETCH u.orders o " +
             "LEFT JOIN FETCH o.items")
      List<User> findAllWithOrdersAndItems();
  }
  ```

  **Usage:**

  ```java
  @Service
  public class UserService {
      
      @Transactional
      public void printUsersWithOrders() {
          List<User> users = userRepository.findAllWithOrders();
          // Single query: 
          // SELECT u.*, o.* FROM users u 
          // LEFT JOIN orders o ON u.id = o.user_id
          
          for (User user : users) {
              System.out.println(user.getUsername());
              System.out.println(user.getOrders().size());
              // No additional queries!
          }
          
          // Total: 1 query (instead of 1 + N)
      }
  }
  ```

  **Important: Use DISTINCT:**

  ```java
  @Repository
  public interface UserRepository extends JpaRepository<User, Long> {
      
      // Without DISTINCT - returns duplicate users
      @Query("SELECT u FROM User u LEFT JOIN FETCH u.orders")
      List<User> findAllWithOrders();
      
      // With DISTINCT - removes duplicates
      @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.orders")
      List<User> findAllWithOrdersDistinct();
  }
  ```

  **Advantages:**
  + Single query
  + Best performance
  + Simple to implement

  **Disadvantages:**
  + Can return duplicates (use DISTINCT)
  + Cartesian product with multiple collections
  + Cannot use pagination with fetch join

</details>

<details>
  <summary>Solution 2: Entity Graph</summary>
  <br/>

  Entity graphs specify which relationships to load.

  **Define entity graph:**

  ```java
  @Entity
  @NamedEntityGraph(
      name = "User.orders",
      attributeNodes = @NamedAttributeNode("orders")
  )
  public class User {
      @Id
      private Long id;
      
      @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
      private List<Order> orders;
  }
  ```

  **Use in repository:**

  ```java
  @Repository
  public interface UserRepository extends JpaRepository<User, Long> {
      
      // Use named entity graph
      @EntityGraph(value = "User.orders")
      List<User> findAll();
      
      @EntityGraph(value = "User.orders")
      Optional<User> findById(Long id);
      
      // Use attribute paths (no need for @NamedEntityGraph)
      @EntityGraph(attributePaths = {"orders"})
      List<User> findAllWithOrders();
      
      // Multiple relationships
      @EntityGraph(attributePaths = {"orders", "orders.items"})
      List<User> findAllWithOrdersAndItems();
  }
  ```

  **Usage:**

  ```java
  @Service
  public class UserService {
      
      @Transactional
      public void printUsersWithOrders() {
          List<User> users = userRepository.findAll();
          // Single query with LEFT JOIN
          
          for (User user : users) {
              System.out.println(user.getOrders().size());
              // No additional queries!
          }
      }
  }
  ```

  **Dynamic entity graph:**

  ```java
  @Service
  public class UserService {
      
      @PersistenceContext
      private EntityManager entityManager;
      
      @Transactional
      public List<User> findAllWithOrders() {
          EntityGraph<User> graph = entityManager.createEntityGraph(User.class);
          graph.addAttributeNodes("orders");
          
          return entityManager
              .createQuery("SELECT u FROM User u", User.class)
              .setHint("javax.persistence.fetchgraph", graph)
              .getResultList();
      }
  }
  ```

  **Advantages:**
  + Flexible (can be applied to any query method)
  + No JPQL needed
  + Reusable

  **Disadvantages:**
  + Less explicit than fetch join
  + Can still have Cartesian product

</details>

<details>
  <summary>Solution 3: Batch Fetching</summary>
  <br/>

  Batch fetching loads multiple collections in fewer queries.

  **Using @BatchSize:**

  ```java
  @Entity
  public class User {
      @Id
      private Long id;
      
      @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
      @BatchSize(size = 10)  // Load 10 users' orders at once
      private List<Order> orders;
  }
  ```

  **How it works:**

  ```java
  @Service
  public class UserService {
      
      @Transactional
      public void printUsersWithOrders() {
          List<User> users = userRepository.findAll();
          // Query 1: SELECT * FROM users
          
          for (User user : users) {
              System.out.println(user.getOrders().size());
          }
          
          // With @BatchSize(size = 10):
          // Query 2: SELECT * FROM orders WHERE user_id IN (1, 2, 3, ..., 10)
          // Query 3: SELECT * FROM orders WHERE user_id IN (11, 12, 13, ..., 20)
          // ...
          
          // Total: 1 + (N / batch_size) queries
          // If 100 users, 11 queries (instead of 101)
      }
  }
  ```

  **Global batch size:**

  ```properties
  # application.properties
  spring.jpa.properties.hibernate.default_batch_fetch_size=10
  ```

  **Advantages:**
  + Reduces queries significantly
  + No code changes in service layer
  + Works with pagination

  **Disadvantages:**
  + Still multiple queries (not single query)
  + Less efficient than fetch join
  + Requires tuning batch size

</details>

<details>
  <summary>Solution 4: Subselect Fetching</summary>
  <br/>

  Subselect fetching loads all collections in a second query.

  **Using @Fetch(FetchMode.SUBSELECT):**

  ```java
  @Entity
  public class User {
      @Id
      private Long id;
      
      @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
      @Fetch(FetchMode.SUBSELECT)
      private List<Order> orders;
  }
  ```

  **How it works:**

  ```java
  @Service
  public class UserService {
      
      @Transactional
      public void printUsersWithOrders() {
          List<User> users = userRepository.findAll();
          // Query 1: SELECT * FROM users
          
          for (User user : users) {
              System.out.println(user.getOrders().size());
          }
          
          // Query 2: SELECT * FROM orders WHERE user_id IN (
          //   SELECT id FROM users
          // )
          
          // Total: 2 queries (instead of 1 + N)
      }
  }
  ```

  **Advantages:**
  + Only 2 queries total
  + Works with any number of parents
  + No Cartesian product

  **Disadvantages:**
  + Subquery can be slow
  + Not as efficient as fetch join
  + Loads all collections (even if not needed)

</details>

<details>
  <summary>Solution 5: DTO Projection</summary>
  <br/>

  DTO projection fetches only needed data in a single query.

  **DTO class:**

  ```java
  public class UserWithOrdersDTO {
      private Long userId;
      private String username;
      private Long orderId;
      private BigDecimal orderTotal;
      
      public UserWithOrdersDTO(Long userId, String username, Long orderId, BigDecimal orderTotal) {
          this.userId = userId;
          this.username = username;
          this.orderId = orderId;
          this.orderTotal = orderTotal;
      }
      
      // Getters
  }
  ```

  **Repository:**

  ```java
  @Repository
  public interface UserRepository extends JpaRepository<User, Long> {
      
      @Query("SELECT new com.example.dto.UserWithOrdersDTO(" +
             "u.id, u.username, o.id, o.total) " +
             "FROM User u LEFT JOIN u.orders o")
      List<UserWithOrdersDTO> findAllWithOrdersDTO();
  }
  ```

  **Usage:**

  ```java
  @Service
  public class UserService {
      
      @Transactional
      public void printUsersWithOrders() {
          List<UserWithOrdersDTO> results = userRepository.findAllWithOrdersDTO();
          // Single query
          
          // Group by user
          Map<Long, List<UserWithOrdersDTO>> grouped = results.stream()
              .collect(Collectors.groupingBy(UserWithOrdersDTO::getUserId));
          
          grouped.forEach((userId, orders) -> {
              System.out.println("User: " + orders.get(0).getUsername());
              System.out.println("Orders: " + orders.size());
          });
      }
  }
  ```

  **Advantages:**
  + Single query
  + Fetch only needed fields
  + No entity overhead

  **Disadvantages:**
  + More code (DTO classes)
  + Manual mapping
  + Not suitable for complex object graphs

</details>

<details>
  <summary>Solution Comparison</summary>
  <br/>

  **Performance comparison:**

  | Solution | Queries | Performance | Complexity | Use Case |
  |----------|---------|-------------|------------|----------|
  | **Fetch Join** | 1 | ⭐⭐⭐⭐⭐ Best | Low | Default choice |
  | **Entity Graph** | 1 | ⭐⭐⭐⭐⭐ Best | Low | Flexible loading |
  | **Batch Fetching** | 1 + N/batch | ⭐⭐⭐ Good | Low | With pagination |
  | **Subselect** | 2 | ⭐⭐⭐⭐ Very Good | Low | Many parents |
  | **DTO Projection** | 1 | ⭐⭐⭐⭐⭐ Best | Medium | Read-only |

  **When to use each:**

  ```java
  // Use Fetch Join for:
  // - Loading single entity with relationships
  // - Best performance
  // - No pagination needed
  @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.orders")
  List<User> findAllWithOrders();
  
  // Use Entity Graph for:
  // - Flexible loading strategy
  // - Multiple query methods
  // - Reusable graphs
  @EntityGraph(attributePaths = {"orders"})
  List<User> findAll();
  
  // Use Batch Fetching for:
  // - Pagination
  // - Large result sets
  // - Acceptable to have multiple queries
  @BatchSize(size = 10)
  private List<Order> orders;
  
  // Use Subselect for:
  // - Loading all collections at once
  // - Many parent entities
  // - Acceptable to have 2 queries
  @Fetch(FetchMode.SUBSELECT)
  private List<Order> orders;
  
  // Use DTO Projection for:
  // - Read-only operations
  // - Reporting
  // - Need specific fields only
  @Query("SELECT new com.example.dto.UserDTO(u.id, u.username) FROM User u")
  List<UserDTO> findAllDTO();
  ```

</details>

<details>
  <summary>Multiple Collections Problem</summary>
  <br/>

  Fetching multiple collections causes Cartesian product.

  **Problem:**

  ```java
  @Entity
  public class User {
      @Id
      private Long id;
      
      @OneToMany(mappedBy = "user")
      private List<Order> orders;
      
      @OneToMany(mappedBy = "user")
      private List<Address> addresses;
  }
  
  // ❌ BAD: Cartesian product
  @Query("SELECT DISTINCT u FROM User u " +
         "LEFT JOIN FETCH u.orders " +
         "LEFT JOIN FETCH u.addresses")
  List<User> findAllWithOrdersAndAddresses();
  
  // If user has 10 orders and 5 addresses:
  // Returns 50 rows (10 * 5 = Cartesian product)
  ```

  **Solution 1: Multiple queries:**

  ```java
  @Repository
  public interface UserRepository extends JpaRepository<User, Long> {
      
      @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.orders")
      List<User> findAllWithOrders();
      
      @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.addresses WHERE u IN :users")
      List<User> findWithAddresses(@Param("users") List<User> users);
  }
  
  @Service
  public class UserService {
      
      @Transactional
      public List<User> findAllWithOrdersAndAddresses() {
          // Query 1: Load users with orders
          List<User> users = userRepository.findAllWithOrders();
          
          // Query 2: Load addresses
          userRepository.findWithAddresses(users);
          
          return users;
      }
  }
  ```

  **Solution 2: Entity graph with multiple queries:**

  ```java
  @Service
  public class UserService {
      
      @PersistenceContext
      private EntityManager entityManager;
      
      @Transactional
      public List<User> findAllWithOrdersAndAddresses() {
          // Query 1: Load users with orders
          EntityGraph<User> graph1 = entityManager.createEntityGraph(User.class);
          graph1.addAttributeNodes("orders");
          
          List<User> users = entityManager
              .createQuery("SELECT DISTINCT u FROM User u", User.class)
              .setHint("javax.persistence.fetchgraph", graph1)
              .getResultList();
          
          // Query 2: Load addresses
          EntityGraph<User> graph2 = entityManager.createEntityGraph(User.class);
          graph2.addAttributeNodes("addresses");
          
          entityManager
              .createQuery("SELECT DISTINCT u FROM User u WHERE u IN :users", User.class)
              .setParameter("users", users)
              .setHint("javax.persistence.fetchgraph", graph2)
              .getResultList();
          
          return users;
      }
  }
  ```

  **Solution 3: Batch fetching:**

  ```java
  @Entity
  public class User {
      @Id
      private Long id;
      
      @OneToMany(mappedBy = "user")
      @BatchSize(size = 10)
      private List<Order> orders;
      
      @OneToMany(mappedBy = "user")
      @BatchSize(size = 10)
      private List<Address> addresses;
  }
  ```

</details>

<details>
  <summary>Best Practices</summary>
  <br/>

  ```java
  // ✅ DO: Use fetch join for single collection
  @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.orders")
  List<User> findAllWithOrders();
  
  // ✅ DO: Use DISTINCT with fetch join
  @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.orders")
  List<User> findAllWithOrders();
  
  // ✅ DO: Use entity graph for flexibility
  @EntityGraph(attributePaths = {"orders"})
  List<User> findAll();
  
  // ✅ DO: Use batch fetching with pagination
  @BatchSize(size = 10)
  private List<Order> orders;
  
  // ✅ DO: Enable SQL logging to detect N+1
  spring.jpa.show-sql=true
  
  // ✅ DO: Use DTO projection for read-only
  @Query("SELECT new com.example.dto.UserDTO(u.id, u.username) FROM User u")
  List<UserDTO> findAllDTO();
  
  // ❌ DON'T: Fetch multiple collections in one query
  @Query("SELECT u FROM User u " +
         "LEFT JOIN FETCH u.orders " +
         "LEFT JOIN FETCH u.addresses")  // Cartesian product!
  List<User> findAll();
  
  // ❌ DON'T: Use fetch join with pagination
  @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.orders")
  Page<User> findAll(Pageable pageable);  // Warning: in-memory pagination!
  
  // ✅ DO: Use batch fetching with pagination instead
  @BatchSize(size = 10)
  private List<Order> orders;
  
  Page<User> findAll(Pageable pageable);  // Works correctly
  
  // ✅ DO: Test query performance
  @Test
  public void testN1Problem() {
      Statistics stats = sessionFactory.getStatistics();
      stats.clear();
      
      List<User> users = userRepository.findAll();
      users.forEach(u -> u.getOrders().size());
      
      long queries = stats.getPrepareStatementCount();
      assertTrue(queries <= 2, "N+1 problem detected: " + queries + " queries");
  }
  ```
 **Key Points**
  <br/>

  **N+1 Problem:**
  + 1 query to load parents
  + N queries to load children (one per parent)
  + Causes poor performance
  + Common with LAZY loading

  **Detection:**
  + Enable SQL logging
  + Use Hibernate statistics
  + Use query logging tools (P6Spy)

  **Solutions:**
  + **Fetch Join** - Single query with JOIN FETCH (best for single collection)
  + **Entity Graph** - Flexible loading with @EntityGraph
  + **Batch Fetching** - Load in batches with @BatchSize
  + **Subselect** - Load all in 2 queries with @Fetch(SUBSELECT)
  + **DTO Projection** - Fetch only needed fields

  **Best Solution:**
  + **Fetch Join** or **Entity Graph** for single collection
  + **Batch Fetching** for pagination
  + **Multiple queries** for multiple collections
  + **DTO Projection** for read-only operations

  **Important:**
  + Always use **DISTINCT** with fetch join
  + Don't fetch **multiple collections** in one query (Cartesian product)
  + Don't use **fetch join with pagination** (in-memory pagination)
  + Use **batch fetching** with pagination instead
  + Enable **SQL logging** to detect N+1
  + Test query **performance**
</details>

## Common Issues

<details>
  <summary>LazyInitializationException</summary>
  <br/>

  **LazyInitializationException** occurs when accessing lazy collection outside transaction.

  **Error message:**

  ```
  org.hibernate.LazyInitializationException: 
  failed to lazily initialize a collection of role: com.example.User.orders, 
  could not initialize proxy - no Session
  ```

  **Common scenario:**

  ```java
  @Service
  public class UserService {
      
      @Transactional
      public User getUser(Long id) {
          return userRepository.findById(id).orElseThrow();
      }  // Transaction ends, session closes
  }
  
  @RestController
  public class UserController {
      
      @GetMapping("/users/{id}")
      public UserDTO getUser(@PathVariable Long id) {
          User user = userService.getUser(id);
          
          // LazyInitializationException!
          List<Order> orders = user.getOrders();
          
          return new UserDTO(user);
      }
  }
  ```

  **Why it happens:**
  + LAZY collection not loaded within transaction
  + Transaction ends, Hibernate session closes
  + Accessing collection outside session fails

  **Solution 1: Fetch join:**

  ```java
  @Repository
  public interface UserRepository extends JpaRepository<User, Long> {
      
      @Query("SELECT u FROM User u LEFT JOIN FETCH u.orders WHERE u.id = :id")
      Optional<User> findByIdWithOrders(@Param("id") Long id);
  }
  ```

  **Solution 2: Entity graph:**

  ```java
  @Repository
  public interface UserRepository extends JpaRepository<User, Long> {
      
      @EntityGraph(attributePaths = {"orders"})
      Optional<User> findById(Long id);
  }
  ```

  **Solution 3: Force initialization:**

  ```java
  @Service
  public class UserService {
      
      @Transactional
      public User getUser(Long id) {
          User user = userRepository.findById(id).orElseThrow();
          user.getOrders().size();  // Force load
          return user;
      }
  }
  ```

  **Solution 4: DTO projection:**

  ```java
  @Query("SELECT new com.example.dto.UserDTO(u.id, u.username) FROM User u")
  UserDTO findUserDTOById(Long id);
  ```

</details>

<details>
  <summary>MultipleBagFetchException</summary>
  <br/>

  **MultipleBagFetchException** occurs when fetching multiple collections with fetch join.

  **Error message:**

  ```
  org.hibernate.loader.MultipleBagFetchException: 
  cannot simultaneously fetch multiple bags
  ```

  **Problem:**

  ```java
  @Entity
  public class User {
      @Id
      private Long id;
      
      @OneToMany(mappedBy = "user")
      private List<Order> orders;  // Bag
      
      @OneToMany(mappedBy = "user")
      private List<Address> addresses;  // Bag
  }
  
  // ❌ Throws MultipleBagFetchException
  @Query("SELECT u FROM User u " +
         "LEFT JOIN FETCH u.orders " +
         "LEFT JOIN FETCH u.addresses")
  List<User> findAllWithOrdersAndAddresses();
  ```

  **What is a "bag"?**
  + Hibernate term for unordered collection (List without @OrderColumn)
  + Cannot fetch multiple bags simultaneously
  + Causes ambiguity in result set mapping

  **Solution 1: Use Set instead of List:**

  ```java
  @Entity
  public class User {
      @Id
      private Long id;
      
      @OneToMany(mappedBy = "user")
      private Set<Order> orders;  // Set instead of List
      
      @OneToMany(mappedBy = "user")
      private Set<Address> addresses;  // Set instead of List
  }
  
  // ✅ Works with Set
  @Query("SELECT DISTINCT u FROM User u " +
         "LEFT JOIN FETCH u.orders " +
         "LEFT JOIN FETCH u.addresses")
  List<User> findAllWithOrdersAndAddresses();
  ```

  **Solution 2: Multiple queries:**

  ```java
  @Repository
  public interface UserRepository extends JpaRepository<User, Long> {
      
      @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.orders")
      List<User> findAllWithOrders();
      
      @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.addresses WHERE u IN :users")
      List<User> findWithAddresses(@Param("users") List<User> users);
  }
  
  @Service
  public class UserService {
      
      @Transactional
      public List<User> findAllWithOrdersAndAddresses() {
          List<User> users = userRepository.findAllWithOrders();
          userRepository.findWithAddresses(users);
          return users;
      }
  }
  ```

  **Solution 3: Use @OrderColumn:**

  ```java
  @Entity
  public class User {
      @Id
      private Long id;
      
      @OneToMany(mappedBy = "user")
      @OrderColumn(name = "order_index")  // Makes it ordered
      private List<Order> orders;
      
      @OneToMany(mappedBy = "user")
      private List<Address> addresses;
  }
  ```

  **Solution 4: Batch fetching:**

  ```java
  @Entity
  public class User {
      @Id
      private Long id;
      
      @OneToMany(mappedBy = "user")
      @BatchSize(size = 10)
      private List<Order> orders;
      
      @OneToMany(mappedBy = "user")
      @BatchSize(size = 10)
      private List<Address> addresses;
  }
  ```

</details>

<details>
  <summary>@OneToOne Lazy Fetch Problem</summary>
  <br/>

  **@OneToOne lazy loading doesn't work on non-owning side.**

  **Problem:**

  ```java
  @Entity
  public class User {
      @Id
      private Long id;
      
      // Non-owning side (mappedBy)
      @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
      private UserProfile profile;  // Always loaded eagerly!
  }
  
  @Entity
  public class UserProfile {
      @Id
      private Long id;
      
      // Owning side (has @JoinColumn)
      @OneToOne(fetch = FetchType.LAZY)
      @JoinColumn(name = "user_id")
      private User user;
  }
  ```

  **Why it happens:**
  + Hibernate needs to know if profile exists
  + Must query database to check
  + Cannot create proxy without knowing if entity exists
  + Results in eager loading despite LAZY setting

  **Test:**

  ```java
  @Transactional
  public void testLazyLoading() {
      User user = userRepository.findById(1L).orElseThrow();
      // SQL: SELECT u.*, p.* FROM users u 
      //      LEFT JOIN user_profiles p ON u.id = p.user_id
      //      WHERE u.id = 1
      
      // Profile already loaded (not lazy!)
      System.out.println(user.getProfile().getBio());
  }
  ```

  **Solution 1: Make relationship optional:**

  ```java
  @Entity
  public class User {
      @Id
      private Long id;
      
      @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, optional = false)
      private UserProfile profile;  // Still doesn't work
  }
  ```

  **Solution 2: Use @MapsId (shared primary key):**

  ```java
  @Entity
  public class User {
      @Id
      private Long id;
      
      @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
      private UserProfile profile;  // Now works!
  }
  
  @Entity
  public class UserProfile {
      @Id
      private Long id;
      
      @MapsId  // Share primary key with User
      @OneToOne(fetch = FetchType.LAZY)
      @JoinColumn(name = "id")
      private User user;
  }
  ```

  **Solution 3: Use bytecode enhancement:**

  ```xml
  <!-- pom.xml -->
  <plugin>
      <groupId>org.hibernate.orm.tooling</groupId>
      <artifactId>hibernate-enhance-maven-plugin</artifactId>
      <version>${hibernate.version}</version>
      <executions>
          <execution>
              <goals>
                  <goal>enhance</goal>
              </goals>
          </execution>
      </executions>
  </plugin>
  ```

  ```properties
  # application.properties
  spring.jpa.properties.hibernate.bytecode.use_reflection_optimizer=false
  spring.jpa.properties.hibernate.enhancer.enableLazyInitialization=true
  ```

  **Solution 4: Change to @ManyToOne:**

  ```java
  @Entity
  public class User {
      @Id
      private Long id;
      
      @OneToMany(mappedBy = "user")
      private List<UserProfile> profiles;  // Changed to List
  }
  
  @Entity
  public class UserProfile {
      @Id
      private Long id;
      
      @ManyToOne(fetch = FetchType.LAZY)
      @JoinColumn(name = "user_id")
      private User user;
  }
  ```

  **Solution 5: Use DTO projection:**

  ```java
  @Query("SELECT new com.example.dto.UserDTO(u.id, u.username) FROM User u")
  UserDTO findUserDTOById(Long id);
  ```

</details>

<details>
  <summary>Detached Entity Passed to Persist</summary>
  <br/>

  **Error occurs when trying to persist entity with existing ID.**

  **Error message:**

  ```
  org.hibernate.PersistentObjectException: 
  detached entity passed to persist: com.example.User
  ```

  **Problem:**

  ```java
  @Service
  public class UserService {
      
      @Transactional
      public void saveUser() {
          User user = new User();
          user.setId(1L);  // Setting ID manually
          user.setUsername("john");
          
          userRepository.save(user);  // Error!
      }
  }
  ```

  **Why it happens:**
  + Entity has ID set
  + Hibernate thinks it's detached (already persisted)
  + Cannot persist detached entity

  **Solution 1: Use merge instead:**

  ```java
  @Transactional
  public void saveUser() {
      User user = new User();
      user.setId(1L);
      user.setUsername("john");
      
      entityManager.merge(user);  // Use merge
  }
  ```

  **Solution 2: Don't set ID:**

  ```java
  @Transactional
  public void saveUser() {
      User user = new User();
      // Don't set ID - let database generate it
      user.setUsername("john");
      
      userRepository.save(user);  // Works
  }
  ```

  **Solution 3: Check if exists:**

  ```java
  @Transactional
  public void saveUser(User user) {
      if (user.getId() != null && userRepository.existsById(user.getId())) {
          userRepository.save(user);  // Update
      } else {
          user.setId(null);
          userRepository.save(user);  // Insert
      }
  }
  ```

</details>

<details>
  <summary>NonUniqueResultException</summary>
  <br/>

  **Error occurs when query returns multiple results but expects single result.**

  **Error message:**

  ```
  javax.persistence.NonUniqueResultException: 
  query did not return a unique result: 2
  ```

  **Problem:**

  ```java
  @Repository
  public interface UserRepository extends JpaRepository<User, Long> {
      
      // Expects single result
      User findByUsername(String username);
  }
  
  @Service
  public class UserService {
      
      @Transactional
      public void getUser() {
          // If multiple users with same username exist
          User user = userRepository.findByUsername("john");  // Error!
      }
  }
  ```

  **Solution 1: Return List:**

  ```java
  @Repository
  public interface UserRepository extends JpaRepository<User, Long> {
      
      List<User> findByUsername(String username);
  }
  ```

  **Solution 2: Return Optional:**

  ```java
  @Repository
  public interface UserRepository extends JpaRepository<User, Long> {
      
      Optional<User> findByUsername(String username);
  }
  ```

  **Solution 3: Use First or Top:**

  ```java
  @Repository
  public interface UserRepository extends JpaRepository<User, Long> {
      
      User findFirstByUsername(String username);
      
      User findTopByUsername(String username);
  }
  ```

  **Solution 4: Add unique constraint:**

  ```java
  @Entity
  @Table(uniqueConstraints = @UniqueConstraint(columnNames = "username"))
  public class User {
      @Id
      private Long id;
      
      @Column(unique = true)
      private String username;
  }
  ```

</details>

<details>
  <summary>ConstraintViolationException</summary>
  <br/>

  **Error occurs when violating database constraints.**

  **Error message:**

  ```
  org.hibernate.exception.ConstraintViolationException: 
  could not execute statement
  ```

  **Common causes:**

  ```java
  // 1. Unique constraint violation
  @Entity
  public class User {
      @Column(unique = true)
      private String email;
  }
  
  // Trying to insert duplicate email
  User user1 = new User("john@example.com");
  User user2 = new User("john@example.com");  // Error!
  
  // 2. Not null constraint violation
  @Entity
  public class User {
      @Column(nullable = false)
      private String username;
  }
  
  User user = new User();
  user.setUsername(null);  // Error!
  
  // 3. Foreign key constraint violation
  @Entity
  public class Order {
      @ManyToOne
      @JoinColumn(name = "user_id", nullable = false)
      private User user;
  }
  
  Order order = new Order();
  order.setUser(null);  // Error!
  ```

  **Solution: Handle gracefully:**

  ```java
  @Service
  public class UserService {
      
      @Transactional
      public void createUser(String email) {
          try {
              User user = new User(email);
              userRepository.save(user);
          } catch (DataIntegrityViolationException e) {
              if (e.getCause() instanceof ConstraintViolationException) {
                  throw new DuplicateEmailException("Email already exists");
              }
              throw e;
          }
      }
  }
  ```

</details>

<details>
  <summary>StaleStateException / OptimisticLockException</summary>
  <br/>

  **Error occurs when updating entity that was modified by another transaction.**

  **Error message:**

  ```
  org.hibernate.StaleStateException: 
  Row was updated or deleted by another transaction
  ```

  **Problem:**

  ```java
  @Entity
  public class Product {
      @Id
      private Long id;
      
      @Version
      private Long version;
      
      private Integer stock;
  }
  
  // Transaction 1
  @Transactional
  public void updateStock1() {
      Product product = productRepository.findById(1L).orElseThrow();
      product.setStock(10);
      productRepository.save(product);  // version = 1
  }
  
  // Transaction 2 (concurrent)
  @Transactional
  public void updateStock2() {
      Product product = productRepository.findById(1L).orElseThrow();
      product.setStock(20);
      productRepository.save(product);  // Error! version mismatch
  }
  ```

  **Solution: Retry logic:**

  ```java
  @Service
  public class ProductService {
      
      @Transactional
      public void updateStockWithRetry(Long id, Integer stock) {
          int maxRetries = 3;
          int attempt = 0;
          
          while (attempt < maxRetries) {
              try {
                  Product product = productRepository.findById(id).orElseThrow();
                  product.setStock(stock);
                  productRepository.save(product);
                  return;
              } catch (OptimisticLockException e) {
                  attempt++;
                  if (attempt >= maxRetries) {
                      throw new RuntimeException("Failed after " + maxRetries + " attempts");
                  }
              }
          }
      }
  }
  ```

</details>

<details>
  <summary>Best Practices</summary>
  <br/>

  ```java
  // ✅ DO: Load lazy collections within transaction
  @Transactional
  public User getUser(Long id) {
      User user = userRepository.findById(id).orElseThrow();
      user.getOrders().size();  // Force load
      return user;
  }
  
  // ✅ DO: Use fetch join to avoid LazyInitializationException
  @Query("SELECT u FROM User u LEFT JOIN FETCH u.orders WHERE u.id = :id")
  Optional<User> findByIdWithOrders(@Param("id") Long id);
  
  // ✅ DO: Use Set for multiple fetch joins
  @OneToMany(mappedBy = "user")
  private Set<Order> orders;  // Set instead of List
  
  // ✅ DO: Use @MapsId for @OneToOne lazy loading
  @MapsId
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id")
  private User user;
  
  // ✅ DO: Return Optional for single result queries
  Optional<User> findByUsername(String username);
  
  // ✅ DO: Add unique constraints
  @Column(unique = true)
  private String email;
  
  // ✅ DO: Handle constraint violations
  try {
      userRepository.save(user);
  } catch (DataIntegrityViolationException e) {
      // Handle gracefully
  }
  
  // ✅ DO: Use @Version for optimistic locking
  @Version
  private Long version;
  
  // ✅ DO: Implement retry logic for optimistic lock failures
  @Retryable(value = OptimisticLockException.class, maxAttempts = 3)
  public void updateProduct(Long id) {
      // Update logic
  }
  
  // ❌ DON'T: Access lazy collections outside transaction
  public User getUser(Long id) {
      return userRepository.findById(id).orElseThrow();
  }
  // Later: user.getOrders() → LazyInitializationException!
  
  // ❌ DON'T: Fetch multiple Lists with fetch join
  @Query("SELECT u FROM User u " +
         "LEFT JOIN FETCH u.orders " +
         "LEFT JOIN FETCH u.addresses")  // MultipleBagFetchException!
  List<User> findAll();
  
  // ❌ DON'T: Expect @OneToOne lazy loading on non-owning side
  @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
  private UserProfile profile;  // Doesn't work!
  
  // ❌ DON'T: Set ID manually when persisting
  User user = new User();
  user.setId(1L);  // Don't do this
  userRepository.save(user);  // Error!
  
  // ❌ DON'T: Return single entity when multiple may exist
  User findByUsername(String username);  // NonUniqueResultException!
  ```
**Key Points**
  <br/>

  **LazyInitializationException:**
  + Accessing lazy collection outside transaction
  + Solution: Fetch join, entity graph, force initialization, DTO

  **MultipleBagFetchException:**
  + Fetching multiple Lists with fetch join
  + Solution: Use Set, multiple queries, @OrderColumn, batch fetching

  **@OneToOne Lazy Problem:**
  + Lazy loading doesn't work on non-owning side
  + Solution: @MapsId, bytecode enhancement, change to @ManyToOne

  **Detached Entity:**
  + Persisting entity with existing ID
  + Solution: Use merge, don't set ID, check if exists

  **NonUniqueResultException:**
  + Query returns multiple results but expects single
  + Solution: Return List/Optional, use First/Top, add unique constraint

  **ConstraintViolationException:**
  + Violating database constraints
  + Solution: Handle gracefully, validate before save

  **OptimisticLockException:**
  + Concurrent modification with @Version
  + Solution: Retry logic, handle gracefully

  **Prevention:**
  + Load lazy collections within transaction
  + Use fetch join or entity graph
  + Use Set for multiple collections
  + Add proper constraints
  + Use @Version for concurrent updates
  + Return Optional for single results
  + Validate data before save

</details>

## Caching

<details>
  <summary>What is Caching?</summary>
  <br/>

  Caching stores frequently accessed data in memory to improve performance.

  **Benefits:**
  + Reduces database queries
  + Improves response time
  + Reduces database load
  + Better scalability

  **Hibernate cache levels:**
  + **First-level cache** (Session cache) - Always enabled
  + **Second-level cache** (SessionFactory cache) - Optional
  + **Query cache** - Caches query results

  **Cache hierarchy:**

  ```
  Application
      ↓
  First-level cache (Session)
      ↓
  Second-level cache (SessionFactory)
      ↓
  Database
  ```

</details>

<details>
  <summary>First-Level Cache (Session Cache)</summary>
  <br/>

  **First-level cache** is associated with Hibernate Session (EntityManager).

  **Characteristics:**
  + Always enabled (cannot be disabled)
  + Scoped to single transaction/session
  + Stores entities loaded in current session
  + Cleared when session closes

  **Example:**

  ```java
  @Service
  public class UserService {
      
      @PersistenceContext
      private EntityManager entityManager;
      
      @Transactional
      public void firstLevelCacheExample() {
          // First query - hits database
          User user1 = entityManager.find(User.class, 1L);
          // SQL: SELECT * FROM users WHERE id = 1
          
          // Second query - hits first-level cache (no SQL)
          User user2 = entityManager.find(User.class, 1L);
          
          // Same object reference
          System.out.println(user1 == user2);  // true
      }
  }
  ```

  **Cache operations:**

  ```java
  @Transactional
  public void cacheOperations() {
      User user = entityManager.find(User.class, 1L);
      
      // Check if entity is in cache
      boolean contains = entityManager.contains(user);
      
      // Remove entity from cache
      entityManager.detach(user);
      
      // Clear entire cache
      entityManager.clear();
      
      // Refresh entity from database
      entityManager.refresh(user);
  }
  ```

  **Benefits:**
  + Automatic
  + No configuration needed
  + Prevents duplicate queries in same transaction

  **Limitations:**
  + Only within single session
  + Not shared across transactions
  + Cleared when session closes

</details>

<details>
  <summary>Second-Level Cache (SessionFactory Cache)</summary>
  <br/>

  **Second-level cache** is shared across all sessions.

  **Characteristics:**
  + Optional (must be enabled)
  + Scoped to SessionFactory (application-wide)
  + Shared across all transactions
  + Survives session closure

  **Enable second-level cache:**

  ```properties
  # application.properties
  spring.jpa.properties.hibernate.cache.use_second_level_cache=true
  spring.jpa.properties.hibernate.cache.region.factory_class=org.hibernate.cache.jcache.JCacheRegionFactory
  spring.jpa.properties.javax.persistence.sharedCache.mode=ENABLE_SELECTIVE
  ```

  **Add dependency (Ehcache):**

  ```xml
  <!-- pom.xml -->
  <dependency>
      <groupId>org.hibernate</groupId>
      <artifactId>hibernate-jcache</artifactId>
  </dependency>
  <dependency>
      <groupId>org.ehcache</groupId>
      <artifactId>ehcache</artifactId>
  </dependency>
  ```

  **Mark entity as cacheable:**

  ```java
  @Entity
  @Cacheable
  @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
  public class User {
      @Id
      private Long id;
      private String username;
      
      @OneToMany(mappedBy = "user")
      @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
      private List<Order> orders;
  }
  ```

  **Usage:**

  ```java
  @Service
  public class UserService {
      
      @Transactional
      public void secondLevelCacheExample() {
          // First transaction
          User user1 = entityManager.find(User.class, 1L);
          // SQL: SELECT * FROM users WHERE id = 1
      }
      
      @Transactional
      public void anotherTransaction() {
          // Second transaction (different session)
          User user2 = entityManager.find(User.class, 1L);
          // No SQL - loaded from second-level cache!
      }
  }
  ```

</details>

<details>
  <summary>Cache Concurrency Strategies</summary>
  <br/>

  **Cache concurrency strategies** control how cache handles concurrent access.

  **READ_ONLY:**
  + For entities that never change
  + Best performance
  + Throws exception on update

  ```java
  @Entity
  @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
  public class Country {
      @Id
      private Long id;
      private String name;
  }
  ```

  **NONSTRICT_READ_WRITE:**
  + For entities that rarely change
  + No locking
  + May return stale data briefly

  ```java
  @Entity
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  public class Category {
      @Id
      private Long id;
      private String name;
  }
  ```

  **READ_WRITE:**
  + For entities that change frequently
  + Uses soft locks
  + Prevents stale data

  ```java
  @Entity
  @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
  public class User {
      @Id
      private Long id;
      private String username;
  }
  ```

  **TRANSACTIONAL:**
  + Full transactional cache
  + Requires JTA
  + Best consistency

  ```java
  @Entity
  @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
  public class Account {
      @Id
      private Long id;
      private BigDecimal balance;
  }
  ```

</details>

<details>
  <summary>Ehcache Configuration</summary>
  <br/>

  **Ehcache** is a popular second-level cache provider.

  **Create ehcache.xml:**

  ```xml
  <!-- src/main/resources/ehcache.xml -->
  <config xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xmlns="http://www.ehcache.org/v3"
          xsi:schemaLocation="http://www.ehcache.org/v3 
                              http://www.ehcache.org/schema/ehcache-core-3.0.xsd">
      
      <!-- Default cache configuration -->
      <cache-template name="default">
          <expiry>
              <ttl unit="minutes">10</ttl>
          </expiry>
          <heap unit="entries">1000</heap>
      </cache-template>
      
      <!-- Entity cache -->
      <cache alias="com.example.User" uses-template="default">
          <expiry>
              <ttl unit="minutes">30</ttl>
          </expiry>
          <heap unit="entries">500</heap>
      </cache>
      
      <!-- Collection cache -->
      <cache alias="com.example.User.orders" uses-template="default">
          <expiry>
              <ttl unit="minutes">15</ttl>
          </expiry>
          <heap unit="entries">1000</heap>
      </cache>
      
      <!-- Query cache -->
      <cache alias="default-query-results-region" uses-template="default"/>
      <cache alias="default-update-timestamps-region" uses-template="default"/>
  </config>
  ```

  **Configure in application.properties:**

  ```properties
  spring.jpa.properties.hibernate.cache.use_second_level_cache=true
  spring.jpa.properties.hibernate.cache.region.factory_class=org.hibernate.cache.jcache.JCacheRegionFactory
  spring.jpa.properties.hibernate.javax.cache.provider=org.ehcache.jsr107.EhcacheCachingProvider
  spring.jpa.properties.hibernate.javax.cache.uri=classpath:ehcache.xml
  ```

</details>

<details>
  <summary>Redis as Second-Level Cache</summary>
  <br/>

  **Redis** can be used as distributed second-level cache.

  **Add dependencies:**

  ```xml
  <!-- pom.xml -->
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-redis</artifactId>
  </dependency>
  <dependency>
      <groupId>org.hibernate</groupId>
      <artifactId>hibernate-jcache</artifactId>
  </dependency>
  <dependency>
      <groupId>com.github.ben-manes.caffeine</groupId>
      <artifactId>caffeine</artifactId>
  </dependency>
  ```

  **Configure Redis:**

  ```properties
  # application.properties
  spring.redis.host=localhost
  spring.redis.port=6379
  
  spring.jpa.properties.hibernate.cache.use_second_level_cache=true
  spring.jpa.properties.hibernate.cache.region.factory_class=org.hibernate.cache.jcache.JCacheRegionFactory
  ```

  **Custom Redis cache configuration:**

  ```java
  @Configuration
  @EnableCaching
  public class CacheConfig {
      
      @Bean
      public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
          RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
              .entryTtl(Duration.ofMinutes(10))
              .serializeKeysWith(RedisSerializationContext.SerializationPair
                  .fromSerializer(new StringRedisSerializer()))
              .serializeValuesWith(RedisSerializationContext.SerializationPair
                  .fromSerializer(new GenericJackson2JsonRedisSerializer()));
          
          return RedisCacheManager.builder(connectionFactory)
              .cacheDefaults(config)
              .build();
      }
  }
  ```

</details>

<details>
  <summary>Query Cache</summary>
  <br/>

  **Query cache** caches query results.

  **Enable query cache:**

  ```properties
  # application.properties
  spring.jpa.properties.hibernate.cache.use_query_cache=true
  ```

  **Mark query as cacheable:**

  ```java
  @Repository
  public interface UserRepository extends JpaRepository<User, Long> {
      
      @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
      @Query("SELECT u FROM User u WHERE u.username = :username")
      Optional<User> findByUsername(@Param("username") String username);
  }
  ```

  **Using EntityManager:**

  ```java
  @Service
  public class UserService {
      
      @PersistenceContext
      private EntityManager entityManager;
      
      @Transactional
      public List<User> findActiveUsers() {
          return entityManager
              .createQuery("SELECT u FROM User u WHERE u.active = true", User.class)
              .setHint("org.hibernate.cacheable", true)
              .getResultList();
      }
  }
  ```

  **Important notes:**
  + Query cache stores query results (IDs)
  + Still needs second-level cache for entities
  + Invalidated when table is updated
  + Use for frequently executed queries

</details>

<details>
  <summary>Cache Eviction</summary>
  <br/>

  **Cache eviction** removes stale data from cache.

  **Manual eviction:**

  ```java
  @Service
  public class UserService {
      
      @PersistenceContext
      private EntityManager entityManager;
      
      @Transactional
      public void evictCache() {
          Cache cache = entityManager.getEntityManagerFactory().getCache();
          
          // Evict specific entity
          cache.evict(User.class, 1L);
          
          // Evict all instances of entity
          cache.evict(User.class);
          
          // Evict entire cache
          cache.evictAll();
      }
  }
  ```

  **Automatic eviction on update:**

  ```java
  @Service
  public class UserService {
      
      @Transactional
      public void updateUser(Long id, String username) {
          User user = userRepository.findById(id).orElseThrow();
          user.setUsername(username);
          userRepository.save(user);
          // Cache automatically updated
      }
  }
  ```

  **Spring Cache eviction:**

  ```java
  @Service
  public class UserService {
      
      @CacheEvict(value = "users", key = "#id")
      public void deleteUser(Long id) {
          userRepository.deleteById(id);
      }
      
      @CacheEvict(value = "users", allEntries = true)
      public void deleteAllUsers() {
          userRepository.deleteAll();
      }
  }
  ```

</details>

<details>
  <summary>Spring Cache Annotations</summary>
  <br/>

  **Spring provides cache annotations** for method-level caching.

  **Enable caching:**

  ```java
  @Configuration
  @EnableCaching
  public class CacheConfig {
  }
  ```

  **@Cacheable - Cache method result:**

  ```java
  @Service
  public class UserService {
      
      @Cacheable(value = "users", key = "#id")
      public User findById(Long id) {
          return userRepository.findById(id).orElseThrow();
      }
      
      @Cacheable(value = "users", key = "#username")
      public User findByUsername(String username) {
          return userRepository.findByUsername(username).orElseThrow();
      }
  }
  ```

  **@CachePut - Update cache:**

  ```java
  @Service
  public class UserService {
      
      @CachePut(value = "users", key = "#user.id")
      public User updateUser(User user) {
          return userRepository.save(user);
      }
  }
  ```

  **@CacheEvict - Remove from cache:**

  ```java
  @Service
  public class UserService {
      
      @CacheEvict(value = "users", key = "#id")
      public void deleteUser(Long id) {
          userRepository.deleteById(id);
      }
      
      @CacheEvict(value = "users", allEntries = true)
      public void deleteAll() {
          userRepository.deleteAll();
      }
  }
  ```

  **@Caching - Multiple cache operations:**

  ```java
  @Service
  public class UserService {
      
      @Caching(
          evict = {
              @CacheEvict(value = "users", key = "#user.id"),
              @CacheEvict(value = "usernames", key = "#user.username")
          }
      )
      public void deleteUser(User user) {
          userRepository.delete(user);
      }
  }
  ```

</details>

<details>
  <summary>Cache Statistics</summary>
  <br/>

  **Monitor cache performance** with statistics.

  **Enable statistics:**

  ```properties
  # application.properties
  spring.jpa.properties.hibernate.generate_statistics=true
  spring.jpa.properties.hibernate.cache.use_structured_entries=true
  ```

  **Get statistics:**

  ```java
  @Service
  public class CacheStatsService {
      
      @PersistenceContext
      private EntityManager entityManager;
      
      public void printCacheStats() {
          Session session = entityManager.unwrap(Session.class);
          Statistics stats = session.getSessionFactory().getStatistics();
          
          System.out.println("Second-level cache hits: " + stats.getSecondLevelCacheHitCount());
          System.out.println("Second-level cache misses: " + stats.getSecondLevelCacheMissCount());
          System.out.println("Second-level cache puts: " + stats.getSecondLevelCachePutCount());
          
          System.out.println("Query cache hits: " + stats.getQueryCacheHitCount());
          System.out.println("Query cache misses: " + stats.getQueryCacheMissCount());
          
          // Clear statistics
          stats.clear();
      }
  }
  ```

</details>

<details>
  <summary>Best Practices</summary>
  <br/>

  ```java
  // ✅ DO: Cache read-only entities
  @Entity
  @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
  public class Country {
      @Id
      private Long id;
      private String name;
  }
  
  // ✅ DO: Cache frequently accessed entities
  @Entity
  @Cacheable
  @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
  public class User {
      @Id
      private Long id;
      private String username;
  }
  
  // ✅ DO: Cache collections
  @OneToMany(mappedBy = "user")
  @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
  private List<Order> orders;
  
  // ✅ DO: Use appropriate concurrency strategy
  @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)  // Never changes
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)  // Rarely changes
  @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)  // Frequently changes
  
  // ✅ DO: Set cache expiration
  <cache alias="com.example.User">
      <expiry>
          <ttl unit="minutes">30</ttl>
      </expiry>
  </cache>
  
  // ✅ DO: Monitor cache statistics
  spring.jpa.properties.hibernate.generate_statistics=true
  
  // ✅ DO: Use query cache for frequent queries
  @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
  List<User> findActiveUsers();
  
  // ✅ DO: Evict cache on updates
  @CacheEvict(value = "users", key = "#id")
  public void deleteUser(Long id) {
      userRepository.deleteById(id);
  }
  
  // ❌ DON'T: Cache entities that change frequently
  @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
  public class AuditLog {  // Bad - changes constantly
      // ...
  }
  
  // ❌ DON'T: Cache large collections
  @OneToMany(mappedBy = "user")
  @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
  private List<Order> orders;  // Bad if user has thousands of orders
  
  // ❌ DON'T: Use READ_ONLY for mutable entities
  @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
  public class User {  // Bad - users can be updated
      // ...
  }
  
  // ❌ DON'T: Forget to configure cache size
  <cache alias="com.example.User">
      <heap unit="entries">1000</heap>  // Set limit
  </cache>
  ```

  **Key Points**
  <br/>

  **First-Level Cache:**
  + Always enabled
  + Session-scoped
  + Automatic
  + Cleared when session closes

  **Second-Level Cache:**
  + Optional (must be enabled)
  + SessionFactory-scoped
  + Shared across sessions
  + Requires cache provider (Ehcache, Redis)

  **Cache Strategies:**
  + **READ_ONLY** - Never changes
  + **NONSTRICT_READ_WRITE** - Rarely changes
  + **READ_WRITE** - Frequently changes
  + **TRANSACTIONAL** - Full transactional support

  **Query Cache:**
  + Caches query results
  + Requires second-level cache
  + Use for frequent queries
  + Invalidated on table updates

  **Cache Providers:**
  + **Ehcache** - Local cache
  + **Redis** - Distributed cache
  + **Caffeine** - High-performance local cache

  **Best Practices:**
  + Cache read-only or rarely changing entities
  + Use appropriate concurrency strategy
  + Set cache expiration
  + Monitor cache statistics
  + Evict cache on updates
  + Don't cache frequently changing data
  + Don't cache large collections
  + Configure cache size limits


</details>


## Pagination and Sorting

<details>
  <summary>What is Pagination?</summary>
  <br/>

  Pagination divides large result sets into smaller pages for better performance and user experience.

  **Why use pagination?**
  + Reduces memory usage
  + Improves query performance
  + Better user experience
  + Prevents loading unnecessary data

  **Problem without pagination:**

  ```java
  // ❌ BAD: Loading all records
  @GetMapping("/users")
  public List<User> getAllUsers() {
      return userRepository.findAll();
      // Loads 1 million users into memory!
  }
  ```

  **Solution with pagination:**

  ```java
  // ✅ GOOD: Loading page by page
  @GetMapping("/users")
  public Page<User> getUsers(@RequestParam int page, @RequestParam int size) {
      Pageable pageable = PageRequest.of(page, size);
      return userRepository.findAll(pageable);
      // Loads only 20 users per page
  }
  ```

</details>

<details>
  <summary>Pageable Interface</summary>
  <br/>

  **Pageable** is an interface for pagination information.

  **Create Pageable:**

  ```java
  // Page 0, size 20
  Pageable pageable = PageRequest.of(0, 20);
  
  // Page 1, size 10
  Pageable pageable = PageRequest.of(1, 10);
  
  // With sorting
  Pageable pageable = PageRequest.of(0, 20, Sort.by("username"));
  
  // With multiple sort fields
  Pageable pageable = PageRequest.of(0, 20, 
      Sort.by("lastName").ascending()
          .and(Sort.by("firstName").ascending())
  );
  
  // Descending order
  Pageable pageable = PageRequest.of(0, 20, Sort.by("createdAt").descending());
  ```

  **Use in repository:**

  ```java
  @Repository
  public interface UserRepository extends JpaRepository<User, Long> {
      
      // Spring Data automatically supports Pageable
      Page<User> findAll(Pageable pageable);
      
      // Custom query with pagination
      Page<User> findByLastName(String lastName, Pageable pageable);
      
      // With @Query
      @Query("SELECT u FROM User u WHERE u.active = true")
      Page<User> findActiveUsers(Pageable pageable);
  }
  ```

  **Controller example:**

  ```java
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
      
      @Autowired
      private UserRepository userRepository;
      
      @GetMapping
      public Page<User> getUsers(
          @RequestParam(defaultValue = "0") int page,
          @RequestParam(defaultValue = "20") int size,
          @RequestParam(defaultValue = "id") String sortBy,
          @RequestParam(defaultValue = "asc") String direction
      ) {
          Sort sort = direction.equalsIgnoreCase("asc") 
              ? Sort.by(sortBy).ascending() 
              : Sort.by(sortBy).descending();
          
          Pageable pageable = PageRequest.of(page, size, sort);
          return userRepository.findAll(pageable);
      }
  }
  ```

</details>

<details>
  <summary>Page Interface</summary>
  <br/>

  **Page** contains paginated results with metadata.

  **Page methods:**

  ```java
  @Service
  public class UserService {
      
      @Autowired
      private UserRepository userRepository;
      
      public void pageExample() {
          Pageable pageable = PageRequest.of(0, 20);
          Page<User> page = userRepository.findAll(pageable);
          
          // Content
          List<User> users = page.getContent();
          
          // Page information
          int currentPage = page.getNumber();           // Current page number (0-indexed)
          int pageSize = page.getSize();                // Page size
          long totalElements = page.getTotalElements(); // Total number of elements
          int totalPages = page.getTotalPages();        // Total number of pages
          
          // Navigation
          boolean hasNext = page.hasNext();             // Has next page?
          boolean hasPrevious = page.hasPrevious();     // Has previous page?
          boolean isFirst = page.isFirst();             // Is first page?
          boolean isLast = page.isLast();               // Is last page?
          
          // Get next/previous page
          Pageable nextPageable = page.nextPageable();
          Pageable previousPageable = page.previousPageable();
          
          // Sorting information
          Sort sort = page.getSort();
          
          // Number of elements in current page
          int numberOfElements = page.getNumberOfElements();
          
          // Is empty?
          boolean isEmpty = page.isEmpty();
      }
  }
  ```

  **Page response example:**

  ```json
  {
    "content": [
      {"id": 1, "username": "john"},
      {"id": 2, "username": "alice"}
    ],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 20,
      "sort": {
        "sorted": true,
        "unsorted": false
      }
    },
    "totalElements": 100,
    "totalPages": 5,
    "last": false,
    "first": true,
    "numberOfElements": 20,
    "size": 20,
    "number": 0,
    "sort": {
      "sorted": true,
      "unsorted": false
    },
    "empty": false
  }
  ```

</details>

<details>
  <summary>Slice Interface</summary>
  <br/>

  **Slice** is a lightweight alternative to Page (no total count).

  **Slice vs Page:**

  ```java
  @Repository
  public interface UserRepository extends JpaRepository<User, Long> {
      
      // Returns Page (with total count)
      Page<User> findAll(Pageable pageable);
      // SQL: SELECT * FROM users LIMIT 20 OFFSET 0
      // SQL: SELECT COUNT(*) FROM users  (additional query)
      
      // Returns Slice (without total count)
      Slice<User> findByActive(boolean active, Pageable pageable);
      // SQL: SELECT * FROM users WHERE active = true LIMIT 21 OFFSET 0
      // No count query!
  }
  ```

  **Slice methods:**

  ```java
  @Service
  public class UserService {
      
      @Autowired
      private UserRepository userRepository;
      
      public void sliceExample() {
          Pageable pageable = PageRequest.of(0, 20);
          Slice<User> slice = userRepository.findByActive(true, pageable);
          
          // Content
          List<User> users = slice.getContent();
          
          // Page information
          int currentPage = slice.getNumber();
          int pageSize = slice.getSize();
          
          // Navigation
          boolean hasNext = slice.hasNext();           // Has next page?
          boolean hasPrevious = slice.hasPrevious();   // Has previous page?
          boolean isFirst = slice.isFirst();           // Is first page?
          boolean isLast = slice.isLast();             // Is last page?
          
          // Get next/previous page
          Pageable nextPageable = slice.nextPageable();
          Pageable previousPageable = slice.previousPageable();
          
          // Number of elements in current page
          int numberOfElements = slice.getNumberOfElements();
          
          // ❌ NOT AVAILABLE in Slice:
          // long totalElements = slice.getTotalElements();  // Compile error!
          // int totalPages = slice.getTotalPages();         // Compile error!
      }
  }
  ```

  **When to use Slice:**
  + Infinite scrolling (social media feeds)
  + Large datasets where count is expensive
  + "Load more" button instead of page numbers
  + Performance is critical

  **When to use Page:**
  + Need total count for UI
  + Page numbers navigation
  + "Showing X of Y results"
  + Smaller datasets

</details>

<details>
  <summary>Sorting</summary>
  <br/>

  **Sort** defines ordering for query results.

  **Create Sort:**

  ```java
  // Single field ascending
  Sort sort = Sort.by("username");
  Sort sort = Sort.by("username").ascending();
  
  // Single field descending
  Sort sort = Sort.by("username").descending();
  
  // Multiple fields
  Sort sort = Sort.by("lastName", "firstName");
  
  // Multiple fields with different directions
  Sort sort = Sort.by(
      Sort.Order.asc("lastName"),
      Sort.Order.desc("createdAt")
  );
  
  // Chaining
  Sort sort = Sort.by("lastName").ascending()
      .and(Sort.by("firstName").ascending());
  
  // Case-insensitive sorting
  Sort sort = Sort.by(Sort.Order.asc("username").ignoreCase());
  
  // Null handling
  Sort sort = Sort.by(
      Sort.Order.asc("email").nullsFirst()
  );
  Sort sort = Sort.by(
      Sort.Order.asc("email").nullsLast()
  );
  ```

  **Use in repository:**

  ```java
  @Repository
  public interface UserRepository extends JpaRepository<User, Long> {
      
      // With Sort parameter
      List<User> findAll(Sort sort);
      
      List<User> findByLastName(String lastName, Sort sort);
      
      // With Pageable (includes sorting)
      Page<User> findAll(Pageable pageable);
  }
  ```

  **Service example:**

  ```java
  @Service
  public class UserService {
      
      @Autowired
      private UserRepository userRepository;
      
      public List<User> getUsersSorted(String sortBy, String direction) {
          Sort sort = direction.equalsIgnoreCase("asc")
              ? Sort.by(sortBy).ascending()
              : Sort.by(sortBy).descending();
          
          return userRepository.findAll(sort);
      }
      
      public Page<User> getUsersPaginated(int page, int size, String sortBy) {
          Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
          return userRepository.findAll(pageable);
      }
  }
  ```

</details>

<details>
  <summary>Dynamic Sorting</summary>
  <br/>

  **Allow users to sort by any field:**

  ```java
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
      
      @Autowired
      private UserRepository userRepository;
      
      @GetMapping
      public Page<User> getUsers(
          @RequestParam(defaultValue = "0") int page,
          @RequestParam(defaultValue = "20") int size,
          @RequestParam(required = false) String[] sort
      ) {
          // sort parameter: ["lastName,asc", "firstName,desc"]
          
          List<Sort.Order> orders = new ArrayList<>();
          
          if (sort != null) {
              for (String sortOrder : sort) {
                  String[] parts = sortOrder.split(",");
                  String property = parts[0];
                  String direction = parts.length > 1 ? parts[1] : "asc";
                  
                  orders.add(direction.equalsIgnoreCase("desc")
                      ? Sort.Order.desc(property)
                      : Sort.Order.asc(property));
              }
          }
          
          Pageable pageable = orders.isEmpty()
              ? PageRequest.of(page, size)
              : PageRequest.of(page, size, Sort.by(orders));
          
          return userRepository.findAll(pageable);
      }
  }
  
  // Usage:
  // GET /api/users?page=0&size=20&sort=lastName,asc&sort=firstName,desc
  ```

  **Using Spring's built-in support:**

  ```java
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
      
      @Autowired
      private UserRepository userRepository;
      
      @GetMapping
      public Page<User> getUsers(Pageable pageable) {
          // Spring automatically creates Pageable from request parameters
          return userRepository.findAll(pageable);
      }
  }
  
  // Usage:
  // GET /api/users?page=0&size=20&sort=lastName,asc&sort=firstName,desc
  ```

</details>

<details>
  <summary>Custom Pagination</summary>
  <br/>

  **Create custom Page:**

  ```java
  @Service
  public class UserService {
      
      @Autowired
      private UserRepository userRepository;
      
      public Page<UserDTO> getUserDTOs(Pageable pageable) {
          Page<User> userPage = userRepository.findAll(pageable);
          
          // Convert to DTO
          List<UserDTO> dtos = userPage.getContent().stream()
              .map(user -> new UserDTO(user.getId(), user.getUsername()))
              .collect(Collectors.toList());
          
          // Create new Page with DTOs
          return new PageImpl<>(dtos, pageable, userPage.getTotalElements());
      }
  }
  ```

  **Manual pagination:**

  ```java
  @Service
  public class UserService {
      
      @PersistenceContext
      private EntityManager entityManager;
      
      public Page<User> findUsers(String searchTerm, Pageable pageable) {
          // Data query
          String dataQuery = "SELECT u FROM User u WHERE u.username LIKE :search";
          List<User> users = entityManager.createQuery(dataQuery, User.class)
              .setParameter("search", "%" + searchTerm + "%")
              .setFirstResult((int) pageable.getOffset())
              .setMaxResults(pageable.getPageSize())
              .getResultList();
          
          // Count query
          String countQuery = "SELECT COUNT(u) FROM User u WHERE u.username LIKE :search";
          Long total = entityManager.createQuery(countQuery, Long.class)
              .setParameter("search", "%" + searchTerm + "%")
              .getSingleResult();
          
          return new PageImpl<>(users, pageable, total);
      }
  }
  ```

</details>

<details>
  <summary>Pagination with Fetch Join</summary>
  <br/>

  **Problem: Fetch join with pagination:**

  ```java
  @Repository
  public interface UserRepository extends JpaRepository<User, Long> {
      
      // ⚠️ WARNING: In-memory pagination!
      @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.orders")
      Page<User> findAllWithOrders(Pageable pageable);
      
      // Hibernate warning:
      // HHH000104: firstResult/maxResults specified with collection fetch; 
      // applying in memory!
  }
  ```

  **Why it's a problem:**
  + Hibernate loads ALL results into memory
  + Then applies pagination in memory
  + Defeats the purpose of pagination
  + Can cause OutOfMemoryError

  **Solution 1: Use two queries:**

  ```java
  @Service
  public class UserService {
      
      @Autowired
      private UserRepository userRepository;
      
      @Transactional
      public Page<User> findUsersWithOrders(Pageable pageable) {
          // Query 1: Get paginated user IDs
          Page<User> userPage = userRepository.findAll(pageable);
          List<Long> userIds = userPage.getContent().stream()
              .map(User::getId)
              .collect(Collectors.toList());
          
          // Query 2: Fetch users with orders
          List<User> usersWithOrders = userRepository.findByIdInWithOrders(userIds);
          
          return new PageImpl<>(usersWithOrders, pageable, userPage.getTotalElements());
      }
  }
  
  @Repository
  public interface UserRepository extends JpaRepository<User, Long> {
      
      @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.orders WHERE u.id IN :ids")
      List<User> findByIdInWithOrders(@Param("ids") List<Long> ids);
  }
  ```

  **Solution 2: Use @BatchSize:**

  ```java
  @Entity
  public class User {
      @Id
      private Long id;
      
      @OneToMany(mappedBy = "user")
      @BatchSize(size = 10)
      private List<Order> orders;
  }
  
  @Repository
  public interface UserRepository extends JpaRepository<User, Long> {
      
      // Works correctly with pagination
      Page<User> findAll(Pageable pageable);
  }
  ```

  **Solution 3: Use Slice instead of Page:**

  ```java
  @Repository
  public interface UserRepository extends JpaRepository<User, Long> {
      
      @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.orders")
      Slice<User> findAllWithOrders(Pageable pageable);
      // Still has in-memory pagination issue, but no count query
  }
  ```

</details>

<details>
  <summary>Pagination Performance</summary>
  <br/>

  **Offset pagination performance:**

  ```java
  // Page 0: Fast
  Pageable pageable = PageRequest.of(0, 20);
  // SQL: SELECT * FROM users LIMIT 20 OFFSET 0
  
  // Page 1000: Slow!
  Pageable pageable = PageRequest.of(1000, 20);
  // SQL: SELECT * FROM users LIMIT 20 OFFSET 20000
  // Database must scan 20000 rows to skip them
  ```

  **Problem with large offsets:**
  + Database scans all skipped rows
  + Performance degrades with page number
  + Not suitable for large datasets

  **Solution: Cursor-based pagination:**

  ```java
  @Repository
  public interface UserRepository extends JpaRepository<User, Long> {
      
      // Cursor-based pagination
      @Query("SELECT u FROM User u WHERE u.id > :lastId ORDER BY u.id")
      List<User> findUsersAfter(@Param("lastId") Long lastId, Pageable pageable);
  }
  
  @Service
  public class UserService {
      
      @Autowired
      private UserRepository userRepository;
      
      public List<User> getNextPage(Long lastId, int size) {
          Pageable pageable = PageRequest.of(0, size);
          return userRepository.findUsersAfter(lastId, pageable);
      }
  }
  
  // Usage:
  // First page: getNextPage(0, 20)
  // Next page: getNextPage(lastUser.getId(), 20)
  ```

  **Cursor-based advantages:**
  + Consistent performance
  + No offset scanning
  + Suitable for infinite scrolling

  **Cursor-based disadvantages:**
  + Cannot jump to specific page
  + Cannot go backwards easily
  + Requires stable sort order

</details>

<details>
  <summary>Pagination Best Practices</summary>
  <br/>

  ```java
  // ✅ DO: Use Pageable for large datasets
  @GetMapping("/users")
  public Page<User> getUsers(Pageable pageable) {
      return userRepository.findAll(pageable);
  }
  
  // ✅ DO: Set default page size
  @GetMapping("/users")
  public Page<User> getUsers(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size
  ) {
      Pageable pageable = PageRequest.of(page, size);
      return userRepository.findAll(pageable);
  }
  
  // ✅ DO: Limit maximum page size
  @GetMapping("/users")
  public Page<User> getUsers(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size
  ) {
      int maxSize = 100;
      int actualSize = Math.min(size, maxSize);
      Pageable pageable = PageRequest.of(page, actualSize);
      return userRepository.findAll(pageable);
  }
  
  // ✅ DO: Use Slice for infinite scrolling
  @GetMapping("/feed")
  public Slice<Post> getFeed(Pageable pageable) {
      return postRepository.findByOrderByCreatedAtDesc(pageable);
  }
  
  // ✅ DO: Use Page when you need total count
  @GetMapping("/users")
  public Page<User> getUsers(Pageable pageable) {
      return userRepository.findAll(pageable);
      // UI shows: "Page 1 of 10"
  }
  
  // ✅ DO: Add indexes for sorted columns
  @Entity
  @Table(indexes = {
      @Index(name = "idx_username", columnList = "username"),
      @Index(name = "idx_created_at", columnList = "created_at")
  })
  public class User {
      private String username;
      private LocalDateTime createdAt;
  }
  
  // ✅ DO: Use cursor-based pagination for large datasets
  @Query("SELECT u FROM User u WHERE u.id > :lastId ORDER BY u.id")
  List<User> findUsersAfter(@Param("lastId") Long lastId, Pageable pageable);
  
  // ❌ DON'T: Use fetch join with Page
  @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.orders")
  Page<User> findAllWithOrders(Pageable pageable);  // In-memory pagination!
  
  // ✅ DO: Use @BatchSize instead
  @Entity
  public class User {
      @OneToMany(mappedBy = "user")
      @BatchSize(size = 10)
      private List<Order> orders;
  }
  
  // ❌ DON'T: Load all data without pagination
  @GetMapping("/users")
  public List<User> getAllUsers() {
      return userRepository.findAll();  // Loads everything!
  }
  
  // ✅ DO: Validate page parameters
  @GetMapping("/users")
  public Page<User> getUsers(
      @RequestParam(defaultValue = "0") @Min(0) int page,
      @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size
  ) {
      Pageable pageable = PageRequest.of(page, size);
      return userRepository.findAll(pageable);
  }
  
  // ✅ DO: Use DTO projection with pagination
  @Query("SELECT new com.example.dto.UserDTO(u.id, u.username) FROM User u")
  Page<UserDTO> findAllUserDTOs(Pageable pageable);
  
  // ✅ DO: Handle empty pages gracefully
  @GetMapping("/users")
  public ResponseEntity<Page<User>> getUsers(Pageable pageable) {
      Page<User> page = userRepository.findAll(pageable);
      
      if (page.isEmpty()) {
          return ResponseEntity.noContent().build();
      }
      
      return ResponseEntity.ok(page);
  }
  ```

  **Summary:**
  + Use **Pageable** for pagination and sorting
  + Use **Page** when you need total count (page numbers navigation)
  + Use **Slice** for infinite scrolling (better performance)
  + Use **Sort** for ordering results
  + Set **default and maximum page size**
  + Add **indexes** for sorted columns
  + Use **cursor-based pagination** for large datasets
  + Don't use **fetch join with Page** (causes in-memory pagination)
  + Use **@BatchSize** instead of fetch join with pagination
  + Validate **page parameters**
  + Use **DTO projection** to reduce data transfer
  + Handle **empty pages** gracefully

</details>

## Batch Processing

<details>
  <summary>What is Batch Processing?</summary>
  <br/>

  Batch processing executes multiple database operations in a single batch for better performance.

  **Why use batch processing?**
  + Reduces database round trips
  + Improves performance significantly
  + Reduces network overhead
  + Better resource utilization

  **Problem without batching:**

  ```java
  // ❌ BAD: Individual inserts
  @Transactional
  public void saveUsers(List<User> users) {
      for (User user : users) {
          userRepository.save(user);
          // SQL: INSERT INTO users ... (1000 times for 1000 users)
      }
      // 1000 database round trips!
  }
  ```

  **Solution with batching:**

  ```java
  // ✅ GOOD: Batch inserts
  @Transactional
  public void saveUsersBatch(List<User> users) {
      for (int i = 0; i < users.size(); i++) {
          entityManager.persist(users.get(i));
          
          if (i % 50 == 0) {
              entityManager.flush();
              entityManager.clear();
          }
      }
      // SQL: INSERT INTO users ... (batched in groups of 50)
      // Only 20 database round trips for 1000 users!
  }
  ```

</details>

<details>
  <summary>Enable Batch Processing</summary>
  <br/>

  **Configure Hibernate batch size:**

  ```properties
  # application.properties
  
  # Enable batch inserts
  spring.jpa.properties.hibernate.jdbc.batch_size=50
  
  # Order inserts for better batching
  spring.jpa.properties.hibernate.order_inserts=true
  
  # Order updates for better batching
  spring.jpa.properties.hibernate.order_updates=true
  
  # Batch versioned entities
  spring.jpa.properties.hibernate.jdbc.batch_versioned_data=true
  
  # Show batch SQL (for debugging)
  spring.jpa.properties.hibernate.show_sql=true
  spring.jpa.properties.hibernate.format_sql=true
  logging.level.org.hibernate.SQL=DEBUG
  logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
  ```

  **Important notes:**
  + Batch size of 50-100 is usually optimal
  + Too large batch size can cause memory issues
  + Too small batch size reduces benefits
  + Test to find optimal size for your use case

</details>

<details>
  <summary>Batch Insert</summary>
  <br/>

  **Basic batch insert:**

  ```java
  @Service
  public class UserService {
      
      @PersistenceContext
      private EntityManager entityManager;
      
      @Transactional
      public void batchInsert(List<User> users) {
          int batchSize = 50;
          
          for (int i = 0; i < users.size(); i++) {
              entityManager.persist(users.get(i));
              
              // Flush and clear every batch
              if (i > 0 && i % batchSize == 0) {
                  entityManager.flush();
                  entityManager.clear();
              }
          }
          
          // Flush remaining entities
          entityManager.flush();
          entityManager.clear();
      }
  }
  ```

  **Why flush and clear?**
  + **flush()** - Sends SQL to database
  + **clear()** - Clears persistence context (frees memory)
  + Without clear(), all entities stay in memory
  + Can cause OutOfMemoryError for large batches

  **Using repository:**

  ```java
  @Service
  public class UserService {
      
      @Autowired
      private UserRepository userRepository;
      
      @PersistenceContext
      private EntityManager entityManager;
      
      @Transactional
      public void batchInsertWithRepository(List<User> users) {
          int batchSize = 50;
          
          for (int i = 0; i < users.size(); i++) {
              userRepository.save(users.get(i));
              
              if (i > 0 && i % batchSize == 0) {
                  entityManager.flush();
                  entityManager.clear();
              }
          }
          
          entityManager.flush();
          entityManager.clear();
      }
  }
  ```

</details>

<details>
  <summary>Batch Update</summary>
  <br/>

  **Basic batch update:**

  ```java
  @Service
  public class UserService {
      
      @PersistenceContext
      private EntityManager entityManager;
      
      @Transactional
      public void batchUpdate(List<User> users) {
          int batchSize = 50;
          
          for (int i = 0; i < users.size(); i++) {
              User user = users.get(i);
              user.setActive(true);
              entityManager.merge(user);
              
              if (i > 0 && i % batchSize == 0) {
                  entityManager.flush();
                  entityManager.clear();
              }
          }
          
          entityManager.flush();
          entityManager.clear();
      }
  }
  ```

  **Bulk update (single query):**

  ```java
  @Repository
  public interface UserRepository extends JpaRepository<User, Long> {
      
      @Modifying
      @Query("UPDATE User u SET u.active = :active WHERE u.lastLogin < :date")
      int bulkUpdateInactiveUsers(@Param("active") boolean active, 
                                   @Param("date") LocalDateTime date);
  }
  
  @Service
  public class UserService {
      
      @Autowired
      private UserRepository userRepository;
      
      @Transactional
      public void deactivateInactiveUsers() {
          LocalDateTime cutoffDate = LocalDateTime.now().minusMonths(6);
          int updated = userRepository.bulkUpdateInactiveUsers(false, cutoffDate);
          // SQL: UPDATE users SET active = false WHERE last_login < ?
          // Single query for all updates!
          
          System.out.println("Updated " + updated + " users");
      }
  }
  ```

  **When to use batch vs bulk:**
  + **Batch update** - When you need to update different fields per entity
  + **Bulk update** - When updating same field(s) for all entities

</details>

<details>
  <summary>Batch Delete</summary>
  <br/>

  **Basic batch delete:**

  ```java
  @Service
  public class UserService {
      
      @PersistenceContext
      private EntityManager entityManager;
      
      @Transactional
      public void batchDelete(List<Long> userIds) {
          int batchSize = 50;
          
          for (int i = 0; i < userIds.size(); i++) {
              User user = entityManager.find(User.class, userIds.get(i));
              if (user != null) {
                  entityManager.remove(user);
              }
              
              if (i > 0 && i % batchSize == 0) {
                  entityManager.flush();
                  entityManager.clear();
              }
          }
          
          entityManager.flush();
          entityManager.clear();
      }
  }
  ```

  **Bulk delete (single query):**

  ```java
  @Repository
  public interface UserRepository extends JpaRepository<User, Long> {
      
      @Modifying
      @Query("DELETE FROM User u WHERE u.active = false")
      int bulkDeleteInactiveUsers();
      
      @Modifying
      @Query("DELETE FROM User u WHERE u.id IN :ids")
      int bulkDeleteByIds(@Param("ids") List<Long> ids);
  }
  
  @Service
  public class UserService {
      
      @Autowired
      private UserRepository userRepository;
      
      @Transactional
      public void deleteInactiveUsers() {
          int deleted = userRepository.bulkDeleteInactiveUsers();
          // SQL: DELETE FROM users WHERE active = false
          // Single query!
          
          System.out.println("Deleted " + deleted + " users");
      }
  }
  ```

</details>

<details>
  <summary>JDBC Batch Insert</summary>
  <br/>

  **Using JDBC for maximum performance:**

  ```java
  @Service
  public class UserService {
      
      @Autowired
      private DataSource dataSource;
      
      public void jdbcBatchInsert(List<User> users) throws SQLException {
          String sql = "INSERT INTO users (username, email, age) VALUES (?, ?, ?)";
          
          try (Connection conn = dataSource.getConnection();
               PreparedStatement ps = conn.prepareStatement(sql)) {
              
              conn.setAutoCommit(false);
              
              int batchSize = 50;
              int count = 0;
              
              for (User user : users) {
                  ps.setString(1, user.getUsername());
                  ps.setString(2, user.getEmail());
                  ps.setInt(3, user.getAge());
                  ps.addBatch();
                  count++;
                  
                  if (count % batchSize == 0) {
                      ps.executeBatch();
                      ps.clearBatch();
                  }
              }
              
              // Execute remaining batch
              ps.executeBatch();
              conn.commit();
          }
      }
  }
  ```

  **Advantages of JDBC batch:**
  + Fastest performance
  + No JPA overhead
  + Direct database access

  **Disadvantages:**
  + No entity lifecycle callbacks
  + No automatic ID generation
  + More boilerplate code
  + No relationship management

</details>

<details>
  <summary>Batch Processing with Generated IDs</summary>
  <br/>

  **Problem with IDENTITY strategy:**

  ```java
  @Entity
  public class User {
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;
      
      // IDENTITY disables batch inserts!
      // Hibernate must get ID immediately after each insert
  }
  ```

  **Solution 1: Use SEQUENCE strategy:**

  ```java
  @Entity
  public class User {
      @Id
      @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
      @SequenceGenerator(name = "user_seq", sequenceName = "user_sequence", allocationSize = 50)
      private Long id;
      
      // SEQUENCE allows batch inserts
      // Hibernate pre-allocates IDs from sequence
  }
  ```

  **Solution 2: Use TABLE strategy:**

  ```java
  @Entity
  public class User {
      @Id
      @GeneratedValue(strategy = GenerationType.TABLE, generator = "user_gen")
      @TableGenerator(name = "user_gen", table = "id_generator", 
                      pkColumnName = "gen_name", valueColumnName = "gen_value",
                      allocationSize = 50)
      private Long id;
      
      // TABLE allows batch inserts
  }
  ```

  **Solution 3: Use UUID:**

  ```java
  @Entity
  public class User {
      @Id
      @GeneratedValue(generator = "uuid2")
      @GenericGenerator(name = "uuid2", strategy = "uuid2")
      @Column(columnDefinition = "BINARY(16)")
      private UUID id;
      
      // UUID allows batch inserts
      // IDs generated in application
  }
  ```

  **Performance comparison:**

  | Strategy | Batch Support | Performance | Use Case |
  |----------|---------------|-------------|----------|
  | IDENTITY | ❌ No | Slow | Small inserts |
  | SEQUENCE | ✅ Yes | Fast | PostgreSQL, Oracle |
  | TABLE | ✅ Yes | Medium | Any database |
  | UUID | ✅ Yes | Fast | Distributed systems |

</details>

<details>
  <summary>Batch Processing with Relationships</summary>
  <br/>

  **Batch insert with @ManyToOne:**

  ```java
  @Service
  public class OrderService {
      
      @PersistenceContext
      private EntityManager entityManager;
      
      @Transactional
      public void batchInsertOrders(User user, List<Order> orders) {
          int batchSize = 50;
          
          for (int i = 0; i < orders.size(); i++) {
              Order order = orders.get(i);
              order.setUser(user);
              entityManager.persist(order);
              
              if (i > 0 && i % batchSize == 0) {
                  entityManager.flush();
                  entityManager.clear();
                  
                  // Re-attach user after clear
                  user = entityManager.merge(user);
              }
          }
          
          entityManager.flush();
          entityManager.clear();
      }
  }
  ```

  **Batch insert with @OneToMany:**

  ```java
  @Service
  public class UserService {
      
      @PersistenceContext
      private EntityManager entityManager;
      
      @Transactional
      public void batchInsertUsersWithOrders(List<User> users) {
          int batchSize = 50;
          int count = 0;
          
          for (User user : users) {
              entityManager.persist(user);
              
              // Persist orders
              for (Order order : user.getOrders()) {
                  order.setUser(user);
                  entityManager.persist(order);
              }
              
              count++;
              if (count % batchSize == 0) {
                  entityManager.flush();
                  entityManager.clear();
              }
          }
          
          entityManager.flush();
          entityManager.clear();
      }
  }
  ```

</details>

<details>
  <summary>StatelessSession for Batch Processing</summary>
  <br/>

  **StatelessSession** bypasses first-level cache for better performance.

  **Using StatelessSession:**

  ```java
  @Service
  public class UserService {
      
      @PersistenceContext
      private EntityManager entityManager;
      
      @Transactional
      public void batchInsertWithStatelessSession(List<User> users) {
          Session session = entityManager.unwrap(Session.class);
          SessionFactory sessionFactory = session.getSessionFactory();
          
          try (StatelessSession statelessSession = sessionFactory.openStatelessSession()) {
              Transaction tx = statelessSession.beginTransaction();
              
              int batchSize = 50;
              
              for (int i = 0; i < users.size(); i++) {
                  statelessSession.insert(users.get(i));
                  
                  if (i > 0 && i % batchSize == 0) {
                      tx.commit();
                      tx = statelessSession.beginTransaction();
                  }
              }
              
              tx.commit();
          }
      }
  }
  ```

  **Advantages of StatelessSession:**
  + No first-level cache (less memory)
  + No dirty checking
  + Faster for bulk operations
  + Better for large datasets

  **Disadvantages:**
  + No automatic dirty checking
  + No cascade operations
  + No lazy loading
  + No entity lifecycle callbacks

  **When to use:**
  + Importing large datasets
  + Batch processing jobs
  + ETL operations
  + When you don't need JPA features

</details>

<details>
  <summary>Performance Optimization</summary>
  <br/>

  **1. Disable unnecessary features:**

  ```properties
  # application.properties
  
  # Disable second-level cache for batch operations
  spring.jpa.properties.hibernate.cache.use_second_level_cache=false
  
  # Disable query cache
  spring.jpa.properties.hibernate.cache.use_query_cache=false
  
  # Disable auto-flush
  spring.jpa.properties.hibernate.flushMode=MANUAL
  ```

  **2. Use read-only transactions:**

  ```java
  @Transactional(readOnly = true)
  public List<User> batchRead() {
      // Hibernate skips dirty checking
      return userRepository.findAll();
  }
  ```

  **3. Optimize batch size:**

  ```java
  @Service
  public class UserService {
      
      @Value("${batch.size:50}")
      private int batchSize;
      
      @Transactional
      public void batchInsert(List<User> users) {
          for (int i = 0; i < users.size(); i++) {
              entityManager.persist(users.get(i));
              
              if (i > 0 && i % batchSize == 0) {
                  entityManager.flush();
                  entityManager.clear();
              }
          }
      }
  }
  ```

  **4. Use native queries for bulk operations:**

  ```java
  @Repository
  public interface UserRepository extends JpaRepository<User, Long> {
      
      @Modifying
      @Query(value = "INSERT INTO users (username, email) " +
                     "SELECT username, email FROM temp_users", 
             nativeQuery = true)
      int bulkInsertFromTemp();
  }
  ```

  **5. Partition large batches:**

  ```java
  @Service
  public class UserService {
      
      @Autowired
      private UserRepository userRepository;
      
      public void processLargeDataset(List<User> users) {
          int partitionSize = 1000;
          
          // Split into partitions
          List<List<User>> partitions = Lists.partition(users, partitionSize);
          
          for (List<User> partition : partitions) {
              batchInsert(partition);
          }
      }
      
      @Transactional
      private void batchInsert(List<User> users) {
          // Process one partition
      }
  }
  ```

</details>

<details>
  <summary>Monitoring Batch Performance</summary>
  <br/>

  **Enable Hibernate statistics:**

  ```properties
  # application.properties
  spring.jpa.properties.hibernate.generate_statistics=true
  ```

  **Monitor batch operations:**

  ```java
  @Service
  public class UserService {
      
      @PersistenceContext
      private EntityManager entityManager;
      
      @Transactional
      public void batchInsertWithStats(List<User> users) {
          Session session = entityManager.unwrap(Session.class);
          SessionFactory sessionFactory = session.getSessionFactory();
          Statistics stats = sessionFactory.getStatistics();
          stats.clear();
          
          long startTime = System.currentTimeMillis();
          
          // Batch insert
          int batchSize = 50;
          for (int i = 0; i < users.size(); i++) {
              entityManager.persist(users.get(i));
              
              if (i > 0 && i % batchSize == 0) {
                  entityManager.flush();
                  entityManager.clear();
              }
          }
          
          entityManager.flush();
          
          long endTime = System.currentTimeMillis();
          
          // Print statistics
          System.out.println("Time taken: " + (endTime - startTime) + "ms");
          System.out.println("Entities inserted: " + stats.getEntityInsertCount());
          System.out.println("Queries executed: " + stats.getPrepareStatementCount());
          System.out.println("Batch size: " + batchSize);
      }
  }
  ```

  **Log batch SQL:**

  ```properties
  # application.properties
  logging.level.org.hibernate.SQL=DEBUG
  logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
  logging.level.org.hibernate.engine.jdbc.batch.internal.BatchingBatch=DEBUG
  ```

</details>

<details>
  <summary>Common Pitfalls</summary>
  <br/>

  **1. Using IDENTITY strategy:**

  ```java
  // ❌ BAD: IDENTITY disables batching
  @Entity
  public class User {
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;
  }
  
  // ✅ GOOD: Use SEQUENCE
  @Entity
  public class User {
      @Id
      @GeneratedValue(strategy = GenerationType.SEQUENCE)
      private Long id;
  }
  ```

  **2. Not flushing and clearing:**

  ```java
  // ❌ BAD: Memory leak
  @Transactional
  public void batchInsert(List<User> users) {
      for (User user : users) {
          entityManager.persist(user);
          // All entities stay in memory!
      }
  }
  
  // ✅ GOOD: Flush and clear
  @Transactional
  public void batchInsert(List<User> users) {
      for (int i = 0; i < users.size(); i++) {
          entityManager.persist(users.get(i));
          
          if (i % 50 == 0) {
              entityManager.flush();
              entityManager.clear();
          }
      }
  }
  ```

  **3. Forgetting to configure batch size:**

  ```properties
  # ❌ BAD: No batch configuration
  # Hibernate uses default (no batching)
  
  # ✅ GOOD: Configure batch size
  spring.jpa.properties.hibernate.jdbc.batch_size=50
  spring.jpa.properties.hibernate.order_inserts=true
  ```

  **4. Using saveAll() incorrectly:**

  ```java
  // ❌ BAD: saveAll() doesn't batch automatically
  @Transactional
  public void saveUsers(List<User> users) {
      userRepository.saveAll(users);
      // No batching without proper configuration!
  }
  
  // ✅ GOOD: Manual batching
  @Transactional
  public void saveUsers(List<User> users) {
      for (int i = 0; i < users.size(); i++) {
          userRepository.save(users.get(i));
          
          if (i % 50 == 0) {
              entityManager.flush();
              entityManager.clear();
          }
      }
  }
  ```

  **5. Mixing batch and non-batch operations:**

  ```java
  // ❌ BAD: Mixing operations
  @Transactional
  public void mixedOperations(List<User> users) {
      for (User user : users) {
          entityManager.persist(user);
          
          // Query breaks batching!
          User existing = userRepository.findByUsername(user.getUsername());
      }
  }
  
  // ✅ GOOD: Separate operations
  @Transactional
  public void separateOperations(List<User> users) {
      // First, query all
      Map<String, User> existing = new HashMap<>();
      for (User user : users) {
          User found = userRepository.findByUsername(user.getUsername());
          if (found != null) {
              existing.put(user.getUsername(), found);
          }
      }
      
      // Then, batch insert
      for (int i = 0; i < users.size(); i++) {
          if (!existing.containsKey(users.get(i).getUsername())) {
              entityManager.persist(users.get(i));
              
              if (i % 50 == 0) {
                  entityManager.flush();
                  entityManager.clear();
              }
          }
      }
  }
  ```

</details>

<details>
  <summary>Best Practices</summary>
  <br/>

  ```java
  // ✅ DO: Configure batch size
  spring.jpa.properties.hibernate.jdbc.batch_size=50
  spring.jpa.properties.hibernate.order_inserts=true
  spring.jpa.properties.hibernate.order_updates=true
  
  // ✅ DO: Use SEQUENCE or UUID for ID generation
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;
  
  // ✅ DO: Flush and clear periodically
  @Transactional
  public void batchInsert(List<User> users) {
      for (int i = 0; i < users.size(); i++) {
          entityManager.persist(users.get(i));
          
          if (i > 0 && i % 50 == 0) {
              entityManager.flush();
              entityManager.clear();
          }
      }
      entityManager.flush();
      entityManager.clear();
  }
  
  // ✅ DO: Use bulk operations for simple updates
  @Modifying
  @Query("UPDATE User u SET u.active = :active WHERE u.lastLogin < :date")
  int bulkUpdate(@Param("active") boolean active, @Param("date") LocalDateTime date);
  
  // ✅ DO: Use StatelessSession for large datasets
  try (StatelessSession session = sessionFactory.openStatelessSession()) {
      Transaction tx = session.beginTransaction();
      for (User user : users) {
          session.insert(user);
      }
      tx.commit();
  }
  
  // ✅ DO: Partition large datasets
  List<List<User>> partitions = Lists.partition(users, 1000);
  for (List<User> partition : partitions) {
      batchInsert(partition);
  }
  
  // ✅ DO: Monitor performance
  Statistics stats = sessionFactory.getStatistics();
  System.out.println("Entities inserted: " + stats.getEntityInsertCount());
  
  // ❌ DON'T: Use IDENTITY strategy for batch inserts
  @GeneratedValue(strategy = GenerationType.IDENTITY)  // Disables batching!
  
  // ❌ DON'T: Forget to flush and clear
  for (User user : users) {
      entityManager.persist(user);
      // Memory leak!
  }
  
  // ❌ DON'T: Query inside batch loop
  for (User user : users) {
      entityManager.persist(user);
      userRepository.findByUsername(user.getUsername());  // Breaks batching!
  }
  
  // ❌ DON'T: Use batch processing for small datasets
  if (users.size() < 10) {
      userRepository.saveAll(users);  // Simple save is fine
  }
  
  // ✅ DO: Use JDBC batch for maximum performance
  try (PreparedStatement ps = conn.prepareStatement(sql)) {
      for (User user : users) {
          ps.setString(1, user.getUsername());
          ps.addBatch();
      }
      ps.executeBatch();
  }
  
  // ✅ DO: Disable unnecessary features for batch operations
  spring.jpa.properties.hibernate.cache.use_second_level_cache=false
  
  // ✅ DO: Use appropriate batch size (50-100)
  int batchSize = 50;  // Test to find optimal size
  
  // ✅ DO: Handle errors gracefully
  @Transactional
  public void batchInsertWithErrorHandling(List<User> users) {
      try {
          for (int i = 0; i < users.size(); i++) {
              entityManager.persist(users.get(i));
              
              if (i % 50 == 0) {
                  entityManager.flush();
                  entityManager.clear();
              }
          }
      } catch (Exception e) {
          log.error("Batch insert failed", e);
          throw e;
      }
  }
  ```

  **Summary:**
  + **Configure batch size** (50-100 is optimal)
  + Use **SEQUENCE or UUID** for ID generation (not IDENTITY)
  + **Flush and clear** periodically to free memory
  + Use **bulk operations** for simple updates/deletes
  + Use **StatelessSession** for large datasets
  + **Partition** large datasets into smaller batches
  + **Monitor performance** with Hibernate statistics
  + Use **JDBC batch** for maximum performance
  + **Disable unnecessary features** (cache, dirty checking)
  + Don't **query inside batch loop**
  + Handle **errors gracefully**
  + Test to find **optimal batch size** for your use case

</details>

## Session vs EntityManager

<details>
  <summary>Overview</summary>
  <br/>

  **EntityManager** is the JPA standard interface, while **Session** is Hibernate-specific.

  **Key differences:**
  + **EntityManager** - JPA standard (portable across providers)
  + **Session** - Hibernate-specific (more features)
  + Session extends EntityManager functionality
  + EntityManager is the recommended approach

  **Relationship:**

  ```
  JPA Standard
      ↓
  EntityManager (interface)
      ↓
  Hibernate Implementation
      ↓
  Session (extends EntityManager)
  ```

  **When to use each:**
  + **EntityManager** - Default choice (portable, standard)
  + **Session** - When you need Hibernate-specific features

</details>

<details>
  <summary>EntityManager (JPA Standard)</summary>
  <br/>

  **EntityManager** is the JPA standard interface for persistence operations.

  **Inject EntityManager:**

  ```java
  @Service
  public class UserService {
      
      @PersistenceContext
      private EntityManager entityManager;
      
      // Use EntityManager
  }
  ```

  **Basic operations:**

  ```java
  @Service
  public class UserService {
      
      @PersistenceContext
      private EntityManager entityManager;
      
      @Transactional
      public void entityManagerOperations() {
          // Create
          User user = new User("john", "john@example.com");
          entityManager.persist(user);
          
          // Read
          User found = entityManager.find(User.class, 1L);
          
          // Update
          found.setUsername("john_updated");
          entityManager.merge(found);
          
          // Delete
          entityManager.remove(found);
          
          // Flush
          entityManager.flush();
          
          // Clear
          entityManager.clear();
          
          // Detach
          entityManager.detach(user);
          
          // Refresh
          entityManager.refresh(user);
          
          // Check if managed
          boolean contains = entityManager.contains(user);
      }
  }
  ```

  **JPQL queries:**

  ```java
  @Transactional
  public List<User> findUsers() {
      return entityManager
          .createQuery("SELECT u FROM User u WHERE u.active = true", User.class)
          .getResultList();
  }
  ```

  **Native queries:**

  ```java
  @Transactional
  public List<User> findUsersNative() {
      return entityManager
          .createNativeQuery("SELECT * FROM users WHERE active = true", User.class)
          .getResultList();
  }
  ```

  **Advantages:**
  + JPA standard (portable)
  + Works with any JPA provider
  + Recommended by Spring
  + Better for long-term maintenance

  **Limitations:**
  + Limited to JPA features
  + No Hibernate-specific features
  + Less flexible than Session

</details>

<details>
  <summary>Session (Hibernate-Specific)</summary>
  <br/>

  **Session** is Hibernate's native interface with additional features.

  **Get Session from EntityManager:**

  ```java
  @Service
  public class UserService {
      
      @PersistenceContext
      private EntityManager entityManager;
      
      @Transactional
      public void useSession() {
          // Unwrap Session from EntityManager
          Session session = entityManager.unwrap(Session.class);
          
          // Use Session
      }
  }
  ```

  **Basic operations:**

  ```java
  @Service
  public class UserService {
      
      @PersistenceContext
      private EntityManager entityManager;
      
      @Transactional
      public void sessionOperations() {
          Session session = entityManager.unwrap(Session.class);
          
          // Create
          User user = new User("john", "john@example.com");
          session.save(user);  // Returns generated ID
          session.persist(user);  // Void return
          
          // Read
          User found = session.get(User.class, 1L);  // Returns null if not found
          User loaded = session.load(User.class, 1L);  // Returns proxy, throws exception if not found
          
          // Update
          session.update(user);
          session.merge(user);
          
          // Delete
          session.delete(user);
          
          // Save or update
          session.saveOrUpdate(user);
          
          // Flush
          session.flush();
          
          // Clear
          session.clear();
          
          // Evict
          session.evict(user);
          
          // Refresh
          session.refresh(user);
          
          // Check if managed
          boolean contains = session.contains(user);
      }
  }
  ```

  **HQL queries:**

  ```java
  @Transactional
  public List<User> findUsers() {
      Session session = entityManager.unwrap(Session.class);
      
      return session
          .createQuery("FROM User u WHERE u.active = true", User.class)
          .list();
  }
  ```

  **Advantages:**
  + More features than EntityManager
  + Hibernate-specific optimizations
  + Additional query options
  + More control over persistence

  **Limitations:**
  + Hibernate-specific (not portable)
  + Ties code to Hibernate
  + Not recommended by Spring

</details>

<details>
  <summary>Method Comparison</summary>
  <br/>

  **Create operations:**

  ```java
  // EntityManager
  entityManager.persist(user);  // Void return
  
  // Session
  session.persist(user);  // Void return
  session.save(user);  // Returns generated ID
  ```

  **Read operations:**

  ```java
  // EntityManager
  User user = entityManager.find(User.class, 1L);
  // Returns null if not found
  
  // Session
  User user1 = session.get(User.class, 1L);
  // Returns null if not found
  
  User user2 = session.load(User.class, 1L);
  // Returns proxy, throws ObjectNotFoundException if not found
  ```

  **Update operations:**

  ```java
  // EntityManager
  entityManager.merge(user);  // Returns managed instance
  
  // Session
  session.merge(user);  // Returns managed instance
  session.update(user);  // Void return, reattaches detached entity
  session.saveOrUpdate(user);  // Saves or updates based on ID
  ```

  **Delete operations:**

  ```java
  // EntityManager
  entityManager.remove(user);
  
  // Session
  session.delete(user);
  session.remove(user);  // Same as delete
  ```

  **Detach operations:**

  ```java
  // EntityManager
  entityManager.detach(user);  // Detach single entity
  entityManager.clear();  // Detach all entities
  
  // Session
  session.evict(user);  // Detach single entity
  session.clear();  // Detach all entities
  ```

  **Query operations:**

  ```java
  // EntityManager - JPQL
  entityManager.createQuery("SELECT u FROM User u", User.class)
      .getResultList();
  
  // Session - HQL
  session.createQuery("FROM User u", User.class)
      .list();
  ```

</details>

<details>
  <summary>get() vs load()</summary>
  <br/>

  **get()** loads entity immediately, **load()** returns proxy.

  **get() method:**

  ```java
  @Transactional
  public void getExample() {
      Session session = entityManager.unwrap(Session.class);
      
      // Executes SELECT immediately
      User user = session.get(User.class, 1L);
      // SQL: SELECT * FROM users WHERE id = 1
      
      if (user == null) {
          System.out.println("User not found");
      } else {
          System.out.println(user.getUsername());
      }
  }
  ```

  **load() method:**

  ```java
  @Transactional
  public void loadExample() {
      Session session = entityManager.unwrap(Session.class);
      
      // Returns proxy without SELECT
      User user = session.load(User.class, 1L);
      // No SQL executed yet
      
      System.out.println(user.getClass().getName());
      // Output: User$HibernateProxy$...
      
      // SELECT executed when accessing field
      System.out.println(user.getUsername());
      // SQL: SELECT * FROM users WHERE id = 1
      
      // If entity doesn't exist, throws ObjectNotFoundException
  }
  ```

  **Comparison:**

  | Aspect | get() | load() |
  |--------|-------|--------|
  | **Returns** | Entity or null | Proxy |
  | **Database hit** | Immediate | Lazy |
  | **Not found** | Returns null | Throws exception |
  | **Use case** | Need to check existence | Know entity exists |

  **When to use get():**
  + Need to check if entity exists
  + Need entity data immediately
  + Safer option (no exception)

  **When to use load():**
  + Know entity exists
  + Only need reference (for relationships)
  + Want lazy loading

  **Example: Setting relationship:**

  ```java
  @Transactional
  public void createOrder(Long userId) {
      Session session = entityManager.unwrap(Session.class);
      
      // Use load() - only need reference
      User user = session.load(User.class, userId);
      // No SELECT executed
      
      Order order = new Order();
      order.setUser(user);  // Just setting reference
      order.setTotal(new BigDecimal("99.99"));
      
      session.save(order);
      // SQL: INSERT INTO orders (user_id, total) VALUES (?, ?)
      // No SELECT for user!
  }
  ```

</details>

<details>
  <summary>save() vs persist()</summary>
  <br/>

  **save()** returns generated ID, **persist()** returns void.

  **save() method:**

  ```java
  @Transactional
  public void saveExample() {
      Session session = entityManager.unwrap(Session.class);
      
      User user = new User("john", "john@example.com");
      
      // Returns generated ID
      Serializable id = session.save(user);
      System.out.println("Generated ID: " + id);
      
      // Can use outside transaction (not recommended)
  }
  ```

  **persist() method:**

  ```java
  @Transactional
  public void persistExample() {
      Session session = entityManager.unwrap(Session.class);
      
      User user = new User("john", "john@example.com");
      
      // Void return
      session.persist(user);
      
      // ID available after flush
      session.flush();
      System.out.println("Generated ID: " + user.getId());
      
      // Must be inside transaction
  }
  ```

  **Comparison:**

  | Aspect | save() | persist() |
  |--------|--------|-----------|
  | **Return type** | Serializable (ID) | void |
  | **JPA standard** | No | Yes |
  | **Transaction** | Optional | Required |
  | **ID generation** | Immediate | On flush |
  | **Recommendation** | Use persist() | Preferred |

  **When to use save():**
  + Need generated ID immediately
  + Hibernate-specific code

  **When to use persist():**
  + JPA standard code (recommended)
  + Don't need ID immediately
  + Better for portability

</details>

<details>
  <summary>update() vs merge()</summary>
  <br/>

  **update()** reattaches detached entity, **merge()** copies state.

  **update() method:**

  ```java
  @Transactional
  public void updateExample() {
      Session session = entityManager.unwrap(Session.class);
      
      // Get detached entity
      User user = getDetachedUser();
      
      // Reattach to session
      session.update(user);
      // user is now managed
      
      user.setUsername("updated");
      // Changes tracked automatically
  }
  ```

  **merge() method:**

  ```java
  @Transactional
  public void mergeExample() {
      Session session = entityManager.unwrap(Session.class);
      
      // Get detached entity
      User detachedUser = getDetachedUser();
      
      // Copy state to managed entity
      User managedUser = session.merge(detachedUser);
      // detachedUser is still detached
      // managedUser is managed
      
      managedUser.setUsername("updated");
      // Changes tracked on managedUser
      
      detachedUser.setEmail("new@example.com");
      // Changes on detachedUser are NOT tracked
  }
  ```

  **Comparison:**

  | Aspect | update() | merge() |
  |--------|----------|---------|
  | **Behavior** | Reattaches entity | Copies state |
  | **Return** | void | Managed entity |
  | **Original entity** | Becomes managed | Stays detached |
  | **JPA standard** | No | Yes |
  | **Use case** | Hibernate code | JPA code |

  **Problem with update():**

  ```java
  @Transactional
  public void updateProblem() {
      Session session = entityManager.unwrap(Session.class);
      
      // Load entity
      User user1 = session.get(User.class, 1L);
      
      // Get detached entity with same ID
      User user2 = getDetachedUser();  // ID = 1
      
      // Try to reattach
      session.update(user2);
      // Exception: NonUniqueObjectException
      // Session already contains entity with ID = 1
  }
  ```

  **merge() handles this:**

  ```java
  @Transactional
  public void mergeSolution() {
      Session session = entityManager.unwrap(Session.class);
      
      // Load entity
      User user1 = session.get(User.class, 1L);
      
      // Get detached entity with same ID
      User user2 = getDetachedUser();  // ID = 1
      
      // Merge state
      User merged = session.merge(user2);
      // Works! Copies state from user2 to user1
      // merged == user1 (same instance)
  }
  ```

</details>

<details>
  <summary>Hibernate-Specific Features</summary>
  <br/>

  **Features only available in Session:**

  **1. Criteria API (legacy):**

  ```java
  @Transactional
  public List<User> findUsersCriteria() {
      Session session = entityManager.unwrap(Session.class);
      
      CriteriaBuilder cb = session.getCriteriaBuilder();
      CriteriaQuery<User> query = cb.createQuery(User.class);
      Root<User> root = query.from(User.class);
      query.select(root).where(cb.equal(root.get("active"), true));
      
      return session.createQuery(query).list();
  }
  ```

  **2. Filters:**

  ```java
  @Entity
  @FilterDef(name = "activeFilter", parameters = @ParamDef(name = "active", type = "boolean"))
  @Filter(name = "activeFilter", condition = "active = :active")
  public class User {
      private boolean active;
  }
  
  @Transactional
  public List<User> findActiveUsers() {
      Session session = entityManager.unwrap(Session.class);
      
      // Enable filter
      session.enableFilter("activeFilter").setParameter("active", true);
      
      List<User> users = session.createQuery("FROM User", User.class).list();
      // SQL: SELECT * FROM users WHERE active = true
      
      // Disable filter
      session.disableFilter("activeFilter");
      
      return users;
  }
  ```

  **3. Statistics:**

  ```java
  @Transactional
  public void getStatistics() {
      Session session = entityManager.unwrap(Session.class);
      SessionFactory sessionFactory = session.getSessionFactory();
      Statistics stats = sessionFactory.getStatistics();
      
      System.out.println("Entity insert count: " + stats.getEntityInsertCount());
      System.out.println("Query execution count: " + stats.getQueryExecutionCount());
      System.out.println("Cache hit count: " + stats.getSecondLevelCacheHitCount());
  }
  ```

  **4. Flush mode:**

  ```java
  @Transactional
  public void setFlushMode() {
      Session session = entityManager.unwrap(Session.class);
      
      // Set flush mode
      session.setFlushMode(FlushMode.MANUAL);
      session.setFlushMode(FlushMode.COMMIT);
      session.setFlushMode(FlushMode.AUTO);
      session.setFlushMode(FlushMode.ALWAYS);
  }
  ```

  **5. Session factory:**

  ```java
  @Transactional
  public void useSessionFactory() {
      Session session = entityManager.unwrap(Session.class);
      SessionFactory sessionFactory = session.getSessionFactory();
      
      // Open new session
      Session newSession = sessionFactory.openSession();
      
      // Open stateless session
      StatelessSession statelessSession = sessionFactory.openStatelessSession();
  }
  ```

  **6. Replicate:**

  ```java
  @Transactional
  public void replicateEntity() {
      Session session = entityManager.unwrap(Session.class);
      
      User user = new User("john", "john@example.com");
      user.setId(100L);  // Set specific ID
      
      // Replicate entity
      session.replicate(user, ReplicationMode.OVERWRITE);
      // Inserts or updates based on ID
  }
  ```

</details>

<details>
  <summary>When to Use Each</summary>
  <br/>

  **Use EntityManager when:**
  + Building portable JPA application ✅
  + Following JPA standards ✅
  + Using Spring Data JPA ✅
  + Want provider independence ✅
  + Standard CRUD operations ✅
  + Long-term maintenance ✅

  **Use Session when:**
  + Need Hibernate-specific features ✅
  + Using Hibernate filters ✅
  + Need session statistics ✅
  + Using legacy Hibernate code ✅
  + Need load() method ✅
  + Need saveOrUpdate() ✅

  **Examples:**

  ```java
  // ✅ GOOD: Use EntityManager by default
  @Service
  public class UserService {
      
      @PersistenceContext
      private EntityManager entityManager;
      
      @Transactional
      public void createUser(User user) {
          entityManager.persist(user);
      }
  }
  
  // ✅ GOOD: Use Session for Hibernate features
  @Service
  public class UserService {
      
      @PersistenceContext
      private EntityManager entityManager;
      
      @Transactional
      public List<User> findActiveUsers() {
          Session session = entityManager.unwrap(Session.class);
          
          // Enable Hibernate filter
          session.enableFilter("activeFilter").setParameter("active", true);
          
          return session.createQuery("FROM User", User.class).list();
      }
  }
  
  // ✅ GOOD: Use Session for load()
  @Service
  public class OrderService {
      
      @PersistenceContext
      private EntityManager entityManager;
      
      @Transactional
      public void createOrder(Long userId) {
          Session session = entityManager.unwrap(Session.class);
          
          // Use load() - only need reference
          User user = session.load(User.class, userId);
          
          Order order = new Order();
          order.setUser(user);
          order.setTotal(new BigDecimal("99.99"));
          
          entityManager.persist(order);
      }
  }
  ```

</details>

<details>
  <summary>Migration from Session to EntityManager</summary>
  <br/>

  **Migrate Hibernate code to JPA:**

  ```java
  // Before: Hibernate Session
  @Service
  public class UserService {
      
      @Autowired
      private SessionFactory sessionFactory;
      
      @Transactional
      public void createUser(User user) {
          Session session = sessionFactory.getCurrentSession();
          session.save(user);
      }
      
      @Transactional
      public User getUser(Long id) {
          Session session = sessionFactory.getCurrentSession();
          return session.get(User.class, id);
      }
      
      @Transactional
      public List<User> findUsers() {
          Session session = sessionFactory.getCurrentSession();
          return session.createQuery("FROM User", User.class).list();
      }
  }
  
  // After: JPA EntityManager
  @Service
  public class UserService {
      
      @PersistenceContext
      private EntityManager entityManager;
      
      @Transactional
      public void createUser(User user) {
          entityManager.persist(user);
      }
      
      @Transactional
      public User getUser(Long id) {
          return entityManager.find(User.class, id);
      }
      
      @Transactional
      public List<User> findUsers() {
          return entityManager
              .createQuery("SELECT u FROM User u", User.class)
              .getResultList();
      }
  }
  ```

  **Migration mapping:**

  | Hibernate Session | JPA EntityManager |
  |-------------------|-------------------|
  | session.save() | entityManager.persist() |
  | session.get() | entityManager.find() |
  | session.load() | entityManager.getReference() |
  | session.update() | entityManager.merge() |
  | session.delete() | entityManager.remove() |
  | session.saveOrUpdate() | Check ID, then persist/merge |
  | session.evict() | entityManager.detach() |
  | session.createQuery().list() | entityManager.createQuery().getResultList() |

</details>

<details>
  <summary>Best Practices</summary>
  <br/>

  ```java
  // ✅ DO: Use EntityManager by default
  @PersistenceContext
  private EntityManager entityManager;
  
  // ✅ DO: Use persist() instead of save()
  entityManager.persist(user);
  
  // ✅ DO: Use find() instead of get()
  User user = entityManager.find(User.class, 1L);
  
  // ✅ DO: Use merge() instead of update()
  User managed = entityManager.merge(user);
  
  // ✅ DO: Use JPQL instead of HQL
  entityManager.createQuery("SELECT u FROM User u", User.class)
      .getResultList();
  
  // ✅ DO: Unwrap Session only when needed
  @Transactional
  public void useHibernateFeature() {
      Session session = entityManager.unwrap(Session.class);
      session.enableFilter("activeFilter");
      // Use Hibernate-specific feature
  }
  
  // ❌ DON'T: Inject SessionFactory in new code
  @Autowired
  private SessionFactory sessionFactory;  // Old approach
  
  // ✅ DO: Use EntityManager instead
  @PersistenceContext
  private EntityManager entityManager;
  
  // ❌ DON'T: Use Session methods when EntityManager equivalent exists
  Session session = entityManager.unwrap(Session.class);
  session.save(user);  // Use entityManager.persist() instead
  
  // ✅ DO: Use getReference() instead of load()
  User user = entityManager.getReference(User.class, 1L);
  
  // ✅ DO: Check for null with find()
  User user = entityManager.find(User.class, 1L);
  if (user == null) {
      throw new UserNotFoundException();
  }
  
  // ✅ DO: Use JPA standard annotations
  @PersistenceContext
  private EntityManager entityManager;
  
  // ❌ DON'T: Mix Session and EntityManager unnecessarily
  @Transactional
  public void mixedApproach() {
      entityManager.persist(user1);
      
      Session session = entityManager.unwrap(Session.class);
      session.save(user2);  // Inconsistent
  }
  
  // ✅ DO: Be consistent
  @Transactional
  public void consistentApproach() {
      entityManager.persist(user1);
      entityManager.persist(user2);
  }
  
  // ✅ DO: Document why you're using Session
  @Transactional
  public List<User> findActiveUsers() {
      // Using Session for Hibernate filter feature
      Session session = entityManager.unwrap(Session.class);
      session.enableFilter("activeFilter");
      return session.createQuery("FROM User", User.class).list();
  }
  ```

  **Summary:**
  + **EntityManager** is JPA standard - use by default
  + **Session** is Hibernate-specific - use only when needed
  + Use **persist()** instead of save()
  + Use **find()** instead of get()
  + Use **merge()** instead of update()
  + Use **getReference()** instead of load()
  + Use **JPQL** instead of HQL
  + **Unwrap Session** only for Hibernate-specific features
  + Be **consistent** in your approach
  + **Document** why you're using Session
  + Prefer **JPA standard** for portability
  + Use **EntityManager** for new code
  + **Migrate** old Session code to EntityManager when possible

</details>








