%{

// Imports.
%}

%token TRUE FALSE AND OR NOT LEFT_PARENTHESES RIGHT_PARENTHESES

%%
program
  : expression { yyout("result: " + $1.obj.toString());}
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

public void setDelegate(ParserDelegate delegate) {
	this.delegate = delegate;
} 

public int yylex() {
	int t = delegate.getToken();
	yylval = delegate.getVal();
	return t;
}

public void yyerror(String s) {
	delegate.showError(s);
}

public void yyout(String s) {
	delegate.showOutput(s);
}
