import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Mike Mirzayanov (mirzayanovmr@gmail.com)
 * @author Maxim Shipko (sladethe@gmail.com)
 *         Date: 10.07.13
 */
public final class StringUtil {

    private static final Map<Class, ToStringConverter> toStringConverterByClass = new HashMap<>();
    private static final ReadWriteLock toStringConverterByClassMapLock = new ReentrantReadWriteLock();

    static final char NON_BREAKING_SPACE = (char) 160;
    static final char ZERO_WIDTH_SPACE = '\u200B';

    public static boolean isWhitespace(char c) {
        return Character.isWhitespace(c) || c == NON_BREAKING_SPACE || c == ZERO_WIDTH_SPACE;
    }

    public static String trim(String s) {
        if (s == null) {
            return null;
        }

        int lastIndex = s.length() - 1;
        int beginIndex = 0;
        int endIndex = lastIndex;

        while (beginIndex <= lastIndex && isWhitespace(s.charAt(beginIndex))) {
            ++beginIndex;
        }

        while (endIndex > beginIndex && isWhitespace(s.charAt(endIndex))) {
            --endIndex;
        }

        return beginIndex == 0 && endIndex == lastIndex ? s : s.substring(beginIndex, endIndex + 1);
    }

    public static boolean isBlank(String s) {
        if (s == null || s.isEmpty()) {
            return true;
        }

        for (int charIndex = s.length() - 1; charIndex >= 0; --charIndex) {
            if (!isWhitespace(s.charAt(charIndex))) {
                return false;
            }
        }

        return true;
    }

    public static String[] split(String s, char separator) {
        int length = s.length();
        int start = 0;
        int i = 0;

        String[] parts = null;
        int count = 0;

        while (i < length) {
            if (s.charAt(i) == separator) {
                if (parts == null) {
                    parts = new String[8];
                } else if (count == parts.length) {
                    String[] tempParts = new String[count << 1];
                    System.arraycopy(parts, 0, tempParts, 0, count);
                    parts = tempParts;
                }
                parts[count++] = s.substring(start, i);
                start = ++i;
                continue;
            }
            ++i;
        }

        if (parts == null) {
            return new String[]{s};
        }

        if (count == parts.length) {
            String[] tempParts = new String[count + 1];
            System.arraycopy(parts, 0, tempParts, 0, count);
            parts = tempParts;
        }

        parts[count++] = s.substring(start, i);

        if (count == parts.length) {
            return parts;
        } else {
            String[] tempParts = new String[count];
            System.arraycopy(parts, 0, tempParts, 0, count);
            return tempParts;
        }
    }

