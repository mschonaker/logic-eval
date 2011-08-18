package logiceval;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import logiceval.Scanner.Token;
import logiceval.generated.Parser;

public class LogicEval {

	private final Map<Token, Short> translation = new HashMap<Scanner.Token, Short>();

	{
		translation.put(Token.AND, Parser.AND);
		translation.put(Token.FALSE, Parser.FALSE);
		translation.put(Token.ID, Parser.ID);
		translation.put(Token.L_PAREN, Parser.LEFT_PARENTHESES);
		translation.put(Token.NOT, Parser.NOT);
		translation.put(Token.OR, Parser.OR);
		translation.put(Token.R_PAREN, Parser.RIGHT_PARENTHESES);
		translation.put(Token.TRUE, Parser.TRUE);
		translation.put(Token.TRUE, Parser.TRUE);
	}

	private short translate(Token t) {
		if (t == null)
			return 0;
		return translation.get(t);
	}

	private final ParserDelegate delegate = new ParserDelegate() {

		private final List<String> errors = new ArrayList<String>();

		@Override
		public Symbol getSymbol() throws LexicalException {
			try {
				Token t = in.nextToken();

				if (t == null)
					return new Symbol((short) 0);

				switch (t) {
				case ID:
					return new Symbol(values.get(in.text).booleanValue(),
							translate(t));
				case TRUE:
					return new Symbol(true, translate(t));
				case FALSE:
					return new Symbol(false, translate(t));
				default:
					return new Symbol(translate(t));
				}

			} catch (IOException e) {
				throw new LexicalException("Unable to read next token", e);
			}
		}

		@Override
		public void showError(String s) {
			err.println(s);
			errors.add(s);
		}

		@Override
		public void showOutput(String s) {
			out.println(s);
		}

		@Override
		public List<String> getErrors() {
			return errors;
		}
	};

	private final Scanner in;
	private final PrintStream out, err;
	private final Parser parser;

	private final Map<String, Boolean> values;

	public LogicEval(Reader in, PrintStream out, PrintStream err,
			boolean debug, Map<String, Boolean> context)
			throws LexicalException {
		try {
			this.out = out;
			this.err = err;
			this.in = new Scanner(in);
			parser = new Parser(delegate, debug);
			values = context;
		} catch (IOException e) {
			throw new LexicalException("Unable to initialize scanner", e);
		}
	}

	public boolean eval() throws LexicalException, SyntaxException {
		if (parser.parse() != 0) {

			StringBuilder es = new StringBuilder();

			for (String s : delegate.getErrors())
				es.append(s).append("\n");

			throw new SyntaxException("Syntax errors during parsing: "
					+ es.toString());
		}

		return parser.getResult();
	}
}
