package AST;

import java.util.*;

// HTTP request: GET/POST/PUT/DELETE
public class RequestStmt extends Statement {
    String method;
    String path;
    List<HeaderStmt> headers;
    BodyStmt body;

    public RequestStmt(String method, String path, List<HeaderStmt> headers, BodyStmt body) {
        this.method = method;
        this.path = path;
        this.headers = headers != null ? headers : new ArrayList<>();
        this.body = body;
    }
}