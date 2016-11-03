import java.math.BigInteger;
import java.util.Random;

public class ECCEG {
    private Point publicKey;
    private BigInteger privateKey;
    private Point basePoint;
    private ECC ECC;
    public static final int BIT_LENGTH = 256;

    public ECCEG(ECC ECC, Point basePoint) {
        this.ECC = ECC;
        this.basePoint = basePoint;
        this.privateKey = new BigInteger(BIT_LENGTH, new Random())
            .mod(ECC.p.subtract(BigInteger.ONE))
            .add(BigInteger.ONE);
        this.publicKey = ECC.multiply(privateKey, basePoint);
    }

    public ECCEG(ECC ECC, Point basePoint, BigInteger privateKey) {
        this.ECC = ECC;
        this.basePoint = basePoint;
        this.privateKey = privateKey;
        this.publicKey = ECC.multiply(privateKey, basePoint);
    }

    public Point getPublicKey() { return this.publicKey; }
    public BigInteger getPrivateKey() { return this.privateKey; }
    public ECC getECC() { return this.ECC; }
    public Point getBasePoint() { return this.basePoint; }

    public Pair<Point, Point> encrypt(BigInteger x) {
        Point p = ECC.intToPoint(x);
        BigInteger k = new BigInteger(BIT_LENGTH, new Random())
            .mod(ECC.p.subtract(BigInteger.ONE))
            .add(BigInteger.ONE);
        Point left = ECC.multiply(k, basePoint);
        Point right = ECC.add(p, ECC.multiply(k, publicKey));
        return new Pair<Point, Point>(left, right);
    }

    public BigInteger decrypt(Pair<Point, Point> p) {
        Point m = ECC.multiply(ECC.b, p.left);
        Point d = ECC.subtract(p.right, m);
        return ECC.pointToInt(d);
    }
}
