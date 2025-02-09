import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import token.Token;
import token.TokenType;

class TokenTest {

    @Nested
    class SingularKeyword_IdentifyKeywordToken_CompareTokenOutput {
        void checkKeywordTokensEqual(String sourceCode, TokenType expectedToken) {
            TokenType actualToken = Token.checkIfKeyword(sourceCode);
            Assertions.assertEquals(expectedToken, actualToken);
        }

        @Test
        void IdentifyKeywordToken_LABEL() {
            checkKeywordTokensEqual("LABEL", TokenType.LABEL);
        }

        @Test
        void IdentifyKeywordToken_GOTO() {
            checkKeywordTokensEqual("GOTO", TokenType.GOTO);
        }

        @Test
        void IdentifyKeywordToken_PRINT() {
            checkKeywordTokensEqual("PRINT", TokenType.PRINT);
        }

        @Test
        void IdentifyKeywordToken_INPUT() {
            checkKeywordTokensEqual("INPUT", TokenType.INPUT);
        }

        @Test
        void IdentifyKeywordToken_LET() {
            checkKeywordTokensEqual("LET", TokenType.LET);
        }

        @Test
        void IdentifyKeywordToken_IF() {
            checkKeywordTokensEqual("IF", TokenType.IF);
        }

        @Test
        void IdentifyKeywordToken_THEN() {
            checkKeywordTokensEqual("THEN", TokenType.THEN);
        }

        @Test
        void IdentifyKeywordToken_ENDIF() {
            checkKeywordTokensEqual("ENDIF", TokenType.ENDIF);
        }

        @Test
        void IdentifyKeywordToken_WHILE() {
            checkKeywordTokensEqual("WHILE", TokenType.WHILE);
        }

        @Test
        void IdentifyKeywordToken_REPEAT() {
            checkKeywordTokensEqual("REPEAT", TokenType.REPEAT);
        }

        @Test
        void IdentifyKeywordToken_ENDWHILE() {
            checkKeywordTokensEqual("ENDWHILE", TokenType.ENDWHILE);
        }

        @Test
        void IdentifyKeywordToken_AND() {
            checkKeywordTokensEqual("AND", TokenType.AND);
        }

        @Test
        void IdentifyKeywordToken_OR() {
            checkKeywordTokensEqual("OR", TokenType.OR);
        }

        @Test
        void IdentifyKeywordToken_NOT() {
            checkKeywordTokensEqual("NOT", TokenType.NOT);
        }
    }
}
