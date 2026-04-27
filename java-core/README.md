# Java Language Features - Interview Preparation Guide

## Java 8+ Features

### Lambda Expressions

<details>
  <summary>What is a Lambda Expression?</summary>
  <br/>

  A lambda expression is a concise way to represent an anonymous function that can be passed around. It enables functional programming in Java.

  **Syntax:**
  ```java
  (parameters) -> expression
  // or
  (parameters) -> { statements; }
  ```

  **Examples:**
  ```java
  // No parameters
  () -> System.out.println("Hello World")
  
  // Single parameter (parentheses optional)
  x -> x * x
  
  // Multiple parameters
  (x, y) -> x + y
  
  // Multiple statements
  (x, y) -> {
      int sum = x + y;
      return sum;
  }
  ```

  **Benefits:**
  - Reduces boilerplate code
  - Enables functional programming style
  - Makes code more readable and maintainable
  - Works seamlessly with Stream API
  
</details>

<details>
  <summary>Functional Interfaces</summary>
  <br/>

  A functional interface is an interface with exactly one abstract method. It can have multiple default or static methods.

  **@FunctionalInterface Annotation:**
  ```java
  @FunctionalInterface
  public interface Calculator {
      int calculate(int a, int b);
      
      // Default methods are allowed
      default void printResult(int result) {
          System.out.println("Result: " + result);
      }
      
      // Static methods are allowed
      static void info() {
          System.out.println("Calculator interface");
      }
  }
  ```

  **Common Built-in Functional Interfaces:**

  + **Predicate<T>**: `boolean test(T t)` - Tests a condition
  + **Function<T,R>**: `R apply(T t)` - Transforms input to output
  + **Consumer<T>**: `void accept(T t)` - Consumes input, no return value
  + **Supplier<T>**: `T get()` - Supplies a value
  + **BiFunction<T,U,R>**: `R apply(T t, U u)` - Two inputs, one output
  + **UnaryOperator<T>**: `T apply(T t)` - Same type in and out
  + **BinaryOperator<T>**: `T apply(T t1, T t2)` - Two same type inputs

  **Usage Example:**
  ```java
  // Predicate
  Predicate<Integer> isEven = num -> num % 2 == 0;
  System.out.println(isEven.test(4)); // true
  
  // Function
  Function<String, Integer> stringLength = str -> str.length();
  System.out.println(stringLength.apply("Hello")); // 5
  
  // Consumer
  Consumer<String> printer = msg -> System.out.println(msg);
  printer.accept("Hello World");
  
  // Supplier
  Supplier<Double> randomValue = () -> Math.random();
  System.out.println(randomValue.get());
  ```
  
</details>

<details>
  <summary>Method References</summary>
  <br/>

  Method references are shorthand notation for lambda expressions that only call an existing method.

  **Types of Method References:**

  + **Static Method**: `ClassName::staticMethod` → `(args) -> ClassName.staticMethod(args)`
  + **Instance Method of Object**: `object::instanceMethod` → `(args) -> object.instanceMethod(args)`
  + **Instance Method of Arbitrary Object**: `ClassName::instanceMethod` → `(obj, args) -> obj.instanceMethod(args)`
  + **Constructor**: `ClassName::new` → `(args) -> new ClassName(args)`

  **Examples:**
  ```java
  // Static method reference
  List<Integer> numbers = Arrays.asList(-3, -2, -1, 0, 1, 2, 3);
  numbers.stream()
         .map(Math::abs)  // Instead of: x -> Math.abs(x)
         .forEach(System.out::println);
  
  // Instance method reference
  String str = "hello";
  Supplier<String> upperCase = str::toUpperCase;  // Instead of: () -> str.toUpperCase()
  
  // Instance method of arbitrary object
  List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
  names.stream()
       .map(String::toUpperCase)  // Instead of: s -> s.toUpperCase()
       .collect(Collectors.toList());
  
  // Constructor reference
  Supplier<List<String>> listSupplier = ArrayList::new;  // Instead of: () -> new ArrayList<>()
  List<String> list = listSupplier.get();
  ```
  
</details>

<details>
  <summary>Effectively Final Variables</summary>
  <br/>

  Lambda expressions can only access local variables that are final or effectively final.

  **Effectively Final:** A variable that is not explicitly declared as `final` but whose value is never changed after initialization.

  **Why This Restriction?**
  + **Closure Semantics:** Lambdas capture variables from enclosing scope and need consistent values
  + **Thread Safety:** Prevents race conditions in multi-threaded environments
  + **Variable Lifetime:** Captured variables must outlive their scope

  **Valid Example:**
  ```java
  public void processData() {
      int multiplier = 10;  // Effectively final
      
      List<Integer> numbers = Arrays.asList(1, 2, 3);
      numbers.stream()
             .map(n -> n * multiplier)  // Can access multiplier
             .forEach(System.out::println);
  }
  ```

  **Invalid Example:**
  ```java
  public void processData() {
      int multiplier = 10;
      
      List<Integer> numbers = Arrays.asList(1, 2, 3);
      numbers.stream()
             .map(n -> n * multiplier)  // Compilation error!
             .forEach(System.out::println);
      
      multiplier = 20;  // Variable is modified, not effectively final
  }
  ```

  **Workaround for Mutable State:**
  ```java
  // Use array
  int[] counter = {0};
  list.forEach(item -> counter[0]++);
  
  // Or AtomicInteger
  AtomicInteger counter = new AtomicInteger(0);
  list.forEach(item -> counter.incrementAndGet());
  ```
  
</details>

<details>
  <summary>Lambda Expression as Variable, Parameter, Return Type</summary>
  <br/>

  Lambda expressions can be used as variables, method parameters, and return types.

  **Lambda as a Variable:**
  ```java
  @FunctionalInterface
  interface StringOperation {
      String apply(String input);
  }
  
  public static void main(String[] args) {
      StringOperation toLowerCase = (input) -> input.toLowerCase();
      System.out.println(toLowerCase.apply("HELLO"));  // Output: hello
  }
  ```

  **Lambda as a Parameter:**
  ```java
  @FunctionalInterface
  interface Filter<T> {
      boolean test(T t);
  }
  
  public static List<String> filterNames(List<String> names, Filter<String> filter) {
      return names.stream()
                  .filter(name -> filter.test(name))
                  .collect(Collectors.toList());
  }
  
  public static void main(String[] args) {
      List<String> names = Arrays.asList("John", "Jane", "Jack", "Doe");
      List<String> filteredNames = filterNames(names, name -> name.startsWith("J"));
  }
  ```

  **Lambda as a Return Type:**
  ```java
  @FunctionalInterface
  interface ArithmeticOperation {
      int operate(int a, int b);
  }
  
  public static ArithmeticOperation getOperation(String operationType) {
      switch (operationType) {
          case "add":
              return (a, b) -> a + b;
          case "subtract":
              return (a, b) -> a - b;
          case "multiply":
              return (a, b) -> a * b;
          default:
              throw new IllegalArgumentException("Unknown operation");
      }
  }
  
  public static void main(String[] args) {
      ArithmeticOperation addOp = getOperation("add");
      System.out.println(addOp.operate(5, 3));  // Output: 8
  }
  ```
  
</details>

<details>
  <summary>Handle Exceptions in Lambda</summary>
  <br/>

  **Unchecked Exceptions (RuntimeException):**

  If the exception thrown inside the lambda is an unchecked exception, it can be propagated directly outside the lambda function.

  ```java
  public static void main(String[] args) {
      List<String> items = Arrays.asList("1", "2", "a", "4");

      try {
          items.forEach(item -> {
              int number = Integer.parseInt(item);  // Might throw NumberFormatException
              System.out.println("Parsed number: " + number);
          });
      } catch (NumberFormatException e) {
          System.out.println("Exception caught outside: " + e.getMessage());
      }
  }
  ```

  **Checked Exceptions:**

  If the exception inside the lambda is a checked exception, you cannot throw it directly. You need to wrap it in a RuntimeException.

  ```java
  public static void main(String[] args) {
      List<String> files = Arrays.asList("file1.txt", "file2.txt", "file3.txt");

      try {
          files.forEach(file -> {
              try {
                  readFile(file);  // Might throw IOException (checked exception)
              } catch (Exception e) {
                  throw new RuntimeException("Checked exception wrapped", e);
              }
          });
      } catch (RuntimeException e) {
          System.out.println("Exception caught: " + e.getCause().getMessage());
      }
  }
  ```
  
</details>

<details>
  <summary>Anonymous Class vs Lambda Expressions</summary>
  <br/>

  **Anonymous Class:**
  + Can implement interfaces with multiple abstract methods or extend a class
  + `this` refers to the anonymous class instance
  + Can access only final or effectively final variables
  + Generates a separate class file for each instance
  + Traditional object-oriented programming style

  **Lambda Expressions:**
  + Can only be used with functional interfaces (single abstract method)
  + `this` refers to the enclosing class instance
  + Can access only final or effectively final variables
  + More efficient, leveraging `invokedynamic` for lightweight execution
  + Functional programming style

  **Example Comparison:**
  ```java
  // Anonymous Class
  Runnable r1 = new Runnable() {
      @Override
      public void run() {
          System.out.println("Hello from anonymous class");
      }
  };
  
  // Lambda Expression
  Runnable r2 = () -> System.out.println("Hello from lambda");
  ```
  
