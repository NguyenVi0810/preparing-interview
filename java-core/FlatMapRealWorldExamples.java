package com.example.flatmap;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Ví dụ thực tế về FlatMap trong OOP
 * Use cases: E-commerce, Social Network, Order Management
 */
public class FlatMapRealWorldExamples {

    // ============================================
    // 1. E-COMMERCE: Quản lý đơn hàng và sản phẩm
    // ============================================


    // Các use case thực tế trong OOP:
// - **E-commerce**: Quản lý đơn hàng và sản phẩm (Customer → Order → Product)
// - **Social Network**: Tìm bạn bè của bạn bè (Friend suggestions)
// - **Organization**: Quản lý nhân viên và phòng ban (Company → Department → Employee)
// - **Blog System**: Quản lý bài viết và comments lồng nhau
// - **Permission System**: Quản lý quyền phân cấp (Role → Permission → SubPermission)
    
    static class Product {
        private String id;
        private String name;
        private double price;

        public Product(String id, String name, double price) {
            this.id = id;
            this.name = name;
            this.price = price;
        }

        public String getId() { return id; }
        public String getName() { return name; }
        public double getPrice() { return price; }

        @Override
        public String toString() {
            return name + " ($" + price + ")";
        }
    }

    static class Order {
        private String orderId;
        private String customerId;
        private List<Product> products;

        public Order(String orderId, String customerId, List<Product> products) {
            this.orderId = orderId;
            this.customerId = customerId;
            this.products = products;
        }

        public String getOrderId() { return orderId; }
        public String getCustomerId() { return customerId; }
        public List<Product> getProducts() { return products; }
    }

    static class Customer {
        private String id;
        private String name;
        private List<Order> orders;

        public Customer(String id, String name, List<Order> orders) {
            this.id = id;
            this.name = name;
            this.orders = orders;
        }

        public String getId() { return id; }
        public String getName() { return name; }
        public List<Order> getOrders() { return orders; }
    }

    /**
     * Use Case 1: Lấy tất cả sản phẩm từ tất cả đơn hàng của tất cả khách hàng
     */
    public static void ecommerceExample() {
        System.out.println("\n=== E-COMMERCE EXAMPLE ===");
        
        // Tạo dữ liệu mẫu
        List<Customer> customers = Arrays.asList(
            new Customer("C1", "John", Arrays.asList(
                new Order("O1", "C1", Arrays.asList(
                    new Product("P1", "Laptop", 1000),
                    new Product("P2", "Mouse", 20)
                )),
                new Order("O2", "C1", Arrays.asList(
                    new Product("P3", "Keyboard", 50)
                ))
            )),
            new Customer("C2", "Jane", Arrays.asList(
                new Order("O3", "C2", Arrays.asList(
                    new Product("P4", "Monitor", 300),
                    new Product("P5", "Webcam", 80)
                ))
            ))
        );

        // Lấy tất cả sản phẩm từ tất cả đơn hàng của tất cả khách hàng
        List<Product> allProducts = customers.stream()
            .flatMap(customer -> customer.getOrders().stream())  // Customer -> Stream<Order>
            .flatMap(order -> order.getProducts().stream())      // Order -> Stream<Product>
            .collect(Collectors.toList());

        System.out.println("Tất cả sản phẩm đã bán: " + allProducts);

        // Tính tổng doanh thu
        double totalRevenue = customers.stream()
            .flatMap(customer -> customer.getOrders().stream())
            .flatMap(order -> order.getProducts().stream())
            .mapToDouble(Product::getPrice)
            .sum();

        System.out.println("Tổng doanh thu: $" + totalRevenue);

        // Tìm sản phẩm có giá > $50
        List<Product> expensiveProducts = customers.stream()
            .flatMap(customer -> customer.getOrders().stream())
            .flatMap(order -> order.getProducts().stream())
            .filter(product -> product.getPrice() > 50)
            .distinct()
            .collect(Collectors.toList());

        System.out.println("Sản phẩm giá > $50: " + expensiveProducts);
    }

    // ============================================
    // 2. SOCIAL NETWORK: Tìm bạn bè của bạn bè
    // ============================================

    static class User {
        private String id;
        private String name;
        private List<User> friends;

        public User(String id, String name) {
            this.id = id;
            this.name = name;
            this.friends = new ArrayList<>();
        }

        public void addFriend(User friend) {
            this.friends.add(friend);
        }

        public String getId() { return id; }
        public String getName() { return name; }
        public List<User> getFriends() { return friends; }

        @Override
        public String toString() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            User user = (User) o;
            return Objects.equals(id, user.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }

