package pl.swidurski.model;

import lombok.Getter;

/**
 * Created by Krystek on 2016-11-06.
 */
public class Result {

    @Getter
    private String value;

    public Result(String result) {
        this.value = result;
    }

    @Override
    public String toString() {
        return String.format(" THEN %s", value);
    }
}
