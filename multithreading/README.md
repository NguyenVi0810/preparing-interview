
## Core Concepts

<details>
  <summary>Thread Lifecycle: States and Transitions</summary>
  <br/>

  A thread in Java goes through various states during its lifetime. Understanding these states is crucial for effective multithreading.

  **Thread States:**

  ```
  NEW → RUNNABLE → RUNNING → TERMINATED
           ↓           ↓
        BLOCKED    WAITING/TIMED_WAITING
           ↓           ↓
        RUNNABLE ← RUNNABLE
  ```

  **1. NEW (Born State)**
  + Thread has been created but not yet started
  + `Thread t = new Thread()` creates a thread in NEW state
  + No system resources allocated yet

  ```java
  Thread thread = new Thread(() -> System.out.println("Hello"));
  System.out.println(thread.getState());  // NEW
  ```

  **2. RUNNABLE (Ready State)**
  + Thread is ready to run and waiting for CPU time
  + Entered by calling `start()` method
  + Thread scheduler decides when to run it
  + May be running or waiting for CPU time slice

  ```java
  Thread thread = new Thread(() -> System.out.println("Hello"));
  thread.start();  // Moves to RUNNABLE state
  System.out.println(thread.getState());  // RUNNABLE
  ```

  **3. RUNNING (Executing State)**
  + Thread is currently executing
  + Thread scheduler has allocated CPU time
  + Not a separate state in Java (part of RUNNABLE)

  **4. BLOCKED (Blocked State)**
  + Thread is waiting to acquire a monitor lock
  + Happens when trying to enter a synchronized block/method that's locked by another thread
  + Automatically moves to RUNNABLE when lock is acquired

  ```java
  public class BlockedExample {
      private static final Object lock = new Object();
      
      public static void main(String[] args) throws InterruptedException {
          Thread t1 = new Thread(() -> {
              synchronized (lock) {
                  try {
                      Thread.sleep(5000);  // Hold lock for 5 seconds
                  } catch (InterruptedException e) {
                      e.printStackTrace();
                  }
              }
          });
          
          Thread t2 = new Thread(() -> {
              synchronized (lock) {  // Will be BLOCKED waiting for lock
                  System.out.println("Got the lock");
              }
          });
          
          t1.start();
          Thread.sleep(100);  // Let t1 acquire lock first
          t2.start();
          Thread.sleep(100);
          
          System.out.println("t2 state: " + t2.getState());  // BLOCKED
      }
  }
  ```

  **5. WAITING (Waiting State)**
  + Thread is waiting indefinitely for another thread to perform a specific action
  + Entered by calling:
    + `Object.wait()` without timeout
    + `Thread.join()` without timeout
    + `LockSupport.park()`

  ```java
  public class WaitingExample {
      private static final Object lock = new Object();
      
      public static void main(String[] args) throws InterruptedException {
          Thread t1 = new Thread(() -> {
              synchronized (lock) {
                  try {
                      lock.wait();  // Enters WAITING state
                  } catch (InterruptedException e) {
                      e.printStackTrace();
                  }
              }
          });
          
          t1.start();
          Thread.sleep(100);
          System.out.println("t1 state: " + t1.getState());  // WAITING
          
          synchronized (lock) {
              lock.notify();  // Wake up t1
          }
      }
  }
  ```

  **6. TIMED_WAITING (Timed Waiting State)**
  + Thread is waiting for a specified period of time
  + Entered by calling:
    + `Thread.sleep(milliseconds)`
    + `Object.wait(timeout)`
    + `Thread.join(timeout)`
    + `LockSupport.parkNanos()`
    + `LockSupport.parkUntil()`

  ```java
  Thread thread = new Thread(() -> {
      try {
          Thread.sleep(5000);  // TIMED_WAITING for 5 seconds
      } catch (InterruptedException e) {
          e.printStackTrace();
      }
  });
  
  thread.start();
  Thread.sleep(100);
  System.out.println(thread.getState());  // TIMED_WAITING
  ```

  **7. TERMINATED (Dead State)**
  + Thread has completed execution
  + Either finished normally or terminated due to exception
  + Cannot be restarted

  ```java
  Thread thread = new Thread(() -> System.out.println("Done"));
  thread.start();
  thread.join();  // Wait for completion
  System.out.println(thread.getState());  // TERMINATED
  ```

  **State Transition Diagram:**

  ```
  NEW
   |
   | start()
   ↓
  RUNNABLE ←──────────────────────────────────┐
   |                                           |
   | acquire lock / notify() / sleep ends      |
   ↓                                           |
  RUNNING                                      |
   |                                           |
   ├─→ synchronized block locked → BLOCKED ───┘
   |
   ├─→ wait() / join() → WAITING ─────────────┘
   |
   ├─→ sleep() / wait(timeout) → TIMED_WAITING ┘
   |
   ├─→ run() completes → TERMINATED
   |
   └─→ exception → TERMINATED
  ```

  **Complete Example:**

  ```java
  public class ThreadLifecycleDemo {
      public static void main(String[] args) throws InterruptedException {
          Thread thread = new Thread(() -> {
              System.out.println("Thread started");
              
              try {
                  // TIMED_WAITING
                  Thread.sleep(1000);
                  
                  synchronized (ThreadLifecycleDemo.class) {
                      // WAITING
                      ThreadLifecycleDemo.class.wait(500);
                  }
              } catch (InterruptedException e) {
                  e.printStackTrace();
              }
              
              System.out.println("Thread ending");
          });
          
          System.out.println("State after creation: " + thread.getState());  // NEW
          
          thread.start();
          System.out.println("State after start(): " + thread.getState());   // RUNNABLE
          
          Thread.sleep(500);
          System.out.println("State during sleep: " + thread.getState());    // TIMED_WAITING
          
          thread.join();
          System.out.println("State after completion: " + thread.getState()); // TERMINATED
      }
  }
  ```

  **Key Points:**
  + A thread can only be started once (calling `start()` twice throws `IllegalThreadStateException`)
  + `BLOCKED` vs `WAITING`: BLOCKED is waiting for a monitor lock, WAITING is waiting for notification
  + `WAITING` vs `TIMED_WAITING`: WAITING waits indefinitely, TIMED_WAITING waits for a specific time
  + Once TERMINATED, a thread cannot be restarted
  
</details>

<details>
  <summary>Thread Creation: Thread Class vs Runnable vs Callable</summary>
  <br/>

  Java provides three main ways to create threads, each with different use cases and advantages.

  **1. Extending Thread Class**

  ```java
  public class MyThread extends Thread {
      @Override
      public void run() {
          System.out.println("Thread running: " + Thread.currentThread().getName());
          for (int i = 0; i < 5; i++) {
              System.out.println(i);
              try {
                  Thread.sleep(500);
              } catch (InterruptedException e) {
                  e.printStackTrace();
              }
          }
      }
  }
  
  // Usage
  public class Main {
      public static void main(String[] args) {
          MyThread thread1 = new MyThread();
          MyThread thread2 = new MyThread();
          
          thread1.start();  // Start thread
          thread2.start();
          
          // thread1.run();  // DON'T do this - runs in main thread, not new thread
      }
  }
  ```

  **Pros:**
  + Simple and straightforward
  + Can override other Thread methods if needed

  **Cons:**
  + Cannot extend another class (Java single inheritance)
  + Tight coupling with Thread class
  + Not recommended for most use cases

  **2. Implementing Runnable Interface**

  ```java
  public class MyRunnable implements Runnable {
      @Override
      public void run() {
          System.out.println("Runnable running: " + Thread.currentThread().getName());
          for (int i = 0; i < 5; i++) {
              System.out.println(i);
              try {
                  Thread.sleep(500);
              } catch (InterruptedException e) {
                  e.printStackTrace();
              }
          }
      }
  }
  
  // Usage
  public class Main {
      public static void main(String[] args) {
          MyRunnable runnable = new MyRunnable();
          
          Thread thread1 = new Thread(runnable);
          Thread thread2 = new Thread(runnable);
          
          thread1.start();
          thread2.start();
      }
  }
  ```

  **Using Lambda (Java 8+):**
  ```java
  public class Main {
      public static void main(String[] args) {
          // Lambda expression
          Thread thread = new Thread(() -> {
              System.out.println("Lambda thread running");
              for (int i = 0; i < 5; i++) {
                  System.out.println(i);
              }
          });
          
          thread.start();
      }
  }
  ```

  **Pros:**
  + Can extend other classes
  + Better object-oriented design (separation of task from thread)
  + Can share same Runnable instance across multiple threads
  + Preferred approach in most cases

  **Cons:**
  + Cannot return a result
  + Cannot throw checked exceptions

  **3. Implementing Callable Interface**

  Callable is similar to Runnable but can return a result and throw checked exceptions.

  ```java
  import java.util.concurrent.Callable;
  import java.util.concurrent.ExecutorService;
  import java.util.concurrent.Executors;
  import java.util.concurrent.Future;
  
  public class MyCallable implements Callable<Integer> {
      private int number;
      
      public MyCallable(int number) {
          this.number = number;
      }
      
      @Override
      public Integer call() throws Exception {
          System.out.println("Calculating sum for: " + number);
          int sum = 0;
          for (int i = 1; i <= number; i++) {
              sum += i;
              Thread.sleep(100);  // Simulate work
          }
          return sum;
      }
  }
  
  // Usage
  public class Main {
      public static void main(String[] args) {
          ExecutorService executor = Executors.newFixedThreadPool(2);
          
          // Submit Callable tasks
          Future<Integer> future1 = executor.submit(new MyCallable(10));
          Future<Integer> future2 = executor.submit(new MyCallable(20));
          
          try {
              // Get results (blocks until computation completes)
              Integer result1 = future1.get();
              Integer result2 = future2.get();
              
              System.out.println("Result 1: " + result1);  // 55
              System.out.println("Result 2: " + result2);  // 210
          } catch (Exception e) {
              e.printStackTrace();
          } finally {
              executor.shutdown();
          }
      }
  }
  ```

  **Using Lambda with Callable:**
  ```java
  ExecutorService executor = Executors.newSingleThreadExecutor();
  
  Future<String> future = executor.submit(() -> {
      Thread.sleep(1000);
      return "Result from Callable";
  });
  
  try {
      String result = future.get();  // Blocks until result is available
      System.out.println(result);
  } catch (Exception e) {
      e.printStackTrace();
  } finally {
      executor.shutdown();
  }
  ```

  **Pros:**
  + Can return a result
  + Can throw checked exceptions
  + Works with ExecutorService for better thread management

  **Cons:**
  + Requires ExecutorService (cannot use with Thread directly)
  + Slightly more complex than Runnable

  **Comparison Table:**

  | Feature | Thread | Runnable | Callable |
  |---------|--------|----------|----------|
  | Return value | No | No | Yes (generic type) |
  | Checked exceptions | No | No | Yes |
  | Inheritance | Extends Thread | Can extend other classes | Can extend other classes |
  | Usage | `thread.start()` | `new Thread(runnable).start()` | `executor.submit(callable)` |
  | Method | `run()` | `run()` | `call()` |
  | Result retrieval | N/A | N/A | `Future.get()` |

  **When to Use:**

  + **Thread class:** Rarely - only when you need to override Thread behavior
  + **Runnable:** Most common - fire-and-forget tasks, no return value needed
  + **Callable:** When you need a return value or exception handling

  **Best Practice Example:**

  ```java
  public class ThreadCreationBestPractices {
      public static void main(String[] args) {
          // 1. Simple task without return value - use Runnable
          Thread simpleThread = new Thread(() -> {
              System.out.println("Simple task");
          });
          simpleThread.start();
          
          // 2. Task with return value - use Callable with ExecutorService
          ExecutorService executor = Executors.newFixedThreadPool(3);
          
          Future<Integer> future = executor.submit(() -> {
              return 42;
          });
          
          try {
              Integer result = future.get();
              System.out.println("Result: " + result);
          } catch (Exception e) {
              e.printStackTrace();
          }
          
          // 3. Multiple tasks - use ExecutorService
          for (int i = 0; i < 5; i++) {
              final int taskId = i;
              executor.submit(() -> {
                  System.out.println("Task " + taskId + " executing");
              });
          }
          
          executor.shutdown();
      }
  }
  ```
  
</details>

<details>
  <summary>Synchronization: synchronized Keyword, Locks, Monitors</summary>
  <br/>

  Synchronization is the mechanism to control access to shared resources by multiple threads to prevent race conditions and data inconsistency.

  **Why Synchronization is Needed:**

  ```java
  // WITHOUT synchronization - Race condition
  public class Counter {
      private int count = 0;
      
      public void increment() {
          count++;  // Not atomic! (read, increment, write)
      }
      
      public int getCount() {
          return count;
      }
  }
  
  public class Main {
      public static void main(String[] args) throws InterruptedException {
          Counter counter = new Counter();
          
          // Create 1000 threads, each incrementing 1000 times
          Thread[] threads = new Thread[1000];
          for (int i = 0; i < 1000; i++) {
              threads[i] = new Thread(() -> {
                  for (int j = 0; j < 1000; j++) {
                      counter.increment();
                  }
              });
              threads[i].start();
          }
          
          // Wait for all threads to complete
          for (Thread thread : threads) {
              thread.join();
          }
          
          System.out.println("Expected: 1000000");
          System.out.println("Actual: " + counter.getCount());  // Less than 1000000!
      }
  }
  ```

  **1. Synchronized Methods**

  ```java
  public class Counter {
      private int count = 0;
      
      // Synchronized instance method
      public synchronized void increment() {
          count++;  // Now thread-safe
      }
      
      public synchronized void decrement() {
          count--;
      }
      
      public synchronized int getCount() {
          return count;
      }
  }
  ```

  **How it works:**
  + Only one thread can execute a synchronized method at a time on the same object
  + Thread acquires the object's intrinsic lock (monitor) before entering
  + Other threads wait until the lock is released

  **2. Synchronized Blocks**

  ```java
  public class Counter {
      private int count = 0;
      private final Object lock = new Object();
      
      public void increment() {
          synchronized (lock) {  // Synchronized on specific object
              count++;
          }
      }
      
      public void decrement() {
          synchronized (lock) {
              count--;
          }
      }
      
      public int getCount() {
          synchronized (lock) {
              return count;
          }
      }
  }
  ```

  **Advantages of synchronized blocks:**
  + More fine-grained control
  + Can synchronize only critical section
  + Better performance (less time holding lock)
  + Can use different locks for different resources

  **3. Synchronized Static Methods**

  ```java
  public class Counter {
      private static int count = 0;
      
      // Synchronized on Class object (Counter.class)
      public static synchronized void increment() {
          count++;
      }
      
      // Equivalent to:
      public static void incrementAlternative() {
          synchronized (Counter.class) {
              count++;
          }
      }
  }
  ```

  **4. Understanding Monitors**

  A monitor is a synchronization construct that allows threads to have mutual exclusion and cooperation.

  **Monitor Lock (Intrinsic Lock):**
  + Every object in Java has an intrinsic lock (monitor lock)
  + When a thread enters a synchronized method/block, it acquires the lock
  + When it exits, it releases the lock
  + Only one thread can hold the lock at a time

  ```java
  public class MonitorExample {
      private final Object monitor = new Object();
      
      public void method1() {
          synchronized (monitor) {  // Acquire monitor lock
              System.out.println("Method 1");
              // Critical section
          }  // Release monitor lock
      }
      
      public void method2() {
          synchronized (monitor) {  // Wait for same monitor lock
              System.out.println("Method 2");
              // Critical section
          }
      }
  }
  ```

  **5. Explicit Locks (java.util.concurrent.locks)**

  ```java
  import java.util.concurrent.locks.Lock;
  import java.util.concurrent.locks.ReentrantLock;
  
  public class Counter {
      private int count = 0;
      private final Lock lock = new ReentrantLock();
      
      public void increment() {
          lock.lock();  // Acquire lock
          try {
              count++;
          } finally {
              lock.unlock();  // Always release in finally block
          }
      }
      
      public void decrement() {
          lock.lock();
          try {
              count--;
          } finally {
              lock.unlock();
          }
      }
      
      public int getCount() {
          lock.lock();
          try {
              return count;
          } finally {
              lock.unlock();
          }
      }
  }
  ```

  **ReentrantLock Advanced Features:**

  ```java
  import java.util.concurrent.locks.ReentrantLock;
  import java.util.concurrent.TimeUnit;
  
  public class AdvancedLockExample {
      private final ReentrantLock lock = new ReentrantLock();
      
      // Try to acquire lock with timeout
      public void methodWithTimeout() {
          try {
              if (lock.tryLock(1, TimeUnit.SECONDS)) {
                  try {
                      // Critical section
                      System.out.println("Lock acquired");
                  } finally {
                      lock.unlock();
                  }
              } else {
                  System.out.println("Could not acquire lock");
              }
          } catch (InterruptedException e) {
              e.printStackTrace();
          }
      }
      
      // Try to acquire lock immediately
      public void methodWithTryLock() {
          if (lock.tryLock()) {
              try {
                  // Critical section
                  System.out.println("Lock acquired immediately");
              } finally {
                  lock.unlock();
              }
          } else {
              System.out.println("Lock not available");
          }
      }
      
      // Check if current thread holds the lock
      public void checkLockStatus() {
          System.out.println("Is locked: " + lock.isLocked());
          System.out.println("Held by current thread: " + lock.isHeldByCurrentThread());
          System.out.println("Queue length: " + lock.getQueueLength());
      }
  }
  ```

  **6. ReadWriteLock**

  Allows multiple readers or one writer at a time.

  ```java
  import java.util.concurrent.locks.ReadWriteLock;
  import java.util.concurrent.locks.ReentrantReadWriteLock;
  
  public class Cache {
      private final Map<String, String> cache = new HashMap<>();
      private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
      
      // Multiple threads can read simultaneously
      public String get(String key) {
          rwLock.readLock().lock();
          try {
              return cache.get(key);
          } finally {
              rwLock.readLock().unlock();
          }
      }
      
      // Only one thread can write at a time
      public void put(String key, String value) {
          rwLock.writeLock().lock();
          try {
              cache.put(key, value);
          } finally {
              rwLock.writeLock().unlock();
          }
      }
  }
  ```

  **synchronized vs ReentrantLock:**

  | Feature | synchronized | ReentrantLock |
  |---------|--------------|---------------|
  | Syntax | Simple | More verbose |
  | Lock acquisition | Automatic | Manual (lock/unlock) |
  | Try lock | No | Yes (tryLock()) |
  | Timeout | No | Yes (tryLock(timeout)) |
  | Fairness | No | Yes (fair mode) |
  | Interruptible | No | Yes (lockInterruptibly()) |
  | Condition variables | One (wait/notify) | Multiple (Condition) |
  | Performance | Good | Slightly better |

  **When to Use:**
  + **synchronized:** Simple cases, automatic lock management
  + **ReentrantLock:** Need timeout, try lock, fairness, or multiple conditions
  + **ReadWriteLock:** Read-heavy scenarios with occasional writes

  **Best Practices:**

  ```java
  public class SynchronizationBestPractices {
      private final Object lock = new Object();
      private int count = 0;
      
      // ✅ Good: Minimize synchronized block
      public void goodExample() {
          // Non-critical code here
          int localVar = someCalculation();
          
          synchronized (lock) {
              // Only critical section
              count += localVar;
          }
          
          // More non-critical code
      }
      
      // ❌ Bad: Entire method synchronized unnecessarily
      public synchronized void badExample() {
          // Non-critical code
          int localVar = someCalculation();
          
          // Critical section
          count += localVar;
          
          // More non-critical code
      }
      
      // ✅ Good: Always use finally with explicit locks
      private final Lock explicitLock = new ReentrantLock();
      
      public void goodLockUsage() {
          explicitLock.lock();
          try {
              // Critical section
          } finally {
              explicitLock.unlock();  // Always release
          }
      }
      
      private int someCalculation() {
          return 42;
      }
  }
  ```

  **Common Pitfalls:**

  ```java
  // ❌ Deadlock example
  public class DeadlockExample {
      private final Object lock1 = new Object();
      private final Object lock2 = new Object();
      
      public void method1() {
          synchronized (lock1) {
              synchronized (lock2) {
                  // Critical section
              }
          }
      }
      
      public void method2() {
          synchronized (lock2) {  // Different order!
              synchronized (lock1) {
                  // Critical section
              }
          }
      }
  }
  
  // ✅ Fix: Always acquire locks in same order
  public class DeadlockFixed {
      private final Object lock1 = new Object();
      private final Object lock2 = new Object();
      
      public void method1() {
          synchronized (lock1) {
              synchronized (lock2) {
                  // Critical section
              }
          }
      }
      
      public void method2() {
          synchronized (lock1) {  // Same order
              synchronized (lock2) {
                  // Critical section
              }
          }
      }
  }
  ```
  
</details>

