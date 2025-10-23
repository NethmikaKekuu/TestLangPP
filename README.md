# TestLang++ DSL Compiler

**SE2062 - Individual Assignment**  
A Domain-Specific Language (DSL) for HTTP API Testing that compiles to JUnit 5 tests.

---

## 📋 Overview

TestLang++ is a custom DSL designed for writing HTTP API tests in a clean, readable syntax. The compiler translates `.test` files into executable JUnit 5 test classes that use Java's built-in `HttpClient` to perform real HTTP requests and assertions.

### Key Features

- ✅ **Simple Syntax**: Write API tests without boilerplate Java code
- ✅ **Variable Substitution**: Define reusable variables for URLs, user IDs, etc.
- ✅ **HTTP Methods**: Supports GET, POST, PUT, DELETE
- ✅ **Flexible Assertions**: Check status codes, headers, and response bodies
- ✅ **JUnit 5 Integration**: Generated tests run with standard JUnit tooling
- ✅ **No External Dependencies**: Uses only Java 11+ HttpClient

---

## 🏗️ Project Structure

```
TestLangPP-Compiler/
├── src/
│   ├── lexer.flex              # JFlex lexer specification
│   ├── parser.cup              # CUP parser specification
│   ├── TestParser.java         # Main compiler driver
│   └── AST/                    # Abstract Syntax Tree classes
│       ├── Program.java
│       ├── TestBlock.java
│       ├── RequestStmt.java
│       ├── AssertStmt.java
│       ├── ConfigBlock.java
│       ├── LetStmt.java
│       ├── HeaderStmt.java
│       ├── BodyStmt.java
│       ├── ConfigItem.java
│       └── CodeGenerator.java  # JUnit code generator
├── lib/
│   ├── jflex-full-1.9.1.jar
│   ├── java-cup-11b.jar
│   ├── java-cup-11b-runtime.jar
│   └── junit-platform-console-standalone-1.9.3.jar
├── examples/
│   ├── given.test              # Example test file
│   └── multi_users.test        # Multiple user tests
├── output/
│   └── GeneratedTests.java     # Generated JUnit test class
├── ClassLib/                   # Compiled .class files
├── build.bat                   # Build script
├── compile.bat                 # Compile & run script
└── README.md

TestLangPP-Backend/
├── src/
│   └── main/java/com/testlang/backend/
│       └── TestLangBackendApplication.java
├── pom.xml
├── README.md
└── target/
    └── testlang-backend-0.0.1-SNAPSHOT.jar
```

---

## 🚀 Quick Start

### Prerequisites

- **Java 11+** (for HttpClient support)
- **JFlex 1.9.1** (included in `lib/`)
- **CUP 0.11b** (included in `lib/`)
- **JUnit 5** (included in `lib/`)
- **Backend Server** running on `http://localhost:8080` (for testing)

### 1. Build the Compiler

```batch
build.bat
```

This will:
- Generate lexer from `lexer.flex`
- Generate parser from `parser.cup`
- Compile all AST classes
- Compile the main compiler

### 2. Write a Test File

Create `examples/my_test.test`:

```
config {
  base_url = "http://localhost:8080";
  header "Content-Type" = "application/json";
}

let user = "admin";
let id = 42;

test Login {
  POST "/api/login" {
    body = "{ \"username\": \"$user\", \"password\": \"1234\" }";
  }
  expect status = 200;
  expect body contains "\"token\":";
}

test GetUser {
  GET "/api/users/$id";
  expect status = 200;
  expect body contains "\"id\": 42";
}
```

### 3. Compile and Run

**Option A: Using compile.bat (Automated)**

```batch
compile.bat examples/my_test.test
```

**Option B: Manual Steps**

```batch
# Step 1: Parse and generate JUnit code
java -cp "ClassLib;lib\java-cup-11b-runtime.jar" TestParser examples/my_test.test

# Step 2: Compile generated tests
javac -cp "lib\junit-platform-console-standalone-1.9.3.jar" -d output output\GeneratedTests.java

# Step 3: Run tests
java -cp "output;lib\junit-platform-console-standalone-1.9.3.jar" org.junit.platform.console.ConsoleLauncher -c GeneratedTests
```

---

## 📖 Language Reference

### File Structure

```
[config block]    # Optional: base URL and default headers
[let statements]  # Optional: variable declarations
test blocks       # Required: at least one test
```

### Config Block (Optional)

```
config {
  base_url = "http://localhost:8080";
  header "Content-Type" = "application/json";
  header "Authorization" = "Bearer token123";
}
```

### Variable Declarations

```
let username = "admin";          // String variable
let userId = 42;                 // Integer variable
```

Variables are substituted using `$variableName` syntax in strings and paths.

### Test Blocks

```
test TestName {
  [HTTP request]
  [assertions]
  ...
}
```

**Requirements:**
- Each test must have **≥1 HTTP request**
- Each test must have **≥2 assertions**

### HTTP Requests

#### GET/DELETE (Simple)
```
GET "/api/users/42";
DELETE "/api/users/42";
```

#### POST/PUT (With Body)
```
POST "/api/login" {
  header "Content-Type" = "application/json";
  body = "{ \"username\": \"admin\", \"password\": \"1234\" }";
}

PUT "/api/users/$id" {
  body = "{ \"role\": \"ADMIN\" }";
}
```

