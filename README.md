# TestLang++ Compilation Guide

Complete guide for building and running the TestLang++ DSL compiler.

---

## 📋 Prerequisites

Before compiling, ensure you have:

- **Java 11+** installed and in PATH
- **JFlex 1.9.1** (in `lib/jflex-full-1.9.1.jar`)
- **CUP 0.11b** (in `lib/java-cup-11b.jar` and `lib/java-cup-11b-runtime.jar`)
- **JUnit 5** (in `lib/junit-platform-console-standalone-1.9.3.jar`)

---

## 🚀 Quick Start (Automated)

### Option 1: Full Build + Run Test (Recommended)

```batch
# Build everything and run a test file
compile.bat examples\given.test
```

This single command will:
1. Build the compiler (if not already built)
2. Parse your `.test` file
3. Generate `GeneratedTests.java`
4. Compile the generated tests
5. Run the tests with JUnit

### Option 2: Build Only

```batch
# Just build the compiler
build.bat
```

---

## 🔧 Manual Compilation (Step-by-Step)

If you prefer to understand each step or need to troubleshoot:

### Step 1: Generate Lexer

```batch
java -jar lib\jflex-full-1.9.1.jar -d src src\lexer.flex
```

**Output:** `src\Lexer.java`

### Step 2: Generate Parser

```batch
java -jar lib\java-cup-11b.jar -destdir src -parser Parser -symbols sym src\parser.cup
```

**Output:** 
- `src\Parser.java`
- `src\sym.java`

### Step 3: Compile AST Classes

```batch
javac -d ClassLib -cp lib\java-cup-11b-runtime.jar src\AST\*.java
```

**Output:** `ClassLib\AST\*.class` files

### Step 4: Compile Main Classes

```batch
javac -cp "ClassLib;lib\java-cup-11b-runtime.jar" -d ClassLib src\*.java
```

**Output:** All `.class` files in `ClassLib\`

### Step 5: Run Parser on Test File

```batch
java -cp "ClassLib;lib\java-cup-11b-runtime.jar" TestParser examples\given.test
```

**Output:** `output\GeneratedTests.java`

### Step 6: Compile Generated Tests

```batch
javac -cp lib\junit-platform-console-standalone-1.9.3.jar -d output output\GeneratedTests.java
```

**Output:** `output\GeneratedTests.class`

### Step 7: Run Tests

```batch
java -jar lib\junit-platform-console-standalone-1.9.3.jar --class-path output --scan-classpath --disable-banner
```

---

## 📁 Directory Structure After Compilation

```
TestLangPP-Compiler/
├── src/
│   ├── lexer.flex              # Source
│   ├── parser.cup              # Source
│   ├── Lexer.java              # Generated (Step 1)
│   ├── Parser.java             # Generated (Step 2)
│   ├── sym.java                # Generated (Step 2)
│   ├── TestParser.java         # Source
│   └── AST/
│       └── *.java              # Source
├── ClassLib/
│   ├── Lexer.class             # Compiled
│   ├── Parser.class            # Compiled
│   ├── sym.class               # Compiled
│   ├── TestParser.class        # Compiled
│   └── AST/
│       └── *.class             # Compiled
└── output/
    ├── GeneratedTests.java     # Generated from .test
    └── GeneratedTests.class    # Compiled JUnit test
```

---

## 🔍 Compilation Workflow Diagram

```
lexer.flex ──[JFlex]──> Lexer.java
parser.cup ──[CUP]───> Parser.java + sym.java
                         │
