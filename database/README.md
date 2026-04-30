## CRUD Operations

<details>
  <summary>SELECT - Retrieve Data</summary>
  <br/>

  **SELECT** retrieves data from database tables.

  **Basic SELECT:**

  ```sql
  -- Select all columns
  SELECT * FROM users;
  
  -- Select specific columns
  SELECT id, username, email FROM users;
  
  -- Select with alias
  SELECT 
      id AS user_id,
      username AS user_name,
      email AS user_email
  FROM users;
  ```

  **WHERE clause (filtering):**

  ```sql
  -- Single condition
  SELECT * FROM users WHERE age > 18;
  
  -- Multiple conditions (AND)
  SELECT * FROM users WHERE age > 18 AND country = 'USA';
  
  -- Multiple conditions (OR)
  SELECT * FROM users WHERE country = 'USA' OR country = 'Canada';
  
  -- IN operator
  SELECT * FROM users WHERE country IN ('USA', 'Canada', 'UK');
  
  -- BETWEEN operator
  SELECT * FROM users WHERE age BETWEEN 18 AND 65;
  
  -- LIKE operator (pattern matching)
  SELECT * FROM users WHERE email LIKE '%@gmail.com';
  SELECT * FROM users WHERE username LIKE 'john%';
  
  -- IS NULL / IS NOT NULL
  SELECT * FROM users WHERE phone IS NULL;
  SELECT * FROM users WHERE phone IS NOT NULL;
  ```

  **ORDER BY (sorting):**

  ```sql
  -- Ascending order (default)
  SELECT * FROM users ORDER BY username;
  SELECT * FROM users ORDER BY username ASC;
  
  -- Descending order
  SELECT * FROM users ORDER BY created_at DESC;
  
  -- Multiple columns
  SELECT * FROM users ORDER BY country ASC, age DESC;
  ```

  **LIMIT and OFFSET (pagination):**

  ```sql
  -- First 10 records
  SELECT * FROM users LIMIT 10;
  
  -- Skip first 10, get next 10 (pagination)
  SELECT * FROM users LIMIT 10 OFFSET 10;
  
  -- Alternative syntax (MySQL)
  SELECT * FROM users LIMIT 10, 10;  -- OFFSET, LIMIT
  ```

  **DISTINCT (unique values):**

  ```sql
  -- Get unique countries
  SELECT DISTINCT country FROM users;
  
  -- Get unique combinations
  SELECT DISTINCT country, city FROM users;
  ```

</details>

<details>
  <summary>INSERT - Add Data</summary>
  <br/>

  **INSERT** adds new records to a table.

  **Insert single row:**

  ```sql
  -- Insert with all columns
  INSERT INTO users (id, username, email, age, country)
  VALUES (1, 'john_doe', 'john@example.com', 25, 'USA');
  
  -- Insert without specifying columns (must match table order)
  INSERT INTO users
  VALUES (2, 'jane_doe', 'jane@example.com', 30, 'Canada');
  
  -- Insert with some columns (others will be NULL or default)
  INSERT INTO users (username, email)
  VALUES ('bob_smith', 'bob@example.com');
  ```

  **Insert multiple rows:**

  ```sql
  INSERT INTO users (username, email, age, country)
  VALUES 
      ('alice', 'alice@example.com', 28, 'USA'),
      ('charlie', 'charlie@example.com', 35, 'UK'),
      ('david', 'david@example.com', 22, 'Canada');
  ```

  **Insert from SELECT:**

  ```sql
  -- Copy data from another table
  INSERT INTO users_backup (username, email, age)
  SELECT username, email, age FROM users WHERE age > 18;
  
  -- Insert with transformation
  INSERT INTO user_summary (username, email_domain)
  SELECT username, SUBSTRING(email, POSITION('@' IN email) + 1)
  FROM users;
  ```

  **Insert with RETURNING (PostgreSQL):**

  ```sql
  -- Return inserted row
  INSERT INTO users (username, email)
  VALUES ('new_user', 'new@example.com')
  RETURNING id, username, created_at;
  ```

  **Insert with ON CONFLICT (PostgreSQL - Upsert):**

  ```sql
  -- Insert or update if exists
  INSERT INTO users (id, username, email)
  VALUES (1, 'john_doe', 'john@example.com')
  ON CONFLICT (id) 
  DO UPDATE SET 
      username = EXCLUDED.username,
      email = EXCLUDED.email,
      updated_at = CURRENT_TIMESTAMP;
  
  -- Insert or do nothing if exists
  INSERT INTO users (username, email)
  VALUES ('john_doe', 'john@example.com')
  ON CONFLICT (username) DO NOTHING;
  ```

  **Insert with IGNORE (MySQL):**

  ```sql
  -- Insert or ignore if duplicate key
  INSERT IGNORE INTO users (username, email)
  VALUES ('john_doe', 'john@example.com');
  ```

</details>

<details>
  <summary>UPDATE - Modify Data</summary>
  <br/>

  **UPDATE** modifies existing records in a table.

  **Basic UPDATE:**

  ```sql
  -- Update single column
  UPDATE users
  SET email = 'newemail@example.com'
  WHERE id = 1;
  
  -- Update multiple columns
  UPDATE users
  SET 
      email = 'newemail@example.com',
      age = 26,
      updated_at = CURRENT_TIMESTAMP
  WHERE id = 1;
  ```

  **Update with conditions:**

  ```sql
  -- Update based on condition
  UPDATE users
  SET status = 'active'
  WHERE last_login > CURRENT_DATE - INTERVAL '30 days';
  
  -- Update multiple rows
  UPDATE users
  SET country = 'USA'
  WHERE country IS NULL;
  
  -- Update with IN
  UPDATE users
  SET status = 'premium'
  WHERE id IN (1, 2, 3, 4, 5);
  ```

  **Update with calculations:**

  ```sql
  -- Increment value
  UPDATE products
  SET stock = stock + 10
  WHERE id = 1;
  
  -- Decrement value
  UPDATE products
  SET stock = stock - 5
  WHERE id = 1;
  
  -- Calculate percentage
  UPDATE products
  SET price = price * 1.1  -- 10% increase
  WHERE category = 'electronics';
  ```

  **Update with subquery:**

  ```sql
  -- Update based on another table
  UPDATE users
  SET status = 'vip'
  WHERE id IN (
      SELECT user_id 
      FROM orders 
      GROUP BY user_id 
      HAVING SUM(total) > 1000
  );
  
  -- Update with JOIN (MySQL)
  UPDATE users u
  INNER JOIN orders o ON u.id = o.user_id
  SET u.total_spent = o.total
  WHERE o.id = 123;
  ```

  **Update with CASE:**

  ```sql
  -- Conditional update
  UPDATE users
  SET discount = CASE
      WHEN age < 18 THEN 0.05
      WHEN age BETWEEN 18 AND 65 THEN 0.10
      ELSE 0.15
  END;
  ```

  **Update all rows (dangerous!):**

  ```sql
  -- Update all rows (use with caution!)
  UPDATE users
  SET status = 'inactive';
  
  -- Always use WHERE clause to avoid updating all rows
  ```

</details>

<details>
  <summary>DELETE - Remove Data</summary>
  <br/>

  **DELETE** removes records from a table.

  **Basic DELETE:**

  ```sql
  -- Delete single row
  DELETE FROM users WHERE id = 1;
  
  -- Delete multiple rows
  DELETE FROM users WHERE age < 18;
  
  -- Delete with IN
  DELETE FROM users WHERE id IN (1, 2, 3, 4, 5);
  ```

  **Delete with conditions:**

  ```sql
  -- Delete based on date
  DELETE FROM logs
  WHERE created_at < CURRENT_DATE - INTERVAL '90 days';
  
  -- Delete based on NULL
  DELETE FROM users WHERE email IS NULL;
  
  -- Delete with multiple conditions
  DELETE FROM users
  WHERE status = 'inactive' AND last_login < '2023-01-01';
  ```

  **Delete with subquery:**

  ```sql
  -- Delete based on another table
  DELETE FROM users
  WHERE id IN (
      SELECT user_id 
      FROM banned_users
  );
  
  -- Delete with NOT EXISTS
  DELETE FROM users u
  WHERE NOT EXISTS (
      SELECT 1 
      FROM orders o 
      WHERE o.user_id = u.id
  );
  ```

  **Delete with JOIN (MySQL):**

  ```sql
  -- Delete with JOIN
  DELETE u
  FROM users u
  INNER JOIN banned_users b ON u.id = b.user_id;
  
  -- Delete from multiple tables
  DELETE u, o
  FROM users u
  INNER JOIN orders o ON u.id = o.user_id
  WHERE u.status = 'deleted';
  ```

  **Delete all rows (dangerous!):**

  ```sql
  -- Delete all rows (use with extreme caution!)
  DELETE FROM users;
  
  -- Better: Use TRUNCATE for deleting all rows (faster, resets auto-increment)
  TRUNCATE TABLE users;
  ```

  **Delete with RETURNING (PostgreSQL):**

  ```sql
  -- Return deleted rows
  DELETE FROM users
  WHERE status = 'inactive'
  RETURNING id, username, email;
  ```

  **Soft delete (recommended):**

  ```sql
  -- Instead of DELETE, use UPDATE to mark as deleted
  UPDATE users
  SET 
      deleted_at = CURRENT_TIMESTAMP,
      status = 'deleted'
  WHERE id = 1;
  
  -- Query only active records
  SELECT * FROM users WHERE deleted_at IS NULL;
  ```

</details>

<details>
  <summary>Aggregate Functions</summary>
  <br/>

  **Aggregate functions** perform calculations on multiple rows.

  **Common aggregate functions:**

  ```sql
  -- COUNT - Count rows
  SELECT COUNT(*) FROM users;
  SELECT COUNT(DISTINCT country) FROM users;
  
  -- SUM - Sum values
  SELECT SUM(price) FROM products;
  SELECT SUM(quantity * price) AS total_revenue FROM order_items;
  
  -- AVG - Average value
  SELECT AVG(age) FROM users;
  SELECT AVG(price) FROM products WHERE category = 'electronics';
  
  -- MIN - Minimum value
  SELECT MIN(price) FROM products;
  SELECT MIN(created_at) FROM users;
  
  -- MAX - Maximum value
  SELECT MAX(price) FROM products;
  SELECT MAX(age) FROM users;
  ```

  **GROUP BY:**

  ```sql
  -- Group by single column
  SELECT country, COUNT(*) AS user_count
  FROM users
  GROUP BY country;
  
  -- Group by multiple columns
  SELECT country, city, COUNT(*) AS user_count
  FROM users
  GROUP BY country, city;
  
  -- Group with multiple aggregates
  SELECT 
      category,
      COUNT(*) AS product_count,
      AVG(price) AS avg_price,
      MIN(price) AS min_price,
      MAX(price) AS max_price
  FROM products
  GROUP BY category;
  ```

  **HAVING (filter groups):**

  ```sql
  -- Filter groups after aggregation
  SELECT country, COUNT(*) AS user_count
  FROM users
  GROUP BY country
  HAVING COUNT(*) > 100;
  
  -- Multiple HAVING conditions
  SELECT category, AVG(price) AS avg_price
  FROM products
  GROUP BY category
  HAVING AVG(price) > 100 AND COUNT(*) > 10;
  ```

  **GROUP BY with ORDER BY:**

  ```sql
  -- Order by aggregate
  SELECT country, COUNT(*) AS user_count
  FROM users
  GROUP BY country
  ORDER BY user_count DESC;
  
  -- Top 5 countries by user count
  SELECT country, COUNT(*) AS user_count
  FROM users
  GROUP BY country
  ORDER BY user_count DESC
  LIMIT 5;
  ```

</details>

<details>
  <summary>Best Practices</summary>
  <br/>

  ```sql
  -- ✅ DO: Always use WHERE clause with UPDATE/DELETE
  UPDATE users SET status = 'inactive' WHERE id = 1;
  DELETE FROM users WHERE id = 1;
  
  -- ❌ DON'T: Update/delete without WHERE (affects all rows!)
  UPDATE users SET status = 'inactive';  -- Updates ALL rows!
  DELETE FROM users;  -- Deletes ALL rows!
  
  -- ✅ DO: Use specific columns in SELECT
  SELECT id, username, email FROM users;
  
  -- ❌ DON'T: Use SELECT * in production (performance impact)
  SELECT * FROM users;  -- Retrieves all columns
  
  -- ✅ DO: Use LIMIT for large result sets
  SELECT * FROM users ORDER BY created_at DESC LIMIT 100;
  
  -- ✅ DO: Use indexes on WHERE/JOIN columns
  CREATE INDEX idx_users_email ON users(email);
  CREATE INDEX idx_users_country ON users(country);
  
  -- ✅ DO: Use transactions for multiple operations
  BEGIN;
  UPDATE accounts SET balance = balance - 100 WHERE id = 1;
  UPDATE accounts SET balance = balance + 100 WHERE id = 2;
  COMMIT;
  
  -- ✅ DO: Use prepared statements to prevent SQL injection
  -- Java example:
  PreparedStatement stmt = conn.prepareStatement(
      "SELECT * FROM users WHERE email = ?"
  );
  stmt.setString(1, email);
  
  -- ❌ DON'T: Concatenate user input (SQL injection risk!)
  String sql = "SELECT * FROM users WHERE email = '" + email + "'";
  
  -- ✅ DO: Use soft delete instead of hard delete
  UPDATE users SET deleted_at = CURRENT_TIMESTAMP WHERE id = 1;
  
  -- ✅ DO: Add timestamps to track changes
  CREATE TABLE users (
      id SERIAL PRIMARY KEY,
      username VARCHAR(50),
      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
  );
  
  -- ✅ DO: Use RETURNING to get inserted/updated data (PostgreSQL)
  INSERT INTO users (username, email)
  VALUES ('john', 'john@example.com')
  RETURNING id, created_at;
  
  -- ✅ DO: Use batch inserts for multiple rows
  INSERT INTO users (username, email) VALUES
      ('user1', 'user1@example.com'),
      ('user2', 'user2@example.com'),
      ('user3', 'user3@example.com');
  
  -- ❌ DON'T: Use multiple single inserts
  INSERT INTO users (username, email) VALUES ('user1', 'user1@example.com');
  INSERT INTO users (username, email) VALUES ('user2', 'user2@example.com');
  INSERT INTO users (username, email) VALUES ('user3', 'user3@example.com');
  
  -- ✅ DO: Use DISTINCT only when necessary (performance impact)
  SELECT DISTINCT country FROM users;
  
  -- ✅ DO: Use COUNT(*) instead of COUNT(column) for counting rows
  SELECT COUNT(*) FROM users;  -- Faster
  SELECT COUNT(id) FROM users;  -- Slower (checks for NULL)
  
  -- ✅ DO: Use EXISTS instead of COUNT for existence checks
  SELECT EXISTS(SELECT 1 FROM users WHERE email = 'john@example.com');
  
  -- ❌ DON'T: Use COUNT for existence checks
  SELECT COUNT(*) FROM users WHERE email = 'john@example.com';
  ```

  **Summary:**
  + **SELECT** - Retrieve data with WHERE, ORDER BY, LIMIT
  + **INSERT** - Add new records (single or multiple rows)
  + **UPDATE** - Modify existing records (always use WHERE!)
  + **DELETE** - Remove records (always use WHERE!)
  + Use **aggregate functions** (COUNT, SUM, AVG, MIN, MAX)
  + Use **GROUP BY** to group rows
  + Use **HAVING** to filter groups
  + Always use **WHERE clause** with UPDATE/DELETE
  + Use **specific columns** instead of SELECT *
  + Use **prepared statements** to prevent SQL injection
  + Use **soft delete** instead of hard delete
  + Use **transactions** for multiple operations
  + Use **indexes** on frequently queried columns
  + Use **LIMIT** for pagination
  + Use **batch inserts** for multiple rows

</details>

## Joins

<details>
  <summary>What are Joins?</summary>
  <br/>

  **Joins** combine rows from two or more tables based on a related column.

  **Sample tables:**

  ```sql
  -- users table
  CREATE TABLE users (
      id INT PRIMARY KEY,
      username VARCHAR(50),
      email VARCHAR(100)
  );
  
  INSERT INTO users VALUES
      (1, 'john', 'john@example.com'),
      (2, 'jane', 'jane@example.com'),
      (3, 'bob', 'bob@example.com'),
      (4, 'alice', 'alice@example.com');
  
  -- orders table
  CREATE TABLE orders (
      id INT PRIMARY KEY,
      user_id INT,
      total DECIMAL(10, 2),
      status VARCHAR(20)
  );
  
  INSERT INTO orders VALUES
      (101, 1, 99.99, 'completed'),
      (102, 1, 149.99, 'pending'),
      (103, 2, 79.99, 'completed'),
      (104, 5, 199.99, 'completed');  -- user_id 5 doesn't exist in users
  ```

  **Join types:**
  + **INNER JOIN** - Returns matching rows from both tables
  + **LEFT JOIN** - Returns all rows from left table + matching rows from right
  + **RIGHT JOIN** - Returns all rows from right table + matching rows from left
  + **FULL OUTER JOIN** - Returns all rows from both tables
  + **CROSS JOIN** - Returns Cartesian product (all combinations)

</details>

<details>
  <summary>INNER JOIN</summary>
  <br/>

  **INNER JOIN** returns only rows that have matching values in both tables.

  **Basic INNER JOIN:**

  ```sql
  -- Get users with their orders
  SELECT 
      users.id,
      users.username,
      orders.id AS order_id,
      orders.total
  FROM users
  INNER JOIN orders ON users.id = orders.user_id;
  
  -- Result:
  -- id | username | order_id | total
  -- 1  | john     | 101      | 99.99
  -- 1  | john     | 102      | 149.99
  -- 2  | jane     | 103      | 79.99
  -- (bob and alice excluded - no orders)
  -- (order 104 excluded - user_id 5 doesn't exist)
  ```

  **Using table aliases:**

  ```sql
  SELECT 
      u.id,
      u.username,
      o.id AS order_id,
      o.total,
      o.status
  FROM users u
  INNER JOIN orders o ON u.id = o.user_id;
  ```

  **Multiple INNER JOINs:**

  ```sql
  -- Join three tables
  SELECT 
      u.username,
      o.id AS order_id,
      oi.product_name,
      oi.quantity,
      oi.price
  FROM users u
  INNER JOIN orders o ON u.id = o.user_id
  INNER JOIN order_items oi ON o.id = oi.order_id;
  ```

  **INNER JOIN with WHERE:**

  ```sql
  -- Filter joined results
  SELECT 
      u.username,
      o.id AS order_id,
      o.total
  FROM users u
  INNER JOIN orders o ON u.id = o.user_id
  WHERE o.status = 'completed' AND o.total > 100;
  ```

  **INNER JOIN with aggregation:**

  ```sql
  -- Count orders per user
  SELECT 
      u.username,
      COUNT(o.id) AS order_count,
      SUM(o.total) AS total_spent
  FROM users u
  INNER JOIN orders o ON u.id = o.user_id
  GROUP BY u.id, u.username;
  ```

  **Alternative syntax (implicit join):**

  ```sql
  -- Old style (not recommended)
  SELECT u.username, o.total
  FROM users u, orders o
  WHERE u.id = o.user_id;
  ```

</details>

<details>
  <summary>LEFT JOIN (LEFT OUTER JOIN)</summary>
  <br/>

  **LEFT JOIN** returns all rows from the left table and matching rows from the right table. If no match, NULL values for right table columns.

  **Basic LEFT JOIN:**

  ```sql
  -- Get all users with their orders (including users without orders)
  SELECT 
      u.id,
      u.username,
      o.id AS order_id,
      o.total
  FROM users u
  LEFT JOIN orders o ON u.id = o.user_id;
  
  -- Result:
  -- id | username | order_id | total
  -- 1  | john     | 101      | 99.99
  -- 1  | john     | 102      | 149.99
  -- 2  | jane     | 103      | 79.99
  -- 3  | bob      | NULL     | NULL     (no orders)
  -- 4  | alice    | NULL     | NULL     (no orders)
  ```

  **Find users without orders:**

  ```sql
  SELECT 
      u.id,
      u.username
  FROM users u
  LEFT JOIN orders o ON u.id = o.user_id
  WHERE o.id IS NULL;
  
  -- Result:
  -- id | username
  -- 3  | bob
  -- 4  | alice
  ```

  **LEFT JOIN with aggregation:**

  ```sql
  -- Count orders per user (including users with 0 orders)
  SELECT 
      u.username,
      COUNT(o.id) AS order_count,
      COALESCE(SUM(o.total), 0) AS total_spent
  FROM users u
  LEFT JOIN orders o ON u.id = o.user_id
  GROUP BY u.id, u.username;
  
  -- Result:
  -- username | order_count | total_spent
  -- john     | 2           | 249.98
  -- jane     | 1           | 79.99
  -- bob      | 0           | 0.00
  -- alice    | 0           | 0.00
  ```

  **Multiple LEFT JOINs:**

  ```sql
  -- Get users with orders and order items (all users included)
  SELECT 
      u.username,
      o.id AS order_id,
      oi.product_name,
      oi.quantity
  FROM users u
  LEFT JOIN orders o ON u.id = o.user_id
  LEFT JOIN order_items oi ON o.id = oi.order_id;
  ```

  **LEFT JOIN with WHERE (careful!):**

  ```sql
  -- This filters AFTER join (excludes users without orders)
  SELECT u.username, o.total
  FROM users u
  LEFT JOIN orders o ON u.id = o.user_id
  WHERE o.status = 'completed';  -- Filters out NULL orders
  
  -- Better: Use AND in ON clause to keep all users
  SELECT u.username, o.total
  FROM users u
  LEFT JOIN orders o ON u.id = o.user_id AND o.status = 'completed';
  ```

</details>

<details>
  <summary>RIGHT JOIN (RIGHT OUTER JOIN)</summary>
  <br/>

  **RIGHT JOIN** returns all rows from the right table and matching rows from the left table. If no match, NULL values for left table columns.

  **Basic RIGHT JOIN:**

  ```sql
  -- Get all orders with their users (including orders without users)
  SELECT 
      u.id,
      u.username,
      o.id AS order_id,
      o.total
  FROM users u
  RIGHT JOIN orders o ON u.id = o.user_id;
  
  -- Result:
  -- id   | username | order_id | total
  -- 1    | john     | 101      | 99.99
  -- 1    | john     | 102      | 149.99
  -- 2    | jane     | 103      | 79.99
  -- NULL | NULL     | 104      | 199.99   (user_id 5 doesn't exist)
  ```

  **Find orders without users:**

  ```sql
  SELECT 
      o.id AS order_id,
      o.user_id,
      o.total
  FROM users u
  RIGHT JOIN orders o ON u.id = o.user_id
  WHERE u.id IS NULL;
  
  -- Result:
  -- order_id | user_id | total
  -- 104      | 5       | 199.99
  ```

  **RIGHT JOIN is equivalent to LEFT JOIN with reversed tables:**

  ```sql
  -- These are equivalent:
  
  -- RIGHT JOIN
  SELECT u.username, o.total
  FROM users u
  RIGHT JOIN orders o ON u.id = o.user_id;
  
  -- LEFT JOIN (reversed)
  SELECT u.username, o.total
  FROM orders o
  LEFT JOIN users u ON o.user_id = u.id;
  ```

  **Note:** Most developers prefer LEFT JOIN over RIGHT JOIN for readability.

</details>

