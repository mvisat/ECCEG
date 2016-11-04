import java.math.BigInteger;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        BigInteger
            a = new BigInteger("1"),
            b = new BigInteger("1"),
            p = new BigInteger("2000003");
        ECC ecc = new ECC(a, b, p);
        ECCEG ecceg = new ECCEG(ecc, ecc.getBasePoint());

        System.out.print("Private key: ");
        System.out.println(ecceg.getPrivateKey());
        System.out.print("Public key: ");
        System.out.println(ecceg.getPublicKey());
        ecceg.savePrivateKey("key.pri");
        System.out.println("Private key saved to key.pri");
        ecceg.savePublicKey("key.pub");
        System.out.println("Public key saved to key.pub");

        FileReader fr = new FileReader();
        byte[] read = fr.fileToBytes("tes.txt");
        System.out.print("Plain text: ");
        System.out.println(new String(read));
        List<Pair<Point,Point>> enc = ecceg.encryptBytes(read);
        System.out.print("Cipher text: ");
        for (Pair<Point,Point> pp: enc) {
            System.out.print(String.format("%02x%02x%02x%02x",
                pp.left.x.intValue(),
                pp.left.y.intValue(),
                pp.right.x.intValue(),
                pp.right.y.intValue()));
        }
        System.out.println();
        List<Point> dec = ecceg.decrypt(enc);
        for (Point pp: dec) System.out.print((char)ecc.pointToInt(pp).byteValue());
        System.out.println();
        fr.savePointsToFile("tes.txt.enc", enc);

        ECCEG ecceg2 = new ECCEG(ecc, ecc.getBasePoint());
        System.out.println("Load private key and public key");
        ecceg2.loadPrivateKey("key.pri");
        ecceg2.loadPublicKey("key.pub");
        List<Pair<Point,Point>> read_enc = fr.loadPointsFromFile("tes.txt.enc");
        List<Point> read_dec = ecceg2.decrypt(read_enc);
        for (Point pp: read_dec) System.out.print((char)ecc.pointToInt(pp).byteValue());
        System.out.println();
    }
}