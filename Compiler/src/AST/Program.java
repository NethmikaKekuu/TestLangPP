package AST;
import java.util.*;

public class Program {
    public ConfigBlock config;
    public List<LetStmt> variables;
    public List<TestBlock> tests;

    public Program(ConfigBlock config, List<LetStmt> variables, List<TestBlock> tests) {
        this.config = config;
        this.variables = variables;
        this.tests = tests;
    }
}