</details>

<details>
  <summary>Stateless vs Stateful Lambda</summary>
  <br/>

  **Stateless Lambdas:**

  By default, lambdas are expected to be stateless, meaning they don't maintain or modify state outside their scope.

  _Example of a stateless lambda:_
  ```java
  List<Integer> numbers = Arrays.asList(1, 2, 3);
  numbers.forEach(n -> System.out.println(n * 2));  // Stateless operation
  ```

  **Stateful Lambdas (Potential Pitfall):**

  Although lambdas are designed to be stateless, it's possible to introduce state if the lambda captures variables or interacts with external mutable objects. This can lead to unexpected behavior, especially in multi-threaded environments.

  ```java
  int[] sum = {0};  // Mutable array
  List<Integer> numbers = Arrays.asList(1, 2, 3);
  numbers.forEach(n -> sum[0] += n);  // Stateful lambda, modifies external state
  System.out.println(sum[0]);  // Outputs: 6
  ```

  **Best Practice:** Avoid stateful lambdas when possible, especially with parallel streams.
  
</details>


### Stream API

<details>
  <summary>What is Stream API?</summary>
  <br/>

  Stream API provides a functional approach to process collections of objects. It allows you to express complex data processing queries in a declarative way.

  **Key Characteristics:**
  + **Not a data structure:** Streams don't store data
  + **Functional:** Operations don't modify the source
  + **Lazy evaluation:** Intermediate operations are not executed until terminal operation is called
  + **Possibly unbounded:** Can work with infinite streams
  + **Consumable:** Can only be traversed once

  **Creating Streams:**
  ```java
  // From collection
  List<String> list = Arrays.asList("a", "b", "c");
  Stream<String> stream1 = list.stream();
  
  // From array
  String[] array = {"a", "b", "c"};
  Stream<String> stream2 = Arrays.stream(array);
  Stream<String> stream3 = Stream.of("a", "b", "c");
  
  // From values
  Stream<Integer> stream4 = Stream.of(1, 2, 3);
  
  // Infinite streams
  Stream<Integer> infiniteStream = Stream.iterate(0, n -> n + 1);
  Stream<Double> randomStream = Stream.generate(Math::random);
  
  // From file
  Stream<String> lines = Files.lines(Paths.get("file.txt"));
  
  // Primitive streams
  IntStream intStream = IntStream.range(1, 10);
  LongStream longStream = LongStream.rangeClosed(1, 100);
  DoubleStream doubleStream = DoubleStream.of(1.0, 2.0, 3.0);
  ```
  
</details>

<details>
  <summary>Intermediate Operations</summary>
  <br/>

  Intermediate operations return a new stream and are lazy (not executed until terminal operation).

  **Common Intermediate Operations:**

  + **filter(Predicate)**: Filters elements based on a condition
  + **map(Function)**: Transforms each element
  + **flatMap(Function)**: Flattens nested streams into a single stream
  + **distinct()**: Removes duplicate elements
  + **sorted()**: Sorts elements in natural order
  + **sorted(Comparator)**: Sorts elements using a comparator
  + **peek(Consumer)**: Performs an action on each element without modifying the stream
  + **limit(long)**: Limits the stream to n elements
  + **skip(long)**: Skips the first n elements
  + **takeWhile(Predicate)**: Takes elements while condition is true (Java 9+)
  + **dropWhile(Predicate)**: Drops elements while condition is true (Java 9+)

  **Examples:**
  ```java
  List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
  
  // Filter even numbers and multiply by 2
  List<Integer> result = numbers.stream()
      .filter(n -> n % 2 == 0)
      .map(n -> n * 2)
      .collect(Collectors.toList());
  // Result: [4, 8, 12, 16, 20]
  
  // Remove duplicates and sort
  List<Integer> numbers2 = Arrays.asList(5, 3, 8, 3, 1, 9, 1);
  List<Integer> sorted = numbers2.stream()
      .distinct()
      .sorted()
      .collect(Collectors.toList());
  // Result: [1, 3, 5, 8, 9]
  
  // Peek for debugging
  List<Integer> result2 = numbers.stream()
      .peek(n -> System.out.println("Before filter: " + n))
      .filter(n -> n > 5)
      .peek(n -> System.out.println("After filter: " + n))
      .collect(Collectors.toList());
  ```
  
</details>

<details>
  <summary>Terminal Operations</summary>
  <br/>

  Terminal operations produce a result or side-effect and close the stream.

  **Common Terminal Operations:**

  + **forEach(Consumer)**: Performs an action for each element
  + **collect(Collector)**: Collects elements into a collection
  + **reduce(BinaryOperator)**: Reduces elements to a single value
  + **count()**: Returns the count of elements
  + **anyMatch(Predicate)**: Returns true if any element matches
  + **allMatch(Predicate)**: Returns true if all elements match
  + **noneMatch(Predicate)**: Returns true if no elements match
  + **findFirst()**: Returns the first element
  + **findAny()**: Returns any element (useful in parallel streams)
  + **min(Comparator)**: Returns the minimum element
  + **max(Comparator)**: Returns the maximum element
  + **toArray()**: Converts stream to array

  **Examples:**
  ```java
  List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
  
  // Sum using reduce
  int sum = numbers.stream()
      .reduce(0, (a, b) -> a + b);
  // Or: .reduce(0, Integer::sum);
  
  // Check if any number > 3
  boolean hasLarge = numbers.stream()
      .anyMatch(n -> n > 3);  // true
  
  // Find first even number
  Optional<Integer> firstEven = numbers.stream()
      .filter(n -> n % 2 == 0)
      .findFirst();  // Optional[2]
  
  // Count elements
  long count = numbers.stream()
      .filter(n -> n > 2)
      .count();  // 3
  
  // Min and Max
  Optional<Integer> min = numbers.stream().min(Integer::compareTo);
  Optional<Integer> max = numbers.stream().max(Integer::compareTo);
  ```
  
</details>

<details>
  <summary>map() vs flatMap()</summary>
  <br/>

  **map():** Transforms each element to another object (one-to-one mapping)

  **flatMap():** Transforms each element to a stream and flattens all streams into one (one-to-many mapping)

  **Comparison:**
  ```java
  // map() - one-to-one
  List<String> words = Arrays.asList("Hello", "World");
  List<Integer> lengths = words.stream()
      .map(String::length)  // Stream<Integer>
      .collect(Collectors.toList());
  // Result: [5, 5]
  
  // flatMap() - one-to-many
  List<String> words2 = Arrays.asList("Hello", "World");
  List<String> letters = words2.stream()
      .map(word -> word.split(""))  // Stream<String[]>
      .flatMap(Arrays::stream)      // Stream<String> - flattened
      .distinct()
      .collect(Collectors.toList());
  // Result: [H, e, l, o, W, r, d]
  ```

  **Practical Example:**
  ```java
  class Person {
      String name;
      List<String> hobbies;
      // constructor, getters
  }
  
  List<Person> people = Arrays.asList(
      new Person("Alice", Arrays.asList("Reading", "Gaming")),
      new Person("Bob", Arrays.asList("Gaming", "Cooking"))
  );
  
  // Get all unique hobbies using flatMap
  List<String> allHobbies = people.stream()
      .flatMap(person -> person.getHobbies().stream())
      .distinct()
      .collect(Collectors.toList());
  // Result: [Reading, Gaming, Cooking]
  ```
  ## 📚 FlatMap Real-World Examples

Để hiểu rõ hơn về cách sử dụng `flatMap` trong các tình huống thực tế, xem các file ví dụ sau:

### [FlatMapRealWorldExamples.java](FlatMapRealWorldExamples.java)
### [FlatMapAdvancedExamples.java](FlatMapAdvancedExamples.java)
**Chạy ví dụ:**
```bash
# Compile
javac java-core/FlatMapRealWorldExamples.java
javac java-core/FlatMapAdvancedExamples.java

# Run
java -cp . com.example.flatmap.FlatMapRealWorldExamples
java -cp . com.example.flatmap.FlatMapAdvancedExamples
```

**Key Takeaways:**
- `flatMap` được sử dụng khi cần "làm phẳng" cấu trúc lồng nhau
- Rất hữu ích cho quan hệ one-to-many (1 Customer → nhiều Orders)
- Kết hợp với `filter`, `map`, `collect` để xử lý dữ liệu phức tạp
- Thường dùng với Optional để loại bỏ giá trị null/empty
- Hiệu quả trong việc duyệt cây/đồ thị và xử lý dữ liệu phân cấp
  
</details>

