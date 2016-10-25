package pl.swidurski.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Krystek on 2016-10-23.
 */
public class Attribute {

    @Getter
    private final String name;
    @Getter
    @Setter
    boolean result;
    @Getter
    @Setter
    List<String> values;
    @Getter
    @Setter
    int index;

    public Attribute(String name) {
        this.name = name;
        this.result = false;
        this.values = new ArrayList<>();
    }

    public void addValue(String value) {
        values.add(value);
    }


    public List<Entry> getDistinctValues() {
        List<Entry> result = new ArrayList<>();
        for (String value : values) {
            Optional<Entry> entry = result.stream().filter(p -> p.getString().equals(value)).findFirst();
            if (entry.isPresent()) {
                entry.get().setCount(entry.get().getCount() + 1);
            } else {
                result.add(new Entry(value));
            }
        }
        return result;
    }

    public int getNumberOfDistinctValues() {
        return getDistinctValues().size();
    }

    public boolean hasDistinctValues() {
        return getNumberOfDistinctValues() > 1;
    }


}
