import java.math.BigInteger;

public class ECC {
    public final BigInteger a, b, p;
    private BigInteger k = BigInteger.valueOf(30);
    private static BigInteger
        MINUS_ONE = BigInteger.valueOf(-1),
        ZERO = BigInteger.valueOf(0),
        ONE = BigInteger.valueOf(1),
        TWO = BigInteger.valueOf(2),
        THREE = BigInteger.valueOf(3);

    public ECC(BigInteger a, BigInteger b, BigInteger p) {
        this.a = a;
        this.b = b;
        this.p = p;
    }

    public Point add(Point p, Point q) {
        if (p.x.compareTo(q.x) == 0 && p.y.compareTo(q.y) == 0)
            return doubles(p);
        else if (p.infinite && q.infinite)
            return new Point(true);
        else if (p.infinite)
            return q;
        else if (q.infinite)
            return p;
        else if (p.x.compareTo(q.x) == 0)
            return new Point(true);

        BigInteger gradient = q.y.subtract(p.y).multiply(q.x.subtract(p.x).mod(this.p).modInverse(this.p)).mod(this.p);
        BigInteger x = gradient.multiply(gradient).subtract(p.x).subtract(q.x).mod(this.p);
        BigInteger y = gradient.multiply(p.x.subtract(x)).subtract(p.y).add(this.p).mod(this.p);
        return new Point(x, y);
    }

    public Point subtract(Point p, Point q) {
        Point minusQ = new Point(q.x, q.y.negate().mod(this.p));
        return add(p, minusQ);
    }

    public Point doubles(Point a) {
        BigInteger gradient = a.x.multiply(a.x).multiply(THREE).add(this.a).multiply(a.y.multiply(TWO).modInverse(this.p)).mod(this.p);
        BigInteger x = gradient.multiply(gradient).subtract(a.x.multiply(TWO)).mod(this.p);
        BigInteger y = gradient.multiply(a.x.subtract(x)).subtract(a.y).mod(this.p);
        return new Point(x, y);
    }

    public Point multiply(BigInteger n, Point p) {
        if (n.equals(ZERO))
            return new Point(ZERO, ZERO);
        else if (n.equals(ONE))
            return p;
        else if (n.mod(TWO).equals(ZERO))
            return multiply(n.divide(TWO), doubles(p));
        else
            return add(multiply(n.subtract(ONE), p), p);
    }

    private BigInteger solveY(BigInteger x) {
        return SquareRootModulo.sqrtP(x.multiply(x).multiply(x).add(this.a.multiply(x)).add(this.b).mod(this.p), this.p);
    }

    public Point intToPoint(BigInteger m) {
        BigInteger mk = m.multiply(k);
        for (BigInteger i = ONE; i.compareTo(k) < 0; i = i.add(ONE)) {
            BigInteger x = mk.add(i);
            BigInteger y = solveY(x);
            if (y != null)
                return new Point (x, y);
        }
        return new Point(BigInteger.valueOf(-1), BigInteger.valueOf(-1));
    }

    public BigInteger pointToInt(Point p) {
        return p.x.subtract(ONE).divide(this.k);
    }

    // Helper class for square root mod
    private static class SquareRootModulo {
        public static BigInteger sqrtP(BigInteger x, BigInteger p) {
            if (p.mod(TWO).equals(ZERO))
                return null;
            BigInteger q = p.subtract(ONE).divide(TWO);
            if (x.modPow(q, p).equals(ONE))
                return null;

            while (q.mod(TWO).equals(ZERO)) {
                q = q.divide(TWO);
                if (x.modPow(q, p).equals(ONE))
                    return complexSqrtP(x, q, p);
            }
            q = q.add(ONE).divide(TWO);
            return x.modPow(q, p);
        }

        private static BigInteger complexSqrtP(BigInteger x, BigInteger q, BigInteger p) {
            BigInteger a = findNonResidue(p);
            if (a == null) return null;
            BigInteger t = (p.subtract(ONE)).divide(TWO);
            BigInteger negativePower = t;

            while (q.mod(TWO).equals(ZERO)) {
                q = q.divide(TWO);
                t = t.divide(TWO);
                if (x.modPow(q, p).compareTo(a.modPow(t, p)) != 0)
                    t = t.add(negativePower);
            }
            BigInteger inverse = x.modInverse(p);
            BigInteger partOne = inverse.modPow(q.subtract(ONE).divide(TWO), p);
            BigInteger partTwo = a.modPow(t.divide(TWO), p);
            return partOne.multiply(partTwo).mod(p);
        }

        private static BigInteger findNonResidue(BigInteger p) {
            BigInteger a = BigInteger.valueOf(2);
            BigInteger q = p.subtract(ONE).divide(TWO);
            while (true) {
                if (a.modPow(q, p).equals(ONE))
                    return a;
                a = a.add(ONE);
            }
        }
    }
}
