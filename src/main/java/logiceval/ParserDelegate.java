package logiceval;

import logiceval.generated.ParserVal;

public interface ParserDelegate {

	int getToken() throws LexicalException;

	ParserVal getVal();

	void showError(String s);

	void showOutput(String s);

}
