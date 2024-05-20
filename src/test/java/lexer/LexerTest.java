package lexer;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LexerTest {
    Lexer lexer;
    String sourceCode;

    @BeforeEach
    void init() {
        this.sourceCode = "PRINT \"Hello World!\"";
        this.lexer = new Lexer(this.sourceCode);
    }

    @Test
    void peekTest() {
        char expectedCurChar = 'P';
        char actualCurChar = this.lexer.getCurrentChar();
        assertEquals(expectedCurChar, actualCurChar);

        expectedCurChar = 'R';
        actualCurChar = this.lexer.peek();
        assertEquals(expectedCurChar, actualCurChar);
    }

    @Test
    void nextCharTest() {
        char expectedCurChar = 'P';
        char actualCurChar = this.lexer.getCurrentChar();
        assertEquals(expectedCurChar, actualCurChar);

        this.lexer.nextChar();
        expectedCurChar = 'R';
        actualCurChar = this.lexer.getCurrentChar();
        assertEquals(expectedCurChar, actualCurChar);
    }

    @Test
    void skipWhiteSpaceTest() {
        for (int i = 0; i < 5; i++) {
            this.lexer.nextChar();
        }
        char expectedCurChar = ' ';
        char actualCurChar = this.lexer.getCurrentChar();
        assertEquals(expectedCurChar, actualCurChar);

        this.lexer.skipWhiteSpace();
        expectedCurChar = '\"';
        actualCurChar = this.lexer.getCurrentChar();
        assertEquals(expectedCurChar, actualCurChar);
    }

    @Test
    void skipCommentTest() {
        this.sourceCode = "# Hello World\n" + "PRINT \"Hello World!\"";
        this.lexer.skipComment();
        char expectedCurChar = 'P';
        char actualCurChar = this.lexer.getCurrentChar();
        assertEquals(expectedCurChar, actualCurChar);
    }
}
