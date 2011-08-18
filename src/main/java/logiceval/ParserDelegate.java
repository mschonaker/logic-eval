package logiceval;

import java.util.List;

public interface ParserDelegate {

	Symbol getSymbol() throws LexicalException;

	void showError(String s);

	void showOutput(String s);

	List<String> getErrors();
	
}
