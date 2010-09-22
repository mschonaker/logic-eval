package logiceval;

import java.io.StringReader;

public class Main {

	public static void main(String[] args) {

		ParserWrapper parserWrapper = new ParserWrapper(new StringReader(
				"((false) or true and false or false)"), System.out,
				System.err, false);
		Boolean b = parserWrapper.eval();
		System.out.println(b);

	}
}
