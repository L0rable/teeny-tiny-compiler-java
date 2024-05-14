package parser;

import lexer.Lexer;
import emitter.Emitter;
import token.Token;
import token.TokenType;
import java.util.ArrayList;

public class Parser {
    private final Lexer lexer;
    private final Emitter emitter;
    
    private final ArrayList<String> symbols;
    private final ArrayList<String> labelsDeclared;
    private final ArrayList<String> labelsGotoed;
    
    private Token curToken;
    private Token peekToken;
    
    public Parser(Lexer lexer, Emitter emitter) {
        this.lexer = lexer;
        this.emitter = emitter;
        
        this.symbols = new ArrayList<String>();
        this.labelsDeclared = new ArrayList<String>();
        this.labelsGotoed = new ArrayList<String>();
        
        this.curToken = null; // may set to empty Token()
        this.peekToken = null;
        
        this.nextToken();
        this.nextToken();
    }
    
    public boolean checkToken(TokenType kind) {
        return kind == this.curToken.getTokenKind();
    }

    public boolean checkPeek(TokenType kind) {
        return kind == this.peekToken.getTokenKind();
    }

    public void match(TokenType kind) {
        if (!this.checkToken(kind))
            this.abort("Expected " + kind.toString() + ", got " + this.curToken.getTokenText());
        this.nextToken();
    }

    public void nextToken() {
        this.curToken = this.peekToken;
        this.peekToken = this.lexer.getToken();
    }
    
    public boolean isComparisonOperator() {
        return this.checkToken(TokenType.EQEQ) || this.checkToken(TokenType.NOTEQ)
                || this.checkToken(TokenType.LT) || this.checkToken(TokenType.LTEQ)
                || this.checkToken(TokenType.GT) || this.checkToken(TokenType.GTEQ);
    }
    
    // newline ::= '\n'+
    public void newline() {
        this.match(TokenType.NEWLINE);
        while (this.checkToken(TokenType.NEWLINE)) {
            this.nextToken();
        }
    }
    
    // primary ::= number | ident
    public void primary() {
        if (this.checkToken(TokenType.NUMBER)) {
            this.emitter.emit(this.curToken.getTokenText());
            this.nextToken();
        }
        else if (this.checkToken(TokenType.IDENTIFIER)) {
            // Ensure the variable already exists.
            if (!this.symbols.contains(this.curToken.getTokenText())) {
                this.abort("Referencing variable before assignment: " + this.curToken.getTokenText());
            }
            this.emitter.emit(this.curToken.getTokenText());
            this.nextToken();
        }
        else {
            this.abort("Unexpected token at " + this.curToken.getTokenText());
        }
    }
    
    // unary ::= ["+" | "-"] primary
    public void unary() {
        if (this.checkToken(TokenType.PLUS) || this.checkToken(TokenType.MINUS)) {
            this.emitter.emit(this.curToken.getTokenText());
            this.nextToken();
        }
        this.primary();
    }
    
    // term ::= unary {( "/" | "*" ) unary
    public void term() {
        this.unary();
        while (this.checkToken(TokenType.SLASH) || this.checkToken(TokenType.ASTERISK)) {
            this.emitter.emit(this.curToken.getTokenText());
            this.nextToken();
            this.unary();
        }
    }
    
    // expression ::= term {( "-" | "+" ) term}
    public void expression() {
        this.term();
        while (this.checkToken(TokenType.MINUS) || this.checkToken(TokenType.PLUS)) {
            this.emitter.emit(this.curToken.getTokenText());
            this.nextToken();
            this.term();
        }
    }

    // comparison ::= expression (("==" | "!=" | ">" | ">=" | "<" | "<=") expression)+
    public void comparison() {
        this.expression();
        // Must be at least 1 (comparison operator -> expression)
        if (this.isComparisonOperator()) {
            this.emitter.emit(this.curToken.getTokenText());
            this.nextToken();
            this.expression();
        }
        else {
            this.abort("Unexpected comparison operator at " + this.curToken.getTokenText());
        }
        // Can have 0 or more (comparison operator -> expression)
        while (this.isComparisonOperator()) {
            this.emitter.emit(this.curToken.getTokenText());
            this.nextToken();
            this.expression();
        }
    }
    
