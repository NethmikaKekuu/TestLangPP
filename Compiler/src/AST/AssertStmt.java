package AST;

// Assertion statement: expect status/header/body
public class AssertStmt extends Statement {
    public String type;       // "status", "header", or "body"
    public String key;        // header key (null for status/body)
    public String value;      // expected value
    public boolean isContains; // true for "contains", false for "="

    public AssertStmt(String type, String key, String value, boolean isContains) {
        this.type = type;
        this.key = key;
        this.value = value;
        this.isContains = isContains;
    }
}