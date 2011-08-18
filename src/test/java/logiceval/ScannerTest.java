package logiceval;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.StringReader;

import logiceval.Scanner.Token;

import org.junit.Test;

public class ScannerTest {

	@Test
	public void testLParen() throws Exception {

		StringReader reader = new StringReader("(");

		Scanner a = new Scanner(reader);

		assertEquals(Token.L_PAREN, a.nextToken());
		assertNull(a.nextToken());

	}

	@Test
	public void testRParen() throws Exception {

		StringReader reader = new StringReader(")");

		Scanner a = new Scanner(reader);

		assertEquals(Token.R_PAREN, a.nextToken());
		assertNull(a.nextToken());

	}

	@Test
	public void testTrue() throws Exception {

		StringReader reader = new StringReader("true");

		Scanner a = new Scanner(reader);

		assertEquals(Token.TRUE, a.nextToken());
		assertNull(a.nextToken());

	}

	@Test
	public void testFalse() throws Exception {

		StringReader reader = new StringReader("false");

		Scanner a = new Scanner(reader);

		assertEquals(Token.FALSE, a.nextToken());
		assertNull(a.nextToken());

	}

	@Test
	public void testNot() throws Exception {

		StringReader reader = new StringReader("not");

		Scanner a = new Scanner(reader);

		assertEquals(Token.NOT, a.nextToken());
		assertNull(a.nextToken());

	}

	@Test
	public void testAnd() throws Exception {

		StringReader reader = new StringReader("and");

		Scanner a = new Scanner(reader);

		assertEquals(Token.AND, a.nextToken());
		assertNull(a.nextToken());

	}

	@Test
	public void testOr() throws Exception {

		StringReader reader = new StringReader("or");

		Scanner a = new Scanner(reader);

		assertEquals(Token.OR, a.nextToken());
		assertNull(a.nextToken());

	}

	@Test
	public void testEspacios() throws Exception {

		StringReader reader = new StringReader("   \n \t     )         \r");

		Scanner a = new Scanner(reader);

		assertEquals(Token.R_PAREN, a.nextToken());
		assertNull(a.nextToken());

	}

	@Test
	public void testId() throws Exception {

		StringReader reader = new StringReader(" a b c ");

		Scanner a = new Scanner(reader);

		assertEquals(Token.ID, a.nextToken());
		assertEquals("a", a.text);
		assertEquals(Token.ID, a.nextToken());
		assertEquals("b", a.text);
		assertEquals(Token.ID, a.nextToken());
		assertEquals("c", a.text);
		assertNull(a.nextToken());

	}

	@Test
	public void testNand() throws Exception {

		StringReader reader = new StringReader(" not (a and b) ");

		Scanner a = new Scanner(reader);

		assertEquals(Token.NOT, a.nextToken());
		assertEquals(Token.L_PAREN, a.nextToken());
		assertEquals(Token.ID, a.nextToken());
		assertEquals("a", a.text);
		assertEquals(Token.AND, a.nextToken());
		assertEquals(Token.ID, a.nextToken());
		assertEquals("b", a.text);
		assertEquals(Token.R_PAREN, a.nextToken());

		assertNull(a.nextToken());

	}

	@Test
	public void testNor() throws Exception {

		StringReader reader = new StringReader(" not ((a or b)) ");

		Scanner a = new Scanner(reader);

		assertEquals(Token.NOT, a.nextToken());
		assertEquals(Token.L_PAREN, a.nextToken());
		assertEquals(Token.L_PAREN, a.nextToken());
		assertEquals(Token.ID, a.nextToken());
		assertEquals("a", a.text);
		assertEquals(Token.OR, a.nextToken());
		assertEquals(Token.ID, a.nextToken());
		assertEquals("b", a.text);
		assertEquals(Token.R_PAREN, a.nextToken());
		assertEquals(Token.R_PAREN, a.nextToken());

		assertNull(a.nextToken());

	}

	@Test
	public void testLong() throws Exception {

		StringReader reader = new StringReader(" (a or b) and not (c or d) ");

		Scanner a = new Scanner(reader);

		assertEquals(Token.L_PAREN, a.nextToken());
		assertEquals(Token.ID, a.nextToken());
		assertEquals("a", a.text);
		assertEquals(Token.OR, a.nextToken());
		assertEquals(Token.ID, a.nextToken());
		assertEquals("b", a.text);
		assertEquals(Token.R_PAREN, a.nextToken());

		assertEquals(Token.AND, a.nextToken());
		assertEquals(Token.NOT, a.nextToken());

		assertEquals(Token.L_PAREN, a.nextToken());
		assertEquals(Token.ID, a.nextToken());
		assertEquals("c", a.text);
		assertEquals(Token.OR, a.nextToken());
		assertEquals(Token.ID, a.nextToken());
		assertEquals("d", a.text);
		assertEquals(Token.R_PAREN, a.nextToken());

		assertNull(a.nextToken());

	}
}
