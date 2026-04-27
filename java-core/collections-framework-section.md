
## Collections Framework

<details>
  <summary>Collections Framework Overview</summary>
  <br/>

  The Java Collections Framework provides a unified architecture for storing and manipulating groups of objects.

  **Core Interfaces Hierarchy:**
  ```java
  Collection
  ├── List (Ordered, allows duplicates)
  │   ├── ArrayList
  │   ├── LinkedList
  │   ├── Vector
  │   └── Stack
  ├── Set (No duplicates)
  │   ├── HashSet
  │   ├── LinkedHashSet
  │   └── SortedSet
  │       └── TreeSet
  └── Queue (FIFO or priority-based)
      ├── PriorityQueue
      ├── LinkedList
      └── Deque
          ├── ArrayDeque
          └── LinkedList
  
  Map (Key-Value pairs, not part of Collection)
  ├── HashMap
  ├── LinkedHashMap
  ├── Hashtable
  ├── SortedMap
  │   └── TreeMap
  └── ConcurrentHashMap
  ```

  **Key Interfaces:**
  + **Collection:** Root interface for all collections
  + **List:** Ordered collection (sequence)
  + **Set:** Collection with no duplicates
  + **Queue:** Collection for holding elements prior to processing
  + **Map:** Object that maps keys to values
  
</details>

<details>
  <summary>List Interface</summary>
  <br/>

  List is an ordered collection (sequence) that allows duplicate elements and provides positional access.

  **Common Implementations:**
  + **ArrayList:** Resizable array implementation
  + **LinkedList:** Doubly-linked list implementation
  + **Vector:** Synchronized resizable array (legacy)
  + **Stack:** LIFO stack (legacy, use Deque instead)

  **ArrayList:**
  ```java
  List<String> list = new ArrayList<>();
  
  // Add elements
  list.add("Apple");
  list.add("Banana");
  list.add("Cherry");
  list.add(1, "Blueberry");  // Insert at index
  
  // Access elements
  String first = list.get(0);  // "Apple"
  int size = list.size();      // 4
  
  // Modify elements
  list.set(0, "Apricot");      // Replace at index
  
  // Remove elements
  list.remove(0);              // Remove by index
  list.remove("Banana");       // Remove by object
  
  // Search
  boolean contains = list.contains("Cherry");  // true
  int index = list.indexOf("Cherry");          // 2
  
  // Iterate
  for (String fruit : list) {
      System.out.println(fruit);
  }
  
  // Using streams
  list.stream()
      .filter(s -> s.startsWith("C"))
      .forEach(System.out::println);
  ```

  **LinkedList:**
  ```java
  LinkedList<String> linkedList = new LinkedList<>();
  
  // Add elements
  linkedList.add("First");
  linkedList.addFirst("New First");  // Add at beginning
  linkedList.addLast("Last");        // Add at end
  
  // Access elements
  String first = linkedList.getFirst();
  String last = linkedList.getLast();
  
  // Remove elements
  linkedList.removeFirst();
  linkedList.removeLast();
  
  // Use as Queue
  linkedList.offer("Element");  // Add to end
  String element = linkedList.poll();  // Remove from beginning
  
  // Use as Stack
  linkedList.push("Top");       // Add to beginning
  String top = linkedList.pop(); // Remove from beginning
  ```

  **ArrayList vs LinkedList:**

  | Operation | ArrayList | LinkedList |
  |-----------|-----------|------------|
  | Get by index | O(1) | O(n) |
  | Add at end | O(1) amortized | O(1) |
  | Add at beginning | O(n) | O(1) |
  | Add at middle | O(n) | O(n) |
  | Remove by index | O(n) | O(n) |
  | Memory overhead | Low | High (node objects) |
  | Cache performance | Better | Worse |

  **When to Use:**
  + **ArrayList:** Random access, iteration, small insertions/deletions
  + **LinkedList:** Frequent insertions/deletions at beginning/end, queue/deque operations
  
</details>

<details>
  <summary>Set Interface</summary>
  <br/>

  Set is a collection that contains no duplicate elements.

  **Common Implementations:**
  + **HashSet:** Hash table implementation, no ordering
  + **LinkedHashSet:** Hash table + linked list, maintains insertion order
  + **TreeSet:** Red-black tree implementation, sorted order

  **HashSet:**
  ```java
  Set<String> set = new HashSet<>();
  
  // Add elements
  set.add("Apple");
  set.add("Banana");
  set.add("Cherry");
  set.add("Apple");  // Duplicate, not added
  
  System.out.println(set.size());  // 3
  
  // Check existence
  boolean contains = set.contains("Banana");  // true
  
  // Remove
  set.remove("Banana");
  
  // Iterate (no guaranteed order)
  for (String fruit : set) {
      System.out.println(fruit);
  }
  
  // Set operations
  Set<String> set1 = new HashSet<>(Arrays.asList("A", "B", "C"));
  Set<String> set2 = new HashSet<>(Arrays.asList("B", "C", "D"));
  
  // Union
  Set<String> union = new HashSet<>(set1);
  union.addAll(set2);  // [A, B, C, D]
  
  // Intersection
  Set<String> intersection = new HashSet<>(set1);
  intersection.retainAll(set2);  // [B, C]
  
  // Difference
  Set<String> difference = new HashSet<>(set1);
  difference.removeAll(set2);  // [A]
  ```

  **LinkedHashSet:**
  ```java
  Set<String> linkedSet = new LinkedHashSet<>();
  linkedSet.add("Banana");
  linkedSet.add("Apple");
  linkedSet.add("Cherry");
  
  // Maintains insertion order
  for (String fruit : linkedSet) {
      System.out.println(fruit);  // Banana, Apple, Cherry
  }
  ```

  **TreeSet:**
  ```java
  Set<String> treeSet = new TreeSet<>();
  treeSet.add("Banana");
  treeSet.add("Apple");
  treeSet.add("Cherry");
  
  // Sorted order (natural ordering)
  for (String fruit : treeSet) {
      System.out.println(fruit);  // Apple, Banana, Cherry
  }
  
  // NavigableSet methods
  String first = ((TreeSet<String>) treeSet).first();  // "Apple"
  String last = ((TreeSet<String>) treeSet).last();    // "Cherry"
  
  // Custom comparator
  Set<String> reverseSet = new TreeSet<>(Comparator.reverseOrder());
  reverseSet.addAll(Arrays.asList("Banana", "Apple", "Cherry"));
  // Order: Cherry, Banana, Apple
  ```

  **HashSet vs LinkedHashSet vs TreeSet:**

  | Feature | HashSet | LinkedHashSet | TreeSet |
  |---------|---------|---------------|---------|
  | Ordering | No order | Insertion order | Sorted order |
  | Performance | O(1) | O(1) | O(log n) |
  | Null elements | One null | One null | No null (with natural ordering) |
  | Memory | Low | Medium | Medium |

  **Custom Objects in Set:**
  ```java
  public class Person {
      private String name;
      private int age;
      
      // Must override equals and hashCode for HashSet
      @Override
      public boolean equals(Object o) {
          if (this == o) return true;
          if (o == null || getClass() != o.getClass()) return false;
          Person person = (Person) o;
          return age == person.age && Objects.equals(name, person.name);
      }
      
      @Override
      public int hashCode() {
          return Objects.hash(name, age);
      }
  }
  
  Set<Person> people = new HashSet<>();
  people.add(new Person("Alice", 30));
  people.add(new Person("Alice", 30));  // Duplicate, not added
  System.out.println(people.size());  // 1
  ```
  
</details>

