package pl.swidurski.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Krystek on 2016-11-06.
 */
public class Rule {

    @Getter
    List<Condition> conditions = new ArrayList<>();

    @Getter
    @Setter
    Result result;

    public void append(Condition condition) {
        conditions.add(condition);
    }

    @Override
    public String toString() {
        if (conditions.isEmpty())
            return "empty conditions set";

        StringBuilder builder = new StringBuilder();
        builder.append("IF ");
        builder.append(conditions.get(0));
        for (int i = 1; i < conditions.size(); i++) {
            builder.append(" AND ");
            builder.append(conditions.get(i));
        }
        builder.append(result);
        return builder.toString();
    }
}
