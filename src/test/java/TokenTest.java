import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import token.Token;
import token.TokenType;

class TokenTest {

    @Nested
    class checkKeywordTests {
        @Test
        void checkIfLABEL() {
            String expected = "LABEL";
            TokenType expectedToken = Token.checkIfKeyword(expected);
            Token actualToken = new Token(expected, TokenType.LABEL);
            Assertions.assertEquals(expectedToken, actualToken.getTokenKind());
        }

        @Test
        void checkIfGOTO() {
            String expected = "GOTO";
            TokenType expectedToken = Token.checkIfKeyword(expected);
            Token actualToken = new Token(expected, TokenType.GOTO);
            Assertions.assertEquals(expectedToken, actualToken.getTokenKind());
        }

        @Test
        void checkIfPRINT() {
            String expected = "PRINT";
            TokenType expectedToken = Token.checkIfKeyword(expected);
            Token actualToken = new Token(expected, TokenType.PRINT);
            Assertions.assertEquals(expectedToken, actualToken.getTokenKind());
        }

        @Test
        void checkIfINPUT() {
            String expected = "INPUT";
            TokenType expectedToken = Token.checkIfKeyword(expected);
            Token actualToken = new Token(expected, TokenType.INPUT);
            Assertions.assertEquals(expectedToken, actualToken.getTokenKind());
        }

        @Test
        void checkIfLET() {
            String expected = "LET";
            TokenType expectedToken = Token.checkIfKeyword(expected);
            Token actualToken = new Token(expected, TokenType.LET);
            Assertions.assertEquals(expectedToken, actualToken.getTokenKind());
        }

        @Test
        void checkIfIF() {
            String expected = "IF";
            TokenType expectedToken = Token.checkIfKeyword(expected);
            Token actualToken = new Token(expected, TokenType.IF);
            Assertions.assertEquals(expectedToken, actualToken.getTokenKind());
        }

        @Test
        void checkIfTHEN() {
            String expected = "THEN";
            TokenType expectedToken = Token.checkIfKeyword(expected);
            Token actualToken = new Token(expected, TokenType.THEN);
            Assertions.assertEquals(expectedToken, actualToken.getTokenKind());
        }

        @Test
        void checkIfENDIF() {
            String expected = "ENDIF";
            TokenType expectedToken = Token.checkIfKeyword(expected);
            Token actualToken = new Token(expected, TokenType.ENDIF);
            Assertions.assertEquals(expectedToken, actualToken.getTokenKind());
        }

        @Test
        void checkIfWHILE() {
            String expected = "WHILE";
            TokenType expectedToken = Token.checkIfKeyword(expected);
            Token actualToken = new Token(expected, TokenType.WHILE);
            Assertions.assertEquals(expectedToken, actualToken.getTokenKind());
        }

        @Test
        void checkIfREPEAT() {
            String expected = "REPEAT";
            TokenType expectedToken = Token.checkIfKeyword(expected);
            Token actualToken = new Token(expected, TokenType.REPEAT);
            Assertions.assertEquals(expectedToken, actualToken.getTokenKind());
        }

        @Test
        void checkIfENDWHILE() {
            String expected = "ENDWHILE";
            TokenType expectedToken = Token.checkIfKeyword(expected);
            Token actualToken = new Token(expected, TokenType.ENDWHILE);
            Assertions.assertEquals(expectedToken, actualToken.getTokenKind());
        }

        @Test
        void checkIfAND() {
            String expected = "AND";
            TokenType expectedToken = Token.checkIfKeyword(expected);
            Token actualToken = new Token(expected, TokenType.AND);
            Assertions.assertEquals(expectedToken, actualToken.getTokenKind());
        }
    }
}
