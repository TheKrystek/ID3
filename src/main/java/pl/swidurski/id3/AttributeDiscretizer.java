package pl.swidurski.id3;

import lombok.Getter;
import pl.swidurski.model.Attribute;
import pl.swidurski.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by student on 06.11.2016.
 */
public class AttributeDiscretizer {


    public static final String FORMAT = "%.2f";
    @Getter
    private Attribute attribute;

    @Getter
    private int numberOfRanges;

    private Double min, max, step;

    @Getter
    private List<Double> inputValues = new ArrayList<>();
    @Getter
    private List<String> discreteValues = new ArrayList<>();
    @Getter
    private List<String> ranges = new ArrayList<>();


    public AttributeDiscretizer(Attribute attribute, int numberOfRanges) throws Exception {
        checkAttribute(attribute);
        this.attribute = attribute;
        checkNumberOfRanges(numberOfRanges);
        this.numberOfRanges = numberOfRanges;

        discretize();
    }

    private void checkAttribute(Attribute attribute) throws Exception {
        if (!attribute.isNumeric()) {
            throw new Exception("Argument has to be numerical");
        }
    }

    private void checkNumberOfRanges(int numberOfRanges) throws Exception {
        int distinctValues = attribute.getNumberOfDistinctValues();
        if (distinctValues < numberOfRanges) {
            throw new Exception("Maximum number of ranges can is " + distinctValues);
        }
        if (numberOfRanges < 1) {
            throw new Exception("Minimal number of ranges can is 1");
        }
    }


    private void loadInputValues() {
        inputValues.addAll(attribute.getValues().stream().map(Double::parseDouble).collect(Collectors.toList()));
    }

    private void discretize() {
        loadInputValues();
        getThresholdValues();
        createRanges();
        assignNewValues();
        replaceValues();
        attribute.setAttributeDiscretizer(this);
    }

    private void assignNewValues() {
        discreteValues.addAll(inputValues.stream().map(this::getRange).collect(Collectors.toList()));
    }

    public String getRange(Double val) {
        return ranges.get(getRangeIndex(val));
    }

    private int getRangeIndex(Double val) {
        if (val < min) {
            return 0;
        }
        if (val >= max) {
            return numberOfRanges - 1;
        }
        return (int) Math.floor((val - min) / step);
    }


    private void createRanges() {
        for (int i = 0; i < numberOfRanges; i++) {
            String lower = String.format(FORMAT, min + (i * step));
            String upper = String.format(FORMAT, min + ((i + 1) * step));
            if (i == 0) lower = "-" + StringUtil.INF;
            if (i == (numberOfRanges - 1)) upper = StringUtil.INF;

            String range = String.format("(%s ; %s]", lower, upper);
            System.out.println(range);
            ranges.add(range);
        }
    }

    private void getThresholdValues() {
        min = inputValues.parallelStream().min(Double::compare).get();
        max = inputValues.parallelStream().max(Double::compare).get();
        step = (max - min) / (numberOfRanges);
    }

    public void replaceValues() {
        attribute.setValues(discreteValues);
    }
}
