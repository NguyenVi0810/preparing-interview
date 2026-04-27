package com.example.flatmap;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Ví dụ nâng cao về FlatMap
 * Use cases: Optional handling, Error handling, Data transformation
 */
public class FlatMapAdvancedExamples {

    // ============================================
    // 1. OPTIONAL HANDLING: Xử lý dữ liệu có thể null
    // ============================================

//     Các kỹ thuật nâng cao:
// - **Optional Handling**: Xử lý dữ liệu có thể null, loại bỏ Optional.empty
// - **Error Handling**: Xử lý kết quả có thể lỗi với Result pattern
// - **Data Transformation**: Chuyển đổi dữ liệu phức tạp (Library → Book → Authors/Tags)
// - **Graph Traversal**: Duyệt cây và đồ thị (TreeNode với children)
// - **Batch Processing**: Xử lý transactions theo batch

    static class UserProfile {
        private String userId;
        private Optional<String> email;
        private Optional<String> phone;
        private Optional<Address> address;

        public UserProfile(String userId, String email, String phone, Address address) {
            this.userId = userId;
            this.email = Optional.ofNullable(email);
            this.phone = Optional.ofNullable(phone);
            this.address = Optional.ofNullable(address);
        }

        public String getUserId() { return userId; }
        public Optional<String> getEmail() { return email; }
        public Optional<String> getPhone() { return phone; }
        public Optional<Address> getAddress() { return address; }
    }

    static class Address {
        private String street;
        private String city;
        private Optional<String> zipCode;

        public Address(String street, String city, String zipCode) {
            this.street = street;
            this.city = city;
            this.zipCode = Optional.ofNullable(zipCode);
        }

        public String getStreet() { return street; }
        public String getCity() { return city; }
        public Optional<String> getZipCode() { return zipCode; }

        @Override
        public String toString() {
            return street + ", " + city + zipCode.map(z -> " " + z).orElse("");
        }
    }

    /**
     * Use Case 1: Lấy tất cả email hợp lệ (loại bỏ Optional.empty)
     */
    public static void optionalHandlingExample() {
        System.out.println("\n=== OPTIONAL HANDLING EXAMPLE ===");

        List<UserProfile> users = Arrays.asList(
            new UserProfile("U1", "alice@example.com", "123456", 
                new Address("123 Main St", "NYC", "10001")),
            new UserProfile("U2", null, "789012", 
                new Address("456 Oak Ave", "LA", null)),
            new UserProfile("U3", "charlie@example.com", null, null),
            new UserProfile("U4", null, null, null)
        );

        // Lấy tất cả email (loại bỏ empty)
        List<String> allEmails = users.stream()
            .map(UserProfile::getEmail)           // Stream<Optional<String>>
            .flatMap(Optional::stream)            // Stream<String> - loại bỏ empty
            .collect(Collectors.toList());

        System.out.println("Tất cả emails: " + allEmails);

        // Lấy tất cả địa chỉ
        List<Address> allAddresses = users.stream()
            .map(UserProfile::getAddress)
            .flatMap(Optional::stream)
            .collect(Collectors.toList());

        System.out.println("Tất cả địa chỉ: " + allAddresses);

        // Lấy tất cả zip codes (nested Optional)
        List<String> allZipCodes = users.stream()
            .map(UserProfile::getAddress)         // Stream<Optional<Address>>
            .flatMap(Optional::stream)            // Stream<Address>
            .map(Address::getZipCode)             // Stream<Optional<String>>
            .flatMap(Optional::stream)            // Stream<String>
            .collect(Collectors.toList());

        System.out.println("Tất cả zip codes: " + allZipCodes);

        // Lấy contact info (email hoặc phone)
        List<String> contactInfo = users.stream()
            .flatMap(user -> Stream.of(user.getEmail(), user.getPhone()))
            .flatMap(Optional::stream)
            .collect(Collectors.toList());

        System.out.println("Tất cả contact info: " + contactInfo);
    }

    // ============================================
    // 2. ERROR HANDLING: Xử lý kết quả có thể lỗi
    // ============================================

    static class Result<T> {
        private final T value;
        private final String error;
        private final boolean success;

        private Result(T value, String error, boolean success) {
            this.value = value;
            this.error = error;
            this.success = success;
        }

        public static <T> Result<T> success(T value) {
            return new Result<>(value, null, true);
        }

        public static <T> Result<T> failure(String error) {
            return new Result<>(null, error, false);
        }

        public boolean isSuccess() { return success; }
        public T getValue() { return value; }
        public String getError() { return error; }

        public Stream<T> stream() {
            return success ? Stream.of(value) : Stream.empty();
        }

        @Override
        public String toString() {
            return success ? "Success(" + value + ")" : "Failure(" + error + ")";
        }
    }

