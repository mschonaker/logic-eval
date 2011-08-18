package logiceval;

public class Symbol {

	public Symbol(boolean bool, short token) {
		this.bool = bool;
		this.token = token;
	}

	public Symbol(boolean bool) {
		this(bool, (short) -1);
	}

	public Symbol(short token) {
		this(false, token);
	}

	public Symbol() {
		this(false, (short) -1);
	}

	public final boolean bool;
	public final short token;

}
