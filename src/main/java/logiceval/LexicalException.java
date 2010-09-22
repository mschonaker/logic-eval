package logiceval;

public class LexicalException extends Exception {

	private static final long serialVersionUID = 2810947104246744507L;

	public LexicalException() {
		super();
	}

	public LexicalException(String message, Throwable cause) {
		super(message, cause);
	}

	public LexicalException(String message) {
		super(message);
	}

	public LexicalException(Throwable cause) {
		super(cause);
	}
}
