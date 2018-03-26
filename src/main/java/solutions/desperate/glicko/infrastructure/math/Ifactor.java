//https://arxiv.org/abs/0908.3030v3
package solutions.desperate.glicko.infrastructure.math;

import java.math.BigInteger;
import java.util.Vector;

/** Factored integers.
 * This class contains a non-negative integer with the prime factor decomposition attached.
 * @since 2006-08-14
 * @since 2012-02-14 The internal representation contains the bases, and becomes sparser if few
 *    prime factors are present.
 * @author Richard J. Mathar
 */
@SuppressWarnings("SameParameterValue")
public class Ifactor implements Cloneable, Comparable<Ifactor>
{
    /**
     * The standard representation of the number
     */
    public BigInteger n ;

    /*
     * The bases and powers of the prime factorization.
     * representation n = primeexp[0]^primeexp[1]*primeexp[2]^primeexp[3]*...
     * The value 0 is represented by an empty vector, the value 1 by a vector of length 1
     * with a single power of 0.
     */
    private Vector<Integer> primeexp ;

    final public static Ifactor ONE = new Ifactor(1) ;

    private final static Ifactor ZERO = new Ifactor(0) ;

    /** Constructor given an integer.
     * constructor with an ordinary integer
     * @param number the standard representation of the integer
     * @author Richard J. Mathar
     */
    Ifactor(int number)
    {
        n = new BigInteger(""+number) ;
        primeexp = new Vector<>() ;
        if( number > 1 )
        {
            int primindx = 0 ;
            Prime primes = new Prime() ;
            /* Test division against all primes.
             */
            while(number > 1)
            {
                int ex=0 ;
                /* primindx=0 refers to 2, =1 to 3, =2 to 5, =3 to 7 etc
                 */
                int p = primes.at(primindx).intValue() ;
                while( number % p == 0 )
                {
                    ex++ ;
                    number /= p ;
                    if ( number == 1 )
                        break ;
                }
                if ( ex > 0 )
                {
                    primeexp.add(p) ;
                    primeexp.add(ex) ;
                }
                primindx++ ;
            }
        }
        else if ( number == 1)
        {
            primeexp.add(1) ;
            primeexp.add(0) ;
        }
    } /* Ifactor */

    /** Constructor given a BigInteger .
     * Constructor with an ordinary integer, calling a prime factor decomposition.
     * @param number the BigInteger representation of the integer
     * @author Richard J. Mathar
     */
    Ifactor(BigInteger number)
    {
        n = number ;
        primeexp = new Vector<>() ;
        if ( number.compareTo(BigInteger.ONE) == 0 )
        {
            primeexp.add(1) ;
            primeexp.add(0) ;
        }
        else
        {
            int primindx = 0 ;
            Prime primes = new Prime() ;
            /* Test for division against all primes.
             */
            while(number.compareTo(BigInteger.ONE) > 0)
            {
                int ex=0 ;
                BigInteger p = primes.at(primindx) ;
                while( number.remainder(p).compareTo(BigInteger.ZERO) == 0 )
                {
                    ex++ ;
                    number = number.divide(p) ;
                    if ( number.compareTo(BigInteger.ONE) == 0 )
                        break ;
                }
                if ( ex > 0 )
                {
                    primeexp.add(p.intValue()) ;
                    primeexp.add(ex) ;
                }
                primindx++ ;
            }
        }
    } /* Ifactor */

    /** Deep copy.
     * @since 2009-08-14
     * @author Richard J. Mathar
     */
    @SuppressWarnings({"unchecked", "MethodDoesntCallSuperMethod", "unused"})
    public Ifactor clone()
    {
        Vector<Integer> p = (Vector<Integer>)primeexp.clone();
        Ifactor cl = new Ifactor(0) ;
        cl.n = new BigInteger(""+n) ;
        return cl ;
    } /* Ifactor.clone */

    /** Comparison of two numbers.
     * The value of this method is in allowing the Vector.contains() calls that use the value,
     * not the reference for comparison.
     * @param oth the number to compare this with.
     * @return true if both are the same numbers, false otherwise.
     * @author Richard J. Mathar
     */
    public boolean equals(final Ifactor oth)
    {
        return (  n.compareTo(oth.n) == 0 ) ;
    } /* Ifactor.equals */

