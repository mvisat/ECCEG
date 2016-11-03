import java.math.BigInteger;

public class Point {
	public BigInteger x, y;
	public boolean infinite;

	public Point() {
		x = y = BigInteger.ZERO;
		infinite = false;
	}

	public Point(boolean infinite) {
		x = y = BigInteger.ZERO;
		this.infinite = infinite;
	}

	public Point(BigInteger x, BigInteger y) {
		this.x = x;
		this.y = y;
	}

	public String toString(){
		return "(" + x + ", " + y + ")";
	}
}
