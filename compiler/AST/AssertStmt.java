package AST;

public class AssertStmt extends Statement {
    public String type; // "status", "header", "body"
    public String key;  // header key if type=header
    public String value; // expected value
    public boolean isContains; // true if `contains` assertion

    public AssertStmt(String type, String key, String value, boolean isContains) {
        this.type = type;
        this.key = key;
        this.value = value;
        this.isContains = isContains;
    }
}