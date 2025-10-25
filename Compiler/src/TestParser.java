import java.io.*;
import java_cup.runtime.*;
import AST.*;

public class TestParser {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Default output: output/GeneratedTests.java");
            return;
        }

        String inputFile = args[0];
        String outputFile = args.length > 1 ? args[1] : "output/GeneratedTests.java";

        try {
            // Parse the input file
            Lexer lexer = new Lexer(new FileReader(inputFile));
            Parser p = new Parser(lexer);

            Symbol result = null;
            try {
                result = p.parse();
            } catch (Exception e) {
                // Parser already printed the error message, just exit
                System.exit(1);
            }

            // Check if parse was successful
            if (result == null || result.value == null) {
                System.err.println("\n[ERROR] Parse failed - no result returned");
                System.exit(1);
            }

            // Verify we got the correct type
            if (!(result.value instanceof Program)) {
                System.err.println("\n[ERROR] Parse failed - invalid parse result");
                System.err.println("  Expected Program but got: " + result.value.getClass().getName());
                System.exit(1);
            }

            Program program = (Program) result.value;

            System.out.println("  Parse successful!");
            System.out.println("  Config: " + (program.config != null ? "present" : "none"));
            System.out.println("  Variables: " + program.variables.size());
            System.out.println("  Tests: " + program.tests.size());

            // Validate
            validateProgram(program);

            // Generate code
            CodeGenerator generator = new CodeGenerator(program);
            generator.generate(outputFile);

            System.out.println("✓ Code generation complete!");
            System.out.println("\nGenerated file: " + outputFile);

        } catch (FileNotFoundException e) {
            System.err.println("\n[ERROR] File not found: " + inputFile);
            System.exit(1);
        } catch (Exception e) {
            System.err.println("\n[ERROR] " + e.getMessage());
            if (System.getenv("DEBUG") != null) {
                e.printStackTrace();
            }
            System.exit(1);
        }
    }

    private static void validateProgram(Program program) throws Exception {
        // Check that each test has at least one request and two assertions
        for (TestBlock test : program.tests) {
            int requestCount = 0;
            int assertionCount = 0;

            for (Statement stmt : test.statements) {
                if (stmt instanceof RequestStmt) requestCount++;
                if (stmt instanceof AssertStmt) assertionCount++;
            }

            if (requestCount < 1) {
                throw new Exception("Test '" + test.name + "' must have at least 1 request");
            }
            if (assertionCount < 2) {
                throw new Exception("Test '" + test.name + "' must have at least 2 assertions");
            }
        }

        System.out.println("✓ Validation passed!");
    }
}