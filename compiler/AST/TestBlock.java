package AST;

import java.util.*;

// Test block: test Name { ... }
public class TestBlock {
    String name;
    List<Statement> statements;

    public TestBlock(String name, List<Statement> statements) {
        this.name = name;
        this.statements = statements != null ? statements : new ArrayList<>();
    }
}