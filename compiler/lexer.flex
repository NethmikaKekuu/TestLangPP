/* lexer.flex - TestLang++ Lexer */

import java_cup.runtime.*;

%%

%class Lexer
%unicode
%cup
%line
%column

%{
    private Symbol symbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }

    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);
    }
%}

/* Patterns */
WHITESPACE = [ \t\r\n]+
LINE_COMMENT = "//"[^\r\n]*
IDENT = [A-Za-z_][A-Za-z0-9_]*
NUMBER = [0-9]+
STRING_CHAR = [^\"\\\r\n]
ESCAPE_SEQ = \\[\"\\]

%%

/* Whitespace and comments */
{WHITESPACE}        { /* skip */ }
{LINE_COMMENT}      { /* skip */ }

/* Keywords */
"config"            { return symbol(sym.CONFIG); }
"base_url"          { return symbol(sym.BASE_URL); }
"header"            { return symbol(sym.HEADER); }
"let"               { return symbol(sym.LET); }
"test"              { return symbol(sym.TEST); }
"GET"               { return symbol(sym.GET); }
"POST"              { return symbol(sym.POST); }
"PUT"               { return symbol(sym.PUT); }
"DELETE"            { return symbol(sym.DELETE); }
"expect"            { return symbol(sym.EXPECT); }
"status"            { return symbol(sym.STATUS); }
"body"              { return symbol(sym.BODY); }
"contains"          { return symbol(sym.CONTAINS); }

/* Punctuation */
"{"                 { return symbol(sym.LBRACE); }
"}"                 { return symbol(sym.RBRACE); }
";"                 { return symbol(sym.SEMI); }
"="                 { return symbol(sym.EQUALS); }
/* Remove this line: "."                 { return symbol(sym.DOT); } */

/* Numbers */
{NUMBER}            { return symbol(sym.NUMBER, yytext()); }

/* Identifiers */
{IDENT}             { return symbol(sym.IDENT, yytext()); }

/* Strings with escape sequences */
\"({STRING_CHAR}|{ESCAPE_SEQ})*\" {
    String raw = yytext();
    // Remove surrounding quotes
    String content = raw.substring(1, raw.length() - 1);
    // Process escape sequences
    content = content.replace("\\\"", "\"").replace("\\\\", "\\");
    return symbol(sym.STRING, content);
}

/* Error fallback */
.                   {
    System.err.println("Illegal character at line " + (yyline + 1) + ": '" + yytext() + "'");
    return symbol(sym.error);
}