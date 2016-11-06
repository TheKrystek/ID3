package pl.swidurski.model;

import lombok.Getter;

/**
 * Created by Krystek on 2016-11-06.
 */
public class EqualityCondition implements Condition {
    @Getter
    String value;
    @Getter
    Attribute attribute;

    public EqualityCondition(Attribute attribute, String value) {
        this.attribute = attribute;
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("%s = %s", attribute.getName(), value);
    }

    @Override
    public String getAttributeName() {
        return attribute.getName();
    }
}