<details>
  <summary>Map Interface</summary>
  <br/>

  Map is an object that maps keys to values, with no duplicate keys allowed.

  **Common Implementations:**
  + **HashMap:** Hash table implementation, no ordering
  + **LinkedHashMap:** Hash table + linked list, maintains insertion order
  + **TreeMap:** Red-black tree implementation, sorted by keys
  + **Hashtable:** Synchronized hash table (legacy)
  + **ConcurrentHashMap:** Thread-safe hash table

  **HashMap:**
  ```java
  Map<String, Integer> map = new HashMap<>();
  
  // Add entries
  map.put("Apple", 10);
  map.put("Banana", 20);
  map.put("Cherry", 30);
  map.put("Apple", 15);  // Updates existing value
  
  // Get value
  Integer value = map.get("Apple");  // 15
  Integer defaultValue = map.getOrDefault("Orange", 0);  // 0
  
  // Check existence
  boolean hasKey = map.containsKey("Banana");    // true
  boolean hasValue = map.containsValue(20);      // true
  
  // Remove
  map.remove("Banana");
  
  // Size
  int size = map.size();  // 2
  
  // Iterate over keys
  for (String key : map.keySet()) {
      System.out.println(key + ": " + map.get(key));
  }
  
  // Iterate over values
  for (Integer val : map.values()) {
      System.out.println(val);
  }
  
  // Iterate over entries
  for (Map.Entry<String, Integer> entry : map.entrySet()) {
      System.out.println(entry.getKey() + ": " + entry.getValue());
  }
  
  // Java 8+ forEach
  map.forEach((key, value) -> System.out.println(key + ": " + value));
  ```

  **Java 8+ Map Methods:**
  ```java
  Map<String, Integer> map = new HashMap<>();
  
  // putIfAbsent - only adds if key doesn't exist
  map.putIfAbsent("Apple", 10);
  map.putIfAbsent("Apple", 20);  // Doesn't update
  
  // computeIfAbsent - compute value if key doesn't exist
  map.computeIfAbsent("Banana", key -> key.length());
  
  // computeIfPresent - compute new value if key exists
  map.computeIfPresent("Apple", (key, value) -> value + 5);
  
  // compute - compute value regardless
  map.compute("Cherry", (key, value) -> value == null ? 1 : value + 1);
  
  // merge - merge values
  map.merge("Apple", 5, (oldValue, newValue) -> oldValue + newValue);
  
  // replace
  map.replace("Apple", 10);
  map.replace("Apple", 10, 15);  // Replace only if current value is 10
  
  // replaceAll
  map.replaceAll((key, value) -> value * 2);
  ```

  **LinkedHashMap:**
  ```java
  Map<String, Integer> linkedMap = new LinkedHashMap<>();
  linkedMap.put("Banana", 20);
  linkedMap.put("Apple", 10);
  linkedMap.put("Cherry", 30);
  
  // Maintains insertion order
  linkedMap.forEach((k, v) -> System.out.println(k + ": " + v));
  // Output: Banana: 20, Apple: 10, Cherry: 30
  
  // LRU Cache using LinkedHashMap
  Map<String, String> lruCache = new LinkedHashMap<String, String>(16, 0.75f, true) {
      @Override
      protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
          return size() > 100;  // Max 100 entries
      }
  };
  ```

  **TreeMap:**
  ```java
  Map<String, Integer> treeMap = new TreeMap<>();
  treeMap.put("Banana", 20);
  treeMap.put("Apple", 10);
  treeMap.put("Cherry", 30);
  
  // Sorted by keys
  treeMap.forEach((k, v) -> System.out.println(k + ": " + v));
  // Output: Apple: 10, Banana: 20, Cherry: 30
  
  // NavigableMap methods
  String firstKey = ((TreeMap<String, Integer>) treeMap).firstKey();  // "Apple"
  String lastKey = ((TreeMap<String, Integer>) treeMap).lastKey();    // "Cherry"
  
  // Range views
  Map<String, Integer> subMap = ((TreeMap<String, Integer>) treeMap)
      .subMap("Apple", "Cherry");  // Apple to Cherry (exclusive)
  ```

  **HashMap vs LinkedHashMap vs TreeMap:**

  | Feature | HashMap | LinkedHashMap | TreeMap |
  |---------|---------|---------------|---------|
  | Ordering | No order | Insertion order | Sorted by keys |
  | Performance | O(1) | O(1) | O(log n) |
  | Null keys | One null | One null | No null |
  | Memory | Low | Medium | Medium |
  
</details>

<details>
  <summary>Queue and Deque</summary>
  <br/>

  Queue is a collection for holding elements prior to processing, typically in FIFO order.

  **Queue Interface:**
  ```java
  Queue<String> queue = new LinkedList<>();
  
  // Add elements
  queue.offer("First");   // Adds to end, returns false if fails
  queue.add("Second");    // Adds to end, throws exception if fails
  
  // Examine head
  String head = queue.peek();    // Returns null if empty
  String head2 = queue.element(); // Throws exception if empty
  
  // Remove head
  String removed = queue.poll();   // Returns null if empty
  String removed2 = queue.remove(); // Throws exception if empty
  
  // Size
  int size = queue.size();
  ```

  **PriorityQueue:**
  ```java
  // Natural ordering (min heap)
  Queue<Integer> pq = new PriorityQueue<>();
  pq.offer(5);
  pq.offer(2);
  pq.offer(8);
  pq.offer(1);
  
  while (!pq.isEmpty()) {
      System.out.println(pq.poll());  // 1, 2, 5, 8
  }
  
  // Custom comparator (max heap)
  Queue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
  maxHeap.offer(5);
  maxHeap.offer(2);
  maxHeap.offer(8);
  
  System.out.println(maxHeap.poll());  // 8
  
  // Custom objects
  class Task {
      String name;
      int priority;
      
      Task(String name, int priority) {
          this.name = name;
          this.priority = priority;
      }
  }
  
  Queue<Task> taskQueue = new PriorityQueue<>(
      Comparator.comparingInt(t -> t.priority)
  );
  
  taskQueue.offer(new Task("Low", 3));
  taskQueue.offer(new Task("High", 1));
  taskQueue.offer(new Task("Medium", 2));
  
  while (!taskQueue.isEmpty()) {
      Task task = taskQueue.poll();
      System.out.println(task.name);  // High, Medium, Low
  }
  ```

  **Deque (Double-Ended Queue):**
  ```java
  Deque<String> deque = new ArrayDeque<>();
  
  // Add at both ends
  deque.addFirst("First");
  deque.addLast("Last");
  deque.offerFirst("New First");
  deque.offerLast("New Last");
  
  // Access both ends
  String first = deque.getFirst();
  String last = deque.getLast();
  String peekFirst = deque.peekFirst();
  String peekLast = deque.peekLast();
  
  // Remove from both ends
  String removedFirst = deque.removeFirst();
  String removedLast = deque.removeLast();
  String pollFirst = deque.pollFirst();
  String pollLast = deque.pollLast();
  
  // Use as Stack (LIFO)
  deque.push("Top");      // Add to front
  String top = deque.pop(); // Remove from front
  
  // Use as Queue (FIFO)
  deque.offer("Element");  // Add to end
  String element = deque.poll();  // Remove from front
  ```

  **ArrayDeque vs LinkedList:**

  | Feature | ArrayDeque | LinkedList |
  |---------|------------|------------|
  | Implementation | Resizable array | Doubly-linked list |
  | Memory | More efficient | More overhead |
  | Performance | Generally faster | Slower |
  | Null elements | Not allowed | Allowed |
  
</details>

<details>
  <summary>Comparable vs Comparator</summary>
  <br/>

  **Comparable:** Natural ordering, single sorting sequence

  ```java
  public class Student implements Comparable<Student> {
      private String name;
      private int age;
      
      public Student(String name, int age) {
          this.name = name;
          this.age = age;
      }
      
      @Override
      public int compareTo(Student other) {
          // Sort by age
          return Integer.compare(this.age, other.age);
      }
      
      // Getters
      public String getName() { return name; }
      public int getAge() { return age; }
  }
  
  // Usage
  List<Student> students = new ArrayList<>();
  students.add(new Student("Alice", 22));
  students.add(new Student("Bob", 20));
  students.add(new Student("Charlie", 21));
  
  Collections.sort(students);  // Sorts by age
  
  // Or with TreeSet
  Set<Student> sortedStudents = new TreeSet<>();
  sortedStudents.addAll(students);  // Automatically sorted
  ```

  **Comparator:** Custom ordering, multiple sorting sequences

  ```java
  public class Student {
      private String name;
      private int age;
      private double gpa;
      
      // Constructor, getters
  }
  
  // Sort by name
  Comparator<Student> nameComparator = new Comparator<Student>() {
      @Override
      public int compare(Student s1, Student s2) {
          return s1.getName().compareTo(s2.getName());
      }
  };
  
  // Sort by age (lambda)
  Comparator<Student> ageComparator = (s1, s2) -> 
      Integer.compare(s1.getAge(), s2.getAge());
  
  // Sort by GPA (method reference)
  Comparator<Student> gpaComparator = 
      Comparator.comparingDouble(Student::getGpa);
  
  // Usage
  List<Student> students = new ArrayList<>();
  
  // Sort by name
  Collections.sort(students, nameComparator);
  // Or
  students.sort(nameComparator);
  
  // Sort by age
  students.sort(ageComparator);
  
  // Sort by GPA
  students.sort(gpaComparator);
  ```

  **Comparator Chaining (Java 8+):**
  ```java
  // Sort by age, then by name
  Comparator<Student> comparator = Comparator
      .comparingInt(Student::getAge)
      .thenComparing(Student::getName);
  
  students.sort(comparator);
  
  // Reverse order
  students.sort(Comparator.comparingInt(Student::getAge).reversed());
  
  // Null-safe comparator
  Comparator<Student> nullSafeComparator = Comparator
      .nullsFirst(Comparator.comparing(Student::getName));
  
  // Multiple criteria
  students.sort(
      Comparator.comparing(Student::getAge)
                .thenComparing(Student::getName)
                .thenComparingDouble(Student::getGpa)
  );
  ```

  **Comparable vs Comparator:**

  | Feature | Comparable | Comparator |
  |---------|------------|------------|
  | Package | java.lang | java.util |
  | Method | compareTo(T o) | compare(T o1, T o2) |
  | Sorting sequences | Single | Multiple |
  | Modify class | Yes | No |
  | Use case | Natural ordering | Custom ordering |
  
</details>