<details>
  <summary>Collectors</summary>
  <br/>

  Collectors are used with `collect()` terminal operation to accumulate stream elements into collections or other results.

  **Common Collectors:**

  ```java
  List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "Alice");
  
  // To List
  List<String> list = names.stream()
      .collect(Collectors.toList());
  
  // To Set (removes duplicates)
  Set<String> set = names.stream()
      .collect(Collectors.toSet());
  
  // To Map
  Map<String, Integer> nameLength = names.stream()
      .distinct()
      .collect(Collectors.toMap(
          name -> name,           // key
          name -> name.length()   // value
      ));
  
  // Joining strings
  String joined = names.stream()
      .collect(Collectors.joining(", "));
  // Result: "Alice, Bob, Charlie, Alice"
  
  // Counting
  long count = names.stream()
      .collect(Collectors.counting());
  
  // Grouping by
  Map<Integer, List<String>> byLength = names.stream()
      .collect(Collectors.groupingBy(String::length));
  // Result: {3=[Bob], 5=[Alice, Alice], 7=[Charlie]}
  
  // Partitioning by (boolean predicate)
  Map<Boolean, List<String>> partition = names.stream()
      .collect(Collectors.partitioningBy(name -> name.length() > 4));
  // Result: {false=[Bob], true=[Alice, Charlie, Alice]}
  
  // Summarizing
  IntSummaryStatistics stats = names.stream()
      .collect(Collectors.summarizingInt(String::length));
  // stats.getAverage(), getMax(), getMin(), getSum(), getCount()
  ```

  **Advanced Collectors:**
  ```java
  // Grouping and counting
  Map<Integer, Long> lengthCount = names.stream()
      .collect(Collectors.groupingBy(
          String::length,
          Collectors.counting()
      ));
  
  // Grouping and joining
  Map<Integer, String> lengthJoined = names.stream()
      .collect(Collectors.groupingBy(
          String::length,
          Collectors.joining(", ")
      ));
  
  // Grouping and mapping
  Map<Integer, List<String>> upperCaseByLength = names.stream()
      .collect(Collectors.groupingBy(
          String::length,
          Collectors.mapping(String::toUpperCase, Collectors.toList())
      ));
  ```
  
</details>

<details>
  <summary>Parallel Streams</summary>
  <br/>

  Parallel streams divide the work across multiple threads to leverage multi-core processors.

  **Creating Parallel Streams:**
  ```java
  // From collection
  List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
  numbers.parallelStream()
         .forEach(System.out::println);
  
  // Convert sequential to parallel
  numbers.stream()
         .parallel()
         .forEach(System.out::println);
  ```

  **When to Use Parallel Streams:**
  + ✅ Large datasets (thousands of elements)
  + ✅ CPU-intensive operations
  + ✅ Independent operations (no shared state)
  + ❌ Small datasets (overhead > benefit)
  + ❌ I/O operations (already waiting)
  + ❌ Operations with side effects

  **Example:**
  ```java
  List<Integer> numbers = IntStream.rangeClosed(1, 1000000)
      .boxed()
      .collect(Collectors.toList());
  
  // Sequential
  long start = System.currentTimeMillis();
  long sum = numbers.stream()
      .mapToLong(Integer::longValue)
      .sum();
  System.out.println("Sequential: " + (System.currentTimeMillis() - start) + "ms");
  
  // Parallel
  start = System.currentTimeMillis();
  sum = numbers.parallelStream()
      .mapToLong(Integer::longValue)
      .sum();
  System.out.println("Parallel: " + (System.currentTimeMillis() - start) + "ms");
  ```

  **Thread Pool:**
  + Parallel streams use `ForkJoinPool.commonPool()`
  + Default size: `Runtime.getRuntime().availableProcessors() - 1`
  + Shared across all parallel streams in the application
  
</details>

<details>
  <summary>Sorting in Stream</summary>
  <br/>

  **Sorting Primitives:**
  ```java
  List<Integer> numbers = Arrays.asList(5, 3, 8, 1, 9);
  
  // Natural order
  List<Integer> sorted = numbers.stream()
      .sorted()
      .collect(Collectors.toList());
  // Result: [1, 3, 5, 8, 9]
  
  // Reverse order
  List<Integer> reversed = numbers.stream()
      .sorted(Comparator.reverseOrder())
      .collect(Collectors.toList());
  // Result: [9, 8, 5, 3, 1]
  ```

  **Sorting Objects:**
  ```java
  class Person {
      String name;
      int age;
      LocalDate birthDate;
      // constructor, getters
  }
  
  List<Person> people = Arrays.asList(
      new Person("Alice", 30, LocalDate.of(1993, 5, 15)),
      new Person("Bob", 25, LocalDate.of(1998, 8, 20)),
      new Person("Charlie", 35, LocalDate.of(1988, 3, 10))
  );
  
  // Sort by age
  List<Person> sortedByAge = people.stream()
      .sorted(Comparator.comparing(Person::getAge))
      .collect(Collectors.toList());
  
  // Sort by name (reverse)
  List<Person> sortedByName = people.stream()
      .sorted(Comparator.comparing(Person::getName).reversed())
      .collect(Collectors.toList());
  
  // Sort by multiple fields
  List<Person> sorted = people.stream()
      .sorted(Comparator.comparing(Person::getAge)
                        .thenComparing(Person::getName))
      .collect(Collectors.toList());
  
  // Sort by date
  List<Person> sortedByDate = people.stream()
      .sorted(Comparator.comparing(Person::getBirthDate))
      .collect(Collectors.toList());
  ```
  
</details>

<details>
  <summary>Convert List to Map</summary>
  <br/>

  **Basic Conversion:**
  ```java
  class Person {
      String name;
      int age;
      // constructor, getters
  }
  
  List<Person> people = Arrays.asList(
      new Person("Alice", 30),
      new Person("Bob", 25),
      new Person("Charlie", 35)
  );
  
  // List to Map
  Map<String, Integer> nameToAge = people.stream()
      .collect(Collectors.toMap(
          Person::getName,    // Key mapper
          Person::getAge      // Value mapper
      ));
  // Result: {Alice=30, Bob=25, Charlie=35}
  ```

  **Handling Duplicate Keys:**
  ```java
  List<String> names = Arrays.asList("Alice", "Bob", "Alice", "Charlie");
  
  // Without handling duplicates - throws IllegalStateException
  // Map<String, Integer> map = names.stream()
  //     .collect(Collectors.toMap(name -> name, String::length));
  
  // With merge function (keep first)
  Map<String, Integer> map1 = names.stream()
      .collect(Collectors.toMap(
          name -> name,
          String::length,
          (existing, replacement) -> existing  // Keep existing
      ));
  
  // With merge function (keep last)
  Map<String, Integer> map2 = names.stream()
      .collect(Collectors.toMap(
          name -> name,
          String::length,
          (existing, replacement) -> replacement  // Keep replacement
      ));
  ```

  **Convert to Specific Map Type:**
  ```java
  // To TreeMap (sorted)
  Map<String, Integer> treeMap = people.stream()
      .collect(Collectors.toMap(
          Person::getName,
          Person::getAge,
          (e1, e2) -> e1,
          TreeMap::new
      ));
  
  // To LinkedHashMap (insertion order)
  Map<String, Integer> linkedMap = people.stream()
      .collect(Collectors.toMap(
          Person::getName,
          Person::getAge,
          (e1, e2) -> e1,
          LinkedHashMap::new
      ));
  ```
  
</details>

<details>
  <summary>Group Elements</summary>
  <br/>

  **Using Collectors.groupingBy:**
  ```java
  List<String> items = Arrays.asList("apple", "banana", "orange", "apple", "banana", "apple");
  
  // Group and count
  Map<String, Long> groupedItems = items.stream()
      .collect(Collectors.groupingBy(
          Function.identity(),
          Collectors.counting()
      ));
  // Result: {orange=1, banana=2, apple=3}
  
  // Group to list
  Map<String, List<String>> groupedToList = items.stream()
      .collect(Collectors.groupingBy(Function.identity()));
  // Result: {orange=[orange], banana=[banana, banana], apple=[apple, apple, apple]}
  ```

  **Grouping Objects:**
  ```java
  class Person {
      String name;
      String city;
      int age;
      // constructor, getters
  }
  
  List<Person> people = Arrays.asList(
      new Person("Alice", "NYC", 30),
      new Person("Bob", "LA", 25),
      new Person("Charlie", "NYC", 35),
      new Person("David", "LA", 28)
  );
  
  // Group by city
  Map<String, List<Person>> byCity = people.stream()
      .collect(Collectors.groupingBy(Person::getCity));
  // Result: {NYC=[Alice, Charlie], LA=[Bob, David]}
  
  // Group by city and count
  Map<String, Long> countByCity = people.stream()
      .collect(Collectors.groupingBy(
          Person::getCity,
          Collectors.counting()
      ));
  // Result: {NYC=2, LA=2}
  
  // Group by city and get names
  Map<String, List<String>> namesByCity = people.stream()
      .collect(Collectors.groupingBy(
          Person::getCity,
          Collectors.mapping(Person::getName, Collectors.toList())
      ));
  // Result: {NYC=[Alice, Charlie], LA=[Bob, David]}
  ```

  **Using Collectors.partitioningBy:**
  ```java
  List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
  
  // Partition by even/odd
  Map<Boolean, List<Integer>> partitioned = numbers.stream()
      .collect(Collectors.partitioningBy(n -> n % 2 == 0));
  // Result: {false=[1, 3, 5, 7, 9], true=[2, 4, 6, 8, 10]}
  
  // Partition people by age > 30
  Map<Boolean, List<Person>> partitionedPeople = people.stream()
      .collect(Collectors.partitioningBy(p -> p.getAge() > 30));
  ```
  
</details>

### Optional Class

