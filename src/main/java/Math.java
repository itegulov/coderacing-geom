/**
 * @author Maxim Shipko (sladethe@gmail.com)
 *         Date: 19.06.2015
 */
public final class Math {
    public static final double E = StrictMath.E;

    public static final double PI = StrictMath.PI;
    public static final double DOUBLE_PI = 2.0D * PI;
    public static final double HALF_PI = PI / 2.0D;
    public static final double THIRD_PI = PI / 3.0D;
    public static final double QUARTER_PI = PI / 4.0D;
    public static final double SIXTH_PI = PI / 6.0D;

    public static final double RADIANS_PER_DEGREE = PI / 180.0D;
    public static final double DEGREES_PER_RADIAN = 180.0D / PI;

    public static final double SQRT_2 = sqrt(2.0D);
    public static final double SQRT_3 = sqrt(3.0D);
    public static final double SQRT_5 = sqrt(5.0D);
    public static final double SQRT_6 = sqrt(6.0D);
    public static final double SQRT_7 = sqrt(7.0D);
    public static final double SQRT_8 = sqrt(8.0D);

    public static final double CBRT_2 = cbrt(2.0D);
    public static final double CBRT_3 = cbrt(3.0D);
    public static final double CBRT_4 = cbrt(4.0D);
    public static final double CBRT_5 = cbrt(5.0D);
    public static final double CBRT_6 = cbrt(6.0D);
    public static final double CBRT_7 = cbrt(7.0D);
    public static final double CBRT_9 = cbrt(9.0D);

    private Math() {
        throw new UnsupportedOperationException();
    }

    public static double linearCombination(final double a1, final double b1,
                                           final double a2, final double b2) {

        // the code below is split in many additions/subtractions that may
        // appear redundant. However, they should NOT be simplified, as they
        // use IEEE754 floating point arithmetic rounding properties.
        // The variable naming conventions are that xyzHigh contains the most significant
        // bits of xyz and xyzLow contains its least significant bits. So theoretically
        // xyz is the sum xyzHigh + xyzLow, but in many cases below, this sum cannot
        // be represented in only one double precision number so we preserve two numbers
        // to hold it as long as we can, combining the high and low order bits together
        // only at the end, after cancellation may have occurred on high order bits

        // split a1 and b1 as one 26 bits number and one 27 bits number
        final double a1High     = Double.longBitsToDouble(Double.doubleToRawLongBits(a1) & ((-1L) << 27));
        final double a1Low      = a1 - a1High;
        final double b1High     = Double.longBitsToDouble(Double.doubleToRawLongBits(b1) & ((-1L) << 27));
        final double b1Low      = b1 - b1High;

        // accurate multiplication a1 * b1
        final double prod1High  = a1 * b1;
        final double prod1Low   = a1Low * b1Low - (((prod1High - a1High * b1High) - a1Low * b1High) - a1High * b1Low);

        // split a2 and b2 as one 26 bits number and one 27 bits number
        final double a2High     = Double.longBitsToDouble(Double.doubleToRawLongBits(a2) & ((-1L) << 27));
        final double a2Low      = a2 - a2High;
        final double b2High     = Double.longBitsToDouble(Double.doubleToRawLongBits(b2) & ((-1L) << 27));
        final double b2Low      = b2 - b2High;

        // accurate multiplication a2 * b2
        final double prod2High  = a2 * b2;
        final double prod2Low   = a2Low * b2Low - (((prod2High - a2High * b2High) - a2Low * b2High) - a2High * b2Low);

        // accurate addition a1 * b1 + a2 * b2
        final double s12High    = prod1High + prod2High;
        final double s12Prime   = s12High - prod2High;
        final double s12Low     = (prod2High - (s12High - s12Prime)) + (prod1High - s12Prime);

        // final rounding, s12 may have suffered many cancellations, we try
        // to recover some bits from the extra words we have saved up to now
        double result = s12High + (prod1Low + prod2Low + s12Low);

        if (Double.isNaN(result)) {
            // either we have split infinite numbers or some coefficients were NaNs,
            // just rely on the naive implementation and let IEEE754 handle this
            result = a1 * b1 + a2 * b2;
        }

        return result;
    }

