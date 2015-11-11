/**
 * @author Maxim Shipko (sladethe@gmail.com)
 *         Date: 07.03.13
 */
public class Holders {
    private Holders() {
        throw new UnsupportedOperationException();
    }

    public static <T> Readable<T> readOnly(final Mutable<T> mutable) {
        return new Mutable<T>() {
            @Override
            public T get() {
                return mutable.get();
            }

            @Override
            public T set(T value) {
                throw new UnsupportedOperationException();
            }
        };
    }

    public static <T> void setQuietly(Writable<T> writable, T value) {
        if (writable != null) {
            try {
                writable.set(value);
            } catch (RuntimeException ignored) {
                // No operations.
            }
        }
    }

    public static <T> void swap(Mutable<T> mutableA, Mutable<T> mutableB) {
        T valueA = mutableA.get();
        mutableA.set(mutableB.get());
        mutableB.set(valueA);
    }

    public static <T> void swapQuietly(Mutable<T> mutableA, Mutable<T> mutableB) {
        if (mutableA != null && mutableB != null) {
            try {
                swap(mutableA, mutableB);
            } catch (RuntimeException ignored) {
                // No operations.
            }
        }
    }
}
