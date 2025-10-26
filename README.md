# TestLangPP

**IT23657496 - Kekulanthale K. M. N. Y**
**LINK FOR THE GITHUB REPO**  https://github.com/IT23657496NethmikaKeku/TestLangPP


A Domain-Specific Language (DSL) for writing intuitive, declarative HTTP API tests that compile to executable JUnit 5 test suites.

## Table of Contents

- [Overview](#overview)
- [Key Features](#key-features)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Quick Start](#quick-start)
- [Language Syntax](#language-syntax)
- [Example Test Files](#example-test-files)
- [Build & Compilation](#build--compilation)
- [Project Structure](#project-structure)
- [Running Tests](#running-tests)
- [Test File Descriptions](#test-file-descriptions)
- [Troubleshooting](#troubleshooting)
- [Command Reference](#command-reference)
- [Additional Resources](#additional-resources)

---

## Overview

TestLangPP is a custom DSL compiler that transforms human-readable test specifications into executable Java code. Write API tests in a clean, declarative syntax and let TestLangPP handle the boilerplate code generation, compilation, and execution.

### Why TestLangPP?

**Intuitive Syntax**: Write tests that read like documentation, making test specifications accessible.

**Type Safety**: Compile-time validation catches errors early in the development cycle.

**Zero Boilerplate**: Focus on test logic and assertions, not on HTTP client setup, connection management, or response parsing.

**JUnit Integration**: Generates standard JUnit 5 tests that can be integrated into any Java testing framework.

**Variable Interpolation**: Use dynamic values with `$variable` syntax for reusable, parameterized tests.

**Flexible Assertions**: Check status codes, headers, and body content with simple, readable assertions.

---

## Key Features

**HTTP Method Support**: GET, POST, PUT, DELETE operations with full request customization

**Global Configuration**: Set base URLs and default headers once, apply to all tests

**Variable Declarations**: Define reusable values with `let` keyword 

**String Interpolation**: Inject variables into URLs and request bodies using `$variable` syntax

**Multiline Strings**: Support for `"""` delimited multiline bodies for complex JSON payloads

**Status Code Ranges**: Test ranges like `200..299` for any 2xx success response

**Header Assertions**: Validate response headers with `contains` or exact match checks

**Body Assertions**: Search response bodies for expected content using string matching

**Error Reporting**: Detailed syntax and semantic error messages with line numbers

**Semantic Validation**: Duplicate variable detection, type checking, and compile-time validation

---

## Architecture

The TestLang++ compiler follows a traditional multi-phase compilation architecture:

```
┌─────────────────────┐
│  .test File         │  TestLang++ Source Code
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│  Lexer (JFlex)      │  Tokenization Phase
│                     │  Converts source text to tokens
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│  Parser (CUP)       │  Syntax Analysis Phase
│                     │  Builds parse tree from tokens
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│  AST Builder        │  Abstract Syntax Tree Construction
│                     │  Creates structured representation
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│  Semantic Validator │  Semantic Analysis Phase
│                     │  Checks types, duplicates, scopes
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│  Code Generator     │  Code Generation Phase
│                     │  Emits Java source code
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│  GeneratedTests     │  JUnit 5 Test Class
│  .java              │  Executable Java code
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│  Java Compiler      │  Bytecode Compilation
│                     │  javac → .class files
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│  JUnit Runner       │  Test Execution
│                     │  Runs tests, reports results
└─────────────────────┘
```

---

## Prerequisites

Ensure you have the following installed and configured:

| Component | Version | Location | Purpose |
|-----------|---------|----------|---------|
| **Java JDK** | 11 or higher | System PATH | Runtime and compilation |
| **JFlex** | 1.9.1 | `lib/jflex-full-1.9.1.jar` | Lexical analyzer generator |
| **CUP** | 0.11b | `lib/java-cup-11b.jar` | Parser generator |
| **CUP Runtime** | 0.11b | `lib/java-cup-11b-runtime.jar` | Parser runtime library |
| **JUnit 5** | 1.9.3+ | `lib/junit-platform-console-standalone-1.9.3.jar` | Test execution framework |

### Backend Server Requirement

**IMPORTANT**: The demo backend server must be running on `http://localhost:8080` before executing tests.

#### Start Backend with Maven:

```bash
cd TestLangPP-Backend
mvn spring-boot:run
```

#### Or Run Pre-built JAR:

```bash
java -jar TestLangPP-Backend/target/testlang-backend-0.0.1-SNAPSHOT.jar
```

The backend provides the following endpoints for testing:
- `POST /api/login` - Authentication endpoint
- `GET /api/users/{id}` - User retrieval
- `POST /api/users` - User creation
- `PUT /api/users/{id}` - User update
- `DELETE /api/users/{id}` - User deletion
- `GET /api/health` - Health check endpoint

---

## Installation

### 1. Clone the Repository

```bash
git clone <repository-url>
cd TestLangPP
```

### 2. Verify Dependencies

Ensure all JAR files are present in the `lib/` directory:

```
lib/
├── jflex-full-1.9.1.jar
├── java-cup-11b.jar
├── java-cup-11b-runtime.jar
└── junit-platform-console-standalone-1.9.3.jar
```

### 3. Verify Java Installation

```bash
java -version
```

Expected output: Java version 11 or higher

### 4. Build the Compiler

```batch
build.bat
```

This automated script will:
1. Generate the lexer from `src/lexer.flex` using JFlex
2. Generate the parser from `src/parser.cup` using CUP
3. Compile all AST classes to `ClassLib/AST/`
4. Compile the main TestParser class to `ClassLib/`

---

## Quick Start

### Option 1: One-Command Compilation (Recommended)

The simplest way to compile and run a test file:

```batch
compile.bat examples\given.test
```

This single command performs all necessary steps:
1. Builds the compiler (if not already built)
2. Parses your `.test` file and performs semantic validation
3. Generates `output/GeneratedTests.java` with JUnit test code
4. Compiles the generated test class with javac
5. Executes tests using JUnit 5 and displays results

### Option 2: Step-by-Step Compilation

For detailed control or troubleshooting:

#### Step 1: Build the compiler

```batch
build.bat
```

#### Step 2: Parse a test file

```batch
java -cp "ClassLib;lib\java-cup-11b-runtime.jar" TestParser examples\given.test
```

This generates `output/GeneratedTests.java`

#### Step 3: Compile generated tests

```batch
javac -cp lib\junit-platform-console-standalone-1.9.3.jar -d output output\GeneratedTests.java
```

#### Step 4: Run tests

```batch
java -jar lib\junit-platform-console-standalone-1.9.3.jar --class-path output --scan-classpath --disable-banner
```

---

## Language Syntax

### Configuration Block

Define global settings that apply to all tests in the file:

```
config {
  base_url = "http://localhost:8080";
  header "Content-Type" = "application/json";
  header "Authorization" = "Bearer token123";
  header "X-API-Key" = "secret-key";
}
```

**Syntax Rules**:
- Must appear before any test blocks
- Only one config block per file
- `base_url` is required for relative URLs
- Headers defined here apply to all requests unless overridden

### Variable Declarations

Declare reusable variables for use throughout your tests:

```
let username = "admin";
let userId = 42;
let apiKey = "secret-key-123";
let endpoint = "/api/users";
```

**Syntax Rules**:
- Variable names must start with a letter or underscore
- Can contain letters, numbers, and underscores
- Cannot redeclare the same variable (semantic error)
- Use `$variable` syntax for interpolation in strings

**Valid Variable Names**:
- `userId`, `user_id`, `_userId`, `userId1`

**Invalid Variable Names**:
- `2userId` (starts with digit)
- `user-id` (contains hyphen)
- `user.id` (contains dot)

### Test Blocks

Define individual test cases with HTTP requests and assertions:

```
test TestName {
  // HTTP request
  METHOD "endpoint" {
    header "Custom-Header" = "value";
    body = "request body";
  }
  
  // Assertions
  expect status = 200;
  expect header "Content-Type" contains "json";
  expect body contains "expected text";
}
```

**Test Block Rules**:
- Test names must be unique
- Test names become JUnit method names
- Must contain at least one HTTP request
- Can contain multiple assertions

### HTTP Methods

#### GET Request

```
test GetUser {
  GET "/api/users/$userId";
  expect status = 200;
  expect body contains "\"id\": $userId";
}
```

#### POST Request

```
test CreateUser {
  POST "/api/users" {
    body = "{ \"name\": \"$username\", \"role\": \"admin\" }";
  }
  expect status = 201;
  expect body contains "\"success\": true";
}
```

#### POST with Custom Headers

```
test LoginWithHeaders {
  POST "/api/login" {
    header "X-Client-Version" = "1.0.0";
    body = "{ \"username\": \"$username\", \"password\": \"pass123\" }";
  }
  expect status = 200;
  expect header "Set-Cookie" contains "session";
}
```

#### PUT Request

```
test UpdateUser {
  PUT "/api/users/$userId" {
    header "Content-Type" = "application/json";
    body = "{ \"name\": \"Updated Name\", \"role\": \"ADMIN\" }";
  }
  expect status = 200;
  expect header "X-App" contains "TestLang";
  expect body contains "\"updated\": true";
}
```

#### DELETE Request

```
test DeleteUser {
  DELETE "/api/users/$userId";
  expect status = 200;
  expect body contains "\"deleted\": true";
}
```

### Multiline Bodies

Use triple quotes for complex, multiline request bodies:

```
test ComplexRequest {
  POST "/api/data" {
    body = """
    {
      "user": "$username",
      "metadata": {
        "timestamp": "2024-01-01",
        "source": "testlang"
      },
      "data": {
        "key1": "value1",
        "key2": "value2"
      }
    }
    """;
  }
  expect status = 200;
}
```

**Multiline String Rules**:
- Delimited by `"""` on separate lines
- Preserves formatting and whitespace
- Supports variable interpolation
- Useful for readable JSON payloads

### Status Code Ranges

Test for any status code within a range:

```
test SuccessRange {
  GET "/api/health";
  expect status in 200..299;  // Any 2xx status
  expect body contains "\"status\": \"ok\"";
}

test RedirectRange {
  GET "/api/redirect";
  expect status in 300..399;  // Any 3xx status
}
```

**Range Syntax**:
- Format: `status in MIN..MAX`
- Inclusive range (includes both MIN and MAX)
- Common ranges: `200..299` (success), `400..499` (client error), `500..599` (server error)

### Assertion Types

#### Status Code Assertion

```
expect status = 200;           // Exact match
expect status in 200..299;     // Range match
```

#### Header Assertion

```
expect header "Content-Type" = "application/json";              // Exact match
expect header "Content-Type" contains "json";                   // Substring match
expect header "X-Custom-Header" = "value";                      // Custom header
```

#### Body Assertion

```
expect body contains "\"token\":";                              // JSON field
expect body contains "\"id\": $userId";                         // With variable
expect body contains "success";                                 // Simple text
```

### Comments

```
// Single-line comment explaining the next test

/* 
   Multi-line comment for
   more detailed explanations
   of complex test scenarios
*/

test Example {
  GET "/api/test";  // Inline comment
  expect status = 200;
}
```

---

## Example Test Files

### Example 1: Basic Authentication Test

**File**: `examples/given.test`

**Purpose**: Demonstrates basic POST and GET requests with authentication

```
config {
  base_url = "http://localhost:8080";
  header "Content-Type" = "application/json";
}

// Define reusable variables
let user = "admin";
let id = 42;

// Test user authentication
test Login {
  POST "/api/login" {
    body = "{ \"username\": \"$user\", \"password\": \"1234\" }";
  }
  expect status = 200;
  expect header "Content-Type" contains "json";
  expect body contains "\"token\":";
}

// Test user retrieval
test GetUser {
  GET "/api/users/$id";
  expect status = 200;
  expect body contains "\"id\": 42";
}
```

**Run**:
```batch
compile.bat examples\given.test
```

**Expected Behavior**:
- Sends POST request to `/api/login` with admin credentials
- Verifies 200 status and token in response
- Sends GET request to `/api/users/42`
- Verifies user data contains correct ID

---

### Example 2: Complete CRUD Operations

**File**: `examples/all_methods.test`

**Purpose**: Demonstrates all HTTP methods (POST, GET, PUT, DELETE)

```
config {
  base_url = "http://localhost:8080";
  header "Content-Type" = "application/json";
  header "X-App" = "TestLangDemo";
}

let user = "admin";
let id = 42;

// Test user login
test Login {
  POST "/api/login" {
    body = "{ \"username\": \"$user\", \"password\": \"1234\" }";
  }
  expect status = 200;
  expect header "Content-Type" contains "json";
  expect body contains "\"token\":";
}

// Test user creation
test CreateUser {
  POST "/api/users" {
    body = "{ \"id\": \"$id\", \"name\": \"Test User\", \"email\": \"test@example.com\", \"role\": \"user\" }";
  }
  expect status = 201;
  expect body contains "success";
}

// Test user retrieval
test GetUser {
  GET "/api/users/$id";
  expect status = 200;
  expect header "Content-Type" contains "json";
  expect body contains "\"id\": 42";
}

// Test user update
test UpdateUser {
  PUT "/api/users/$id" {
    header "Content-Type" = "application/json";
    body = "{ \"role\": \"ADMIN\" }";
  }
  expect status = 200;
  expect header "X-App" = "TestLangDemo";
  expect header "Content-Type" contains "json";
  expect body contains "\"updated\": true";
  expect body contains "\"role\": \"ADMIN\"";
}

// Test user deletion
test DeleteUser {
  DELETE "/api/users/$id";
  expect status = 200;
  expect header "Content-Type" contains "json";
  expect body contains "\"deleted\": true";
}
```

**Run**:
```batch
compile.bat examples\all_methods.test
```

**Expected Behavior**:
- Creates a new user via POST
- Retrieves the user via GET
- Updates user role to ADMIN via PUT
- Deletes the user via DELETE
- Verifies all status codes and response content

---

### Example 3: Advanced Features (Multiline & Ranges)

**File**: `examples/optional.test`

**Purpose**: Demonstrates multiline strings and status code ranges

```
config {
  base_url = "http://localhost:8080";
  header "Content-Type" = "application/json";
}

let userId = "42";

// Multiline body example
test LoginMultiline {
  POST "/api/login" {
    body = """
    {
      "username": "admin",
      "password": "1234",
      "remember": true,
      "deviceInfo": {
        "browser": "Chrome",
        "os": "Windows"
      }
    }
    """;
  }
  expect status = 200;
  expect body contains "token";
}

// Status range example
test SuccessRange {
  GET "/api/users/$userId";
  expect status in 200..299;
  expect body contains "user";
}

// Health check with range
test AnySuccess {
  GET "/api/health";
  expect status in 200..299;
  expect body contains "status";
}

// Update with multiline body
test UpdateWithMultilineBody {
  PUT "/api/users/$userId" {
    body = """
    {
      "role": "ADMIN",
      "permissions": [
        "read",
        "write",
        "delete"
      ],
      "active": true
    }
    """;
  }
  expect status in 200..299;
  expect body contains "updated";
}
```

**Run**:
```batch
compile.bat examples\optional.test
```

**Expected Behavior**:
- Tests multiline JSON bodies with proper formatting
- Uses status ranges (200..299) for flexible success checking
- Demonstrates nested JSON structures
- Verifies response content without exact status matching

---

## Build & Compilation

### Full Build Process

The build process consists of several distinct phases:

#### Phase 1: Generate Lexer

```batch
java -jar lib\jflex-full-1.9.1.jar -d src src\lexer.flex
```

**Input**: `src/lexer.flex` (lexical specification)

**Output**: `src/Lexer.java` (Java lexer implementation)

**Purpose**: Converts the lexical specification into a Java class that tokenizes input

#### Phase 2: Generate Parser

```batch
java -jar lib\java-cup-11b.jar -destdir src -parser Parser -symbols sym src\parser.cup
```

**Input**: `src/parser.cup` (grammar specification)

**Output**: 
- `src/Parser.java` (parser implementation)
- `src/sym.java` (token symbol definitions)

**Purpose**: Converts the grammar specification into a Java parser that builds parse trees

#### Phase 3: Compile AST Classes

```batch
javac -d ClassLib -cp lib\java-cup-11b-runtime.jar src\AST\*.java
```

**Input**: All Java files in `src/AST/`

**Output**: Compiled classes in `ClassLib/AST/*.class`

**Purpose**: Compiles the Abstract Syntax Tree node classes

#### Phase 4: Compile Main Classes

```batch
javac -cp "ClassLib;lib\java-cup-11b-runtime.jar" -d ClassLib src\*.java
```

**Input**: All Java files in `src/` (Lexer, Parser, TestParser)

**Output**: Compiled classes in `ClassLib/*.class`

**Purpose**: Compiles the main compiler classes

### Incremental Builds

For faster recompilation when only specific components change:

#### Modified `.test` file only

```batch
java -cp "ClassLib;lib\java-cup-11b-runtime.jar" TestParser examples\your_file.test
javac -cp lib\junit-platform-console-standalone-1.9.3.jar -d output output\GeneratedTests.java
```

**Use when**: You only changed the test specification

#### Modified AST classes

```batch
javac -d ClassLib -cp lib\java-cup-11b-runtime.jar src\AST\*.java
javac -cp "ClassLib;lib\java-cup-11b-runtime.jar" -d ClassLib src\TestParser.java
```

**Use when**: You modified AST node implementations

#### Modified parser grammar

```batch
java -jar lib\java-cup-11b.jar -destdir src -parser Parser -symbols sym src\parser.cup
javac -cp "ClassLib;lib\java-cup-11b-runtime.jar" -d ClassLib src\Parser.java src\sym.java
javac -cp "ClassLib;lib\java-cup-11b-runtime.jar" -d ClassLib src\TestParser.java
```

**Use when**: You changed the grammar specification

#### Modified lexer specification

```batch
java -jar lib\jflex-full-1.9.1.jar -d src src\lexer.flex
javac -cp "ClassLib;lib\java-cup-11b-runtime.jar" -d ClassLib src\Lexer.java
javac -cp "ClassLib;lib\java-cup-11b-runtime.jar" -d ClassLib src\TestParser.java
```

**Use when**: You changed the lexical specification

### Clean Build

For a complete rebuild from scratch:

```batch
# Delete all generated and compiled files
rmdir /s /q ClassLib
rmdir /s /q output
del /q src\Lexer.java src\Parser.java src\sym.java

# Rebuild everything
build.bat
```

---

## Project Structure

### Compiler Directory Structure

```
TestLangPP/
├── lib/                                          # External libraries
│   ├── jflex-full-1.9.1.jar                      # Lexer generator
│   ├── java-cup-11b.jar                          # Parser generator
│   ├── java-cup-11b-runtime.jar                  # Parser runtime
│   └── junit-platform-console-standalone-1.9.3.jar # Test framework
│
├── src/                                          # Source code
│   ├── lexer.flex                                # Lexer specification (hand-written)
│   ├── parser.cup                                # Grammar specification (hand-written)
│   ├── TestParser.java                           # Main compiler class (hand-written)
│   ├── Lexer.java                                # Generated lexer (auto-generated)
│   ├── Parser.java                               # Generated parser (auto-generated)
│   ├── sym.java                                  # Generated token symbols (auto-generated)
│   │
│   └── AST/                                      # Abstract Syntax Tree nodes (hand-written)
│       ├── AssertStmt.java                       # Assertion statement node
│       ├── BodyStmt.java                         # Request body statement node
│       ├── CodeGenerator.java                    # Code generation visitor
│       ├── ConfigBlock.java                      # Configuration block node
│       ├── ConfigItem.java                       # Configuration item node
│       ├── HeaderStmt.java                       # Header statement node
│       ├── LetStmt.java                          # Variable declaration node
│       ├── Program.java                          # Root program node
│       ├── RequestStmt.java                      # HTTP request statement node
│       ├── Statement.java                        # Base statement interface
│       └── TestBlock.java                        # Test block node
│
├── ClassLib/                                     # Compiled classes (auto-generated)
│   ├── Lexer.class                               # Compiled lexer
│   ├── Parser.class                              # Compiled parser
│   ├── sym.class                                 # Compiled symbol table
│   ├── TestParser.class                          # Compiled main class
│   └── AST/                                      # Compiled AST nodes
│       ├── AssertStmt.class
│       ├── BodyStmt.class
│       ├── CodeGenerator.class
│       ├── ConfigBlock.class
│       ├── ConfigItem.class
│       ├── HeaderStmt.class
│       ├── LetStmt.class
│       ├── Program.class
│       ├── RequestStmt.class
│       ├── Statement.class
│       └── TestBlock.class
│
├── examples/                                     # Example test files
│   ├── given.test                                # Basic authentication example
│   ├── all_methods.test                          # All HTTP methods example
│   ├── optional.test                             # Advanced features example
│   ├── test_duplicate.test                       # Semantic error example
│   ├── invalid.test                              # Lexical error example
│   ├── invalid2.test                             # Type error example
│   ├── invalid3.test                             # Type error example
│   └── invalid4.test                             # Syntax error example
│
├── output/                                       # Generated test files (auto-generated)
│   ├── GeneratedTests.java                       # Generated JUnit test class
│   └── GeneratedTests.class                      # Compiled test class
│
├── build.bat                                     # Build automation script
├── compile.bat                                   # Compile and run script
└── README.md                                     # This documentation file
```

### Backend Directory Structure

```
TestLangPP-Backend/
├── src/main/java/com/testlang/backend/
│   ├── config/
│   │   └── WebConfig.java                        # CORS and web configuration
|   |   └── RequestLoggingFilter.java             # Logs HTTP requests and responses  
│   │
│   ├── controller/
│   │   ├── AuthController.java                   # POST /api/login endpoint
│   │   ├── HealthController.java                 # GET /api/health endpoint
│   │   └── UserController.java                   # User CRUD endpoints
│   │                                             # GET/POST/PUT/DELETE /api/users
│   │
│   ├── model/
│   │   ├── ApiResponse.java                      # Generic API response model
│   │   ├── LoginRequest.java                     # Login request DTO
│   │   ├── LoginResponse.java                    # Login response DTO
│   │   └── User.java                             # User entity model
│   │
│   └── BackendApplication.java                   # Spring Boot main class
│
├── src/main/resources/
│   └── application.properties                    # Spring Boot configuration
│
├── pom.xml                                       # Maven build configuration
└── target/
    └── testlang-backend-0.0.1-SNAPSHOT.jar       # Executable JAR file
```

---

## Running Tests

### Available Test Files

The `examples/` directory contains various test files demonstrating different features and error scenarios:

#### Valid Test Files

```batch
# Basic authentication and user retrieval
compile.bat examples\given.test

# Complete CRUD operations with all HTTP methods
compile.bat examples\all_methods.test

# Advanced features: multiline bodies and status ranges
compile.bat examples\optional.test
```

#### Error Demonstration Files

```batch
# Duplicate variable declaration (semantic error)
compile.bat examples\test_duplicate.test

# Invalid variable name starting with digit (lexical error)
compile.bat examples\invalid.test

# Numeric body instead of string (type error)
compile.bat examples\invalid2.test

# String status instead of integer (type error)
compile.bat examples\invalid3.test

# Missing semicolon (syntax error)
compile.bat examples\invalid4.test
```

---

## Test File Descriptions

### Valid Test Files

#### `given.test`
**Purpose**: Basic authentication and user retrieval

**Tests**:
- **Login**: Posts credentials to `/api/login`
  - Verifies status 200
  - Checks Content-Type header contains "json"
  - Confirms response body contains token field
  
- **GetUser**: Retrieves user data from `/api/users/42`
  - Verifies status 200
  - Confirms user ID is 42 in response body

**Run**: `compile.bat examples\given.test`

---

#### `all_methods.test`
**Purpose**: Comprehensive demonstration of all HTTP methods

**Tests**:
- **Login**: Posts credentials to `/api/login`
  - Verifies status 200
  - Checks headers and token presence
  
- **CreateUser**: Posts new user data to `/api/users`
  - Verifies status 201 (Created)
  - Checks response contains "success"
  
- **GetUser**: Gets user data from `/api/users/42`
  - Verifies status 200
  - Checks Content-Type header
  - Confirms user ID in response
  
- **UpdateUser**: Puts updated data to `/api/users/42`
  - Verifies status 200
  - Checks X-App header equals "TestLangDemo"
  - Checks Content-Type header
  - Confirms response contains "updated": true
  - Confirms response contains "role": "ADMIN"
  
- **DeleteUser**: Deletes user at `/api/users/42`
  - Verifies status 200
  - Checks Content-Type header
  - Confirms response contains "deleted": true

**Run**: `compile.bat examples\all_methods.test`

---

#### `optional.test`
**Purpose**: Advanced syntax features

**Tests**:
- **LoginMultiline**: Posts multiline JSON body to `/api/login`
  - Uses triple-quoted multiline string
  - Verifies status 200
  - Checks response contains "token"
  
- **SuccessRange**: Gets user from `/api/users/42`
  - Uses status range check (200..299)
  - Verifies response contains "user"
  
- **AnySuccess**: Gets health status from `/api/health`
  - Uses status range check (200..299)
  - Verifies response contains "status"
  
- **UpdateWithMultilineBody**: Puts multiline JSON to `/api/users/42`
  - Uses triple-quoted multiline string
  - Uses status range check (200..299)
  - Verifies response contains "updated"

**Run**: `compile.bat examples\optional.test`

---

### Error Demonstration Files

#### `invalid.test`
**Purpose**: Demonstrates lexical validation error

**Error**: Invalid variable name starting with digit

**Code**:
```
let 2a = "x";
```

**Expected Error Message**:
```
Line 1: Variable name cannot start with a digit: 2a
```

**Explanation**: Variable names must start with a letter or underscore according to language syntax rules

**Run**: `compile.bat examples\invalid.test`

---

#### `invalid2.test`
**Purpose**: Demonstrates type checking error

**Error**: Numeric body instead of string

**Code**:
```
test InvalidBody {
  POST "/x" {
    body = 123;
  };
  expect status = 200;
  expect body contains "ok";
}
```

**Expected Error Message**:
```
Line 4: Body must be a string, not a number
```

**Explanation**: Request body values must be strings (quoted). Numeric literals are not valid for body content.

**Run**: `compile.bat examples\invalid2.test`

---

#### `invalid3.test`
**Purpose**: Demonstrates type checking error

**Error**: String status instead of integer

**Code**:
```
test InvalidStatus {
  GET "/y";
  expect status = "200";
}
```

**Expected Error Message**:
```
Line 3: Status code must be an integer, not a string
```

**Explanation**: HTTP status codes must be numeric literals, not string literals. Use `200` instead of `"200"`.

**Run**: `compile.bat examples\invalid3.test`

---

#### `invalid4.test`
**Purpose**: Demonstrates syntax error

**Error**: Missing semicolon after GET statement

**Code**:
```
test MissingSemicolon {
  GET "/z"
  expect status = 200;
}
```

**Expected Error Message**:
```
Line 3: Syntax error - expected ';' after request statement
```

**Explanation**: All statements in TestLangPP must be terminated with a semicolon. The parser expects a semicolon after the GET request.

**Run**: `compile.bat examples\invalid4.test`

---

#### `test_duplicate.test`
**Purpose**: Demonstrates semantic validation error

**Error**: Duplicate variable declaration

**Code**:
```
let userId = "42";
let userId = "100";

test Test {
  GET "/api/users/$userId";
  expect status = 200;
  expect body contains "test";
}
```

**Expected Error Message**:
```
Line 2: Variable 'userId' is already declared
```

**Explanation**: The semantic analyzer detects duplicate variable declarations before code generation, preventing runtime conflicts.

**Run**: `compile.bat examples\test_duplicate.test`

---

### Expected Output Format

When running a valid test file, you should see output similar to:

```
========================================
TestLangPP - Compile Test File
========================================

Input: examples\given.test

========================================
[1/3] Parsing and generating code...
========================================

✓ Parse successful!
  Config: present
  Variables: 2
  Tests: 2
✓ Validation passed!
Generated: output\GeneratedTests.java
✓ Code generation complete!

Generated file: output\GeneratedTests.java
[OK] Code generated

========================================
[2/3] Compiling generated tests...
========================================

[OK] Generated tests compiled successfully

========================================
[3/3] Running tests...
========================================

NOTE: Make sure your API server is running!
      Default: http://localhost:8080
----------------------------------------

Test run finished after 523 ms
[         2 containers found      ]
[         0 containers skipped    ]
[         2 containers started    ]
[         0 containers aborted    ]
[         2 containers successful ]
[         0 containers failed     ]
[         2 tests found           ]
[         0 tests skipped         ]
[         2 tests started         ]
[         0 tests aborted         ]
[         2 tests successful      ]
[         0 tests failed          ]

========================================
Test execution complete!
========================================
```

---

## Troubleshooting

### Common Errors and Solutions

#### Error: "Class not found"

**Symptoms**: 
```
Error: Could not find or load main class TestParser
```

**Cause**: Compiler classes not built or CLASSPATH incorrect

**Solution**: Rebuild from scratch
```batch
build.bat
```

**Verification**:
- Check that `ClassLib\TestParser.class` exists
- Check that `ClassLib\AST\` contains multiple `.class` files

---

#### Error: "Cannot find symbol: sym"

**Symptoms**:
```
src\Parser.java:X: error: cannot find symbol
  symbol:   class sym
```

**Cause**: Parser not generated from grammar specification

**Solution**: 
```batch
java -jar lib\java-cup-11b.jar -destdir src -parser Parser -symbols sym src\parser.cup
javac -cp "ClassLib;lib\java-cup-11b-runtime.jar" -d ClassLib src\Parser.java src\sym.java
```

**Verification**: Check that `src\sym.java` exists

---

#### Error: "Lexer.java not found"

**Symptoms**:
```
javac: file not found: src\Lexer.java
```

**Cause**: Lexer not generated from specification

**Solution**:
```batch
java -jar lib\jflex-full-1.9.1.jar -d src src\lexer.flex
javac -cp "ClassLib;lib\java-cup-11b-runtime.jar" -d ClassLib src\Lexer.java
```

**Verification**: Check that `src\Lexer.java` exists

---

#### Error: Syntax Errors in `.test` Files

**Example Error**:
```
Line 10: Syntax error - expected ';' after request statement
```

**Common Issues and Corrections**:

**Missing Semicolon**:
```
// Wrong
GET "/api/users" expect status = 200;

// Correct
GET "/api/users";
expect status = 200;
```

**Invalid Variable Name**:
```
// Wrong - starts with digit
let 2user = "admin";

// Correct
let user2 = "admin";
```

**Duplicate Variable**:
```
// Wrong - duplicate declaration
let userId = 42;
let userId = 100;  // Error: duplicate variable

// Correct - use different names
let userId = 42;
let accountId = 100;
```

**Wrong Type for Body**:
```
// Wrong - numeric body
POST "/api/data" {
  body = 123;
}

// Correct - string body
POST "/api/data" {
  body = "123";
}
```

**Wrong Type for Status**:
```
// Wrong - string status
expect status = "200";

// Correct - numeric status
expect status = 200;
```

---

#### Error: "Connection refused"

**Symptoms**:
```
java.net.ConnectException: Connection refused: connect
```

**Cause**: Backend server not running

**Solution**: Start the backend server

**Option 1 - Maven**:
```batch
cd TestLangPP-Backend
mvn spring-boot:run
```

**Option 2 - Pre-built JAR**:
```batch
java -jar TestLangPP-Backend\target\testlang-backend-0.0.1-SNAPSHOT.jar
```

**Verification**: 
- Open browser to `http://localhost:8080/api/health`
- Should see JSON response with status "ok"

---

#### Error: Tests Fail with 404 Not Found

**Symptoms**:
```
Test failed: Expected status 200 but got 404
```

**Cause**: Incorrect endpoint URL or base URL misconfiguration

**Checks**:
1. Verify `base_url` in config block matches backend server
2. Ensure backend is running on correct port (default: 8080)
3. Check endpoint paths match backend controller routes
4. Verify no typos in endpoint strings

**Available Backend Endpoints**:
- `POST /api/login`
- `GET /api/users/{id}`
- `POST /api/users`
- `PUT /api/users/{id}`
- `DELETE /api/users/{id}`
- `GET /api/health`

---

#### Error: Compilation Errors in Generated Code

**Symptoms**: Generated Java code fails to compile

**Cause**: Bug in code generator

**Debug Steps**:

1. View generated code:
```batch
type output\GeneratedTests.java
```

2. Check for obvious syntax errors (missing braces, quotes, etc.)

3. Review AST construction in parser specification

4. Verify code generation logic in `CodeGenerator.java`

5. Check variable interpolation is working correctly

**Common Generated Code Issues**:
- Unescaped quotes in strings
- Missing imports
- Incorrect method signatures
- Type mismatches

---

#### Error: JUnit Tests Don't Run

**Symptoms**: No tests executed or "No tests found"

**Cause**: Test class not properly annotated or compiled

**Solution**:

1. Verify generated test class exists:
```batch
dir output\GeneratedTests.class
```

2. Check JUnit JAR is correct version:
```batch
java -jar lib\junit-platform-console-standalone-1.9.3.jar --version
```

3. Recompile generated tests:
```batch
javac -cp lib\junit-platform-console-standalone-1.9.3.jar -d output output\GeneratedTests.java
```

4. Run with verbose output:
```batch
java -jar lib\junit-platform-console-standalone-1.9.3.jar --class-path output --scan-classpath
```

---

#### Error: "OutOfMemoryError" During Compilation

**Symptoms**:
```
java.lang.OutOfMemoryError: Java heap space
```

**Cause**: Large test files or insufficient heap space

**Solution**: Increase Java heap size
```batch
set JAVA_OPTS=-Xmx1024m
javac %JAVA_OPTS% -cp lib\junit-platform-console-standalone-1.9.3.jar -d output output\GeneratedTests.java
```

---

## Command Reference

### Essential Commands

| Command | Description | When to Use |
|---------|-------------|-------------|
| `build.bat` | Build compiler from scratch | Initial setup, after grammar/lexer changes |
| `compile.bat <file.test>` | Compile and run a test file | Running tests end-to-end |
| `java -cp "ClassLib;lib\java-cup-11b-runtime.jar" TestParser <file>` | Parse test file only | Testing parser without running tests |
| `type output\GeneratedTests.java` | View generated Java code | Debugging code generation |
| `dir ClassLib\*.class` | List compiled classes | Verifying build output |

### Backend Commands

| Command | Description | When to Use |
|---------|-------------|-------------|
| `cd TestLangPP-Backend && mvn spring-boot:run` | Start backend with Maven | Development and testing |
| `java -jar TestLangPP-Backend\target\testlang-backend-0.0.1-SNAPSHOT.jar` | Run pre-built JAR | Production or when Maven unavailable |
| `mvn clean package` | Build backend JAR | After backend code changes |
| `curl http://localhost:8080/api/health` | Test backend health | Verifying backend is running |

### Development Commands

| Command | Description | When to Use |
|---------|-------------|-------------|
| `rmdir /s /q ClassLib output` | Clean build artifacts | Before fresh rebuild |
| `java -jar lib\junit-platform-console-standalone-1.9.3.jar --class-path output --scan-classpath` | Run tests manually | Running tests without full compilation |
| `javac -verbose -cp "ClassLib;lib\java-cup-11b-runtime.jar" -d ClassLib src\*.java` | Verbose compilation | Debugging compilation issues |
| `java -version` | Check Java version | Verifying Java installation |

### Advanced Commands

| Command | Description | When to Use |
|---------|-------------|-------------|
| `java -jar lib\jflex-full-1.9.1.jar --dump src\lexer.flex` | Dump lexer tables | Debugging lexer behavior |
| `java -jar lib\java-cup-11b.jar -dump src\parser.cup` | Dump parser tables | Debugging parser conflicts |
| `java -cp "ClassLib;lib\java-cup-11b-runtime.jar" TestParser -debug <file>` | Parse with debug output | Detailed parsing trace |

---

## Environment Variables (Optional)

For easier command-line usage, you can set environment variables:

```batch
set CLASSPATH=ClassLib;lib\java-cup-11b-runtime.jar
set JUNIT_JAR=lib\junit-platform-console-standalone-1.9.3.jar
set TESTLANG_HOME=%CD%
```

Then commands become shorter:

```batch
# Instead of full classpath
java -cp "ClassLib;lib\java-cup-11b-runtime.jar" TestParser examples\given.test

# Use
java TestParser examples\given.test

# Instead of full JUnit path
javac -cp lib\junit-platform-console-standalone-1.9.3.jar -d output output\GeneratedTests.java

# Use
javac -cp %JUNIT_JAR% -d output output\GeneratedTests.java
```

---

## Build Verification Checklist

After running `build.bat`, verify the following:

**Generated Source Files**:
- [ ] `src\Lexer.java` exists and is recent
- [ ] `src\Parser.java` exists and is recent
- [ ] `src\sym.java` exists and is recent

**Compiled Classes**:
- [ ] `ClassLib\TestParser.class` exists
- [ ] `ClassLib\Lexer.class` exists
- [ ] `ClassLib\Parser.class` exists
- [ ] `ClassLib\sym.class` exists
- [ ] `ClassLib\AST\` contains at least 11 `.class` files

**No Errors**:
- [ ] No compilation errors in build output
- [ ] No "cannot find symbol" errors
- [ ] No "class not found" errors

**Backend**:
- [ ] Backend server runs on port 8080
- [ ] Health endpoint responds: `http://localhost:8080/api/health`

**Test Compilation**:
- [ ] Example test compiles successfully: `compile.bat examples\given.test`
- [ ] Generated tests execute without errors
- [ ] JUnit reports successful test runs

---

## Performance Tips

### Compilation Performance

**Incremental Builds**: Use incremental compilation commands when only specific files change to save time.

**Parallel Compilation**: For large projects, consider parallel javac execution:
```batch
javac -J-XX:+UseParallelGC -d ClassLib -cp lib\java-cup-11b-runtime.jar src\AST\*.java
```

**Classpath Optimization**: Set CLASSPATH once rather than specifying on each command.

### Test Execution Performance

**Disable Banner**: Use `--disable-banner` flag for faster JUnit output.

**Filter Tests**: Run specific tests using JUnit filters:
```batch
java -jar lib\junit-platform-console-standalone-1.9.3.jar --class-path output --select-method GeneratedTests#Login
```

**Backend Warmup**: Keep backend running between test executions to avoid startup time.

---

## Additional Resources

### Official Documentation

**JFlex Manual**: [https://jflex.de/manual.html](https://jflex.de/manual.html)
- Lexical analyzer generator documentation
- Regular expression syntax
- Lexer actions and state machines

**CUP Manual**: [http://www2.cs.tum.edu/projects/cup/](http://www2.cs.tum.edu/projects/cup/)
- Parser generator documentation
- Grammar specification syntax
- Conflict resolution strategies

**JUnit 5 User Guide**: [https://junit.org/junit5/docs/current/user-guide/](https://junit.org/junit5/docs/current/user-guide/)
- Test framework documentation
- Assertions and annotations
- Test lifecycle management

**Spring Boot Documentation**: [https://spring.io/projects/spring-boot](https://spring.io/projects/spring-boot)
- Backend framework documentation
- REST API development
- Configuration management

### Related Topics

**Compiler Design**:
- "Compilers: Principles, Techniques, and Tools" (Dragon Book)
- "Engineering a Compiler" by Cooper and Torczon
- "Modern Compiler Implementation" by Appel

**Domain-Specific Languages**:
- "Domain-Specific Languages" by Martin Fowler
- "DSL Engineering" by Markus Voelter

**HTTP Testing**:
- REST API testing best practices
- HTTP specification (RFC 7230-7235)
- JSON specification (RFC 8259)

---

## Development Roadmap

### Planned Features

**Language Enhancements**:
- Regular expression matching for response validation
- JSON path queries (e.g., `$.user.name`)
- Environment variable support
- Test parameterization and data-driven tests
- Support for authentication tokens and sessions

---
### Language Features
- Additional HTTP methods (PATCH, OPTIONS, HEAD)
- Response validation (schema validation, regex matching)

### Tooling
- IDE plugins for syntax highlighting and auto-completion
- Command-line debugger for step-through execution

### Documentation
- Additional examples and tutorials
- Video walkthroughs
- Interactive playground
- Translation to other languages

---

## Academic Context

This project was developed as part of coursework demonstrating fundamental concepts in:

**Compiler Design Principles**:
- Lexical analysis and tokenization
- Syntax analysis and parsing
- Abstract syntax tree construction
- Semantic analysis and type checking
- Code generation and optimization

**Language Implementation**:
- Grammar design and specification
- Symbol table management
- Error handling and reporting
- Runtime system design

**Domain-Specific Languages**:
- Language design for specific problem domains
- Trade-offs between expressiveness and simplicity
- Integration with existing ecosystems
- User-centered language design

---

## Author

**Kekulanthale K. M. N. Y**  
Student ID: IT23657496

For questions, suggestions, or issues, please contact through the course platform or create an issue in the repository.

---

---

**Made with dedication for better API testing**

**Version**: 1.0.0  
**Last Updated**: 2024  
**Status**: Active Development