<details>
  <summary>Collections Utility Class</summary>
  <br/>

  The Collections class provides static methods for operating on collections.

  **Sorting:**
  ```java
  List<Integer> numbers = Arrays.asList(5, 2, 8, 1, 9);
  
  // Sort in natural order
  Collections.sort(numbers);  // [1, 2, 5, 8, 9]
  
  // Sort in reverse order
  Collections.sort(numbers, Collections.reverseOrder());  // [9, 8, 5, 2, 1]
  
  // Sort with custom comparator
  Collections.sort(numbers, (a, b) -> b - a);  // Descending
  ```

  **Searching:**
  ```java
  List<Integer> numbers = Arrays.asList(1, 2, 5, 8, 9);
  
  // Binary search (list must be sorted)
  int index = Collections.binarySearch(numbers, 5);  // 2
  int notFound = Collections.binarySearch(numbers, 3);  // -3 (insertion point)
  
  // Max and Min
  Integer max = Collections.max(numbers);  // 9
  Integer min = Collections.min(numbers);  // 1
  
  // Frequency
  List<String> words = Arrays.asList("a", "b", "a", "c", "a");
  int frequency = Collections.frequency(words, "a");  // 3
  ```

  **Modifying Collections:**
  ```java
  List<Integer> numbers = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
  
  // Reverse
  Collections.reverse(numbers);  // [5, 4, 3, 2, 1]
  
  // Shuffle
  Collections.shuffle(numbers);  // Random order
  
  // Rotate
  Collections.rotate(numbers, 2);  // Rotate right by 2
  
  // Swap
  Collections.swap(numbers, 0, 4);  // Swap elements at index 0 and 4
  
  // Fill
  Collections.fill(numbers, 0);  // Replace all elements with 0
  
  // Replace all
  Collections.replaceAll(numbers, 0, 1);  // Replace all 0s with 1s
  ```

  **Creating Special Collections:**
  ```java
  // Empty collections
  List<String> emptyList = Collections.emptyList();
  Set<String> emptySet = Collections.emptySet();
  Map<String, Integer> emptyMap = Collections.emptyMap();
  
  // Singleton collections
  List<String> singletonList = Collections.singletonList("Only");
  Set<String> singletonSet = Collections.singleton("Only");
  Map<String, Integer> singletonMap = Collections.singletonMap("key", 1);
  
  // Unmodifiable collections
  List<String> list = new ArrayList<>(Arrays.asList("A", "B", "C"));
  List<String> unmodifiableList = Collections.unmodifiableList(list);
  // unmodifiableList.add("D");  // Throws UnsupportedOperationException
  
  // Synchronized collections
  List<String> syncList = Collections.synchronizedList(new ArrayList<>());
  Set<String> syncSet = Collections.synchronizedSet(new HashSet<>());
  Map<String, Integer> syncMap = Collections.synchronizedMap(new HashMap<>());
  
  // Checked collections (type-safe at runtime)
  List<String> checkedList = Collections.checkedList(
      new ArrayList<>(), String.class);
  ```

  **Copying and Filling:**
  ```java
  List<String> source = Arrays.asList("A", "B", "C");
  List<String> dest = new ArrayList<>(Arrays.asList("X", "Y", "Z"));
  
  // Copy (dest must be at least as long as source)
  Collections.copy(dest, source);  // dest becomes ["A", "B", "C"]
  
  // AddAll
  List<String> combined = new ArrayList<>();
  Collections.addAll(combined, "A", "B", "C");
  
  // NCopies
  List<String> repeated = Collections.nCopies(5, "Hello");
  // ["Hello", "Hello", "Hello", "Hello", "Hello"]
  ```
  
</details>

<details>
  <summary>Concurrent Collections</summary>
  <br/>

  Thread-safe collections for concurrent access without external synchronization.

  **ConcurrentHashMap:**
  ```java
  Map<String, Integer> concurrentMap = new ConcurrentHashMap<>();
  
  // Thread-safe operations
  concurrentMap.put("key1", 1);
  concurrentMap.putIfAbsent("key2", 2);
  
  // Atomic operations
  concurrentMap.compute("key1", (key, value) -> value == null ? 1 : value + 1);
  concurrentMap.merge("key1", 1, Integer::sum);
  
  // Bulk operations
  concurrentMap.forEach((key, value) -> System.out.println(key + ": " + value));
  
  // Search
  String result = concurrentMap.search(1, (key, value) -> 
      value > 5 ? key : null);
  
  // Reduce
  Integer sum = concurrentMap.reduce(1, 
      (key, value) -> value,
      Integer::sum);
  ```

  **CopyOnWriteArrayList:**
  ```java
  List<String> cowList = new CopyOnWriteArrayList<>();
  
  // Thread-safe add
  cowList.add("A");
  cowList.add("B");
  
  // Iterator doesn't throw ConcurrentModificationException
  for (String item : cowList) {
      cowList.add("C");  // Safe during iteration
  }
  
  // Best for read-heavy scenarios
  ```

  **CopyOnWriteArraySet:**
  ```java
  Set<String> cowSet = new CopyOnWriteArraySet<>();
  cowSet.add("A");
  cowSet.add("B");
  
  // Thread-safe, no duplicates
  ```

  **BlockingQueue:**
  ```java
  BlockingQueue<String> queue = new LinkedBlockingQueue<>(10);
  
  // Producer thread
  new Thread(() -> {
      try {
          queue.put("Item1");  // Blocks if queue is full
          queue.offer("Item2", 1, TimeUnit.SECONDS);  // Timeout
      } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
      }
  }).start();
  
  // Consumer thread
  new Thread(() -> {
      try {
          String item = queue.take();  // Blocks if queue is empty
          String item2 = queue.poll(1, TimeUnit.SECONDS);  // Timeout
      } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
      }
  }).start();
  ```

  **ConcurrentLinkedQueue:**
  ```java
  Queue<String> concurrentQueue = new ConcurrentLinkedQueue<>();
  
  // Thread-safe operations
  concurrentQueue.offer("A");
  concurrentQueue.offer("B");
  
  String head = concurrentQueue.poll();
  ```
  
</details>

<details>
  <summary>Collections Best Practices</summary>
  <br/>

  **DO:**
  + ✅ Use interface types for declarations: `List<String> list = new ArrayList<>()`
  + ✅ Specify initial capacity for large collections
  + ✅ Use appropriate collection for the use case
  + ✅ Override equals() and hashCode() for custom objects in Set/Map
  + ✅ Use immutable collections when possible
  + ✅ Use concurrent collections for multi-threaded access

  **DON'T:**
  + ❌ Don't use raw types: `List list = new ArrayList()`
  + ❌ Don't modify collection while iterating (use Iterator.remove())
  + ❌ Don't use Vector or Hashtable (use ArrayList and HashMap instead)
  + ❌ Don't use null as map key (except HashMap allows one null key)

  **Performance Tips:**
  ```java
  // Good: Specify initial capacity
  List<String> list = new ArrayList<>(1000);
  Map<String, Integer> map = new HashMap<>(1000);
  
  // Good: Use appropriate collection
  // For frequent insertions/deletions at beginning
  Deque<String> deque = new ArrayDeque<>();
  
  // For sorted unique elements
  Set<String> sortedSet = new TreeSet<>();
  
  // For LRU cache
  Map<String, String> lruCache = new LinkedHashMap<>(16, 0.75f, true);
  
  // Good: Immutable collections (Java 9+)
  List<String> immutableList = List.of("A", "B", "C");
  Set<String> immutableSet = Set.of("A", "B", "C");
  Map<String, Integer> immutableMap = Map.of("A", 1, "B", 2);
  ```

  **Common Pitfalls:**
  ```java
  // Bad: ConcurrentModificationException
  List<String> list = new ArrayList<>(Arrays.asList("A", "B", "C"));
  for (String item : list) {
      if (item.equals("B")) {
          list.remove(item);  // Throws ConcurrentModificationException
      }
  }
  
  // Good: Use Iterator
  Iterator<String> iterator = list.iterator();
  while (iterator.hasNext()) {
      String item = iterator.next();
      if (item.equals("B")) {
          iterator.remove();  // Safe
      }
  }
  
  // Or use removeIf (Java 8+)
  list.removeIf(item -> item.equals("B"));
  ```
  
</details>


## Internal Implementation Details

<details>
  <summary>How ArrayList Works Internally</summary>
  <br/>

  ArrayList is backed by a dynamically resizable array.

  **Internal Structure:**
  ```java
  public class ArrayList<E> {
      private static final int DEFAULT_CAPACITY = 10;
      private Object[] elementData;  // Internal array
      private int size;              // Number of elements
      
      // Constructor
      public ArrayList() {
          this.elementData = new Object[DEFAULT_CAPACITY];
      }
  }
  ```

  **How ArrayList Resizes:**

  When the internal array is full, ArrayList creates a new larger array and copies elements.

  **Resize Process:**
  1. Check if array is full: `size == elementData.length`
  2. Calculate new capacity: `newCapacity = oldCapacity + (oldCapacity >> 1)` (grows by 50%)
  3. Create new array with new capacity
  4. Copy all elements from old array to new array using `Arrays.copyOf()`
  5. Replace old array with new array

  **Example:**
  ```java
  // Initial capacity: 10
  ArrayList<String> list = new ArrayList<>();
  
  // Add 10 elements - no resize
  for (int i = 0; i < 10; i++) {
      list.add("Item" + i);  // Capacity: 10
  }
  
  // Add 11th element - triggers resize
  list.add("Item10");  // Capacity: 10 + 5 = 15
  
  // Add more elements
  for (int i = 11; i < 15; i++) {
      list.add("Item" + i);  // Capacity: 15
  }
  
  // Add 16th element - triggers resize again
  list.add("Item15");  // Capacity: 15 + 7 = 22
  ```

  **Growth Pattern:**
  ```
  Initial: 10
  After 1st resize: 15 (10 + 10/2)
  After 2nd resize: 22 (15 + 15/2)
  After 3rd resize: 33 (22 + 22/2)
  After 4th resize: 49 (33 + 33/2)
  ```

  **Time Complexity:**
  + **Add at end:** O(1) amortized (O(n) when resize happens)
  + **Add at index:** O(n) (need to shift elements)
  + **Get by index:** O(1)
  + **Remove:** O(n) (need to shift elements)

  **Memory Considerations:**
  ```java
  // Bad: Default capacity for large list
  ArrayList<String> list = new ArrayList<>();  // Capacity: 10
  for (int i = 0; i < 10000; i++) {
      list.add("Item" + i);  // Multiple resizes!
  }
  
  // Good: Specify initial capacity
  ArrayList<String> list = new ArrayList<>(10000);  // Capacity: 10000
  for (int i = 0; i < 10000; i++) {
      list.add("Item" + i);  // No resize needed
  }
  ```

  **trimToSize() Method:**
  ```java
  ArrayList<String> list = new ArrayList<>(1000);
  list.add("A");
  list.add("B");
  list.add("C");
  
  // Capacity: 1000, Size: 3 (wasting memory)
  
  list.trimToSize();  // Reduces capacity to 3
  ```
  