    @SuppressWarnings("AssignmentToMethodParameter")
    public static long findGreatestCommonDivisor(long numberA, long numberB) {
        @SuppressWarnings("TooBroadScope") long temp;

        while (numberA != 0 && numberB != 0) {
            numberA %= numberB;
            temp = numberA;
            numberA = numberB;
            numberB = temp;
        }

        return numberA + numberB;
    }

    public static long findLeastCommonMultiple(long numberA, long numberB) {
        return numberA / findGreatestCommonDivisor(numberA, numberB) * numberB;
    }

    public static long findLeastCommonMultiple(long[] numbers) {
        int numberCount = numbers.length;

        if (numberCount == 0) {
            throw new IllegalArgumentException("Can't find LCM for zero numbers.");
        }

        if (numberCount == 1) {
            return numbers[0];
        }

        long leastCommonMultiple = findLeastCommonMultiple(numbers[0], numbers[1]);

        for (int numberIndex = 2; numberIndex < numberCount; ++numberIndex) {
            leastCommonMultiple = findLeastCommonMultiple(leastCommonMultiple, numbers[numberIndex]);
        }

        return leastCommonMultiple;
    }

    public static int avg(int numberA, int numberB) {
        return numberA / 2 + numberB / 2 + (numberA % 2 + numberB % 2) / 2;
    }

    public static int avg(int numberA, int numberB, int numberC) {
        return numberA / 3 + numberB / 3 + numberC / 3 + (numberA % 3 + numberB % 3 + numberC % 3) / 3;
    }

    public static int avg(int numberA, int numberB, int numberC, int numberD) {
        return numberA / 4 + numberB / 4 + numberC / 4 + numberD / 4
                + (numberA % 4 + numberB % 4 + numberC % 4 + numberD % 4) / 4;
    }

    public static long avg(long numberA, long numberB) {
        return numberA / 2L + numberB / 2L + (numberA % 2L + numberB % 2L) / 2L;
    }

    public static long avg(long numberA, long numberB, long numberC) {
        return numberA / 3L + numberB / 3L + numberC / 3L + (numberA % 3L + numberB % 3L + numberC % 3L) / 3L;
    }

    public static long avg(long numberA, long numberB, long numberC, long numberD) {
        return numberA / 4L + numberB / 4L + numberC / 4L + numberD / 4L
                + (numberA % 4L + numberB % 4L + numberC % 4L + numberD % 4L) / 4L;
    }

    public static float avg(float numberA, float numberB) {
        return numberA * 0.5F + numberB * 0.5F;
    }

    public static float avg(float numberA, float numberB, float numberC) {
        return numberA / 3.0F + numberB / 3.0F + numberC / 3.0F;
    }

    public static float avg(float numberA, float numberB, float numberC, float numberD) {
        return numberA * 0.25F + numberB * 0.25F + numberC * 0.25F + numberD * 0.25F;
    }

    public static double avg(double numberA, double numberB) {
        return numberA * 0.5D + numberB * 0.5D;
    }

    public static double avg(double numberA, double numberB, double numberC) {
        return numberA / 3.0D + numberB / 3.0D + numberC / 3.0D;
    }

    public static double avg(double numberA, double numberB, double numberC, double numberD) {
        return numberA * 0.25D + numberB * 0.25D + numberC * 0.25D + numberD * 0.25D;
    }

    public static double sqr(double value) {
        return value * value;
    }

    public static double sumSqr(double numberA, double numberB) {
        return numberA * numberA + numberB * numberB;
    }

    public static double sumSqr(double numberA, double numberB, double numberC) {
        return numberA * numberA + numberB * numberB + numberC * numberC;
    }

    public static double sumSqr(double numberA, double numberB, double numberC, double numberD) {
        return numberA * numberA + numberB * numberB + numberC * numberC + numberD * numberD;
    }

    public static double pow(double base, double exponent) {
        return StrictMath.pow(base, exponent);
    }

    public static int min(int numberA, int numberB) {
        return numberA <= numberB ? numberA : numberB;
    }

    public static int min(int numberA, int numberB, int numberC) {
        return min(numberA <= numberB ? numberA : numberB, numberC);
    }

    public static int min(int numberA, int numberB, int numberC, int numberD) {
        return min(numberA <= numberB ? numberA : numberB, numberC <= numberD ? numberC : numberD);
    }

