import java.io.FileReader;
import java_cup.runtime.Symbol;

public class LexerTest {
    public static void main(String[] args) throws Exception {
        // You can change this to the path of your .test file
        String filePath = "../given.test";

        System.out.println("🔍 Reading file: " + filePath);
        FileReader reader = new FileReader(filePath);
        Lexer lexer = new Lexer(reader);

        Symbol token;
        while ((token = lexer.next_token()).sym != sym.EOF) {
            System.out.println(
                    "Token: " + sym.terminalNames[token.sym] +
                            (token.value != null ? (" -> " + token.value) : "")
            );
        }

        System.out.println("✅ Lexer finished successfully!");
    }
}