<details>
  <summary>Volatile Keyword: Memory Visibility</summary>
  <br/>

  The `volatile` keyword ensures that changes to a variable are immediately visible to all threads.

  **The Problem: Memory Visibility**

  ```java
  // WITHOUT volatile - may run forever!
  public class VolatileProblem {
      private static boolean flag = false;
      
      public static void main(String[] args) throws InterruptedException {
          Thread thread = new Thread(() -> {
              while (!flag) {
                  // Thread may cache 'flag' value and never see the update
              }
              System.out.println("Flag is true!");
          });
          
          thread.start();
          Thread.sleep(1000);
          flag = true;  // Main thread updates flag
          System.out.println("Flag set to true");
          // Thread may never terminate!
      }
  }
  ```

  **Why This Happens:**

  ```
  Main Memory
      flag = false
         ↓
    CPU Cache (Thread 1)     CPU Cache (Main Thread)
      flag = false              flag = true
         ↓                          ↓
    Thread reads cached       Main thread updates
    value repeatedly          but Thread 1 doesn't see it
  ```

  **The Solution: volatile**

  ```java
  // WITH volatile - works correctly
  public class VolatileSolution {
      private static volatile boolean flag = false;
      
      public static void main(String[] args) throws InterruptedException {
          Thread thread = new Thread(() -> {
              while (!flag) {
                  // Now reads from main memory every time
              }
              System.out.println("Flag is true!");
          });
          
          thread.start();
          Thread.sleep(1000);
          flag = true;  // Immediately visible to all threads
          System.out.println("Flag set to true");
          // Thread terminates correctly
      }
  }
  ```

  **What volatile Does:**

  1. **Visibility Guarantee:** Changes are immediately visible to all threads
  2. **Happens-Before Relationship:** Writes to volatile variable happen-before subsequent reads
  3. **No Caching:** Reads/writes go directly to main memory
  4. **Prevents Reordering:** Compiler/CPU cannot reorder operations around volatile

  **volatile vs synchronized:**

  ```java
  public class VolatileVsSynchronized {
      // volatile: Only visibility guarantee
      private volatile int volatileCounter = 0;
      
      // synchronized: Visibility + atomicity
      private int syncCounter = 0;
      
      // ❌ NOT thread-safe (volatile doesn't guarantee atomicity)
      public void incrementVolatile() {
          volatileCounter++;  // Read-modify-write is NOT atomic
      }
      
      // ✅ Thread-safe (synchronized guarantees atomicity)
      public synchronized void incrementSync() {
          syncCounter++;  // Atomic operation
      }
      
      // ✅ Thread-safe (volatile is enough for simple assignment)
      public void setVolatile(int value) {
          volatileCounter = value;  // Simple write is atomic
      }
  }
  ```

  **When to Use volatile:**

  **✅ Use volatile when:**
  + Simple flag/status variable
  + One writer, multiple readers
  + No compound operations (like increment)
  + Variable doesn't depend on its current value

  ```java
  public class GoodVolatileUsage {
      // ✅ Good: Simple flag
      private volatile boolean shutdownRequested = false;
      
      public void shutdown() {
          shutdownRequested = true;
      }
      
      public void run() {
          while (!shutdownRequested) {
              // Do work
          }
      }
      
      // ✅ Good: Status variable
      private volatile String status = "READY";
      
      public void setStatus(String newStatus) {
          status = newStatus;
      }
      
      public String getStatus() {
          return status;
      }
      
      // ✅ Good: Reference assignment
      private volatile Connection connection;
      
      public void setConnection(Connection conn) {
          connection = conn;
      }
      
      public Connection getConnection() {
          return connection;
      }
  }
  ```

  **❌ Don't use volatile when:**
  + Need atomicity for compound operations
  + Multiple variables need to be updated together
  + Variable depends on its current value

  ```java
  public class BadVolatileUsage {
      // ❌ Bad: Increment is not atomic
      private volatile int counter = 0;
      
      public void increment() {
          counter++;  // NOT thread-safe!
          // Equivalent to:
          // int temp = counter;  // Read
          // temp = temp + 1;     // Modify
          // counter = temp;      // Write
          // Another thread can interfere between these steps
      }
      
      // ✅ Fix: Use AtomicInteger
      private AtomicInteger atomicCounter = new AtomicInteger(0);
      
      public void incrementAtomic() {
          atomicCounter.incrementAndGet();  // Thread-safe
      }
      
      // ❌ Bad: Multiple related variables
      private volatile int x = 0;
      private volatile int y = 0;
      
      public void update() {
          x = 10;  // Another thread might see x=10, y=0
          y = 20;  // Inconsistent state!
      }
      
      // ✅ Fix: Use synchronized
      private int x2 = 0;
      private int y2 = 0;
      
      public synchronized void updateSync() {
          x2 = 10;
          y2 = 20;  // Atomic update of both
      }
  }
  ```

  **Real-World Example: Double-Checked Locking**

  ```java
  public class Singleton {
      // volatile is crucial here!
      private static volatile Singleton instance;
      
      private Singleton() {}
      
      public static Singleton getInstance() {
          if (instance == null) {  // First check (no locking)
              synchronized (Singleton.class) {
                  if (instance == null) {  // Second check (with locking)
                      instance = new Singleton();
                  }
              }
          }
          return instance;
      }
  }
  ```

  **Why volatile is needed:**
  + Without volatile, another thread might see partially constructed object
  + Object construction is not atomic: allocate memory → initialize → assign reference
  + volatile ensures proper ordering

  **Performance Considerations:**

  ```java
  public class PerformanceComparison {
      private int normalVar = 0;
      private volatile int volatileVar = 0;
      private int syncVar = 0;
      
      // Fastest: No synchronization (but not thread-safe)
      public void updateNormal() {
          normalVar++;
      }
      
      // Medium: volatile (visibility guarantee, no locking)
      public void updateVolatile() {
          volatileVar = 10;  // Simple assignment
      }
      
      // Slowest: synchronized (visibility + atomicity + locking)
      public synchronized void updateSync() {
          syncVar++;
      }
  }
  ```

  **volatile vs AtomicInteger:**

  ```java
  // volatile: Good for simple flags
  private volatile boolean flag = false;
  
  public void setFlag(boolean value) {
      flag = value;  // Simple assignment - OK
  }
  
  // AtomicInteger: Good for counters
  private AtomicInteger counter = new AtomicInteger(0);
  
  public void increment() {
      counter.incrementAndGet();  // Atomic increment
  }
  
  public void addValue(int delta) {
      counter.addAndGet(delta);  // Atomic add
  }
  
  public int compareAndSet(int expected, int newValue) {
      counter.compareAndSet(expected, newValue);  // CAS operation
  }
  ```

  **Key Points:**
  + volatile guarantees visibility, NOT atomicity
  + Use volatile for simple flags and status variables
  + Use synchronized or AtomicXXX for compound operations
  + volatile is faster than synchronized but less powerful
  + volatile prevents instruction reordering around the variable
  
</details>

<details>
  <summary>Thread Safety: Immutability, Thread-Local Variables</summary>
  <br/>

  Thread safety means that a class or method behaves correctly when accessed by multiple threads, regardless of scheduling or interleaving of thread execution.

  **1. Immutability**

  Immutable objects are inherently thread-safe because their state cannot be modified after construction.

  **Creating Immutable Classes:**

  ```java
  public final class ImmutablePerson {
      private final String name;
      private final int age;
      private final List<String> hobbies;
      
      public ImmutablePerson(String name, int age, List<String> hobbies) {
          this.name = name;
          this.age = age;
          // Defensive copy to prevent external modification
          this.hobbies = new ArrayList<>(hobbies);
      }
      
      public String getName() {
          return name;
      }
      
      public int getAge() {
          return age;
      }
      
      public List<String> getHobbies() {
          // Return unmodifiable view
          return Collections.unmodifiableList(hobbies);
      }
  }
  
  // Usage - Thread-safe without synchronization
  public class Main {
      public static void main(String[] args) {
          List<String> hobbies = Arrays.asList("Reading", "Gaming");
          ImmutablePerson person = new ImmutablePerson("Alice", 30, hobbies);
          
          // Multiple threads can safely read
          Thread t1 = new Thread(() -> System.out.println(person.getName()));
          Thread t2 = new Thread(() -> System.out.println(person.getAge()));
          
          t1.start();
          t2.start();
      }
  }
  ```

  **Rules for Immutable Classes:**
  1. Declare class as `final` (prevent subclassing)
  2. Make all fields `private` and `final`
  3. Don't provide setter methods
  4. Make defensive copies of mutable objects in constructor
  5. Don't allow methods to modify state
  6. Return defensive copies or unmodifiable views of mutable fields

  **Java Built-in Immutable Classes:**
  ```java
  // String is immutable
  String str = "Hello";
  String newStr = str.concat(" World");  // Creates new String, doesn't modify original
  
  // Integer, Long, Double, etc. are immutable
  Integer num = 10;
  Integer newNum = num + 5;  // Creates new Integer
  
  // LocalDate, LocalDateTime are immutable (Java 8+)
  LocalDate date = LocalDate.now();
  LocalDate tomorrow = date.plusDays(1);  // Creates new LocalDate
  ```

  **Benefits of Immutability:**
  + Thread-safe without synchronization
  + Can be safely shared between threads
  + Can be cached and reused
  + Simpler to understand and reason about
  + No defensive copying needed

  **2. Thread-Local Variables**

  ThreadLocal provides thread-confined variables - each thread has its own independent copy.

  **Basic ThreadLocal Usage:**

  ```java
  public class ThreadLocalExample {
      // Each thread gets its own copy
      private static ThreadLocal<Integer> threadLocalValue = new ThreadLocal<>();
      
      public static void main(String[] args) {
          // Thread 1
          Thread t1 = new Thread(() -> {
              threadLocalValue.set(100);
              System.out.println("Thread 1: " + threadLocalValue.get());  // 100
          });
          
          // Thread 2
          Thread t2 = new Thread(() -> {
              threadLocalValue.set(200);
              System.out.println("Thread 2: " + threadLocalValue.get());  // 200
          });
          
          t1.start();
          t2.start();
      }
  }
  ```

  **ThreadLocal with Initial Value:**

  ```java
  public class ThreadLocalWithInitial {
      // Using withInitial (Java 8+)
      private static ThreadLocal<Integer> threadId = ThreadLocal.withInitial(() -> {
          return (int) (Math.random() * 1000);
      });
      
      // Or override initialValue (older approach)
      private static ThreadLocal<String> threadName = new ThreadLocal<String>() {
          @Override
          protected String initialValue() {
              return "Thread-" + Thread.currentThread().getId();
          }
      };
      
      public static void main(String[] args) {
          Runnable task = () -> {
              System.out.println("Thread ID: " + threadId.get());
              System.out.println("Thread Name: " + threadName.get());
          };
          
          new Thread(task).start();
          new Thread(task).start();
          new Thread(task).start();
      }
  }
  ```

  **Real-World Example: Database Connection Per Thread**

  ```java
  public class DatabaseConnectionManager {
      private static ThreadLocal<Connection> connectionHolder = new ThreadLocal<>();
      
      public static Connection getConnection() {
          Connection conn = connectionHolder.get();
          if (conn == null) {
              conn = createNewConnection();
              connectionHolder.set(conn);
          }
          return conn;
      }
      
      public static void closeConnection() {
          Connection conn = connectionHolder.get();
          if (conn != null) {
              try {
                  conn.close();
              } catch (SQLException e) {
                  e.printStackTrace();
              } finally {
                  connectionHolder.remove();  // Important: prevent memory leak
              }
          }
      }
      
      private static Connection createNewConnection() {
          // Create and return database connection
          return null;  // Placeholder
      }
  }
  ```

  **Real-World Example: SimpleDateFormat (Not Thread-Safe)**

  ```java
  public class DateFormatter {
      // ❌ Bad: SimpleDateFormat is not thread-safe
      private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      
      public static String formatDate(Date date) {
          return dateFormat.format(date);  // Race condition!
      }
      
      // ✅ Good: Use ThreadLocal
      private static final ThreadLocal<SimpleDateFormat> threadSafeDateFormat = 
          ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));
      
      public static String formatDateThreadSafe(Date date) {
          return threadSafeDateFormat.get().format(date);  // Thread-safe
      }
      
      // ✅ Better: Use DateTimeFormatter (Java 8+, immutable and thread-safe)
      private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      
      public static String formatDateBest(LocalDate date) {
          return date.format(formatter);  // Thread-safe, no ThreadLocal needed
      }
  }
  ```

  **Real-World Example: User Context**

  ```java
  public class UserContext {
      private static ThreadLocal<User> currentUser = new ThreadLocal<>();
      
      public static void setCurrentUser(User user) {
          currentUser.set(user);
      }
      
      public static User getCurrentUser() {
          return currentUser.get();
      }
      
      public static void clear() {
          currentUser.remove();  // Important: clean up
      }
  }
  
  // Usage in web application
  public class RequestHandler {
      public void handleRequest(HttpRequest request) {
          try {
              // Set user for this thread
              User user = authenticateUser(request);
              UserContext.setCurrentUser(user);
              
              // Process request - any method can access current user
              processRequest();
              
          } finally {
              // Always clean up to prevent memory leak
              UserContext.clear();
          }
      }
      
      private void processRequest() {
          User user = UserContext.getCurrentUser();
          System.out.println("Processing request for: " + user.getName());
      }
      
      private User authenticateUser(HttpRequest request) {
          return new User("Alice");
      }
  }
  ```

  **ThreadLocal Memory Leak Prevention:**

  ```java
  public class ThreadLocalBestPractices {
      private static ThreadLocal<ExpensiveObject> threadLocal = new ThreadLocal<>();
      
      public void doWork() {
          try {
              // Set value
              threadLocal.set(new ExpensiveObject());
              
              // Use value
              ExpensiveObject obj = threadLocal.get();
              obj.doSomething();
              
          } finally {
              // ✅ Always remove to prevent memory leak
              threadLocal.remove();
          }
      }
      
      // ❌ Bad: Not removing ThreadLocal in long-lived threads (like thread pools)
      public void badExample() {
          threadLocal.set(new ExpensiveObject());
          // If thread is reused, old value remains in memory
      }
  }
  ```

  **InheritableThreadLocal:**

  Allows child threads to inherit values from parent thread.

  ```java
  public class InheritableThreadLocalExample {
      private static InheritableThreadLocal<String> inheritableThreadLocal = 
          new InheritableThreadLocal<>();
      
      public static void main(String[] args) {
          inheritableThreadLocal.set("Parent Value");
          
          System.out.println("Parent: " + inheritableThreadLocal.get());
          
          // Child thread inherits value
          Thread child = new Thread(() -> {
              System.out.println("Child: " + inheritableThreadLocal.get());  // "Parent Value"
              
              // Child can modify its own copy
              inheritableThreadLocal.set("Child Value");
              System.out.println("Child modified: " + inheritableThreadLocal.get());
          });
          
          child.start();
          
          try {
              child.join();
          } catch (InterruptedException e) {
              e.printStackTrace();
          }
          
          // Parent value unchanged
          System.out.println("Parent after child: " + inheritableThreadLocal.get());
      }
  }
  ```

  **3. Other Thread Safety Techniques**

  **Stateless Classes:**
  ```java
  // Thread-safe because it has no state
  public class MathUtils {
      public static int add(int a, int b) {
          return a + b;
      }
      
      public static int multiply(int a, int b) {
          return a * b;
      }
  }
  ```

  **Stack Confinement:**
  ```java
  public class StackConfinement {
      public void processData() {
          // Local variables are confined to thread's stack
          int localVar = 0;
          List<String> localList = new ArrayList<>();
          
          // These are thread-safe because they're local
          localVar++;
          localList.add("item");
      }
  }
  ```

  **Atomic Variables:**
  ```java
  import java.util.concurrent.atomic.*;
  
  public class AtomicExample {
      private AtomicInteger counter = new AtomicInteger(0);
      private AtomicLong longCounter = new AtomicLong(0);
      private AtomicBoolean flag = new AtomicBoolean(false);
      private AtomicReference<String> reference = new AtomicReference<>("initial");
      
      public void increment() {
          counter.incrementAndGet();  // Atomic
      }
      
      public void compareAndSwap() {
          int expected = 10;
          int newValue = 20;
          boolean success = counter.compareAndSet(expected, newValue);
      }
      
      public void updateReference() {
          reference.updateAndGet(current -> current + " updated");
      }
  }
  ```

  **Comparison of Thread Safety Techniques:**

  | Technique | Pros | Cons | Use Case |
  |-----------|------|------|----------|
  | Immutability | Simple, no synchronization needed | Cannot modify state | Value objects, DTOs |
  | ThreadLocal | No contention, fast | Memory overhead per thread | Per-thread context, formatters |
  | synchronized | Simple, built-in | Can cause contention | Shared mutable state |
  | Atomic classes | Lock-free, fast | Limited operations | Counters, flags |
  | Stateless | Inherently thread-safe | Not always possible | Utility classes |

  **Best Practices:**

  ```java
  public class ThreadSafetyBestPractices {
      // ✅ Prefer immutability
      private final String immutableField = "constant";
      
      // ✅ Use ThreadLocal for per-thread state
      private static ThreadLocal<DateFormat> formatter = 
          ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));
      
      // ✅ Use atomic variables for simple counters
      private AtomicInteger counter = new AtomicInteger(0);
      
      // ✅ Minimize scope of synchronization
      private final Object lock = new Object();
      private int sharedState = 0;
      
      public void updateState(int value) {
          // Do expensive work outside synchronized block
          int result = expensiveCalculation(value);
          
          synchronized (lock) {
              // Only critical section synchronized
              sharedState = result;
          }
      }
      
      // ✅ Always clean up ThreadLocal
      public void doWork() {
          try {
              formatter.get().format(new Date());
          } finally {
              formatter.remove();
          }
      }
      
      private int expensiveCalculation(int value) {
          return value * 2;
      }
  }
  ```

  **Key Points:**
  + Immutable objects are inherently thread-safe
  + ThreadLocal provides thread-confined variables
  + Always call `ThreadLocal.remove()` to prevent memory leaks
  + Prefer immutability over synchronization when possible
  + Use atomic variables for simple counters and flags
  + Minimize the scope of synchronization
  
</details>


## Advanced Concurrency