<details>
  <summary>What is Optional?</summary>
  <br/>

  `Optional<T>` is a container object that may or may not contain a non-null value. It helps avoid `NullPointerException` and makes code more readable.

  **Purpose:**
  + Explicit handling of absence of value
  + Reduces null checks
  + Encourages better API design
  + Chainable operations

  **Creating Optional:**
  ```java
  // Empty Optional
  Optional<String> empty = Optional.empty();
  
  // Optional with non-null value
  Optional<String> opt = Optional.of("Hello");
  
  // Optional that may be null
  String str = null;
  Optional<String> optNullable = Optional.ofNullable(str);
  
  // Note: Optional.of(null) throws NullPointerException
  ```
  
</details>

<details>
  <summary>Optional Methods</summary>
  <br/>

  **Checking for Value:**
  ```java
  Optional<String> opt = Optional.of("Hello");
  
  // Check if value present
  if (opt.isPresent()) {
      System.out.println(opt.get());
  }
  
  // Java 11+: Check if empty
  if (opt.isEmpty()) {
      System.out.println("Empty");
  }
  ```

  **Getting Value:**
  ```java
  // get() - throws NoSuchElementException if empty
  String value = opt.get();
  
  // orElse() - returns default if empty
  String value = opt.orElse("Default");
  
  // orElseGet() - returns result of Supplier if empty (lazy)
  String value = opt.orElseGet(() -> "Default");
  
  // orElseThrow() - throws exception if empty
  String value = opt.orElseThrow(() -> new IllegalStateException("No value"));
  ```

  **Transforming Value:**
  ```java
  Optional<String> opt = Optional.of("hello");
  
  // map() - transforms value if present
  Optional<String> upper = opt.map(String::toUpperCase);
  // Result: Optional["HELLO"]
  
  // flatMap() - for nested Optionals
  Optional<String> result = opt.flatMap(s -> Optional.of(s.toUpperCase()));
  
  // filter() - filters based on predicate
  Optional<String> filtered = opt.filter(s -> s.length() > 3);
  // Result: Optional["hello"]
  ```

  **Consuming Value:**
  ```java
  // ifPresent() - executes if value present
  opt.ifPresent(value -> System.out.println(value));
  
  // Java 9+: ifPresentOrElse()
  opt.ifPresentOrElse(
      value -> System.out.println("Found: " + value),
      () -> System.out.println("Not found")
  );
  
  // Java 9+: or() - returns alternative Optional
  Optional<String> result = opt.or(() -> Optional.of("Alternative"));
  ```

  **Practical Example:**
  ```java
  public Optional<User> findUserById(Long id) {
      return Optional.ofNullable(userRepository.findById(id));
  }
  
  // Usage
  String userName = findUserById(1L)
      .map(User::getName)
      .map(String::toUpperCase)
      .orElse("Unknown");
  ```
  
</details>

<details>
  <summary>Optional Best Practices</summary>
  <br/>

  **DO:**
  + ✅ Use as return type for methods that might not return a value
  + ✅ Use `orElse()` for simple defaults
  + ✅ Use `orElseGet()` when default is expensive to create
  + ✅ Chain operations with `map()` and `flatMap()`
  + ✅ Use `filter()` to add conditions

  **DON'T:**
  + ❌ Don't use Optional for fields
  + ❌ Don't use Optional for method parameters
  + ❌ Don't use Optional for collections (return empty collection instead)
  + ❌ Don't call `get()` without checking `isPresent()`
  + ❌ Don't use Optional just to avoid null checks

  **Good Examples:**
  ```java
  // Good: Return Optional
  public Optional<User> findUser(String email) {
      return Optional.ofNullable(userMap.get(email));
  }
  
  // Good: Chain operations
  String result = findUser("test@example.com")
      .map(User::getProfile)
      .map(Profile::getDisplayName)
      .orElse("Guest");
  
  // Good: Use orElseGet for expensive operations
  User user = findUser(email)
      .orElseGet(() -> createDefaultUser());
  ```

  **Bad Examples:**
  ```java
  // Bad: Optional field
  public class User {
      private Optional<String> middleName;  // Don't do this
  }
  
  // Bad: Optional parameter
  public void setName(Optional<String> name) {  // Don't do this
      // ...
  }
  
  // Bad: Get without check
  Optional<String> opt = Optional.empty();
  String value = opt.get();  // Throws NoSuchElementException
  
  // Bad: Using Optional for collections
  public Optional<List<User>> getUsers() {  // Don't do this
      return Optional.ofNullable(users);
  }
  // Good: Return empty collection
  public List<User> getUsers() {
      return users != null ? users : Collections.emptyList();
  }
  ```
  
</details>

### Default and Static Methods in Interfaces

<details>
  <summary>Default Methods</summary>
  <br/>

  Default methods allow you to add new methods to interfaces without breaking existing implementations.

  **Syntax:**
  ```java
  public interface Vehicle {
      // Abstract method
      void start();
      
      // Default method
      default void stop() {
          System.out.println("Vehicle stopped");
      }
      
      default void honk() {
          System.out.println("Beep beep!");
      }
  }
  ```

  **Implementation:**
  ```java
  public class Car implements Vehicle {
      @Override
      public void start() {
          System.out.println("Car started");
      }
      
      // Can override default method
      @Override
      public void stop() {
          System.out.println("Car stopped with brakes");
      }
      
      // honk() is inherited from interface
  }
  
  // Usage
  Car car = new Car();
  car.start();  // Car started
  car.stop();   // Car stopped with brakes
  car.honk();   // Beep beep!
  ```

  **Why Default Methods?**
  + **Backward compatibility:** Add methods without breaking implementations
  + **Code reuse:** Share common implementation across implementations
  + **Evolution:** Interfaces can evolve over time

  **Multiple Inheritance Conflict:**
  ```java
  interface A {
      default void hello() {
          System.out.println("Hello from A");
      }
  }
  
  interface B {
      default void hello() {
          System.out.println("Hello from B");
      }
  }
  
  // Must override to resolve conflict
  class C implements A, B {
      @Override
      public void hello() {
          A.super.hello();  // Call specific interface method
          // Or provide own implementation
          System.out.println("Hello from C");
      }
  }
  ```
  
</details>

<details>
  <summary>Static Methods in Interfaces</summary>
  <br/>

  Static methods in interfaces are utility methods that belong to the interface, not to instances.

  **Syntax:**
  ```java
  public interface MathUtils {
      static int add(int a, int b) {
          return a + b;
      }
      
      static int multiply(int a, int b) {
          return a * b;
      }
      
      static double average(int... numbers) {
          return Arrays.stream(numbers).average().orElse(0.0);
      }
  }
  
  // Usage
  int sum = MathUtils.add(5, 3);  // 8
  int product = MathUtils.multiply(5, 3);  // 15
  double avg = MathUtils.average(1, 2, 3, 4, 5);  // 3.0
  ```

  **Characteristics:**
  + Cannot be overridden
  + Must be called using interface name
  + Not inherited by implementing classes
  + Can access only static members

  **Practical Example - Factory Methods:**
  ```java
  public interface StringValidator {
      boolean validate(String input);
      
      // Static factory method
      static StringValidator emailValidator() {
          return input -> input.contains("@") && input.contains(".");
      }
      
      static StringValidator lengthValidator(int minLength) {
          return input -> input.length() >= minLength;
      }
      
      static StringValidator notEmptyValidator() {
          return input -> input != null && !input.isEmpty();
      }
  }
  
  // Usage
  StringValidator emailVal = StringValidator.emailValidator();
  boolean isValid = emailVal.validate("test@example.com");  // true
  
  StringValidator lengthVal = StringValidator.lengthValidator(5);
  boolean isValid2 = lengthVal.validate("hello");  // true
  ```
  
</details>

<details>
  <summary>Default vs Static Methods</summary>
  <br/>

  **Default Methods:**
  + Can be overridden by implementing classes
  + Can access instance methods and fields (through `this`)
  + Inherited by implementing classes
  + Called on instance: `object.defaultMethod()`
  + Used for providing default behavior

  **Static Methods:**
  + Cannot be overridden
  + Cannot access instance methods or fields
  + Not inherited by implementing classes
  + Called on interface: `Interface.staticMethod()`
  + Used for utility methods

  **Example:**
  ```java
  public interface Calculator {
      // Abstract method
      int calculate(int a, int b);
      
      // Default method - can be overridden
      default void printResult(int result) {
          System.out.println("Result: " + result);
      }
      
      // Static method - cannot be overridden
      static boolean isPositive(int number) {
          return number > 0;
      }
  }
  
  class Addition implements Calculator {
      @Override
      public int calculate(int a, int b) {
          return a + b;
      }
      
      // Can override default method
      @Override
      public void printResult(int result) {
          System.out.println("Sum: " + result);
      }
      
      // Cannot override static method
  }
  
  // Usage
  Addition add = new Addition();
  int result = add.calculate(5, 3);
  add.printResult(result);  // Sum: 8
  
  boolean positive = Calculator.isPositive(result);  // true
  ```
  
</details>


---

## Java 11+ Features

