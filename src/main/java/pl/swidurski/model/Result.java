package pl.swidurski.model;

/**
 * Created by Krystek on 2016-11-06.
 */
public class Result {

    private String result;

    public Result(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return String.format(" THEN %s", result);
    }
}