    public static boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }

    @SuppressWarnings({"AccessingNonPublicFieldOfAnotherObject", "OverlyComplexMethod"})
    private static boolean shouldSkipField(String stringValue, ToStringOptions options, Mutable<Boolean> quoted) {
        if (options.skipNulls && stringValue == null) {
            return true;
        }

        if (options.skipEmptyStrings) {
            if (quoted != null && quoted.get() != null && quoted.get()) {
                if ("''".equals(stringValue) || "\"\"".equals(stringValue)) {
                    return true;
                }
            } else {
                if (isEmpty(stringValue)) {
                    return true;
                }
            }
        }

        if (options.skipBlankStrings) {
            if (quoted != null && quoted.get() != null && quoted.get()) {
                if (isBlank(stringValue) || isBlank(stringValue.substring(1, stringValue.length() - 1))) {
                    return true;
                }
            } else {
                if (isBlank(stringValue)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static String getSimpleName(Class clazz, boolean addEnclosingClassNames) {
        String simpleName = clazz.getSimpleName();
        if (addEnclosingClassNames) {
            while ((clazz = clazz.getEnclosingClass()) != null) {
                simpleName = String.format("%s.%s", clazz.getSimpleName(), simpleName);
            }
        }
        return simpleName;
    }

    private static String valueToString(Object value, Mutable<Boolean> quoted) {
        if (value == null) {
            return null;
        }

        Class<?> valueClass = value.getClass();
        ToStringConverter toStringConverter;

        if (valueClass.isArray()) {
            return arrayToString(value);
        } else if ((toStringConverter = getToStringConverter(valueClass, true)) != null) {
            return toStringConverter.convert(value);
        } else if (value instanceof Collection) {
            return collectionToString((Collection) value);
        } else if (value instanceof Map) {
            return mapToString((Map) value);
        } else if (value instanceof Map.Entry) {
            Map.Entry entry = (Map.Entry) value;
            return valueToString(entry.getKey(), null) + ": " + valueToString(entry.getValue(), null);
        } else if (value instanceof SimplePair) {
            SimplePair pair = (SimplePair) value;
            return '(' + valueToString(pair.getFirst(), null) + ", " + valueToString(pair.getSecond(), null) + ')';
        } else if (valueClass == Character.class) {
            Holders.setQuietly(quoted, true);
            return "'" + value + '\'';
        } else if (valueClass == Boolean.class
                || valueClass == Byte.class
                || valueClass == Short.class
                || valueClass == Integer.class
                || valueClass == Long.class
                || valueClass == Float.class
                || valueClass == Double.class) {
            return value.toString();
        } else if (valueClass.isEnum()) {
            return ((Enum) value).name();
        } else if (valueClass == String.class) {
            Holders.setQuietly(quoted, true);
            return '\'' + (String) value + '\'';
        } else {
            Holders.setQuietly(quoted, true);
            return '\'' + String.valueOf(value) + '\'';
        }
    }

    public static String capitalize(final String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }

        final char firstChar = str.charAt(0);
        final char newChar = Character.toUpperCase(firstChar);
        if (firstChar == newChar) {
            return str;
        }

        char[] newChars = new char[strLen];
        newChars[0] = newChar;
        str.getChars(1, strLen, newChars, 1);
        return String.valueOf(newChars);
    }

    private static String collectionToString(Collection collection) {
        StringBuilder builder = new StringBuilder("[");
        Iterator iterator = collection.iterator();

        if (iterator.hasNext()) {
            builder.append(valueToString(iterator.next(), null));

            while (iterator.hasNext()) {
                builder.append(", ").append(valueToString(iterator.next(), null));
            }
        }

        return builder.append(']').toString();
    }

    private static String mapToString(Map map) {
        StringBuilder builder = new StringBuilder("{");
        Iterator iterator = map.entrySet().iterator();

        if (iterator.hasNext()) {
            builder.append(valueToString(iterator.next(), null));

            while (iterator.hasNext()) {
                builder.append(", ").append(valueToString(iterator.next(), null));
            }
        }

        return builder.append('}').toString();
    }

    private static String arrayToString(Object array) {
        StringBuilder builder = new StringBuilder("[");
        int length = Array.getLength(array);

        if (length > 0) {
            builder.append(valueToString(Array.get(array, 0), null));

            for (int i = 1; i < length; ++i) {
                builder.append(", ").append(valueToString(Array.get(array, i), null));
            }
        }

        return builder.append(']').toString();
    }

    @SuppressWarnings("unchecked")
    public static <T> ToStringConverter<? super T> getToStringConverter(
            Class<T> valueClass, boolean checkSuperclasses) {
        Lock readLock = toStringConverterByClassMapLock.readLock();
        readLock.lock();
        try {
            if (checkSuperclasses) {
                Class localClass = valueClass;
                while (localClass != null) {
                    ToStringConverter toStringConverter = toStringConverterByClass.get(localClass);
                    if (toStringConverter != null) {
                        return toStringConverter;
                    }
                    localClass = localClass.getSuperclass();
                }

                return null;
            } else {
                return toStringConverterByClass.get(valueClass);
            }
        } finally {
            readLock.unlock();
        }
    }

    private static String fieldToString(Object value, String fieldName, ToStringOptions options) {
        if (value.getClass() == Boolean.class || value.getClass() == boolean.class) {
            return (boolean) value ? fieldName : '!' + fieldName;
        }

        Mutable<Boolean> quoted = new SimpleMutable<>();
        String stringValue = valueToString(value, quoted);

        if (shouldSkipField(stringValue, options, quoted)) {
            return null;
        }

        return fieldName + '=' + stringValue;
    }

    @SuppressWarnings({"OverloadedVarargsMethod", "AssignmentToMethodParameter", "AccessingNonPublicFieldOfAnotherObject"})
    public static String toString(Object object, ToStringOptions options, String... fieldNames) {
        Class<?> objectClass = object.getClass();

        if (fieldNames.length == 0) {
            Set<String> allFieldNames = ReflectionUtil.getFieldsByNameMap(objectClass).keySet();
            fieldNames = allFieldNames.toArray(new String[allFieldNames.size()]);
        }

        StringBuilder builder = new StringBuilder(getSimpleName(objectClass, options.addEnclosingClassNames))
                .append(" {");
        boolean firstAppendix = true;

        for (int fieldIndex = 0, fieldCount = fieldNames.length; fieldIndex < fieldCount; ++fieldIndex) {
            String fieldName = fieldNames[fieldIndex];
            if (isBlank(fieldName)) {
                throw new IllegalArgumentException("Field name can not be neither 'null' nor blank.");
            }

            Object deepValue = ReflectionUtil.getDeepValue(object, fieldName);
            String fieldAsString;

            if (deepValue == null) {
                if (options.skipNulls || options.skipEmptyStrings || options.skipBlankStrings) {
                    continue;
                } else {
                    fieldAsString = fieldName + "=null";
                }
            } else {
                fieldAsString = fieldToString(deepValue, fieldName, options);
                if (fieldAsString == null) {
                    continue;
                }
            }

            if (firstAppendix) {
                firstAppendix = false;
            } else {
                builder.append(", ");
            }

            builder.append(fieldAsString);
        }

        return builder.append('}').toString();
    }

    @SuppressWarnings({"OverloadedVarargsMethod", "AccessingNonPublicFieldOfAnotherObject"})
    public static <T> String toString(
            Class<? extends T> objectClass, T object, boolean skipNulls, String... fieldNames) {
        ToStringOptions options = new ToStringOptions();
        options.skipNulls = skipNulls;
        return toString(objectClass, object, options, fieldNames);
    }

    @SuppressWarnings({"OverloadedVarargsMethod", "AccessingNonPublicFieldOfAnotherObject"})
    public static <T> String toString(
            Class<? extends T> objectClass, T object, ToStringOptions options,
            String... fieldNames) {
        if (object == null) {
            return getSimpleName(objectClass, options.addEnclosingClassNames) + " {null}";
        }

        return toString(object, options, fieldNames);
    }

    public static String toString(Object object, boolean skipNulls, String... fieldNames) {
        ToStringOptions options = new ToStringOptions();
        options.skipNulls = skipNulls;
        return toString(object, options, fieldNames);
    }

    @SuppressWarnings("InterfaceNeverImplemented")
    public interface ToStringConverter<T> {

        String convert(T value);
    }

    public static final class ToStringOptions {

        private boolean skipNulls;
        private boolean skipEmptyStrings;
        private boolean skipBlankStrings;
        private boolean addEnclosingClassNames;

        public ToStringOptions() {
        }

        public ToStringOptions(
                boolean skipNulls, boolean skipEmptyStrings, boolean skipBlankStrings, boolean addEnclosingClassNames) {
            this.skipNulls = skipNulls;
            this.skipEmptyStrings = skipEmptyStrings;
            this.skipBlankStrings = skipBlankStrings;
            this.addEnclosingClassNames = addEnclosingClassNames;
        }

        public boolean isSkipNulls() {
            return skipNulls;
        }

        public void setSkipNulls(boolean skipNulls) {
            this.skipNulls = skipNulls;
        }

        public boolean isSkipEmptyStrings() {
            return skipEmptyStrings;
        }

        public void setSkipEmptyStrings(boolean skipEmptyStrings) {
            this.skipEmptyStrings = skipEmptyStrings;
        }

        public boolean isSkipBlankStrings() {
            return skipBlankStrings;
        }

        public void setSkipBlankStrings(boolean skipBlankStrings) {
            this.skipBlankStrings = skipBlankStrings;
        }

        public boolean isAddEnclosingClassNames() {
            return addEnclosingClassNames;
        }

        public void setAddEnclosingClassNames(boolean addEnclosingClassNames) {
            this.addEnclosingClassNames = addEnclosingClassNames;
        }
    }
}