</details>

<details>
  <summary>How HashMap Works Internally</summary>
  <br/>

  HashMap uses an array of buckets with linked lists or trees for collision handling.

  **Internal Structure:**
  ```java
  public class HashMap<K,V> {
      static final int DEFAULT_INITIAL_CAPACITY = 16;
      static final float DEFAULT_LOAD_FACTOR = 0.75f;
      static final int TREEIFY_THRESHOLD = 8;
      static final int UNTREEIFY_THRESHOLD = 6;
      
      transient Node<K,V>[] table;  // Array of buckets
      transient int size;            // Number of entries
      int threshold;                 // Resize threshold
      final float loadFactor;        // Load factor
      
      static class Node<K,V> {
          final int hash;
          final K key;
          V value;
          Node<K,V> next;  // For linked list
      }
  }
  ```

  **How HashMap Stores Data:**

  **Step 1: Calculate Hash Code**
  ```java
  // HashMap calculates hash
  int hash = key.hashCode();
  hash = hash ^ (hash >>> 16);  // Spread bits for better distribution
  ```

  **Step 2: Calculate Bucket Index**
  ```java
  int index = (n - 1) & hash;  // n is array length (power of 2)
  // Example: if n = 16, index = hash & 15
  ```

  **Step 3: Store in Bucket**
  ```java
  // If bucket is empty
  table[index] = new Node(hash, key, value, null);
  
  // If bucket has collision
  // Add to linked list or tree
  ```

  **Visual Example:**
  ```java
  HashMap with capacity 16:
  
  Index  Bucket
  -----  ------
  0   -> null
  1   -> null
  2   -> [key1, value1] -> [key2, value2] -> null  (Collision: linked list)
  3   -> null
  4   -> [key3, value3] -> null
  5   -> null
  ...
  15  -> null
  ```
  
</details>

<details>
  <summary>HashMap Collision Handling</summary>
  <br/>

  **What is a Collision?**

  A collision occurs when two different keys produce the same bucket index. This happens because:
  1. Multiple hash codes can map to the same bucket (limited number of buckets)
  2. Different keys might have the same hash code (though rare with good hash functions)

  HashMap handles collisions using **Separate Chaining** - storing multiple entries in the same bucket using linked lists or trees.

  **Understanding Collisions Step-by-Step:**

  **Step 1: Hash Code Calculation**
  ```java
  String key1 = "A";
  String key2 = "C";
  
  int hash1 = key1.hashCode();  // Example: 65
  int hash2 = key2.hashCode();  // Example: 67
  ```

  **Step 2: Bucket Index Calculation**
  ```java
  // HashMap capacity = 16 (buckets 0-15)
  int index1 = hash1 & (16 - 1);  // 65 & 15 = 1
  int index2 = hash2 & (16 - 1);  // 67 & 15 = 3
  
  // No collision - different buckets
  ```

  **Step 3: Collision Example**
  ```java
  String key3 = "Q";  // Assume hashCode = 81
  int index3 = 81 & 15;  // = 1 (same as key1!)
  
  // COLLISION! Both "A" and "Q" map to bucket 1
  ```

  **Collision Scenarios:**

  **Scenario 1: No Collision (Ideal Case)**
  ```java
  HashMap<String, Integer> map = new HashMap<>();
  map.put("A", 1);  // Calculates: hash("A") & 15 = 5
  map.put("B", 2);  // Calculates: hash("B") & 15 = 10
  
  // Internal structure:
  Bucket 0:  null
  Bucket 1:  null
  ...
  Bucket 5:  [A, 1] -> null
  ...
  Bucket 10: [B, 2] -> null
  ...
  Bucket 15: null
  
  // Each key has its own bucket - O(1) access
  ```

  **Scenario 2: Collision with Linked List (Chaining)**
  ```java
  map.put("C", 3);  // Calculates: hash("C") & 15 = 5 (collision with "A"!)
  
  // What happens:
  // 1. HashMap finds bucket 5 is already occupied by [A, 1]
  // 2. Creates a linked list: [A, 1] -> [C, 3]
  // 3. New entry is added to the end of the chain
  
  // Internal structure after collision:
  Bucket 5:  [A, 1] -> [C, 3] -> null  (Linked list)
  Bucket 10: [B, 2] -> null
  
  // Access time: O(n) where n = chain length
  ```

  **Scenario 3: Multiple Collisions**
  ```java
  map.put("D", 4);  // hash("D") & 15 = 5 (another collision!)
  map.put("E", 5);  // hash("E") & 15 = 5 (another collision!)
  
  // Chain grows longer:
  Bucket 5: [A, 1] -> [C, 3] -> [D, 4] -> [E, 5] -> null
  
  // To find "E", HashMap must:
  // 1. Go to bucket 5
  // 2. Traverse: A -> C -> D -> E (4 comparisons)
  // Performance degrades to O(n)
  ```

  **Collision Resolution Process (Detailed):**

  **When Adding a New Entry:**
  ```
  1. Calculate hash code:
     hash = key.hashCode()
     hash = hash ^ (hash >>> 16)  // Spread bits for better distribution
  
  2. Calculate bucket index:
     index = (capacity - 1) & hash
  
  3. Check bucket at index:
     
     Case A: Bucket is empty
     ├─> Create new Node(hash, key, value, null)
     └─> Place in bucket[index]
     
     Case B: Bucket has entries (collision!)
     ├─> Start at head of linked list/tree
     ├─> Traverse each node:
     │   ├─> Compare hash codes first (fast integer comparison)
     │   ├─> If hash matches, compare keys using equals() (slower)
     │   ├─> If key matches: UPDATE value, return old value
     │   └─> If key doesn't match: continue to next node
     └─> If reached end: ADD new node to chain
  ```

  **When Retrieving a Value:**
  ```java
  V value = map.get("C");
  
  // Step-by-step process:
  1. Calculate hash and index for "C"
     hash = "C".hashCode() ^ ("C".hashCode() >>> 16)
     index = hash & 15 = 5
  
  2. Go to bucket[5]
     Found: [A, 1] -> [C, 3] -> [D, 4] -> null
  
  3. Traverse the chain:
     Node 1: Compare "C" with "A"
             ├─> hash("C") != hash("A")? Check first (fast)
             └─> "C".equals("A")? No, continue to next
     
     Node 2: Compare "C" with "C"
             ├─> hash("C") == hash("C")? Yes
             ├─> "C".equals("C")? Yes, FOUND!
             └─> Return value: 3
  
  // Total comparisons: 2 (O(n) where n = chain length)
  ```

  **Why Hash Code is Checked Before equals():**
  ```java
  // HashMap optimization: Check hash first
  if (node.hash == hash &&  // Fast integer comparison
      (node.key == key || key.equals(node.key))) {  // Slower equals()
      // Found!
  }
  
  // This is faster because:
  // 1. Integer comparison (hash) is very fast
  // 2. equals() might be expensive (string comparison, object comparison)
  // 3. If hash doesn't match, skip equals() entirely
  ```

  **Code Example:**
  ```java
  // Simplified collision handling
  public V put(K key, V value) {
      int hash = hash(key);
      int index = (n - 1) & hash;
      
      Node<K,V> node = table[index];
      
      // No collision
      if (node == null) {
          table[index] = new Node<>(hash, key, value, null);
          return null;
      }
      
      // Collision - traverse linked list
      Node<K,V> prev = null;
      while (node != null) {
          // Key already exists - update value
          if (node.hash == hash && 
              (node.key == key || key.equals(node.key))) {
              V oldValue = node.value;
              node.value = value;
              return oldValue;
          }
          prev = node;
          node = node.next;
      }
      
      // Key doesn't exist - add new node
      prev.next = new Node<>(hash, key, value, null);
      return null;
  }
  ```

  **Why Collisions Happen:**
  1. **Poor hash function:** Different keys produce same hash code
  2. **Limited bucket size:** Multiple hash codes map to same bucket index
  3. **Hash code distribution:** Uneven distribution of hash codes

  **Example of Collision:**
  ```java
  class BadHashKey {
      String value;
      
      @Override
      public int hashCode() {
          return 1;  // Always returns 1 - terrible hash function!
      }
  }
  
  HashMap<BadHashKey, String> map = new HashMap<>();
  map.put(new BadHashKey("A"), "Value A");
  map.put(new BadHashKey("B"), "Value B");
  map.put(new BadHashKey("C"), "Value C");
  
  // All keys have same hash code
  // All stored in same bucket as linked list
  // Performance degrades to O(n)
  ```
  
</details>

