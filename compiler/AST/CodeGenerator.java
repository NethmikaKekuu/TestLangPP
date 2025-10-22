package AST;

import java.io.*;
import java.util.*;

public class CodeGenerator {
    private Program program;
    private PrintWriter out;
    private Map<String, String> variables;

    public CodeGenerator(Program program) {
        this.program = program;
        this.variables = new HashMap<>();

        // Store variables for substitution
        for (LetStmt let : program.variables) {
            variables.put(let.name, let.value);
        }
    }

    public void generate(String outputFile) throws IOException {
        out = new PrintWriter(new FileWriter(outputFile));

        writeImports();
        writeClassHeader();
        writeSetupMethod();
        writeTestMethods();
        writeClassFooter();

        out.close();
        System.out.println("Generated: " + outputFile);
    }

    private void writeImports() {
        out.println("import org.junit.jupiter.api.*;");
        out.println("import static org.junit.jupiter.api.Assertions.*;");
        out.println("import java.net.http.*;");
        out.println("import java.net.*;");
        out.println("import java.time.Duration;");
        out.println("import java.nio.charset.StandardCharsets;");
        out.println("import java.util.*;");
        out.println();
    }

    private void writeClassHeader() {
        out.println("public class GeneratedTests {");

        // Base URL
        String baseUrl = (program.config != null && program.config.baseUrl != null)
                ? program.config.baseUrl
                : "";
        out.println("    static String BASE = \"" + baseUrl + "\";");

        // Default headers
        out.println("    static Map<String,String> DEFAULT_HEADERS = new HashMap<>();");
        out.println("    static HttpClient client;");
        out.println();
    }

    private void writeSetupMethod() {
        out.println("    @BeforeAll");
        out.println("    static void setup() {");
        out.println("        client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();");

        // Add default headers from config
        if (program.config != null) {
            for (Map.Entry<String, String> header : program.config.defaultHeaders.entrySet()) {
                out.println("        DEFAULT_HEADERS.put(\"" + header.getKey() + "\", \"" + header.getValue() + "\");");
            }
        }

        out.println("    }");
        out.println();
    }

    private void writeTestMethods() {
        for (TestBlock test : program.tests) {
            writeTestMethod(test);
        }
    }

    private void writeTestMethod(TestBlock test) {
        out.println("    @Test");
        out.println("    void test_" + test.name + "() throws Exception {");

        // Process statements
        RequestStmt currentRequest = null;
        List<AssertStmt> assertions = new ArrayList<>();

        for (Statement stmt : test.statements) {
            if (stmt instanceof RequestStmt) {
                // If we had a previous request, generate it now
                if (currentRequest != null) {
                    generateRequest(currentRequest, assertions);
                    assertions.clear();
                }
                currentRequest = (RequestStmt) stmt;
            } else if (stmt instanceof AssertStmt) {
                assertions.add((AssertStmt) stmt);
            }
        }

        // Generate the last request
        if (currentRequest != null) {
            generateRequest(currentRequest, assertions);
        }

        out.println("    }");
        out.println();
    }

    private void generateRequest(RequestStmt req, List<AssertStmt> assertions) {
        // Substitute variables in path
        String path = substituteVariables(req.path);

        // Build URL
        String url;
        if (path.startsWith("http://") || path.startsWith("https://")) {
            url = path;
        } else if (path.startsWith("/")) {
            url = "BASE + \"" + path + "\"";
        } else {
            url = "\"" + path + "\"";
        }

        // Create request builder
        out.println("        HttpRequest.Builder b = HttpRequest.newBuilder(URI.create(" + url + "))");
        out.println("            .timeout(Duration.ofSeconds(10))");

        // Add method and body
        switch (req.method) {
            case "GET":
                out.println("            .GET();");
                break;
            case "DELETE":
                out.println("            .DELETE();");
                break;
            case "POST":
            case "PUT":
                if (req.body != null) {
                    String bodyContent = substituteVariables(req.body.content);
                    bodyContent = escapeJavaString(bodyContent);
                    out.println("            ." + req.method + "(HttpRequest.BodyPublishers.ofString(\"" + bodyContent + "\"));");
                } else {
                    out.println("            ." + req.method + "(HttpRequest.BodyPublishers.noBody());");
                }
                break;
        }

        // Add default headers
        out.println("        for (var e : DEFAULT_HEADERS.entrySet()) b.header(e.getKey(), e.getValue());");

        // Add request-specific headers
        for (HeaderStmt header : req.headers) {
            String key = escapeJavaString(header.key);
            String value = escapeJavaString(substituteVariables(header.value));
            out.println("        b.header(\"" + key + "\", \"" + value + "\");");
        }

        // Send request
        out.println("        HttpResponse<String> resp = client.send(b.build(), HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));");
        out.println();

        // Generate assertions
        for (AssertStmt assertion : assertions) {
            generateAssertion(assertion);
        }
    }

    private void generateAssertion(AssertStmt assertion) {
        switch (assertion.type) {
            case "status":
                out.println("        assertEquals(" + assertion.value + ", resp.statusCode());");
                break;

            case "header":
                String headerKey = escapeJavaString(assertion.key);
                String headerValue = escapeJavaString(substituteVariables(assertion.value));

                if (assertion.isContains) {
                    out.println("        assertTrue(resp.headers().firstValue(\"" + headerKey + "\").orElse(\"\").contains(\"" + headerValue + "\"));");
                } else {
                    out.println("        assertEquals(\"" + headerValue + "\", resp.headers().firstValue(\"" + headerKey + "\").orElse(\"\"));");
                }
                break;

            case "body":
                String bodyValue = escapeJavaString(substituteVariables(assertion.value));
                if (assertion.isContains) {
                    out.println("        assertTrue(resp.body().contains(\"" + bodyValue + "\"));");
                } else {
                    out.println("        assertEquals(\"" + bodyValue + "\", resp.body());");
                }
                break;
        }
    }

    private void writeClassFooter() {
        out.println("}");
    }

    private String substituteVariables(String text) {
        String result = text;
        for (Map.Entry<String, String> var : variables.entrySet()) {
            result = result.replace("$" + var.getKey(), var.getValue());
        }
        return result;
    }

    private String escapeJavaString(String s) {
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}