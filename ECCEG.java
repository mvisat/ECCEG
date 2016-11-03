import java.math.BigInteger;
import java.util.*;

public class ECCEG {
    private Point publicKey;
    private BigInteger privateKey;
    private Point basePoint;
    private ECC ECC;

    public ECCEG(ECC ECC, Point basePoint) {
        this.ECC = ECC;
        this.basePoint = basePoint;
        this.privateKey = new BigInteger(ECC.p.bitLength(), new Random())
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

    public Pair<Point, Point> encrypt(Point p) {
        BigInteger k = new BigInteger(ECC.p.bitLength(), new Random())
            .mod(ECC.p.subtract(BigInteger.ONE))
            .add(BigInteger.ONE);
        Point left = ECC.multiply(k, basePoint);
        Point right = ECC.add(p, ECC.multiply(k, publicKey));
        return new Pair<Point, Point>(left, right);
    }

    public List<Pair<Point, Point>> encryptBytes(byte[] bytes) {
        List<Pair<Point, Point>> ret = new ArrayList<>();
        for (int i = 0; i < bytes.length; ++i)
            ret.add(encrypt(ECC.intToPoint(BigInteger.valueOf(bytes[i]))));
        return ret;
    }

    public Point decrypt(Pair<Point, Point> p) {
        Point m = ECC.multiply(privateKey, p.left);
        return ECC.subtract(p.right, m);
    }

    public List<Point> decrypt(List<Pair<Point, Point>> l) {
        List<Point> ret = new ArrayList<>();
        for (Pair<Point, Point> p: l)
            ret.add(decrypt(p));
        return ret;
    }
}