<details>
  <summary>ExecutorService and Thread Pools</summary>
  <br/>

  ExecutorService provides a high-level API for managing threads and executing tasks asynchronously.

  **Why Use ExecutorService?**

  ```java
  // ❌ Bad: Manual thread management
  public class ManualThreadManagement {
      public void processTasks() {
          for (int i = 0; i < 100; i++) {
              Thread thread = new Thread(() -> {
                  // Process task
              });
              thread.start();  // Creates 100 threads!
          }
      }
  }
  
  // ✅ Good: Using ExecutorService
  public class ExecutorServiceExample {
      public void processTasks() {
          ExecutorService executor = Executors.newFixedThreadPool(10);
          
          for (int i = 0; i < 100; i++) {
              executor.submit(() -> {
                  // Process task
              });
          }
          
          executor.shutdown();  // Reuses 10 threads for 100 tasks
      }
  }
  ```

  **Benefits:**
  + Thread reuse (reduces overhead)
  + Automatic thread lifecycle management
  + Task queue management
  + Better resource control
  + Built-in task scheduling

  **Basic ExecutorService Usage:**

  ```java
  import java.util.concurrent.*;
  
  public class ExecutorServiceBasics {
      public static void main(String[] args) {
          // Create executor with 3 threads
          ExecutorService executor = Executors.newFixedThreadPool(3);
          
          // Submit Runnable tasks (no return value)
          executor.submit(() -> {
              System.out.println("Task 1 by " + Thread.currentThread().getName());
          });
          
          // Submit Callable tasks (with return value)
          Future<Integer> future = executor.submit(() -> {
              Thread.sleep(1000);
              return 42;
          });
          
          try {
              // Get result (blocks until complete)
              Integer result = future.get();
              System.out.println("Result: " + result);
              
              // Get result with timeout
              Integer result2 = future.get(2, TimeUnit.SECONDS);
              
          } catch (InterruptedException | ExecutionException | TimeoutException e) {
              e.printStackTrace();
          }
          
          // Shutdown executor
          executor.shutdown();  // No new tasks accepted
          
          try {
              // Wait for existing tasks to complete
              if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                  executor.shutdownNow();  // Force shutdown
              }
          } catch (InterruptedException e) {
              executor.shutdownNow();
          }
      }
  }
  ```

  **ExecutorService Methods:**

  ```java
  ExecutorService executor = Executors.newFixedThreadPool(5);
  
  // Submit single task
  Future<?> future1 = executor.submit(runnable);
  Future<T> future2 = executor.submit(callable);
  
  // Execute without Future (fire and forget)
  executor.execute(runnable);
  
  // Submit multiple tasks
  List<Callable<Integer>> tasks = Arrays.asList(
      () -> 1,
      () -> 2,
      () -> 3
  );
  
  // Execute all tasks, wait for all to complete
  List<Future<Integer>> futures = executor.invokeAll(tasks);
  
  // Execute all tasks, return first completed result
  Integer result = executor.invokeAny(tasks);
  
  // Shutdown methods
  executor.shutdown();           // Graceful shutdown
  executor.shutdownNow();        // Immediate shutdown
  boolean isShutdown = executor.isShutdown();
  boolean isTerminated = executor.isTerminated();
  executor.awaitTermination(10, TimeUnit.SECONDS);
  ```

  **Types of Thread Pools:**

  **1. FixedThreadPool**

  Fixed number of threads. Tasks wait in queue if all threads are busy.

  ```java
  ExecutorService executor = Executors.newFixedThreadPool(5);
  
  // Internal implementation
  new ThreadPoolExecutor(
      5,                      // corePoolSize
      5,                      // maximumPoolSize (same as core)
      0L,                     // keepAliveTime
      TimeUnit.MILLISECONDS,
      new LinkedBlockingQueue<Runnable>()  // Unbounded queue
  );
  ```

  **Characteristics:**
  + Fixed number of threads (5 in example)
  + Unbounded task queue
  + Threads are never terminated (unless pool is shutdown)
  + Good for: Known, stable workload

  **Use Case:**
  ```java
  // Processing fixed number of concurrent requests
  ExecutorService executor = Executors.newFixedThreadPool(10);
  
  for (int i = 0; i < 100; i++) {
      final int taskId = i;
      executor.submit(() -> {
          System.out.println("Processing task " + taskId);
          // Process task
      });
  }
  
  executor.shutdown();
  ```

  **2. CachedThreadPool**

  Creates new threads as needed, reuses idle threads. Threads idle for 60 seconds are terminated.

  ```java
  ExecutorService executor = Executors.newCachedThreadPool();
  
  // Internal implementation
  new ThreadPoolExecutor(
      0,                      // corePoolSize (no core threads)
      Integer.MAX_VALUE,      // maximumPoolSize (unlimited)
      60L,                    // keepAliveTime (60 seconds)
      TimeUnit.SECONDS,
      new SynchronousQueue<Runnable>()  // No queue, direct handoff
  );
  ```

  **Characteristics:**
  + No core threads
  + Creates threads on demand
  + Idle threads terminated after 60 seconds
  + No task queue (direct handoff)
  + Good for: Many short-lived tasks

  **Use Case:**
  ```java
  // Handling burst of short tasks
  ExecutorService executor = Executors.newCachedThreadPool();
  
  // Burst of 1000 tasks
  for (int i = 0; i < 1000; i++) {
      executor.submit(() -> {
          // Quick task (< 1 second)
          System.out.println("Quick task");
      });
  }
  
  // Threads will be created as needed and cleaned up after 60s idle
  executor.shutdown();
  ```

  **3. SingleThreadExecutor**

  Single worker thread. Tasks executed sequentially in order.

  ```java
  ExecutorService executor = Executors.newSingleThreadExecutor();
  
  // Internal implementation
  new ThreadPoolExecutor(
      1,                      // corePoolSize
      1,                      // maximumPoolSize
      0L,                     // keepAliveTime
      TimeUnit.MILLISECONDS,
      new LinkedBlockingQueue<Runnable>()
  );
  ```

  **Characteristics:**
  + Only one thread
  + Tasks executed sequentially
  + Guarantees order of execution
  + Good for: Sequential task processing, event loop

  **Use Case:**
  ```java
  // Sequential log writing
  ExecutorService logWriter = Executors.newSingleThreadExecutor();
  
  logWriter.submit(() -> System.out.println("Log 1"));
  logWriter.submit(() -> System.out.println("Log 2"));
  logWriter.submit(() -> System.out.println("Log 3"));
  // Always prints in order: Log 1, Log 2, Log 3
  
  logWriter.shutdown();
  ```

  **4. ScheduledThreadPool**

  Fixed number of threads for scheduled/periodic tasks.

  ```java
  ScheduledExecutorService executor = Executors.newScheduledThreadPool(3);
  
  // Schedule one-time task with delay
  executor.schedule(() -> {
      System.out.println("Executed after 5 seconds");
  }, 5, TimeUnit.SECONDS);
  
  // Schedule periodic task (fixed rate)
  executor.scheduleAtFixedRate(() -> {
      System.out.println("Executed every 10 seconds");
  }, 0, 10, TimeUnit.SECONDS);  // initialDelay, period
  
  // Schedule periodic task (fixed delay)
  executor.scheduleWithFixedDelay(() -> {
      System.out.println("Executed 5 seconds after previous completion");
  }, 0, 5, TimeUnit.SECONDS);  // initialDelay, delay
  
  // Shutdown after some time
  executor.schedule(() -> {
      executor.shutdown();
  }, 60, TimeUnit.SECONDS);
  ```

  **Characteristics:**
  + Fixed number of threads
  + Supports delayed and periodic execution
  + Good for: Scheduled tasks, periodic jobs

  **scheduleAtFixedRate vs scheduleWithFixedDelay:**
  ```java
  // Fixed Rate: Executes at fixed intervals (ignores execution time)
  // If task takes 3s and period is 5s: 0s, 5s, 10s, 15s...
  executor.scheduleAtFixedRate(task, 0, 5, TimeUnit.SECONDS);
  
  // Fixed Delay: Waits fixed time AFTER previous completion
  // If task takes 3s and delay is 5s: 0s, 8s, 16s, 24s...
  executor.scheduleWithFixedDelay(task, 0, 5, TimeUnit.SECONDS);
  ```

  **5. WorkStealingPool (Java 8+)**

  Uses ForkJoinPool with work-stealing algorithm. Parallelism based on CPU cores.

  ```java
  ExecutorService executor = Executors.newWorkStealingPool();
  
  // Or specify parallelism level
  ExecutorService executor2 = Executors.newWorkStealingPool(4);
  
  // Internal: Uses ForkJoinPool
  // Idle threads "steal" work from busy threads' queues
  ```

  **Characteristics:**
  + Based on ForkJoinPool
  + Work-stealing algorithm
  + Default parallelism = number of CPU cores
  + Good for: Recursive tasks, divide-and-conquer algorithms

  **Use Case:**
  ```java
  // Parallel processing
  ExecutorService executor = Executors.newWorkStealingPool();
  
  List<Callable<Integer>> tasks = new ArrayList<>();
  for (int i = 0; i < 100; i++) {
      final int num = i;
      tasks.add(() -> {
          // CPU-intensive task
          return num * num;
      });
  }
  
  List<Future<Integer>> results = executor.invokeAll(tasks);
  executor.shutdown();
  ```

  **Thread Pool Comparison:**

  | Type | Core Threads | Max Threads | Queue | Keep Alive | Use Case |
  |------|--------------|-------------|-------|------------|----------|
  | FixedThreadPool | N | N | Unbounded | 0 | Stable workload |
  | CachedThreadPool | 0 | Unlimited | None | 60s | Short-lived tasks |
  | SingleThreadExecutor | 1 | 1 | Unbounded | 0 | Sequential processing |
  | ScheduledThreadPool | N | N | DelayedWorkQueue | 0 | Scheduled tasks |
  | WorkStealingPool | CPU cores | Many | Work-stealing | - | Parallel processing |

  **Custom ThreadPoolExecutor:**

  ```java
  ThreadPoolExecutor executor = new ThreadPoolExecutor(
      5,                          // corePoolSize
      10,                         // maximumPoolSize
      60L,                        // keepAliveTime
      TimeUnit.SECONDS,           // time unit
      new ArrayBlockingQueue<>(100),  // bounded queue
      new ThreadPoolExecutor.CallerRunsPolicy()  // rejection policy
  );
  
  // Custom thread factory
  executor.setThreadFactory(new ThreadFactory() {
      private AtomicInteger counter = new AtomicInteger(0);
      
      @Override
      public Thread newThread(Runnable r) {
          Thread thread = new Thread(r);
          thread.setName("CustomThread-" + counter.incrementAndGet());
          thread.setDaemon(false);
          return thread;
      }
  });
  ```

  **Rejection Policies:**

  ```java
  // 1. AbortPolicy (default) - Throws RejectedExecutionException
  new ThreadPoolExecutor.AbortPolicy();
  
  // 2. CallerRunsPolicy - Caller thread executes the task
  new ThreadPoolExecutor.CallerRunsPolicy();
  
  // 3. DiscardPolicy - Silently discards the task
  new ThreadPoolExecutor.DiscardPolicy();
  
  // 4. DiscardOldestPolicy - Discards oldest task in queue
  new ThreadPoolExecutor.DiscardOldestPolicy();
  
  // 5. Custom policy
  new RejectedExecutionHandler() {
      @Override
      public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
          // Custom handling (e.g., log, retry, queue elsewhere)
          System.out.println("Task rejected: " + r);
      }
  };
  ```

  **Best Practices:**

  ```java
  public class ExecutorServiceBestPractices {
      public void goodExample() {
          ExecutorService executor = Executors.newFixedThreadPool(10);
          
          try {
              // Submit tasks
              List<Future<Integer>> futures = new ArrayList<>();
              for (int i = 0; i < 100; i++) {
                  Future<Integer> future = executor.submit(() -> {
                      // Task logic
                      return 42;
                  });
                  futures.add(future);
              }
              
              // Process results
              for (Future<Integer> future : futures) {
                  try {
                      Integer result = future.get();
                      // Handle result
                  } catch (ExecutionException e) {
                      // Handle task exception
                      e.printStackTrace();
                  }
              }
              
          } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
          } finally {
              // ✅ Always shutdown
              executor.shutdown();
              try {
                  if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                      executor.shutdownNow();
                      if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                          System.err.println("Executor did not terminate");
                      }
                  }
              } catch (InterruptedException e) {
                  executor.shutdownNow();
                  Thread.currentThread().interrupt();
              }
          }
      }
  }
  ```

  **Monitoring Thread Pool:**

  ```java
  ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
  
  // Monitor pool status
  System.out.println("Active threads: " + executor.getActiveCount());
  System.out.println("Pool size: " + executor.getPoolSize());
  System.out.println("Core pool size: " + executor.getCorePoolSize());
  System.out.println("Max pool size: " + executor.getMaximumPoolSize());
  System.out.println("Queue size: " + executor.getQueue().size());
  System.out.println("Completed tasks: " + executor.getCompletedTaskCount());
  System.out.println("Total tasks: " + executor.getTaskCount());
  ```
  
</details>

<details>
  <summary>Future and CompletableFuture</summary>
  <br/>

  Future represents the result of an asynchronous computation. CompletableFuture extends Future with powerful composition and chaining capabilities.

  **1. Future Interface**

  **Basic Future Usage:**

  ```java
  import java.util.concurrent.*;
  
  public class FutureExample {
      public static void main(String[] args) {
          ExecutorService executor = Executors.newFixedThreadPool(2);
          
          // Submit task and get Future
          Future<Integer> future = executor.submit(() -> {
              Thread.sleep(2000);  // Simulate long task
              return 42;
          });
          
          System.out.println("Task submitted, doing other work...");
          
          try {
              // Check if task is done
              if (!future.isDone()) {
                  System.out.println("Task still running...");
              }
              
              // Get result (blocks until complete)
              Integer result = future.get();
              System.out.println("Result: " + result);
              
              // Or get with timeout
              Integer result2 = future.get(3, TimeUnit.SECONDS);
              
          } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
          } catch (ExecutionException e) {
              System.out.println("Task threw exception: " + e.getCause());
          } catch (TimeoutException e) {
              System.out.println("Task timed out");
              future.cancel(true);  // Cancel the task
          }
          
          executor.shutdown();
      }
  }
  ```

  **Future Methods:**

  ```java
  Future<Integer> future = executor.submit(callable);
  
  // Get result (blocks)
  Integer result = future.get();
  Integer result2 = future.get(5, TimeUnit.SECONDS);  // With timeout
  
  // Check status
  boolean isDone = future.isDone();
  boolean isCancelled = future.isCancelled();
  
  // Cancel task
  boolean cancelled = future.cancel(true);  // mayInterruptIfRunning
  ```

  **Limitations of Future:**
  + Cannot manually complete
  + Cannot chain multiple Futures
  + Cannot combine multiple Futures
  + No exception handling without blocking
  + No callback mechanism

  **2. CompletableFuture (Java 8+)**

  CompletableFuture solves Future's limitations with non-blocking operations and functional composition.

  **Creating CompletableFuture:**

  ```java
  import java.util.concurrent.CompletableFuture;
  
  // 1. Run async task (no return value)
  CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
      System.out.println("Running async task");
  });
  
  // 2. Supply async task (with return value)
  CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
      return "Hello from async task";
  });
  
  // 3. With custom executor
  ExecutorService executor = Executors.newFixedThreadPool(10);
  CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> {
      return "Result";
  }, executor);
  
  // 4. Already completed future
  CompletableFuture<String> future4 = CompletableFuture.completedFuture("Immediate result");
  
  // 5. Manual completion
  CompletableFuture<String> future5 = new CompletableFuture<>();
  future5.complete("Manual result");  // Complete manually
  future5.completeExceptionally(new RuntimeException("Error"));  // Complete with exception
  ```

  **Transforming Results:**

  ```java
  // thenApply - Transform result (synchronous)
  CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> 10)
      .thenApply(result -> result * 2)
      .thenApply(result -> result + 5);
  
  System.out.println(future.get());  // 25
  
  // thenApplyAsync - Transform result (asynchronous)
  CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> 10)
      .thenApplyAsync(result -> {
          // Runs in different thread
          return result * 2;
      });
  ```

  **Consuming Results:**

  ```java
  // thenAccept - Consume result (no return)
  CompletableFuture.supplyAsync(() -> "Hello")
      .thenAccept(result -> {
          System.out.println("Result: " + result);
      });
  
  // thenRun - Run action after completion (no access to result)
  CompletableFuture.supplyAsync(() -> "Hello")
      .thenRun(() -> {
          System.out.println("Task completed");
      });
  ```

  **Chaining Multiple Futures:**

  ```java
  // thenCompose - Chain dependent futures (flatMap)
  CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "User123")
      .thenCompose(userId -> fetchUserDetails(userId))
      .thenCompose(user -> fetchUserOrders(user));
  
  private static CompletableFuture<User> fetchUserDetails(String userId) {
      return CompletableFuture.supplyAsync(() -> {
          // Fetch from database
          return new User(userId, "Alice");
      });
  }
  
  private static CompletableFuture<List<Order>> fetchUserOrders(User user) {
      return CompletableFuture.supplyAsync(() -> {
          // Fetch orders
          return Arrays.asList(new Order(1), new Order(2));
      });
  }
  ```

  **Combining Multiple Futures:**

  ```java
  // thenCombine - Combine two independent futures
  CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> 10);
  CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> 20);
  
  CompletableFuture<Integer> combined = future1.thenCombine(future2, (result1, result2) -> {
      return result1 + result2;
  });
  
  System.out.println(combined.get());  // 30
  
  // thenAcceptBoth - Consume results of two futures
  future1.thenAcceptBoth(future2, (result1, result2) -> {
      System.out.println("Sum: " + (result1 + result2));
  });
  
  // runAfterBoth - Run action after both complete
  future1.runAfterBoth(future2, () -> {
      System.out.println("Both completed");
  });
  ```

  **Combining Multiple Futures (Any/All):**

  ```java
  CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
      sleep(1000);
      return "Result 1";
  });
  
  CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
      sleep(2000);
      return "Result 2";
  });
  
  CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> {
      sleep(3000);
      return "Result 3";
  });
  
  // allOf - Wait for all to complete
  CompletableFuture<Void> allOf = CompletableFuture.allOf(future1, future2, future3);
  
  allOf.thenRun(() -> {
      try {
          String r1 = future1.get();
          String r2 = future2.get();
          String r3 = future3.get();
          System.out.println("All results: " + r1 + ", " + r2 + ", " + r3);
      } catch (Exception e) {
          e.printStackTrace();
      }
  });
  
  // anyOf - Wait for any to complete
  CompletableFuture<Object> anyOf = CompletableFuture.anyOf(future1, future2, future3);
  
  anyOf.thenAccept(result -> {
      System.out.println("First result: " + result);  // "Result 1" (fastest)
  });
  ```

  **Exception Handling:**

  ```java
  // exceptionally - Handle exception
  CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
      if (true) throw new RuntimeException("Error!");
      return 42;
  }).exceptionally(ex -> {
      System.out.println("Exception: " + ex.getMessage());
      return 0;  // Default value
  });
  
  // handle - Handle both result and exception
  CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> {
      return 42;
  }).handle((result, ex) -> {
      if (ex != null) {
          System.out.println("Error: " + ex.getMessage());
          return 0;
      }
      return result * 2;
  });
  
  // whenComplete - Peek at result/exception (doesn't transform)
  CompletableFuture<Integer> future3 = CompletableFuture.supplyAsync(() -> {
      return 42;
  }).whenComplete((result, ex) -> {
      if (ex != null) {
          System.out.println("Failed with: " + ex.getMessage());
      } else {
          System.out.println("Succeeded with: " + result);
      }
  });
  ```

  **Real-World Example: Parallel API Calls**

  ```java
  public class ParallelAPICallsExample {
      public static void main(String[] args) {
          long start = System.currentTimeMillis();
          
          // Sequential (slow)
          String user = fetchUser();
          String orders = fetchOrders();
          String recommendations = fetchRecommendations();
          // Total time: 3 seconds
          
          // Parallel with CompletableFuture (fast)
          CompletableFuture<String> userFuture = 
              CompletableFuture.supplyAsync(() -> fetchUser());
          
          CompletableFuture<String> ordersFuture = 
              CompletableFuture.supplyAsync(() -> fetchOrders());
          
          CompletableFuture<String> recommendationsFuture = 
              CompletableFuture.supplyAsync(() -> fetchRecommendations());
          
          // Combine all results
          CompletableFuture<String> allData = CompletableFuture.allOf(
              userFuture, ordersFuture, recommendationsFuture
          ).thenApply(v -> {
              try {
                  return userFuture.get() + ", " + 
                         ordersFuture.get() + ", " + 
                         recommendationsFuture.get();
              } catch (Exception e) {
                  throw new RuntimeException(e);
              }
          });
          
          allData.thenAccept(result -> {
              long end = System.currentTimeMillis();
              System.out.println("Result: " + result);
              System.out.println("Time: " + (end - start) + "ms");  // ~1 second
          }).join();
      }
      
      private static String fetchUser() {
          sleep(1000);
          return "User data";
      }
      
      private static String fetchOrders() {
          sleep(1000);
          return "Orders data";
      }
      
      private static String fetchRecommendations() {
          sleep(1000);
          return "Recommendations data";
      }
      
      private static void sleep(long millis) {
          try {
              Thread.sleep(millis);
          } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
          }
      }
  }
  ```

  **CompletableFuture Method Summary:**

  | Method | Purpose | Returns |
  |--------|---------|---------|
  | `supplyAsync` | Start async task with result | CompletableFuture<T> |
  | `runAsync` | Start async task without result | CompletableFuture<Void> |
  | `thenApply` | Transform result | CompletableFuture<U> |
  | `thenAccept` | Consume result | CompletableFuture<Void> |
  | `thenRun` | Run action after completion | CompletableFuture<Void> |
  | `thenCompose` | Chain dependent futures | CompletableFuture<U> |
  | `thenCombine` | Combine two futures | CompletableFuture<V> |
  | `allOf` | Wait for all futures | CompletableFuture<Void> |
  | `anyOf` | Wait for any future | CompletableFuture<Object> |
  | `exceptionally` | Handle exception | CompletableFuture<T> |
  | `handle` | Handle result or exception | CompletableFuture<U> |
  | `whenComplete` | Peek at result/exception | CompletableFuture<T> |

  **Best Practices:**

  ```java
  public class CompletableFutureBestPractices {
      private static final ExecutorService executor = Executors.newFixedThreadPool(10);
      
      public void goodExample() {
          // ✅ Use custom executor for CPU-intensive tasks
          CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
              // CPU-intensive work
              return heavyComputation();
          }, executor);
          
          // ✅ Always handle exceptions
          future.exceptionally(ex -> {
              log.error("Task failed", ex);
              return 0;  // Default value
          });
          
          // ✅ Use join() instead of get() to avoid checked exceptions
          Integer result = future.join();
          
          // ✅ Combine multiple independent operations
          CompletableFuture<String> combined = CompletableFuture
              .supplyAsync(() -> fetchData1(), executor)
              .thenCombine(
                  CompletableFuture.supplyAsync(() -> fetchData2(), executor),
                  (data1, data2) -> data1 + data2
              );
      }
      
      private Integer heavyComputation() {
          return 42;
      }
      
      private String fetchData1() {
          return "Data1";
      }
      
      private String fetchData2() {
          return "Data2";
      }
  }
  ```

  **Common Pitfalls:**

  ```java
  // ❌ Bad: Blocking in async chain
  CompletableFuture.supplyAsync(() -> "data")
      .thenApply(data -> {
          try {
              return expensiveOperation(data).get();  // Blocking!
          } catch (Exception e) {
              throw new RuntimeException(e);
          }
      });
  
  // ✅ Good: Use thenCompose for async chaining
  CompletableFuture.supplyAsync(() -> "data")
      .thenCompose(data -> expensiveOperation(data));
  
  // ❌ Bad: Not handling exceptions
  CompletableFuture.supplyAsync(() -> {
      throw new RuntimeException("Error");
  });  // Exception is swallowed!
  
  // ✅ Good: Handle exceptions
  CompletableFuture.supplyAsync(() -> {
      throw new RuntimeException("Error");
  }).exceptionally(ex -> {
      log.error("Error occurred", ex);
      return null;
  });
  ```
  
</details>

