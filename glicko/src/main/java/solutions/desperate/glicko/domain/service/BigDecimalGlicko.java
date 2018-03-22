package solutions.desperate.glicko.domain.service;

import solutions.desperate.glicko.domain.model.Player;

import java.math.BigDecimal;
import java.math.MathContext;

import static java.math.BigDecimal.ONE;
import static solutions.desperate.glicko.infrastructure.BigDecimalMath.exp;
import static solutions.desperate.glicko.infrastructure.BigDecimalMath.sqrt;

public class BigDecimalGlicko {
    private final static BigDecimal DEFAULT_RATING = BigDecimal.valueOf(1500D);
    private final static BigDecimal DEFAULT_RD = BigDecimal.valueOf(200D);
    private final static BigDecimal DEFAULT_VOLATILITY = BigDecimal.valueOf(0.06D);
    private final static BigDecimal DEFAULT_TAU = BigDecimal.valueOf(0.5D);
    private final static BigDecimal SCALE = BigDecimal.valueOf(173.7178D);
    private final static BigDecimal EPSILON = BigDecimal.valueOf(0.000001D);
    private final static MathContext PRECISION = MathContext.DECIMAL64;
    private final static BigDecimal PI = new BigDecimal("3.1415926535897932384626433832795028841971693993751058209749445923");
    private final static BigDecimal TWO = BigDecimal.valueOf(2);
    private final static BigDecimal THREE = BigDecimal.valueOf(3);

    //Step 2
    private static BigDecimal convertRatingToGlicko2(BigDecimal rating) {
        return (rating.subtract(DEFAULT_RATING)).divide(SCALE, PRECISION);
    }

    private static BigDecimal convertRdToGlicko2(BigDecimal rd) {
        return rd.divide(SCALE, PRECISION);
    }

    //Step 3
    private static BigDecimal volatilityG(BigDecimal rd) {
        return ONE.divide(sqrt(ONE.add(THREE.multiply(rd.pow(2)).divide(PI.pow(2), PRECISION)), 64), PRECISION);
    }

    private static BigDecimal volatilityE(BigDecimal rating1, BigDecimal rating2, BigDecimal g) {
        return ONE.divide(ONE.add(exp(g.negate().multiply(rating1.subtract(rating2)))), PRECISION);
    }

    private static BigDecimal variance(BigDecimal e, BigDecimal g) {
        return ONE.divide(g.pow(2).multiply(e.multiply(ONE.subtract(e))), PRECISION);
    }

    //Step 4
    public static BigDecimal delta(BigDecimal e, BigDecimal g, BigDecimal variance, int result) {
        return variance.multiply(g).multiply(BigDecimal.valueOf(result).subtract(e));
    }

    //Step 5
    /*private static double f(double x, double delta, double rd, double v, double a) {
        return (Math.exp(x) * (Math.pow(delta, 2) - Math.pow(rd, 2) - v - Math.exp(x)) /
                (2.0 * Math.pow(Math.pow(rd, 2) + v + Math.exp(x), 2))) - ((x - a) / Math.pow(DEFAULT_TAU, 2));
    }

    private static double volatilityMarked(double delta, double rd, double volatility, double v) {
        double a = Math.log(Math.pow(volatility, 2));

        double A = a;
        double B;
        if (Math.pow(delta, 2) > Math.pow(rd, 2) + v) {
            B = Math.log(Math.pow(delta, 2) - Math.pow(rd, 2) - v);
        } else {
            double k = 1;
            B = a - (k * Math.abs(DEFAULT_TAU));

            while (f(B, delta, rd, v, a) < 0) {
                k++;
                B = a - (k * Math.abs(DEFAULT_TAU));
            }
        }

        double fA = f(A, delta, rd, v, a);
        double fB = f(B, delta, rd, v, a);

        while (Math.abs(B - A) > EPSILON) {
            double C = A + (((A - B) * fA) / (fB - fA));
            double fC = f(C, delta, rd, v, a);

            if (fC * fB < 0) {
                A = B;
                fA = fB;
            } else {
                fA = fA / 2.0;
            }

            B = C;
            fB = fC;
        }

        return Math.exp(A / 2.0);
    }*/

    //Step 6
    private static BigDecimal preRatingRd(BigDecimal rd, BigDecimal rdMarked) {
        return sqrt(rd.pow(2).add(rdMarked.pow(2)), 64);
    }

    //Step 7
    private static BigDecimal rdMarked(BigDecimal rdStarred, BigDecimal v) {
        return ONE.divide(sqrt(ONE.divide(rdStarred.pow(2).add(ONE.divide(v, PRECISION)), PRECISION), 64), PRECISION);
    }

    private static BigDecimal ratingMarked(BigDecimal rating, BigDecimal rdMarked, BigDecimal g, int result, BigDecimal e) {
        return rating.add(rdMarked.pow(2).multiply(g).multiply(BigDecimal.valueOf(result).subtract(e)));
    }

    public Player noGamesRd(Player player) {
        //TODO FIX ME
        BigDecimal rd = BigDecimal.valueOf(player.rd());
        BigDecimal volatility = BigDecimal.valueOf(player.volatility());
        return new Player(player.name(), player.rating(), SCALE.multiply(preRatingRd(convertRdToGlicko2(rd), volatility)).doubleValue(), player.volatility());
    }

    public Player defaultPlayer(String name) {
        return null;
    }

    public Player glicko2(Player player1, Player player2, int result) {
        return null;
    }
}