    /**
     * Use Case 2: Parse dữ liệu và lọc ra các kết quả thành công
     */
    public static void errorHandlingExample() {
        System.out.println("\n=== ERROR HANDLING EXAMPLE ===");

        List<String> inputs = Arrays.asList("123", "456", "invalid", "789", "abc");

        // Parse integers và lọc ra các kết quả thành công
        List<Result<Integer>> results = inputs.stream()
            .map(input -> {
                try {
                    return Result.success(Integer.parseInt(input));
                } catch (NumberFormatException e) {
                    return Result.failure("Invalid number: " + input);
                }
            })
            .collect(Collectors.toList());

        System.out.println("Tất cả results: " + results);

        // Chỉ lấy các giá trị thành công
        List<Integer> successValues = results.stream()
            .flatMap(Result::stream)
            .collect(Collectors.toList());

        System.out.println("Chỉ giá trị thành công: " + successValues);

        // Lấy các lỗi
        List<String> errors = results.stream()
            .filter(r -> !r.isSuccess())
            .map(Result::getError)
            .collect(Collectors.toList());

        System.out.println("Các lỗi: " + errors);
    }

    // ============================================
    // 3. DATA TRANSFORMATION: Chuyển đổi dữ liệu phức tạp
    // ============================================

    static class Book {
        private String title;
        private List<String> authors;
        private List<String> tags;

        public Book(String title, List<String> authors, List<String> tags) {
            this.title = title;
            this.authors = authors;
            this.tags = tags;
        }

        public String getTitle() { return title; }
        public List<String> getAuthors() { return authors; }
        public List<String> getTags() { return tags; }

        @Override
        public String toString() {
            return title + " by " + String.join(", ", authors);
        }
    }

    static class Library {
        private String name;
        private List<Book> books;

        public Library(String name, List<Book> books) {
            this.name = name;
            this.books = books;
        }

        public String getName() { return name; }
        public List<Book> getBooks() { return books; }
    }

    /**
     * Use Case 3: Tìm kiếm và phân tích dữ liệu sách
     */
    public static void dataTransformationExample() {
        System.out.println("\n=== DATA TRANSFORMATION EXAMPLE ===");

        List<Library> libraries = Arrays.asList(
            new Library("Central Library", Arrays.asList(
                new Book("Clean Code", 
                    Arrays.asList("Robert Martin"), 
                    Arrays.asList("programming", "java", "best-practices")),
                new Book("Design Patterns", 
                    Arrays.asList("Gang of Four"), 
                    Arrays.asList("programming", "oop", "patterns"))
            )),
            new Library("Tech Library", Arrays.asList(
                new Book("Effective Java", 
                    Arrays.asList("Joshua Bloch"), 
                    Arrays.asList("java", "best-practices")),
                new Book("Spring in Action", 
                    Arrays.asList("Craig Walls"), 
                    Arrays.asList("java", "spring", "framework"))
            ))
        );

        // Lấy tất cả sách từ tất cả thư viện
        List<Book> allBooks = libraries.stream()
            .flatMap(library -> library.getBooks().stream())
            .collect(Collectors.toList());

        System.out.println("Tất cả sách: " + allBooks);

        // Lấy tất cả tác giả unique
        Set<String> allAuthors = libraries.stream()
            .flatMap(library -> library.getBooks().stream())
            .flatMap(book -> book.getAuthors().stream())
            .collect(Collectors.toSet());

        System.out.println("Tất cả tác giả: " + allAuthors);

        // Lấy tất cả tags và đếm số lần xuất hiện
        Map<String, Long> tagCounts = libraries.stream()
            .flatMap(library -> library.getBooks().stream())
            .flatMap(book -> book.getTags().stream())
            .collect(Collectors.groupingBy(tag -> tag, Collectors.counting()));

        System.out.println("Tag counts: " + tagCounts);

        // Tìm sách có tag "java"
        List<Book> javaBooks = libraries.stream()
            .flatMap(library -> library.getBooks().stream())
            .filter(book -> book.getTags().contains("java"))
            .collect(Collectors.toList());

        System.out.println("Sách về Java: " + javaBooks);
    }

    // ============================================
    // 4. GRAPH TRAVERSAL: Duyệt cây/đồ thị
    // ============================================

    static class TreeNode {
        private String value;
        private List<TreeNode> children;

        public TreeNode(String value) {
            this.value = value;
            this.children = new ArrayList<>();
        }

        public void addChild(TreeNode child) {
            this.children.add(child);
        }

        public String getValue() { return value; }
        public List<TreeNode> getChildren() { return children; }

        @Override
        public String toString() {
            return value;
        }
    }