<details>
  <summary>Synchronization Utilities: CountDownLatch, CyclicBarrier, Semaphore, Phaser</summary>
  <br/>

  Java provides high-level synchronization utilities for coordinating threads.

  **1. CountDownLatch**

  Allows one or more threads to wait until a set of operations in other threads completes.

  **How it works:**
  + Initialize with a count
  + Threads call `await()` to wait
  + Other threads call `countDown()` to decrement count
  + When count reaches zero, waiting threads are released
  + **One-time use** - cannot be reset

  **Basic Usage:**

  ```java
  import java.util.concurrent.CountDownLatch;
  
  public class CountDownLatchExample {
      public static void main(String[] args) throws InterruptedException {
          int numWorkers = 3;
          CountDownLatch latch = new CountDownLatch(numWorkers);
          
          // Start worker threads
          for (int i = 0; i < numWorkers; i++) {
              final int workerId = i;
              new Thread(() -> {
                  System.out.println("Worker " + workerId + " starting");
                  try {
                      Thread.sleep(1000);  // Simulate work
                  } catch (InterruptedException e) {
                      e.printStackTrace();
                  }
                  System.out.println("Worker " + workerId + " finished");
                  latch.countDown();  // Decrement count
              }).start();
          }
          
          // Main thread waits for all workers
          System.out.println("Main thread waiting for workers...");
          latch.await();  // Blocks until count reaches 0
          System.out.println("All workers finished. Main thread continues.");
      }
  }
  ```

  **Real-World Example: Service Startup**

  ```java
  public class ServiceStartupExample {
      public static void main(String[] args) throws InterruptedException {
          CountDownLatch startupLatch = new CountDownLatch(3);
          
          // Start database service
          new Thread(() -> {
              System.out.println("Starting database...");
              sleep(2000);
              System.out.println("Database ready");
              startupLatch.countDown();
          }).start();
          
          // Start cache service
          new Thread(() -> {
              System.out.println("Starting cache...");
              sleep(1500);
              System.out.println("Cache ready");
              startupLatch.countDown();
          }).start();
          
          // Start message queue
          new Thread(() -> {
              System.out.println("Starting message queue...");
              sleep(1000);
              System.out.println("Message queue ready");
              startupLatch.countDown();
          }).start();
          
          // Wait for all services
          System.out.println("Waiting for all services to start...");
          startupLatch.await();
          System.out.println("All services started. Application ready!");
      }
      
      private static void sleep(long millis) {
          try { Thread.sleep(millis); } catch (InterruptedException e) {}
      }
  }
  ```

  **With Timeout:**

  ```java
  CountDownLatch latch = new CountDownLatch(3);
  
  // Wait with timeout
  boolean completed = latch.await(5, TimeUnit.SECONDS);
  if (completed) {
      System.out.println("All tasks completed");
  } else {
      System.out.println("Timeout! Some tasks not completed");
  }
  ```

  **2. CyclicBarrier**

  Allows a set of threads to wait for each other to reach a common barrier point.

  **How it works:**
  + Initialize with number of parties (threads)
  + Each thread calls `await()` when it reaches the barrier
  + When all threads reach the barrier, they are all released
  + **Reusable** - can be used multiple times

  **Basic Usage:**

  ```java
  import java.util.concurrent.CyclicBarrier;
  
  public class CyclicBarrierExample {
      public static void main(String[] args) {
          int numThreads = 3;
          
          // Optional barrier action (runs when all threads reach barrier)
          Runnable barrierAction = () -> {
              System.out.println("All threads reached barrier. Proceeding...");
          };
          
          CyclicBarrier barrier = new CyclicBarrier(numThreads, barrierAction);
          
          for (int i = 0; i < numThreads; i++) {
              final int threadId = i;
              new Thread(() -> {
                  try {
                      System.out.println("Thread " + threadId + " doing phase 1");
                      Thread.sleep(1000);
                      
                      System.out.println("Thread " + threadId + " waiting at barrier");
                      barrier.await();  // Wait for all threads
                      
                      System.out.println("Thread " + threadId + " doing phase 2");
                      Thread.sleep(1000);
                      
                      System.out.println("Thread " + threadId + " waiting at barrier again");
                      barrier.await();  // Reuse barrier
                      
                      System.out.println("Thread " + threadId + " completed");
                      
                  } catch (Exception e) {
                      e.printStackTrace();
                  }
              }).start();
          }
      }
  }
  ```

  **Real-World Example: Parallel Matrix Computation**

  ```java
  public class MatrixComputationExample {
      private static final int NUM_WORKERS = 4;
      private static final int MATRIX_SIZE = 1000;
      
      public static void main(String[] args) {
          int[][] matrix = new int[MATRIX_SIZE][MATRIX_SIZE];
          CyclicBarrier barrier = new CyclicBarrier(NUM_WORKERS, () -> {
              System.out.println("Phase completed. All workers synchronized.");
          });
          
          // Divide work among workers
          int rowsPerWorker = MATRIX_SIZE / NUM_WORKERS;
          
          for (int i = 0; i < NUM_WORKERS; i++) {
              final int startRow = i * rowsPerWorker;
              final int endRow = (i + 1) * rowsPerWorker;
              
              new Thread(() -> {
                  try {
                      // Phase 1: Initialize
                      for (int row = startRow; row < endRow; row++) {
                          for (int col = 0; col < MATRIX_SIZE; col++) {
                              matrix[row][col] = row + col;
                          }
                      }
                      barrier.await();  // Wait for all to finish phase 1
                      
                      // Phase 2: Process
                      for (int row = startRow; row < endRow; row++) {
                          for (int col = 0; col < MATRIX_SIZE; col++) {
                              matrix[row][col] *= 2;
                          }
                      }
                      barrier.await();  // Wait for all to finish phase 2
                      
                      System.out.println("Worker completed");
                      
                  } catch (Exception e) {
                      e.printStackTrace();
                  }
              }).start();
          }
      }
  }
  ```

  **3. Semaphore**

  Controls access to a shared resource through permits.

  **How it works:**
  + Initialize with number of permits
  + Thread calls `acquire()` to get a permit (blocks if none available)
  + Thread calls `release()` to return a permit
  + Used to limit concurrent access to resources

  **Basic Usage:**

  ```java
  import java.util.concurrent.Semaphore;
  
  public class SemaphoreExample {
      public static void main(String[] args) {
          // Allow max 3 concurrent threads
          Semaphore semaphore = new Semaphore(3);
          
          for (int i = 0; i < 10; i++) {
              final int threadId = i;
              new Thread(() -> {
                  try {
                      System.out.println("Thread " + threadId + " waiting for permit");
                      semaphore.acquire();  // Get permit
                      
                      System.out.println("Thread " + threadId + " got permit. Working...");
                      Thread.sleep(2000);  // Simulate work
                      
                      System.out.println("Thread " + threadId + " releasing permit");
                      
                  } catch (InterruptedException e) {
                      e.printStackTrace();
                  } finally {
                      semaphore.release();  // Return permit
                  }
              }).start();
          }
      }
  }
  ```

  **Real-World Example: Connection Pool**

  ```java
  public class ConnectionPool {
      private final Semaphore semaphore;
      private final List<Connection> connections;
      
      public ConnectionPool(int poolSize) {
          this.semaphore = new Semaphore(poolSize);
          this.connections = new ArrayList<>();
          
          // Initialize connections
          for (int i = 0; i < poolSize; i++) {
              connections.add(new Connection("Connection-" + i));
          }
      }
      
      public Connection getConnection() throws InterruptedException {
          semaphore.acquire();  // Wait for available connection
          return getNextAvailableConnection();
      }
      
      public void releaseConnection(Connection connection) {
          returnConnection(connection);
          semaphore.release();  // Release permit
      }
      
      private synchronized Connection getNextAvailableConnection() {
          for (Connection conn : connections) {
              if (!conn.isInUse()) {
                  conn.setInUse(true);
                  return conn;
              }
          }
          return null;
      }
      
      private synchronized void returnConnection(Connection connection) {
          connection.setInUse(false);
      }
  }
  
  // Usage
  public class Main {
      public static void main(String[] args) {
          ConnectionPool pool = new ConnectionPool(3);
          
          for (int i = 0; i < 10; i++) {
              final int taskId = i;
              new Thread(() -> {
                  try {
                      Connection conn = pool.getConnection();
                      System.out.println("Task " + taskId + " using " + conn.getName());
                      Thread.sleep(2000);  // Use connection
                      pool.releaseConnection(conn);
                      System.out.println("Task " + taskId + " released connection");
                  } catch (InterruptedException e) {
                      e.printStackTrace();
                  }
              }).start();
          }
      }
  }
  ```

  **Semaphore with tryAcquire:**

  ```java
  Semaphore semaphore = new Semaphore(3);
  
  // Try to acquire without blocking
  if (semaphore.tryAcquire()) {
      try {
          // Got permit, do work
      } finally {
          semaphore.release();
      }
  } else {
      System.out.println("No permit available");
  }
  
  // Try to acquire with timeout
  if (semaphore.tryAcquire(2, TimeUnit.SECONDS)) {
      try {
          // Got permit within 2 seconds
      } finally {
          semaphore.release();
      }
  }
  
  // Acquire multiple permits
  semaphore.acquire(2);  // Acquire 2 permits
  try {
      // Do work
  } finally {
      semaphore.release(2);  // Release 2 permits
  }
  ```

  **4. Phaser (Java 7+)**

  More flexible than CountDownLatch and CyclicBarrier. Supports dynamic number of parties and multiple phases.

  **How it works:**
  + Parties can register/deregister dynamically
  + Supports multiple phases
  + Each party calls `arriveAndAwaitAdvance()` to wait for others
  + Automatically advances to next phase when all parties arrive

  **Basic Usage:**

  ```java
  import java.util.concurrent.Phaser;
  
  public class PhaserExample {
      public static void main(String[] args) {
          Phaser phaser = new Phaser(1);  // Register main thread
          
          int numThreads = 3;
          for (int i = 0; i < numThreads; i++) {
              final int threadId = i;
              phaser.register();  // Register each thread
              
              new Thread(() -> {
                  System.out.println("Thread " + threadId + " phase 0");
                  phaser.arriveAndAwaitAdvance();  // Wait for phase 0
                  
                  System.out.println("Thread " + threadId + " phase 1");
                  phaser.arriveAndAwaitAdvance();  // Wait for phase 1
                  
                  System.out.println("Thread " + threadId + " phase 2");
                  phaser.arriveAndAwaitAdvance();  // Wait for phase 2
                  
                  phaser.arriveAndDeregister();  // Deregister when done
              }).start();
          }
          
          // Main thread participates
          phaser.arriveAndAwaitAdvance();  // Phase 0
          System.out.println("Main: Phase 0 complete");
          
          phaser.arriveAndAwaitAdvance();  // Phase 1
          System.out.println("Main: Phase 1 complete");
          
          phaser.arriveAndAwaitAdvance();  // Phase 2
          System.out.println("Main: Phase 2 complete");
          
          phaser.arriveAndDeregister();  // Main deregisters
      }
  }
  ```

  **Real-World Example: Multi-Phase Data Processing**

  ```java
  public class DataProcessingPipeline {
      public static void main(String[] args) {
          int numWorkers = 4;
          Phaser phaser = new Phaser(numWorkers) {
              @Override
              protected boolean onAdvance(int phase, int registeredParties) {
                  System.out.println("Phase " + phase + " completed. Parties: " + registeredParties);
                  return phase >= 2 || registeredParties == 0;  // Terminate after phase 2
              }
          };
          
          for (int i = 0; i < numWorkers; i++) {
              final int workerId = i;
              new Thread(() -> {
                  // Phase 0: Load data
                  System.out.println("Worker " + workerId + " loading data");
                  sleep(1000);
                  phaser.arriveAndAwaitAdvance();
                  
                  // Phase 1: Process data
                  System.out.println("Worker " + workerId + " processing data");
                  sleep(1000);
                  phaser.arriveAndAwaitAdvance();
                  
                  // Phase 2: Save results
                  System.out.println("Worker " + workerId + " saving results");
                  sleep(1000);
                  phaser.arriveAndAwaitAdvance();
                  
                  System.out.println("Worker " + workerId + " completed");
              }).start();
          }
      }
      
      private static void sleep(long millis) {
          try { Thread.sleep(millis); } catch (InterruptedException e) {}
      }
  }
  ```

  **Phaser Methods:**

  ```java
  Phaser phaser = new Phaser();
  
  // Registration
  phaser.register();                    // Register new party
  phaser.bulkRegister(5);              // Register multiple parties
  
  // Arrival
  phaser.arrive();                      // Arrive but don't wait
  phaser.arriveAndAwaitAdvance();      // Arrive and wait for others
  phaser.arriveAndDeregister();        // Arrive and deregister
  
  // Query
  int phase = phaser.getPhase();       // Current phase number
  int parties = phaser.getRegisteredParties();  // Number of registered parties
  int arrived = phaser.getArrivedParties();     // Number arrived at current phase
  int unarrived = phaser.getUnarrivedParties(); // Number not yet arrived
  
  // Termination
  boolean isTerminated = phaser.isTerminated();
  phaser.forceTermination();           // Force termination
  ```

  **Comparison Table:**

  | Feature | CountDownLatch | CyclicBarrier | Semaphore | Phaser |
  |---------|----------------|---------------|-----------|--------|
  | Reusable | No | Yes | Yes | Yes |
  | Dynamic parties | No | No | N/A | Yes |
  | Barrier action | No | Yes | N/A | Yes (onAdvance) |
  | Use case | Wait for tasks | Synchronize phases | Limit access | Multi-phase sync |
  | Parties can leave | No | No | N/A | Yes (deregister) |
  | Timeout support | Yes | Yes | Yes | Yes |

  **When to Use:**

  + **CountDownLatch:** Wait for N tasks to complete (one-time event)
    - Example: Wait for services to start, wait for workers to finish
  
  + **CyclicBarrier:** Synchronize threads at multiple points (reusable)
    - Example: Parallel algorithms with multiple phases, game rounds
  
  + **Semaphore:** Limit concurrent access to resources
    - Example: Connection pools, rate limiting, resource management
  
  + **Phaser:** Complex multi-phase synchronization with dynamic parties
    - Example: Data processing pipelines, iterative algorithms, dynamic workflows
  
</details>

