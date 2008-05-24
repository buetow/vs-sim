package utils;

public final class VSPair<A,B> {
	private A a;
	private B b;

	public VSPair(A a, B b) {
		this.a = a;
		this.b = b;
	}

	public A getA() {
		return a;
	}

	public B getB() {
		return b;
	}
}
