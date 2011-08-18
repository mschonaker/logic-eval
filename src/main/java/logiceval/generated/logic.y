%{

// Imports.

import java.util.List;
import java.util.LinkedList;

import logiceval.LexicalException;
import logiceval.ParserDelegate;
import logiceval.Symbol;

%}

%token TRUE FALSE AND OR NOT LEFT_PARENTHESES RIGHT_PARENTHESES ID

%%
program
  : expression { result = $1.bool; }
  | { yyout("Empty program");}
  
  | error { yyerror("Not an expression");}
  ;

expression
  : expression OR term {$$ = new Symbol($1.bool || $3.bool); } 
  | term
  
  | expression OR error { yyerror("Término inválido"); }
  ;
  
term
  : term AND factor {$$ = new Symbol($1.bool && $3.bool); }
  | factor
  
  | term AND error { yyerror("Factor inválido"); }
  ;
  
factor
  : TRUE 
  | FALSE 
  | ID 
  | NOT factor { $$ = new Symbol(!$2.bool); }
  | LEFT_PARENTHESES expression RIGHT_PARENTHESES { $$ = $2; }
  
  | LEFT_PARENTHESES expression error { yyerror("Se esperaba ')'"); } 
  | LEFT_PARENTHESES error { yyerror("Expresión inválida"); }
  | NOT error { yyerror("Factor inválido"); }
  ;
%%

private final ParserDelegate delegate;
private boolean result = false;

public Parser(ParserDelegate delegate, boolean debug) {
	this.delegate = delegate;
	this.yydebug = debug;
}

public int parse() throws LexicalException {
	return yyparse();
}

private int yylex() throws LexicalException {
	Symbol s = delegate.getSymbol();
	int t = s.token;
	yylval = s;
	return t;
}

private void yyerror(String s) {
	delegate.showError(s);
}

private void yyout(String s) {
	delegate.showOutput(s);
}

public boolean getResult() {
  return result;
}
