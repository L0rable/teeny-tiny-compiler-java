import lexer.Lexer;
import parser.Parser;
import emitter.Emitter;

import java.io.*;

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

            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream sourceFile;
            if (args.length > 1) {
                if (args[1].equals("testPrograms")) {
                    sourceFile = classloader.getResourceAsStream("srcPrograms/" + args[0]);
                } else {
                    sourceFile = new FileInputStream(args[0]);
                }
            } else if (args.length > 0) {
                sourceFile = new FileInputStream(args[0]);
            } else {
                System.out.println("Default Source File: helloWorld.teenytiny");
                sourceFile = classloader.getResourceAsStream("srcPrograms/helloWorld.teenytiny");
            }

            String source = readFromFile(sourceFile);

            Lexer lexer = new Lexer(source);
            Emitter emitter = new Emitter("output.c");
            Parser parser = new Parser(lexer, emitter);

            parser.program();
            emitter.writeFile();
            System.out.println("Compiling completed.");
        }
        catch (IOException e) { e.printStackTrace(); }
    }
}
