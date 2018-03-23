package solutions.desperate.glicko.infrastructure.math;

import java.util.*;
import java.math.*;

/**
 * Fractions (rational numbers).
 * They are ratios of two BigInteger numbers, reduced to coprime
 * numerator and denominator.
 *
 * @author Richard J. Mathar
 * @since 2006-06-25
 */
public class Rational implements Cloneable, Comparable<Rational> {
    /**
     * numerator
     */
    private BigInteger a;

    /**
     * denominator, always larger than zero.
     */
    BigInteger b;

    /**
     * The maximum and minimum value of a standard Java integer, 2^31.
     *
     * @since 2009-05-18
     */
    private static BigInteger MAX_INT = new BigInteger("2147483647");
    private static BigInteger MIN_INT = new BigInteger("-2147483648");

    /**
     * The constant 0.
     */
    static public Rational ZERO = new Rational();

    /**
     * The constant 1.
     */
    static Rational ONE = new Rational(1, 1);

    /**
     * Default ctor, which represents the zero.
     *
     * @author Richard J. Mathar
     * @since 2007-11-17
     */
    Rational() {
        a = BigInteger.ZERO;
        b = BigInteger.ONE;
    } /* ctor */

    /**
     * ctor from a numerator and denominator.
     *
     * @param a the numerator.
     * @param b the denominator.
     * @author Richard J. Mathar
     */
    Rational(BigInteger a, BigInteger b) {
        this.a = a;
        this.b = b;
        normalize();
    } /* ctor */

    /**
     * ctor from a numerator.
     *
     * @param a the BigInteger.
     * @author Richard J. Mathar
     */
    Rational(BigInteger a) {
        this.a = a;
        b = new BigInteger("1");
    } /* ctor */

    /**
     * ctor from a numerator and denominator.
     *
     * @param a the numerator.
     * @param b the denominator.
     * @author Richard J. Mathar
     */
    Rational(int a, int b) {
        this(new BigInteger("" + a), new BigInteger("" + b));
    } /* ctor */

    /**
     * ctor from a terminating continued fraction.
     * Constructs the value of cfr[0]+1/(cfr[1]+1/(cfr[2]+...))).
     *
     * @param cfr The coefficients cfr[0], cfr[1],... of the continued fraction.
     *            An exception is thrown if any of these is zero.
     * @author Richard J. Mathar
     * @since 2012-03-08
     */
    private Rational(Vector<BigInteger> cfr) {
        if (cfr.size() == 0)
            throw new NumberFormatException("Empty continued fraction");
        else if (cfr.size() == 1) {
            this.a = cfr.firstElement();
            this.b = BigInteger.ONE;
        } else {
            /* recursive this = cfr[0]+1/(cfr[1]+...) where cfr[1]+... = rec =rec.a/rec.b
             * this = cfr[0]+rec.b/rec.a = (cfr[0]*rec.a+rec.b)/rec.a .
             * Create a cloned version of references to cfr, without cfr[0]
             */
            Vector<BigInteger> clond = new Vector<>();
            for (int i = 1; i < cfr.size(); i++)
                clond.add(cfr.elementAt(i));
            Rational rec = new Rational(clond);
            this.a = cfr.firstElement().multiply(rec.a).add(rec.b);
            this.b = rec.a;
            normalize();
        }
    } /* ctor */

    /**
     * Create a copy.
     *
     * @author Richard J. Mathar
     * @since 2008-11-07
     */
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Rational clone() {
        /* protected access means this does not work
         * return new Rational(a.clone(), b.clone()) ;
         */
        BigInteger aclon = new BigInteger("" + a);
        BigInteger bclon = new BigInteger("" + b);
        return new Rational(aclon, bclon);
    } /* Rational.clone */

    /**
     * Multiply by another fraction.
     *
     * @param val a second rational number.
     * @return the product of this with the val.
     * @author Richard J. Mathar
     */
    public Rational multiply(final Rational val) {
        BigInteger num = a.multiply(val.a);
        BigInteger deno = b.multiply(val.b);
        /* Normalization to an coprime format will be done inside
         * the ctor() and is not duplicated here.
         */
        return (new Rational(num, deno));
    } /* Rational.multiply */

    /**
     * Multiply by a BigInteger.
     *
     * @param val a second number.
     * @return the product of this with the value.
     * @author Richard J. Mathar
     */
    public Rational multiply(final BigInteger val) {
        Rational val2 = new Rational(val, BigInteger.ONE);
        return (multiply(val2));
    } /* Rational.multiply */

