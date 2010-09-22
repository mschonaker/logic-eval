package logiceval;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StreamTokenizer;

public class ParserWrapper implements ParserDelegate {

	public static enum Token {

		TRUE(Parser.TRUE), FALSE(Parser.FALSE), AND(Parser.AND), OR(Parser.OR), NOT(
				Parser.NOT), LEFT_PARENTHESES(Parser.LEFT_PARENTHESES), RIGHT_PARENTHESES(
				Parser.RIGHT_PARENTHESES), EOF(0);

		private int code;

		private Token(int code) {
			this.code = code;
		}

		public int getCode() {
			return code;
		}
	}

	private Parser parser;
	private ParserVal current = new ParserVal();;

	private StreamTokenizer in;
	private PrintStream out, err;

	public ParserWrapper(Reader in, PrintStream out, PrintStream err,
			boolean debug) {
		this.in = new StreamTokenizer(in);
		this.in.eolIsSignificant(false);
		this.out = out;
		this.err = err;
		parser = new Parser();
		parser.setDelegate(this);
		parser.yydebug = debug;
	}

	public int getToken() {

		try {
			int nextToken = in.nextToken();

			switch (nextToken) {
			case (StreamTokenizer.TT_EOF):
				return Token.EOF.getCode();
			case (StreamTokenizer.TT_WORD):

				try {
					return Token.valueOf(in.sval.toUpperCase()).getCode();
				} catch (IllegalArgumentException e) {
					throw new RuntimeException("Lexical error", e);
				}

			default:
				if (nextToken == '(')
					return Token.LEFT_PARENTHESES.getCode();
				if (nextToken == ')')
					return Token.RIGHT_PARENTHESES.getCode();
				throw new RuntimeException("Lexical error");
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void showError(String s) {
		err.println(s);
	}

	public void showOutput(String s) {
		out.println(s);
	}

	public void parse() {
		if (parser.yyparse() != 0)
			out.println("PARSE FAILURE");
		else
			out.println("PARSE SUCCESSFUL");
	}

	public ParserVal getVal() {
		return current;
	}
}