    /**
     * Use Case 2: Tìm "bạn bè của bạn bè" (Friend suggestions)
     */
    public static void socialNetworkExample() {
        System.out.println("\n=== SOCIAL NETWORK EXAMPLE ===");

        // Tạo users
        User alice = new User("U1", "Alice");
        User bob = new User("U2", "Bob");
        User charlie = new User("U3", "Charlie");
        User david = new User("U4", "David");
        User eve = new User("U5", "Eve");

        // Thiết lập quan hệ bạn bè
        alice.addFriend(bob);
        alice.addFriend(charlie);
        
        bob.addFriend(alice);
        bob.addFriend(david);
        bob.addFriend(eve);
        
        charlie.addFriend(alice);
        charlie.addFriend(eve);

        // Tìm bạn bè của bạn bè Alice (friend suggestions)
        Set<User> friendsOfFriends = alice.getFriends().stream()
            .flatMap(friend -> friend.getFriends().stream())  // Lấy bạn bè của mỗi bạn
            .filter(user -> !user.equals(alice))              // Loại bỏ chính Alice
            .filter(user -> !alice.getFriends().contains(user)) // Loại bỏ bạn bè hiện tại
            .collect(Collectors.toSet());

        System.out.println("Bạn bè của " + alice.getName() + ": " + alice.getFriends());
        System.out.println("Gợi ý kết bạn cho " + alice.getName() + ": " + friendsOfFriends);
    }

    // ============================================
    // 3. ORGANIZATION: Quản lý nhân viên và phòng ban
    // ============================================

    static class Employee {
        private String id;
        private String name;
        private List<String> skills;

        public Employee(String id, String name, List<String> skills) {
            this.id = id;
            this.name = name;
            this.skills = skills;
        }

        public String getId() { return id; }
        public String getName() { return name; }
        public List<String> getSkills() { return skills; }

        @Override
        public String toString() {
            return name + " (" + String.join(", ", skills) + ")";
        }
    }

    static class Department {
        private String name;
        private List<Employee> employees;

        public Department(String name, List<Employee> employees) {
            this.name = name;
            this.employees = employees;
        }

        public String getName() { return name; }
        public List<Employee> getEmployees() { return employees; }
    }

    static class Company {
        private String name;
        private List<Department> departments;

        public Company(String name, List<Department> departments) {
            this.name = name;
            this.departments = departments;
        }

        public String getName() { return name; }
        public List<Department> getDepartments() { return departments; }
    }

    /**
     * Use Case 3: Tìm tất cả nhân viên có skill cụ thể trong công ty
     */
    public static void organizationExample() {
        System.out.println("\n=== ORGANIZATION EXAMPLE ===");

        Company company = new Company("Tech Corp", Arrays.asList(
            new Department("Engineering", Arrays.asList(
                new Employee("E1", "Alice", Arrays.asList("Java", "Spring", "Docker")),
                new Employee("E2", "Bob", Arrays.asList("Python", "Django", "AWS"))
            )),
            new Department("DevOps", Arrays.asList(
                new Employee("E3", "Charlie", Arrays.asList("Docker", "Kubernetes", "AWS")),
                new Employee("E4", "David", Arrays.asList("Terraform", "AWS", "Jenkins"))
            )),
            new Department("Data", Arrays.asList(
                new Employee("E5", "Eve", Arrays.asList("Python", "Spark", "SQL"))
            ))
        ));

        // Lấy tất cả nhân viên trong công ty
        List<Employee> allEmployees = company.getDepartments().stream()
            .flatMap(dept -> dept.getEmployees().stream())
            .collect(Collectors.toList());

        System.out.println("Tất cả nhân viên: " + allEmployees);

        // Tìm nhân viên có skill "AWS"
        List<Employee> awsExperts = company.getDepartments().stream()
            .flatMap(dept -> dept.getEmployees().stream())
            .filter(emp -> emp.getSkills().contains("AWS"))
            .collect(Collectors.toList());

        System.out.println("Nhân viên có skill AWS: " + awsExperts);

        // Lấy tất cả skills unique trong công ty
        Set<String> allSkills = company.getDepartments().stream()
            .flatMap(dept -> dept.getEmployees().stream())
            .flatMap(emp -> emp.getSkills().stream())
            .collect(Collectors.toSet());

        System.out.println("Tất cả skills trong công ty: " + allSkills);
    }

    // ============================================
    // 4. BLOG SYSTEM: Quản lý bài viết và comments
    // ============================================

    static class Comment {
        private String author;
        private String content;
        private List<Comment> replies;

        public Comment(String author, String content) {
            this.author = author;
            this.content = content;
            this.replies = new ArrayList<>();
        }

        public void addReply(Comment reply) {
            this.replies.add(reply);
        }

        public String getAuthor() { return author; }
        public String getContent() { return content; }
        public List<Comment> getReplies() { return replies; }

        @Override
        public String toString() {
            return author + ": " + content;
        }
    }

    static class BlogPost {
        private String title;
        private String author;
        private List<Comment> comments;

        public BlogPost(String title, String author, List<Comment> comments) {
            this.title = title;
            this.author = author;
            this.comments = comments;
        }

        public String getTitle() { return title; }
        public String getAuthor() { return author; }
        public List<Comment> getComments() { return comments; }
    }