    /**
     * Multiply by an integer.
     *
     * @param val a second number.
     * @return the product of this with the value.
     * @author Richard J. Mathar
     */
    public Rational multiply(final int val) {
        BigInteger tmp = new BigInteger("" + val);
        return multiply(tmp);
    } /* Rational.multiply */

    /**
     * Power to an integer.
     *
     * @param exponent the exponent.
     * @return this value raised to the power given by the exponent.
     * If the exponent is 0, the value 1 is returned.
     * @author Richard J. Mathar
     */
    public Rational pow(int exponent) {
        if (exponent == 0)
            return new Rational(1, 1);

        BigInteger num = a.pow(Math.abs(exponent));
        BigInteger deno = b.pow(Math.abs(exponent));
        if (exponent > 0)
            return (new Rational(num, deno));
        else
            return (new Rational(deno, num));
    } /* Rational.pow */

    /**
     * Power to an integer.
     *
     * @param exponent the exponent.
     * @return this value raised to the power given by the exponent.
     * If the exponent is 0, the value 1 is returned.
     * @author Richard J. Mathar
     * @since 2009-05-18
     */
    public Rational pow(BigInteger exponent) throws NumberFormatException {
        /* test for overflow */
        if (exponent.compareTo(MAX_INT) > 0)
            throw new NumberFormatException("Exponent " + exponent.toString() + " too large.");
        if (exponent.compareTo(MIN_INT) < 0)
            throw new NumberFormatException("Exponent " + exponent.toString() + " too small.");

        /* promote to the simpler interface above */
        return pow(exponent.intValue());
    } /* Rational.pow */

    /**
     * r-th root.
     *
     * @param r the inverse of the exponent.
     *          2 for the square root, 3 for the third root etc
     * @return this value raised to the inverse power given by the root argument, this^(1/r).
     * @author Richard J. Mathar
     * @since 2009-05-18
     */
    public Rational root(BigInteger r) throws NumberFormatException {
        /* test for overflow */
        if (r.compareTo(MAX_INT) > 0)
            throw new NumberFormatException("Root " + r.toString() + " too large.");
        if (r.compareTo(MIN_INT) < 0)
            throw new NumberFormatException("Root " + r.toString() + " too small.");

        int rthroot = r.intValue();
        /* cannot pull root of a negative value with even-valued root */
        if (compareTo(ZERO) < 0 && (rthroot % 2) == 0)
            throw new NumberFormatException("Negative basis " + toString() + " with odd root " + r.toString());

        /* extract a sign such that we calculate |n|^(1/r), still r carrying any sign
         */
        final boolean flipsign = compareTo(ZERO) < 0 && (rthroot % 2) != 0;

        /* delegate the main work to ifactor#root()
         */
        Ifactor num = new Ifactor(a.abs());
        Ifactor deno = new Ifactor(b);
        final Rational resul = num.root(rthroot).divide(deno.root(rthroot));
        if (flipsign)
            return resul.negate();
        else
            return resul;
    } /* Rational.root */

    /**
     * Raise to a rational power.
     *
     * @param exponent The exponent.
     * @return This value raised to the power given by the exponent.
     * If the exponent is 0, the value 1 is returned.
     * @author Richard J. Mathar
     * @since 2009-05-18
     */
    public Rational pow(Rational exponent) throws NumberFormatException {
        if (exponent.a.compareTo(BigInteger.ZERO) == 0)
            return new Rational(1, 1);

        /* calculate (a/b)^(exponent.a/exponent.b) as ((a/b)^exponent.a)^(1/exponent.b)
         * = tmp^(1/exponent.b)
         */
        Rational tmp = pow(exponent.a);
        return tmp.root(exponent.b);
    } /* Rational.pow */

    /**
     * Divide by another fraction.
     *
     * @param val A second rational number.
     * @return The value of this/val
     * @author Richard J. Mathar
     */
    public Rational divide(final Rational val) {
        if (val.compareTo(Rational.ZERO) == 0)
            throw new ArithmeticException("Dividing " + toString() + " through zero.");
        BigInteger num = a.multiply(val.b);
        BigInteger deno = b.multiply(val.a);
        /* Reduction to a coprime format is done inside the ctor,
         * and not repeated here.
         */
        return (new Rational(num, deno));
    } /* Rational.divide */