<details>
  <summary>Advanced Locks: ReentrantLock, ReadWriteLock, StampedLock</summary>
  <br/>

  Java provides advanced lock implementations with more features than synchronized keyword.

  **1. ReentrantLock**

  A reentrant mutual exclusion lock with extended capabilities.

  **Basic Usage:**

  ```java
  import java.util.concurrent.locks.ReentrantLock;
  
  public class ReentrantLockExample {
      private final ReentrantLock lock = new ReentrantLock();
      private int count = 0;
      
      public void increment() {
          lock.lock();  // Acquire lock
          try {
              count++;
          } finally {
              lock.unlock();  // Always release in finally
          }
      }
      
      public int getCount() {
          lock.lock();
          try {
              return count;
          } finally {
              lock.unlock();
          }
      }
  }
  ```

  **Why "Reentrant"?**

  A thread can acquire the same lock multiple times without deadlocking itself.

  ```java
  public class ReentrantExample {
      private final ReentrantLock lock = new ReentrantLock();
      
      public void outerMethod() {
          lock.lock();
          try {
              System.out.println("Outer method");
              innerMethod();  // Can acquire lock again
          } finally {
              lock.unlock();
          }
      }
      
      public void innerMethod() {
          lock.lock();  // Same thread acquires lock again (reentrant)
          try {
              System.out.println("Inner method");
          } finally {
              lock.unlock();
          }
      }
  }
  ```

  **tryLock() - Non-blocking Lock Attempt:**

  ```java
  public class TryLockExample {
      private final ReentrantLock lock = new ReentrantLock();
      
      public void doWork() {
          // Try to acquire lock immediately
          if (lock.tryLock()) {
              try {
                  // Got the lock, do work
                  System.out.println("Lock acquired");
              } finally {
                  lock.unlock();
              }
          } else {
              // Couldn't get lock, do alternative action
              System.out.println("Lock not available, doing something else");
          }
      }
      
      public void doWorkWithTimeout() {
          try {
              // Try to acquire lock with timeout
              if (lock.tryLock(2, TimeUnit.SECONDS)) {
                  try {
                      // Got the lock within 2 seconds
                      System.out.println("Lock acquired within timeout");
                  } finally {
                      lock.unlock();
                  }
              } else {
                  System.out.println("Timeout waiting for lock");
              }
          } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
          }
      }
  }
  ```

  **Fairness:**

  ```java
  // Fair lock - threads acquire lock in order they requested it
  ReentrantLock fairLock = new ReentrantLock(true);
  
  // Unfair lock (default) - no guarantee of order, better performance
  ReentrantLock unfairLock = new ReentrantLock(false);
  
  public class FairnessExample {
      public static void main(String[] args) {
          ReentrantLock fairLock = new ReentrantLock(true);
          
          for (int i = 0; i < 5; i++) {
              final int threadId = i;
              new Thread(() -> {
                  fairLock.lock();
                  try {
                      System.out.println("Thread " + threadId + " acquired lock");
                      Thread.sleep(100);
                  } catch (InterruptedException e) {
                      e.printStackTrace();
                  } finally {
                      fairLock.unlock();
                  }
              }).start();
          }
      }
  }
  ```

  **Interruptible Lock:**

  ```java
  public class InterruptibleLockExample {
      private final ReentrantLock lock = new ReentrantLock();
      
      public void doWork() {
          try {
              // Can be interrupted while waiting for lock
              lock.lockInterruptibly();
              try {
                  // Do work
                  System.out.println("Working...");
                  Thread.sleep(5000);
              } finally {
                  lock.unlock();
              }
          } catch (InterruptedException e) {
              System.out.println("Interrupted while waiting for lock");
              Thread.currentThread().interrupt();
          }
      }
  }
  ```

  **Condition Variables:**

  ```java
  import java.util.concurrent.locks.Condition;
  
  public class BoundedBuffer<T> {
      private final ReentrantLock lock = new ReentrantLock();
      private final Condition notFull = lock.newCondition();
      private final Condition notEmpty = lock.newCondition();
      
      private final T[] buffer;
      private int count, putIndex, takeIndex;
      
      @SuppressWarnings("unchecked")
      public BoundedBuffer(int capacity) {
          buffer = (T[]) new Object[capacity];
      }
      
      public void put(T item) throws InterruptedException {
          lock.lock();
          try {
              while (count == buffer.length) {
                  notFull.await();  // Wait until not full
              }
              buffer[putIndex] = item;
              putIndex = (putIndex + 1) % buffer.length;
              count++;
              notEmpty.signal();  // Signal that buffer is not empty
          } finally {
              lock.unlock();
          }
      }
      
      public T take() throws InterruptedException {
          lock.lock();
          try {
              while (count == 0) {
                  notEmpty.await();  // Wait until not empty
              }
              T item = buffer[takeIndex];
              buffer[takeIndex] = null;
              takeIndex = (takeIndex + 1) % buffer.length;
              count--;
              notFull.signal();  // Signal that buffer is not full
              return item;
          } finally {
              lock.unlock();
          }
      }
  }
  ```

  **Monitoring Lock State:**

  ```java
  ReentrantLock lock = new ReentrantLock();
  
  // Check lock state
  boolean isLocked = lock.isLocked();
  boolean isHeldByCurrentThread = lock.isHeldByCurrentThread();
  int holdCount = lock.getHoldCount();  // Number of times current thread holds lock
  
  // Check waiting threads
  int queueLength = lock.getQueueLength();
  boolean hasQueuedThreads = lock.hasQueuedThreads();
  
  // Fair lock specific
  boolean isFair = lock.isFair();
  ```

  **2. ReadWriteLock**

  Allows multiple readers OR one writer. Improves performance for read-heavy scenarios.

  **Basic Usage:**

  ```java
  import java.util.concurrent.locks.ReadWriteLock;
  import java.util.concurrent.locks.ReentrantReadWriteLock;
  
  public class ReadWriteLockExample {
      private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
      private final Map<String, String> cache = new HashMap<>();
      
      // Multiple threads can read simultaneously
      public String read(String key) {
          rwLock.readLock().lock();
          try {
              return cache.get(key);
          } finally {
              rwLock.readLock().unlock();
          }
      }
      
      // Only one thread can write at a time
      public void write(String key, String value) {
          rwLock.writeLock().lock();
          try {
              cache.put(key, value);
          } finally {
              rwLock.writeLock().unlock();
          }
      }
  }
  ```

  **Real-World Example: Cache Implementation**

  ```java
  public class ThreadSafeCache<K, V> {
      private final Map<K, V> cache = new HashMap<>();
      private final ReadWriteLock lock = new ReentrantReadWriteLock();
      
      public V get(K key) {
          lock.readLock().lock();
          try {
              return cache.get(key);
          } finally {
              lock.readLock().unlock();
          }
      }
      
      public void put(K key, V value) {
          lock.writeLock().lock();
          try {
              cache.put(key, value);
          } finally {
              lock.writeLock().unlock();
          }
      }
      
      public V computeIfAbsent(K key, Function<K, V> mappingFunction) {
          // Try read lock first
          lock.readLock().lock();
          try {
              V value = cache.get(key);
              if (value != null) {
                  return value;
              }
          } finally {
              lock.readLock().unlock();
          }
          
          // Need to compute, acquire write lock
          lock.writeLock().lock();
          try {
              // Double-check after acquiring write lock
              V value = cache.get(key);
              if (value == null) {
                  value = mappingFunction.apply(key);
                  cache.put(key, value);
              }
              return value;
          } finally {
              lock.writeLock().unlock();
          }
      }
      
      public void clear() {
          lock.writeLock().lock();
          try {
              cache.clear();
          } finally {
              lock.writeLock().unlock();
          }
      }
      
      public int size() {
          lock.readLock().lock();
          try {
              return cache.size();
          } finally {
              lock.readLock().unlock();
          }
      }
  }
  ```

  **Lock Downgrading:**

  ```java
  public class LockDowngradingExample {
      private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
      private Map<String, String> data = new HashMap<>();
      
      public void updateData(String key, String value) {
          rwLock.writeLock().lock();
          try {
              data.put(key, value);
              
              // Downgrade to read lock
              rwLock.readLock().lock();  // Acquire read lock before releasing write lock
          } finally {
              rwLock.writeLock().unlock();  // Release write lock
          }
          
          try {
              // Now holding read lock, can read data
              String result = data.get(key);
              System.out.println("Updated value: " + result);
          } finally {
              rwLock.readLock().unlock();
          }
      }
  }
  ```

  **Note:** Lock upgrading (read → write) is NOT supported and will cause deadlock!

  ```java
  // ❌ DEADLOCK! Cannot upgrade from read to write lock
  rwLock.readLock().lock();
  try {
      // Read data
      rwLock.writeLock().lock();  // DEADLOCK!
      try {
          // Write data
      } finally {
          rwLock.writeLock().unlock();
      }
  } finally {
      rwLock.readLock().unlock();
  }
  ```

  **3. StampedLock (Java 8+)**

  More advanced lock with optimistic reading. Better performance than ReadWriteLock in some scenarios.

  **Three Lock Modes:**
  1. **Write Lock:** Exclusive lock (like write lock in ReadWriteLock)
  2. **Read Lock:** Shared lock (like read lock in ReadWriteLock)
  3. **Optimistic Read:** Non-blocking read that may need validation

  **Basic Usage:**

  ```java
  import java.util.concurrent.locks.StampedLock;
  
  public class StampedLockExample {
      private final StampedLock lock = new StampedLock();
      private double x, y;
      
      // Write lock
      public void move(double deltaX, double deltaY) {
          long stamp = lock.writeLock();  // Acquire write lock
          try {
              x += deltaX;
              y += deltaY;
          } finally {
              lock.unlockWrite(stamp);  // Release write lock
          }
      }
      
      // Read lock
      public double distanceFromOrigin() {
          long stamp = lock.readLock();  // Acquire read lock
          try {
              return Math.sqrt(x * x + y * y);
          } finally {
              lock.unlockRead(stamp);  // Release read lock
          }
      }
      
      // Optimistic read (best performance)
      public double distanceFromOriginOptimistic() {
          long stamp = lock.tryOptimisticRead();  // Optimistic read
          double currentX = x;
          double currentY = y;
          
          if (!lock.validate(stamp)) {  // Check if data was modified
              // Data was modified, acquire read lock
              stamp = lock.readLock();
              try {
                  currentX = x;
                  currentY = y;
              } finally {
                  lock.unlockRead(stamp);
              }
          }
          
          return Math.sqrt(currentX * currentX + currentY * currentY);
      }
  }
  ```

  **Optimistic Read Explained:**

  ```java
  public class OptimisticReadExample {
      private final StampedLock lock = new StampedLock();
      private int value = 0;
      
      public int optimisticRead() {
          // 1. Try optimistic read (no actual locking)
          long stamp = lock.tryOptimisticRead();
          
          // 2. Read data
          int currentValue = value;
          
          // 3. Validate - check if data was modified during read
          if (!lock.validate(stamp)) {
              // Data was modified, need to acquire actual read lock
              stamp = lock.readLock();
              try {
                  currentValue = value;
              } finally {
                  lock.unlockRead(stamp);
              }
          }
          
          return currentValue;
      }
      
      public void write(int newValue) {
          long stamp = lock.writeLock();
          try {
              value = newValue;
          } finally {
              lock.unlockWrite(stamp);
          }
      }
  }
  ```

  **Lock Conversion:**

  ```java
  public class LockConversionExample {
      private final StampedLock lock = new StampedLock();
      private int value = 0;
      
      public void conditionalWrite(int newValue) {
          // Start with optimistic read
          long stamp = lock.tryOptimisticRead();
          int currentValue = value;
          
          if (!lock.validate(stamp)) {
              // Upgrade to read lock
              stamp = lock.readLock();
              try {
                  currentValue = value;
              } finally {
                  lock.unlockRead(stamp);
              }
          }
          
          if (currentValue < newValue) {
              // Need to write, acquire write lock
              stamp = lock.writeLock();
              try {
                  if (value < newValue) {  // Double-check
                      value = newValue;
                  }
              } finally {
                  lock.unlockWrite(stamp);
              }
          }
      }
      
      // Try to convert read lock to write lock
      public void tryConvertToWrite() {
          long stamp = lock.readLock();
          try {
              // Try to convert to write lock
              long writeStamp = lock.tryConvertToWriteLock(stamp);
              if (writeStamp != 0) {
                  // Conversion successful
                  stamp = writeStamp;
                  value++;
              } else {
                  // Conversion failed, release read lock and acquire write lock
                  lock.unlockRead(stamp);
                  stamp = lock.writeLock();
                  value++;
              }
          } finally {
              lock.unlock(stamp);  // Works for both read and write locks
          }
      }
  }
  ```

  **Real-World Example: Point Tracker**

  ```java
  public class PointTracker {
      private final StampedLock lock = new StampedLock();
      private double x, y;
      
      public void move(double deltaX, double deltaY) {
          long stamp = lock.writeLock();
          try {
              x += deltaX;
              y += deltaY;
          } finally {
              lock.unlockWrite(stamp);
          }
      }
      
      public double[] getCoordinates() {
          long stamp = lock.tryOptimisticRead();
          double currentX = x;
          double currentY = y;
          
          if (!lock.validate(stamp)) {
              stamp = lock.readLock();
              try {
                  currentX = x;
                  currentY = y;
              } finally {
                  lock.unlockRead(stamp);
              }
          }
          
          return new double[]{currentX, currentY};
      }
      
      public void moveIfAtOrigin(double newX, double newY) {
          long stamp = lock.readLock();
          try {
              while (x == 0.0 && y == 0.0) {
                  // Try to upgrade to write lock
                  long writeStamp = lock.tryConvertToWriteLock(stamp);
                  if (writeStamp != 0) {
                      stamp = writeStamp;
                      x = newX;
                      y = newY;
                      break;
                  } else {
                      // Upgrade failed, release and acquire write lock
                      lock.unlockRead(stamp);
                      stamp = lock.writeLock();
                  }
              }
          } finally {
              lock.unlock(stamp);
          }
      }
  }
  ```

  **Lock Comparison:**

  | Feature | synchronized | ReentrantLock | ReadWriteLock | StampedLock |
  |---------|--------------|---------------|---------------|-------------|
  | Syntax | Simple | Verbose | Verbose | Verbose |
  | Try lock | No | Yes | Yes | Yes |
  | Timeout | No | Yes | Yes | Yes |
  | Fairness | No | Optional | Optional | No |
  | Interruptible | No | Yes | Yes | No |
  | Condition variables | 1 | Multiple | Multiple | No |
  | Read/Write separation | No | No | Yes | Yes |
  | Optimistic read | No | No | No | Yes |
  | Reentrant | Yes | Yes | Yes | No |
  | Performance | Good | Good | Better (read-heavy) | Best (read-heavy) |

  **When to Use:**

  + **synchronized:** Simple cases, automatic management
  + **ReentrantLock:** Need tryLock, timeout, fairness, or multiple conditions
  + **ReadWriteLock:** Read-heavy workload with occasional writes
  + **StampedLock:** Very read-heavy workload, need maximum performance

  **Performance Comparison Example:**

  ```java
  public class LockPerformanceComparison {
      private int value = 0;
      
      // synchronized
      public synchronized int readSync() {
          return value;
      }
      
      public synchronized void writeSync(int newValue) {
          value = newValue;
      }
      
      // ReadWriteLock
      private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
      
      public int readRW() {
          rwLock.readLock().lock();
          try {
              return value;
          } finally {
              rwLock.readLock().unlock();
          }
      }
      
      public void writeRW(int newValue) {
          rwLock.writeLock().lock();
          try {
              value = newValue;
          } finally {
              rwLock.writeLock().unlock();
          }
      }
      
      // StampedLock
      private final StampedLock stampedLock = new StampedLock();
      
      public int readStamped() {
          long stamp = stampedLock.tryOptimisticRead();
          int currentValue = value;
          if (!stampedLock.validate(stamp)) {
              stamp = stampedLock.readLock();
              try {
                  currentValue = value;
              } finally {
                  stampedLock.unlockRead(stamp);
              }
          }
          return currentValue;
      }
      
      public void writeStamped(int newValue) {
          long stamp = stampedLock.writeLock();
          try {
              value = newValue;
          } finally {
              stampedLock.unlockWrite(stamp);
          }
      }
  }
  ```

  **Best Practices:**

  ```java
  public class LockBestPractices {
      private final ReentrantLock lock = new ReentrantLock();
      
      // ✅ Always use try-finally
      public void goodExample() {
          lock.lock();
          try {
              // Critical section
          } finally {
              lock.unlock();  // Always release
          }
      }
      
      // ❌ Bad: No finally block
      public void badExample() {
          lock.lock();
          // Critical section
          lock.unlock();  // May not execute if exception thrown
      }
      
      // ✅ Use tryLock to avoid deadlock
      public void tryLockExample() {
          if (lock.tryLock()) {
              try {
                  // Got lock
              } finally {
                  lock.unlock();
              }
          } else {
              // Couldn't get lock, do alternative
          }
      }
      
      // ✅ Check lock state before unlocking
      public void safeUnlock() {
          if (lock.isHeldByCurrentThread()) {
              lock.unlock();
          }
      }
  }
  ```
  
</details>


<details>
  <summary>Concurrent Collections: ConcurrentHashMap, CopyOnWriteArrayList</summary>
  <br/>

  Java provides thread-safe collection implementations optimized for concurrent access.

  **1. ConcurrentHashMap**

  Thread-safe HashMap with better concurrency than synchronized HashMap or Hashtable.

  **How it Works:**

  + **Segmented Locking (Java 7):** Divides map into segments, each with its own lock
  + **Lock-Free Reads (Java 8+):** Uses CAS operations and volatile variables
  + **No locking for reads:** Multiple threads can read simultaneously
  + **Fine-grained locking for writes:** Only locks affected bucket/node

  **Basic Usage:**

  ```java
  import java.util.concurrent.ConcurrentHashMap;
  
  public class ConcurrentHashMapExample {
      public static void main(String[] args) {
          ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
          
          // Put operations
          map.put("A", 1);
          map.put("B", 2);
          map.put("C", 3);
          
          // Get operations (thread-safe, no locking)
          Integer value = map.get("A");
          
          // putIfAbsent - atomic operation
          Integer previous = map.putIfAbsent("D", 4);
          
          // Remove
          map.remove("B");
          
          // Replace
          map.replace("A", 1, 10);  // Replace only if current value is 1
          
          // Iterate (weakly consistent - may not reflect recent updates)
          for (Map.Entry<String, Integer> entry : map.entrySet()) {
              System.out.println(entry.getKey() + ": " + entry.getValue());
          }
      }
  }
  ```

  **Atomic Operations (Java 8+):**

  ```java
  ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
  
  // computeIfAbsent - compute value if key doesn't exist
  map.computeIfAbsent("key", k -> expensiveComputation(k));
  
  // computeIfPresent - compute new value if key exists
  map.computeIfPresent("key", (k, v) -> v + 1);
  
  // compute - compute value regardless
  map.compute("key", (k, v) -> v == null ? 1 : v + 1);
  
  // merge - merge values
  map.merge("key", 1, (oldValue, newValue) -> oldValue + newValue);
  
  // Example: Word count
  String[] words = {"apple", "banana", "apple", "cherry", "banana", "apple"};
  ConcurrentHashMap<String, Integer> wordCount = new ConcurrentHashMap<>();
  
  for (String word : words) {
      wordCount.merge(word, 1, Integer::sum);
  }
  
  System.out.println(wordCount);  // {apple=3, banana=2, cherry=1}
  ```

  **Bulk Operations (Java 8+):**

  ```java
  ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
  map.put("A", 1);
  map.put("B", 2);
  map.put("C", 3);
  map.put("D", 4);
  
  // forEach - perform action on each entry
  map.forEach(1, (key, value) -> {
      System.out.println(key + ": " + value);
  });
  
  // search - find first matching entry
  String result = map.search(1, (key, value) -> {
      return value > 2 ? key : null;
  });
  
  // reduce - combine all entries
  Integer sum = map.reduce(1,
      (key, value) -> value,  // Transformer
      (v1, v2) -> v1 + v2     // Reducer
  );
  
  // reduceKeys
  String concatenated = map.reduceKeys(1, (k1, k2) -> k1 + k2);
  
  // reduceValues
  Integer maxValue = map.reduceValues(1, (v1, v2) -> Math.max(v1, v2));
  ```

  **Parallelism Threshold:**

  The first parameter in bulk operations is the parallelism threshold:
  + If map size > threshold, operation is performed in parallel
  + Use `1` for parallel execution
  + Use `Long.MAX_VALUE` for sequential execution

  **Real-World Example: Cache Implementation**

  ```java
  public class ThreadSafeCache<K, V> {
      private final ConcurrentHashMap<K, V> cache = new ConcurrentHashMap<>();
      private final Function<K, V> loader;
      
      public ThreadSafeCache(Function<K, V> loader) {
          this.loader = loader;
      }
      
      public V get(K key) {
          // Atomic: compute if absent
          return cache.computeIfAbsent(key, loader);
      }
      
      public void put(K key, V value) {
          cache.put(key, value);
      }
      
      public void invalidate(K key) {
          cache.remove(key);
      }
      
      public void invalidateAll() {
          cache.clear();
      }
      
      public int size() {
          return cache.size();
      }
  }
  
  // Usage
  ThreadSafeCache<String, User> userCache = new ThreadSafeCache<>(userId -> {
      // Load from database
      return database.findUser(userId);
  });
  
  // Multiple threads can safely access
  User user = userCache.get("user123");
  ```

  **Real-World Example: Request Counter**

  ```java
  public class RequestCounter {
      private final ConcurrentHashMap<String, AtomicLong> counters = new ConcurrentHashMap<>();
      
      public void recordRequest(String endpoint) {
          counters.computeIfAbsent(endpoint, k -> new AtomicLong(0))
                  .incrementAndGet();
      }
      
      public long getCount(String endpoint) {
          AtomicLong counter = counters.get(endpoint);
          return counter != null ? counter.get() : 0;
      }
      
      public Map<String, Long> getAllCounts() {
          Map<String, Long> result = new HashMap<>();
          counters.forEach((endpoint, counter) -> {
              result.put(endpoint, counter.get());
          });
          return result;
      }
      
      public void reset() {
          counters.clear();
      }
  }
  ```

  **ConcurrentHashMap vs Hashtable vs Collections.synchronizedMap:**

  ```java
  // 1. Hashtable (legacy, avoid)
  Hashtable<String, Integer> hashtable = new Hashtable<>();
  // - Synchronized on entire table
  // - Poor concurrency
  // - Null keys/values not allowed
  
  // 2. Collections.synchronizedMap
  Map<String, Integer> syncMap = Collections.synchronizedMap(new HashMap<>());
  // - Synchronized on entire map
  // - Poor concurrency
  // - Must manually synchronize iteration
  synchronized (syncMap) {
      for (Map.Entry<String, Integer> entry : syncMap.entrySet()) {
          // Process entry
      }
  }
  
  // 3. ConcurrentHashMap (best)
  ConcurrentHashMap<String, Integer> concurrentMap = new ConcurrentHashMap<>();
  // - Fine-grained locking
  // - Excellent concurrency
  // - Lock-free reads
  // - Null keys/values not allowed
  // - No need to synchronize iteration
  ```

  **Comparison Table:**

  | Feature | HashMap | Hashtable | SynchronizedMap | ConcurrentHashMap |
  |---------|---------|-----------|-----------------|-------------------|
  | Thread-safe | No | Yes | Yes | Yes |
  | Null key | Yes | No | Yes | No |
  | Null value | Yes | No | Yes | No |
  | Locking | None | Entire table | Entire map | Fine-grained |
  | Read performance | Fast | Slow | Slow | Fast |
  | Write performance | Fast | Slow | Slow | Good |
  | Iteration | Fail-fast | Fail-fast | Manual sync needed | Weakly consistent |
  | Recommended | Single-thread | Never | Rarely | Multi-thread |

  **2. CopyOnWriteArrayList**

  Thread-safe ArrayList where all mutative operations create a new copy of the underlying array.

  **How it Works:**

  + **Reads:** No locking, reads from current array snapshot
  + **Writes:** Lock, copy entire array, modify copy, replace reference
  + **Iteration:** Uses snapshot, never throws ConcurrentModificationException
  + **Best for:** Read-heavy scenarios with infrequent writes

  **Basic Usage:**

  ```java
  import java.util.concurrent.CopyOnWriteArrayList;
  
  public class CopyOnWriteArrayListExample {
      public static void main(String[] args) {
          CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
          
          // Add elements
          list.add("A");
          list.add("B");
          list.add("C");
          
          // Read (no locking)
          String element = list.get(0);
          
          // Iterate (safe, uses snapshot)
          for (String item : list) {
              System.out.println(item);
              // Can modify list during iteration (won't affect this iteration)
              list.add("D");
          }
          
          // Remove
          list.remove("B");
          
          // Contains
          boolean contains = list.contains("A");
      }
  }
  ```

  **Safe Iteration:**

  ```java
  CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
  list.add("A");
  list.add("B");
  list.add("C");
  
  // Thread 1: Iterate
  new Thread(() -> {
      for (String item : list) {
          System.out.println("Reading: " + item);
          try {
              Thread.sleep(100);
          } catch (InterruptedException e) {
              e.printStackTrace();
          }
      }
  }).start();
  
  // Thread 2: Modify during iteration
  new Thread(() -> {
      try {
          Thread.sleep(50);
          list.add("D");  // Safe! Won't affect ongoing iteration
          System.out.println("Added D");
      } catch (InterruptedException e) {
          e.printStackTrace();
      }
  }).start();
  ```

  **Real-World Example: Event Listeners**

  ```java
  public class EventManager {
      private final CopyOnWriteArrayList<EventListener> listeners = 
          new CopyOnWriteArrayList<>();
      
      public void addListener(EventListener listener) {
          listeners.add(listener);
      }
      
      public void removeListener(EventListener listener) {
          listeners.remove(listener);
      }
      
      public void fireEvent(Event event) {
          // Safe to iterate even if listeners are added/removed concurrently
          for (EventListener listener : listeners) {
              try {
                  listener.onEvent(event);
              } catch (Exception e) {
                  // Handle listener exception
                  e.printStackTrace();
              }
          }
      }
  }
  
  interface EventListener {
      void onEvent(Event event);
  }
  
  class Event {
      private final String type;
      private final Object data;
      
      public Event(String type, Object data) {
          this.type = type;
          this.data = data;
      }
      
      // Getters
  }
  ```

  **Real-World Example: Observer Pattern**

  ```java
  public class Subject {
      private final CopyOnWriteArrayList<Observer> observers = 
          new CopyOnWriteArrayList<>();
      private String state;
      
      public void attach(Observer observer) {
          observers.add(observer);
      }
      
      public void detach(Observer observer) {
          observers.remove(observer);
      }
      
      public void setState(String state) {
          this.state = state;
          notifyObservers();
      }
      
      private void notifyObservers() {
          // Safe iteration, observers can attach/detach during notification
          for (Observer observer : observers) {
              observer.update(state);
          }
      }
  }
  
  interface Observer {
      void update(String state);
  }
  ```

  **Performance Considerations:**

  ```java
  public class CopyOnWritePerformance {
      public static void main(String[] args) {
          CopyOnWriteArrayList<Integer> cowList = new CopyOnWriteArrayList<>();
          
          // ❌ Bad: Frequent writes (copies array each time)
          long start = System.currentTimeMillis();
          for (int i = 0; i < 10000; i++) {
              cowList.add(i);  // Very slow! 10000 array copies
          }
          long end = System.currentTimeMillis();
          System.out.println("Time: " + (end - start) + "ms");  // Very slow
          
          // ✅ Good: Batch writes
          List<Integer> temp = new ArrayList<>();
          for (int i = 0; i < 10000; i++) {
              temp.add(i);
          }
          cowList.addAll(temp);  // Single copy
          
          // ✅ Good: Many reads (very fast, no locking)
          start = System.currentTimeMillis();
          for (int i = 0; i < 1000000; i++) {
              int value = cowList.get(i % cowList.size());
          }
          end = System.currentTimeMillis();
          System.out.println("Read time: " + (end - start) + "ms");  // Very fast
      }
  }
  ```

  **3. CopyOnWriteArraySet**

  Thread-safe Set implementation backed by CopyOnWriteArrayList.

  ```java
  import java.util.concurrent.CopyOnWriteArraySet;
  
  CopyOnWriteArraySet<String> set = new CopyOnWriteArraySet<>();
  
  set.add("A");
  set.add("B");
  set.add("A");  // Duplicate, not added
  
  System.out.println(set.size());  // 2
  
  // Safe iteration
  for (String item : set) {
      set.add("C");  // Safe during iteration
  }
  ```

  **4. ConcurrentLinkedQueue**

  Thread-safe unbounded queue based on linked nodes.

  ```java
  import java.util.concurrent.ConcurrentLinkedQueue;
  
  ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();
  
  // Add elements
  queue.offer("A");
  queue.offer("B");
  queue.offer("C");
  
  // Remove and return head
  String head = queue.poll();  // "A"
  
  // Peek at head without removing
  String peek = queue.peek();  // "B"
  
  // Size (may not be accurate in concurrent environment)
  int size = queue.size();
  ```

  **5. ConcurrentLinkedDeque**

  Thread-safe double-ended queue.

  ```java
  import java.util.concurrent.ConcurrentLinkedDeque;
  
  ConcurrentLinkedDeque<String> deque = new ConcurrentLinkedDeque<>();
  
  // Add at both ends
  deque.offerFirst("A");
  deque.offerLast("B");
  
  // Remove from both ends
  String first = deque.pollFirst();
  String last = deque.pollLast();
  ```

  **6. BlockingQueue Implementations**

  Thread-safe queues with blocking operations.

  ```java
  import java.util.concurrent.*;
  
  // ArrayBlockingQueue - bounded queue backed by array
  BlockingQueue<String> arrayQueue = new ArrayBlockingQueue<>(10);
  
  // LinkedBlockingQueue - optionally bounded queue backed by linked nodes
  BlockingQueue<String> linkedQueue = new LinkedBlockingQueue<>();
  BlockingQueue<String> boundedLinkedQueue = new LinkedBlockingQueue<>(100);
  
  // PriorityBlockingQueue - unbounded priority queue
  BlockingQueue<Integer> priorityQueue = new PriorityBlockingQueue<>();
  
  // SynchronousQueue - queue with no capacity (direct handoff)
  BlockingQueue<String> syncQueue = new SynchronousQueue<>();
  
  // DelayQueue - queue where elements can only be taken when delay expires
  BlockingQueue<Delayed> delayQueue = new DelayQueue<>();
  
  // Blocking operations
  arrayQueue.put("A");           // Blocks if full
  String item = arrayQueue.take(); // Blocks if empty
  
  // Timed operations
  boolean added = arrayQueue.offer("B", 1, TimeUnit.SECONDS);
  String item2 = arrayQueue.poll(1, TimeUnit.SECONDS);
  ```

  **When to Use Each Collection:**

  | Collection | Use Case | Read Performance | Write Performance |
  |------------|----------|------------------|-------------------|
  | ConcurrentHashMap | General-purpose map | Excellent | Good |
  | CopyOnWriteArrayList | Read-heavy list, few writes | Excellent | Poor |
  | CopyOnWriteArraySet | Read-heavy set, few writes | Excellent | Poor |
  | ConcurrentLinkedQueue | Unbounded queue | Good | Good |
  | ConcurrentLinkedDeque | Unbounded deque | Good | Good |
  | ArrayBlockingQueue | Bounded queue | Good | Good |
  | LinkedBlockingQueue | Optionally bounded queue | Good | Good |

  **Best Practices:**

  ```java
  public class ConcurrentCollectionsBestPractices {
      // ✅ Use ConcurrentHashMap for general concurrent map
      private final ConcurrentHashMap<String, User> userCache = new ConcurrentHashMap<>();
      
      // ✅ Use CopyOnWriteArrayList for listeners/observers
      private final CopyOnWriteArrayList<EventListener> listeners = 
          new CopyOnWriteArrayList<>();
      
      // ✅ Use BlockingQueue for producer-consumer
      private final BlockingQueue<Task> taskQueue = new LinkedBlockingQueue<>();
      
      // ❌ Don't use CopyOnWriteArrayList for frequent writes
      public void badExample() {
          CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<>();
          for (int i = 0; i < 10000; i++) {
              list.add(i);  // Very slow!
          }
      }
      
      // ✅ Batch writes for CopyOnWriteArrayList
      public void goodExample() {
          List<Integer> temp = new ArrayList<>();
          for (int i = 0; i < 10000; i++) {
              temp.add(i);
          }
          CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<>(temp);
      }
      
      // ✅ Use atomic operations in ConcurrentHashMap
      public void atomicOperations() {
          userCache.computeIfAbsent("key", k -> loadUser(k));
          userCache.merge("key", newUser, (old, new_) -> mergeUsers(old, new_));
      }
      
      private User loadUser(String key) { return null; }
      private User mergeUsers(User old, User new_) { return new_; }
  }
  ```
  
