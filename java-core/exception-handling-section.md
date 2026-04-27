
## Exception Handling

<details>
  <summary>Exception Hierarchy</summary>
  <br/>

  Java exceptions are organized in a hierarchy with `Throwable` at the top.

  **Exception Hierarchy:**
  ```java
  Throwable
  ├── Error (Unchecked)
  │   ├── OutOfMemoryError
  │   ├── StackOverflowError
  │   └── VirtualMachineError
  └── Exception
      ├── RuntimeException (Unchecked)
      │   ├── NullPointerException
      │   ├── IllegalArgumentException
      │   ├── IndexOutOfBoundsException
      │   ├── ArithmeticException
      │   └── ClassCastException
      └── Checked Exceptions
          ├── IOException
          ├── SQLException
          ├── ClassNotFoundException
          └── InterruptedException
  ```

  **Key Points:**
  + **Error:** Serious problems that applications should not try to catch (e.g., OutOfMemoryError)
  + **RuntimeException:** Unchecked exceptions that occur during runtime
  + **Checked Exceptions:** Must be declared or caught at compile time
  
</details>

<details>
  <summary>Checked vs Unchecked Exceptions</summary>
  <br/>

  **Checked Exceptions:**
  + Must be declared in method signature with `throws` or caught with try-catch
  + Checked at compile time
  + Represent recoverable conditions
  + Examples: IOException, SQLException, ClassNotFoundException

  **Unchecked Exceptions (RuntimeException):**
  + Do not need to be declared or caught
  + Checked at runtime
  + Usually represent programming errors
  + Examples: NullPointerException, IllegalArgumentException, IndexOutOfBoundsException

  **Comparison:**
  ```java
  // Checked Exception - must handle
  public void readFile(String path) throws IOException {
      FileReader reader = new FileReader(path);  // Throws IOException
      // ...
  }
  
  // Or catch it
  public void readFile(String path) {
      try {
          FileReader reader = new FileReader(path);
          // ...
      } catch (IOException e) {
          System.err.println("Error reading file: " + e.getMessage());
      }
  }
  
  // Unchecked Exception - no need to declare
  public int divide(int a, int b) {
      return a / b;  // May throw ArithmeticException, but no need to declare
  }
  
  // Can still catch if needed
  public int safeDivide(int a, int b) {
      try {
          return a / b;
      } catch (ArithmeticException e) {
          System.err.println("Cannot divide by zero");
          return 0;
      }
  }
  ```

  **When to Use Which:**

  **Use Checked Exceptions when:**
  + The caller can reasonably be expected to recover from the exception
  + External resources are involved (files, network, database)
  + The exception represents a condition outside the program's control

  **Use Unchecked Exceptions when:**
  + The exception represents a programming error (bug)
  + Recovery is not possible or practical
  + The exception indicates violation of preconditions (null arguments, invalid state)

  **Examples:**
  ```java
  // Checked - caller can retry or use different file
  public void processFile(String filename) throws FileNotFoundException {
      File file = new File(filename);
      if (!file.exists()) {
          throw new FileNotFoundException("File not found: " + filename);
      }
      // process file
  }
  
  // Unchecked - programming error, should be fixed in code
  public void setAge(int age) {
      if (age < 0) {
          throw new IllegalArgumentException("Age cannot be negative");
      }
      this.age = age;
  }
  ```
  
</details>

<details>
  <summary>Try-Catch-Finally</summary>
  <br/>

  **Basic Try-Catch:**
  ```java
  try {
      // Code that may throw exception
      int result = divide(10, 0);
  } catch (ArithmeticException e) {
      // Handle exception
      System.err.println("Error: " + e.getMessage());
  }
  ```

  **Multiple Catch Blocks:**
  ```java
  try {
      String str = null;
      int length = str.length();  // NullPointerException
      int result = 10 / 0;        // ArithmeticException
  } catch (NullPointerException e) {
      System.err.println("Null pointer: " + e.getMessage());
  } catch (ArithmeticException e) {
      System.err.println("Arithmetic error: " + e.getMessage());
  } catch (Exception e) {
      System.err.println("General error: " + e.getMessage());
  }
  ```

  **Multi-Catch (Java 7+):**
  ```java
  try {
      // Code that may throw multiple exceptions
      processFile("data.txt");
  } catch (IOException | SQLException e) {
      // Handle both exceptions the same way
      System.err.println("Error: " + e.getMessage());
      logError(e);
  }
  ```

  **Finally Block:**
  ```java
  FileReader reader = null;
  try {
      reader = new FileReader("file.txt");
      // Read file
  } catch (IOException e) {
      System.err.println("Error reading file: " + e.getMessage());
  } finally {
      // Always executed, even if exception occurs
      if (reader != null) {
          try {
              reader.close();
          } catch (IOException e) {
              System.err.println("Error closing file: " + e.getMessage());
          }
      }
  }
  ```

  **Try-Catch-Finally Execution Order:**
  ```java
  public static int testFinally() {
      try {
          System.out.println("Try block");
          return 1;
      } catch (Exception e) {
          System.out.println("Catch block");
          return 2;
      } finally {
          System.out.println("Finally block");
          // Finally executes even if return in try/catch
          // But don't return from finally - it overrides try/catch return
      }
  }
  
  // Output:
  // Try block
  // Finally block
  // Returns: 1
  ```

  **Important Notes:**
  + Finally block always executes (except System.exit() or JVM crash)
  + Finally executes even if there's a return statement in try or catch
  + Avoid returning from finally block - it overrides try/catch return values
  
