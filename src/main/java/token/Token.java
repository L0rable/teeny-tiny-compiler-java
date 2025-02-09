package token;

public class Token {
    private final String tokenText;
    private final TokenType tokenKind;
    
    public Token() {
        this.tokenText = "";
        this.tokenKind = TokenType.EOF;
    }
    
    public Token(String tokenText, TokenType tokenKind) {
        this.tokenText = tokenText;
        this.tokenKind = tokenKind;
    }
    
    public String getTokenText() {
        return this.tokenText;
    }
    
    public TokenType getTokenKind() {
        return this.tokenKind;
    }

    public static TokenType checkIfKeyword(String tokenString) {
        for (TokenType kind : TokenType.values()) {
            boolean checkIfKeyword = kind.value >= 100 && kind.value < 200 && kind.toString().equals(tokenString);
            if (checkIfKeyword) {
                return kind;
            }
        }
        return null;
    }

    public static String convertKeyword(TokenType type) {
        return switch (type) {
            case AND -> "&&";
            case OR -> "||";
            case NOT -> "!";
            default -> null;
        };
    }
}
