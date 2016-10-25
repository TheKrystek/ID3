package pl.swidurski.model;

import lombok.Data;

/**
 * Created by Krystek on 2016-10-24.
 */
@Data
public class Info {
    private double entropy, conditionalEntropy, informationGain;

    @Override
    public String toString() {
        return "Info{" +
                "entropy=" + entropy +
                ", conditionalEntropy=" + conditionalEntropy +
                ", informationGain=" + informationGain +
                '}';
    }
}
