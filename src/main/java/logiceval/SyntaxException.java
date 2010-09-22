package logiceval;

public class SyntaxException extends Exception {

	private static final long serialVersionUID = 7246855906045504411L;

	public SyntaxException() {
		super();
	}

	public SyntaxException(String message, Throwable cause) {
		super(message, cause);
	}

	public SyntaxException(String message) {
		super(message);
	}

	public SyntaxException(Throwable cause) {
		super(cause);
	}
}
