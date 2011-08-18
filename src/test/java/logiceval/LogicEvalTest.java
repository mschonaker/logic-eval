package logiceval;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class LogicEvalTest {

	@Test
	public void expression() throws Exception {
		Map<String, Boolean> context = new HashMap<String, Boolean>();
		context.put("a", Boolean.TRUE);

		LogicEval eval = new LogicEval(new StringReader(
				"((a) or false and true)"), System.out, System.err, false,
				context);
		assertTrue(eval.eval());

	}

	@Test
	public void testTrue() throws Exception {
		LogicEval eval = new LogicEval(new StringReader("true"), System.out,
				System.err, false, Collections.<String, Boolean> emptyMap());
		assertTrue(eval.eval());

	}

	@Test
	public void testFalse() throws Exception {
		LogicEval eval = new LogicEval(new StringReader("false"), System.out,
				System.err, false, Collections.<String, Boolean> emptyMap());
		assertFalse(eval.eval());

	}
	
	@Test
	public void testAnd() throws Exception {
		LogicEval eval = new LogicEval(new StringReader("false and true"), System.out,
				System.err, false, Collections.<String, Boolean> emptyMap());
		assertFalse(eval.eval());
		
	}
	
	@Test
	public void testAnd2() throws Exception {
		LogicEval eval = new LogicEval(new StringReader("true and true"), System.out,
				System.err, false, Collections.<String, Boolean> emptyMap());
		assertTrue(eval.eval());
	}
	
	@Test
	public void testAnd3() throws Exception {
		LogicEval eval = new LogicEval(new StringReader("true and false"), System.out,
				System.err, false, Collections.<String, Boolean> emptyMap());
		assertFalse(eval.eval());
	}
	
	@Test
	public void testAnd4() throws Exception {
		LogicEval eval = new LogicEval(new StringReader("false and false"), System.out,
				System.err, false, Collections.<String, Boolean> emptyMap());
		assertFalse(eval.eval());
	}
}
