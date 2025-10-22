package AST;

import java.util.*;

// Root of the parse tree
public class Program {
    ConfigBlock config;
    List<LetStmt> variables;
    List<TestBlock> tests;

    public Program(ConfigBlock config, List<LetStmt> variables, List<TestBlock> tests) {
        this.config = config;
        this.variables = variables != null ? variables : new ArrayList<>();
        this.tests = tests != null ? tests : new ArrayList<>();
    }
}