    /**
     * Use Case 4: Duyệt cây và lấy tất cả nodes
     */
    public static void graphTraversalExample() {
        System.out.println("\n=== GRAPH TRAVERSAL EXAMPLE ===");

        // Tạo cây
        TreeNode root = new TreeNode("Root");
        
        TreeNode child1 = new TreeNode("Child1");
        TreeNode child2 = new TreeNode("Child2");
        
        TreeNode grandChild1 = new TreeNode("GrandChild1");
        TreeNode grandChild2 = new TreeNode("GrandChild2");
        TreeNode grandChild3 = new TreeNode("GrandChild3");

        root.addChild(child1);
        root.addChild(child2);
        
        child1.addChild(grandChild1);
        child1.addChild(grandChild2);
        
        child2.addChild(grandChild3);

        // Duyệt cây và lấy tất cả nodes (BFS-like)
        List<TreeNode> allNodes = getAllNodesRecursive(root);
        System.out.println("Tất cả nodes: " + allNodes);

        // Đếm tổng số nodes
        long totalNodes = countNodes(root);
        System.out.println("Tổng số nodes: " + totalNodes);

        // Tìm node theo value
        Optional<TreeNode> found = findNode(root, "GrandChild2");
        System.out.println("Tìm 'GrandChild2': " + found);
    }

    // Helper method: Lấy tất cả nodes trong cây
    private static List<TreeNode> getAllNodesRecursive(TreeNode node) {
        return Stream.concat(
            Stream.of(node),
            node.getChildren().stream()
                .flatMap(child -> getAllNodesRecursive(child).stream())
        ).collect(Collectors.toList());
    }

    // Helper method: Đếm số nodes
    private static long countNodes(TreeNode node) {
        return 1 + node.getChildren().stream()
            .flatMap(child -> Stream.of(countNodes(child)))
            .mapToLong(Long::longValue)
            .sum();
    }

    // Helper method: Tìm node theo value
    private static Optional<TreeNode> findNode(TreeNode node, String value) {
        return Stream.concat(
            Stream.of(node),
            node.getChildren().stream()
                .flatMap(child -> findNode(child, value).stream())
        ).filter(n -> n.getValue().equals(value))
         .findFirst();
    }

    // ============================================
    // 5. BATCH PROCESSING: Xử lý dữ liệu theo batch
    // ============================================

    static class Transaction {
        private String id;
        private String userId;
        private double amount;
        private String status; // "pending", "completed", "failed"

        public Transaction(String id, String userId, double amount, String status) {
            this.id = id;
            this.userId = userId;
            this.amount = amount;
            this.status = status;
        }

        public String getId() { return id; }
        public String getUserId() { return userId; }
        public double getAmount() { return amount; }
        public String getStatus() { return status; }

        @Override
        public String toString() {
            return id + " ($" + amount + " - " + status + ")";
        }
    }

    static class TransactionBatch {
        private String batchId;
        private List<Transaction> transactions;

        public TransactionBatch(String batchId, List<Transaction> transactions) {
            this.batchId = batchId;
            this.transactions = transactions;
        }

        public String getBatchId() { return batchId; }
        public List<Transaction> getTransactions() { return transactions; }
    }

    /**
     * Use Case 5: Xử lý transactions theo batch
     */
    public static void batchProcessingExample() {
        System.out.println("\n=== BATCH PROCESSING EXAMPLE ===");

        List<TransactionBatch> batches = Arrays.asList(
            new TransactionBatch("B1", Arrays.asList(
                new Transaction("T1", "U1", 100.0, "completed"),
                new Transaction("T2", "U2", 200.0, "pending"),
                new Transaction("T3", "U1", 150.0, "completed")
            )),
            new TransactionBatch("B2", Arrays.asList(
                new Transaction("T4", "U3", 300.0, "failed"),
                new Transaction("T5", "U2", 250.0, "completed")
            )),
            new TransactionBatch("B3", Arrays.asList(
                new Transaction("T6", "U1", 400.0, "pending")
            ))
        );

        // Lấy tất cả transactions
        List<Transaction> allTransactions = batches.stream()
            .flatMap(batch -> batch.getTransactions().stream())
            .collect(Collectors.toList());

        System.out.println("Tất cả transactions: " + allTransactions);

        // Lấy transactions đã hoàn thành
        List<Transaction> completedTransactions = batches.stream()
            .flatMap(batch -> batch.getTransactions().stream())
            .filter(t -> t.getStatus().equals("completed"))
            .collect(Collectors.toList());

        System.out.println("Completed transactions: " + completedTransactions);

        // Tính tổng tiền theo user
        Map<String, Double> totalByUser = batches.stream()
            .flatMap(batch -> batch.getTransactions().stream())
            .filter(t -> t.getStatus().equals("completed"))
            .collect(Collectors.groupingBy(
                Transaction::getUserId,
                Collectors.summingDouble(Transaction::getAmount)
            ));

        System.out.println("Tổng tiền theo user: " + totalByUser);

        // Tính tổng tiền tất cả completed transactions
        double totalCompleted = batches.stream()
            .flatMap(batch -> batch.getTransactions().stream())
            .filter(t -> t.getStatus().equals("completed"))
            .mapToDouble(Transaction::getAmount)
            .sum();

        System.out.println("Tổng tiền completed: $" + totalCompleted);
    }

    // ============================================
    // MAIN METHOD
    // ============================================

    public static void main(String[] args) {
        optionalHandlingExample();
        errorHandlingExample();
        dataTransformationExample();
        graphTraversalExample();
        batchProcessingExample();
    }
}
