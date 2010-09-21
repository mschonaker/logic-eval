package logiceval;

public interface ParserDelegate {

	int getToken();
	
	ParserVal getVal();

	void showError(String s);
	
	void showOutput(String s);

}