<details>
  <summary>HashMap: Linked List vs Tree (Treeification)</summary>
  <br/>

  **Why HashMap Uses Trees:**

  When too many collisions occur in a single bucket, HashMap converts the linked list to a **Red-Black Tree** to improve performance from O(n) to O(log n).

  **Treeification Thresholds:**
  ```
  TREEIFY_THRESHOLD = 8      // Convert to tree when bucket has 8+ nodes
  UNTREEIFY_THRESHOLD = 6    // Convert back to list when bucket has 6- nodes
  MIN_TREEIFY_CAPACITY = 64  // Minimum capacity before treeification
  ```

  **When Treeification Happens:**
  1. Bucket has 8 or more nodes (linked list)
  2. HashMap capacity is at least 64
  3. If capacity < 64, HashMap resizes instead of treeifying

  **Visual Example:**
  ```
  // Before Treeification (Linked List)
  Bucket 5: [A,1] -> [B,2] -> [C,3] -> [D,4] -> [E,5] -> [F,6] -> [G,7] -> [H,8] -> null
  
  // After Treeification (Red-Black Tree)
  Bucket 5:     [D,4]
               /      \
           [B,2]      [F,6]
           /   \      /   \
        [A,1] [C,3] [E,5] [G,7]
                            \
                           [H,8]
  ```

  **Performance Comparison:**

  | Bucket Size | Linked List | Tree |
  |-------------|-------------|------|
  | 1-7 nodes | O(n) ≈ O(1-7) | Not used |
  | 8+ nodes | O(n) | O(log n) |
  | 100 nodes | O(100) | O(log 100) ≈ O(7) |
  | 1000 nodes | O(1000) | O(log 1000) ≈ O(10) |

  **Why Not Always Use Trees?**
  1. **Memory overhead:** Tree nodes use more memory than linked list nodes
  2. **Small lists:** For small lists (< 8), linked list is faster
  3. **Complexity:** Tree operations are more complex

  **Tree Node Structure:**
  ```java
  static final class TreeNode<K,V> extends LinkedHashMap.Entry<K,V> {
      TreeNode<K,V> parent;
      TreeNode<K,V> left;
      TreeNode<K,V> right;
      TreeNode<K,V> prev;
      boolean red;  // Red-Black Tree color
  }
  ```

  **Treeification Example:**
  ```java
  HashMap<String, Integer> map = new HashMap<>(64);
  
  // Create keys that collide (same bucket)
  // Assuming these keys hash to same bucket
  map.put("Key1", 1);
  map.put("Key2", 2);
  map.put("Key3", 3);
  map.put("Key4", 4);
  map.put("Key5", 5);
  map.put("Key6", 6);
  map.put("Key7", 7);
  map.put("Key8", 8);  // Triggers treeification
  
  // Bucket is now a Red-Black Tree
  // Search time: O(log 8) instead of O(8)
  ```

  **Untreeification (Tree to List):**
  ```java
  // When removing elements from tree
  map.remove("Key8");
  map.remove("Key7");
  map.remove("Key6");  // Triggers untreeification (6 nodes left)
  
  // Bucket converts back to linked list
  ```

  **Why Red-Black Tree?**
  + **Balanced:** Guarantees O(log n) operations
  + **Self-balancing:** Maintains balance during insertions/deletions
  + **Better than AVL:** Fewer rotations needed
  + **Comparable keys:** Requires keys to be Comparable or use Comparator
  
</details>

<details>
  <summary>HashMap Resizing (Rehashing)</summary>
  <br/>

  HashMap automatically resizes when the number of entries exceeds the threshold.

  **Resize Trigger:**
  ```
  threshold = capacity * loadFactor
  
  // Default values
  capacity = 16
  loadFactor = 0.75
  threshold = 16 * 0.75 = 12
  
  // Resize happens when size > 12
  ```

  **Resize Process:**
  1. Create new array with double capacity
  2. Rehash all existing entries
  3. Redistribute entries to new buckets
  4. Update threshold

  **Example:**
  ```java
  HashMap<String, Integer> map = new HashMap<>();  // Capacity: 16
  
  // Add 12 entries - no resize
  for (int i = 0; i < 12; i++) {
      map.put("Key" + i, i);
  }
  
  // Add 13th entry - triggers resize
  map.put("Key12", 12);  // Capacity: 32, Threshold: 24
  ```

  **Rehashing Process:**
  ```
  Old capacity: 16
  Old buckets:  [0-15]
  
  New capacity: 32
  New buckets:  [0-31]
  
  // Each entry is rehashed
  For each entry:
      newIndex = hash & (newCapacity - 1)
      Place entry in new bucket
  ```

  **Why Capacity is Power of 2:**
  ```java
  // Efficient modulo operation
  index = hash % capacity
  
  // When capacity is power of 2 (e.g., 16)
  index = hash & (capacity - 1)  // Faster bitwise AND
  
  // Example:
  hash = 25
  capacity = 16
  
  // Method 1: Modulo (slower)
  index = 25 % 16 = 9
  
  // Method 2: Bitwise AND (faster)
  index = 25 & 15 = 9
  
  // Binary:
  25 = 11001
  15 = 01111
  &  = 01001 = 9
  ```

  **Load Factor Impact:**

  | Load Factor | Space | Performance | Collisions |
  |-------------|-------|-------------|------------|
  | 0.5 | More space | Faster | Fewer |
  | 0.75 (default) | Balanced | Balanced | Moderate |
  | 1.0 | Less space | Slower | More |

  **Custom Load Factor:**
  ```java
  // Low load factor - fewer collisions, more space
  HashMap<String, Integer> map1 = new HashMap<>(16, 0.5f);
  
  // High load factor - more collisions, less space
  HashMap<String, Integer> map2 = new HashMap<>(16, 1.0f);
  ```

  **Resize Cost:**
  ```java
  // Resize is expensive: O(n)
  // All entries must be rehashed and moved
  
  // Bad: Multiple resizes
  HashMap<String, Integer> map = new HashMap<>();  // Capacity: 16
  for (int i = 0; i < 1000; i++) {
      map.put("Key" + i, i);  // Multiple resizes!
  }
  
  // Good: Specify initial capacity
  HashMap<String, Integer> map = new HashMap<>(1500);  // Capacity: 2048
  for (int i = 0; i < 1000; i++) {
      map.put("Key" + i, i);  // No resize needed
  }
  ```
  
</details>

<details>
  <summary>HashSet Internal Implementation</summary>
  <br/>

  HashSet is internally backed by a HashMap!

  **Internal Structure:**
  ```java
  public class HashSet<E> {
      private transient HashMap<E, Object> map;
      private static final Object PRESENT = new Object();
      
      public HashSet() {
          map = new HashMap<>();
      }
      
      public boolean add(E e) {
          return map.put(e, PRESENT) == null;
      }
      
      public boolean contains(Object o) {
          return map.containsKey(o);
      }
      
      public boolean remove(Object o) {
          return map.remove(o) == PRESENT;
      }
  }
  ```

  **How It Works:**
  + HashSet stores elements as **keys** in HashMap
  + All keys map to the same dummy value (`PRESENT`)
  + Uses HashMap's key uniqueness to ensure no duplicates

  **Example:**
  ```java
  HashSet<String> set = new HashSet<>();
  set.add("Apple");
  set.add("Banana");
  set.add("Apple");  // Duplicate, not added
  
  // Internally stored as:
  HashMap:
    "Apple"  -> PRESENT
    "Banana" -> PRESENT
  ```
  
</details>

<details>
  <summary>LinkedHashMap Internal Implementation</summary>
  <br/>

  LinkedHashMap extends HashMap and maintains insertion order using a doubly-linked list.

  **Internal Structure:**
  ```java
  public class LinkedHashMap<K,V> extends HashMap<K,V> {
      static class Entry<K,V> extends HashMap.Node<K,V> {
          Entry<K,V> before, after;  // Doubly-linked list pointers
      }
      
      transient Entry<K,V> head;  // First entry
      transient Entry<K,V> tail;  // Last entry
      final boolean accessOrder;  // false: insertion order, true: access order
  }
  ```

  **How It Maintains Order:**
  ```java
  // HashMap structure (for fast lookup)
  Bucket 0: null
  Bucket 1: [B, 2]
  Bucket 2: [C, 3]
  Bucket 3: [A, 1]
  
  // Linked list structure (for order)
  head -> [A, 1] <-> [B, 2] <-> [C, 3] <- tail
  ```

  **Insertion Order Example:**
  ```java
  LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
  map.put("C", 3);
  map.put("A", 1);
  map.put("B", 2);
  
  // Iteration order: C, A, B (insertion order)
  map.forEach((k, v) -> System.out.println(k + ": " + v));
  ```

  **Access Order (LRU Cache):**
  ```java
  LinkedHashMap<String, Integer> lruCache = 
      new LinkedHashMap<>(16, 0.75f, true) {  // accessOrder = true
          @Override
          protected boolean removeEldestEntry(Map.Entry<String, Integer> eldest) {
              return size() > 3;  // Max 3 entries
          }
      };
  
  lruCache.put("A", 1);
  lruCache.put("B", 2);
  lruCache.put("C", 3);
  
  lruCache.get("A");  // Access A - moves to end
  
  lruCache.put("D", 4);  // Removes B (least recently used)
  
  // Order: C, A, D
  ```
  
</details>

