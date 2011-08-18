package logiceval;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Un analizador léxico de expresiones booleanas.
 */
public class Scanner {

	/**
	 * Tokens que retorna el método {@link Scanner#nextToken()},
	 */
	public static enum Token {

		TRUE, FALSE, AND, OR, NOT, ID, L_PAREN, R_PAREN

	}

	/**
	 * Estados posibles del analizador.
	 */
	private static enum Estado {

		INICIAL, FINAL, ID

	}

	/**
	 * Grupos de caracteres para simplificar la matriz de acciones semánticas.
	 */
	private static enum Grupo {

		ESPACIOS, L_PAREN, R_PAREN, LETRA, FIN

	}

	/**
	 * Una interfaz para los elementos que se agreguen a la matriz de acciones
	 * semánticas.
	 */
	private static interface Accion {

		public void ejecutar() throws IOException;

	}

	/**
	 * Un hashmap para las palabras reservadas de String x Token.
	 */
	private final Map<String, Token> reservadas = new HashMap<String, Token>();

	/**
	 * La matriz de acciones semánticas.
	 */
	private final Accion[][] acciones;

	/**
	 * La entrada de texto. Inicializado fuera de la clase.
	 */
	private final Reader entrada;

	/**
	 * El estado actual del parser.
	 */
	private Estado estado;

	/**
	 * El caracter actual en la "cinta de entrada".
	 */
	private int actual;

	/**
	 * El token actual. Modificado por las acciones semánticas. El estado del
	 * parser no es consistente durante la ejecución del método
	 * {@link #nextToken()}.
	 */
	private Token token;

	/**
	 * Un buffer para acumular los valores de los identificadores y las palabras
	 * reservadas.
	 */
	private StringBuilder buffer = new StringBuilder();

	/**
	 * El texto del último identificador reconocido. No es consistente durante
	 * la ejecución del método {@link #nextToken()}.
	 */
	public String text = null;

	/**
	 * "Inicializer". Inicializa variables locales. Se ejecuta antes que
	 * cualquier constructor.
	 */
	{
		// Palabras reservadas x token.

		reservadas.put("true", Token.TRUE);
		reservadas.put("false", Token.FALSE);
		reservadas.put("and", Token.AND);
		reservadas.put("not", Token.NOT);
		reservadas.put("or", Token.OR);

		// Acciones semánticas. Cada una es una clase anónima que cumple con la
		// interfaz Accion. Como son una clase anónima tienen acceso a las
		// variables de estado (internas) del analizador, como por ejemplo el
		// caracter actual, la entrada, el estado actual, etc.
		// Se declaran como variables, ya que algunas pueden ser reutilizadas.

		Accion fin = new Accion() {

			@Override
			public void ejecutar() throws IOException {
				token = null;
				estado = Estado.FINAL;
			}

		};

		Accion lparen = new Accion() {

			@Override
			public void ejecutar() throws IOException {
				token = Token.L_PAREN;
				estado = Estado.FINAL;
				actual = entrada.read();
			}
		};

		Accion rparen = new Accion() {

			@Override
			public void ejecutar() throws IOException {
				token = Token.R_PAREN;
				estado = Estado.FINAL;
				actual = entrada.read();
			}
		};

		Accion ignore = new Accion() {

			@Override
			public void ejecutar() throws IOException {
				actual = entrada.read();
			}
		};

		Accion buff = new Accion() {

			@Override
			public void ejecutar() throws IOException {
				buffer.append((char) actual);
				estado = Estado.ID;
				actual = entrada.read();
			}
		};

		Accion finbuff = new Accion() {

			@Override
			public void ejecutar() throws IOException {
				estado = Estado.FINAL;
				text = buffer.toString();
				token = reservadas.containsKey(text) ? reservadas.get(text)
						: Token.ID;
			}
		};

		// "Allocation" en memoria de la matriz de acciones semánticas.
		// La matriz está organizada en columnas de estado x filas de grupos de
		// caracteres.
		// Las dimensiones se toman de los tamaños de los enums, para hacer más
		// flexible el código (values().length).
		// Tranquilamente se podría haber declarado
		// acciones = new Action[8][3];
		// Que son las dimensiones de los enums de estado y grupo
		// respectivamente.

		acciones = new Accion[Estado.values().length][Grupo.values().length];

		// Inicialización de la matriz de acciones semánticas. Los índices se
		// toman de la función ordinal() de los enums, que asigna un número
		// entero de posición empezando por 0 a cada elemento del enum. Con esto
		// el código es más flexible por si hay que sacar o agregar un estado o
		// grupo de caracteres.
		// Los lados derechos de las asignaciones son las instancias de clases
		// anónimas de acción semántica declaradas un poco más arriba.
		// Tranquilamente, se podría haber declarado algo como:
		// acciones[0][3] = fin;
		// acciones[0][0] = lparen;
		// ...
		// pero es mucho más trabajo para mantenerlo.
		// Algunos elementos de la matriz pueden faltar. Faltarían acciones de
		// error.

		acciones[Estado.INICIAL.ordinal()][Grupo.FIN.ordinal()] = fin;
		acciones[Estado.INICIAL.ordinal()][Grupo.L_PAREN.ordinal()] = lparen;
		acciones[Estado.INICIAL.ordinal()][Grupo.R_PAREN.ordinal()] = rparen;
		acciones[Estado.INICIAL.ordinal()][Grupo.ESPACIOS.ordinal()] = ignore;
		acciones[Estado.INICIAL.ordinal()][Grupo.LETRA.ordinal()] = buff;
		acciones[Estado.ID.ordinal()][Grupo.LETRA.ordinal()] = buff;
		acciones[Estado.ID.ordinal()][Grupo.FIN.ordinal()] = finbuff;
		acciones[Estado.ID.ordinal()][Grupo.ESPACIOS.ordinal()] = finbuff;
		acciones[Estado.ID.ordinal()][Grupo.L_PAREN.ordinal()] = finbuff;
		acciones[Estado.ID.ordinal()][Grupo.R_PAREN.ordinal()] = finbuff;
	}

