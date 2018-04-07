package solutions.desperate.glicko.domain.service.glicko;

import solutions.desperate.glicko.domain.model.Player;

import java.math.BigDecimal;

public class DoubleGlicko implements Glicko {
    private final static double DEFAULT_RATING = 1500D;
    private final static double DEFAULT_RD = 200D;
    private final static double DEFAULT_VOLATILITY = 0.06D;
    private final static double DEFAULT_TAU = 0.5D;
    private final static double SCALE = 173.7178D;
    private final static double EPSILON = 0.000001D;

    //Step 2
    private static double convertRatingToGlicko2(double rating) {
        return (rating - DEFAULT_RATING) / SCALE;
    }


    private static double convertRdToGlicko2(double rd) {
        return rd / SCALE;
    }


    //Step 3
    private static double volatilityG(double rd) {
        return 1 / Math.sqrt(1 + ((3 * Math.pow(rd, 2)) / Math.pow(Math.PI, 2)));
    }

    private static double volatilityE(double rating1, double rating2, double g) {
        return 1 / (1 + Math.exp((-1 * g) * (rating1 - rating2)));
    }

    private static double variance(double e, double g) {
        return 1 / (Math.pow(g, 2) * e * (1 - e));
    }

    //Step 4
    private static double delta(double e, double g, double variance, double result) {
        return variance * g * (result - e);
    }

    //Step 5
    private static double f(double x, double delta, double rd, double v, double a) {
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

        //System.out.println(fB);

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
    }

    //Step 6
    private static double preRatingRd(double rd, double rdMarked) {
        return Math.sqrt(Math.pow(rd, 2) + Math.pow(rdMarked, 2));
    }

    //Step 7
    private static double rdMarked(double rdStarred, double v) {
        return 1 / Math.sqrt((1 / Math.pow(rdStarred, 2)) + (1 / v));
    }

    private static double ratingMarked(double rating, double rdMarked, double g, double result, double e) {
        return rating + (Math.pow(rdMarked, 2) * g * (result - e));
    }

    public Player defaultPlayer(String name) {
        return new Player(name,
                          String.valueOf(DEFAULT_RATING),
                          String.valueOf(DEFAULT_RD),
                          String.valueOf(DEFAULT_VOLATILITY));
    }

    public Player noGamesRd(Player player) {
        return new Player(player.name(),
                          player.rating(),
                          BigDecimal.valueOf(SCALE * preRatingRd(convertRdToGlicko2(Double.parseDouble(player.rd())), Double.parseDouble(player.volatility()))).toPlainString(),
                          player.volatility());
    }

    public Player glicko2(Player player1, Player player2, double result) {
        //Step 2
        double player1Rating = convertRatingToGlicko2(Double.parseDouble(player1.rating()));
        double player1Rd = convertRdToGlicko2(Double.parseDouble(player1.rd()));
        double player2Rating = convertRatingToGlicko2(Double.parseDouble(player2.rating()));
        double player2Rd = convertRdToGlicko2(Double.parseDouble(player2.rd()));
        //Step 3
        double g = volatilityG(player2Rd);
        double e = volatilityE(player1Rating, player2Rating, g);
        double variance = variance(e, g);
        //Step 4
        double delta = delta(e, g, variance, result);
        //Step 5
        double volatilityMarked = volatilityMarked(delta, player1Rd, Double.parseDouble(player1.volatility()), variance);
        //Step 6
        double rdStarred = preRatingRd(player1Rd, volatilityMarked);
        //Step 7
        double rdMarked = rdMarked(rdStarred, variance);
        double ratingMarked = ratingMarked(player1Rating, rdMarked, g, result, e);
        //Step 8
        return new Player(player1.id(),
                          player1.name(),
                          String.valueOf(DEFAULT_RATING + (SCALE * ratingMarked)),
                          String.valueOf(BigDecimal.valueOf(SCALE * rdMarked)),
                          String.valueOf(BigDecimal.valueOf(volatilityMarked)),
                          player1.games());
    }
}