    /**
     * Use Case 4: Lấy tất cả comments (bao gồm replies) từ blog posts
     */
    public static void blogSystemExample() {
        System.out.println("\n=== BLOG SYSTEM EXAMPLE ===");

        // Tạo comments với replies
        Comment comment1 = new Comment("User1", "Great article!");
        Comment reply1 = new Comment("User2", "I agree!");
        Comment reply2 = new Comment("User3", "Thanks for sharing");
        comment1.addReply(reply1);
        comment1.addReply(reply2);

        Comment comment2 = new Comment("User4", "Very helpful");

        List<BlogPost> posts = Arrays.asList(
            new BlogPost("Java Streams", "Author1", Arrays.asList(comment1, comment2)),
            new BlogPost("Spring Boot", "Author2", Arrays.asList(
                new Comment("User5", "Nice tutorial")
            ))
        );

        // Lấy tất cả comments (không bao gồm replies)
        List<Comment> allComments = posts.stream()
            .flatMap(post -> post.getComments().stream())
            .collect(Collectors.toList());

        System.out.println("Tất cả comments: " + allComments);

        // Lấy tất cả comments bao gồm cả replies (flatten nested structure)
        List<Comment> allCommentsWithReplies = posts.stream()
            .flatMap(post -> post.getComments().stream())
            .flatMap(comment -> {
                List<Comment> all = new ArrayList<>();
                all.add(comment);
                all.addAll(comment.getReplies());
                return all.stream();
            })
            .collect(Collectors.toList());

        System.out.println("Tất cả comments + replies: " + allCommentsWithReplies);

        // Đếm tổng số comments và replies
        long totalComments = posts.stream()
            .flatMap(post -> post.getComments().stream())
            .flatMap(comment -> {
                List<Comment> all = new ArrayList<>();
                all.add(comment);
                all.addAll(comment.getReplies());
                return all.stream();
            })
            .count();

        System.out.println("Tổng số comments (bao gồm replies): " + totalComments);
    }

    // ============================================
    // 5. PERMISSION SYSTEM: Quản lý quyền phân cấp
    // ============================================

    static class Permission {
        private String name;
        private List<Permission> subPermissions;

        public Permission(String name) {
            this.name = name;
            this.subPermissions = new ArrayList<>();
        }

        public void addSubPermission(Permission permission) {
            this.subPermissions.add(permission);
        }

        public String getName() { return name; }
        public List<Permission> getSubPermissions() { return subPermissions; }

        @Override
        public String toString() {
            return name;
        }
    }

    static class Role {
        private String name;
        private List<Permission> permissions;

        public Role(String name, List<Permission> permissions) {
            this.name = name;
            this.permissions = permissions;
        }

        public String getName() { return name; }
        public List<Permission> getPermissions() { return permissions; }
    }

    /**
     * Use Case 5: Lấy tất cả permissions (bao gồm sub-permissions) của một role
     */
    public static void permissionSystemExample() {
        System.out.println("\n=== PERMISSION SYSTEM EXAMPLE ===");

        // Tạo permission hierarchy
        Permission readUser = new Permission("user:read");
        Permission writeUser = new Permission("user:write");
        Permission deleteUser = new Permission("user:delete");

        Permission userManagement = new Permission("user:*");
        userManagement.addSubPermission(readUser);
        userManagement.addSubPermission(writeUser);
        userManagement.addSubPermission(deleteUser);

        Permission readProduct = new Permission("product:read");
        Permission writeProduct = new Permission("product:write");

        Permission productManagement = new Permission("product:*");
        productManagement.addSubPermission(readProduct);
        productManagement.addSubPermission(writeProduct);

        // Tạo role
        Role adminRole = new Role("ADMIN", Arrays.asList(userManagement, productManagement));

        // Lấy tất cả permissions (bao gồm sub-permissions)
        List<Permission> allPermissions = adminRole.getPermissions().stream()
            .flatMap(permission -> {
                List<Permission> all = new ArrayList<>();
                all.add(permission);
                all.addAll(permission.getSubPermissions());
                return all.stream();
            })
            .collect(Collectors.toList());

        System.out.println("Role: " + adminRole.getName());
        System.out.println("Tất cả permissions: " + allPermissions);

        // Kiểm tra xem role có permission cụ thể không
        boolean hasDeleteUser = adminRole.getPermissions().stream()
            .flatMap(permission -> {
                List<Permission> all = new ArrayList<>();
                all.add(permission);
                all.addAll(permission.getSubPermissions());
                return all.stream();
            })
            .anyMatch(p -> p.getName().equals("user:delete"));

        System.out.println("Has 'user:delete' permission: " + hasDeleteUser);
    }

    // ============================================
    // MAIN METHOD
    // ============================================

    public static void main(String[] args) {
        ecommerceExample();
        socialNetworkExample();
        organizationExample();
        blogSystemExample();
        permissionSystemExample();
    }
}