    /** Multiply with another positive integer.
     * @param oth the second factor.
     * @return the product of both numbers.
     * @author Richard J. Mathar
     */
    private Ifactor multiply(final int oth)
    {
        /* the optimization is to factorize oth _before_ multiplying
         */
        return( multiply(new Ifactor(oth)) ) ;
    } /* Ifactor.multiply */

    /** Multiply with another positive integer.
     * @param oth the second factor.
     * @return the product of both numbers.
     * @author Richard J. Mathar
     */
    public Ifactor multiply(final Ifactor oth)
    {
        /* This might be done similar to the lcm() implementation by adding
         * the powers of the components and calling the constructor with the
         * list of exponents. This here is the simplest implementation, but slow because
         * it calls another prime factorization of the product:
         * return( new Ifactor(n.multiply(oth.n))) ;
         */
        return multGcdLcm(oth,0) ;
    }

    /** Multiply with another positive integer.
     * @param oth the second factor.
     * @param type 0 to multiply, 1 for gcd, 2 for lcm
     * @return the product, gcd or lcm of both numbers.
     * @author Richard J. Mathar
     */
    private Ifactor multGcdLcm(final Ifactor oth, int type)
    {
        Ifactor prod = new Ifactor(0) ;
        /* skip the case where 0*something =0, falling thru to the empty representation for 0
         */
        if( primeexp.size() != 0 && oth.primeexp.size() != 0)
        {
            /* Cases of 1 times something return something.
             * Cases of lcm(1, something) return something.
             * Cases of gcd(1, something) return 1.
             */
            if (primeexp.firstElement() == 1 && type == 0)
                return oth ;
            else if (primeexp.firstElement() == 1 && type == 2)
                return oth ;
            else if (primeexp.firstElement() == 1 && type == 1)
                return this ;
            else if (oth.primeexp.firstElement() == 1 && type ==0)
                return this ;
            else if (oth.primeexp.firstElement() == 1 && type ==2)
                return this ;
            else if (oth.primeexp.firstElement() == 1 && type ==1)
                return oth ;
            else
            {
                int idxThis = 0 ;
                int idxOth = 0 ;
                switch(type)
                {
                    case 0 :
                        prod.n = n.multiply(oth.n) ;
                        break;
                    case 1 :
                        prod.n = n.gcd(oth.n) ;
                        break;
                    case 2 :
                        /* the awkward way, lcm = product divided by gcd
                         */
                        prod.n = n.multiply(oth.n).divide( n.gcd(oth.n) ) ;
                        break;
                }

                /* scan both representations left to right, increasing prime powers
                 */
                while( idxOth < oth.primeexp.size()  || idxThis < primeexp.size() )
                {
                    if ( idxOth >= oth.primeexp.size() )
                    {
                        /* exhausted the list in oth.primeexp; copy over the remaining 'this'
                         * if multiplying or lcm, discard if gcd.
                         */
                        if ( type == 0 || type == 2)
                        {
                            prod.primeexp.add( primeexp.elementAt(idxThis) ) ;
                            prod.primeexp.add( primeexp.elementAt(idxThis+1) ) ;
                        }
                        idxThis += 2 ;
                    }
                    else if ( idxThis >= primeexp.size() )
                    {
                        /* exhausted the list in primeexp; copy over the remaining 'oth'
                         */
                        if ( type == 0 || type == 2)
                        {
                            prod.primeexp.add( oth.primeexp.elementAt(idxOth) ) ;
                            prod.primeexp.add( oth.primeexp.elementAt(idxOth+1) ) ;
                        }
                        idxOth += 2 ;
                    }
                    else
                    {
                        Integer p ;
                        int ex ;
                        switch ( primeexp.elementAt(idxThis).compareTo(oth.primeexp.elementAt(idxOth) ) )
                        {
                            case 0 :
                                /* same prime bases p in both factors */
                                p = primeexp.elementAt(idxThis) ;
                                switch(type)
                                {
                                    case 0 :
                                        /* product means adding exponents */
                                        ex = primeexp.elementAt(idxThis + 1) +
                                                oth.primeexp.elementAt(idxOth + 1);
                                        break;
                                    case 1 :
                                        /* gcd means minimum of exponents */
                                        ex = Math.min(primeexp.elementAt(idxThis + 1),
                                                oth.primeexp.elementAt(idxOth + 1)) ;
                                        break;
                                    default :
                                        /* lcm means maximum of exponents */
                                        ex = Math.max(primeexp.elementAt(idxThis + 1),
                                                oth.primeexp.elementAt(idxOth + 1)) ;
                                        break;
                                }
                                prod.primeexp.add( p ) ;
                                prod.primeexp.add(ex) ;
                                idxOth += 2 ;
                                idxThis += 2 ;
                                break ;
                            case 1:
                                /* this prime base bigger than the other and taken later */
                                if ( type == 0 || type == 2)
                                {
                                    prod.primeexp.add( oth.primeexp.elementAt(idxOth) ) ;
                                    prod.primeexp.add( oth.primeexp.elementAt(idxOth+1) ) ;
                                }
                                idxOth += 2 ;
                                break ;
                            default:
                                /* this prime base smaller than the other and taken now */
                                if ( type == 0 || type == 2)
                                {
                                    prod.primeexp.add( primeexp.elementAt(idxThis) ) ;
                                    prod.primeexp.add( primeexp.elementAt(idxThis+1) ) ;
                                }
                                idxThis += 2 ;
                        }
                    }
                }
            }
        }
        return prod ;
    } /* Ifactor.multGcdLcm */

