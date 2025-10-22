public class sym {
    public static final int EOF = 0;
    public static final int error = 1;
    public static final int CONFIG = 2;
    public static final int BASE_URL = 3;
    public static final int HEADER = 4;
    public static final int LET = 5;
    public static final int TEST = 6;
    public static final int GET = 7;
    public static final int POST = 8;
    public static final int PUT = 9;
    public static final int DELETE = 10;
    public static final int EXPECT = 11;
    public static final int STATUS = 12;
    public static final int BODY = 13;
    public static final int CONTAINS = 14;
    public static final int LBRACE = 15;
    public static final int RBRACE = 16;
    public static final int SEMI = 17;
    public static final int EQUALS = 18;
    public static final int STRING = 19;
    public static final int NUMBER = 20;
    public static final int IDENT = 21;

    // For printing readable token names
    public static final String[] terminalNames = {
            "EOF", "error", "CONFIG", "BASE_URL", "HEADER", "LET", "TEST",
            "GET", "POST", "PUT", "DELETE", "EXPECT", "STATUS", "BODY", "CONTAINS",
            "LBRACE", "RBRACE", "SEMI", "EQUALS", "STRING", "NUMBER", "IDENT"
    };
}
