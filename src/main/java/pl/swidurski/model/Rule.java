package pl.swidurski.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Krystek on 2016-11-06.
 */
public class Rule {

    @Getter
    List<Condition> conditions = new ArrayList<>();

    @Getter
    Result result;

    public void append(Condition condition){
        conditions.add(condition);
    }

}
