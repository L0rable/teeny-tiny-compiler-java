import lexer.Lexer;
import parser.Parser;
import emitter.Emitter;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Main {
    private static String readFromFile(InputStream inputStream) throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("\\n"))
                    line = line.replace("\\n", "\n");
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }
    
    public static void main(String[] args) {
        try {
            System.out.println("Teeny Tiny Compiler");
            
            InputStream sourceFile = new FileInputStream("../programs/sourceCode.teenytiny");
            String source = readFromFile(sourceFile);
            
            Lexer lexer = new Lexer(source);
            Emitter emitter = new Emitter("output.c");
            Parser parser = new Parser(lexer, emitter);
            
            parser.program();
            emitter.writeFile();
            System.out.println("Compiling completed.");
        } 
        catch (IOException e) { System.err.println(e); }
    }
}