    // statement ::=
    public void statement() {
        // "PRINT" (expression | string)
        if (this.checkToken(TokenType.PRINT)) {
            this.nextToken();
            if (this.checkToken(TokenType.STRING)) {
                this.emitter.emitLine("printf(\"" + this.curToken.getTokenText() + "\\n\");");
                this.nextToken();
            }
            else {
                this.emitter.emit("printf(\"%" + ".2f\\n\", (float)(");
                this.expression();
                this.emitter.emitLine("));");
            }
        }
        // | "IF" comparison "THEN" newline {statement} "ENDIF"
        else if (this.checkToken(TokenType.IF)) {
            this.nextToken();
            this.emitter.emit("if (");
            this.comparison();

            this.match(TokenType.THEN);
            this.newline();
            this.emitter.emit(") {");

            // Zero or more statements in the body
            while (!this.checkToken(TokenType.ENDIF)) {
//                this.emitter.emitIndent();
                this.statement();
            }
            this.match(TokenType.ENDIF);
            this.emitter.emitLine("}");
        }
        // | "WHILE" comparison "REPEAT" newline {statement newline} "ENDWHILE"
        else if (this.checkToken(TokenType.WHILE)) {
            this.nextToken();
            this.emitter.emit("while (");
            this.comparison();

            this.match(TokenType.REPEAT);
            this.newline();
            this.emitter.emitLine(") {");

            while (!this.checkToken(TokenType.ENDWHILE)) {
                this.statement();
            }
            this.match(TokenType.ENDWHILE);
            this.emitter.emitLine("}");
        }
        // | "LABEL" ident
        else if (this.checkToken(TokenType.LABEL)) {
            this.nextToken();
            // Make sure this label doesn't already exist.
            if (this.labelsDeclared.contains(this.curToken.getTokenText())) {
                this.abort("Label already exists: " + this.curToken.getTokenText());
            }
            this.labelsDeclared.add(this.curToken.getTokenText());
            
            this.match(TokenType.IDENTIFIER);
            this.emitter.emitLine(this.curToken.getTokenText() + ":");
        }
        // | "GOTO" ident
        else if (this.checkToken(TokenType.GOTO)) {
            this.nextToken();
            this.labelsGotoed.add(this.curToken.getTokenText());
            this.match(TokenType.IDENTIFIER);
            this.emitter.emitLine("goto " + this.curToken.getTokenText() + ";");
        }
        // | "LET" ident "=" expression
        else if (this.checkToken(TokenType.LET)) {
            this.nextToken();
            // Check if ident exists in symbol table. If not, declare it.
            if (!this.symbols.contains(this.curToken.getTokenText())) {
                this.symbols.add(this.curToken.getTokenText());
                this.emitter.headerLine("float " + this.curToken.getTokenText() + ";");
            }
            this.emitter.emit(this.curToken.getTokenText() + " = ");
            this.match(TokenType.IDENTIFIER);
            this.match(TokenType.EQ);
            
            this.expression();
            this.emitter.emitLine(";");
        }
        // | "INPUT" ident
        else if (this.checkToken(TokenType.INPUT)) {
            this.nextToken();
            
            // If variable doesn't already exist, declare it.
            if (!this.symbols.contains(this.curToken.getTokenText())) {
                this.symbols.add(this.curToken.getTokenText());
                this.emitter.headerLine("float " + this.curToken.getTokenText() + ";");
            }
            // Emit scanf but also validate the input.
            // If invalid, set the variable to 0 and clear the input.
            this.emitter.emitLine("if(0 == scanf(\"%" + "f\", &" + this.curToken.getTokenText() + ")) {");
            this.emitter.emitLine(this.curToken.getTokenText() + " = 0;");
            this.emitter.emit("scanf(\"%");
            this.emitter.emitLine("*s\");");
            this.emitter.emitLine("}");
            
            this.match(TokenType.IDENTIFIER);
        }
        else {
            this.abort("Invalid statement at " + this.curToken.getTokenText() + " (" + this.curToken.getTokenKind() + ")");
        }
        
        // All statements end in newline
        this.newline();
    }
    
    public void abort(String message) {
        System.err.println("Parser error. " + message);
        System.exit(0); 
    }
    
    public void program() {
        System.out.println("PROGRAM");
        
        this.emitter.headerLine("#include <stdio.h>");
        this.emitter.headerLine("int main(void) {");

        // Since some newlines are required in our grammar, need to skip the excess.
        while (this.checkToken(TokenType.NEWLINE)) {
            this.nextToken();
        }
        
        // Parse all the statements in the program.
        while (!this.checkToken(TokenType.EOF)) {
            this.statement();
        }
        
        this.emitter.emitLine("return 0;");
        this.emitter.emitLine("}");
        
        // Check that each label referenced in a GOTO is declared.
        for (String label : this.labelsGotoed) {
            if (!this.labelsDeclared.contains(label))
                this.abort("Attempting to GOTO to undeclared label: " + label);
        }
    }
}