<details>
  <summary>FULL OUTER JOIN</summary>
  <br/>

  **FULL OUTER JOIN** returns all rows from both tables. If no match, NULL values for missing side.

  **Basic FULL OUTER JOIN:**

  ```sql
  -- Get all users and all orders (including unmatched)
  SELECT 
      u.id AS user_id,
      u.username,
      o.id AS order_id,
      o.total
  FROM users u
  FULL OUTER JOIN orders o ON u.id = o.user_id;
  
  -- Result:
  -- user_id | username | order_id | total
  -- 1       | john     | 101      | 99.99
  -- 1       | john     | 102      | 149.99
  -- 2       | jane     | 103      | 79.99
  -- 3       | bob      | NULL     | NULL     (no orders)
  -- 4       | alice    | NULL     | NULL     (no orders)
  -- NULL    | NULL     | 104      | 199.99   (no user)
  ```

  **Find unmatched rows from both tables:**

  ```sql
  -- Users without orders OR orders without users
  SELECT 
      u.id AS user_id,
      u.username,
      o.id AS order_id,
      o.total
  FROM users u
  FULL OUTER JOIN orders o ON u.id = o.user_id
  WHERE u.id IS NULL OR o.id IS NULL;
  
  -- Result:
  -- user_id | username | order_id | total
  -- 3       | bob      | NULL     | NULL
  -- 4       | alice    | NULL     | NULL
  -- NULL    | NULL     | 104      | 199.99
  ```

  **Emulate FULL OUTER JOIN (MySQL doesn't support it):**

  ```sql
  -- MySQL workaround using UNION
  SELECT u.id AS user_id, u.username, o.id AS order_id, o.total
  FROM users u
  LEFT JOIN orders o ON u.id = o.user_id
  
  UNION
  
  SELECT u.id AS user_id, u.username, o.id AS order_id, o.total
  FROM users u
  RIGHT JOIN orders o ON u.id = o.user_id
  WHERE u.id IS NULL;
  ```

</details>

<details>
  <summary>CROSS JOIN</summary>
  <br/>

  **CROSS JOIN** returns the Cartesian product of both tables (all possible combinations).

  **Basic CROSS JOIN:**

  ```sql
  -- Sample tables
  CREATE TABLE colors (id INT, name VARCHAR(20));
  INSERT INTO colors VALUES (1, 'Red'), (2, 'Blue'), (3, 'Green');
  
  CREATE TABLE sizes (id INT, name VARCHAR(20));
  INSERT INTO sizes VALUES (1, 'Small'), (2, 'Medium'), (3, 'Large');
  
  -- Get all color-size combinations
  SELECT 
      c.name AS color,
      s.name AS size
  FROM colors c
  CROSS JOIN sizes s;
  
  -- Result (9 rows = 3 colors × 3 sizes):
  -- color | size
  -- Red   | Small
  -- Red   | Medium
  -- Red   | Large
  -- Blue  | Small
  -- Blue  | Medium
  -- Blue  | Large
  -- Green | Small
  -- Green | Medium
  -- Green | Large
  ```

  **Alternative syntax:**

  ```sql
  -- Implicit CROSS JOIN
  SELECT c.name AS color, s.name AS size
  FROM colors c, sizes s;
  ```

  **CROSS JOIN with WHERE (becomes INNER JOIN):**

  ```sql
  -- This is effectively an INNER JOIN
  SELECT c.name, s.name
  FROM colors c
  CROSS JOIN sizes s
  WHERE c.id = s.id;
  ```

  **Use cases:**
  + Generate all combinations (product variants, test cases)
  + Create calendar tables (dates × time slots)
  + Generate report templates

</details>

<details>
  <summary>SELF JOIN</summary>
  <br/>

  **SELF JOIN** joins a table to itself.

  **Employee hierarchy example:**

  ```sql
  -- employees table
  CREATE TABLE employees (
      id INT PRIMARY KEY,
      name VARCHAR(50),
      manager_id INT
  );
  
  INSERT INTO employees VALUES
      (1, 'Alice', NULL),      -- CEO (no manager)
      (2, 'Bob', 1),           -- Reports to Alice
      (3, 'Charlie', 1),       -- Reports to Alice
      (4, 'David', 2),         -- Reports to Bob
      (5, 'Eve', 2);           -- Reports to Bob
  
  -- Get employees with their managers
  SELECT 
      e.name AS employee,
      m.name AS manager
  FROM employees e
  LEFT JOIN employees m ON e.manager_id = m.id;
  
  -- Result:
  -- employee | manager
  -- Alice    | NULL
  -- Bob      | Alice
  -- Charlie  | Alice
  -- David    | Bob
  -- Eve      | Bob
  ```

  **Find employees without managers:**

  ```sql
  SELECT name
  FROM employees
  WHERE manager_id IS NULL;
  ```

  **Find employees in same department:**

  ```sql
  -- Find pairs of employees with same manager
  SELECT 
      e1.name AS employee1,
      e2.name AS employee2,
      m.name AS manager
  FROM employees e1
  INNER JOIN employees e2 ON e1.manager_id = e2.manager_id
  INNER JOIN employees m ON e1.manager_id = m.id
  WHERE e1.id < e2.id;  -- Avoid duplicates
  ```

</details>

<details>
  <summary>Join Performance Tips</summary>
  <br/>

  **Optimize join performance:**

  ```sql
  -- ✅ DO: Create indexes on join columns
  CREATE INDEX idx_orders_user_id ON orders(user_id);
  CREATE INDEX idx_order_items_order_id ON order_items(order_id);
  
  -- ✅ DO: Use INNER JOIN when possible (faster than OUTER JOIN)
  SELECT u.username, o.total
  FROM users u
  INNER JOIN orders o ON u.id = o.user_id;
  
  -- ✅ DO: Filter early with WHERE in ON clause
  SELECT u.username, o.total
  FROM users u
  LEFT JOIN orders o ON u.id = o.user_id AND o.status = 'completed';
  
  -- ❌ DON'T: Filter late with WHERE (slower)
  SELECT u.username, o.total
  FROM users u
  LEFT JOIN orders o ON u.id = o.user_id
  WHERE o.status = 'completed';
  
  -- ✅ DO: Use EXPLAIN to analyze query performance
  EXPLAIN SELECT u.username, o.total
  FROM users u
  INNER JOIN orders o ON u.id = o.user_id;
  
  -- ✅ DO: Join on indexed columns
  -- Good: Join on primary key
  FROM users u
  INNER JOIN orders o ON u.id = o.user_id;
  
  -- Bad: Join on non-indexed column
  FROM users u
  INNER JOIN orders o ON u.email = o.user_email;
  
  -- ✅ DO: Use appropriate join type
  -- INNER JOIN - Only matching rows (fastest)
  -- LEFT JOIN - All left rows + matching right rows
  -- FULL OUTER JOIN - All rows from both tables (slowest)
  
  -- ✅ DO: Limit result set
  SELECT u.username, o.total
  FROM users u
  INNER JOIN orders o ON u.id = o.user_id
  ORDER BY o.created_at DESC
  LIMIT 100;
  ```

</details>

<details>
  <summary>Best Practices</summary>
  <br/>

  ```sql
  -- ✅ DO: Use explicit JOIN syntax (not implicit)
  SELECT u.username, o.total
  FROM users u
  INNER JOIN orders o ON u.id = o.user_id;
  
  -- ❌ DON'T: Use implicit join (old style)
  SELECT u.username, o.total
  FROM users u, orders o
  WHERE u.id = o.user_id;
  
  -- ✅ DO: Use table aliases for readability
  SELECT u.username, o.total
  FROM users u
  INNER JOIN orders o ON u.id = o.user_id;
  
  -- ✅ DO: Specify join columns explicitly
  FROM users u
  INNER JOIN orders o ON u.id = o.user_id;
  
  -- ❌ DON'T: Use NATURAL JOIN (implicit column matching)
  FROM users u
  NATURAL JOIN orders o;  -- Matches all columns with same name
  
  -- ✅ DO: Use LEFT JOIN to include all rows from left table
  SELECT u.username, COUNT(o.id) AS order_count
  FROM users u
  LEFT JOIN orders o ON u.id = o.user_id
  GROUP BY u.id, u.username;
  
  -- ✅ DO: Use INNER JOIN when you only want matching rows
  SELECT u.username, o.total
  FROM users u
  INNER JOIN orders o ON u.id = o.user_id;
  
  -- ✅ DO: Create indexes on foreign key columns
  CREATE INDEX idx_orders_user_id ON orders(user_id);
  
  -- ✅ DO: Use COALESCE for NULL handling in LEFT JOIN
  SELECT 
      u.username,
      COALESCE(SUM(o.total), 0) AS total_spent
  FROM users u
  LEFT JOIN orders o ON u.id = o.user_id
  GROUP BY u.id, u.username;
  
  -- ✅ DO: Filter in ON clause for LEFT JOIN
  FROM users u
  LEFT JOIN orders o ON u.id = o.user_id AND o.status = 'completed';
  
  -- ❌ DON'T: Filter in WHERE clause for LEFT JOIN (changes behavior)
  FROM users u
  LEFT JOIN orders o ON u.id = o.user_id
  WHERE o.status = 'completed';  -- Excludes users without orders
  
  -- ✅ DO: Use appropriate join type
  -- INNER JOIN - Only matching rows
  -- LEFT JOIN - All left + matching right
  -- RIGHT JOIN - All right + matching left (prefer LEFT JOIN)
  -- FULL OUTER JOIN - All rows from both tables
  -- CROSS JOIN - Cartesian product
  
  -- ✅ DO: Order joins from smallest to largest table
  FROM small_table s
  INNER JOIN large_table l ON s.id = l.small_id;
  
  -- ✅ DO: Use EXISTS instead of JOIN for existence checks
  SELECT u.username
  FROM users u
  WHERE EXISTS (
      SELECT 1 FROM orders o WHERE o.user_id = u.id
  );
  
  -- ❌ DON'T: Use JOIN just to check existence
  SELECT DISTINCT u.username
  FROM users u
  INNER JOIN orders o ON u.id = o.user_id;
  ```

  **Summary:**
  + **INNER JOIN** - Returns only matching rows from both tables
  + **LEFT JOIN** - Returns all rows from left table + matching rows from right
  + **RIGHT JOIN** - Returns all rows from right table + matching rows from left
  + **FULL OUTER JOIN** - Returns all rows from both tables
  + **CROSS JOIN** - Returns Cartesian product (all combinations)
  + **SELF JOIN** - Joins table to itself
  + Use **explicit JOIN syntax** (not implicit)
  + Use **table aliases** for readability
  + Create **indexes on join columns**
  + Use **INNER JOIN** when possible (faster)
  + Use **LEFT JOIN** to include all rows from left table
  + Filter in **ON clause** for LEFT JOIN (not WHERE)
  + Use **COALESCE** for NULL handling
  + Use **EXPLAIN** to analyze query performance
  + Use **appropriate join type** for your use case

</details>

## Subqueries

<details>
  <summary>What are Subqueries?</summary>
  <br/>

  **Subqueries** (nested queries) are queries embedded within another query.

  **Types of subqueries:**
  + **Scalar subquery** - Returns single value (one row, one column)
  + **Row subquery** - Returns single row (multiple columns)
  + **Column subquery** - Returns single column (multiple rows)
  + **Table subquery** - Returns multiple rows and columns

  **Subquery locations:**
  + **SELECT clause** - As a column value
  + **FROM clause** - As a derived table
  + **WHERE clause** - For filtering
  + **HAVING clause** - For filtering groups

  **Basic example:**

  ```sql
  -- Find users with above-average age
  SELECT username, age
  FROM users
  WHERE age > (SELECT AVG(age) FROM users);
  ```

</details>

<details>
  <summary>Subqueries in WHERE Clause</summary>
  <br/>

  **Subqueries in WHERE** are used for filtering.

  **Scalar subquery (single value):**

  ```sql
  -- Users older than average age
  SELECT username, age
  FROM users
  WHERE age > (SELECT AVG(age) FROM users);
  
  -- Products more expensive than average
  SELECT name, price
  FROM products
  WHERE price > (SELECT AVG(price) FROM products);
  
  -- Orders with total greater than user's average
  SELECT id, total
  FROM orders
  WHERE total > (
      SELECT AVG(total) 
      FROM orders 
      WHERE user_id = orders.user_id
  );
  ```

  **IN operator (multiple values):**

  ```sql
  -- Users who have placed orders
  SELECT username
  FROM users
  WHERE id IN (SELECT DISTINCT user_id FROM orders);
  
  -- Products in specific categories
  SELECT name, price
  FROM products
  WHERE category_id IN (
      SELECT id 
      FROM categories 
      WHERE name IN ('Electronics', 'Books')
  );
  
  -- Users who ordered specific product
  SELECT username
  FROM users
  WHERE id IN (
      SELECT user_id 
      FROM orders o
      JOIN order_items oi ON o.id = oi.order_id
      WHERE oi.product_id = 123
  );
  ```

  **NOT IN operator:**

  ```sql
  -- Users who have NOT placed orders
  SELECT username
  FROM users
  WHERE id NOT IN (SELECT user_id FROM orders WHERE user_id IS NOT NULL);
  
  -- Products never ordered
  SELECT name
  FROM products
  WHERE id NOT IN (SELECT product_id FROM order_items);
  ```

  **Comparison operators:**

  ```sql
  -- Users with age greater than ANY user in USA
  SELECT username, age
  FROM users
  WHERE age > ANY (SELECT age FROM users WHERE country = 'USA');
  
  -- Users with age greater than ALL users in USA
  SELECT username, age
  FROM users
  WHERE age > ALL (SELECT age FROM users WHERE country = 'USA');
  
  -- Products cheaper than most expensive product in category
  SELECT name, price
  FROM products p1
  WHERE price < (
      SELECT MAX(price) 
      FROM products p2 
      WHERE p2.category_id = p1.category_id
  );
  ```

</details>

<details>
  <summary>EXISTS and NOT EXISTS</summary>
  <br/>

  **EXISTS** checks if subquery returns any rows (more efficient than IN for large datasets).

  **EXISTS:**

  ```sql
  -- Users who have placed orders
  SELECT username
  FROM users u
  WHERE EXISTS (
      SELECT 1 
      FROM orders o 
      WHERE o.user_id = u.id
  );
  
  -- Products that have been ordered
  SELECT name
  FROM products p
  WHERE EXISTS (
      SELECT 1 
      FROM order_items oi 
      WHERE oi.product_id = p.id
  );
  
  -- Users who ordered in last 30 days
  SELECT username
  FROM users u
  WHERE EXISTS (
      SELECT 1 
      FROM orders o 
      WHERE o.user_id = u.id 
      AND o.created_at > CURRENT_DATE - INTERVAL '30 days'
  );
  ```

  **NOT EXISTS:**

  ```sql
  -- Users who have NOT placed orders
  SELECT username
  FROM users u
  WHERE NOT EXISTS (
      SELECT 1 
      FROM orders o 
      WHERE o.user_id = u.id
  );
  
  -- Products never ordered
  SELECT name
  FROM products p
  WHERE NOT EXISTS (
      SELECT 1 
      FROM order_items oi 
      WHERE oi.product_id = p.id
  );
  
  -- Categories with no products
  SELECT name
  FROM categories c
  WHERE NOT EXISTS (
      SELECT 1 
      FROM products p 
      WHERE p.category_id = c.id
  );
  ```

  **EXISTS vs IN:**

  ```sql
  -- EXISTS (better for large datasets)
  SELECT username
  FROM users u
  WHERE EXISTS (
      SELECT 1 FROM orders o WHERE o.user_id = u.id
  );
  
  -- IN (simpler syntax, but slower for large datasets)
  SELECT username
  FROM users
  WHERE id IN (SELECT user_id FROM orders);
  ```

</details>

<details>
  <summary>Correlated Subqueries</summary>
  <br/>

  **Correlated subqueries** reference columns from outer query (executed once per outer row).

  **Basic correlated subquery:**

  ```sql
  -- Users with above-average orders for their country
  SELECT username, country
  FROM users u
  WHERE (
      SELECT COUNT(*) 
      FROM orders o 
      WHERE o.user_id = u.id
  ) > (
      SELECT AVG(order_count)
      FROM (
          SELECT COUNT(*) AS order_count
          FROM orders o2
          JOIN users u2 ON o2.user_id = u2.id
          WHERE u2.country = u.country
          GROUP BY u2.id
      ) AS country_avg
  );
  
  -- Products more expensive than category average
  SELECT name, price, category_id
  FROM products p1
  WHERE price > (
      SELECT AVG(price)
      FROM products p2
      WHERE p2.category_id = p1.category_id
  );
  ```

  **Correlated subquery in SELECT:**

  ```sql
  -- Get user with their order count
  SELECT 
      username,
      (SELECT COUNT(*) FROM orders o WHERE o.user_id = u.id) AS order_count
  FROM users u;
  
  -- Get product with category name
  SELECT 
      p.name,
      p.price,
      (SELECT c.name FROM categories c WHERE c.id = p.category_id) AS category_name
  FROM products p;
  ```

  **Correlated subquery with EXISTS:**

  ```sql
  -- Users who ordered specific product
  SELECT username
  FROM users u
  WHERE EXISTS (
      SELECT 1
      FROM orders o
      JOIN order_items oi ON o.id = oi.order_id
      WHERE o.user_id = u.id AND oi.product_id = 123
  );
  ```

  **Performance note:** Correlated subqueries can be slow (executed for each outer row). Consider using JOINs instead.

</details>

<details>
  <summary>Non-Correlated Subqueries</summary>
  <br/>

  **Non-correlated subqueries** are independent of outer query (executed once).

  **Basic non-correlated subquery:**

  ```sql
  -- Users older than average age
  SELECT username, age
  FROM users
  WHERE age > (SELECT AVG(age) FROM users);
  
  -- Products in top 3 categories by product count
  SELECT name, category_id
  FROM products
  WHERE category_id IN (
      SELECT category_id
      FROM products
      GROUP BY category_id
      ORDER BY COUNT(*) DESC
      LIMIT 3
  );
  ```

  **Non-correlated subquery in SELECT:**

  ```sql
  -- Get average age alongside user data
  SELECT 
      username,
      age,
      (SELECT AVG(age) FROM users) AS avg_age,
      age - (SELECT AVG(age) FROM users) AS age_diff
  FROM users;
  ```

  **Non-correlated subquery in FROM:**

  ```sql
  -- Get users with order statistics
  SELECT 
      u.username,
      os.order_count,
      os.total_spent
  FROM users u
  JOIN (
      SELECT 
          user_id,
          COUNT(*) AS order_count,
          SUM(total) AS total_spent
      FROM orders
      GROUP BY user_id
  ) os ON u.id = os.user_id;
  ```

</details>

<details>
  <summary>Subqueries in SELECT Clause</summary>
  <br/>

  **Subqueries in SELECT** return values for each row.

  **Scalar subquery:**

  ```sql
  -- Get user with order count
  SELECT 
      username,
      email,
      (SELECT COUNT(*) FROM orders WHERE user_id = users.id) AS order_count
  FROM users;
  
  -- Get product with category name
  SELECT 
      name,
      price,
      (SELECT name FROM categories WHERE id = products.category_id) AS category
  FROM products;
  
  -- Get order with user name
  SELECT 
      id,
      total,
      (SELECT username FROM users WHERE id = orders.user_id) AS customer
  FROM orders;
  ```

  **Multiple subqueries:**

  ```sql
  -- Get user with multiple statistics
  SELECT 
      username,
      (SELECT COUNT(*) FROM orders WHERE user_id = users.id) AS order_count,
      (SELECT SUM(total) FROM orders WHERE user_id = users.id) AS total_spent,
      (SELECT MAX(created_at) FROM orders WHERE user_id = users.id) AS last_order
  FROM users;
  ```

  **Note:** Multiple subqueries in SELECT can be slow. Consider using JOINs or window functions.

</details>

<details>
  <summary>Subqueries in FROM Clause</summary>
  <br/>

  **Subqueries in FROM** create derived tables (inline views).

  **Basic derived table:**

  ```sql
  -- Get users with order statistics
  SELECT 
      u.username,
      os.order_count,
      os.total_spent
  FROM users u
  JOIN (
      SELECT 
          user_id,
          COUNT(*) AS order_count,
          SUM(total) AS total_spent
      FROM orders
      GROUP BY user_id
  ) os ON u.id = os.user_id;
  ```

  **Multiple derived tables:**

  ```sql
  -- Compare user orders with category averages
  SELECT 
      u.username,
      uo.order_count,
      ca.avg_orders
  FROM users u
  JOIN (
      SELECT user_id, COUNT(*) AS order_count
      FROM orders
      GROUP BY user_id
  ) uo ON u.id = uo.user_id
  CROSS JOIN (
      SELECT AVG(order_count) AS avg_orders
      FROM (
          SELECT COUNT(*) AS order_count
          FROM orders
          GROUP BY user_id
      ) AS counts
  ) ca;
  ```

  **Derived table with WHERE:**

  ```sql
  -- Get top 10 customers by spending
  SELECT username, total_spent
  FROM (
      SELECT 
          u.username,
          SUM(o.total) AS total_spent
      FROM users u
      JOIN orders o ON u.id = o.user_id
      GROUP BY u.id, u.username
      ORDER BY total_spent DESC
      LIMIT 10
  ) AS top_customers;
  ```

  **Common Table Expression (CTE) - cleaner alternative:**

  ```sql
  -- Same as above, but more readable
  WITH top_customers AS (
      SELECT 
          u.username,
          SUM(o.total) AS total_spent
      FROM users u
      JOIN orders o ON u.id = o.user_id
      GROUP BY u.id, u.username
      ORDER BY total_spent DESC
      LIMIT 10
  )
  SELECT username, total_spent
  FROM top_customers;
  ```

</details>

<details>
  <summary>Common Table Expressions (CTE)</summary>
  <br/>

  **CTEs** (WITH clause) create temporary named result sets (more readable than subqueries).

  **Basic CTE:**

  ```sql
  -- Calculate order statistics
  WITH order_stats AS (
      SELECT 
          user_id,
          COUNT(*) AS order_count,
          SUM(total) AS total_spent,
          AVG(total) AS avg_order
      FROM orders
      GROUP BY user_id
  )
  SELECT 
      u.username,
      os.order_count,
      os.total_spent,
      os.avg_order
  FROM users u
  JOIN order_stats os ON u.id = os.user_id;
  ```

  **Multiple CTEs:**

  ```sql
  -- Compare user spending with averages
  WITH user_spending AS (
      SELECT 
          user_id,
          SUM(total) AS total_spent
      FROM orders
      GROUP BY user_id
  ),
  spending_stats AS (
      SELECT 
          AVG(total_spent) AS avg_spent,
          MAX(total_spent) AS max_spent
      FROM user_spending
  )
  SELECT 
      u.username,
      us.total_spent,
      ss.avg_spent,
      us.total_spent - ss.avg_spent AS diff_from_avg
  FROM users u
  JOIN user_spending us ON u.id = us.user_id
  CROSS JOIN spending_stats ss
  WHERE us.total_spent > ss.avg_spent;
  ```

  **Recursive CTE:**

  ```sql
  -- Employee hierarchy (recursive)
  WITH RECURSIVE employee_hierarchy AS (
      -- Base case: top-level employees
      SELECT id, name, manager_id, 1 AS level
      FROM employees
      WHERE manager_id IS NULL
      
      UNION ALL
      
      -- Recursive case: employees reporting to previous level
      SELECT e.id, e.name, e.manager_id, eh.level + 1
      FROM employees e
      JOIN employee_hierarchy eh ON e.manager_id = eh.id
  )
  SELECT * FROM employee_hierarchy
  ORDER BY level, name;
  ```

  **CTE vs Subquery:**

  ```sql
  -- CTE (more readable)
  WITH high_value_orders AS (
      SELECT user_id, total
      FROM orders
      WHERE total > 1000
  )
  SELECT u.username, COUNT(*) AS high_value_count
  FROM users u
  JOIN high_value_orders hvo ON u.id = hvo.user_id
  GROUP BY u.id, u.username;
  
  -- Subquery (less readable)
  SELECT u.username, COUNT(*) AS high_value_count
  FROM users u
  JOIN (
      SELECT user_id, total
      FROM orders
      WHERE total > 1000
  ) hvo ON u.id = hvo.user_id
  GROUP BY u.id, u.username;
  ```

</details>

<details>
  <summary>Best Practices</summary>
  <br/>

  ```sql
  -- ✅ DO: Use EXISTS instead of IN for large datasets
  SELECT username
  FROM users u
  WHERE EXISTS (SELECT 1 FROM orders o WHERE o.user_id = u.id);
  
  -- ❌ DON'T: Use IN with large subquery results
  SELECT username
  FROM users
  WHERE id IN (SELECT user_id FROM orders);  -- Slower for large datasets
  
  -- ✅ DO: Use CTEs for complex queries (more readable)
  WITH order_stats AS (
      SELECT user_id, COUNT(*) AS order_count
      FROM orders
      GROUP BY user_id
  )
  SELECT u.username, os.order_count
  FROM users u
  JOIN order_stats os ON u.id = os.user_id;
  
  -- ✅ DO: Use JOINs instead of correlated subqueries when possible
  -- JOIN (faster)
  SELECT u.username, COUNT(o.id) AS order_count
  FROM users u
  LEFT JOIN orders o ON u.id = o.user_id
  GROUP BY u.id, u.username;
  
  -- Correlated subquery (slower)
  SELECT 
      username,
      (SELECT COUNT(*) FROM orders WHERE user_id = users.id) AS order_count
  FROM users;
  
  -- ✅ DO: Use NOT EXISTS instead of NOT IN with NULLs
  SELECT username
  FROM users u
  WHERE NOT EXISTS (SELECT 1 FROM orders o WHERE o.user_id = u.id);
  
  -- ❌ DON'T: Use NOT IN with nullable columns (unexpected results)
  SELECT username
  FROM users
  WHERE id NOT IN (SELECT user_id FROM orders);  -- Fails if user_id has NULLs
  
  -- ✅ DO: Use scalar subqueries for single values
  SELECT username, age
  FROM users
  WHERE age > (SELECT AVG(age) FROM users);
  
  -- ✅ DO: Use derived tables for complex aggregations
  SELECT username, order_count
  FROM (
      SELECT u.username, COUNT(o.id) AS order_count
      FROM users u
      LEFT JOIN orders o ON u.id = o.user_id
      GROUP BY u.id, u.username
  ) AS user_orders
  WHERE order_count > 5;
  
  -- ✅ DO: Limit subquery results when possible
  SELECT username
  FROM users
  WHERE id IN (
      SELECT user_id 
      FROM orders 
      ORDER BY created_at DESC 
      LIMIT 100
  );
  
  -- ✅ DO: Use EXPLAIN to analyze subquery performance
  EXPLAIN SELECT username
  FROM users
  WHERE id IN (SELECT user_id FROM orders);
  
  -- ✅ DO: Consider materialized views for frequently used subqueries
  CREATE MATERIALIZED VIEW user_order_stats AS
  SELECT 
      user_id,
      COUNT(*) AS order_count,
      SUM(total) AS total_spent
  FROM orders
  GROUP BY user_id;
  
  -- ❌ DON'T: Use SELECT * in subqueries
  SELECT username
  FROM users
  WHERE id IN (SELECT user_id FROM orders);  -- Good
  
  SELECT username
  FROM users
  WHERE id IN (SELECT * FROM orders);  -- Bad (returns all columns)
  
  -- ✅ DO: Use appropriate subquery type
  -- Scalar: Single value
  -- Column: Multiple rows, single column (IN, EXISTS)
  -- Row: Single row, multiple columns
  -- Table: Multiple rows and columns (FROM clause)
  ```

  **Summary:**
  + **Subqueries** are queries nested within another query
  + **Correlated subqueries** reference outer query (slower, executed per row)
  + **Non-correlated subqueries** are independent (faster, executed once)
  + Use **EXISTS** instead of IN for large datasets
  + Use **NOT EXISTS** instead of NOT IN with nullable columns
  + Use **CTEs** (WITH clause) for complex queries (more readable)
  + Use **JOINs** instead of correlated subqueries when possible
  + Use **scalar subqueries** for single values
  + Use **derived tables** for complex aggregations
  + Use **EXPLAIN** to analyze subquery performance
  + Avoid **SELECT *** in subqueries
  + Consider **materialized views** for frequently used subqueries

</details>

## Aggregations

<details>
  <summary>What are Aggregate Functions?</summary>
  <br/>

  **Aggregate functions** perform calculations on multiple rows and return a single value.

  **Common aggregate functions:**
  + **COUNT()** - Count rows
  + **SUM()** - Sum values
  + **AVG()** - Average value
  + **MIN()** - Minimum value
  + **MAX()** - Maximum value

  **Basic example:**

  ```sql
  -- Count total users
  SELECT COUNT(*) FROM users;
  
  -- Average age
  SELECT AVG(age) FROM users;
  
  -- Total revenue
  SELECT SUM(total) FROM orders;
  ```

</details>

<details>
  <summary>COUNT Function</summary>
  <br/>

  **COUNT()** counts the number of rows.

  **COUNT variations:**

  ```sql
  -- Count all rows (including NULLs)
  SELECT COUNT(*) FROM users;
  
  -- Count non-NULL values in column
  SELECT COUNT(email) FROM users;
  
  -- Count distinct values
  SELECT COUNT(DISTINCT country) FROM users;
  
  -- Count with condition
  SELECT COUNT(*) FROM users WHERE age > 18;
  ```

  **COUNT with GROUP BY:**

  ```sql
  -- Count users per country
  SELECT country, COUNT(*) AS user_count
  FROM users
  GROUP BY country;
  
  -- Count orders per user
  SELECT user_id, COUNT(*) AS order_count
  FROM orders
  GROUP BY user_id;
  
  -- Count products per category
  SELECT category_id, COUNT(*) AS product_count
  FROM products
  GROUP BY category_id;
  ```

  **COUNT vs COUNT(column):**

  ```sql
  -- Sample data
  CREATE TABLE users (
      id INT,
      username VARCHAR(50),
      email VARCHAR(100)  -- Some emails are NULL
  );
  
  INSERT INTO users VALUES
      (1, 'john', 'john@example.com'),
      (2, 'jane', NULL),
      (3, 'bob', 'bob@example.com'),
      (4, 'alice', NULL);
  
  -- COUNT(*) - Counts all rows (4)
  SELECT COUNT(*) FROM users;  -- Result: 4
  
  -- COUNT(email) - Counts non-NULL emails (2)
  SELECT COUNT(email) FROM users;  -- Result: 2
  
  -- COUNT(DISTINCT email) - Counts unique non-NULL emails (2)
  SELECT COUNT(DISTINCT email) FROM users;  -- Result: 2
  ```

</details>

<details>
  <summary>SUM Function</summary>
  <br/>

  **SUM()** calculates the sum of numeric values.

  **Basic SUM:**

  ```sql
  -- Total revenue
  SELECT SUM(total) FROM orders;
  
  -- Total quantity sold
  SELECT SUM(quantity) FROM order_items;
  
  -- Total salary
  SELECT SUM(salary) FROM employees;
  ```

  **SUM with GROUP BY:**

  ```sql
  -- Total revenue per user
  SELECT user_id, SUM(total) AS total_spent
  FROM orders
  GROUP BY user_id;
  
  -- Total sales per product
  SELECT product_id, SUM(quantity) AS total_sold
  FROM order_items
  GROUP BY product_id;
  
  -- Total revenue per month
  SELECT 
      DATE_TRUNC('month', created_at) AS month,
      SUM(total) AS monthly_revenue
  FROM orders
  GROUP BY DATE_TRUNC('month', created_at)
  ORDER BY month;
  ```

  **SUM with calculations:**

  ```sql
  -- Total revenue (quantity * price)
  SELECT SUM(quantity * price) AS total_revenue
  FROM order_items;
  
  -- Total with tax
  SELECT SUM(total * 1.1) AS total_with_tax
  FROM orders;
  
  -- Total profit (revenue - cost)
  SELECT SUM(price - cost) AS total_profit
  FROM products;
  ```

  **SUM with CASE:**

  ```sql
  -- Sum completed orders only
  SELECT SUM(
      CASE 
          WHEN status = 'completed' THEN total 
          ELSE 0 
      END
  ) AS completed_revenue
  FROM orders;
  
  -- Sum by status
  SELECT 
      SUM(CASE WHEN status = 'completed' THEN total ELSE 0 END) AS completed,
      SUM(CASE WHEN status = 'pending' THEN total ELSE 0 END) AS pending,
      SUM(CASE WHEN status = 'cancelled' THEN total ELSE 0 END) AS cancelled
  FROM orders;
  ```

</details>

<details>
  <summary>AVG Function</summary>
  <br/>

  **AVG()** calculates the average of numeric values.

  **Basic AVG:**

  ```sql
  -- Average age
  SELECT AVG(age) FROM users;
  
  -- Average order total
  SELECT AVG(total) FROM orders;
  
  -- Average product price
  SELECT AVG(price) FROM products;
  ```

  **AVG with GROUP BY:**

  ```sql
  -- Average age per country
  SELECT country, AVG(age) AS avg_age
  FROM users
  GROUP BY country;
  
  -- Average order total per user
  SELECT user_id, AVG(total) AS avg_order
  FROM orders
  GROUP BY user_id;
  
  -- Average price per category
  SELECT category_id, AVG(price) AS avg_price
  FROM products
  GROUP BY category_id;
  ```

  **AVG with ROUND:**

  ```sql
  -- Round to 2 decimal places
  SELECT ROUND(AVG(price), 2) AS avg_price
  FROM products;
  
  -- Round to integer
  SELECT ROUND(AVG(age)) AS avg_age
  FROM users;
  ```

  **AVG with filtering:**

  ```sql
  -- Average of completed orders only
  SELECT AVG(total) AS avg_completed_order
  FROM orders
  WHERE status = 'completed';
  
  -- Average age of adults
  SELECT AVG(age) AS avg_adult_age
  FROM users
  WHERE age >= 18;
  ```

</details>

<details>
  <summary>MIN and MAX Functions</summary>
  <br/>

  **MIN()** finds the minimum value, **MAX()** finds the maximum value.

  **Basic MIN/MAX:**

  ```sql
  -- Youngest and oldest user
  SELECT MIN(age) AS youngest, MAX(age) AS oldest
  FROM users;
  
  -- Cheapest and most expensive product
  SELECT MIN(price) AS cheapest, MAX(price) AS most_expensive
  FROM products;
  
  -- First and last order date
  SELECT MIN(created_at) AS first_order, MAX(created_at) AS last_order
  FROM orders;
  ```

  **MIN/MAX with GROUP BY:**

  ```sql
  -- Min/max age per country
  SELECT 
      country,
      MIN(age) AS youngest,
      MAX(age) AS oldest
  FROM users
  GROUP BY country;
  
  -- Min/max price per category
  SELECT 
      category_id,
      MIN(price) AS cheapest,
      MAX(price) AS most_expensive
  FROM products
  GROUP BY category_id;
  ```

  **MIN/MAX with dates:**

  ```sql
  -- First and last login per user
  SELECT 
      user_id,
      MIN(login_at) AS first_login,
      MAX(login_at) AS last_login
  FROM user_logins
  GROUP BY user_id;
  
  -- Earliest and latest order per user
  SELECT 
      user_id,
      MIN(created_at) AS first_order,
      MAX(created_at) AS last_order
  FROM orders
  GROUP BY user_id;
  ```

</details>

<details>
  <summary>GROUP BY Clause</summary>
  <br/>

  **GROUP BY** groups rows with same values into summary rows.

  **Basic GROUP BY:**

  ```sql
  -- Count users per country
  SELECT country, COUNT(*) AS user_count
  FROM users
  GROUP BY country;
  
  -- Total revenue per user
  SELECT user_id, SUM(total) AS total_spent
  FROM orders
  GROUP BY user_id;
  
  -- Average price per category
  SELECT category_id, AVG(price) AS avg_price
  FROM products
  GROUP BY category_id;
  ```

  **GROUP BY multiple columns:**

  ```sql
  -- Count users per country and city
  SELECT country, city, COUNT(*) AS user_count
  FROM users
  GROUP BY country, city;
  
  -- Revenue per user and status
  SELECT user_id, status, SUM(total) AS revenue
  FROM orders
  GROUP BY user_id, status;
  
  -- Product count per category and brand
  SELECT category_id, brand, COUNT(*) AS product_count
  FROM products
  GROUP BY category_id, brand;
  ```

  **GROUP BY with multiple aggregates:**

  ```sql
  -- User statistics per country
  SELECT 
      country,
      COUNT(*) AS user_count,
      AVG(age) AS avg_age,
      MIN(age) AS youngest,
      MAX(age) AS oldest
  FROM users
  GROUP BY country;
  
  -- Order statistics per user
  SELECT 
      user_id,
      COUNT(*) AS order_count,
      SUM(total) AS total_spent,
      AVG(total) AS avg_order,
      MIN(total) AS min_order,
      MAX(total) AS max_order
  FROM orders
  GROUP BY user_id;
  ```

  **GROUP BY with ORDER BY:**

  ```sql
  -- Top 10 countries by user count
  SELECT country, COUNT(*) AS user_count
  FROM users
  GROUP BY country
  ORDER BY user_count DESC
  LIMIT 10;
  
  -- Top spenders
  SELECT user_id, SUM(total) AS total_spent
  FROM orders
  GROUP BY user_id
  ORDER BY total_spent DESC
  LIMIT 10;
  ```

  **GROUP BY with dates:**

  ```sql
  -- Orders per day
  SELECT 
      DATE(created_at) AS order_date,
      COUNT(*) AS order_count,
      SUM(total) AS daily_revenue
  FROM orders
  GROUP BY DATE(created_at)
  ORDER BY order_date;
  
  -- Orders per month
  SELECT 
      DATE_TRUNC('month', created_at) AS month,
      COUNT(*) AS order_count,
      SUM(total) AS monthly_revenue
  FROM orders
  GROUP BY DATE_TRUNC('month', created_at)
  ORDER BY month;
  
  -- Orders per year
  SELECT 
      EXTRACT(YEAR FROM created_at) AS year,
      COUNT(*) AS order_count,
      SUM(total) AS yearly_revenue
  FROM orders
  GROUP BY EXTRACT(YEAR FROM created_at)
  ORDER BY year;
  ```

</details>

<details>
  <summary>HAVING Clause</summary>
  <br/>

  **HAVING** filters groups after aggregation (WHERE filters before aggregation).

  **Basic HAVING:**

  ```sql
  -- Countries with more than 100 users
  SELECT country, COUNT(*) AS user_count
  FROM users
  GROUP BY country
  HAVING COUNT(*) > 100;
  
  -- Users who spent more than $1000
  SELECT user_id, SUM(total) AS total_spent
  FROM orders
  GROUP BY user_id
  HAVING SUM(total) > 1000;
  
  -- Categories with average price > $50
  SELECT category_id, AVG(price) AS avg_price
  FROM products
  GROUP BY category_id
  HAVING AVG(price) > 50;
  ```

  **HAVING with multiple conditions:**

  ```sql
  -- Countries with 100+ users and avg age > 30
  SELECT country, COUNT(*) AS user_count, AVG(age) AS avg_age
  FROM users
  GROUP BY country
  HAVING COUNT(*) > 100 AND AVG(age) > 30;
  
  -- Users with 5+ orders and total spent > $500
  SELECT user_id, COUNT(*) AS order_count, SUM(total) AS total_spent
  FROM orders
  GROUP BY user_id
  HAVING COUNT(*) >= 5 AND SUM(total) > 500;
  ```

  **WHERE vs HAVING:**

  ```sql
  -- WHERE filters rows BEFORE grouping
  SELECT country, COUNT(*) AS user_count
  FROM users
  WHERE age > 18  -- Filter individual rows first
  GROUP BY country
  HAVING COUNT(*) > 50;  -- Then filter groups
  
  -- Execution order:
  -- 1. WHERE filters rows (age > 18)
  -- 2. GROUP BY groups remaining rows
  -- 3. HAVING filters groups (count > 50)
  ```

  **HAVING with ORDER BY:**

  ```sql
  -- Top 10 countries by user count (min 100 users)
  SELECT country, COUNT(*) AS user_count
  FROM users
  GROUP BY country
  HAVING COUNT(*) > 100
  ORDER BY user_count DESC
  LIMIT 10;
  ```

  **HAVING with aggregate aliases:**

  ```sql
  -- Some databases allow using aliases in HAVING
  SELECT user_id, SUM(total) AS total_spent
  FROM orders
  GROUP BY user_id
  HAVING total_spent > 1000  -- PostgreSQL, MySQL allow this
  ORDER BY total_spent DESC;
  
  -- Standard SQL (works everywhere)
  SELECT user_id, SUM(total) AS total_spent
  FROM orders
  GROUP BY user_id
  HAVING SUM(total) > 1000
  ORDER BY total_spent DESC;
  ```

</details>

<details>
  <summary>Advanced Aggregations</summary>
  <br/>

  **DISTINCT with aggregates:**

  ```sql
  -- Count distinct countries
  SELECT COUNT(DISTINCT country) AS country_count
  FROM users;
  
  -- Count distinct users who ordered
  SELECT COUNT(DISTINCT user_id) AS customer_count
  FROM orders;
  
  -- Sum distinct order totals (removes duplicates)
  SELECT SUM(DISTINCT total) FROM orders;
  ```

  **Conditional aggregation:**

  ```sql
  -- Count by status using CASE
  SELECT 
      COUNT(CASE WHEN status = 'completed' THEN 1 END) AS completed,
      COUNT(CASE WHEN status = 'pending' THEN 1 END) AS pending,
      COUNT(CASE WHEN status = 'cancelled' THEN 1 END) AS cancelled
  FROM orders;
  
  -- Sum by status
  SELECT 
      SUM(CASE WHEN status = 'completed' THEN total ELSE 0 END) AS completed_revenue,
      SUM(CASE WHEN status = 'pending' THEN total ELSE 0 END) AS pending_revenue
  FROM orders;
  ```

  **FILTER clause (PostgreSQL):**

  ```sql
  -- Cleaner syntax for conditional aggregation
  SELECT 
      COUNT(*) FILTER (WHERE status = 'completed') AS completed,
      COUNT(*) FILTER (WHERE status = 'pending') AS pending,
      SUM(total) FILTER (WHERE status = 'completed') AS completed_revenue
  FROM orders;
  ```

  **String aggregation:**

  ```sql
  -- Concatenate values (PostgreSQL)
  SELECT 
      user_id,
      STRING_AGG(product_name, ', ') AS products
  FROM order_items
  GROUP BY user_id;
  
  -- MySQL
  SELECT 
      user_id,
      GROUP_CONCAT(product_name SEPARATOR ', ') AS products
  FROM order_items
  GROUP BY user_id;
  ```

  **Array aggregation (PostgreSQL):**

  ```sql
  -- Collect values into array
  SELECT 
      user_id,
      ARRAY_AGG(product_id) AS product_ids
  FROM order_items
  GROUP BY user_id;
  ```

</details>

<details>
  <summary>Best Practices</summary>
  <br/>

  ```sql
  -- ✅ DO: Use COUNT(*) to count rows (faster)
  SELECT COUNT(*) FROM users;
  
  -- ❌ DON'T: Use COUNT(column) unless checking for NULLs
  SELECT COUNT(id) FROM users;  -- Slower, checks for NULLs
  
  -- ✅ DO: Use WHERE to filter before aggregation
  SELECT country, COUNT(*) AS user_count
  FROM users
  WHERE age > 18  -- Filter first
  GROUP BY country;
  
  -- ✅ DO: Use HAVING to filter after aggregation
  SELECT country, COUNT(*) AS user_count
  FROM users
  GROUP BY country
  HAVING COUNT(*) > 100;  -- Filter groups
  
  -- ✅ DO: Include all non-aggregated columns in GROUP BY
  SELECT country, city, COUNT(*) AS user_count
  FROM users
  GROUP BY country, city;  -- Both country and city in GROUP BY
  
  -- ❌ DON'T: Select non-aggregated columns not in GROUP BY
  SELECT country, city, COUNT(*) AS user_count
  FROM users
  GROUP BY country;  -- ERROR: city not in GROUP BY
  
  -- ✅ DO: Use COALESCE for NULL handling
  SELECT 
      user_id,
      COALESCE(SUM(total), 0) AS total_spent
  FROM orders
  GROUP BY user_id;
  
  -- ✅ DO: Use ROUND for decimal precision
  SELECT ROUND(AVG(price), 2) AS avg_price
  FROM products;
  
  -- ✅ DO: Use meaningful aliases
  SELECT 
      country,
      COUNT(*) AS user_count,
      AVG(age) AS avg_age
  FROM users
  GROUP BY country;
  
  -- ✅ DO: Order by aggregate for rankings
  SELECT user_id, SUM(total) AS total_spent
  FROM orders
  GROUP BY user_id
  ORDER BY total_spent DESC
  LIMIT 10;
  
  -- ✅ DO: Use indexes on GROUP BY columns
  CREATE INDEX idx_users_country ON users(country);
  CREATE INDEX idx_orders_user_id ON orders(user_id);
  
  -- ✅ DO: Use DISTINCT only when necessary
  SELECT COUNT(DISTINCT country) FROM users;  -- Good
  SELECT DISTINCT country FROM users;  -- Good
  
  -- ❌ DON'T: Use DISTINCT unnecessarily (performance impact)
  SELECT DISTINCT * FROM users;  -- Usually not needed
  
  -- ✅ DO: Use conditional aggregation for pivoting
  SELECT 
      user_id,
      SUM(CASE WHEN status = 'completed' THEN total ELSE 0 END) AS completed,
      SUM(CASE WHEN status = 'pending' THEN total ELSE 0 END) AS pending
  FROM orders
  GROUP BY user_id;
  
  -- ✅ DO: Use EXPLAIN to analyze aggregation performance
  EXPLAIN SELECT country, COUNT(*) AS user_count
  FROM users
  GROUP BY country;
  ```

  **Summary:**
  + **COUNT()** - Count rows (use COUNT(*) for all rows)
  + **SUM()** - Sum numeric values
  + **AVG()** - Calculate average
  + **MIN()** - Find minimum value
  + **MAX()** - Find maximum value
  + **GROUP BY** - Group rows for aggregation
  + **HAVING** - Filter groups after aggregation
  + Use **WHERE** to filter before aggregation (faster)
  + Use **HAVING** to filter after aggregation
  + Include all **non-aggregated columns** in GROUP BY
  + Use **COALESCE** for NULL handling
  + Use **ROUND** for decimal precision
  + Use **indexes** on GROUP BY columns
  + Use **DISTINCT** only when necessary
  + Use **conditional aggregation** for pivoting
  + Use **EXPLAIN** to analyze performance

</details>

## Window Functions

<details>
  <summary>What are Window Functions?</summary>
  <br/>

  **Window functions** perform calculations across a set of rows related to the current row (without grouping).

  **Key differences from aggregate functions:**
  + **Aggregate functions** (GROUP BY) - Collapse rows into groups
  + **Window functions** - Keep all rows, add calculated columns

  **Common window functions:**
  + **ROW_NUMBER()** - Unique sequential number for each row
  + **RANK()** - Rank with gaps for ties
  + **DENSE_RANK()** - Rank without gaps for ties
  + **NTILE()** - Divide rows into N groups
  + **LAG()** - Access previous row value
  + **LEAD()** - Access next row value
  + **FIRST_VALUE()** - First value in window
  + **LAST_VALUE()** - Last value in window

  **Basic syntax:**

  ```sql
  SELECT 
      column1,
      column2,
      WINDOW_FUNCTION() OVER (
          PARTITION BY partition_column
          ORDER BY order_column
          ROWS/RANGE frame_specification
      ) AS result
  FROM table;
  ```

</details>

<details>
  <summary>ROW_NUMBER Function</summary>
  <br/>

  **ROW_NUMBER()** assigns a unique sequential number to each row.

  **Basic ROW_NUMBER:**

  ```sql
  -- Assign row numbers to all users
  SELECT 
      username,
      email,
      ROW_NUMBER() OVER (ORDER BY username) AS row_num
  FROM users;
  
  -- Result:
  -- username | email              | row_num
  -- alice    | alice@example.com  | 1
  -- bob      | bob@example.com    | 2
  -- charlie  | charlie@example.com| 3
  -- john     | john@example.com   | 4
  ```

  **ROW_NUMBER with PARTITION BY:**

  ```sql
  -- Row number per country
  SELECT 
      username,
      country,
      ROW_NUMBER() OVER (PARTITION BY country ORDER BY username) AS row_num
  FROM users;
  
  -- Result:
  -- username | country | row_num
  -- alice    | USA     | 1
  -- john     | USA     | 2
  -- bob      | UK      | 1
  -- charlie  | UK      | 2
  ```

  **Get top N per group:**

  ```sql
  -- Top 3 orders per user
  WITH ranked_orders AS (
      SELECT 
          user_id,
          id AS order_id,
          total,
          ROW_NUMBER() OVER (PARTITION BY user_id ORDER BY total DESC) AS rank
      FROM orders
  )
  SELECT user_id, order_id, total
  FROM ranked_orders
  WHERE rank <= 3;
  ```

  **Pagination with ROW_NUMBER:**

  ```sql
  -- Get rows 11-20
  WITH numbered_rows AS (
      SELECT 
          *,
          ROW_NUMBER() OVER (ORDER BY created_at DESC) AS row_num
      FROM users
  )
  SELECT *
  FROM numbered_rows
  WHERE row_num BETWEEN 11 AND 20;
  ```

</details>

<details>
  <summary>RANK and DENSE_RANK Functions</summary>
  <br/>

  **RANK()** assigns rank with gaps for ties, **DENSE_RANK()** assigns rank without gaps.

  **RANK vs DENSE_RANK:**

  ```sql
  -- Sample data: users with scores
  SELECT 
      username,
      score,
      RANK() OVER (ORDER BY score DESC) AS rank,
      DENSE_RANK() OVER (ORDER BY score DESC) AS dense_rank,
      ROW_NUMBER() OVER (ORDER BY score DESC) AS row_num
  FROM users;
  
  -- Result:
  -- username | score | rank | dense_rank | row_num
  -- alice    | 100   | 1    | 1          | 1
  -- bob      | 100   | 1    | 1          | 2  (tie)
  -- charlie  | 95    | 3    | 2          | 3  (RANK skips 2)
  -- david    | 90    | 4    | 3          | 4
  -- eve      | 90    | 4    | 3          | 5  (tie)
  -- frank    | 85    | 6    | 4          | 6  (RANK skips 5)
  ```

  **RANK with PARTITION BY:**

  ```sql
  -- Rank users by score within each country
  SELECT 
      username,
      country,
      score,
      RANK() OVER (PARTITION BY country ORDER BY score DESC) AS country_rank
  FROM users;
  
  -- Result:
  -- username | country | score | country_rank
  -- alice    | USA     | 100   | 1
  -- john     | USA     | 95    | 2
  -- bob      | UK      | 100   | 1
  -- charlie  | UK      | 90    | 2
  ```

  **Find top ranked items:**

  ```sql
  -- Top 3 products per category by sales
  WITH ranked_products AS (
      SELECT 
          p.name,
          p.category_id,
          SUM(oi.quantity) AS total_sold,
          RANK() OVER (PARTITION BY p.category_id ORDER BY SUM(oi.quantity) DESC) AS rank
      FROM products p
      JOIN order_items oi ON p.id = oi.product_id
      GROUP BY p.id, p.name, p.category_id
  )
  SELECT name, category_id, total_sold, rank
  FROM ranked_products
  WHERE rank <= 3;
  ```

</details>

<details>
  <summary>NTILE Function</summary>
  <br/>

  **NTILE(n)** divides rows into n approximately equal groups.

  **Basic NTILE:**

  ```sql
  -- Divide users into 4 quartiles by age
  SELECT 
      username,
      age,
      NTILE(4) OVER (ORDER BY age) AS quartile
  FROM users;
  
  -- Result:
  -- username | age | quartile
  -- alice    | 20  | 1  (youngest 25%)
  -- bob      | 25  | 1
  -- charlie  | 30  | 2
  -- david    | 35  | 2
  -- eve      | 40  | 3
  -- frank    | 45  | 3
  -- grace    | 50  | 4
  -- henry    | 55  | 4  (oldest 25%)
  ```

  **NTILE with PARTITION BY:**

  ```sql
  -- Divide users into 3 groups per country
  SELECT 
      username,
      country,
      age,
      NTILE(3) OVER (PARTITION BY country ORDER BY age) AS age_group
  FROM users;
  ```

  **Use cases:**
  + Divide customers into segments (high/medium/low value)
  + Create percentile groups
  + Distribute workload evenly

  **Customer segmentation example:**

  ```sql
  -- Segment customers by spending
  WITH customer_spending AS (
      SELECT 
          user_id,
          SUM(total) AS total_spent,
          NTILE(3) OVER (ORDER BY SUM(total) DESC) AS segment
      FROM orders
      GROUP BY user_id
  )
  SELECT 
      user_id,
      total_spent,
      CASE segment
          WHEN 1 THEN 'High Value'
          WHEN 2 THEN 'Medium Value'
          WHEN 3 THEN 'Low Value'
      END AS customer_segment
  FROM customer_spending;
  ```

</details>

<details>
  <summary>LAG and LEAD Functions</summary>
  <br/>

  **LAG()** accesses previous row value, **LEAD()** accesses next row value.

  **Basic LAG:**

  ```sql
  -- Get previous order total for each user
  SELECT 
      user_id,
      id AS order_id,
      total,
      LAG(total) OVER (PARTITION BY user_id ORDER BY created_at) AS previous_order
  FROM orders;
  
  -- Result:
  -- user_id | order_id | total  | previous_order
  -- 1       | 101      | 99.99  | NULL (first order)
  -- 1       | 102      | 149.99 | 99.99
  -- 1       | 103      | 79.99  | 149.99
  -- 2       | 201      | 199.99 | NULL (first order)
  -- 2       | 202      | 89.99  | 199.99
  ```

  **Basic LEAD:**

  ```sql
  -- Get next order total for each user
  SELECT 
      user_id,
      id AS order_id,
      total,
      LEAD(total) OVER (PARTITION BY user_id ORDER BY created_at) AS next_order
  FROM orders;
  
  -- Result:
  -- user_id | order_id | total  | next_order
  -- 1       | 101      | 99.99  | 149.99
  -- 1       | 102      | 149.99 | 79.99
  -- 1       | 103      | 79.99  | NULL (last order)
  ```

  **LAG with offset and default:**

  ```sql
  -- Get value from 2 rows back, default to 0
  SELECT 
      user_id,
      total,
      LAG(total, 2, 0) OVER (PARTITION BY user_id ORDER BY created_at) AS two_orders_ago
  FROM orders;
  ```

  **Calculate differences:**

  ```sql
  -- Calculate order-to-order change
  SELECT 
      user_id,
      id AS order_id,
      total,
      total - LAG(total) OVER (PARTITION BY user_id ORDER BY created_at) AS change
  FROM orders;
  
  -- Calculate percentage change
  SELECT 
      user_id,
      total,
      ROUND(
          (total - LAG(total) OVER (PARTITION BY user_id ORDER BY created_at)) 
          / LAG(total) OVER (PARTITION BY user_id ORDER BY created_at) * 100,
          2
      ) AS pct_change
  FROM orders;
  ```

  **Time series analysis:**

  ```sql
  -- Daily revenue with previous day comparison
  SELECT 
      DATE(created_at) AS order_date,
      SUM(total) AS daily_revenue,
      LAG(SUM(total)) OVER (ORDER BY DATE(created_at)) AS prev_day_revenue,
      SUM(total) - LAG(SUM(total)) OVER (ORDER BY DATE(created_at)) AS revenue_change
  FROM orders
  GROUP BY DATE(created_at);
  ```

</details>

<details>
  <summary>FIRST_VALUE and LAST_VALUE Functions</summary>
  <br/>

  **FIRST_VALUE()** returns first value in window, **LAST_VALUE()** returns last value.

  **Basic FIRST_VALUE:**

  ```sql
  -- Get first order total for each user
  SELECT 
      user_id,
      id AS order_id,
      total,
      FIRST_VALUE(total) OVER (PARTITION BY user_id ORDER BY created_at) AS first_order
  FROM orders;
  
  -- Result:
  -- user_id | order_id | total  | first_order
  -- 1       | 101      | 99.99  | 99.99
  -- 1       | 102      | 149.99 | 99.99
  -- 1       | 103      | 79.99  | 99.99
  ```

  **Basic LAST_VALUE:**

  ```sql
  -- Get last order total for each user
  SELECT 
      user_id,
      id AS order_id,
      total,
      LAST_VALUE(total) OVER (
          PARTITION BY user_id 
          ORDER BY created_at
          ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING
      ) AS last_order
  FROM orders;
  
  -- Note: ROWS BETWEEN clause is important for LAST_VALUE
  ```

  **Compare with first/last values:**

  ```sql
  -- Compare each order with user's first and last order
  SELECT 
      user_id,
      id AS order_id,
      total,
      FIRST_VALUE(total) OVER (
          PARTITION BY user_id ORDER BY created_at
      ) AS first_order,
      LAST_VALUE(total) OVER (
          PARTITION BY user_id ORDER BY created_at
          ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING
      ) AS last_order,
      total - FIRST_VALUE(total) OVER (
          PARTITION BY user_id ORDER BY created_at
      ) AS diff_from_first
  FROM orders;
  ```

</details>

<details>
  <summary>Aggregate Window Functions</summary>
  <br/>

  **Aggregate functions** can be used as window functions.

  **Running totals:**

  ```sql
  -- Running total of orders per user
  SELECT 
      user_id,
      id AS order_id,
      total,
      SUM(total) OVER (PARTITION BY user_id ORDER BY created_at) AS running_total
  FROM orders;
  
  -- Result:
  -- user_id | order_id | total  | running_total
  -- 1       | 101      | 99.99  | 99.99
  -- 1       | 102      | 149.99 | 249.98
  -- 1       | 103      | 79.99  | 329.97
  ```

  **Running average:**

  ```sql
  -- Running average of order totals
  SELECT 
      user_id,
      id AS order_id,
      total,
      ROUND(AVG(total) OVER (PARTITION BY user_id ORDER BY created_at), 2) AS running_avg
  FROM orders;
  ```

  **Moving average:**

  ```sql
  -- 3-order moving average
  SELECT 
      user_id,
      id AS order_id,
      total,
      ROUND(AVG(total) OVER (
          PARTITION BY user_id 
          ORDER BY created_at
          ROWS BETWEEN 2 PRECEDING AND CURRENT ROW
      ), 2) AS moving_avg_3
  FROM orders;
  ```

  **Cumulative count:**

  ```sql
  -- Count of orders up to current row
  SELECT 
      user_id,
      id AS order_id,
      created_at,
      COUNT(*) OVER (PARTITION BY user_id ORDER BY created_at) AS order_number
  FROM orders;
  ```

  **Compare with overall average:**

  ```sql
  -- Compare each order with user's average
  SELECT 
      user_id,
      id AS order_id,
      total,
      ROUND(AVG(total) OVER (PARTITION BY user_id), 2) AS user_avg,
      ROUND(total - AVG(total) OVER (PARTITION BY user_id), 2) AS diff_from_avg
  FROM orders;
  ```

</details>

<details>
  <summary>Window Frame Specification</summary>
  <br/>

  **Window frames** define which rows to include in calculation.

  **Frame types:**
  + **ROWS** - Physical rows
  + **RANGE** - Logical range based on values

  **Frame boundaries:**
  + **UNBOUNDED PRECEDING** - Start of partition
  + **N PRECEDING** - N rows before current
  + **CURRENT ROW** - Current row
  + **N FOLLOWING** - N rows after current
  + **UNBOUNDED FOLLOWING** - End of partition

  **Frame examples:**

  ```sql
  -- Default frame (RANGE BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW)
  SELECT 
      id,
      total,
      SUM(total) OVER (ORDER BY id) AS running_total
  FROM orders;
  
  -- Explicit frame: all rows up to current
  SELECT 
      id,
      total,
      SUM(total) OVER (
          ORDER BY id
          ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW
      ) AS running_total
  FROM orders;
  
  -- Frame: 2 rows before to 2 rows after (5-row window)
  SELECT 
      id,
      total,
      AVG(total) OVER (
          ORDER BY id
          ROWS BETWEEN 2 PRECEDING AND 2 FOLLOWING
      ) AS moving_avg_5
  FROM orders;
  
  -- Frame: 3 rows before to current (4-row window)
  SELECT 
      id,
      total,
      AVG(total) OVER (
          ORDER BY id
          ROWS BETWEEN 3 PRECEDING AND CURRENT ROW
      ) AS moving_avg_4
  FROM orders;
  
  -- Frame: current to 2 rows after
  SELECT 
      id,
      total,
      SUM(total) OVER (
          ORDER BY id
          ROWS BETWEEN CURRENT ROW AND 2 FOLLOWING
      ) AS next_3_total
  FROM orders;
  
  -- Frame: entire partition
  SELECT 
      user_id,
      total,
      AVG(total) OVER (
          PARTITION BY user_id
          ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING
      ) AS user_avg
  FROM orders;
  ```

</details>

<details>
  <summary>Best Practices</summary>
  <br/>

  ```sql
  -- ✅ DO: Use ROW_NUMBER for unique sequential numbers
  SELECT 
      username,
      ROW_NUMBER() OVER (ORDER BY created_at) AS row_num
  FROM users;
  
  -- ✅ DO: Use RANK for rankings with gaps
  SELECT 
      username,
      score,
      RANK() OVER (ORDER BY score DESC) AS rank
  FROM users;
  
  -- ✅ DO: Use DENSE_RANK for rankings without gaps
  SELECT 
      username,
      score,
      DENSE_RANK() OVER (ORDER BY score DESC) AS dense_rank
  FROM users;
  
  -- ✅ DO: Use PARTITION BY to group calculations
  SELECT 
      username,
      country,
      ROW_NUMBER() OVER (PARTITION BY country ORDER BY username) AS row_num
  FROM users;
  
  -- ✅ DO: Use LAG/LEAD for time series analysis
  SELECT 
      order_date,
      revenue,
      revenue - LAG(revenue) OVER (ORDER BY order_date) AS daily_change
  FROM daily_sales;
  
  -- ✅ DO: Use NTILE for segmentation
  SELECT 
      user_id,
      total_spent,
      NTILE(4) OVER (ORDER BY total_spent DESC) AS quartile
  FROM customer_spending;
  
  -- ✅ DO: Use CTEs with window functions for readability
  WITH ranked_orders AS (
      SELECT 
          user_id,
          order_id,
          total,
          ROW_NUMBER() OVER (PARTITION BY user_id ORDER BY total DESC) AS rank
      FROM orders
  )
  SELECT * FROM ranked_orders WHERE rank <= 3;
  
  -- ✅ DO: Specify frame for LAST_VALUE
  SELECT 
      id,
      total,
      LAST_VALUE(total) OVER (
          ORDER BY id
          ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING
      ) AS last_total
  FROM orders;
  
  -- ✅ DO: Use window functions instead of self-joins
  -- Window function (better)
  SELECT 
      id,
      total,
      LAG(total) OVER (ORDER BY id) AS prev_total
  FROM orders;
  
  -- Self-join (slower)
  SELECT 
      o1.id,
      o1.total,
      o2.total AS prev_total
  FROM orders o1
  LEFT JOIN orders o2 ON o2.id = o1.id - 1;
  
  -- ✅ DO: Use indexes on PARTITION BY and ORDER BY columns
  CREATE INDEX idx_orders_user_created ON orders(user_id, created_at);
  
  -- ✅ DO: Use window functions for running totals
  SELECT 
      order_date,
      daily_revenue,
      SUM(daily_revenue) OVER (ORDER BY order_date) AS running_total
  FROM daily_sales;
  
  -- ❌ DON'T: Use window functions when simple aggregation suffices
  -- Window function (unnecessary)
  SELECT DISTINCT
      country,
      COUNT(*) OVER (PARTITION BY country) AS user_count
  FROM users;
  
  -- Simple GROUP BY (better)
  SELECT country, COUNT(*) AS user_count
  FROM users
  GROUP BY country;
  
  -- ✅ DO: Use EXPLAIN to analyze window function performance
  EXPLAIN SELECT 
      username,
      ROW_NUMBER() OVER (ORDER BY created_at) AS row_num
  FROM users;
  ```

  **Summary:**
  + **Window functions** perform calculations without grouping rows
  + **ROW_NUMBER()** - Unique sequential number for each row
  + **RANK()** - Rank with gaps for ties
  + **DENSE_RANK()** - Rank without gaps for ties
  + **NTILE(n)** - Divide rows into n groups
  + **LAG()** - Access previous row value
  + **LEAD()** - Access next row value
  + **FIRST_VALUE()** - First value in window
  + **LAST_VALUE()** - Last value in window (specify frame!)
  + Use **PARTITION BY** to group calculations
  + Use **ORDER BY** to define row order
  + Use **frame specification** to define calculation window
  + Use **CTEs** for readability
  + Use **indexes** on PARTITION BY and ORDER BY columns
  + Use window functions instead of **self-joins**
  + Don't use window functions when **simple aggregation** suffices

</details>

## Indexes

<details>
  <summary>What are Indexes?</summary>
  <br/>

  **Indexes** are database structures that improve query performance by allowing faster data retrieval.

  **How indexes work:**
  + Similar to a book index - helps find data without scanning entire table
  + Creates a separate data structure (usually B-tree) with pointers to table rows
  + Trades storage space and write performance for faster reads

  **Benefits:**
  + **Faster SELECT queries** - Especially with WHERE, JOIN, ORDER BY
  + **Faster sorting** - ORDER BY uses index instead of sorting
  + **Faster joins** - JOIN operations use indexes on join columns
  + **Enforce uniqueness** - UNIQUE indexes prevent duplicate values

  **Costs:**
  + **Storage space** - Indexes consume disk space
  + **Slower writes** - INSERT, UPDATE, DELETE must update indexes
  + **Maintenance overhead** - Indexes need to be maintained

  **Basic example:**

  ```sql
  -- Without index: Full table scan (slow)
  SELECT * FROM users WHERE email = 'john@example.com';
  -- Scans all rows to find matching email
  
  -- Create index
  CREATE INDEX idx_users_email ON users(email);
  
  -- With index: Index lookup (fast)
  SELECT * FROM users WHERE email = 'john@example.com';
  -- Uses index to quickly find matching row
  ```

</details>

<details>
  <summary>B-Tree Index (Default)</summary>
  <br/>

  **B-Tree** (Balanced Tree) is the default and most common index type.

  **Characteristics:**
  + Balanced tree structure
  + Good for equality and range queries
  + Supports ORDER BY
  + Works with most data types

  **Create B-Tree index:**

  ```sql
  -- Single column index
  CREATE INDEX idx_users_email ON users(email);
  
  -- Composite index (multiple columns)
  CREATE INDEX idx_users_country_city ON users(country, city);
  
  -- Unique index
  CREATE UNIQUE INDEX idx_users_username ON users(username);
  
  -- Index with specific name
  CREATE INDEX idx_orders_user_created ON orders(user_id, created_at);
  ```

  **When B-Tree is efficient:**

  ```sql
  -- Equality queries
  SELECT * FROM users WHERE email = 'john@example.com';
  
  -- Range queries
  SELECT * FROM orders WHERE created_at BETWEEN '2024-01-01' AND '2024-12-31';
  SELECT * FROM products WHERE price > 100;
  
  -- Sorting
  SELECT * FROM users ORDER BY username;
  
  -- Joins
  SELECT u.username, o.total
  FROM users u
  JOIN orders o ON u.id = o.user_id;
  
  -- IN queries
  SELECT * FROM users WHERE country IN ('USA', 'Canada', 'UK');
  
  -- LIKE with prefix
  SELECT * FROM users WHERE username LIKE 'john%';
  ```

  **When B-Tree is NOT efficient:**

  ```sql
  -- LIKE with leading wildcard (can't use index)
  SELECT * FROM users WHERE email LIKE '%@gmail.com';
  
  -- Functions on indexed column (can't use index)
  SELECT * FROM users WHERE LOWER(email) = 'john@example.com';
  
  -- OR conditions on different columns (may not use index)
  SELECT * FROM users WHERE email = 'john@example.com' OR username = 'john';
  ```

</details>

<details>
  <summary>Composite Indexes</summary>
  <br/>

  **Composite indexes** (multi-column indexes) index multiple columns together.

  **Create composite index:**

  ```sql
  -- Index on country and city
  CREATE INDEX idx_users_country_city ON users(country, city);
  
  -- Index on user_id and created_at
  CREATE INDEX idx_orders_user_created ON orders(user_id, created_at);
  
  -- Index on category_id, brand, and price
  CREATE INDEX idx_products_cat_brand_price ON products(category_id, brand, price);
  ```

  **Column order matters (left-to-right rule):**

  ```sql
  -- Index: (country, city, age)
  CREATE INDEX idx_users_country_city_age ON users(country, city, age);
  
  -- ✅ Uses index (matches left-most columns)
  SELECT * FROM users WHERE country = 'USA';
  SELECT * FROM users WHERE country = 'USA' AND city = 'NYC';
  SELECT * FROM users WHERE country = 'USA' AND city = 'NYC' AND age > 18;
  
  -- ✅ Partially uses index (country only)
  SELECT * FROM users WHERE country = 'USA' AND age > 18;
  
  -- ❌ Cannot use index (doesn't start with country)
  SELECT * FROM users WHERE city = 'NYC';
  SELECT * FROM users WHERE age > 18;
  SELECT * FROM users WHERE city = 'NYC' AND age > 18;
  ```

  **Best practices for column order:**

  ```sql
  -- 1. Equality columns first, range columns last
  CREATE INDEX idx_orders_user_status_created ON orders(user_id, status, created_at);
  
  -- Good: Uses full index
  SELECT * FROM orders 
  WHERE user_id = 1 AND status = 'completed' AND created_at > '2024-01-01';
  
  -- 2. High cardinality columns first
  -- email (unique) before country (low cardinality)
  CREATE INDEX idx_users_email_country ON users(email, country);
  
  -- 3. Most frequently queried columns first
  CREATE INDEX idx_products_category_brand ON products(category_id, brand);
  ```

  **When to use composite vs separate indexes:**

  ```sql
  -- Composite index (better for combined queries)
  CREATE INDEX idx_users_country_city ON users(country, city);
  SELECT * FROM users WHERE country = 'USA' AND city = 'NYC';
  
  -- Separate indexes (better for individual queries)
  CREATE INDEX idx_users_country ON users(country);
  CREATE INDEX idx_users_city ON users(city);
  SELECT * FROM users WHERE country = 'USA';  -- Uses first index
  SELECT * FROM users WHERE city = 'NYC';     -- Uses second index
  ```

</details>

<details>
  <summary>Unique Indexes</summary>
  <br/>

  **Unique indexes** enforce uniqueness and improve query performance.

  **Create unique index:**

  ```sql
  -- Single column unique index
  CREATE UNIQUE INDEX idx_users_email ON users(email);
  
  -- Composite unique index
  CREATE UNIQUE INDEX idx_users_username_email ON users(username, email);
  
  -- Unique constraint (creates unique index automatically)
  ALTER TABLE users ADD CONSTRAINT uk_users_email UNIQUE (email);
  ```

  **Unique index vs unique constraint:**

  ```sql
  -- Unique index (lower level, database-specific)
  CREATE UNIQUE INDEX idx_users_email ON users(email);
  
  -- Unique constraint (higher level, SQL standard)
  ALTER TABLE users ADD CONSTRAINT uk_users_email UNIQUE (email);
  -- Creates unique index automatically
  ```

  **NULL handling in unique indexes:**

  ```sql
  -- Most databases allow multiple NULLs in unique index
  CREATE UNIQUE INDEX idx_users_phone ON users(phone);
  
  INSERT INTO users (username, phone) VALUES ('john', NULL);   -- OK
  INSERT INTO users (username, phone) VALUES ('jane', NULL);   -- OK (multiple NULLs allowed)
  INSERT INTO users (username, phone) VALUES ('bob', '123');   -- OK
  INSERT INTO users (username, phone) VALUES ('alice', '123'); -- ERROR (duplicate)
  ```

  **Partial unique index (PostgreSQL):**

  ```sql
  -- Unique only for non-NULL values
  CREATE UNIQUE INDEX idx_users_phone ON users(phone) WHERE phone IS NOT NULL;
  ```

</details>

<details>
  <summary>Partial Indexes</summary>
  <br/>

  **Partial indexes** (filtered indexes) index only rows that meet a condition.

  **Create partial index (PostgreSQL):**

  ```sql
  -- Index only active users
  CREATE INDEX idx_users_active ON users(username) WHERE status = 'active';
  
  -- Index only recent orders
  CREATE INDEX idx_orders_recent ON orders(user_id, created_at) 
  WHERE created_at > '2024-01-01';
  
  -- Index only non-NULL emails
  CREATE INDEX idx_users_email ON users(email) WHERE email IS NOT NULL;
  
  -- Index only high-value orders
  CREATE INDEX idx_orders_high_value ON orders(user_id, total) WHERE total > 1000;
  ```

  **Benefits:**
  + **Smaller index size** - Only indexes relevant rows
  + **Faster writes** - Fewer rows to update
  + **Better performance** - More focused index

  **Usage:**

  ```sql
  -- Query must match index condition to use partial index
  
  -- ✅ Uses partial index
  SELECT * FROM users WHERE status = 'active' AND username = 'john';
  
  -- ❌ Cannot use partial index (different condition)
  SELECT * FROM users WHERE status = 'inactive' AND username = 'john';
  ```

  **SQL Server filtered index:**

  ```sql
  -- SQL Server syntax
  CREATE INDEX idx_users_active ON users(username) WHERE status = 'active';
  ```

</details>

<details>
  <summary>Covering Indexes</summary>
  <br/>

  **Covering indexes** include all columns needed by a query (index-only scan).

  **Create covering index:**

  ```sql
  -- Index includes all columns in query
  CREATE INDEX idx_users_email_username_age ON users(email, username, age);
  
  -- Query uses only indexed columns (index-only scan)
  SELECT username, age FROM users WHERE email = 'john@example.com';
  -- No need to access table - all data in index
  ```

  **INCLUDE clause (PostgreSQL, SQL Server):**

  ```sql
  -- PostgreSQL
  CREATE INDEX idx_users_email_inc ON users(email) INCLUDE (username, age);
  
  -- SQL Server
  CREATE INDEX idx_users_email_inc ON users(email) INCLUDE (username, age);
  
  -- Query uses index-only scan
  SELECT username, age FROM users WHERE email = 'john@example.com';
  ```

  **Benefits:**
  + **Faster queries** - No table access needed
  + **Reduced I/O** - Reads only index, not table

  **Trade-offs:**
  + **Larger index** - Stores additional columns
  + **Slower writes** - More data to update

  **Example:**

  ```sql
  -- Without covering index
  CREATE INDEX idx_orders_user ON orders(user_id);
  SELECT user_id, total, status FROM orders WHERE user_id = 1;
  -- 1. Index lookup (user_id)
  -- 2. Table access (total, status)
  
  -- With covering index
  CREATE INDEX idx_orders_user_inc ON orders(user_id) INCLUDE (total, status);
  SELECT user_id, total, status FROM orders WHERE user_id = 1;
  -- 1. Index-only scan (all columns in index)
  ```

</details>

<details>
  <summary>Full-Text Indexes</summary>
  <br/>

  **Full-text indexes** enable efficient text search.

  **Create full-text index (MySQL):**

  ```sql
  -- Create full-text index
  CREATE FULLTEXT INDEX idx_products_name_desc ON products(name, description);
  
  -- Full-text search
  SELECT * FROM products 
  WHERE MATCH(name, description) AGAINST ('laptop computer');
  
  -- Boolean mode
  SELECT * FROM products 
  WHERE MATCH(name, description) AGAINST ('+laptop -refurbished' IN BOOLEAN MODE);
  ```

  **Create full-text index (PostgreSQL):**

  ```sql
  -- Add tsvector column
  ALTER TABLE products ADD COLUMN search_vector tsvector;
  
  -- Update search vector
  UPDATE products 
  SET search_vector = to_tsvector('english', name || ' ' || description);
  
  -- Create GIN index
  CREATE INDEX idx_products_search ON products USING GIN(search_vector);
  
  -- Full-text search
  SELECT * FROM products 
  WHERE search_vector @@ to_tsquery('english', 'laptop & computer');
  ```

  **Use cases:**
  + Search product names and descriptions
  + Search article content
  + Search user comments
  + Any text-heavy search

</details>

<details>
  <summary>Hash Indexes</summary>
  <br/>

  **Hash indexes** use hash table for equality comparisons (PostgreSQL).

  **Create hash index:**

  ```sql
  -- PostgreSQL only
  CREATE INDEX idx_users_email_hash ON users USING HASH(email);
  ```

  **Characteristics:**
  + **Fast equality lookups** - O(1) average case
  + **No range queries** - Only supports = operator
  + **No sorting** - Cannot be used for ORDER BY
  + **Smaller than B-tree** - More compact

  **When to use:**

  ```sql
  -- ✅ Good for equality queries
  SELECT * FROM users WHERE email = 'john@example.com';
  
  -- ❌ Cannot use for range queries
  SELECT * FROM users WHERE email > 'john@example.com';
  
  -- ❌ Cannot use for LIKE
  SELECT * FROM users WHERE email LIKE 'john%';
  
  -- ❌ Cannot use for ORDER BY
  SELECT * FROM users ORDER BY email;
  ```

  **Note:** B-tree indexes are usually preferred over hash indexes in PostgreSQL.

</details>

<details>
  <summary>When to Create Indexes</summary>
  <br/>

  **Create indexes for:**

  ```sql
  -- 1. Primary keys (automatic)
  CREATE TABLE users (
      id SERIAL PRIMARY KEY  -- Automatically creates index
  );
  
  -- 2. Foreign keys (manual)
  CREATE INDEX idx_orders_user_id ON orders(user_id);
  
  -- 3. Columns in WHERE clause
  CREATE INDEX idx_users_email ON users(email);
  SELECT * FROM users WHERE email = 'john@example.com';
  
  -- 4. Columns in JOIN conditions
  CREATE INDEX idx_orders_user_id ON orders(user_id);
  SELECT * FROM users u JOIN orders o ON u.id = o.user_id;
  
  -- 5. Columns in ORDER BY
  CREATE INDEX idx_users_created ON users(created_at);
  SELECT * FROM users ORDER BY created_at DESC;
  
  -- 6. Columns in GROUP BY
  CREATE INDEX idx_orders_user_id ON orders(user_id);
  SELECT user_id, COUNT(*) FROM orders GROUP BY user_id;
  
  -- 7. Columns with high cardinality (many unique values)
  CREATE INDEX idx_users_email ON users(email);  -- Good (unique)
  -- Don't index: gender (low cardinality - only 2-3 values)
  
  -- 8. Frequently queried columns
  CREATE INDEX idx_products_category ON products(category_id);
  ```

</details>

<details>
  <summary>When NOT to Create Indexes</summary>
  <br/>

  **Avoid indexes for:**

  ```sql
  -- 1. Small tables (< 1000 rows)
  -- Full table scan is faster than index lookup
  
  -- 2. Columns with low cardinality
  -- Don't index: gender, boolean flags, status with few values
  CREATE INDEX idx_users_gender ON users(gender);  -- BAD (only 2-3 values)
  
  -- 3. Frequently updated columns
  -- Index maintenance overhead > query benefit
  CREATE INDEX idx_users_last_login ON users(last_login);  -- BAD (updated often)
  
  -- 4. Columns never used in queries
  -- Wastes space and slows writes
  
  -- 5. Tables with heavy write workload
  -- Indexes slow down INSERT, UPDATE, DELETE
  
  -- 6. Columns used with functions
  -- Index won't be used anyway
  SELECT * FROM users WHERE LOWER(email) = 'john@example.com';
  -- Index on email won't help (use functional index instead)
  
  -- 7. Redundant indexes
  -- Index on (country, city) makes index on (country) redundant
  CREATE INDEX idx_users_country_city ON users(country, city);
  CREATE INDEX idx_users_country ON users(country);  -- REDUNDANT
  ```

</details>

<details>
  <summary>Index Maintenance</summary>
  <br/>

  **View indexes:**

  ```sql
  -- PostgreSQL
  SELECT 
      tablename,
      indexname,
      indexdef
  FROM pg_indexes
  WHERE tablename = 'users';
  
  -- MySQL
  SHOW INDEXES FROM users;
  
  -- SQL Server
  SELECT 
      i.name AS index_name,
      c.name AS column_name
  FROM sys.indexes i
  JOIN sys.index_columns ic ON i.object_id = ic.object_id AND i.index_id = ic.index_id
  JOIN sys.columns c ON ic.object_id = c.object_id AND ic.column_id = c.column_id
  WHERE i.object_id = OBJECT_ID('users');
  ```

  **Drop index:**

  ```sql
  -- PostgreSQL, MySQL
  DROP INDEX idx_users_email;
  
  -- SQL Server
  DROP INDEX idx_users_email ON users;
  ```

  **Rebuild index (defragmentation):**

  ```sql
  -- PostgreSQL
  REINDEX INDEX idx_users_email;
  REINDEX TABLE users;
  
  -- MySQL
  ALTER TABLE users ENGINE=InnoDB;  -- Rebuilds all indexes
  
  -- SQL Server
  ALTER INDEX idx_users_email ON users REBUILD;
  ```

  **Analyze index usage:**

  ```sql
  -- PostgreSQL
  SELECT 
      schemaname,
      tablename,
      indexname,
      idx_scan,
      idx_tup_read,
      idx_tup_fetch
  FROM pg_stat_user_indexes
  WHERE tablename = 'users';
  
  -- Find unused indexes
  SELECT 
      schemaname,
      tablename,
      indexname,
      idx_scan
  FROM pg_stat_user_indexes
  WHERE idx_scan = 0
  AND indexname NOT LIKE '%_pkey';  -- Exclude primary keys
  ```

</details>

<details>
  <summary>Index Performance Impact</summary>
  <br/>

  **Why indexes make SELECT fast:**

  **1. Reduces rows scanned:**

  ```sql
  -- Table with 1,000,000 users
  
  -- WITHOUT INDEX: Full table scan
  SELECT * FROM users WHERE email = 'john@example.com';
  -- Scans ALL 1,000,000 rows to find 1 matching row
  -- Time: ~500ms (reads entire table from disk)
  
  -- WITH INDEX: Index lookup
  CREATE INDEX idx_users_email ON users(email);
  SELECT * FROM users WHERE email = 'john@example.com';
  -- Scans only ~3-4 index pages (B-tree depth) to find matching row
  -- Time: ~5ms (reads only index pages + 1 table row)
  ```

  **2. B-Tree structure enables fast lookup:**

  ```
  Without Index (Sequential Scan):
  Row 1 → Row 2 → Row 3 → ... → Row 1,000,000
  Must check EVERY row until match found
  Time Complexity: O(n) - linear
  
  With B-Tree Index (Binary Search):
  Root Node
    ├─ Branch Node (emails A-M)
    │   ├─ Leaf Node (emails A-D)
    │   └─ Leaf Node (emails E-M)
    └─ Branch Node (emails N-Z)
        ├─ Leaf Node (emails N-S)
        └─ Leaf Node (emails T-Z) ← john@example.com found here
  
  Only traverses 3-4 levels (tree depth)
  Time Complexity: O(log n) - logarithmic
  
  Example: 1,000,000 rows
  - Without index: 1,000,000 comparisons
  - With index: ~20 comparisons (log₂ 1,000,000 ≈ 20)
  ```

  **3. Index stores sorted data:**

  ```sql
  -- Table data (unsorted):
  id | email
  1  | zoe@example.com
  2  | alice@example.com
  3  | john@example.com
  4  | bob@example.com
  
  -- Index data (sorted):
  email               | row_pointer
  alice@example.com   | → Row 2
  bob@example.com     | → Row 4
  john@example.com    | → Row 3
  zoe@example.com     | → Row 1
  
  -- Binary search on sorted data is FAST
  -- Jump to middle → compare → jump left/right → repeat
  ```

  **4. Index-only scans (covering indexes):**

  ```sql
  -- Without covering index: Index + Table access
  CREATE INDEX idx_users_email ON users(email);
  SELECT email, username FROM users WHERE email = 'john@example.com';
  -- Step 1: Index lookup (find row pointer)
  -- Step 2: Table access (read username from table row)
  -- 2 disk reads
  
  -- With covering index: Index-only scan
  CREATE INDEX idx_users_email_username ON users(email, username);
  SELECT email, username FROM users WHERE email = 'john@example.com';
  -- Step 1: Index lookup (find email AND username in index)
  -- No table access needed!
  -- 1 disk read (50% faster)
  ```

  **5. Avoids sorting:**

  ```sql
  -- Without index: Full scan + sort
  SELECT * FROM users ORDER BY email LIMIT 10;
  -- Step 1: Read all 1,000,000 rows
  -- Step 2: Sort all rows by email (expensive!)
  -- Step 3: Return first 10
  -- Time: ~1000ms
  
  -- With index: Index scan (already sorted)
  CREATE INDEX idx_users_email ON users(email);
  SELECT * FROM users ORDER BY email LIMIT 10;
  -- Step 1: Read first 10 rows from index (already sorted)
  -- No sorting needed!
  -- Time: ~5ms
  ```

  **Why indexes make writes SLOWER:**

  **1. Must update multiple data structures:**

  ```sql
  -- Table with 5 indexes
  CREATE TABLE users (
      id SERIAL PRIMARY KEY,           -- Index 1: pk_users_id
      username VARCHAR(50),
      email VARCHAR(100),
      country VARCHAR(50),
      created_at TIMESTAMP
  );
  
  CREATE INDEX idx_users_username ON users(username);    -- Index 2
  CREATE INDEX idx_users_email ON users(email);          -- Index 3
  CREATE INDEX idx_users_country ON users(country);      -- Index 4
  CREATE INDEX idx_users_created ON users(created_at);   -- Index 5
  
  -- INSERT without indexes: 1 write operation
  INSERT INTO users (username, email, country, created_at)
  VALUES ('john', 'john@example.com', 'USA', NOW());
  -- Writes to: Table only
  -- Time: ~1ms
  
  -- INSERT with 5 indexes: 6 write operations
  INSERT INTO users (username, email, country, created_at)
  VALUES ('john', 'john@example.com', 'USA', NOW());
  -- Writes to:
  -- 1. Table (insert row)
  -- 2. Primary key index (insert id)
  -- 3. Username index (insert username)
  -- 4. Email index (insert email)
  -- 5. Country index (insert country)
  -- 6. Created_at index (insert timestamp)
  -- Time: ~6ms (6x slower)
  ```

  **2. B-Tree rebalancing overhead:**

  ```
  INSERT into indexed column:
  
  Step 1: Find correct position in B-tree
  Step 2: Insert value into leaf node
  Step 3: If leaf node is full → SPLIT node
  Step 4: Update parent node pointers
  Step 5: If parent is full → SPLIT parent (recursive)
  Step 6: May need to create new root node
  
  Example: Insert 'john@example.com'
  
  Before:
  Leaf Node: [alice@..., bob@..., charlie@...]
  
  After:
  Leaf Node 1: [alice@..., bob@...]
  Leaf Node 2: [charlie@..., john@...]  ← Split occurred
  Parent Node: Updated with new pointers
  
  This rebalancing takes time!
  ```

  **3. UPDATE affects multiple indexes:**

  ```sql
  -- UPDATE without indexes: 1 write
  UPDATE users SET email = 'newemail@example.com' WHERE id = 1;
  -- Writes to: Table only
  -- Time: ~1ms
  
  -- UPDATE with indexes: Multiple writes
  UPDATE users SET email = 'newemail@example.com' WHERE id = 1;
  -- Writes to:
  -- 1. Table (update row)
  -- 2. Email index (delete old email, insert new email)
  -- Each index update requires:
  --   - Find old value in B-tree
  --   - Delete old value
  --   - Find position for new value
  --   - Insert new value
  --   - Possibly rebalance tree
  -- Time: ~3-5ms
  ```

  **4. DELETE must remove from all indexes:**

  ```sql
  -- DELETE without indexes: 1 write
  DELETE FROM users WHERE id = 1;
  -- Writes to: Table only
  -- Time: ~1ms
  
  -- DELETE with 5 indexes: 6 writes
  DELETE FROM users WHERE id = 1;
  -- Writes to:
  -- 1. Table (mark row as deleted)
  -- 2. Primary key index (remove id)
  -- 3. Username index (remove username)
  -- 4. Email index (remove email)
  -- 5. Country index (remove country)
  -- 6. Created_at index (remove timestamp)
  -- Each removal may trigger B-tree rebalancing
  -- Time: ~5-7ms
  ```

  **5. Index fragmentation over time:**

  ```
  After many INSERT/UPDATE/DELETE operations:
  
  Fragmented Index (inefficient):
  Page 1: [alice@...] [empty] [empty] [empty]
  Page 2: [bob@...] [empty] [charlie@...] [empty]
  Page 3: [empty] [david@...] [empty] [empty]
  
  - Wasted space (empty slots)
  - More pages to read
  - Slower queries
  
  Solution: REINDEX (rebuilds index)
  
  Rebuilt Index (efficient):
  Page 1: [alice@...] [bob@...] [charlie@...] [david@...]
  Page 2: [eve@...] [frank@...] [grace@...] [henry@...]
  
  - No wasted space
  - Fewer pages to read
  - Faster queries
  ```

  **Real-world performance comparison:**

  ```sql
  -- Test table: 1,000,000 users
  
  -- SELECT without index
  SELECT * FROM users WHERE email = 'john@example.com';
  -- Execution time: 450ms
  -- Rows scanned: 1,000,000
  -- Pages read: 10,000
  
  -- SELECT with index
  CREATE INDEX idx_users_email ON users(email);
  SELECT * FROM users WHERE email = 'john@example.com';
  -- Execution time: 3ms (150x faster!)
  -- Rows scanned: 1
  -- Pages read: 4 (index pages) + 1 (table page) = 5
  
  -- INSERT without indexes (1000 rows)
  INSERT INTO users (username, email) VALUES (...);  -- x1000
  -- Execution time: 100ms
  -- Average per row: 0.1ms
  
  -- INSERT with 5 indexes (1000 rows)
  INSERT INTO users (username, email) VALUES (...);  -- x1000
  -- Execution time: 600ms (6x slower)
  -- Average per row: 0.6ms
  ```

  **Index size impact:**

  ```sql
  -- PostgreSQL
  SELECT 
      tablename,
      indexname,
      pg_size_pretty(pg_relation_size(indexname::regclass)) AS index_size
  FROM pg_indexes
  WHERE tablename = 'users';
  
  -- Example output:
  -- tablename | indexname              | index_size
  -- users     | pk_users_id            | 21 MB
  -- users     | idx_users_email        | 35 MB
  -- users     | idx_users_username     | 32 MB
  -- users     | idx_users_country_city | 28 MB
  -- Total index size: 116 MB
  -- Table size: 150 MB
  -- Indexes consume 77% of table size!
  
  -- MySQL
  SELECT 
      table_name,
      index_name,
      ROUND(stat_value * @@innodb_page_size / 1024 / 1024, 2) AS size_mb
  FROM mysql.innodb_index_stats
  WHERE table_name = 'users' AND stat_name = 'size';
  ```

  **Memory impact:**

  ```
  Database memory usage:
  
  Without indexes:
  - Buffer pool: Caches table pages
  - Memory needed: ~150 MB (table size)
  
  With indexes:
  - Buffer pool: Caches table pages + index pages
  - Memory needed: ~266 MB (table + indexes)
  - 77% more memory required!
  
  If indexes don't fit in memory:
  - Must read from disk (slow)
  - Defeats purpose of index
  ```

  **Trade-offs summary:**

  | Aspect | Without Index | With Index | Impact |
  |--------|--------------|------------|--------|
  | SELECT speed | 450ms (full scan) | 3ms (index lookup) | **150x faster** |
  | INSERT speed | 0.1ms | 0.6ms | **6x slower** |
  | UPDATE speed | 1ms | 3-5ms | **3-5x slower** |
  | DELETE speed | 1ms | 5-7ms | **5-7x slower** |
  | Storage | 150 MB | 266 MB | **+77% space** |
  | Memory | 150 MB | 266 MB | **+77% RAM** |
  | Maintenance | None | Periodic rebuild | **Admin overhead** |

  **When to use indexes:**

  ```sql
  -- ✅ Read-heavy workload (90% SELECT, 10% writes)
  -- Benefit: 150x faster reads
  -- Cost: 6x slower writes (acceptable)
  
  -- ❌ Write-heavy workload (10% SELECT, 90% writes)
  -- Benefit: 150x faster reads (rarely used)
  -- Cost: 6x slower writes (significant impact)
  
  -- ✅ Large tables (1M+ rows)
  -- Benefit: Huge performance gain
  -- Cost: Worth the overhead
  
  -- ❌ Small tables (< 1000 rows)
  -- Benefit: Minimal (full scan is fast anyway)
  -- Cost: Not worth the overhead
  ```

</details>

<details>
  <summary>Best Practices</summary>
  <br/>

  ```sql
  -- ✅ DO: Index foreign keys
  CREATE INDEX idx_orders_user_id ON orders(user_id);
  
  -- ✅ DO: Index columns in WHERE clause
  CREATE INDEX idx_users_email ON users(email);
  SELECT * FROM users WHERE email = 'john@example.com';
  
  -- ✅ DO: Index columns in JOIN conditions
  CREATE INDEX idx_orders_user_id ON orders(user_id);
  SELECT * FROM users u JOIN orders o ON u.id = o.user_id;
  
  -- ✅ DO: Use composite indexes for combined queries
  CREATE INDEX idx_users_country_city ON users(country, city);
  SELECT * FROM users WHERE country = 'USA' AND city = 'NYC';
  
  -- ✅ DO: Put equality columns before range columns
  CREATE INDEX idx_orders_user_status_created ON orders(user_id, status, created_at);
  
  -- ✅ DO: Use covering indexes for frequently accessed columns
  CREATE INDEX idx_users_email_inc ON users(email) INCLUDE (username, age);
  
  -- ✅ DO: Use partial indexes for filtered queries
  CREATE INDEX idx_users_active ON users(username) WHERE status = 'active';
  
  -- ✅ DO: Use EXPLAIN to verify index usage
  EXPLAIN SELECT * FROM users WHERE email = 'john@example.com';
  
  -- ✅ DO: Monitor index usage and remove unused indexes
  SELECT indexname, idx_scan FROM pg_stat_user_indexes WHERE idx_scan = 0;
  
  -- ✅ DO: Rebuild indexes periodically
  REINDEX TABLE users;
  
  -- ❌ DON'T: Index small tables
  -- Full table scan is faster for tables < 1000 rows
  
  -- ❌ DON'T: Index low cardinality columns
  CREATE INDEX idx_users_gender ON users(gender);  -- BAD (only 2-3 values)
  
  -- ❌ DON'T: Create redundant indexes
  CREATE INDEX idx_users_country_city ON users(country, city);
  CREATE INDEX idx_users_country ON users(country);  -- REDUNDANT
  
  -- ❌ DON'T: Index columns used with functions
  SELECT * FROM users WHERE LOWER(email) = 'john@example.com';
  -- Index on email won't help
  
  -- ✅ DO: Use functional index instead
  CREATE INDEX idx_users_email_lower ON users(LOWER(email));
  
  -- ❌ DON'T: Over-index (too many indexes slow writes)
  -- Aim for 3-5 indexes per table
  
  -- ✅ DO: Name indexes descriptively
  CREATE INDEX idx_users_email ON users(email);  -- Good
  CREATE INDEX idx1 ON users(email);  -- Bad
  
  -- ✅ DO: Consider index size vs benefit
  -- Large indexes consume memory and disk space
  
  -- ✅ DO: Test query performance with and without index
  EXPLAIN ANALYZE SELECT * FROM users WHERE email = 'john@example.com';
  ```

  **Summary:**
  + **Indexes** improve read performance but slow writes
  + **B-Tree** is the default and most common index type
  + **Composite indexes** follow left-to-right rule
  + **Unique indexes** enforce uniqueness
  + **Partial indexes** index only filtered rows
  + **Covering indexes** include all query columns (index-only scan)
  + Index **foreign keys** for faster joins
  + Index columns in **WHERE, JOIN, ORDER BY, GROUP BY**
  + Don't index **small tables** or **low cardinality** columns
  + Use **EXPLAIN** to verify index usage
  + Monitor and **remove unused indexes**
  + **Rebuild indexes** periodically
  + Balance **read performance** vs **write performance**
  + Aim for **3-5 indexes per table**

</details>

## Transactions

<details>
  <summary>What are Transactions?</summary>
  <br/>

  **Transactions** are sequences of database operations that are treated as a single unit of work.

  **Key characteristics:**
  + **Atomicity** - All operations succeed or all fail (no partial completion)
  + **Consistency** - Database moves from one valid state to another
  + **Isolation** - Concurrent transactions don't interfere with each other
  + **Durability** - Committed changes persist even after system failure

  **Basic transaction commands:**

  ```sql
  -- Start transaction
  BEGIN;  -- or START TRANSACTION;
  
  -- Execute operations
  UPDATE accounts SET balance = balance - 100 WHERE id = 1;
  UPDATE accounts SET balance = balance + 100 WHERE id = 2;
  
  -- Commit (save changes)
  COMMIT;
  
  -- Or rollback (undo changes)
  ROLLBACK;
  ```

  **Why use transactions:**
  + Ensure data integrity
  + Handle errors gracefully
  + Maintain consistency across multiple operations
  + Prevent partial updates
  + Support concurrent access

</details>

<details>
  <summary>ACID Properties</summary>
  <br/>

  **ACID** ensures reliable transaction processing.

  **Atomicity:**

  ```sql
  -- Transfer money between accounts
  BEGIN;
  
  UPDATE accounts SET balance = balance - 100 WHERE id = 1;
  -- If this fails, the entire transaction is rolled back
  UPDATE accounts SET balance = balance + 100 WHERE id = 2;
  
  COMMIT;
  
  -- Either both updates happen, or neither happens
  -- No partial transfer (e.g., money deducted but not credited)
  ```

  **Consistency:**

  ```sql
  -- Constraint: balance cannot be negative
  ALTER TABLE accounts ADD CONSTRAINT check_balance CHECK (balance >= 0);
  
  BEGIN;
  
  UPDATE accounts SET balance = balance - 100 WHERE id = 1;
  -- If balance would become negative, transaction fails
  -- Database remains in consistent state
  
  COMMIT;
  ```

  **Isolation:**

  ```sql
  -- Transaction 1
  BEGIN;
  UPDATE accounts SET balance = balance - 100 WHERE id = 1;
  -- Transaction 2 cannot see this change until COMMIT
  COMMIT;
  
  -- Transaction 2 (running concurrently)
  BEGIN;
  SELECT balance FROM accounts WHERE id = 1;
  -- Sees old balance until Transaction 1 commits
  COMMIT;
  ```

  **Durability:**

  ```sql
  BEGIN;
  
  INSERT INTO orders (user_id, total) VALUES (1, 99.99);
  
  COMMIT;
  -- Once committed, data persists even if:
  -- - Database crashes
  -- - Server restarts
  -- - Power failure occurs
  ```

  **ACID violations and consequences:**

  | Property | Violation | Consequence |
  |----------|-----------|-------------|
  | Atomicity | Partial update | Inconsistent data (e.g., money deducted but not credited) |
  | Consistency | Constraint violation | Invalid data state (e.g., negative balance) |
  | Isolation | Dirty read | Reading uncommitted changes from other transactions |
  | Durability | Data loss | Committed data disappears after crash |

</details>

<details>
  <summary>Transaction Commands</summary>
  <br/>

  **BEGIN / START TRANSACTION:**

  ```sql
  -- Start transaction (PostgreSQL, MySQL)
  BEGIN;
  
  -- Alternative syntax
  START TRANSACTION;
  
  -- With options (PostgreSQL)
  BEGIN ISOLATION LEVEL READ COMMITTED;
  BEGIN READ WRITE;
  BEGIN READ ONLY;
  ```

  **COMMIT:**

  ```sql
  -- Save all changes permanently
  BEGIN;
  
  INSERT INTO users (username, email) VALUES ('john', 'john@example.com');
  UPDATE users SET status = 'active' WHERE id = 1;
  DELETE FROM users WHERE status = 'deleted';
  
  COMMIT;  -- All changes are saved
  ```

  **ROLLBACK:**

  ```sql
  -- Undo all changes
  BEGIN;
  
  UPDATE accounts SET balance = balance - 100 WHERE id = 1;
  UPDATE accounts SET balance = balance + 100 WHERE id = 2;
  
  -- Something went wrong, undo everything
  ROLLBACK;  -- Both updates are undone
  ```

  **SAVEPOINT:**

  ```sql
  -- Create checkpoint within transaction
  BEGIN;
  
  INSERT INTO orders (user_id, total) VALUES (1, 99.99);
  
  SAVEPOINT order_created;
  
  INSERT INTO order_items (order_id, product_id, quantity) 
  VALUES (1, 101, 2);
  
  -- Rollback to savepoint (keeps order, removes order_items)
  ROLLBACK TO SAVEPOINT order_created;
  
  -- Or commit everything
  COMMIT;
  ```

  **RELEASE SAVEPOINT:**

  ```sql
  BEGIN;
  
  INSERT INTO users (username) VALUES ('john');
  SAVEPOINT user_created;
  
  INSERT INTO profiles (user_id, bio) VALUES (1, 'Bio text');
  
  -- Release savepoint (can no longer rollback to it)
  RELEASE SAVEPOINT user_created;
  
  COMMIT;
  ```

</details>

<details>
  <summary>Isolation Levels</summary>
  <br/>

  **Isolation levels** control how transactions interact with each other.

  **Isolation levels (from weakest to strongest):**

  | Level | Dirty Read | Non-Repeatable Read | Phantom Read | Performance |
  |-------|------------|---------------------|--------------|-------------|
  | READ UNCOMMITTED | ✅ Possible | ✅ Possible | ✅ Possible | Fastest |
  | READ COMMITTED | ❌ Prevented | ✅ Possible | ✅ Possible | Fast |
  | REPEATABLE READ | ❌ Prevented | ❌ Prevented | ✅ Possible | Slower |
  | SERIALIZABLE | ❌ Prevented | ❌ Prevented | ❌ Prevented | Slowest |

  **READ UNCOMMITTED:**

  ```sql
  -- Transaction 1
  BEGIN;
  UPDATE accounts SET balance = 1000 WHERE id = 1;
  -- Not committed yet
  
  -- Transaction 2 (READ UNCOMMITTED)
  SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;
  BEGIN;
  SELECT balance FROM accounts WHERE id = 1;
  -- Sees 1000 (uncommitted change) - DIRTY READ
  COMMIT;
  
  -- Transaction 1 rolls back
  ROLLBACK;  -- Balance is NOT 1000!
  ```

  **READ COMMITTED (default in PostgreSQL, Oracle):**

  ```sql
  -- Transaction 1
  BEGIN;
  UPDATE accounts SET balance = 1000 WHERE id = 1;
  -- Not committed yet
  
  -- Transaction 2 (READ COMMITTED)
  SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
  BEGIN;
  SELECT balance FROM accounts WHERE id = 1;
  -- Sees old balance (e.g., 500) - NO DIRTY READ
  
  -- Transaction 1 commits
  COMMIT;
  
  -- Transaction 2 reads again
  SELECT balance FROM accounts WHERE id = 1;
  -- Now sees 1000 - NON-REPEATABLE READ
  COMMIT;
  ```

  **REPEATABLE READ (default in MySQL):**

  ```sql
  -- Transaction 1
  SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;
  BEGIN;
  SELECT balance FROM accounts WHERE id = 1;  -- Sees 500
  
  -- Transaction 2
  BEGIN;
  UPDATE accounts SET balance = 1000 WHERE id = 1;
  COMMIT;
  
  -- Transaction 1 reads again
  SELECT balance FROM accounts WHERE id = 1;
  -- Still sees 500 - NO NON-REPEATABLE READ
  COMMIT;
  ```

  **SERIALIZABLE:**

  ```sql
  -- Transaction 1
  SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
  BEGIN;
  SELECT COUNT(*) FROM accounts WHERE balance > 100;  -- Returns 5
  
  -- Transaction 2
  BEGIN;
  INSERT INTO accounts (balance) VALUES (200);
  COMMIT;
  
  -- Transaction 1 reads again
  SELECT COUNT(*) FROM accounts WHERE balance > 100;
  -- Still returns 5 - NO PHANTOM READ
  COMMIT;
  ```

  **Set isolation level:**

  ```sql
  -- PostgreSQL
  SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
  SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;
  SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
  
  -- MySQL
  SET SESSION TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;
  SET SESSION TRANSACTION ISOLATION LEVEL READ COMMITTED;
  SET SESSION TRANSACTION ISOLATION LEVEL REPEATABLE READ;
  SET SESSION TRANSACTION ISOLATION LEVEL SERIALIZABLE;
  
  -- For single transaction
  BEGIN TRANSACTION ISOLATION LEVEL SERIALIZABLE;
  ```

</details>

<details>
  <summary>Concurrency Problems</summary>
  <br/>

  **Dirty Read:**

  ```sql
  -- Transaction 1
  BEGIN;
  UPDATE products SET stock = 0 WHERE id = 1;
  -- Not committed
  
  -- Transaction 2 (READ UNCOMMITTED)
  BEGIN;
  SELECT stock FROM products WHERE id = 1;  -- Sees 0
  -- Decides not to show product to customer
  COMMIT;
  
  -- Transaction 1 rolls back
  ROLLBACK;  -- Stock is NOT 0!
  -- Product should have been shown to customer
  ```

  **Non-Repeatable Read:**

  ```sql
  -- Transaction 1
  BEGIN;
  SELECT price FROM products WHERE id = 1;  -- Sees $100
  
  -- Do some calculations...
  
  -- Transaction 2
  BEGIN;
  UPDATE products SET price = 150 WHERE id = 1;
  COMMIT;
  
  -- Transaction 1 reads again
  SELECT price FROM products WHERE id = 1;  -- Now sees $150
  -- Calculations are now incorrect!
  COMMIT;
  ```

  **Phantom Read:**

  ```sql
  -- Transaction 1
  BEGIN;
  SELECT COUNT(*) FROM orders WHERE status = 'pending';  -- Returns 10
  
  -- Calculate average...
  
  -- Transaction 2
  BEGIN;
  INSERT INTO orders (status) VALUES ('pending');
  COMMIT;
  
  -- Transaction 1 reads again
  SELECT COUNT(*) FROM orders WHERE status = 'pending';  -- Returns 11
  -- Average calculation is now incorrect!
  COMMIT;
  ```

  **Lost Update:**

  ```sql
  -- Transaction 1
  BEGIN;
  SELECT stock FROM products WHERE id = 1;  -- Sees 10
  -- Calculate new stock: 10 - 3 = 7
  
  -- Transaction 2
  BEGIN;
  SELECT stock FROM products WHERE id = 1;  -- Sees 10
  -- Calculate new stock: 10 - 5 = 5
  UPDATE products SET stock = 5 WHERE id = 1;
  COMMIT;
  
  -- Transaction 1 continues
  UPDATE products SET stock = 7 WHERE id = 1;  -- Overwrites Transaction 2's update!
  COMMIT;
  -- Stock should be 2 (10 - 3 - 5), but it's 7!
  ```

  **Solution: Use locking or optimistic concurrency:**

  ```sql
  -- Pessimistic locking (FOR UPDATE)
  BEGIN;
  SELECT stock FROM products WHERE id = 1 FOR UPDATE;  -- Locks row
  -- Other transactions must wait
  UPDATE products SET stock = stock - 3 WHERE id = 1;
  COMMIT;
  
  -- Optimistic locking (version column)
  BEGIN;
  SELECT stock, version FROM products WHERE id = 1;  -- stock=10, version=1
  UPDATE products 
  SET stock = 7, version = 2 
  WHERE id = 1 AND version = 1;  -- Only updates if version hasn't changed
  -- If version changed, retry transaction
  COMMIT;
  ```

</details>

<details>
  <summary>Locking Mechanisms</summary>
  <br/>

  **Pessimistic Locking (FOR UPDATE):**

  ```sql
  -- Lock row for update
  BEGIN;
  SELECT * FROM accounts WHERE id = 1 FOR UPDATE;
  -- Other transactions cannot read or update this row
  UPDATE accounts SET balance = balance - 100 WHERE id = 1;
  COMMIT;
  ```

  **FOR UPDATE NOWAIT:**

  ```sql
  -- Don't wait if row is locked
  BEGIN;
  SELECT * FROM accounts WHERE id = 1 FOR UPDATE NOWAIT;
  -- If row is locked, immediately throws error
  -- Instead of waiting
  COMMIT;
  ```

  **FOR UPDATE SKIP LOCKED:**

  ```sql
  -- Skip locked rows
  BEGIN;
  SELECT * FROM tasks 
  WHERE status = 'pending' 
  ORDER BY created_at 
  LIMIT 10
  FOR UPDATE SKIP LOCKED;
  -- Returns only unlocked rows
  -- Useful for job queues
  COMMIT;
  ```

  **FOR SHARE (Shared Lock):**

  ```sql
  -- Allow other transactions to read, but not update
  BEGIN;
  SELECT * FROM products WHERE id = 1 FOR SHARE;
  -- Other transactions can SELECT FOR SHARE
  -- But cannot UPDATE or DELETE
  COMMIT;
  ```

  **Optimistic Locking (Version Column):**

  ```sql
  -- Add version column
  ALTER TABLE products ADD COLUMN version INT DEFAULT 1;
  
  -- Read with version
  SELECT id, name, price, version FROM products WHERE id = 1;
  -- Returns: id=1, name='Product', price=100, version=5
  
  -- Update with version check
  UPDATE products 
  SET price = 120, version = version + 1
  WHERE id = 1 AND version = 5;
  
  -- If version changed (another transaction updated), no rows affected
  -- Application should retry
  ```

  **Table-Level Locking:**

  ```sql
  -- Lock entire table
  BEGIN;
  LOCK TABLE accounts IN EXCLUSIVE MODE;
  -- No other transaction can read or write
  UPDATE accounts SET balance = balance * 1.05;
  COMMIT;
  
  -- Lock modes:
  -- ACCESS SHARE - Allows reads, blocks writes
  -- ROW SHARE - Allows reads and row locks
  -- ROW EXCLUSIVE - Allows reads, blocks table locks
  -- SHARE - Allows reads, blocks writes
  -- EXCLUSIVE - Blocks all access
  ```

</details>

<details>
  <summary>Deadlocks</summary>
  <br/>

  **What is a deadlock?**

  Two or more transactions waiting for each other to release locks.

  **Deadlock example:**

  ```sql
  -- Transaction 1
  BEGIN;
  UPDATE accounts SET balance = balance - 100 WHERE id = 1;  -- Locks account 1
  -- Waiting for lock on account 2...
  UPDATE accounts SET balance = balance + 100 WHERE id = 2;  -- Blocked!
  
  -- Transaction 2 (running concurrently)
  BEGIN;
  UPDATE accounts SET balance = balance - 50 WHERE id = 2;   -- Locks account 2
  -- Waiting for lock on account 1...
  UPDATE accounts SET balance = balance + 50 WHERE id = 1;   -- Blocked!
  
  -- DEADLOCK! Both transactions waiting for each other
  -- Database detects deadlock and aborts one transaction
  ```

  **Deadlock detection:**

  ```sql
  -- PostgreSQL: Check for deadlocks
  SELECT * FROM pg_stat_activity WHERE wait_event_type = 'Lock';
  
  -- MySQL: Check for deadlocks
  SHOW ENGINE INNODB STATUS;
  ```

  **Preventing deadlocks:**

  ```sql
  -- 1. Always access resources in same order
  -- Transaction 1
  BEGIN;
  UPDATE accounts SET balance = balance - 100 WHERE id = 1;  -- Lock 1 first
  UPDATE accounts SET balance = balance + 100 WHERE id = 2;  -- Then lock 2
  COMMIT;
  
  -- Transaction 2
  BEGIN;
  UPDATE accounts SET balance = balance - 50 WHERE id = 1;   -- Lock 1 first
  UPDATE accounts SET balance = balance + 50 WHERE id = 2;   -- Then lock 2
  COMMIT;
  -- No deadlock! Both access in same order
  
  -- 2. Keep transactions short
  BEGIN;
  UPDATE accounts SET balance = balance - 100 WHERE id = 1;
  COMMIT;  -- Release lock quickly
  
  -- 3. Use lower isolation levels
  SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
  
  -- 4. Use timeouts
  SET lock_timeout = '5s';  -- PostgreSQL
  SET innodb_lock_wait_timeout = 5;  -- MySQL
  
  -- 5. Use NOWAIT
  BEGIN;
  SELECT * FROM accounts WHERE id = 1 FOR UPDATE NOWAIT;
  -- Fails immediately if locked, instead of waiting
  COMMIT;
  ```

  **Handling deadlocks in application:**

  ```java
  // Retry logic for deadlocks
  int maxRetries = 3;
  for (int i = 0; i < maxRetries; i++) {
      try {
          // Execute transaction
          transferMoney(fromAccount, toAccount, amount);
          break;  // Success
      } catch (DeadlockException e) {
          if (i == maxRetries - 1) {
              throw e;  // Max retries reached
          }
          Thread.sleep(100 * (i + 1));  // Exponential backoff
      }
  }
  ```

</details>

<details>
  <summary>Transaction Best Practices</summary>
  <br/>

  ```sql
  -- ✅ DO: Keep transactions short
  BEGIN;
  UPDATE accounts SET balance = balance - 100 WHERE id = 1;
  UPDATE accounts SET balance = balance + 100 WHERE id = 2;
  COMMIT;  -- Quick commit
  
  -- ❌ DON'T: Include slow operations in transactions
  BEGIN;
  UPDATE accounts SET balance = balance - 100 WHERE id = 1;
  -- Call external API (slow!)
  -- Send email (slow!)
  COMMIT;  -- Holds locks for too long
  
  -- ✅ DO: Use appropriate isolation level
  -- For most cases, READ COMMITTED is sufficient
  SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
  
  -- ❌ DON'T: Use SERIALIZABLE unless necessary
  SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;  -- Slowest
  
  -- ✅ DO: Handle errors and rollback
  BEGIN;
  UPDATE accounts SET balance = balance - 100 WHERE id = 1;
  -- Check if balance is sufficient
  IF (SELECT balance FROM accounts WHERE id = 1) < 0 THEN
      ROLLBACK;
  ELSE
      COMMIT;
  END IF;
  
  -- ✅ DO: Use savepoints for partial rollback
  BEGIN;
  INSERT INTO orders (user_id, total) VALUES (1, 99.99);
  SAVEPOINT order_created;
  
  INSERT INTO order_items (order_id, product_id) VALUES (1, 101);
  -- If this fails, rollback to savepoint
  ROLLBACK TO SAVEPOINT order_created;
  
  COMMIT;
  
  -- ✅ DO: Access resources in consistent order
  -- Always lock accounts in ascending ID order
  BEGIN;
  UPDATE accounts SET balance = balance - 100 WHERE id = 1;
  UPDATE accounts SET balance = balance + 100 WHERE id = 2;
  COMMIT;
  
  -- ✅ DO: Use FOR UPDATE for pessimistic locking
  BEGIN;
  SELECT * FROM accounts WHERE id = 1 FOR UPDATE;
  UPDATE accounts SET balance = balance - 100 WHERE id = 1;
  COMMIT;
  
  -- ✅ DO: Use version column for optimistic locking
  UPDATE products 
  SET stock = stock - 1, version = version + 1
  WHERE id = 1 AND version = 5;
  
  -- ✅ DO: Set lock timeouts
  SET lock_timeout = '5s';  -- PostgreSQL
  SET innodb_lock_wait_timeout = 5;  -- MySQL
  
  -- ✅ DO: Use indexes on frequently locked columns
  CREATE INDEX idx_accounts_id ON accounts(id);
  
  -- ✅ DO: Monitor long-running transactions
  SELECT * FROM pg_stat_activity 
  WHERE state = 'active' 
  AND xact_start < NOW() - INTERVAL '1 minute';
  
  -- ❌ DON'T: Use SELECT * in transactions
  SELECT * FROM accounts WHERE id = 1;  -- Locks all columns
  
  -- ✅ DO: Select only needed columns
  SELECT id, balance FROM accounts WHERE id = 1;
  
  -- ✅ DO: Use batch operations when possible
  -- Instead of multiple transactions
  BEGIN;
  INSERT INTO users (username) VALUES ('user1');
  COMMIT;
  BEGIN;
  INSERT INTO users (username) VALUES ('user2');
  COMMIT;
  
  -- Better: Single transaction
  BEGIN;
  INSERT INTO users (username) VALUES ('user1'), ('user2');
  COMMIT;
  
  -- ✅ DO: Use connection pooling
  -- Reuse database connections instead of creating new ones
  
  -- ✅ DO: Log transaction details for debugging
  -- Transaction ID, start time, duration, operations
  ```

  **Summary:**
  + **Transactions** ensure ACID properties (Atomicity, Consistency, Isolation, Durability)
  + Use **BEGIN/COMMIT/ROLLBACK** to control transactions
  + Choose appropriate **isolation level** (READ COMMITTED is usually sufficient)
  + Use **FOR UPDATE** for pessimistic locking
  + Use **version column** for optimistic locking
  + **Prevent deadlocks** by accessing resources in consistent order
  + Keep transactions **short** to minimize lock contention
  + Use **savepoints** for partial rollback
  + Set **lock timeouts** to prevent indefinite waiting
  + **Monitor** long-running transactions
  + Use **batch operations** when possible
  + Always **handle errors** and rollback when necessary

</details>

## Normalization

<details>
  <summary>What is Normalization?</summary>
  <br/>

  **Normalization** is the process of organizing database tables to reduce redundancy and improve data integrity.

  **Goals of normalization:**
  + Eliminate redundant data
  + Ensure data dependencies make sense
  + Reduce data anomalies (insert, update, delete)
  + Improve data integrity
  + Optimize storage

  **Normal forms:**
  + **1NF (First Normal Form)** - Atomic values, no repeating groups
  + **2NF (Second Normal Form)** - 1NF + no partial dependencies
  + **3NF (Third Normal Form)** - 2NF + no transitive dependencies
  + **BCNF (Boyce-Codd Normal Form)** - Stricter version of 3NF
  + **4NF, 5NF** - Advanced forms (rarely used in practice)

  **Trade-offs:**
  + **Pros**: Less redundancy, better integrity, easier updates
  + **Cons**: More joins, potentially slower queries, complex schema

</details>

<details>
  <summary>First Normal Form (1NF)</summary>
  <br/>

  **1NF Rules:**
  + Each column contains atomic (indivisible) values
  + Each column contains values of single type
  + Each column has unique name
  + Order of rows doesn't matter

  **Violation example (NOT in 1NF):**

  ```sql
  -- BAD: Multiple values in single column
  CREATE TABLE orders (
      id INT PRIMARY KEY,
      customer_name VARCHAR(100),
      products VARCHAR(500)  -- "Laptop, Mouse, Keyboard"
  );
  
  INSERT INTO orders VALUES 
      (1, 'John', 'Laptop, Mouse, Keyboard'),
      (2, 'Jane', 'Phone, Charger');
  
  -- Problems:
  -- - Cannot easily query for specific product
  -- - Cannot enforce product constraints
  -- - Difficult to update individual products
  ```

  **1NF Solution:**

  ```sql
  -- GOOD: Separate table for products
  CREATE TABLE orders (
      id INT PRIMARY KEY,
      customer_name VARCHAR(100)
  );
  
  CREATE TABLE order_items (
      id INT PRIMARY KEY,
      order_id INT,
      product_name VARCHAR(100),
      FOREIGN KEY (order_id) REFERENCES orders(id)
  );
  
  INSERT INTO orders VALUES 
      (1, 'John'),
      (2, 'Jane');
  
  INSERT INTO order_items VALUES
      (1, 1, 'Laptop'),
      (2, 1, 'Mouse'),
      (3, 1, 'Keyboard'),
      (4, 2, 'Phone'),
      (5, 2, 'Charger');
  
  -- Now we can:
  -- - Query for specific products
  -- - Add/remove products easily
  -- - Enforce product constraints
  ```

  **Another violation (repeating groups):**

  ```sql
  -- BAD: Repeating columns
  CREATE TABLE orders (
      id INT PRIMARY KEY,
      customer_name VARCHAR(100),
      product1 VARCHAR(100),
      product2 VARCHAR(100),
      product3 VARCHAR(100)
  );
  
  -- Problems:
  -- - Limited to 3 products
  -- - Wasted space if fewer products
  -- - Difficult to query all products
  ```

  **1NF Solution:**

  ```sql
  -- GOOD: Use separate rows
  CREATE TABLE orders (
      id INT PRIMARY KEY,
      customer_name VARCHAR(100)
  );
  
  CREATE TABLE order_items (
      order_id INT,
      product_name VARCHAR(100),
      PRIMARY KEY (order_id, product_name),
      FOREIGN KEY (order_id) REFERENCES orders(id)
  );
  ```

</details>

<details>
  <summary>Second Normal Form (2NF)</summary>
  <br/>

  **2NF Rules:**
  + Must be in 1NF
  + No partial dependencies (non-key columns must depend on entire primary key)

  **Partial dependency:** Non-key column depends on part of composite primary key.

  **Violation example (NOT in 2NF):**

  ```sql
  -- BAD: Partial dependency
  CREATE TABLE order_items (
      order_id INT,
      product_id INT,
      product_name VARCHAR(100),      -- Depends only on product_id
      product_price DECIMAL(10, 2),   -- Depends only on product_id
      quantity INT,                    -- Depends on both order_id and product_id
      PRIMARY KEY (order_id, product_id)
  );
  
  INSERT INTO order_items VALUES
      (1, 101, 'Laptop', 999.99, 1),
      (1, 102, 'Mouse', 29.99, 2),
      (2, 101, 'Laptop', 999.99, 1);  -- Duplicate product info!
  
  -- Problems:
  -- - Product name/price duplicated for each order
  -- - If product price changes, must update multiple rows
  -- - Inconsistency risk (different prices for same product)
  ```

  **2NF Solution:**

  ```sql
  -- GOOD: Separate products table
  CREATE TABLE products (
      id INT PRIMARY KEY,
      name VARCHAR(100),
      price DECIMAL(10, 2)
  );
  
  CREATE TABLE order_items (
      order_id INT,
      product_id INT,
      quantity INT,
      PRIMARY KEY (order_id, product_id),
      FOREIGN KEY (product_id) REFERENCES products(id)
  );
  
  INSERT INTO products VALUES
      (101, 'Laptop', 999.99),
      (102, 'Mouse', 29.99);
  
  INSERT INTO order_items VALUES
      (1, 101, 1),
      (1, 102, 2),
      (2, 101, 1);
  
  -- Benefits:
  -- - No duplicate product info
  -- - Update price in one place
  -- - Consistent product data
  ```

  **Another example:**

  ```sql
  -- BAD: Student courses with partial dependency
  CREATE TABLE student_courses (
      student_id INT,
      course_id INT,
      student_name VARCHAR(100),    -- Depends only on student_id
      student_email VARCHAR(100),   -- Depends only on student_id
      course_name VARCHAR(100),     -- Depends only on course_id
      grade VARCHAR(2),             -- Depends on both
      PRIMARY KEY (student_id, course_id)
  );
  
  -- GOOD: Separate tables
  CREATE TABLE students (
      id INT PRIMARY KEY,
      name VARCHAR(100),
      email VARCHAR(100)
  );
  
  CREATE TABLE courses (
      id INT PRIMARY KEY,
      name VARCHAR(100)
  );
  
  CREATE TABLE enrollments (
      student_id INT,
      course_id INT,
      grade VARCHAR(2),
      PRIMARY KEY (student_id, course_id),
      FOREIGN KEY (student_id) REFERENCES students(id),
      FOREIGN KEY (course_id) REFERENCES courses(id)
  );
  ```

</details>

<details>
  <summary>Third Normal Form (3NF)</summary>
  <br/>

  **3NF Rules:**
  + Must be in 2NF
  + No transitive dependencies (non-key columns must not depend on other non-key columns)

  **Transitive dependency:** A → B → C (A determines B, B determines C)

  **Violation example (NOT in 3NF):**

  ```sql
  -- BAD: Transitive dependency
  CREATE TABLE employees (
      id INT PRIMARY KEY,
      name VARCHAR(100),
      department_id INT,
      department_name VARCHAR(100),    -- Depends on department_id (transitive)
      department_location VARCHAR(100) -- Depends on department_id (transitive)
  );
  
  INSERT INTO employees VALUES
      (1, 'John', 10, 'Sales', 'New York'),
      (2, 'Jane', 10, 'Sales', 'New York'),  -- Duplicate department info
      (3, 'Bob', 20, 'IT', 'San Francisco');
  
  -- Problems:
  -- - Department info duplicated for each employee
  -- - If department location changes, must update multiple rows
  -- - Inconsistency risk (different locations for same department)
  ```

  **3NF Solution:**

  ```sql
  -- GOOD: Separate departments table
  CREATE TABLE departments (
      id INT PRIMARY KEY,
      name VARCHAR(100),
      location VARCHAR(100)
  );
  
  CREATE TABLE employees (
      id INT PRIMARY KEY,
      name VARCHAR(100),
      department_id INT,
      FOREIGN KEY (department_id) REFERENCES departments(id)
  );
  
  INSERT INTO departments VALUES
      (10, 'Sales', 'New York'),
      (20, 'IT', 'San Francisco');
  
  INSERT INTO employees VALUES
      (1, 'John', 10),
      (2, 'Jane', 10),
      (3, 'Bob', 20);
  
  -- Benefits:
  -- - No duplicate department info
  -- - Update department in one place
  -- - Consistent department data
  ```

  **Another example:**

  ```sql
  -- BAD: Customer orders with transitive dependency
  CREATE TABLE orders (
      id INT PRIMARY KEY,
      customer_id INT,
      customer_name VARCHAR(100),
      customer_email VARCHAR(100),
      customer_city VARCHAR(100),
      customer_zip VARCHAR(10),
      city_state VARCHAR(50)  -- Depends on customer_city (transitive)
  );
  
  -- GOOD: Separate tables
  CREATE TABLE cities (
      name VARCHAR(100) PRIMARY KEY,
      state VARCHAR(50)
  );
  
  CREATE TABLE customers (
      id INT PRIMARY KEY,
      name VARCHAR(100),
      email VARCHAR(100),
      city VARCHAR(100),
      zip VARCHAR(10),
      FOREIGN KEY (city) REFERENCES cities(name)
  );
  
  CREATE TABLE orders (
      id INT PRIMARY KEY,
      customer_id INT,
      FOREIGN KEY (customer_id) REFERENCES customers(id)
  );
  ```

</details>

<details>
  <summary>Boyce-Codd Normal Form (BCNF)</summary>
  <br/>

  **BCNF Rules:**
  + Must be in 3NF
  + Every determinant must be a candidate key

  **BCNF is stricter than 3NF** - handles edge cases where 3NF allows anomalies.

  **Violation example (NOT in BCNF):**

  ```sql
  -- BAD: Professor teaches course in specific room
  CREATE TABLE teaching (
      professor VARCHAR(100),
      course VARCHAR(100),
      room VARCHAR(50),
      PRIMARY KEY (professor, course)
  );
  
  -- Constraint: Each professor uses only one room
  -- But room is not part of primary key
  
  INSERT INTO teaching VALUES
      ('Dr. Smith', 'Math 101', 'Room A'),
      ('Dr. Smith', 'Math 201', 'Room A'),  -- Same room
      ('Dr. Jones', 'CS 101', 'Room B');
  
  -- Problem: Can insert ('Dr. Smith', 'Physics 101', 'Room C')
  -- Violates constraint that Dr. Smith uses Room A
  ```

  **BCNF Solution:**

  ```sql
  -- GOOD: Separate professor-room relationship
  CREATE TABLE professor_rooms (
      professor VARCHAR(100) PRIMARY KEY,
      room VARCHAR(50)
  );
  
  CREATE TABLE teaching (
      professor VARCHAR(100),
      course VARCHAR(100),
      PRIMARY KEY (professor, course),
      FOREIGN KEY (professor) REFERENCES professor_rooms(professor)
  );
  
  INSERT INTO professor_rooms VALUES
      ('Dr. Smith', 'Room A'),
      ('Dr. Jones', 'Room B');
  
  INSERT INTO teaching VALUES
      ('Dr. Smith', 'Math 101'),
      ('Dr. Smith', 'Math 201'),
      ('Dr. Jones', 'CS 101');
  
  -- Now constraint is enforced by foreign key
  ```

  **Note:** Most databases in 3NF are also in BCNF. BCNF violations are rare in practice.

</details>

<details>
  <summary>Denormalization</summary>
  <br/>

  **Denormalization** intentionally introduces redundancy to improve query performance.

  **When to denormalize:**
  + Read-heavy workloads (many queries, few updates)
  + Complex joins are too slow
  + Reporting and analytics
  + Caching frequently accessed data
  + Reducing database load

  **Denormalization techniques:**

  **1. Add redundant columns:**

  ```sql
  -- Normalized (3NF)
  CREATE TABLE orders (
      id INT PRIMARY KEY,
      customer_id INT,
      FOREIGN KEY (customer_id) REFERENCES customers(id)
  );
  
  CREATE TABLE customers (
      id INT PRIMARY KEY,
      name VARCHAR(100),
      email VARCHAR(100)
  );
  
  -- Query requires JOIN
  SELECT o.id, c.name, c.email
  FROM orders o
  JOIN customers c ON o.customer_id = c.id;
  
  -- Denormalized (add customer info to orders)
  CREATE TABLE orders (
      id INT PRIMARY KEY,
      customer_id INT,
      customer_name VARCHAR(100),    -- Redundant
      customer_email VARCHAR(100),   -- Redundant
      FOREIGN KEY (customer_id) REFERENCES customers(id)
  );
  
  -- Query without JOIN (faster)
  SELECT id, customer_name, customer_email
  FROM orders;
  
  -- Trade-off: Must update orders when customer info changes
  ```

  **2. Precompute aggregates:**

  ```sql
  -- Normalized
  CREATE TABLE orders (
      id INT PRIMARY KEY,
      customer_id INT,
      total DECIMAL(10, 2)
  );
  
  -- Query requires aggregation
  SELECT customer_id, COUNT(*), SUM(total)
  FROM orders
  GROUP BY customer_id;
  
  -- Denormalized (add summary table)
  CREATE TABLE customer_stats (
      customer_id INT PRIMARY KEY,
      order_count INT,
      total_spent DECIMAL(10, 2),
      last_updated TIMESTAMP
  );
  
  -- Update stats when order is created
  INSERT INTO orders (customer_id, total) VALUES (1, 99.99);
  
  UPDATE customer_stats
  SET order_count = order_count + 1,
      total_spent = total_spent + 99.99,
      last_updated = NOW()
  WHERE customer_id = 1;
  
  -- Query is now simple lookup (faster)
  SELECT order_count, total_spent
  FROM customer_stats
  WHERE customer_id = 1;
  ```

  **3. Materialized views:**

  ```sql
  -- Create materialized view (precomputed query result)
  CREATE MATERIALIZED VIEW customer_order_summary AS
  SELECT 
      c.id,
      c.name,
      COUNT(o.id) AS order_count,
      SUM(o.total) AS total_spent
  FROM customers c
  LEFT JOIN orders o ON c.id = o.customer_id
  GROUP BY c.id, c.name;
  
  -- Query materialized view (fast)
  SELECT * FROM customer_order_summary WHERE id = 1;
  
  -- Refresh periodically
  REFRESH MATERIALIZED VIEW customer_order_summary;
  ```

  **4. Duplicate data across tables:**

  ```sql
  -- Normalized
  CREATE TABLE products (
      id INT PRIMARY KEY,
      name VARCHAR(100),
      category_id INT
  );
  
  CREATE TABLE categories (
      id INT PRIMARY KEY,
      name VARCHAR(100)
  );
  
  -- Denormalized (add category name to products)
  CREATE TABLE products (
      id INT PRIMARY KEY,
      name VARCHAR(100),
      category_id INT,
      category_name VARCHAR(100)  -- Redundant
  );
  
  -- Faster queries, but must update products when category name changes
  ```

  **Denormalization trade-offs:**

  | Aspect | Normalized | Denormalized |
  |--------|-----------|--------------|
  | Storage | Less space | More space |
  | Writes | Faster | Slower (update multiple places) |
  | Reads | Slower (joins) | Faster (no joins) |
  | Consistency | Easier | Harder (must sync) |
  | Maintenance | Simpler | More complex |

  **Best practices:**
  + Start with normalized design
  + Denormalize only when performance issues arise
  + Measure performance before and after
  + Document denormalization decisions
  + Use triggers or application logic to maintain consistency
  + Consider caching instead of denormalization

</details>

<details>
  <summary>Data Anomalies</summary>
  <br/>

  **Anomalies** are problems that occur in poorly normalized databases.

  **Insert Anomaly:**

  ```sql
  -- BAD: Cannot insert department without employee
  CREATE TABLE employees (
      id INT PRIMARY KEY,
      name VARCHAR(100),
      department_id INT,
      department_name VARCHAR(100),
      department_location VARCHAR(100)
  );
  
  -- Cannot insert new department until we have an employee
  -- INSERT INTO employees (department_id, department_name, department_location)
  -- VALUES (30, 'Marketing', 'Chicago');  -- ERROR: name cannot be NULL
  
  -- GOOD: Separate departments table
  CREATE TABLE departments (
      id INT PRIMARY KEY,
      name VARCHAR(100),
      location VARCHAR(100)
  );
  
  -- Can insert department without employees
  INSERT INTO departments VALUES (30, 'Marketing', 'Chicago');
  ```

  **Update Anomaly:**

  ```sql
  -- BAD: Must update multiple rows
  CREATE TABLE employees (
      id INT PRIMARY KEY,
      name VARCHAR(100),
      department_id INT,
      department_name VARCHAR(100),
      department_location VARCHAR(100)
  );
  
  INSERT INTO employees VALUES
      (1, 'John', 10, 'Sales', 'New York'),
      (2, 'Jane', 10, 'Sales', 'New York'),
      (3, 'Bob', 10, 'Sales', 'New York');
  
  -- To change department location, must update all employees
  UPDATE employees 
  SET department_location = 'Boston' 
  WHERE department_id = 10;  -- Updates 3 rows
  
  -- Risk: Might miss some rows, causing inconsistency
  
  -- GOOD: Update one row
  CREATE TABLE departments (
      id INT PRIMARY KEY,
      name VARCHAR(100),
      location VARCHAR(100)
  );
  
  UPDATE departments 
  SET location = 'Boston' 
  WHERE id = 10;  -- Updates 1 row
  ```

  **Delete Anomaly:**

  ```sql
  -- BAD: Deleting last employee deletes department info
  CREATE TABLE employees (
      id INT PRIMARY KEY,
      name VARCHAR(100),
      department_id INT,
      department_name VARCHAR(100),
      department_location VARCHAR(100)
  );
  
  INSERT INTO employees VALUES
      (1, 'John', 10, 'Sales', 'New York');
  
  -- Delete last employee in Sales department
  DELETE FROM employees WHERE id = 1;
  -- Department info is lost!
  
  -- GOOD: Department info preserved
  CREATE TABLE departments (
      id INT PRIMARY KEY,
      name VARCHAR(100),
      location VARCHAR(100)
  );
  
  CREATE TABLE employees (
      id INT PRIMARY KEY,
      name VARCHAR(100),
      department_id INT,
      FOREIGN KEY (department_id) REFERENCES departments(id)
  );
  
  DELETE FROM employees WHERE id = 1;
  -- Department still exists in departments table
  ```

</details>

<details>
  <summary>Normalization Best Practices</summary>
  <br/>

  ```sql
  -- ✅ DO: Start with normalized design (3NF)
  CREATE TABLE customers (
      id INT PRIMARY KEY,
      name VARCHAR(100),
      email VARCHAR(100)
  );
  
  CREATE TABLE orders (
      id INT PRIMARY KEY,
      customer_id INT,
      total DECIMAL(10, 2),
      FOREIGN KEY (customer_id) REFERENCES customers(id)
  );
  
  -- ✅ DO: Use foreign keys to enforce relationships
  ALTER TABLE orders
  ADD CONSTRAINT fk_customer
  FOREIGN KEY (customer_id) REFERENCES customers(id);
  
  -- ✅ DO: Avoid redundant data
  -- BAD: Store customer name in orders
  -- GOOD: Store customer_id and JOIN when needed
  
  -- ✅ DO: Use surrogate keys (auto-increment IDs)
  CREATE TABLE products (
      id SERIAL PRIMARY KEY,  -- Surrogate key
      sku VARCHAR(50) UNIQUE, -- Natural key
      name VARCHAR(100)
  );
  
  -- ✅ DO: Document denormalization decisions
  -- Add comment explaining why data is denormalized
  CREATE TABLE orders (
      id INT PRIMARY KEY,
      customer_id INT,
      customer_name VARCHAR(100),  -- Denormalized for performance
      -- Reason: Avoid JOIN in 90% of queries
      -- Maintained by: trigger on customers table
      FOREIGN KEY (customer_id) REFERENCES customers(id)
  );
  
  -- ✅ DO: Use triggers to maintain denormalized data
  CREATE TRIGGER update_order_customer_name
  AFTER UPDATE ON customers
  FOR EACH ROW
  BEGIN
      UPDATE orders
      SET customer_name = NEW.name
      WHERE customer_id = NEW.id;
  END;
  
  -- ✅ DO: Consider read vs write ratio
  -- High reads, low writes → Consider denormalization
  -- High writes, low reads → Keep normalized
  
  -- ✅ DO: Use indexes on foreign keys
  CREATE INDEX idx_orders_customer_id ON orders(customer_id);
  
  -- ✅ DO: Measure performance before denormalizing
  EXPLAIN ANALYZE
  SELECT o.id, c.name
  FROM orders o
  JOIN customers c ON o.customer_id = c.id;
  
  -- ❌ DON'T: Denormalize prematurely
  -- Start normalized, denormalize only when needed
  
  -- ❌ DON'T: Store calculated values (unless performance critical)
  -- BAD: Store total in orders table
  CREATE TABLE orders (
      id INT PRIMARY KEY,
      subtotal DECIMAL(10, 2),
      tax DECIMAL(10, 2),
      total DECIMAL(10, 2)  -- Calculated: subtotal + tax
  );
  
  -- GOOD: Calculate on the fly
  CREATE TABLE orders (
      id INT PRIMARY KEY,
      subtotal DECIMAL(10, 2),
      tax DECIMAL(10, 2)
  );
  
  SELECT id, subtotal, tax, (subtotal + tax) AS total
  FROM orders;
  
  -- ❌ DON'T: Use composite natural keys as primary keys
  -- BAD: Composite natural key
  CREATE TABLE order_items (
      order_id INT,
      product_sku VARCHAR(50),
      quantity INT,
      PRIMARY KEY (order_id, product_sku)
  );
  
  -- GOOD: Surrogate key
  CREATE TABLE order_items (
      id SERIAL PRIMARY KEY,
      order_id INT,
      product_id INT,
      quantity INT,
      UNIQUE (order_id, product_id)
  );
  
  -- ✅ DO: Use appropriate data types
  CREATE TABLE users (
      id INT PRIMARY KEY,
      email VARCHAR(255),      -- Not TEXT
      age SMALLINT,            -- Not INT (saves space)
      balance DECIMAL(10, 2),  -- Not FLOAT (precision)
      created_at TIMESTAMP     -- Not VARCHAR
  );
  
  -- ✅ DO: Add constraints to enforce data integrity
  CREATE TABLE products (
      id INT PRIMARY KEY,
      name VARCHAR(100) NOT NULL,
      price DECIMAL(10, 2) CHECK (price > 0),
      stock INT CHECK (stock >= 0),
      category_id INT NOT NULL,
      FOREIGN KEY (category_id) REFERENCES categories(id)
  );
  ```

  **Summary:**
  + **1NF** - Atomic values, no repeating groups
  + **2NF** - 1NF + no partial dependencies
  + **3NF** - 2NF + no transitive dependencies
  + **BCNF** - 3NF + every determinant is a candidate key
  + **Normalization** reduces redundancy and improves integrity
  + **Denormalization** improves performance but increases complexity
  + Start with **normalized design** (3NF)
  + **Denormalize** only when performance issues arise
  + Use **foreign keys** to enforce relationships
  + Use **triggers** to maintain denormalized data
  + **Measure performance** before and after denormalization
  + **Document** denormalization decisions
  + Avoid **data anomalies** (insert, update, delete)
  + Use **appropriate data types** and **constraints**

</details>

## Schema Design & Constraints

<details>
  <summary>What is Schema Design?</summary>
  <br/>

  **Schema design** is the process of defining the structure of a database, including tables, columns, relationships, and constraints.

  **Key components:**
  + **Tables** - Store related data
  + **Columns** - Define data attributes
  + **Primary Keys** - Uniquely identify rows
  + **Foreign Keys** - Define relationships between tables
  + **Constraints** - Enforce data integrity rules
  + **Indexes** - Improve query performance

  **Design principles:**
  + Start with requirements (what data to store, what queries to support)
  + Identify entities (users, orders, products)
  + Define relationships (one-to-many, many-to-many)
  + Normalize to 3NF (reduce redundancy)
  + Add constraints (enforce business rules)
  + Create indexes (optimize queries)

  **Example schema:**

  ```sql
  -- E-commerce schema
  CREATE TABLE customers (
      id SERIAL PRIMARY KEY,
      email VARCHAR(255) UNIQUE NOT NULL,
      name VARCHAR(100) NOT NULL,
      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
  );
  
  CREATE TABLE products (
      id SERIAL PRIMARY KEY,
      sku VARCHAR(50) UNIQUE NOT NULL,
      name VARCHAR(200) NOT NULL,
      price DECIMAL(10, 2) NOT NULL CHECK (price > 0),
      stock INT NOT NULL DEFAULT 0 CHECK (stock >= 0)
  );
  
  CREATE TABLE orders (
      id SERIAL PRIMARY KEY,
      customer_id INT NOT NULL,
      status VARCHAR(20) DEFAULT 'pending',
      total DECIMAL(10, 2) NOT NULL,
      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      FOREIGN KEY (customer_id) REFERENCES customers(id)
  );
  
  CREATE TABLE order_items (
      id SERIAL PRIMARY KEY,
      order_id INT NOT NULL,
      product_id INT NOT NULL,
      quantity INT NOT NULL CHECK (quantity > 0),
      price DECIMAL(10, 2) NOT NULL,
      FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
      FOREIGN KEY (product_id) REFERENCES products(id)
  );
  ```

</details>

<details>
  <summary>Primary Keys</summary>
  <br/>

  **Primary keys** uniquely identify each row in a table.

  **Types of primary keys:**

  **1. Surrogate keys (auto-increment IDs):**

  ```sql
  -- PostgreSQL
  CREATE TABLE users (
      id SERIAL PRIMARY KEY,  -- Auto-increment
      username VARCHAR(50)
  );
  
  -- MySQL
  CREATE TABLE users (
      id INT AUTO_INCREMENT PRIMARY KEY,
      username VARCHAR(50)
  );
  
  -- SQL Server
  CREATE TABLE users (
      id INT IDENTITY(1,1) PRIMARY KEY,
      username VARCHAR(50)
  );
  
  -- Pros:
  -- - Simple, consistent
  -- - No business logic dependency
  -- - Easy to reference in foreign keys
  -- - Immutable (never changes)
  
  -- Cons:
  -- - No business meaning
  -- - Extra column
  ```

  **2. Natural keys (business data):**

  ```sql
  -- Email as primary key
  CREATE TABLE users (
      email VARCHAR(255) PRIMARY KEY,
      name VARCHAR(100)
  );
  
  -- SSN as primary key
  CREATE TABLE employees (
      ssn VARCHAR(11) PRIMARY KEY,
      name VARCHAR(100)
  );
  
  -- Pros:
  -- - Has business meaning
  -- - No extra column needed
  
  -- Cons:
  -- - May change (email, phone)
  -- - May be long (affects foreign keys)
  -- - May be composite (multiple columns)
  -- - Privacy concerns (SSN)
  ```

  **3. Composite keys (multiple columns):**

  ```sql
  -- Order items identified by order_id + product_id
  CREATE TABLE order_items (
      order_id INT,
      product_id INT,
      quantity INT,
      PRIMARY KEY (order_id, product_id),
      FOREIGN KEY (order_id) REFERENCES orders(id),
      FOREIGN KEY (product_id) REFERENCES products(id)
  );
  
  -- Student enrollments
  CREATE TABLE enrollments (
      student_id INT,
      course_id INT,
      semester VARCHAR(20),
      grade VARCHAR(2),
      PRIMARY KEY (student_id, course_id, semester)
  );
  
  -- Pros:
  -- - Natural relationship representation
  -- - No extra column
  
  -- Cons:
  -- - Complex foreign key references
  -- - Larger indexes
  -- - More difficult to reference
  ```

  **4. UUID/GUID keys:**

  ```sql
  -- PostgreSQL
  CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
  
  CREATE TABLE users (
      id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
      username VARCHAR(50)
  );
  
  -- Insert generates UUID automatically
  INSERT INTO users (username) VALUES ('john');
  -- id: 550e8400-e29b-41d4-a716-446655440000
  
  -- Pros:
  -- - Globally unique (across databases)
  -- - Can generate client-side
  -- - Good for distributed systems
  -- - No collision risk
  
  -- Cons:
  -- - Larger storage (16 bytes vs 4 bytes for INT)
  -- - Slower indexes
  -- - Not human-readable
  -- - Random order (affects B-tree performance)
  ```

  **Best practices:**

  ```sql
  -- ✅ DO: Use surrogate keys for most tables
  CREATE TABLE users (
      id SERIAL PRIMARY KEY,
      email VARCHAR(255) UNIQUE NOT NULL
  );
  
  -- ✅ DO: Add UNIQUE constraint on natural keys
  CREATE TABLE products (
      id SERIAL PRIMARY KEY,
      sku VARCHAR(50) UNIQUE NOT NULL  -- Natural key as unique
  );
  
  -- ✅ DO: Use composite keys for junction tables
  CREATE TABLE user_roles (
      user_id INT,
      role_id INT,
      PRIMARY KEY (user_id, role_id)
  );
  
  -- ❌ DON'T: Use mutable data as primary key
  CREATE TABLE users (
      email VARCHAR(255) PRIMARY KEY  -- Email can change!
  );
  
  -- ✅ BETTER: Use surrogate key
  CREATE TABLE users (
      id SERIAL PRIMARY KEY,
      email VARCHAR(255) UNIQUE NOT NULL
  );
  
  -- ❌ DON'T: Use long strings as primary key
  CREATE TABLE sessions (
      token VARCHAR(500) PRIMARY KEY  -- Too long!
  );
  
  -- ✅ BETTER: Use hash or surrogate key
  CREATE TABLE sessions (
      id SERIAL PRIMARY KEY,
      token_hash VARCHAR(64) UNIQUE NOT NULL
  );
  ```

</details>

<details>
  <summary>Foreign Keys</summary>
  <br/>

  **Foreign keys** define relationships between tables and enforce referential integrity.

  **Basic foreign key:**

  ```sql
  CREATE TABLE customers (
      id SERIAL PRIMARY KEY,
      name VARCHAR(100)
  );
  
  CREATE TABLE orders (
      id SERIAL PRIMARY KEY,
      customer_id INT NOT NULL,
      total DECIMAL(10, 2),
      FOREIGN KEY (customer_id) REFERENCES customers(id)
  );
  
  -- Valid insert
  INSERT INTO customers (id, name) VALUES (1, 'John');
  INSERT INTO orders (customer_id, total) VALUES (1, 99.99);  -- OK
  
  -- Invalid insert (referential integrity violation)
  INSERT INTO orders (customer_id, total) VALUES (999, 99.99);  -- ERROR
  -- ERROR: foreign key constraint violated
  ```

  **ON DELETE actions:**

  ```sql
  -- CASCADE: Delete child rows when parent is deleted
  CREATE TABLE orders (
      id SERIAL PRIMARY KEY,
      customer_id INT NOT NULL,
      FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE
  );
  
  DELETE FROM customers WHERE id = 1;
  -- All orders for customer 1 are also deleted
  
  -- RESTRICT: Prevent deletion if child rows exist (default)
  CREATE TABLE orders (
      id SERIAL PRIMARY KEY,
      customer_id INT NOT NULL,
      FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE RESTRICT
  );
  
  DELETE FROM customers WHERE id = 1;
  -- ERROR: cannot delete customer with existing orders
  
  -- SET NULL: Set foreign key to NULL when parent is deleted
  CREATE TABLE orders (
      id SERIAL PRIMARY KEY,
      customer_id INT,  -- Nullable
      FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE SET NULL
  );
  
  DELETE FROM customers WHERE id = 1;
  -- customer_id in orders is set to NULL
  
  -- SET DEFAULT: Set foreign key to default value
  CREATE TABLE orders (
      id SERIAL PRIMARY KEY,
      customer_id INT DEFAULT 0,
      FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE SET DEFAULT
  );
  
  DELETE FROM customers WHERE id = 1;
  -- customer_id in orders is set to 0
  
  -- NO ACTION: Similar to RESTRICT (check at end of transaction)
  CREATE TABLE orders (
      id SERIAL PRIMARY KEY,
      customer_id INT NOT NULL,
      FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE NO ACTION
  );
  ```

  **ON UPDATE actions:**

  ```sql
  -- CASCADE: Update child rows when parent key changes
  CREATE TABLE orders (
      id SERIAL PRIMARY KEY,
      customer_id INT NOT NULL,
      FOREIGN KEY (customer_id) REFERENCES customers(id) 
          ON UPDATE CASCADE
          ON DELETE CASCADE
  );
  
  UPDATE customers SET id = 100 WHERE id = 1;
  -- customer_id in orders is updated to 100
  
  -- RESTRICT: Prevent update if child rows exist
  CREATE TABLE orders (
      id SERIAL PRIMARY KEY,
      customer_id INT NOT NULL,
      FOREIGN KEY (customer_id) REFERENCES customers(id) ON UPDATE RESTRICT
  );
  
  UPDATE customers SET id = 100 WHERE id = 1;
  -- ERROR: cannot update customer with existing orders
  ```

  **Composite foreign keys:**

  ```sql
  -- Parent table with composite key
  CREATE TABLE courses (
      department VARCHAR(10),
      course_number VARCHAR(10),
      name VARCHAR(100),
      PRIMARY KEY (department, course_number)
  );
  
  -- Child table references composite key
  CREATE TABLE enrollments (
      student_id INT,
      department VARCHAR(10),
      course_number VARCHAR(10),
      grade VARCHAR(2),
      FOREIGN KEY (department, course_number) 
          REFERENCES courses(department, course_number)
  );
  ```

  **Self-referencing foreign keys:**

  ```sql
  -- Employee hierarchy
  CREATE TABLE employees (
      id SERIAL PRIMARY KEY,
      name VARCHAR(100),
      manager_id INT,
      FOREIGN KEY (manager_id) REFERENCES employees(id)
  );
  
  INSERT INTO employees (id, name, manager_id) VALUES
      (1, 'CEO', NULL),           -- No manager
      (2, 'VP Sales', 1),         -- Reports to CEO
      (3, 'Sales Rep', 2);        -- Reports to VP Sales
  ```

  **Multiple foreign keys to same table:**

  ```sql
  -- Flight with departure and arrival airports
  CREATE TABLE airports (
      code VARCHAR(3) PRIMARY KEY,
      name VARCHAR(100)
  );
  
  CREATE TABLE flights (
      id SERIAL PRIMARY KEY,
      departure_airport VARCHAR(3),
      arrival_airport VARCHAR(3),
      FOREIGN KEY (departure_airport) REFERENCES airports(code),
      FOREIGN KEY (arrival_airport) REFERENCES airports(code)
  );
  ```

  **Deferred constraints (PostgreSQL):**

  ```sql
  -- Check constraint at end of transaction
  CREATE TABLE orders (
      id SERIAL PRIMARY KEY,
      customer_id INT NOT NULL,
      FOREIGN KEY (customer_id) REFERENCES customers(id)
          DEFERRABLE INITIALLY DEFERRED
  );
  
  BEGIN;
  INSERT INTO orders (customer_id) VALUES (1);  -- OK even if customer doesn't exist yet
  INSERT INTO customers (id, name) VALUES (1, 'John');  -- Create customer
  COMMIT;  -- Constraint checked here
  ```

</details>

<details>
  <summary>Constraints</summary>
  <br/>

  **Constraints** enforce data integrity rules.

  **NOT NULL constraint:**

  ```sql
  CREATE TABLE users (
      id SERIAL PRIMARY KEY,
      email VARCHAR(255) NOT NULL,      -- Cannot be NULL
      name VARCHAR(100) NOT NULL,
      phone VARCHAR(20)                 -- Can be NULL
  );
  
  INSERT INTO users (email, name) VALUES ('john@example.com', 'John');  -- OK
  INSERT INTO users (name) VALUES ('Jane');  -- ERROR: email cannot be NULL
  ```

  **UNIQUE constraint:**

  ```sql
  CREATE TABLE users (
      id SERIAL PRIMARY KEY,
      email VARCHAR(255) UNIQUE NOT NULL,     -- Must be unique
      username VARCHAR(50) UNIQUE NOT NULL
  );
  
  INSERT INTO users (email, username) VALUES ('john@example.com', 'john');  -- OK
  INSERT INTO users (email, username) VALUES ('john@example.com', 'john2'); -- ERROR: email not unique
  
  -- Composite unique constraint
  CREATE TABLE user_roles (
      user_id INT,
      role_id INT,
      UNIQUE (user_id, role_id)  -- Combination must be unique
  );
  ```

  **CHECK constraint:**

  ```sql
  CREATE TABLE products (
      id SERIAL PRIMARY KEY,
      name VARCHAR(200) NOT NULL,
      price DECIMAL(10, 2) CHECK (price > 0),           -- Price must be positive
      stock INT CHECK (stock >= 0),                     -- Stock cannot be negative
      discount DECIMAL(5, 2) CHECK (discount BETWEEN 0 AND 100)  -- 0-100%
  );
  
  INSERT INTO products (name, price, stock) VALUES ('Laptop', 999.99, 10);  -- OK
  INSERT INTO products (name, price, stock) VALUES ('Mouse', -10, 5);       -- ERROR: price must be > 0
  INSERT INTO products (name, price, stock) VALUES ('Keyboard', 50, -5);    -- ERROR: stock must be >= 0
  
  -- Multi-column CHECK constraint
  CREATE TABLE orders (
      id SERIAL PRIMARY KEY,
      subtotal DECIMAL(10, 2),
      tax DECIMAL(10, 2),
      total DECIMAL(10, 2),
      CHECK (total = subtotal + tax)  -- Ensure total is correct
  );
  
  -- Named CHECK constraint
  CREATE TABLE employees (
      id SERIAL PRIMARY KEY,
      salary DECIMAL(10, 2),
      CONSTRAINT check_salary_positive CHECK (salary > 0)
  );
  ```

  **DEFAULT constraint:**

  ```sql
  CREATE TABLE users (
      id SERIAL PRIMARY KEY,
      username VARCHAR(50) NOT NULL,
      status VARCHAR(20) DEFAULT 'active',              -- Default value
      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,   -- Default to now
      is_verified BOOLEAN DEFAULT FALSE
  );
  
  INSERT INTO users (username) VALUES ('john');
  -- status = 'active', created_at = now, is_verified = false
  ```

  **ENUM constraint (PostgreSQL):**

  ```sql
  -- Create enum type
  CREATE TYPE order_status AS ENUM ('pending', 'processing', 'shipped', 'delivered', 'cancelled');
  
  CREATE TABLE orders (
      id SERIAL PRIMARY KEY,
      status order_status DEFAULT 'pending'
  );
  
  INSERT INTO orders (status) VALUES ('shipped');     -- OK
  INSERT INTO orders (status) VALUES ('invalid');     -- ERROR: invalid enum value
  
  -- MySQL enum
  CREATE TABLE orders (
      id INT PRIMARY KEY,
      status ENUM('pending', 'processing', 'shipped', 'delivered', 'cancelled') DEFAULT 'pending'
  );
  ```

  **Adding constraints to existing tables:**

  ```sql
  -- Add NOT NULL
  ALTER TABLE users ALTER COLUMN email SET NOT NULL;
  
  -- Add UNIQUE
  ALTER TABLE users ADD CONSTRAINT unique_email UNIQUE (email);
  
  -- Add CHECK
  ALTER TABLE products ADD CONSTRAINT check_price CHECK (price > 0);
  
  -- Add FOREIGN KEY
  ALTER TABLE orders 
  ADD CONSTRAINT fk_customer 
  FOREIGN KEY (customer_id) REFERENCES customers(id);
  
  -- Add DEFAULT
  ALTER TABLE users ALTER COLUMN status SET DEFAULT 'active';
  ```

  **Dropping constraints:**

  ```sql
  -- Drop constraint by name
  ALTER TABLE users DROP CONSTRAINT unique_email;
  
  -- Drop NOT NULL
  ALTER TABLE users ALTER COLUMN phone DROP NOT NULL;
  
  -- Drop DEFAULT
  ALTER TABLE users ALTER COLUMN status DROP DEFAULT;
  
  -- Drop foreign key
  ALTER TABLE orders DROP CONSTRAINT fk_customer;
  ```

  **Disabling constraints (for bulk operations):**

  ```sql
  -- PostgreSQL: Disable triggers (includes foreign keys)
  ALTER TABLE orders DISABLE TRIGGER ALL;
  -- Bulk insert...
  ALTER TABLE orders ENABLE TRIGGER ALL;
  
  -- MySQL: Disable foreign key checks
  SET FOREIGN_KEY_CHECKS = 0;
  -- Bulk insert...
  SET FOREIGN_KEY_CHECKS = 1;
  ```

</details>

<details>
  <summary>Entity-Relationship Modeling</summary>
  <br/>

  **Entity-Relationship (ER) modeling** defines entities and their relationships.

  **One-to-Many (1:N) relationship:**

  ```sql
  -- One customer has many orders
  CREATE TABLE customers (
      id SERIAL PRIMARY KEY,
      name VARCHAR(100)
  );
  
  CREATE TABLE orders (
      id SERIAL PRIMARY KEY,
      customer_id INT NOT NULL,
      total DECIMAL(10, 2),
      FOREIGN KEY (customer_id) REFERENCES customers(id)
  );
  
  -- One customer → Many orders
  -- One order → One customer
  ```

  **Many-to-Many (M:N) relationship:**

  ```sql
  -- Students enroll in many courses
  -- Courses have many students
  
  CREATE TABLE students (
      id SERIAL PRIMARY KEY,
      name VARCHAR(100)
  );
  
  CREATE TABLE courses (
      id SERIAL PRIMARY KEY,
      name VARCHAR(100)
  );
  
  -- Junction table (associative entity)
  CREATE TABLE enrollments (
      student_id INT,
      course_id INT,
      grade VARCHAR(2),
      enrolled_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      PRIMARY KEY (student_id, course_id),
      FOREIGN KEY (student_id) REFERENCES students(id),
      FOREIGN KEY (course_id) REFERENCES courses(id)
  );
  
  -- Many students → Many courses
  ```

  **One-to-One (1:1) relationship:**

  ```sql
  -- One user has one profile
  CREATE TABLE users (
      id SERIAL PRIMARY KEY,
      email VARCHAR(255) UNIQUE NOT NULL
  );
  
  CREATE TABLE profiles (
      user_id INT PRIMARY KEY,  -- Primary key = foreign key
      bio TEXT,
      avatar_url VARCHAR(500),
      FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
  );
  
  -- One user → One profile
  -- One profile → One user
  
  -- Alternative: Use UNIQUE constraint
  CREATE TABLE profiles (
      id SERIAL PRIMARY KEY,
      user_id INT UNIQUE NOT NULL,  -- UNIQUE enforces 1:1
      bio TEXT,
      FOREIGN KEY (user_id) REFERENCES users(id)
  );
  ```

  **Self-referencing relationship:**

  ```sql
  -- Employee hierarchy (manager-subordinate)
  CREATE TABLE employees (
      id SERIAL PRIMARY KEY,
      name VARCHAR(100),
      manager_id INT,
      FOREIGN KEY (manager_id) REFERENCES employees(id)
  );
  
  -- Category hierarchy (parent-child)
  CREATE TABLE categories (
      id SERIAL PRIMARY KEY,
      name VARCHAR(100),
      parent_id INT,
      FOREIGN KEY (parent_id) REFERENCES categories(id)
  );
  ```

  **Inheritance relationships:**

  ```sql
  -- Option 1: Single table (all types in one table)
  CREATE TABLE vehicles (
      id SERIAL PRIMARY KEY,
      type VARCHAR(20),  -- 'car', 'truck', 'motorcycle'
      make VARCHAR(50),
      model VARCHAR(50),
      -- Car-specific
      num_doors INT,
      -- Truck-specific
      cargo_capacity INT,
      -- Motorcycle-specific
      engine_cc INT
  );
  -- Pros: Simple queries, no joins
  -- Cons: Many NULL values, no type-specific constraints
  
  -- Option 2: Class table (separate table per type)
  CREATE TABLE vehicles (
      id SERIAL PRIMARY KEY,
      make VARCHAR(50),
      model VARCHAR(50)
  );
  
  CREATE TABLE cars (
      id INT PRIMARY KEY,
      num_doors INT,
      FOREIGN KEY (id) REFERENCES vehicles(id)
  );
  
  CREATE TABLE trucks (
      id INT PRIMARY KEY,
      cargo_capacity INT,
      FOREIGN KEY (id) REFERENCES vehicles(id)
  );
  -- Pros: No NULL values, type-specific constraints
  -- Cons: Requires joins, more complex queries
  
  -- Option 3: Concrete table (duplicate common columns)
  CREATE TABLE cars (
      id SERIAL PRIMARY KEY,
      make VARCHAR(50),
      model VARCHAR(50),
      num_doors INT
  );
  
  CREATE TABLE trucks (
      id SERIAL PRIMARY KEY,
      make VARCHAR(50),
      model VARCHAR(50),
      cargo_capacity INT
  );
  -- Pros: No joins, simple queries
  -- Cons: Duplicate columns, difficult to query all vehicles
  ```

</details>

<details>
  <summary>Schema Design Best Practices</summary>
  <br/>

  ```sql
  -- ✅ DO: Use consistent naming conventions
  -- Tables: plural nouns (users, orders, products)
  -- Columns: snake_case (created_at, user_id)
  -- Primary keys: id
  -- Foreign keys: table_name_id (user_id, order_id)
  
  CREATE TABLE customers (
      id SERIAL PRIMARY KEY,
      first_name VARCHAR(50),
      last_name VARCHAR(50),
      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
  );
  
  CREATE TABLE orders (
      id SERIAL PRIMARY KEY,
      customer_id INT NOT NULL,
      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      FOREIGN KEY (customer_id) REFERENCES customers(id)
  );
  
  -- ✅ DO: Use appropriate data types
  CREATE TABLE products (
      id SERIAL PRIMARY KEY,
      name VARCHAR(200) NOT NULL,           -- Not TEXT
      price DECIMAL(10, 2) NOT NULL,        -- Not FLOAT (precision)
      stock SMALLINT NOT NULL DEFAULT 0,    -- Not INT (saves space)
      is_active BOOLEAN DEFAULT TRUE,       -- Not CHAR(1)
      created_at TIMESTAMP NOT NULL         -- Not VARCHAR
  );
  
  -- ✅ DO: Add timestamps for auditing
  CREATE TABLE users (
      id SERIAL PRIMARY KEY,
      email VARCHAR(255) UNIQUE NOT NULL,
      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
  );
  
  -- ✅ DO: Use surrogate keys for most tables
  CREATE TABLE products (
      id SERIAL PRIMARY KEY,              -- Surrogate key
      sku VARCHAR(50) UNIQUE NOT NULL,    -- Natural key as unique
      name VARCHAR(200)
  );
  
  -- ✅ DO: Add indexes on foreign keys
  CREATE TABLE orders (
      id SERIAL PRIMARY KEY,
      customer_id INT NOT NULL,
      FOREIGN KEY (customer_id) REFERENCES customers(id)
  );
  
  CREATE INDEX idx_orders_customer_id ON orders(customer_id);
  
  -- ✅ DO: Use NOT NULL for required fields
  CREATE TABLE users (
      id SERIAL PRIMARY KEY,
      email VARCHAR(255) NOT NULL,        -- Required
      name VARCHAR(100) NOT NULL,         -- Required
      phone VARCHAR(20)                   -- Optional
  );
  
  -- ✅ DO: Add CHECK constraints for business rules
  CREATE TABLE products (
      id SERIAL PRIMARY KEY,
      price DECIMAL(10, 2) CHECK (price > 0),
      stock INT CHECK (stock >= 0),
      discount DECIMAL(5, 2) CHECK (discount BETWEEN 0 AND 100)
  );
  
  -- ✅ DO: Use CASCADE carefully
  CREATE TABLE order_items (
      id SERIAL PRIMARY KEY,
      order_id INT NOT NULL,
      FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
  );
  -- Deleting order deletes all order items (usually desired)
  
  CREATE TABLE orders (
      id SERIAL PRIMARY KEY,
      customer_id INT NOT NULL,
      FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE RESTRICT
  );
  -- Cannot delete customer with orders (usually desired)
  
  -- ✅ DO: Document schema with comments
  COMMENT ON TABLE users IS 'Registered users of the application';
  COMMENT ON COLUMN users.email IS 'User email address (unique, used for login)';
  
  -- ✅ DO: Use junction tables for many-to-many
  CREATE TABLE user_roles (
      user_id INT,
      role_id INT,
      assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      PRIMARY KEY (user_id, role_id),
      FOREIGN KEY (user_id) REFERENCES users(id),
      FOREIGN KEY (role_id) REFERENCES roles(id)
  );
  
  -- ❌ DON'T: Use reserved keywords as names
  -- BAD: order, user, select, table
  CREATE TABLE "order" (id INT);  -- Requires quotes
  
  -- GOOD: orders, users
  CREATE TABLE orders (id INT);
  
  -- ❌ DON'T: Store calculated values (unless performance critical)
  CREATE TABLE orders (
      id SERIAL PRIMARY KEY,
      subtotal DECIMAL(10, 2),
      tax DECIMAL(10, 2),
      total DECIMAL(10, 2)  -- Calculated: subtotal + tax
  );
  
  -- BETTER: Calculate on the fly
  SELECT id, subtotal, tax, (subtotal + tax) AS total FROM orders;
  
  -- ❌ DON'T: Use generic column names
  CREATE TABLE products (
      id INT,
      name VARCHAR(100),
      value VARCHAR(100),  -- What value?
      data TEXT            -- What data?
  );
  
  -- BETTER: Use descriptive names
  CREATE TABLE products (
      id INT,
      name VARCHAR(100),
      price DECIMAL(10, 2),
      description TEXT
  );
  
  -- ❌ DON'T: Store multiple values in single column
  CREATE TABLE users (
      id INT,
      name VARCHAR(100),
      hobbies VARCHAR(500)  -- "reading, gaming, cooking"
  );
  
  -- BETTER: Separate table
  CREATE TABLE user_hobbies (
      user_id INT,
      hobby VARCHAR(100),
      PRIMARY KEY (user_id, hobby)
  );
  
  -- ✅ DO: Use soft delete for important data
  CREATE TABLE users (
      id SERIAL PRIMARY KEY,
      email VARCHAR(255),
      deleted_at TIMESTAMP,  -- NULL = active, NOT NULL = deleted
      deleted_by INT
  );
  
  -- Query active users
  SELECT * FROM users WHERE deleted_at IS NULL;
  
  -- ✅ DO: Version your schema
  CREATE TABLE schema_migrations (
      version VARCHAR(50) PRIMARY KEY,
      applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
  );
  
  -- ✅ DO: Use transactions for schema changes
  BEGIN;
  ALTER TABLE users ADD COLUMN phone VARCHAR(20);
  CREATE INDEX idx_users_phone ON users(phone);
  COMMIT;
  ```

  **Summary:**
  + **Primary keys** uniquely identify rows (prefer surrogate keys)
  + **Foreign keys** define relationships and enforce referential integrity
  + **Constraints** enforce data integrity (NOT NULL, UNIQUE, CHECK)
  + Use **appropriate data types** for each column
  + Add **timestamps** for auditing (created_at, updated_at)
  + Use **indexes** on foreign keys and frequently queried columns
  + Use **CASCADE** carefully (understand implications)
  + Use **junction tables** for many-to-many relationships
  + Use **consistent naming conventions**
  + **Document** schema with comments
  + Use **soft delete** for important data
  + **Version** your schema changes
  + Use **transactions** for schema modifications

</details>

## Views

<details>
  <summary>What are Views?</summary>
  <br/>

  **Views** are virtual tables based on SQL queries. They don't store data themselves but provide a way to simplify complex queries and control data access.

  **Basic view:**

  ```sql
  -- Create view
  CREATE VIEW active_users AS
  SELECT id, username, email, created_at
  FROM users
  WHERE deleted_at IS NULL;
  
  -- Query view like a table
  SELECT * FROM active_users;
  SELECT * FROM active_users WHERE username LIKE 'john%';
  ```

  **Benefits of views:**
  + Simplify complex queries
  + Encapsulate business logic
  + Provide data abstraction
  + Control data access (security)
  + Maintain backward compatibility
  + Reuse common queries

  **Types of views:**
  + **Simple views** - Based on single table
  + **Complex views** - Based on multiple tables (joins)
  + **Materialized views** - Store query results physically

</details>

<details>
  <summary>Creating Views</summary>
  <br/>

  **Simple view:**

  ```sql
  -- View of active products
  CREATE VIEW active_products AS
  SELECT id, name, price, stock
  FROM products
  WHERE is_active = TRUE;
  
  -- Use view
  SELECT * FROM active_products WHERE price > 100;
  ```

  **View with joins:**

  ```sql
  -- Customer order summary
  CREATE VIEW customer_orders AS
  SELECT 
      c.id AS customer_id,
      c.name AS customer_name,
      c.email,
      o.id AS order_id,
      o.total,
      o.status,
      o.created_at AS order_date
  FROM customers c
  LEFT JOIN orders o ON c.id = o.customer_id;
  
  -- Query view
  SELECT * FROM customer_orders WHERE customer_id = 1;
  ```

  **View with aggregations:**

  ```sql
  -- Customer statistics
  CREATE VIEW customer_stats AS
  SELECT 
      c.id,
      c.name,
      c.email,
      COUNT(o.id) AS order_count,
      COALESCE(SUM(o.total), 0) AS total_spent,
      MAX(o.created_at) AS last_order_date
  FROM customers c
  LEFT JOIN orders o ON c.id = o.customer_id
  GROUP BY c.id, c.name, c.email;
  
  -- Query view
  SELECT * FROM customer_stats WHERE order_count > 5;
  ```

  **View with subqueries:**

  ```sql
  -- Products with category name
  CREATE VIEW products_with_category AS
  SELECT 
      p.id,
      p.name,
      p.price,
      (SELECT name FROM categories WHERE id = p.category_id) AS category_name
  FROM products p;
  ```

  **View with calculations:**

  ```sql
  -- Order totals with tax
  CREATE VIEW order_totals AS
  SELECT 
      id,
      customer_id,
      subtotal,
      subtotal * 0.1 AS tax,
      subtotal * 1.1 AS total
  FROM orders;
  ```

  **View with UNION:**

  ```sql
  -- All transactions (orders and refunds)
  CREATE VIEW all_transactions AS
  SELECT id, customer_id, amount, 'order' AS type, created_at
  FROM orders
  UNION ALL
  SELECT id, customer_id, amount, 'refund' AS type, created_at
  FROM refunds;
  ```

  **Replace existing view:**

  ```sql
  -- PostgreSQL, MySQL
  CREATE OR REPLACE VIEW active_users AS
  SELECT id, username, email, status, created_at
  FROM users
  WHERE deleted_at IS NULL;
  
  -- SQL Server
  ALTER VIEW active_users AS
  SELECT id, username, email, status, created_at
  FROM users
  WHERE deleted_at IS NULL;
  ```

</details>

<details>
  <summary>Querying Views</summary>
  <br/>

  **Views behave like tables:**

  ```sql
  -- Create view
  CREATE VIEW active_products AS
  SELECT id, name, price, stock
  FROM products
  WHERE is_active = TRUE;
  
  -- SELECT
  SELECT * FROM active_products;
  SELECT name, price FROM active_products WHERE price > 100;
  
  -- WHERE
  SELECT * FROM active_products WHERE stock > 0;
  
  -- ORDER BY
  SELECT * FROM active_products ORDER BY price DESC;
  
  -- LIMIT
  SELECT * FROM active_products LIMIT 10;
  
  -- JOIN with other tables
  SELECT 
      ap.name,
      ap.price,
      c.name AS category_name
  FROM active_products ap
  JOIN categories c ON ap.category_id = c.id;
  
  -- JOIN with other views
  SELECT 
      cs.customer_name,
      cs.order_count,
      ap.name AS product_name
  FROM customer_stats cs
  JOIN order_items oi ON cs.customer_id = oi.customer_id
  JOIN active_products ap ON oi.product_id = ap.id;
  ```

  **Views in subqueries:**

  ```sql
  -- Customers with above-average spending
  SELECT *
  FROM customer_stats
  WHERE total_spent > (SELECT AVG(total_spent) FROM customer_stats);
  ```

  **Views with CTEs:**

  ```sql
  WITH high_value_customers AS (
      SELECT * FROM customer_stats WHERE total_spent > 1000
  )
  SELECT * FROM high_value_customers WHERE order_count > 10;
  ```

</details>

<details>
  <summary>Updatable Views</summary>
  <br/>

  **Simple views can be updated:**

  ```sql
  -- Create simple view
  CREATE VIEW active_users AS
  SELECT id, username, email, status
  FROM users
  WHERE deleted_at IS NULL;
  
  -- INSERT through view
  INSERT INTO active_users (username, email, status)
  VALUES ('john', 'john@example.com', 'active');
  -- Inserts into users table
  
  -- UPDATE through view
  UPDATE active_users
  SET status = 'inactive'
  WHERE id = 1;
  -- Updates users table
  
  -- DELETE through view
  DELETE FROM active_users WHERE id = 1;
  -- Deletes from users table
  ```

  **Views with CHECK OPTION:**

  ```sql
  -- Prevent updates that violate view condition
  CREATE VIEW active_users AS
  SELECT id, username, email, status
  FROM users
  WHERE status = 'active'
  WITH CHECK OPTION;
  
  -- This works
  UPDATE active_users SET email = 'new@example.com' WHERE id = 1;
  
  -- This fails (violates WHERE condition)
  UPDATE active_users SET status = 'inactive' WHERE id = 1;
  -- ERROR: new row violates check option
  ```

  **Non-updatable views:**

  Views with these features cannot be updated:
  + Aggregations (COUNT, SUM, AVG, etc.)
  + DISTINCT
  + GROUP BY
  + HAVING
  + UNION
  + Joins (in some databases)
  + Subqueries in SELECT

  ```sql
  -- Non-updatable view (has aggregation)
  CREATE VIEW customer_stats AS
  SELECT 
      customer_id,
      COUNT(*) AS order_count,
      SUM(total) AS total_spent
  FROM orders
  GROUP BY customer_id;
  
  -- Cannot update
  UPDATE customer_stats SET order_count = 10 WHERE customer_id = 1;
  -- ERROR: cannot update view with aggregation
  ```

  **INSTEAD OF triggers (make non-updatable views updatable):**

  ```sql
  -- PostgreSQL: Create INSTEAD OF trigger
  CREATE VIEW customer_stats AS
  SELECT 
      c.id,
      c.name,
      COUNT(o.id) AS order_count
  FROM customers c
  LEFT JOIN orders o ON c.id = o.customer_id
  GROUP BY c.id, c.name;
  
  -- Create trigger function
  CREATE FUNCTION update_customer_stats()
  RETURNS TRIGGER AS $$
  BEGIN
      UPDATE customers
      SET name = NEW.name
      WHERE id = NEW.id;
      RETURN NEW;
  END;
  $$ LANGUAGE plpgsql;
  
  -- Create INSTEAD OF trigger
  CREATE TRIGGER update_customer_stats_trigger
  INSTEAD OF UPDATE ON customer_stats
  FOR EACH ROW
  EXECUTE FUNCTION update_customer_stats();
  
  -- Now can update view
  UPDATE customer_stats SET name = 'John Doe' WHERE id = 1;
  ```

</details>

<details>
  <summary>Materialized Views</summary>
  <br/>

  **Materialized views** store query results physically (like a table).

  **Create materialized view:**

  ```sql
  -- PostgreSQL
  CREATE MATERIALIZED VIEW customer_order_summary AS
  SELECT 
      c.id,
      c.name,
      c.email,
      COUNT(o.id) AS order_count,
      COALESCE(SUM(o.total), 0) AS total_spent,
      MAX(o.created_at) AS last_order_date
  FROM customers c
  LEFT JOIN orders o ON c.id = o.customer_id
  GROUP BY c.id, c.name, c.email;
  
  -- Query materialized view (fast, no joins/aggregations)
  SELECT * FROM customer_order_summary WHERE order_count > 5;
  ```

  **Refresh materialized view:**

  ```sql
  -- Refresh (update data)
  REFRESH MATERIALIZED VIEW customer_order_summary;
  
  -- Refresh without locking (PostgreSQL)
  REFRESH MATERIALIZED VIEW CONCURRENTLY customer_order_summary;
  -- Requires unique index
  
  -- Create unique index for concurrent refresh
  CREATE UNIQUE INDEX idx_customer_summary_id 
  ON customer_order_summary(id);
  ```

  **Automatic refresh (using triggers):**

  ```sql
  -- Create function to refresh materialized view
  CREATE FUNCTION refresh_customer_summary()
  RETURNS TRIGGER AS $$
  BEGIN
      REFRESH MATERIALIZED VIEW CONCURRENTLY customer_order_summary;
      RETURN NULL;
  END;
  $$ LANGUAGE plpgsql;
  
  -- Create trigger on orders table
  CREATE TRIGGER refresh_customer_summary_trigger
  AFTER INSERT OR UPDATE OR DELETE ON orders
  FOR EACH STATEMENT
  EXECUTE FUNCTION refresh_customer_summary();
  ```

  **Scheduled refresh (using cron or scheduler):**

  ```sql
  -- PostgreSQL: Use pg_cron extension
  CREATE EXTENSION pg_cron;
  
  -- Schedule refresh every hour
  SELECT cron.schedule(
      'refresh-customer-summary',
      '0 * * * *',  -- Every hour
      'REFRESH MATERIALIZED VIEW CONCURRENTLY customer_order_summary'
  );
  ```

  **Materialized view vs regular view:**

  | Aspect | Regular View | Materialized View |
  |--------|-------------|-------------------|
  | Storage | No data stored | Data stored physically |
  | Query speed | Slower (executes query) | Faster (reads stored data) |
  | Data freshness | Always current | Stale until refreshed |
  | Disk space | None | Uses disk space |
  | Indexes | Cannot create | Can create indexes |
  | Use case | Simple queries, current data | Complex queries, reporting |

  **When to use materialized views:**
  + Complex queries with joins/aggregations
  + Reporting and analytics
  + Data doesn't change frequently
  + Query performance is critical
  + Can tolerate stale data

</details>

<details>
  <summary>View Security & Access Control</summary>
  <br/>

  **Views for data security:**

  ```sql
  -- Hide sensitive columns
  CREATE VIEW public_users AS
  SELECT id, username, email, created_at
  FROM users;
  -- Excludes: password_hash, ssn, credit_card
  
  -- Grant access to view only
  GRANT SELECT ON public_users TO app_user;
  REVOKE ALL ON users FROM app_user;
  
  -- Now app_user can only see public columns
  ```

  **Row-level security with views:**

  ```sql
  -- Show only user's own orders
  CREATE VIEW my_orders AS
  SELECT id, total, status, created_at
  FROM orders
  WHERE customer_id = current_user_id();  -- Function returns current user
  
  -- User can only see their own orders
  SELECT * FROM my_orders;
  ```

  **Department-specific views:**

  ```sql
  -- Sales department view
  CREATE VIEW sales_orders AS
  SELECT id, customer_id, total, created_at
  FROM orders
  WHERE department = 'sales';
  
  -- Finance department view
  CREATE VIEW finance_orders AS
  SELECT id, customer_id, total, payment_method, created_at
  FROM orders;
  
  -- Grant appropriate access
  GRANT SELECT ON sales_orders TO sales_team;
  GRANT SELECT ON finance_orders TO finance_team;
  ```

  **Masked data views:**

  ```sql
  -- Mask sensitive data
  CREATE VIEW masked_users AS
  SELECT 
      id,
      username,
      CONCAT(SUBSTRING(email, 1, 3), '***@***', SUBSTRING(email FROM '@(.*)')) AS email,
      '***-**-' || RIGHT(ssn, 4) AS ssn_last4
  FROM users;
  
  -- Shows: john → joh***@***.com, 123-45-6789 → ***-**-6789
  ```

</details>

<details>
  <summary>View Performance Considerations</summary>
  <br/>

  **Views don't improve performance:**

  ```sql
  -- View is just a stored query
  CREATE VIEW expensive_query AS
  SELECT 
      c.name,
      COUNT(o.id) AS order_count,
      SUM(o.total) AS total_spent
  FROM customers c
  LEFT JOIN orders o ON c.id = o.customer_id
  GROUP BY c.id, c.name;
  
  -- This is still slow (executes full query)
  SELECT * FROM expensive_query WHERE name = 'John';
  
  -- Equivalent to:
  SELECT 
      c.name,
      COUNT(o.id) AS order_count,
      SUM(o.total) AS total_spent
  FROM customers c
  LEFT JOIN orders o ON c.id = o.customer_id
  WHERE c.name = 'John'
  GROUP BY c.id, c.name;
  ```

  **Use materialized views for performance:**

  ```sql
  -- Materialized view stores results
  CREATE MATERIALIZED VIEW customer_summary AS
  SELECT 
      c.id,
      c.name,
      COUNT(o.id) AS order_count,
      SUM(o.total) AS total_spent
  FROM customers c
  LEFT JOIN orders o ON c.id = o.customer_id
  GROUP BY c.id, c.name;
  
  -- Create index on materialized view
  CREATE INDEX idx_customer_summary_name ON customer_summary(name);
  
  -- This is fast (reads from stored data)
  SELECT * FROM customer_summary WHERE name = 'John';
  ```

  **View optimization tips:**

  ```sql
  -- ✅ DO: Add indexes on base tables
  CREATE INDEX idx_orders_customer_id ON orders(customer_id);
  CREATE INDEX idx_orders_created_at ON orders(created_at);
  
  -- ✅ DO: Filter early in view definition
  CREATE VIEW recent_orders AS
  SELECT id, customer_id, total, created_at
  FROM orders
  WHERE created_at > CURRENT_DATE - INTERVAL '30 days';
  
  -- ✅ DO: Use materialized views for complex queries
  CREATE MATERIALIZED VIEW daily_sales AS
  SELECT 
      DATE(created_at) AS sale_date,
      COUNT(*) AS order_count,
      SUM(total) AS revenue
  FROM orders
  GROUP BY DATE(created_at);
  
  -- ❌ DON'T: Nest views too deeply
  CREATE VIEW view1 AS SELECT * FROM table1;
  CREATE VIEW view2 AS SELECT * FROM view1;
  CREATE VIEW view3 AS SELECT * FROM view2;  -- Too many layers!
  
  -- ✅ DO: Use EXPLAIN to analyze view queries
  EXPLAIN SELECT * FROM customer_orders WHERE customer_id = 1;
  ```

</details>

<details>
  <summary>Managing Views</summary>
  <br/>

  **List all views:**

  ```sql
  -- PostgreSQL
  SELECT table_name, view_definition
  FROM information_schema.views
  WHERE table_schema = 'public';
  
  -- MySQL
  SHOW FULL TABLES WHERE table_type = 'VIEW';
  
  -- SQL Server
  SELECT name, definition
  FROM sys.views v
  JOIN sys.sql_modules m ON v.object_id = m.object_id;
  ```

  **View definition:**

  ```sql
  -- PostgreSQL
  SELECT pg_get_viewdef('customer_orders', true);
  
  -- MySQL
  SHOW CREATE VIEW customer_orders;
  
  -- SQL Server
  EXEC sp_helptext 'customer_orders';
  ```

  **Drop view:**

  ```sql
  -- Drop view
  DROP VIEW customer_orders;
  
  -- Drop if exists
  DROP VIEW IF EXISTS customer_orders;
  
  -- Drop materialized view
  DROP MATERIALIZED VIEW customer_order_summary;
  
  -- Drop cascade (drop dependent views)
  DROP VIEW customer_orders CASCADE;
  ```

  **Rename view:**

  ```sql
  -- PostgreSQL
  ALTER VIEW customer_orders RENAME TO customer_order_list;
  
  -- MySQL
  RENAME TABLE customer_orders TO customer_order_list;
  ```

  **View dependencies:**

  ```sql
  -- PostgreSQL: Find views that depend on a table
  SELECT DISTINCT dependent_view.relname AS view_name
  FROM pg_depend
  JOIN pg_rewrite ON pg_depend.objid = pg_rewrite.oid
  JOIN pg_class AS dependent_view ON pg_rewrite.ev_class = dependent_view.oid
  JOIN pg_class AS source_table ON pg_depend.refobjid = source_table.oid
  WHERE source_table.relname = 'orders'
  AND dependent_view.relkind = 'v';
  ```

</details>

<details>
  <summary>View Best Practices</summary>
  <br/>

  ```sql
  -- ✅ DO: Use views to simplify complex queries
  CREATE VIEW customer_order_summary AS
  SELECT 
      c.id,
      c.name,
      COUNT(o.id) AS order_count,
      SUM(o.total) AS total_spent
  FROM customers c
  LEFT JOIN orders o ON c.id = o.customer_id
  GROUP BY c.id, c.name;
  
  -- ✅ DO: Use views for security (hide sensitive columns)
  CREATE VIEW public_users AS
  SELECT id, username, email, created_at
  FROM users;
  -- Excludes password_hash, ssn
  
  -- ✅ DO: Use descriptive view names
  CREATE VIEW active_premium_customers AS
  SELECT * FROM customers
  WHERE status = 'active' AND tier = 'premium';
  
  -- ✅ DO: Document views with comments
  COMMENT ON VIEW customer_order_summary IS 
  'Customer statistics including order count and total spending';
  
  -- ✅ DO: Use materialized views for reporting
  CREATE MATERIALIZED VIEW monthly_sales_report AS
  SELECT 
      DATE_TRUNC('month', created_at) AS month,
      COUNT(*) AS order_count,
      SUM(total) AS revenue
  FROM orders
  GROUP BY DATE_TRUNC('month', created_at);
  
  -- ✅ DO: Create indexes on materialized views
  CREATE INDEX idx_monthly_sales_month 
  ON monthly_sales_report(month);
  
  -- ✅ DO: Refresh materialized views regularly
  REFRESH MATERIALIZED VIEW CONCURRENTLY monthly_sales_report;
  
  -- ✅ DO: Use WITH CHECK OPTION for updatable views
  CREATE VIEW active_users AS
  SELECT id, username, email, status
  FROM users
  WHERE status = 'active'
  WITH CHECK OPTION;
  
  -- ❌ DON'T: Use views for simple column selection
  CREATE VIEW user_emails AS
  SELECT email FROM users;  -- Unnecessary
  
  -- ❌ DON'T: Nest views too deeply
  CREATE VIEW view1 AS SELECT * FROM table1;
  CREATE VIEW view2 AS SELECT * FROM view1;
  CREATE VIEW view3 AS SELECT * FROM view2;  -- Hard to maintain
  
  -- ❌ DON'T: Use SELECT * in views
  CREATE VIEW all_users AS
  SELECT * FROM users;  -- Breaks if table structure changes
  
  -- ✅ BETTER: Specify columns
  CREATE VIEW all_users AS
  SELECT id, username, email, created_at FROM users;
  
  -- ❌ DON'T: Expect views to improve performance
  -- Views don't cache results (use materialized views instead)
  
  -- ✅ DO: Use views for backward compatibility
  -- After renaming column from 'name' to 'full_name'
  CREATE VIEW users_legacy AS
  SELECT id, full_name AS name, email
  FROM users;
  -- Old code can still use 'name' column
  
  -- ✅ DO: Use views to enforce business rules
  CREATE VIEW valid_orders AS
  SELECT * FROM orders
  WHERE total > 0 AND status IN ('pending', 'processing', 'completed');
  
  -- ✅ DO: Version control view definitions
  -- Store CREATE VIEW statements in migration files
  
  -- ✅ DO: Test view performance
  EXPLAIN ANALYZE SELECT * FROM customer_order_summary;
  ```

  **Summary:**
  + **Views** are virtual tables based on queries
  + **Regular views** don't store data (execute query each time)
  + **Materialized views** store data physically (faster queries)
  + Use views to **simplify complex queries**
  + Use views for **security** (hide sensitive data)
  + Use views for **data abstraction**
  + Use **materialized views** for reporting and analytics
  + **Refresh** materialized views regularly
  + Create **indexes** on materialized views
  + Use **WITH CHECK OPTION** for updatable views
  + **Document** views with comments
  + Avoid **nesting views** too deeply
  + Specify **columns explicitly** (not SELECT *)
  + Use **EXPLAIN** to analyze view performance
  + **Version control** view definitions

</details>