    /**
     * Divide by an integer.
     *
     * @param val a second number.
     * @return the value of this/val
     * @author Richard J. Mathar
     */
    public Rational divide(BigInteger val) {
        if (val.compareTo(BigInteger.ZERO) == 0)
            throw new ArithmeticException("Dividing " + toString() + " through zero.");
        Rational val2 = new Rational(val, BigInteger.ONE);
        return (divide(val2));
    } /* Rational.divide */

    /**
     * Divide by an integer.
     *
     * @param val A second number.
     * @return The value of this/val
     * @author Richard J. Mathar
     */
    public Rational divide(int val) {
        if (val == 0)
            throw new ArithmeticException("Dividing " + toString() + " through zero.");
        Rational val2 = new Rational(val, 1);
        return (divide(val2));
    } /* Rational.divide */

    /**
     * Add another fraction.
     *
     * @param val The number to be added
     * @return this+val.
     * @author Richard J. Mathar
     */
    public Rational add(Rational val) {
        BigInteger num = a.multiply(val.b).add(b.multiply(val.a));
        BigInteger deno = b.multiply(val.b);
        return (new Rational(num, deno));
    } /* Rational.add */

    /**
     * Add another integer.
     *
     * @param val The number to be added
     * @return this+val.
     * @author Richard J. Mathar
     */
    public Rational add(BigInteger val) {
        Rational val2 = new Rational(val, BigInteger.ONE);
        return (add(val2));
    } /* Rational.add */

    /**
     * Add another integer.
     *
     * @param val The number to be added
     * @return this+val.
     * @author Richard J. Mathar
     * @since May 26 2010
     */
    public Rational add(int val) {
        BigInteger val2 = a.add(b.multiply(new BigInteger("" + val)));
        return new Rational(val2, b);
    } /* Rational.add */

    /**
     * Compute the negative.
     *
     * @return -this.
     * @author Richard J. Mathar
     */
    private Rational negate() {
        return (new Rational(a.negate(), b));
    } /* Rational.negate */

    /**
     * Subtract another fraction.
     *
     * @param val the number to be subtracted from this
     * @return this - val.
     * @author Richard J. Mathar
     */
    public Rational subtract(Rational val) {
        Rational val2 = val.negate();
        return (add(val2));
    } /* Rational.subtract */

    /**
     * Subtract an integer.
     *
     * @param val the number to be subtracted from this
     * @return this - val.
     * @author Richard J. Mathar
     */
    public Rational subtract(BigInteger val) {
        Rational val2 = new Rational(val, BigInteger.ONE);
        return (subtract(val2));
    } /* Rational.subtract */

    /**
     * Subtract an integer.
     *
     * @param val the number to be subtracted from this
     * @return this - val.
     * @author Richard J. Mathar
     */
    public Rational subtract(int val) {
        Rational val2 = new Rational(val, 1);
        return (subtract(val2));
    } /* Rational.subtract */

    /**
     * Absolute value.
     *
     * @return The absolute (non-negative) value of this.
     * @author Richard J. Mathar
     */
    public Rational abs() {
        return (new Rational(a.abs(), b.abs()));
    }

    /**
     * floor(): the nearest integer not greater than this.
     *
     * @return The integer rounded towards negative infinity.
     * @author Richard J. Mathar
     */
    public BigInteger floor() {
        /* is already integer: return the numerator
         */
        if (b.compareTo(BigInteger.ONE) == 0)
            return a;
        else if (a.compareTo(BigInteger.ZERO) > 0)
            return a.divide(b);
        else
            return a.divide(b).subtract(BigInteger.ONE);
    } /* Rational.floor */

    /**
     * ceil(): the nearest integer not smaller than this.
     *
     * @return The integer rounded towards positive infinity.
     * @author Richard J. Mathar
     * @since 2010-05-26
     */
    public BigInteger ceil() {
        /* is already integer: return the numerator
         */
        if (b.compareTo(BigInteger.ONE) == 0)
            return a;
        else if (a.compareTo(BigInteger.ZERO) > 0)
            return a.divide(b).add(BigInteger.ONE);
        else
            return a.divide(b);
    } /* Rational.ceil */

    /**
     * Remove the fractional part.
     *
     * @return The integer rounded towards zero.
     * @author Richard J. Mathar
     */
    public BigInteger trunc() {
        /* is already integer: return the numerator
         */
        if (b.compareTo(BigInteger.ONE) == 0)
            return a;
        else
            return a.divide(b);
    } /* Rational.trunc */

