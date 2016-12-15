package pl.swidurski.gp;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Author: Krystian Świdurski
 */
public class DiscreteRange implements Range {

    public List<Double> range = new ArrayList<>();

    @Getter
    private final double beginning;
    @Getter
    private final double end;

    public DiscreteRange(Collection<Double> values) {
        range.addAll(values);
        beginning = range.get(0);
        end = range.get(range.size() - 1);
    }

    @Override
    public double get(int index) {
        return range.get(index);
    }

    @Override
    public int size() {
        return range.size();
    }
}