### Assertions

```
expect status = 200;                              // Status code equals
expect header "Content-Type" = "application/json"; // Header equals
expect header "Content-Type" contains "json";      // Header contains
expect body contains "\"token\":";                 // Body contains
```

### Comments

```
// This is a line comment
let id = 42;  // Comments can appear after statements
```

---

## 🔧 Backend Server

A Spring Boot backend is provided to test against. It provides:

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/login` | POST | User login, returns token |
| `/api/users/{id}` | GET | Get user by ID |
| `/api/users/{id}` | PUT | Update user |
| `/api/users/{id}` | DELETE | Delete user |

### Starting the Backend

```batch
cd TestLangPP-Backend
mvn spring-boot:run
```

Or:

```batch
java -jar target/TestLangPP-0.0.1-SNAPSHOT.jar
```

The server runs on `http://localhost:8080`

---

## 📝 Example Test Files

### Example 1: Basic Authentication Test

```
config {
  base_url = "http://localhost:8080";
}

let user = "admin";

test Login {
  POST "/api/login" {
    body = "{ \"username\": \"$user\", \"password\": \"1234\" }";
  }
  expect status = 200;
  expect body contains "token";
}
```

### Example 2: Multiple Users

```
config {
  base_url = "http://localhost:8080";
}

let userId1 = "42";
let userId2 = "100";

test User42 {
  GET "/api/users/$userId1";
  expect status = 200;
  expect body contains "\"id\": 42";
}

test User100 {
  GET "/api/users/$userId2";
  expect status = 200;
  expect body contains "\"id\": 100";
}
```

### Example 3: Update Operation

```
config {
  base_url = "http://localhost:8080";
  header "Content-Type" = "application/json";
}

let userId = "42";

test UpdateUser {
  PUT "/api/users/$userId" {
    body = "{ \"role\": \"ADMIN\" }";
  }
  expect status = 200;
  expect header "Content-Type" contains "json";
  expect body contains "\"updated\": true";
}
```

---

## 🧪 Generated Code Example

**Input (TestLang++):**

```
test Login {
  POST "/api/login" {
    body = "{ \"username\": \"admin\" }";
  }
  expect status = 200;
  expect body contains "token";
}
```

**Output (JUnit 5):**

```java
@Test
void test_Login() throws Exception {
    HttpRequest.Builder b = HttpRequest.newBuilder(URI.create(BASE + "/api/login"))
        .timeout(Duration.ofSeconds(10))
        .POST(HttpRequest.BodyPublishers.ofString("{ \"username\": \"admin\" }"));
    for (var e : DEFAULT_HEADERS.entrySet()) b.header(e.getKey(), e.getValue());
    HttpResponse<String> resp = client.send(b.build(), 
        HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

    assertEquals(200, resp.statusCode());
    assertTrue(resp.body().contains("token"));
}
```

---

## ❌ Error Handling

The compiler provides clear error messages:

### Syntax Errors

```
Syntax error at line 14, near 'null'
Expected ';' after request statement
```

### Semantic Errors

```
Test 'Login' must have at least 2 assertions
Duplicate variable 'user'
```

### Invalid Constructs

| Error | Reason |
|-------|--------|
| `let 2a = "x";` | Identifier cannot start with digit |
| `expect status = "200";` | Status must be integer, not string |
| `POST "/x" { body = 123; }` | Body must be string |

---

## 🎯 Implementation Details

### Compiler Pipeline

```
.test file
    ↓
[Lexer] → Tokens
    ↓
[Parser] → AST
    ↓
[Validator] → Semantic checks
    ↓
[CodeGenerator] → GeneratedTests.java
    ↓
[javac] → .class files
    ↓
[JUnit] → Test execution
```

### Technology Stack

- **JFlex**: Lexical analyzer generator
- **CUP**: Parser generator (LALR)
- **Java 11+**: HttpClient for HTTP requests
- **JUnit 5**: Test framework

### Design Patterns

- **Visitor Pattern**: AST traversal in code generator
- **Builder Pattern**: HTTP request construction
- **Factory Pattern**: Symbol and AST node creation


---

## 🐛 Troubleshooting

### "Class not found" errors

```batch
# Rebuild the project
build.bat
```

### "Connection refused" when running tests

- Ensure backend server is running on port 8080
- Check firewall settings

### Tests fail with 404

- Verify backend endpoints match test URLs
- Check variable substitution is correct

### Parser errors

- Ensure semicolons after GET/DELETE statements
- Check proper nesting of request blocks
- Verify all strings use double quotes

---

## 📚 References

- [JFlex Documentation](https://jflex.de/manual.html)
- [CUP Documentation](http://www2.cs.tum.edu/projects/cup/)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Java HttpClient](https://docs.oracle.com/en/java/javase/11/docs/api/java.net.http/java/net/http/HttpClient.html)

---

## 👤 Author

**SE2062 Assignment**  
Individual Project - TestLang++ DSL Compiler

---

## 📄 License

This project is submitted as part of SE2062 coursework.

---

## 🎓 Academic Integrity

This implementation follows the assignment specification. All work is original except where explicitly cited.
