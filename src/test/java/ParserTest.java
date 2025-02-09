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
          this.emitter = new Emitter(outputFileLocation);
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

     String expectedProgram(String expectedOutput) {
          return "#include <stdio.h>" + "\n" + "\n" + "int main() {" + "\n" +
                  expectedOutput +
                  "    " + "return 0;" + "\n" + "}" + "\n";
     }

     @Test
     void LABELStatementTest() throws IOException {
          String sourceCode = "LABEL goBack" + "\n";
          String expectedOutput = "    goBack:" + "\n";
          String expected = expectedProgram(expectedOutput);
          emitProgram(sourceCode);
          String actual = Files.readString(outputFile.toPath());
          Assertions.assertEquals(expected, actual);
     }

     @Test
     void GOTOStatementTest() throws IOException {
          String sourceCode = "LABEL goBack" + "\n" +
                  "GOTO goBack" + "\n";
          String expectedOutput = "    goBack:" + "\n" +
                  "    goto goBack;" + "\n";
          String expected = expectedProgram(expectedOutput);
          emitProgram(sourceCode);
          String actual = Files.readString(outputFile.toPath());
          Assertions.assertEquals(expected, actual);
     }

     @Test
     void PRINTStatementTest1() throws IOException {
          String sourceCode = "PRINT \"Hello World!\"\n";
          String expectedOutput = "    printf(\"Hello World!\\n\");" + "\n";
          String expected = expectedProgram(expectedOutput);
          emitProgram(sourceCode);
          String actual = Files.readString(outputFile.toPath());
          Assertions.assertEquals(expected, actual);
     }

     @Test
     void PRINTStatementTest2() throws IOException {
          String sourceCode = "PRINT 99\n";
          String expectedOutput = "    printf(\"%.2f\\n\", (float)(99));" + "\n";
          String expected = expectedProgram(expectedOutput);
          emitProgram(sourceCode);
          String actual = Files.readString(outputFile.toPath());
          Assertions.assertEquals(expected, actual);
     }

     @Test
     void INPUTStatementTest() throws IOException {
          String sourceCode = "INPUT nums" + "\n";
          String expectedOutput = "    float nums;" + "\n" +
                  "    if (0 == scanf(\"%f\", &nums)) {" + "\n" +
                  "        nums = 0;" + "\n" +
                  "        scanf(\"%*s\");" + "\n" +
                  "    }" + "\n";
          String expected = expectedProgram(expectedOutput);
          emitProgram(sourceCode);
          String actual = Files.readString(outputFile.toPath());
          Assertions.assertEquals(expected, actual);
     }

     @Test
     void LETStatementTest() throws IOException {
          String sourceCode = "LET a = 0" + "\n";
          String expectedOutput = "    float a;" + "\n" +
                  "    a = 0;" + "\n";
          String expected = expectedProgram(expectedOutput);
          emitProgram(sourceCode);
          String actual = Files.readString(outputFile.toPath());
          Assertions.assertEquals(expected, actual);
     }

     @Test
     void LETThenPRINTTest() throws IOException {
          String sourceCode = "LET a = 0" + "\n" +
                  "PRINT a" + "\n";
          String expectedOutput = "    float a;" + "\n" +
                  "    a = 0;" + "\n" +
                  "    printf(\"%" + ".2f\\n\", (float)(a));" + "\n";
          String expected = expectedProgram(expectedOutput);
          emitProgram(sourceCode);
          String actual = Files.readString(outputFile.toPath());
          Assertions.assertEquals(expected, actual);
     }

     @Test
     void IFStatementTest1() throws IOException {
          String sourceCode = "IF 1 == 1 THEN" + "\n" +
                  "ENDIF" + "\n";
          String expectedOutput = "    if (1==1) {" + "}" + "\n";
          String expected = expectedProgram(expectedOutput);
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
          String expectedOutput = "    if (1==1) {" + "\n" +
                  "        printf(\"IF statement TRUE\\n\");" + "\n" +
                  "        printf(\"1 == 1\\n\");" + "\n" +
                  "    }" + "\n";
          String expected = expectedProgram(expectedOutput);
          emitProgram(sourceCode);
          String actual = Files.readString(outputFile.toPath());
          Assertions.assertEquals(expected, actual);
     }

     @Test
     void WHILEStatementTest1() throws IOException {
          String sourceCode = "WHILE 1 == 1 REPEAT" + "\n" + "ENDWHILE" + "\n";
          String expectedOutput = "    while (1==1) {" + "}" + "\n";
          String expected = expectedProgram(expectedOutput);
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
          String expectedOutput = "    while (1==1) {" + "\n" +
                  "        printf(\"WHILE statement TRUE\\n\");" + "\n" +
                  "        printf(\"1 == 1\\n\");" + "\n" +
                  "    }" + "\n";
          String expected = expectedProgram(expectedOutput);
          emitProgram(sourceCode);
          String actual = Files.readString(outputFile.toPath());
          Assertions.assertEquals(expected, actual);
     }

     @Test
     void PARENTHESESStatmentTest1() throws IOException {
          String sourceCode = "LET a = (1 + 1)" + "\n";
          String expectedOutput = "    float a;" + "\n" +
                  "    a = (1+1);" + "\n";
          String expected = expectedProgram(expectedOutput);
          emitProgram(sourceCode);
          String actual = Files.readString(outputFile.toPath());
          Assertions.assertEquals(expected, actual);
     }

     @Test
     void PARENTHESESStatmentTest2() throws IOException {
          String sourceCode = "LET a = (1 + 1) + (2 * 6)" + "\n";
          String expectedOutput = "    float a;" + "\n" +
                  "    a = (1+1)+(2*6);" + "\n";
          String expected = expectedProgram(expectedOutput);
          emitProgram(sourceCode);
          String actual = Files.readString(outputFile.toPath());
          Assertions.assertEquals(expected, actual);
     }

     @Test
     void PARENTHESESStatmentTest3() throws IOException {
          String sourceCode = "LET a = ((1 + 1) + (2 * 6))" + "\n";
          String expectedOutput = "    float a;" + "\n" +
                  "    a = ((1+1)+(2*6));" + "\n";
          String expected = expectedProgram(expectedOutput);
          emitProgram(sourceCode);
          String actual = Files.readString(outputFile.toPath());
          Assertions.assertEquals(expected, actual);
     }

     @Test
     void testStatementUsingLogicalANDKeyword() throws IOException {
          String sourceCode = "IF 1 == 1 AND 2 == 2 THEN" + "\n" +
                  "ENDIF" + "\n";
          String expectedOutput = "    if (1==1&&2==2) {" + "}" + "\n";
          String expected = expectedProgram(expectedOutput);
          emitProgram(sourceCode);
          String actual = Files.readString(outputFile.toPath());
          Assertions.assertEquals(expected, actual);
     }

     @Test
     void testStatementUsingLogicalORKeyword() throws IOException {
          String sourceCode = "IF 1 == 1 OR 2 == 2 THEN" + "\n" +
                  "ENDIF" + "\n";
          String expectedOutput = "    if (1==1||2==2) {" + "}" + "\n";
          String expected = expectedProgram(expectedOutput);
          emitProgram(sourceCode);
          String actual = Files.readString(outputFile.toPath());
          Assertions.assertEquals(expected, actual);
     }

     @Test
     void testStatementUsingLogicalNOTKeyword() throws IOException {
          String sourceCode = "IF NOT 1 == 1 THEN" + "\n" +
                  "ENDIF" + "\n";
          String expectedOutput = "    if (!1==1) {" + "}" + "\n";
          String expected = expectedProgram(expectedOutput);
          emitProgram(sourceCode);
          String actual = Files.readString(outputFile.toPath());
          Assertions.assertEquals(expected, actual);
     }
}
