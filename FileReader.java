import java.util.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.math.BigInteger;

public class FileReader {

    public static byte[] fileToBytes(String path) {
        try {
            return Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            return new byte[0];
        }
    }

    public static void saveFile(String stringpath, byte[] content) {
        try {
            FileOutputStream fos = new FileOutputStream(stringpath);
            fos.write(content);
            fos.close();
        } catch (IOException e) {}
    }

    public byte[] intToBytes(int x) {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putInt(x);
        return buffer.array();
    }

    public int bytesToInt(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.put(bytes);
        buffer.flip();//need flip
        return buffer.getInt();
    }

    public void savePointsToFile(String path, List<Pair<Point,Point>> pairpoints) {
        byte[] b = new byte[pairpoints.size() * 16];
        int j = 0;
        for (Pair<Point,Point> ppoint : pairpoints) {
            byte[] btemp = intToBytes(ppoint.left.x.intValue());
            for (int i = 0; i < btemp.length; i++) {
                b[j] = btemp[i];
                j++;
            }
            btemp = intToBytes(ppoint.left.y.intValue());
            for (int i = 0; i < btemp.length; i++) {
                b[j] = btemp[i];
                j++;
            }
            btemp = intToBytes(ppoint.right.x.intValue());
            for (int i = 0; i < btemp.length; i++) {
                b[j] = btemp[i];
                j++;
            }
            btemp = intToBytes(ppoint.right.y.intValue());
            for (int i = 0; i < btemp.length; i++) {
                b[j] = btemp[i];
                j++;
            }
        }
        saveFile(path, b);
    }

    public List<Pair<Point,Point>> loadPointsFromFile(String stringpath) {
        Path path = Paths.get(stringpath);
        byte[] rawData = new byte[0];
        try {
            rawData = Files.readAllBytes(path);
        } catch (IOException e) {}
        List<Pair<Point,Point>> pair = new ArrayList<>();
        byte[] btemp = new byte[4];
        int f = 0, s;
        Point point1 = new Point(BigInteger.valueOf(1), BigInteger.valueOf(1));
        Point point2 = new Point(BigInteger.valueOf(1), BigInteger.valueOf(1));
        for (int i = 0; i < rawData.length; i++) {
            btemp[i % 4] = rawData[i];
            if (i % 4 == 3) {
                if ((i / 4) % 4 == 0) {
                    f = bytesToInt(btemp);
                }
                if ((i / 4) % 4 == 1) {
                    s = bytesToInt(btemp);
                    point1 = new Point(BigInteger.valueOf(f), BigInteger.valueOf(s));
                }
                if ((i / 4) % 4 == 2) {
                    f = bytesToInt(btemp);
                }
                if ((i / 4) % 4 == 3) {
                    s = bytesToInt(btemp);
                    point2 = new Point(BigInteger.valueOf(f), BigInteger.valueOf(s));
                    pair.add(new Pair<Point,Point>(point1, point2));
                }
            }
        }
        return pair;
    }
}