</details>

<details>
  <summary>Try-With-Resources (Java 7+)</summary>
  <br/>

  Try-with-resources automatically closes resources that implement `AutoCloseable` interface.

  **Basic Syntax:**
  ```java
  // Old way - manual closing
  BufferedReader reader = null;
  try {
      reader = new BufferedReader(new FileReader("file.txt"));
      String line = reader.readLine();
      System.out.println(line);
  } catch (IOException e) {
      e.printStackTrace();
  } finally {
      if (reader != null) {
          try {
              reader.close();
          } catch (IOException e) {
              e.printStackTrace();
          }
      }
  }
  
  // New way - try-with-resources
  try (BufferedReader reader = new BufferedReader(new FileReader("file.txt"))) {
      String line = reader.readLine();
      System.out.println(line);
  } catch (IOException e) {
      e.printStackTrace();
  }
  // reader.close() is called automatically
  ```

  **Multiple Resources:**
  ```java
  try (FileInputStream input = new FileInputStream("input.txt");
       FileOutputStream output = new FileOutputStream("output.txt")) {
      
      int data;
      while ((data = input.read()) != -1) {
          output.write(data);
      }
  } catch (IOException e) {
      e.printStackTrace();
  }
  // Both input and output are closed automatically in reverse order
  ```

  **Java 9+ Enhancement:**
  ```java
  // Java 9+ allows effectively final variables
  BufferedReader reader = new BufferedReader(new FileReader("file.txt"));
  
  try (reader) {  // Can use existing variable
      String line = reader.readLine();
      System.out.println(line);
  } catch (IOException e) {
      e.printStackTrace();
  }
  ```

  **Custom AutoCloseable:**
  ```java
  public class DatabaseConnection implements AutoCloseable {
      private Connection connection;
      
      public DatabaseConnection(String url) throws SQLException {
          this.connection = DriverManager.getConnection(url);
          System.out.println("Connection opened");
      }
      
      public void executeQuery(String sql) throws SQLException {
          // Execute query
      }
      
      @Override
      public void close() throws SQLException {
          if (connection != null && !connection.isClosed()) {
              connection.close();
              System.out.println("Connection closed");
          }
      }
  }
  
  // Usage
  try (DatabaseConnection db = new DatabaseConnection("jdbc:mysql://localhost/test")) {
      db.executeQuery("SELECT * FROM users");
  } catch (SQLException e) {
      e.printStackTrace();
  }
  // close() is called automatically
  ```

  **Benefits:**
  + Automatic resource management
  + Cleaner, more readable code
  + Prevents resource leaks
  + Resources closed in reverse order of creation
  + Suppressed exceptions are available via `getSuppressed()`

  **Suppressed Exceptions:**
  ```java
  try (MyResource resource = new MyResource()) {
      throw new Exception("Exception in try block");
  } catch (Exception e) {
      System.out.println("Main exception: " + e.getMessage());
      
      // If close() also throws exception, it's suppressed
      Throwable[] suppressed = e.getSuppressed();
      for (Throwable t : suppressed) {
          System.out.println("Suppressed: " + t.getMessage());
      }
  }
  ```
  
</details>

