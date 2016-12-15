package pl.swidurski.gp;

/**
 * Author: Krystian Świdurski
 */
public interface UpdateEvent {
    void fire(Population population, Individual champion, int iteration);
}
