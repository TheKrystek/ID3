package pl.swidurski.gp;

/**
 * Author: Krystian Świdurski
 */
public interface StopCondition {
    boolean stop(Individual champion, int iteration);
}
