import emitter.Emitter;
import org.junit.jupiter.api.*;

import java.io.*;
import java.nio.file.Files;

class EmitterTest {
    Emitter emitter;
    final static String outputFileLocation = "outputTest.c";
    File outputFile;

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

    @Test
    void writeNothingToFileTest() throws IOException {
        this.emitter.writeFile();
        String expected = "";
        String actual = Files.readString(this.outputFile.toPath());
        Assertions.assertEquals(expected, actual);
    }

    @Nested
    class EmitterMethodTests {
        @Test
        void singleEmitTest() throws IOException {
            emitter.emit("// Comment");
            emitter.writeFile();
            String expected = "// Comment";
            String actual = Files.readString(outputFile.toPath());
            Assertions.assertEquals(expected, actual);
        }

        @Test
        void multipleEmitTest() throws IOException {
            emitter.emit("// Comment");
            emitter.emit(", still a comment");
            emitter.writeFile();
            String expected = "// Comment, still a comment";
            String actual = Files.readString(outputFile.toPath());
            Assertions.assertEquals(expected, actual);
        }

        @Test
        void singleEmitLineTest() throws IOException {
            emitter.emitLine("// Comment");
            emitter.writeFile();
            String expected = "// Comment\n";
            String actual = Files.readString(outputFile.toPath());
            Assertions.assertEquals(expected, actual);
        }

        @Test
        void multipleEmitLineTest() throws IOException {
            emitter.emitLine("// Comment");
            emitter.emitLine("float a = 0;");
            emitter.writeFile();
            String expected = "// Comment\n" + "float a = 0;\n";
            String actual = Files.readString(outputFile.toPath());
            Assertions.assertEquals(expected, actual);
        }

        @Test
        void singleEmitIndentTest() throws IOException {
            emitter.emitIndent(1);
            emitter.emit("// Comment");
            emitter.writeFile();
            String expected = "    // Comment";
            String actual = Files.readString(outputFile.toPath());
            Assertions.assertEquals(expected, actual);
        }

        @Test
        void multipleEmitIndentTest() throws IOException {
            emitter.emitIndent(2);
            emitter.emit("// Comment");
            emitter.writeFile();
            String expected = "        // Comment";
            String actual = Files.readString(outputFile.toPath());
            Assertions.assertEquals(expected, actual);
        }

        @Test
        void singleHeaderLineTest() throws IOException {
            emitter.headerLine("#include <stdio.h>");
            emitter.writeFile();
            String expected = "#include <stdio.h>\n";
            String actual = Files.readString(outputFile.toPath());
            Assertions.assertEquals(expected, actual);
        }

        @Test
        void multipleHeaderLineTest() throws IOException {
            emitter.headerLine("#include <stdio.h>");
            emitter.headerLine("");
            emitter.headerLine("int main() {");
            emitter.writeFile();
            String expected = "#include <stdio.h>\n" + "\n" + "int main() {\n";
            String actual = Files.readString(outputFile.toPath());
            Assertions.assertEquals(expected, actual);
        }

        @Test
        void singleHeaderIndentTest() throws IOException {
            emitter.headerIndent(1);
            emitter.headerLine("#include <stdio.h>");
            emitter.writeFile();
            String expected = "    #include <stdio.h>\n";
            String actual = Files.readString(outputFile.toPath());
            Assertions.assertEquals(expected, actual);
        }

        @Test
        void multipleHeaderIndentTest() throws IOException {
            emitter.headerLine("#include <stdio.h>");
            emitter.headerIndent(2);
            emitter.headerLine("");
            emitter.headerIndent(1);
            emitter.headerLine("int main() {");
            emitter.writeFile();
            String expected = "#include <stdio.h>\n" + "        \n" + "    int main() {\n";
            String actual = Files.readString(outputFile.toPath());
            Assertions.assertEquals(expected, actual);
        }
    }

    @Nested
    class edgeCaseTests {
        @Test
        void noOverwritingTest() throws IOException {
            emitter.emitLine("// Hello World");
            emitter.writeFile();
            String expected = "// Hello World\n";
            String actual = Files.readString(outputFile.toPath());
            Assertions.assertEquals(expected, actual);

            emitter.emit("printf(\"Hello World!\\n\");");
            emitter.writeFile();
            expected = "// Hello World\n" + "printf(\"Hello World!\\n\");";
            actual = Files.readString(outputFile.toPath());
            Assertions.assertEquals(expected, actual);
        }

        @Test
        void orderOfEmitTest1() throws IOException {
            emitter.emitIndent(1);
            emitter.headerLine("#include <stdio.h>");
            emitter.writeFile();
            String expected = "#include <stdio.h>\n" + "    ";
            String actual = Files.readString(outputFile.toPath());
            Assertions.assertEquals(expected, actual);
        }

        @Test
        void orderOfEmitTest2() throws IOException {
            emitter.emit("float a = 0");
            emitter.headerIndent(1);
            emitter.writeFile();
            String expected = "float a = 0";
            String actual = Files.readString(outputFile.toPath());
            Assertions.assertEquals(expected, actual);
        }

        @Test
        void orderOfEmitTest3() throws IOException {
            emitter.headerIndent(1);
            emitter.emitIndent(2);
            emitter.headerLine("#include <stdio.h>");
            emitter.emit("printf(\"Hello World!\\n\");");
            emitter.emitIndent(1);
            emitter.writeFile();
            String expected = "    #include <stdio.h>\n" + "        printf(\"Hello World!\\n\");    ";
            String actual = Files.readString(outputFile.toPath());
            Assertions.assertEquals(expected, actual);
        }
    }
}