    public static long min(long numberA, long numberB) {
        return numberA <= numberB ? numberA : numberB;
    }

    public static long min(long numberA, long numberB, long numberC) {
        return min(numberA <= numberB ? numberA : numberB, numberC);
    }

    public static long min(long numberA, long numberB, long numberC, long numberD) {
        return min(numberA <= numberB ? numberA : numberB, numberC <= numberD ? numberC : numberD);
    }

    public static float min(float numberA, float numberB) {
        return java.lang.Math.min(numberA, numberB);
    }

    public static float min(float numberA, float numberB, float numberC) {
        return min(min(numberA, numberB), numberC);
    }

    public static float min(float numberA, float numberB, float numberC, float numberD) {
        return min(min(numberA, numberB), min(numberC, numberD));
    }

    public static double min(double numberA, double numberB) {
        return java.lang.Math.min(numberA, numberB);
    }

    public static double min(double numberA, double numberB, double numberC) {
        return min(min(numberA, numberB), numberC);
    }

    public static double min(double numberA, double numberB, double numberC, double numberD) {
        return min(min(numberA, numberB), min(numberC, numberD));
    }

    public static int max(int numberA, int numberB) {
        return numberA >= numberB ? numberA : numberB;
    }

    public static int max(int numberA, int numberB, int numberC) {
        return max(numberA >= numberB ? numberA : numberB, numberC);
    }

    public static int max(int numberA, int numberB, int numberC, int numberD) {
        return max(numberA >= numberB ? numberA : numberB, numberC >= numberD ? numberC : numberD);
    }

    public static long max(long numberA, long numberB) {
        return numberA >= numberB ? numberA : numberB;
    }

    public static long max(long numberA, long numberB, long numberC) {
        return max(numberA >= numberB ? numberA : numberB, numberC);
    }

    public static long max(long numberA, long numberB, long numberC, long numberD) {
        return max(numberA >= numberB ? numberA : numberB, numberC >= numberD ? numberC : numberD);
    }

    public static float max(float numberA, float numberB) {
        return java.lang.Math.max(numberA, numberB);
    }

    public static float max(float numberA, float numberB, float numberC) {
        return max(max(numberA, numberB), numberC);
    }

    public static float max(float numberA, float numberB, float numberC, float numberD) {
        return max(max(numberA, numberB), max(numberC, numberD));
    }

    public static double max(double numberA, double numberB) {
        return java.lang.Math.max(numberA, numberB);
    }

    public static double max(double numberA, double numberB, double numberC) {
        return max(max(numberA, numberB), numberC);
    }

    public static double max(double numberA, double numberB, double numberC, double numberD) {
        return max(max(numberA, numberB), max(numberC, numberD));
    }

    public static int abs(int value) {
        return value < 0 ? -value : value;
    }

    public static long abs(long value) {
        return value < 0 ? -value : value;
    }

    public static float abs(float value) {
        return java.lang.Math.abs(value);
    }

    public static double abs(double value) {
        return java.lang.Math.abs(value);
    }

    public static double sqrt(double value) {
        return StrictMath.sqrt(value);
    }

    public static double cbrt(double value) {
        return StrictMath.cbrt(value);
    }

    public static float round(float value) {
        return java.lang.Math.round(value);
    }

    public static double round(double value) {
        return java.lang.Math.round(value);
    }

    public static double floor(double value) {
        return StrictMath.floor(value);
    }

    public static double ceil(double value) {
        return StrictMath.ceil(value);
    }

    public static double hypot(double cathetusA, double cathetusB) {
        return StrictMath.hypot(cathetusA, cathetusB);
    }

    public static double sin(double value) {
        return StrictMath.sin(value);
    }

    public static double cos(double value) {
        return StrictMath.cos(value);
    }

    public static double tan(double value) {
        return StrictMath.tan(value);
    }

    public static double asin(double value) {
        return StrictMath.asin(value);
    }

    public static double acos(double value) {
        return StrictMath.acos(value);
    }

    public static double atan(double value) {
        return StrictMath.atan(value);
    }

    public static double atan2(double y, double x) {
        return StrictMath.atan2(y, x);
    }
}