<details>
  <summary>Local Variable Type Inference (var)</summary>
  <br/>

  The `var` keyword allows local variable type inference, reducing verbosity while maintaining type safety.

  **Basic Usage:**
  ```java
  // Before Java 10
  String message = "Hello";
  List<String> names = new ArrayList<>();
  Map<String, Integer> scores = new HashMap<>();
  
  // With var (Java 10+)
  var message = "Hello";  // Inferred as String
  var names = new ArrayList<String>();  // Inferred as ArrayList<String>
  var scores = new HashMap<String, Integer>();  // Inferred as HashMap<String, Integer>
  ```

  **Where var Can Be Used:**
  + ✅ Local variables with initializers
  + ✅ Loop variables
  + ✅ Try-with-resources

  **Where var Cannot Be Used:**
  + ❌ Method parameters
  + ❌ Method return types
  + ❌ Fields
  + ❌ Without initializer
  + ❌ Lambda parameters (before Java 11)

  **Examples:**
  ```java
  // Loop variables
  for (var i = 0; i < 10; i++) {
      System.out.println(i);
  }
  
  for (var name : names) {
      System.out.println(name);
  }
  
  // Try-with-resources
  try (var reader = new BufferedReader(new FileReader("file.txt"))) {
      var line = reader.readLine();
  }
  
  // Stream operations
  var result = list.stream()
      .filter(x -> x > 0)
      .collect(Collectors.toList());
  
  // Map entry
  var map = new HashMap<String, Integer>();
  for (var entry : map.entrySet()) {
      System.out.println(entry.getKey() + ": " + entry.getValue());
  }
  ```

  **Java 11: var in Lambda Parameters:**
  ```java
  // Java 11+ allows var in lambda parameters
  BiFunction<Integer, Integer, Integer> add = (var a, var b) -> a + b;
  
  // Useful for annotations
  list.stream()
      .map((@NonNull var x) -> x.toString())
      .collect(Collectors.toList());
  ```

  **Best Practices:**
  ```java
  // Good: Type is obvious
  var message = "Hello";
  var count = 10;
  var users = getUserList();
  var stream = list.stream();
  
  // Bad: Type is not clear
  var result = process();  // What type is this?
  var data = getData();    // Unclear
  
  // Good: Use explicit type when clarity needed
  InputStream stream = getStream();  // Better than var
  
  // Bad: Diamond operator without type
  var list = new ArrayList<>();  // Type is ArrayList<Object>
  
  // Good: Specify type parameter
  var list = new ArrayList<String>();  // Type is ArrayList<String>
  ```

  **Common Pitfalls:**
  ```java
  // Pitfall 1: Loses specific type
  var list = new ArrayList<String>();  // Type is ArrayList<String>, not List<String>
  
  // Pitfall 2: Cannot use with null
  var x = null;  // Compilation error
  
  // Pitfall 3: Cannot use without initializer
  var x;  // Compilation error
  x = 10;
  ```
  
</details>

<details>
  <summary>New String Methods (Java 11)</summary>
  <br/>

  Java 11 added several useful methods to the String class.

  **isBlank():**
  ```java
  String str1 = "";
  String str2 = "   ";
  String str3 = "Hello";
  
  str1.isBlank();  // true (empty)
  str2.isBlank();  // true (only whitespace)
  str3.isBlank();  // false
  
  // vs isEmpty()
  str2.isEmpty();  // false (has characters)
  str2.isBlank();  // true (only whitespace)
  ```

  **lines():**
  ```java
  String multiline = "Line 1\nLine 2\nLine 3";
  
  multiline.lines()
           .forEach(System.out::println);
  // Output:
  // Line 1
  // Line 2
  // Line 3
  
  // Count non-empty lines
  long count = multiline.lines()
                        .filter(line -> !line.isBlank())
                        .count();
  
  // Collect to list
  List<String> lineList = multiline.lines()
                                   .collect(Collectors.toList());
  ```

  **strip(), stripLeading(), stripTrailing():**
  ```java
  String str = "  Hello World  ";
  
  str.strip();          // "Hello World" (removes leading and trailing)
  str.stripLeading();   // "Hello World  " (removes leading only)
  str.stripTrailing();  // "  Hello World" (removes trailing only)
  
  // vs trim() - strip() handles Unicode whitespace better
  String unicode = "\u2000Hello\u2000";
  unicode.trim();   // Still has Unicode spaces
  unicode.strip();  // Properly removes Unicode spaces
  ```

  **repeat(int count):**
  ```java
  String str = "Java";
  
  str.repeat(3);  // "JavaJavaJava"
  
  // Practical use
  String separator = "=".repeat(50);
  System.out.println(separator);
  // ==================================================
  
  String indent = " ".repeat(4);
  System.out.println(indent + "Indented text");
  //     Indented text
  ```

  **Practical Examples:**
  ```java
  // Validate input
  public boolean isValidInput(String input) {
      return input != null && !input.isBlank();
  }
  
  // Process multi-line text
  public List<String> getNonEmptyLines(String text) {
      return text.lines()
                 .filter(line -> !line.isBlank())
                 .map(String::strip)
                 .collect(Collectors.toList());
  }
  
  // Create formatted output
  public String createBox(String content) {
      int length = content.length() + 4;
      String border = "*".repeat(length);
      return border + "\n* " + content + " *\n" + border;
  }
  ```
  
</details>

<details>
  <summary>HTTP Client (Java 11)</summary>
  <br/>

  Java 11 introduced a new HTTP Client API that supports HTTP/1.1 and HTTP/2, synchronous and asynchronous programming models.

  **Basic GET Request:**
  ```java
  // Create HTTP Client
  HttpClient client = HttpClient.newHttpClient();
  
  // Create request
  HttpRequest request = HttpRequest.newBuilder()
      .uri(URI.create("https://api.example.com/users"))
      .GET()
      .build();
  
  // Send synchronously
  HttpResponse<String> response = client.send(request, 
      HttpResponse.BodyHandlers.ofString());
  
  System.out.println("Status: " + response.statusCode());
  System.out.println("Body: " + response.body());
  ```

  **POST Request with JSON:**
  ```java
  String json = "{\"name\":\"John\",\"age\":30}";
  
  HttpRequest request = HttpRequest.newBuilder()
      .uri(URI.create("https://api.example.com/users"))
      .header("Content-Type", "application/json")
      .POST(HttpRequest.BodyPublishers.ofString(json))
      .build();
  
  HttpResponse<String> response = client.send(request,
      HttpResponse.BodyHandlers.ofString());
  ```

  **Asynchronous Request:**
  ```java
  HttpClient client = HttpClient.newHttpClient();
  
  HttpRequest request = HttpRequest.newBuilder()
      .uri(URI.create("https://api.example.com/users"))
      .build();
  
  // Send asynchronously
  client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
        .thenApply(HttpResponse::body)
        .thenAccept(System.out::println)
        .join();
  ```

  **Custom HTTP Client Configuration:**
  ```java
  HttpClient client = HttpClient.newBuilder()
      .version(HttpClient.Version.HTTP_2)
      .connectTimeout(Duration.ofSeconds(10))
      .followRedirects(HttpClient.Redirect.NORMAL)
      .build();
  ```

  **Different HTTP Methods:**
  ```java
  // GET
  HttpRequest getRequest = HttpRequest.newBuilder()
      .uri(URI.create("https://api.example.com/users/1"))
      .GET()
      .build();
  
  // POST
  HttpRequest postRequest = HttpRequest.newBuilder()
      .uri(URI.create("https://api.example.com/users"))
      .POST(HttpRequest.BodyPublishers.ofString(json))
      .build();
  
  // PUT
  HttpRequest putRequest = HttpRequest.newBuilder()
      .uri(URI.create("https://api.example.com/users/1"))
      .PUT(HttpRequest.BodyPublishers.ofString(json))
      .build();
  
  // DELETE
  HttpRequest deleteRequest = HttpRequest.newBuilder()
      .uri(URI.create("https://api.example.com/users/1"))
      .DELETE()
      .build();
  ```

  **Headers and Authentication:**
  ```java
  HttpRequest request = HttpRequest.newBuilder()
      .uri(URI.create("https://api.example.com/users"))
      .header("Content-Type", "application/json")
      .header("Authorization", "Bearer " + token)
      .header("User-Agent", "MyApp/1.0")
      .GET()
      .build();
  ```

  **Response Handling:**
  ```java
  // String response
  HttpResponse<String> response = client.send(request,
      HttpResponse.BodyHandlers.ofString());
  
  // File response
  HttpResponse<Path> response = client.send(request,
      HttpResponse.BodyHandlers.ofFile(Paths.get("output.txt")));
  
  // Stream response
  HttpResponse<InputStream> response = client.send(request,
      HttpResponse.BodyHandlers.ofInputStream());
  
  // Discard response body
  HttpResponse<Void> response = client.send(request,
      HttpResponse.BodyHandlers.discarding());
  ```

  **Multiple Asynchronous Requests:**
  ```java
  List<URI> uris = List.of(
      URI.create("https://api.example.com/users/1"),
      URI.create("https://api.example.com/users/2"),
      URI.create("https://api.example.com/users/3")
  );
  
  HttpClient client = HttpClient.newHttpClient();
  
  List<CompletableFuture<String>> futures = uris.stream()
      .map(uri -> HttpRequest.newBuilder(uri).build())
      .map(request -> client.sendAsync(request, HttpResponse.BodyHandlers.ofString()))
      .map(future -> future.thenApply(HttpResponse::body))
      .collect(Collectors.toList());
  
  // Wait for all to complete
  CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
      .join();
  
  // Get results
  List<String> results = futures.stream()
      .map(CompletableFuture::join)
      .collect(Collectors.toList());
  ```
  