</details>

<details>
  <summary>Atomic Classes: AtomicInteger, AtomicReference, and More</summary>
  <br/>

  Atomic classes provide lock-free thread-safe operations on single variables using Compare-And-Swap (CAS) operations.

  **Why Use Atomic Classes?**

  ```java
  // ❌ Problem: Non-atomic increment
  public class Counter {
      private int count = 0;
      
      public void increment() {
          count++;  // NOT atomic! (read, increment, write)
      }
  }
  
  // ❌ Solution 1: synchronized (works but slower)
  public class SynchronizedCounter {
      private int count = 0;
      
      public synchronized void increment() {
          count++;  // Thread-safe but uses locking
      }
  }
  
  // ✅ Solution 2: AtomicInteger (lock-free, faster)
  public class AtomicCounter {
      private AtomicInteger count = new AtomicInteger(0);
      
      public void increment() {
          count.incrementAndGet();  // Thread-safe, lock-free
      }
  }
  ```

  **How Atomic Classes Work:**

  + Use **Compare-And-Swap (CAS)** CPU instruction
  + Lock-free algorithm
  + Retry loop until successful
  + Better performance than synchronized in low-contention scenarios

  **CAS Operation:**
  ```
  boolean compareAndSet(expectedValue, newValue) {
      if (currentValue == expectedValue) {
          currentValue = newValue;
          return true;
      }
      return false;
  }
  ```

  **1. AtomicInteger**

  Thread-safe integer with atomic operations.

  **Basic Operations:**

  ```java
  import java.util.concurrent.atomic.AtomicInteger;
  
  public class AtomicIntegerExample {
      public static void main(String[] args) {
          AtomicInteger counter = new AtomicInteger(0);
          
          // Get and set
          int value = counter.get();
          counter.set(10);
          
          // Atomic increment/decrement
          int newValue = counter.incrementAndGet();  // ++counter
          int oldValue = counter.getAndIncrement();  // counter++
          int newValue2 = counter.decrementAndGet(); // --counter
          int oldValue2 = counter.getAndDecrement(); // counter--
          
          // Atomic add
          int result = counter.addAndGet(5);    // counter += 5, return new value
          int result2 = counter.getAndAdd(5);   // counter += 5, return old value
          
          // Compare and set
          boolean success = counter.compareAndSet(10, 20);  // If value is 10, set to 20
          
          // Get and set
          int previous = counter.getAndSet(100);  // Set to 100, return old value
          
          // Update with function (Java 8+)
          counter.updateAndGet(v -> v * 2);     // Update and return new value
          counter.getAndUpdate(v -> v * 2);     // Update and return old value
          
          // Accumulate with function
          counter.accumulateAndGet(5, (current, x) -> current + x);
          counter.getAndAccumulate(5, (current, x) -> current + x);
      }
  }
  ```

  **Real-World Example: Request Counter**

  ```java
  public class RequestCounter {
      private final AtomicInteger totalRequests = new AtomicInteger(0);
      private final AtomicInteger successfulRequests = new AtomicInteger(0);
      private final AtomicInteger failedRequests = new AtomicInteger(0);
      
      public void recordSuccess() {
          totalRequests.incrementAndGet();
          successfulRequests.incrementAndGet();
      }
      
      public void recordFailure() {
          totalRequests.incrementAndGet();
          failedRequests.incrementAndGet();
      }
      
      public double getSuccessRate() {
          int total = totalRequests.get();
          if (total == 0) return 0.0;
          return (double) successfulRequests.get() / total * 100;
      }
      
      public void reset() {
          totalRequests.set(0);
          successfulRequests.set(0);
          failedRequests.set(0);
      }
      
      public String getStats() {
          return String.format("Total: %d, Success: %d, Failed: %d, Success Rate: %.2f%%",
              totalRequests.get(),
              successfulRequests.get(),
              failedRequests.get(),
              getSuccessRate());
      }
  }
  ```

  **2. AtomicLong**

  Thread-safe long with atomic operations (same API as AtomicInteger).

  ```java
  import java.util.concurrent.atomic.AtomicLong;
  
  public class AtomicLongExample {
      private final AtomicLong totalBytes = new AtomicLong(0);
      
      public void addBytes(long bytes) {
          totalBytes.addAndGet(bytes);
      }
      
      public long getTotalBytes() {
          return totalBytes.get();
      }
      
      public String getTotalMB() {
          return String.format("%.2f MB", totalBytes.get() / 1024.0 / 1024.0);
      }
  }
  ```

  **3. AtomicBoolean**

  Thread-safe boolean with atomic operations.

  ```java
  import java.util.concurrent.atomic.AtomicBoolean;
  
  public class AtomicBooleanExample {
      private final AtomicBoolean initialized = new AtomicBoolean(false);
      private final AtomicBoolean shutdownRequested = new AtomicBoolean(false);
      
      public void initialize() {
          // Ensure initialization happens only once
          if (initialized.compareAndSet(false, true)) {
              // Perform initialization
              System.out.println("Initializing...");
              // Only one thread will execute this
          }
      }
      
      public void shutdown() {
          shutdownRequested.set(true);
      }
      
      public void run() {
          while (!shutdownRequested.get()) {
              // Do work
          }
      }
  }
  ```

  **Real-World Example: One-Time Initialization**

  ```java
  public class LazyInitializer {
      private final AtomicBoolean initialized = new AtomicBoolean(false);
      private ExpensiveResource resource;
      
      public ExpensiveResource getResource() {
          if (!initialized.get()) {
              synchronized (this) {
                  if (initialized.compareAndSet(false, true)) {
                      resource = new ExpensiveResource();
                      System.out.println("Resource initialized");
                  }
              }
          }
          return resource;
      }
  }
  ```

  **4. AtomicReference**

  Thread-safe reference to an object.

  ```java
  import java.util.concurrent.atomic.AtomicReference;
  
  public class AtomicReferenceExample {
      private final AtomicReference<String> status = new AtomicReference<>("READY");
      
      public void updateStatus(String newStatus) {
          status.set(newStatus);
      }
      
      public String getStatus() {
          return status.get();
      }
      
      public boolean transitionTo(String expectedStatus, String newStatus) {
          return status.compareAndSet(expectedStatus, newStatus);
      }
      
      public static void main(String[] args) {
          AtomicReferenceExample example = new AtomicReferenceExample();
          
          // Transition from READY to RUNNING
          boolean success = example.transitionTo("READY", "RUNNING");
          System.out.println("Transition successful: " + success);  // true
          
          // Try to transition from READY to STOPPED (will fail)
          success = example.transitionTo("READY", "STOPPED");
          System.out.println("Transition successful: " + success);  // false
      }
  }
  ```

  **Real-World Example: Immutable State Updates**

  ```java
  public class UserSession {
      private static class SessionData {
          final String userId;
          final long loginTime;
          final int requestCount;
          
          SessionData(String userId, long loginTime, int requestCount) {
              this.userId = userId;
              this.loginTime = loginTime;
              this.requestCount = requestCount;
          }
          
          SessionData incrementRequests() {
              return new SessionData(userId, loginTime, requestCount + 1);
          }
      }
      
      private final AtomicReference<SessionData> sessionData;
      
      public UserSession(String userId) {
          this.sessionData = new AtomicReference<>(
              new SessionData(userId, System.currentTimeMillis(), 0)
          );
      }
      
      public void recordRequest() {
          sessionData.updateAndGet(SessionData::incrementRequests);
      }
      
      public int getRequestCount() {
          return sessionData.get().requestCount;
      }
      
      public long getLoginTime() {
          return sessionData.get().loginTime;
      }
  }
  ```

  **5. AtomicReferenceArray**

  Thread-safe array of references.

  ```java
  import java.util.concurrent.atomic.AtomicReferenceArray;
  
  public class AtomicReferenceArrayExample {
      private final AtomicReferenceArray<String> array = new AtomicReferenceArray<>(10);
      
      public void set(int index, String value) {
          array.set(index, value);
      }
      
      public String get(int index) {
          return array.get(index);
      }
      
      public boolean compareAndSet(int index, String expected, String newValue) {
          return array.compareAndSet(index, expected, newValue);
      }
      
      public String getAndSet(int index, String newValue) {
          return array.getAndSet(index, newValue);
      }
  }
  ```

  **6. AtomicIntegerArray and AtomicLongArray**

  Thread-safe arrays of integers/longs.

  ```java
  import java.util.concurrent.atomic.AtomicIntegerArray;
  
  public class AtomicIntegerArrayExample {
      private final AtomicIntegerArray counters = new AtomicIntegerArray(10);
      
      public void increment(int index) {
          counters.incrementAndGet(index);
      }
      
      public void add(int index, int delta) {
          counters.addAndGet(index, delta);
      }
      
      public int get(int index) {
          return counters.get(index);
      }
      
      public int getTotalCount() {
          int total = 0;
          for (int i = 0; i < counters.length(); i++) {
              total += counters.get(i);
          }
          return total;
      }
  }
  ```

  **7. AtomicStampedReference**

  AtomicReference with version stamp to solve ABA problem.

  **ABA Problem:**
  ```
  Thread 1: Read value A
  Thread 2: Change A to B
  Thread 3: Change B back to A
  Thread 1: CAS succeeds (thinks nothing changed, but it did!)
  ```

  **Solution with AtomicStampedReference:**

  ```java
  import java.util.concurrent.atomic.AtomicStampedReference;
  
  public class AtomicStampedReferenceExample {
      private final AtomicStampedReference<String> ref = 
          new AtomicStampedReference<>("Initial", 0);
      
      public void update(String newValue) {
          int[] stampHolder = new int[1];
          String currentValue = ref.get(stampHolder);
          int currentStamp = stampHolder[0];
          
          // Update with incremented stamp
          ref.compareAndSet(currentValue, newValue, currentStamp, currentStamp + 1);
      }
      
      public String getValue() {
          return ref.getReference();
      }
      
      public int getStamp() {
          int[] stampHolder = new int[1];
          ref.get(stampHolder);
          return stampHolder[0];
      }
      
      public static void main(String[] args) {
          AtomicStampedReferenceExample example = new AtomicStampedReferenceExample();
          
          int[] stampHolder = new int[1];
          String value = example.ref.get(stampHolder);
          int stamp = stampHolder[0];
          
          System.out.println("Value: " + value + ", Stamp: " + stamp);
          
          // Update
          example.update("New Value");
          
          value = example.ref.get(stampHolder);
          stamp = stampHolder[0];
          System.out.println("Value: " + value + ", Stamp: " + stamp);
      }
  }
  ```

  **8. AtomicMarkableReference**

  AtomicReference with boolean mark.

  ```java
  import java.util.concurrent.atomic.AtomicMarkableReference;
  
  public class AtomicMarkableReferenceExample {
      private final AtomicMarkableReference<String> ref = 
          new AtomicMarkableReference<>("Initial", false);
      
      public void markAsProcessed(String expectedValue) {
          ref.compareAndSet(expectedValue, expectedValue, false, true);
      }
      
      public boolean isMarked() {
          return ref.isMarked();
      }
      
      public String getValue() {
          return ref.getReference();
      }
  }
  ```

  **9. LongAdder and DoubleAdder (Java 8+)**

  Better performance than AtomicLong for high-contention scenarios.

  ```java
  import java.util.concurrent.atomic.LongAdder;
  
  public class LongAdderExample {
      private final LongAdder counter = new LongAdder();
      
      public void increment() {
          counter.increment();  // Faster than AtomicLong in high contention
      }
      
      public void add(long value) {
          counter.add(value);
      }
      
      public long getCount() {
          return counter.sum();  // Note: sum() is not atomic
      }
      
      public void reset() {
          counter.reset();
      }
      
      public long sumThenReset() {
          return counter.sumThenReset();
      }
  }
  ```

  **LongAdder vs AtomicLong:**

  ```java
  public class PerformanceComparison {
      public static void main(String[] args) throws InterruptedException {
          int numThreads = 100;
          int iterations = 1000000;
          
          // Test AtomicLong
          AtomicLong atomicCounter = new AtomicLong(0);
          long start = System.currentTimeMillis();
          
          Thread[] threads1 = new Thread[numThreads];
          for (int i = 0; i < numThreads; i++) {
              threads1[i] = new Thread(() -> {
                  for (int j = 0; j < iterations; j++) {
                      atomicCounter.incrementAndGet();
                  }
              });
              threads1[i].start();
          }
          
          for (Thread thread : threads1) {
              thread.join();
          }
          
          long atomicTime = System.currentTimeMillis() - start;
          System.out.println("AtomicLong time: " + atomicTime + "ms");
          
          // Test LongAdder
          LongAdder adderCounter = new LongAdder();
          start = System.currentTimeMillis();
          
          Thread[] threads2 = new Thread[numThreads];
          for (int i = 0; i < numThreads; i++) {
              threads2[i] = new Thread(() -> {
                  for (int j = 0; j < iterations; j++) {
                      adderCounter.increment();
                  }
              });
              threads2[i].start();
          }
          
          for (Thread thread : threads2) {
              thread.join();
          }
          
          long adderTime = System.currentTimeMillis() - start;
          System.out.println("LongAdder time: " + adderTime + "ms");
          System.out.println("LongAdder is " + (atomicTime / (double) adderTime) + "x faster");
      }
  }
  ```

  **10. LongAccumulator and DoubleAccumulator**

  Generalized version of LongAdder with custom accumulation function.

  ```java
  import java.util.concurrent.atomic.LongAccumulator;
  
  public class LongAccumulatorExample {
      // Sum accumulator (like LongAdder)
      private final LongAccumulator sum = new LongAccumulator(Long::sum, 0);
      
      // Max accumulator
      private final LongAccumulator max = new LongAccumulator(Long::max, Long.MIN_VALUE);
      
      // Min accumulator
      private final LongAccumulator min = new LongAccumulator(Long::min, Long.MAX_VALUE);
      
      public void accumulate(long value) {
          sum.accumulate(value);
          max.accumulate(value);
          min.accumulate(value);
      }
      
      public long getSum() {
          return sum.get();
      }
      
      public long getMax() {
          return max.get();
      }
      
      public long getMin() {
          return min.get();
      }
      
      public void reset() {
          sum.reset();
          max.reset();
          min.reset();
      }
  }
  ```

  **Atomic Classes Comparison:**

  | Class | Use Case | Performance | Notes |
  |-------|----------|-------------|-------|
  | AtomicInteger | Counter, flags | Good | General purpose |
  | AtomicLong | Large counters | Good | General purpose |
  | AtomicBoolean | Flags, one-time init | Excellent | Simple operations |
  | AtomicReference | Object references | Good | Immutable updates |
  | LongAdder | High-contention counter | Excellent | sum() not atomic |
  | LongAccumulator | Custom accumulation | Excellent | Flexible |
  | AtomicStampedReference | ABA problem | Good | Version tracking |
  | AtomicMarkableReference | Mark/unmark | Good | Boolean flag |

  **When to Use:**

  + **AtomicInteger/Long:** General-purpose counters, low-to-medium contention
  + **LongAdder:** High-contention counters (many threads incrementing)
  + **AtomicBoolean:** Flags, one-time initialization
  + **AtomicReference:** Immutable object updates, state transitions
  + **LongAccumulator:** Custom accumulation logic (max, min, product)
  + **AtomicStampedReference:** When ABA problem is a concern

  **Best Practices:**

  ```java
  public class AtomicBestPractices {
      // ✅ Use AtomicInteger for simple counters
      private final AtomicInteger counter = new AtomicInteger(0);
      
      // ✅ Use LongAdder for high-contention counters
      private final LongAdder highContentionCounter = new LongAdder();
      
      // ✅ Use AtomicReference for immutable updates
      private final AtomicReference<ImmutableState> state = 
          new AtomicReference<>(new ImmutableState());
      
      // ✅ Use compareAndSet for conditional updates
      public void conditionalUpdate() {
          int current = counter.get();
          int newValue = current + 1;
          while (!counter.compareAndSet(current, newValue)) {
              current = counter.get();
              newValue = current + 1;
          }
      }
      
      // ✅ Use updateAndGet for functional updates
      public void functionalUpdate() {
          counter.updateAndGet(v -> v * 2);
      }
      
      // ❌ Don't use AtomicInteger for complex state
      private final AtomicInteger complexState = new AtomicInteger(0);  // Bad
      
      // ✅ Use AtomicReference with immutable object instead
      private final AtomicReference<ComplexState> betterState = 
          new AtomicReference<>(new ComplexState());
      
      private static class ImmutableState {
          // Immutable fields
      }
      
      private static class ComplexState {
          // Complex state fields
      }
  }
  ```
  
