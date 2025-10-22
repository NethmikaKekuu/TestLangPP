package AST;

// Header in request block
public class HeaderStmt {
    public String key;
    public String value;

    public HeaderStmt(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
