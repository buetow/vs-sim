/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package utils;

// TODO: Auto-generated Javadoc
/**
 * The Class VSTupel.
 */
public final class VSTupel<A,B,C> {
    
    /** The a. */
    private A a;
    
    /** The b. */
    private B b;
    
    /** The c. */
    private C c;

    /**
     * Instantiates a new vS tupel.
     * 
     * @param a the a
     * @param b the b
     * @param c the c
     */
    public VSTupel(A a, B b, C c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    /**
     * Gets the a.
     * 
     * @return the a
     */
    public A getA() {
        return a;
    }

    /**
     * Gets the b.
     * 
     * @return the b
     */
    public B getB() {
        return b;
    }

    /**
     * Gets the c.
     * 
     * @return the c
     */
    public C getC() {
        return c;
    }
}
