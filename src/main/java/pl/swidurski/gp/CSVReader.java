package pl.swidurski.gp;

import pl.swidurski.gp.operators.Variable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by student on 23.10.2016.
 */
public class CSVReader {

    public static final String SEPARATOR = ",";
    private final File file;

    public CSVReader(File file) {
        this.file = file;
    }


    public List<Variable> read() throws IOException {
        List<String> lines = Files.readAllLines(file.toPath());
        String[] header = split(lines, 0);

        List<Variable> variables = new ArrayList<>();
        List<List<Double>> ranges = new ArrayList<>();

        // Read header - create variables
        for (int i = 0; i < header.length; i++) {
            variables.add(new Variable(header[i]));
            ranges.add(new ArrayList<>());
        }

        // Read all values
        for (int i = 1; i < lines.size(); i++) {
            String[] line = split(lines, i);
            for (int j = 0; j < line.length; j++) {
                ranges.get(j).add(new Double(line[j]));
            }
        }

        for (int i = 0; i < variables.size(); i++) {
            variables.get(i).setRange(new DiscreteRange(ranges.get(i)));
        }
        return variables;
    }

    private String[] split(List<String> strings, int i) {
        return strings.get(i).split(SEPARATOR);
    }
}
