import java_cup.runtime.*;
%%
%class Lexer
%line
%column
%cup
%{   
    private Symbol symbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }
    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);
    }
%}
LineTerminator          = \r | \n | \r\n
WhiteSpace              = {LineTerminator} | [ \t\f]
And                     = [aA][nN][dD]
S                       = [sS]              // Does the parser really register this or does it assume its
Let                     = [lL][eE][tT]      // part of the next Path variable?
For                     = [fF][oO][rR]
In                      = [iI][nN]
Return                  = [rR][eE][tT][uU][rR][nN]
Variable                = [A-Za-z0-9]*         
Where                   = [wW][hH][eE][rR][eE]
Nodes                   = [nN][oO][dD][eE][sS]    
Edges                   = [eE][dD][gG][eE][sS]   
Paths                   = [pP][aA][tT][hH][sS]
PosNumber               = "["[0-9]+"]"
Number                  = [0-9]+ | [0-9]+"."[0-9]* | "."[0-9]+   
String                  = ['][^'\r\n]*[']              
Comparison              = [<] | [>] | [=] | [<][>] | [<][=] | [>][=]    
Equal                   = [:][=]
%%
/*-------------------------Lexical Rules Section-----------------------------*/
<YYINITIAL> {
    ";"                 { return symbol(sym.SEMI); }
    ","                 { return symbol(sym.COMMA); }
    "("                 { return symbol(sym.LPAREN); }
    ")"                 { return symbol(sym.RPAREN); }
    "["                 { return symbol(sym.LBRACK); }
    "]"                 { return symbol(sym.RBRACK); }
    "_"                 { return symbol(sym.UNDERSCORE); }  
    "*"                 { return symbol(sym.STAR); }
    "."                 { return symbol(sym.DOT); }
    {Equal}             { return symbol(sym.EQUAL); }
    {And}               { return symbol(sym.AND); }
    {S}                 { return symbol(sym.S); }
    {Let}               { return symbol(sym.LET); }
    {For}               { return symbol(sym.FOR); }
    {In}                { return symbol(sym.IN); }
    {Return}            { return symbol(sym.RETURN); }
    {Where}             { return symbol(sym.WHERE); }
    {Nodes}             { return symbol(sym.NODES); }
    {Edges}             { return symbol(sym.EDGES); }
    {Paths}             { return symbol(sym.PATHS); }
    {PosNumber}         { return symbol(sym.POSNUMBER, new String(yytext())); }
    {Number}            { return symbol(sym.NUMBER, new String(yytext())); }
    {Variable}          { return symbol(sym.VARIABLE, new String(yytext())); }
    {Comparison}        { return symbol(sym.COMPARISON, new String(yytext())); }
    {String}            { return symbol(sym.STRING, new String(yytext())); }
    {WhiteSpace}        { /* just skip what was found, do nothing */ }   
}
[^]                     { System.out.println("Syntax Error - Scanning problem");
                          return symbol(sym.ERROR); }