<details>
  <summary>Custom Exceptions</summary>
  <br/>

  Creating custom exceptions helps provide more specific error information and better error handling.

  **Creating Custom Checked Exception:**
  ```java
  public class InsufficientFundsException extends Exception {
      private double amount;
      
      public InsufficientFundsException(double amount) {
          super("Insufficient funds. Required: " + amount);
          this.amount = amount;
      }
      
      public InsufficientFundsException(String message, double amount) {
          super(message);
          this.amount = amount;
      }
      
      public double getAmount() {
          return amount;
      }
  }
  
  // Usage
  public void withdraw(double amount) throws InsufficientFundsException {
      if (balance < amount) {
          throw new InsufficientFundsException(amount);
      }
      balance -= amount;
  }
  ```

  **Creating Custom Unchecked Exception:**
  ```java
  public class InvalidUserException extends RuntimeException {
      private String userId;
      
      public InvalidUserException(String userId) {
          super("Invalid user: " + userId);
          this.userId = userId;
      }
      
      public InvalidUserException(String message, String userId) {
          super(message);
          this.userId = userId;
      }
      
      public InvalidUserException(String message, Throwable cause, String userId) {
          super(message, cause);
          this.userId = userId;
      }
      
      public String getUserId() {
          return userId;
      }
  }
  
  // Usage
  public User findUser(String userId) {
      User user = userRepository.findById(userId);
      if (user == null) {
          throw new InvalidUserException(userId);
      }
      return user;
  }
  ```

  **Best Practices for Custom Exceptions:**

  **1. Provide Multiple Constructors:**
  ```java
  public class BusinessException extends Exception {
      public BusinessException() {
          super();
      }
      
      public BusinessException(String message) {
          super(message);
      }
      
      public BusinessException(String message, Throwable cause) {
          super(message, cause);
      }
      
      public BusinessException(Throwable cause) {
          super(cause);
      }
  }
  ```

  **2. Include Relevant Context:**
  ```java
  public class OrderProcessingException extends Exception {
      private String orderId;
      private String customerId;
      private OrderStatus status;
      
      public OrderProcessingException(String message, String orderId, 
                                       String customerId, OrderStatus status) {
          super(message);
          this.orderId = orderId;
          this.customerId = customerId;
          this.status = status;
      }
      
      // Getters
      public String getOrderId() { return orderId; }
      public String getCustomerId() { return customerId; }
      public OrderStatus getStatus() { return status; }
  }
  ```

  **3. Use Meaningful Names:**
  ```java
  // Good
  public class PaymentFailedException extends Exception { }
  public class InvalidEmailFormatException extends RuntimeException { }
  public class DatabaseConnectionException extends Exception { }
  
  // Bad
  public class MyException extends Exception { }
  public class ErrorException extends Exception { }
  public class Exception1 extends Exception { }
  ```

  **4. Document Exceptions:**
  ```java
  /**
   * Thrown when a user attempts to access a resource they don't have permission for.
   * 
   * @author Your Name
   * @since 1.0
   */
  public class UnauthorizedAccessException extends RuntimeException {
      /**
       * Creates a new UnauthorizedAccessException.
       * 
       * @param userId the ID of the user attempting access
       * @param resourceId the ID of the resource being accessed
       */
      public UnauthorizedAccessException(String userId, String resourceId) {
          super(String.format("User %s is not authorized to access resource %s", 
                              userId, resourceId));
      }
  }
  ```

  **Practical Example - Exception Hierarchy:**
  ```java
  // Base exception for application
  public class ApplicationException extends Exception {
      public ApplicationException(String message) {
          super(message);
      }
      
      public ApplicationException(String message, Throwable cause) {
          super(message, cause);
      }
  }
  
  // Specific exceptions
  public class ValidationException extends ApplicationException {
      private Map<String, String> errors;
      
      public ValidationException(String message, Map<String, String> errors) {
          super(message);
          this.errors = errors;
      }
      
      public Map<String, String> getErrors() {
          return errors;
      }
  }
  
  public class ResourceNotFoundException extends ApplicationException {
      private String resourceType;
      private String resourceId;
      
      public ResourceNotFoundException(String resourceType, String resourceId) {
          super(String.format("%s not found with id: %s", resourceType, resourceId));
          this.resourceType = resourceType;
          this.resourceId = resourceId;
      }
      
      public String getResourceType() { return resourceType; }
      public String getResourceId() { return resourceId; }
  }
  
  // Usage
  public User getUser(String userId) throws ResourceNotFoundException {
      User user = userRepository.findById(userId);
      if (user == null) {
          throw new ResourceNotFoundException("User", userId);
      }
      return user;
  }
  ```
  
</details>