</details>

## Java 17+ Features

<details>
  <summary>Records (Java 14 Preview, Java 16 Final)</summary>
  <br/>

  Records are a special kind of class designed to hold immutable data. They automatically generate constructor, getters, `equals()`, `hashCode()`, and `toString()` methods.

  **Basic Record:**
  ```java
  // Traditional class
  public class Person {
      private final String name;
      private final int age;
      
      public Person(String name, int age) {
          this.name = name;
          this.age = age;
      }
      
      public String getName() { return name; }
      public int getAge() { return age; }
      
      @Override
      public boolean equals(Object o) { /* ... */ }
      
      @Override
      public int hashCode() { /* ... */ }
      
      @Override
      public String toString() { /* ... */ }
  }
  
  // With Record (much simpler!)
  public record Person(String name, int age) {}
  ```

  **Usage:**
  ```java
  Person person = new Person("Alice", 30);
  
  System.out.println(person.name());  // Alice (not getName())
  System.out.println(person.age());   // 30
  System.out.println(person);         // Person[name=Alice, age=30]
  
  Person person2 = new Person("Alice", 30);
  System.out.println(person.equals(person2));  // true
  ```

  **Custom Constructor:**
  ```java
  public record Person(String name, int age) {
      // Compact constructor - validates parameters
      public Person {
          if (age < 0) {
              throw new IllegalArgumentException("Age cannot be negative");
          }
          if (name == null || name.isBlank()) {
              throw new IllegalArgumentException("Name cannot be blank");
          }
      }
  }
  
  // Alternative: Canonical constructor
  public record Person(String name, int age) {
      public Person(String name, int age) {
          this.name = name.trim();
          this.age = Math.max(age, 0);
      }
  }
  ```

  **Additional Methods:**
  ```java
  public record Person(String name, int age) {
      // Custom methods
      public boolean isAdult() {
          return age >= 18;
      }
      
      public String getDisplayName() {
          return name.toUpperCase();
      }
      
      // Static factory method
      public static Person of(String name, int age) {
          return new Person(name, age);
      }
  }
  ```

  **Records with Interfaces:**
  ```java
  public interface Identifiable {
      String getId();
  }
  
  public record User(String id, String name, String email) implements Identifiable {
      @Override
      public String getId() {
          return id;
      }
  }
  ```

  **Nested Records:**
  ```java
  public record Address(String street, String city, String zipCode) {}
  
  public record Person(String name, int age, Address address) {}
  
  // Usage
  Address address = new Address("123 Main St", "NYC", "10001");
  Person person = new Person("Alice", 30, address);
  System.out.println(person.address().city());  // NYC
  ```

  **Records in Collections:**
  ```java
  List<Person> people = List.of(
      new Person("Alice", 30),
      new Person("Bob", 25),
      new Person("Charlie", 35)
  );
  
  // Sorting
  List<Person> sorted = people.stream()
      .sorted(Comparator.comparing(Person::age))
      .collect(Collectors.toList());
  
  // Grouping
  Map<Integer, List<Person>> byAge = people.stream()
      .collect(Collectors.groupingBy(Person::age));
  ```

  **Limitations:**
  + Records are implicitly final (cannot be extended)
  + Cannot extend other classes (but can implement interfaces)
  + All fields are implicitly final
  + Cannot declare instance fields outside the record header
  
</details>

<details>
  <summary>Sealed Classes (Java 15 Preview, Java 17 Final)</summary>
  <br/>

  Sealed classes restrict which classes can extend or implement them, providing more control over inheritance hierarchy.

  **Basic Sealed Class:**
  ```java
  // Sealed class with permitted subclasses
  public sealed class Shape 
      permits Circle, Rectangle, Triangle {
      // Common methods
  }
  
  // Permitted subclasses must be final, sealed, or non-sealed
  public final class Circle extends Shape {
      private final double radius;
      // ...
  }
  
  public final class Rectangle extends Shape {
      private final double width;
      private final double height;
      // ...
  }
  
  public non-sealed class Triangle extends Shape {
      // non-sealed allows further extension
  }
  ```

  **Sealed Interfaces:**
  ```java
  public sealed interface Payment 
      permits CreditCardPayment, PayPalPayment, CashPayment {
      void process();
  }
  
  public final class CreditCardPayment implements Payment {
      @Override
      public void process() {
          System.out.println("Processing credit card payment");
      }
  }
  
  public final class PayPalPayment implements Payment {
      @Override
      public void process() {
          System.out.println("Processing PayPal payment");
      }
  }
  
  public final class CashPayment implements Payment {
      @Override
      public void process() {
          System.out.println("Processing cash payment");
      }
  }
  ```

  **Sealed Classes with Pattern Matching:**
  ```java
  public sealed interface Result<T> 
      permits Success, Failure {
  }
  
  public record Success<T>(T value) implements Result<T> {}
  public record Failure<T>(String error) implements Result<T> {}
  
  // Usage with pattern matching
  public static <T> void handleResult(Result<T> result) {
      switch (result) {
          case Success<T> s -> System.out.println("Success: " + s.value());
          case Failure<T> f -> System.out.println("Failure: " + f.error());
      }
  }
  ```

  **Why Use Sealed Classes?**
  + **Domain Modeling:** Represent a fixed set of alternatives
  + **Exhaustiveness:** Compiler can check all cases are handled
  + **Security:** Prevent unauthorized extensions
  + **API Design:** Control which classes can implement your interface

  **Example - HTTP Response:**
  ```java
  public sealed interface HttpResponse 
      permits SuccessResponse, ErrorResponse, RedirectResponse {
      int statusCode();
  }
  
  public record SuccessResponse(int statusCode, String body) 
      implements HttpResponse {}
  
  public record ErrorResponse(int statusCode, String message) 
      implements HttpResponse {}
  
  public record RedirectResponse(int statusCode, String location) 
      implements HttpResponse {}
  
  // Exhaustive switch
  public static void handleResponse(HttpResponse response) {
      switch (response) {
          case SuccessResponse s -> System.out.println("Success: " + s.body());
          case ErrorResponse e -> System.out.println("Error: " + e.message());
          case RedirectResponse r -> System.out.println("Redirect to: " + r.location());
      }
      // No default needed - compiler knows all cases are covered
  }
  ```
  
</details>

<details>
  <summary>Pattern Matching for instanceof (Java 16)</summary>
  <br/>

  Pattern matching simplifies the common pattern of testing and casting objects.

  **Before Java 16:**
  ```java
  public static void printLength(Object obj) {
      if (obj instanceof String) {
          String str = (String) obj;  // Explicit cast
          System.out.println(str.length());
      }
  }
  ```

  **With Pattern Matching:**
  ```java
  public static void printLength(Object obj) {
      if (obj instanceof String str) {  // Pattern variable
          System.out.println(str.length());  // No cast needed
      }
  }
  ```

  **Scope of Pattern Variable:**
  ```java
  public static void process(Object obj) {
      if (obj instanceof String str) {
          System.out.println(str.toUpperCase());  // str is in scope
      }
      // str is not in scope here
      
      // Works with logical AND
      if (obj instanceof String str && str.length() > 5) {
          System.out.println(str);  // str is in scope
      }
      
      // Works with negation
      if (!(obj instanceof String str)) {
          return;
      }
      System.out.println(str);  // str is in scope after the if
  }
  ```

  **Multiple Patterns:**
  ```java
  public static String format(Object obj) {
      if (obj instanceof Integer i) {
          return "Integer: " + i;
      } else if (obj instanceof String s) {
          return "String: " + s;
      } else if (obj instanceof List<?> list) {
          return "List of size: " + list.size();
      } else {
          return "Unknown type";
      }
  }
  ```

  **Pattern Matching in Switch (Java 17 Preview, Java 21 Final):**
  ```java
  public static String formatValue(Object obj) {
      return switch (obj) {
          case Integer i -> "Integer: " + i;
          case String s -> "String: " + s;
          case List<?> list -> "List of size: " + list.size();
          case null -> "Null value";
          default -> "Unknown type";
      };
  }
  ```

  **Guarded Patterns:**
  ```java
  public static String describe(Object obj) {
      return switch (obj) {
          case String s when s.length() > 10 -> "Long string";
          case String s -> "Short string";
          case Integer i when i > 0 -> "Positive integer";
          case Integer i -> "Non-positive integer";
          default -> "Other type";
      };
  }
  ```

  **Practical Example:**
  ```java
  public static double calculateArea(Shape shape) {
      if (shape instanceof Circle c) {
          return Math.PI * c.radius() * c.radius();
      } else if (shape instanceof Rectangle r) {
          return r.width() * r.height();
      } else if (shape instanceof Triangle t) {
          return 0.5 * t.base() * t.height();
      }
      throw new IllegalArgumentException("Unknown shape");
  }
  
  // Or with switch (Java 17+)
  public static double calculateArea(Shape shape) {
      return switch (shape) {
          case Circle c -> Math.PI * c.radius() * c.radius();
          case Rectangle r -> r.width() * r.height();
          case Triangle t -> 0.5 * t.base() * t.height();
      };
  }
  ```
  
