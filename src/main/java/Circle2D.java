/**
 * x^2 + y^2 + ax + by + c = 0
 *
 * @author Maxim Shipko (sladethe@gmail.com)
 *         Date: 30.06.2015
 */
@SuppressWarnings("StandardVariableNames")
public class Circle2D {
    public static final double DEFAULT_EPSILON = Line2D.DEFAULT_EPSILON;

    private final double a;
    private final double b;
    private final double c;

    private final double squaredRadius;
    private final double radius;

    public Circle2D(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;

        this.squaredRadius = (a * a + b * b) / 4.0D - c;

        if (this.squaredRadius < 0.0D) {
            throw new IllegalArgumentException(String.format(
                    "Squared radius of circle is negative: a=%s, b=%s, c=%s.", a, b, c
            ));
        }

        this.radius = Math.sqrt(squaredRadius);
    }

    public Circle2D( Point2D center, double radius) {
        if (radius < 0.0D) {
            throw new IllegalArgumentException("Argument 'radius' is negative.");
        }

        this.squaredRadius = radius * radius;
        this.radius = radius;

        this.a = -2.0D * center.getX();
        this.b = -2.0D * center.getY();
        this.c = (a * a + b * b) / 4.0D - squaredRadius;
    }

    public Circle2D( Circle2D circle) {
        this.a = circle.a;
        this.b = circle.b;
        this.c = circle.c;

        this.squaredRadius = circle.squaredRadius;
        this.radius = circle.radius;
    }

    public double getA() {
        return a;
    }

    public Circle2D setA(double a) {
        return new Circle2D(a, b, c);
    }

    public double getB() {
        return b;
    }

    public Circle2D setB(double b) {
        return new Circle2D(a, b, c);
    }

    public double getC() {
        return c;
    }

    public Circle2D setC(double c) {
        return new Circle2D(a, b, c);
    }

    public double getSquaredRadius() {
        return squaredRadius;
    }

    public double getRadius() {
        return radius;
    }

    public double getCenterX() {
        return -a / 2.0D;
    }

    public double getCenterY() {
        return -b / 2.0D;
    }

    public Circle2D copy() {
        return new Circle2D(this);
    }

    @Override
    public String toString() {
        return StringUtil.toString(this, false, "a", "b", "c");
    }
}