    /** Integer division through  another positive integer.
     * @param oth the denominator.
     * @return the division of this through the oth, discarding the remainder.
     * @author Richard J. Mathar
     */
    public Ifactor divide(final Ifactor oth)
    {
        /*
         * representation, which would avoid a more strenous factorization of the integer ratio
         */
        return  new Ifactor(n.divide(oth.n)) ;
    } /* Ifactor.divide */

    /** Summation with another positive integer
     * @param oth the other term.
     * @return the sum of both numbers
     * @author Richard J. Mathar
     */
    public Ifactor add(final BigInteger oth)
    {
        /* avoid refactorization if oth is zero...
         */
        if ( oth.compareTo(BigInteger.ZERO) != 0 )
            return  new Ifactor(n.add(oth)) ;
        else
            return this ;
    } /* Ifactor.add */

    /** Exponentiation with a positive integer.
     * @param exponent the non-negative exponent
     * @return n^exponent. If exponent=0, the result is 1.
     * @author Richard J. Mathar
     */
    public Ifactor pow(final int exponent) throws ArithmeticException
    {
        /* three simple cases first
         */
        if ( exponent < 0 )
            throw new ArithmeticException("Cannot raise "+ toString() + " to negative " + exponent) ;
        else if ( exponent == 0)
            return new Ifactor(1) ;
        else if ( exponent == 1)
            return this ;

        /* general case, the vector with the prime factor powers, which are component-wise
         * exponentiation of the individual prime factor powers.
         */
        Ifactor pows = new Ifactor(0) ;
        for(int i=0 ; i < primeexp.size() ; i += 2)
        {
            Integer p = primeexp.elementAt(i) ;
            int ex = primeexp.elementAt(i + 1);
            pows.primeexp.add( p ) ;
            pows.primeexp.add(ex * exponent) ;
        }
        return pows ;
    } /* Ifactor.pow */

    /** Pulling the r-th root.
     * @param r the positive or negative (nonzero) root.
     * @return n^(1/r).
     *   The return value falls into the Ifactor class if r is positive, but if r is negative
     *   a Rational type is needed.
     * @since 2009-05-18
     * @author Richard J. Mathar
     */
    public Rational root(final int r) throws ArithmeticException
    {
        if ( r == 0 )
            throw new ArithmeticException("Cannot pull zeroth root of "+ toString()) ;
        else if ( r < 0 )
        {
            /* a^(-1/b)= 1/(a^(1/b))
             */
            final Rational invRoot = root(-r) ;
            return Rational.ONE.divide(invRoot) ;
        }
        else
        {
            BigInteger pows = BigInteger.ONE ;
            for(int i=0 ; i < primeexp.size() ; i += 2)
            {
                /* all exponents must be multiples of r to succeed (that is, to
                 * stay in the range of rational results).
                 */
                int ex = primeexp.elementAt(i + 1);
                if ( ex % r != 0 )
                    throw new ArithmeticException("Cannot pull "+ r+"th root of "+ toString()) ;

                //
            }
            /* convert result to a Rational; unfortunately this will loose the prime factorization */
            return new Rational(pows) ;
        }
    } /* Ifactor.root */


    /** Sum of the divisors of the number.
     * @return the sum of all divisors of the number, 1+....+n.
     * @author Richard J. Mathar
     */
    public Ifactor sigma()
    {
        return sigma(1) ;
    } /* Ifactor.sigma */

