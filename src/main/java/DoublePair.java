/**
 * @author Maxim Shipko (sladethe@gmail.com)
 *         Date: 11.07.13
 */
public class DoublePair extends Pair<Double, Double> {
    public DoublePair() {
    }

    public DoublePair(Double first, Double second) {
        super(first, second);
    }

    public DoublePair(SimplePair<Double, Double> pair) {
        super(pair);
    }
}
