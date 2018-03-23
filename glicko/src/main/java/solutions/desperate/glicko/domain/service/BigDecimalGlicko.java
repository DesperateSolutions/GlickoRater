package solutions.desperate.glicko.domain.service;

import solutions.desperate.glicko.domain.model.Player;
import solutions.desperate.glicko.domain.service.glicko.Glicko;

import java.math.BigDecimal;
import java.math.MathContext;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static solutions.desperate.glicko.infrastructure.math.BigDecimalMath.*;

public class BigDecimalGlicko implements Glicko {
    private final static BigDecimal DEFAULT_RATING = BigDecimal.valueOf(1500D);
    private final static BigDecimal DEFAULT_RD = BigDecimal.valueOf(200D);
    private final static BigDecimal DEFAULT_VOLATILITY = BigDecimal.valueOf(0.06D);
    private final static BigDecimal DEFAULT_TAU = BigDecimal.valueOf(0.5D);
    private final static BigDecimal SCALE = BigDecimal.valueOf(173.7178D);
    private final static BigDecimal EPSILON = BigDecimal.valueOf(0.000001D);
    private final static MathContext PRECISION = MathContext.DECIMAL64;
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
        return ONE.divide(sqrt(ONE.add(THREE.multiply(rd.pow(2)).divide(PI.pow(2), PRECISION)), PRECISION), PRECISION);
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

    private static BigDecimal f(BigDecimal x, BigDecimal delta, BigDecimal rd, BigDecimal v, BigDecimal a) {
        return (exp(x).multiply(delta.pow(2).subtract(rd.pow(2).subtract(v).subtract(exp(x))))
                .divide(TWO.multiply(rd.pow(2).add(v).add(exp(x)).pow(2)), PRECISION)).subtract(x.subtract(a).divide(DEFAULT_TAU.pow(2), PRECISION));
    }

    //Step 5

    private static BigDecimal volatilityMarked(BigDecimal delta, BigDecimal rd, BigDecimal volatility, BigDecimal v) {
        BigDecimal a = log(volatility.pow(2));

        //BigDecimal is immutable so this is safe
        BigDecimal A = a;
        BigDecimal B;
        if (delta.pow(2).compareTo(rd.pow(2).add(v)) > 0) {
            B = log(delta.pow(2).subtract(rd.pow(2).subtract(v)));
        } else {
            BigDecimal k = ONE;
            B = a.subtract(k.multiply(DEFAULT_TAU.abs()));

            while (f(B, delta, rd, v, a).compareTo(ZERO) < 0) {
                k = k.add(ONE);
                B = a.subtract(k.multiply(DEFAULT_TAU.abs()));
            }
        }

        BigDecimal fA = f(A, delta, rd, v, a);
        BigDecimal fB = f(B, delta, rd, v, a);

        while (B.subtract(A).abs().compareTo(EPSILON) > 0) {
            BigDecimal C = A.add((A.subtract(B).multiply(fA)).divide(fB.subtract(fA), PRECISION));
            BigDecimal fC = f(C, delta, rd, v, a);

            if (fC.multiply(fB).compareTo(ZERO) < 0) {
                A = B;
                fA = fB;
            } else {
                fA = fA.divide(TWO, PRECISION);
            }

            B = C;
            fB = fC;
        }

        return exp(A.divide(TWO, PRECISION));
    }

    //Step 6
    private static BigDecimal preRatingRd(BigDecimal rd, BigDecimal rdMarked) {
        return sqrt(rd.pow(2).add(rdMarked.pow(2)), PRECISION);
    }

    //Step 7
    private static BigDecimal rdMarked(BigDecimal rdStarred, BigDecimal v) {
        return ONE.divide(sqrt(ONE.divide(rdStarred.pow(2), PRECISION).add(ONE.divide(v, PRECISION)), PRECISION), PRECISION);
    }

    private static BigDecimal ratingMarked(BigDecimal rating, BigDecimal rdMarked, BigDecimal g, int result, BigDecimal e) {
        return rating.add(rdMarked.pow(2).multiply(g).multiply(BigDecimal.valueOf(result).subtract(e)));
    }

    public Player noGamesRd(Player player) {
        BigDecimal rd = player.rd();
        BigDecimal volatility = player.volatility();
        return new Player(player.name(), player.rating(), SCALE.multiply(preRatingRd(convertRdToGlicko2(rd), volatility)), player.volatility());
    }

    public Player defaultPlayer(String name) {
        return new Player(name, DEFAULT_RATING, DEFAULT_RD, DEFAULT_VOLATILITY);
    }

    public Player glicko2(Player player1, Player player2, int result) {
        //Step 2
        BigDecimal player1Rating = convertRatingToGlicko2(player1.rating());
        BigDecimal player1Rd = convertRdToGlicko2(player1.rd());
        BigDecimal player2Rating = convertRatingToGlicko2(player2.rating());
        BigDecimal player2Rd = convertRdToGlicko2(player2.rd());
        //Step 3
        BigDecimal g = volatilityG(player2Rd);
        BigDecimal e = volatilityE(player1Rating, player2Rating, g);
        BigDecimal variance = variance(e, g);
        //Step 4
        BigDecimal delta = delta(e, g, variance, result);
        //Step 5
        BigDecimal volatilityMarked = volatilityMarked(delta, player1Rd, player1.volatility(), variance);
        //Step 6
        BigDecimal rdStarred = preRatingRd(player1Rd, volatilityMarked);
        //Step 7
        BigDecimal rdMarked = rdMarked(rdStarred, variance);
        BigDecimal ratingMarked = ratingMarked(player1Rating, rdMarked, g, result, e);
        //Step 8
        return new Player(player1.name(), DEFAULT_RATING.add(SCALE.multiply(ratingMarked)), SCALE.multiply(rdMarked), volatilityMarked);
    }
}