<details>
  <summary>TreeMap Internal Implementation</summary>
  <br/>

  TreeMap is implemented using a **Red-Black Tree** (self-balancing binary search tree).

  **Internal Structure:**
  ```java
  public class TreeMap<K,V> {
      private transient Entry<K,V> root;  // Root of tree
      private final Comparator<? super K> comparator;
      
      static final class Entry<K,V> {
          K key;
          V value;
          Entry<K,V> left;
          Entry<K,V> right;
          Entry<K,V> parent;
          boolean color = BLACK;  // Red-Black Tree color
      }
  }
  ```

  **Red-Black Tree Properties:**
  1. Every node is either red or black
  2. Root is always black
  3. All leaves (null) are black
  4. Red nodes cannot have red children
  5. All paths from root to leaves have same number of black nodes

  **Visual Example:**
  ```java
  TreeMap<Integer, String> map = new TreeMap<>();
  map.put(5, "Five");
  map.put(3, "Three");
  map.put(7, "Seven");
  map.put(1, "One");
  map.put(9, "Nine");
  
  // Internal tree structure:
          5(B)
         /    \
      3(R)    7(R)
      /         \
    1(B)        9(B)
  
  // B = Black, R = Red
  ```

  **Operations Complexity:**
  + **Get:** O(log n)
  + **Put:** O(log n)
  + **Remove:** O(log n)
  + **Iteration:** O(n)

  **Why Red-Black Tree?**
  + Guarantees O(log n) for all operations
  + Self-balancing (maintains height ≈ log n)
  + Better than AVL tree for insertions/deletions (fewer rotations)
  
</details>

<details>
  <summary>ConcurrentHashMap Internal Implementation</summary>
  <br/>

  ConcurrentHashMap uses **lock striping** and **CAS operations** for thread-safe concurrent access.

  **Java 7 Implementation (Segments):**
  ```java
  // Divided into segments (default 16)
  Segment 0: HashMap
  Segment 1: HashMap
  ...
  Segment 15: HashMap
  
  // Each segment has its own lock
  // Multiple threads can access different segments concurrently
  ```

  **Java 8+ Implementation (Node-level locking):**
  ```java
  public class ConcurrentHashMap<K,V> {
      transient volatile Node<K,V>[] table;
      
      static class Node<K,V> {
          final int hash;
          final K key;
          volatile V val;        // Volatile for visibility
          volatile Node<K,V> next;
      }
  }
  ```

  **Concurrency Mechanisms:**

  **1. CAS (Compare-And-Swap):**
  ```java
  // Atomic operation for updating values
  if (current_value == expected_value) {
      current_value = new_value;
      return true;
  }
  return false;
  ```

  **2. Synchronized Blocks:**
  ```java
  // Lock only the specific bucket during modification
  synchronized (bucket) {
      // Modify bucket
  }
  ```

  **3. Volatile Variables:**
  ```java
  // Ensures visibility across threads
  volatile Node<K,V>[] table;
  volatile V val;
  ```

  **Put Operation:**
  ```
  1. Calculate hash and bucket index
  2. If bucket is empty:
     - Use CAS to add new node (no lock needed)
  3. If bucket is not empty:
     - Synchronize on bucket head
     - Traverse and add/update node
  ```

  **Get Operation:**
  ```
  // No locking needed for reads!
  1. Calculate hash and bucket index
  2. Traverse bucket (linked list or tree)
  3. Return value
  
  // Uses volatile for visibility
  ```

  **Why ConcurrentHashMap is Fast:**
  + **Fine-grained locking:** Locks only specific buckets
  + **Lock-free reads:** No locking for get operations
  + **CAS operations:** Atomic updates without locks
  + **Multiple threads:** Can modify different buckets simultaneously
  
</details>


## Detailed Comparisons

<details>
  <summary>ArrayList vs LinkedList vs Vector</summary>
  <br/>

  **Detailed Comparison:**

  | Feature | ArrayList | LinkedList | Vector |
  |---------|-----------|------------|--------|
  | **Data Structure** | Dynamic array | Doubly-linked list | Dynamic array |
  | **Random Access** | O(1) - Fast | O(n) - Slow | O(1) - Fast |
  | **Insert at Beginning** | O(n) - Slow | O(1) - Fast | O(n) - Slow |
  | **Insert at End** | O(1) amortized | O(1) - Fast | O(1) amortized |
  | **Insert at Middle** | O(n) - Slow | O(n) - Slow | O(n) - Slow |
  | **Delete** | O(n) - Slow | O(1) if node known | O(n) - Slow |
  | **Memory Overhead** | Low (just array) | High (node objects) | Low (just array) |
  | **Cache Performance** | Excellent | Poor | Excellent |
  | **Thread Safety** | Not synchronized | Not synchronized | Synchronized |
  | **Growth Rate** | 50% (1.5x) | N/A | 100% (2x) |
  | **Initial Capacity** | 10 | N/A | 10 |
  | **Iterator** | Fail-fast | Fail-fast | Fail-fast |
  | **Since Version** | Java 1.2 | Java 1.2 | Java 1.0 (Legacy) |

  **Memory Comparison:**
  ```java
  // ArrayList: Stores only elements
  ArrayList: [E1][E2][E3][E4][E5]...
  Memory per element: ~4 bytes (reference)
  
  // LinkedList: Stores element + 2 pointers
  LinkedList: [prev|E1|next] <-> [prev|E2|next] <-> [prev|E3|next]
  Memory per element: ~24 bytes (object + 2 pointers)
  
  // Vector: Same as ArrayList but synchronized
  Vector: [E1][E2][E3][E4][E5]...
  Memory per element: ~4 bytes (reference)
  ```

  **Performance Benchmarks:**
  ```java
  // For 100,000 elements
  
  Operation              ArrayList    LinkedList    Vector
  ----------------       ---------    ----------    ------
  Add at end             1 ms         2 ms          3 ms
  Add at beginning       50 ms        1 ms          55 ms
  Get by index           0.1 ms       500 ms        0.1 ms
  Iterate all            2 ms         3 ms          4 ms
  Remove from middle     25 ms        250 ms        30 ms
  ```

  **When to Use:**

  **ArrayList:**
  + ✅ Random access is frequent
  + ✅ Iteration is common
  + ✅ Adding at end mostly
  + ✅ Memory efficiency is important
  + ❌ Frequent insertions/deletions at beginning

  **LinkedList:**
  + ✅ Frequent insertions/deletions at beginning/end
  + ✅ Implementing queue/deque
  + ✅ No random access needed
  + ❌ Random access is frequent
  + ❌ Memory is limited

  **Vector:**
  + ✅ Thread safety is required (but prefer Collections.synchronizedList())
  + ✅ Legacy code compatibility
  + ❌ Modern applications (use ArrayList + synchronization)

  **Code Examples:**
  ```java
  // ArrayList - Best for random access
  List<String> arrayList = new ArrayList<>();
  arrayList.add("A");
  arrayList.add("B");
  String item = arrayList.get(0);  // O(1) - Fast!
  
  // LinkedList - Best for queue operations
  LinkedList<String> linkedList = new LinkedList<>();
  linkedList.addFirst("A");  // O(1) - Fast!
  linkedList.addLast("B");   // O(1) - Fast!
  String first = linkedList.removeFirst();  // O(1) - Fast!
  
  // Vector - Thread-safe but slower
  List<String> vector = new Vector<>();
  vector.add("A");  // Synchronized - slower
  
  // Better alternative to Vector
  List<String> syncList = Collections.synchronizedList(new ArrayList<>());
  ```
  
</details>

<details>
  <summary>HashSet vs LinkedHashSet vs TreeSet</summary>
  <br/>

  **Detailed Comparison:**

  | Feature | HashSet | LinkedHashSet | TreeSet |
  |---------|---------|---------------|---------|
  | **Data Structure** | HashMap | HashMap + LinkedList | Red-Black Tree |
  | **Ordering** | No order | Insertion order | Sorted (natural/custom) |
  | **Add** | O(1) | O(1) | O(log n) |
  | **Remove** | O(1) | O(1) | O(log n) |
  | **Contains** | O(1) | O(1) | O(log n) |
  | **Iteration** | O(n) - random order | O(n) - insertion order | O(n) - sorted order |
  | **Null Elements** | One null allowed | One null allowed | No null (with Comparator: maybe) |
  | **Memory** | Low | Medium (extra pointers) | Medium (tree nodes) |
  | **Thread Safety** | Not synchronized | Not synchronized | Not synchronized |
  | **Duplicates** | Not allowed | Not allowed | Not allowed |
  | **Comparable Required** | No | No | Yes (or Comparator) |
  | **Since Version** | Java 1.2 | Java 1.4 | Java 1.2 |

  **Internal Structure:**
  ```java
  // HashSet: Uses HashMap internally
  HashSet {
      HashMap<E, Object> map;
      // Elements stored as keys, dummy PRESENT as value
  }
  
  // LinkedHashSet: HashMap + Doubly-linked list
  LinkedHashSet {
      HashMap<E, Object> map;
      Entry<E> head, tail;  // Maintains insertion order
  }
  
  // TreeSet: Red-Black Tree
  TreeSet {
      TreeMap<E, Object> map;
      // Elements stored in sorted tree structure
  }
  ```

  **Performance Comparison:**
  ```java
  // For 100,000 elements
  
  Operation              HashSet    LinkedHashSet    TreeSet
  ----------------       -------    -------------    -------
  Add                    10 ms      12 ms            45 ms
  Contains               8 ms       9 ms             35 ms
  Remove                 9 ms       10 ms            40 ms
  Iterate (ordered)      N/A        15 ms            20 ms
  First/Last element     N/A        N/A              O(log n)
  Range operations       N/A        N/A              O(log n)
  ```

  **When to Use:**

  **HashSet:**
  + ✅ Need fast add/remove/contains
  + ✅ Order doesn't matter
  + ✅ Memory efficiency is important
  + ❌ Need sorted or insertion order

  **LinkedHashSet:**
  + ✅ Need insertion order
  + ✅ Predictable iteration order
  + ✅ Cache-friendly iteration
  + ❌ Need sorted order
  + ❌ Memory is very limited

  **TreeSet:**
  + ✅ Need sorted order
  + ✅ Need range operations (subSet, headSet, tailSet)
  + ✅ Need first/last element access
  + ❌ Performance is critical
  + ❌ Elements are not Comparable

  **Code Examples:**
  ```java
  // HashSet - No order
  Set<String> hashSet = new HashSet<>();
  hashSet.add("Banana");
  hashSet.add("Apple");
  hashSet.add("Cherry");
  System.out.println(hashSet);  // Random order: [Cherry, Apple, Banana]
  
  // LinkedHashSet - Insertion order
  Set<String> linkedSet = new LinkedHashSet<>();
  linkedSet.add("Banana");
  linkedSet.add("Apple");
  linkedSet.add("Cherry");
  System.out.println(linkedSet);  // [Banana, Apple, Cherry]
  
  // TreeSet - Sorted order
  Set<String> treeSet = new TreeSet<>();
  treeSet.add("Banana");
  treeSet.add("Apple");
  treeSet.add("Cherry");
  System.out.println(treeSet);  // [Apple, Banana, Cherry]
  
  // TreeSet - Range operations
  TreeSet<Integer> numbers = new TreeSet<>(Arrays.asList(1, 5, 10, 15, 20));
  System.out.println(numbers.subSet(5, 15));   // [5, 10]
  System.out.println(numbers.headSet(10));     // [1, 5]
  System.out.println(numbers.tailSet(10));     // [10, 15, 20]
  System.out.println(numbers.first());         // 1
  System.out.println(numbers.last());          // 20
  ```
  
