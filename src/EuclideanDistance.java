/**
 * Created by ishmam on 4/23/2017.
 */
public class EuclideanDistance implements DistanceStrategy {
    @Override
    public Double findDistance(Double x, Double y) {
        double result = x - y;
        return Math.sqrt(result*result);
    }
}
