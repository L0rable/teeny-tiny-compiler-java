import emitter.Emitter;
import lexer.Lexer;
import org.junit.jupiter.api.*;
import parser.Parser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

class ParserTest {
     Parser parser;
     Lexer lexer;
     Emitter emitter;
     File outputFile;
     final static String outputFileLocation = "outputTest.c";

     @BeforeEach
     void init() {
          String sampleSourceCode = "PRINT \"Hello World!\"";
          this.lexer = new Lexer(sampleSourceCode);
          this.emitter = new Emitter(outputFileLocation);
          this.parser = new Parser(this.lexer, this.emitter);
          this.outputFile = new File(outputFileLocation);
     }

     @AfterAll
     static void cleanup() {
          File outputFile = new File(outputFileLocation);
          outputFile.deleteOnExit();
     }

     void emitProgram(String statement) throws IOException {
          lexer = new Lexer(statement);
          parser = new Parser(lexer, emitter);
          parser.program();
          emitter.writeFile();
     }

     String expectedProgram(String statements) {
          return "#include <stdio.h>" + "\n" + "\n" + "int main() {" + "\n" +
                  statements +
                  "    " + "return 0;" + "\n" + "}" + "\n";
     }

     @Test
     void PRINTStatementTest1() throws IOException {
          String sourceCode = "PRINT \"Hello World!\"\n";
          String statements = "    printf(\"Hello World!\\n\");" + "\n";
          String expected = expectedProgram(statements);
          emitProgram(sourceCode);
          String actual = Files.readString(outputFile.toPath());
          Assertions.assertEquals(expected, actual);
     }

     @Test
     void PRINTStatementTest2() throws IOException {
          String sourceCode = "PRINT 99\n";
          String statements = "    printf(\"%.2f\\n\", (float)(99));" + "\n";
          String expected = expectedProgram(statements);
          emitProgram(sourceCode);
          String actual = Files.readString(outputFile.toPath());
          Assertions.assertEquals(expected, actual);
     }

     @Test
     void IFStatementTest1() throws IOException {
          String sourceCode = "IF 1 == 1 THEN" + "\n" +
                  "ENDIF" + "\n";
          String statements = "    if (1==1) {" + "}" + "\n";
          String expected = expectedProgram(statements);
          emitProgram(sourceCode);
          String actual = Files.readString(outputFile.toPath());
          Assertions.assertEquals(expected, actual);
     }

     @Test
     void IFStatementTest2() throws IOException {
          String sourceCode = "IF 1 == 1 THEN" + "\n" +
                  "    PRINT \"IF statement TRUE\"" + "\n" +
                  "    PRINT \"1 == 1\"" + "\n" +
                  "ENDIF" + "\n";
          String statements = "    if (1==1) {" + "\n" +
                  "        printf(\"IF statement TRUE\\n\");" + "\n" +
                  "        printf(\"1 == 1\\n\");" + "\n" +
                  "    }" + "\n";
          String expected = expectedProgram(statements);
          emitProgram(sourceCode);
          String actual = Files.readString(outputFile.toPath());
          Assertions.assertEquals(expected, actual);
     }

     @Test
     void WHILEStatementTest1() throws IOException {
          String sourceCode = "WHILE 1 == 1 REPEAT" + "\n" + "ENDWHILE" + "\n";
          String statements = "    while (1==1) {" + "}" + "\n";
          String expected = expectedProgram(statements);
          emitProgram(sourceCode);
          String actual = Files.readString(outputFile.toPath());
          Assertions.assertEquals(expected, actual);
     }

     @Test
     void WHILEStatementTest2() throws IOException {
          String sourceCode = "WHILE 1 == 1 REPEAT" + "\n" +
                  "    PRINT \"WHILE statement TRUE\"" + "\n" +
                  "    PRINT \"1 == 1\"" + "\n" +
                  "ENDWHILE" + "\n";
          String statements = "    while (1==1) {" + "\n" +
                  "        printf(\"WHILE statement TRUE\\n\");" + "\n" +
                  "        printf(\"1 == 1\\n\");" + "\n" +
                  "    }" + "\n";
          String expected = expectedProgram(statements);
          emitProgram(sourceCode);
          String actual = Files.readString(outputFile.toPath());
          Assertions.assertEquals(expected, actual);
     }

     @Test
     void LABELStatementTest() throws IOException {
          String sourceCode = "LABEL goBack" + "\n";
          String statements = "    goBack:" + "\n";
          String expected = expectedProgram(statements);
          emitProgram(sourceCode);
          String actual = Files.readString(outputFile.toPath());
          Assertions.assertEquals(expected, actual);
     }

     @Test
     void GOTOStatementTest() throws IOException {
          String sourceCode = "LABEL goBack" + "\n" +
                  "GOTO goBack" + "\n";
          String statements = "    goBack:" + "\n" +
                  "    goto goBack;" + "\n";
          String expected = expectedProgram(statements);
          emitProgram(sourceCode);
          String actual = Files.readString(outputFile.toPath());
          Assertions.assertEquals(expected, actual);
     }

     @Test
     void LETStatementTest() throws IOException {
          String sourceCode = "LET a = 0" + "\n";
          String statements = "    float a;" + "\n" +
                  "    a = 0;" + "\n";
          String expected = expectedProgram(statements);
          emitProgram(sourceCode);
          String actual = Files.readString(outputFile.toPath());
          Assertions.assertEquals(expected, actual);
     }

     @Test
     void LETThenPRINTTest() throws IOException {
          String sourceCode = "LET a = 0" + "\n" +
                  "PRINT a" + "\n";
          String statements = "    float a;" + "\n" +
                  "    a = 0;" + "\n" +
                  "    printf(\"%" + ".2f\\n\", (float)(a));" + "\n";
          String expected = expectedProgram(statements);
          emitProgram(sourceCode);
          String actual = Files.readString(outputFile.toPath());
          Assertions.assertEquals(expected, actual);
     }

     @Test
     void INPUTStatementTest() throws IOException {
          String sourceCode = "INPUT nums" + "\n";
          String statements = "    float nums;" + "\n" +
                  "    if (0 == scanf(\"%f\", &nums)) {" + "\n" +
                  "        nums = 0;" + "\n" +
                  "        scanf(\"%*s\");" + "\n" +
                  "    }" + "\n";
          String expected = expectedProgram(statements);
          emitProgram(sourceCode);
          String actual = Files.readString(outputFile.toPath());
          Assertions.assertEquals(expected, actual);
     }
}