</details>


## Fork/Join Framework and Common Issues

<details>
  <summary>Fork/Join Framework: Parallel Processing</summary>
  <br/>

  The Fork/Join Framework is designed for parallel processing of tasks that can be broken down into smaller subtasks (divide-and-conquer).

  **Key Concepts:**

  + **Fork:** Split a task into smaller subtasks
  + **Join:** Wait for subtasks to complete and combine results
  + **Work-Stealing:** Idle threads steal work from busy threads' queues
  + **ForkJoinPool:** Special thread pool for fork/join tasks

  **How It Works:**

  ```
  Main Task
     |
     Fork
     |
  +--+--+
  |     |
  Sub1  Sub2
  |     |
  Fork  Fork
  |     |
  +-+   +-+
  | |   | |
  A B   C D
  | |   | |
  Join  Join
  |     |
  Join  |
     |  |
     +--+
     |
   Result
  ```

  **1. RecursiveTask<V> - Returns a Result**

  ```java
  import java.util.concurrent.*;
  
  public class SumTask extends RecursiveTask<Long> {
      private static final int THRESHOLD = 10_000;
      private final long[] array;
      private final int start;
      private final int end;
      
      public SumTask(long[] array, int start, int end) {
          this.array = array;
          this.start = start;
          this.end = end;
      }
      
      @Override
      protected Long compute() {
          int length = end - start;
          
          // Base case: compute directly if small enough
          if (length <= THRESHOLD) {
              return computeDirectly();
          }
          
          // Recursive case: split into subtasks
          int mid = start + length / 2;
          
          // Fork left subtask
          SumTask leftTask = new SumTask(array, start, mid);
          leftTask.fork();  // Execute asynchronously
          
          // Compute right subtask in current thread
          SumTask rightTask = new SumTask(array, mid, end);
          Long rightResult = rightTask.compute();
          
          // Join left subtask (wait for result)
          Long leftResult = leftTask.join();
          
          // Combine results
          return leftResult + rightResult;
      }
      
      private Long computeDirectly() {
          long sum = 0;
          for (int i = start; i < end; i++) {
              sum += array[i];
          }
          return sum;
      }
      
      public static void main(String[] args) {
          // Create array
          long[] array = new long[1_000_000];
          for (int i = 0; i < array.length; i++) {
              array[i] = i + 1;
          }
          
          // Create ForkJoinPool
          ForkJoinPool pool = new ForkJoinPool();
          
          // Execute task
          SumTask task = new SumTask(array, 0, array.length);
          long result = pool.invoke(task);
          
          System.out.println("Sum: " + result);
          
          pool.shutdown();
      }
  }
  ```

  **2. RecursiveAction - No Return Value**

  ```java
  import java.util.concurrent.*;
  
  public class ArrayIncrementTask extends RecursiveAction {
      private static final int THRESHOLD = 10_000;
      private final int[] array;
      private final int start;
      private final int end;
      
      public ArrayIncrementTask(int[] array, int start, int end) {
          this.array = array;
          this.start = start;
          this.end = end;
      }
      
      @Override
      protected void compute() {
          int length = end - start;
          
          if (length <= THRESHOLD) {
              // Base case: process directly
              for (int i = start; i < end; i++) {
                  array[i]++;
              }
          } else {
              // Recursive case: split
              int mid = start + length / 2;
              
              ArrayIncrementTask leftTask = new ArrayIncrementTask(array, start, mid);
              ArrayIncrementTask rightTask = new ArrayIncrementTask(array, mid, end);
              
              // Fork both subtasks
              invokeAll(leftTask, rightTask);  // Fork and join both
          }
      }
      
      public static void main(String[] args) {
          int[] array = new int[1_000_000];
          
          ForkJoinPool pool = new ForkJoinPool();
          ArrayIncrementTask task = new ArrayIncrementTask(array, 0, array.length);
          pool.invoke(task);
          
          System.out.println("First element: " + array[0]);
          System.out.println("Last element: " + array[array.length - 1]);
          
          pool.shutdown();
      }
  }
  ```

  **3. ForkJoinPool**

  ```java
  // Use common pool (recommended)
  ForkJoinPool commonPool = ForkJoinPool.commonPool();
  
  // Create custom pool
  ForkJoinPool customPool = new ForkJoinPool(4);  // 4 threads
  
  // Execute task
  Long result = pool.invoke(task);  // Blocks until complete
  
  // Submit task (non-blocking)
  ForkJoinTask<Long> future = pool.submit(task);
  Long result2 = future.get();
  
  // Execute task asynchronously
  pool.execute(task);
  
  // Shutdown
  pool.shutdown();
  ```

  **ForkJoinPool Deep Dive:**

  **What is ForkJoinPool?**

  ForkJoinPool is a specialized ExecutorService designed for work-stealing algorithms. Unlike traditional thread pools where each thread has its own queue, ForkJoinPool allows idle threads to "steal" work from busy threads, maximizing CPU utilization.

  **Internal Architecture:**

  ```
  ForkJoinPool
  ├── Worker Thread 1
  │   └── Deque (Double-ended queue)
  │       ├── Task A (head - for stealing)
  │       ├── Task B
  │       └── Task C (tail - for own work)
  │
  ├── Worker Thread 2
  │   └── Deque
  │       ├── Task D
  │       └── Task E
  │
  └── Worker Thread 3 (idle)
      └── Deque (empty)
          └── Steals Task A from Thread 1
  ```

  **How Work-Stealing Works:**

  1. **Each worker thread has its own deque (double-ended queue)**
     - Thread pushes new tasks to the tail (LIFO - Last In First Out)
     - Thread pops tasks from the tail for execution
     - Other threads steal from the head (FIFO - First In First Out)

  2. **When a thread becomes idle:**
     - It randomly selects another thread's queue
     - Steals a task from the head of that queue
     - Continues stealing until it finds work or all queues are empty

  3. **Why this is efficient:**
     - Reduces contention (threads work on opposite ends of deque)
     - Balances load automatically
     - Minimizes thread idle time
     - Cache-friendly (threads work on their own tasks first)

  **Detailed Example of Work-Stealing:**

  ```java
  public class WorkStealingDemo {
      public static void main(String[] args) {
          // Create pool with 4 threads
          ForkJoinPool pool = new ForkJoinPool(4);
          
          // Submit a large task that will be split
          BigTask task = new BigTask(1000);
          pool.invoke(task);
          
          /*
           * What happens internally:
           * 
           * 1. Thread-1 receives BigTask(1000)
           *    - Splits into BigTask(500) and BigTask(500)
           *    - Forks BigTask(500) to its queue
           *    - Computes BigTask(500) itself
           * 
           * 2. Thread-1's queue: [BigTask(500)]
           *    - Thread-1 is busy computing
           * 
           * 3. Thread-2 is idle
           *    - Steals BigTask(500) from Thread-1's queue
           *    - Splits into BigTask(250) and BigTask(250)
           * 
           * 4. Thread-2's queue: [BigTask(250)]
           *    - Thread-2 computes BigTask(250)
           * 
           * 5. Thread-3 is idle
           *    - Steals BigTask(250) from Thread-2's queue
           *    - Continues splitting and processing
           * 
           * Result: All 4 threads are working, load is balanced
           */
      }
      
      static class BigTask extends RecursiveAction {
          private final int size;
          
          BigTask(int size) {
              this.size = size;
          }
          
          @Override
          protected void compute() {
              if (size < 10) {
                  // Do actual work
                  System.out.println(Thread.currentThread().getName() + " processing " + size);
              } else {
                  // Split
                  BigTask left = new BigTask(size / 2);
                  BigTask right = new BigTask(size / 2);
                  invokeAll(left, right);
              }
          }
      }
  }
  ```

  **ForkJoinPool Configuration:**

  ```java
  // 1. Common Pool (Recommended for most cases)
  ForkJoinPool commonPool = ForkJoinPool.commonPool();
  // - Shared across entire application
  // - Parallelism = Runtime.getRuntime().availableProcessors() - 1
  // - Lazy initialization (created on first use)
  // - Cannot be shut down
  
  System.out.println("Common pool parallelism: " + commonPool.getParallelism());
  System.out.println("Common pool size: " + commonPool.getPoolSize());
  
  // 2. Custom Pool with specific parallelism
  ForkJoinPool customPool = new ForkJoinPool(8);  // 8 worker threads
  
  // 3. Custom Pool with all options
  ForkJoinPool advancedPool = new ForkJoinPool(
      4,                              // parallelism (number of worker threads)
      ForkJoinPool.defaultForkJoinWorkerThreadFactory,  // thread factory
      null,                           // uncaught exception handler
      true                            // asyncMode (FIFO vs LIFO)
  );
  
  // 4. Async Mode (FIFO scheduling)
  ForkJoinPool asyncPool = new ForkJoinPool(
      4,
      ForkJoinPool.defaultForkJoinWorkerThreadFactory,
      null,
      true  // asyncMode = true (FIFO for event-style tasks)
  );
  ```

  **Parallelism vs Pool Size:**

  ```java
  ForkJoinPool pool = new ForkJoinPool(4);
  
  // Parallelism: Target number of active threads
  int parallelism = pool.getParallelism();  // 4
  
  // Pool Size: Actual number of worker threads (may be less than parallelism)
  int poolSize = pool.getPoolSize();  // May be 0-4 depending on workload
  
  // Active Thread Count: Threads currently executing tasks
  int activeCount = pool.getActiveThreadCount();
  
  // Running Thread Count: Threads not blocked
  int runningCount = pool.getRunningThreadCount();
  
  // Queued Task Count: Tasks waiting in queues
  long queuedTasks = pool.getQueuedTaskCount();
  
  // Queued Submission Count: Tasks submitted but not yet executed
  int queuedSubmissions = pool.getQueuedSubmissionCount();
  
  System.out.println("Parallelism: " + parallelism);
  System.out.println("Pool Size: " + poolSize);
  System.out.println("Active Threads: " + activeCount);
  System.out.println("Running Threads: " + runningCount);
  System.out.println("Queued Tasks: " + queuedTasks);
  System.out.println("Queued Submissions: " + queuedSubmissions);
  ```

  **Understanding Async Mode:**

  ```java
  // LIFO Mode (default, asyncMode = false)
  // - Good for recursive divide-and-conquer tasks
  // - Tasks pushed to tail, popped from tail (LIFO)
  // - Better cache locality
  ForkJoinPool lifoPool = new ForkJoinPool(4, 
      ForkJoinPool.defaultForkJoinWorkerThreadFactory, null, false);
  
  // FIFO Mode (asyncMode = true)
  // - Good for event-driven tasks
  // - Tasks pushed to tail, popped from head (FIFO)
  // - Better for tasks that don't spawn subtasks
  ForkJoinPool fifoPool = new ForkJoinPool(4,
      ForkJoinPool.defaultForkJoinWorkerThreadFactory, null, true);
  
  /*
   * LIFO Example (Recursive tasks):
   * Thread's queue: [Task1, Task2, Task3]
   *                  head           tail
   * Thread pops from tail: Task3 (most recent)
   * Other threads steal from head: Task1 (oldest)
   * 
   * FIFO Example (Event tasks):
   * Thread's queue: [Task1, Task2, Task3]
   *                  head           tail
   * Thread pops from head: Task1 (oldest)
   * Other threads steal from head: Task1 (same end)
   */
  ```

  **Common Pool vs Custom Pool:**

  ```java
  public class CommonVsCustomPool {
      public static void main(String[] args) {
          // Common Pool
          ForkJoinPool commonPool = ForkJoinPool.commonPool();
          System.out.println("Common Pool Parallelism: " + commonPool.getParallelism());
          // Typically: CPU cores - 1
          // On 8-core machine: 7
          
          /*
           * Advantages of Common Pool:
           * - Shared resource (efficient memory usage)
           * - Automatically sized based on CPU cores
           * - No need to manage lifecycle
           * - Used by parallel streams by default
           * 
           * Disadvantages:
           * - Shared by entire application
           * - Cannot customize parallelism
           * - Cannot shut down
           * - May be affected by other code using it
           */
          
          // Custom Pool
          ForkJoinPool customPool = new ForkJoinPool(16);
          System.out.println("Custom Pool Parallelism: " + customPool.getParallelism());
          
          /*
           * Advantages of Custom Pool:
           * - Isolated from other code
           * - Customizable parallelism
           * - Can be shut down
           * - Can configure async mode
           * 
           * Disadvantages:
           * - Need to manage lifecycle
           * - Additional memory overhead
           * - Need to explicitly pass to tasks
           */
          
          // When to use Custom Pool:
          // 1. Need different parallelism than CPU cores
          // 2. Want isolation from other code
          // 3. Need to control lifecycle
          // 4. Running blocking operations (use more threads)
          
          customPool.shutdown();
      }
  }
  ```

  **Key Differences: ForkJoinPool vs ThreadPoolExecutor:**

  | Feature | ForkJoinPool | ThreadPoolExecutor |
  |---------|--------------|-------------------|
  | **Queue per thread** | Yes (deque) | No (shared queue) |
  | **Work-stealing** | Yes | No |
  | **Best for** | Recursive tasks | Independent tasks |
  | **Task type** | ForkJoinTask | Runnable/Callable |
  | **Scheduling** | LIFO (default) | FIFO |
  | **Load balancing** | Automatic (stealing) | Manual (queue) |
  | **Cache locality** | Better | Worse |
  | **Overhead** | Lower for recursive | Lower for simple |

  **Real-World Example: Parallel Merge Sort**

  ```java
  public class ParallelMergeSort extends RecursiveAction {
      private static final int THRESHOLD = 1000;
      private final int[] array;
      private final int start;
      private final int end;
      
      public ParallelMergeSort(int[] array, int start, int end) {
          this.array = array;
          this.start = start;
          this.end = end;
      }
      
      @Override
      protected void compute() {
          int length = end - start;
          
          if (length <= THRESHOLD) {
              // Base case: use sequential sort
              Arrays.sort(array, start, end);
          } else {
              // Recursive case: split and merge
              int mid = start + length / 2;
              
              ParallelMergeSort leftTask = new ParallelMergeSort(array, start, mid);
              ParallelMergeSort rightTask = new ParallelMergeSort(array, mid, end);
              
              // Fork both tasks
              invokeAll(leftTask, rightTask);
              
              // Merge sorted halves
              merge(start, mid, end);
          }
      }
      
      private void merge(int start, int mid, int end) {
          int[] temp = new int[end - start];
          int i = start, j = mid, k = 0;
          
          while (i < mid && j < end) {
              if (array[i] <= array[j]) {
                  temp[k++] = array[i++];
              } else {
                  temp[k++] = array[j++];
              }
          }
          
          while (i < mid) temp[k++] = array[i++];
          while (j < end) temp[k++] = array[j++];
          
          System.arraycopy(temp, 0, array, start, temp.length);
      }
      
      public static void main(String[] args) {
          int[] array = new int[1_000_000];
          Random random = new Random();
          for (int i = 0; i < array.length; i++) {
              array[i] = random.nextInt(1000);
          }
          
          long start = System.currentTimeMillis();
          
          ForkJoinPool pool = new ForkJoinPool();
          ParallelMergeSort task = new ParallelMergeSort(array, 0, array.length);
          pool.invoke(task);
          
          long end = System.currentTimeMillis();
          System.out.println("Time: " + (end - start) + "ms");
          System.out.println("Sorted: " + isSorted(array));
          
          pool.shutdown();
      }
      
      private static boolean isSorted(int[] array) {
          for (int i = 1; i < array.length; i++) {
              if (array[i] < array[i - 1]) return false;
          }
          return true;
      }
  }
  ```

  **Real-World Example: Parallel File Search**

  ```java
  public class FileSearchTask extends RecursiveTask<List<File>> {
      private final File directory;
      private final String searchTerm;
      
      public FileSearchTask(File directory, String searchTerm) {
          this.directory = directory;
          this.searchTerm = searchTerm;
      }
      
      @Override
      protected List<File> compute() {
          List<File> results = new ArrayList<>();
          File[] files = directory.listFiles();
          
          if (files == null) return results;
          
          List<FileSearchTask> subtasks = new ArrayList<>();
          
          for (File file : files) {
              if (file.isDirectory()) {
                  // Fork subtask for subdirectory
                  FileSearchTask subtask = new FileSearchTask(file, searchTerm);
                  subtask.fork();
                  subtasks.add(subtask);
              } else if (file.getName().contains(searchTerm)) {
                  // Found matching file
                  results.add(file);
              }
          }
          
          // Join all subtasks
          for (FileSearchTask subtask : subtasks) {
              results.addAll(subtask.join());
          }
          
          return results;
      }
      
      public static void main(String[] args) {
          File rootDir = new File("/path/to/search");
          String searchTerm = ".txt";
          
          ForkJoinPool pool = new ForkJoinPool();
          FileSearchTask task = new FileSearchTask(rootDir, searchTerm);
          List<File> results = pool.invoke(task);
          
          System.out.println("Found " + results.size() + " files:");
          results.forEach(file -> System.out.println(file.getAbsolutePath()));
          
          pool.shutdown();
      }
  }
  ```

  **Real-World Example: Parallel Image Processing**

  ```java
  public class ImageBlurTask extends RecursiveAction {
      private static final int THRESHOLD = 10000;
      private final int[] pixels;
      private final int start;
      private final int end;
      private final int width;
      
      public ImageBlurTask(int[] pixels, int start, int end, int width) {
          this.pixels = pixels;
          this.start = start;
          this.end = end;
          this.width = width;
      }
      
      @Override
      protected void compute() {
          int length = end - start;
          
          if (length <= THRESHOLD) {
              // Base case: process pixels directly
              for (int i = start; i < end; i++) {
                  pixels[i] = blur(i);
              }
          } else {
              // Recursive case: split
              int mid = start + length / 2;
              
              ImageBlurTask leftTask = new ImageBlurTask(pixels, start, mid, width);
              ImageBlurTask rightTask = new ImageBlurTask(pixels, mid, end, width);
              
              invokeAll(leftTask, rightTask);
          }
      }
      
      private int blur(int index) {
          // Simple blur: average with neighbors
          // Implementation details omitted
          return pixels[index];
      }
  }
  ```

  **Work-Stealing Algorithm:**

  ```
  Thread 1 Queue: [Task1, Task2, Task3, Task4]
  Thread 2 Queue: [Task5, Task6]
  Thread 3 Queue: []  (idle)
  
  Thread 3 steals Task4 from Thread 1's queue
  
  Thread 1 Queue: [Task1, Task2, Task3]
  Thread 2 Queue: [Task5, Task6]
  Thread 3 Queue: [Task4]  (now working)
  ```

  **Best Practices:**

  ```java
  public class ForkJoinBestPractices {
      // ✅ Use common pool for most cases
      public void useCommonPool() {
          ForkJoinPool pool = ForkJoinPool.commonPool();
          MyTask task = new MyTask();
          pool.invoke(task);
      }
      
      // ✅ Choose appropriate threshold
      private static final int THRESHOLD = 10_000;  // Tune based on task
      
      // ✅ Fork left, compute right pattern
      public class GoodTask extends RecursiveTask<Long> {
          @Override
          protected Long compute() {
              if (shouldSplit()) {
                  GoodTask left = new GoodTask();
                  left.fork();  // Fork left
                  
                  GoodTask right = new GoodTask();
                  Long rightResult = right.compute();  // Compute right
                  
                  Long leftResult = left.join();  // Join left
                  return leftResult + rightResult;
              }
              return computeDirectly();
          }
          
          private boolean shouldSplit() { return true; }
          private Long computeDirectly() { return 0L; }
      }
      
      // ✅ Use invokeAll for multiple subtasks
      public class MultipleSubtasks extends RecursiveAction {
          @Override
          protected void compute() {
              RecursiveAction task1 = new MyAction();
              RecursiveAction task2 = new MyAction();
              RecursiveAction task3 = new MyAction();
              
              invokeAll(task1, task2, task3);  // Fork and join all
          }
      }
      
      // ❌ Don't fork too many small tasks
      public class BadTask extends RecursiveTask<Long> {
          @Override
          protected Long compute() {
              if (size == 1) {  // Too small!
                  BadTask left = new BadTask();
                  left.fork();
                  // ...
              }
              return 0L;
          }
      }
      
      // ❌ Don't use blocking operations in tasks
      public class BlockingTask extends RecursiveTask<Long> {
          @Override
          protected Long compute() {
              try {
                  Thread.sleep(1000);  // Bad! Blocks worker thread
              } catch (InterruptedException e) {
                  e.printStackTrace();
              }
              return 0L;
          }
      }
      
      private static class MyTask extends RecursiveAction {
          @Override
          protected void compute() {}
      }
      
      private static class MyAction extends RecursiveAction {
          @Override
          protected void compute() {}
      }
  }
  ```

  **Performance Comparison:**

  ```java
  public class PerformanceComparison {
      public static void main(String[] args) {
          long[] array = new long[10_000_000];
          for (int i = 0; i < array.length; i++) {
              array[i] = i + 1;
          }
          
          // Sequential
          long start = System.currentTimeMillis();
          long sum = 0;
          for (long value : array) {
              sum += value;
          }
          long sequentialTime = System.currentTimeMillis() - start;
          System.out.println("Sequential: " + sequentialTime + "ms, Sum: " + sum);
          
          // Parallel Stream
          start = System.currentTimeMillis();
          long parallelSum = Arrays.stream(array).parallel().sum();
          long parallelStreamTime = System.currentTimeMillis() - start;
          System.out.println("Parallel Stream: " + parallelStreamTime + "ms, Sum: " + parallelSum);
          
          // Fork/Join
          start = System.currentTimeMillis();
          ForkJoinPool pool = new ForkJoinPool();
          SumTask task = new SumTask(array, 0, array.length);
          long forkJoinSum = pool.invoke(task);
          long forkJoinTime = System.currentTimeMillis() - start;
          System.out.println("Fork/Join: " + forkJoinTime + "ms, Sum: " + forkJoinSum);
          
          pool.shutdown();
      }
      
      static class SumTask extends RecursiveTask<Long> {
          private static final int THRESHOLD = 10_000;
          private final long[] array;
          private final int start, end;
          
          SumTask(long[] array, int start, int end) {
              this.array = array;
              this.start = start;
              this.end = end;
          }
          
          @Override
          protected Long compute() {
              if (end - start <= THRESHOLD) {
                  long sum = 0;
                  for (int i = start; i < end; i++) {
                      sum += array[i];
                  }
                  return sum;
              }
              int mid = start + (end - start) / 2;
              SumTask left = new SumTask(array, start, mid);
              left.fork();
              SumTask right = new SumTask(array, mid, end);
              return right.compute() + left.join();
          }
      }
  }
  ```

  **When to Use Fork/Join:**

  + ✅ Recursive divide-and-conquer algorithms
  + ✅ Large data sets that can be split
  + ✅ CPU-intensive tasks
  + ✅ Tasks with similar execution time
  + ❌ I/O-bound tasks
  + ❌ Tasks with blocking operations
  + ❌ Small data sets (overhead not worth it)
  + ❌ Tasks with very different execution times

  **Fork/Join vs Other Approaches:**

  | Approach | Use Case | Complexity | Performance |
  |----------|----------|------------|-------------|
  | Sequential | Small data, simple logic | Low | Baseline |
  | ExecutorService | Independent tasks | Medium | Good |
  | Parallel Streams | Collection processing | Low | Good |
  | Fork/Join | Recursive divide-and-conquer | High | Excellent |

  **Key Points:**

  + Fork/Join is designed for recursive parallel tasks
  + Work-stealing ensures efficient CPU utilization
  + Choose appropriate threshold to balance parallelism and overhead
  + Use common pool unless you need custom configuration
  + Avoid blocking operations in tasks
  + Best for CPU-intensive divide-and-conquer algorithms
  
