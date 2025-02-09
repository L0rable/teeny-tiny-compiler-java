# Teeny Tiny Compiler in Java
First off thanks to [Austin](https://austinhenley.com/index.html) for writing up the 3 part article on [building a compiler](https://austinhenley.com/blog/teenytinycompiler1.html) which compiles Teeny Tiny (similar to BASIC) to C.
Instead of using Python, I decided to do it Java because why not.

## General Overview
To start off the compiler is built from 3 stages of processing:

![alt text](https://austinhenley.com/blog/images/compilersteps.png)

1. [Lexer](https://austinhenley.com/blog/teenytinycompiler1.html): Takes in the input code and identifies the keywords called "Tokens"
2. [Parser](https://austinhenley.com/blog/teenytinycompiler2.html): Verifies the grammar, which is the order of the Tokens. This can be expressed through [Backus–Naur form](https://en.wikipedia.org/wiki/Backus%E2%80%93Naur_form) and [Extended](https://en.wikipedia.org/wiki/Extended_Backus%E2%80%93Naur_form)  Standard Notation.
```
program ::= {statement}
statement ::= "PRINT" (expression | string) newline
    | "IF" comparison "THEN" newline {statement} "ENDIF" newline
    | "WHILE" comparison "REPEAT" newline {statement} "ENDWHILE" newline
    | "LABEL" ident newline
    | "GOTO" ident newline
    | "LET" ident "=" expression newline
    | "INPUT" ident newline
comparison ::= ["NOT"] expression (("AND" | "OR") expression)+
    | ["NOT"] expression (("==" | "!=" | ">" | ">=" | "<" | "<=") expression)+
expression ::= term {( "-" | "+" ) term}
term ::= unary {( "/" | "*" ) unary}
unary ::= ["+" | "-"] primary
primary ::= number | ident
newline ::= '\n'+
```
3. [Emitter](https://austinhenley.com/blog/teenytinycompiler3.html): Produces the output code and then writes it to a file.

## Current features
This includes the features from the tutorial:
- Numerical variables
- Basic arithmetic
- If statements
- While loops
- Print text and numbers
- Input numbers
- Labels and goto
- Comments

Here are the features I have implemented:
- Indentation
- Parentheses for expressions

## Future features
There are a bunch that are list on [last part](https://austinhenley.com/blog/teenytinycompiler3.html) of the tutorial article.
<br>

## Sample Code
```
PRINT "How many fibonacci numbers do you want?"
INPUT nums
PRINT ""

LET a = 0
LET b = 1
WHILE nums > 0 REPEAT
    PRINT a
    LET c = a + b
    LET a = b
    LET b = c
    LET nums = nums - 1
ENDWHILE
```

## Build & Run
Using the Exec Maven Plugin to run and test the compiler. I have also included Apache Maven JAR Plugin to build JAR executables.
<br>
Run default source file (helloWorld.teenytiny): <br>
```mvn compile Exec:java```
<br>
Run a test program file: <br>
```mvn compile exec:java -Dexec.args="average.teenytiny testPrograms"```
<br>
Run a specific program file: <br>
```mvn compile exec:java -Dexec.args="~/Desktop/average.teenytiny"```
<br>
Run tests: <br>
```mvn test```