    /** Sum of the k-th powers of divisors of the number.
     * @param k The exponent of the powers.
     * @return the sum of all divisors of the number, 1^k+....+n^k.
     * @author Richard J. Mathar
     */
    public Ifactor sigma(int k)
    {
        /* the question is whether keeping a factorization  is worth the effort
         * or whether one should simply multiply these to return a BigInteger...
         */
        if( n.compareTo(BigInteger.ONE) == 0 )
            return  ONE ;
        else if( n.compareTo(BigInteger.ZERO) == 0 )
            return  ZERO ;
        else
        {
            /* multiplicative: sigma_k(p^e) = [p^(k*(e+1))-1]/[p^k-1]
             * sigma_0(p^e) = e+1.
             */
            Ifactor resul = Ifactor.ONE ;
            for(int i=0 ; i < primeexp.size() ; i += 2)
            {
                int ex = primeexp.elementAt(i + 1);
                if ( k == 0 )
                    resul = resul.multiply(ex+1) ;
                else
                {
                    Integer p = primeexp.elementAt(i) ;
                    BigInteger num = (new BigInteger(p.toString())).pow(k*(ex+1)).subtract(BigInteger.ONE) ;
                    BigInteger deno = (new BigInteger(p.toString())).pow(k).subtract(BigInteger.ONE) ;
                    /* This division is of course exact, no remainder
                     * The costly prime factorization is hidden here.
                     */
                    Ifactor f = new Ifactor(num.divide(deno)) ;
                    resul = resul.multiply(f) ;
                }
            }
            return resul ;
        }
    } /* Ifactor.sigma */

    /** The sum of the prime factor exponents, without multiplicity.
     * @return the number of distinct prime factors.
     * @since 2008-10-16
     * @author Richard J. Mathar
     */
    public int omega()
    {
        return primeexp.size()/2 ;
    } /* Ifactor.omega */

    /** The square-free part.
     * @return the minimum m such that m times this number is a square.
     * @since 2008-10-16
     * @author Richard J. Mathar
     */
    public BigInteger core()
    {
        BigInteger resul = BigInteger.ONE ;
        for(int i=0 ; i < primeexp.size() ; i += 2)
            if (primeexp.elementAt(i + 1) % 2 != 0)
                resul = resul.multiply( new BigInteger(primeexp.elementAt(i).toString()) );
        return resul ;
    } /* Ifactor.core */

    /** Maximum of two values.
     * @param oth the number to compare this with.
     * @return the larger of the two values.
     * @author Richard J. Mathar
     */
    public Ifactor max(final Ifactor oth)
    {
        if( n.compareTo(oth.n) >= 0 )
            return this ;
        else
            return oth  ;
    } /* Ifactor.max */

    /** Minimum of two values.
     * @param oth the number to compare this with.
     * @return the smaller of the two values.
     * @author Richard J. Mathar
     */
    public Ifactor min(final Ifactor oth)
    {
        if( n.compareTo(oth.n) <= 0 )
            return this ;
        else
            return oth ;
    } /* Ifactor.min */

    /** Maximum of a list of values.
     * @param set list of numbers.
     * @return the largest in the list.
     * @author Richard J. Mathar
     */
    public static Ifactor max(final Vector<Ifactor> set)
    {
        Ifactor resul = set.elementAt(0) ;
        for(int i=1; i < set.size() ; i++)
            resul = resul.max(set.elementAt(i)) ;
        return resul ;
    } /* Ifactor.max */

    /** Minimum of a list of values.
     * @param set list of numbers.
     * @return the smallest in the list.
     * @author Richard J. Mathar
     */
    public static Ifactor min(final Vector<Ifactor> set)
    {
        Ifactor resul = set.elementAt(0) ;
        for(int i=1; i < set.size() ; i++)
            resul = resul.min(set.elementAt(i)) ;
        return resul ;
    } /* Ifactor.min */

    /** Compare value against another Ifactor
     * @param oth The value to be compared agains.
     * @return 1, 0 or -1 according to being larger, equal to or smaller than oth.
     * @since 2012-02-15
     * @author Richard J. Mathar
     */
    @SuppressWarnings("NullableProblems")
    public int compareTo(final Ifactor oth)
    {
        return n.compareTo(oth.n) ;
    } /* compareTo */

} /* Ifactor */