</details>

<details>
  <summary>HashMap vs LinkedHashMap vs TreeMap vs Hashtable</summary>
  <br/>

  **Detailed Comparison:**

  | Feature | HashMap | LinkedHashMap | TreeMap | Hashtable |
  |---------|---------|---------------|---------|-----------|
  | **Data Structure** | Hash table | Hash table + LinkedList | Red-Black Tree | Hash table |
  | **Ordering** | No order | Insertion/Access order | Sorted by keys | No order |
  | **Get** | O(1) | O(1) | O(log n) | O(1) |
  | **Put** | O(1) | O(1) | O(log n) | O(1) |
  | **Remove** | O(1) | O(1) | O(log n) | O(1) |
  | **Iteration** | O(n) random | O(n) ordered | O(n) sorted | O(n) random |
  | **Null Key** | One null | One null | No null | No null |
  | **Null Values** | Allowed | Allowed | Allowed | Not allowed |
  | **Thread Safety** | Not synchronized | Not synchronized | Not synchronized | Synchronized |
  | **Memory** | Low | Medium | Medium | Low |
  | **Initial Capacity** | 16 | 16 | N/A | 11 |
  | **Load Factor** | 0.75 | 0.75 | N/A | 0.75 |
  | **Since Version** | Java 1.2 | Java 1.4 | Java 1.2 | Java 1.0 (Legacy) |

  **Performance Comparison:**
  ```java
  // For 100,000 entries
  
  Operation              HashMap    LinkedHashMap    TreeMap    Hashtable
  ----------------       -------    -------------    -------    ---------
  Put                    15 ms      18 ms            60 ms      25 ms
  Get                    10 ms      12 ms            45 ms      18 ms
  Remove                 12 ms      14 ms            50 ms      20 ms
  Iterate (ordered)      N/A        20 ms            25 ms      N/A
  ContainsKey            10 ms      11 ms            45 ms      18 ms
  ```

  **Memory Overhead:**
  ```java
  // HashMap: Entry objects only
  HashMap: Bucket -> [Entry] -> [Entry] -> null
  Memory per entry: ~32 bytes
  
  // LinkedHashMap: Entry + before/after pointers
  LinkedHashMap: Bucket -> [Entry with before/after] -> ...
  Memory per entry: ~40 bytes
  
  // TreeMap: TreeNode with parent/left/right/color
  TreeMap: TreeNode with 4 pointers + color
  Memory per entry: ~48 bytes
  
  // Hashtable: Same as HashMap but synchronized
  Hashtable: Bucket -> [Entry] -> [Entry] -> null
  Memory per entry: ~32 bytes
  ```

  **When to Use:**

  **HashMap:**
  + ✅ Need fast key-value lookups
  + ✅ Order doesn't matter
  + ✅ Single-threaded or external synchronization
  + ✅ Most common use case
  + ❌ Need ordering

  **LinkedHashMap:**
  + ✅ Need insertion order or access order
  + ✅ Implementing LRU cache
  + ✅ Predictable iteration order
  + ❌ Memory is very limited

  **TreeMap:**
  + ✅ Need sorted keys
  + ✅ Need range operations
  + ✅ Need first/last key access
  + ❌ Performance is critical

  **Hashtable:**
  + ✅ Legacy code compatibility
  + ✅ Thread safety (but prefer ConcurrentHashMap)
  + ❌ Modern applications

  **Code Examples:**
  ```java
  // HashMap - No order, fastest
  Map<String, Integer> hashMap = new HashMap<>();
  hashMap.put("Banana", 2);
  hashMap.put("Apple", 1);
  hashMap.put("Cherry", 3);
  hashMap.forEach((k, v) -> System.out.println(k + ": " + v));
  // Random order: Cherry: 3, Apple: 1, Banana: 2
  
  // LinkedHashMap - Insertion order
  Map<String, Integer> linkedMap = new LinkedHashMap<>();
  linkedMap.put("Banana", 2);
  linkedMap.put("Apple", 1);
  linkedMap.put("Cherry", 3);
  linkedMap.forEach((k, v) -> System.out.println(k + ": " + v));
  // Insertion order: Banana: 2, Apple: 1, Cherry: 3
  
  // LinkedHashMap - Access order (LRU Cache)
  Map<String, Integer> lruCache = new LinkedHashMap<>(16, 0.75f, true) {
      @Override
      protected boolean removeEldestEntry(Map.Entry<String, Integer> eldest) {
          return size() > 3;
      }
  };
  lruCache.put("A", 1);
  lruCache.put("B", 2);
  lruCache.put("C", 3);
  lruCache.get("A");  // Access A - moves to end
  lruCache.put("D", 4);  // Removes B (least recently used)
  // Order: C, A, D
  
  // TreeMap - Sorted order
  Map<String, Integer> treeMap = new TreeMap<>();
  treeMap.put("Banana", 2);
  treeMap.put("Apple", 1);
  treeMap.put("Cherry", 3);
  treeMap.forEach((k, v) -> System.out.println(k + ": " + v));
  // Sorted order: Apple: 1, Banana: 2, Cherry: 3
  
  // TreeMap - Range operations
  TreeMap<Integer, String> numbers = new TreeMap<>();
  numbers.put(1, "One");
  numbers.put(5, "Five");
  numbers.put(10, "Ten");
  numbers.put(15, "Fifteen");
  
  System.out.println(numbers.subMap(5, 15));     // {5=Five, 10=Ten}
  System.out.println(numbers.firstKey());        // 1
  System.out.println(numbers.lastKey());         // 15
  System.out.println(numbers.higherKey(5));      // 10
  System.out.println(numbers.lowerKey(10));      // 5
  
  // Hashtable - Thread-safe but legacy
  Map<String, Integer> hashtable = new Hashtable<>();
  hashtable.put("Apple", 1);  // Synchronized
  // hashtable.put(null, 1);  // NullPointerException
  
  // Better alternative to Hashtable
  Map<String, Integer> concurrentMap = new ConcurrentHashMap<>();
  ```
  
</details>

