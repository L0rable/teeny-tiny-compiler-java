package token;

public enum TokenType {
    // Types(-1-99)
    EOF(-1),
    NEWLINE(0),
    NUMBER(1),
    IDENTIFIER(2),
    STRING(3),
    // Keywords(100-199)
    LABEL(101),
    GOTO(102),
    PRINT(103),
    INPUT(104),
    LET(105),
    IF(106),
    THEN(107),
    ENDIF(108),
    WHILE(109),
    REPEAT(110),
    ENDWHILE(111),
    AND(112),
    OR(113),
    NOT(114),
    // Operators(200-299)
    EQ(201),
    PLUS(202),
    MINUS(203),
    ASTERISK(204),
    SLASH(205),
    EQEQ(206),
    NOTEQ(207),
    LT(208),
    LTEQ(209),
    GT(210),
    GTEQ(211),
    PARENTHESESLEFT(212),
    PARENTHESESRIGHT(213);
    public final int value;
    
    private TokenType(int value) {
        this.value = value;
    }
}
