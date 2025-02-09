import lexer.Lexer;
import org.junit.jupiter.api.*;
import token.Token;
import token.TokenType;

class LexerTest {
    Lexer lexer;
    String sourceCode;

    @Test
    void WhitespaceInSourceCode_IgnoreWhitespace_IdentifyTokens() {
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
    void CommentsInSourceCode_IgnoreComments_IdentifyTokens() {
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
    class SingularSourceCodeInput_LexerIdentifyToken_CompareLexerOutput {
        void checkTokensEqual(String sourceCode, Token expectedToken) {
            lexer = new Lexer(sourceCode);
            Token actualToken = lexer.getToken();
            Assertions.assertEquals(expectedToken.getTokenText(), actualToken.getTokenText());
            Assertions.assertEquals(expectedToken.getTokenKind(), actualToken.getTokenKind());
        }

        @Test
        void IdentifyTypesToken_EOF() {
            sourceCode = "\0";
            String expected = "";
            Token tokenEOF = new Token(expected, TokenType.EOF);
            checkTokensEqual(sourceCode, tokenEOF);
        }

        @Test
        void IdentifyTypesToken_NEWLINE() {
            sourceCode = "\n";
            String expected = "\\n";
            Token tokenNEWLINE = new Token(expected, TokenType.NEWLINE);
            checkTokensEqual(sourceCode, tokenNEWLINE);
        }

        @Test
        void IdentifyTypesToken_NUMBER_1() {
            sourceCode = "99";
            Token tokenNUMBER = new Token(sourceCode, TokenType.NUMBER);
            checkTokensEqual(sourceCode, tokenNUMBER);
        }

        @Test
        void IdentifyTypesToken_NUMBER_2() {
            sourceCode = "99.99";
            Token tokenNUMBER = new Token(sourceCode, TokenType.NUMBER);
            checkTokensEqual(sourceCode, tokenNUMBER);
        }

        @Test
        void IdentifyTypesToken_IDENTIFIER() {
            sourceCode = "varName";
            Token tokenIDENTIFIER = new Token(sourceCode, TokenType.IDENTIFIER);
            checkTokensEqual(sourceCode, tokenIDENTIFIER);
        }

        @Test
        void IdentifyTypesToken_STRING() {
            sourceCode = "\"Hello World!\"";
            String expected = "Hello World!";
            Token tokenSTRING = new Token(expected, TokenType.STRING);
            checkTokensEqual(sourceCode, tokenSTRING);
        }

        @Test
        void IdentifyLogicalOperatorToken_AND() {
            sourceCode = "AND";
            String expectedOutputCode = "&&";
            Token tokenAND = new Token(expectedOutputCode, TokenType.AND);
            checkTokensEqual(sourceCode, tokenAND);
        }

        @Test
        void IdentifyLogicalOperatorToken_OR() {
            sourceCode = "OR";
            String expectedOutputCode = "||";
            Token tokenOR = new Token(expectedOutputCode, TokenType.OR);
            checkTokensEqual(sourceCode, tokenOR);
        }

        @Test
        void IdentifyLogicalOperatorToken_NOT() {
            sourceCode = "NOT";
            String expectedOutputCode = "!";
            Token tokenNOT = new Token(expectedOutputCode, TokenType.NOT);
            checkTokensEqual(sourceCode, tokenNOT);
        }

        @Test
        void IdentifyRelationalOperatorToken_EQ() {
            sourceCode = "=";
            Token tokenEQ = new Token(sourceCode, TokenType.EQ);
            checkTokensEqual(sourceCode, tokenEQ);
        }

        @Test
        void IdentifyRelationalOperatorToken_EQEQ() {
            sourceCode = "==";
            Token tokenEQEQ = new Token(sourceCode, TokenType.EQEQ);
            checkTokensEqual(sourceCode, tokenEQEQ);
        }

        @Test
        void IdentifyArithmeticOperatorToken_PLUS() {
            sourceCode = "+";
            Token tokenPLUS = new Token(sourceCode, TokenType.PLUS);
            checkTokensEqual(sourceCode, tokenPLUS);
        }

        @Test
        void IdentifyArithmeticOperatorToken_MINUS() {
            sourceCode = "-";
            Token tokenMINUS = new Token(sourceCode, TokenType.MINUS);
            checkTokensEqual(sourceCode, tokenMINUS);
        }

        @Test
        void IdentifyArithmeticOperatorToken_ASTERISK() {
            sourceCode = "*";
            Token tokenASTERISK = new Token(sourceCode, TokenType.ASTERISK);
            checkTokensEqual(sourceCode, tokenASTERISK);
        }

        @Test
        void IdentifyArithmeticOperatorToken_SLASH() {
            sourceCode = "/";
            Token tokenSLASH = new Token(sourceCode, TokenType.SLASH);
            checkTokensEqual(sourceCode, tokenSLASH);
        }

        @Test
        void IdentifyArithmeticOperatorToken_NOTEQ() {
            sourceCode = "!=";
            Token tokenNOTEQ = new Token(sourceCode, TokenType.NOTEQ);
            checkTokensEqual(sourceCode, tokenNOTEQ);
        }

        @Test
        void IdentifyArithmeticOperatorToken_LT() {
            sourceCode = "<";
            Token tokenLT = new Token(sourceCode, TokenType.LT);
            checkTokensEqual(sourceCode, tokenLT);
        }

        @Test
        void IdentifyArithmeticOperatorToken_LTEQ() {
            sourceCode = "<=";
            Token tokenLTEQ = new Token(sourceCode, TokenType.LTEQ);
            checkTokensEqual(sourceCode, tokenLTEQ);
        }

        @Test
        void IdentifyArithmeticOperatorToken_GT() {
            sourceCode = ">";
            Token tokenGT = new Token(sourceCode, TokenType.GT);
            checkTokensEqual(sourceCode, tokenGT);
        }

        @Test
        void IdentifyArithmeticOperatorToken_GTEQ() {
            sourceCode = ">=";
            Token tokenGTEQ = new Token(sourceCode, TokenType.GTEQ);
            checkTokensEqual(sourceCode, tokenGTEQ);
        }

        @Test
        void IdentifyArithmeticOperatorToken_PARENTHESESLEFT() {
            sourceCode = "(";
            Token tokenPARENTHESESLEFT = new Token(sourceCode, TokenType.PARENTHESESLEFT);
            checkTokensEqual(sourceCode, tokenPARENTHESESLEFT);
        }

        @Test
        void IdentifyArithmeticOperatorToken_PARENTHESESRIGHT() {
            sourceCode = ")";
            Token tokenPARENTHESESRIGHT = new Token(sourceCode, TokenType.PARENTHESESRIGHT);
            checkTokensEqual(sourceCode, tokenPARENTHESESRIGHT);
        }
    }
}