AST/*.java ──[javac]──> ClassLib/AST/*.class
                         │
*.java ─────[javac]──> ClassLib/*.class
                         │
                    [TestParser]
                         │
given.test ────────> output/GeneratedTests.java
                         │
                    [javac]
                         │
                    GeneratedTests.class
                         │
                    [JUnit 5]
                         │
                    Test Results
```

---

## 🛠️ Troubleshooting

### Error: "Class not found"

**Solution:** Rebuild from scratch
```batch
build.bat
```

### Error: "Cannot find symbol: sym"

**Problem:** Parser not generated

**Solution:** 
```batch
java -jar lib\java-cup-11b.jar -destdir src -parser Parser -symbols sym src\parser.cup
```

### Error: "Lexer.java not found"

**Problem:** Lexer not generated

**Solution:**
```batch
java -jar lib\jflex-full-1.9.1.jar -d src src\lexer.flex
```

### Error: "Line N: expected ';' after request"

**Problem:** Syntax error in your `.test` file

**Example:**
```
// Wrong
GET "/api/users" expect status = 200;

// Correct
GET "/api/users";
expect status = 200;
```

### Error: Tests fail with "Connection refused"

**Problem:** Backend server not running

**Solution:**
```batch
cd TestLangPP-Backend
mvn spring-boot:run
```

Or run the JAR:
```batch
java -jar TestLangPP-Backend\target\testlang-backend-0.0.1-SNAPSHOT.jar
```

---

## 🧪 Testing Your Build

### Create a Simple Test File

Create `examples\test_build.test`:

```
config {
  base_url = "http://localhost:8080";
}

test SimpleGet {
  GET "/api/users/1";
  expect status = 200;
  expect body contains "id";
}
```

### Compile and Run

```batch
compile.bat examples\test_build.test
```

### Expected Output

```
========================================
TestLangPP - Compile Test File
========================================

Input: examples\test_build.test

========================================
[1/3] Parsing and generating code...
========================================

✓ Parse successful!
  Config: present
  Variables: 0
  Tests: 1
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
[         1 containers found      ]
[         0 containers skipped    ]
[         1 containers started    ]
[         0 containers aborted    ]
[         1 containers successful ]
[         0 containers failed     ]
[         1 tests found           ]
[         0 tests skipped         ]
[         1 tests started         ]
[         0 tests aborted         ]
[         1 tests successful      ]
[         0 tests failed          ]

========================================
Test execution complete!
========================================
```

---

## 🎯 Common Commands Reference

| Task | Command |
|------|---------|
| Full build from scratch | `build.bat` |
| Compile and run test | `compile.bat examples\given.test` |
| Clean build | Delete `ClassLib\*` and `output\*`, then `build.bat` |
| Start backend server | `cd TestLangPP-Backend && mvn spring-boot:run` |
| View generated code | `type output\GeneratedTests.java` |
| Run only JUnit tests | `java -jar lib\junit-platform-console-standalone-1.9.3.jar --class-path output --scan-classpath` |

---

## 📝 Environment Variables (Optional)

For easier access, you can set:

```batch
set CLASSPATH=ClassLib;lib\java-cup-11b-runtime.jar
set JUNIT_JAR=lib\junit-platform-console-standalone-1.9.3.jar
```

Then commands become shorter:
```batch
java TestParser examples\given.test
javac -cp %JUNIT_JAR% -d output output\GeneratedTests.java
```

---

## 🔄 Incremental Compilation

If you only modify:

### Just the `.test` file
```batch
java -cp "ClassLib;lib\java-cup-11b-runtime.jar" TestParser examples\given.test
javac -cp lib\junit-platform-console-standalone-1.9.3.jar -d output output\GeneratedTests.java
```

### Just AST classes
```batch
javac -d ClassLib -cp lib\java-cup-11b-runtime.jar src\AST\*.java
```

### Just the parser
```batch
java -jar lib\java-cup-11b.jar -destdir src -parser Parser -symbols sym src\parser.cup
javac -cp "ClassLib;lib\java-cup-11b-runtime.jar" -d ClassLib src\Parser.java src\sym.java
```

### Just the lexer
```batch
java -jar lib\jflex-full-1.9.1.jar -d src src\lexer.flex
javac -cp "ClassLib;lib\java-cup-11b-runtime.jar" -d ClassLib src\Lexer.java
```

---

## 🎓 Build Script Details

### build.bat Flow

1. **Check directories** - Verifies lib/ and src/ exist
2. **Clean previous builds** - Removes old generated files
3. **Generate Lexer** - JFlex → Lexer.java
4. **Generate Parser** - CUP → Parser.java + sym.java
5. **Compile AST** - javac AST/*.java → ClassLib/AST/
6. **Compile Main** - javac *.java → ClassLib/

### compile.bat Flow

1. **Check if built** - Calls build.bat if needed
2. **Verify input** - Checks if .test file exists
3. **Parse** - Runs TestParser on .test file
4. **Compile tests** - javac GeneratedTests.java
5. **Run tests** - JUnit 5 test execution

---

## ✅ Verification Checklist

After compilation, verify:

- [ ] `src\Lexer.java` exists
- [ ] `src\Parser.java` exists
- [ ] `src\sym.java` exists
- [ ] `ClassLib\TestParser.class` exists
- [ ] `ClassLib\AST\` contains multiple .class files
- [ ] `output\GeneratedTests.java` exists (after parsing)
- [ ] No compilation errors
- [ ] Tests run successfully (with backend running)

---

## 🆘 Getting Help

If you encounter issues:

1. **Check Java version**: `java -version` (should be 11+)
2. **Verify CLASSPATH**: Make sure paths use semicolons (`;`) on Windows
3. **Check file paths**: Use backslashes (`\`) on Windows
4. **Enable debug mode**: Set `DEBUG=1` environment variable
5. **View full stack trace**: The compiler shows traces in debug mode

---

## 📚 Additional Resources

- JFlex Manual: https://jflex.de/manual.html
- CUP Manual: http://www2.cs.tum.edu/projects/cup/
- JUnit 5 Guide: https://junit.org/junit5/docs/current/user-guide/
- Assignment Spec: See assignment document for language reference
