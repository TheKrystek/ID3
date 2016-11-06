package pl.swidurski.model;

import lombok.Getter;
import pl.swidurski.utils.StringUtil;

/**
 * Created by Krystek on 2016-11-06.
 */
public class RangeCondition implements Condition {

    public static final String FORMAT = "%.2f";
    @Getter
    Double lower;
    @Getter
    Double upper;
    @Getter
    Attribute attribute;

    public RangeCondition(Attribute attribute, Double lower, Double upper) {
        this.attribute = attribute;
        this.lower = lower;
        this.upper = upper;
    }


    @Override
    public String toString() {
        if (lower == null && upper == null) {
            return asString("is a number");
        }

        String l = String.format(FORMAT, lower);
        String u = String.format(FORMAT, upper);

        if (lower == null) {
            l = "-" + StringUtil.INF;
        }
        if (upper == null) {
            u = StringUtil.INF;
        }
        return asString(String.format("in (%s ; %s]", l, u));
    }

    private String asString(String range) {
        return String.format("%s %s", getAttributeName(), range);
    }

    @Override
    public String getAttributeName() {
        return attribute.getName();
    }
}
