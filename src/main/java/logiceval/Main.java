package logiceval;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class Main {

	public static void main(String[] args) throws LexicalException,
			SyntaxException {

		Map<String, Boolean> context = new HashMap<String, Boolean>();
		context.put("a", Boolean.TRUE);

		LogicEval eval = new LogicEval(new StringReader(
				"((a) or false and true)"), System.out, System.err,
				false, context);
		Boolean b = eval.eval();
		System.out.println("Result=" + b);
	}
}