<details>
  <summary>Exception Handling Best Practices</summary>
  <br/>

  **DO:**
  + ✅ Catch specific exceptions rather than generic Exception
  + ✅ Log exceptions with context information
  + ✅ Clean up resources in finally or use try-with-resources
  + ✅ Provide meaningful error messages
  + ✅ Document exceptions in JavaDoc with @throws
  + ✅ Preserve the original exception when rethrowing

  **DON'T:**
  + ❌ Don't catch Exception or Throwable unless absolutely necessary
  + ❌ Don't swallow exceptions (empty catch blocks)
  + ❌ Don't use exceptions for flow control
  + ❌ Don't log and rethrow the same exception
  + ❌ Don't return null instead of throwing exception

  **Good Examples:**
  ```java
  // Good: Specific exception handling
  public User findUser(String userId) {
      try {
          return userRepository.findById(userId);
      } catch (DataAccessException e) {
          logger.error("Failed to find user: {}", userId, e);
          throw new UserNotFoundException("User not found: " + userId, e);
      }
  }
  
  // Good: Preserve original exception
  public void processData(String data) throws ProcessingException {
      try {
          // Process data
      } catch (IOException e) {
          throw new ProcessingException("Failed to process data", e);
      }
  }
  
  // Good: Try-with-resources
  public String readFile(String path) throws IOException {
      try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
          return reader.lines().collect(Collectors.joining("\n"));
      }
  }
  ```

  **Bad Examples:**
  ```java
  // Bad: Catching generic Exception
  try {
      processData();
  } catch (Exception e) {  // Too broad
      // Handle
  }
  
  // Bad: Swallowing exceptions
  try {
      riskyOperation();
  } catch (Exception e) {
      // Empty catch - exception is lost
  }
  
  // Bad: Using exceptions for flow control
  try {
      int value = Integer.parseInt(input);
  } catch (NumberFormatException e) {
      value = 0;  // Use validation instead
  }
  
  // Bad: Log and rethrow
  try {
      processData();
  } catch (IOException e) {
      logger.error("Error", e);  // Logged here
      throw e;  // And will be logged again by caller
  }
  
  // Bad: Returning null
  public User findUser(String id) {
      try {
          return userRepository.findById(id);
      } catch (Exception e) {
          return null;  // Throw exception instead
      }
  }
  ```

  **Exception Translation:**
  ```java
  // Translate low-level exceptions to high-level exceptions
  public class UserService {
      public User getUser(String userId) throws UserServiceException {
          try {
              return userRepository.findById(userId);
          } catch (SQLException e) {
              // Translate SQLException to UserServiceException
              throw new UserServiceException(
                  "Failed to retrieve user: " + userId, e);
          }
      }
  }
  ```

  **Fail Fast Principle:**
  ```java
  // Good: Validate early
  public void processOrder(Order order) {
      if (order == null) {
          throw new IllegalArgumentException("Order cannot be null");
      }
      if (order.getItems().isEmpty()) {
          throw new IllegalArgumentException("Order must have at least one item");
      }
      // Process order
  }
  ```
  
</details>

<details>
  <summary>Common Exception Scenarios</summary>
  <br/>

  **Null Pointer Prevention:**
  ```java
  // Bad
  public String getUserName(User user) {
      return user.getName();  // NullPointerException if user is null
  }
  
  // Good: Validate
  public String getUserName(User user) {
      if (user == null) {
          throw new IllegalArgumentException("User cannot be null");
      }
      return user.getName();
  }
  
  // Good: Use Optional
  public Optional<String> getUserName(User user) {
      return Optional.ofNullable(user)
                     .map(User::getName);
  }
  ```

  **Resource Management:**
  ```java
  // Good: Try-with-resources
  public void copyFile(String source, String destination) throws IOException {
      try (InputStream in = new FileInputStream(source);
           OutputStream out = new FileOutputStream(destination)) {
          
          byte[] buffer = new byte[1024];
          int length;
          while ((length = in.read(buffer)) > 0) {
              out.write(buffer, 0, length);
          }
      }
  }
  ```

  **Retrying Operations:**
  ```java
  public void connectWithRetry(String url, int maxRetries) throws ConnectionException {
      int attempts = 0;
      Exception lastException = null;
      
      while (attempts < maxRetries) {
          try {
              connect(url);
              return;  // Success
          } catch (ConnectionException e) {
              lastException = e;
              attempts++;
              if (attempts < maxRetries) {
                  try {
                      Thread.sleep(1000 * attempts);  // Exponential backoff
                  } catch (InterruptedException ie) {
                      Thread.currentThread().interrupt();
                      throw new ConnectionException("Interrupted during retry", ie);
                  }
              }
          }
      }
      
      throw new ConnectionException(
          "Failed after " + maxRetries + " attempts", lastException);
  }
  ```

  **Validation with Multiple Errors:**
  ```java
  public void validateUser(User user) throws ValidationException {
      Map<String, String> errors = new HashMap<>();
      
      if (user.getName() == null || user.getName().isEmpty()) {
          errors.put("name", "Name is required");
      }
      
      if (user.getEmail() == null || !user.getEmail().contains("@")) {
          errors.put("email", "Valid email is required");
      }
      
      if (user.getAge() < 18) {
          errors.put("age", "Must be 18 or older");
      }
      
      if (!errors.isEmpty()) {
          throw new ValidationException("Validation failed", errors);
      }
  }
  ```
  
</details>
