package pl.swidurski.model;

/**
 * Created by Krystek on 2016-10-23.
 */
public class Entry {
    public int getCount() {
        return count;
    }

    public String getString() {
        return string;
    }


    public int count;
    String string;

    public Entry(String string, int count) {
        this.count = count;
        this.string = string;
    }

    public Entry(String string) {
        this(string, 1);
    }


    public void setCount(int count) {
        this.count = count;
    }

    public void setString(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return string + " " + count + " ";
    }
}