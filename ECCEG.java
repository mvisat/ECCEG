import java.math.BigInteger;
import java.util.Random;

public class ECCEG {
    private Point publicKey;
    private BigInteger privateKey;
    private BigInteger a, b, p;
    private Point basePoint;
    private ECC ECC;
    public static final int BIT_LENGTH = 256;

    public ECCEG(BigInteger a, BigInteger b, BigInteger p, Point basePoint) {
        this.a = a;
        this.b = b;
        this.p = p;
        this.basePoint = basePoint;
        this.ECC = new ECC(a, b, p);
        this.privateKey = new BigInteger(BIT_LENGTH, new Random())
            .mod(p.subtract(BigInteger.ONE))
            .add(BigInteger.ONE);
        this.publicKey = ECC.multiply(privateKey, basePoint);
    }

    public Point getPublicKey() { return this.publicKey; }
    public BigInteger getPrivateKey() { return this.privateKey; }
    public BigInteger getA() { return this.a; }
    public BigInteger getB() { return this.b; }
    public BigInteger getP() { return this.p; }
    public Point getBasePoint() { return this.basePoint; }

    public Pair<Point, Point> encrypt(Point publicKey, BigInteger x) {
        Point p = ECC.intToPoint(x);
        BigInteger k = new BigInteger(BIT_LENGTH, new Random())
            .mod(this.p.subtract(BigInteger.ONE))
            .add(BigInteger.ONE);
        Point left = ECC.multiply(k, basePoint);
        Point right = ECC.add(p, ECC.multiply(k, publicKey));
        return new Pair<Point, Point>(left, right);
    }

    public BigInteger decrypt(Pair<Point, Point> p) {
        Point m = ECC.multiply(b, p.left);
        Point d = ECC.subtract(p.right, m);
        return ECC.pointToInt(d);
    }
}