</details>

<details>
  <summary>Text Blocks (Java 13 Preview, Java 15 Final)</summary>
  <br/>

  Text blocks provide a cleaner way to write multi-line strings without escape sequences.

  **Basic Syntax:**
  ```java
  // Before text blocks
  String html = "<html>\n" +
                "  <body>\n" +
                "    <p>Hello, World!</p>\n" +
                "  </body>\n" +
                "</html>";
  
  // With text blocks
  String html = """
      <html>
        <body>
          <p>Hello, World!</p>
        </body>
      </html>
      """;
  ```

  **JSON Example:**
  ```java
  // Before
  String json = "{\n" +
                "  \"name\": \"John\",\n" +
                "  \"age\": 30,\n" +
                "  \"city\": \"New York\"\n" +
                "}";
  
  // With text blocks
  String json = """
      {
        "name": "John",
        "age": 30,
        "city": "New York"
      }
      """;
  ```

  **SQL Query:**
  ```java
  String query = """
      SELECT u.id, u.name, u.email
      FROM users u
      WHERE u.age > 18
        AND u.active = true
      ORDER BY u.name
      """;
  ```

  **Indentation:**
  ```java
  // Indentation is relative to the closing delimiter
  String text = """
          Line 1
          Line 2
          Line 3
          """;
  // Result: "Line 1\nLine 2\nLine 3\n"
  
  // Closing delimiter determines base indentation
  String text2 = """
      Line 1
      Line 2
      """;
  // Result: "Line 1\nLine 2\n"
  ```

  **Escape Sequences:**
  ```java
  // New line at end (default)
  String text1 = """
      Hello
      World
      """;
  // Result: "Hello\nWorld\n"
  
  // No new line at end (use \)
  String text2 = """
      Hello
      World\
      """;
  // Result: "Hello\nWorld"
  
  // Preserve trailing spaces (use \s)
  String text3 = """
      Hello   \s
      World
      """;
  // Result: "Hello   \nWorld\n"
  ```

  **String Interpolation (using formatted()):**
  ```java
  String name = "Alice";
  int age = 30;
  
  String message = """
      Hello, %s!
      You are %d years old.
      """.formatted(name, age);
  
  // Or with String.format()
  String message2 = String.format("""
      Hello, %s!
      You are %d years old.
      """, name, age);
  ```

  **Practical Examples:**
  ```java
  // HTML Template
  public String generateHtml(String title, String content) {
      return """
          <!DOCTYPE html>
          <html>
          <head>
              <title>%s</title>
          </head>
          <body>
              <div class="content">
                  %s
              </div>
          </body>
          </html>
          """.formatted(title, content);
  }
  
  // SQL with parameters
  public String buildQuery(String tableName, List<String> columns) {
      String columnList = String.join(", ", columns);
      return """
          SELECT %s
          FROM %s
          WHERE active = true
          ORDER BY created_at DESC
          """.formatted(columnList, tableName);
  }
  
  // Test data
  String testJson = """
      {
        "users": [
          {"id": 1, "name": "Alice"},
          {"id": 2, "name": "Bob"}
        ]
      }
      """;
  ```

  **Benefits:**
  + No need for escape sequences for quotes and newlines
  + Better readability for multi-line strings
  + Automatic indentation management
  + Easier to maintain
  
</details>

<details>
  <summary>Pattern Matching for switch (Java 17 Preview, Java 21 Final)</summary>
  <br/>

  Enhanced switch expressions with pattern matching provide more powerful and expressive switch statements.

  **Basic Pattern Matching:**
  ```java
  // Traditional switch
  public static String formatOld(Object obj) {
      String result;
      if (obj instanceof Integer) {
          result = "Integer: " + obj;
      } else if (obj instanceof String) {
          result = "String: " + obj;
      } else {
          result = "Unknown";
      }
      return result;
  }
  
  // Pattern matching switch
  public static String format(Object obj) {
      return switch (obj) {
          case Integer i -> "Integer: " + i;
          case String s -> "String: " + s;
          case null -> "Null value";
          default -> "Unknown";
      };
  }
  ```

  **Guarded Patterns (when clause):**
  ```java
  public static String describe(Object obj) {
      return switch (obj) {
          case String s when s.isEmpty() -> "Empty string";
          case String s when s.length() < 10 -> "Short string: " + s;
          case String s -> "Long string: " + s.substring(0, 10) + "...";
          case Integer i when i > 0 -> "Positive: " + i;
          case Integer i when i < 0 -> "Negative: " + i;
          case Integer i -> "Zero";
          case null -> "Null";
          default -> "Other type";
      };
  }
  ```

  **Pattern Matching with Records:**
  ```java
  record Point(int x, int y) {}
  record Circle(Point center, int radius) {}
  record Rectangle(Point topLeft, Point bottomRight) {}
  
  public static String describe(Object shape) {
      return switch (shape) {
          case Circle(Point(int x, int y), int r) -> 
              "Circle at (%d,%d) with radius %d".formatted(x, y, r);
          case Rectangle(Point(int x1, int y1), Point(int x2, int y2)) -> 
              "Rectangle from (%d,%d) to (%d,%d)".formatted(x1, y1, x2, y2);
          default -> "Unknown shape";
      };
  }
  ```

  **Exhaustiveness with Sealed Classes:**
  ```java
  sealed interface Result permits Success, Failure {}
  record Success(String data) implements Result {}
  record Failure(String error) implements Result {}
  
  // No default needed - compiler knows all cases
  public static void handle(Result result) {
      switch (result) {
          case Success(String data) -> System.out.println("Success: " + data);
          case Failure(String error) -> System.out.println("Error: " + error);
      }
  }
  ```

  **Multiple Patterns:**
  ```java
  public static String classify(Object obj) {
      return switch (obj) {
          case String s, StringBuilder sb -> "Text type";
          case Integer i, Long l, Short sh -> "Integer type";
          case Float f, Double d -> "Floating point type";
          default -> "Other type";
      };
  }
  ```
  
</details>

## Generics

<details>
  <summary>What are Generics?</summary>
  <br/>

  Generics enable types (classes and interfaces) to be parameters when defining classes, interfaces, and methods. They provide compile-time type safety and eliminate the need for casting.

  **Benefits:**
  + **Type Safety:** Compile-time type checking prevents runtime errors
  + **Code Reusability:** Write generic algorithms that work with different types
  + **Elimination of Casts:** No need for explicit type casting
  + **Stronger Type Checks:** Catch errors at compile time rather than runtime

  **Basic Generic Class:**
  ```java
  // Without Generics
  public class Box {
      private Object value;
      
      public void set(Object value) {
          this.value = value;
      }
      
      public Object get() {
          return value;
      }
  }
  
  // Usage requires casting
  Box box = new Box();
  box.set("Hello");
  String str = (String) box.get();  // Cast required
  
  // With Generics
  public class Box<T> {
      private T value;
      
      public void set(T value) {
          this.value = value;
      }
      
      public T get() {
          return value;
      }
  }
  
  // Usage - no casting needed
  Box<String> box = new Box<>();
  box.set("Hello");
  String str = box.get();  // No cast needed
  ```

  **Generic Methods:**
  ```java
  public class Utils {
      // Generic method
      public static <T> void printArray(T[] array) {
          for (T element : array) {
              System.out.println(element);
          }
      }
      
      // Multiple type parameters
      public static <K, V> void printKeyValue(K key, V value) {
          System.out.println(key + ": " + value);
      }
  }
  
  // Usage
  String[] names = {"Alice", "Bob", "Charlie"};
  Utils.printArray(names);
  
  Utils.printKeyValue("Name", "Alice");
  Utils.printKeyValue(1, 100);
  ```
  
</details>

<details>
  <summary>Bounded Type Parameters</summary>
  <br/>

  Bounded type parameters restrict the types that can be used as type arguments.

  **Upper Bounded (extends):**
  ```java
  // T must be Number or its subclass
  public class NumberBox<T extends Number> {
      private T value;
      
      public void set(T value) {
          this.value = value;
      }
      
      public double getDoubleValue() {
          return value.doubleValue();  // Can call Number methods
      }
  }
  
  // Valid
  NumberBox<Integer> intBox = new NumberBox<>();
  NumberBox<Double> doubleBox = new NumberBox<>();
  
  // Invalid - String is not a Number
  // NumberBox<String> stringBox = new NumberBox<>();  // Compilation error
  ```

  **Multiple Bounds:**
  ```java
  // T must implement both Comparable and Serializable
  public class SortableBox<T extends Comparable<T> & Serializable> {
      private T value;
      
      public void set(T value) {
          this.value = value;
      }
      
      public int compareTo(T other) {
          return value.compareTo(other);
      }
  }
  ```

  **Bounded Generic Methods:**
  ```java
  // Find maximum in array
  public static <T extends Comparable<T>> T findMax(T[] array) {
      if (array == null || array.length == 0) {
          return null;
      }
      
      T max = array[0];
      for (int i = 1; i < array.length; i++) {
          if (array[i].compareTo(max) > 0) {
              max = array[i];
          }
      }
      return max;
  }
  
  // Usage
  Integer[] numbers = {3, 7, 2, 9, 1};
  Integer max = findMax(numbers);  // 9
  
  String[] names = {"Alice", "Charlie", "Bob"};
  String maxName = findMax(names);  // "Charlie"
  ```

  **Practical Example:**
  ```java
  public class Repository<T extends BaseEntity> {
      private List<T> entities = new ArrayList<>();
      
      public void save(T entity) {
          entities.add(entity);
      }
      
      public T findById(Long id) {
          return entities.stream()
              .filter(e -> e.getId().equals(id))
              .findFirst()
              .orElse(null);
      }
      
      public List<T> findAll() {
          return new ArrayList<>(entities);
      }
  }
  ```
  
