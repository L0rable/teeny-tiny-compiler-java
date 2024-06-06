import lexer.Lexer;
import org.junit.jupiter.api.*;
import token.Token;
import token.TokenType;

class LexerTest {
    Lexer lexer;
    String sourceCode;

    @Test
    void skipWhiteSpaceTest() {
        this.sourceCode = "      \"Hello\" \"World\"    \"!\"";
        lexer = new Lexer(sourceCode);
        Token expectedToken = new Token("Hello", TokenType.STRING);
        Token actualToken = lexer.getToken();
        Assertions.assertEquals(expectedToken.getTokenText(), actualToken.getTokenText());
        Assertions.assertEquals(expectedToken.getTokenKind(), actualToken.getTokenKind());

        expectedToken = new Token("World", TokenType.STRING);
        actualToken = lexer.getToken();
        Assertions.assertEquals(expectedToken.getTokenText(), actualToken.getTokenText());
        Assertions.assertEquals(expectedToken.getTokenKind(), actualToken.getTokenKind());

        expectedToken = new Token("!", TokenType.STRING);
        actualToken = lexer.getToken();
        Assertions.assertEquals(expectedToken.getTokenText(), actualToken.getTokenText());
        Assertions.assertEquals(expectedToken.getTokenKind(), actualToken.getTokenKind());
    }

    @Test
    void skipCommentTest() {
        this.sourceCode = "# Hello World" + ", still a comment\n" +
                "\"Hello World!\"" + "# Comment";
        lexer = new Lexer(sourceCode);
        Token expectedToken = new Token("\\n", TokenType.NEWLINE);
        Token actualToken = lexer.getToken();
        Assertions.assertEquals(expectedToken.getTokenText(), actualToken.getTokenText());
        Assertions.assertEquals(expectedToken.getTokenKind(), actualToken.getTokenKind());

        expectedToken = new Token("Hello World!", TokenType.STRING);
        actualToken = lexer.getToken();
        Assertions.assertEquals(expectedToken.getTokenText(), actualToken.getTokenText());
        Assertions.assertEquals(expectedToken.getTokenKind(), actualToken.getTokenKind());
    }

    @Nested
    class getTokenTests {
        void checkTokensEqual(String actualSourceCode, Token expectedToken) {
            lexer = new Lexer(actualSourceCode);
            Token actualToken = lexer.getToken();
            Assertions.assertEquals(expectedToken.getTokenText(), actualToken.getTokenText());
            Assertions.assertEquals(expectedToken.getTokenKind(), actualToken.getTokenKind());
        }

        @Test
        void getTokenEOFTest() {
            sourceCode = "\0";
            String expected = "";
            Token tokenEOF = new Token(expected, TokenType.EOF);
            checkTokensEqual(sourceCode, tokenEOF);
        }

        @Test
        void getTokenNEWLINETest() {
            sourceCode = "\n";
            String expected = "\\n";
            Token tokenNEWLINE = new Token(expected, TokenType.NEWLINE);
            checkTokensEqual(sourceCode, tokenNEWLINE);
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
        void getTokenSTRINGTest() {
            sourceCode = "\"Hello World!\"";
            String expected = "Hello World!";
            Token tokenSTRING = new Token(expected, TokenType.STRING);
            checkTokensEqual(sourceCode, tokenSTRING);
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
        void getTokenPARENTHESESLEFTTest() {
            sourceCode = "(";
            Token tokenPARENTHESESLEFT = new Token(sourceCode, TokenType.PARENTHESESLEFT);
            checkTokensEqual(sourceCode, tokenPARENTHESESLEFT);
        }

        @Test
        void getTokenPARENTHESESRIGHTTest() {
            sourceCode = ")";
            Token tokenPARENTHESESRIGHT = new Token(sourceCode, TokenType.PARENTHESESRIGHT);
            checkTokensEqual(sourceCode, tokenPARENTHESESRIGHT);
        }
    }
}