	/**
	 * Constructor. Acepta un {@link Reader} como entrada de texto, inicializado
	 * fuera de la clase.
	 */
	public Scanner(Reader in) throws IOException {
		this.entrada = in;
		// Lee el primer caracter de la entrada.
		actual = in.read();
	}

	/**
	 * Devuelve el siguiente token. O null si no quedan más.
	 */
	public Token nextToken() throws IOException {

		// Estado inicial.
		estado = Estado.INICIAL;
		// Borra el contenido del buffer.
		buffer.delete(0, buffer.length());
		// El texto asociado se pone en null.
		text = null;

		// Mientras no se alcance el estado final:
		while (estado != Estado.FINAL) {

			// Clasificar el caracter actual.
			Grupo grupo = grupo(actual);

			// Obtener la acción semántica asociada al estado x grupo actuales.
			Accion accion = acciones[estado.ordinal()][grupo.ordinal()];
			if (accion == null)
				System.out.println(estado + " " + grupo);

			// Ejecutar la acción semántica asociada.
			accion.ejecutar();
		}

		// Retornar el token obtenido.
		return token;
	}

	/**
	 * Clasifica los caracteres leídos en grupos.
	 */
	private Grupo grupo(int c) {

		if (c == '(')
			return Grupo.L_PAREN;

		if (c == ')')
			return Grupo.R_PAREN;

		if (c >= 'a' && c <= 'z')
			return Grupo.LETRA;

		if (c == ' ' || c == '\t' || c == '\n' || c == '\r')
			return Grupo.ESPACIOS;

		if (c == -1)
			return Grupo.FIN;

		throw new IllegalArgumentException("Caracter inválido");
	}

	/**
	 * Método main con una prueba. Imprime:
	 * 
	 * <pre>
	 * L_PAREN ID(a) OR ID(b) R_PAREN AND NOT L_PAREN ID(c) OR ID(d) R_PAREN
	 * </pre>
	 */
	public static void main(String[] args) throws IOException {

		StringReader reader = new StringReader(" (a or b) and not (c or d) ");

		Scanner a = new Scanner(reader);

		Token t;
		while ((t = a.nextToken()) != null) {

			System.out.print(t);
			if (t == Token.ID)
				System.out.print("(" + a.text + ")");

			System.out.print(" ");
		}
	}
}