</details>

<details>
  <summary>Wildcards</summary>
  <br/>

  Wildcards represent an unknown type and are denoted by the question mark (?).

  **Unbounded Wildcard (?):**
  ```java
  // Accepts List of any type
  public static void printList(List<?> list) {
      for (Object element : list) {
          System.out.println(element);
      }
  }
  
  // Usage
  List<Integer> intList = Arrays.asList(1, 2, 3);
  List<String> strList = Arrays.asList("A", "B", "C");
  
  printList(intList);  // Works
  printList(strList);  // Works
  ```

  **Upper Bounded Wildcard (? extends Type):**
  ```java
  // Accepts List of Number or its subtypes (Integer, Double, etc.)
  public static double sumNumbers(List<? extends Number> list) {
      double sum = 0.0;
      for (Number num : list) {
          sum += num.doubleValue();
      }
      return sum;
  }
  
  // Usage
  List<Integer> intList = Arrays.asList(1, 2, 3);
  List<Double> doubleList = Arrays.asList(1.5, 2.5, 3.5);
  
  double sum1 = sumNumbers(intList);     // Works
  double sum2 = sumNumbers(doubleList);  // Works
  ```

  **Lower Bounded Wildcard (? super Type):**
  ```java
  // Accepts List of Integer or its supertypes (Number, Object)
  public static void addIntegers(List<? super Integer> list) {
      list.add(1);
      list.add(2);
      list.add(3);
  }
  
  // Usage
  List<Integer> intList = new ArrayList<>();
  List<Number> numList = new ArrayList<>();
  List<Object> objList = new ArrayList<>();
  
  addIntegers(intList);  // Works
  addIntegers(numList);  // Works
  addIntegers(objList);  // Works
  ```

  **PECS Principle (Producer Extends, Consumer Super):**
  ```java
  // Producer - use extends (reading from collection)
  public static void copyNumbers(List<? extends Number> source, 
                                  List<? super Number> destination) {
      for (Number num : source) {
          destination.add(num);  // Producer extends, Consumer super
      }
  }
  
  // Usage
  List<Integer> intList = Arrays.asList(1, 2, 3);
  List<Number> numList = new ArrayList<>();
  
  copyNumbers(intList, numList);  // Works
  ```

  **When to Use Which Wildcard:**
  + **? extends T**: Use when you only read from a structure (Producer)
  + **? super T**: Use when you only write to a structure (Consumer)
  + **?**: Use when you don't care about the type
  + **T**: Use when you need to read and write

  **Practical Example:**
  ```java
  public class CollectionUtils {
      // Read-only operation - use extends
      public static <T> void printAll(Collection<? extends T> collection) {
          for (T item : collection) {
              System.out.println(item);
          }
      }
      
      // Write operation - use super
      public static void addNumbers(List<? super Integer> list) {
          list.add(1);
          list.add(2);
          list.add(3);
      }
      
      // Copy operation - both extends and super
      public static <T> void copy(List<? extends T> source, 
                                   List<? super T> destination) {
          for (T item : source) {
              destination.add(item);
          }
      }
  }
  ```
  
</details>

<details>
  <summary>Type Erasure</summary>
  <br/>

  Type erasure is the process by which the Java compiler removes all generic type information during compilation. This ensures backward compatibility with older Java versions.

  **How Type Erasure Works:**
  ```java
  // Before compilation (source code)
  public class Box<T> {
      private T value;
      
      public void set(T value) {
          this.value = value;
      }
      
      public T get() {
          return value;
      }
  }
  
  // After type erasure (bytecode)
  public class Box {
      private Object value;  // T replaced with Object
      
      public void set(Object value) {
          this.value = value;
      }
      
      public Object get() {
          return value;
      }
  }
  ```

  **With Bounded Types:**
  ```java
  // Before compilation
  public class NumberBox<T extends Number> {
      private T value;
      
      public void set(T value) {
          this.value = value;
      }
      
      public T get() {
          return value;
      }
  }
  
  // After type erasure
  public class NumberBox {
      private Number value;  // T replaced with Number (the bound)
      
      public void set(Number value) {
          this.value = value;
      }
      
      public Number get() {
          return value;
      }
  }
  ```

  **Implications of Type Erasure:**

  **1. Cannot Create Instances of Type Parameters:**
  ```java
  public class Box<T> {
      // Invalid - cannot instantiate T
      // private T value = new T();  // Compilation error
      
      // Workaround: Use Class object
      private T value;
      
      public Box(Class<T> clazz) throws Exception {
          value = clazz.getDeclaredConstructor().newInstance();
      }
  }
  ```

  **2. Cannot Create Arrays of Generic Types:**
  ```java
  // Invalid
  // List<String>[] stringLists = new List<String>[10];  // Compilation error
  
  // Workaround: Use List of Lists
  List<List<String>> stringLists = new ArrayList<>();
  
  // Or use raw type with warning
  @SuppressWarnings("unchecked")
  List<String>[] stringLists = (List<String>[]) new List[10];
  ```

  **3. Cannot Use Primitives as Type Parameters:**
  ```java
  // Invalid
  // List<int> numbers = new ArrayList<>();  // Compilation error
  
  // Must use wrapper classes
  List<Integer> numbers = new ArrayList<>();
  ```

  **4. Cannot Perform instanceof with Generic Types:**
  ```java
  public static <T> void checkType(Object obj) {
      // Invalid
      // if (obj instanceof T) { }  // Compilation error
      // if (obj instanceof List<String>) { }  // Compilation error
      
      // Valid - use raw type
      if (obj instanceof List) {
          List<?> list = (List<?>) obj;
      }
  }
  ```

  **5. Cannot Overload Methods with Same Erasure:**
  ```java
  public class Example {
      // Invalid - both methods have same erasure
      // public void process(List<String> list) { }
      // public void process(List<Integer> list) { }  // Compilation error
      
      // After erasure, both become: process(List list)
  }
  ```

  **6. Static Context Cannot Reference Type Parameters:**
  ```java
  public class Box<T> {
      // Invalid
      // private static T value;  // Compilation error
      // public static T getValue() { }  // Compilation error
      
      // Static members cannot use class type parameters
  }
  ```

  **Workarounds for Type Erasure Limitations:**
  ```java
  public class GenericFactory<T> {
      private Class<T> type;
      
      public GenericFactory(Class<T> type) {
          this.type = type;
      }
      
      public T createInstance() throws Exception {
          return type.getDeclaredConstructor().newInstance();
      }
      
      public boolean isInstance(Object obj) {
          return type.isInstance(obj);
      }
      
      public Class<T> getType() {
          return type;
      }
  }
  
  // Usage
  GenericFactory<String> factory = new GenericFactory<>(String.class);
  String str = factory.createInstance();
  boolean isString = factory.isInstance("Hello");
  ```
  
</details>

<details>
  <summary>Generic Best Practices</summary>
  <br/>

  **DO:**
  + ✅ Use generics for type safety and code reusability
  + ✅ Follow PECS principle (Producer Extends, Consumer Super)
  + ✅ Use bounded type parameters when you need specific functionality
  + ✅ Use wildcards for flexibility in method parameters
  + ✅ Prefer `List<String>` over raw type `List`

  **DON'T:**
  + ❌ Don't use raw types (causes unchecked warnings)
  + ❌ Don't ignore compiler warnings about unchecked operations
  + ❌ Don't mix generic and non-generic code unnecessarily
  + ❌ Don't create arrays of generic types

  **Good Examples:**
  ```java
  // Good: Generic method with bounded type
  public static <T extends Comparable<T>> T max(List<T> list) {
      return list.stream().max(T::compareTo).orElse(null);
  }
  
  // Good: Using wildcards for flexibility
  public static void printCollection(Collection<?> collection) {
      collection.forEach(System.out::println);
  }
  
  // Good: PECS principle
  public static <T> void copy(List<? extends T> src, List<? super T> dest) {
      for (T item : src) {
          dest.add(item);
      }
  }
  ```

  **Bad Examples:**
  ```java
  // Bad: Using raw type
  List list = new ArrayList();  // Don't do this
  list.add("String");
  list.add(123);  // No type safety
  
  // Good: Use generic type
  List<String> list = new ArrayList<>();
  
  // Bad: Ignoring type safety
  @SuppressWarnings("unchecked")
  List<String> list = (List<String>) getList();  // Avoid if possible
  
  // Bad: Creating generic array
List<String>[] array = new List<String>[10];  // Compilation error
  ```
  
</details>


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

