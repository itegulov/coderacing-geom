/**
 * @author Maxim Shipko (sladethe@gmail.com)
 *         Date: 24.07.2013
 */
public final class NumberUtil {
    private NumberUtil() {
        throw new UnsupportedOperationException();
    }

    public static Byte toByte(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Byte) {
            return (Byte) value;
        }

        if (value instanceof Short) {
            return toByte((short) value);
        }

        if (value instanceof Integer) {
            return toByte((int) value);
        }

        if (value instanceof Long) {
            return toByte((long) value);
        }

        if (value instanceof Float) {
            return toByte((float) value);
        }

        if (value instanceof Double) {
            return toByte((double) value);
        }

        if (value instanceof Number) {
            return toByte(((Number) value).doubleValue());
        }

        return toByte(Double.parseDouble(StringUtil.trim(value.toString())));
    }

    public static Byte toByte(String value) {
        return value == null ? null : toByte(Double.parseDouble(StringUtil.trim(value)));
    }

    public static byte toByte(short value) {
        @SuppressWarnings("NumericCastThatLosesPrecision") byte byteValue = (byte) value;
        if ((short) byteValue == value) {
            return byteValue;
        }
        throw new IllegalArgumentException("Can't convert short " + value + " to byte.");
    }

    public static byte toByte(int value) {
        @SuppressWarnings("NumericCastThatLosesPrecision") byte byteValue = (byte) value;
        if ((int) byteValue == value) {
            return byteValue;
        }
        throw new IllegalArgumentException("Can't convert int " + value + " to byte.");
    }

    public static byte toByte(long value) {
        @SuppressWarnings("NumericCastThatLosesPrecision") byte byteValue = (byte) value;
        if ((long) byteValue == value) {
            return byteValue;
        }
        throw new IllegalArgumentException("Can't convert long " + value + " to byte.");
    }

    public static byte toByte(float value) {
        @SuppressWarnings("NumericCastThatLosesPrecision") byte byteValue = (byte) value;
        if (Math.abs((float) byteValue - value) < 1.0F) {
            return byteValue;
        }
        throw new IllegalArgumentException("Can't convert float " + value + " to byte.");
    }

    public static byte toByte(double value) {
        @SuppressWarnings("NumericCastThatLosesPrecision") byte byteValue = (byte) value;
        if (Math.abs((double) byteValue - value) < 1.0D) {
            return byteValue;
        }
        throw new IllegalArgumentException("Can't convert double " + value + " to byte.");
    }

    public static Integer toInt(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Byte) {
            return (int) (byte) value;
        }

        if (value instanceof Short) {
            return (int) (short) value;
        }

        if (value instanceof Integer) {
            return (Integer) value;
        }

        if (value instanceof Long) {
            return toInt((long) value);
        }

        if (value instanceof Float) {
            return toInt((float) value);
        }

        if (value instanceof Double) {
            return toInt((double) value);
        }

        if (value instanceof Number) {
            return toInt(((Number) value).doubleValue());
        }

        return toInt(Double.parseDouble(StringUtil.trim(value.toString())));
    }

    public static Integer toInt(String value) {
        return value == null ? null : toInt(Double.parseDouble(StringUtil.trim(value)));
    }

    public static int toInt(long value) {
        @SuppressWarnings("NumericCastThatLosesPrecision") int intValue = (int) value;
        if ((long) intValue == value) {
            return intValue;
        }
        throw new IllegalArgumentException("Can't convert long " + value + " to int.");
    }

    public static int toInt(float value) {
        @SuppressWarnings("NumericCastThatLosesPrecision") int intValue = (int) value;
        if (Math.abs((float) intValue - value) < 1.0F) {
            return intValue;
        }
        throw new IllegalArgumentException("Can't convert float " + value + " to int.");
    }

    public static int toInt(double value) {
        @SuppressWarnings("NumericCastThatLosesPrecision") int intValue = (int) value;
        if (Math.abs((double) intValue - value) < 1.0D) {
            return intValue;
        }
        throw new IllegalArgumentException("Can't convert double " + value + " to int.");
    }

    public static Long toLong(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Byte) {
            return (long) (byte) value;
        }

        if (value instanceof Short) {
            return (long) (short) value;
        }

        if (value instanceof Integer) {
            return (long) (int) value;
        }

        if (value instanceof Long) {
            return (Long) value;
        }

        if (value instanceof Float) {
            return toLong((float) value);
        }

        if (value instanceof Double) {
            return toLong((double) value);
        }

        if (value instanceof Number) {
            return toLong(((Number) value).doubleValue());
        }

        return toLong(Double.parseDouble(StringUtil.trim(value.toString())));
    }

    public static Long toLong(String value) {
        return value == null ? null : toLong(Double.parseDouble(StringUtil.trim(value)));
    }

    public static long toLong(float value) {
        @SuppressWarnings("NumericCastThatLosesPrecision") long longValue = (long) value;
        if (Math.abs((float) longValue - value) < 1.0F) {
            return longValue;
        }
        throw new IllegalArgumentException("Can't convert float " + value + " to long.");
    }

    public static long toLong(double value) {
        @SuppressWarnings("NumericCastThatLosesPrecision") long longValue = (long) value;
        if (Math.abs((double) longValue - value) < 1.0D) {
            return longValue;
        }
        throw new IllegalArgumentException("Can't convert double " + value + " to long.");
    }

    public static boolean equals(Byte numberA, Byte numberB) {
        return numberA == null ? numberB == null : numberA.equals(numberB);
    }

    public static boolean equals(Byte numberA, Byte numberB, Byte numberC) {
        return numberA == null ? numberB == null && numberC == null : numberA.equals(numberB) && numberA.equals(numberC);
    }

    public static boolean equals(Short numberA, Short numberB) {
        return numberA == null ? numberB == null : numberA.equals(numberB);
    }

    public static boolean equals(Short numberA, Short numberB, Short numberC) {
        return numberA == null ? numberB == null && numberC == null : numberA.equals(numberB) && numberA.equals(numberC);
    }

    public static boolean equals(Integer numberA, Integer numberB) {
        return numberA == null ? numberB == null : numberA.equals(numberB);
    }

    public static boolean equals(Integer numberA, Integer numberB, Integer numberC) {
        return numberA == null ? numberB == null && numberC == null : numberA.equals(numberB) && numberA.equals(numberC);
    }

    public static boolean equals(Long numberA, Long numberB) {
        return numberA == null ? numberB == null : numberA.equals(numberB);
    }

    public static boolean equals(Long numberA, Long numberB, Long numberC) {
        return numberA == null ? numberB == null && numberC == null : numberA.equals(numberB) && numberA.equals(numberC);
    }

    public static boolean equals(Float numberA, Float numberB) {
        return numberA == null ? numberB == null : numberA.equals(numberB);
    }

    public static boolean equals(Float numberA, Float numberB, Float numberC) {
        return numberA == null ? numberB == null && numberC == null : numberA.equals(numberB) && numberA.equals(numberC);
    }

    public static boolean equals(Double numberA, Double numberB) {
        return numberA == null ? numberB == null : numberA.equals(numberB);
    }

    public static boolean equals(Double numberA, Double numberB, Double numberC) {
        return numberA == null ? numberB == null && numberC == null : numberA.equals(numberB) && numberA.equals(numberC);
    }

    @SuppressWarnings("SimplifiableIfStatement")
    public static boolean nearlyEquals(Float numberA, Float numberB, float epsilon) {
        if (numberA == null) {
            return numberB == null;
        }

        if (numberB == null) {
            return false;
        }

        if (numberA.equals(numberB)) {
            return true;
        }

        if (Float.isInfinite(numberA) || Float.isNaN(numberA)
                || Float.isInfinite(numberB) || Float.isNaN(numberB)) {
            return false;
        }

        return Math.abs(numberA - numberB) < epsilon;
    }

    @SuppressWarnings("SimplifiableIfStatement")
    public static boolean nearlyEquals(Double numberA, Double numberB, double epsilon) {
        if (numberA == null) {
            return numberB == null;
        }

        if (numberB == null) {
            return false;
        }

        if (numberA.equals(numberB)) {
            return true;
        }

        if (Double.isInfinite(numberA) || Double.isNaN(numberA)
                || Double.isInfinite(numberB) || Double.isNaN(numberB)) {
            return false;
        }

        return Math.abs(numberA - numberB) < epsilon;
    }

    public static byte nullToZero(Byte value) {
        return value == null ? (byte) 0 : value;
    }

    public static short nullToZero(Short value) {
        return value == null ? (short) 0 : value;
    }

    public static int nullToZero(Integer value) {
        return value == null ? 0 : value;
    }

    public static long nullToZero(Long value) {
        return value == null ? 0L : value;
    }

    public static float nullToZero(Float value) {
        return value == null ? 0.0F : value;
    }

    public static double nullToZero(Double value) {
        return value == null ? 0.0D : value;
    }
}
