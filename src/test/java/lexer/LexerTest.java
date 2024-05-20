package lexer;

import org.junit.jupiter.api.*;
import token.Token;
import token.TokenType;

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

    @Nested
    class getTokenTests {
        void checkTokensEqual(String actualSourceCode, Token expectedToken) {
            lexer = new Lexer(actualSourceCode);
            Token actualToken = lexer.getToken();
            assertEquals(expectedToken.getTokenText(), actualToken.getTokenText());
            assertEquals(expectedToken.getTokenKind(), actualToken.getTokenKind());
        }

        @Test
        void getTokenEQTest() {
            sourceCode = "=";
            Token tokenEQ = new Token(sourceCode, TokenType.EQ);
            checkTokensEqual(sourceCode, tokenEQ);
        }

        @Test
        void getTokenEQEQTest() {
            sourceCode = "==";
            Token tokenEQEQ = new Token(sourceCode, TokenType.EQEQ);
            checkTokensEqual(sourceCode, tokenEQEQ);
        }

        @Test
        void getTokenPLUSTest() {
            sourceCode = "+";
            Token tokenPLUS = new Token(sourceCode, TokenType.PLUS);
            checkTokensEqual(sourceCode, tokenPLUS);
        }

        @Test
        void getTokenMINUSTest() {
            sourceCode = "-";
            Token tokenMINUS = new Token(sourceCode, TokenType.MINUS);
            checkTokensEqual(sourceCode, tokenMINUS);
        }

        @Test
        void getTokenASTERISKTest() {
            sourceCode = "*";
            Token tokenASTERISK = new Token(sourceCode, TokenType.ASTERISK);
            checkTokensEqual(sourceCode, tokenASTERISK);
        }

        @Test
        void getTokenSLASHTest() {
            sourceCode = "/";
            Token tokenSLASH = new Token(sourceCode, TokenType.SLASH);
            checkTokensEqual(sourceCode, tokenSLASH);
        }

        @Test
        void getTokenNOTEQTest() {
            sourceCode = "!=";
            Token tokenNOTEQ = new Token(sourceCode, TokenType.NOTEQ);
            checkTokensEqual(sourceCode, tokenNOTEQ);
        }

        @Test
        void getTokenLTTest() {
            sourceCode = "<";
            Token tokenLT = new Token(sourceCode, TokenType.LT);
            checkTokensEqual(sourceCode, tokenLT);
        }

        @Test
        void getTokenLTEQTest() {
            sourceCode = "<=";
            Token tokenLTEQ = new Token(sourceCode, TokenType.LTEQ);
            checkTokensEqual(sourceCode, tokenLTEQ);
        }

        @Test
        void getTokenGTTest() {
            sourceCode = ">";
            Token tokenGT = new Token(sourceCode, TokenType.GT);
            checkTokensEqual(sourceCode, tokenGT);
        }

        @Test
        void getTokenGTEQTest() {
            sourceCode = ">=";
            Token tokenGTEQ = new Token(sourceCode, TokenType.GTEQ);
            checkTokensEqual(sourceCode, tokenGTEQ);
        }

        @Test
        void getTokenSTRINGTest() {
            sourceCode = "\"Hello World!\"";
            String expected = "Hello World!";
            Token tokenSTRING = new Token(expected, TokenType.STRING);
            checkTokensEqual(sourceCode, tokenSTRING);
        }

        @Test
        void getTokenNUMBERTest() {
            sourceCode = "99";
            Token tokenNUMBER = new Token(sourceCode, TokenType.NUMBER);
            checkTokensEqual(sourceCode, tokenNUMBER);
        }

        @Test
        void getTokenNUMBERTest1() {
            sourceCode = "99.99";
            Token tokenNUMBER = new Token(sourceCode, TokenType.NUMBER);
            checkTokensEqual(sourceCode, tokenNUMBER);
        }

        @Test
        void getTokenIDENTIFIERTest() {
            sourceCode = "varName";
            Token tokenIDENTIFIER = new Token(sourceCode, TokenType.IDENTIFIER);
            checkTokensEqual(sourceCode, tokenIDENTIFIER);
        }

        @Test
        void getTokenNEWLINETest() {
            sourceCode = "\n";
            String expected = "\\n";
            Token tokenNEWLINE = new Token(expected, TokenType.NEWLINE);
            checkTokensEqual(sourceCode, tokenNEWLINE);
        }

        @Test
        void getTokenEOFTest() {
            sourceCode = "\0";
            String expected = "";
            Token tokenEOF = new Token(expected, TokenType.EOF);
            checkTokensEqual(sourceCode, tokenEOF);
        }
    }
}