<details>
  <summary>HashMap vs ConcurrentHashMap</summary>
  <br/>

  **Detailed Comparison:**

  | Feature | HashMap | ConcurrentHashMap |
  |---------|---------|-------------------|
  | **Thread Safety** | Not thread-safe | Thread-safe |
  | **Locking Mechanism** | None | Fine-grained locking (bucket-level) |
  | **Null Key** | One null allowed | Not allowed |
  | **Null Values** | Allowed | Not allowed |
  | **Performance (Single Thread)** | Faster | Slightly slower |
  | **Performance (Multi Thread)** | Poor (needs external sync) | Excellent |
  | **Iterator** | Fail-fast | Weakly consistent |
  | **Size Method** | O(1) | O(n) in Java 7, O(1) in Java 8+ |
  | **Concurrent Reads** | Not safe | Safe, no locking |
  | **Concurrent Writes** | Not safe | Safe, minimal locking |
  | **Memory** | Lower | Higher (additional sync structures) |

  **Concurrency Comparison:**
  ```java
  // HashMap - Not thread-safe
  Map<String, Integer> hashMap = new HashMap<>();
  
  // Thread 1
  hashMap.put("A", 1);
  
  // Thread 2 (concurrent)
  hashMap.put("B", 2);  // Can cause data corruption!
  
  // Possible issues:
  // 1. Lost updates
  // 2. Infinite loop (Java 7)
  // 3. ConcurrentModificationException during iteration
  
  // ConcurrentHashMap - Thread-safe
  Map<String, Integer> concurrentMap = new ConcurrentHashMap<>();
  
  // Thread 1
  concurrentMap.put("A", 1);  // Safe
  
  // Thread 2 (concurrent)
  concurrentMap.put("B", 2);  // Safe, no data corruption
  ```

  **Locking Mechanism:**
  ```java
  // HashMap: No locking
  // All threads access same data structure
  // External synchronization needed
  
  synchronized (hashMap) {
      hashMap.put("A", 1);  // Locks entire map
  }
  
  // ConcurrentHashMap: Fine-grained locking
  // Java 7: Segment-level locking (16 segments)
  Segment 0: [Bucket 0-3]   <- Lock 0
  Segment 1: [Bucket 4-7]   <- Lock 1
  ...
  Segment 15: [Bucket 60-63] <- Lock 15
  
  // Java 8+: Bucket-level locking
  Bucket 0: <- Lock only this bucket during write
  Bucket 1: <- Different lock
  ...
  
  // Multiple threads can write to different buckets simultaneously!
  ```

  **Performance Comparison:**
  ```java
  // Single Thread (1 thread, 1M operations)
  HashMap:            100 ms
  ConcurrentHashMap:  120 ms  (20% slower due to sync overhead)
  
  // Multi Thread (10 threads, 1M operations each)
  HashMap (synchronized):     5000 ms  (all threads wait for lock)
  ConcurrentHashMap:          800 ms   (6x faster!)
  ```

  **Iterator Behavior:**
  ```java
  // HashMap - Fail-fast iterator
  Map<String, Integer> hashMap = new HashMap<>();
  hashMap.put("A", 1);
  hashMap.put("B", 2);
  
  for (String key : hashMap.keySet()) {
      hashMap.put("C", 3);  // ConcurrentModificationException!
  }
  
  // ConcurrentHashMap - Weakly consistent iterator
  Map<String, Integer> concurrentMap = new ConcurrentHashMap<>();
  concurrentMap.put("A", 1);
  concurrentMap.put("B", 2);
  
  for (String key : concurrentMap.keySet()) {
      concurrentMap.put("C", 3);  // No exception, may or may not see "C"
  }
  ```

  **When to Use:**

  **HashMap:**
  + ✅ Single-threaded application
  + ✅ External synchronization available
  + ✅ Performance is critical
  + ✅ Null keys/values needed
  + ❌ Multiple threads accessing

  **ConcurrentHashMap:**
  + ✅ Multi-threaded application
  + ✅ High concurrency
  + ✅ Frequent reads and writes
  + ✅ No external synchronization wanted
  + ❌ Need null keys/values

  **Code Examples:**
  ```java
  // HashMap with external synchronization
  Map<String, Integer> syncMap = Collections.synchronizedMap(new HashMap<>());
  
  // Still need to synchronize iteration
  synchronized (syncMap) {
      for (Map.Entry<String, Integer> entry : syncMap.entrySet()) {
          System.out.println(entry.getKey() + ": " + entry.getValue());
      }
  }
  
  // ConcurrentHashMap - No external synchronization needed
  Map<String, Integer> concurrentMap = new ConcurrentHashMap<>();
  
  // Thread-safe operations
  concurrentMap.put("A", 1);
  concurrentMap.putIfAbsent("B", 2);
  concurrentMap.computeIfAbsent("C", k -> 3);
  concurrentMap.merge("A", 5, Integer::sum);  // A = 1 + 5 = 6
  
  // Safe iteration (no synchronization needed)
  concurrentMap.forEach((k, v) -> System.out.println(k + ": " + v));
  
  // Atomic operations
  concurrentMap.compute("A", (k, v) -> v == null ? 1 : v + 1);
  concurrentMap.merge("B", 1, Integer::sum);
  
  // Bulk operations (Java 8+)
  concurrentMap.forEach(1, (k, v) -> System.out.println(k + ": " + v));
  Integer sum = concurrentMap.reduce(1, (k, v) -> v, Integer::sum);
  ```
  
</details>

<details>
  <summary>ArrayList vs CopyOnWriteArrayList</summary>
  <br/>

  **Detailed Comparison:**

  | Feature | ArrayList | CopyOnWriteArrayList |
  |---------|-----------|----------------------|
  | **Thread Safety** | Not thread-safe | Thread-safe |
  | **Modification Strategy** | In-place | Copy entire array |
  | **Read Performance** | O(1) - Fast | O(1) - Fast |
  | **Write Performance** | O(1) amortized | O(n) - Slow |
  | **Iterator** | Fail-fast | Snapshot (never fails) |
  | **Memory** | Low | High (copies on write) |
  | **Best For** | Single thread | Read-heavy, few writes |
  | **Concurrent Reads** | Not safe | Safe, no locking |
  | **Concurrent Writes** | Not safe | Safe, but expensive |

  **How CopyOnWriteArrayList Works:**
  ```java
  // On write operation:
  1. Lock the list
  2. Create a copy of entire array
  3. Modify the copy
  4. Replace old array with new copy
  5. Unlock
  
  // Example:
  CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
  list.add("A");  // Array: ["A"]
  list.add("B");  // Creates new array: ["A", "B"]
  list.add("C");  // Creates new array: ["A", "B", "C"]
  
  // Each add creates a new array!
  ```

  **Performance Comparison:**
  ```java
  // Read operations (1M reads)
  ArrayList:                50 ms
  CopyOnWriteArrayList:     50 ms  (Same performance)
  
  // Write operations (10K writes)
  ArrayList:                10 ms
  CopyOnWriteArrayList:     5000 ms  (500x slower!)
  
  // Mixed (90% reads, 10% writes, 100K operations)
  ArrayList (synchronized): 800 ms
  CopyOnWriteArrayList:     600 ms  (Better for read-heavy)
  ```

  **When to Use:**

  **ArrayList:**
  + ✅ Single-threaded
  + ✅ Frequent modifications
  + ✅ Balanced read/write ratio
  + ❌ Multi-threaded access

  **CopyOnWriteArrayList:**
  + ✅ Multi-threaded
  + ✅ Read-heavy workload (90%+ reads)
  + ✅ Rare modifications
  + ✅ Iteration during modification
  + ❌ Frequent writes
  + ❌ Large lists

  **Code Examples:**
  ```java
  // ArrayList - Fail-fast iterator
  List<String> arrayList = new ArrayList<>(Arrays.asList("A", "B", "C"));
  
  for (String item : arrayList) {
      arrayList.add("D");  // ConcurrentModificationException!
  }
  
  // CopyOnWriteArrayList - Safe iteration
  List<String> cowList = new CopyOnWriteArrayList<>(Arrays.asList("A", "B", "C"));
  
  for (String item : cowList) {
      cowList.add("D");  // No exception! Iterator uses snapshot
  }
  
  // Use case: Event listeners
  CopyOnWriteArrayList<EventListener> listeners = new CopyOnWriteArrayList<>();
  
  // Add listeners (rare operation)
  listeners.add(new EventListener());
  
  // Notify all listeners (frequent operation)
  for (EventListener listener : listeners) {
      listener.onEvent();  // Safe even if listeners are added/removed
  }
  ```
  
</details>

<details>
  <summary>PriorityQueue vs ArrayDeque vs LinkedList</summary>
  <br/>

  **Detailed Comparison:**

  | Feature | PriorityQueue | ArrayDeque | LinkedList |
  |---------|---------------|------------|------------|
  | **Data Structure** | Binary heap | Circular array | Doubly-linked list |
  | **Ordering** | Priority order | FIFO/LIFO | FIFO/LIFO |
  | **Add** | O(log n) | O(1) amortized | O(1) |
  | **Remove** | O(log n) | O(1) | O(1) |
  | **Peek** | O(1) | O(1) | O(1) |
  | **Get by index** | Not supported | Not supported | O(n) |
  | **Null elements** | Not allowed | Not allowed | Allowed |
  | **Thread Safety** | Not synchronized | Not synchronized | Not synchronized |
  | **Memory** | Low | Low | High (node objects) |
  | **Use Case** | Priority-based | Stack/Queue | Queue/Deque/List |

  **When to Use:**

  **PriorityQueue:**
  + ✅ Need elements in priority order
  + ✅ Always need min/max element
  + ✅ Task scheduling
  + ❌ Need FIFO order

  **ArrayDeque:**
  + ✅ Need stack (LIFO)
  + ✅ Need queue (FIFO)
  + ✅ Performance is critical
  + ✅ Memory efficiency important
  + ❌ Need random access

  **LinkedList:**
  + ✅ Need List operations
  + ✅ Frequent insertions at both ends
  + ✅ Need null elements
  + ❌ Memory is limited

  **Code Examples:**
  ```java
  // PriorityQueue - Priority order
  Queue<Integer> pq = new PriorityQueue<>();
  pq.offer(5);
  pq.offer(2);
  pq.offer(8);
  System.out.println(pq.poll());  // 2 (min element)
  System.out.println(pq.poll());  // 5
  System.out.println(pq.poll());  // 8
  
  // ArrayDeque - Fast stack/queue
  Deque<String> deque = new ArrayDeque<>();
  deque.push("A");  // Stack operation
  deque.push("B");
  System.out.println(deque.pop());  // B (LIFO)
  
  deque.offer("C");  // Queue operation
  deque.offer("D");
  System.out.println(deque.poll());  // A (FIFO)
  
  // LinkedList - Flexible but slower
  LinkedList<String> list = new LinkedList<>();
  list.addFirst("A");
  list.addLast("B");
  list.add(1, "C");  // Can insert at index
  System.out.println(list.get(1));  // C
  ```
  
</details>