</details>

<details>
  <summary>Common Concurrency Issues: Deadlock, Livelock, Race Conditions, Starvation</summary>
  <br/>

  Understanding and preventing common concurrency issues is crucial for building robust multithreaded applications.

  **1. Deadlock**

  Two or more threads are blocked forever, each waiting for the other to release a resource.

  **Deadlock Example:**

  ```java
  public class DeadlockExample {
      private final Object lock1 = new Object();
      private final Object lock2 = new Object();
      
      public void method1() {
          synchronized (lock1) {
              System.out.println("Thread 1: Holding lock1...");
              
              try { Thread.sleep(100); } catch (InterruptedException e) {}
              
              System.out.println("Thread 1: Waiting for lock2...");
              synchronized (lock2) {
                  System.out.println("Thread 1: Holding lock1 & lock2");
              }
          }
      }
      
      public void method2() {
          synchronized (lock2) {
              System.out.println("Thread 2: Holding lock2...");
              
              try { Thread.sleep(100); } catch (InterruptedException e) {}
              
              System.out.println("Thread 2: Waiting for lock1...");
              synchronized (lock1) {
                  System.out.println("Thread 2: Holding lock1 & lock2");
              }
          }
      }
      
      public static void main(String[] args) {
          DeadlockExample example = new DeadlockExample();
          
          Thread t1 = new Thread(() -> example.method1());
          Thread t2 = new Thread(() -> example.method2());
          
          t1.start();
          t2.start();
          
          // DEADLOCK! Both threads wait forever
      }
  }
  ```

  **Deadlock Conditions (All 4 must be present):**

  1. **Mutual Exclusion:** Resources cannot be shared
  2. **Hold and Wait:** Thread holds resource while waiting for another
  3. **No Preemption:** Resources cannot be forcibly taken
  4. **Circular Wait:** Circular chain of threads waiting for resources

  **Deadlock Prevention:**

  **Solution 1: Lock Ordering**

  ```java
  public class DeadlockFixed {
      private final Object lock1 = new Object();
      private final Object lock2 = new Object();
      
      public void method1() {
          synchronized (lock1) {  // Always acquire lock1 first
              synchronized (lock2) {
                  System.out.println("Thread 1: Holding both locks");
              }
          }
      }
      
      public void method2() {
          synchronized (lock1) {  // Same order! lock1 then lock2
              synchronized (lock2) {
                  System.out.println("Thread 2: Holding both locks");
              }
          }
      }
  }
  ```

  **Solution 2: tryLock with Timeout**

  ```java
  import java.util.concurrent.locks.Lock;
  import java.util.concurrent.locks.ReentrantLock;
  import java.util.concurrent.TimeUnit;
  
  public class DeadlockAvoidance {
      private final Lock lock1 = new ReentrantLock();
      private final Lock lock2 = new ReentrantLock();
      
      public void method1() {
          try {
              if (lock1.tryLock(1, TimeUnit.SECONDS)) {
                  try {
                      if (lock2.tryLock(1, TimeUnit.SECONDS)) {
                          try {
                              System.out.println("Thread 1: Got both locks");
                          } finally {
                              lock2.unlock();
                          }
                      } else {
                          System.out.println("Thread 1: Couldn't get lock2, releasing lock1");
                      }
                  } finally {
                      lock1.unlock();
                  }
              }
          } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
          }
      }
  }
  ```

  **Solution 3: Lock-Free Algorithms**

  ```java
  import java.util.concurrent.atomic.AtomicInteger;
  
  public class LockFreeCounter {
      private final AtomicInteger count = new AtomicInteger(0);
      
      public void increment() {
          count.incrementAndGet();  // No locks, no deadlock
      }
  }
  ```

  **Deadlock Detection:**

  ```java
  import java.lang.management.*;
  
  public class DeadlockDetector {
      public static void detectDeadlock() {
          ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
          long[] deadlockedThreads = threadMXBean.findDeadlockedThreads();
          
          if (deadlockedThreads != null) {
              System.out.println("Deadlock detected!");
              ThreadInfo[] threadInfos = threadMXBean.getThreadInfo(deadlockedThreads);
              
              for (ThreadInfo threadInfo : threadInfos) {
                  System.out.println("Thread: " + threadInfo.getThreadName());
                  System.out.println("State: " + threadInfo.getThreadState());
                  System.out.println("Locked on: " + threadInfo.getLockName());
                  System.out.println("Locked by: " + threadInfo.getLockOwnerName());
                  System.out.println();
              }
          } else {
              System.out.println("No deadlock detected");
          }
      }
      
      public static void main(String[] args) throws InterruptedException {
          // Start threads that may deadlock
          // ...
          
          // Check for deadlock periodically
          Thread.sleep(5000);
          detectDeadlock();
      }
  }
  ```

  **2. Livelock**

  Threads are not blocked but keep changing state in response to each other without making progress.

  **Livelock Example:**

  ```java
  public class LivelockExample {
      static class Spoon {
          private Diner owner;
          
          public Spoon(Diner owner) {
              this.owner = owner;
          }
          
          public Diner getOwner() {
              return owner;
          }
          
          public synchronized void setOwner(Diner owner) {
              this.owner = owner;
          }
          
          public synchronized void use() {
              System.out.println(owner.name + " is eating");
          }
      }
      
      static class Diner {
          private String name;
          private boolean isHungry;
          
          public Diner(String name) {
              this.name = name;
              this.isHungry = true;
          }
          
          public void eatWith(Spoon spoon, Diner spouse) {
              while (isHungry) {
                  // If spouse is hungry, give them the spoon
                  if (spoon.getOwner() != this) {
                      try {
                          Thread.sleep(1);
                      } catch (InterruptedException e) {
                          e.printStackTrace();
                      }
                      continue;
                  }
                  
                  // If spouse is hungry, be polite and give them the spoon
                  if (spouse.isHungry) {
                      System.out.println(name + ": You eat first, " + spouse.name);
                      spoon.setOwner(spouse);
                      continue;  // LIVELOCK! Both keep giving spoon to each other
                  }
                  
                  // Eat
                  spoon.use();
                  isHungry = false;
                  spoon.setOwner(spouse);
              }
          }
      }
      
      public static void main(String[] args) {
          Diner husband = new Diner("Husband");
          Diner wife = new Diner("Wife");
          Spoon spoon = new Spoon(husband);
          
          Thread t1 = new Thread(() -> husband.eatWith(spoon, wife));
          Thread t2 = new Thread(() -> wife.eatWith(spoon, husband));
          
          t1.start();
          t2.start();
          
          // LIVELOCK! Both keep giving spoon to each other
      }
  }
  ```

  **Livelock Solution:**

  ```java
  public class LivelockFixed {
      static class Diner {
          private String name;
          private boolean isHungry;
          
          public Diner(String name) {
              this.name = name;
              this.isHungry = true;
          }
          
          public void eatWith(Spoon spoon, Diner spouse) {
              while (isHungry) {
                  if (spoon.getOwner() != this) {
                      try {
                          Thread.sleep(1);
                      } catch (InterruptedException e) {
                          e.printStackTrace();
                      }
                      continue;
                  }
                  
                  // Solution: Add randomness or priority
                  Random random = new Random();
                  if (spouse.isHungry && random.nextBoolean()) {
                      System.out.println(name + ": You eat first, " + spouse.name);
                      spoon.setOwner(spouse);
                      continue;
                  }
                  
                  // Eat
                  spoon.use();
                  isHungry = false;
                  spoon.setOwner(spouse);
              }
          }
      }
  }
  ```

  **3. Race Condition**

  Multiple threads access shared data concurrently, and the outcome depends on the timing of their execution.

  **Race Condition Example:**

  ```java
  public class RaceConditionExample {
      private int count = 0;
      
      public void increment() {
          count++;  // NOT atomic! Read-Modify-Write
          // Step 1: Read count (e.g., 0)
          // Step 2: Add 1 (0 + 1 = 1)
          // Step 3: Write back (count = 1)
          // Another thread can interfere between steps!
      }
      
      public static void main(String[] args) throws InterruptedException {
          RaceConditionExample example = new RaceConditionExample();
          
          Thread[] threads = new Thread[100];
          for (int i = 0; i < 100; i++) {
              threads[i] = new Thread(() -> {
                  for (int j = 0; j < 1000; j++) {
                      example.increment();
                  }
              });
              threads[i].start();
          }
          
          for (Thread thread : threads) {
              thread.join();
          }
          
          System.out.println("Expected: 100000");
          System.out.println("Actual: " + example.count);  // Less than 100000!
      }
  }
  ```

  **Race Condition Solutions:**

  **Solution 1: Synchronized**

  ```java
  public class RaceConditionFixed1 {
      private int count = 0;
      
      public synchronized void increment() {
          count++;  // Now thread-safe
      }
  }
  ```

  **Solution 2: Atomic Variables**

  ```java
  import java.util.concurrent.atomic.AtomicInteger;
  
  public class RaceConditionFixed2 {
      private AtomicInteger count = new AtomicInteger(0);
      
      public void increment() {
          count.incrementAndGet();  // Atomic operation
      }
  }
  ```

  **Solution 3: Lock**

  ```java
  import java.util.concurrent.locks.Lock;
  import java.util.concurrent.locks.ReentrantLock;
  
  public class RaceConditionFixed3 {
      private int count = 0;
      private final Lock lock = new ReentrantLock();
      
      public void increment() {
          lock.lock();
          try {
              count++;
          } finally {
              lock.unlock();
          }
      }
  }
  ```

  **Complex Race Condition: Check-Then-Act**

  ```java
  public class CheckThenActRace {
      private Map<String, User> cache = new HashMap<>();
      
      // ❌ Race condition!
      public User getUser(String id) {
          if (!cache.containsKey(id)) {  // Check
              User user = loadFromDatabase(id);
              cache.put(id, user);  // Act
              return user;
          }
          return cache.get(id);
      }
      
      // ✅ Fixed with synchronization
      public synchronized User getUserFixed(String id) {
          if (!cache.containsKey(id)) {
              User user = loadFromDatabase(id);
              cache.put(id, user);
              return user;
          }
          return cache.get(id);
      }
      
      // ✅ Better: Use ConcurrentHashMap
      private ConcurrentHashMap<String, User> concurrentCache = new ConcurrentHashMap<>();
      
      public User getUserBetter(String id) {
          return concurrentCache.computeIfAbsent(id, this::loadFromDatabase);
      }
      
      private User loadFromDatabase(String id) {
          return new User(id);
      }
      
      static class User {
          String id;
          User(String id) { this.id = id; }
      }
  }
  ```

  **4. Starvation**

  A thread is perpetually denied access to resources it needs.

  **Starvation Example:**

  ```java
  public class StarvationExample {
      private final Object lock = new Object();
      
      public void highPriorityTask() {
          synchronized (lock) {
              // High priority thread keeps acquiring lock
              while (true) {
                  System.out.println("High priority working");
                  try {
                      Thread.sleep(1);
                  } catch (InterruptedException e) {
                      e.printStackTrace();
                  }
              }
          }
      }
      
      public void lowPriorityTask() {
          synchronized (lock) {
              // Low priority thread starves (never gets lock)
              System.out.println("Low priority working");
          }
      }
      
      public static void main(String[] args) {
          StarvationExample example = new StarvationExample();
          
          Thread highPriority = new Thread(() -> example.highPriorityTask());
          highPriority.setPriority(Thread.MAX_PRIORITY);
          
          Thread lowPriority = new Thread(() -> example.lowPriorityTask());
          lowPriority.setPriority(Thread.MIN_PRIORITY);
          
          highPriority.start();
          lowPriority.start();
          
          // Low priority thread may starve
      }
  }
  ```

  **Starvation Solutions:**

  **Solution 1: Fair Locks**

  ```java
  import java.util.concurrent.locks.ReentrantLock;
  
  public class StarvationFixed1 {
      private final ReentrantLock lock = new ReentrantLock(true);  // Fair lock
      
      public void task() {
          lock.lock();
          try {
              // Work
          } finally {
              lock.unlock();
          }
      }
  }
  ```

  **Solution 2: Avoid Thread Priorities**

  ```java
  public class StarvationFixed2 {
      // Don't rely on thread priorities
      // Use ExecutorService with fair queuing instead
      
      ExecutorService executor = Executors.newFixedThreadPool(10);
      
      public void submitTask(Runnable task) {
          executor.submit(task);  // Fair queuing
      }
  }
  ```

  **Solution 3: Bounded Waiting**

  ```java
  import java.util.concurrent.Semaphore;
  
  public class StarvationFixed3 {
      private final Semaphore semaphore = new Semaphore(1, true);  // Fair semaphore
      
      public void task() {
          try {
              semaphore.acquire();
              // Work
          } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
          } finally {
              semaphore.release();
          }
      }
  }
  ```

  **5. Priority Inversion**

  High-priority thread waits for low-priority thread to release a resource.

  **Priority Inversion Example:**

  ```java
  public class PriorityInversionExample {
      private final Object lock = new Object();
      
      public static void main(String[] args) {
          PriorityInversionExample example = new PriorityInversionExample();
          
          // Low priority thread acquires lock
          Thread lowPriority = new Thread(() -> {
              synchronized (example.lock) {
                  System.out.println("Low priority: Holding lock");
                  try {
                      Thread.sleep(5000);  // Hold lock for 5 seconds
                  } catch (InterruptedException e) {
                      e.printStackTrace();
                  }
              }
          });
          lowPriority.setPriority(Thread.MIN_PRIORITY);
          lowPriority.start();
          
          try { Thread.sleep(100); } catch (InterruptedException e) {}
          
          // High priority thread waits for lock
          Thread highPriority = new Thread(() -> {
              System.out.println("High priority: Waiting for lock");
              synchronized (example.lock) {
                  System.out.println("High priority: Got lock");
              }
          });
          highPriority.setPriority(Thread.MAX_PRIORITY);
          highPriority.start();
          
          // High priority thread is blocked by low priority thread!
      }
  }
  ```

  **Comparison Table:**

  | Issue | Description | Symptoms | Prevention |
  |-------|-------------|----------|------------|
  | **Deadlock** | Threads wait for each other forever | Application hangs | Lock ordering, tryLock |
  | **Livelock** | Threads keep changing state without progress | High CPU, no progress | Add randomness, priorities |
  | **Race Condition** | Outcome depends on timing | Incorrect results, data corruption | Synchronization, atomic operations |
  | **Starvation** | Thread never gets resources | Some threads never execute | Fair locks, avoid priorities |
  | **Priority Inversion** | High priority waits for low priority | Performance degradation | Priority inheritance |

  **Best Practices to Avoid Issues:**

  ```java
  public class ConcurrencyBestPractices {
      // ✅ 1. Minimize shared mutable state
      private final AtomicInteger counter = new AtomicInteger(0);  // Immutable reference
      
      // ✅ 2. Use concurrent collections
      private final ConcurrentHashMap<String, String> cache = new ConcurrentHashMap<>();
      
      // ✅ 3. Use higher-level concurrency utilities
      private final ExecutorService executor = Executors.newFixedThreadPool(10);
      
      // ✅ 4. Always acquire locks in same order
      private final Object lock1 = new Object();
      private final Object lock2 = new Object();
      
      public void method1() {
          synchronized (lock1) {
              synchronized (lock2) {
                  // Work
              }
          }
      }
      
      public void method2() {
          synchronized (lock1) {  // Same order!
              synchronized (lock2) {
                  // Work
              }
          }
      }
      
      // ✅ 5. Use tryLock with timeout
      private final Lock lock = new ReentrantLock();
      
      public void methodWithTimeout() {
          try {
              if (lock.tryLock(1, TimeUnit.SECONDS)) {
                  try {
                      // Work
                  } finally {
                      lock.unlock();
                  }
              }
          } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
          }
      }
      
      // ✅ 6. Keep synchronized blocks small
      public void minimizeLockScope() {
          // Non-critical work
          int result = expensiveCalculation();
          
          synchronized (lock1) {
              // Only critical section
              counter.addAndGet(result);
          }
          
          // More non-critical work
      }
      
      // ✅ 7. Use immutable objects when possible
      private final ImmutableConfig config = new ImmutableConfig();
      
      // ✅ 8. Document thread-safety guarantees
      /**
       * Thread-safe counter using AtomicInteger.
       * All methods are thread-safe and lock-free.
       */
      public void increment() {
          counter.incrementAndGet();
      }
      
      private int expensiveCalculation() {
          return 42;
      }
      
      private static class ImmutableConfig {
          // Immutable fields
      }
  }
  ```

  **Debugging Concurrency Issues:**

  ```java
  public class ConcurrencyDebugging {
      // Enable assertions
      static {
          ClassLoader.getSystemClassLoader().setDefaultAssertionStatus(true);
      }
      
      // Use thread-safe assertions
      private final AtomicInteger counter = new AtomicInteger(0);
      
      public void increment() {
          int oldValue = counter.get();
          counter.incrementAndGet();
          assert counter.get() == oldValue + 1 : "Counter increment failed";
      }
      
      // Log thread information
      public void logThreadInfo() {
          Thread current = Thread.currentThread();
          System.out.println("Thread: " + current.getName());
          System.out.println("State: " + current.getState());
          System.out.println("Priority: " + current.getPriority());
      }
      
      // Use thread dumps
      public static void printThreadDump() {
          Map<Thread, StackTraceElement[]> allThreads = Thread.getAllStackTraces();
          for (Map.Entry<Thread, StackTraceElement[]> entry : allThreads.entrySet()) {
              Thread thread = entry.getKey();
              StackTraceElement[] stackTrace = entry.getValue();
              
              System.out.println("Thread: " + thread.getName());
              System.out.println("State: " + thread.getState());
              for (StackTraceElement element : stackTrace) {
                  System.out.println("  " + element);
              }
              System.out.println();
          }
      }
  }
  ```

  **Key Takeaways:**

  + **Deadlock:** Prevent with lock ordering or tryLock
  + **Livelock:** Add randomness or priorities to break the cycle
  + **Race Condition:** Use synchronization or atomic operations
  + **Starvation:** Use fair locks and avoid thread priorities
  + **Prevention is better than detection:** Design thread-safe code from the start
  + **Use high-level concurrency utilities:** They handle many issues for you
  + **Test thoroughly:** Concurrency bugs are hard to reproduce
  
</details>


