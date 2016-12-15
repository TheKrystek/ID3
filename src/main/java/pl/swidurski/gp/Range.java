package pl.swidurski.gp;

/**
 * Author: Krystian Świdurski
 */
public interface Range {
    double getBeginning();

    double getEnd();

    double get(int index);

    int size();
}
