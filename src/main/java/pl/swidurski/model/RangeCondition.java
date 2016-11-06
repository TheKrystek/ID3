package pl.swidurski.model;

import lombok.Getter;
import pl.swidurski.utils.StringUtil;

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
}