    /**
     * Compares the value of this with another constant.
     *
     * @param val the other constant to compare with
     * @return -1, 0 or 1 if this number is numerically less than, equal to,
     * or greater than val.
     * @author Richard J. Mathar
     */
    @SuppressWarnings("NullableProblems")
    public int compareTo(final Rational val) {
        /* Since we have always kept the denominators positive,
         * simple cross-multiplying works without changing the sign.
         */
        final BigInteger left = a.multiply(val.b);
        final BigInteger right = val.a.multiply(b);
        return left.compareTo(right);
    } /* Rational.compareTo */

    /**
     * Compares the value of this with another constant.
     *
     * @param val the other constant to compare with
     * @return -1, 0 or 1 if this number is numerically less than, equal to,
     * or greater than val.
     * @author Richard J. Mathar
     */
    public int compareTo(final BigInteger val) {
        final Rational val2 = new Rational(val, BigInteger.ONE);
        return (compareTo(val2));
    } /* Rational.compareTo */

    /**
     * Return a string in the format number/denom.
     * If the denominator equals 1, print just the numerator without a slash.
     *
     * @return the human-readable version in base 10
     * @author Richard J. Mathar
     */
    public String toString() {
        if (b.compareTo(BigInteger.ONE) != 0)
            return (a.toString() + "/" + b.toString());
        else
            return a.toString();
    } /* Rational.toString */

    /**
     * Return a double value representation.
     *
     * @return The value with double precision.
     * @author Richard J. Mathar
     * @since 2008-10-26
     */
    public double doubleValue() {
        /* To meet the risk of individual overflows of the exponents of
         * a separate invocation a.doubleValue() or b.doubleValue(), we divide first
         * in a BigDecimal environment and convert the result.
         */
        BigDecimal adivb = (new BigDecimal(a)).divide(new BigDecimal(b), MathContext.DECIMAL128);
        return adivb.doubleValue();
    } /* Rational.doubleValue */

    /**
     * Return a representation as BigDecimal.
     *
     * @param mc the mathematical context which determines precision, rounding mode etc
     * @return A representation as a BigDecimal floating point number.
     * @author Richard J. Mathar
     * @since 2008-10-26
     */
    public BigDecimal BigDecimalValue(MathContext mc) {
        /* numerator and denominator individually rephrased
         */
        BigDecimal n = new BigDecimal(a);
        BigDecimal d = new BigDecimal(b);
        /* the problem with n.divide(d,mc) is that the apparent precision might be
         * smaller than what is set by mc if the value has a precise truncated representation.
         * 1/4 will appear as 0.25, independent of mc
         */
        return BigDecimalMath.scalePrec(n.divide(d, mc), mc);
    } /* Rational.BigDecimalValue */

    /**
     * Compares the value of this with another constant.
     *
     * @param val The other constant to compare with
     * @return The arithmetic maximum of this and val.
     * @author Richard J. Mathar
     * @since 2008-10-19
     */
    public Rational max(final Rational val) {
        if (compareTo(val) > 0)
            return this;
        else
            return val;
    } /* Rational.max */

    /**
     * Compares the value of this with another constant.
     *
     * @param val The other constant to compare with
     * @return The arithmetic minimum of this and val.
     * @author Richard J. Mathar
     * @since 2008-10-19
     */
    public Rational min(final Rational val) {
        if (compareTo(val) < 0)
            return this;
        else
            return val;
    } /* Rational.min */

    /**
     * True if the value is integer.
     * Equivalent to the indication whether a conversion to an integer
     * can be exact.
     *
     * @author Richard J. Mathar
     * @since 2010-05-26
     */
    public boolean isBigInteger() {
        return (b.abs().compareTo(BigInteger.ONE) == 0);
    } /* Rational.isBigInteger */


    /**
     * Normalize to coprime numerator and denominator.
     * Also copy a negative sign of the denominator to the numerator.
     *
     * @author Richard J. Mathar
     * @since 2008-10-19
     */
    protected void normalize() {
        /* compute greatest common divisor of numerator and denominator
         */
        final BigInteger g = a.gcd(b);
        if (g.compareTo(BigInteger.ONE) > 0) {
            a = a.divide(g);
            b = b.divide(g);
        }
        if (b.compareTo(BigInteger.ZERO) < 0) {
            a = a.negate();
            b = b.negate();
        }
    } /* Rational.normalize */
} /* Rational */
