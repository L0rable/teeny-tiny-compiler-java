import org.junit.jupiter.api.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

class MainTest {
    final static String outputFileLocation = "output.c";
    final static String sourceProgramsDir = "src/main/resources/srcPrograms/";
    final static String expectedOutputDir = "src/main/resources/expectedOutput/";
    File outputFile;

    @BeforeEach
    void init() {
        outputFile = new File(outputFileLocation);
    }

    @AfterAll
    static void cleanup() {
        File outputFile = new File(outputFileLocation);
        outputFile.deleteOnExit();
    }

    void compareFileContents(File expectedOutputFile, File actualOutputFile) throws IOException {
        try (BufferedReader expected = Files.newBufferedReader(expectedOutputFile.toPath());
             BufferedReader actual = Files.newBufferedReader(actualOutputFile.toPath())) {
            long lineNumber = 1;
            String line1 = "", line2 = "";
            while ((line1 = expected.readLine()) != null) {
                line2 = actual.readLine();
                Assertions.assertEquals(line1, line2);
                lineNumber++;
            }
        }
    }

    @Test
    void noSourceFileTest() throws IOException {
        String expectedOutputProgramLocation = expectedOutputDir + "helloWorld.c";
        File expectedOutputFile = new File(expectedOutputProgramLocation);
        String[] args = new String[0];
        Main.main(args);
        compareFileContents(expectedOutputFile, this.outputFile);
    }

    @Test
    void absoluteFilePathTest() throws IOException {
        String expectedOutputProgramLocation = expectedOutputDir + "helloWorld.c";
        File expectedOutputFile = new File(expectedOutputProgramLocation);
        String[] args = new String[1];
        args[0] = sourceProgramsDir + "helloWorld.teenytiny";
        Main.main(args);
        compareFileContents(expectedOutputFile, this.outputFile);
    }

    @Test
    void testProgramArgsTest1() throws IOException {
        String expectedOutputProgramLocation = expectedOutputDir + "helloWorld.c";
        File expectedOutputFile = new File(expectedOutputProgramLocation);
        String[] args = new String[2];
        args[0] = sourceProgramsDir + "helloWorld.teenytiny";
        args[1] = "";
        Main.main(args);
        compareFileContents(expectedOutputFile, this.outputFile);
    }

    @Test
    void testProgramArgsTest2() throws IOException {
        String expectedOutputProgramLocation = expectedOutputDir + "helloWorld.c";
        File expectedOutputFile = new File(expectedOutputProgramLocation);
        String[] args = new String[2];
        args[0] = sourceProgramsDir + "helloWorld.teenytiny";
        args[1] = "noTestPrograms";
        Main.main(args);
        compareFileContents(expectedOutputFile, this.outputFile);
    }

    @Nested
    class endToEndTests {
        @Test
        void srcProgramTest1() throws IOException {
            String expectedOutputProgramLocation = expectedOutputDir + "helloWorld.c";
            File expectedOutputFile = new File(expectedOutputProgramLocation);
            String[] args = new String[2];
            args[0] = "helloWorld.teenytiny";
            args[1] = "testPrograms";
            Main.main(args);
            compareFileContents(expectedOutputFile, outputFile);
        }

        @Test
        void srcProgramTest2() throws IOException {
            String expectedOutputProgramLocation = expectedOutputDir + "average.c";
            File expectedOutputFile = new File(expectedOutputProgramLocation);
            String[] args = new String[2];
            args[0] = "average.teenytiny";
            args[1] = "testPrograms";
            Main.main(args);
            compareFileContents(expectedOutputFile, outputFile);
        }

        @Test
        void srcProgramTest3() throws IOException {
            String expectedOutputProgramLocation = expectedOutputDir + "fibonacci.c";
            File expectedOutputFile = new File(expectedOutputProgramLocation);
            String[] args = new String[2];
            args[0] = "fibonacci.teenytiny";
            args[1] = "testPrograms";
            Main.main(args);
            compareFileContents(expectedOutputFile, outputFile);
        }
    }
}
