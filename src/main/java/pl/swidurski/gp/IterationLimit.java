package pl.swidurski.gp;

/**
 * Author: Krystian Świdurski
 */
public class IterationLimit implements StopCondition {
    private final int limit;

    public IterationLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public boolean stop(Individual champion, int iteration) {
        return iteration >= limit;
    }
}
