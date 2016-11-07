package pl.swidurski.model;

import lombok.Getter;

/**
 * Created by Krystek on 2016-11-06.
 */
public class RangeCondition implements Condition {

    @Getter
    String value;
    @Getter
    Attribute attribute;

    public RangeCondition(Attribute attribute, String value) {
        this.attribute = attribute;
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("(%s in %s)", attribute.getName(), value);
    }

    @Override
    public String getAttributeName() {
        return attribute.getName();
    }

    @Override
    public boolean match(String value) {
        Double val = getDouble(value);
        if (val == null || getAttribute().getAttributeDiscretizer() == null) {
            return false;
        }
        return this.value.equals(getAttribute().getAttributeDiscretizer().getRange(val));
    }


    private Double getDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
