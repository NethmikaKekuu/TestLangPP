package AST;

// Variable declaration: let name = value;
public class LetStmt {
    public String name;
    public String value;
    public boolean isNumber;
    public int line;

    public LetStmt(String name, String value, boolean isNumber) {
        this.name = name;
        this.value = value;
        this.isNumber = isNumber;
    }
}
