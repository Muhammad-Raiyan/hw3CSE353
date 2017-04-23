/**
 * Created by ishmam on 4/23/2017.
 */
public class ManhattanDistance implements DistanceStrategy{
    @Override
    public Double findDistance(Double x, Double y) {
        return Math.abs(x-y);
    }
}
