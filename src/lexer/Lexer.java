package lexer;

import token.TokenType;
import token.Token;

public class Lexer {
    private final String source;
    private Character curChar;
    private int curPos;
    
    public Lexer(String source) {
        this.source = source;
        this.curChar = '\0';
        this.curPos = -1;
        
        this.nextChar();
    }
    
    public char getCurrentChar() {
        return this.curChar;
    }
    
    public char peek() {
        if (this.curPos + 1 >= this.source.length()) {
            return '\0';
        }
        return this.source.charAt(curPos+1);
    }
    
    public void nextChar() {
        this.curPos += 1;
        if (this.curPos >= this.source.length()) {
            this.curChar = '\0';
        }
        else {
            this.curChar = this.source.charAt(curPos);
        }
    }
    
    public void skipWhiteSpace() {
        while (this.curChar == ' ' || this.curChar == '\t' || this.curChar == '\r') {
            this.nextChar();
        }
    }
    
    public void skipComment() {
        if (this.curChar == '#') {
            while (this.curChar != '\n') {
                this.nextChar();
            }
        }
    }
    
    public void abort(String message) {
        System.err.println("Lexer error. " + message);
        System.exit(0);
    }
    
    // TODO: change so each If statement does a return of the token
    // instead of just setting it
    public Token getToken() {
        Token token;
        
        this.skipWhiteSpace();
        this.skipComment();
        
        if (this.curChar == '=') {
            if (this.peek() == '=') {
                this.nextChar();
                token = new Token("==", TokenType.EQEQ);
            }
            else
                token = new Token("=", TokenType.EQ);
        }
        else if (this.curChar == '+') {
            token = new Token("+", TokenType.PLUS);
        }
        else if (this.curChar == '-') {
            token = new Token("-", TokenType.MINUS);
        }
        else if (this.curChar == '*') {
            token = new Token("*", TokenType.ASTERISK);
        }
        else if (this.curChar == '/') {
            token = new Token("/", TokenType.SLASH);
        }
        else if (this.curChar == '!') {
            if (this.peek() == '=') {
                this.nextChar();
                token = new Token("!=", TokenType.NOTEQ);
            } else {
                token = new Token();
                this.abort("Unknown Token: !" + this.peek());
            }
        }
        else if (this.curChar == '<') {
            if (this.peek() == '=') {
                this.nextChar();
                token = new Token("<=", TokenType.LTEQ);
            }
            else
                token = new Token("<", TokenType.LT);
        }
        else if (this.curChar == '>') {
            if (this.peek() == '=') {
                this.nextChar();
                token = new Token(">=", TokenType.GTEQ);
            }
            else
                token = new Token(">", TokenType.GT);
        }
        else if (this.curChar == '\"') {
            this.nextChar();
            int startPos = this.curPos;
            
            while (this.curChar != '\"') {
                if (this.curChar == '\r' || this.curChar == '\n' || 
                    this.curChar == '\t' || this.curChar == '\\' || this.curChar == '%') {
                    this.abort("Illegal character in string");
                }
                this.nextChar();
            }
            
            String tokenText = this.source.substring(startPos, this.curPos);
            token = new Token(tokenText, TokenType.STRING);
            
        }
        else if (Character.isDigit(this.curChar)) {
            int startPos = this.curPos;
            
            while (Character.isDigit(this.peek()))
                this.nextChar();
            
            if (this.peek() == '.') {
                this.nextChar();
                if (!Character.isDigit(this.peek()))
                    this.abort("Illegal character in number.");
                while (Character.isDigit(this.peek()))
                    this.nextChar();
            }

            String tokenText = this.source.substring(startPos, this.curPos + 1);
            token = new Token(tokenText, TokenType.NUMBER);
        }
        else if (Character.isAlphabetic(this.curChar)) {
            // Handle Identifiers and Keywords      
            int startPos = this.curPos;
            
            // This might not check whether "alpha numeric" characters (A–Z, a–z and 0–9)
            while (Character.isAlphabetic(this.peek()) || Character.isDigit(this.peek()))
                this.nextChar();
            
            String tokenText = this.source.substring(startPos, this.curPos + 1);
            TokenType keyword = Token.checkIfKeyword(tokenText);
            
            if (keyword == null)
                token = new Token(tokenText, TokenType.IDENTIFIER);
            else
                token = new Token(tokenText, keyword);
            
        }
        else if (this.curChar == '\n') {
            token = new Token("\\n", TokenType.NEWLINE);
        }
        else if (this.curChar == '\0') {
            token = new Token("", TokenType.EOF);
        }
        else {
            token = new Token();
            this.abort("Unknown Token: " + this.curChar);
        }
        
        this.nextChar();
        return token;
    }
}
