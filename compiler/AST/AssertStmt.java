package AST;

// Assertion: expect status/header/body
public class AssertStmt extends Statement {  // Make it public
    public String type; // "status", "header", "body"
    public String key;  // status code (as string) OR header key OR null for body
    public String value; // expected value or substring
    public boolean isContains; // true if "contains", false if "equals"

    public AssertStmt(String type, String key, String value, boolean isContains) {
        this.type = type;
        this.key = key;
        this.value = value;
        this.isContains = isContains;
    }
}
