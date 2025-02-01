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
    
    private int curIndents;
    
    public Parser(Lexer lexer, Emitter emitter) {
        this.lexer = lexer;
        this.emitter = emitter;
        
        this.symbols = new ArrayList<String>();
        this.labelsDeclared = new ArrayList<String>();
        this.labelsGotoed = new ArrayList<String>();
        
        this.curToken = null;
        this.peekToken = null;
        
        this.curIndents = 1;
        
        this.nextToken();
        this.nextToken();
    }
    
    private boolean checkToken(TokenType kind) {
        return kind == this.curToken.getTokenKind();
    }

    private void match(TokenType kind) {
        if (!this.checkToken(kind))
            this.abort("Expected " + kind.toString() + ", got " + this.curToken.getTokenText());
        this.nextToken();
    }

    private void nextToken() {
        this.curToken = this.peekToken;
        this.peekToken = this.lexer.getToken();
    }
    
    private boolean isComparisonOperator() {
        return this.checkToken(TokenType.EQEQ) || this.checkToken(TokenType.NOTEQ)
                || this.checkToken(TokenType.LT) || this.checkToken(TokenType.LTEQ)
                || this.checkToken(TokenType.GT) || this.checkToken(TokenType.GTEQ);
    }

    private boolean isLogicalOperator() {
//        return this.checkToken(TokenType.AND) || this.checkToken(TokenType.OR) || this.checkToken(TokenType.NOT);
        return this.checkToken(TokenType.AND);
    }
    
    // newline ::= '\n'+
    private void newline() {
        this.match(TokenType.NEWLINE);
        while (this.checkToken(TokenType.NEWLINE)) {
            this.nextToken();
        }
    }
    
    // primary ::= number | ident
    private void primary() {
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
            this.abort("primary: Unexpected token at " + this.curToken.getTokenText());
        }
    }
    
    // unary ::= ["+" | "-"] primary
    private void unary() {
        if (this.checkToken(TokenType.PLUS) || this.checkToken(TokenType.MINUS)) {
            this.emitter.emit(this.curToken.getTokenText());
            this.nextToken();
        }
        this.primary();
    }
    
    // term ::= unary {( "/" | "*" ) unary}
    private void term() {
        this.unary();
        while (this.checkToken(TokenType.SLASH) || this.checkToken(TokenType.ASTERISK)) {
            this.emitter.emit(this.curToken.getTokenText());
            this.nextToken();
            this.unary();
        }
    }
    
    // expression ::= term {( "-" | "+" ) term}
    //      | ("(")+ expression (")")+
    //      | ("(")+ expression (")")+ {( "/" | "*" | "-" | "+" )  ("(")+ expression (")")+}
    private void expression() {
        if (this.checkToken(TokenType.PARENTHESESLEFT)) {
            boolean nextExpression = true;
            int parentheses = 0;
            while (nextExpression) {
                while (this.checkToken(TokenType.PARENTHESESLEFT)) {
                    parentheses++;
                    this.emitter.emit(this.curToken.getTokenText());
                    this.nextToken();
                }
                while (!this.checkToken(TokenType.PARENTHESESRIGHT)) {
                    this.term();
                    while (this.checkToken(TokenType.MINUS) || this.checkToken(TokenType.PLUS)) {
                        this.emitter.emit(this.curToken.getTokenText());
                        this.nextToken();
                        this.term();
                    }
                }
                for (int i = 0; i < parentheses; i++) {
                    if (!this.checkToken(TokenType.PARENTHESESRIGHT)) {
                        this.abort("expression: Expected ')' character at " + this.curToken.getTokenText());
                    }
                    parentheses--;
                    this.emitter.emit(this.curToken.getTokenText());
                    this.nextToken();
                }

                nextExpression = this.checkToken(TokenType.SLASH) || this.checkToken(TokenType.ASTERISK)
                        || this.checkToken(TokenType.MINUS) || this.checkToken(TokenType.PLUS);
                if (nextExpression) {
                    this.emitter.emit(this.curToken.getTokenText());
                    this.nextToken();
                }
            }

            if (parentheses >= 1) {
                if (this.checkToken(TokenType.PARENTHESESRIGHT)) {
                    this.emitter.emit(this.curToken.getTokenText());
                    this.nextToken();
                }
                else {
                    this.abort("expression: Expected ')' character at " + this.curToken.getTokenText());
                }
            }
        }
        else {
            this.term();
            while (this.checkToken(TokenType.MINUS) || this.checkToken(TokenType.PLUS)) {
                this.emitter.emit(this.curToken.getTokenText());
                this.nextToken();
                this.term();
            }
        }
    }

    // comparison ::= expression (("NOT" | "AND" | "OR") expression)+
    //          | expression (("==" | "!=" | ">" | ">=" | "<" | "<=") expression)+
    private void comparison() {
        this.expression();
        // Must be at least 1 (comparison operator -> expression)
        if (this.isLogicalOperator() || this.isComparisonOperator()) {
            this.emitter.emit(this.curToken.getTokenText());
            this.nextToken();
            this.expression();
        } else {
            this.abort("comparison: Unexpected logical/comparison operator at " + this.curToken.getTokenText());
        }
        // Can have 0 or more (comparison operator -> expression)
        while (this.isLogicalOperator() || this.isComparisonOperator()) {
            this.emitter.emit(this.curToken.getTokenText());
            this.nextToken();
            this.expression();
        }
    }
    
    // statement ::=
    private void statement() {
        // | "LABEL" ident
        if (this.checkToken(TokenType.LABEL)) {
            this.nextToken();
            // Make sure this label doesn't already exist.
            if (this.labelsDeclared.contains(this.curToken.getTokenText())) {
                this.abort("Label already exists: " + this.curToken.getTokenText());
            }
            this.labelsDeclared.add(this.curToken.getTokenText());

            this.emitter.emitLine(this.curToken.getTokenText() + ":");
            this.match(TokenType.IDENTIFIER);
        }
        // | "GOTO" ident
        else if (this.checkToken(TokenType.GOTO)) {
            this.nextToken();
            this.labelsGotoed.add(this.curToken.getTokenText());
            this.emitter.emitLine("goto " + this.curToken.getTokenText() + ";");
            this.match(TokenType.IDENTIFIER);
        }
        // "PRINT" (expression | string)
        else if (this.checkToken(TokenType.PRINT)) {
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
        // | "INPUT" ident
        else if (this.checkToken(TokenType.INPUT)) {
            this.nextToken();

            // If variable doesn't already exist, declare it.
            if (!this.symbols.contains(this.curToken.getTokenText())) {
                this.symbols.add(this.curToken.getTokenText());
                this.emitter.headerIndent(1);
                this.emitter.headerLine("float " + this.curToken.getTokenText() + ";");
            }
            // Emit scanf but also validate the input.
            // If invalid, set the variable to 0 and clear the input.
            this.curIndents++;
            this.emitter.emitLine("if (0 == scanf(\"%" + "f\", &" + this.curToken.getTokenText() + ")) {");

            this.emitter.emitIndent(this.curIndents);
            this.emitter.emitLine(this.curToken.getTokenText() + " = 0;");

            this.emitter.emitIndent(this.curIndents);
            this.emitter.emit("scanf(\"%");
            this.emitter.emitLine("*s\");");

            this.curIndents--;
            this.emitter.emitIndent(this.curIndents);
            this.emitter.emitLine("}");

            this.match(TokenType.IDENTIFIER);
        }
        // | "LET" ident "=" expression
        else if (this.checkToken(TokenType.LET)) {
            this.nextToken();

            // Check if ident exists in symbol table. If not, declare it.
            if (!this.symbols.contains(this.curToken.getTokenText())) {
                this.symbols.add(this.curToken.getTokenText());
                this.emitter.headerIndent(1);
                this.emitter.headerLine("float " + this.curToken.getTokenText() + ";");
            }
            this.emitter.emit(this.curToken.getTokenText() + " = ");
            this.match(TokenType.IDENTIFIER);
            this.match(TokenType.EQ);

            this.expression();
            this.emitter.emitLine(";");
        }
        // | "IF" comparison "THEN" newline {statement} "ENDIF"
        else if (this.checkToken(TokenType.IF)) {
            this.nextToken();
            this.emitter.emit("if (");
            this.comparison();

            this.match(TokenType.THEN);
            this.newline();
            boolean zeroStatements = this.checkToken(TokenType.ENDIF);
            if (zeroStatements) {
                this.emitter.emit(") {");
            } else {
                this.emitter.emitLine(") {");
            }

            while (!this.checkToken(TokenType.ENDIF)) {
                this.curIndents++;
                this.emitter.emitIndent(this.curIndents);
                this.statement();
            }
            this.match(TokenType.ENDIF);
            this.curIndents = 1;
            if (!zeroStatements) {
                this.emitter.emitIndent(this.curIndents);
            }
            this.emitter.emitLine("}");
        }
        // | "WHILE" comparison "REPEAT" newline {statement newline} "ENDWHILE"
        else if (this.checkToken(TokenType.WHILE)) {
            this.nextToken();
            this.emitter.emit("while (");
            this.comparison();

            this.match(TokenType.REPEAT);
            this.newline();
            boolean zeroStatements = this.checkToken(TokenType.ENDWHILE);
            if (zeroStatements) {
                this.emitter.emit(") {");
            } else {
                this.emitter.emitLine(") {");
            }

            while (!this.checkToken(TokenType.ENDWHILE)) {
                this.curIndents++;
                this.emitter.emitIndent(this.curIndents);
                this.statement();
            }

            this.match(TokenType.ENDWHILE);
            this.curIndents = 1;
            if (!zeroStatements) {
                this.emitter.emitIndent(this.curIndents);
            }
            this.emitter.emitLine("}");
        }
        else {
            this.abort("Invalid statement at " + this.curToken.getTokenText() + " (" + this.curToken.getTokenKind() + ")");
        }

        this.curIndents = 1;

        // All statements end in newline
        this.newline();
    }
    
    private void abort(String message) {
        System.err.println("Parser error. " + message);
        System.exit(0); 
    }
    
    public void program() {
        System.out.println("PROGRAM");
        
        this.emitter.headerLine("#include <stdio.h>");
        this.emitter.headerLine("");
        this.emitter.headerLine("int main() {");

        // Since some newlines are required in our grammar, need to skip the excess.
        while (this.checkToken(TokenType.NEWLINE)) {
            this.nextToken();
        }
        
        // Parse all the statements in the program.
        while (!this.checkToken(TokenType.EOF)) {
//            this.curIndents = 1;
            this.emitter.emitIndent(this.curIndents);
            this.statement();
        }
        
        this.emitter.emitIndent(this.curIndents);
        this.emitter.emitLine("return 0;");
        this.emitter.emitLine("}");
        
        // Check that each label referenced in a GOTO is declared.
        for (String label : this.labelsGotoed) {
            if (!this.labelsDeclared.contains(label))
                this.abort("Attempting to GOTO to undeclared label: " + label);
        }
    }
}