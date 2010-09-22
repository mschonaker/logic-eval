%{

// Imports.
%}

%token TRUE FALSE AND OR NOT LEFT_PARENTHESES RIGHT_PARENTHESES

%%
program
  : expression { result = (Boolean) $1.obj; }
  | { yyout("Empty program");}
  
  | error { yyerror("Not an expression");}
  ;

expression
  : expression OR term {$$ = new ParserVal((Boolean) $1.obj || (Boolean) $3.obj); yyout("or"); } 
  | term { $$ = new ParserVal($1.obj); }
  
  | expression OR error { yyerror("Término inválido"); }
  ;
  
term
  : term AND factor {$$ = new ParserVal((Boolean) $1.obj || (Boolean) $2.obj); yyout("and"); }
  | factor { $$ = new ParserVal($1.obj); }
  
  | term AND error { yyerror("Factor inválido"); }
  ;
  
factor
  : TRUE { $$ = new ParserVal(Boolean.TRUE); yyout("true"); }
  | FALSE { $$ = new ParserVal(Boolean.FALSE); yyout("false"); }
  | NOT factor { $$ = new ParserVal(!(Boolean)$2.obj); yyout("not"); }
  | LEFT_PARENTHESES expression RIGHT_PARENTHESES { $$ = new ParserVal($2.obj); }
  
  | LEFT_PARENTHESES expression error { yyerror("Se esperaba ')'"); } 
  | LEFT_PARENTHESES error { yyerror("Expresión inválida"); }
  | NOT error { yyerror("Factor inválido"); }
  ;
%%

private ParserDelegate delegate;

private Boolean result = null;

public void setDelegate(ParserDelegate delegate) {
	this.delegate = delegate;
} 

private int yylex() {
	int t = delegate.getToken();
	yylval = delegate.getVal();
	return t;
}

private void yyerror(String s) {
	delegate.showError(s);
}

private void yyout(String s) {
	delegate.showOutput(s);
}

public Boolean getResult() {
  return result;
}