/**
 * @author Maxim Shipko (sladethe@gmail.com)
 *         Date: 22.07.13
 */
public class Vector2D extends DoublePair {
    public static final double DEFAULT_EPSILON = Line2D.DEFAULT_EPSILON;

    public Vector2D(double x, double y) {
        super(x, y);
    }

    public Vector2D(double x1, double y1, double x2, double y2) {
        super(x2 - x1, y2 - y1);
    }

    public Vector2D(Point2D point1, Point2D point2) {
        super(point2.getX() - point1.getX(), point2.getY() - point1.getY());
    }

    public Vector2D(Vector2D vector) {
        super(vector.getX(), vector.getY());
    }

    public double getX() {
        Double x = getFirst();
        return x == null ? 0.0D : x;
    }

    public void setX(double x) {
        setFirst(x);
    }

    public double getY() {
        Double y = getSecond();
        return y == null ? 0.0D : y;
    }

    public void setY(double y) {
        setSecond(y);
    }

    public Vector2D add(Vector2D vector) {
        setX(getX() + vector.getX());
        setY(getY() + vector.getY());
        return this;
    }

    public Vector2D add(double x, double y) {
        setX(getX() + x);
        setY(getY() + y);
        return this;
    }

    public Vector2D subtract(Vector2D vector) {
        setX(getX() - vector.getX());
        setY(getY() - vector.getY());
        return this;
    }

    public Vector2D subtract(double x, double y) {
        setX(getX() - x);
        setY(getY() - y);
        return this;
    }

    public Vector2D multiply(double factor) {
        setX(factor * getX());
        setY(factor * getY());
        return this;
    }

    public Vector2D rotate(double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);

        double x = getX();
        double y = getY();

        setX(x * cos - y * sin);
        setY(x * sin + y * cos);

        return this;
    }

    public double dotProduct(Vector2D vector) {
        return Math.linearCombination(getX(), vector.getX(), getY(), vector.getY());
    }

    public Vector2D negate() {
        setX(-getX());
        setY(-getY());
        return this;
    }

    public Vector2D normalize() {
        double length = getLength();
        if (length == 0.0D) {
            throw new IllegalStateException("Can't set angle of zero-width vector.");
        }
        setX(getX() / length);
        setY(getY() / length);
        return this;
    }

    public double getAngle() {
        return Math.atan2(getY(), getX());
    }

    public double getNorm() {
        return Math.sqrt (getX() * getX() + getY() * getY());
    }

    public Vector2D setAngle(double angle) {
        double length = getLength();
        if (length == 0.0D) {
            throw new IllegalStateException("Can't set angle of zero-width vector.");
        }
        setX(Math.cos(angle) * length);
        setY(Math.sin(angle) * length);
        return this;
    }

    public static double angle(Vector2D v1, Vector2D v2) {

        double normProduct = v1.getNorm() * v2.getNorm();
        if (normProduct == 0) {
            throw new IllegalArgumentException("Norm is zero");
        }

        double dot = v1.dotProduct(v2);
        double threshold = normProduct * 0.9999;
        if ((dot < -threshold) || (dot > threshold)) {
            // the vectors are almost aligned, compute using the sine
            final double n = Math.abs(Math.linearCombination(v1.getX(), v2.getY(), -v1.getX(), v2.getX()));
            if (dot >= 0) {
                return Math.asin(n / normProduct);
            }
            return Math.PI - Math.asin(n / normProduct);
        }

        // the vectors are sufficiently separated to use the cosine
        return Math.acos(dot / normProduct);

    }

    public double getAngle(Vector2D vector) {
        return angle(this, vector);
    }

    public double getLength() {
        return Math.hypot(getX(), getY());
    }

    public Vector2D setLength(double length) {
        double currentLength = getLength();
        if (currentLength == 0.0D) {
            throw new IllegalStateException("Can't resize zero-width vector.");
        }
        return multiply(length / currentLength);
    }

    public double getSquaredLength() {
        return getX() * getX() + getY() * getY();
    }

    public Vector2D setSquaredLength(double squaredLength) {
        double currentSquaredLength = getSquaredLength();
        if (currentSquaredLength == 0.0D) {
            throw new IllegalStateException("Can't resize zero-width vector.");
        }
        return multiply(Math.sqrt(squaredLength / currentSquaredLength));
    }

    public Vector2D copy() {
        return new Vector2D(this);
    }

    public Vector2D copyNegate() {
        return new Vector2D(-getX(), -getY());
    }

    public boolean nearlyEquals(Vector2D vector, double epsilon) {
        return vector != null
                && NumberUtil.nearlyEquals(getX(), vector.getX(), epsilon)
                && NumberUtil.nearlyEquals(getY(), vector.getY(), epsilon);
    }

    public boolean nearlyEquals(Vector2D vector) {
        return nearlyEquals(vector, DEFAULT_EPSILON);
    }

    public boolean nearlyEquals(double x, double y, double epsilon) {
        return NumberUtil.nearlyEquals(getX(), x, epsilon)
                && NumberUtil.nearlyEquals(getY(), y, epsilon);
    }

    public boolean nearlyEquals(double x, double y) {
        return nearlyEquals(x, y, DEFAULT_EPSILON);
    }

    @Override
    public String toString() {
        return StringUtil.toString(this, false, "x", "y");
    }
}
